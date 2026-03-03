(function () {
    'use strict';

    /**
     * Translation Cache Service
     * Handles offline support by caching translation files locally
     */
    function TranslationCacheService($window, $q) {
        const CACHE_PREFIX = 'medplat_translation_cache_';
        const CACHE_VERSION_KEY = 'medplat_translation_version';
        const CURRENT_VERSION = '1.0.0';

        return {
            /**
             * Get cached translation for a specific language
             * @param {string} language - Language code (e.g., 'en', 'hi')
             * @returns {object} Cached translation or null
             */
            getFromCache: function (language) {
                try {
                    const cacheKey = CACHE_PREFIX + language;
                    const cached = $window.localStorage.getItem(cacheKey);
                    return cached ? JSON.parse(cached) : null;
                } catch (e) {
                    console.warn('Error reading translation cache:', e);
                    return null;
                }
            },

            /**
             * Save translation to local storage cache
             * @param {string} language - Language code
             * @param {object} translations - Translation data
             */
            saveToCache: function (language, translations) {
                try {
                    const cacheKey = CACHE_PREFIX + language;
                    $window.localStorage.setItem(cacheKey, JSON.stringify(translations));
                    // Store version info
                    $window.localStorage.setItem(CACHE_VERSION_KEY, CURRENT_VERSION);
                    return true;
                } catch (e) {
                    console.warn('Error saving translation cache:', e);
                    return false;
                }
            },

            /**
             * Check if cache is valid for a specific language
             * @param {string} language - Language code
             * @returns {boolean} Cache validity
             */
            isCacheValid: function (language) {
                return this.getFromCache(language) !== null;
            },

            /**
             * Clear cache for a specific language or all languages
             * @param {string} language - Language code (optional). If not provided, clears all
             */
            clearCache: function (language) {
                try {
                    if (language) {
                        const cacheKey = CACHE_PREFIX + language;
                        $window.localStorage.removeItem(cacheKey);
                    } else {
                        // Clear all translation caches
                        for (let i = 0; i < $window.localStorage.length; i++) {
                            const key = $window.localStorage.key(i);
                            if (key && key.startsWith(CACHE_PREFIX)) {
                                $window.localStorage.removeItem(key);
                            }
                        }
                    }
                    return true;
                } catch (e) {
                    console.warn('Error clearing translation cache:', e);
                    return false;
                }
            },

            /**
             * Get all cached languages
             * @returns {array} List of cached language codes
             */
            getCachedLanguages: function () {
                const languages = [];
                try {
                    for (let i = 0; i < $window.localStorage.length; i++) {
                        const key = $window.localStorage.key(i);
                        if (key && key.startsWith(CACHE_PREFIX)) {
                            const lang = key.replace(CACHE_PREFIX, '');
                            languages.push(lang);
                        }
                    }
                } catch (e) {
                    console.warn('Error reading cached languages:', e);
                }
                return languages;
            }
        };
    }

    angular.module('imtecho.service').factory('TranslationCacheService', ['$window', '$q', TranslationCacheService]);
})();
