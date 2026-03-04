(function () {
    var alphabetsOnlyDir = function ($parse) {
        return {
            require: ['^form', 'ngModel'],
            link: function (scope, element, attrs) {
                scope.$watch(attrs.ngModel, function (newValue, oldValue) {
                    var model = $parse(attrs.ngModel);
                    if (newValue !== '') {
                        var res;
                        if (attrs.allowDashNDot) {
                            res = /^[a-zA-Z\.\-() ]+$/.test(newValue);
                        }
                        else if (attrs.noSpace) {
                            res = /^[a-zA-Z_]+$/.test(newValue);
                        }
                        else {
                            res = /^[a-zA-Z() ]+$/.test(newValue);
                        }
                        if (res) {
                            model.assign(scope, newValue);
                        }
                        else {
                            model.assign(scope, oldValue);
                        }
                    }
                });
            }
        };
    };
    angular.module('imtecho.directives').directive('alphabetsOnly', alphabetsOnlyDir);
})();
