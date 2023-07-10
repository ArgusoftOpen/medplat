(() => {
    let techoFormFieldDirective = (toaster, GeneralUtil, $compile, $timeout, UUIDgenerator, $rootScope) => {
        return {
            restrict: 'E',
            scope: {
                config: '=',
                configJson: '=',
                iteratorIndicesMap: '=?',
            },
            require: '^ngController',
            templateUrl: 'app/common/components/techoFormField/template.html',
            link: ($scope, elements, attributes, ctrl) => {
                $scope.ctrl = ctrl;
                $scope.uuid = UUIDgenerator.generateUUID();
                $scope.defaultTextPattern = "^[^<>\$\^\|\?\\\\]*$";
                const BETWEEN_OPERATOR = "BT";
                const OPERATOR_MAPPING = {
                    "EQ": "==",
                    "NQ": "!=",
                    "EQWithType": "===",
                    "NQWithType": "!==",
                    "GT": ">",
                    "LT": "<",
                    "GTE": ">=",
                    "LTE": "<="
                }

                let _generateExpression = (option, fieldName) => {
                    if (option.operator === BETWEEN_OPERATOR) {
                        return `> ${option.value1} && ctrl.${fieldName} < ${option.value2}`;
                    } else {
                        return `${OPERATOR_MAPPING[option.operator]} ${option.value1}`;
                    }
                }

                let _getExpressionFromJson = (data, array, parentJoinType) => {
                    let joinType = data.conditions.rule === 'AND' ? '&&' : '||';
                    let expressionArray = [];
                    data.conditions.options.forEach((option) => {
                        if (option.hasOwnProperty('conditions')) {
                            _getExpressionFromJson(option, array, joinType);
                        }
                        switch (option.type) {
                            case 'FIELD':
                                if (option.fieldName.includes('[$index]')) {
                                    let exArray = option.fieldName.split("[$index].");
                                    let exElement = "";
                                    let exString = "";
                                    exArray.forEach((element, index) => {
                                        exElement = index === 0 ? element : exElement.concat(`[$index].${element}`);
                                        if ($scope.iteratorIndicesMap[exElement] !== null && typeof $scope.iteratorIndicesMap[exElement] !== 'undefined') {
                                            exString = index === 0 ? exString.concat(`${element}[${$scope.iteratorIndicesMap[exElement]}]`) : exString.concat(`.${element}[${$scope.iteratorIndicesMap[exElement]}]`);
                                        } else {
                                            exString = index === 0 ? exString.concat(`${element}`) : exString.concat(`.${element}`);
                                        }
                                    });
                                    expressionArray.push(`(ctrl.${exString} ${_generateExpression(option, exString)})`);
                                } else {
                                    let exArray = option.fieldName.split(".");
                                    if ($scope.iteratorIndicesMap[exArray.slice(0, exArray.length - 1).join(".")] !== null && typeof $scope.iteratorIndicesMap[exArray.slice(0, exArray.length - 1).join(".")] !== 'undefined') {
                                        exArray[exArray.length - 2] = `${exArray[exArray.length - 2]}[${$scope.iteratorIndicesMap[exArray.slice(0, exArray.length - 1).join(".")]}]`;
                                    }
                                    expressionArray.push(`(ctrl.${exArray.join(".")} ${_generateExpression(option, exArray.join("."))})`);
                                }
                                break;
                            case 'CUSTOM':
                                let exArray = option.expression.split("[$index].");
                                let exElement = "";
                                exArray.forEach((element, index) => {
                                    exElement = index === 0 ? element : exElement.concat(`[$index].${element}`);
                                    if ($scope.iteratorIndicesMap[exElement] !== null && typeof $scope.iteratorIndicesMap[exElement] !== 'undefined') {
                                        exArray[index] = element.concat(`[${$scope.iteratorIndicesMap[exElement]}]`);
                                    }
                                });
                                expressionArray.push(`ctrl.${exArray.join(".")}`);
                                break;
                        }
                    });
                    if (expressionArray.length) {
                        if ($scope[array] && $scope[array].length) {
                            $scope[array] = `(${expressionArray.join(joinType)})`.concat(parentJoinType || joinType).concat($scope[array]);
                        } else {
                            $scope[array] = `(${expressionArray.join(joinType)})`;
                        }
                    }
                }

                let _init = () => {
                    if ($scope.configJson.fieldType) {
                        $scope.componentIdentifier = `${$scope.configJson.fieldKey}${$scope.uuid}`;
                        /* $scope.uniqueId = '';
                        if ($scope.iteratorIndicesMap) {
                            for (const property in $scope.iteratorIndicesMap) {
                                $scope.uniqueId += `__${$scope.iteratorIndicesMap[property]}`;
                            }
                        } */
                        if ($scope.configJson.fieldType === 'CUSTOM_HTML') {
                            $timeout(function () {
                                let angularElement = angular.element($scope.configJson.customHTML);
                                let linkFunction = $compile(angularElement);
                                let newScope = $scope.$new();
                                let el = linkFunction(newScope);
                                angular.element(`#customHTML${$scope.uuid}`).append(el[0]);
                            })
                            return;
                        }
                        if ($scope.configJson.ngModel) {
                            let ngModelArray = $scope.configJson.ngModel.split("[$index].");
                            let ngModelElement = "";
                            ngModelArray.forEach((element, index) => {
                                ngModelElement = index === 0 ? element : ngModelElement.concat(`[$index].${element}`);
                                $scope.ngModelObject = index === 0 ? GeneralUtil.getPropByPath(ctrl, element) : GeneralUtil.getPropByPath($scope.ngModelObject, element);
                                if ($scope.iteratorIndicesMap[ngModelElement] !== null && typeof $scope.iteratorIndicesMap[ngModelElement] !== 'undefined') {
                                    $scope.ngModelObject = $scope.ngModelObject[$scope.iteratorIndicesMap[ngModelElement]];
                                }
                            });
                        }
                        $scope.isRequired = String($scope.configJson.isRequired) === 'true';
                        $scope.isHidden = String($scope.configJson.isHidden) === 'true';
                        $scope.isDisabled = String($scope.configJson.isDisabled) === 'true';
                        if ($scope.configJson.events && $scope.configJson.events !== 'null' && $scope.configJson.events !== '[]') {
                            let events = JSON.parse($scope.configJson.events);
                            events.forEach((event) => {
                                let e = `ng${event.type}`;
                                $scope[e] = ctrl[event.value];
                            });
                        }
                        if ($scope.configJson.visibility && $scope.configJson.visibility !== 'null' && $scope.configJson.visibility !== '{}') {
                            _getExpressionFromJson(JSON.parse($scope.configJson.visibility), 'visible');
                        } else {
                            $scope.visible = 'true';
                        }
                        if ($scope.configJson.requirable && $scope.configJson.requirable !== 'null' && $scope.configJson.requirable !== '{}') {
                            _getExpressionFromJson(JSON.parse($scope.configJson.requirable), 'required');
                        } else {
                            $scope.required = 'false';
                        }
                        if ($scope.configJson.disability && $scope.configJson.disability !== 'null' && $scope.configJson.disability !== '{}') {
                            _getExpressionFromJson(JSON.parse($scope.configJson.disability), 'disabled');
                        } else {
                            $scope.disabled = 'false';
                        }
                        switch ($scope.configJson.fieldType) {
                            case 'DROPDOWN':
                                if (typeof $scope.configJson.isMultiple === 'undefined' || String($scope.configJson.isMultiple) === 'false') {
                                    $scope.configJson.isMultiple = false;
                                } else {
                                    $scope.configJson.isMultiple = true;
                                }
                                $scope.optionsArray = [];
                                if ($scope.configJson && $scope.configJson.optionsType) {
                                    switch ($scope.configJson.optionsType) {
                                        case 'staticOptions':
                                            try {
                                                $scope.optionsArray.data = JSON.parse($scope.configJson.staticOptions);
                                            } catch (error) {
                                                toaster.pop('error', `Error parsing JSON for ${$scope.configJson.fieldKey}`);
                                            }
                                            break;
                                        case 'listValueField':
                                            try {
                                                $scope.optionsArray.data = JSON.parse($scope.configJson.listValueField).map((element) => {
                                                    return {
                                                        key: element['id'],
                                                        value: element['value']
                                                    }
                                                });
                                                if ($scope.configJson.staticOptions && $scope.configJson.staticOptions.length) {
                                                    $scope.optionsArray.data.push(...JSON.parse($scope.configJson.staticOptions));
                                                }
                                            } catch (error) {
                                                toaster.pop('error', `Error parsing JSON for ${$scope.configJson.fieldKey}`);
                                            }
                                            break;
                                        case 'queryBuilder':
                                            $scope.ngModelObject[`${$scope.configJson.fieldKey}List`] = { data: [] };
                                            $scope.optionsArray = $scope.ngModelObject[`${$scope.configJson.fieldKey}List`];
                                            $rootScope.$broadcast('onAllComponentsLoaded');
                                            break;
                                    }
                                }
                                break;
                            case 'CHECKBOX':
                                $scope.optionsArray = [];
                                if ($scope.configJson && $scope.configJson.staticOptions) {
                                    $scope.optionsArray = JSON.parse($scope.configJson.staticOptions);
                                }
                                break;
                            case 'RADIO':
                                $scope.optionsArray = [];
                                if (typeof $scope.configJson.isBoolean === 'undefined' || String($scope.configJson.isBoolean) === 'false') {
                                    $scope.configJson.isBoolean = false;
                                } else {
                                    $scope.configJson.isBoolean = true;
                                }
                                if ($scope.configJson && $scope.configJson.optionsType) {
                                    switch ($scope.configJson.optionsType) {
                                        case 'staticOptions':
                                            try {
                                                $scope.optionsArray = JSON.parse($scope.configJson.staticOptions);
                                            } catch (error) {
                                                toaster.pop('error', `Error parsing JSON for ${$scope.configJson.fieldKey}`);
                                            }
                                            break;
                                        case 'listValueField':
                                            try {
                                                $scope.optionsArray = JSON.parse($scope.configJson.listValueField).map((element) => {
                                                    return {
                                                        key: element['id'],
                                                        value: element['value']
                                                    }
                                                });
                                                if ($scope.configJson.staticOptions && $scope.configJson.staticOptions.length) {
                                                    $scope.optionsArray.push(...JSON.parse($scope.configJson.staticOptions));
                                                }
                                            } catch (error) {
                                                toaster.pop('error', `Error parsing JSON for ${$scope.configJson.fieldKey}`);
                                            }
                                            break;
                                        case 'queryBuilder':
                                            $scope.ngModelObject[`${$scope.configJson.fieldKey}List`] = { data: [] };
                                            $scope.optionsArray = $scope.ngModelObject[`${$scope.configJson.fieldKey}List`].data;
                                            // $rootScope.$broadcast('onAllComponentsLoaded');
                                            break;
                                    }
                                } else if ($scope.configJson && $scope.configJson.staticOptions) {
                                    $scope.optionsArray = JSON.parse($scope.configJson.staticOptions);
                                }
                                break;
                            case 'DATE':
                                //Min Date Validation
                                let minDateArray = $scope.configJson.minDateField.split(".");
                                if (Array.isArray(minDateArray) && minDateArray.length > 1) {
                                    $scope.minDateProperty = minDateArray[minDateArray.length - 1];
                                    minDateArray = minDateArray.slice(0, minDateArray.length - 1);
                                    if (minDateArray[minDateArray.length - 1].includes("[$index]")) {
                                        minDateArray = minDateArray.join(".").split("[$index]");
                                        minDateArray = minDateArray.slice(0, minDateArray.length - 1);
                                    } else {
                                        minDateArray = minDateArray.join(".").split("[$index].");
                                    }
                                    let minDateElement = "";
                                    minDateArray.forEach((element, index) => {
                                        minDateElement = index === 0 ? element : minDateElement.concat(`[$index].${element}`);
                                        $scope.minDateObject = index === 0 ? GeneralUtil.getPropByPath(ctrl, element) : GeneralUtil.getPropByPath($scope.minDateObject, element);
                                        if ($scope.iteratorIndicesMap[minDateElement] !== null && typeof $scope.iteratorIndicesMap[minDateElement] !== 'undefined') {
                                            $scope.minDateObject = $scope.minDateObject[$scope.iteratorIndicesMap[minDateElement]];
                                        }
                                    });
                                } else {
                                    [$scope.minDateObject, $scope.minDateProperty] = GeneralUtil.getObjAndPropByPath(ctrl, $scope.configJson.minDateField);
                                }
                                //Max Date Validation
                                let maxDateArray = $scope.configJson.maxDateField.split(".");
                                if (Array.isArray(maxDateArray) && maxDateArray.length > 1) {
                                    $scope.maxDateProperty = maxDateArray[maxDateArray.length - 1];
                                    maxDateArray = maxDateArray.slice(0, maxDateArray.length - 1);
                                    if (maxDateArray[maxDateArray.length - 1].includes("[$index]")) {
                                        maxDateArray = maxDateArray.join(".").split("[$index]");
                                        maxDateArray = maxDateArray.slice(0, maxDateArray.length - 1);
                                    } else {
                                        maxDateArray = maxDateArray.join(".").split("[$index].");
                                    }
                                    let maxDateElement = "";
                                    maxDateArray.forEach((element, index) => {
                                        maxDateElement = index === 0 ? element : maxDateElement.concat(`[$index].${element}`);
                                        $scope.maxDateObject = index === 0 ? GeneralUtil.getPropByPath(ctrl, element) : GeneralUtil.getPropByPath($scope.maxDateObject, element);
                                        if ($scope.iteratorIndicesMap[maxDateElement] !== null && typeof $scope.iteratorIndicesMap[maxDateElement] !== 'undefined') {
                                            $scope.maxDateObject = $scope.maxDateObject[$scope.iteratorIndicesMap[maxDateElement]];
                                        }
                                    });
                                } else {
                                    [$scope.maxDateObject, $scope.maxDateProperty] = GeneralUtil.getObjAndPropByPath(ctrl, $scope.configJson.maxDateField);
                                }
                                break;
                            case 'TABLE':
                                try {
                                    $scope.tableConfig = JSON.parse($scope.configJson.tableConfig);
                                } catch (error) {
                                    toaster.pop('error', `Error parsing JSON for ${$scope.configJson.fieldKey}`);
                                }
                                $scope.tableObject = GeneralUtil.getPropByPath(ctrl, $scope.configJson.tableObject);
                                break;
                            case 'INFORMATION_TEXT':
                                $scope.isClickable = String($scope.configJson.isClickable) === 'true'
                                break;
                        }
                    }
                }

                $scope.getDynamicStyles = function () {
                    return $scope.configJson.cssStyle || null;
                }

                $scope.getLabelDynamicStyles = function () {
                    return $scope.config.labelCssStyles;
                }

                $scope.getInputDynamicStyles = function () {
                    return $scope.config.inputCssStyles;
                }

                _init();
            }
        }
    };
    angular.module('imtecho.directives').directive('techoFormFieldDirective', techoFormFieldDirective);
})();
