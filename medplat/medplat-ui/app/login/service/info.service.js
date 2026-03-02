'use strict';
(function () {
    function InfoService($resource) {
        var api = $resource('/api/mobile/getdata', {},
            {
                submit: {
                    method: 'POST',
                    params: {
                        action: 'submit'
                    },
                    transformResponse: function (res) {
                        return {
                            result: res
                        };
                    }
                }
            });
        return {
            submit: function (submitObj) {
                return api.submit(submitObj).$promise;
            }
        };
    }
    angular.module('imtecho.service').factory('InfoService', InfoService);
})();
