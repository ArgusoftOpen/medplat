(function (angular) {
    angular.module('imtecho.directives').directive('dynamicview', function ($timeout, $sessionStorage, $templateCache, mapToArrayFilter, $sce, AuthenticateService, $state, Mask, GeneralUtil, toaster) {
        return {
            restrict: 'E',
            template: $templateCache.get('app/common/directives/dynamicview/tmpl.html'),
            scope: {
                config: '=',
                data: '<',
                containers: '=',
                headings: '=',
                navigateToConfig: '&',
                isTableContainerConfigurable: '=',
                filterSearch: '&',
                searchConfig: '=',
                pagingService: '=?',
                navigationstate: '='
            },
            link: function ($scope, element, attrs) {
                $scope.customSort = function (index , fieldName, isPagination) {
                    if(index == $scope.selectedIndex || index == $scope.selectedReverseIndex){
                        reverse = !reverse;
                    }
                    else{
                        reverse = false;
                    }
                    if (!isPagination) {
                        if (!reverse) {
                            $scope.selectedIndex = index;
                            $scope.selectedReverseIndex = null;
                            if (typeof $scope.data.filteredData.body[0][fieldName].displayValue === 'string') {
                                $scope.data.filteredData.body.sort(function (oldRow, newRow) {
                                    if (oldRow[fieldName].displayValue < newRow[fieldName].displayValue) {
                                        return -1;
                                    }
                                    if (oldRow[fieldName].displayValue > newRow[fieldName].displayValue) {
                                        return 1;
                                    }
                                    return 0;
                                })
                            } else {
                                $scope.data.filteredData.body.sort(function (oldRow, newRow) {
                                    return oldRow[fieldName].displayValue - newRow[fieldName].displayValue;
                                })
                            }
                        }
                        else {
                            $scope.selectedIndex = null;
                            $scope.selectedReverseIndex = index;
                            if (typeof $scope.data.filteredData.body[0][fieldName].displayValue === 'string') {
                                $scope.data.filteredData.body.sort(function (oldRow, newRow) {
                                    if (oldRow[fieldName].displayValue < newRow[fieldName].displayValue) {
                                        return 1;
                                    }
                                    if (oldRow[fieldName].displayValue > newRow[fieldName].displayValue) {
                                        return -1;
                                    }
                                    return 0;
                                })
                            } else {
                                $scope.data.filteredData.body.sort(function (oldRow, newRow) {
                                    return newRow[fieldName].displayValue - oldRow[fieldName].displayValue;
                                })
                            }
                        }
                    }
                }
                AuthenticateService.getLoggedInUser().then(function (res) {
                    $scope.canDrillDown = false;
                    $scope.loggedInUser = res;
                    if($scope.navigationstate!=null){
                        $scope.getRights();
                    }
                });
                $scope.getRights = function () {
                    Mask.show();
                    AuthenticateService.getAssignedFeature($scope.navigationstate).then(function (res) {
                        if (!!res) {
                            $scope.rights = res.featureJson;
                            $scope.rights.canDrillDown ? $scope.canDrillDown = true : $scope.canDrillDown = false;
                        }
                    }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                        Mask.hide();
                    });
                };
                $scope.isTemplateConfigPage = attrs.isTemplateConfigPage;
                $scope.isContainerConfig = attrs.isContainerConfig;
                $scope.navigateToState = function (row, header, $this) {
                    var temp = row[header.fieldName].customState;
                    //replace data for @fieldParam@ of row elements
                    var rowFields = temp.match(/@(.*?)@/g);
                    if (rowFields) {
                        _.each(rowFields, function (field) {
                            temp = temp.replace(new RegExp(field), row[field.substring(1, field.length - 1)].displayValue);
                        });
                    }
                    //replace data for $fieldParam$ of filter elements
                    var fields = temp.match(/\$(.*?)\$/g);
                    if (fields) {
                        _.each(fields, function (field) {
                            var keyString = field.substring(1, field.length - 1)
                            var replacingValue = _.find($scope.config.containers.fieldsContainer, function (filterField) {
                                return filterField.fieldName === keyString;
                            });
                            if (!replacingValue) {
                                replacingValue = _.find($scope.config.containers.fieldsContainer, function (filterField) {
                                    return "from_" + filterField.fieldName === keyString;
                                });
                                if (replacingValue && replacingValue.value) {
                                    replacingValue.value = moment(replacingValue.value.startDate).format("MM/DD/YYYY");
                                }
                            }
                            if (!replacingValue) {
                                replacingValue = _.find($scope.config.containers.fieldsContainer, function (filterField) {
                                    return "to_" + filterField.fieldName === keyString;
                                });
                                if (replacingValue && replacingValue.value) {
                                    replacingValue.value = moment(replacingValue.value.endDate).format("MM/DD/YYYY");
                                }
                            }
                            if (replacingValue && replacingValue.value && replacingValue.fieldType == 'date') {
                                replacingValue.value = moment(replacingValue.value).format("MM/DD/YYYY");
                            } else if (replacingValue && replacingValue.value && (replacingValue.fieldType == 'dateFromTo' || replacingValue.fieldType === 'onlyMonthFromTo')) {
                                if ("from_" + replacingValue.fieldName === keyString) {
                                    replacingValue.value = moment($scope.data["from_" + replacingValue.fieldName], "DD/MM/YYYY").format("MM/DD/YYYY");
                                }
                                if ("to_" + replacingValue.fieldName === keyString) {
                                    replacingValue.value = moment($scope.data["to_" + replacingValue.fieldName], "DD/MM/YYYY").format("MM/DD/YYYY");
                                }
                            }
                            if (replacingValue && replacingValue.value) {
                                temp = temp.replace(new RegExp('\\$' + keyString + '\\$'), replacingValue.value);
                            } else {
                                temp = temp.replace(new RegExp('\\$' + keyString + '\\$'), 'null');
                            }
                        });
                    }
                    var parameters = JSON.parse(temp.substring(temp.indexOf('(') + 1, temp.length - 1));
                    var state = temp.substring(0, temp.indexOf('(')) + '/' + angular.toJson(parameters);
                    if ($sessionStorage.asldkfjlj) {
                        $sessionStorage.asldkfjlj[state] = true;
                    } else {
                        $sessionStorage.asldkfjlj = {};
                        $sessionStorage.asldkfjlj[state] = true;
                    }
                    var url = $state.href(temp.substring(0, temp.indexOf('(')), parameters, { inherit: false, absolute: false });
                    sessionStorage.setItem('linkClick', 'true')
                    $scope.$applyAsync(function () {
                        window.open(url, '_blank');
                    });
                };
                $scope.fixHeader = function () {
                    $timeout(function () {
                        $(".header-fixed").tableHeadFixer();
                    });
                };
                $scope.$watch('config.containers', function (newValue) {
                    if (newValue) {
                        $scope.isFilterToBeShown = _.filter($scope.config.containers.fieldsContainer, function (field) {
                            return !field.isHidden;
                        }).length > 0;
                    }
                });
                $scope.$watch('config.isFilterOpen', function (newValue) {
                    if (newValue) {
                        $scope.isFilterOpen = newValue;
                    }
                });
                $scope.$watch('config.containers.tableContainer[0].htmlData', function (newValue) {
                    if (newValue) {
                        if (newValue.includes('#table#')) {
                            var str = newValue.split('#table#');
                            $scope.aboveData = str[0];
                            $scope.belowData = str[1];
                        }
                    }
                });
                $scope.$watch('config.containers.tableContainer[0].dataColspan', function (newValue) {
                    if (newValue) {
                        $scope.colSpan = newValue;
                    }
                });
                if (attrs.isContainerConfig != null) {
                    var setContainersAndHeadings = function (ele, elementType) {
                        var elementName = ele.attr('name');
                        var displayName = ele.attr('header');
                        if (displayName == null) {
                            displayName = elementName;
                        }
                        //to add the tooltip
                        ele.attr('title', displayName);
                        var elementJson = { name: elementName, displayName: displayName };
                        if (elementType === 'container') {
                            $scope.containers.push(elementJson);
                        } else {
                            $scope.headings.push(elementJson);
                        }
                    };
                    $scope.templateLoaded = function () {
                        $timeout(function () {
                            $scope.$apply(function () {
                                $scope.containers = [];
                                $scope.headings = [];
                            });
                            if ($scope.config.layout != null) {
                                $(element).find("container").each(function () {
                                    setContainersAndHeadings($(this), 'container');
                                });
                                $(element).find("heading").each(function () {
                                    setContainersAndHeadings($(this), 'heading');
                                });
                            }
                        });
                    };
                    $scope.$watch(function () {
                        if ($scope.config && $scope.config.templateType) {
                            return $scope.config.templateType;
                        }
                    }, function () {
                        $scope.templateLoaded();
                    });
                    $scope.$watch(function () {
                        if ($scope.isTableContainerConfigurable) {
                            return $scope.isTableContainerConfigurable;
                        }
                    }, function () {
                        $scope.templateLoaded();
                    });
                    $scope.focusNext = function ($event) {
                        var clickedElement = angular.element($event.target);
                        var elementToBeFocus = angular.element(clickedElement.next('input'));
                        if (elementToBeFocus.length <= 0) {
                            if (clickedElement.parent('span')) {
                                elementToBeFocus = angular.element(clickedElement.parent('span').next('input'));
                            }
                        }
                        elementToBeFocus.focus();
                    };
                }
                if ($scope.config && $scope.config.containers) {
                    $scope.containersArray = mapToArrayFilter($scope.config.containers);
                }
                $scope.clearFilters = function () {
                    _.each($scope.config.containers.fieldsContainer, function (field) {
                        if ($scope.data && $scope.data[field.fieldName]) {
                            delete $scope.data[field.fieldName];
                        }
                    });
                };
                $scope.toTrustedHTML = function (html) {
                    if (html || html === 0) {
                        html = html.toString();
                        return $sce.trustAsHtml(html);
                    }
                };

                $scope.toggleFilter = function () {
                    if (angular.element('.filter-div').hasClass('active')) {
                        angular.element('body').css("overflow", "auto");
                    } else {
                        angular.element('body').css("overflow", "hidden");
                    }
                    angular.element('.cst-backdrop').fadeToggle();
                    angular.element('.filter-div').toggleClass('active');
                };
            }
        };
    });
    angular.module('imtecho.directives').directive('datacomponent', function ($sce, APP_CONFIG, $rootScope, $templateCache, $state) {
        return {
            restrict: 'AE',
            template: $templateCache.get('app/common/directives/dynamicview/datacomponent.html'),
            scope: {
                data: "=",
                config: '<'
            },
            link: function ($scope, element, attrs) {
                $scope.angularDateFormat = APP_CONFIG.angularDateFormat;
                $scope.authToken = $rootScope.authToken;
                $scope.apiPath = APP_CONFIG.apiPath;
                $scope.fieldName = attrs.name;
                if ($scope.config) {
                    $scope.config.isNavigationLink = $scope.config.navigationState != null ? true : false;
                }
                var dataWatch = $scope.$watch("data", function (data) {
                    if (data) {
                        var fields = $scope.fieldName.split(".");
                        var valueObj = $scope.data[fields[0]];
                        for (var i = 1; i < fields.length; i++) {
                            valueObj = valueObj[fields[i]];
                        }
                        $scope.displayObject = valueObj;
                        if ($scope.displayObject == null) {
                            $scope.displayObject = 'N.A';
                        }
                        dataWatch();
                    }
                });
                $scope.fieldType = attrs.type;
                if ($scope.fieldType === 'file' && attrs.hasFileArray) {
                    $scope.fieldType = 'fileList';
                }
                $scope.toTrustedHTML = function (html) {
                    if (html && typeof html === 'string') {
                        return $sce.trustAsHtml(html);
                    }
                };
                $scope.navigateToLink = function () {
                    if ($scope.config.isNavigationLink) {
                        $scope.config.navigationState = $scope.config.navigationState.replace(new RegExp("data.", 'g'), "$scope.data");
                        $state.go(($scope.config.navigationState));
                    }
                };
            }
        };
    });
    angular.module('imtecho.directives').directive('inputcomponent', function ($sce, $templateCache, $timeout, $rootScope) {
        return {
            restrict: 'AE',
            template: $templateCache.get('app/common/directives/dynamicview/inputcomponent.html'),
            scope: {
                data: "=",
                config: '='
            },
            link: function ($scope, element, attrs) {
                $scope.locationChange = function () {
                    $scope.$emit('locationChange', $scope.config.selectedLocation)
                }
                $scope.labelClass = attrs.labelClass;
                $scope.inputClass = attrs.inputClass;
                $scope.onlyMonthFrom = 'from_' + $scope.config.fieldName;
                $scope.onlyMonthTo = 'to_' + $scope.config.fieldName;
                $scope.dateFrom = 'from_' + $scope.config.fieldName;
                $scope.dateTo = 'to_' + $scope.config.fieldName;
                $scope.minDateSelected = false;
                $scope.maxDateSelected = false;
                $scope.fieldValueChanged = function (fieldName, fieldValue) {
                    if ($scope.config.fieldChangeCallback != null) {
                        $scope.config.fieldChangeCallback(fieldName, fieldValue);
                    }
                };
                $scope.minDateFieldValidation = function (fieldName, fieldValue) {
                    $scope.minDateSelected = true;
                    $scope.minDate = new Date(fieldValue);
                    $scope.fieldValueChanged(fieldName, fieldValue)
                };
                $scope.maxDateFieldValidation = function (fieldName, fieldValue) {
                    $scope.maxDateSelected = true;
                    $scope.maxDate = new Date(fieldValue);
                    $scope.fieldValueChanged(fieldName, fieldValue)
                };
                $scope.minMonthFieldValidation = function (fieldName, fieldValue) {
                    $scope.minMonthSelected = true;
                    $scope.minMonth = new Date(fieldValue);
                    $scope.fieldValueChanged(fieldName, fieldValue)
                };
                $scope.maxMonthFieldValidation = function (fieldName, fieldValue) {
                    $scope.maxMonthSelected = true;
                    $scope.maxMonth = new Date(fieldValue);
                    $scope.fieldValueChanged(fieldName, fieldValue)
                };
                var mergedObj = {};
                //if configuration contains resourceCategoryItemFieldDto then perform below merge operations
                if ($scope.config.resourceCategoryItemFieldDto) {
                    //override resourceCategoryItemFieldDto with resourceCategoryItemFieldDto.configJson
                    angular.merge(mergedObj, $scope.config.resourceCategoryItemFieldDto);
                    if (mergedObj.configJson) {
                        angular.merge(mergedObj, mergedObj.configJson);
                    }
                    if ($scope.config.configJson) {
                        angular.merge(mergedObj, $scope.config.configJson);
                    }
                }
                if ($scope.config.configJson && $scope.config.configJson.isFormula) {
                    $scope.$watch("data", function () {
                        var formula = $scope.config.configJson.formula;
                        if ($scope.config.configJson.formulaFieldsMap) {
                            angular.forEach($scope.config.configJson.formulaFieldsMap, function (fieldName, fieldVariable) {
                                if ($scope.data[fieldName]) {
                                    var fieldValue = angular.copy($scope.data[fieldName]);
                                    if (fieldValue instanceof Date) {
                                        fieldValue = $scope.data[fieldName].getTime();
                                    }
                                    formula = formula.replace(new RegExp(fieldVariable, 'g'), fieldValue);
                                } else {
                                    formula = null;
                                }
                            });
                            try {
                                /*jshint ignore:start*/
                                var calculatedValue = (eval(formula));
                                $scope.data[$scope.config.resourceCategoryItemFieldDto.fieldName] = calculatedValue;
                                /*jshint ignore:end*/
                            } catch (e) {
                                $scope.data[$scope.config.resourceCategoryItemFieldDto.fieldName] = 0;
                            }
                        }
                    }, true);
                }

                var checkDisableFromConfig = function () {
                    var equals = ($scope.config.configJson.disabledWhenStatusEqual && $scope.config.configJson.disabledWhenStatusEqual.indexOf($scope.data.status) >= 0);
                    var notequals = ($scope.config.configJson.disabledWhenStatusNotEqual && $scope.config.configJson.disabledWhenStatusNotEqual.indexOf($scope.data.status) < 0);
                    if (equals || notequals) {
                        $scope.isDisabledFromConfiguration = true;
                    } else {
                        $scope.isDisabledFromConfiguration = false;
                    }
                };

                $scope.addHasElementClass = function () {
                    if ($(element).has(".input-element")) {
                        $(element).addClass("haselements");
                    }
                };
                var setDefaultValue = function () {
                    if (!$scope.data[$scope.config.resourceCategoryItemFieldDto.fieldName] && $scope.config.configJson.defaultValue) {
                        var defaultValue;
                        switch ($scope.config.resourceCategoryItemFieldDto.fieldType) {
                            case 'text':
                            case 'number':
                            case 'currency':
                                defaultValue = $scope.config.configJson.defaultValue;
                                break;
                            case 'combo':
                            case 'multiselect':
                            case 'autocomplete':
                                defaultValue = _.find($scope.config.resourceCategoryItemFieldDto.resourceCategoryItemFieldOptionList, function (option) {
                                    return option.id === $scope.config.configJson.defaultValue;
                                });
                                break;
                            case 'date':
                                if ($scope.config.configJson.defaultValue && $scope.config.configJson.defaultValue.isToday) {
                                    defaultValue = $rootScope.today;
                                }
                                break;
                        }
                        if (defaultValue) {
                            $scope.data[$scope.config.resourceCategoryItemFieldDto.fieldName] = defaultValue;
                        }
                    }
                };
                var setDefault = $(element).closest('dynamicview').attr('set-default-value');
                if (setDefault === 'true') {
                    setDefault = true;
                }
                else {
                    setDefault = false;
                }
                if (setDefault) {
                    setDefaultValue();
                }
                $scope.removeHasElementClass = function () {
                    $timeout(function () {
                        if ($(element).has(".input-element").length <= 0) {
                            $(element).removeClass("haselements");
                        }
                        delete $scope.data[$scope.config.resourceCategoryItemFieldDto.fieldName];
                    });
                };
                var configObj = {
                        create: false,
                        valueField: 'key',
                        labelField: 'value',
                        highlight: true,
                        searchField: ['value'],
                        onFocus: function () {
                            this.onSearchChange("");
                        },
                        onBlur: function () {
                            var selectize = this;
                            var value = this.getValue();
                            $timeout(function () {
                                if (!value) {
                                    selectize.clearOptions();
                                    selectize.refreshOptions();
                                }
                            }, 200);
                        },
                        load: function (query, callback) {
                            var selectize = this;
                            var value = this.getValue();
                            if (!value) {
                                selectize.clearOptions();
                                selectize.refreshOptions();
                            }
                            var promise;
                            promise = $scope.config.fieldSelectizeCallback($scope.config.fieldName, query);
                            promise.then(function (res) {
                                callback(res);
                            }, function () {
                                callback();
                            });
                        }
                    };
                if ($scope.config.fieldType === 'selectizeForCombo') {
                    configObj.maxItems = 1;
                    $scope.selectizeReportComboConfig = configObj;
                }
                if ($scope.config.fieldType === 'selectizeForMulti') {
                    $scope.selectizeForMulti = configObj;
                }
                var dataWatch = $scope.$watch("data", function (data) {
                    if (data && data.isRetrieved) {
                        checkDisableFromConfig();
                        if ($scope.config.resourceCategoryItemFieldDto.fieldType === 'person') {
                            if (data[$scope.config.resourceCategoryItemFieldDto.fieldName] && data[$scope.config.resourceCategoryItemFieldDto.fieldName].isOrg) {
                                if (data.address) {
                                    data.address.isDisabled = true;
                                }
                            }
                        }
                        dataWatch();
                    }
                }, true);
                $scope.toTrustedHTML = function (html) {
                    if (html) {
                        html = html.toString();
                        return $sce.trustAsHtml(html);
                    }
                };

                 //Month Constraint
                if (!!$scope.config.setCustomMinMonth || $scope.config.setCustomMinMonth === 0) {
                    $scope.fixedMinMonth = new Date().setMonth(new Date().getMonth() - $scope.config.setCustomMinMonth);
                }
                if (!!$scope.config.setCustomMaxMonth || $scope.config.setCustomMaxMonth === 0) {
                    $scope.fixedMaxMonth = new Date().setMonth(new Date().getMonth() + $scope.config.setCustomMaxMonth);
                }
                if (!!$scope.config.setFixedMinMonth) {
                    $scope.fixedMinMonth = $scope.config.setFixedMinMonth;
                }
                if (!!$scope.config.setFixedMaxMonth) {
                    $scope.fixedMaxMonth = $scope.config.setFixedMaxMonth;
                }
                if (!!$scope.config.setMonthRange) {
                    $scope.fixedMinMonth = new Date().setMonth(new Date().getMonth() - $scope.config.setMonthRange);
                    $scope.fixedMaxMonth = new Date();
                }
                $scope.minMonth = $scope.fixedMinMonth ? $scope.fixedMinMonth : '';
                $scope.maxMonth = $scope.fixedMaxMonth ? $scope.fixedMaxMonth : '';
                //Date Constraint
                if (!!$scope.config.setCustomMinDate || $scope.config.setCustomMinDate === 0) {
                    $scope.fixedMinDate = new Date().setDate(new Date().getDate() - $scope.config.setCustomMinDate);
                }

                if (!!$scope.config.setCustomMaxDate || $scope.config.setCustomMaxDate === 0) {
                    $scope.fixedMaxDate = new Date().setDate(new Date().getDate() + $scope.config.setCustomMaxDate);
                }
                if (!!$scope.config.setFixedMinDate) {
                    $scope.fixedMinDate = $scope.config.setFixedMinDate;
                }
                if (!!$scope.config.setFixedMaxDate) {
                    $scope.fixedMaxDate = $scope.config.setFixedMaxDate;
                }
                if (!!$scope.config.setDateRange) {
                    $scope.fixedMinDate = new Date().setDate(new Date().getDate() - $scope.config.setDateRange);
                    $scope.fixedMaxDate = new Date();
                }
                $scope.minDate = $scope.fixedMinDate ? $scope.fixedMinDate : '';
                $scope.maxDate = $scope.fixedMaxDate ? $scope.fixedMaxDate : '';
                $scope.dateRangeOptions = {
                    minDate : $scope.fixedMinDate,
                    maxDate : $scope.fixedMaxDate
                };

                //set default value for dates / months
                if($scope.config.isSetDefault && $scope.config.isSetDefault==true){
                    switch($scope.config.fieldType){
                        case "dateFromTo" :
                            switch($scope.config.defaultValueOption){
                                case "yesterday&lastndays" : 
                                    $scope.data[$scope.dateTo] = new Date().setDate(new Date().getDate() - 1);
                                    $scope.data[$scope.dateFrom] = new Date().setDate(new Date().getDate() - ($scope.config.setLastNdays+1));
                                    break;
                                case "today&yesterday" : 
                                    $scope.data[$scope.dateTo] = new Date().setDate(new Date().getDate());
                                    $scope.data[$scope.dateFrom] = new Date().setDate(new Date().getDate() - 1);
                                    break;
                                case "today&lastndays" : 
                                    $scope.data[$scope.dateTo] = new Date().setDate(new Date().getDate());
                                    $scope.data[$scope.dateFrom] = new Date().setDate(new Date().getDate() - $scope.config.setLastNdays);
                                    break;
                                default : 
                                    break;    
                            }
                            break;
                        case "date" :
                            switch($scope.config.defaultValueOption){
                                case "today" : 
                                    $scope.data[$scope.config.fieldName] = new Date().setDate(new Date().getDate());
                                    break;
                                case "yesterday" : 
                                    $scope.data[$scope.config.fieldName] = new Date().setDate(new Date().getDate() - 1);
                                    break;
                                case "lastndays" : 
                                    $scope.data[$scope.config.fieldName] = new Date().setDate(new Date().getDate() - $scope.config.setLastNdays);
                                    break;
                                default : 
                                    break;    
                            }
                            break;
                        case "onlyMonthFromTo" :
                            switch($scope.config.defaultValueOption){
                                case "currentmonth&lastmonth" : 
                                    $scope.data[$scope.onlyMonthFrom] = new Date().setMonth(new Date().getMonth() - 1);
                                    $scope.data[$scope.onlyMonthTo] = new Date().setMonth(new Date().getMonth());
                                    break;
                                case "current&lastnmonth" : 
                                    $scope.data[$scope.onlyMonthFrom] = new Date().setMonth(new Date().getMonth() - $scope.config.setLastNdays);
                                    $scope.data[$scope.onlyMonthTo] = new Date().setMonth(new Date().getMonth());
                                    break;
                                default : 
                                    break;    
                            }
                            break;
                        case "onlyMonth" :
                            switch($scope.config.defaultValueOption){
                                case "currentmonth" : 
                                    $scope.data[$scope.config.fieldName] = new Date().setMonth(new Date().getMonth());
                                    break;
                                case "lastmonth" : 
                                    $scope.data[$scope.config.fieldName] = new Date().setMonth(new Date().getMonth() - 1);
                                    break;
                                default : 
                                    break;    
                            }
                            break;
                        case "dateNTime" :
                            switch($scope.config.defaultValueOption){
                                case "today" : 
                                    $scope.data[$scope.config.fieldName] = new Date().setDate(new Date().getDate());
                                    break;
                                case "yesterday" : 
                                    $scope.data[$scope.config.fieldName] = new Date().setDate(new Date().getDate() - 1);
                                    break;
                                case "lastndays" : 
                                    $scope.data[$scope.config.fieldName] = new Date().setDate(new Date().getDate() - $scope.config.setLastNdays);
                                    break;
                                default : 
                                    break;    
                            }
                            break;
                        default :
                            break;
                    }
                }
            }
        };
    });
    angular.module('imtecho.directives').directive('replacehtml', function (DummyDataGenerator, $compile) {
        return {
            restrict: 'AE',
            scope: {
                html: "=",
                availableFields: '=',
                data: '=?',
                isTemplateConfigure: '=',
                childTemplate: '='
            },
            link: function (scope, element, attr) {
                var placeholderFields;
                scope.templateData = '<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <strong>&nbsp; New Test Status Report</strong></p><p> &nbsp; &nbsp; &nbsp; &nbsp; <strong> Name </strong> &nbsp;:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;[[Name]]</p><p> &nbsp; &nbsp; &nbsp; &nbsp; <strong> Roof </strong> :&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;[[Roof]]</p><p> &nbsp; &nbsp; <strong> Ceiling </strong>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;[[Ceiling]]</p><p> &nbsp;</p>';
                scope.$watch('html', function (newValue) {
                    if (newValue) {
                        scope.html = newValue;
                        placeholderFields = scope.html.match(/\[\[(.*?)\]\]/g);
                        if (scope.isTemplateConfigure && scope.data && Object.keys(scope.data).length <= 0) {
                            createDummyData();
                        }
                        else if (scope.data) {
                            replaceHtml();
                        }
                    }
                });
                scope.$watch('availableFields', function (newValue) {
                    if (newValue) {
                        scope.availableFields = newValue;
                        if (scope.html) {
                            placeholderFields = scope.html.match(/\[\[(.*?)\]\]/g);
                            if (scope.isTemplateConfigure && !scope.data) {
                                createDummyData();
                            }
                            else if (scope.data) {
                                replaceHtml();
                            }
                        }
                    }
                });
                var createDummyData = function () {
                    if (!scope.data) {
                        scope.data = {};
                    }
                    if (placeholderFields) {
                        _.each(placeholderFields, function (fieldMatched) {
                            var actualField = _.find(scope.availableFields, function (field) {
                                return '[[' + field.displayName + ']]' === fieldMatched;
                            });
                            if (actualField) {
                                if (actualField.fieldType === 'file') {
                                    scope.data.fileNameDto = DummyDataGenerator.generateFieldData(actualField);
                                }
                                else {
                                    scope.data[actualField.fieldName] = DummyDataGenerator.generateFieldData(actualField);
                                }
                            }
                        });
                    }
                    replaceHtml();
                };
                var replaceHtml = function () {
                    var temp = angular.copy(scope.html);
                    if (placeholderFields) {
                        _.each(placeholderFields, function (fieldMatched) {
                            var actualField = _.find(scope.availableFields, function (field) {
                                return '[[' + field.displayName + ']]' === fieldMatched;
                            });
                            if (scope.childTemplate && fieldMatched === '[[' + scope.childTemplate.name + ']]') {
                                if (!scope.isTemplateConfigure) {
                                    temp = temp.replace(fieldMatched, '<span ng-repeat="child in data.childList"><span replacehtml html="childTemplate.configJson.templateConfig" available-fields="availableFields.child" data="child"></span></span>');
                                }
                                else {
                                    temp = temp.replace(fieldMatched, '<span replacehtml html="childTemplate.configJson.templateConfig" available-fields="availableFields.child" is-template-configure="true"  data="data"></span>');
                                }
                            }
                            if (actualField) {
                                temp = temp.replace(fieldMatched, "<span datacomponent name='" + actualField.fieldName + "' type='" + actualField.fieldType + "' data='data'" + "></span>");
                            }
                        });
                    }
                    scope.htmlDisplay = temp;
                    element.html($compile(scope.htmlDisplay)(scope));
                };
            }
        };
    });
    angular.module('imtecho.directives').directive('uiSrefIf', function ($compile) {
        return {
            link: function ($scope, $element, $attrs) {
                var uiSrefVal = $attrs.uiSrefVal;
                var uiSrefIf = $attrs.uiSrefIf;
                var isLink = $scope.$eval(uiSrefIf);
                $element.removeAttr('ui-sref-if');
                $element.removeAttr('ui-sref-val');
                if (isLink) {
                    $element.attr('ui-sref', $scope.$eval(uiSrefVal));
                } else {
                    $element.removeAttr('ui-sref');
                    $element.removeAttr('href');
                }
                $compile($element)($scope);
            }
        };
    });
})(window.angular);
