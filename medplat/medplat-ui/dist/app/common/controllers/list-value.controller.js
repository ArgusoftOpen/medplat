(function (angular) {
    let listValueModalController = function ($scope, $uibModalInstance, listFieldKey, title, QueryDAO, Mask, GeneralUtil, toaster) {
        $scope.listFieldKey = listFieldKey;
        $scope.title = title;
        $scope.listValues = [];

        $scope.init = () => {
            Mask.show();
            QueryDAO.execute({
                code: 'retrieve_list_values_by_field_key',
                parameters: {
                    fieldKey: $scope.listFieldKey
                }
            }).then((response) => {
                if (Array.isArray(response.result) && response.result.length) {
                    $scope.listValues = response.result;
                }
            }).catch((error) => {
                GeneralUtil.showMessageOnApiCallFailure(error);
            }).finally(() => {
                Mask.hide();
            })
        }

        $scope.addNew = () => {
            $scope.listValues.push({
                editMode: true,
                value: '',
                is_active: true,
                is_new: true,
                field_key: $scope.listFieldKey
            });
        }

        $scope.editListValue = (listValue) => {
            listValue.editMode = true;
        }

        $scope.toggleStatus = (listValue) => {
            listValue.statusChanged = true;
            listValue.is_active = !listValue.is_active
        }
        $scope.ok = function () {
            $scope.listValueForm.$setSubmitted();
            if ($scope.listValueForm.$valid) {
                let queryDto = [];
                let statusChangedDto = [];
                $scope.listValues.forEach((listValue) => {
                    if (listValue.is_new) {
                        queryDto.push({
                            code: 'insert_listvalues',
                            parameters: {
                                value: listValue.value,
                                fieldKey: listValue.field_key,
                                fileSize: 0,
                                multimediaType: null
                            },
                            sequence: queryDto.length + 1
                        });
                    } else if (listValue.editMode) {
                        queryDto.push({
                            code: 'update_listvalues',
                            parameters: {
                                value: listValue.value,
                                fileSize: 0,
                                multimediaType: null,
                                id: listValue.id
                            },
                            sequence: queryDto.length + 1
                        });
                    }
                    if (listValue.statusChanged) {
                        statusChangedDto.push({
                            code: 'update_active_inactive_listvalues',
                            parameters: {
                                isActive: listValue.is_active,
                                id: listValue.id
                            },
                            sequence: statusChangedDto.length + 1
                        })
                    }
                });
                Mask.show();
                QueryDAO.executeAll(queryDto).then(() => {
                    return QueryDAO.executeAll(statusChangedDto);
                }).then(() => {
                    toaster.pop('success', 'Data updated successfully');
                }).catch((error) => {
                    GeneralUtil.showMessageOnApiCallFailure(error);
                }).finally(() => {
                    Mask.hide();
                    $uibModalInstance.close();
                });
            }
        };
        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };

        $scope.init();
    };
    angular.module('imtecho.controllers').controller('listValueModalController', listValueModalController);
})(window.angular);
