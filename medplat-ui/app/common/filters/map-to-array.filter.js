(function (angular) {
    var imtechoFilters = angular.module("imtecho.filters");
    imtechoFilters.filter('mapToArray', function () {
        return function (map, config) {
            var array = [];
            config = config || {};
            angular.forEach(map, function (value, key) {
                var object = {};
                object[config.keyField || 'key'] = key;
                object.value = value;
                array.push(object);
            });
            return array;
        };
    });
})(window.angular);
