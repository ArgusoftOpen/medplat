(function (angular) {
    angular.module('imtecho.service').factory('Navigation', function ($state, $sessionStorage) {
        var Navigation = {};
        var historyStates = {};
        var allowNavigation = 0;
        var generateHistoryKey = function (stateName, params) {
            var key = stateName + '/';
            if (params) {
                key += angular.toJson(params);
            } else {
                key += "{}";
            }
            return key;
        };

        Navigation.init = function () {
            if ($sessionStorage.asldkfjlj) {
                historyStates = $sessionStorage.asldkfjlj;
            }
            var $stateGo = $state.go;
            $state.go = function (to, params, options) {
                allowNavigation = 1;
                $stateGo(to, params, options);
            };
            //Written for specific condition in web dashboard
            $state.goURL = function (to, params, options) {
                $stateGo(to, params, options);
            };
        };

        Navigation.clearHistory = function () {
            historyStates = {};
            $sessionStorage.asldkfjlj = historyStates;
        };

        Navigation.resetAllowNavigation = function () {
            allowNavigation = 0;
        };

        Navigation.canNavigate = function (state, params) {
            var result = false;
            if (allowNavigation > 0) {
                historyStates[generateHistoryKey(state, params)] = true;
                $sessionStorage.asldkfjlj = historyStates;
                result = true;
            } else if (historyStates[generateHistoryKey(state, params)] === true) {
                result = true;
            }
            return result;
        };

        Navigation.setHistoryState = function (state, params) {
            historyStates[generateHistoryKey(state, params)] = true;
            $sessionStorage.asldkfjlj = historyStates;
        };

        return Navigation;
    });
})(window.angular);
