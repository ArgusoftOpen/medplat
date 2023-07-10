const config = require('../../../../config');
const loginPage = require('../../../../login/login.po');
const browserFunctions = require('../../../../common/browser_functions');
const {
    openSelectQueryTab,
    selectDeselectFeatureAccess,
} = require('../../../../common/utils');
const memberMigrationOutPage = require('./member.po');
const testData = require('./member.td');

const {
    gvkFeatureConstant,
} = require('../../../../common/gvk_constant')

describe('Member Migration Out', () => {
    beforeAll(async () => {
        await loginPage.login(config.QA_SUPER_ADMIN_USER, config.QA_SUPER_ADMIN_PASSWORD);
        await openSelectQueryTab();
    });

    beforeEach(browserFunctions.beforeEach);

    afterEach(browserFunctions.afterEach);

    afterAll(browserFunctions.afterAll);

    it('GVK Verification -> MMO -> 1 -> To verify successful verification of Member Migration Out with Update Location.', async function () {
        console.log('\nMMO -> 1');

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
            select: [gvkFeatureConstant.canMemberMigrationOutVerification],            
        });
        await memberMigrationOutPage.executeVerificationAndSubmission(testData.mmo001);
    });

    it('GVK Verification -> MMO -> 2 -> To verify successful verification of Member Migration Out with Mark as LFU out of state.', async function () {
        console.log('\nMMO -> 2');

        await memberMigrationOutPage.executeVerificationAndSubmission(testData.mmo002);
    });

    it('GVK Verification -> MMO -> 3 -> To verify successful verification of Member Migration Out with Mark as LFU not out of state.', async function () {
        console.log('\nMMO -> 3');
        // TODO: Reverify. Not working because of bug.
        await memberMigrationOutPage.executeVerificationAndSubmission(testData.mmo003);
    });

    it('GVK Verification -> MMO -> 4 -> To verify successful verification of Member Migration Out with Not Yet Resolved after two unsuccessful verification.', async function () {
        console.log('\nMMO -> 4');

        await memberMigrationOutPage.notResolvedAfterUnsuccessfulAttempt(testData.mmo004);
    }, 420000);

    it('GVK Verification -> MMO -> 5 -> To verify successful verification of Member Migration Out with Rollback.', async function () {
        console.log('\nMMO -> 5');

        await memberMigrationOutPage.executeVerificationAndSubmission(testData.mmo005);
    });

    it('GVK Verification -> MMO -> 6 -> To verify successful verification of Member Migration Out without successful call.', async function () {
        console.log('\nMMO -> 6');

        await memberMigrationOutPage.executeVerificationUnsuccessful(testData.mmo006);
    });
});
