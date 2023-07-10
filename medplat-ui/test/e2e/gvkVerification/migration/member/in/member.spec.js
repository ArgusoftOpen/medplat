const config = require('../../../../config');
const loginPage = require('../../../../login/login.po');
const browserFunctions = require('../../../../common/browser_functions');
const {
    openSelectQueryTab,
    selectDeselectFeatureAccess,
} = require('../../../../common/utils');
const memberMigrationInPage = require('./member.po');
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

    it('GVK Verification -> MMI -> 1 -> Member Migration In with Create Temporary Member 001', async function () {
        console.log('\Member Migration In with Create Temporary Member 001');

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
            select: [gvkFeatureConstant.canMemberMigrationInVerification],            
        });
        await memberMigrationInPage.createTemporaryMember(testData.mmi001);
    },42000000);

    it('GVK Verification -> MMI -> 2 -> Member Migration In with Search For Member with member id 002', async function () {
        console.log('\nMember Migration In with Search For Member with member id 002');

        await memberMigrationInPage.searchForMeberWithMemberId(testData.mmi002);
    });

    it('GVK Verification -> MMI -> 3 -> Member Migration In with Search For Member with family id 003', async function () {
        console.log('\nMember Migration In with Search For Member with family id 003');
        
        await memberMigrationInPage.searchForMeberWithFamilyId(testData.mmi003);
    });

    it('GVK Verification -> MMI -> 4 -> Member Migration In with Search For Member with Mobile Number 004', async function () {
        console.log('\nMember Migration In with Search For Member with Mobile Number 004');

        await memberMigrationInPage.searchForMeberWithMobileNumber(testData.mmi004);
    },);

    it('GVK Verification -> MMI -> 5 -> Member Migration In with Search For Member with Mobile Number search in family 005', async function () {
        console.log('\nMember Migration In with Search For Member with Mobile Number search in family 005');

        await memberMigrationInPage.searchForMeberWithMobileNumberInFamily(testData.mmi004);
    });

    it('GVK Verification -> MMI -> 6 -> Member Migration In with Search For Member with Organization Unit 006', async function () {
        console.log('\nMember Migration In with Search For Member with Organization Unit 006');

        await memberMigrationInPage.searchForMeberWithOrganizationUnit(testData.mmi006);
    });

    it('GVK Verification -> MMI -> 7 -> Member Migration In without successfull call 007', async function () {
        console.log('\nMember Migration In without successfull call 007');

        await memberMigrationInPage.unsuccessfullCall(testData.mmi007);
    },420000);
});
