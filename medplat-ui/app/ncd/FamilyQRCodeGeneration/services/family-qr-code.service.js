(function () {
    function FamilyQRCodeDAO($resource, APP_CONFIG) {
        let api = $resource(APP_CONFIG.apiPath + '/familyqrcode/:action', {},
            {
                getFamilies: {
                    method: 'GET',
                    isArray: true,
                    params: {
                        action: 'family'
                    }
                },
                generateQRCode: {
                    method: 'GET',
                    responseType: 'blob',
                    transformResponse: function (res) {
                        return {
                            data: res
                        };
                    }
                },
                generatePdf: {
                    method: 'GET',
                    responseType: 'arraybuffer',
                    transformResponse: function (res) {
                        return {
                            data: res
                        };
                    }
                },
                getFamiliesSewa: {
                    method: 'GET',
                    isArray: true,
                    params: {
                        action: 'family'
                    }
                },
                generatePdfSewa: {
                    method: 'GET',
                    responseType: 'arraybuffer',
                    transformResponse: function (res) {
                        return {
                            data: res
                        };
                    }
                }
            });
        return {
            getFamilies: function (params) {
                return api.getFamilies({ action: 'family', locationId: params.location, fromDate: params.fromDate, toDate: params.toDate, limit: params.limit, offset: params.offset}, {}).$promise;
            },
            generateQrCode: function (familyId) {
                return api.generateQRCode({ action: 'generateqrcode', familyId }).$promise;
            },
            generatePdf: function (params) {
                return api.generatePdf({ action: 'generatepdf', familyId: params.familyId, locationId: params.location, fromDate: params.fromDate, toDate: params.toDate}, {}).$promise;
            },
            getFamiliesSewa: function (params) {
                return api.getFamiliesSewa({ action: 'familysewa', locationId: params.location, fromDate: params.fromDate, toDate: params.toDate, limit: params.limit, offset: params.offset}, {}).$promise;
            },
            generatePdfSewa: function (params) {
                return api.generatePdfSewa({ action: 'generatepdfsewa', familyId: params.familyId, locationId: params.location, fromDate: params.fromDate, toDate: params.toDate}, {}).$promise;
            }
        };
    }
    angular.module('imtecho.service').factory('FamilyQRCodeDAO', FamilyQRCodeDAO);
})();