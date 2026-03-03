angular.module('imtecho.service')
    .factory('DataPersistService', [function () {
        var datapersistService = this;
        datapersistService.setData = function (data) {
            this.data = data;
        };
        datapersistService.getData = function () {
            return this.data;
        };
        return datapersistService;
    }]);
