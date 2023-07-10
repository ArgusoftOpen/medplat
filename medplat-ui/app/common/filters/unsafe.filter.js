(function () {
    var techoFilters = angular.module("imtecho.filters");
    techoFilters.filter('unsafe', function ($sce) {
        return function (val) {
            return $sce.trustAsHtml(val);
        };
    });
})();
