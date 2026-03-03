(function () {
    var techoFilters = angular.module("imtecho.filters");
    techoFilters.filter('locationName', function () {
        return function (input) {
            input = input || '';
            if (input === 'D') {
                return "District";
            } else if (input === 'S') {
                return "State";
            } else if (input === 'C') {
                return "Corporation";
            } else if (input === 'B') {
                return "Block";
            } else if (input === 'Z') {
                return "Zone";
            } else if (input === 'U') {
                return "UPHC";
            } else if (input === 'P') {
                return "PHC";
            } else if (input === 'SC') {
                return "Sub center";
            } else if (input === 'UA') {
                return "Urban area";
            } else if (input === 'V') {
                return "Village";
            } else {
                return input;
            }
        };
    });
})();
