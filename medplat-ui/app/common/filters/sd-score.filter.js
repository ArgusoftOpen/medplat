(function () {
    let techoFilters = angular.module("imtecho.filters");
    techoFilters.filter('sdscore', function () {
        return (input) => {
            if (input) {
                switch (input) {
                    case 'SD4':
                        return 'Less than -4';
                    case 'SD3':
                        return '-4 to -3';
                    case 'SD2':
                        return '-3 to -2';
                    case 'SD1':
                        return '-2 to -1';
                    case 'MEDIAN':
                        return 'MEDIAN';
                    default:
                        return 'NONE'
                }
            } else {
                return 'N.A';
            }
        };
    });
})();
