(function () {
    let generateRowTemplateDirective = function ($templateCache) {
        return {
            restrict: 'E',
            scope: {
                config: '<',
                elementIndex: '<',
                siblingElements: '<',
                elements: '<',
                iteratorIndicesMap: '<',
                constraintConfig: '<'
            },
            template: $templateCache.get('app/admin/applicationmanagement/managesystemconstraints/directives/generatedynamictemplate/row/generate-row-template.html'),
            link: function (scope) {
                scope.getDynamicStyles = function () {
                    return scope.config.cssStyles;
                }
            }
        };
    };
    angular.module('imtecho.directives').directive('generateRowTemplateDirective', generateRowTemplateDirective);
})();
