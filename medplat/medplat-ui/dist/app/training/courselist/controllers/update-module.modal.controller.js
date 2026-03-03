(function (angular) {
    var UpdateModuleController = function ($uibModal, $uibModalInstance, topics, toaster, AuthenticateService, APP_CONFIG, $rootScope, Mask, GeneralUtil, TimelineConfigDAO, $filter) {
        let $ctrl = this;
        // MAX FILE SIZE IN BYTES (DECIMAL)
        $ctrl.maxImageSize = 1000000; // 1MB
        $ctrl.maxVideoSize = 50000000; // 50MB
        $ctrl.maxPdfSize = 5000000; // 5MB
        $ctrl.maxAudioSize = 10000000; // 10MB
        $ctrl.topicForUpdate = {
            topicOrder: topics[0].length + 1
        };
        $ctrl.allowedVideoExts = ['mp4', 'avi', 'mpg', '3gp', 'mov'];
        $ctrl.allowedPdfExts = ['pdf'];
        $ctrl.allowedAudioExts = ['m4a', 'flac', 'mp3', 'wav', 'wma', 'aac'];
        $ctrl.allowedImageExts = ['jpeg', 'jpg', 'png', 'gif'];
        $ctrl.accessToken = $rootScope.authToken;
        $ctrl.apiPath = APP_CONFIG.apiPath;
        $ctrl.topicsList = topics[0];
        $ctrl.isShowControls = false;
        $ctrl.isShowAdd = true;

        $ctrl.sortLessons = (lessons) => {
            let sorted = $filter('orderBy')(lessons, ['mediaOrder']);
            return sorted;
        }

        if (topics[1] === true) {
            $ctrl.addFlag = topics[1];
            $ctrl.media = {
                mediaOrder: 1,
                pdfObject: {},
                videoObject: {
                    video: {},
                    pdf: {}
                },
                audioObject: {},
                imageObject: {}
            };
        } else {
            $ctrl.topicForUpdate = angular.copy(topics[1]);
            $ctrl.topicForUpdate.mediaDataList = $ctrl.sortLessons($ctrl.topicForUpdate.mediaDataList);
            $ctrl.media = {
                mediaOrder: Array.isArray($ctrl.topicForUpdate.mediaDataList) ? $ctrl.topicForUpdate.mediaDataList.length + 1 : 1,
                pdfObject: {},
                videoObject: {
                    video: {},
                    pdf: {}
                },
                audioObject: {},
                imageObject: {}
            };
        }

        $ctrl.uploadFile = {
            singleFile: true,
            testChunks: false,
            allowDuplicateUploads: true,
            chunkSize: 10 * 1024 * 1024 * 1024,
            headers: {
                Authorization: 'Bearer ' + AuthenticateService.getToken()
            },
            uploadMethod: 'POST'
        };

        $ctrl.upload = ($file, $event, $flow, media, type) => {
            $ctrl.responseMessage = {};
            media.isError = false;
            switch (type) {
                case 'PDF':
                    if ($ctrl.allowedPdfExts.indexOf($file.getExtension()) < 0) {
                        toaster.pop('danger', `${$file.getExtension()} format is not supported. Please upload file having extension ${$ctrl.allowedPdfExts.toString()}`);
                        media.isError = true;
                    }
                    break;
                case 'VIDEO':
                    if ($ctrl.allowedVideoExts.indexOf($file.getExtension()) < 0) {
                        toaster.pop('danger', `${$file.getExtension()} format is not supported. Please upload video having extension ${$ctrl.allowedVideoExts.toString()}`);
                        media.isError = true;
                    }
                    break;
                case 'AUDIO':
                    if ($ctrl.allowedAudioExts.indexOf($file.getExtension()) < 0) {
                        toaster.pop('danger', `${$file.getExtension()} format is not supported. Please upload audio having extension ${$ctrl.allowedAudioExts.toString()}`);
                        media.isError = true;
                    }
                    break;
                case 'IMAGE':
                    if ($ctrl.allowedImageExts.indexOf($file.getExtension()) < 0) {
                        toaster.pop('danger', `${$file.getExtension()} format is not supported. Please upload image having extension ${$ctrl.allowedImageExts.toString()}`);
                        media.isError = true;
                    }
                    break;
            }
            if (media.isError) {
                $flow.cancel();
                return false;
            }
            $ctrl.mediaType = type ? type : 'NONE';
            $flow.opts.target = APP_CONFIG.apiPath + '/document/uploaddocument/TRAINING/false';
        };

        $ctrl.uploadFn = ($files, $event, $flow, media, type) => {
            let sizeErr = false;
            switch ($ctrl.mediaType) {
                case 'IMAGE':
                    if($files[0].size > $ctrl.maxImageSize) {
                        $ctrl.popSizeError($ctrl.maxImageSize);
                        $flow.cancel();
                        sizeErr = true;
                    }
                    break;
                case 'VIDEO':
                    if($files[0].size > $ctrl.maxVideoSize) {
                        $ctrl.popSizeError($ctrl.maxVideoSize);
                        $flow.cancel();
                        sizeErr = true;
                    }
                    break;
                case 'PDF':
                    if($files[0].size > $ctrl.maxPdfSize) {
                        $ctrl.popSizeError($ctrl.maxPdfSize);
                        $flow.cancel();
                        sizeErr = true;
                    }
                    break;
                case 'AUDIO':
                    if($files[0].size > $ctrl.maxAudioSize) {
                        $ctrl.popSizeError($ctrl.maxAudioSize);
                        $flow.cancel();
                        sizeErr = true;
                    }
                    break;
            }

            if (!media.isError && !sizeErr) {
                Mask.show();
                AuthenticateService.refreshAccessToken().then(() => {
                    $flow.opts.headers.Authorization = 'Bearer ' + AuthenticateService.getToken();
                    media.flow = ($flow);
                    $flow.upload();
                    $ctrl.isUploading = true;
                    if($ctrl.mediaType && $ctrl.mediaType == "VIDEO" && $ctrl.size == null) {
                        $ctrl.videoSize = $flow.files[0].file.size;
                    } else {
                        $ctrl.size = $flow.files[0].file.size;
                    }
                }).catch((error) => {
                    GeneralUtil.showMessageOnApiCallFailure(error)
                }).finally(() => {
                    Mask.hide();
                });
            }
        };

        $ctrl.popSizeError = (maxSize) => {
            toaster.pop('info', `${$ctrl.mediaType} file size must be less than or equal to ${maxSize/1000000}MB`);
            return;
        }

        $ctrl.getUploadResponse = ($file, $message, $flow, media) => {
            $ctrl.isUploading = false;
            media.mediaName = JSON.parse($message).id;
            media.originalMediaName = $file.name;
        };

        $ctrl.fileError = ($file, $message, media) => {
            media.flow.files = [];
            $ctrl.isUploading = false;
            toaster.pop('danger', 'Error in file upload.');
        };

        $ctrl.removeFile = (media) => {
            if (media.mediaName) {
                $ctrl.isRemove = true;
                TimelineConfigDAO.removeFile(media.mediaName).then(() => {
                    if (!!media.flow && !!media.flow.files) {
                        media.flow.files = [];
                    }
                    media.mediaName = undefined;
                    media.originalMediaName = undefined;
                    if($ctrl.mediaType && $ctrl.mediaType == "VIDEO" && $ctrl.videoSize) { $ctrl.videoSize = null; }
                    if($ctrl.size) { $ctrl.size = null; }
                }).catch((error) => {
                    GeneralUtil.showMessageOnApiCallFailure(error);
                }).finally(() => {
                    Mask.hide();
                    $ctrl.isRemove = false;
                });
            }
        };

        const _checkIfIsModuleDuplicate = function () {
            if ($ctrl.addFlag) {
                if (!$ctrl.topicsList || $ctrl.topicsList.length === 0) {
                    return false
                }
                return $ctrl.topicsList.find(topic => topic.topicName === $ctrl.topicForUpdate.topicName) ? true : false;
            }
            if ($ctrl.topicsList.length === 1) {
                return false;
            }
            return $ctrl.topicsList.find(topic => {
                if (!!topic.isNew) {
                    return (topic.topicName === $ctrl.topicForUpdate.topicName && topic.topicOrder !== $ctrl.topicForUpdate.topicOrder) ? true : false;
                } else {
                    return (topic.topicName === $ctrl.topicForUpdate.topicName && topic.topicId !== $ctrl.topicForUpdate.topicId) ? true : false;
                }
            });      
        }

        const _findDuplicateMedia = function () {
            if (!$ctrl.topicForUpdate.mediaDataList || $ctrl.topicForUpdate.mediaDataList.length < 2) {
                return [];
            }
            let mediaNameArr = $ctrl.topicForUpdate.mediaDataList.map(media => media.title);
            return $ctrl.topicForUpdate.mediaDataList.map((media, mediaIdx) => mediaNameArr.indexOf(media.title) != mediaIdx ? mediaIdx : undefined).filter(x => x);
        }

        $ctrl.ok = () => {
            $ctrl.topicUpdateForm.$setSubmitted();
            $ctrl.isModuleDuplicate = _checkIfIsModuleDuplicate();
            $ctrl.duplicateMedia = _findDuplicateMedia();
            if ($ctrl.duplicateMedia.length && $ctrl.isModuleDuplicate) {
                toaster.pop('error', `Modules or Lessons with same name are not allowed.`);
                return;
            } else if ($ctrl.isModuleDuplicate) {
                toaster.pop('error', `Modules with same name are not allowed.`);
                return;
            } else if ($ctrl.duplicateMedia.length) {
                toaster.pop('error', `Lessons with same name are not allowed.`);
                return;
            }
            if ($ctrl.topicUpdateForm.$valid) {
                if (Array.isArray($ctrl.topicForUpdate.mediaDataList) && $ctrl.topicForUpdate.mediaDataList.length) {
                    $ctrl.topicForUpdate.mediaDataList.forEach((media) => {
                        media.editMode = false;
                    });
                    if ($ctrl.addFlag) {
                        $ctrl.topicForUpdate.isNew = true;
                        $ctrl.topicsList.push($ctrl.topicForUpdate);
                        toaster.pop('success', 'Module saved successfully');
                        $uibModalInstance.close($ctrl.topicsList);
                    } else {
                        $uibModalInstance.close($ctrl.topicForUpdate);
                    }
                } else {
                    toaster.pop('error', 'Please add atleast one lesson to save the module');
                }
            }
        };

        $ctrl.cancel = () => {
            $uibModalInstance.dismiss('cancel');
        };

        $ctrl.addLesson = () => {
            $ctrl.topicUpdateForm.$setSubmitted();
            if (!$ctrl.media.pdfObject.originalMediaName && !$ctrl.media.videoObject.video.originalMediaName && !$ctrl.media.audioObject.originalMediaName && !$ctrl.media.imageObject.originalMediaName) {
                let mediaType = $ctrl.media.mediaType ? $ctrl.media.mediaType : 'video or pdf or audio or image';
                toaster.pop('danger', `Please upload ${mediaType}`);
                return;
            }
            if ($ctrl.topicUpdateForm.$valid) {
                if (!Array.isArray($ctrl.topicForUpdate.mediaDataList)) {
                    $ctrl.topicForUpdate.mediaDataList = [];
                }
                if ($ctrl.videoSize != null) {
                    $ctrl.media.size = ($ctrl.size) ? ($ctrl.size + $ctrl.videoSize) : $ctrl.videoSize;
                } else {
                    $ctrl.media.size = $ctrl.size;
                }
                $ctrl.topicForUpdate.mediaDataList.push($ctrl.media);
                $ctrl.media = {
                    mediaOrder: $ctrl.topicForUpdate.mediaDataList.length + 1,
                    pdfObject: {},
                    videoObject: {
                        video: {},
                        pdf: {}
                    },
                    audioObject: {},
                    imageObject: {}
                };
                $ctrl.isShowControls = false;
                $ctrl.isShowAdd = true;
                $ctrl.topicUpdateForm.$setPristine();
                $ctrl.topicForUpdate.mediaDataList = $ctrl.sortLessons($ctrl.topicForUpdate.mediaDataList);
                if($ctrl.videoSize) { $ctrl.videoSize = null; }
                if($ctrl.size) { $ctrl.size = null; }
            }
        }

        $ctrl.cancelLesson = () => {
            $ctrl.media = {
                mediaOrder: Array.isArray($ctrl.topicForUpdate.mediaDataList) ? $ctrl.topicForUpdate.mediaDataList.length + 1 : 1,
                pdfObject: {},
                videoObject: {
                    video: {},
                    pdf: {}
                },
                audioObject: {},
                imageObject: {}
            };
            $ctrl.isShowControls = false;
            $ctrl.isShowAdd = true;
            $ctrl.topicUpdateForm.$setPristine();
        }

        $ctrl.mediaTypeChanged = () => {
            $ctrl.removeFile($ctrl.media.pdfObject);
            $ctrl.removeFile($ctrl.media.videoObject.video);
            $ctrl.removeFile($ctrl.media.videoObject.pdf);
            $ctrl.removeFile($ctrl.media.audioObject);
            $ctrl.removeFile($ctrl.media.imageObject);
        }

        $ctrl.editLesson = (index) => {
            $ctrl.topicForUpdate.mediaDataList[index].editMode = true;
        }

        $ctrl.updateLesson = (index) => {
            if ($ctrl.topicForUpdate.mediaDataList[index].title && $ctrl.topicForUpdate.mediaDataList[index].description) {
                $ctrl.topicForUpdate.mediaDataList[index].editMode = false;
            }
        }

        $ctrl.deleteLesson = (media) => {
            var modalInstance = $uibModal.open({
                templateUrl: 'app/common/views/confirmation.modal.html',
                controller: 'ConfirmModalController',
                windowClass: 'cst-modal',
                size: 'med',
                resolve: {
                    message: function () {
                        return "Are you sure to remove this lesson?";
                    }
                }
            });
            modalInstance.result.then(() => {
                const maxMedia = $ctrl.topicForUpdate.mediaDataList.reduce((previous, current) => {
                    return previous.mediaOrder > current.mediaOrder ? previous : current;
                });
                $ctrl.topicForUpdate.mediaDataList = $ctrl.topicForUpdate.mediaDataList.filter(m => m.mediaOrder !== media.mediaOrder);
                $ctrl.media = {
                    mediaOrder: Array.isArray($ctrl.topicForUpdate.mediaDataList) ? $ctrl.topicForUpdate.mediaDataList.length + 1 : 1,
                    pdfObject: {},
                    videoObject: {
                        video: {},
                        pdf: {}
                    },
                    audioObject: {},
                    imageObject: {}
                };
                for (let i = media.mediaOrder + 1; i <= maxMedia.mediaOrder; i++) {
                    const currentMedia = $ctrl.topicForUpdate.mediaDataList.find(m => m.mediaOrder === i);
                    currentMedia.mediaOrder--;
                }
                $ctrl.topicForUpdate.mediaDataList = $ctrl.sortLessons($ctrl.topicForUpdate.mediaDataList);
            });
        }

        $ctrl.incrementLesson = (media) => {
            let previousMedia = $ctrl.topicForUpdate.mediaDataList.find(m => m.mediaOrder === media.mediaOrder - 1);
            media.mediaOrder--;
            previousMedia.mediaOrder++;
            $ctrl.topicForUpdate.mediaDataList = $ctrl.sortLessons($ctrl.topicForUpdate.mediaDataList)
        }

        $ctrl.decrementLesson = (media) => {
            let nextMedia = $ctrl.topicForUpdate.mediaDataList.find(m => m.mediaOrder === media.mediaOrder + 1);
            media.mediaOrder++;
            nextMedia.mediaOrder--;
            $ctrl.topicForUpdate.mediaDataList = $ctrl.sortLessons($ctrl.topicForUpdate.mediaDataList)
        }
    };
    angular.module('imtecho.controllers').controller('UpdateModuleController', UpdateModuleController);
})(window.angular);
