(function (angular) {
    angular.module('imtecho.interceptor').factory('HttpRequestResponseInterceptor', function ($rootScope, $q, $injector) {
        var cleanIt = function (entity, url) {
            if (angular.isArray(entity)) {
                angular.forEach(entity, function (e) {
                    cleanIt(e, url);
                });
            } else if (angular.isObject(entity)) {
                angular.forEach(entity, function (value, key) {
                    if (cleanIt.exceptionFieldListForClean.indexOf(key) === -1) {
                        if (angular.isString(value) && (value === 'null' || value === '')) {
                            delete entity[key];
                        } else if (angular.isString(value) && !isNaN(value)) {
                            console.warn("Key(" + key + ") contains Number value(" + value + ") in String representation from " + url + ".");
                        } else {
                            cleanIt(value, url);
                        }
                    }
                });
            }
            return entity;
        };
        cleanIt.exceptionFieldListForClean = ['fpRefId'];
        return {
            request: function (config) {
                var $http = $injector.get('$http');
                config.headers = config.headers || {};
                //return for template requests
                if (config.url.indexOf("/api/") < 0 && config.url.indexOf("/oauth/") < 0) {
                    delete config.headers.Authorization;
                }
                if (config.url.indexOf("views/") >= 0) {
                    if (!config.params) {
                        config.params = {};
                    }
                    config.params.version = 'v$VERSION$';
                }

                config.withCredentials = true;
                $http.defaults.useXDomain = true;
                delete $http.defaults.headers.common['X-Requested-With'];

                return config;
            },
            requestError: function (rejection) {
                // Return the promise rejection.
                return $q.reject(rejection);
            },
            response: function (response, headers) {
                if (response.headers().totalrecords != null) {
                    $rootScope.totalrecords = response.headers().totalrecords;
                }
                return response;
            },
            responseError: function (response) {
                var AuthenticateService = $injector.get('AuthenticateService');
                var $http = $injector.get('$http');
                var status = response.status ? response.status : undefined;
                if (status === 401) {
                    if (response.data.error === "invalid_token") {
                        return AuthenticateService.refreshAccessToken().then(function (refreshTokenResponse) {
                            response.config.headers.Authorization = "Bearer " + refreshTokenResponse.access_token;
                            return $http(response.config);
                        }, function (error) {
                            if (error.error === "invalid_grant" &&
                                (error.error_description.indexOf("Invalid refresh token") >= 0)) {
                                console.log('$broadcast : invalid_grant');
                                $rootScope.$broadcast('invalid_grant');
                            }
                            return $q.reject(response);
                        });
                    } else {
                        console.log('$broadcast : invalid_auth');
                        $rootScope.$broadcast('invalid_auth');
                        return $q.reject(response);
                    }
                } else if (status === 400) {
                    if (response.data.error === "invalid_grant" &&
                        (response.data.error_description.indexOf("Invalid refresh token") >= 0)) {
                        console.log('$broadcast : invalid_grant');
                        $rootScope.$broadcast('invalid_grant');
                    }
                    return $q.reject(response);
                }
                else if (status === -1) {
                    response.data = {
                        error: 'server_unreachable',
                        error_description: 'Server unreachable, please try after some time.',
                        message: 'Server unreachable, please try after some time.'
                    };
                    return $q.reject(response);
                } else {
                    if (!status) {
                        response = {
                            data: {
                                error: 'reason_unknown',
                                error_description: 'Something went wrong, please try after some time or refresh.',
                                message: 'Something went wrong, please try after some time or refresh.'
                            }
                        };
                    }
                    return $q.reject(response);
                }
            }
        };
    });

    angular.module('imtecho.interceptor').config(function ($httpProvider) {
        $httpProvider.interceptors.push('HttpRequestResponseInterceptor');
    });
})(window.angular);
