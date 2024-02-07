(function () {
    function NcdMoSpecialistMemberDAO($resource, APP_CONFIG) {
        var api = $resource(APP_CONFIG.apiPath + '/ncd/:action/:subaction/:id', {}, {
            saveEcgData: {
                method: 'POST',
                params: {
                    action: 'saveEcgData',
                },
                transformResponse: function (res) {
                    return {
                        data: res
                    };
                }
            },
            saveStrokeData: {
                method: 'POST',
                params: {
                    action: 'saveStrokeData',
                },
                transformResponse: function (res) {
                    return {
                        data: res
                    };
                }
            },
            saveAmputationData: {
                method: 'POST',
                params: {
                    action: 'saveAmputationData',
                },
                transformResponse: function (res) {
                    return {
                        data: res
                    };
                }
            },
            saveRenalData: {
                method: 'POST',
                params: {
                    action: 'saveRenalData',
                },
                transformResponse: function (res) {
                    return {
                        data: res
                    };
                }
            }
        });
        return {
            saveEcgData: function (data) {
                return api.saveEcgData({}, data).$promise;
            },
            saveStrokeData: function (data) {
                return api.saveStrokeData({}, data).$promise;
            },
            saveAmputationData: function (data) {
                return api.saveAmputationData({}, data).$promise;
            },
            saveRenalData: function (data) {
                return api.saveRenalData({}, data).$promise;
            },
            retakeEcgData: function (data) {
                return api.retakeEcgData({}, data).$promise;
            }
        };
    }
    angular.module('imtecho.service').factory('NcdMoSpecialistMemberDAO', NcdMoSpecialistMemberDAO);
})();
