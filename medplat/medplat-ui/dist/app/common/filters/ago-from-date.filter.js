(function () {
    var techoFilters = angular.module("imtecho.filters");
    techoFilters.filter('agoFromDate', function () {
        return function (date) {
            if (angular.isString(date)) {
                var parsedDate = new Date(date);
                var current = new Date();
                var diff = current.getTime() - parsedDate.getTime();
                var diffInDays = Math.round(diff / (1000 * 60 * 60 * 24));
                if (diffInDays > 0 && diffInDays < 7) {
                    return diffInDays + "day(s) ago";
                }
                else if (diffInDays < 31) {
                    var week = Math.floor(diffInDays / 7);
                    return week + "week(s) ago";
                }
                else {
                    var month = Math.floor(diffInDays / 30);
                    return month + "month(s) ago";
                }
            }
        };
    });
})();
