(function () {
    'use strict';

    /**
     * Language Management Service
     * Handles language preference, persistence, and switching
     */
    function LanguageService($translate, $rootScope, $window, TranslationCacheService, AuthenticateService) {
        const LANGUAGE_STORAGE_KEY = 'medplat_selected_language';
        const SUPPORTED_LANGUAGES = {
            'en': { code: 'en', name: 'English', flag: '🇬🇧' },
            'hi': { code: 'hi', name: 'हिंदी', flag: '🇮🇳' }
        };

        let currentLanguage = null;

        return {
            /**
             * Initialize language service
             * Sets language based on user preference or browser/system default
             */
            init: function () {
                // Try to get user's preferred language from profile
                const savedLanguage = $window.localStorage.getItem(LANGUAGE_STORAGE_KEY);
                
                let languageToUse = savedLanguage || this.getBrowserLanguage() || 'en';
                
                // Ensure language is supported
                if (!SUPPORTED_LANGUAGES[languageToUse]) {
                    languageToUse = 'en';
                }

                this.setLanguage(languageToUse);
            },

            /**
             * Get current language code
             * @returns {string} Current language code
             */
            getCurrentLanguage: function () {
                return currentLanguage || $translate.use() || 'en';
            },

            /**
             * Get browser's preferred language
             * @returns {string} Language code or null
             */
            getBrowserLanguage: function () {
                const browserLang = $window.navigator.language || $window.navigator.userLanguage;
                if (!browserLang) return null;
                
                // Extract language code (e.g., 'en' from 'en-US')
                const langCode = browserLang.split('-')[0].toLowerCase();
                return SUPPORTED_LANGUAGES[langCode] ? langCode : null;
            },

            /**
             * Set language and persist preference
             * @param {string} languageCode - Language code (e.g., 'en', 'hi')
             * @returns {Promise} Promise resolved when language is set
             */
            setLanguage: function (languageCode) {
                const self = this;
                
                // Validate language code
                if (!SUPPORTED_LANGUAGES[languageCode]) {
                    console.error('Unsupported language:', languageCode);
                    return Promise.reject('Unsupported language');
                }

                currentLanguage = languageCode;

                // Use $translate.use() to set the language
                return $translate.use(languageCode).then(function () {
                    // Save to local storage
                    try {
                        $window.localStorage.setItem(LANGUAGE_STORAGE_KEY, languageCode);
                    } catch (e) {
                        console.warn('Could not save language preference:', e);
                    }

                    // Broadcast language change event
                    $rootScope.$broadcast('languageChanged', {
                        language: languageCode,
                        name: SUPPORTED_LANGUAGES[languageCode].name
                    });

                    // Try to update user profile if authenticated
                    self.updateUserLanguagePreference(languageCode);

                    return languageCode;
                });
            },

            /**
             * Get list of all supported languages
             * @returns {array} Array of language objects with code, name, and flag
             */
            getSupportedLanguages: function () {
                return Object.values(SUPPORTED_LANGUAGES);
            },

            /**
             * Get language info by code
             * @param {string} languageCode - Language code
             * @returns {object} Language info object
             */
            getLanguageInfo: function (languageCode) {
                return SUPPORTED_LANGUAGES[languageCode] || null;
            },

            /**
             * Update user's language preference in the backend
             * @param {string} languageCode - Language code
             */
            updateUserLanguagePreference: function (languageCode) {
                try {
                    AuthenticateService.getLoggedInUser().then(function (user) {
                        if (user && user.data) {
                            // Map language codes to legacy codes if needed
                            const mappedCode = languageCode === 'en' ? 'EN' : languageCode.toUpperCase();
                            
                            // This would be called through your existing UserDAO
                            // For now, we'll just log it to localStorage as a preference
                            $window.localStorage.setItem('medplat_user_language_pref', mappedCode);
                        }
                    }).catch(function (err) {
                        // User not authenticated, preference stored locally only
                    });
                } catch (e) {
                    console.warn('Could not update user language preference:', e);
                }
            },

            /**
             * Check if language is RTL (Right-to-Left)
             * @param {string} languageCode - Language code
             * @returns {boolean} True if language is RTL
             */
            isRTL: function (languageCode) {
                const rtlLanguages = ['ar', 'he', 'ur'];
                return rtlLanguages.includes((languageCode || this.getCurrentLanguage()).toLowerCase());
            },

            /**
             * Set document direction based on language
             * @param {string} languageCode - Language code
             */
            setDirection: function (languageCode) {
                const direction = this.isRTL(languageCode) ? 'rtl' : 'ltr';
                document.documentElement.setAttribute('dir', direction);
                document.documentElement.setAttribute('lang', languageCode);
            }
        };
    }

    angular.module('imtecho.service').factory('LanguageService', ['$translate', '$rootScope', '$window', 'TranslationCacheService', 'AuthenticateService', LanguageService]);
})();
