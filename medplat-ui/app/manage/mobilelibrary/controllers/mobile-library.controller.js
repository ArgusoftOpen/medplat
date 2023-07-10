(function () {
    function MobileLibraryController(RoleDAO, APP_CONFIG, Mask, GeneralUtil, $rootScope, toaster, UploadDocumentService, $uibModal) {
        var ctrl = this;
        ctrl.isFilterOpen = false;
        ctrl.category = null;
        ctrl.fileName = null;
        ctrl.fileExtention = null;
        ctrl.isFormatError = false;
        ctrl.isSizeError = false;
        ctrl.accessToken = $rootScope.authToken;
        ctrl.apiPath = APP_CONFIG.apiPath;
        ctrl.isUploadFileError = false;
        ctrl.folderHierarchy = {};
        ctrl.levels = [];

        var init = function () {
            Mask.show();
            UploadDocumentService.retreiveAll().then(function (res) {
                ctrl.queryResult = res;
                res.forEach((categoryObject) => {
                    if (categoryObject.category) {
                        var splitedArray = categoryObject.category.split("/");
                        for (var i = 0; i < splitedArray.length; i++) {
                            ctrl.folderHierarchy[i] = ctrl.folderHierarchy[i] || [];
                            if (i == 0) {
                                if (!ctrl.folderHierarchy[i].includes(splitedArray[i]))
                                    (ctrl.folderHierarchy[i]).push(splitedArray[i]);
                            }
                        }
                    }
                });
                ctrl.levels = [];
                if (ctrl.folderHierarchy[0] == undefined)
                    ctrl.folderHierarchy[0] = [];
                ctrl.levels.push({ folderNames: ctrl.folderHierarchy[0] });
                Mask.hide();
            }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                Mask.hide();
            });
            $(document).ready(function () {
                $('input[name="tags"]').tagsinput({
                });
            });
        }

        ctrl.resetFields = function () {
            ctrl.category = null;
            ctrl.fileName = null;
            ctrl.fileExtention = null;
            ctrl.isFormatError = false;
            ctrl.isSizeError = false;
            ctrl.flow = null;
            ctrl.isUploadFileError = false;
            ctrl.folderHierarchy = {};
            ctrl.levels = [];
            ctrl.folderPath = '';
            ctrl.description = '';
            ctrl.tags = '';
            $("#tags").tagsinput('removeAll');
        }

        ctrl.retrieveRoleList = function () {
            RoleDAO.retireveAll(true).then(function (res) {
                ctrl.roleList = res;
            });
        };

        /**
         * To get folder list
         */
        ctrl.getFolderHierarchy = function (folderName, index) {
            ctrl.levels.length = index + 1;
            var folderPath = '';
            if (folderName == undefined) {
                //'select'
                for (let i = 0; i < ctrl.levels.length - 1; i++) {
                    if (i != ctrl.levels.length - 2)
                        folderPath += ctrl.levels[i].selectedFolder + "/";
                    else
                        folderPath += ctrl.levels[i].selectedFolder;
                }
            }
            else {
                for (let i = 0; i < ctrl.levels.length; i++) {
                    if (i != ctrl.levels.length - 1)
                        folderPath += ctrl.levels[i].selectedFolder + "/";
                    else
                        folderPath += ctrl.levels[i].selectedFolder;
                }
            }

            ctrl.folderPath = folderPath;
            var patternedArray = ctrl.queryResult.filter((libraryObject) => {
                return libraryObject.category.match("^" + folderPath);
            });
            var subFolders = [];
            var files = [];
            patternedArray.forEach((categoryObject) => {
                if (categoryObject.category) {
                    var splitedArray = categoryObject.category.split("/");
                    if (splitedArray[index + 1]) {
                        if (!subFolders.includes(splitedArray[index + 1]))
                            subFolders.push(splitedArray[index + 1]);
                    }
                }
                if (categoryObject.category == ctrl.folderPath && !files.includes(categoryObject.fileName))
                    files.push(categoryObject);
            });

            ctrl.fileNames = files;
            if (folderName && subFolders.length != 0)
                ctrl.levels.push({ folderNames: subFolders });
        };

        /**
         * To create new folder
         */
        ctrl.createFolder = function () {
            if (!ctrl.folderName) {
                toaster.pop("warning", "Please enter folder name!");
                return;
            }
            if (ctrl.levels[ctrl.levels.length - 1].selectedFolder) {
                ctrl.levels.push({ folderNames: [ctrl.folderName] });
            } else {
                var isDuplicate = false;
                var tempFolderNames = ctrl.levels[ctrl.levels.length - 1].folderNames;
                tempFolderNames.forEach((name) => {
                    if (name.toLowerCase() == ctrl.folderName.toLowerCase()) {
                        isDuplicate = true;
                        toaster.pop("error", "Folder with same name already exists");
                    }
                });
                if (!tempFolderNames.includes(ctrl.folderName) && !isDuplicate)
                    ctrl.levels[ctrl.levels.length - 1].folderNames.push(ctrl.folderName);
            }
            $("#actionModal").modal('hide');

        };

        /**
         * To change the state status
         */
        ctrl.toggleState = function (file) {
            let changedState;
            if (file.state === "ACTIVE") {
                changedState = 'Unpublished';
            } else {
                changedState = 'Published';
            }

            var currentStateMsg = file.state == 'ACTIVE' ? 'Published' : 'Unpublished';
            var modalInstance = $uibModal.open({
                templateUrl: 'app/common/views/confirmation.modal.html',
                controller: 'ConfirmModalController',
                windowClass: 'cst-modal',
                size: 'med',
                resolve: {
                    message: function () {
                        return "Are you sure you want to change the state from " + currentStateMsg + ' to ' + changedState + '?';
                    }
                }
            });
            modalInstance.result.then(function () {
                Mask.show();
                UploadDocumentService.updateStateStatus(file.fileName, changedState == 'Published' ? 'ACTIVE' : 'INACTIVE').then(function () {
                    file.state = changedState == 'Published' ? 'ACTIVE' : 'INACTIVE';
                    ctrl.queryResult.filter((libraryObj) => {
                        return libraryObj.fileName == file.fileName;
                    })[0].state = file.state;
                    toaster.pop('success', 'File state changed from ' + currentStateMsg + ' to ' + changedState + ' successfully');
                }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                    Mask.hide();
                });
            });
        };

        ctrl.uploadFile = {
            singleFile: true,
            testChunks: false,
            allowDuplicateUploads: true,
            chunkSize: 50 * 1024 * 1024,
            headers: {
                Authorization: 'Bearer ' + ctrl.accessToken
            },
            uploadMethod: 'POST'
        };

        ctrl.upload = function ($file, $event, $flow) {
            ctrl.isUploadFileError = false;
            ctrl.fileExtention = $file.getExtension();
            ctrl.fileSize = $file.size;
            $flow.opts.target = APP_CONFIG.apiPath + '/upload/document';
            $flow.opts.query = { path: ctrl.folderPath };
            if ($file.size > 52428800) {
                ctrl.isSizeError = true;
                $flow.cancel();
                return;
            } else if ($file.getExtension() === "pdf"
                || $file.getExtension() === "mp4" || $file.getExtension() === "mp3"
                || $file.getExtension() === "png" || $file.getExtension() === "jpg") {
                ctrl.isFormatError = false;
                ctrl.isSizeError = false;
            } else {
                ctrl.isFormatError = true;
                $flow.cancel();
                return;
            }
        }

        ctrl.uploadFn = function ($files, $event, $flow) {
            Mask.show();
            ctrl.flow = ($flow);
            if (!ctrl.isFormatError && !ctrl.isSizeError) {
                ctrl.flow.upload();
            } else {
                ctrl.flow.cancel();
            }
            Mask.hide();
        }

        ctrl.getUploadResponse = function ($file, $message, $flow) {
            ctrl.isUploadFileError = false;
            ctrl.fileName = $message;
        };

        ctrl.removeDocument = function (fileName) {
            if (fileName) {
                UploadDocumentService.removeDocument(fileName).then(function (response) {
                    toaster.pop("success", fileName + " is successfully deleted");
                    ctrl.removeFileFromFlow(fileName);
                });
            }
        }

        ctrl.removeFileFromFlow = function (fileName) {
            if (ctrl.flow != undefined) {
                for (var i = 0; i < ctrl.flow.files.length; i++) {
                    if (ctrl.flow.files[i].name === fileName) {
                        ctrl.flow.files.splice(i, 1);
                    }
                }
                ctrl.fileName = null;
                ctrl.fileExtention = null;
            }
        }

        ctrl.uploadDocumentFile = function (form) {
            if (!ctrl.folderPath) {
                toaster.pop("warning", "Please select main folder!");
                return;
            }
            if (!ctrl.flow) {
                toaster.pop("warning", "Please upload file!");
                return;
            }
            if (ctrl.flow && (ctrl.fileName === null || ctrl.fileName === "")) {
                ctrl.isUploadFileError = true;
                return;
            } else
                ctrl.isUploadFileError = false;

            if (ctrl.uploadFileForm.$invalid)
                return;
            var mobileLibrabyObj = {}

            mobileLibrabyObj.category = ctrl.folderPath;
            mobileLibrabyObj.fileName = ctrl.fileName;
            mobileLibrabyObj.fileType = ctrl.fileExtention;
            mobileLibrabyObj.description = ctrl.description;
            mobileLibrabyObj.tag = ctrl.tags
            mobileLibrabyObj.state = "ACTIVE";
            Mask.show();
            UploadDocumentService.uploadDocumentFile(mobileLibrabyObj).then(function (res) {
                toaster.pop('success', 'Record submitted successfully');
                ctrl.resetFields();
                init();
                ctrl.uploadFileForm.$setPristine();
            }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                Mask.hide();
            })
        }

        ctrl.closeModal = function () {
            $("#actionModal").modal('hide');
            ctrl.folderName = '';
        }

        ctrl.openModal = function () {
            $("#actionModal").modal({ backdrop: 'static', keyboard: false });
            ctrl.folderName = '';
        }

        ctrl.toggleFilter = function (fileObject) {
            if (fileObject)
            {
                $("#tagstoggle").tagsinput('removeAll');
                ctrl.fileObject = fileObject;
                if(fileObject.tag){
                    var arr = fileObject.tag.split(',');
                let elt = $('#tagstoggle');
                $(elt).tagsinput();
                arr.map((val, index) => {
                    $(elt).tagsinput('add', val);
                  })
                }
            }
            ctrl.isFilterOpen = !ctrl.isFilterOpen;
        };

        ctrl.updateDescription = function () {
            if (ctrl.editDiscriptionForm.$valid) {
                Mask.show();
                ctrl.fileObject.tag = ctrl.fileObject.tags;
                UploadDocumentService.updateMobileLibrary(ctrl.fileObject).then(function (res) {
                    toaster.pop('success', 'Description updated successfully');
                    ctrl.queryResult.filter((libraryObj) => {
                        return libraryObj.fileName == ctrl.fileObject.fileName;
                    })[0].description = ctrl.fileObject.description;
                    ctrl.toggleFilter();
                }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                    Mask.hide();
                })
            }

        }

        init();
    }
    angular.module('imtecho.controllers').controller('MobileLibraryController', MobileLibraryController);
})();
