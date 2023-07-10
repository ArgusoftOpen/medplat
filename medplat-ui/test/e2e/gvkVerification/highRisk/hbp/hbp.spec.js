const config = require('../../../config');
const loginPage = require('../../../login/login.po');
const browserFunctions = require('../../../common/browser_functions');
const {
    openSelectQueryTab,
    selectDeselectFeatureAccess,
} = require('../../../common/utils');

const {
    gvkFeatureConstant,
} = require('../../../common/gvk_constant')

const hbpFunction = require('./hbp.po');

describe('Highrisk HBP', () => {
    beforeAll(async () => {
        await loginPage.login(config.QA_SUPER_ADMIN_USER, config.QA_SUPER_ADMIN_PASSWORD);
        await openSelectQueryTab();
    });

    beforeEach(browserFunctions.beforeEach);

    afterEach(browserFunctions.afterEach);

    afterAll(browserFunctions.afterAll);

    it('GVK Verification -> High Risk HBP -> 1 -> High Risk Follow Up – Follow Up call for checking High BP medicine given 001', async function () {

        console.log('High Risk Follow Up – Follow Up call for checking High BP medicine given 001')
        await selectDeselectFeatureAccess({
            select: [gvkFeatureConstant.canHighriskFollowupVerification]
        });

        await hbpFunction.highBloodPressureFunction();

        await selectDeselectFeatureAccess({
            deSelect: [gvkFeatureConstant.canHighriskFollowupVerification]
        });

    });

});
