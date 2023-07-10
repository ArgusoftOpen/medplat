const config = require('../../config');
const loginPage = require('../../login/login.po');
const browserFunctions = require('../../common/browser_functions');
const {
    openSelectQueryTab,
    selectDeselectFeatureAccess,
} = require('../../common/utils');

const {
    gvkFeatureConstant,
} = require('../../common/gvk_constant')

const duplicateMemberFunction = require('./duplicateMember.po');

describe('Duplicate Member Verification', () => {
    beforeAll(async () => {
        await loginPage.login(config.QA_SUPER_ADMIN_USER, config.QA_SUPER_ADMIN_PASSWORD);
        await openSelectQueryTab();
    });

    beforeEach(browserFunctions.beforeEach);

    afterEach(browserFunctions.afterEach);

    afterAll(browserFunctions.afterAll);

    it('GVK Verification -> Duplicate Member Verification -> 1 -> Duplicate Member Verification with Invalid Member1 001', async function () {
        console.log('\nDuplicate Member Verification -> 1');
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
            select: [gvkFeatureConstant.canDuplicateMemberVerification]

        });

        await duplicateMemberFunction.invalidFirstMemberRecord();
    }, 4200000);

    it('GVK Verification -> Duplicate Member Verification -> 2 -> Duplicate Member Verification with Invalid Member2 002', async function () {
        console.log('\nDuplicate Member Verification with Invalid Member2 002');

        await duplicateMemberFunction.invalidSecondMemberRecord();
    }, 4200000);

    it('GVK Verification -> Duplicate Member Verification -> 3 -> Duplicate Member Verification with both Member1 and Member2 valid 003', async function () {
        console.log('\nDuplicate Member Verification with both Member1 and Member2 valid 003');

        await duplicateMemberFunction.validBothMemberRecord();
    });

    it('GVK Verification -> Duplicate Member Verification -> 4 -> Duplicate Member Verification with both Member1 and Member2 invalid 004', async function () {
        console.log('\nDuplicate Member Verification with both Member1 and Member2 invalid 004');

        await duplicateMemberFunction.invalidBothMemberRecord();
    });

    it('GVK Verification -> Duplicate Member Verification -> 5 -> Duplicate Member Verification with Member1 invalid and confirm action as NO 005', async function () {
        console.log('\nDuplicate Member Verification with Member1 invalid and confirm action as NO 005');

        await duplicateMemberFunction.invalidFirstMemberRecordWithNoConfirmation();
    });

    it('GVK Verification -> Duplicate Member Verification -> 6 -> Duplicate Member Verification without successfull call 006', async function () {
        console.log('\nDuplicate Member Verification without successfull call 006');

        await duplicateMemberFunction.unscussessfullCall();
    },420000);
});
