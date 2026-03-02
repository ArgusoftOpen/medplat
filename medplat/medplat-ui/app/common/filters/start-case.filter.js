(function () {
    let techoFilters = angular.module("imtecho.filters");
    techoFilters.filter('startCase', function () {
        return (input) => {
            if (input) {
                return input
                    // insert a space before all caps
                    .replace(/([A-Z])/g, ' $1')
                    // uppercase the first character
                    .replace(/^./, function (str) { return str.toUpperCase(); })
            } else {
                return 'N.A';
            }
        };
    });
})();
