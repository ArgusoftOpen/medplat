(function () {
    var sort = function () {
        return {
            scope: {
                fieldName: '=',
                orderColumn: '=',
                sortOrder: '='
            },
            replace: true,
            template: '<i class="glyphicon" ng-class="{\'fa fa-sort-asc size10\' : orderColumn === fieldName && sortOrder===\'asc\',  \'fa fa-sort-desc size10\' : orderColumn===fieldName && sortOrder===\'dsc\',\'fa fa-sort size10\':orderColumn!==fieldName}"></i>',
            link: function (scope, element, attrs) { }
        };
    };
    angular.module('imtecho.directives').directive('sort', ['APP_CONFIG', '$uibModal', sort]);
})();
