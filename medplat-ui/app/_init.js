(function () {

    angular.module('imtecho.controllers', []);
    angular.module('imtecho.components', []);
    angular.module('imtecho.directives', []);
    angular.module('imtecho.filters', []);
    angular.module('imtecho.constant', []);
    angular.module('imtecho.interceptor', []);

    // Added translate support here
    angular.module('imtecho.service', ['ngResource']);

    // Main module with translation support
    angular.module('imtecho', [
        'imtecho.controllers',
        'imtecho.components',
        'imtecho.directives',
        'imtecho.filters',
        'imtecho.constant',
        'imtecho.interceptor',
        'imtecho.service',
        'pascalprecht.translate'
    ])

    // Translation Configuration
    .config(function ($translateProvider) {

        $translateProvider.useStaticFilesLoader({
            prefix: 'app/translations/',
            suffix: '.json'
        });

        $translateProvider.preferredLanguage('en');

        $translateProvider.fallbackLanguage('en');

        $translateProvider.useLocalStorage();

    })

    // Language Switch Function
    .run(function ($rootScope, $translate) {

        $rootScope.changeLanguage = function (lang) {
            $translate.use(lang);
        };

    });

    angular.module('imtecho.directives').run(function ($templateCache, $http) {
    });

})();