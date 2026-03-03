(function () {
    function ServerManageDAO($resource, APP_CONFIG) {
        var api = $resource(APP_CONFIG.apiPath + '/server/:action/:systemKey', {},
            {
                retrieveBySystemKey: {
                    method: 'GET'
                },
                getFilesOfFolderPath: {
                    method: 'GET'
                },
                download: {
                    method: 'GET'
                },
                executeCommand: {
                    method: 'POST',
                    params: {
                        action: 'execute-command-HGBYEHSE95'
                    }, transformResponse: function (res) {
                        return { result: res };
                    }
                }
            });
        return {

            retrieveKeyValueBySystemKey: function (systemKey) {
                return api.retrieveBySystemKey({ systemKey: systemKey }).$promise
            },

            retrieveFilesByFolderPath: function (folderPath) {
                return api.getFilesOfFolderPath({ action: "getFiles", 'folderPath': folderPath }).$promise
            },
            download: function (filePath) {
                return api.download({ action: "download", 'filePath': filePath }).$promise
            },
            executeCommand: function (command, host, userName, password) {
                return api.executeCommand({ 'command': command, 'host': host, 'userName': userName, 'password': password }).$promise
            }
        };
    }
    angular.module('imtecho.service').factory('ServerManageDAO', ['$resource', 'APP_CONFIG', ServerManageDAO]);
})();
