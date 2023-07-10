(function (angular, Math) {
    angular.module('imtecho.service').factory('GeneralUtil', function (toaster, ENV) {
        var GeneralUtil = {};
        /**
         * General error message will be shown based on responseError.data. If undefined errorMsg will be shown.
         *
         * @param {Object} responseError - Error response from $responce api
         * @param {String} errorMsg
         * @returns {undefined}
         */
        GeneralUtil.showMessageOnApiCallFailure = function (responseError, errorMsg) {
            if (responseError && responseError.status === 499) {
                toaster.warning('Multiple actions detected', 'You might have clicked same button twice.');
            } else {
                if (responseError && responseError.status === 401 && responseError.data.error === "invalid_token") {
                    toaster.pop({
                        type: 'error',
                        title: 'Your Session has Expired!'
                    });
                } else if (responseError && responseError.data && angular.isDefined(responseError.data.message) && responseError.data.message) {
                    toaster.pop({
                        type: 'error',
                        body: responseError.data.message
                    });
                } else if (errorMsg) {
                    toaster.pop({
                        type: 'error',
                        body: errorMsg
                    });
                } else {
                    toaster.pop({
                        type: 'error',
                        title: 'Operation unsuccessful',
                        body: 'Something went wrong.'
                    });
                }
            }
        };

        GeneralUtil.getPropByPath = (obj, path) => {
            if (!obj || !path) {
                return obj;
            }
            path = path.replace(/\[(\w+)\]/g, '.$1'); // convert indexes to properties
            path = path.replace(/^\./, '');           // strip a leading dot
            var a = path.split('.');
            for (var i = 0; i < a.length; ++i) {
                var k = a[i];
                if (k in obj) {
                    obj = obj[k];
                } else {
                    return;
                }
            }
            return obj;
        };

        GeneralUtil.getObjAndPropByPath = (obj, path) => {
            if (!obj || !path) {
                return obj;
            }
            path = path.replace(/\[(\w+)\]/g, '.$1'); // convert indexes to properties
            path = path.replace(/^\./, '');           // strip a leading dot
            var a = path.split('.');
            if (a.length > 1) {
                for (var i = 0; i < (a.length - 1); ++i) {
                    var k = a[i];
                    if (k in obj) {
                        obj = obj[k];
                    } else {
                        return;
                    }
                }
            }
            return [obj, a[a.length - 1]];
        }

        GeneralUtil.moveElementOfArrayByIndex = (array, index, delta) => {
            let newIndex = index + delta;
            if (array.length < 2) {
                toaster.pop({
                    type: 'error',
                    title: 'Insufficient elements present in array to move!'
                });
                return; // Insufficient elements present.
            }
            if (newIndex < 0 || newIndex == array.length) {
                toaster.pop({
                    type: 'error',
                    title: 'Element is at extreme end of array to move!'
                });
                return; // Already at the top or bottom.
            }
            let indexes = [index, newIndex].sort((a, b) => a - b); // Sort the indices
            array.splice(indexes[0], 2, array[indexes[1]], array[indexes[0]]); // Replace from lowest index, two elements, reverting the order
        };

        const STRENGTH = {
            0: { text: "Too Short", description: "Password must be of minimum 8 characters" },
            1: { text: "Weak", description: "Password should contain at least one lowercase letter, one uppercase letter, one number and one special character." },
            2: { text: "Fair", description: "Password should not contain three or more consecutive same characters" },
            3: { text: "Good", description: "Password should not contain user name or application name" },
            4: { text: "Strong", description: "Password is strong enough" }
        }

        GeneralUtil.checkPassword = (passwordField, passwordValue, userName) => {
            var password = document.getElementById(passwordField);
            var meter = document.getElementById('password-strength-meter');
            var text = document.getElementById('password-strength-text');
            meter.value = null;
            var val = password.value;

            let lowerPassword = passwordValue && passwordValue.toLowerCase();


            if (!passwordValue || passwordValue && passwordValue.length < 8) {
                meter.value = 0;
            } else if (!/^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[^a-zA-Z0-9])(?!.*\s).{8,}$/.test(passwordValue)) {
                meter.value = 1;
            } else if (!GeneralUtil.checkForConsecutive(passwordValue)) {
                meter.value = 2;
            } else if (lowerPassword && (lowerPassword.includes('techo') || lowerPassword.includes('medplat') ||
                lowerPassword.includes('ekavach') || lowerPassword.includes('impacthealth') || lowerPassword.includes(userName))) {
                meter.value = 3;
            } else {
                meter.value = 4;
            }

            if (val !== "") {
                text.innerHTML = "<strong>" + STRENGTH[meter.value].text + ":" + "</strong>" + "<span class='feedback'>" + STRENGTH[meter.value].description + "</span";
            }
            else {
                text.innerHTML = "";
            }

            return {
                value: meter.value,
                text: text.innerHTML
            }
        }

        GeneralUtil.checkForConsecutive = function (passwordValue) {
            // Check for sequential digits
            for (var i in passwordValue) {
                if (+passwordValue[+i + 1] == +passwordValue[i] + 1 &&
                    +passwordValue[+i + 2] == +passwordValue[i] + 2) {
                    return false;
                }
            }

            // Check for sequential alphabetical characters
            for (var i in passwordValue) {
                if (String.fromCharCode(passwordValue.charCodeAt(i) + 1) == passwordValue[+i + 1] &&
                    String.fromCharCode(passwordValue.charCodeAt(i) + 2) == passwordValue[+i + 2]) {
                    return false;
                }
            }

            return true;
        }

        GeneralUtil.getImagesPath = () => {
            switch (ENV.implementation) {
                case 'medplat':
                case 'sewa_rural':
                    return 'img/medplat/';
                case 'telangana':
                    return 'img/telangana/';
                case 'uttarakhand':
                    return 'img/uttarakhand/'
                default:
                    return 'img/argus/';
            }
        }

        GeneralUtil.getLogoImages = () => {
            let imagesPath = GeneralUtil.getImagesPath();
            let logoImages = [];
            switch (ENV.implementation) {
                case 'medplat':
                case 'sewa_rural':
                    logoImages = [
                    //     {
                    //     link: "https://gujhealth.gujarat.gov.in/",
                    //     source: `${imagesPath}govtofgujarat.png`
                    // }, {
                    //     link: "http://nhm.gov.in",
                    //     source: `img/nhm-logo.png`
                    // }, {
                    //     link: "http://www.sewarural.org/",
                    //     source: `${imagesPath}sewa-rural.png`
                    // }, {
                    //     link: "http://unicef.in",
                    //     source: `img/unicef.png`
                    // }, {
                    //     link: "http://emri.in",
                    //     source: `${imagesPath}gvk-emri.png`
                    // }
                ];
                    break;
                case 'telangana':
                    logoImages = [
                    //     {
                    //     link: "https://www.telangana.gov.in/",
                    //     source: `${imagesPath}govtoftelangana.png`
                    // }, {
                    //     link: "http://nhm.gov.in",
                    //     source: `img/nhm-logo.png`
                    // }, {
                    //     link: "https://www.armman.org/",
                    //     source: `${imagesPath}armman.png`
                    // }, {
                    //     link: "http://unicef.in",
                    //     source: `img/unicef.png`
                    // }
                ];
                    break;
                case 'uttarakhand':
                    logoImages = [
                    //     {
                    //     link: "https://uk.gov.in/",
                    //     source: `${imagesPath}govtofuttarakhand.png`
                    // }
                ];
                    break;
            }
            return [imagesPath, logoImages];
        }

        GeneralUtil.getLocalLanguage = () => {
            switch (ENV.implementation) {
                case 'medplat':
                case 'sewa_rural':
                    return 'Gujarati';
                case 'telangana':
                    return 'Telugu';
                case 'uttarakhand':
                    return 'Hindi';
                default:
                    return 'English';
            }
        }

        GeneralUtil.getAppName = () => {
            switch (ENV.implementation) {
                case 'uttarakhand':
                    return 'Chardham';
                case 'sewa_rural':
                    return 'Sewa Rural';
                case 'telangana':
                    return 'Amma-Kosam';
                default:
                    return 'MEDplat';
            }
        }

        GeneralUtil.getEnv = () => {
            return ENV.implementation
        }

        return GeneralUtil;
    });
})(window.angular, window.Math);
