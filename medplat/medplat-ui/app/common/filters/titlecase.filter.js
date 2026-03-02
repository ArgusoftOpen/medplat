(function () {
    var agdFilters = angular.module("imtecho.filters");
    agdFilters.filter('titlecase', function () {
        return function (input) {
            var smallWords = /^(a|an|and|as|at|but|by|en|for|if|in|nor|of|on|or|per|the|to|vs?\.?|via)$/i;
            if (input != null) {
                input = input.toLowerCase();
                input = input.replace(/_/g, " ");
                return input.replace(/[A-Za-z0-9\u00C0-\u00FF_]+[^\s-]*/g, function (match, index, title) {
                    if (index > 0 && index + match.length !== title.length &&
                        match.search(smallWords) > -1 && title.charAt(index - 2) !== ":" &&
                        (title.charAt(index + match.length) !== '-' || title.charAt(index - 1) === '-') &&
                        title.charAt(index - 1).search(/[^\s-]/) < 0) {
                        return match.toLowerCase();
                    }
                    if (match.substr(1).search(/[A-Z]|\../) > -1) {
                        return match;
                    }
                    return match.charAt(0).toUpperCase() + match.substr(1);
                });
            } else {
                return 'NA';
            }
        };
    });
})();
