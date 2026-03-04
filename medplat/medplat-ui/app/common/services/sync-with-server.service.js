(function (angular) {
    angular.module('imtecho.service').service('syncWithServerService', function (QueryDAO, $uibModal, toaster,Mask) {
        this.serverList = [];
        var dto = {
            code: 'get_active_server_list',
            parameters: {
            }
        };
        // get_active_server_list
        QueryDAO.executeQuery(dto).then(function (res) {
            this.serverList = res.result;
        });

        
        this.syncWithServer = async function (featureUUID) {
            // when FeatureUUID is not available that means respective feature item is not saved Properly 
            if (featureUUID === null) {
                toaster.pop('info', "No configuration Found.\n Please open respective feature item and save it.");
                return;
            }

            Mask.show();
            let syncedServerList = [];
            
            var retrieveDetailDto = {
                code: 'get_server_list_which_are_in_sync_for_auto_sync_configuration',
                parameters: {
                    featureUUID: featureUUID
                }
            };

            // get server list and mark as selected true 
            await QueryDAO.executeQuery(retrieveDetailDto).then(function (res) {
                syncedServerList = res.result;
                if (syncedServerList.length > 0) {
                    syncedServerList.forEach(element => {
                        this.serverList.forEach(server => {
                            if (server.server_name.toLowerCase() === element.server_name.toLowerCase()) {
                                server.Selected = true;
                            }
                            
                        });
                    });
                    Mask.hide();
                    return;
                }
                else {
                    _.forEach(this.serverList, (d) => { d.Selected = false });
                    Mask.hide();
                    return;
                }
            });
            
            $uibModal.open({
                templateUrl: 'app/admin/report/views/sync-with-server.model.html',
                controllerAs: 'mdCtrl',
                controller: function (serverList, $uibModalInstance, $http, APP_CONFIG) {
                    let mdCtrl = this;
                    let selectedServer = [];
                    mdCtrl.serverList = serverList;
                    mdCtrl.save = function () {
                        
                        Mask.show();

                        for (var i = 0; i < mdCtrl.serverList.length; i++) {
                            if (mdCtrl.serverList[i].Selected) {
                                selectedServer.push(mdCtrl.serverList[i].id);
                            }
                        }

                        // make a call to insert entry in system_sync_config_access
                        let bodyData = {
                            featureUUID: featureUUID,
                            serverlist: selectedServer
                        }

                        
                        $http.post(APP_CONFIG.apiPath + '/server/save/sync-with-server', bodyData).then((response) => {
                            mdCtrl.isProcessing = false;                            
                            $uibModalInstance.dismiss('cancel');
                            toaster.pop('success', "updated Successfully ");
                            Mask.hide();
                        })
                        .catch((err) => {
                            mdCtrl.isProcessing = false;
                            $uibModalInstance.dismiss('cancel');
                            toaster.pop('danger', "Insertion failed");
                            Mask.hide();
                        });                        
                    }

                    mdCtrl.cancel = function () {
                        $uibModalInstance.dismiss('cancel');
                        Mask.hide();
                    }

                },
                windowClass: 'cst-modal',
                size: 'med',
                resolve: {
                    serverList: function () {
                        return this.serverList;
                    }
                }
            }).closed.then(function(){
                _.forEach(this.serverList, (d) => { d.Selected = false });
            });
        }
        return this;
    });
})(window.angular);
