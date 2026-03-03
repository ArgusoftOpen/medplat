(function () {
    function RchDataMigrationCtrl(QueryDAO, Mask, AuthenticateService, GeneralUtil) {
        var ctrl = this;
        ctrl.requestList = [];

        ctrl.fetchList = function (locationId) {
            if (!locationId) {
                locationId = null;
            }
            var dto = {
                code: 'rch_data_migration_fetchlist',
                parameters: {
                    locationId: locationId,
                    userId: ctrl.userDetail.id
                }
            };
            Mask.show();
            QueryDAO.executeQuery(dto).then(function (res) {
                ctrl.requestList = res.result;
            }).catch(function (err) {
                GeneralUtil.showMessageOnApiCallFailure(err);
            }).finally(function () {
                Mask.hide();
            })
        }

        AuthenticateService.getLoggedInUser().then(function (res) {
            ctrl.userDetail = res.data;
            ctrl.fetchList();
        });

        ctrl.submit = function () {
            ctrl.fetchList(ctrl.locationId);
            ctrl.toggleFilter();
        }

        ctrl.toggleFilter = function () {
            if (angular.element('.filter-div').hasClass('active')) {
                angular.element('body').css("overflow", "auto");
            } else {
                angular.element('body').css("overflow", "hidden");
            }
            angular.element('.cst-backdrop').fadeToggle();
            angular.element('.filter-div').toggleClass('active');
        }
    }
    angular.module('imtecho.controllers').controller('RchDataMigrationCtrl', RchDataMigrationCtrl);
})();
