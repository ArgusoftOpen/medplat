(function () {
    function MemberInfoController(Mask, GeneralUtil, $stateParams, $timeout, QueryDAO, toaster) {
        var ctrl = this;
        ctrl.immunisationGiven = [];
        ctrl.immunisationGivenList = [];
        ctrl.childServiceVisitDatesList = [];
        ctrl.ancVisitDatesList = [];
        ctrl.memberObj = {};
        ctrl.ancInfoObj = {};
        ctrl.paramDataFlag = false;
        ctrl.uniqueHealthId = null;
        ctrl.enterFlag = false;

        var init = function () {
            if ($stateParams.uniqueHealthId != null) {
                ctrl.uniqueHealthId = $stateParams.uniqueHealthId;
                ctrl.submit();
                ctrl.paramDataFlag = true;
            } else {
                ctrl.paramDataFlag = false;
            }
            $timeout(function () {
                ctrl.toggleFilter();
            }, 10)
        };

        ctrl.resetField = function () {
            ctrl.memberObj = {};
            ctrl.immunisationGivenList = [];
            ctrl.immunisationGiven = [];
            ctrl.childServiceVisitDatesList = [];
            ctrl.ancVisitDatesList = [];
            ctrl.ancInfoObj = {};
        }

        ctrl.toggleFilter = function () {
            if (angular.element('.filter-div').hasClass('active')) {
                angular.element('body').css("overflow", "auto");
            } else {
                if (angular.element('.filter-div').length > 0) {
                    angular.element('body').css("overflow", "hidden");
                }
            }
            angular.element('.cst-backdrop').fadeToggle();
            angular.element('.filter-div').toggleClass('active');
        };

        ctrl.submit = function () {
            if (!ctrl.uniqueHealthId) {
                toaster.pop("error", "Member Id can not be blank. Please enter valid memberId");
                ctrl.isDataSetFlag = false;
                ctrl.resetField();
                return;
            } else {
                ctrl.isDataSetFlag = true;
                ctrl.getMemberData();
            }
            $timeout(function () {
                ctrl.enterFlag = false;
            }, 500)
        };

        ctrl.enterSubmit = function () {
            ctrl.enterFlag = true;
            ctrl.submit();
        };

        ctrl.getMemberData = function () {
            Mask.show();
            ctrl.resetField();
            var dtoList = [];

            var queryDto1 = {
                code: 'retrieve_member_info_by_health_id',
                parameters: {
                    healthid: "'"+ctrl.uniqueHealthId+"'"
                },
                sequence: 1
            };
            dtoList.push(queryDto1);

            var queryDto2 = {
                code: 'retrieve_rch_child_service_master_info',
                parameters: {
                    healthid: "'"+ctrl.uniqueHealthId+"'"
                },
                sequence: 2
            };
            dtoList.push(queryDto2);

            var queryDto3 = {
                code: 'retrieve_rch_wpd_mother_master_info',
                parameters: {
                    healthid: "'"+ctrl.uniqueHealthId+"'"
                },
                sequence: 3
            };
            dtoList.push(queryDto3);

            var queryDto4 = {
                code: 'retrieve_rch_wpd_child_master_info',
                parameters: {
                    healthid: "'"+ctrl.uniqueHealthId+"'"
                },
                sequence: 4
            };
            dtoList.push(queryDto4);

            var queryDto5 = {
                code: 'retrieve_rch_pnc_mother_master_info',
                parameters: {
                    healthid: "'"+ctrl.uniqueHealthId+"'"
                },
                sequence: 5
            };
            dtoList.push(queryDto5);

            var queryDto6 = {
                code: 'retrieve_rch_pnc_child_master_info',
                parameters: {
                    healthid: "'"+ctrl.uniqueHealthId+"'"
                },
                sequence: 6
            };
            dtoList.push(queryDto6);

            var queryDto7 = {
                code: 'retrieve_rch_pregnancy_registration_det',
                parameters: {
                    healthid: "'"+ctrl.uniqueHealthId+"'"
                },
                sequence: 7
            };
            dtoList.push(queryDto7);

            var queryDto8 = {
                code: 'retrieve_anc_information',
                parameters: {
                    healthid: "'"+ctrl.uniqueHealthId+"'"
                },
                sequence: 8
            };
            dtoList.push(queryDto8);

            QueryDAO.executeAllQuery(dtoList).then(function (res) {

                if (!res[0].result[0]) {
                    toaster.pop("error", "This memberId is not valid. Please enter valid memberId");
                    ctrl.isDataSetFlag = false;
                    if (ctrl.enterFlag) {
                        $timeout(function () {
                            ctrl.toggleFilter();
                        }, 50)
                    }
                    return;
                }

                ctrl.memberObj = res[0].result[0];
                ctrl.memberObj.childServiceMasterDtos = res[1].result;
                ctrl.memberObj.wpdMotherDtos = res[2].result;
                ctrl.memberObj.wpdChildDtos = res[3].result;
                ctrl.memberObj.pncMotherDtos = res[4].result;
                ctrl.memberObj.pncChildDtos = res[5].result;
                ctrl.memberObj.pregnancyRegistrationDetailDtos = res[6].result;
                ctrl.memberObj.ancInfoObj = res[7].result;

                if (ctrl.memberObj.immunisationGiven != null) {
                    ctrl.immunisationGiven = ctrl.memberObj.immunisationGiven.split(",");
                    for (var i = 0; i < ctrl.immunisationGiven.length; i++) {
                        var immunisationobj = ctrl.immunisationGiven[i].split("#");
                        ctrl.immunisationGivenList.push({
                            key: immunisationobj[0],
                            value: immunisationobj[1]
                        })
                    }
                }
                if (ctrl.memberObj.childServiceVisitDatesList != null) {
                    ctrl.childServiceVisitDatesList = ctrl.memberObj.childServiceVisitDatesList.split(",");
                }
                if (ctrl.memberObj.ancVisitDatesList != null) {
                    ctrl.ancVisitDatesList = ctrl.memberObj.ancVisitDatesList.split(",");
                }
                if (!ctrl.enterFlag) {
                    $timeout(function () {
                        ctrl.toggleFilter();
                    }, 50)
                }
            }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                Mask.hide();
            });
        };

        init();
    }
    angular.module('imtecho.controllers').controller('MemberInfoController', MemberInfoController);
})();
