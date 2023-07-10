(function () {
    function FhsDashboardController($rootScope, $state, $timeout, FhsDashboardService, DataPersistService, AuthenticateService, StateConstant, Mask, QueryDAO, LocationService, GeneralUtil) {
        var ctrl = this;
        ctrl.appName = GeneralUtil.getAppName();
        var locationId;
        var locationName;
        ctrl.isStarPerformerCollapsed = false;

        var init = function () {
            ctrl.fhsDetails = [];
            ctrl.familiesAndMemberDetails = [];
            ctrl.bar_chart_label_index_map = {};
            ctrl.locIds = [];
            ctrl.familiesAndMembers = [];
            ctrl.center = [22.9515504, 71.86528599999997];
            ctrl.markers = [];
            ctrl.dataloaded = false;
            ctrl.maploader = false;
            ctrl.starperformers = [];
            ctrl.performerloaded = false;
            ctrl.flagForNullValue = true;
            ctrl.isFamiliesAndMembers = false;
            ctrl.villageType = StateConstant.villageType;
            ctrl.urbanArea = StateConstant.urbanAreaType;
            ctrl.starPerformersLoaded = false;
            ctrl.village = null;
            ctrl.lastUpdateDate = null;
            ctrl.fetchUptoLevel = 4;
            ctrl.isDistrict = true;
            AuthenticateService.getLoggedInUser().then(function (user) {
                FhsDashboardService.getLastUpdateTime().then(function (res) {
                    ctrl.lastUpdateDate = new Date(+res.data);
                })
                ctrl.userDetail = user.data;
                locationId = ctrl.userDetail.minLocationId;
                locationName = ctrl.userDetail.minLocationName;
                LocationService.retrieveById(locationId).then(function (res) {
                    if ($rootScope.previousState.name == "techo.dashboard.fhsreport") {
                        if (!DataPersistService.getData()) {
                            $state.reload();
                        }
                        $timeout(function () {
                            angular.element(document.querySelector('#card1')).click();
                            var persistedData = DataPersistService.getData();
                            if (persistedData) {
                                ctrl.familiesAndMembers = persistedData.locations;
                                ctrl.familiesAndMemberDetails = persistedData.details;
                                ctrl.items = persistedData.excelData;
                            }

                            for (let i = 0; i < ctrl.familiesAndMemberDetails.length; i++) {
                                if (!ctrl.familiesAndMemberDetails[i].totalMember || ctrl.familiesAndMemberDetails[i].totalMember == null) {
                                    ctrl.familiesAndMemberDetails[i].totalMember = 0;
                                }

                                var key_barChart = ctrl.familiesAndMemberDetails[i].value;
                                var value_barChart = ctrl.familiesAndMemberDetails[i].id;
                                ctrl.bar_chart_label_index_map[key_barChart] = value_barChart;
                            }

                            ctrl.setBargraph(ctrl.familiesAndMemberDetails);
                            angular.element(document.querySelector('#showDetails')).click();
                        });
                    } else {
                        $timeout(function () {
                            angular.element(document.querySelector('#card1')).click();
                            ctrl.getFamiliesAndMembers(locationId, locationName, res.type);
                        });
                    }
                })
            });

            ctrl.showbar = true;
            ctrl.series = ['Imported From eMamta', 'Verified', 'Archived', 'To be Processed', 'New Families Added'];
            ctrl.chartOptions = {
                legend: {
                    display: true
                },
                scales: {
                    yAxes: [{
                        id: 'y-axis-1', type: 'linear', position: 'left',
                        ticks: {
                            min: 0,
                            beginAtZero: true,
                            callback: function (value, index, values) {
                                if (Math.floor(value) === value) {
                                    return value;
                                }
                            }
                        }
                    }]
                },
                plugins: {
                    labels: {
                        render: function (args) {
                            return '';
                        }
                    }
                }
            };
            ctrl.colors = [
                {
                    backgroundColor: '#243A50' // bar chart color for importedFromEmamta -rgba(247,70,74,1)
                },
                {
                    backgroundColor: '#30BCED'// bar chart color for Verified - #46BFBD
                },
                {
                    backgroundColor: '#FC5130' // bar chart color for Archived - #DCDCDC
                },
                {
                    backgroundColor: '#F2AF29' // bar chart color for unverified - #868e96
                },
                {
                    backgroundColor: '#90BE6D' // bar chart color for New Families Added - #2db54f(Green)
                }
            ];
        };

        var filterSureveyData = function (originalSurveyData) {
            if (!!originalSurveyData) {
                var filteredData = _.reject(originalSurveyData, function (surveyDetail) {
                    var total = surveyDetail.verifiedFHS + surveyDetail.unverifiedFHS + surveyDetail.archivedFHS + surveyDetail.emriVerifiedOK + surveyDetail.emriVerifiedEdited;
                    if (total === 0) {
                        var listWithSameName = _.where(originalSurveyData, { value: surveyDetail.value });
                        if (!!listWithSameName && listWithSameName.length > 1) {
                            return true;
                        }
                        return false;
                    } else {
                        return false;
                    }
                });
                return filteredData;
            } else {
                return [];
            }
        };

        ctrl.onClickBarChartFunction = function (activePoints, evt) {
            if (ctrl.familiesAndMembers.length == 4) {
                return;
            }
            if (activePoints.length > 0) {
                ctrl.showBarChartSpinnerFlag = true;
                var barChartLabel = activePoints[0]['_model'].label;
                var location_id_bar_chart = ctrl.bar_chart_label_index_map[barChartLabel];
                var familiesName = {
                    "id": location_id_bar_chart,
                    "name": barChartLabel
                };
                if (!angular.equals(ctrl.familiesAndMembers[ctrl.familiesAndMembers.length - 1], familiesName)) {
                    var type = "";
                    for (let i = 0; i < ctrl.familiesAndMemberDetails.length; i++) {
                        if (familiesName.id == ctrl.familiesAndMemberDetails[i].id) {
                            type = ctrl.familiesAndMemberDetails[i].locationType;
                        }
                        if (!ctrl.familiesAndMemberDetails[i].totalMember || ctrl.familiesAndMemberDetails[i].totalMember == null) {
                            ctrl.familiesAndMemberDetails[i].totalMember = 0;
                        }
                    }
                    if (type != ctrl.villageType && type != ctrl.urbanArea) {
                        Mask.show();
                        LocationService.retrieveById(location_id_bar_chart).then(function (res) {
                            familiesName.type = res.type;
                            ctrl.familiesAndMembers.push(familiesName);
                            ctrl.getFamiliesAndMembers(familiesName.id, familiesName.name, res.type);
                        }).finally(function () {
                            Mask.hide();
                        })
                    }
                }
            }
        };

        ctrl.getFamiliesAndMembers = function (idOfLocation, nameOfLocation, locationType) {
            if (!ctrl.checkIfFamiliesAndMembersExists(locationId)) {
                var familiesName = {
                    "id": idOfLocation,
                    "name": nameOfLocation,
                    "type": locationType
                };

                ctrl.familiesAndMembers.push(familiesName);
            }
            if (ctrl.familiesAndMembers.length > 1) {
                let i;
                for (i = 0; i < ctrl.familiesAndMembers.length; i++) {
                    if (ctrl.familiesAndMembers[i].id === locationId)
                        break;
                }
                if (i !== ctrl.familiesAndMembers.length - 1) {
                    ctrl.familiesAndMembers.splice(i + 1, ctrl.familiesAndMembers.length - (i + 1));
                }
            }

            ctrl.isFamiliesAndMembers = true;
            Mask.show();
            var queryDto = {
                code: "fhs_dashboard_locationwise_data",
                parameters: {
                    locationId: locationId
                }
            }
            var locationObj = _.find(ctrl.familiesAndMembers, function (obj) {
                return obj.id == locationId
            })
            if (locationObj.type == 'S') {
                ctrl.isDistrict = true
            } else {
                ctrl.isDistrict = false;
            }
            QueryDAO.execute(queryDto).then(function (res) {
                var originalSurveyData = res.result;
                var firstChilds = _.filter(originalSurveyData, function (data) {
                    return data.parent == locationId;
                })
                var secondChilds = _.filter(originalSurveyData, function (data) {
                    return data.parent != locationId;
                })
                var groupByData = _.groupBy(secondChilds, function (child) {
                    return child.parent;
                })
                var filteredData = filterSureveyData(firstChilds)
                angular.forEach(filteredData, function (parent) {
                    parent.childs = groupByData[parent.id];
                })

                for (let i = 0; i < filteredData.length; i++) {
                    var key_barChart = filteredData[i].value;
                    var value_barChart = filteredData[i].id;
                    ctrl.bar_chart_label_index_map[key_barChart] = value_barChart;
                }

                ctrl.familiesAndMemberDetails = filteredData;

                if (ctrl.familiesAndMemberDetails.length > 0) {
                    $timeout(function () {
                        $(".header-fixed").tableHeadFixer();
                    });
                }
                if (ctrl.showbar) {
                    ctrl.setBargraph(ctrl.familiesAndMemberDetails);
                }
                ctrl.dataLoaded = true;
                ctrl.isFamiliesAndMembers = false;
                ctrl.excelData = ctrl.familiesAndMemberDetails;
                ctrl.items = [];

                ctrl.personNameFlag = false;
                for (let i = 0; i < ctrl.excelData.length; i++) {
                    if (ctrl.excelData[i].personName) {
                        ctrl.personNameFlag = true;
                        break;
                    }
                }

                for (let i = 0; i < ctrl.excelData.length; i++) {
                    var memberDetails = ctrl.excelData[i];
                    var excelObj = {
                        "Location": memberDetails.value,
                        "Imported From eMamta": memberDetails.importedFromEmamta,
                        "Imported eMamta Member": memberDetails.importedFromEmamtaMember,
                        "To be processed": memberDetails.unverifiedFHS,
                        "In Reverification": memberDetails.inReverification,
                        "Verified": memberDetails.verifiedFHS,
                        "Archived": memberDetails.archivedFHS,
                        "New Families Added": memberDetails.newFamily,
                        [`Total Families in ${ctrl.appName}`]: memberDetails.newFamily + memberDetails.verifiedFHS,
                        [`Total Members in ${ctrl.appName}`]: memberDetails.totalMember,
                        "Total Members Under 1 Year": memberDetails.totalMemberUnder1Year,
                        "Total Members Under 5 Years": memberDetails.totalMemberUnder5Year,
                        "No. of Field Workers": memberDetails.worker
                    };
                    ctrl.items.push(excelObj);
                }
            }).finally(function () {
                Mask.hide();
            });
            ctrl.pdffilename = ctrl.familiesAndMembers[ctrl.familiesAndMembers.length - 1].name;
        };

        //bar graph
        ctrl.setBargraph = function (data) {
            var new_data = _.filter(data, function (location) {
                if (location.id != null) {
                    return location;
                }
            });
            if (new_data.length > 0) {
                ctrl.emptyDataMassageFlag = false;
                ctrl.labels = [];
                ctrl.barImported = [];
                ctrl.barVerified = [];
                ctrl.barUnverified = [];
                ctrl.barArchieved = [];
                ctrl.barNewFamiliesAdded = [];
                for (let i = 0; i < new_data.length; i++) {
                    ctrl.barImported.push(new_data[i].importedFromEmamta);
                    ctrl.barVerified.push(new_data[i].verifiedFHS);
                    ctrl.barUnverified.push(new_data[i].unverifiedFHS);
                    ctrl.barArchieved.push(new_data[i].archivedFHS);
                    ctrl.barNewFamiliesAdded.push(new_data[i].newFamily);
                    ctrl.labels.push(new_data[i].value);
                }
                ctrl.bars = [ctrl.barImported, ctrl.barVerified, ctrl.barArchieved, ctrl.barUnverified, ctrl.barNewFamiliesAdded];
            } else {
                ctrl.emptyDataMassageFlag = true;
                ctrl.emptyDataMassage = "No records found...";
            }
        };

        ctrl.showBarGraph = function () {
            ctrl.showDetailViewFlag = false;
            ctrl.showbar = true;
            if (ctrl.showbar) {
                ctrl.setBargraph(ctrl.familiesAndMemberDetails);
            }
        };

        //bar graph ends
        ctrl.checkIfFamiliesAndMembersExists = function (idOfLocation) {
            for (let i = 0; i < ctrl.familiesAndMembers.length; i++) {
                if (idOfLocation === ctrl.familiesAndMembers[i].id)
                    return true;
            }
            return false;
        };

        ctrl.navigateToFamilyReport = function (idOfLocation) {
            if (!!idOfLocation) {
                var data = {
                    locations: ctrl.familiesAndMembers,
                    details: ctrl.familiesAndMemberDetails,
                    excelData: ctrl.items
                };
                DataPersistService.setData(data);
                $state.go("techo.dashboard.fhsreport", { locationId: idOfLocation, userId: ctrl.userDetail.id });
            }
        };

        ctrl.checkIflocationexists = function (idOfLocation) {
            for (let i = 0; i < ctrl.locIds.length; i++) {
                if (idOfLocation === ctrl.locIds[i].id)
                    return true;
            }
            return false;
        };

        ctrl.mapDetails = function (flag) {
            if (flag) {
                ctrl.maploader = true;
            }
            if (ctrl.dataloaded) {
                return;
            }
            ctrl.markers = [];
            FhsDashboardService.getVillagesName().then(function (res) {
                ctrl.markers = res;
            });
        };

        ctrl.getstarperformers = function () {
            if (!ctrl.starPerformersLoaded) {
                Mask.show();
                var queryDto = {
                    code: "fhs_dashboard_star_performer",
                    parameters: {
                        userId: ctrl.userDetail.id
                    }
                };
                QueryDAO.execute(queryDto).then(function (res) {
                    ctrl.starperformers = res.result;
                    console.log("ctrl.starperformers : ", ctrl.starperformers);
                    ctrl.performerloaded = true;
                    ctrl.starPerformersLoaded = true;

                }).finally(function () {
                    Mask.hide();
                });
            }
        };

        ctrl.saveExcel = function () {
            var mystyle = {
                headers: true,
                column: { style: { Font: { Bold: "1" } } }
            };
            alasql('SELECT * INTO XLSX("' + ctrl.pdffilename + '",?) FROM ?', [mystyle, ctrl.items]);
        };

        ctrl.showDetailView = function () {
            if (ctrl.familiesAndMembers[ctrl.familiesAndMembers.length - 1].type != 'S') {
                ctrl.isDistrict = false;
            } else {
                ctrl.isDistrict = true;
            }
            $timeout(function () {
                $(".header-fixed").tableHeadFixer();
            });
            ctrl.showbar = false;
            ctrl.showDetailViewFlag = true;
        };

        //print option for fhs dashboard
        ctrl.printPdf = function () {
            var header = "<h2>" + "FHS families detail report for " + ctrl.pdffilename + " location" + "</h2>";
            ctrl.footer = "Generated by " + ctrl.userDetail.name + " at " + new Date().toLocaleString();
            $('#fhsdetails-table').printThis({
                importCSS: false,
                loadCSS: 'styles/css/printable.css',
                header: header,
                base: "./",
                pageTitle: ctrl.appName
            });
        };

        ctrl.toggleFilter = function () {
            if (angular.element('.filter-div').hasClass('active')) {
                angular.element('body').css("overflow", "auto");
            } else {
                angular.element('body').css("overflow", "hidden");
            }
            angular.element('.cst-backdrop').fadeToggle();
            angular.element('.filter-div').toggleClass('active');
        };

        ctrl.submit = function () {
            ctrl.familiesAndMembers = [];
            var level = 1;
            while (level < ctrl.selectedLocation.finalSelected.level) {
                ctrl.familiesAndMembers.push({
                    id: ctrl.selectedLocation['level' + level].id,
                    name: ctrl.selectedLocation['level' + level].name
                })
                level++;
            }
            ctrl.getFamiliesAndMembers(ctrl.selectedLocation.finalSelected.optionSelected.id, ctrl.selectedLocation.finalSelected.optionSelected.name, ctrl.selectedLocation.finalSelected.optionSelected.type);
            ctrl.toggleFilter();
        };

        init();

    }
    angular.module('imtecho.controllers').controller('FhsDashboardController', FhsDashboardController);
})();
