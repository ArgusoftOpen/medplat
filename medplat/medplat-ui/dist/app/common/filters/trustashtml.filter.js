(function () {
    var techoFilters = angular.module("imtecho.filters");
    techoFilters.filter('trustAsHtml', function ($sce) {
        return function (html) {
            if (html || html == 0) {
                html = html.toString();
                return $sce.trustAsHtml(html);
            }
        };
    });
})();
