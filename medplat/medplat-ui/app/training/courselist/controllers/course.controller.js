(function () {
    function CourseController(CourseService, toaster, GeneralUtil, $filter, Mask, $uibModal, $timeout, RoleDAO, $stateParams, $state, QueryDAO, $q, AuthenticateService, APP_CONFIG, DrTechoDAO, SohElementConfigurationDAO ,$sce) {
        let ctrl = this;
        ctrl.allowedFileExtns = ['jpg', 'png', 'jpeg'];
        ctrl.filePayload = { 'COURSE': {} };
        ctrl.isFileUploading = { 'COURSE': false };
        ctrl.isFileRemoving = { 'COURSE': {} };
        ctrl.isFileUploadProcessing = { 'COURSE': {} };
        ctrl.testConfig = {};
        ctrl.oldTestConfig = {};
        ctrl.courseImageConfig = {};
        // MAX FILE SIZE IN BYTES (DECIMAL)
        ctrl.maxImageSize = 1000000; // 1MB

        ctrl.fileUploadOptions = {
            singleFile: true,
            testChunks: false,
            allowDuplicateUploads: false,
            simultaneousUploads: 1,
            chunkSize: 10 * 1024 * 1024 * 1024,
            headers: {
                Authorization: 'Bearer ' + AuthenticateService.getToken()
            },
            uploadMethod: 'POST'
        };

        ctrl.onFileAdded = function ($file, $event, $flow, typeId) {
            if (!ctrl.allowedFileExtns.includes($file.getExtension())) {
                ctrl.filePayload[typeId].isError = true;
                ctrl.filePayload[typeId].errorMessage = `Only .${ctrl.allowedFileExtns.join(', .')} files supported!`;
                $event.preventDefault();
                return false;
            }
            delete ctrl.filePayload[typeId].errorMessage;
            ctrl.filePayload[typeId].isError = false;
            $flow.opts.target = `${APP_CONFIG.apiPath}/document/uploaddocument/TRAINING/false`;
        };

        ctrl.onFileSubmitted = function ($files, $event, $flow, typeId) {
            let sizeErr = false;
            if($files[0].size > ctrl.maxImageSize) {
                sizeErr = true;
                toaster.pop('info', `IMAGE file size must be less than or equal to ${ctrl.maxImageSize/1000000}MB`);
                $flow.cancel();
            }
            if (!$files || $files.length === 0 || sizeErr) {
                return;
            }

            Mask.show();
            AuthenticateService.refreshAccessToken().then(function () {
                $flow.opts.headers.Authorization = 'Bearer ' + AuthenticateService.getToken();
                ctrl.filePayload[typeId].flow = ($flow);
                $flow.upload();
                if (!ctrl.filePayload[typeId].isError) {
                    ctrl.isFileUploading[typeId] = true;
                    $files.forEach(file => ctrl.isFileUploadProcessing[typeId][file.name] = true)
                }
            }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                Mask.hide();
            });
        };

        ctrl.onFileSuccess = function ($file, $message, $flow, typeId) {
            ctrl.isFileUploading[typeId] = false;
            ctrl.isFileUploadProcessing[typeId][$file.name] = false;
            let fileDetails = {};
            try {
                fileDetails = JSON.parse($message);
            } catch (error) {
                console.log(error);
            }
            if (typeId === 'COURSE') {
                ctrl.courseImageConfig.mediaId = fileDetails.id;
                ctrl.courseImageConfig.mediaExtension = fileDetails.extension;
                ctrl.courseImageConfig.mediaName = fileDetails.actualFileName;
                return;
            }
            let dataObj = ctrl.testConfig[typeId].quizCompletionMessage ? ctrl.testConfig[typeId].quizCompletionMessage : {};
            dataObj.mediaId = fileDetails.id;
            dataObj.mediaExtension = fileDetails.extension;
            dataObj.mediaName = fileDetails.actualFileName;
            ctrl.testConfig[typeId].quizCompletionMessage = dataObj;
        };

        ctrl.onFileError = function ($file, $message, typeId) {
            ctrl.isFileUploading[typeId] = false;
            ctrl.isFileUploadProcessing[typeId][$file.name] = false;
            ctrl.filePayload[typeId].flow.files = ctrl.filePayload[typeId].flow.files.filter(e => e.name !== $file.name);
            toaster.pop('danger', 'Error in file upload!');
        };

        function _removeFile(dataObj, typeId, fileName) {
            if (ctrl.filePayload[typeId].flow && ctrl.filePayload[typeId].flow.files) {
                ctrl.filePayload[typeId].flow.files = ctrl.filePayload[typeId].flow.files
                    .filter(e => e.name !== fileName);
            }
            dataObj.mediaId = null;
            dataObj.mediaExtension = null;
            dataObj.mediaName = null;
            ctrl.isFileUploadProcessing[typeId] = {};
        }

        ctrl.onRemoveFile = function (typeId, fileName) {
            let dataObj = {}
            if (typeId === 'COURSE') {
                dataObj = ctrl.courseImageConfig;
            } else if (ctrl.testConfig[typeId].quizCompletionMessage) {
                dataObj = ctrl.testConfig[typeId].quizCompletionMessage;
            }
            if (dataObj.mediaId) {
                ctrl.isFileRemoving[typeId][fileName] = true;
                DrTechoDAO.removeFile(dataObj.mediaId)
                    .then(function () {
                        _removeFile(dataObj, typeId, fileName);
                    }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                        ctrl.isFileRemoving[typeId][fileName] = false;
                    });
            } else {
                _removeFile(dataObj, typeId, fileName);
            }
        };

        ctrl.viewImageModal = function (dataObj) {
            let modalInstance = $uibModal.open({
                templateUrl: 'app/training/courselist/views/view-image.modal.html',
                controllerAs: 'viewImageModalCtrl',
                controller: function ($uibModalInstance) {
                    let viewImageModalCtrl = this;
                    viewImageModalCtrl.id = dataObj.mediaId;
                    viewImageModalCtrl.title = dataObj.mediaName;

                    const _init = function () {
                        Mask.show();
                        SohElementConfigurationDAO.getFileById(viewImageModalCtrl.id).then(res => {
                            viewImageModalCtrl.isFileUploaded = true;
                            viewImageModalCtrl.attachmentImage = URL.createObjectURL(res.data)
                        }).catch(err => {
                            if (err.status === 404) {
                                toaster.pop('error', "Image not found!");
                            } else {
                                GeneralUtil.showMessageOnApiCallFailure(error);
                            }
                            viewImageModalCtrl.attachmentImage = null;
                        }).finally(() => {
                            Mask.hide();
                        });
                    }

                    viewImageModalCtrl.ok = function () {
                        $uibModalInstance.dismiss('cancel');
                    }

                    _init();
                },
                windowClass: 'cst-modal',
                size: 'xl',
                resolve: {
                }
            });
            modalInstance.result.then(function () { }, function (err) { });
        }

        ctrl.viewDefaultImageModal = function (dataObj) {
            let modalInstance = $uibModal.open({
                templateUrl: 'app/training/courselist/views/view-image.modal.html',
                controllerAs: 'viewImageModalCtrl',
                controller: function ($uibModalInstance) {
                    let viewImageModalCtrl = this;
                    const _init = function () {
                        if (dataObj) {
                            viewImageModalCtrl.attachmentImage = dataObj
                        }
                        else {
                            viewImageModalCtrl.attachmentImage = null
                        }
                    }
                    viewImageModalCtrl.ok = function () {
                        $uibModalInstance.dismiss('cancel');
                    }
                    _init();
                },
                windowClass: 'cst-modal',
                size: 's',
                resolve: {
                }
            });
            modalInstance.result.then(function () { }, function (err) { });
        }

        ctrl.onChangeAllowReviewBeforeSubmission = (selectedSetTypeConfig) => {
            if (selectedSetTypeConfig.provideInteractiveFeedbackAfterEachQuestion && selectedSetTypeConfig.allowReviewBeforeSubmission) {
                toaster.pop('error', "Please first set \"No\" to the question \"Provide interactive feedback after each question?\".");
                $timeout(() => { selectedSetTypeConfig.allowReviewBeforeSubmission = false; });
            }
        }

        ctrl.onChangeProvideInteractiveFeedbackAfterEachQuestion = (selectedSetTypeConfig) => {
            if (selectedSetTypeConfig.provideInteractiveFeedbackAfterEachQuestion && selectedSetTypeConfig.allowReviewBeforeSubmission) {
                toaster.pop('error', "Please first set \"No\" to the question \"Allow review of questions before submission?\".");
                $timeout(() => { selectedSetTypeConfig.provideInteractiveFeedbackAfterEachQuestion = false; });
            }
        }

        ctrl.init = () => {
            ctrl.courseObj = {};
            ctrl.topics = [];
            ctrl.courseFormSubmitted = false;

            let promises = [];
            promises.push(QueryDAO.execute({
                code: 'fetch_listvalue_detail_from_field',
                parameters: {
                    field: 'LMS Question Set types'
                }
            }));
            promises.push(RoleDAO.retireveAll(true));
            promises.push(QueryDAO.execute({
                code: 'get_course_module_name'
            }));
            Mask.show();
            $q.all(promises).then(responses => {
                ctrl.questionSetTypes = responses[0].result;
                ctrl.testTypeId = null;
                ctrl.questionSetTypes.forEach(type => {
                    ctrl.filePayload[type.id] = {};
                    ctrl.isFileUploading[type.id] = false;
                    ctrl.isFileRemoving[type.id] = {};
                    ctrl.isFileUploadProcessing[type.id] = {};
                    ctrl.testConfig[type.id] = {
                        isCaseStudyQuestionSetType: false,
                        doYouWantAQuizToBeMarked: false,
                        allowReviewBeforeSubmission: true,
                        quizCompletionMessage: {
                            message: "Well Done! You have completed the Quiz",
                            mediaId: null,
                            mediaExtension: null,
                            mediaName: null
                        },
                        showQuizScoreQuizCompletion: true,
                        noOfMaximumAttempts: null,
                        provideOptionToLockTheQuiz: false,
                        provideOptionToRestartTestFromCompletionScreen: true,
                        showTimeTakenToCompleteTheQuiz: true,
                        provideInteractiveFeedbackAfterEachQuestion: false
                    }
                })
                ctrl.roles = responses[1];
                ctrl.moduleConstants = responses[2].result;
                ctrl.isUpdateForm = false;
                if ($stateParams.id) {
                    ctrl.isUpdateForm = true;
                    ctrl.getCoursebyId($stateParams.id);
                } else {
                    ctrl.courseObj.isAllowedToSkipLessons = false;
                }
                ctrl.validCourseName = true;
            }).catch(GeneralUtil.showMessageOnApiCallFailure).finally(Mask.hide);
        };

        ctrl.getCoursebyId = (id) => {
            Mask.show();
            CourseService.getCourseById(id).then((response) => {
                ctrl.courseObj = response;
                ctrl.tempCourseName = response.courseName;
                if (ctrl.courseObj.courseType === 'OFFLINE') {
                    ctrl.topics = ctrl.orderTopics(response.topicMasterDtos);
                    ctrl.count = _.groupBy(ctrl.topics, 'topicDay');
                } else {
                    ctrl.topics = ctrl.orderTopics(ctrl.transformTopicMediaListToMediaDataList(response.topicMasterDtos));
                }
                if (ctrl.courseObj.courseImageJson) {
                    try {
                        ctrl.courseImageConfig = JSON.parse(ctrl.courseObj.courseImageJson);
                    } catch (error) {
                        console.log('error while parsing JSON testConfig ::: ', error);
                    }
                }
                if (ctrl.courseObj.testConfigJson) {
                    try {
                        let parsedTestConfig = JSON.parse(ctrl.courseObj.testConfigJson);
                        ctrl.oldTestConfig = angular.copy(parsedTestConfig);
                        for (const typeId in ctrl.testConfig) {
                            if (parsedTestConfig[typeId]) {
                                for (const que in ctrl.testConfig[typeId]) {
                                    if (parsedTestConfig[typeId][que] !== undefined) {
                                        ctrl.testConfig[typeId][que] = parsedTestConfig[typeId][que];
                                    }
                                }
                            }
                        }
                    } catch (error) {
                        console.log('error while parsing JSON testConfig ::: ', error);
                    }
                }
            }).catch((error) => {
                GeneralUtil.showMessageOnApiCallFailure(error);
            }).finally(() => {
                Mask.hide();
            });
        }

        const _checkTestConfigQuestionValidation = () => {
            ctrl.showTestConfigQuestionValidationMessage = false;
            ctrl.testConfigQuestionValidationMessage = `Oops! Both <strong>Allow review of questions before submission?</strong> and
            <strong>Provide interactive feedback after each question?</strong>
            fields can't be <strong>Yes</strong> at the same time for `;
            for (const typeId in ctrl.testConfig) {
                if (ctrl.testConfig[typeId].provideInteractiveFeedbackAfterEachQuestion && ctrl.testConfig[typeId].allowReviewBeforeSubmission) {
                    ctrl.showTestConfigQuestionValidationMessage = true;
                    ctrl.testConfigQuestionValidationMessage += (ctrl.questionSetTypes.find(type => type.id == typeId).value + " ");
                }
            }
            ctrl.testConfigQuestionValidationMessage += "question type(s).";
            return ctrl.showTestConfigQuestionValidationMessage ? false : true;
        }

        const _findDuplicateModules = function (topics) {
            let moduleNameArr = topics.map(topic => topic.topicName);
            return topics.map((topic, topicIdx) => moduleNameArr.indexOf(topic.topicName) != topicIdx ? topicIdx : undefined).filter(x => x);
        }

        const _findDuplicateMedia = function (topics) {
            let mediaNameDupArr = [];
            topics.forEach((topic, topicIdx) => {
                let mediaNameArr = topic.mediaDataList.map(media => media.title);
                if (topic.mediaDataList.filter((media, mediaIdx) => mediaNameArr.indexOf(media.title) != mediaIdx).length) {
                    mediaNameDupArr.push(topicIdx)
                }
            })
            return mediaNameDupArr;
        }

        ctrl.checkCourseName = function (courseName) {
            QueryDAO.execute({
                code: 'verify_course_name',
                parameters: {
                    courseName: courseName,
                    courseId: Number($stateParams.id ? $stateParams.id : null),
                }
            }).then((res) => {
                if(res.result.length > 0){
                    ctrl.validCourseName = false;
                } else {
                    ctrl.validCourseName = true;
                }
            }).catch((error) => {
                GeneralUtil.showMessageOnApiCallFailure(error);
            });
        }

        ctrl.createCourse = () => {
            ctrl.courseForm.$setSubmitted();
            if (ctrl.courseForm.$valid && _checkTestConfigQuestionValidation() && ctrl.validCourseName) {
                if (Array.isArray(ctrl.topics) && ctrl.topics.length) {
                    if (ctrl.courseObj.courseType === 'ONLINE') {
                        ctrl.topics = ctrl.transformMediaDataListToTopicMediaList(ctrl.topics);
                        ctrl.courseObj.testConfigJson = JSON.stringify(ctrl.testConfig);
                    } else {
                        ctrl.courseObj.testConfigJson = null;
                    }
                    ctrl.courseObj.courseImageJson = JSON.stringify(ctrl.courseImageConfig);
                    let topics = angular.copy(ctrl.topics);
                    let course = angular.copy(ctrl.courseObj);
                    ctrl.duplicateTopicArr = _findDuplicateModules(topics);
                    if (ctrl.courseObj.courseType === 'ONLINE') {
                        ctrl.duplicateMediaArr = _findDuplicateMedia(topics);
                    }
                    if ((Array.isArray(ctrl.duplicateTopicArr) && ctrl.duplicateTopicArr.length) || (Array.isArray(ctrl.duplicateMediaArr) && ctrl.duplicateMediaArr.length)) {
                        toaster.pop('error', 'Modules or Lessons with same name are not allowed.');
                        return;
                    }
                    course.topicMasterDtos = topics;
                    course.topicMasterDtos.forEach(topic => {
                        delete topic.mediaDataList
                        delete topic.isNew
                    });
                    if (ctrl.courseObj.courseDescription === null || ctrl.courseObj.courseDescription === '') {
                        course.courseDescription = ctrl.courseObj.courseName;
                    }
                    Mask.show();
                    CourseService.createCourse(course).then(() => {
                        toaster.pop('success', 'Course created successfully!');
                        $state.go('techo.manage.courselist');
                    }).catch((error) => {
                        GeneralUtil.showMessageOnApiCallFailure(error);
                    }).finally(() => {
                        Mask.hide();
                    });
                } else if (ctrl.courseObj.courseType === 'ONLINE') {
                    toaster.pop('error', 'Atleast one module is required');
                } else {
                    toaster.pop('error', 'Atleast one topic is required');
                }
            }
        };

        ctrl.addTopic = () => {
            let modalInstance = $uibModal.open({
                templateUrl: ctrl.courseObj.courseType === 'OFFLINE' ? 'app/training/courselist/views/update-course.modal.html' : 'app/training/courselist/views/update-module.modal.html',
                controller: ctrl.courseObj.courseType === 'OFFLINE' ? 'UpdateModalController' : 'UpdateModuleController',
                controllerAs: '$ctrl',
                windowClass: 'cst-modal',
                backdrop: 'static',
                size: 'xl',
                resolve: {
                    topics: () => {
                        return [ctrl.topics, true];
                    }
                }
            });
            modalInstance.result.then((topicsList) => {
                ctrl.topics = topicsList;
                if (ctrl.topics.length > 0) {
                    $timeout(() => {
                        $(".header-fixed").tableHeadFixer();
                    });
                }
                ctrl.topics = ctrl.orderTopics(ctrl.topics);
                if (ctrl.courseObj.courseType === 'OFFLINE') {
                    ctrl.count = _.groupBy(ctrl.topics, 'topicDay');
                }
            }, () => {
            });
        };

        ctrl.deleteTopic = (topic) => {
            let modalInstance = $uibModal.open({
                templateUrl: 'app/common/views/confirmation.modal.html',
                controller: 'ConfirmModalController',
                windowClass: 'cst-modal',
                size: 'med',
                resolve: {
                    message: () => {
                        return `Are you sure you want to delete ${topic.topicName}?`;
                    }
                }
            });
            modalInstance.result.then(() => {
                const maxTopic = ctrl.topics.reduce((previous, current) => {
                    return previous.topicOrder > current.topicOrder ? previous : current;
                });
                if (ctrl.courseObj.courseType === 'OFFLINE') {
                    ctrl.topics = ctrl.topics.filter((t) => {
                        return t.topicDay !== topic.topicDay && t.topicOrder !== topic.topicOrder && t.topicName !== topic.topicName;
                    });
                } else {
                    ctrl.topics = ctrl.topics.filter(t => t.topicOrder !== topic.topicOrder);
                    for (let i = topic.topicOrder + 1; i <= maxTopic.topicOrder; i++) {
                        const currentTopic = ctrl.topics.find(t => t.topicOrder === i);
                        currentTopic.topicOrder--;
                    }
                }
                ctrl.topics = ctrl.orderTopics(ctrl.topics);
                if (ctrl.courseObj.courseType === 'OFFLINE') {
                    ctrl.count = _.groupBy(ctrl.topics, 'topicDay');
                    toaster.pop('success', 'Topic deleted successfully');
                } else {
                    toaster.pop('success', 'Module deleted successfully');
                }
            }, () => {
            });
        };

        ctrl.editTopic = (topic) => {
            let modalInstance = $uibModal.open({
                templateUrl: ctrl.courseObj.courseType === 'OFFLINE' ? 'app/training/courselist/views/update-course.modal.html' : 'app/training/courselist/views/update-module.modal.html',
                controller: ctrl.courseObj.courseType === 'OFFLINE' ? 'UpdateModalController' : 'UpdateModuleController',
                controllerAs: '$ctrl',
                windowClass: 'cst-modal',
                backdrop: 'static',
                size: 'xl',
                resolve: {
                    topics: () => {
                        return [ctrl.topics, topic];
                    }
                }
            });
            modalInstance.result.then((updatedTopic) => {
                let index;
                if (topic.topicId) {
                    index = ctrl.topics.findIndex(t => t.topicId === topic.topicId);
                } else {
                    if (ctrl.courseObj.courseType === 'OFFLINE') {
                        index = ctrl.topics.findIndex(t => t.topicOrder === topic.topicOrder && t.topicDay === topic.topicDay && t.topicName === topic.topicName);
                    } else {
                        index = ctrl.topics.findIndex(t => t.topicOrder === topic.topicOrder && t.topicName === topic.topicName);
                    }
                }
                ctrl.topics[index] = angular.copy(updatedTopic);
                ctrl.topics = ctrl.orderTopics(ctrl.topics);
                if (ctrl.courseObj.courseType === 'OFFLINE') {
                    ctrl.count = _.groupBy(ctrl.topics, 'topicDay');
                    toaster.pop('success', 'Topic updated successfully');
                } else {
                    ctrl.topics = ctrl.transformMediaDataListToTopicMediaList(ctrl.topics);
                    toaster.pop('success', 'Module updated successfully');
                }

            }, () => {
            });
        };

        ctrl.incrementTopic = (topic) => {
            let previousTopic = ctrl.topics.find(t => t.topicOrder === topic.topicOrder - 1);
            topic.topicOrder--;
            previousTopic.topicOrder++;
            ctrl.topics = ctrl.orderTopics(ctrl.topics);
        }

        ctrl.decrementTopic = (topic) => {
            let nextTopic = ctrl.topics.find(t => t.topicOrder === topic.topicOrder + 1);
            topic.topicOrder++;
            nextTopic.topicOrder--;
            ctrl.topics = ctrl.orderTopics(ctrl.topics);
        }

        ctrl.cancel = () => {
            ctrl.topics = [];
            ctrl.courseObj = {};
            ctrl.courseForm.$setPristine();
            ctrl.courseFormSubmitted = false;
        };

        ctrl.orderTopics = (topic) => {
            let sorted = [];
            if (ctrl.courseObj.courseType === 'OFFLINE') {
                sorted = $filter('orderBy')(topic, ['topicDay', 'topicOrder']);
            } else {
                sorted = $filter('orderBy')(topic, ['topicOrder']);
            }
            return sorted;
        };

        let updateCourseAction = (course) => {
            Mask.show();
            CourseService.updateCourse(course).then(() => {
                toaster.pop('success', 'Course updated successfully');
                $state.go('techo.manage.courselist');
            }).catch(GeneralUtil.showMessageOnApiCallFailure).finally(Mask.hide);
        }

        let _checkIfMinimumMarksSetToYesFromNo = () => {
            for (const typeId in ctrl.testConfig) {
                if (ctrl.testConfig[typeId].doYouWantAQuizToBeMarked === true && ctrl.oldTestConfig[typeId] && ctrl.oldTestConfig[typeId].doYouWantAQuizToBeMarked === false) {
                    return true;
                }
            }
            return false;
        }

        let _checkIfCaseStudySetToYesFromNo = () => {
            for (const typeId in ctrl.testConfig) {
                if (ctrl.testConfig[typeId].isCaseStudyQuestionSetType === true && ctrl.oldTestConfig[typeId] && ctrl.oldTestConfig[typeId].isCaseStudyQuestionSetType === false) {
                    return true;
                }
            }
            return false;
        }

        ctrl.updateCourse = () => {
            ctrl.courseForm.$setSubmitted();
            if (ctrl.courseForm.$valid && _checkTestConfigQuestionValidation() && ctrl.validCourseName) {
                if (Array.isArray(ctrl.topics) && ctrl.topics.length) {
                    if (ctrl.courseObj.courseType === 'ONLINE') {
                        ctrl.topics = ctrl.transformMediaDataListToTopicMediaList(ctrl.topics);
                        ctrl.courseObj.testConfigJson = JSON.stringify(ctrl.testConfig);
                    }
                    ctrl.courseObj.courseImageJson = JSON.stringify(ctrl.courseImageConfig);
                    let topics = angular.copy(ctrl.topics);
                    let course = angular.copy(ctrl.courseObj);
                    ctrl.duplicateTopicArr = _findDuplicateModules(topics);
                    if (ctrl.courseObj.courseType === 'ONLINE') {
                        ctrl.duplicateMediaArr = _findDuplicateMedia(topics);
                    }
                    if ((Array.isArray(ctrl.duplicateTopicArr) && ctrl.duplicateTopicArr.length) || (Array.isArray(ctrl.duplicateMediaArr) && ctrl.duplicateMediaArr.length)) {
                        toaster.pop('error', 'Modules or Lessons with same name are not allowed.');
                        return;
                    }
                    course.topicMasterDtos = topics;
                    course.topicMasterDtos.forEach(topic => {
                        delete topic.mediaDataList
                        delete topic.isNew
                    });
                    if (ctrl.courseObj.courseDescription === null || ctrl.courseObj.courseDescription === '') {
                        course.courseDescription = ctrl.courseObj.courseName;
                    }
                    let isMinimumMarksSetToYesFromNo = _checkIfMinimumMarksSetToYesFromNo();
                    let isCaseStudySetToYesFromNo = _checkIfCaseStudySetToYesFromNo();
                    if (ctrl.courseObj.courseType === 'ONLINE' && (isMinimumMarksSetToYesFromNo || isCaseStudySetToYesFromNo)) {
                        let minimumMarksSetToYesFromNoMsg = "Make sure to make respective modifications to question set configurations of Minimum Marks based on updating the test configuration of this course.";
                        let caseStudySetToYesFromNoMsg = "Make sure to make respective modifications to question set configurations for Case Study type based on updating the test configuration of this course.";
                        let modalInstance = $uibModal.open({
                            templateUrl: 'app/common/views/confirmation.modal.html',
                            controller: 'ConfirmModalController',
                            windowClass: 'cst-modal',
                            size: 'med',
                            backdrop: 'static',
                            resolve: {
                                message: () => {
                                    return isCaseStudySetToYesFromNo ? caseStudySetToYesFromNoMsg : minimumMarksSetToYesFromNoMsg;
                                }
                            }
                        });
                        modalInstance.result.then(() => {
                            updateCourseAction(course);
                        }, () => { });
                    } else {
                        updateCourseAction(course);
                    }
                } else {
                    toaster.pop('error', 'Atleast one topic is required');
                }
            }
        }

        ctrl.courseTypeChanged = () => {
            ctrl.duplicateMediaArr = [];
            ctrl.duplicateTopicArr = [];
            ctrl.courseObj.trainerRoleIds = null;
        }

        ctrl.fetchQuestionSetTypeConfigs = () => {
            let promises = [];
            promises.push(QueryDAO.execute({
                code: 'fetch_listvalue_detail_from_field',
                parameters: {
                    field: 'LMS Question Set types'
                }
            })); Mask.show();
            $q.all(promises).then(responses => {
                ctrl.questionSetTypes = responses[0].result;
                ctrl.questionSetTypes.forEach(type => {
                    if (!ctrl.testConfig[type.id]) {
                        ctrl.filePayload[type.id] = {};
                        ctrl.isFileUploading[type.id] = false;
                        ctrl.isFileRemoving[type.id] = {};
                        ctrl.isFileUploadProcessing[type.id] = {};
                        ctrl.testConfig[type.id] = {
                            isCaseStudyQuestionSetType: false,
                            doYouWantAQuizToBeMarked: false,
                            allowReviewBeforeSubmission: true,
                            quizCompletionMessage: {
                                message: "Well Done! You have completed the Quiz",
                                mediaId: null,
                                mediaExtension: null,
                                mediaName: null
                            },
                            showQuizScoreQuizCompletion: true,
                            noOfMaximumAttempts: null,
                            provideOptionToLockTheQuiz: false,
                            provideOptionToRestartTestFromCompletionScreen: true,
                            showTimeTakenToCompleteTheQuiz: true,
                            provideInteractiveFeedbackAfterEachQuestion: false
                        }
                    }
                })
            }).catch(GeneralUtil.showMessageOnApiCallFailure).finally(Mask.hide);
        }

        ctrl.manageQuestionSetType = () => {
            let modalInstance = $uibModal.open({
                templateUrl: 'app/common/views/list-value.html',
                controller: 'listValueModalController',
                windowClass: 'cst-modal',
                size: 'lg',
                backdrop: 'static',
                resolve: {
                    listFieldKey: () => 'lms_question_set_types',
                    title: () => 'Manage Question Set Configuration'
                }
            });
            modalInstance.result.then(function () {
                ctrl.fetchQuestionSetTypeConfigs();
            }, function () {
            });
        }

        ctrl.transformMediaDataListToTopicMediaList = (topicsList) => {
            topicsList.forEach(topic => {
                if (Array.isArray(topic.mediaDataList) && topic.mediaDataList.length) {
                    let topicMediaList = []
                    topic.mediaDataList.forEach(media => {
                        let obj = {};
                        obj.id = media.id;
                        obj.title = media.title;
                        obj.description = media.description;
                        obj.isUserFeedbackRequired = media.isUserFeedbackRequired;
                        obj.mediaType = media.mediaType;
                        obj.mediaOrder = media.mediaOrder;
                        obj.size = media.size;
                        switch (media.mediaType) {
                            case 'PDF':
                                obj.mediaId = media.pdfObject.mediaName;
                                obj.mediaFileName = media.pdfObject.originalMediaName;
                                topicMediaList.push(obj);
                                break;
                            case 'VIDEO':
                                obj.mediaId = media.videoObject.video.mediaName;
                                obj.mediaFileName = media.videoObject.video.originalMediaName;
                                obj.transcriptFileId = media.videoObject.pdf.mediaName;
                                obj.transcriptFileName = media.videoObject.pdf.originalMediaName;
                                topicMediaList.push(obj);
                                break;
                            case 'AUDIO':
                                obj.mediaId = media.audioObject.mediaName;
                                obj.mediaFileName = media.audioObject.originalMediaName;
                                topicMediaList.push(obj);
                                break;
                            case 'IMAGE':
                                obj.mediaId = media.imageObject.mediaName;
                                obj.mediaFileName = media.imageObject.originalMediaName;
                                topicMediaList.push(obj);
                                break;
                        }
                    });
                    topic.topicMediaList = topicMediaList;
                } else {
                    topic.topicMediaList = [];
                }
            });
            return topicsList;
        }

        ctrl.transformTopicMediaListToMediaDataList = (topicsList) => {
            topicsList.forEach(topic => {
                topic.topicOrder = Number(topic.topicOrder);
                if (Array.isArray(topic.topicMediaList) && topic.topicMediaList.length) {
                    let mediaDataList = []
                    topic.topicMediaList.forEach(media => {
                        let obj = {};
                        obj.id = media.id;
                        obj.title = media.title;
                        obj.description = media.description;
                        obj.isUserFeedbackRequired = media.isUserFeedbackRequired;
                        obj.mediaType = media.mediaType;
                        obj.mediaOrder = media.mediaOrder;
                        obj.mediaName = media.mediaId;
                        obj.originalMediaName = media.mediaFileName;
                        switch (media.mediaType) {
                            case 'PDF':
                                media.pdfObject = obj;
                                break;
                            case 'VIDEO':
                                let videoObject = {
                                    video: {},
                                    pdf: {}
                                }
                                videoObject.id = media.id;
                                videoObject.title = media.title;
                                videoObject.description = media.description;
                                videoObject.isUserFeedbackRequired = media.isUserFeedbackRequired;
                                videoObject.mediaType = media.mediaType;
                                videoObject.mediaOrder = media.mediaOrder;
                                videoObject.video.mediaName = media.mediaId;
                                videoObject.video.originalMediaName = media.mediaFileName;
                                videoObject.pdf.mediaName = media.transcriptFileId;
                                videoObject.pdf.originalMediaName = media.transcriptFileName;
                                media.videoObject = videoObject;
                                break;
                            case 'AUDIO':
                                media.audioObject = obj;
                                break;
                            case 'IMAGE':
                                media.imageObject = obj;
                                break;
                        }
                        mediaDataList.push(media);
                    });
                    topic.mediaDataList = mediaDataList;
                } else {
                    topic.mediaDataList = [];
                }
            });
            return topicsList;
        }

        //Add code for preview overview
        ctrl.previewOverview = (dataObj, image) => {
            let modalInstance = $uibModal.open({
                templateUrl: 'app/training/courselist/views/course-preview.modal.html',
                controllerAs: 'coursePreviewModalCtrl',
                controller: function ($uibModalInstance) {
                    let coursePreviewModalCtrl = this;
                    coursePreviewModalCtrl.id = image.mediaId;
                    coursePreviewModalCtrl.title = dataObj.courseName;
                    coursePreviewModalCtrl.image = image.mediaName;
                    coursePreviewModalCtrl.courseName = dataObj.courseName;
                    coursePreviewModalCtrl.courseDesc = dataObj.courseDescription;
                    coursePreviewModalCtrl.overviewName = dataObj.courseName;
                    coursePreviewModalCtrl.overviewDesc = dataObj.courseDescription;

                    const _init = function () {
                        coursePreviewModalCtrl.topics = ctrl.topics
                        coursePreviewModalCtrl.selectedTab = "overview"
                        coursePreviewModalCtrl.selectedTab1 = "Pending"
                        coursePreviewModalCtrl.mediaType = "initial"
                        if (coursePreviewModalCtrl.id) {
                            Mask.show();
                            SohElementConfigurationDAO.getFileById(coursePreviewModalCtrl.id).then(res => {
                                coursePreviewModalCtrl.isFileUploaded = true;
                                coursePreviewModalCtrl.attachmentImage = URL.createObjectURL(res.data)
                                coursePreviewModalCtrl.overviewImage = URL.createObjectURL(res.data)
                            }).catch(err => {
                                if (err.status === 404) {
                                    toaster.pop('error', "Image not found!");
                                } else {
                                    GeneralUtil.showMessageOnApiCallFailure(error);
                                }
                                coursePreviewModalCtrl.attachmentImage = null;
                            }).finally(() => {
                                Mask.hide();
                            });
                        }
                    }

                    coursePreviewModalCtrl.setImageOverview = function (item) {
                        coursePreviewModalCtrl.mediaType = angular.lowercase(item.mediaType);
                        let id = item.mediaId;
                        if (!item.mediaId) {
                            switch (item.mediaType) {
                                case 'PDF':
                                    id = item.pdfObject.mediaName;
                                    break;
                                case 'VIDEO':
                                    id = item.videoObject.video.mediaName;
                                    break;
                                case 'AUDIO':
                                    id = item.audioObject.mediaName;
                                    break;
                                case 'IMAGE':
                                    id = item.imageObject.mediaName;
                                    break;
                            }
                        }
                        SohElementConfigurationDAO.getFileById(id).then(res => {
                            if (coursePreviewModalCtrl.mediaType == 'pdf') {
                                var file = new Blob([res.data], { type: 'application/pdf' });
                                var fileURL = URL.createObjectURL(file);
                                coursePreviewModalCtrl.overviewImage = $sce.trustAsResourceUrl(fileURL);
                            } else {
                                coursePreviewModalCtrl.overviewImage = URL.createObjectURL(res.data)
                            }
                        }).catch(err => {
                            coursePreviewModalCtrl.overviewImage = null;
                        })

                        coursePreviewModalCtrl.overviewName = item.title;
                        coursePreviewModalCtrl.overviewDesc = item.description;
                    }

                    coursePreviewModalCtrl.ok = function () {
                        $uibModalInstance.dismiss('cancel');
                    }

                    _init();
                },
                windowClass: 'cst-modal',
                size: 'xl',
                resolve: {
                }
            });
            modalInstance.result.then(function () { }, function (err) { });
        };

        ctrl.setTestTypeValue = function () {
            ctrl.questionSetTypes.filter(value => {
                if (value.id === ctrl.testTypeId) {
                    ctrl.testTypeValue = value.value;
                }
            })
        }

        ctrl.init();
    }
    angular.module('imtecho.controllers').controller('CourseController', CourseController);
})();
