(function (angular) {
    var MOSpecialistMembersList = function (QueryDAO, GeneralUtil, Mask, PagingForQueryBuilderService, AuthenticateService, $state, $window, $uibModal) {
        var ctrl = this;
        ctrl.isSearch = false;

        ctrl.init = function () {
            ctrl.pagingService = PagingForQueryBuilderService.initialize();
            AuthenticateService.getLoggedInUser().then(function (user) {
                Mask.show();
                ctrl.user = user
                ctrl.retrieveMemberList(true);
            });
        }

        ctrl.retrieveMemberList = function (reset) {
            var querydto = {}
            if (reset) {
                ctrl.membersSearchList = [];
                ctrl.pagingService.resetOffSetAndVariables();
            }
            Mask.show();
            if (ctrl.isSearch) {
                if (ctrl.searchForm.$valid) {
                    if (ctrl.search.searchBy === 'orgUnit') {
                        querydto = {
                            code: "fetch_mo_specialist_patients_list",
                            parameters: {
                                userId: ctrl.user.data.id,
                                locationId: ctrl.search.locationId,
                                uniqueHealthId: null,
                                limit: ctrl.pagingService.limit,
                                offset: ctrl.pagingService.offSet
                            }
                        }
                    }
                    if (ctrl.search.searchBy === 'uniqueHealthId') {
                        querydto = {
                            code: "fetch_mo_specialist_patients_list",
                            parameters: {
                                userId: ctrl.user.data.id,
                                locationId: null,
                                uniqueHealthId: ctrl.search.uniqueHealthId,
                                limit: ctrl.pagingService.limit,
                                offset: ctrl.pagingService.offSet
                            }
                        }
                    }
                }
            } else {
                querydto = {
                    code: "fetch_mo_specialist_patients_list",
                    parameters: {
                        userId: ctrl.user.data.id,
                        locationId: null,
                        uniqueHealthId: null,
                        limit: ctrl.pagingService.limit,
                        offset: ctrl.pagingService.offSet
                    }
                }
            }
            ctrl.pagingService.getNextPage(QueryDAO.execute, querydto, ctrl.membersSearchList, null).then((response) => {
                ctrl.membersSearchList = response;
                if(ctrl.isSearch && reset){
                    ctrl.toggleFilter()
                }
                Mask.hide();
            }).catch((error) => {
                GeneralUtil.showMessageOnApiCallFailure(error);
            }).finally(() => {
                Mask.hide();
            });
        }

        ctrl.fillExamineForm = function (id) {
            let url = $state.href('techo.ncd.mospecialistmember', { id: id });
            sessionStorage.setItem('linkClick', 'true')
            $window.open(url, '_blank');
        }

        ctrl.showInfoModel = function () {
            let modalInstance = $uibModal.open({
                template: `<div style="margin: 10px;padding: 10px;">
                <div>
                    <span class="dot" style="background-color: blue;"></span>
                    <span> - Blue records represents, the form is submitted with unsatisfactoy image response</span>
                </div>
                <div>
                    <span class="dot" style="background-color: #ff4d4d;"></span>
                    <span> - Pink records represents, new form is submitted by researcher role after your response</span>
                </div>
                <div>
                    <span class="dot" style="background-color: red;"></span>
                    <span> - Red records represents, Cardiologist have rejected the case</span>
                </div>
                <div>
                    <span class="dot" style="background-color: green;"></span>
                    <span> - Green records represents, the form is submitted by you</span>
                </div>
                <div>
                    <span class="dot" style="background-color: black;"></span>
                    <span> - Black records represents, not responded patients</span>
                </div>
                <br />
                <br />
                <button class="btn btn-primary pull-right" ng-click="ctrl.cancel()">
                    Close
                </button>
            </div>`,
                windowClass: 'cst-modal',
                backdrop: 'static',
                size: 'md',
                controllerAs: 'ctrl',
                resolve: {},
                controller: function ($uibModalInstance) {
                    this.cancel = function () {
                        $uibModalInstance.dismiss();
                    }
                }
            });

            modalInstance.result
                .then(function () { }, function () { })
        }

        ctrl.toggleFilter = function () {
            ctrl.searchForm.$setPristine();
            if (angular.element('.filter-div').hasClass('active')) {
                ctrl.modalClosed = true;
                angular.element('body').css("overflow", "auto");
            } else {
                ctrl.modalClosed = false;
                angular.element('body').css("overflow", "hidden");
            }
            angular.element('.cst-backdrop').fadeToggle();
            angular.element('.filter-div').toggleClass('active');
        }

        ctrl.init();
    };
    angular.module('imtecho.controllers').controller('MOSpecialistMembersList', MOSpecialistMembersList);
})(window.angular);