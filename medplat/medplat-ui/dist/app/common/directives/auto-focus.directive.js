(function () {
    'use strict';
    angular.module('imtecho.directives').directive('autoFocus', function ($timeout) {
        return {
            link: function (scope, element, attrs) {
                attrs.$observe("autoFocus", function (newValue) {
                    if (newValue === "true") {
                        $timeout(function () {
                            element[0].focus()
                        });
                    }
                });
            }
        };
    });
})();
