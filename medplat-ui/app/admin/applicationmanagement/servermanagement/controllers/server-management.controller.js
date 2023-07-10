(function () {
    function ServerManagementController(ServerManageDAO, $rootScope, APP_CONFIG, toaster, Mask,$uibModal) {
        var ctrl = this;
        ctrl.apiPath = APP_CONFIG.apiPath;
        ctrl.accessToken = $rootScope.authToken;
        ctrl.DBBackupFileList = [];
        ctrl.LogFileList = [];
        ctrl.isShow = false;
        if ($rootScope.loggedInUserId === 'hshah' || $rootScope.loggedInUserId === 'slamba' ||
            $rootScope.loggedInUserId === 'kkpatel' || $rootScope.loggedInUserId === 'kkpatel_t'
            || $rootScope.loggedInUserId === 'hshah_t' || $rootScope.loggedInUserId === 'slamba_t') {
            ctrl.isShow = true;
        }

        ctrl.executeCommand = function () {
            if (!!ctrl.command && !!ctrl.host && !!ctrl.userName && !!ctrl.password) {
                Mask.show();
                ServerManageDAO.executeCommand(ctrl.command, ctrl.host, ctrl.userName, ctrl.password).then(function (response) {
                    ctrl.result = response.result;
                }).catch((error) => {
                    if (error && error.data && angular.isDefined(error.data.message)) {
                        toaster.pop('danger', error.data.message);
                    }
                }).finally(function () {
                    Mask.hide();
                });
            }
        }

        /**
         * @param systemKey
         * @description Using system key get all files of folder of key value(path)
         */
        ctrl.getFilesOf = function (systemKey) {
            ServerManageDAO.retrieveKeyValueBySystemKey(systemKey).then(function (response) {
                ctrl.folderPath = response.keyValue;
                if (ctrl.folderPath != null && ctrl.folderPath !== '') {
                    if (systemKey == 'TECHO_DB_BACKUP_PATH')
                        ctrl.DBBackupFolderPath = response.keyValue;
                    else
                        ctrl.LogFolderPath = response.keyValue;
                    // Get files of folder
                    ServerManageDAO.retrieveFilesByFolderPath(ctrl.folderPath).then(function (response1) {
                        if (systemKey == 'TECHO_DB_BACKUP_PATH')
                            ctrl.DBBackupFileList = response1.fileList;
                        else
                            ctrl.LogFileList = response1.fileList;
                    });
                } else {
                    toaster.pop('danger', 'Folder path is not specified');
                }
            });
        }

        /* ctrl.download = function (filePath, fileName) {
            console.log(filePath);
            console.log(fileName);
            ServerManageDAO.download(filePath)
                .then(function (response) {
                    console.log('data', response);
                    // var anchor = angular.element('<a/>');
                    // anchor.attr({
                    //     href: response,
                    //     target: '_blank',
                    //     download: fileName
                    // })[0].click();
                }, function (error) {
                    console.log('error : ', error);
                });
        };

        ctrl.getSystemConfigByKey = function (systemKey) {
            ServerManageDAO.retrieveKeyValueBySystemKey(systemKey).then(function (response) {
                ctrl.keyValue = response.keyValue;
            });
        } */

        ctrl.getFilesOf('TECHO_DB_BACKUP_PATH');
        ctrl.getFilesOf('TECHO_LOG_FILES_PATH');
    }
    angular.module('imtecho.controllers').controller('ServerManagementController', ServerManagementController);
})();
