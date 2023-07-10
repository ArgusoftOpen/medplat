(function () {
    function TranslatorLabelController(QueryDAO, Mask, toaster, GeneralUtil, $filter, AuthenticateService, InternationalizationDAO) {
        var translatorLabel = this;
        var actualData = [];
        translatorLabel.localLanguage = GeneralUtil.getLocalLanguage();

        var clearData = function () {
            translatorLabel.search = {
                language: 'EN'
            };
            translatorLabel.newQueryConfig = {};
            translatorLabel.queryConfigurationForm.$setPristine();
        };

        translatorLabel.search = {
            isPending: true,
            language: 'EN'
        };

        translatorLabel.pagingService = {
            offset: 0,
            limit: 100,
            index: 0,
            allRetrieved: false,
            pagingRetrivalOn: false
        };

        AuthenticateService.getLoggedInUser().then(function (res) {
            translatorLabel.currentUser = res.data;
        });

        translatorLabel.configuredLabels = [];

        translatorLabel.appNameList = [
            { 'text': 'WEB', value: 'WEB' },
            { 'text': 'SOH', value: 'SOH' },
            { 'text': 'Techo Mobile APP', value: 'TECHO_MOBILE_APP' }
        ]

        translatorLabel.getAppName = function (appValue) {
            let appName = translatorLabel.appNameList.find(app => app.value === appValue);
            return appName ? appName.text : null;
        }

        var retrieveAll = function () {
            if (!translatorLabel.pagingService.pagingRetrivalOn && !translatorLabel.pagingService.allRetrieved) {
                translatorLabel.pagingService.pagingRetrivalOn = true;
                setoffsetLimit();
                let queryDto = {
                    code: 'translation_label_retrival_1',
                    parameters: {
                        limit: translatorLabel.pagingService.limit,
                        offset: translatorLabel.pagingService.offset,
                        startsWith: translatorLabel.search.startsWith,
                        searchText: translatorLabel.search.searchText,
                        appName: translatorLabel.search.appName || null
                    }
                };
                Mask.show();
                QueryDAO.execute(queryDto).then(function (resForData) {
                    if (resForData.result.length === 0) {
                        translatorLabel.pagingService.allRetrieved = true;
                    } else {
                        translatorLabel.pagingService.allRetrieved = false;
                        actualData = actualData.concat(resForData.result);
                        actualData.forEach(data => {
                            const temp = translatorLabel.configuredLabels.filter(d => d.key == data.key);
                            if (temp.length > 0) {
                                translatorLabel.configuredLabels.forEach(ad => {
                                    if (ad.key === data.key) {
                                        if (data.language == 'GU') {
                                            ad.guValue = data.text;
                                        } else if (data.language == 'EN') {
                                            ad.enValue = data.text;
                                        }
                                    }
                                });
                            } else {
                                translatorLabel.configuredLabels.push({
                                    key: data.key,
                                    label: data.label,
                                    enValue: data.language == 'EN' ? data.text : null,
                                    guValue: data.language == 'GU' ? data.text : null,
                                    appName: data.appName,
                                    isEditable: false
                                });
                            }
                        });
                    }
                    Mask.hide();
                }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                    translatorLabel.pagingService.pagingRetrivalOn = false;
                    Mask.hide();
                });
            }
        };

        const saveLabelsOnServer = function (newQueryConfigs) {
            let count = newQueryConfigs.length;
            newQueryConfigs.forEach(newQueryConfig => {
                delete newQueryConfig.label
                delete newQueryConfig.languagetodisplay
                delete newQueryConfig.key
                let queryDto = {
                    code: 'translation_label_update_1',
                    parameters: newQueryConfig
                };
                Mask.show();
                QueryDAO.execute(queryDto).then(function () {
                    count--;
                    if (count == 0) {
                        toaster.pop('success', 'Translation Label Configuration Updated Successfully');
                        clearData();
                        translatorLabel.searchData(true);
                        InternationalizationDAO.updateLabelsMap().then(function () { }, function () { });
                    }
                }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                    Mask.hide();
                });
            });
        }

        translatorLabel.toggleIsEditable = function (labelDetails) {
            translatorLabel.configuredLabels.forEach(ad => {
                if (ad.key === labelDetails.key) {
                    ad.isEditable = !ad.isEditable;
                }
            });
        }

        translatorLabel.saveLabel = function (labelDetails) {
            translatorLabel.toggleIsEditable(labelDetails);
            if (!labelDetails.isEditable) {
                const newQueryConfigs = [];
                const enValueUpdated = document.getElementById(labelDetails.key + '-enValue').innerText;
                const guValueUpdated = document.getElementById(labelDetails.key + '-guValue').innerText;
                let selectedAppName = document.getElementById(labelDetails.key + '-appName');
                const appName = selectedAppName.options[selectedAppName.selectedIndex].value;
                if (enValueUpdated != labelDetails.enValue || appName != labelDetails.appName) {
                    if (!enValueUpdated || enValueUpdated.trim() == "") {
                        toaster.pop('error', 'Label is empty');
                        translatorLabel.toggleIsEditable(labelDetails);
                        return;
                    }
                    const enLngConfig = actualData.filter(data => data.key == labelDetails.key && data.language == 'EN');
                    const newQueryConfig = angular.copy(enLngConfig[0]);
                    newQueryConfig.text = enValueUpdated;
                    newQueryConfig.oldKey = newQueryConfig.key;
                    newQueryConfig.appName = appName;
                    newQueryConfigs.push(newQueryConfig);
                }
                if (guValueUpdated != labelDetails.guValue || appName != labelDetails.appName) {
                    if (!guValueUpdated || guValueUpdated.trim() == "") {
                        toaster.pop('error', 'Label is empty');
                        translatorLabel.toggleIsEditable(labelDetails);
                        return;
                    }
                    const guLngConfig = actualData.filter(data => data.key == labelDetails.key && data.language == 'GU');
                    const newQueryConfig = angular.copy(guLngConfig[0]);
                    newQueryConfig.text = guValueUpdated;
                    newQueryConfig.appName = appName;
                    newQueryConfig.oldKey = newQueryConfig.key;
                    newQueryConfigs.push(newQueryConfig);
                }
                if (newQueryConfigs.length > 0) {
                    saveLabelsOnServer(newQueryConfigs);
                }
            }
        }

        translatorLabel.openAddEditModal = function (configuredQuery) {
            if (configuredQuery) {
                translatorLabel.newQueryConfig = angular.copy(configuredQuery);
                translatorLabel.newQueryConfig.oldKey = translatorLabel.newQueryConfig.key;
                translatorLabel.newQueryConfig.gujText = null;
                translatorLabel.newQueryConfig.engText = null;
                translatorLabel.isEditMode = true;
            } else {
                translatorLabel.isEditMode = false;
            }
            toggleFilter();
        };

        let toggleFilter = function () {
            if (angular.element('.filter-div').hasClass('active')) {
                angular.element('body').css("overflow", "auto");
            } else {
                angular.element('body').css("overflow", "hidden");
            }
            angular.element('.cst-backdrop').fadeToggle();
            angular.element('.filter-div').toggleClass('active');
        };

        translatorLabel.saveQuery = function () {
            translatorLabel.queryConfigurationForm.$setSubmitted();
            if (translatorLabel.queryConfigurationForm.$valid) {
                delete translatorLabel.newQueryConfig.label
                delete translatorLabel.newQueryConfig.languagetodisplay
                delete translatorLabel.newQueryConfig.key
                if (translatorLabel.isEditMode) {
                    let queryDto = {
                        code: 'translation_label_update_1',
                        parameters: translatorLabel.newQueryConfig
                    };
                    Mask.show();
                    QueryDAO.execute(queryDto).then(function () {
                        toaster.pop('success', 'Translation Label Configuration Updated Successfully');
                        toggleFilter();
                        clearData();
                        translatorLabel.searchData(true);
                        InternationalizationDAO.updateLabelsMap().then(function () { }, function () { });
                    }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                        Mask.hide();
                    });
                } else {
                    translatorLabel.newQueryConfig.label = translatorLabel.newQueryConfig.engText
                    let labelCheckDto = {
                        code: 'translation_label_check_1',
                        parameters: {
                            label: translatorLabel.newQueryConfig.label,
                            appName: translatorLabel.newQueryConfig.appName
                        }
                    }
                    Mask.show();
                    QueryDAO.execute(labelCheckDto).then(function (response) {
                        if (response.result.length === 0) {
                            const queryConfigForGuj = JSON.parse(JSON.stringify(translatorLabel.newQueryConfig));
                            queryConfigForGuj.text = translatorLabel.newQueryConfig.gujText;
                            queryConfigForGuj.language = 'GU';
                            delete queryConfigForGuj.engText;
                            delete queryConfigForGuj.gujText;

                            const queryConfigForEng = JSON.parse(JSON.stringify(translatorLabel.newQueryConfig));
                            queryConfigForEng.text = translatorLabel.newQueryConfig.engText;
                            queryConfigForEng.language = 'EN';
                            delete queryConfigForEng.engText;
                            delete queryConfigForEng.gujText;

                            const queryDtos = [];
                            queryDtos.push({
                                code: 'translation_label_add_1',
                                parameters: queryConfigForEng,
                                sequence: 1
                            });
                            queryDtos.push({
                                code: 'translation_label_add_1',
                                parameters: queryConfigForGuj,
                                sequence: 2
                            });
                            QueryDAO.executeAll(queryDtos).then(function () {
                                toaster.pop('success', 'Translation Label Configuration Saved Successfully');
                                toggleFilter();
                                clearData();
                                translatorLabel.searchData(true);
                                InternationalizationDAO.updateLabelsMap().then(function () { }, function () { });
                            }, GeneralUtil.showMessageOnApiCallFailure).finally(Mask.hide);
                        } else {
                            toaster.pop('error', 'This label already exists');
                            Mask.hide();
                        }
                    }, GeneralUtil.showMessageOnApiCallFailure).finally(Mask.hide);
                }
            }
        };

        translatorLabel.close = function () {
            translatorLabel.newQueryConfig = {};
            translatorLabel.queryConfigurationForm.$setPristine();
            toggleFilter();
        };

        translatorLabel.searchData = function (reset) {
            if (reset) {
                actualData = [];
                translatorLabel.pagingService.index = 0;
                translatorLabel.pagingService.allRetrieved = false;
                translatorLabel.pagingService.pagingRetrivalOn = false;
                translatorLabel.configuredLabels = [];
            }
            retrieveAll();
        };

        var setoffsetLimit = function () {
            translatorLabel.pagingService.limit = 100;
            translatorLabel.pagingService.offset = translatorLabel.pagingService.index * 100;
            translatorLabel.pagingService.index = translatorLabel.pagingService.index + 1;
        };

        translatorLabel.letterSearch = (letter) => {
            translatorLabel.search.startsWith = letter;
            translatorLabel.search.searchText = null;
        }

        translatorLabel.clearAllFilters = () => {
            translatorLabel.search.startsWith = null;
            translatorLabel.search.searchText = null;
            translatorLabel.search.appName = null;
            translatorLabel.searchData(true)
        }

        retrieveAll();
    }
    angular.module('imtecho.controllers').controller('TranslatorLabelController', TranslatorLabelController);
})();
