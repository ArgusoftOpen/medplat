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

const beneficiaryServiceFunction = require('./beneficiaryService.po');

describe('Beneficiary Service Verification', () => {
    beforeAll(async () => {
        await loginPage.login(config.QA_SUPER_ADMIN_USER, config.QA_SUPER_ADMIN_PASSWORD);
        await openSelectQueryTab();
    });

    beforeEach(browserFunctions.beforeEach);

    afterEach(browserFunctions.afterEach);

    afterAll(browserFunctions.afterAll);

    it('GVK Verification -> Beneficiary Service Verification -> 1 -> Beneficiary Service Verification with Delivery in Hospital verification 001', async function () {
        console.log('\nBeneficiary Service Verification -> 001');
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
            select: [gvkFeatureConstant.canPerformAnmVerification]

        });

        await beneficiaryServiceFunction.deliveryDoneAtHospital();
    });

    it('GVK Verification -> Beneficiary Service Verification -> 2 -> Beneficiary Service Verification with Delivery in Home verification 002', async function () {
        console.log('\nBeneficiary Service Verification 002');

        await beneficiaryServiceFunction.deliveryDoneAtHome();
    });

    it('GVK Verification -> Beneficiary Service Verification -> 3 -> Beneficiary Service Verification with Delivery in On The Way verification 003', async function () {
        console.log('\nBeneficiary Service Verification 003');

        await beneficiaryServiceFunction.deliveryDoneOnTheWay();
    });

    it('GVK Verification -> Beneficiary Service Verification -> 4 -> Beneficiary Service Verification without Delivery verification 004', async function () {
        console.log('\nBeneficiary Service Verification 004');

        await beneficiaryServiceFunction.noDelivery();
    });

    it('GVK Verification -> Beneficiary Service Verification -> 5 -> Beneficiary Service Verification with TT verification 005', async function () {
        console.log('\nBeneficiary Service Verification 005');

        await beneficiaryServiceFunction.withTTVerification();
    });

    it('GVK Verification -> Beneficiary Service Verification -> 6 -> Beneficiary Service Verification without TT verification 006', async function () {
        console.log('\nBeneficiary Service Verification 006');

        await beneficiaryServiceFunction.withoutTTVerification();
    });

    it('GVK Verification -> Beneficiary Service Verification -> 7 -> Beneficiary Service Verification with Cant Determine TT verification 007', async function () {
        console.log('\nBeneficiary Service Verification 007');

        await beneficiaryServiceFunction.notDetermineTTVerification();
    });

    it('GVK Verification -> Beneficiary Service Verification -> 8 -> Beneficiary Service Verification without successfull call 008', async function () {
        console.log('\nBeneficiary Service Verification 8');

        await beneficiaryServiceFunction.unSuccessfullVerification();
    },420000);

    it('GVK Verification -> Beneficiary Service Verification -> 9 -> Beneficiary Service Verification with Delivery in Hospital verification with total child born with minimum value 009', async function () {
        console.log('\nBeneficiary Service Verification 009');

        await beneficiaryServiceFunction.mininumChildCountVerification();
    });

    it('GVK Verification -> Beneficiary Service Verification -> 10 -> Beneficiary Service Verification with Delivery in Hospital verification with total child born with maximum value 010', async function () {
        console.log('\nBeneficiary Service Verification 010');

        await beneficiaryServiceFunction.maximumChildCountVerification();
    });

    it('GVK Verification -> Beneficiary Service Verification -> 11 -> Beneficiary Service Verification with Delivery in Hospital verification with total child born not equal to no. of male and female child 011', async function () {
        console.log('\nBeneficiary Service Verification 011');

        await beneficiaryServiceFunction.equalChildCountVerification();
    });

    it('GVK Verification -> Beneficiary Service Verification -> 12 -> Beneficiary Service Verification with Delivery in Hospital verification with wrong details 012', async function () {
        console.log('\nBeneficiary Service Verification 012');

        await beneficiaryServiceFunction.wrongDetailChildDeliveryVerification();
    });

});
