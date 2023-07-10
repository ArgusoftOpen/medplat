(function () {
    function ManageAnnouncementController(AnnouncementDAO, $filter, APP_CONFIG, $rootScope, Mask, toaster, $state, $stateParams, GeneralUtil, AuthenticateService, QueryDAO) {
        let ctrl = this;
        ctrl.env = GeneralUtil.getEnv();
        ctrl.accessToken = $rootScope.authToken;
        let previousDay = new Date();
        ctrl.updateMode = false;
        ctrl.announcementObject = {
            fromDate: new Date(),
        };
        ctrl.announcementObject.announcementType = 'no';
        ctrl.todayFormatted = $filter('date')(previousDay, 'yyyy-MM-dd');
        ctrl.allowedImageExtensions = ['gif', 'jpg', 'png', 'psd', 'tif', 'jpeg'];
        ctrl.allowedVideoExtensions = ['mp4', '3gp'];
        ctrl.allowedAudioExtensions = ['mp3'];
        ctrl.allowedPdfExtensions = ['pdf'];
        ctrl.selectedLocationsId = [];
        ctrl.validatedLocations = [];
        ctrl.healthInfraList = [];

        ctrl.getAnnouncement = function (id) {
            ctrl.selectedLocationsId = [];
            Mask.show();
            AnnouncementDAO.retrieveById(id).then(function (response) {
                ctrl.announcementObject.subject = response.subject;
                ctrl.announcementObject.fromDate = new Date(response.fromDate);
                ctrl.announcementObject.fromTime = new Date(response.fromDate);
                ctrl.announcementObject.role = response.announcementForArray;
                ctrl.sendToChanged();
                ctrl.announcementObject.healthFacilities = response.healthInfras
                ctrl.announcementObject.mediaName = response.mediaPath;
                ctrl.announcementObject.mediaExists = response.mediaExists;
                ctrl.validatedLocations = _.uniq(response.locations, 'locationId');
                ctrl.announcementObjectForUpdate = {
                    id: response.id,
                    isActive: response.isActive,
                    defaultLanguage: response.defaultLanguage,
                    createdBy: response.createdBy,
                    createdOn: response.createdOn
                };
                for (let i = 0; i < response.locations.length; i++) {
                    if (!ctrl.selectedLocationsId.includes(response.locations[i].locationId)) {
                        ctrl.selectedLocationsId.push(response.locations[i].locationId);
                    }
                }
                AuthenticateService.getLoggedInUser().then(function (res) {
                    ctrl.loggedInUser = res.data;
                }, function () { }).finally(function () {
                    Mask.hide();
                })
            }, function () {
                GeneralUtil.showMessageOnApiCallFailure();
                Mask.hide();
            });
        }

        if ($stateParams.id) {
            ctrl.updateMode = true;
            ctrl.getAnnouncement($stateParams.id);
        } else {
            Mask.show();
            AuthenticateService.getLoggedInUser().then(function (res) {
                ctrl.loggedInUser = res.data;
            }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                Mask.hide();
            })
        }

        ctrl.saveAnnouncement = function (form) {
            ctrl.checkTimeValidity();
            if (ctrl.announcementForm.$valid) {
                if (ctrl.announcementObject.subject != null && ctrl.announcementObject.role != null && ctrl.announcementObject.fromDate != null) {
                    if ((ctrl.announcementObject.announcementType === 'yes' && ctrl.announcementObject.mediaName != null) || ctrl.announcementObject.announcementType === 'no') {
                        if (ctrl.selectedLocationsId.length > 0 || ctrl.env === 'uttarakhand') {
                            ctrl.isSaving = true;
                            let announcementDto = {
                                subject: ctrl.announcementObject.subject,
                                isActive: true,
                                fromDate: ctrl.announcementObject.fromDate,
                                defaultLanguage: 'GU',
                                mediaPath: ctrl.announcementObject.mediaName,
                                fileExtension: ctrl.announcementObject.mediaExtension,
                                locations: ctrl.validatedLocations || [],
                                healthInfras: ctrl.announcementObject.healthFacilities || []
                            };
                            announcementDto.announcementForArray = [];
                            ctrl.announcementObject.role.forEach(value => announcementDto.announcementForArray.push(value));
                            if (ctrl.announcementObject.announcementType === 'yes') {
                                announcementDto.containsMultimedia = true;
                            } else {
                                announcementDto.containsMultimedia = false;
                            }

                            Mask.show();
                            AnnouncementDAO.createOrUpdate(announcementDto).then(function () {
                                ctrl.isSaving = false;
                                toaster.pop('success', 'Announcement saved!');
                                // $state.go('techo.manage.announcements');
                            }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                                Mask.hide();
                                ctrl.isUploading = false;
                                ctrl.isSaving = false
                            })
                        } else {
                            toaster.pop('danger', 'Please add atleast one area of intervention');
                        }
                    } else {
                        toaster.pop('danger', 'Please upload media');
                    }
                }
            }
        }

        ctrl.updateAnnouncement = function (form) {
            ctrl.checkTimeValidity();
            if (ctrl.announcementForm.$valid) {
                if (ctrl.announcementObject.subject != null && ctrl.announcementObject.fromDate != null) {
                    if (ctrl.selectedLocationsId.length > 0) {
                        ctrl.isSaving = true;
                        let announcementDto = {
                            id: ctrl.announcementObjectForUpdate.id,
                            subject: ctrl.announcementObject.subject,
                            fromDate: ctrl.announcementObject.fromDate,
                            locations: ctrl.validatedLocations || [],
                            healthInfras: ctrl.announcementObject.healthFacilities || [],
                            announcementForArray: ctrl.announcementObject.role,
                            isActive: ctrl.announcementObjectForUpdate.isActive,
                            defaultLanguage: 'GU',
                        };

                        Mask.show();
                        AnnouncementDAO.createOrUpdate(announcementDto).then(function () {
                            ctrl.isSaving = false;
                            toaster.pop('success', 'Announcement saved!');
                            // $state.go('techo.manage.announcements');
                        }).catch((error) => {
                            GeneralUtil.showMessageOnApiCallFailure(error);
                        }).finally(() => {
                            Mask.hide();
                            ctrl.isSaving = false;
                        })
                    } else {
                        toaster.pop('danger', 'Please add atleast one area of intervention');
                    }
                }
            }
        }

        ctrl.uploadFile = {
            singleFile: true,
            testChunks: false,
            allowDuplicateUploads: true,
            chunkSize: 10 * 1024 * 1024,
            headers: {
                Authorization: 'Bearer ' + AuthenticateService.getToken()
            },
            uploadMethod: 'POST'
        };

        ctrl.upload = function ($file, $event, $flow) {
            ctrl.responseMessage = {};
            if ((ctrl.announcementObject.mediatype == 'audio' && ctrl.allowedAudioExtensions.indexOf($file.getExtension()) < 0) || (ctrl.announcementObject.mediatype == 'video' && ctrl.allowedVideoExtensions.indexOf($file.getExtension()) < 0) || (ctrl.announcementObject.mediatype == 'image' && ctrl.allowedImageExtensions.indexOf($file.getExtension()) < 0) || (ctrl.announcementObject.mediatype === 'pdf' && ctrl.allowedPdfExtensions.indexOf($file.getExtension()) < 0)) {
                ctrl.isError = true;
                ctrl.errorMessage = 'Please select correct file to upload!';
                $flow.cancel();
                return false;
            }
            delete ctrl.errorMessage;
            ctrl.isError = false;
            ctrl.isFormatError = false;
            $flow.opts.target = APP_CONFIG.apiPath + '/announcement/upload';
        };

        ctrl.uploadFn = function ($files, $event, $flow) {
            Mask.show();
            ctrl.flow = ($flow);
            if (!ctrl.isFormatError) {
                $flow.upload();
                ctrl.isUploading = true;
            } else {
                $flow.cancel();
            }
            Mask.hide();
        };

        ctrl.getUploadResponse = function ($file, $message, $flow) {
            ctrl.isUploading = false;
            let fileName = $message;
            ctrl.uploadedFile = $file;
            ctrl.announcementObject.mediaSize = $file.size / 1000;
            ctrl.announcementObject.mediaExtension = $file.file.type;
            ctrl.announcementObject.mediaName = fileName;
        };

        ctrl.removeFile = function () {
            if (ctrl.announcementObject.mediaName) {
                AnnouncementDAO.removeFile(ctrl.announcementObject.mediaName);
            }
            ctrl.flow.files = [];
            ctrl.announcementObject.mediaName = null;
            ctrl.announcementObject.mediaExtension = null;
        }

        ctrl.announcementTypeChanged = function (value) {
            if (ctrl.flow) {
                if (ctrl.flow.files.length > 0) {
                    ctrl.removeFile();
                }
            }
        }

        ctrl.selectedArea = function () {
            ctrl.duplicateEntry = false;
            if (ctrl.selectedLocation.finalSelected !== null) {
                if (ctrl.selectedLocation.finalSelected.optionSelected) {
                    if (ctrl.selectedLocationsId.length > 0) {
                        for (let i = 0; i < ctrl.selectedLocationsId.length; i++) {
                            if (ctrl.selectedLocationsId[i] == ctrl.selectedLocation.finalSelected.optionSelected.id) {
                                ctrl.duplicateEntry = true;
                                break;
                            }
                        }
                    }
                    if (!ctrl.duplicateEntry) {
                        let selectedobj = {
                            locationId: ctrl.selectedLocation.finalSelected.optionSelected.id,
                            type: ctrl.selectedLocation.finalSelected.optionSelected.type,
                            level: ctrl.selectedLocation.finalSelected.level,
                            name: ctrl.selectedLocation.finalSelected.optionSelected.name
                        };
                        AnnouncementDAO.validateaoi(ctrl.selectedLocationsId, ctrl.selectedLocation.finalSelected.optionSelected.id).then(function (res) {
                            if (res.message.includes("Duplicacy")) {
                                ctrl.duplicateEntry = true;
                            } else {
                                ctrl.duplicateEntry = false;
                                ctrl.selectedLocationsId.push(ctrl.selectedLocation.finalSelected.optionSelected.id);
                                selectedobj.locationFullName = res.message;
                                ctrl.validatedLocations.push(selectedobj);
                            }
                        });
                    }
                }
            }
        };

        ctrl.removeSelectedArea = function (field, index) {
            ctrl.validatedLocations.splice(index, 1);
            ctrl.selectedLocationsId.splice(index, 1);
        }

        ctrl.checkTimeValidity = () => {
            if (!!ctrl.announcementObject.fromDate) {
                ctrl.announcementObject.fromDate = new Date(
                    ctrl.announcementObject.fromDate.getFullYear(),
                    ctrl.announcementObject.fromDate.getMonth(),
                    ctrl.announcementObject.fromDate.getDate(),
                    ctrl.announcementObject.fromTime.getHours(),
                    ctrl.announcementObject.fromTime.getMinutes(),
                    ctrl.announcementObject.fromTime.getMilliseconds());
                if (ctrl.announcementObject.fromDate <= moment()) {
                    ctrl.announcementForm.startTime.$setValidity('time', false);
                } else {
                    ctrl.announcementForm.startTime.$setValidity('time', true);
                }
            }
        }

        ctrl.sendToChanged = () => {
            if (ctrl.env === 'uttarakhand') {
                ctrl.announcementObject.healthFacilities = [];
                ctrl.announcementObject.sendToAll = false;
                Mask.show();
                QueryDAO.execute({
                    code: 'chardham_retrieve_health_infra_for_announcement',
                    parameters: {
                        type: ctrl.announcementObject.role || null
                    }
                }).then((response) => {
                    ctrl.healthInfraList = response.result;
                }).catch((error) => {
                    GeneralUtil.showMessageOnApiCallFailure(error);
                }).finally(() => {
                    Mask.hide();
                });
            }
        }

        ctrl.sendToAllChanged = () => {
            if (ctrl.env === 'uttarakhand') {
                if (ctrl.announcementObject.sendToAll) {
                    ctrl.announcementObject.healthFacilities = ctrl.healthInfraList.map(h => h.id);
                }
            }
        }
    }
    angular.module('imtecho.controllers').controller('ManageAnnouncementController', ManageAnnouncementController);
})();
