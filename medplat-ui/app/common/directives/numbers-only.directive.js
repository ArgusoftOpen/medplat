(function () {
    var numbersOnlyDir = function ($parse) {
        return {
            require: ['ngModel'],
            link: function (scope, element, attrs, ctrls) {
                scope.$watch(attrs.ngModel, function (newValue, oldValue) {
                    /* jshint ignore:start */
                    var model = $parse(attrs.ngModel);
                    var res;
                    var functionCall;
                    functionCall = $parse(attrs.functionCall);
                    if (newValue !== '' && newValue !== undefined && newValue != null) {
                        //both negative allowed and decimal allowed
                        if ((attrs.negativeallowed !== undefined && attrs.negativeallowed.toString() === "true") && (attrs.decimalallowed !== undefined && attrs.decimalallowed.toString() === 'true')) {
                            res = /^[-]?[0-9]*[.]?[0-9]*$/.test(newValue);

                            if (res) {
                                model.assign(scope, newValue);
                                functionCall(scope);
                            } else {
                                if (oldValue) {
                                    model.assign(scope, oldValue);
                                } else {
                                    model.assign(scope, '');
                                    functionCall(scope);

                                }
                            }
                        }
                        //only negative numbers
                        else if (attrs.negativeallowed !== undefined && attrs.negativeallowed.toString() === "true") {
                            res = /^-?[0-9]*$/.test(newValue);

                            if (res) {
                                model.assign(scope, newValue);
                                functionCall(scope);
                            } else {
                                if (oldValue) {
                                    model.assign(scope, oldValue);
                                    functionCall(scope);
                                } else {
                                    model.assign(scope, '');
                                    functionCall(scope);
                                }
                            }
                        }
                        //only decimal
                        else if (attrs.decimalallowed !== undefined && attrs.decimalallowed.toString() === 'true') {
                            if (attrs.allowedDecimalPoints) {
                                var allowedDecimalPoint = parseInt(attrs.allowedDecimalPoints)
                                var regexString = '^[0-9]*[.]?[0-9]{0,' + allowedDecimalPoint + '}$';
                                var regExp = new RegExp(regexString);
                                res = regExp.test(newValue);
                            } else {
                                res = /^[0-9]*[.]?[0-9]*$/.test(newValue);
                            }
                            if (res) {
                                model.assign(scope, newValue);
                                functionCall(scope);
                            } else {
                                if (oldValue) {
                                    model.assign(scope, oldValue);
                                    functionCall(scope);
                                } else {
                                    model.assign(scope, '');
                                    functionCall(scope);
                                }
                            }
                        } else if (attrs.slashallowed !== undefined && attrs.slashallowed.toString() === 'true') {
                            res = /^[0-9]*[/]?[0-9]*$/.test(newValue);

                            if (res) {
                                model.assign(scope, newValue);
                                functionCall(scope);
                            } else {
                                if (oldValue) {
                                    model.assign(scope, oldValue);
                                    functionCall(scope);
                                } else {
                                    model.assign(scope, '');
                                    functionCall(scope);
                                }
                            }
                        } else if (attrs.restrictzero !== undefined && attrs.restrictzero.toString() === 'true') {
                            res = /^[1-9][0-9]*$/.test(newValue);

                            if (res) {
                                model.assign(scope, newValue);
                                functionCall(scope);
                            } else {
                                if (oldValue) {
                                    model.assign(scope, oldValue);
                                    functionCall(scope);
                                } else {
                                    model.assign(scope, '');
                                    functionCall(scope);
                                }
                            }
                        } else {
                            res = /^[0-9]*$/.test(newValue);

                            if (res) {
                                model.assign(scope, newValue);
                                functionCall(scope);
                            } else {
                                if (oldValue) {

                                    model.assign(scope, oldValue);
                                    functionCall(scope);
                                } else {
                                    model.assign(scope, '');
                                    functionCall(scope);
                                }
                            }
                        }
                        let nV = newValue;
                        ctrls[0].$setValidity('max', (attrs.max && parseFloat(nV) <= parseFloat(attrs.max)) || attrs.max === undefined || !nV);
                        ctrls[0].$setValidity('min', (attrs.min && parseFloat(nV) >= parseFloat(attrs.min)) || attrs.min === undefined || !nV);
                        ctrls[0].$setValidity('maxlength', (attrs.maxLength && nV.length <= parseInt(attrs.maxLength)) || attrs.maxLength === undefined || !nV);
                        ctrls[0].$setValidity('minlength', (attrs.minLength && nV.length >= parseInt(attrs.minLength)) || attrs.minLength === undefined || !nV);
                    } else {
                        ctrls[0].$setValidity('max', true)
                        ctrls[0].$setValidity('min', true)
                        ctrls[0].$setValidity('maxlength', true)
                        ctrls[0].$setValidity('minlength', true)
                        model.assign(scope, '');
                        if (oldValue) {
                            functionCall(scope);
                        }
                    }
                    /* jshint ignore:end */
                });
            }
        };
    };
    angular.module('imtecho.directives').directive('numbersOnly', ['$parse', numbersOnlyDir]);
})();
