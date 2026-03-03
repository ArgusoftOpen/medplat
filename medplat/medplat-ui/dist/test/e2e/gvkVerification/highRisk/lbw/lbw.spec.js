const config = require('../../../config');
const loginPage = require('../../../login/login.po');
const browserFunctions = require('../../../common/browser_functions');
const {
    openSelectQueryTab,
    selectDeselectFeatureAccess,
} = require('../../../common/utils');
const lbwFunction = require('./lbw.po');

const {
    gvkFeatureConstant,
} = require('../../../common/gvk_constant')

describe('Highrisk Low Birth weight', () => {
    beforeAll(async () => {
        await loginPage.login(config.QA_SUPER_ADMIN_USER, config.QA_SUPER_ADMIN_PASSWORD);
        await openSelectQueryTab();
    });

    beforeEach(browserFunctions.beforeEach);

    afterEach(browserFunctions.afterEach);

    afterAll(browserFunctions.afterAll);

    it('GVK Verification -> High Risk Low Birth Weight -> 1 -> High Risk Follow Up – Follow Up call for checking LBW child hospitalized 001', async function () {
        console.log('\nHigh Risk Low Birth Weight -> 1');
        await selectDeselectFeatureAccess({
            select: [gvkFeatureConstant.canHighriskFollowupVerification]
        });
        await lbwFunction.verifyBeneficiaryTreatmentAdmittedInHospital();
    });

    it('GVK Verification -> High Risk Low Birth Weight -> 2 -> High Risk Follow Up – Follow Up call for checking LBW child not hospitalized but willing to hospitalized 002', async function () {
        console.log('\nHigh Risk Low Birth Weight -> 2');

        await lbwFunction.verifyBeneficiaryTreatmentNotAdmittedInHospitalPickedupSchedule();
    });

    it('GVK Verification -> High Risk Low Birth Weight -> 3 -> High Risk Follow Up – Follow Up call for checking LBW child not hospitalized and not willing to hospitalized 003', async function () {
        console.log('\nHigh Risk Low Birth Weight -> 3');

        await lbwFunction.verifyBeneficiaryTreatmentNotAdmittedInHospitalPickedupSchedulePending();
        await selectDeselectFeatureAccess({
            deSelect: [gvkFeatureConstant.canHighriskFollowupVerification]
        });
    });

});
