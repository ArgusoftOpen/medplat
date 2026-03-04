(function () {
    function MobileMenuManagementController(Mask, GeneralUtil, QueryDAO, toaster, $uibModal) {
        let ctrl = this;

        ctrl.pagingService = {
            offset: 0,
            limit: 100,
            index: 0,
            allRetrieved: false,
            pagingRetrivalOn: false
        };

        let setOffsetLimit = function () {
            ctrl.pagingService.limit = 100;
            ctrl.pagingService.offset = ctrl.pagingService.index * 100;
            ctrl.pagingService.index = ctrl.pagingService.index + 1;
        };

        let retrieveAll = function () {
            if (!ctrl.pagingService.pagingRetrivalOn && !ctrl.pagingService.allRetrieved) {
                ctrl.pagingService.pagingRetrivalOn = true;
                setOffsetLimit();
                var queryDto = {
                    code: 'mobile_menu_list',
                    parameters: {
                        limit: ctrl.pagingService.limit,
                        offset: ctrl.pagingService.offset,
                        search: ctrl.menuName
                    }
                };
                Mask.show();
                QueryDAO.executeQuery(queryDto).then(function (response) {
                    if (response.result.length === 0) {
                        ctrl.pagingService.allRetrieved = true;
                        if (ctrl.pagingService.index === 1) {
                            ctrl.menus = response.result;
                        }
                    } else {
                        ctrl.pagingService.allRetrieved = false;
                        if (ctrl.pagingService.index > 1) {
                            ctrl.menus = ctrl.menus.concat(response.result);
                        } else {
                            ctrl.menus = response.result;
                        }
                    }
                }, function (err) {
                    GeneralUtil.showMessageOnApiCallFailure(err);
                    ctrl.pagingService.allRetrieved = true;
                }).finally(function () {
                    ctrl.pagingService.pagingRetrivalOn = false;
                    Mask.hide();
                });
            }
        };

        ctrl.searchData = function (reset) {
            if (reset) {
                ctrl.pagingService.index = 0;
                ctrl.pagingService.allRetrieved = false;
                ctrl.pagingService.pagingRetrivalOn = false;
            }
            retrieveAll();
        };

        var init = function () {
            retrieveAll();
        }

        init();

        ctrl.deleteMenu = function (menuId) {
            const modalInstance = $uibModal.open({
                templateUrl: 'app/common/views/confirmation.modal.html',
                controller: 'ConfirmModalController',
                windowClass: 'cst-modal',
                size: 'med',
                resolve: {
                    message: function () {
                        return "Are you sure you want to delete this?";
                    }
                }
            });
            modalInstance.result.then(function () {
                const queryDto = {
                    code: 'delete_mobile_menu_role_relation',
                    parameters: {
                        id: menuId
                    }
                };
                Mask.show();
                QueryDAO.executeQuery(queryDto).then(function (response) {
                    ctrl.searchData(true);
                    toaster.pop('success', 'Mobile Menu Deleted Successfully.');
                }, function (err) {
                    GeneralUtil.showMessageOnApiCallFailure(err);
                }).finally(function () {
                    Mask.hide();
                });
            }, function () { });
        }
    }

    angular.module('imtecho.controllers').controller('MobileMenuManagementController', MobileMenuManagementController);
})();
