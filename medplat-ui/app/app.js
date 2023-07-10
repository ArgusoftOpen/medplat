(function (angular) {
    //Define the main module.'
    var as = angular.module("imtecho", [
        "ui.router",
        "imtecho.interceptor",
        "imtecho.controllers",
        "imtecho.directives",
        "imtecho.service",
        "imtecho.filters",
        "imtecho.constant",
        "oc.lazyLoad",
        'ngResource',
        'ngMessages',
        'ngCookies',
        "toaster",
        'chart.js',
        // "720kb.datepicker",
        "ordinal",
        'ngStorage',
        'ui.mask',
        'daterangepicker',
        'textAngular',
        'config'
    ]);
    as.config(function (AuthenticateServiceProvider, MaskProvider) {
        MaskProvider.setTemplate('<i class ="fa fa-cog fa-spin fa-2x"></i>');
        AuthenticateServiceProvider.setClientDetails('imtecho-ui', 'imtecho-ui-secret');
    });
    as.run(function ($rootScope, AuthenticateService, $state, toaster, Mask, Navigation, $http, APP_CONFIG, UUIDgenerator, $stateParams) {
        $rootScope.isLoggedIn = false;
        $rootScope.isLocked = false;
        var current_page_Id;
        var prev_temp_id;
        var timeStart;
        var curr_page_title;
        var data_to_be_stored = {};
        var isActiveTab = true;
        var tab_activation_time;
        var active_tab_time = new Date().getTime();
        var userIdFromCache = null;

        AuthenticateService.init();
        Navigation.init();
        $rootScope.logOut = function () {
            Mask.hide();
            Mask.show();
            data_to_be_stored = {
                'prevStateId': prev_temp_id,
                'currStateId': current_page_Id,
                'nextStateId': null,
                'userId': $rootScope.loggedInUserId || -1,
                'activeTabTime': (tab_activation_time || 0) + (new Date().getTime() - active_tab_time),
                'totalTime': new Date().getTime() - timeStart,
                'pageTitle': curr_page_title,
                'browserCloseDet': true
            };
            if (data_to_be_stored.prevStateId !== null && data_to_be_stored.currStateId !== null && data_to_be_stored.pageTitle != null)
            {
                $http.post(APP_CONFIG.apiPath + '/insert_user_analytics_details', data_to_be_stored).then((response) => {
                    //console.log(response);
                }).catch((error) => {
                });
            }
            
            AuthenticateService.logOut().then(function (data) {
                Mask.hide();
                toaster.clear("*");
                $state.go("login");
            });
        };

        window.addEventListener('focus', function () {
            // document.title = 'focused';
            if (!isActiveTab) {
                active_tab_time = new Date().getTime();
                isActiveTab = true;
            }
        });

        window.addEventListener('blur', function () {
            isActiveTab = false;
            tab_activation_time = (tab_activation_time || 0) + new Date().getTime() - active_tab_time;
        });

        // when browser gets terminated
        window.onbeforeunload = function (e) {
            window.onunload = function () {
                data_to_be_stored = {
                    'prevStateId': prev_temp_id,
                    'currStateId': current_page_Id,
                    'nextStateId': null,
                    'userId': $rootScope.loggedInUserId || -1,
                    'activeTabTime': (tab_activation_time || 0) + (new Date().getTime() - active_tab_time),
                    'totalTime': new Date().getTime() - timeStart,
                    'pageTitle': curr_page_title,
                    'browserCloseDet': true
                };
                if ((data_to_be_stored.activeTabTime !== null || data_to_be_stored.totalTime !== null || data_to_be_stored.currStateId != null) && data_to_be_stored.pageTitle !== "login") {
                    window.localStorage.setItem('pendingActiveTabTime', JSON.stringify(data_to_be_stored));
                    $http.post(APP_CONFIG.apiPath + '/insert_user_analytics_details', data_to_be_stored).then((response) => {
                    }).catch((error) => {
                    });

                }
            }
            //return undefined;
        };

        // LocalServer.startListioning(7710);
        $rootScope.$on('$stateChangeStart', async function (event, toState, toParams, fromState, fromParams) {
            Mask.hide();
            var pendingData = window.localStorage.getItem('pendingActiveTabTime');
            if (pendingData && $rootScope.isLoggedIn && pendingData && JSON.parse(pendingData).activeTabTime !== null && JSON.parse(pendingData).totalTime !== null && pendingData.pageTitle !== "login") {
                try {
                    userIdFromCache = JSON.parse(pendingData).userId;
                    await $http.post(APP_CONFIG.apiPath + '/insert_user_analytics_details', JSON.parse(pendingData));
                    window.localStorage.removeItem('pendingActiveTabTime');
                } catch (err) { }
            }

            $http.defaults.headers.common.title = toState.title;
            if (toState.name === 'login' || toState.name === 'resetpassword' || toState.name === 'logindownload' || toState.name === 'info' || toState.name === 'healthidcardotp') {
                if (localStorage.getItem('token') && toState.name === 'login') {
                    $rootScope.isLoggedIn = true;
                    event.preventDefault();
                    if (!fromState.name) {
                        $state.go('techo.dashboard.webtasks');
                    }
                }
            } else if (!$rootScope.isLoggedIn) {
                event.preventDefault();
                $state.go("login");
            } else if (localStorage.getItem('isLocked')) {
                if (toState.name !== 'lockscreen') {
                    event.preventDefault();
                    $state.go("lockscreen");
                }
            } else if ($rootScope.forcePasswordReset) {
                if (toState.name !== 'resetpassword') {
                    event.preventDefault();
                    $state.go('resetpassword');
                }
            } else if (!Navigation.canNavigate(toState.name, toParams)) {
                if (sessionStorage.getItem('linkClick') === 'true') {
                    sessionStorage.setItem('linkClick', '');
                    Navigation.setHistoryState(toState.name, toParams);
                } else if (toState.name !== 'techo.home' || (fromState.name === 'techo.home' && toState.name !== 'techo.home')) {
                    event.preventDefault();
                    $state.go("techo.dashboard.webtasks");
                }
            } else {
                Mask.show();
            }
            Navigation.resetAllowNavigation();
        });

        $rootScope.$on('$stateChangeSuccess', async function (event, to, from, fromParams) {
            current_page_Id = UUIDgenerator.generateUUID();
            if (!$rootScope.loggedInUserId) {
                await AuthenticateService.getLoggedInUser().then(user => {
                    userIdFromCache = user.data.id;
                }
                )
            }
            function insertToDb(prevId, currId, nextId, stateTime) {
                data_to_be_stored = {
                    'prevStateId': prevId,
                    'currStateId': currId,
                    'nextStateId': nextId,
                    'userId': $rootScope.loggedInUserId ? $rootScope.loggedInUserId : (userIdFromCache !== null ? userIdFromCache : -1),
                    'activeTabTime': stateTime === 0 ? 0 : (tab_activation_time || 0) + (new Date().getTime() - active_tab_time),
                    'totalTime': stateTime,
                    'pageTitle': $state.current.name === "techo.report.view" ? ($state.current.name + "({" + $state.params.id + "})") : $state.current.name,
                    'browserCloseDet': false
                }
                if ((data_to_be_stored.activeTabTime !== null || data_to_be_stored.totalTime !== null || data_to_be_stored.currStateId != null) && data_to_be_stored.pageTitle !== "login") {
                    $http.post(APP_CONFIG.apiPath + '/insert_user_analytics_details', data_to_be_stored).then((response) => {
                        active_tab_time = new Date().getTime();
                        timeStart = new Date().getTime();
                        tab_activation_time = null;
                        isActiveTab = true;
                        curr_page_title = $state.current.name === "techo.report.view" ? ($state.current.name + "({" + $state.params.id + "})") : $state.current.name
                    }).catch((error) => {
                    });
                }
            }

            document.body.scrollTop = document.documentElement.scrollTop = 0;
            $rootScope.previousState = from;
            $rootScope.previousStateParams = fromParams;
            $rootScope.currentState = to;
            $http.defaults.headers.common.title = to.title;
            $http.defaults.headers.common.pageId = current_page_Id;

            if (angular.isUndefined(timeStart)) {
                timeStart = new Date().getTime();
                insertToDb(null, current_page_Id, null, 0);
                prev_temp_id = current_page_Id;
                curr_page_title = $state.current.name === "techo.report.view" ? ($state.current.name + "({" + $state.params.id + "})") : $state.current.name
            } else {
                var timeSpent = new Date().getTime() - timeStart;
                if ($rootScope.isLoggedIn) {
                    insertToDb(prev_temp_id, current_page_Id, null, timeSpent);
                    prev_temp_id = current_page_Id;
                }
            }

            /* if (!$rootScope.breadcrumb) {
                $rootScope.breadcrumb = [];
            }
            if ($rootScope.breadcrumb) {
                var index = _.findIndex($rootScope.breadcrumb, function (stateObject) {
                    return angular.equals(stateObject.state, to) && angular.equals(stateObject.params, toParams);
                });
                if (index > 0) {
                    $rootScope.breadcrumb.length = index;
                }
                if (index === 0) {
                    $rootScope.breadcrumb = [];
                }
            }
            $rootScope.breadcrumb.push({
                state: to, params: toParams
            }); */

            //to avoid deleting in reload mode
            if (to.name.indexOf('login') >= 0) {
                //Hide mask explicitly if user is navigated to login page
                Mask.hide(null, true);
            } else {
                Mask.hide();
            }
            if (to.name.indexOf('techo.home') >= 0) {
                Navigation.clearHistory();
            }
        });
        $rootScope.$on('$stateChangeError', function (event, unfoundState, fromState, fromParams) {
            Mask.hide();
        });
        $rootScope.$on('$stateNotFound', function (event, unfoundState, fromState, fromParams) {
            Mask.hide();
        });
    });
})(window.angular);
