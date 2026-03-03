(function () {
    'use strict';

    /**
     * Language Switcher Controller
     * Provides UI for switching between available languages
     */
    function LanguageSwitcherController(LanguageService, $scope) {
        const vm = this;

        /**
         * Initialize controller
         */
        function init() {
            vm.currentLanguage = LanguageService.getCurrentLanguage();
            vm.supportedLanguages = LanguageService.getSupportedLanguages();
            vm.languageInfo = LanguageService.getLanguageInfo(vm.currentLanguage);
        }

        /**
         * Change language
         * @param {string} languageCode - Language code to switch to
         */
        vm.switchLanguage = function (languageCode) {
            LanguageService.setLanguage(languageCode).then(function (lang) {
                vm.currentLanguage = lang;
                vm.languageInfo = LanguageService.getLanguageInfo(lang);
                
                // Trigger document direction update for RTL languages
                LanguageService.setDirection(lang);
                
                // Broadcast event for any custom listeners
                $scope.$broadcast('languageSwitched', { language: lang });
            }).catch(function (err) {
                console.error('Error switching language:', err);
            });
        };

        /**
         * Get language display name
         * @param {object} language - Language object
         * @returns {string} Display name
         */
        vm.getLanguageName = function (language) {
            return language.name;
        };

        /**
         * Get language flag
         * @param {object} language - Language object
         * @returns {string} Flag emoji or code
         */
        vm.getLanguageFlag = function (language) {
            return language.flag || language.code.toUpperCase();
        };

        /**
         * Check if language is currently selected
         * @param {string} languageCode - Language code
         * @returns {boolean} True if selected
         */
        vm.isSelected = function (languageCode) {
            return vm.currentLanguage === languageCode;
        };

        // Listen for language changes from other sources
        $scope.$on('languageChanged', function (event, data) {
            vm.currentLanguage = data.language;
            vm.languageInfo = LanguageService.getLanguageInfo(data.language);
        });

        init();
    }

    angular.module('imtecho.controllers')
        .controller('LanguageSwitcherController', ['LanguageService', '$scope', LanguageSwitcherController]);
})();
