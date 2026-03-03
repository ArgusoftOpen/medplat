(function () {
    function MigrationDAO($resource, APP_CONFIG) {
        var api = $resource(APP_CONFIG.apiPath + '/migration/:action/:id', {}, {});
        return {
            retrieveSimilarmembers: function (migrationId) {
                return api.query({ action: 'similarmembers', id: migrationId }).$promise
            },
            confirmMember: function (migrationId, member) {
                return api.save({ action: 'confirmMember', id: migrationId }, member).$promise;
            },
            createNewTemp: function (migrationId) {
                return api.save({ action: 'temporaryMember', id: migrationId }, {}).$promise;
            },
            retrieveMigratedIn: function () {
                return api.query({ 'action': 'migratedIn' }).$promise;
            },
            searchMembers: function (criteria) {
                return api.query(criteria).$promise
            }
        };
    }
    angular.module('imtecho.service').factory('MigrationDAO', MigrationDAO);
})();
