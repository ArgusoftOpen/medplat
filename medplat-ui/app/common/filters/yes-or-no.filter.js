(function () {
    var agdFilters = angular.module("imtecho.filters");
    agdFilters.filter('yesOrNo', function () {
        return function (input, variableForTrue, variableForFalse) {
            if (variableForTrue || variableForFalse) {
                return input === true ? variableForTrue : variableForFalse
            } else {
                return input === undefined || input === null || input === '' ? 'N.A' : input === true ? 'Yes' : 'No';
            }
        };
    });
})();
