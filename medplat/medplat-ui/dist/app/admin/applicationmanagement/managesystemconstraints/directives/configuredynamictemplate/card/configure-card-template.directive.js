(function () {
    let configureCardTemplateDirective = function ($uibModal, GeneralUtil) {
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
            templateUrl: 'app/admin/applicationmanagement/managesystemconstraints/directives/configuredynamictemplate/card/configure-card-template.html',
            link: function (scope) {
                scope.childWebComponentList = [
                    { "type": "CARD", "name": "Add Card (1 * 12 Col)" },
                    { "type": "ROW", "size": "12", "name": "Add Row (1 * 12 Col)" }
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

                scope.onRemoveWebComponent = function () {
                    scope.siblingElements.splice(scope.elementIndex, 1);
                }

                scope.manageCardConfig = function (config) {
                    let modalInstance = $uibModal.open({
                        templateUrl: 'app/admin/applicationmanagement/managesystemconstraints/directives/configuredynamictemplate/card/configure-card-template-config.modal.html',
                        windowClass: 'cst-modal',
                        backdrop: 'static',
                        size: 'md',
                        controllerAs: 'ctrl',
                        resolve: {},
                        controller: function ($uibModalInstance) {
                            let manageCardConfigCtrl = this;
                            manageCardConfigCtrl.config = angular.copy(config);

                            manageCardConfigCtrl.ok = function () {
                                manageCardConfigCtrl.manageConfigForm.$setSubmitted();
                                if (manageCardConfigCtrl.manageConfigForm.$valid) {
                                    Object.assign(config, manageCardConfigCtrl.config);
                                    $uibModalInstance.close();
                                }
                            }

                            manageCardConfigCtrl.cancel = function () {
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
    angular.module('imtecho.directives').directive('configureCardTemplateDirective', configureCardTemplateDirective);
})();
