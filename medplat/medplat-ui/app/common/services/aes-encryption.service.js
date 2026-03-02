(function (angular) {
    angular.module('imtecho.service').factory('AESEncryptionService', function ($rootScope) {
        let AESEncryptionService = {};


        AESEncryptionService.encrypt = function(data) {
            try {
                let encryptionKey = $rootScope.loginEncryptionKey;
                let initVector = $rootScope.loginInitVector;
                var cipher = CryptoJS.AES.encrypt(data, CryptoJS.enc.Utf8.parse(encryptionKey), {
                    iv: CryptoJS.enc.Utf8.parse(initVector),
                    mode: CryptoJS.mode.CBC,
                    padding: CryptoJS.pad.Pkcs7
                });
    
                return cipher.toString();
            } catch (ex) {
                return null;
            }
        }

        AESEncryptionService.decrypt = function (encryptedData) {
            try {
                let encryptionKey = $rootScope.loginEncryptionKey;
                let initVector = $rootScope.loginInitVector;

                var decrypted = CryptoJS.AES.decrypt(encryptedData, CryptoJS.enc.Utf8.parse(encryptionKey), {
                    iv: CryptoJS.enc.Utf8.parse(initVector),
                    mode: CryptoJS.mode.CBC,
                    padding: CryptoJS.pad.Pkcs7
                });
    
                return decrypted.toString(CryptoJS.enc.Utf8);
            } catch (ex) {
                return null;
            }
        };

        return AESEncryptionService;


    });
})(window.angular);