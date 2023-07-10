(function () {
    var techoFilters = angular.module("imtecho.filters");
    techoFilters.filter('emptylabel', function () {
        return function (text, placeholder) {
            if (!placeholder) {
                placeholder = 'N.A';
            }
            // Trim any whitespace and show placeholder if no content
            if ([undefined, null, '', 'null'].includes(text) || (typeof text === 'string' && text.trim() === '')) {
                return placeholder;
            }
            return text;
        };
    });
})();
