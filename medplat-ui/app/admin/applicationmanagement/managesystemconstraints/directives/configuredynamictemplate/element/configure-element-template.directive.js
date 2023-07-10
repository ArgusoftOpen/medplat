(function () {
    let configureElementTemplateDirective = function ($uibModal) {
        return {
            restrict: 'E',
            scope: {
                element: '<',
                elementIndex: '<',
                siblingElements: '<',
                cardClasses: '<',
                rowClasses: '<',
                colClasses: '<',
                formFieldList: '<',
                onChildWebComponentChanged: '&'
            },
            templateUrl: 'app/admin/applicationmanagement/managesystemconstraints/directives/configuredynamictemplate/element/configure-element-template.html',
            link: function (scope) {

                scope.onChildWebComponentChangedDir = function (childWebComponent, elements, element, parentComponentType) {
                    scope.onChildWebComponentChanged({ childWebComponent, elements, element, parentComponentType });
                }

                scope.manageFieldConfig = function (config) {
                    let modalInstance = $uibModal.open({
                        templateUrl: 'app/admin/applicationmanagement/managesystemconstraints/directives/configuredynamictemplate/element/configure-element-template-config.modal.html',
                        windowClass: 'cst-modal',
                        backdrop: 'static',
                        size: 'md',
                        controllerAs: 'ctrl',
                        resolve: {},
                        controller: function ($uibModalInstance) {
                            let manageFieldConfigCtrl = this;
                            manageFieldConfigCtrl.config = angular.copy(config);

                            manageFieldConfigCtrl.ok = function () {
                                manageFieldConfigCtrl.manageConfigForm.$setSubmitted();
                                if (manageFieldConfigCtrl.manageConfigForm.$valid) {
                                    Object.assign(config, manageFieldConfigCtrl.config);
                                    $uibModalInstance.close();
                                }
                            }

                            manageFieldConfigCtrl.cancel = function () {
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
    angular.module('imtecho.directives').directive('configureElementTemplateDirective', configureElementTemplateDirective);
})();
