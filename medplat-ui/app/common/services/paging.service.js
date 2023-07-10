(function () {
    var PagingService = function (APP_CONFIG, $q) {
        var ctrl = this;
        ctrl.busy = false;
        ctrl.offSet = 0;
        ctrl.isQueryService = false;
        ctrl.limit = APP_CONFIG.limit;
        this.initialize = function (isQueryService) {
            ctrl.isQueryService = isQueryService;
            ctrl.resetOffSetAndVariables();
            return ctrl;
        };
        this.getOffset = function (pageNumber) {
            if (pageNumber === null) {
                pageNumber = 1;
            }
            return (pageNumber - 1) * ctrl.limit;
        };
        this.resetOffSetAndVariables = function () {
            ctrl.busy = false;
            ctrl.offSet = 0;
            ctrl.allRetrieved = false;
            ctrl.responseObj = {};
        };
        this.getNextPage = function (nextPageFunctionWithPromise, paramsToPass, list, pathVariables) {
            if (list === null) {
                list = [];
            }
            var deferred = $q.defer();
            if (ctrl.busy || ctrl.allRetrieved) {
                deferred.resolve(list);
            } else {
                ctrl.busy = true;
                if (pathVariables) {
                    nextPageFunctionWithPromise(pathVariables, paramsToPass).then(function (res) {
                        if (!list || ctrl.offSet === 0) {
                            list = [];
                        }
                        if (!!ctrl.isQueryService) {
                            res = res.result;
                        }
                        list = list.concat(res);
                        if (list.length === 0 || list.length < ctrl.limit || list.length === ctrl.offSet) {
                            ctrl.allRetrieved = true;
                        }
                        ctrl.offSet = list.length;
                        ctrl.busy = false;
                        if (res != null) {
                            deferred.resolve(list);
                        } else {
                            deferred.resolve();
                        }
                    }).catch(function (e) {
                        //if server error then stop calling server resource
                        if (e && e.status === 500) {
                            ctrl.allRetrieved = true;
                        }
                        ctrl.busy = false;
                        deferred.reject(e);
                    });
                } else {
                    nextPageFunctionWithPromise(paramsToPass).then(function (res) {
                        if (!list || ctrl.offSet === 0) {
                            list = [];
                        }
                        if (!!ctrl.isQueryService) {
                            res = res.result;
                        }
                        list = list.concat(res);
                        if (list.length === 0 || list.length < ctrl.limit || list.length === ctrl.offSet) {
                            ctrl.allRetrieved = true;
                        }
                        ctrl.offSet = list.length;
                        ctrl.busy = false;
                        if (res != null) {
                            deferred.resolve(list);
                        } else {
                            deferred.resolve();
                        }
                    }).catch(function (e) {
                        //if server error then stop calling server resource
                        if (e && e.status === 500) {
                            ctrl.allRetrieved = true;
                        }
                        ctrl.busy = false;
                        deferred.reject(e);
                    });
                }
            }
            return deferred.promise;
        };
    };
    angular.module("imtecho.service").service('PagingService', PagingService);
})();
