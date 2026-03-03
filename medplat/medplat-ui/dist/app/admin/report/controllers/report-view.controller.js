(function () {
    function ReportViewController($state, Mask, GeneralUtil, ReportDAO, ReportFieldUtil, $rootScope, APP_CONFIG, toaster,
        AuthenticateService, $timeout, $q, $filter, $uibModal, $http) {
        var rv = this;
        rv.appName = GeneralUtil.getAppName();
        rv.reportObj = {
            configJson: {}
        };
        rv.accessToken = $rootScope.authToken;
        rv.apiPath = APP_CONFIG.apiPath;
        rv.paramMap = {};
        rv.pagingService = {
            offSet: 0,
            limit: 100,
            index: 0,
            allRetrieved: false,
            pagingRetrivalOn: false
        };
        rv.locationLevelSelected;
        rv.searchString = null;
        var fieldConfigObjMap = {};
        var fieldChangeCallback = function (fieldName, value) {
            _.each(rv.reportObj.configJson.containers, function (container, key) {
                var index = 0;
                _.each(container, function (field) {
                    index++;
                    if (field.fieldName === fieldName) {
                        field.value = value;
                        rv.data[field.fieldName] = value;
                        if (field.value === '' || typeof field.value == 'undefined') {
                            delete field.value;
                            delete rv.data[field.fieldName];
                        }
                        field.index = index;
                        field.container = key;
                    }
                    if (field.availableImplicitParameters && field.fieldName != 'tableField' && field.availableImplicitParameters.indexOf(fieldName) >= 0) {
                        delete field.value;
                        delete rv.data[field.fieldName];
                        field.index = index;
                        field.container = key;
                        var paramObj = rv.reportObj.configJson.containers.fieldsContainer.reduce(function (obj, item) {
                            obj[item.fieldName] = rv.data[item.fieldName];
                            return obj;
                        }, {});
                        Mask.show();
                        ReportDAO.retrieveComboDataByUUID(field.queryUUID, paramObj).then(function (res) {
                            _.each(rv.reportObj.configJson.containers[key], function (containerFields) {
                                if (containerFields.fieldName === field.fieldName) {
                                    containerFields.optionsByQuery = res;
                                }
                            });
                        }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                            Mask.hide();
                        });
                    }
                });
            });
            rv.reportObj.configJson.containers = ReportFieldUtil.extractFieldValueFromData(rv.reportObj.configJson.containers, rv.data);
            if (rv.reportObj.configJson.layout.indexOf("WithPagination") >= 0) {
                rv.reportObj.configJson.containers.limit = 100;
                rv.reportObj.configJson.containers.offSet = 0;
            }
        };

        rv.searchFilter = function (reset, isSearch, isDownloadOffline) {
            if (rv.config && isSearch) {
                rv.clearSearch();
            }
            rv.locationHierarchy = "";
            if (isSearch) {
                rv.reportForm.$setSubmitted();
            }
            if (rv.reportForm.$valid) {
                if (reset) {
                    rv.pagingService.index = 0;
                    rv.pagingService.allRetrieved = false;
                    rv.reportObj.configJson.filterDisplay = [];
                    _.each(rv.reportObj.configJson.containers.fieldsContainer, function (filterField) {
                        if (!filterField.isHidden) {
                            filterField.searchField = {};
                            if (rv.data[filterField.fieldName] && filterField.fieldType == 'comboForReport') {
                                var matchedField = _.find(filterField.optionsByQuery, function (option) {
                                    if (option.key === filterField.value) {
                                        return true;
                                    }
                                });
                                if (matchedField) {
                                    filterField.searchField[filterField.fieldName] = matchedField.value;
                                    let object = _.find(rv.reportObj.configJson.filterDisplay, function (filter) {
                                        return filterField.fieldName == filter.fieldName;
                                    });
                                    if (object) {
                                        object.value = matchedField.value;
                                    } else {
                                        rv.reportObj.configJson.filterDisplay.push({
                                            fieldName: filterField.fieldName,
                                            displayName: filterField.displayName,
                                            value: matchedField.value
                                        })
                                    }
                                }
                            } else if (rv.data[filterField.fieldName] && filterField.fieldType == 'multiselectForReport') {
                                var matchedFields = _.filter(filterField.optionsByQuery, function (option) {
                                    if (filterField.value.indexOf(option.key) >= 0) {
                                        return true;
                                    }
                                });
                                filterField.searchField = {};
                                if (matchedFields && matchedFields.length > 0) {
                                    filterField.searchField[filterField.fieldName] = matchedFields.map(field => field.value).join();
                                    let object = _.find(rv.reportObj.configJson.filterDisplay, function (filter) {
                                        return filterField.fieldName == filter.fieldName;
                                    });
                                    if (object) {
                                        object.value = matchedField.value;
                                    } else {
                                        rv.reportObj.configJson.filterDisplay.push({
                                            fieldName: filterField.fieldName,
                                            displayName: filterField.displayName,
                                            value: matchedField.value
                                        });
                                    }
                                }
                            } else if (rv.data[filterField.fieldName] && filterField.fieldType == 'location' && !filterField.isHidden) {
                                if (filterField.selectedLocation.finalSelected.optionSelected) {
                                    rv.reportObj.configJson.locationLevelSelected = filterField.selectedLocation.finalSelected.level;
                                } else if (filterField.selectedLocation.finalSelected.level >= 0) {
                                    rv.reportObj.configJson.locationLevelSelected = filterField.selectedLocation.finalSelected.level - 1;
                                }
                                for (var index = 1; index <= filterField.selectedLocation.finalSelected.level; index++) {
                                    if (filterField.selectedLocation['level' + index]) {
                                        rv.locationHierarchy += " --> " + filterField.selectedLocation['level' + index].name;
                                        var type = $filter('locationName')(filterField.selectedLocation['level' + index].type);
                                        filterField.searchField[type] = filterField.selectedLocation['level' + index].name;
                                    }
                                }
                                let object = _.find(rv.reportObj.configJson.filterDisplay, function (filter) {
                                    return 'location' == filter.fieldName;
                                });
                                if (!!filterField.demographicFilterRequired && filterField.demographicFilterRequired) {
                                    var key = 'demographic_' + filterField.fieldName;
                                    if (rv.reportObj.configJson.filterDisplay.length > 0) {
                                        rv.reportObj.configJson.filterDisplay.map(function (item, fieldindex) {
                                            if (rv.reportObj.configJson.filterDisplay[fieldindex]['fieldName'] === key) {
                                                rv.reportObj.configJson.filterDisplay.splice(fieldindex, 1);
                                            }
                                        })
                                    }
                                    rv.reportObj.configJson.filterDisplay.push({
                                        fieldName: 'demographic_' + filterField.fieldName,
                                        displayName: 'Demographic Location',
                                        value: filterField.selectedLocation.selectedDemographic == 'U' ? 'Urban' : filterField.selectedLocation.selectedDemographic == 'R' ? 'Rural' : 'Both'
                                    });
                                }
                                if (object) {
                                    object.value = rv.locationHierarchy.substring(5);
                                } else {
                                    rv.reportObj.configJson.filterDisplay.push({
                                        fieldName: 'location',
                                        displayName: 'Location',
                                        value: rv.locationHierarchy.substring(5)
                                    })
                                }
                            } else if (rv.data[filterField.fieldName] && filterField.fieldType == 'selectizeForCombo') {
                                filterField.searchField[filterField.fieldName] = filterField.objectValue.value;
                                let object = _.find(rv.reportObj.configJson.filterDisplay, function (filter) {
                                    return filterField.fieldName == filter.fieldName;
                                });
                                if (object) {
                                    object.value = filterField.searchField[filterField.fieldName];
                                } else {
                                    rv.reportObj.configJson.filterDisplay.push({
                                        fieldName: filterField.fieldName,
                                        displayName: filterField.displayName,
                                        value: filterField.searchField[filterField.fieldName]
                                    })
                                }
                            } else if (rv.data[filterField.fieldName] && filterField.fieldType === 'date') {
                                filterField.searchField[filterField.fieldName] = moment(new Date(moment(filterField.value).format("YYYY-MM-DD"))).format("DD/MM/YYYY")
                                let object = _.find(rv.reportObj.configJson.filterDisplay, function (filter) {
                                    return filterField.fieldName == filter.fieldName;
                                });
                                if (object) {
                                    object.value = filterField.searchField[filterField.fieldName]
                                } else {
                                    rv.reportObj.configJson.filterDisplay.push({
                                        fieldName: filterField.fieldName,
                                        displayName: filterField.displayName,
                                        value: filterField.searchField[filterField.fieldName]
                                    })
                                }
                            } else if (rv.data[filterField.fieldName] && filterField.fieldType === 'dateNTime') {
                                filterField.searchField[filterField.fieldName] = $filter('date')(filterField.value, 'dd-MM-yyyy HH:mm');
                                let object = _.find(rv.reportObj.configJson.filterDisplay, function (filter) {
                                    return filterField.fieldName == filter.fieldName;
                                });
                                if (object) {
                                    object.value = filterField.searchField[filterField.fieldName];
                                } else {
                                    rv.reportObj.configJson.filterDisplay.push({
                                        fieldName: filterField.fieldName,
                                        displayName: filterField.displayName,
                                        value: filterField.searchField[filterField.fieldName]
                                    })
                                }
                            } else if (rv.data[filterField.fieldName] && filterField.fieldType === 'onlyMonthFromTo') {
                                if ((rv.data["from_" + filterField.fieldName]) || rv.data["to_" + filterField.fieldName]) {
                                    filterField.searchField["from_" + filterField.fieldName] = moment(new Date(moment(rv.data["from_" + filterField.fieldName]).format("YYYY-MM-DD"))).format("MM/YYYY")
                                    filterField.searchField["to_" + filterField.fieldName] = moment(new Date(moment(rv.data["to_" + filterField.fieldName]).format("YYYY-MM-DD"))).format("MM/YYYY")
                                    let object = _.find(rv.reportObj.configJson.filterDisplay, function (filter) {
                                        return "from_" + filterField.fieldName == filter.fieldName;
                                    });
                                    if (object) {
                                        object.value = moment(new Date(moment(rv.data["from_" + filterField.fieldName]).format("YYYY-MM-DD"))).format("MM/YYYY");
                                    } else {
                                        rv.reportObj.configJson.filterDisplay.push({
                                            fieldName: "from_" + filterField.fieldName,
                                            displayName: "From " + filterField.displayName,
                                            value: moment(new Date(moment(rv.data["from_" + filterField.fieldName]).format("YYYY-MM-DD"))).format("MM/YYYY")
                                        });
                                    }
                                    object = _.find(rv.reportObj.configJson.filterDisplay, function (filter) {
                                        return "to_" + filterField.fieldName == filter.fieldName;
                                    });
                                    if (object) {
                                        object.value = moment(new Date(moment(rv.data["to_" + filterField.fieldName]).format("YYYY-MM-DD"))).format("MM/YYYY");
                                    } else {
                                        rv.reportObj.configJson.filterDisplay.push({
                                            fieldName: "to_" + filterField.fieldName,
                                            displayName: "To " + filterField.displayName,
                                            value: moment(new Date(moment(rv.data["to_" + filterField.fieldName]).format("YYYY-MM-DD"))).format("MM/YYYY")
                                        })
                                    }
                                }
                            } else if (rv.data[filterField.fieldName] && filterField.fieldType === 'dateFromTo') {
                                if ((rv.data["from_" + filterField.fieldName]) || rv.data["to_" + filterField.fieldName]) {
                                    filterField.searchField["from_" + filterField.fieldName] = moment(new Date(moment(rv.data["from_" + filterField.fieldName]).format("YYYY-MM-DD"))).format("DD/MM/YYYY")
                                    filterField.searchField["to_" + filterField.fieldName] = moment(new Date(moment(rv.data["to_" + filterField.fieldName]).format("YYYY-MM-DD"))).format("DD/MM/YYYY")
                                    let object = _.find(rv.reportObj.configJson.filterDisplay, function (filter) {
                                        return "from_" + filterField.fieldName == filter.fieldName;
                                    });
                                    if (object) {
                                        object.value = moment(new Date(moment(rv.data["from_" + filterField.fieldName]).format("YYYY-MM-DD"))).format("DD/MM/YYYY");
                                    } else {
                                        rv.reportObj.configJson.filterDisplay.push({
                                            fieldName: "from_" + filterField.fieldName,
                                            displayName: "From " + filterField.displayName,
                                            value: moment(new Date(moment(rv.data["from_" + filterField.fieldName]).format("YYYY-MM-DD"))).format("DD/MM/YYYY")
                                        });
                                    }
                                    object = _.find(rv.reportObj.configJson.filterDisplay, function (filter) {
                                        return "to_" + filterField.fieldName == filter.fieldName;
                                    });
                                    if (object) {
                                        object.value = moment(new Date(moment(rv.data["to_" + filterField.fieldName]).format("YYYY-MM-DD"))).format("DD/MM/YYYY");
                                    } else {
                                        rv.reportObj.configJson.filterDisplay.push({
                                            fieldName: "to_" + filterField.fieldName,
                                            displayName: "To " + filterField.displayName,
                                            value: moment(new Date(moment(rv.data["to_" + filterField.fieldName]).format("YYYY-MM-DD"))).format("DD/MM/YYYY")
                                        })
                                    }
                                }
                            } else if (rv.data[filterField.fieldName] && filterField.fieldType === 'dateRangePicker') {
                                if ((rv.data[filterField.fieldName].startDate || rv.data[filterField.fieldName].endDate)) {
                                    filterField.searchField["from_" + filterField.fieldName] = rv.data[filterField.fieldName].startDate;
                                    filterField.searchField["to_" + filterField.fieldName] = rv.data[filterField.fieldName].endDate;
                                    let object = _.find(rv.reportObj.configJson.filterDisplay, function (filter) {
                                        return "from_" + filterField.fieldName == filter.fieldName;
                                    });
                                    if (object) {
                                        object.value = moment(new Date(moment(rv.data["from_" + filterField.fieldName]).format("YYYY-MM-DD"))).format("DD/MM/YYYY");
                                    } else {
                                        rv.reportObj.configJson.filterDisplay.push({
                                            fieldName: "from_" + filterField.fieldName,
                                            displayName: "From " + filterField.displayName,
                                            value: moment(new Date(moment(rv.data["from_" + filterField.fieldName]).format("YYYY-MM-DD"))).format("DD/MM/YYYY")
                                        });
                                    }
                                    object = _.find(rv.reportObj.configJson.filterDisplay, function (filter) {
                                        return "to_" + filterField.fieldName == filter.fieldName;
                                    });
                                    if (object) {
                                        object.value = moment(new Date(moment(rv.data["to_" + filterField.fieldName]).format("YYYY-MM-DD"))).format("DD/MM/YYYY");
                                    } else {
                                        rv.reportObj.configJson.filterDisplay.push({
                                            fieldName: "to_" + filterField.fieldName,
                                            displayName: "To " + filterField.displayName,
                                            value: moment(new Date(moment(rv.data["to_" + filterField.fieldName]).format("YYYY-MM-DD"))).format("DD/MM/YYYY")
                                        })
                                    }
                                }
                            } else {
                                if (rv.data[filterField.fieldName] && filterField.fieldType === 'onlyMonth') {
                                    filterField.searchField[filterField.fieldName] = moment(filterField.value).format("MM/YYYY");
                                } else {
                                    filterField.searchField[filterField.fieldName] = filterField.value;
                                }
                                let object = _.find(rv.reportObj.configJson.filterDisplay, function (filter) {
                                    return filterField.fieldName == filter.fieldName;
                                });
                                if (object) {
                                    if (rv.data[filterField.fieldName] && filterField.fieldType === 'onlyMonth') {
                                        object.value = moment(filterField.searchField[filterField.fieldName]).format("MM/YYYY");
                                    } else {
                                        object.value = filterField.searchField[filterField.fieldName];
                                    }
                                } else {
                                    rv.reportObj.configJson.filterDisplay.push({
                                        fieldName: filterField.fieldName,
                                        displayName: filterField.displayName,
                                        value: filterField.searchField[filterField.fieldName]
                                    })
                                }
                            }
                        }
                    });
                }
                _.each(rv.reportObj.configJson.containers, function (container) {
                    _.each(container, function (field) {
                        if (field.value === '' || typeof field.value === 'undefined') {
                            delete field.value;
                        }
                    });
                });
                rv.reportObj.configJson.containers = ReportFieldUtil.extractFieldValueFromData(rv.reportObj.configJson.containers, rv.data);
                if (!!isDownloadOffline) {
                    downloadOffline();
                } else {
                    retrieveTableDataByQueryId();
                }
            }
            rv.locationHierarchy = rv.locationHierarchy.substring(5);
        };

        var extractImplicitParams = function (queryParams) {
            var availableImplicitParameters = [];
            if (queryParams) {
                queryParams = queryParams.split(',');
                _.each(rv.reportObj.configJson.containers, function (container) {
                    if (container) {
                        _.each(container, function (field) {
                            if (queryParams.includes(field.fieldName)) {
                                availableImplicitParameters.push(field.fieldName);
                            }
                        });
                    }
                });
            }
            return availableImplicitParameters;
        };

        var extractQueriesFromFields = function () {
            angular.forEach(rv.reportObj.configJson.containers, function (containerFields) {
                if (containerFields) {
                    angular.forEach(containerFields, function (field) {
                        fieldConfigObjMap[field.fieldName] = field;
                        if (field.fieldType === 'comboForReport' || field.fieldType === 'multiselectForReport') {
                            if (!field.isQuery && field.availableOptions) {
                                field.optionsByQuery = convertTagToOptionList(field);
                            }
                        }
                        field.availableImplicitParameters = extractImplicitParams(field.queryParams);
                        field.fieldChangeCallback = fieldChangeCallback;
                        field.fieldSelectizeCallback = fieldSelectizeCallback;
                        field.setSelectizeObject = setSelectizeObject;
                    });
                }
            });
            rv.data = ReportFieldUtil.extractDataFromField(rv.reportObj.configJson.containers, rv.data);
            rv.data.fromReport = true;
            rv.data.tableData = rv.reportObj.configJson.containers.tableContainer[0].tableData;
            rv.data.tableToDisplay = ReportFieldUtil.generateTableData(rv.reportObj.configJson.containers.tableContainer[0].tableData, rv.reportObj.configJson.containers);
            rv.data.filteredData = angular.copy(rv.data.tableToDisplay);
            if (rv.data.tableToDisplay.body.length > 0) {
                $timeout(function () {
                    $(".header-fixed").tableHeadFixer();
                });
            }
        };

        var init = function () {
            rv.currentDate = moment().format("DD/MM/YYYY (hh:mm)");
            rv.configList = [];
            rv.pageval = {};
            AuthenticateService.getLoggedInUser().then(function (user) {
                rv.user = user.data;
                $http.defaults.headers.common.title = "Dynamic Report";
                if ($state.params.id && $state.params.id !== '') {
                    Mask.show();
                    ReportDAO.getDynamicDetail($state.params.id, $state.params.type).then(function (res) {
                        rv.configLocationLevel = res.configJson.locationLevel;
                        rv.reportObj = res;
                        $rootScope.currentState.title = res.name;
                        $http.defaults.headers.common.title = res.name;
                        extractQueriesFromFields();
                        if ($state.params.queryParams) {
                            var queryParams = {};
                            _.map($state.params.queryParams, function (key, param) {
                                if (key != 'null') {
                                    queryParams[param] = key
                                }
                            })
                            var paramsKeys = _.keys(queryParams);
                            if (!rv.reportObj.configJson.filterDisplay) {
                                rv.reportObj.configJson.filterDisplay = [];
                            }
                            let fieldList = [],
                                promiseList = [];
                            _.each(rv.reportObj.configJson.containers.fieldsContainer, function (field) {

                                _.each(paramsKeys, function (key) {

                                    if (field.fieldName === key) {
                                        field.value = queryParams[key];
                                        fieldList.push(field);
                                        // if(field.fieldType='date'){
                                        //     field.value=new Date(queryParams[key]);
                                        // }
                                        if (field.queryUUIDForParam) {
                                            // Mask.show()
                                            promiseList.push(ReportDAO.retrieveDataByQueryUUID(field.queryUUIDForParam, queryParams));
                                            // ReportDAO.retrieveDataByQueryId(field.queryIdForParam, queryParams).then(function (res) {
                                            //     if (res && res.length > 0 && res[0][field.fieldName]) {
                                            //         rv.reportObj.configJson.filterDisplay.push({
                                            //             displayName: field.displayName,
                                            //             fieldName: field.fieldName, value: res[0][field.fieldName]
                                            //         });

                                            //     } else {
                                            //         rv.reportObj.configJson.filterDisplay.push({
                                            //             displayName: field.displayName,
                                            //             fieldName: field.fieldName, value: ''
                                            //         });
                                            //     }

                                            // }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                                            //     Mask.hide();
                                            // });
                                            // } else {
                                            //     rv.reportObj.configJson.filterDisplay.push({
                                            //         displayName: field.displayName,
                                            //         fieldName: field.fieldName, value: queryParams[key]
                                            //     });
                                            // }
                                        }
                                    } else if ("from_" + field.fieldName == key) {
                                        if (!field.value) {
                                            field.value = {};
                                        }
                                        field.value.startDate = moment(queryParams[key]);
                                        fieldList.push(field);

                                        if (field.queryUUIDForParam) {
                                            // Mask.show()
                                            promiseList.push(ReportDAO.retrieveDataByQueryUUID(field.queryUUIDForParam, queryParams));
                                            // ReportDAO.retrieveDataByQueryId(field.queryIdForParam, queryParams).then(function (res) {
                                            //         if (res && res.length > 0 && res[0][field.fieldName]) {
                                            //             rv.reportObj.configJson.filterDisplay.push({
                                            //                 displayName: field.displayName,
                                            //                 fieldName: field.fieldName, value: res[0][field.fieldName]
                                            //             });

                                            //         } else {
                                            //             rv.reportObj.configJson.filterDisplay.push({
                                            //                 displayName: field.displayName,
                                            //                 fieldName: field.fieldName, value: ''
                                            //             });
                                            //         }

                                            //     }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                                            //         Mask.hide();
                                            //     });
                                            // } else {
                                            //     rv.reportObj.configJson.filterDisplay.push({
                                            //         displayName: field.displayName,
                                            //         fieldName: field.fieldName, value: moment(queryParams[key]).format("DD/MM/YYYY")
                                            //     });
                                            // }
                                        }
                                    } else if ("to_" + field.fieldName == key) {
                                        if (!field.value) {
                                            field.value = {};
                                        }
                                        fieldList.push(field);

                                        field.value.endDate = queryParams[key];
                                        if (field.queryUUIDForParam) {
                                            promiseList.push(ReportDAO.retrieveDataByQueryUUID(field.queryUUIDForParam, queryParams));
                                        }
                                        //     Mask.show();
                                        //     promiseList.push(ReportDAO.retrieveDataByQueryId(field.queryIdForParam, queryParams));
                                        //     ReportDAO.retrieveDataByQueryId(field.queryIdForParam, queryParams).then(function (res) {
                                        //         if (res && res.length > 0 && res[0][field.fieldName]) {
                                        //             rv.reportObj.configJson.filterDisplay.push({
                                        //                 displayName: field.displayName,
                                        //                 fieldName: field.fieldName, value: res[0][field.fieldName]
                                        //             });

                                        //         } else {
                                        //             rv.reportObj.configJson.filterDisplay.push({
                                        //                 displayName: field.displayName,
                                        //                 fieldName: field.fieldName, value: ''
                                        //             });
                                        //         }

                                        //     }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                                        //         Mask.hide();
                                        //     });
                                        // } else {
                                        //     rv.reportObj.configJson.filterDisplay.push({fieldName: "To " + field.displayName, value: moment(queryParams[key]).format("DD/MM/YYYY")});
                                        // }
                                    }
                                });
                            });
                            $q.all(promiseList).then(function (resList) {
                                _.each(fieldList, function (field) {
                                    let response = _.find(resList, function (resObj) {
                                        if (resObj[0]) {
                                            return resObj[0][field.fieldName] != null
                                        }
                                        return false;
                                    });
                                    if (field.queryIdForParam) {
                                        if (response && response.length > 0 && response[0][field.fieldName]) {
                                            rv.reportObj.configJson.filterDisplay.push({
                                                displayName: field.displayName,
                                                fieldName: field.fieldName,
                                                value: response[0][field.fieldName]
                                            });
                                        } else {
                                            rv.reportObj.configJson.filterDisplay.push({
                                                displayName: field.displayName,
                                                fieldName: field.fieldName,
                                                value: ''
                                            });
                                        }
                                    } else {
                                        rv.reportObj.configJson.filterDisplay.push({
                                            displayName: field.displayName,
                                            fieldName: field.fieldName,
                                            value: queryParams[field.fieldName]
                                        });
                                    }
                                });
                            })
                            rv.reportObj.configJson.containers.fieldsContainer.isShown = _.filter(rv.reportObj.configJson.containers.fieldsContainer, function (field) {
                                return !field.isHidden;
                            }).length > 0;
                            rv.searchConfig = angular.copy(rv.reportObj.configJson);
                            retrieveTableDataByQueryId(null);
                        } else {
                            rv.searchConfig = angular.copy(rv.reportObj.configJson);
                            retrieveTableData(null, null);
                            Mask.hide();
                        }
                    }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                        Mask.hide();
                    });
                }
            });
        };

        var convertTagToOptionList = function (field) {
            var arrayOfOptions = field.availableOptions.split('|');
            if (!arrayOfOptions) {
                arrayOfOptions = field.availableOptions;
            }
            var optionList = [];
            _.each(arrayOfOptions, function (option) {
                let object = {
                    key: option,
                    value: option
                };
                optionList.push(object);
            });
            return optionList;
        };

        rv.printReport = function () {
            /* if (rv.reportObj.configJson.layout.indexOf("WithPagination") >= 0) {
                rv.data.footer = "Generated by " + rv.user.userName + " at " + new Date().toLocaleString();
                var modalInstanceProperties = {
                    controllerAs: 'prf',
                    controller: ['$scope', 'type', '$uibModalInstance', function($scope, type, $uibModalInstance) {
                        var prf = this;
                        prf.print = {
                            rangeTo: 5000,
                            rangeFrom: 0
                        };
                        prf.ok = function() {
                            prf.form.$setSubmitted();
                            if (prf.form.$valid && (prf.print.rangeTo - prf.print.rangeFrom) > 0 && (prf.print.rangeTo - prf.print.rangeFrom) <= 5000) {
                                $uibModalInstance.close({ rangeTo: prf.print.rangeTo, rangeFrom: prf.print.rangeFrom });
                            } else if (!prf.print || !(prf.print.rangeTo - prf.print.rangeFrom) > 0) {
                                toaster.pop('error', "Enter valid range")
                            } else if (!prf.print || (prf.print.rangeTo - prf.print.rangeFrom) > 5000) {
                                toaster.pop('error', "The difference between from and to rows cannot be more than 5000")
                            }
                        };
                        prf.cancel = function() {
                            $uibModalInstance.dismiss('cancel');
                        };
                    }],
                    windowClass: 'cst-modal',
                    backdrop: 'static',
                    size: 'lg',
                    templateUrl: 'app/admin/report/views/print-range-form.modal.html',
                    resolve: {
                        type: function() {
                            return {};
                        }
                    }
                };
                var modalInstance = $uibModal.open(modalInstanceProperties);
                modalInstance.result.then(function(range) {
                    rv.paramMap.limit = (range.rangeTo - range.rangeFrom);
                    rv.paramMap.offset = range.rangeFrom;
                    Mask.show();
                    ReportDAO.retrieveDataByQueryId(rv.reportObj.configJson.containers.tableContainer[0].queryId, rv.paramMap).then(function(res) {
                        rv.data.printableTable = ReportFieldUtil.generateTableData(res, rv.reportObj.configJson.containers)
                        printReport();
                    }).finally(function() {
                        Mask.hide();
                    });
                }, function() {});
            } else {
                printReport();
            } */

            /* var modalInstanceProperties = {
                controllerAs: 'downloadPdf',
                controller: ['$scope', '$uibModalInstance', function ($scope, $uibModalInstance) {
                    var downloadPdf = this;
                    downloadPdf.pdfOptions = {};
                    downloadPdf.referenceColumns = [];
                    downloadPdf.tableColumns = [];
                    downloadPdf.pdfOptions.numberOfColumnPerPage = 10;

                    const _init = () => {
                        if (!rv.reportObj.configJson.containers.tableFieldContainer) {
                            rv.reportObj.configJson.containers.tableFieldContainer = [];
                        }
                        let tableFieldContainer = rv.reportObj.configJson.containers.tableFieldContainer;
                        if (rv.data.tableData && rv.data.tableData.length > 0) {
                            downloadPdf.tableColumns = _.keys(rv.data.tableData[0]);
                            _.each(downloadPdf.tableColumns, function (column) {
                                var headPushed = false;
                                _.each(tableFieldContainer, function (tableField) {
                                    if (column == tableField.fieldName && tableField.isReferenceColumn) {
                                        downloadPdf.referenceColumns.push(tableField.fieldName);
                                        headPushed = true
                                    }
                                });
                            });
                        }
                        downloadPdf.pdfOptions.numberOfColumnPerPage = downloadPdf.tableColumns.length > 10 ? 10 : downloadPdf.tableColumns.length;
                    };

                    downloadPdf.ok = () => {
                        downloadPdf.form.$setSubmitted();
                        if (downloadPdf.form.$valid) {
                            $uibModalInstance.close({ pdfOptions: downloadPdf.pdfOptions, referenceColumns: downloadPdf.referenceColumns });
                        }
                    };

                    downloadPdf.cancel = () => {
                        $uibModalInstance.dismiss('cancel');
                    };

                    _init();
                }],
                windowClass: 'cst-modal',
                backdrop: 'static',
                size: 'lg',
                templateUrl: 'app/admin/report/views/download-report-pdf.modal.html',
                resolve: {}
            };
            var modalInstance = $uibModal.open(modalInstanceProperties);
            modalInstance.result.then(function ({ pdfOptions, referenceColumns }) {

            }, function () { }); */

            let referenceColumns = [];
            if (!rv.reportObj.configJson.containers.tableFieldContainer) {
                rv.reportObj.configJson.containers.tableFieldContainer = [];
            }
            let tableFieldContainer = rv.reportObj.configJson.containers.tableFieldContainer;
            let tableColumns = [];
            if (rv.data.tableData && rv.data.tableData.length > 0) {
                tableColumns = _.keys(rv.data.tableData[0]);
                _.each(tableColumns, function (column) {
                    _.each(tableFieldContainer, function (tableField) {
                        if (column == tableField.fieldName && tableField.isReferenceColumn) {
                            referenceColumns.push(tableField.fieldName);
                        }
                    });
                });
            }
            let numberOfColumnPerPage = null;
            if (rv.reportObj.configJson.numberOfColumnPerPage) {
                numberOfColumnPerPage = tableColumns.length < Number(rv.reportObj.configJson.numberOfColumnPerPage) ? tableColumns.length : Number(rv.reportObj.configJson.numberOfColumnPerPage);
            }


            if (rv.reportObj.configJson.layout.indexOf("WithPagination") >= 0) {
                var paramMap = rv.reportObj.configJson.containers.fieldsContainer.reduce(function (obj, item) {
                    obj[item.fieldName] = rv.data[item.fieldName] == undefined ? null : rv.data[item.fieldName];
                    return obj;
                }, {});
                paramMap['limit_offset'] = 'limit null offset 0';
                rv.paramMap['limit_offset'] = 'limit null offset 0';
            }

            var reportExcelDto = {
                filterObj: rv.reportObj.configJson.filterDisplay,
                paramObj: rv.paramMap,
                reportName: $rootScope.currentState.title,
                isLandscape: rv.reportObj.configJson.isLandscape || false,
                numberOfColumnPerPage: numberOfColumnPerPage,
                numberOfRecordsPerPage: rv.reportObj.configJson.numberOfRecordsPerPage ? Number(rv.reportObj.configJson.numberOfRecordsPerPage) : null,
                referenceColumns
            }
            Mask.show();
            ReportDAO.downloadPdf(rv.reportObj.configJson.containers.tableContainer[0].queryUUID, reportExcelDto).then(function (res) {
                if (res.data !== null && navigator.msSaveBlob) {
                    return navigator.msSaveBlob(new Blob([res.data], { type: '' }));
                }
                var a = $("<a style='display: none;'/>");
                var url = window.URL.createObjectURL(new Blob([res.data], { type: 'application/pdf' }));
                a.attr("href", url);
                a.attr("download", $rootScope.currentState.title + "_" + new Date().getTime() + ".pdf");
                $("body").append(a);
                a[0].click();
                window.URL.revokeObjectURL(url);
                a.remove();
            }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                Mask.hide();
            });
        };

        rv.saveExcel = function () {
            if (rv.reportObj.configJson.layout.indexOf("WithPagination") >= 0) {
                var paramMap = rv.reportObj.configJson.containers.fieldsContainer.reduce(function (obj, item) {
                    obj[item.fieldName] = rv.data[item.fieldName] == undefined ? null : rv.data[item.fieldName];
                    return obj;
                }, {});
                paramMap['limit_offset'] = 'limit null offset 0';
                rv.paramMap['limit_offset'] = 'limit null offset 0';
            }
            var reportExcelDto = {
                filterObj: rv.reportObj.configJson.filterDisplay,
                paramObj: rv.paramMap,
                reportName: $rootScope.currentState.title
            }
            Mask.show();
            ReportDAO.downloadExcel(rv.reportObj.configJson.containers.tableContainer[0].queryUUID, reportExcelDto).then(function (res) {
                if (res.data !== null && navigator.msSaveBlob) {
                    return navigator.msSaveBlob(new Blob([res.data], { type: '' }));
                }
                var a = $("<a style='display: none;'/>");
                var url = window.URL.createObjectURL(new Blob([res.data], { type: 'application/vnd.ms-excel' }));
                a.attr("href", url);
                a.attr("download", $rootScope.currentState.title + "_" + new Date().getTime() + ".xlsx");
                $("body").append(a);
                a[0].click();
                window.URL.revokeObjectURL(url);
                a.remove();
            }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                Mask.hide();
            });

        };

        /* const processAndDownloadExcel = function (data) {
            var mystyle = {
                headers: true,
                column: { style: { Font: { Bold: "1" } } }
            };
            var month = new Date().getMonth() + 1;
            var reportName = rv.reportObj.name + '-' + new Date().getDate() + '/' + month + ' ' + new Date().getHours() + ':' + new Date().getMinutes();
            var dataCopy = [];
            if (_.keys(data[0]).find(col => col.includes("hidden_"))) {
                _.each(data, function (row, col) {
                    var rowObject = {};
                    var actualKeys = _.reject(_.keys(row), function (key) {
                        return key.includes('hidden_');
                    });
                    _.each(actualKeys, function (key, index) {
                        if (key.startsWith("percent_col")) {
                            rowObject["%" + actualKeys[index - 1]] = row[key];
                        } else
                            rowObject[key] = row[key];
                    });
                    dataCopy.push(rowObject);
                });
            } else if (_.keys(data[0]).find(col => col.startsWith("percent_col"))) {
                _.each(data, function (row, index) {
                    var rowObject = {};
                    var keyList = _.keys(row);
                    _.each(keyList, function (key, index) {
                        if (key.startsWith("percent_col")) {
                            rowObject["% " + keyList[index - 1]] = row[key];
                        } else
                            rowObject[key] = row[key];
                    });
                    dataCopy.push(rowObject);
                });
            } else
                dataCopy = data;
            dataCopy = JSON.parse(JSON.stringify(dataCopy));
            alasql('SELECT * INTO XLSX("' + reportName + '",?) FROM ?', [mystyle, dataCopy]);
        }; */

        var retrieveTableData = function (retrieveAll, onlyTableData, isQueryParams) {
            if (rv.reportObj.configJson.layout.indexOf("WithPagination") >= 0 && !retrieveAll) {
                if (!rv.pagingService.pagingRetrivalOn && !rv.pagingService.allRetrieved) {
                    rv.pagingService.pagingRetrivalOn = true;
                    setOffsetLimit();
                    rv.reportObj.configJson.containers.limit = rv.pagingService.limit;
                    rv.reportObj.configJson.containers.offset = rv.pagingService.offset;
                    rv.reportObj.configJson.containers.searchstring = rv.searchString ? rv.searchString : true;
                    Mask.show();
                    var reportObjCopy = angular.copy(rv.reportObj)
                    _.each(reportObjCopy.configJson.containers.fieldsContainer, function (filterField) {
                        if (!filterField.searchField) {
                            delete filterField.value;
                        }
                    });
                    return ReportDAO.getDymaicReport(reportObjCopy).then(function (resForData) {
                        if (resForData.configJson.containers.tableContainer[0].tableData.length === 0) {
                            rv.pagingService.allRetrieved = true;
                            if (rv.pagingService.index === 1) {
                                if (onlyTableData) {
                                    rv.data.tableData = resForData.configJson.containers.tableContainer[0].tableData;
                                    rv.data.tableToDisplay = ReportFieldUtil.generateTableData(resForData.configJson.containers.tableContainer[0].tableData, rv.reportObj.configJson.containers)
                                    rv.data.filteredData = angular.copy(rv.data.tableToDisplay);
                                } else {
                                    rv.reportObj = resForData;
                                    extractQueriesFromFields();
                                }
                            }
                        } else {
                            rv.pagingService.allRetrieved = false;
                            if (rv.pagingService.index > 1) {
                                rv.reportObj.configJson.containers.tableContainer[0].tableData = rv.reportObj.configJson.containers.tableContainer[0].tableData.concat(resForData.configJson.containers.tableContainer[0].tableData);
                            } else {
                                if (onlyTableData) {
                                    rv.data.tableData = resForData.configJson.containers.tableContainer[0].tableData;
                                    rv.data.tableToDisplay = ReportFieldUtil.generateTableData(resForData.configJson.containers.tableContainer[0].tableData, rv.reportObj.configJson.containers)
                                    rv.data.filteredData = angular.copy(rv.data.tableToDisplay);
                                } else {
                                    rv.reportObj = resForData;
                                    extractQueriesFromFields();
                                }
                            }
                        }
                        if (isQueryParams) {
                            retrieveTableDataByQueryId();
                        }
                    }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                        rv.pagingService.pagingRetrivalOn = false;
                        rv.pageLoaded = true;
                        Mask.hide();
                    });
                }
            } else {
                delete rv.reportObj.configJson.containers.limit;
                delete rv.reportObj.configJson.containers.offset;
                Mask.show();
                return ReportDAO.getDymaicReport(angular.copy(rv.reportObj)).then(function (resForData) {
                    if (onlyTableData) {
                        rv.data.tableData = resForData.configJson.containers.tableContainer[0].tableData;
                        rv.data.tableToDisplay = ReportFieldUtil.generateTableData(resForData.configJson.containers.tableContainer[0].tableData, rv.reportObj.configJson.containers)
                        rv.data.filteredData = angular.copy(rv.data.tableToDisplay);
                    } else {
                        rv.reportObj = resForData;
                        extractQueriesFromFields();
                    }
                    rv.pagingService.allRetrieved = true;
                    if (isQueryParams) {
                        retrieveTableDataByQueryId();
                    }
                }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                    rv.pageLoaded = true;
                    Mask.hide();
                });
            }
        };

        var setOffsetLimit = function () {
            rv.pagingService.limit = 100;
            rv.pagingService.offset = rv.pagingService.index * 100;
            rv.pagingService.index = rv.pagingService.index + 1;
        };

        /* const replaceParameters = function (query, queryParam) {
            var params = query.match(/#(.*?)#/g);
            _.each(params, function (param) {
                _.each(rv.reportObj.configJson.containers.fieldsContainer, function (field) {
                    if ('#' + field.fieldName + '#' === param) {
                        if (field.value) {
                            query = query.replace(new RegExp(param), field.value);
                        } else {
                            query = query.replace(new RegExp(param), null);
                        }
                    }
                });
            });
            query = query.replace(new RegExp('#query#'), queryParam);
            return query;
        }; */

        var fieldSelectizeCallback = function (fieldName, query) {
            var field = _.find(rv.reportObj.configJson.containers.fieldsContainer, function (fieldsContainer) {
                return fieldsContainer.fieldName === fieldName;
            });
            let paramMap = {
                query: query
            };
            return ReportDAO.retrieveComboDataByUUID(field.queryUUID, paramMap);
        };

        var setSelectizeObject = function (config, options, value) {
            config.objectValue = _.find(options, function (option, key) {
                return key == value;
            });
        };

        var downloadOffline = function () {
            var paramMap = rv.reportObj.configJson.containers.fieldsContainer.reduce(function (obj, item) {
                obj[item.fieldName] = item.value;
                return obj;
            }, {});
            rv.reportObj.configJson.containers.fieldsContainer.forEach(function (field) {
                if (field.fieldType === 'onlyMonthFromTo' || field.fieldType === 'dateFromTo') {
                    if (rv.data["from_" + field.fieldName] != null) {
                        paramMap["from_" + field.fieldName] = moment(new Date(moment(rv.data["from_" + field.fieldName]).format("YYYY-MM-DD"))).format("MM/DD/YYYY");
                    }
                    if (rv.data["to_" + field.fieldName] != null) {
                        paramMap["to_" + field.fieldName] = moment(new Date(moment(rv.data["to_" + field.fieldName]).format("YYYY-MM-DD"))).format("MM/DD/YYYY");
                    }
                    if (paramMap[field.fieldName] != null) {
                        delete paramMap[field.fieldName];
                    }
                }
                if (field.fieldType === 'dateRangePicker') {
                    delete paramMap[field.fieldName];
                    paramMap["from_" + field.fieldName] = moment(new Date(moment(rv.data[field.fieldName].startDate).format("YYYY-MM-DD"))).format("MM/DD/YYYY");
                    paramMap["to_" + field.fieldName] = moment(new Date(moment(rv.data[field.fieldName].endDate).format("YYYY-MM-DD"))).format("MM/DD/YYYY");
                }
                if (field.fieldType === 'location' && !!field.demographicFilterRequired && field.demographicFilterRequired) {
                    paramMap['demographic_' + field.fieldName] = field.selectedLocation.selectedDemographic;
                }
            });

            rv.paramMap = paramMap;
            let referenceColumns = [];
            if (!rv.reportObj.configJson.containers.tableFieldContainer) {
                rv.reportObj.configJson.containers.tableFieldContainer = [];
            }
            let tableFieldContainer = rv.reportObj.configJson.containers.tableFieldContainer;
            let tableColumns = [];
            if (rv.data.tableData && rv.data.tableData.length > 0) {
                tableColumns = _.keys(rv.data.tableData[0]);
                _.each(tableColumns, function (column) {
                    var headPushed = false;
                    _.each(tableFieldContainer, function (tableField) {
                        if (column == tableField.fieldName && tableField.isReferenceColumn) {
                            referenceColumns.push(tableField.fieldName);
                            headPushed = true
                        }
                    });
                });
            }
            let numberOfColumnPerPage = null;
            if (rv.reportObj.configJson.numberOfColumnPerPage) {
                numberOfColumnPerPage = tableColumns.length < Number(rv.reportObj.configJson.numberOfColumnPerPage) ? tableColumns.length : Number(rv.reportObj.configJson.numberOfColumnPerPage);
            }

            var reportExcelDto = {
                filterObj: rv.reportObj.configJson.filterDisplay,
                paramObj: rv.paramMap,
                reportName: $rootScope.currentState.title,
                isLandscape: rv.reportObj.configJson.isLandscape || false,
                numberOfColumnPerPage: numberOfColumnPerPage,
                numberOfRecordsPerPage: rv.reportObj.configJson.numberOfRecordsPerPage ? Number(rv.reportObj.configJson.numberOfRecordsPerPage) : null,
                referenceColumns
            }
            var modalInstance = $uibModal.open({
                templateUrl: 'app/admin/report/views/file-type.modal.html',
                windowClass: 'cst-modal',
                backdrop: 'static',
                size: 'lg',
                controllerAs: 'fileTypeCtrl',
                controller: function ($uibModalInstance) {
                    let fileTypeCtrl = this;

                    fileTypeCtrl.submit = () => {
                        if (!!fileTypeCtrl.fileTypeForm.$valid) {
                            Mask.show();
                            reportExcelDto.fileType = fileTypeCtrl.fileType;
                            ReportDAO.downloadOffline(rv.reportObj.configJson.containers.tableContainer[0].queryId, reportExcelDto).then(function (res) {
                                toaster.pop('success', "Report offline requested sucessfully.");
                                $uibModalInstance.close();
                            }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                                Mask.hide();
                            });
                        }
                    }

                    fileTypeCtrl.cancel = () => {
                        $uibModalInstance.dismiss('cancel');
                        Mask.hide();
                    }
                }
            });
            modalInstance.result.then(function () {
                let url = $state.href('techo.manage.reportoffline');
                sessionStorage.setItem('linkClick', 'true');
                window.open(url, '_blank');
            });
        };

        var retrieveTableDataByQueryId = function (retrieveAll) {
            var paramMap = rv.reportObj.configJson.containers.fieldsContainer.reduce(function (obj, item) {
                obj[item.fieldName] = item.value;
                return obj;
            }, {});
            rv.reportObj.configJson.containers.fieldsContainer.forEach(function (field) {
                if (field.fieldType === 'onlyMonthFromTo' || field.fieldType === 'dateFromTo') {
                    if (rv.data["from_" + field.fieldName] != null) {
                        paramMap["from_" + field.fieldName] = moment(new Date(moment(rv.data["from_" + field.fieldName]).format("YYYY-MM-DD"))).format("MM/DD/YYYY");
                    }
                    if (rv.data["to_" + field.fieldName] != null) {
                        paramMap["to_" + field.fieldName] = moment(new Date(moment(rv.data["to_" + field.fieldName]).format("YYYY-MM-DD"))).format("MM/DD/YYYY");
                    }
                    if (paramMap[field.fieldName] != null) {
                        delete paramMap[field.fieldName];
                    }
                }
                if (field.fieldType === 'dateRangePicker') {
                    delete paramMap[field.fieldName];
                    paramMap["from_" + field.fieldName] = moment(new Date(moment(rv.data[field.fieldName].startDate).format("YYYY-MM-DD"))).format("MM/DD/YYYY");
                    paramMap["to_" + field.fieldName] = moment(new Date(moment(rv.data[field.fieldName].endDate).format("YYYY-MM-DD"))).format("MM/DD/YYYY");
                }
                if (field.fieldType === 'location' && !!field.demographicFilterRequired && field.demographicFilterRequired) {
                    paramMap['demographic_' + field.fieldName] = field.selectedLocation.selectedDemographic;
                }
            });

            rv.paramMap = paramMap;
            if (rv.reportObj.configJson.layout.indexOf("WithPagination") >= 0 && !retrieveAll) {
                if (!rv.pagingService.pagingRetrivalOn && !rv.pagingService.allRetrieved) {
                    rv.pagingService.pagingRetrivalOn = true;
                    setOffsetLimit();
                    rv.reportObj.configJson.containers.limit = rv.pagingService.limit;
                    rv.reportObj.configJson.containers.offset = rv.pagingService.offset;
                    paramMap["limit"] = rv.reportObj.configJson.containers.limit;
                    paramMap["offset"] = rv.reportObj.configJson.containers.offset;
                    Mask.show();
                    var reportObjCopy = angular.copy(rv.reportObj)
                    _.each(reportObjCopy.configJson.containers.fieldsContainer, function (filterField) {
                        if (!filterField.searchField) {
                            delete filterField.value;
                        }
                    });
                    paramMap["searchstring"] = rv.searchString ? rv.searchString : true;
                    return ReportDAO.retrieveDataByQueryUUID(rv.reportObj.configJson.containers.tableContainer[0].queryUUID, paramMap).then(function (resForData) {
                        if (resForData.length === 0 || resForData.length < rv.pagingService.limit) {
                            rv.pagingService.allRetrieved = true;
                            if (rv.pagingService.index === 1) {
                                rv.data.tableData = resForData;
                                rv.data.tableToDisplay = ReportFieldUtil.generateTableData(resForData, rv.reportObj.configJson.containers)
                                rv.data.filteredData = angular.copy(rv.data.tableToDisplay);
                            } else {
                                rv.data.tableData = rv.data.tableData.concat(resForData);
                                rv.data.tableToDisplay = ReportFieldUtil.generateTableData(rv.data.tableData, rv.reportObj.configJson.containers)
                                rv.data.filteredData = angular.copy(rv.data.tableToDisplay);
                            }
                        } else {
                            rv.pagingService.allRetrieved = false;
                            if (rv.pagingService.index > 1) {
                                rv.data.tableData = rv.data.tableData.concat(resForData);
                                rv.data.tableToDisplay = ReportFieldUtil.generateTableData(rv.data.tableData, rv.reportObj.configJson.containers);
                                rv.data.filteredData = angular.copy(rv.data.tableToDisplay);
                            } else {
                                rv.data.tableData = resForData;
                                rv.data.tableToDisplay = ReportFieldUtil.generateTableData(resForData, rv.reportObj.configJson.containers);
                                rv.data.filteredData = angular.copy(rv.data.tableToDisplay);
                            }
                        }
                    }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                        rv.pagingService.pagingRetrivalOn = false;
                        rv.pageLoaded = true;
                        Mask.hide();
                    });
                }
            } else {
                Mask.show()
                ReportDAO.retrieveDataByQueryUUID(rv.reportObj.configJson.containers.tableContainer[0].queryUUID, paramMap).then(function (res) {
                    rv.toggleFilter();
                    rv.data.tableData = res;
                    rv.data.tableToDisplay = ReportFieldUtil.generateTableData(res, rv.reportObj.configJson.containers);
                    rv.data.filteredData = angular.copy(rv.data.tableToDisplay);
                }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                    rv.pageLoaded = true;
                    Mask.hide();
                });
            }
            rv.searchFilterArrayValues = [];
            rv.reportObj.configJson.containers.fieldsContainer.forEach(function (field) {
                if (field.fieldType == 'date' || field.fieldType == 'dateFromTo' || field.fieldType == 'onlyMonthFromTo' || field.fieldType == 'onlyMonth') {
                    var fieldKeys = Object.keys(field.searchField);
                    fieldKeys.forEach(function (key) {
                        if (field.searchField[key] != null) {
                            rv.searchFilterArrayValues.push(field.searchField[key]);
                        }
                    });
                }
            });
            if (rv.searchFilterArrayValues.length > 1) {
                rv.range = rv.searchFilterArrayValues.join(' -- ');
            } else if (rv.searchFilterArrayValues.length == 1) {
                rv.range = rv.searchFilterArrayValues[0];
            }
        };

        rv.toggleFilter = function () {
            if (angular.element('.filter-div').hasClass('active')) {
                angular.element('body').css("overflow", "auto");
            } else {
                angular.element('body').css("overflow", "hidden");
            }
            angular.element('.cst-backdrop').fadeToggle();
            angular.element('.filter-div').toggleClass('active');
        };

        rv.filterData = function (value) {
            if (!value) {
                if (rv.config.SearchTerm && rv.config.searchOption) {
                    if (rv.config.searchitem) {
                        rv.config.searchitem.push({
                            SearchTerm: rv.config.SearchTerm,
                            searchOption: rv.config.searchOption,
                            searchKeyword: rv.config.searchKeyword
                        });
                    }
                    else {
                        rv.config.searchitem = [];
                        rv.config.searchitem.push({
                            SearchTerm: rv.config.SearchTerm,
                            searchOption: rv.config.searchOption,
                            searchKeyword: rv.config.searchKeyword
                        });
                    }
                }
                else {
                    toaster.pop('error', 'Search term or search option cannot be empty');
                    rv.data.filteredData.body = rv.data.tableToDisplay.body;
                }
            }
            if (rv.reportObj.configJson.layout === 'dynamicReportWithPagination') {
                //call retrieveTableDataByQueryId adding search criteria
                rv.searchString = null;
                if (rv.config) {
                    angular.forEach(rv.config.searchitem, function (search) {
                        switch (search.searchOption) {
                            case 'exactMatch':
                                if (search.searchKeyword) {
                                    if (rv.searchString === null) {
                                        rv.searchString = '"' +search.SearchTerm + '"' + "='" + search.searchKeyword + "'";
                                    } else {
                                        rv.searchString = rv.searchString + " and " + '"' +search.SearchTerm + '"' + "='" + search.searchKeyword + "'";
                                    }
                                }
                                else {
                                    toaster.pop('error', 'Please enter a search key');
                                    rv.data.filteredData.body = rv.filteredData.body;
                                }
                                break;
                            case 'contains':
                                if (search.searchKeyword) {
                                    if (rv.searchString === null) {
                                        rv.searchString = '"' +search.SearchTerm + '"' + " ilike '%" + search.searchKeyword + "%'";
                                    } else {
                                        rv.searchString = rv.searchString + " and " + '"' +search.SearchTerm + '"' + " ilike '%" + search.searchKeyword + "%'";
                                    }
                                }
                                else {
                                    toaster.pop('error', 'Please enter a search key');
                                    rv.data.filteredData.body = rv.data.filteredData.body;
                                }
                                break;
                            case 'equal':
                                if (search.searchKeyword) {
                                    if (rv.searchString === null) {
                                        rv.searchString = '"' +search.SearchTerm + '"' + "=" + search.searchKeyword;
                                    } else {
                                        rv.searchString = rv.searchString + " and " + '"' +search.SearchTerm + '"' + "=" + search.searchKeyword;
                                    }
                                }
                                else {
                                    toaster.pop('error', 'Please enter a search key');
                                    rv.data.filteredData.body = rv.data.filteredData.body;
                                }
                                break;
                            case 'notEqual':
                                if (search.searchKeyword) {
                                    if (rv.searchString === null) {
                                        rv.searchString = '"' +search.SearchTerm + '"' + "!=" + search.searchKeyword;
                                    } else {
                                        rv.searchString = rv.searchString + " and " + '"' +search.SearchTerm + '"' + "!=" + search.searchKeyword;
                                    }
                                }
                                else {
                                    toaster.pop('error', 'Please enter a search key');
                                    rv.data.filteredData.body = rv.data.filteredData.body;
                                }
                                break;
                            case 'greater':
                                if (search.searchKeyword) {
                                    if (rv.searchString === null) {
                                        rv.searchString = '"' +search.SearchTerm + '"' + ">" + search.searchKeyword;
                                    } else {
                                        rv.searchString = rv.searchString + " and " + '"' +search.SearchTerm + '"' + ">" + search.searchKeyword;
                                    }
                                }
                                else {
                                    toaster.pop('error', 'Please enter a search key');
                                    rv.data.filteredData.body = rv.data.filteredData.body;
                                }
                                break;
                            case 'less':
                                if (search.searchKeyword) {
                                    if (rv.searchString === null) {
                                        rv.searchString = '"' +search.SearchTerm + '"' + "<" + search.searchKeyword;
                                    } else {
                                        rv.searchString = rv.searchString + " and " + '"' +search.SearchTerm + '"' + "<" + search.searchKeyword;
                                    }
                                }
                                else {
                                    toaster.pop('error', 'Please enter a search key');
                                    rv.data.filteredData.body = rv.data.filteredData.body;
                                }
                                break;
                            case 'greaterEqual':
                                if (search.searchKeyword) {
                                    if (rv.searchString === null) {
                                        rv.searchString = '"' +search.SearchTerm + '"' + ">=" + search.searchKeyword;
                                    } else {
                                        rv.searchString = rv.searchString + " and " + '"' +search.SearchTerm + '"' + ">=" + search.searchKeyword;
                                    }
                                }
                                else {
                                    toaster.pop('error', 'Please enter a search key');
                                    rv.data.filteredData.body = rv.data.filteredData.body;
                                }
                                break;
                            case 'lessEqual':
                                if (search.searchKeyword) {
                                    if (rv.searchString === null) {
                                        rv.searchString = '"' +search.SearchTerm + '"' + "<=" + search.searchKeyword;
                                    } else {
                                        rv.searchString = rv.searchString + " and " + '"' +search.SearchTerm + '"' + "<=" + search.searchKeyword;
                                    }
                                }
                                else {
                                    toaster.pop('error', 'Please enter a search key');
                                    rv.data.filteredData.body = rv.data.filteredData.body;
                                }
                                break;
                            case 'nullValue':
                                if (rv.searchString === null) {
                                    rv.searchString = '"' +search.SearchTerm + '"' + " is null";
                                } else {
                                    rv.searchString = rv.searchString + " and " + '"' +search.SearchTerm + '"' + " is null";
                                }
                                break;
                            case 'notNullValue':
                                if (rv.searchString === null) {
                                    rv.searchString = '"' +search.SearchTerm + '"' + " is not null";
                                } else {
                                    rv.searchString = rv.searchString + " and " + '"' +search.SearchTerm + '"' + " is not null";
                                }
                                break;
                            default:

                        }
                    });
                    rv.config.SearchTerm = null;
                    rv.config.searchKeyword = null;
                    rv.config.searchOption = null;
                    rv.pagingService = {
                        offSet: 0,
                        limit: 100,
                        index: 0,
                        allRetrieved: false,
                        pagingRetrivalOn: false
                    };
                    if(!rv.isFilter){
                        rv.headerForSearch = rv.data.tableToDisplay.header
                        rv.isFilter = true;
                    }
                    retrieveTableDataByQueryId();
                }
            }
            else {
                if (rv.config.searchitem && rv.config.searchitem.length > 0) {
                    rv.data.filteredData.body = rv.data.tableToDisplay.body;
                    angular.forEach(rv.config.searchitem, function (search) {
                        switch (search.searchOption) {
                            case 'exactMatch':
                                if (search.searchKeyword) {
                                    rv.data.filteredData.body = rv.data.filteredData.body.filter(function (item) {
                                        return (item[search.SearchTerm].displayValue === search.searchKeyword)
                                    });
                                }
                                else {
                                    toaster.pop('error', 'Please enter a search key');
                                    rv.data.filteredData.body = rv.filteredData.body;
                                }
                                break;
                            case 'contains':
                                if (search.searchKeyword) {
                                    var regexString = '[a-zA-Z0-9]*' + angular.lowercase(search.searchKeyword) + '[a-zA-Z0-9]*';
                                    var regExp = new RegExp(regexString);
                                    rv.data.filteredData.body = rv.data.filteredData.body.filter(function (item) {
                                        var temp = angular.lowercase(item[search.SearchTerm].displayValue);
                                        if (temp != null) {
                                            return regExp.test(temp);
                                        }
                                    });
                                }
                                else {
                                    toaster.pop('error', 'Please enter a search key');
                                    rv.data.filteredData.body = rv.data.filteredData.body;
                                }
                                break;
                            case 'equal':
                                if (search.searchKeyword) {
                                    rv.data.filteredData.body = rv.data.filteredData.body.filter(function (item) {
                                        return (item[search.SearchTerm].displayValue == parseInt(search.searchKeyword))
                                    });
                                }
                                else {
                                    toaster.pop('error', 'Please enter a search key');
                                    rv.data.filteredData.body = rv.data.filteredData.body;
                                }
                                break;
                            case 'notEqual':
                                if (search.searchKeyword) {
                                    rv.data.filteredData.body = rv.data.filteredData.body.filter(function (item) {
                                        return (item[search.SearchTerm].displayValue != parseInt(search.searchKeyword))
                                    });
                                }
                                else {
                                    toaster.pop('error', 'Please enter a search key');
                                    rv.data.filteredData.body = rv.data.filteredData.body;
                                }
                                break;
                            case 'greater':
                                if (search.searchKeyword) {
                                    rv.data.filteredData.body = rv.data.filteredData.body.filter(function (item) {
                                        return (item[search.SearchTerm].displayValue > parseInt(search.searchKeyword) && item[search.SearchTerm].displayValue)
                                    });
                                }
                                else {
                                    toaster.pop('error', 'Please enter a search key');
                                    rv.data.filteredData.body = rv.data.filteredData.body;
                                }
                                break;
                            case 'less':
                                if (search.searchKeyword) {
                                    rv.data.filteredData.body = rv.data.filteredData.body.filter(function (item) {
                                        return (item[search.SearchTerm].displayValue < parseInt(search.searchKeyword) && item[search.SearchTerm].displayValue)
                                    });
                                }
                                else {
                                    toaster.pop('error', 'Please enter a search key');
                                    rv.data.filteredData.body = rv.data.filteredData.body;
                                }
                                break;
                            case 'greaterEqual':
                                if (search.searchKeyword) {
                                    rv.data.filteredData.body = rv.data.filteredData.body.filter(function (item) {
                                        return (item[search.SearchTerm].displayValue >= parseInt(search.searchKeyword) && item[search.SearchTerm].displayValue)
                                    });
                                }
                                else {
                                    toaster.pop('error', 'Please enter a search key');
                                    rv.data.filteredData.body = rv.data.filteredData.body;
                                }
                                break;
                            case 'lessEqual':
                                if (search.searchKeyword) {
                                    rv.data.filteredData.body = rv.data.filteredData.body.filter(function (item) {
                                        return (item[search.SearchTerm].displayValue <= parseInt(search.searchKeyword) && item[search.SearchTerm].displayValue)
                                    });
                                }
                                else {
                                    toaster.pop('error', 'Please enter a search key');
                                    rv.data.filteredData.body = rv.data.filteredData.body;
                                }
                                break;
                            case 'nullValue':
                                rv.data.filteredData.body = rv.data.filteredData.body.filter(function (item) {
                                    if (item[search.SearchTerm].displayValue !== 0) {
                                        return (!item[search.SearchTerm].displayValue)
                                    }
                                    else {
                                        return (!!item[search.SearchTerm].displayValue)
                                    }
                                });
                                break;
                            case 'notNullValue':
                                rv.data.filteredData.body = rv.data.filteredData.body.filter(function (item) {
                                    if (item[search.SearchTerm].displayValue !== 0) {
                                        return (!!item[search.SearchTerm].displayValue)
                                    }
                                    else {
                                        return (!item[search.SearchTerm].displayValue)
                                    }
                                });
                                break;
                            default:

                        }
                    });
                    rv.config.SearchTerm = null;
                    rv.config.searchKeyword = null;
                    rv.config.searchOption = null;
                }
            }

        };

        rv.clearSearch = function () {
            rv.data.filteredData.body = rv.data.tableToDisplay.body;
            rv.config.SearchTerm = null;
            rv.config.searchKeyword = null;
            rv.config.searchOption = null;
            rv.config.searchitem = [];
            rv.searchString = null;
            if(rv.reportObj.configJson.layout === 'dynamicReportWithPagination'){
                rv.filterData(true);
            }

        };

        rv.removeSelectedArea = function (item, index) {
            rv.config.searchitem.splice(index, 1);
            if(rv.reportObj.configJson.layout === 'dynamicReportWithPagination'){
                rv.filterData(true);
            }
            else{
                if (rv.config.searchitem && rv.config.searchitem.length > 0) {
                    rv.filterData(true);
                }
                else {
                    rv.clearSearch();
                }
            }
        }

        init();
    }
    angular.module('imtecho.controllers').controller('ReportViewController', ReportViewController);
})();
