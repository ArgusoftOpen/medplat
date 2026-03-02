'use strict';
(function () {
    function UploadDocumentService($resource, APP_CONFIG) {
        var api = $resource(APP_CONFIG.apiPath + '/upload/document/:action/:fileName', {}, {
            removeDocument: {
                method: 'DELETE',
                params: {
                    action: 'delete'
                }
            },
            uploadDocumentFile: {
                method: 'POST',
                params: {
                    action: 'createmobilelibrarydocument'
                }
            },
            getFolderHierarchy: {
                method: 'GET',
                params: {
                    action: 'getFolderHierarchy'
                }
            },
            createFolder: {
                method: 'POST',
                params: {
                    action: 'createFolder'
                }
            },
            updateStateStatus: {
                method: 'PUT',
                params: {
                    action: 'updateStateStatus'
                }
            },
            retreiveAll:
            {
                method: 'GET',
                params: {
                    action: 'retreiveAll'
                },
                isArray: true
            },
            update: {
                method: 'PUT',
                params: {
                    action: 'update'
                }
            }
        });
        return {
            removeDocument: function (file) {
                return api.removeDocument({ file: file }).$promise;
            },
            uploadDocumentFile: function (submitObj) {
                return api.uploadDocumentFile(submitObj).$promise;
            },
            getFolderHierarchy: function (folderPath) {
                return api.getFolderHierarchy({ path: folderPath }).$promise;
            },
            createFolder: function (folderPath, folderName) {
                return api.createFolder({ path: folderPath, folderName: folderName }).$promise;
            },
            updateStateStatus: function (file, state) {
                return api.updateStateStatus({ fileName: file, state: state }).$promise;
            },
            retreiveAll: function () {
                return api.retreiveAll().$promise;
            },
            uploadDocument: function () {
                return api.uploadDocument().$promise;
            },
            updateMobileLibrary: function (mobileLibraryObject) {
                return api.update(mobileLibraryObject).$promise;
            }
        };
    }
    angular.module('imtecho.service').factory('UploadDocumentService', UploadDocumentService);
})();
