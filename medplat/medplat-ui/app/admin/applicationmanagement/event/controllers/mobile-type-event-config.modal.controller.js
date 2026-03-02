(function () {
    var TypeConfigModalController = function ($uibModalInstance, GeneralUtil, type, NotificationDAO, Mask, QueryDAO, PushNotificationDAO) {
        var typeconfig = this;

        const init = function () {
            if (type.baseDateFieldName || (type.smsConfigJson && type.smsConfigJson.mobileNumberFieldName)) {
                typeconfig.editMode = true;
                if (!type.smsConfigJson) {
                    type.smsConfigJson = {};
                }
                if (!type.pushNotificationConfigJson) {
                    type.pushNotificationConfigJson = {};
                }
                if (!type.smsConfigJson.mobileNumberFieldName) {
                    type.smsConfigJson.mobileNumberFieldName = type.type === 'SMS' ? type.baseDateFieldName : '';
                }
            } else {
                typeconfig.editMode = false;
            }
            if (type.type == 'MOBILE') {
                Mask.show();
                NotificationDAO.retrieveAllNotifications(true).then(function (res) {
                    typeconfig.notificationTypes = res;
                    typeconfig.type = type;
                    if (type.mobileNotificationType) {
                        typeconfig.type.mobileNotification = _.find(res, function (notificationType) {
                            return notificationType.id == type.mobileNotificationType;
                        });
                    }
                }, GeneralUtil.showMessageOnApiCallFailure).finally((function () {
                    Mask.hide();
                }));
            } else {
                typeconfig.type = type;
                if (typeconfig.type.type == 'QUERY') {
                    retrieveQueryMasters();
                }
                if (typeconfig.type.type == 'PUSH_NOTIFICATION') {
                    retrievePushNotificationType();
                }
                else if(typeconfig.type.type == 'SYSTEM_FUNCTION'){
                    typeconfig.retrieveSystemFunctions();
                }
            }
        };

        typeconfig.ok = function () {
            typeconfig.mobileNotificaitonForm.$setSubmitted();
            if (isFormValid() && typeconfig.mobileNotificaitonForm.$valid) {
                if (typeconfig.type.type == 'MOBILE') {
                    typeconfig.type.mobileNotificationType = typeconfig.type.mobileNotification.id;
                }
                if (typeconfig.type.type == 'PUSH_NOTIFICATION') {
                    typeconfig.type.template = typeconfig.type.pushNotificationConfigJson.template
                }
                $uibModalInstance.close(typeconfig.type);
            }
        };

        typeconfig.addMobileNotification = function () {
            if (isFormValid()) {
                if (!typeconfig.type.mobileNotificationConfigs) {
                    typeconfig.type.mobileNotificationConfigs = [];
                }
                typeconfig.type.mobileNotificationConfigs.push({});
                typeconfig.newMobileConfig = {};
                typeconfig.mobileNotificaitonForm.$setPristine();
            }
        };

        typeconfig.onNotificationTypeChange = (notificationTypeId) => {
            typeconfig.type.notificationType =
                typeconfig.notificationTypeList.find(type => type.id == notificationTypeId);

            typeconfig.type.pushNotificationConfigJson.template = typeconfig.type.notificationType.message;
            typeconfig.type.pushNotificationConfigJson.heading = typeconfig.type.notificationType.heading;
        }

        typeconfig.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };

        typeconfig.deleteConfig = function (config) {
            typeconfig.type.mobileNotificationConfigs = _.without(typeconfig.type.mobileNotificationConfigs, config);
        };

        var isFormValid = function () {
            if (typeconfig.type.mobileNotificationConfigs) {
                for (var index = 0; index < typeconfig.type.mobileNotificationConfigs.length; index++) {
                    typeconfig.mobileForm['form' + index].$setSubmitted();
                    if (typeconfig.mobileForm['form' + index].$invalid) {
                        return false;
                    }
                }
            }
            return true;
        };

        var retrieveQueryMasters = function () {
            Mask.show();
            return QueryDAO.retrieveAllConfigured(true).then(function (res) {
                typeconfig.queryMasters = res;
            }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                Mask.hide();
            });
        };

        var retrievePushNotificationType = function () {
            Mask.show();
            return PushNotificationDAO.getNotificationTypeList().then(function (res) {
                typeconfig.notificationTypeList = res;
                typeconfig.notificationTypeList = typeconfig.notificationTypeList.filter((type) => {
                    return type.isActive;
                });
            }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                Mask.hide();
            });
        };

        typeconfig.retrieveSystemFunctions = function(){
            var dto = {
                code: 'system_function_retrieve_all',
                parameters: {}
            };
            Mask.show();
            QueryDAO.execute(dto).then(function (res) {
                typeconfig.systemFunctions = res.result.map((systemFunction)=>{
                    systemFunction.parameters = JSON.parse(systemFunction.parameters);
                    return systemFunction;
                });
            }).finally(()=>{
                Mask.hide();
            });
        }

        typeconfig.setImplicitParameters = function () {
            typeconfig.type.queryMasterParamJson = [];
            var queryMaster = _.find(typeconfig.queryMasters, function (master) {
                return master.code == typeconfig.type.queryCode;
            });
            if (queryMaster) {
                _.each(_.uniq(queryMaster.params.split(',')), function (param) {
                    typeconfig.type.queryMasterParamJson.push({ parameterName: param });
                });
            }
        };
        typeconfig.setFunctionParams = function(){
            typeconfig.type.functionParams = [];
            let systemFunction = _.find(typeconfig.systemFunctions,function(func){
                return func.id == typeconfig.type.systemFunctionId;
            });
            if(systemFunction){
                _.each(_.uniq(systemFunction.parameters),function(param){
                    typeconfig.type.functionParams.push(param)
                })
            }
        }

        init();
    };
    angular.module('imtecho.controllers').controller('TypeConfigModalController', TypeConfigModalController);
})();
