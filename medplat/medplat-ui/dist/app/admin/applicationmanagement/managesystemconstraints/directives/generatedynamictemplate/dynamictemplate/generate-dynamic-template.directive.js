(function () {
    let generateDynamicTemplateDirective = function () {
        return {
            restrict: 'E',
            scope: {
                constraintConfig: '<',
                templateConfig: '<',
                form: '=?',
                submitBtnTxt: '=?',
                backButtonReq: '=?',
                onAllComponentsLoaded: '&?',
                submitFn: '&',
                goBackFn: '&'
            },
            templateUrl: 'app/admin/applicationmanagement/managesystemconstraints/directives/generatedynamictemplate/dynamictemplate/generate-dynamic-template.html',
            link: function (scope, elements, attrs) {

                const _init = () => {
                    if (!scope.submitBtnTxt) {
                        scope.submitBtnTxt = 'Save';
                    }
                    if (scope.backButtonReq === null || scope.backButtonReq === undefined) {
                        scope.backButtonReq = true
                    }
                }

                scope.submit = function () {
                    scope.submitFn({});
                }

                scope.goBack = function () {
                    scope.goBackFn({});
                }

                scope.$on('onAllComponentsLoaded', function () {
                    if (scope.onAllComponentsLoaded) {
                        scope.onAllComponentsLoaded({});
                    }
                });

                _init();
            }
        };
    };
    angular.module('imtecho.directives').directive('generateDynamicTemplateDirective', generateDynamicTemplateDirective);
})();
