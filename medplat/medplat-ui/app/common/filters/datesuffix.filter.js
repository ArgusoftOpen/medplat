(function () {
    var techoFilters = angular.module("imtecho.filters");
    techoFilters.filter('dateSuffix', function ($filter) {
        var suffixes = ["th", "st", "nd", "rd"];
        return function (input) {
            if (input !== null && input !== "Invalid Date" && input !== "" && input) {
                var dtfilter = $filter('date')(input, 'd MMM yyyy');
                var day = parseInt(dtfilter.slice(0, 2));
                var relevantDigits = (day < 30) ? day % 20 : day % 30;
                var suffix = (relevantDigits <= 3) ? suffixes[relevantDigits] : suffixes[0];
                dtfilter = dtfilter.replace(' ', suffix + " ");
                return dtfilter;
            } else {
                return "";
            }
        };
    });
})();
