(function () {
    function ManageVolunteersController(VolunteersDAO, toaster, AuthenticateService, QueryDAO, Mask, GeneralUtil) {
        var managevolunteerscontroller = this;
        var todayDate = new Date();
        managevolunteerscontroller.currentDate = new Date(todayDate.getFullYear(), todayDate.getMonth(), 1);
        managevolunteerscontroller.volunteersDto = {};
        managevolunteerscontroller.maxDate = moment();

        var init = function () {
            Mask.show();
            AuthenticateService.getLoggedInUser().then(function (user) {
                managevolunteerscontroller.loggedInUser = user.data;
                QueryDAO.executeQuery({
                    code: 'retrieve_health_infra_for_user',
                    parameters: {
                        userId: managevolunteerscontroller.loggedInUser.id
                    }
                }).then(function (res) {
                    Mask.hide();
                    managevolunteerscontroller.institutes = res.result;

                }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                    Mask.hide();
                });
            });
        };

        managevolunteerscontroller.getData = function () {
            if (managevolunteerscontroller.volunteersDto.monthYear != null && managevolunteerscontroller.volunteersDto.healthInfrastructureId != null) {
                Mask.show();
                VolunteersDAO.retrieveData(managevolunteerscontroller.volunteersDto.healthInfrastructureId, moment(managevolunteerscontroller.volunteersDto.monthYear).valueOf()).then(function (res) {
                    if (res.id != null) {
                        res.monthYear = new Date(res.monthYear);
                        managevolunteerscontroller.volunteersDto = res;
                        managevolunteerscontroller.volunteersDto.monthYear = new Date(res.monthYear.getFullYear(), res.monthYear.getMonth(), res.monthYear.getDate());
                        managevolunteerscontroller.disabledValue = true;
                    } else {
                        managevolunteerscontroller.volunteersDto.noOfVolunteers = null
                        managevolunteerscontroller.disabledValue = false
                    }
                }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                    Mask.hide();
                });
            }
        }

        managevolunteerscontroller.createOrUpdate = function () {
            if (managevolunteerscontroller.volunteersForm.$valid) {
                Mask.show();
                VolunteersDAO.createOrUpdate(managevolunteerscontroller.volunteersDto).then(function (res) {
                    toaster.pop('success', 'Data Submitted Successfully');
                    managevolunteerscontroller.volunteersDto = {}
                    managevolunteerscontroller.volunteersForm.$setPristine();
                }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                    Mask.hide();
                });
            }
        }

        init();
    }
    angular.module('imtecho.controllers').controller('ManageVolunteersController', ManageVolunteersController);
})();
