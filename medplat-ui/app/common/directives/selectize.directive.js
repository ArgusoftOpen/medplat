(function () {
    var app = angular.module('imtecho.directives');
    var selectizeDirective = function ($compile, $timeout) {
        return {
            restrict: 'A',
            require: '?ngModel',
            scope: {
                agSelectize: "=",
                tagObj: "=",
                options: "=",
                ngModel: "=",
                ngDisabled: "=",
                tagFlag: "=",
                parameterDataType: "=",
                setObject: "=",
                settings: "=?settings"
            },
            link: function (scope, element, attrs, controller) {
                scope.selectize = scope.agSelectize;
                var $select = element.selectize(scope.selectize);
                scope.selectizeObj = $select[0].selectize;
                scope.settings = scope.selectizeObj.settings;
                scope.dataFormatFlag = true;
                var num;
                //finding input created by selectize to apply tabOnEnter directive.
                if (angular.isDefined(attrs.tabOnEnter)) {
                    var input = angular.element($select[0].parentNode).find("input");
                    input.attr('tab-on-enter', '');
                    input.attr('tabindex', attrs.tabindex);
                    $compile(input)(scope);
                }
                scope.$watch("ngDisabled", function (value) {
                    if (value === true) {
                        scope.selectizeObj.disable();
                    } else {
                        scope.selectizeObj.enable();
                    }
                });
                scope.$watch("tagObj", function (value) {
                    if (value != null) {
                        var obj;
                        if (scope.agSelectize.delimiter) {
                            obj = value.split(scope.agSelectize.delimiter);
                        } else {
                            obj = value.split(",");
                        }
                        if (angular.isDefined(scope.parameterDataType) && scope.parameterDataType !== 'String') {
                            angular.forEach(obj, function (val) {
                                if (angular.isDefined(scope.parameterDataType) && scope.parameterDataType !== 'String') {
                                    if (scope.parameterDataType === 'Number') {
                                        num = parseInt(val);
                                        if (isNaN(num)) {
                                            scope.dataFormatFlag = false;
                                        } else {
                                            scope.dataFormatFlag = true;
                                        }
                                    } else if (scope.parameterDataType === 'Double') {
                                        num = parseFloat(val);
                                        if (isNaN(num)) {
                                            scope.dataFormatFlag = false;
                                        } else {
                                            scope.dataFormatFlag = true;
                                        }
                                    } else if (scope.parameterDataType === 'Boolean') {
                                        if (Boolean(val)) {
                                            scope.dataFormatFlag = true;
                                        } else {
                                            scope.dataFormatFlag = false;
                                        }
                                    } else if (scope.parameterDataType === 'Date') {
                                        num = parseDate(val);
                                        if (angular.isDate(num)) {
                                            scope.dataFormatFlag = true;
                                        } else {
                                            scope.dataFormatFlag = false;
                                        }
                                    }
                                }
                                scope.value = val;
                                scope.selectizeObj.addOption({ id: val, text: val });
                            });
                            if (scope.dataFormatFlag) {
                                scope.selectizeObj.setValue(obj);
                            } else {
                                scope.selectizeObj.removeOption(scope.value);
                            }
                        } else {
                            scope.selectizeObj.options = {};
                            angular.forEach(obj, function (val) {
                                scope.selectizeObj.addOption({ id: val, text: val });
                            });
                            scope.selectizeObj.setValue(obj);
                        }
                    } else {
                        scope.selectizeObj.setValue(null);
                    }
                });
                /* jshint ignore:start */
                var ngModelWatchTimeout = {};
                var createTimeOut = function (fun, value) {
                    if (ngModelWatchTimeout[value]) {
                        $timeout.cancel(ngModelWatchTimeout[value]);
                    }
                    ngModelWatchTimeout[value] = $timeout(fun);
                };
                if (attrs.ngModel != null) {
                    scope.$watch("ngModel", function (value) {
                        if (value != null) {
                            if (angular.isArray(value)) {
                                angular.forEach(value, function (obj) {
                                    scope.selectizeObj.addOption(obj);
                                });
                            } else {
                                if (attrs.setObject) {
                                    if (attrs.setObject && scope.setObject === undefined) {
                                        scope.setObject = {};
                                    }
                                    var selectizeOptions = [];
                                    selectizeOptions = scope.selectizeObj.options;
                                    if (Object.keys(selectizeOptions).length > 0) {
                                        var propertyToCheck = 'id';
                                        if (attrs.isProp === 'true') {
                                            propertyToCheck = 'assetId';
                                        }
                                        scope.setObject = _.find(selectizeOptions, function (file) {
                                            return file[propertyToCheck] == value;
                                        });
                                    }
                                    else {
                                        if (scope.setObject) {
                                            if (!scope.options) {
                                                scope.options = [];
                                            }
                                            scope.options.push(scope.setObject);
                                        }
                                    }
                                }
                                scope.selectizeObj.addOption(value);
                            }
                            createTimeOut(function () {
                                if (attrs.tagObj == null) {
                                    scope.selectizeObj.setValue(value);
                                }
                            }, value);
                        } else {
                            createTimeOut(function () {
                                controller.$pristine = false;
                                scope.selectizeObj.setValue(null);
                            }, value);
                        }
                    }, true);
                }
                scope.$watch('options', function (newTags, oldTags) {
                    if (attrs.setObject) {
                        if (!scope.setObject || scope.setObject.id != scope.ngModel) {
                            scope.setObject = _.find(newTags, function (file) {
                                return file.id == scope.ngModel;
                            });
                        }
                    }
                    if (angular.isDefined(newTags)) {
                        if (oldTags != null && newTags.length < oldTags.length) {
                            return;
                        }
                        angular.forEach(newTags, function (tag) {
                            scope.selectizeObj.addOption(tag);
                        });
                    }
                }, true);
                /* jshint ignore:end */
                scope.$watch('optgroups', function (newGroups, oldGroups) {
                    if (newGroups !== undefined) {
                        if (newGroups.length < oldGroups.length) {
                            angular.forEach(oldGroups, function (obj) {
                                if (JSON.stringify(newGroups).indexOf(JSON.stringify(obj)) < 0) {
                                    angular.forEach(scope.selectize.options, function (option) {
                                        if (option[scope.selectize.optgroupField] === obj.id) {
                                            scope.selectizeObj.removeOption(option.id);
                                        }
                                    });
                                }
                            });
                        } else {
                            angular.forEach(newGroups, function (tag) {
                                scope.selectizeObj.addOptionGroup(tag.id, tag);
                            });
                        }
                    }
                }, true);
                scope.$on('$destroy', function () {
                    scope.selectizeObj.destroy();
                });
            }
        };
    };
    app.directive('agSelectize', selectizeDirective);
})();
