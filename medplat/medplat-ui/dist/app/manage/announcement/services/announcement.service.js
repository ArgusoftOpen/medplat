(function () {
    function AnnouncementDAO($resource, APP_CONFIG) {
        var api = $resource(APP_CONFIG.apiPath + '/announcement/:action/:subaction/:id', {},
            {
                createOrUpdate: {
                    method: 'POST',
                    isArray: true

                },
                removeFile: {
                    method: 'DELETE',
                    params: {
                        action: 'remove'
                    }
                },
                retrieveById: {
                    method: 'GET'
                },
                toggleStatusById: {
                    method: 'PUT'
                },
                retrieveByCriteria: {
                    method: 'GET',
                    isArray: true,
                },
                downloadMedia: {
                    method: 'GET',
                    responseType: 'arraybuffer',
                    //                        transformResponse: function (data) {
                    //                            return{
                    //                                response: new Blob([data], {type: 'image/jpeg'})
                    //                            }
                    //                        }
                },
            });
        return {
            createOrUpdate: function (announcementDto) {
                return api.createOrUpdate({}, announcementDto).$promise;
            },
            removeFile: function (file) {
                return api.removeFile({ file: file }).$promise;
            },
            retrieveById: function (id) {
                return api.retrieveById({ id: id }).$promise
            },
            toggleStatusById: function (id) {
                return api.toggleStatusById({}, id).$promise
            },
            retrieveByCriteria: function (params) {
                return api.retrieveByCriteria(params, {}).$promise;
            },
            validateaoi: function (locationIds, toBeAdded) {
                return api.get({ action: 'validateaoi', locationIds: locationIds, toBeAdded: toBeAdded }).$promise;
            },
            downloadMedia: function (mediaPath, fileExtension) {
                return api.get({ action: 'downloadMedia', mediaPath: mediaPath, fileExtension: fileExtension }).$promise;
            }
        };
    }
    angular.module('imtecho.service').factory('AnnouncementDAO', ['$resource', 'APP_CONFIG', AnnouncementDAO]);
})();
