(function () {
    var datepicker = function (APP_CONFIG, $templateCache, $timeout) {
        return {
            restrict: 'AE',
            scope: {
                ngDisabled: "=",
                ngModel: '=',
                minDate: '=',
                maxDate: '=',
                ngRequired: '=?',
                dateOptions: '=',
                onChange: '&',
                ngChange: '&',
                dateMonthFormat: '=',
                minDateMonth: '=',
                maxDateMonth: '='
            },
            replace: true,
            template: $templateCache.get('app/common/directives/datepicker/datepicker.html'),
            link: function (scope, element, attrs) {
                var watch = scope.$watch("ngModel", function (value) {
                    if (value) {
                        scope.ngModel = new Date(value);
                        watch();
                    }
                });
                var onChange = scope.onChange();
                scope.onChange = function () {
                    if (angular.isFunction(onChange)) {
                        onChange(scope.ngModel);
                    }
                    $timeout(function () {
                        scope.ngChange();
                    });
                };
                scope.dateFormat = APP_CONFIG.angularDateFormat;
                scope.popup = {
                    opened: false
                };
                scope.open = function () {
                    scope.popup.opened = true;
                };
                scope.close = function () {
                    scope.popup.opened = false;
                };
                //to close datepicker on tab press
                element.bind('keydown', function (event) {
                    scope.$apply(function () {
                        var tabKeyCode = 9;
                        if (event && event.keyCode && event.keyCode === tabKeyCode) {
                            scope.close();
                        }
                    });
                });
                scope.$watch('minDate', function (newVal) {
                    if (newVal) {
                        scope.datePickerOptions.minDate = scope.minDate;
                    }
                });
                scope.$watch('maxDate', function (newVal) {
                    if (newVal) {
                        scope.datePickerOptions.maxDate = scope.maxDate;
                    }
                });
                scope.$watch('dateMonthFormat', function (newVal) {
                    if (newVal) {
                        scope.dateFormat = APP_CONFIG.angularMonthFormat;
                    }
                });
                scope.$watch('minDateMonth', function (newVal) {
                    if (newVal) {
                        scope.datePickerOptions.minDate = newVal;
                    }
                });
                scope.$watch('maxDateMonth', function (newVal) {
                    if (newVal) {
                        scope.datePickerOptions.maxDate = newVal;
                    }
                });
                // Disable weekend selection
                scope.datePickerOptions = { minDate: scope.minDate, maxDate: scope.maxDate, startingDay: 1, showWeeks: false };
                if (attrs.dayToDisable) {
                    function disabled({ date, mode }) {
                        var days = attrs.dayToDisable.split(" ");
                        var condition = "mode==='day' && date.getDay()===Number(" + days[0] + ")";
                        for (var index = 1; index < days.length; index++) {
                            condition = condition.concat("|| date.getDay()===Number(" + days[index] + ")");
                        }
                        return eval(condition);
                    }
                    scope.datePickerOptions.dateDisabled = disabled;
                }
                angular.extend(scope.datePickerOptions, scope.dateOptions);
            }
        };
    };

    angular.module('imtecho.directives').directive('datepicker', datepicker);
    // Uncomment this to remove timezone from date whenever required. Also use this directive along with datepicker
    /* var dateToIso = function (APP_CONFIG) {
        var linkFunction = function (scope, element, attrs, ngModelCtrl) {
            ngModelCtrl.$parsers.push(function (datepickerValue) {
                return new Date(moment(datepickerValue).format("YYYY-MM-DD"));
            });
        };
        return {
            restrict: "A",
            require: "ngModel",
            link: linkFunction
        };
    };
    angular.module('agd.directives').directive('dateToIso', ['APP_CONFIG', dateToIso]); */
})();
