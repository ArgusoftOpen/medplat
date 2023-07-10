(function () {
    function EventConfigurationController(NotificationDAO, $state, Mask, GeneralUtil, toaster, $uibModal, $q, EventConfigDAO, FormDAO, QueryDAO) {
        var eventConfig = this;
        var selectedCondition = {}, selectedTypeConfig = {};

        eventConfig.addConfiguration = function () {
            if (eventConfig.eventObj && eventConfig.eventObj.notificationConfigDetails && isFormValid()) {
                if (!eventConfig.eventObj.notificationConfigDetails) {
                    eventConfig.eventObj.notificationConfigDetails = [];
                }
                eventConfig.eventObj.notificationConfigDetails.push({ implicitParam: ['#resource_id#'] });
            } else if (!eventConfig.configurationForm) {
                if (!eventConfig.eventObj) {
                    eventConfig.eventObj = {};
                }
                if (!eventConfig.eventObj.notificationConfigDetails) {
                    eventConfig.eventObj.notificationConfigDetails = [];
                }
                eventConfig.eventObj.notificationConfigDetails.push({ implicitParam: ['#resource_id#'] });
            }
        };

        eventConfig.openConditionModal = function (config, condition, index) {
            eventConfig.selectedConfig = (config);
            if (condition) {
                selectedCondition = condition;
                selectedCondition.index = index;
                eventConfig.newCondition = angular.copy(condition);
                eventConfig.conditionEditMode = true;
            } else {
                selectedCondition = {};
                eventConfig.newCondition = {};
                eventConfig.conditionEditMode = false;

            }
            eventConfig.newType = {};
            eventConfig.conditionForm.$setPristine();
            eventConfig.toggleFilter();
        };

        eventConfig.toggleFilter = function () {
            if (angular.element('.filter-div').hasClass('active')) {
                eventConfig.modalClosed = true;
                angular.element('body').css("overflow", "auto");
            } else {
                eventConfig.modalClosed = false;
                angular.element('body').css("overflow", "hidden");
            }
            angular.element('.cst-backdrop').fadeToggle();
            angular.element('.filter-div').toggleClass('active');
            if (CKEDITOR.instances) {
                for (var ck_instance in CKEDITOR.instances) {
                    CKEDITOR.instances[ck_instance].destroy();
                }
            }
        };

        eventConfig.addTypeConfig = function (config, index) {
            eventConfig.newTypeForm.$setSubmitted();
            if (config || eventConfig.newTypeForm.$valid) {
                if (config) {
                    config.index = index;
                    selectedTypeConfig = config;
                    eventConfig.newType = angular.copy(config);
                    eventConfig.typeEditMode = true;
                } else {
                    eventConfig.typeEditMode = false;
                }
                openAddConfigurationModal().then(function () {
                    addTypeConfiguration();
                }, function () {
                    eventConfig.newType = {};
                    eventConfig.newTypeForm.$setPristine();
                });
            }
        };

        var addTypeConfiguration = function () {
            if (!eventConfig.newCondition.notificaitonConfigsType) {
                eventConfig.newCondition.notificaitonConfigsType = [];
            }
            if (eventConfig.typeEditMode) {
                eventConfig.newCondition.notificaitonConfigsType[selectedTypeConfig.index] = eventConfig.newType;
            } else {
                eventConfig.newCondition.notificaitonConfigsType.push(eventConfig.newType);
            }
            eventConfig.newType = {};
            eventConfig.newTypeForm.$setPristine();
        };

        var openAddConfigurationModal = function () {
            var defer = $q.defer();
            eventConfig.newType.implicitParam = angular.copy(eventConfig.selectedConfig.implicitParam);
            var modalInstanceProperties = {
                controller: 'TypeConfigModalController',
                controllerAs: 'typeconfig',
                windowClass: 'cst-modal',
                backdrop: 'static',
                size: 'lg',
                resolve: {
                    type: function () {
                        return angular.copy(eventConfig.newType);
                    }
                }
            };
            if (eventConfig.newType.type === 'MOBILE') {
                modalInstanceProperties.templateUrl = 'app/admin/applicationmanagement/event/views/mobile-type-event-config.modal.html';
            } else {
                modalInstanceProperties.templateUrl = 'app/admin/applicationmanagement/event/views/other-type-event-config.modal.html';
            }
            var modalInstance = $uibModal.open(modalInstanceProperties);
            modalInstance.result.then(function (type) {
                eventConfig.newType = type;
                defer.resolve();

            }, function () {
                defer.reject();
            });
            return defer.promise;
        };

        eventConfig.addCondition = function () {
            eventConfig.conditionForm.$setSubmitted();
            if (!eventConfig.selectedConfig.conditions) {
                eventConfig.selectedConfig.conditions = [];
            }
            if (eventConfig.conditionEditMode) {
                eventConfig.selectedConfig.conditions[selectedCondition.index] = eventConfig.newCondition;
            }
            if (eventConfig.conditionForm.$valid && (eventConfig.newCondition.notificaitonConfigsType && eventConfig.newCondition.notificaitonConfigsType.length > 0) || eventConfig.newCondition.condition) {
                if (!eventConfig.conditionEditMode) {
                    eventConfig.selectedConfig.conditions.push(angular.copy(eventConfig.newCondition));
                }
                eventConfig.newCondition = {};
                eventConfig.newType = {};
                eventConfig.toggleFilter();
            } else if (eventConfig.conditionForm.$valid) {
                toaster.pop('error', 'To save please add atleast a condition or type for configuration.');
            }

        };

        eventConfig.deleteCondition = function (config, condition) {
            config.conditions = _.without(config.conditions, condition);
        };

        eventConfig.deleteConfig = function (config) {
            eventConfig.eventObj.notificationConfigDetails = _.without(eventConfig.eventObj.notificationConfigDetails, config);
        };

        eventConfig.deleteConditionConfig = function (config) {
            eventConfig.newCondition.notificaitonConfigsType = _.without(eventConfig.newCondition.notificaitonConfigsType, config);
        };

        eventConfig.saveEvent = function () {
            eventConfig.manageTemplateForm.$setSubmitted();
            if (eventConfig.manageTemplateForm.$valid && eventConfig.eventObj.notificationConfigDetails
                && eventConfig.eventObj.notificationConfigDetails.length > 0 &&
                (eventConfig.eventObj.notificationConfigDetails[eventConfig.eventObj.notificationConfigDetails.length - 1].query || eventConfig.eventObj.notificationConfigDetails[eventConfig.eventObj.notificationConfigDetails.length - 1].conditions)) {
                Mask.show();
                if (eventConfig.eventObj.eventType === 'FORM_SUBMITTED') {

                    eventConfig.eventObj.formTypeId = eventConfig.eventObj.form?.id;
                    eventConfig.eventObj.eventTypeDetailCode = eventConfig.eventObj.form?.code;
                } else {
                    delete eventConfig.eventObj.formTypeId;
                    delete eventConfig.eventObj.formCode;
                }
                eventConfig.onTriggerWhenChange();
                EventConfigDAO.saveOrUpdate(eventConfig.eventObj).then(function (res) {
                    if (eventConfig.isEdit) {
                        toaster.pop('success', 'Event Configuration Updated Successfully');
                    } else {
                        toaster.pop('success', 'Event Configuration Saved Successfully');
                    }

                    $state.go('techo.notification.all');
                }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                    Mask.hide();
                });
            } else if (eventConfig.manageTemplateForm.$valid) {
                toaster.pop('warning', "Please add atleast query / condition for the configuration");
            }
        };

        var isFormValid = function () {
            for (var index = 0; index < eventConfig.eventObj.notificationConfigDetails.length; index++) {
                eventConfig.configurationForm['form' + index].$setSubmitted();
                if (eventConfig.configurationForm['form' + index].$invalid) {
                    return false;
                }
            }
            return true;
        };

        var init = function () {
            Mask.show();
            FormDAO.retrieveAll({ is_active: true }).then(function (res) {
                eventConfig.forms = res;
                EventConfigDAO.retrieveManualEvents().then(function (manualEvents) {
                    eventConfig.events = manualEvents;
                    if ($state.params.id) {
                        EventConfigDAO.retrieveById($state.params.id).then(function (event) {
                            eventConfig.eventObj = event;
                            _.each(eventConfig.eventObj.notificationConfigDetails, function (config) {
                                eventConfig.setImplicitParam(config);
                            });
                            eventConfig.eventObj.form = _.find(eventConfig.forms, function (form) {
                                return form.id == eventConfig.eventObj.formTypeId;
                            })
                            eventConfig.isEdit = true;
                        });
                    }
                }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                    Mask.hide();
                });
            }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                Mask.hide();
            });

            NotificationDAO.retrieveAllNotifications(true).then(function (res) {
                eventConfig.notificationTypeMap = {};
                if (res) {
                    _.each(res, function (type) {
                        eventConfig.notificationTypeMap[type.id] = type.notificationName;
                    });
                }
            });
            QueryDAO.retrieveAllConfigured(true).then(function (res) {
                eventConfig.queryMasterMap = {};
                if (res) {
                    _.each(res, function (type) {
                        eventConfig.queryMasterMap[type.id] = type.code;
                    });
                }
            });
        };

        eventConfig.onTriggerWhenChange = function () {
            if (eventConfig.eventObj.trigerWhen === 'IMMEDIATELY') {
                delete eventConfig.eventObj.day;
                delete eventConfig.eventObj.hour;
                delete eventConfig.eventObj.minute;
            } else if (['HOURLY', 'MINUTE'].includes(eventConfig.eventObj.trigerWhen)) {
                delete eventConfig.eventObj.day;
                delete eventConfig.eventObj.hour;
            } else if (eventConfig.eventObj.trigerWhen === 'DAILY') {
                delete eventConfig.eventObj.day;
            }
        };

        eventConfig.onEventChange = function () {
            delete eventConfig.eventObj.eventTypeDetailCode;
            delete eventConfig.eventObj.formId;
            delete eventConfig.eventObj.trigerWhen;
            delete eventConfig.eventObj.day;
            delete eventConfig.eventObj.hour;
            delete eventConfig.eventObj.minute;
        };

        eventConfig.setImplicitParam = function (config) {
            config.implicitParam = ['#resourceId#'];
            if (config.query && config.query.search(/\bfrom\b(?!(.|\n)*(\bfrom\b))/) >= 0) {
                var namedParameters = (config.query.substring(6, config.query.search(/\bfrom\b(?!(.|\n)*(\bfrom\b))/))).match(/(\w*\(.*?\) as \w*|[^\(\),]+)(?=\s*,|\s*$)/g);
                _.each(namedParameters, function (parameter) {
                    parameter = parameter.trim();
                    if (parameter != '*') {
                        var aliasIndex = parameter.lastIndexOf(" as ");
                        if (aliasIndex >= 0) {
                            var parameterWithAlias = (parameter.substring(aliasIndex + 4, parameter.length)).trim();
                            config.implicitParam.push('#' + parameterWithAlias + '#');
                        } else {
                            config.implicitParam.push('#' + parameter + '#');
                        }
                    }
                });
            }
        };

        init();
    }
    angular.module('imtecho.controllers').controller('EventConfigurationController', EventConfigurationController);
})();
