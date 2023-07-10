(function (angular) {
    angular.module('imtecho.directives')
        .directive('validate', function ($templateCache, $timeout, $compile) {
            return {
                restrict: 'EA',
                require: '^form',
                transclude: true,
                controllerAs: 'validateCntrl',
                replace: true,
                template: $templateCache.get('app/common/directives/formvalidation/tmpl.html'),
                scope: {
                    getHasErrorFun: '&hasError'
                },
                controller: function ($scope, $element, $attrs) {
                    var parentForm;
                    var getParentForm = function (formCntrl) {
                        if (!parentForm) {
                            var parentFromCntrl = formCntrl;
                            var previousFormCntrl = formCntrl;
                            while (!parentForm) {
                                if (parentFromCntrl.$$parentForm) {
                                    previousFormCntrl = parentFromCntrl;
                                    parentFromCntrl = parentFromCntrl.$$parentForm;
                                } else {
                                    parentForm = previousFormCntrl;
                                }
                            }
                        }
                        return parentForm;
                    };
                    this.getMessage = function (error) {
                        var jqFormControl = $($element).find('[form-control]');
                        var msgName = error + 'Msg';
                        var errorMsg;
                        switch (error) {
                            case 'required':
                                errorMsg = 'Required';
                                break;
                            case 'min':
                                errorMsg = 'Minimum allowed value is ' + jqFormControl.attr('min');
                                break;
                            case 'max':
                                errorMsg = 'Maximum allowed value is ' + jqFormControl.attr('max');
                                break;
                            case 'minlength':
                                errorMsg = `Please enter atleast ${jqFormControl.attr('minlength')} characters`;
                                break;
                            case 'maxlength':
                                errorMsg = `Maximum ${jqFormControl.attr('maxlength')} characters allowed`;
                                break;
                            case 'number':
                                errorMsg = 'Please enter a number';
                                break;
                            case 'email':
                                errorMsg = 'Please enter a valid email';
                                break;
                            case 'pattern':
                                errorMsg = 'Characters <, >, ?, ^, $, |, \\ are not allowed';
                                break;
                        }
                        return ($attrs[msgName] || errorMsg || error);
                    };
                    this.getForAttribute = function () {
                        return $($element).find('[form-control]').attr('name');
                    };
                    if (angular.isFunction($scope.getHasErrorFun())) {
                        this.hasError = function (formCtrl, formElement) {
                            return $scope.getHasErrorFun()(getParentForm(formCtrl), formElement);
                        };
                    } else {
                        this.hasError = function (formCtrl, formElement) {
                            return ((formElement && formElement.$invalid) && (getParentForm(formCtrl).$submitted || formElement.$dirty));
                        };
                    }
                },
                link: function ($scope, element, attrs, formCtrl) {
                    $timeout(function () {
                        var formControl = angular.element(element.find('[form-control]'));

                        //in case of datepicker consider parent as form control as required attribute is getting appended to parent
                        if (formControl.hasClass('datepicker')) {
                            formControl = formControl.parent();
                        }

                        var controlLabel = angular.element(element.find('[control-label]'));
                        var appendAsterisk = function (isRequired) {
                            if (isRequired) {
                                if (controlLabel.length !== 0) {
                                    controlLabel.addClass("required");
                                }
                            } else {
                                if (controlLabel.length !== 0) {
                                    controlLabel.removeClass("required");
                                }
                            }
                        };

                        $scope.$watch(function () {
                            return formControl.is('[required]');
                        }, function (isRequired) {
                            appendAsterisk(isRequired);
                        });

                        if (formControl[0].type == 'text' && !formControl[0].getAttribute("ng-pattern")) {
                            formControl.attr("ng-pattern", '/^[^<>\$\^\|\?\\\\]*$/');
                            $compile(formControl)($scope.$parent);
                        }

                        $scope.hasError = function () {
                            var flag = $scope.validateCntrl.hasError(formCtrl, formCtrl[formControl.attr('name')]);
                            return flag;
                        };
                    });
                }
            };
        })
        .directive('placeholder', function ($templateCache, $interval) {
            return {
                restrict: 'E',
                require: ["^validate", "^form"],
                replace: true,
                scope: false,
                template: $templateCache.get('app/common/directives/formvalidation/placeholder.template.html'),
                link: function (scope, element, attrs, controllers) {
                    var validate = controllers[0];
                    var form = controllers[1];
                    var findElementIntervalPromise = $interval(function () {
                        if (form[validate.getForAttribute()]) {
                            scope.formElement = form[validate.getForAttribute()];
                            $interval.cancel(findElementIntervalPromise);
                        }
                    }, 1);
                    scope.hasError = function () {
                        return validate.hasError(form, scope.formElement);
                    };
                    scope.getMessage = function (error) {
                        return validate.getMessage(error);
                    };
                    scope.getObjectKeys = (object) => {
                        return object !== null && object !== undefined ? Object.keys(object) : [];
                    }
                }
            };
        });
})(window.angular);
