(function () {
    function featureSyncController (QueryDAO,$http,APP_CONFIG,toaster,Mask,$uibModal,$filter,PagingService) {
        let featureCtrl = this;
        featureCtrl.PagingService = PagingService.initialize();
        featureCtrl.serverList = [];
        featureCtrl.featuresNameList = [];
        featureCtrl.featureToBeList = [];
        featureCtrl.featureTypeList = ['QUERY_BUILDER','REPORT_CONFIGURATION','NOTIFICATION_BUILDER','EVENT_BUILDER']
        featureCtrl.isAllSelect = false;
        featureCtrl.selectTitle = 'Select All';
        featureCtrl.sortType = 'asc';
        featureCtrl.assignSortType = 'asc';


        featureCtrl.getFeatureItems = function(callback) {
            Mask.show();
            // get feature name with UUID
            featureCtrl.get_feature_dto = {
                code: 'get_feature_name_with_uuid',
                parameters: {
                    featureType : featureCtrl.selectedFeatureType,
                    searchText : featureCtrl.search || null,
                    limit : featureCtrl.PagingService.limit,
                    offset : featureCtrl.PagingService.offSet
                }
            };

            let featureList = featureCtrl.featureToBeList;

            PagingService.getNextPage(function (get_feature_dto) {
                return QueryDAO.execute(get_feature_dto).then(function (response) {
                    return response.result;
                }).catch(function (err) {
                    return err;
                })
            }, featureCtrl.get_feature_dto, featureList, null).then(function (response) {
                featureCtrl.featureToBeList = response;
                // To select respective items of Selected Server
                callback(null,response);
                featureCtrl.changeServer();

            })
            .catch((err) => {
                callback(err,null);
                toaster.pop('danger','data fetch went wrong');
            })
            .finally(() => {
                Mask.hide();
            })
        }


        featureCtrl.init = function() {
            var dto = {
                code: 'get_active_server_list',
                parameters: {
                }
            };
            // get_active_server_list
            QueryDAO.execute(dto).then(function (res) {
                featureCtrl.serverList = res.result;
            });

        }

        featureCtrl.assignSorting = function() {
            Mask.show();

            let selectedItems = [] ;
            let nonSelectedItems = [];
            selectedItems = _.filter(featureCtrl.featureToBeList,(d) => { return d.server_names});
            nonSelectedItems = _.filter(featureCtrl.featureToBeList,(d) => { return !d.server_names || d.server_names === null || d.server_names === undefined });

            if (featureCtrl.assignSortType === 'asc') {
                featureCtrl.assignSortType = 'desc'
                featureCtrl.featureToBeList = selectedItems.concat(nonSelectedItems);
            }
            else{
                featureCtrl.assignSortType = 'asc'
                featureCtrl.featureToBeList = nonSelectedItems.concat(selectedItems);
            }
            Mask.hide();
        }



        featureCtrl.sorting = function() {
            Mask.show();
            let selectedItems = [] ;
            let nonSelectedItems = [];
            selectedItems = _.filter(featureCtrl.featureToBeList,(d) => { return d.selected === true});
            nonSelectedItems = _.filter(featureCtrl.featureToBeList,(d) => { return d.selected === false || d.selected === undefined ||  d.selected === null });
            featureCtrl.featureToBeList = [];
            if (featureCtrl.sortType === 'asc') {
                featureCtrl.sortType = 'desc'
                featureCtrl.featureToBeList = selectedItems.concat(nonSelectedItems);
            }
            else{
                featureCtrl.sortType = 'asc'
                featureCtrl.featureToBeList = nonSelectedItems.concat(selectedItems);
            }

            Mask.hide();
        }

        featureCtrl.getSearchedItem = function() {
            featureCtrl.PagingService.resetOffSetAndVariables();
            featureCtrl.featureToBeList = [];
            featureCtrl.featuresNameList = [];
            featureCtrl.getFeatureList();
        }

        featureCtrl.changeServer = function(value) {
            // Reset the All checked Item (Multi select)
            // if (!value) {
            //     _.forEach(featureCtrl.featureToBeList,(d) => {d.selected = false});
            // }

            // Check feature_item based on selected server (Multiple)
            // if (featureCtrl.featureToBeList) {
            //     _.forEach(featureCtrl.featureToBeList,(data) => {
            //         _.forEach(featureCtrl.selectedServer,(ser) => {
            //             if(data.server_names && data.server_names.includes(ser.server_name)){
            //                 data.selected = true;
            //             }
            //         })

            //     });
            // }

            // (Single)
            // if (featureCtrl.featureToBeList) {
            //     _.forEach(featureCtrl.featureToBeList,(data) => {
            //         if(data.server_names && data.server_names.includes(featureCtrl.selectedServer.server_name)){
            //             data.selected = true;
            //         }
            //     });
            // }
        }

        featureCtrl.select = function() {
            var filter = $filter('filter');
            var filtered = filter(featureCtrl.featureToBeList, featureCtrl.search);
            if (featureCtrl.selectTitle === 'Select All') {
                featureCtrl.selectTitle = 'Deselect All';

                _.forEach(featureCtrl.featureToBeList,(d) => {
                    _.forEach(filtered,(f) => {
                        if (f.feature_uuid === d.feature_uuid) {
                            d.selected = true;
                        }
                    })
                });
            }
            else{
                featureCtrl.selectTitle = 'Select All';
                _.forEach(featureCtrl.featureToBeList,(d) => {
                    _.forEach(filtered,(f) => {
                        if (f.feature_uuid === d.feature_uuid) {
                            d.selected = false;
                        }
                    })
                });
            }
        }

        featureCtrl.getFeatureList = function () {
            featureCtrl.getFeatureItems((err,data) => {
                if (!err) {

                }
            });
        }

        featureCtrl.changeFeatureType = function() {
            // De-deselect previously selected of removed feature type (Multiple)
            // _.forEach(featureCtrl.featuresNameList,(d) => {
            //     if (!(featureCtrl.selectedFeatureType && featureCtrl.selectedFeatureType.includes(d.feature_type))) {
            //         d.selected = false;
            //     }
            // });


            // (Multiple)

            // _.forEach(featureCtrl.featureToBeList,(d) => {
            //     _.forEach(featureCtrl.featuresNameList,(f) => {
            //         if (d.feature_uuid === f.feature_uuid && featureCtrl.selectedFeatureType.includes(d.feature_type) && d.selected === true) {
            //             f.selected = d.selected;
            //         }
            //     })
            // })

            // // Again filter on available feature type
            // featureCtrl.featureToBeList = _.filter(featureCtrl.featuresNameList,(d) => {
            //     return featureCtrl.selectedFeatureType && featureCtrl.selectedFeatureType.includes(d.feature_type);
            // });

            // featureCtrl.changeServer('no need to clear existing item');

            featureCtrl.PagingService.resetOffSetAndVariables();
            featureCtrl.featureToBeList = [];
            featureCtrl.featuresNameList = [];
            featureCtrl.getFeatureList();

        }

        featureCtrl.cancel =  function() {
            window.history.back();
        }

        featureCtrl.saveAllconfiguration = function (type) {
            if (!featureCtrl.feature_sync_form.$invalid) {
                var modalInstance = $uibModal.open({
                    templateUrl: 'app/common/views/confirmation.modal.html',
                    controller: 'ConfirmModalController',
                    windowClass: 'cst-modal',
                    size: 'med',
                    resolve: {
                        message: function () {
                            return "Are you sure you want to <b>" + type + "</b> all the selected feature Items?";
                        }
                    }
                });
                modalInstance.result.then(function () {
                    Mask.show();
                    let bodyData = [];

                    if (featureCtrl.selectTitle === 'Deselect All') {
                        let dto = {
                            featureUUID: null,
                            serverlist: [featureCtrl.selectedServer.id], // Single Select
                            type: featureCtrl.selectedFeatureType,
                            selectAll : true
                        }
                        bodyData.push(dto);
                    }
                    else{
                        let concateServerIds = [];
                        featureCtrl.featuresNameList = [];
                        featureCtrl.featuresNameList = featureCtrl.featureToBeList; // for single selection
                        featureCtrl.featuresNameList.forEach(element => {
                            // let concateServerIds = _.map(featureCtrl.selectedServer, (d) => { return d.id }); // Multiple
                            concateServerIds = [];
                            concateServerIds.push(featureCtrl.selectedServer.id); // Single
                            if (type === 'Sync' && element.server_names) {
                                concateServerIds = concateServerIds.concat(element.server_ids.split(',').map(Number));
                                concateServerIds = _.filter(_.uniq(concateServerIds), (d) => { return d != null });
                            }

                            if (element.selected) {
                                let dto = {
                                    featureUUID: element.feature_uuid,
                                    serverlist: concateServerIds,
                                    type: element.feature_type,
                                    selectAll : false
                                }
                                bodyData.push(dto);
                            }
                            // concateServerIds = [];
                        });
                    }

                    Mask.show();
                    if (bodyData.length > 0) {
                        $http.post(APP_CONFIG.apiPath + `/server/save/sync-with-server/${type}/mass`, bodyData).then((response) => {


                            featureCtrl.PagingService.resetOffSetAndVariables();
                            featureCtrl.featureToBeList = [];
                            featureCtrl.featuresNameList = [];

                            featureCtrl.getFeatureItems((err,data) => {
                                if (!err) {
                                    toaster.pop('success', "Updated Successfully!");
                                }
                            });
                        })
                        .catch((err) => {
                            toaster.pop('danger', "Insertion failed");
                        })
                        .finally(()=>{
                            Mask.hide();
                        })
                    }
                    else {
                        toaster.pop('info', "Please Select atleast one item ");
                    }

                }).catch(function () {
                }).finally(function () {
                    Mask.hide();
                });
            }
        }

        featureCtrl.init();

    }

    angular.module('imtecho.controllers').controller('FeatureSyncController', featureSyncController);
})()
