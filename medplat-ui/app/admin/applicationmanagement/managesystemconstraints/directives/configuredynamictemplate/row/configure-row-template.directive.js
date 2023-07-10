(function () {
    let configureRowTemplateDirective = function ($uibModal, GeneralUtil) {
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
            templateUrl: 'app/admin/applicationmanagement/managesystemconstraints/directives/configuredynamictemplate/row/configure-row-template.html',
            link: function (scope) {
                scope.childWebComponentList = [
                    { "type": "CARD", "name": "Add Card (1 * 12 Col)" },
                    { "type": "ROW", "size": "12", "name": "Add Row (1 * 12 Col)" },
                    { "type": "COL", "size": "12", "name": "Add Column (1 * 12 Col)" },
                    { "type": "COL", "size": "6", "name": "Add Column (2 * 6 Col)" },
                    { "type": "COL", "size": "4", "name": "Add Column (3 * 4 Col)" },
                    { "type": "COL", "size": "3", "name": "Add Column (4 * 3 Col)" }
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

                scope.manageRowConfig = function (config) {
                    let modalInstance = $uibModal.open({
                        templateUrl: 'app/admin/applicationmanagement/managesystemconstraints/directives/configuredynamictemplate/col/configure-col-template-config.modal.html',
                        windowClass: 'cst-modal',
                        backdrop: 'static',
                        size: 'md',
                        controllerAs: 'ctrl',
                        resolve: {},
                        controller: function ($uibModalInstance) {
                            let manageRowConfigCtrl = this;
                            manageRowConfigCtrl.config = angular.copy(config);

                            manageRowConfigCtrl.ok = function () {
                                manageRowConfigCtrl.manageConfigForm.$setSubmitted();
                                if (manageRowConfigCtrl.manageConfigForm.$valid) {
                                    Object.assign(config, manageRowConfigCtrl.config);
                                    $uibModalInstance.close();
                                }
                            }

                            manageRowConfigCtrl.cancel = function () {
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
    angular.module('imtecho.directives').directive('configureRowTemplateDirective', configureRowTemplateDirective);
})();
