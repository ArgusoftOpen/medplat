const config = require('../../../config');
const loginPage = require('../../../login/login.po');
const browserFunctions = require('../../../common/browser_functions');
const {
    openSelectQueryTab,
    selectDeselectFeatureAccess,
} = require('../../../common/utils');
const anemiaFunction = require('./anemia.po');

const {
    gvkFeatureConstant,
} = require('../../../common/gvk_constant');

describe('Highrisk Anemia', () => {
    beforeAll(async () => {
        await loginPage.login(config.QA_SUPER_ADMIN_USER, config.QA_SUPER_ADMIN_PASSWORD);
        await openSelectQueryTab();
    });

    beforeEach(browserFunctions.beforeEach);

    afterEach(browserFunctions.afterEach);

    afterAll(browserFunctions.afterAll);

    it('GVK Verification -> High Risk Anemia -> 1 -> High Risk Follow Up – Follow Up call for checking with FCM 001', async function () {
        console.log('\nHigh Risk Anemia -> 1');

        await selectDeselectFeatureAccess({
            select: [gvkFeatureConstant.canHighriskFollowupVerification]
        });

        await anemiaFunction.verifyBeneficiaryTreatmentGivenBloodGivenFCMNotGivenIronSucroseInjection();
    });


    it('GVK Verification -> High Risk Anemia -> 2 -> High Risk Follow Up – Follow Up call for checking with Iron 002', async function () {
        console.log('\nHigh Risk Anemia -> 2');

        await anemiaFunction.verifyBeneficiaryTreatmentGivenBloodNotGivenFCMGivenIronSucroseThreeInjection();
    });


    it('GVK Verification -> High Risk Anemia -> 3 -> High Risk Follow Up – Follow Up call for checking with no FCM/Iron blood 003', async function () {
        console.log('\nHigh Risk Anemia -> 3');

        await anemiaFunction.verifyBeneficiaryTreatmentGivenBloodNotGivenFCMNotGivenIronSucroseInjection();
    });


    it('GVK Verification -> High Risk Anemia -> 4 -> High Risk Follow Up – Follow Up call for checking for Anemia without schedule date 004', async function () {
        console.log('\nHigh Risk Anemia -> 4');

        await anemiaFunction.verifyBeneficiaryTreatmentNotGivenBloodNotGivenFCMNotGivenIronSucroseInjectionPickedUpScheduleForNextVisit();
    });

    it('GVK Verification -> High Risk Anemia -> 5,6 -> High Risk follow up - Follow up call for checking- schedule pending 005,006', async function () {
        console.log('\nHigh Risk Anemia -> 5,6');

        await anemiaFunction.verifyBeneficiaryTreatmentNotGivenBloodNotGivenFCMNotGivenIronSucroseInjectionPickedUpScheduleForNextVisitPending();
    });

    it('GVK Verification -> High Risk Anemia -> 7 -> High Risk Follow Up – Follow Up call for checking with no Blood, FCM/Iron blood 004', async function () {
        console.log('\nHigh Risk Anemia -> 7');

        await anemiaFunction.verifyBeneficiaryTreatmentNotGivenBloodNotGivenFCMTwoIronSucroseInjectionPickedUpScheduleForNextVisit();

        await selectDeselectFeatureAccess({
            deSelect: [gvkFeatureConstant.canHighriskFollowupVerification]
        });
    });


});
