angular.module("imtecho.components")
.component("referral", {
    templateUrl: 'app/ncd/refferedpatientsdnhdd/components/referral/referral.html',
    bindings: {
        referralDto: '=',
        singleComponentForm: '=',
        institutionType: '<',
        fetchLevels: '<',
        infrastructureId: '<'
    },
controllerAs: 'referral',
controller: function ($scope, GeneralUtil, QueryDAO, Mask) {
    const referral = this;
    referral.fetchHealthInfra = function(){
        Mask.show()
            QueryDAO.execute({
                code: 'retrieve_health_infra_by_level',
                parameters: {
                    infrastructureType: referral.infrastructureId,
                    filter: referral.fetchLevels
                }
            }).then(function (res) {
                referral.healthInfraTypes = res.result;
                referral.healthInfraIds = referral.healthInfraTypes.map(item => item.health_infra_type_id);
                // referral.showHealthInfrastructures = true;
                QueryDAO.execute({
                    code: 'health_infras_retrival_by_type',
                    parameters: {
                        types: referral.healthInfraIds
                    }
                }).then(function (res) {
                    referral.healthInfras = res.result;
                    Mask.hide()
                });
            });  
    }
}
});
