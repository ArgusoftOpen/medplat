(function () {
    function syncServerManageController($uibModal,QueryDAO) {
        let syncServerCtrl = this;
        syncServerCtrl.serverList = [];

        syncServerCtrl.init = function() {
            var dto = {
                code: 'get_active_server_list',
                parameters: {
                }
            };
            // get_active_server_list
            QueryDAO.executeQuery(dto).then(function (res) {
                syncServerCtrl.serverList = res.result;
            });
        }

        syncServerCtrl.changePassword = function(server) {
            
            var modalInstance = $uibModal.open({
                templateUrl: 'app/admin/applicationmanagement/servermanagement/views/change-password.model.html',
                controllerAs: 'changePasswordCtrl',
                controller: function ($uibModalInstance, Mask,toaster, selectedServer) {
                    let changePasswordCtrl = this;
                    changePasswordCtrl.passwordNotMatch;
                    changePasswordCtrl.server = selectedServer;

                    changePasswordCtrl.checkPassword = function () {
                        if (changePasswordCtrl.password === changePasswordCtrl.confirmPassword) {
                            changePasswordCtrl.passwordNotMatch = false;
                        }
                        else{
                            changePasswordCtrl.passwordNotMatch = true;                           
                        }
                    }

                    changePasswordCtrl.save = function () {
                        if (changePasswordCtrl.password === changePasswordCtrl.confirmPassword) {
                            changePasswordCtrl.passwordNotMatch = false;
                        }
                        else{
                            changePasswordCtrl.passwordNotMatch = true;
                            return;
                        }
                        
                        changePasswordCtrl.changePassword.$setSubmitted();
                        if (!changePasswordCtrl.changePassword.$invalid) {
                            var dto = {
                                code: 'update_list_server_password',
                                parameters: {
                                    password : changePasswordCtrl.password,
                                    id : changePasswordCtrl.server.id
                                }
                            };
                            // get_active_server_list
                            QueryDAO.executeQuery(dto).then(function (res) {                            
                                if (res.result[0].id !== null) {
                                    $uibModalInstance.dismiss('cancel');
                                    toaster.pop('success', "Updated Successfully ");
                                    Mask.hide();
                                }
                            })
                            .catch((err) => {
                                $uibModalInstance.dismiss('cancel');
                                toaster.pop('danger', "Update failed");
                                Mask.hide();
                            });
                        }

                    }

                    changePasswordCtrl.cancel = function() {
                        $uibModalInstance.dismiss('cancel');
                        Mask.hide();
                    }

                },
                windowClass: 'cst-modal',
                size: 'med',
                resolve: {
                    selectedServer : function () {
                        return server;
                    }                    
                }
            });
            modalInstance.result.then(function () {
                Mask.show();
            }, function (err) {

            });
        }

        syncServerCtrl.openAddEditModal = function (server) {
            var modalInstance = $uibModal.open({
                templateUrl: 'app/admin/applicationmanagement/servermanagement/views/add-server.model.html',
                controllerAs: 'mdCtrl',
                controller: function ($uibModalInstance, Mask, $http, APP_CONFIG, toaster, serverList) {
                    let mdCtrl = this;
                    mdCtrl.isEdit = false;
                    mdCtrl.serverList = serverList;
                    mdCtrl.enteredServerDetails = {};
                    if (server) {
                        mdCtrl.enteredServerDetails.id = server.id;
                        mdCtrl.enteredServerDetails.serverName = server.server_name;
                        mdCtrl.enteredServerDetails.userName = server.username;
                        mdCtrl.enteredServerDetails.host = server.host_url;
                        mdCtrl.enteredServerDetails.isActive = server.is_active;
                        mdCtrl.isEdit = true
                    }
                    mdCtrl.invalidServerName = false;
                    

                    mdCtrl.checkServerNameAVailability = function () {
                        if (mdCtrl.isEdit) {

                            let isMatched = _.filter(mdCtrl.serverList, (data) => {
                                if (data.server_name.toLowerCase() === mdCtrl.enteredServerDetails.serverName.toLowerCase()) {
                                    if (data.server_name.toLowerCase() === server.server_name.toLowerCase()) {
                                        return false;
                                    }
                                    return true;
                                }
                            });
                            if (isMatched.length > 0) {
                                mdCtrl.invalidServerName = true;
                            }
                            else {
                                mdCtrl.invalidServerName = false;
                            }

                        }
                        else {
                            if (mdCtrl.enteredServerDetails.serverName) {
                                let isMatched = _.filter(mdCtrl.serverList, (data) => {
                                    if (data.server_name.toLowerCase() === mdCtrl.enteredServerDetails.serverName.toLowerCase()) {
                                        return true;
                                    }
                                });
                                if (isMatched.length > 0) {
                                    mdCtrl.invalidServerName = true;
                                }
                                else {
                                    mdCtrl.invalidServerName = false;
                                }
                            }
                            else {
                                mdCtrl.invalidServerName = false;
                            }
                        }
                    }


                    mdCtrl.save = function () {
                        mdCtrl.addServerForm.$setSubmitted();
                        if (!mdCtrl.invalidServerName && mdCtrl.addServerForm.$valid) {
                            var bodyData = {
                                    id :  mdCtrl.enteredServerDetails.id || null,                               
                                    serverName: mdCtrl.enteredServerDetails.serverName,
                                    username: mdCtrl.enteredServerDetails.userName || null,
                                    password: mdCtrl.enteredServerDetails.password || null,
                                    host: mdCtrl.enteredServerDetails.host,
                                    isActive: mdCtrl.enteredServerDetails.isActive                                
                            }
                            
                            $http.post(APP_CONFIG.apiPath + '/server/addOrUpdate', bodyData).then((response) => {
                                syncServerCtrl.init();
                                $uibModalInstance.dismiss('cancel');
                                toaster.pop('success', "Inserted Successfully ");
                                Mask.hide();
                            })
                            .catch((err) => {
                                $uibModalInstance.dismiss('cancel');
                                toaster.pop('danger', "Insertion failed");
                                Mask.hide();
                            });
                                
                        }
                    }

                    mdCtrl.cancel = function () {
                        $uibModalInstance.dismiss('cancel');
                        Mask.hide();
                    }
                },
                windowClass: 'cst-modal',
                size: 'med',
                resolve: {
                    serverList : function () {
                        return syncServerCtrl.serverList;
                    }
                }
            });
            modalInstance.result.then(function () {
                Mask.show();
            }, function (err) {

            });
        }

        syncServerCtrl.init();
    
    }


    angular.module('imtecho.controllers').controller('syncServerManageController', syncServerManageController);
})();