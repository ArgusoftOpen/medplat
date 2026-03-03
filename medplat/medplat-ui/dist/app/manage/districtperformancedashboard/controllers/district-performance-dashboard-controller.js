(function () {
    function DistrictPerformanceDashboardController(QueryDAO, Mask, GeneralUtil, $scope, DistrictPerformanceDashboardDao, DistrictPerformanceDashboardConstant) {
        var ctrl = this;
        ctrl.selectedLocation = null;
        ctrl.requiredLevel = 3;
        ctrl.financialYearRanges = [];
        ctrl.today = new Date();
        ctrl.isSubmitted = false;
        ctrl.locationList = [];
        ctrl.performanceData = [];
        ctrl.mockData = DistrictPerformanceDashboardConstant.districtPrformanceBasciData
        ctrl.dynamicLocationLabels = [];
        ctrl.selectedLocationDetails = {};
        ctrl.parentLocationDetails = {};
        ctrl.selectedLocationPerformanceData = {};

        ctrl.init = function () {
            QueryDAO.execute({
                code: 'get_last_n_financial_year_range',
                parameters: {
                    count: 7 //year range from 2013-2014 to 2020-2021
                }
            }).then(function (res) {
                ctrl.financialYearRanges = res.result;
                Mask.hide();
            }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                Mask.hide();
            });
        };

        // close or open right side location menu
        ctrl.toggleFilter = function () {
            if (angular.element('.filter-div').hasClass('active')) {
                angular.element('body').css("overflow", "auto");
            } else {
                angular.element('body').css("overflow", "hidden");
            }
            angular.element('.cst-backdrop').fadeToggle();
            angular.element('.filter-div').toggleClass('active');
        }

        ctrl.submit = function () {
            if (ctrl.locationForm.$invalid) {
                return;
            }
            ctrl.locationList = [];
            ctrl.performanceData = [];
            ctrl.dynamicLocationLabels = [];
            ctrl.mainHeaders = [];
            ctrl.headers = [];
            ctrl.count = 0;
            ctrl.headerCount = 0;
            ctrl.dynamiCount = 0;

            let keys = Object.keys(ctrl.selectedLocation).filter((location) => {
                if (ctrl.selectedLocation[location]) {
                    ctrl.selectedLocation[location].locType = ctrl.selectedLocation[location].locType === null ? 'State' : ctrl.selectedLocation[location].locType;
                    return ctrl.selectedLocation[location].id && ctrl.selectedLocation[location].locType !== 'Region';
                } else {
                    return null;
                }
            })
            // Define all eligible locations for fact sheet like state, district, block
            ctrl.locationList = keys.reduce((r, k) => ctrl.selectedLocation[k] && r.concat(ctrl.selectedLocation[k]), []).reverse();

            ctrl.locationList.forEach(location => {
                location.isChecked = false;
            })

            // Always selected location comes at position 0 and it's parent location comes at postion 1
            ctrl.locationList[0].isChecked = true;
            ctrl.locationList[1].isChecked = true;
            Mask.show();
            QueryDAO.execute({
                code: 'district_wise_rch_dashboard_analytics',
                parameters: {
                    location_id: ctrl.getLocationIds(),
                    financial_year: ctrl.financialYear
                }
            }).then(function (res) {
                ctrl.isSubmitted = true;
                ctrl.selectedLocationDetails = ctrl.locationList[0];
                ctrl.parentLocationDetails = ctrl.locationList[1];
                ctrl.response = res.result;

                // Get performance details for selected location to disply value for financial year, days, Pregnant Women etc. fields in UI.
                ctrl.selectedLocationPerformanceData = ctrl.response.find(data => data.location_id === ctrl.selectedLocationDetails.id);

                // Set common fields to performanceData array like name, tooltip fields.
                ctrl.mockData.forEach(data => {
                    let obj = {
                        "name": data.indicatorName,
                        "elaTooltip": data.locationElaTooltip,
                        "noTooltip": data.locationNoTooltip,
                        "percentageTooltip": data.locationPercentageTooltip
                    }
                    ctrl.performanceData.push(obj);
                })

                // Set main headers like Indicators, State, District etc.
                ctrl.mainHeaders = [
                    "Indicators",
                    ctrl.locationList[0].locType,
                    ctrl.locationList[1].locType
                ];

                // Initialize value for selected location column
                ctrl.initializeSheet(ctrl.locationList[0], false);

                // Initialize value for parent location column
                ctrl.initializeSheet(ctrl.locationList[1], false);

                ctrl.toggleFilter(); // close side bar when data retrieved successfully
                Mask.hide();
            }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                Mask.hide();
            });
        }

        ctrl.getLocationIds = function () {
            let parentLocationId, childLocationId;
            if (ctrl.locationList[0].locType === 'Block' || ctrl.locationList[0].locType === 'Zone') {
                parentLocationId = ctrl.locationList[1].id;
                childLocationId = ctrl.locationList[0].id;
            } else if (ctrl.locationList[0].locType === 'District' || ctrl.locationList[0].locType === 'Corporation') {
                parentLocationId = ctrl.locationList[0].id;
                childLocationId = null;
            }
            return [parentLocationId, childLocationId];
        }

        /**
         * Generate dynamic data based on selected location
         */
        ctrl.initializeSheet = function (location, addNewColumn) {
            if (location.isChecked) {
                // Get performance details from response by location id
                let locationData = ctrl.response.find(res => res.location_id === location.id) || {};

                // Set dynamic fields to performanceData array like ELA, No., Percentage e.g. DistrictElaValue, StateNoValue
                ctrl.mockData.forEach(data => {
                    let obj = ctrl.performanceData.find(x => x.name === data.indicatorName);
                    if (obj) {
                        obj[location.locType + "ElaValue"] = ctrl.getValue(locationData, data.locationElaFieldName);
                        obj[location.locType + "NoValue"] = ctrl.getValue(locationData, data.locationNoFieldName);
                        obj[location.locType + "PerValue"] = ctrl.getValue(locationData, data.locationPercentageFieldName);
                    }
                })

                // Set labels of dynamic fields
                ctrl.dynamiCount += 1;
                ctrl.dynamicLocationLabels = ctrl.dynamicLocationLabels.concat([
                    { index: ctrl.dynamiCount, value: location.locType + "ElaValue" },
                    { index: ctrl.dynamiCount, value: location.locType + "NoValue" },
                    { index: ctrl.dynamiCount, value: location.locType + "PerValue" }
                ]);

                // Set headers for dynamic fields
                ctrl.headerCount += 1;
                ctrl.headers = ctrl.headers.concat([
                    { index: ctrl.headerCount, value: "ELA/Denominator" },
                    { index: ctrl.headerCount, value: "No." },
                    { index: ctrl.headerCount, value: "%" }
                ]);
                !addNewColumn && ctrl.handleRankDetails(location);

                // Only if someone manually click on checkox
                if (addNewColumn) {
                    ctrl.count += 3; // Used for colspna
                    ctrl.mainHeaders.push(location.locType);
                }
            } else {
                ctrl.headers = ctrl.headers.filter(header => !(header.index === ctrl.dynamiCount));
                ctrl.dynamicLocationLabels = ctrl.dynamicLocationLabels.filter(dlabel => dlabel.index !== ctrl.headerCount);
                ctrl.mainHeaders.splice(-1, 1);
                // Remove property from object of performanceData array by location type
                ctrl.performanceData.forEach(data => {
                    delete data[location.locType + "ElaValue"];
                    delete data[location.locType + "NoValue"];
                    delete data[location.locType + "PerValue"];
                    delete data[location.locType + "RankValue"];
                })
                ctrl.count -= 3; // Remove colspan
                ctrl.dynamiCount -= 1; // Remove dynamic labels
                ctrl.headerCount -= 1; // Remove dynamic headers
            }
        }

        ctrl.getValue = function (location, label) {
            return label === "-" ? "-" : location[label];
        }

        /**
         * Handle rank details for selected location
         */
        ctrl.handleRankDetails = function (location) {
            // display rank in column of selected location means first column.
            if (ctrl.headers.length <= 3) {
                let rankLocationDetails = null;
                // Get child location index
                let childLocationIndex = ctrl.locationList.findIndex(x => x.id === location.id);
                if (childLocationIndex >= 0) {
                    // Get child location details
                    rankLocationDetails = ctrl.response.find(res => res.location_id === ctrl.locationList[childLocationIndex].id) || {};
                }
                ctrl.headers.push({ index: ctrl.headerCount, value: `Rank In ${this.parentLocationDetails.locType}` })
                ctrl.mockData.forEach(data => {
                    let obj = ctrl.performanceData.find(x => x.name === data.indicatorName);
                    if (obj) {
                        // Set rank details of child in parent location column
                        obj[location.locType + "RankValue"] = rankLocationDetails[data.locationRankField];
                    }
                })
                // Get current location details
                let currentLocationDetails = ctrl.response.find(res => res.location_id === location.id) || {};
                let totalNumberOfLocations = currentLocationDetails.total_number_of_locations;
                // Add dynamic column rank
                ctrl.dynamicLocationLabels.push(
                    { index: ctrl.dynamiCount, value: location.locType + "RankValue", totalNumberOfLocations }
                )
            }
        }

        /**
         * Manage tooltip for particular label
         */
        ctrl.checkForTooltip = function (sheet, label) {
            let value = "";
            if (label.includes("Per")) {
                value = sheet["percentageTooltip"];
            } else if (label.includes("No")) {
                value = sheet["noTooltip"];
            } else if (label.includes("Ela")) {
                value = sheet["elaTooltip"];
            }
            return value;
        }

        // Print district performance report
        ctrl.printPdf = function () {
            $("thead tr th").css("position", "inherit");
            Mask.show();
            $('#printableBlock').printThis({
                importCSS: false,
                loadCSS: 'styles/css/idsp-form-s-printable.css',
                header: '',
                footer: '',
                base: "./",
                printDelay: 333,
                pageTitle: '',
                afterPrint: function () {
                    $scope.$apply(function () {
                        Mask.hide();
                    })
                }
            })
        }

        ctrl.printExcel = function () {
            let locationIds = ctrl.getLocationIds().filter(x => x != null);
            Mask.show();
            DistrictPerformanceDashboardDao.downloadMedia(locationIds, ctrl.financialYear).then((res) => {
                if (res.data !== null && navigator.msSaveBlob) {
                    return navigator.msSaveBlob(new Blob([res.data], { type: '' }));
                }
                let a = $("<a style='display: none;'/>");
                let url = window.URL.createObjectURL(new Blob([res.data], { type: 'application/vnd.ms-excel' }));
                a.attr("href", url);
                a.attr("download", `DISTRICT FACTSHEET - ${ctrl.financialYear}.xlsx`)
                $("body").append(a);
                a[0].click();
                window.URL.revokeObjectURL(url);
                a.remove();
            }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                Mask.hide();
            })
        }

        ctrl.init();
    }
    angular.module('imtecho.controllers').controller('DistrictPerformanceDashboardController', DistrictPerformanceDashboardController);
})();
