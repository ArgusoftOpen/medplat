(function () {
    var techoFilters = angular.module("imtecho.filters");
    techoFilters.filter('hospitaltypeDisplayName', function () {
        return function (input, array) {
            var type = _.find(array, function (typeObject) {
                return typeObject.id == input;
            });
            if (type) {
                return type.value;
            }
        };
    });
})();
