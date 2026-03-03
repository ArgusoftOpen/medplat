(function () {
    var removespacesDirective = function () {
        return {
            restrict: 'A',
            link: function ($scope, $element) {
                $element.bind('input', function () {
                    $(this).val($(this).val().replace(/ /g, ''));
                });
            }
        };
    };
    angular.module('imtecho.directives').directive('removeSpaces', removespacesDirective);
})();
