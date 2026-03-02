class ProtractorHelper {
    static getChromeDriverPath() {
        if (/^win/.test(process.platform)) {
            return './test/drivers/chromedriver.exe';
        }
        return './test/drivers/chromedriver';
    }

    static getSpecs() {
        return [
            // 'test/e2e/login/login.spec.js',
            'test/e2e/gvkVerification/highRisk/sam/sam.spec.js',
            'test/e2e/gvkVerification/highRisk/anemia/anemia.spec.js',
            'test/e2e/gvkVerification/highRisk/lbw/lbw.spec.js',
            'test/e2e/gvkVerification/highRisk/hbp/hbp.spec.js',
            'test/e2e/gvkVerification/immunization/immunization.spec.js',
            'test/e2e/gvkVerification/migration/member/out/member.spec.js',
            'test/e2e/gvkVerification/duplicateMember/duplicateMember.spec.js',
            'test/e2e/gvkVerification/migration/family/out/family.spec.js',
            'test/e2e/gvkVerification/beneficiaryService/beneficiaryService.spec.js',
            'test/e2e/gvkVerification/migration/member/in/member.spec.js',
        ];
    }

    static getSuites() {
        return {
            login: [
                'test/e2e/login/login.spec.js'
            ],
            highRisk: [
                'test/e2e/gvkVerification/highRisk/sam/sam.spec.js',
                'test/e2e/gvkVerification/highRisk/anemia/anemia.spec.js',
                'test/e2e/gvkVerification/highRisk/lbw/lbw.spec.js',
                'test/e2e/gvkVerification/highRisk/hbp/hbp.spec.js',
            ],
            immunization: [
                'test/e2e/gvkVerification/immunization/immunization.spec.js',
            ],
            migration: [
                'test/e2e/gvkVerification/memberMigrationOut/memberMigrationOut.spec.js'
            ],
            duplicate: [
                'test/e2e/gvkVerification/duplicateMember/duplicateMember.spec.js',
            ],
            beneficiaryService: [
                'test/e2e/gvkVerification/beneficiaryService/beneficiaryService.spec.js'
            ]
        };
    }

    static getChromeOptions(defaultDirectory, chromeArgs= ["--headless"] ) {
        const chromeOptions = {
            'browserName': 'chrome',
            'chromeOptions': {
                'excludeSwitches': ['enable-automation'],
                'useAutomationExtension': false,
                'prefs': {
                    'credentials_enable_service': false,
                    'profile': {
                        'password_manager_enabled': false
                    },
                    'download': {
                        'prompt_for_download': false,
                        'directory_upgrade': true,
                        'default_directory': defaultDirectory
                    }
                }
            }
        };
        if (chromeArgs) {
            chromeOptions.chromeOptions.args = chromeArgs;
        }
        return chromeOptions;
    }

    static getDateString() {
        const cd = new Date();
        return `${cd.getFullYear()}-${(cd.getMonth() + 1)}-${cd.getDate()} ${cd.getHours()}.${cd.getMinutes()}.${cd.getSeconds()}`;
    }

    static addCustomLocators(protractorInstance) {
        protractorInstance.By.addLocator('dataTest',
            function (dataTestValues, optionalParentElement) {
                /* This function will be serialized as a string and will execute in the
                 browser. The first argument is the value of the data-test attribute.
                 The second argument is the parent element, if any. */

                let selectorString = '';

                if (Array.isArray(dataTestValues)) {

                    dataTestValues.forEach(dataTestValue => {
                        selectorString += `[data-test="${dataTestValue}"] `;
                    });

                    selectorString = selectorString.trim();
                } else {
                    selectorString = `[data-test="${dataTestValues}"]`;
                }

                const using = optionalParentElement || document;
                return using.querySelectorAll(selectorString);
            });

        protractorInstance.By.addLocator('dataTestContainingText',
            function (dataTestValue, selectorText, exactMatch, optionalParentElement) {
                const using = optionalParentElement || document;
                const elements = using.querySelectorAll(`[data-test="${dataTestValue}"]`);

                return Array.prototype.filter.call(elements, function (elem) {
                    return exactMatch
                        ? elem.textContent === selectorText
                        : elem.textContent.includes(selectorText);
                });
            });
    }
}

module.exports = ProtractorHelper;
