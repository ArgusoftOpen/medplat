(function () {
    let techoFilters = angular.module("imtecho.filters");
    techoFilters.filter('gender', function () {
        return (input) => {
            if (input) {
                switch (input) {
                    case 'M':
                        return 'Male';
                    case 'F':
                    case 'FM':
                        return 'Female';
                    case 'T':
                        return 'Transgender';
                    default:
                        return 'N.A'
                }
            } else {
                return 'N.A';
            }
        };
    });
})();
