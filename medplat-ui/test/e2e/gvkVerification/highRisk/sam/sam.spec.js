const config = require('../../../config');
const loginPage = require('../../../login/login.po');
const browserFunctions = require('../../../common/browser_functions');
const {
    openSelectQueryTab,
} = require('../../../common/utils');
const samPage = require('./sam.po');
const testData = require('./sam.td');

describe('Highrisk SAM', () => {
    beforeAll(async () => {
        await loginPage.login(config.QA_SUPER_ADMIN_USER, config.QA_SUPER_ADMIN_PASSWORD);
        await openSelectQueryTab();
    });

    beforeEach(browserFunctions.beforeEach);

    afterEach(browserFunctions.afterEach);

    afterAll(browserFunctions.afterAll);

    it('GVK Verification -> High Risk SAM -> 1 -> To verify user can call FHW to confirm that beneficiary is having SAM disease.', async function () {
        console.log('\nHigh Risk SAM -> 1');
        const detailsObj = {
            desease: 'SAM',
            isHighRisk: true,
            ...testData.sam001
        };
        await samPage.submitFhwVerificationForm(detailsObj);
    });

    it('GVK Verification -> High Risk SAM -> 2 -> To verify successfull confirmation and scheduling of the beneficiary with SAM disease.', async function () {
        console.log('\nHigh Risk SAM -> 2');

        await samPage.executeBeneficiaryVerificationPositiveSteps();
    });

    it('GVK Verification -> High Risk SAM -> 3 -> To verify user can do successfull follow up checking of the beneficiary with SAM disease.', async function () {
        console.log('\nHigh Risk SAM -> 3');

        await samPage.executeFollowupVerificationStepsPositive();
    });

    it('GVK Verification -> High Risk SAM -> 004 -> High Risk Follow Up – Follow Up call for checking SAM with No 108 service and willing to schedule-004', async function () {
        console.log('\nHigh Risk SAM -> 4');

        await samPage.verifyBeneficiaryTreatmentNo108ServicePickedUpSchedule();
    });

    it('GVK Verification -> High Risk SAM -> 005 -> High Risk Follow Up – Follow Up call for checking SAM with No 108 service and no decision but willing to schedule 108 service 005', async function () {
        console.log('\nHigh Risk SAM -> 5');

        await samPage.verifyBeneficiaryTreatmentNo108ServicePickUpSchedulePending();
    });

    it('GVK Verification -> High Risk SAM -> 006 -> High Risk Follow Up – Follow Up call for checking SAM-108 not came and 108 not required 006', async function () {
        console.log('\nHigh Risk SAM -> 6');

        await samPage.verifyBeneficiaryTreatmentNo108ServiceNotWillngToHelped();
    });

    it('GVK Verification -> High Risk SAM -> 007 -> High Risk Follow Up – Follow Up call for checking SAM no hospital visit but need to visit 007', async function () {
        console.log('\nHigh Risk SAM -> 7');

        await samPage.verifyBeneficiaryTreatmentNoHospitalVisitPickedUpSchedule();
    });

    it('GVK Verification -> High Risk SAM -> 008 -> High Risk Follow Up – Follow Up call for checking SAM no hospital visit and not willing to visit 008', async function () {
        console.log('\nHigh Risk SAM -> 8');

        await samPage.verifyBeneficiaryTreatmentNoHospitalVisitNotWillngToHelped();
    });

    it('GVK Verification -> High Risk SAM -> 009 -> High Risk Follow Up – Follow Up call for checking SAM no child centre visit but need to visit 009', async function () {
        console.log('\nHigh Risk SAM -> 9');

        await samPage.verifyBeneficiaryTreatmentNotAdmittedInHospitalPickedUpSchedule();
    });

    it('GVK Verification -> High Risk SAM -> 010 -> High Risk Follow Up – Follow Up call for checking SAM no child centre visit and no to visit 010', async function () {
        console.log('\nHigh Risk SAM -> 10');

        await samPage.verifyBeneficiaryTreatmentNotAdmittedInHospitalPickedUpSchedule();
    });

    it('GVK Verification -> High Risk SAM -> 11 -> To verify successfull confirmation of beneficiary with no hospital visit and not willing to visit.', async function () {
        console.log('\nHigh Risk SAM -> 11');

        await samPage.verifyNoVisitTwice();
    });

    it('GVK Verification -> High Risk SAM -> 12 -> To verify successfull confirmation of beneficiary with no hospital visit but willing to visit on schedule date.', async function () {
        console.log('\nHigh Risk SAM -> 12');

        await samPage.verifyVisitOnSecondCall();
    });

    it('GVK Verification -> High Risk SAM -> 13 -> To verify successfull confirmation of beneficiary with no hospital visit and no decision for 108 service then scheduling the date.', async function () {
        console.log('\nHigh Risk SAM -> 13');

        await samPage.verifyVisitWithScheduleNotDecided();
    });

    it('GVK Verification -> High Risk SAM -> 14 -> To verify the Successfull verification of the beneficiary with SAM disease No by FHW', async function () {
        console.log('\nHigh Risk SAM -> 14');

        await samPage.executeVerificationStepsForNoVisit();
    });

    it('GVK Verification -> High Risk SAM -> 17 -> To verify successfull confirmation and scheduling without successfull call to beneficiary with SAM disease.', async function () {
        console.log('\nHigh Risk SAM -> 17');

        await samPage.executeVerificationStepsForNotReachableCall();
    },420000);
});
