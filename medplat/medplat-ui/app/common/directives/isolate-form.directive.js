(function () {
    var isolateForm = function () {
        return {
            restrict: 'A',
            require: '?form',
            link: function (scope, element, attrs, formController) {
                if (!formController) {
                    return;
                }
                // Remove this form from parent controller
                var parentFormController = element.parent().controller('form');
                parentFormController.$removeControl(formController);
            }
        };
    };
    angular.module('imtecho.directives').directive('isolateForm', isolateForm);
})();
