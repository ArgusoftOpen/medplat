(function () {
    var chosenDirective = function ($timeout) {
        return {
            restrict: 'A',
            scope: { ngModel: "=", ngDisabled: "=", chosenDirective: "=", chosenOpen: "=", chosenSearch: "=", chosenOptions: '=' },
            link: function (scope, element, attrs) {
                var chosen = { disable_search_threshold: 0, disable_search: false };
                scope.$watch("chosenOptions", function () {
                    angular.extend(chosen, scope.chosenOptions);
                    element.chosen(chosen);
                });
                scope.$watch("ngModel", function (value) {
                    if (value) {
                        $timeout(function () {
                            element.trigger("chosen:updated");
                        });
                    } else {
                        element.trigger("chosen:updated");
                    }
                }, true);
                if (attrs.chosenOpen != null) {
                    scope.$watch("chosenOpen", function (value) {
                        if (value) {
                            element.trigger('chosen:open');
                        }
                    });
                }
                element.on('chosen:hiding_dropdown', function (evt, params) {
                    $(".open").find(".filter-dropdown").dropdown("toggle");
                });
                element.on('chosen:no_results', function (a, b, c) {
                    var noRes = element.next().find('.no-results');
                    var searchBox = element.next().find('.chosen-search input');
                    noRes.on('click', function () {
                        if (angular.isFunction(scope.chosenOptions.onNoResultClick)) {
                            var promise = scope.chosenOptions.onNoResultClick(searchBox.val());
                            if (promise && promise.then) {
                                promise.then(function () {
                                    element.trigger('chosen:close');
                                });
                            }
                        }
                    });
                });
                scope.$watch("ngDisabled", function (value) {
                    element.trigger("chosen:updated");
                });
                scope.$watch("chosenDirective", function (value) {
                    if (value) {
                        $timeout(function () {
                            element.trigger("chosen:updated");
                        });
                    }
                }, true);
                $timeout(function () {
                    element.trigger("chosen:updated");
                });
                scope.$on('chosen:update', function () {
                    $timeout(function () {
                        element.trigger("chosen:updated");
                    });
                });
            }
        };
    };
    angular.module('imtecho.directives').directive('chosenDirective', chosenDirective);
})();
