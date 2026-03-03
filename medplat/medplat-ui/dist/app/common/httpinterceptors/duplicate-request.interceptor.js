(function (angular) {
    angular.module('imtecho.interceptor').factory('DuplicateRequestInterceptor', function ($q) {
        var httpRequests = {};
        var DUPLICATED_REQUEST_STATUS_CODE = 499; // I just made it up - nothing special
        var EMPTY_BODY = '';
        var EMPTY_HEADERS = {};

        var logForUrl = function (url, config) {
            return config.url.indexOf(url) !== -1;
        };
        var buildRejectedRequestPromise = function (requestConfig) {
            var dfd = $q.defer();
            // build response for duplicated request
            var response = { data: EMPTY_BODY, headers: EMPTY_HEADERS, status: DUPLICATED_REQUEST_STATUS_CODE, config: requestConfig };
            // reject promise with response above
            console.debug('Such request is already in progress, rejecting this one', requestConfig);
            dfd.reject(response);
            return dfd.promise;
        };
        var generateKey = function (requestConfig) {
            var splited = requestConfig.url.split('.');
            var prefix = '';
            if (splited[splited.length - 1] === 'html') {
                prefix = 'html:';
            }
            return prefix + requestConfig.url + requestConfig.method + JSON.stringify(requestConfig.params) + JSON.stringify(requestConfig.data);
        };

        return {
            request: function (config) {
                if (logForUrl('/api/file/all', config)) {
                    console.log(generateKey(config));
                }
                if (generateKey(config).startsWith('html:')) {
                    return config;
                }
                // Contains the data about the request before it is sent.
                if (httpRequests[generateKey(config)]) {
                    return buildRejectedRequestPromise(config);
                } else {
                    httpRequests[generateKey(config)] = config;
                }
                return config;
            },
            response: function (response) {
                if (logForUrl('/api/file/all', response.config)) {
                    console.log(generateKey(response.config));
                }
                delete httpRequests[generateKey(response.config)];
                return response;
            },
            responseError: function (response) {
                if (logForUrl('/api/file/all', response.config)) {
                    console.log(generateKey(response.config));
                }
                var status = response.status ? response.status : undefined;
                if (!status) {
                    httpRequests = {};
                } else if (status !== DUPLICATED_REQUEST_STATUS_CODE) { // if request rejected by our duplication handler
                    delete httpRequests[generateKey(response.config)];
                }
                return $q.reject(response);
            }
        };
    });

    angular.module('imtecho.interceptor').config(function ($httpProvider) {
        $httpProvider.interceptors.push('DuplicateRequestInterceptor');
    });
})(window.angular);
