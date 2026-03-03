(function () {
    let generateCardTemplateDirective = function (GeneralUtil, UUIDgenerator, $compile, $timeout,$templateCache) {
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
            require: '^ngController',
            template: $templateCache.get('app/admin/applicationmanagement/managesystemconstraints/directives/generatedynamictemplate/card/generate-card-template.html'),
            link: function (scope, elements, attributes, ctrl) {
                scope.ctrl = ctrl;
                scope.currentTime = new Date().getTime();
                scope.uuid = UUIDgenerator.generateUUID();
                scope.iterator = [];

                const _init = () => {
                    if (scope.config.isRepeatable) {
                        // scope.iterator = GeneralUtil.getPropByPath(scope.ctrl, scope.config.ngModel);
                        let ngModelArray = scope.config.ngModel.split("[$index].");
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

                scope.getDynamicStyles = function () {
                    return scope.config.cssStyles;
                }

                scope.repeatElement = function () {
                    scope.iterator.push({});
                }

                scope.deleteRepeatedElement = function () {
                    if (scope.iteratorIndicesMap[scope.config.ngModel] > (scope.iterator.length - 1)) {
                        scope.iterator.splice(scope.iterator.length - 1, 1);
                        return;
                    }
                    scope.iterator.splice(scope.iteratorIndicesMap[scope.config.ngModel], 1);
                }

                _init();
            }
        };
    };
    angular.module('imtecho.directives').directive('generateCardTemplateDirective', generateCardTemplateDirective);
})();


angular.module('imtecho.directives').directive('dynamic', function ($compile) {
    return {
        restrict: 'A',
        replace: true,
        require: '^ngController',
        link: function (scope, ele, attrs, ctrl) {
            scope.$watch(attrs.dynamic, function (html) {
                scope.ctrl = ctrl;
                ele.html(html);
                $compile(ele.contents())(scope);
            });
        }
    };
});
