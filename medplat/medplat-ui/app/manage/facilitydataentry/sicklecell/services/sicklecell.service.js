(function () {
    function SicklecellService($resource, APP_CONFIG) {
        var api = $resource(APP_CONFIG.apiPath + '/sicklecell/:action/:subaction/:id', {}, {
            create: {
                method: 'POST',
            },
        });
        return {
            create: (sicklecellDto) => {
                return api.create({}, sicklecellDto).$promise;
            }
        };
    }
    angular.module('imtecho.service').factory('SicklecellService', ['$resource', 'APP_CONFIG', SicklecellService]);
})();
