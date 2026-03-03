(function () {
    let configureColTemplateDirective = function ($uibModal, GeneralUtil) {
        return {
            restrict: 'E',
            scope: {
                config: '<',
                elements: '<',
                siblingElements: '<',
                elementIndex: '<',
                formFieldList: '<',
                onChildWebComponentChanged: '&'
            },
            templateUrl: 'app/admin/applicationmanagement/managesystemconstraints/directives/configuredynamictemplate/col/configure-col-template.html',
            link: function (scope) {
                scope.childWebComponentList = [
                    { "type": "CARD", "name": "Add Card (1 * 12 Col)" },
                    { "type": "ROW", "size": "12", "name": "Add Row (1 * 12 Col)" },
                    { "type": "CUSTOM_HTML", "name": "Add Custom HTML" },
                    ...scope.formFieldList
                ]

                scope.onChildWebComponentChangedDir = function (childWebComponent, elements, element, parentComponentType) {
                    scope.onChildWebComponentChanged({ childWebComponent, elements, element, parentComponentType });
                }

                scope.moveUp = function () {
                    GeneralUtil.moveElementOfArrayByIndex(scope.siblingElements, scope.elementIndex, -1);
                }

                scope.moveDown = function () {
                    GeneralUtil.moveElementOfArrayByIndex(scope.siblingElements, scope.elementIndex, 1);
                }

                scope.onRemoveWebComponent = function (index) {
                    scope.elements.splice(index, 1);
                }

                scope.manageColConfig = function (config) {
                    let modalInstance = $uibModal.open({
                        templateUrl: 'app/admin/applicationmanagement/managesystemconstraints/directives/configuredynamictemplate/col/configure-col-template-config.modal.html',
                        windowClass: 'cst-modal',
                        backdrop: 'static',
                        size: 'md',
                        controllerAs: 'ctrl',
                        resolve: {},
                        controller: function ($uibModalInstance) {
                            let manageColConfigCtrl = this;
                            manageColConfigCtrl.config = angular.copy(config);

                            manageColConfigCtrl.ok = function () {
                                manageColConfigCtrl.manageConfigForm.$setSubmitted();
                                if (manageColConfigCtrl.manageConfigForm.$valid) {
                                    Object.assign(config, manageColConfigCtrl.config);
                                    $uibModalInstance.close();
                                }
                            }

                            manageColConfigCtrl.cancel = function () {
                                $uibModalInstance.dismiss();
                            }
                        }
                    });

                    modalInstance.result
                        .then(function () { }, function () { })
                }
            }
        };
    };
    angular.module('imtecho.directives').directive('configureColTemplateDirective', configureColTemplateDirective);
})();
