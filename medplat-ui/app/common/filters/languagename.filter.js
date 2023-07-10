(function () {
    var techoFilters = angular.module("imtecho.filters");
    techoFilters.filter('languagename', function (USER) {
        return function (input) {
            input = input || '';
            var out = '';
            if (input === USER.prefferedLanguage.english) {
                out = "English";
            } else if (input === USER.prefferedLanguage.gujarati) {
                out = "Gujarati";
            }
            return out;
        };
    });
})();
