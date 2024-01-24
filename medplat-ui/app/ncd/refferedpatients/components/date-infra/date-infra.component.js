angular.module("imtecho.components")
    .component("dateinfra", {
        templateUrl: 'app/ncd/refferedpatients/components/date-infra/date-infra.html',
        bindings: {
            obj: '=',
            onChange: '&',
            onHealthInfraChange: '&',
            medicineData: '='
        },
        controllerAs: 'ctrl',
        controller: function (AuthenticateService,QueryDAO,NcdDAO) {
            var ctrl = this;
            ctrl.today = new Date();
            ctrl.today.setHours(0,0,0,0);
            ctrl.HealthInfras=[]
            ctrl.obj = {}
            ctrl.medicineData = []

            var init = function(){
                AuthenticateService.getLoggedInUser().then(function (res) {
                    ctrl.retrieveLoggedInUserHealthInfra(res.data.id);
                })
            }

            ctrl.retrieveLoggedInUserHealthInfra = function (userId) {
                if (userId) {
                    QueryDAO.execute({
                        code: 'retrieve_health_infra_for_user',
                        parameters: {
                            userId: userId
                        }
                    }).then(function (res) {
                        ctrl.HealthInfras = res.result;
                        ctrl.obj.healthInfraId = ctrl.obj.healthInfraId? ctrl.obj.healthInfraId:ctrl.HealthInfras[0].id
                        ctrl.retrieveMedicinesByHealthInfra();
                    })
                }
            }

            ctrl.retrieveMedicinesByHealthInfra = function () {
                if (ctrl.obj.healthInfraId) {
                    NcdDAO.retriveAllDrugInventory(ctrl.obj.healthInfraId, 'CFY').then(function (res) {
                        ctrl.medicineData = res
                    })
                }
            }

            ctrl.healthChange = function(){
                ctrl.onHealthInfraChange()
            }

            init();
        }
    });
