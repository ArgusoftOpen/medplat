(function () {
    angular.module('imtecho.directives').directive('languageSwitcher', function ($translate, $rootScope) {
        return {
            restrict: 'E',
            replace: true,
            template:
                '<div class="language-switcher dropdown" uib-dropdown>' +
                    '<a class="dropdown-toggle lang-toggle" uib-dropdown-toggle>' +
                        '<em class="fa fa-globe"></em> ' +
                        '<span class="lang-label">{{currentLang.label}}</span>' +
                    '</a>' +
                    '<ul class="dropdown-menu dropdown-menu-right" uib-dropdown-menu>' +
                        '<li ng-repeat="lang in languages" ng-class="{\'active\': lang.code === currentLang.code}">' +
                            '<a href="javascript:void(0)" ng-click="switchLanguage(lang.code)">' +
                                '{{lang.label}}' +
                            '</a>' +
                        '</li>' +
                    '</ul>' +
                '</div>',
            link: function (scope) {
                scope.languages = [
                    { code: 'en', label: 'English' },
                    { code: 'hi', label: 'हिंदी' },
                    { code: 'gu', label: 'ગુજરાતી' }
                ];

                function updateCurrentLang() {
                    var currentCode = $translate.use() || $translate.proposedLanguage() || 'en';
                    scope.currentLang = scope.languages.find(function (l) {
                        return l.code === currentCode;
                    }) || scope.languages[0];
                }

                scope.switchLanguage = function (langCode) {
                    $translate.use(langCode);
                    $rootScope.currentLanguage = langCode;
                    updateCurrentLang();
                };

                updateCurrentLang();

                $rootScope.$on('$translateChangeSuccess', function () {
                    updateCurrentLang();
                });
            }
        };
    });
})();
