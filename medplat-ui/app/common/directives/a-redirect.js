(function () {
    var a = function ($sessionStorage, $timeout) {
        return {
            restrict: "E",
            link: function (scope, elem, attrs) {
                var target;
                if (attrs.stateName != '' && attrs.stateParams != '') {
                    if (!attrs.target && attrs.target == '') {
                        target = '_self';
                    } else {
                        target = '_blank';
                    }
                    if (attrs.stateName && attrs.stateParams) {
                        var state = attrs.stateName + '/' + attrs.stateParams;
                        if ($sessionStorage.asldkfjlj) {
                            $sessionStorage.asldkfjlj[state] = true;
                        } else {
                            $sessionStorage.asldkfjlj = {};
                            $sessionStorage.asldkfjlj[state] = true;
                        }
                    }

                    $(elem).mousedown(function (event) {

                        switch (event.which) {
                            case 1:
                            case 2:
                                $timeout(function () {
                                    $(this).attr('target', '_blank');
                                });
                                break;
                            case 3:
                            default:
                                $timeout(function () {
                                    $(this).attr('target', target);
                                });
                                break;
                        }
                    });
                }
            }
        };
    };
    angular.module('imtecho.directives').directive('a', a);
})();
