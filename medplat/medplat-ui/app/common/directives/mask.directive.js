/**
 *
 * Mask - Directive for displaying loading mask over any element
 *
 * Usage 1 : <div id="demo" mask></div>  For showing mask on element
 * else
 * Usage 2 : <div mask></div>  For showing mask on whole page i.e. body element
 * else
 * Usage 2 : <div isdefault="true" mask></div>  For default mask element
 * You have to use MaskProvider in app.js and set it's properties like template, templateUrl,
 * message, imagePath
 * properties can be set once at a time. You can combine only imagePath and message means you can set both and will get
 * output also for both
 * the service for masking has two functions :
 * show()-> this is used the show the masking in stack manner
 *        while using the directive as in Usage 1 show method must be called with "element id" as Mask.show('demo');
 *        while using the directive as in Usage 2 show method must be called as Mask.show();
 * hide()-> this is used to remove masking in stack manner
 *        while using the directive as in Usage 1 show method must be called with "element id" as Mask.hide('demo');
 *        while using the directive as in Usage 2 show method must be called as Mask.hide();
 * hide(null,true)--> used to explicitly hide all masks and clear the stack
 */
(function (angular) {
    angular.module('imtecho.directives').directive('mask', function (Mask) {
        function linkFn(scope, element, attrs) {
            scope.$on(
                "$destroy",
                function handleDestroyEvent() {
                    Mask.remove();
                }
            );

            scope.mask = Mask.register(attrs.id, attrs.isdefault);

            scope.$watch(function () {
                return scope.mask.mask;
            }, function (newVal) {
                if (newVal) {
                    doMask();
                } else {
                    $(element).unmaskSpinner();
                }
            }, true);

            function doMask() {
                if (!!scope.mask) {
                    if (scope.mask.template) {
                        $(element).maskSpinner("", scope.mask.delay, scope.mask.template, "", "");
                    } else if (scope.mask.templateUrl) {
                        $(element).maskSpinner("", scope.mask.delay, "", scope.mask.templateUrl, "");
                    } else if (scope.mask.message && scope.mask.imagePath) {
                        $(element).maskSpinner(scope.mask.message, scope.mask.delay, "", "", scope.mask.imagePath);
                    } else if (scope.mask.imagePath) {
                        $(element).maskSpinner("", scope.mask.delay, "", "", scope.mask.imagePath);
                    } else if (scope.mask.message) {
                        $(element).maskSpinner(scope.mask.message, scope.mask.delay, "", "", "");
                    } else {
                        $(element).maskSpinner("", scope.mask.delay, "", "", "");
                    }
                }
            }
        }
        return {
            restrict: 'A',
            link: linkFn
        };
    });

    angular.module('imtecho.directives').provider('Mask', function () {
        var maskProviderData = {};

        this.setTemplate = function (template) {
            maskProviderData.template = template;
        };

        this.setTemplateUrl = function (url) {
            maskProviderData.templateUrl = url;
        };

        this.setMessage = function (message) {
            maskProviderData.message = message;
        };

        this.setImagePath = function (imagePath) {
            maskProviderData.imagePath = imagePath;
        };

        this.$get = function () {
            var maskService = {};
            var maskList = {};
            var defaultMaskJson = {};
            maskService.register = function (elementId, isdefault) {
                var maskJson = angular.copy(maskProviderData);
                maskJson.delay = 0;
                maskJson.counter = 0;
                maskJson.show = function () {
                    maskJson.mask = true;
                    maskJson.counter++;
                };
                maskJson.hide = function (isExplicit) {
                    if (isExplicit) {
                        maskJson.counter = 0;
                        maskJson.mask = false;
                    } else if (!!maskJson.counter) {
                        maskJson.counter--;
                    }
                    if (!maskJson.counter) {
                        maskJson.mask = false;
                    }
                };
                if (isdefault) {
                    defaultMaskJson = maskJson;
                }
                else if (elementId) {
                    maskList[elementId] = maskJson;
                }
                else {
                    defaultMaskJson = maskJson;
                }
                return maskJson;
            };

            maskService.remove = function () {
                maskList = {};
            };

            maskService.show = function (elementId) {
                var thisMaskJson = {};
                if (elementId) {
                    thisMaskJson = maskList[elementId];
                }
                else {
                    thisMaskJson = defaultMaskJson;
                }
                if (angular.isDefined(thisMaskJson)) {
                    maskService.maskTemp = thisMaskJson.show();
                }
            };


            maskService.hide = function (elementId, isExplicit) {
                var thisMaskJson = {};
                if (elementId) {
                    thisMaskJson = maskList[elementId];
                }
                else {
                    thisMaskJson = defaultMaskJson;
                }
                if (angular.isDefined(thisMaskJson)) {
                    maskService.maskTemp = thisMaskJson.hide(isExplicit);
                }
            };

            return maskService;
        };
    });
})(window.angular);
