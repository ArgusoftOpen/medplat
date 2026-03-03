(function () {
    'use strict';

    /**
     * Translation Loader with Offline Support
     * Loads translations from local cache first, then from files/API
     */
    function TranslationLoaderFactory($q, $http, TranslationCacheService) {
        return function (options) {
            const deferred = $q.defer();
            const language = options.key;
            const prefix = options.prefix || 'app/locales/';
            const suffix = options.suffix || '.json';

            // Try to get from cache first
            const cached = TranslationCacheService.getFromCache(language);
            if (cached) {
                console.log('Loading translations from cache for language:', language);
                deferred.resolve(cached);
                return deferred.promise;
            }

            // If not in cache, load from file
            const url = prefix + language + suffix;
            
            $http.get(url).then(function (response) {
                    const data = response.data;
                    console.log('Loaded translations from file for language:', language);
                    TranslationCacheService.saveToCache(language, data);
                    deferred.resolve(data);
                }, function (err) {
                    console.error('Failed to load translations for language:', language, err);
                    const fallbackCache = TranslationCacheService.getFromCache(language);
                    if (fallbackCache) {
                        console.log('Using cached translations as fallback for:', language);
                        deferred.resolve(fallbackCache);
                    } else {
                        console.warn('No translations available for language:', language);
                        deferred.reject('Unable to load translations for ' + language);
                    }
                });

            return deferred.promise;
        };
    }

    // register on the service submodule so the factory is available
    // before the main module (`imtecho`) is defined in app.js
    angular.module('imtecho.service').factory('TranslationLoaderFactory', ['$q', '$http', 'TranslationCacheService', TranslationLoaderFactory]);
})();
