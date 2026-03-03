const config = require('../../config');
const loginPage = require('../../login/login.po');
const browserFunctions = require('../../common/browser_functions');
const {
    openSelectQueryTab,
    selectDeselectFeatureAccess
} = require('../../common/utils');

const {
    gvkFeatureConstant,
} = require('../../common/gvk_constant')

const immunizationPage = require('./immunization.po');
const testData = require('./immunization.td');

describe('GVK Immunization', () => {
    beforeAll(async () => {
        await loginPage.login(config.QA_SUPER_ADMIN_USER, config.QA_SUPER_ADMIN_PASSWORD);
        await openSelectQueryTab();
    });

    beforeEach(browserFunctions.beforeEach);

    afterEach(browserFunctions.afterEach);

    afterAll(browserFunctions.afterAll);

    it(`GVK Verification -> Immunization -> 1 -> To verify user can do successfull Verification call for Indradhanush(Immunization) with not asked for vaccination to beneficiary.`, async function () {
        console.log('\nImmunization -> 1');
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
            select: [gvkFeatureConstant.canImmunisationVerification],
        });
        await immunizationPage.executeVerificationAndSubmission(testData.immunization001);
    });

    it(`GVK Verification -> Immunization -> 2 -> To verify user can do successfull Verification call for Indradhanush(Immunization) beneficiary asked for vaccination but not told to come for vaccination.`, async function () {
        console.log('\nImmunization -> 2');

        await immunizationPage.executeVerificationAndSubmission(testData.immunization002);
    });

    it(`GVK Verification -> Immunization -> 3 -> To verify user can do successfull Verification call for Indradhanush(Immunization) beneficiary asked for vaccination and told to come for vaccination on said date.`, async function () {
        console.log('\nImmunization -> 3');

        await immunizationPage.executeVerificationAndSubmission(testData.immunization003);
    });

    it(`GVK Verification -> Immunization -> 4 -> To verify user can do successfull Verification call for Indradhanush(Immunization) beneficiary asked for vaccination and told to come for vaccination but don't know the date.`, async function () {
        console.log('\nImmunization -> 4');

        await immunizationPage.executeVerificationAndSubmission(testData.immunization004);
    });

    it(`GVK Verification -> Immunization -> 5 -> To verify user can do Verification call for Indradhanush(Immunization) without successful call to beneficiary.`, async function () {
        console.log('\nImmunization -> 5');

        await immunizationPage.verifyUnsuccessfulMax(testData.immunization005);
    }, 4200000);
});
