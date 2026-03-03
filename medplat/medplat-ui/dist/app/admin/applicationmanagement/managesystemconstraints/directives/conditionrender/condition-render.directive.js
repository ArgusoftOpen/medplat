(() => {
    let conditionRenderDirective = () => {
        return {
            restrict: 'E',
            scope: {
                index: '=',
                option: '=',
                level: '=',
                options: '=',
                array: '=',
                fieldMasterDtos: '='
            },
            templateUrl: 'app/admin/applicationmanagement/managesystemconstraints/directives/conditionrender/condition-render.html',
            link: ($scope) => {
                const OPTION = {
                    type: "",
                    operator: "",
                    value1: "",
                    value2: "",
                    fieldName: "",
                    queryCode: ""
                }

                let _init = () => {
                    if ($scope.level) {
                        $scope.childLevel = (parseInt($scope.level) + 1).toString();
                    }
                }

                $scope.addCondition = (index) => {
                    $scope.options[index].conditions.options.push({ ...OPTION });
                }

                $scope.addGroup = (index, rule) => {
                    $scope.options[index].conditions.options.push({
                        conditions: {
                            rule,
                            options: [{ ...OPTION }]
                        }
                    });
                }

                $scope.invertGroup = (index) => {
                    $scope.options[index].conditions.rule = $scope.options[index].conditions.rule === 'AND' ? 'OR' : 'AND'
                }

                $scope.deleteGroup = (index) => {
                    $scope.options.splice(index, 1);
                }

                $scope.deleteCondition = (index) => {
                    $scope.options.splice(index, 1);
                }

                $scope.typeChanged = (index) => {
                    $scope.options[index] = {
                        ...$scope.options[index],
                        fieldName: null,
                        expression: null,
                        queryCode: null,
                        operator: null,
                        value1: null,
                        value2: null
                    }
                }

                $scope.operatorChanged = (index) => {
                    $scope.options[index].value2 = null;
                }

                _init();
            }
        }
    };
    angular.module('imtecho.directives').directive('conditionRenderDirective', conditionRenderDirective);
})();
