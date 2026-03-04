(function () {
    var techoFilters = angular.module("imtecho.filters");
    techoFilters.filter('reverseIterate', function () {
        return function (items) {
            return items && angular.isArray(items) && items.slice().reverse();
        };
    });
})();
