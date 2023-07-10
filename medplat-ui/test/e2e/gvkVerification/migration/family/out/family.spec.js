const config = require('../../../../config');
const loginPage = require('../../../../login/login.po');
const browserFunctions = require('../../../../common/browser_functions');
const {
    openSelectQueryTab,
    selectDeselectFeatureAccess,
} = require('../../../../common/utils');
const familyMigrationOutPage = require('./family.po');
const testData = require('./family.td');

const {
    gvkFeatureConstant,
} = require('../../../../common/gvk_constant')

describe('Family Migration Out', () => {
    beforeAll(async () => {
        await loginPage.login(config.QA_SUPER_ADMIN_USER, config.QA_SUPER_ADMIN_PASSWORD);
        await openSelectQueryTab();
    });

    beforeEach(browserFunctions.beforeEach);

    afterEach(browserFunctions.afterEach);

    afterAll(browserFunctions.afterAll);

    it('GVK Verification -> Family Migration Out -> 1 -> Family Migration Out with Update Location 001', async function () {
        console.log('\nFamily Migration Out -> 1');

        await selectDeselectFeatureAccess({
            deSelect: [
                gvkFeatureConstant.canFamilyVerification,
                gvkFeatureConstant.canAbsentVerification,
                gvkFeatureConstant.canImmunisationVerification,
                gvkFeatureConstant.canHighriskFollowupVerification,
                gvkFeatureConstant.canHighriskFollowupVerificationForFhw,
                gvkFeatureConstant.canPregnancyRegistrationsVerification,
                gvkFeatureConstant.canPregnancyRegistrationsPhoneNumberVerification,
                gvkFeatureConstant.canMemberMigrationOutVerification,
                gvkFeatureConstant.canMemberMigrationInVerification,
                gvkFeatureConstant.canPerformEligibleCoupleCounselling,
                gvkFeatureConstant.canPerformAnmVerification,
                gvkFeatureConstant.canFamilyMigrationOutVerification,
                gvkFeatureConstant.canDuplicateMemberVerification,
            ],
            select: [gvkFeatureConstant.canFamilyMigrationOutVerification],
        });
        await familyMigrationOutPage.executeVerificationAndSubmission(testData.familyMigratinOut001);
    }, 4200000);

    it('GVK Verification -> Family Migration Out -> 2 -> Family Migration Out with Mark as LFU out of state 002', async function () {
        console.log('\nFamily Migration Out -> 2');

        await familyMigrationOutPage.executeVerificationAndSubmission(testData.familyMigratinOut002);
    }, 4200000);

    it('GVK Verification -> Family Migration Out -> 3 -> Family Migration Out with Mark as LFU not out of state 003', async function () {
        console.log('\nFamily Migration Out -> 3');
        // TODO: Reverify. Not working because of bug.
        await familyMigrationOutPage.executeVerificationAndSubmission(testData.familyMigratinOut003);
    }, 4200000);

    it('GVK Verification -> Family Migration Out -> 4 -> Family Migration Out with Not Yet Resolved 004', async function () {
        console.log('\nFamily Migration Out -> 4');

        await familyMigrationOutPage.notResolvedAfterUnsuccessfulAttempt(testData.familyMigratinOut004);
    }, 5600000);

    it('GVK Verification -> Family Migration Out -> 5 -> Family Migration Out with Rollback 005', async function () {
        console.log('\nFamily Migration Out -> 5');

        await familyMigrationOutPage.executeVerificationAndSubmission(testData.familyMigratinOut005);
    }, 4200000);

    it('GVK Verification -> Family Migration Out -> 6 -> Family Migration Out without successfull call 006', async function () {
        console.log('\nFamily Migration Out -> 6');

        await familyMigrationOutPage.executeVerificationUnsuccessful(testData.familyMigratinOut006);        
    }, 5600000);
});
