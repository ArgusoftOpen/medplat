/**
 * If input value is number then only number filter apply otherwise returns values as it is.
 */
(function () {
    var techoFilters = angular.module("imtecho.filters");
    techoFilters.filter('numberFormat', function ($filter) {
        return function (text) {
            return isNaN(text) ? text : $filter('number')(text)
        };
    });
})();
