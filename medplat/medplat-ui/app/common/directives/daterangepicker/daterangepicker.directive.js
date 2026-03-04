(function () {
    var daterangepicker = function ($templateCache) {
        return {
            restrict: 'AE',
            scope: {
                ngDisabled: "=",
                ngModel: '=',
                minDate: '=',
                maxDate: '=',
                ngRequired: '=?',
                dateRangeOptions: '='
            },
            replace: true,
            template: $templateCache.get('app/common/directives/daterangepicker/daterangepicker.html'),
            link: function (scope, element, attrs) {
                scope.isOpen = false;
                scope.ngModel = {
                    startDate: null,
                    endDate: null
                }
                scope.options = {
                    locale: {
                        format: "DD/MM/YYYY"
                    },
                    label: "Select Date Range",
                    opens: "left",
                    drops: "down",
                    alwaysShowCalendars: true,
                    separator: " / "
                }

                scope.$watch("ngModel", function (newVal) {
                    if (newVal) {
                        var from = moment(new Date(newVal.startDate)).format('MM/DD/YYYY');
                        var to = moment(new Date(newVal.endDate)).format('MM/DD/YYYY');

                        newVal['from_date_range'] = from;
                        newVal['to_date_range'] = to;
                    }
                });

                scope.$watch('minDate', function (newVal) {
                    if (newVal) {
                        scope.options.minDate = scope.minDate;
                    }
                });
                scope.$watch('maxDate', function (newVal) {
                    if (newVal) {
                        scope.options.maxDate = scope.maxDate;
                    }
                });

                scope.$watch('dateRangeOptions', function (newVal) {
                    if (newVal && Object.keys(newVal).length > 0) {
                        scope.options.ranges = {};
                        if (newVal.isLastWeek) {
                            scope.options.ranges['Last 7 days'] = [moment().subtract(6, 'days'), moment()]
                        }
                        if (newVal.isLastMonth) {
                            scope.options.ranges['Last Month'] = [moment().subtract(1, 'month'), moment()]
                        }
                        if (newVal.isLastYear) {
                            scope.options.ranges['Last 1 year'] = [moment().subtract(1, 'year'), moment()]
                        }
                        if (newVal.isFinancialYear) {
                            var currentDate = new Date();
                            var currentMonth = currentDate.getMonth();
                            var currentYear = currentDate.getFullYear();
                            if (currentMonth >= 1 && currentMonth <= 3) {
                                currentYear--;
                            }
                            var financialYear = new Date(currentYear, 3, 1);
                            scope.options.ranges['Current Financial Year'] = [financialYear, moment()];
                        }
                    }
                });

                scope.open = function () {
                    if (element[0].style.display == 'none' || !element[0].style.display) {
                        $(".datepicker").focus();
                    }
                    else {
                        $(".datepicker").blur();
                    }
                }
            }
        };
    }

    angular.module('imtecho.directives').directive('daterangepicker', daterangepicker);
})();
