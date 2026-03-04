(function (angular) {
    angular.module('imtecho.service').provider('AuthenticateService', function () {
        var clientId, clientSecret;
        var AuthenticateService = function ($rootScope, $http, $location, $timeout, APP_CONFIG, $q, $state, $sessionStorage, AESEncryptionService) {
            if (!clientId) {
                throw "Client Id is not configured kindly set it in configuration phase useing AuthenticateServiceProvider";
            }
            if (!clientSecret) {
                throw "Client Secret is not configured kindly set it in configuration phase useing AuthenticateServiceProvider";
            }
            var service = {};
            var token = {};
            var refreshAccessTokenDefer;
            var refreshAccessTokenTimeoutPromise;
            var encodedClienSecret = btoa(clientId + ":" + clientSecret);
            var loggedInUser, linearMenuItems = {};
            var loggedInUserDefer, menuDefer = $q.defer();
            var simplifyMenu = function (menuItem) {
                if (menuItem && angular.isArray(menuItem)) {
                    angular.forEach(menuItem, function (entity) {
                        simplifyMenu(entity);
                    });
                } else {
                    if (menuItem.navigationState) {
                        linearMenuItems[menuItem.navigationState] = menuItem;
                    }
                    if (menuItem.featureJson) {
                        try {
                            menuItem.featureJson = menuItem.featureJson ? angular.fromJson(menuItem.featureJson) : {};
                        } catch (error) {
                            console.error('Error while parsing JSON featureJson ::: ', menuItem.featureJson, '\n For navigationState ::: ', menuItem.navigationState);
                            menuItem.featureJson = {};
                        }
                    }
                    if (menuItem.subGroups && angular.isArray(menuItem.subGroups)) {
                        angular.forEach(menuItem.subGroups, function (entity) {
                            entity._parent = menuItem;
                            simplifyMenu(entity);
                        });
                    }
                }
            };
            var setToken = function (data) {
                localStorage.setItem('token', JSON.stringify(data));
                token = data;
                $http.defaults.headers.common.Authorization = 'Bearer ' + data.access_token;
                $rootScope.authToken = data.access_token;
            };
            var clearRefreshTokenTimeout = function () {
                if (angular.isDefined(refreshAccessTokenTimeoutPromise)) {
                    $timeout.cancel(refreshAccessTokenTimeoutPromise);
                    refreshAccessTokenTimeoutPromise = undefined;
                }
            };

            service.getLoggedInUser = function (forceReload) {
                var localDefer = $q.defer();
                if (loggedInUser && !forceReload) {
                    localDefer.resolve(loggedInUser);
                    return localDefer.promise;
                } else if (!loggedInUserDefer || forceReload) {
                    loggedInUserDefer = $q.defer();
                    var reqForUser = {
                        method: 'GET',
                        url: APP_CONFIG.apiPath + "/login/principle",
                    };
                    $http(reqForUser).then(function (data) {
                        if (data.data.roleId) {
                            $rootScope.loggedInUserId = data.data.id;
                            $http.get(APP_CONFIG.apiPath + "/login/menu").then(function (features) {
                                data.data.features = (features.data);
                                service.setMenuItems(features.data);
                                loggedInUserDefer.resolve(data);
                                loggedInUser = data;
                                loggedInUserDefer = undefined;
                            }, function (err) {
                                data.roles = [];
                                loggedInUserDefer.resolve(data);
                                loggedInUser = data;
                                loggedInUserDefer = undefined;
                            });
                        } else {
                            data.roles = [];
                            loggedInUserDefer.resolve(data);
                            loggedInUser = data;
                            loggedInUserDefer = undefined;
                        }
                    }, function (error) {
                        loggedInUserDefer.reject(error);
                        loggedInUserDefer = undefined;
                    });
                    return loggedInUserDefer.promise;
                } else {
                    return loggedInUserDefer.promise;
                }
            };
            service.login = function (userName, password, onLoginConfirmed, onLoginFailure, loginAs) {
                if (!menuDefer) {
                    menuDefer = $q.defer();
                }
                // var data = $.param({
                //     grant_type: "password",
                //     username: userName,
                //     password: password,
                //     loginas: loginAs,
                //     client_id: clientId
                // });
                // var config = {
                //     headers: {
                //         "Authorization": "Basic " + encodedClienSecret,
                //         'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
                //     }
                // };
                $http({
                        method: 'POST',
                        url: APP_CONFIG.serverPath + "/oauth/token?grant_type=password&username="+encodeURIComponent(AESEncryptionService.encrypt(userName))+"&password="+encodeURIComponent(AESEncryptionService.encrypt(password))+"&loginas=&client_id="+clientId,
                        headers: {
                            "Authorization": "Basic " + encodedClienSecret,
                            'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
                        }}).then(function (response) {
                    setToken(response.data);
                    $rootScope.isLoggedIn = true;
                    refreshAccessTokenTimeoutPromise = $timeout(function () {
                        clearRefreshTokenTimeout();
                        service.refreshAccessToken();
                    }, ((response.data.expires_in - 1) * 1000));
                    if (!!onLoginConfirmed) {
                        onLoginConfirmed();
                    }
                }, function (error) {
                    clearRefreshTokenTimeout();
                    if (!!onLoginFailure) {
                        error.data.message = error.data.error_description;
                        onLoginFailure(error);
                    }
                });
            };

            service.getKeyAndIV = () => {
                return $http.get(APP_CONFIG.apiPath + "/login/get-key-and-iv").then(response => {
                    $rootScope.loginEncryptionKey = response.data.key;
                    $rootScope.loginInitVector = response.data.initVector;
                    return Promise.resolve();
                }).catch(error => {
                    return Promise.reject(error);
                })
            }

            service.getSystemNotice = () => {
                return $http.get(APP_CONFIG.apiPath + "/mobile/systemNotice");
            }

            service.getApkInfo = () => {
                return $http.get(APP_CONFIG.apiPath + "/mobile/apkInfo");
            }

            service.setMenuItems = function (menuItems) {
                linearMenuItems = {};
                delete menuItems.$promise;
                delete menuItems.$resolved;
                angular.forEach(menuItems, function (menuItem) {
                    simplifyMenu(menuItem);
                });
                menuDefer.resolve(menuItems);
            };
            service.getMenuItems = function () {
                return menuDefer.promise;
            };
            service.getAssignedFeature = function (state) {
                var assignedFeatureDeffer = $q.defer();
                menuDefer.promise.then(function () {
                    if (linearMenuItems[state] != null && !linearMenuItems[state].systemConstraintConfigs) {
                        let promises = [];
                        promises.push($http.get(`${APP_CONFIG.apiPath}/systemConstraint/configs/${linearMenuItems[state].id}`));
                        promises.push($http.get(`${APP_CONFIG.apiPath}/systemConstraint/webTemplateConfigs/${linearMenuItems[state].id}`));
                        $q.all(promises).then(responses => {
                            linearMenuItems[state].systemConstraintConfigs = responses[0].data;
                            linearMenuItems[state].webTemplateConfigs = responses[1].data;
                        }, console.error).finally(function () {
                            assignedFeatureDeffer.resolve(linearMenuItems[state]);
                        });
                    } else {
                        assignedFeatureDeffer.resolve(linearMenuItems[state]);
                    }
                });
                return assignedFeatureDeffer.promise;
            };
            service.refreshAccessToken = function () {
                if (!refreshAccessTokenDefer) {
                    refreshAccessTokenDefer = $q.defer();
                    // var data = $.param({
                    //     grant_type: "refresh_token",
                    //     refresh_token: token.refresh_token
                    // });
                    // var config = {
                    //     headers: {
                    //         "Authorization": "Basic " + encodedClienSecret,
                    //         'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
                    //     }
                    // };
                    $http({
                        method: 'POST',
                        url: APP_CONFIG.serverPath + "/oauth/token?grant_type=refresh_token&refresh_token="+token.refresh_token,
                        headers: {
                            "Authorization": "Basic " + encodedClienSecret,
                            'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
                        }}).then(function (response) {
                        setToken(response.data);
                        refreshAccessTokenDefer.resolve(response.data);
                        refreshAccessTokenDefer = undefined;
                        refreshAccessTokenTimeoutPromise = $timeout(function () {
                            clearRefreshTokenTimeout();
                            service.refreshAccessToken();
                        }, ((response.data.expires_in - 1) * 1000));
                    }, (function (error) {
                        refreshAccessTokenDefer.reject(error);
                        refreshAccessTokenDefer = undefined;
                        clearRefreshTokenTimeout();
                    }));
                    return refreshAccessTokenDefer.promise;
                } else {
                    return refreshAccessTokenDefer.promise;
                }
            };
            service.init = function () {
                var tokenCookie = localStorage.getItem('token');
                if (tokenCookie) {
                    console.log("FOUND TOKEN");
                    setToken(JSON.parse(tokenCookie));
                    $rootScope.isLoggedIn = true;
                    token = JSON.parse(tokenCookie);
                } else {
                    console.log("TOKEN NOT FOUND");
                    if ($location.path() !== "/" && $location.path() !== "") {
                        $state.go("login");
                    }
                }
            };
            service.getToken = function () {
                return token.access_token;
            };
            service.logOut = function () {
                if (loggedInUser && loggedInUser.data && loggedInUser.data.userName) {
                    var reqForLogoutUser = {
                        method: 'GET',
                        url: APP_CONFIG.apiPath + "/user/logout",
                        params: {
                            userName: loggedInUser.data.userName,
                            clientId: "imtecho-ui"
                        }
                    };
                    return $http(reqForLogoutUser).then(function (data) {
                        localStorage.removeItem('token');
                        token = {};
                        loggedInUser = undefined;
                        menuDefer = undefined;
                        delete $http.defaults.headers.common.Authorization;
                        delete $rootScope.authToken;
                        $rootScope.isLoggedIn = false;
                        delete $sessionStorage.asldkfjlj;
                        console.log($sessionStorage.asldkfjlj)
                    }, function (error) {
                        loggedInUser = undefined;
                        return Promise.resolve();
                    });
                } else {
                    localStorage.removeItem('token');
                    token = {};
                    loggedInUser = undefined;
                    menuDefer = undefined;
                    delete $http.defaults.headers.common.Authorization;
                    delete $rootScope.authToken;
                    $rootScope.isLoggedIn = false;
                    delete $sessionStorage.asldkfjlj;
                    console.log($sessionStorage.asldkfjlj);
                    return Promise.resolve();
                }
            };
            return service;
        };
        this.setClientDetails = function (clientIdP, clientSecretP) {
            clientId = clientIdP;
            clientSecret = clientSecretP;
        };
        this.$get = AuthenticateService;
    });
})(window.angular);
