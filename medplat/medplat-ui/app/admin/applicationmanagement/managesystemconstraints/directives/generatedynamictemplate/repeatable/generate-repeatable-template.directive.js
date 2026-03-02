(function () {
    let generateRepeatableTemplateDirective = function (GeneralUtil,$templateCache) {
        return {
            restrict: 'E',
            scope: {
                element: '<',
                elementIndex: '<',
                siblingElements: '<',
                iteratorIndicesMap: '<',
                cardClasses: '<',
                rowClasses: '<',
                colClasses: '<',
                constraintConfig: '<'
            },
            require: '^ngController',
            template: $templateCache.get('app/admin/applicationmanagement/managesystemconstraints/directives/generatedynamictemplate/repeatable/generate-repeatable-template.html'),
            link: function (scope, elements, attributes, ctrl) {
                scope.ctrl = ctrl;
                scope.iterator = [];

                const _init = () => {
                    if (scope.element.config.isRepeatable) {
                        // scope.iterator = GeneralUtil.getPropByPath(scope.ctrl, scope.element.config.ngModel);
                        let ngModelArray = scope.element.config.ngModel.split("[$index].");
                        let ngModelElement = "";
                        ngModelArray.forEach((element, index) => {
                            ngModelElement = index === 0 ? element : ngModelElement.concat(`[$index].${element}`);
                            scope.iterator = index === 0 ? GeneralUtil.getPropByPath(scope.ctrl, element) : GeneralUtil.getPropByPath(scope.iterator, element);
                            if (scope.iteratorIndicesMap && scope.iteratorIndicesMap[ngModelElement] !== null && typeof scope.iteratorIndicesMap[ngModelElement] !== 'undefined') {
                                scope.iterator = scope.iterator[scope.iteratorIndicesMap[ngModelElement]];
                            }
                        });
                    }
                }

                _init();
            }
        };
    };
    angular.module('imtecho.directives').directive('generateRepeatableTemplateDirective', generateRepeatableTemplateDirective);
})();
