(function (angular) {
    angular.module('imtecho.directives').directive('jqSortable', function ($timeout) {
        return {
            restrict: 'A',
            controllerAs: 'jqSortableCntrl',
            scope: {
                sortData: "=",
                methodCall: "=",
                options: "="
            },
            controller: function ($scope, $element, $attrs) {
                var defaults = {};
                var handleCount = 0;
                this.addItem = function (element) {
                    $scope.reload(defaults);
                };
                this.removeItem = function (element) {
                    $scope.reload(defaults);
                };
                this.addHandle = function () {
                    handleCount++;
                    defaults.handle = '.jq-sortable-handle';
                    $scope.reload(defaults);
                };
                this.removeHandle = function () {
                    if (handleCount > 0) {
                        handleCount--;
                    }
                    if (handleCount === 0) {
                        delete defaults.handle;
                    }
                    $scope.reload(defaults);
                };
            },
            link: function (scope, element, attrs) {
                var createTimeoutPromise;
                var sortable;
                var defaults = {
                    draggable: ".jq-sortable-item",
                    scroll: true,
                    animation: 150,
                    setData: function (dataTransfer, dragEl) {
                        dataTransfer.setData('Text', dragEl.textContent);
                    },
                    onSort: function (evt) {
                        scope.sortData.splice(evt.newIndex, 0,
                            scope.sortData.splice(evt.oldIndex, 1)[0]);
                        var o = evt.oldIndex;
                        var n = evt.newIndex;
                        var movedown = false;
                        if (o < n) {
                            movedown = true;
                        }
                        if (movedown) {
                            for (var i = (n); i > o - 1; i--) {
                                scope.sortData[i][attrs.index] = i;
                            }
                        } else {
                            scope.sortData[n][attrs.index] = n;
                            for (var counter = n + 1; counter < (o + 1); counter++) {
                                scope.sortData[counter][attrs.index] = scope.sortData[counter][attrs.index] + 1;
                            }
                        }
                        scope.$apply();
                        if (angular.isDefined(scope.methodCall)) {
                            scope.methodCall();
                            scope.$apply();
                        }
                    }
                };
                scope.reload = function (options) {
                    if (createTimeoutPromise) {
                        $timeout.cancel(createTimeoutPromise);
                    }
                    createTimeoutPromise = $timeout(function () {
                        if (sortable) {
                            sortable.destroy();
                        }
                        var defaultOptions = angular.merge({}, defaults, options);
                        sortable = Sortable.create(element[0], angular.merge(defaultOptions, (scope.options || {})));
                    }, 200);
                };
            }
        };
    });
    angular.module('imtecho.directives').directive('jqSortableItem', function () {
        return {
            require: '^jqSortable',
            controller: function () { },
            scope: {},
            link: function (scope, element, attrs, jqSortableCtrl) {
                element.addClass('jq-sortable-item');
                jqSortableCtrl.addItem(element);
                scope.$on('$destroy', function () {
                    jqSortableCtrl.removeItem(element);
                });
            }
        };
    });
    angular.module('imtecho.directives').directive('jqSortableHandle', function () {
        return {
            require: ['^jqSortable', '^jqSortableItem'],
            scope: {},
            link: function (scope, element, attrs, controllers) {
                var jqSortableCtrl = controllers[0];
                element.addClass('jq-sortable-handle');
                jqSortableCtrl.addHandle();
                scope.$on('$destroy', function () {
                    jqSortableCtrl.removeHandle();
                });
            }
        };
    });
})(window.angular);
