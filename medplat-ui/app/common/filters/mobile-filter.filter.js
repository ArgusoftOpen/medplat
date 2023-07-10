(function () {
    var techoFilters = angular.module("imtecho.filters");
    techoFilters.filter('mobileFilter', function () {
        return function (input) {
            if (!!input && input.length == 10) {
                return input.slice(0, 2) + "-" + input.slice(2, 6) + "-" + input.slice(6, 10);
            }
        };
    });
})();
