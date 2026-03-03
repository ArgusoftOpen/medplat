(function () {
    function WebTasksController(WebTasksService, PagingService, AuthenticateService, $state, Mask, toaster, $uibModal, $sessionStorage, GeneralUtil, QueryDAO) {
        var webtasks = this;
        webtasks.env = GeneralUtil.getEnv();
        webtasks.pagingService = PagingService.initialize();
        webtasks.taskBarBaskets = {};
        webtasks.taskDetailList = [];
        webtasks.taskheaders = [];
        webtasks.innerTotalCount = 0;
        webtasks.innerDueCount = 0;
        webtasks.colorCodeFlag = false;
        webtasks.colors = ['#dd4b39', '#00a65a', '#f39c12', '#00c0ef', '#333333', '#ffc90e'];

        webtasks.getLoggedInUser = function () {
            Mask.show();
            AuthenticateService.getLoggedInUser().then(function (user) {
                Mask.hide();
                webtasks.loggedInUser = user.data;
                $state.current.title = '';
                QueryDAO.execute({
                    code: 'check_if_user_login_first_time',
                    parameters: {
                        userId: webtasks.loggedInUser.id
                    }
                }).then((response) => {
                    if (!response.result[0].firstTimePasswordChanged) {
                        let modalInstance = $uibModal.open({
                            templateUrl: 'app/manage/users/views/update-password.modal.html',
                            controller: 'ResetPasswordModalController as resetPassword',
                            size: 'lg',
                            backdrop: 'static',
                            keyboard: false,
                            resolve: {
                                user: function () {
                                    return {
                                        id: webtasks.loggedInUser.id, showCancel: false,
                                        userName: webtasks.loggedInUser.userName, isFirstTimePasswordChangeModal: true
                                    };
                                }
                            }
                        });
                        modalInstance.result.then(function () {
                        }, function () {
                        });
                    }
                }).catch((error) => {
                    GeneralUtil.showMessageOnApiCallFailure(error);
                }).finally(() => {
                    Mask.hide();
                });
                webtasks.retrievePreferenceByUserId();
            }, function () {
                Mask.hide();
                toaster.pop('error', 'Unable to get user details');
            });
        }

        webtasks.retrievePreferenceByUserId = function () {
            Mask.show();
            WebTasksService.retrievePreferenceByUserId(webtasks.loggedInUser.id).then(function (response) {
                Mask.hide();
                if (response.taskbar) {
                    webtasks.showTaskBar = response.taskbar === 'show' ? true : false;
                } else {
                    webtasks.showTaskBar = true;
                }
                if (response.baskets) {
                    webtasks.hiddenBaskets = response.baskets;
                } else {
                    webtasks.hiddenBaskets = [];
                }
                webtasks.getWebTaskCount();
            }, function (error) {
                Mask.hide();
                toaster.pop('error', 'Unable to retrieve basket preference');
            });
        }

        webtasks.applyColorCodeToBaskets = function () {
            if (!webtasks.colorCodeFlag) {
                webtasks.colorCodeFlag = true;
                webtasks.taskBarBaskets.baskets.forEach(function (basket) {
                    if (basket.colorCode == null) {
                        basket.colorCode = webtasks.colors[Math.floor(Math.random() * webtasks.colors.length)];
                    }
                });
            }
        }

        webtasks.getTaskDetailWithAction = function (task) {
            webtasks.selectedTaskForAction = task
            webtasks.selectedTaskForAction.isOtherDetails = false;
            Mask.show();
            WebTasksService.getTaskDetailWithAction(task.taskId).then(function (response) {
                Mask.hide();
                webtasks.otherTasksForUser = response.other;
                webtasks.actionOptionsForSelectedTask = response.selected[0].webTasks[0].actions;
                $("#actionModal").modal({ backdrop: 'static', keyboard: false });
            }, function (error) {
                Mask.hide();
                toaster.pop('error', 'Unable to retrieve task details');
            });
        }

        webtasks.getWebTaskDetailByType = function () {
            if (webtasks.isDataFilteredOnLocation) {
                webtasks.criteria = { locationId: webtasks.locationId, taskTypeId: webtasks.currentBasket.id, limit: webtasks.pagingService.limit, offset: webtasks.pagingService.offSet };
            } else {
                webtasks.criteria = { taskTypeId: webtasks.currentBasket.id, limit: webtasks.pagingService.limit, offset: webtasks.pagingService.offSet };
            }
            var taskDetailList = webtasks.taskDetailList;
            Mask.show();
            PagingService.getNextPage(WebTasksService.getWebTaskDetailByType, webtasks.criteria, taskDetailList, null).then(function (response) {
                Mask.hide();
                webtasks.taskDetailList = response;
                if (webtasks.taskDetailList != null && webtasks.taskDetailList.length > 0) {
                    webtasks.taskheaders = Object.keys(webtasks.taskDetailList[0].details);
                }
            }, function (error) {
                Mask.hide();
                toaster.pop('error', 'Unable to retrieve task details');
            });
        }

        webtasks.saveExcel = function () {
            if (webtasks.isDataFilteredOnLocation) {
                webtasks.criteria = { locationId: webtasks.locationId, taskTypeId: webtasks.currentBasket.id, limit: null, offset: 0 };
            } else {
                webtasks.criteria = { taskTypeId: webtasks.currentBasket.id, limit: null, offset: 0 };
            }
            Mask.show();
            WebTasksService.getWebTaskDetailByType(webtasks.criteria).then(function (response) {
                Mask.hide();
                webtasks.taskDetails = [];
                if (response != null && response.length > 0) {
                    response.forEach((task) => {
                        Object.keys(task.details).forEach(function (key) {
                            if (task.details[key] === null) {
                                task.details[key] = '';
                            }
                            if (typeof task.details[key] === "boolean") {
                                task.details[key] = task.details[key] ? "Yes" : "No";
                            }
                        })
                        webtasks.taskDetails.push(task.details);
                    })
                }
                processAndDownloadExcel(webtasks.taskDetails);
            }, function (error) {
                Mask.hide();
                toaster.pop('error', 'Unable to download');
            });
        }

        var processAndDownloadExcel = function (data) {
            var mystyle = {
                headers: true,
                column: { style: { Font: { Bold: "1" } } }
            };
            var month = new Date().getMonth() + 1;
            var fileName = webtasks.currentBasket.name + '-' + new Date().getDate() + '/' + month + ' ' + new Date().getHours() + ':' + new Date().getMinutes() + ' ' + new Date().getFullYear().toString().substr(-2);
            var dataCopy = [];
            dataCopy = data;
            dataCopy = JSON.parse(JSON.stringify(dataCopy));
            alasql('SELECT * INTO XLSX("' + fileName + '",?) FROM ?', [mystyle, dataCopy]);
        };

        webtasks.getWebTaskCount = function () {
            Mask.show();
            WebTasksService.getWebTaskCount().then(function (response) {
                Mask.hide();
                response.forEach(function (basket) {
                    if (webtasks.hiddenBaskets.length > 0) {
                        if (webtasks.hiddenBaskets.includes(basket.id)) {
                            basket.showInTaskBar = false;
                            webtasks.innerTotalCount += basket.count;
                            webtasks.innerDueCount += basket.dueCount;
                        } else {
                            basket.showInTaskBar = true;
                        }

                    } else {
                        basket.showInTaskBar = true;
                    }
                })
                webtasks.taskBarBaskets.baskets = response;
                webtasks.taskBarBaskets.baskets.sort(function (oldBasket, newBasket) {
                    if (oldBasket.orderNo == null && newBasket.orderNo == null) {
                        return 0;
                    } else if (oldBasket.orderNo == null) {
                        return 1;
                    } else if (newBasket.orderNo == null) {
                        return -1;
                    } else {
                        return oldBasket.orderNo - newBasket.orderNo;
                    }
                });
                webtasks.applyColorCodeToBaskets();
                if ($state.params.id) {
                    webtasks.taskBarBaskets.baskets.forEach(function (basket) {
                        if (basket.id == $state.params.id) {
                            webtasks.clearPreviousTaskList(basket);
                        }
                    });
                }
            }, GeneralUtil.showMessageOnApiCallFailure).finally(Mask.hide);
        }

        webtasks.init = function () {
            webtasks.getLoggedInUser();
        }

        webtasks.clearPreviousTaskList = function (basketObj, fromLocationFilter) {
            webtasks.currentBasket = basketObj;
            $state.current.title = webtasks.currentBasket.name;
            $state.goURL('techo.dashboard.webtasks', { id: webtasks.currentBasket.id });
            webtasks.taskDetailList = [];
            webtasks.taskheaders = [];
            webtasks.pagingService.offSet = 0;
            webtasks.pagingService = PagingService.initialize();
            if (!webtasks.currentBasket.isLocationBasedFilterRequired)
                webtasks.locationId = null;
            if (fromLocationFilter)
                webtasks.isDataFilteredOnLocation = true;
            else
                webtasks.isDataFilteredOnLocation = false;
            webtasks.getWebTaskDetailByType();
            webtasks.innerTotalCount = 0;
            webtasks.innerDueCount = 0;
            WebTasksService.getWebTaskCount().then(function (response) {
                response.forEach(function (basket) {
                    if (webtasks.hiddenBaskets.length > 0) {
                        if (webtasks.hiddenBaskets.includes(basket.id)) {
                            basket.showInTaskBar = false;
                            webtasks.innerTotalCount += basket.count;
                            webtasks.innerDueCount += basket.dueCount;
                        } else {
                            basket.showInTaskBar = true;
                        }
                    } else {
                        basket.showInTaskBar = true;
                    }
                })
                webtasks.taskBarBaskets.baskets = response;
                webtasks.taskBarBaskets.baskets.sort(function (oldBasket, newBasket) {
                    if (oldBasket.orderNo == null && newBasket.orderNo == null) {
                        return 0;
                    } else if (oldBasket.orderNo == null) {
                        return 1;
                    } else if (newBasket.orderNo == null) {
                        return -1;
                    } else {
                        return oldBasket.orderNo - newBasket.orderNo;
                    }
                })
            });
        }

        webtasks.saveActions = function () {
            webtasks.selectedTaskActionForm.$setSubmitted();
            webtasks.otherDetails && webtasks.otherDetails.$setSubmitted();
            if (webtasks.selectedTaskActionForm.$valid
                && webtasks.otherDetails.$valid
            ) {
                webtasks.tasks = [];
                webtasks.tasks.push(webtasks.selectedTaskForAction);
                if (webtasks.otherTasksForUser != null && webtasks.otherTasksForUser.length > 0) {
                    webtasks.otherTasksForUser.forEach(function (parent) {
                        parent.webTasks.forEach(function (child) {
                            webtasks.tasks.push(child);
                        });
                    });
                }
                Mask.show();
                WebTasksService.saveActions(webtasks.tasks).then(function (saveResponse) {
                    Mask.hide();
                    $("#actionModal").modal('hide');
                    WebTasksService.getWebTaskCount().then(function (countResponse) {
                        webtasks.clearPreviousTaskList(webtasks.currentBasket);
                    })
                }, function (error) {
                    Mask.hide();
                    toaster.pop('error', 'Unable to save task actions');
                })
            }
        };

        webtasks.actionChange = function (taskDetail) {
            webtasks.selectedTaskForAction.isOtherDetails = taskDetail.action && taskDetail.action.isOtherDetailsRequired || false;
            if (taskDetail.action) {
                taskDetail.selectedAction = taskDetail.action.actionKey;
                taskDetail.isOtherDetailsRequired = taskDetail.action.isOtherDetailsRequired;
            } else {
                taskDetail.selectedAction = null;
                taskDetail.isOtherDetailsRequired = null;
            }
        };

        webtasks.changeBasketPreference = function (basket, status) {
            var userBasketPreferenceDto = {};
            userBasketPreferenceDto.userId = webtasks.loggedInUser.id;
            if (status === 'show') {
                var basketIndex = webtasks.hiddenBaskets.indexOf(basket.id);
                webtasks.hiddenBaskets.splice(basketIndex, 1);
            } else if (status === 'hide') {
                webtasks.hiddenBaskets.push(basket.id);
            }
            userBasketPreferenceDto.preference = JSON.stringify({
                taskbar: 'show',
                baskets: webtasks.hiddenBaskets
            });
            Mask.show();
            WebTasksService.saveBasketPreference(userBasketPreferenceDto).then(function (response) {
                Mask.hide();
                if (status === 'show') {
                    basket.showInTaskBar = true;
                    webtasks.innerTotalCount -= basket.count;
                    webtasks.innerDueCount -= basket.dueCount;
                } else {
                    basket.showInTaskBar = false;
                    webtasks.innerTotalCount += basket.count;
                    webtasks.innerDueCount += basket.dueCount;
                }
            }, function (error) {
                Mask.hide();
                toaster.pop('error', 'Unable to save basket preference');
            });
        };

        webtasks.navigateToUrl = function (url) {
            var temp = url;
            var parameters = JSON.parse(temp.substring(temp.indexOf('(') + 1, temp.length - 1));
            var state = temp.substring(0, temp.indexOf('(')) + '/' + angular.toJson(parameters);
            if ($sessionStorage.asldkfjlj) {
                $sessionStorage.asldkfjlj[state] = true;
            } else {
                $sessionStorage.asldkfjlj = {};
                $sessionStorage.asldkfjlj[state] = true;
            }
            let url2 = $state.href(temp.substring(0, temp.indexOf('(')), parameters, { inherit: false, absolute: false });
            sessionStorage.setItem('linkClick', 'true');
            window.open(url2, '_blank');
        };

        webtasks.openModal = function (task) {
            if (task.details.modal_name === 'cmtc_probable_confirmation_modal') {
                let modalInstance = $uibModal.open({
                    templateUrl: 'app/manage/webtasks/views/mo-verification-for-child-screening.modal.html',
                    controller: 'MoVerificationForChildScreening',
                    windowClass: 'cst-modal',
                    size: 'lg',
                    resolve: {
                        message: function () {
                            return "Please select the action to be performed on this child";
                        },
                        childId: function () {
                            return task.details.id;
                        },
                        childName: function () {
                            return task.details['Child Name'];
                        },
                        areaName: function () {
                            return task.details['Area Name'];
                        },
                        villageName: function () {
                            return task.details.Village;
                        },
                        fhwName: function () {
                            return task.details['FHW Name'];
                        },
                        fhwContactNumber: function () {
                            return task.details['FHW Contact Number'];
                        },
                        motherName: function () {
                            return task.details['Mother Name'];
                        },
                        motherContact: function () {
                            return task.details['Mother Contact No.']
                        }
                    }
                });
                modalInstance.result.then(function () {
                    webtasks.tasks = [];
                    task.selectedAction = 'VERIFIED';
                    webtasks.tasks.push(task)
                    Mask.show();
                    WebTasksService.saveActions(webtasks.tasks).then(function (saveResponse) {
                        Mask.hide();
                        WebTasksService.getWebTaskCount().then(function (countResponse) {
                            webtasks.clearPreviousTaskList(webtasks.currentBasket);
                        })
                    }, function (error) {
                        Mask.hide();
                        toaster.pop('error', 'Unable to save task actions');
                    })
                }, function () {

                });
            } else if (task.details.modal_name === 'maternal_death_verification_modal_mo') {
                let modalInstance = $uibModal.open({
                    templateUrl: 'app/manage/webtasks/views/mo-maternal-death-verification.modal.html',
                    controller: 'MaternalDeathVerificationModalMo',
                    windowClass: 'cst-modal',
                    size: 'lg',
                    resolve: {
                        message: function () {
                            return "Please Select Action";
                        },
                        task: function () {
                            return task
                        }
                    }
                });
                modalInstance.result.then(function (task) {
                    webtasks.tasks = [];
                    task.selectedAction = task.action.actionKey
                    webtasks.tasks.push(task)
                    Mask.show();
                    WebTasksService.saveActions(webtasks.tasks).then(function (saveResponse) {
                        Mask.hide();
                        WebTasksService.getWebTaskCount().then(function (countResponse) {
                            webtasks.clearPreviousTaskList(webtasks.currentBasket);
                        })
                    }, function (error) {
                        Mask.hide();
                        toaster.pop('error', 'Unable to save task actions');
                    })
                }, function () {

                });
            }
        }

        webtasks.cancelActions = function () {
            $("#actionModal").modal('hide');
            webtasks.selectedTaskActionForm.$setPristine();
        };

        webtasks.removeStyles = function () {
            setTimeout(function () {
                $('ul.dropdown-menu.more-infobox').attr('style', '');
            }, 0);
        };

        webtasks.toggleFilter = function () {
            if (angular.element('.filter-div').hasClass('active')) {
                angular.element('body').css("overflow", "auto");
            } else {
                angular.element('body').css("overflow", "hidden");
            }
            angular.element('.cst-backdrop').fadeToggle();
            angular.element('.filter-div').toggleClass('active');
        }

        webtasks.submit = function () {
            webtasks.toggleFilter();
        }

        webtasks.init();
    }
    angular.module('imtecho.controllers').controller('WebTasksController', WebTasksController);
})();
