(function () {
    let generateElementTemplateDirective = function ($templateCache) {
        return {
            restrict: 'E',
            scope: {
                element: '<',
                elementIndex: '<',
                siblingElements: '<',
                iteratorIndex: '<',
                iteratorIndicesMap: '<',
                cardClasses: '<',
                rowClasses: '<',
                colClasses: '<',
                constraintConfig: '<'
            },
            template: $templateCache.get('app/admin/applicationmanagement/managesystemconstraints/directives/generatedynamictemplate/element/generate-element-template.html'),
            link: function (scope) {

                const _init = function () {
                    if (!scope.iteratorIndicesMap) scope.iteratorIndicesMap = {};
                    if (scope.element.config.isRepeatable) {
                        if (scope.iteratorIndicesMap[scope.element.config.ngModel] !== null && scope.iteratorIndicesMap[scope.element.config.ngModel] !== undefined) {
                            scope.iteratorIndicesMap = angular.copy(scope.iteratorIndicesMap);
                        }
                        scope.iteratorIndicesMap[scope.element.config.ngModel] = scope.iteratorIndex;
                    }
                }

                _init();
            }
        };
    };
    angular.module('imtecho.directives').directive('generateElementTemplateDirective', generateElementTemplateDirective);
})();
