/**
 * Show tooltip only if value is enable otherwise not
 */
(function () {
    var techoFilters = angular.module("imtecho.filters");
    techoFilters.filter('showtooltip', function () {
        return function (text,value) {
            return isNaN(value) ? '' : text;
        };
    });
})();
