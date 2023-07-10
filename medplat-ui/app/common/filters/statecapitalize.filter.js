(function () {
    var techoFilters = angular.module("imtecho.filters");
    techoFilters.filter('statecapitalize', function () {
        return function (input) {
            return (!!input) ? input.charAt(0).toUpperCase() + input.substr(1).toLowerCase() : '';
        };
    });
})();
