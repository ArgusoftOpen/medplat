const EC = protractor.ExpectedConditions;

const {
    gvkVerificationConstant,
    gvkFeatureConstant,
    gvkCallTypeConstnant,
} = require('../../common/gvk_constant')

const {
    submitButton,
    callMother,
    callBeneficiary,
    callFamilyMember,
    callStatusDropDown,
    noRecordsToVerify
} = require('../../common/cssIdentifiers');

const {
    checkToaster,
    clickOnElement,
    getQueryResult,
    dropdownListSelector,
    executeQuery,
    startCallVerification,
    verifyDataInDb,
} = require('../../common/utils');
const cssIdentifiers = require('./beneficiaryService.es');
const testData = require('./beneficiaryService.td');

module.exports = {
    deliveryDoneAtHospital,
    deliveryDoneAtHome,
    deliveryDoneOnTheWay,
    noDelivery,
    withTTVerification,
    withoutTTVerification,
    notDetermineTTVerification,
    unSuccessfullVerification,
    mininumChildCountVerification,
    maximumChildCountVerification,
    equalChildCountVerification,
    wrongDetailChildDeliveryVerification
};


/**
 * Beneficiary Service Verification with Delivery in Hospital verification 001
 */
async function deliveryDoneAtHospital() {

    const recordId = 114374956;

    await executeQuery(`delete from gvk_anm_verification_info where member_id = ${recordId};`);
    await executeQuery(`delete from gvk_anm_verification_response where member_id = ${recordId};`);

    // verify the member state
    const memberStateVerificationQueryResult = await getQueryResult(`select * from imt_member where (basic_state ='VERIFIED' or basic_state='NEW') and id = ${recordId}`);
    expect(memberStateVerificationQueryResult).toBeDefined();

    // insert record into imt_member_duplicate_member_detail
    await executeQuery(`INSERT INTO public.gvk_anm_verification_info
        (member_id, anm_id, asha_id, location_id, service_type, reference_id, schedule_date, 
        call_attempt, gvk_call_status, created_by, created_on, modified_by, modified_on, load_date, priority, service_date, wpd_mother_reference_id)
        VALUES(${recordId}, 65935, 12773, 124752, '${gvkCallTypeConstnant.FHW_CHILD_SERVICE_PREGNANCY_VERIFICATION}', 126128916, (current_date - 1), 0,        
        '${gvkVerificationConstant.callToBeProccessed}', 59220, (current_date - 1), 59220,
        (current_date - 1), '2019-12-01 00:00:00', null, '2019-04-10 00:00:00', '8787694');`);
        
    await startCallVerification();

    const verifyMemberInfoBeforeExecArray = await verifyDataInDb(
        `select * from gvk_anm_verification_info where member_id= ${recordId} order by id desc limit 1`,
        testData.beneficiaryService001.verifyMemberInfoBeforeExec.data);
    console.log(`verifyMemberInfoBeforeExecArray = ${verifyMemberInfoBeforeExecArray}`);
    expect(verifyMemberInfoBeforeExecArray.length).toEqual(0);

    // page title verification
    let pageTitle = element(by.binding('gvkreverification.pageTitle'));
    expect(pageTitle.getText()).toBe(testData.beneficiaryServicePageTitle);


    // call 
    await clickOnElement(callMother);
    await checkToaster('error');


    await clickOnElement(cssIdentifiers.babyDeliveredRadioBtn['yes']);

    await element(cssIdentifiers.totalChildInput).clear().sendKeys('1');
    await element(cssIdentifiers.maleChildCountInput).clear().sendKeys('0');
    await element(cssIdentifiers.femaleChildCountInput).clear().sendKeys('1');

    // select 'Not Reachable' drop down 
    await dropdownListSelector(cssIdentifiers.deliveryPlaceDropDown, 'HOSPITAL');

    await clickOnElement(cssIdentifiers.childServiceVaccinationStatusRadioBtn['yes']);


    await clickOnElement(submitButton);
    await checkToaster('success');

    const verifyMemberInfoAfterExecArray = await verifyDataInDb(
        `select * from gvk_anm_verification_info where member_id= ${recordId} order by id desc limit 1`,
        testData.beneficiaryService001.verifyMemberInfoAfterExec.data);
    console.log(`verifyMemberInfoAfterExecArray = ${verifyMemberInfoAfterExecArray}`);
    expect(verifyMemberInfoAfterExecArray.length).toEqual(0);


    const callManageDetail = await getQueryResult(`select * from gvk_manage_call_master where member_id= ${recordId} order by id desc limit 1`);

    const verifyCallMasterRespnseAfterExecArray = await verifyDataInDb(
        `select * from gvk_manage_call_master where member_id= ${recordId} order by id desc limit 1`,
        testData.beneficiaryService001.callMasterResponse.data);
    console.log(`verifyCallMasterRespnseAfterExecArray = ${verifyCallMasterRespnseAfterExecArray}`);
    expect(verifyCallMasterRespnseAfterExecArray.length).toEqual(0);

    testData.beneficiaryService001.verifyMemberRespnseAfterExec.data.manage_call_master_id = callManageDetail.id;
    const verifyMemberRespnseAfterExecArray = await verifyDataInDb(
        `select * from gvk_anm_verification_response where member_id=${recordId} order by id desc limit 1`,
        testData.beneficiaryService001.verifyMemberRespnseAfterExec.data);
    console.log(`verifyMemberRespnseAfterExecArray = ${verifyMemberRespnseAfterExecArray}`);
    expect(verifyMemberRespnseAfterExecArray.length).toEqual(0);


    // delete the record
    await executeQuery(`delete from gvk_anm_verification_info where member_id = ${recordId};`);
    return executeQuery(`delete from gvk_anm_verification_response where member_id = ${recordId};`);

}

/**
 * Beneficiary Service Verification with Delivery in Home verification 002
 */
async function deliveryDoneAtHome() {

    const recordId = 113485506;

    await executeQuery(`delete from gvk_anm_verification_info where member_id = ${recordId};`);
    await executeQuery(`delete from gvk_anm_verification_response where member_id = ${recordId};`);

    // verify the member state
    const memberStateVerificationQueryResult = await getQueryResult(`select * from imt_member where (basic_state ='VERIFIED' or basic_state='NEW') and id = ${recordId}`);
    expect(memberStateVerificationQueryResult).toBeDefined();

    // insert record into imt_member_duplicate_member_detail
    await executeQuery(`INSERT INTO public.gvk_anm_verification_info
        (member_id, anm_id, asha_id, location_id, service_type, reference_id, schedule_date, 
        call_attempt, gvk_call_status, created_by, created_on, modified_by, modified_on, load_date, priority, service_date, wpd_mother_reference_id)
        VALUES(${recordId}, 65933, 15542, 125630, '${gvkCallTypeConstnant.FHW_CHILD_SERVICE_PREGNANCY_VERIFICATION}', 127637464, (current_date - 1), 0,        
        '${gvkVerificationConstant.callToBeProccessed}', 59220, (current_date - 1), 59220,
        (current_date - 1), '2019-12-01 00:00:00', null, '2019-04-08 00:00:00', '7471765');`);


    await startCallVerification();

    const verifyMemberInfoBeforeExecArray = await verifyDataInDb(
        `select * from gvk_anm_verification_info where member_id= ${recordId} order by id desc limit 1`,
        testData.beneficiaryService002.verifyMemberInfoBeforeExec.data);
    console.log(`verifyMemberInfoBeforeExecArray = ${verifyMemberInfoBeforeExecArray}`);
    expect(verifyMemberInfoBeforeExecArray.length).toEqual(0);

    // page title verification
    let pageTitle = element(by.binding('gvkreverification.pageTitle'));
    expect(pageTitle.getText()).toBe(testData.beneficiaryServicePageTitle);


    // call 
    await clickOnElement(callMother);
    await checkToaster('error');


    await clickOnElement(cssIdentifiers.babyDeliveredRadioBtn['yes']);

    await element(cssIdentifiers.totalChildInput).clear().sendKeys('1');
    await element(cssIdentifiers.maleChildCountInput).clear().sendKeys('1');
    await element(cssIdentifiers.femaleChildCountInput).clear().sendKeys('0');

    // select 'Not Reachable' drop down 
    await dropdownListSelector(cssIdentifiers.deliveryPlaceDropDown, 'HOME');

    await clickOnElement(cssIdentifiers.childServiceVaccinationStatusRadioBtn['yes']);


    await clickOnElement(submitButton);
    await checkToaster('success');

    const verifyMemberInfoAfterExecArray = await verifyDataInDb(
        `select * from gvk_anm_verification_info where member_id= ${recordId} order by id desc limit 1`,
        testData.beneficiaryService002.verifyMemberInfoAfterExec.data);
    console.log(`verifyMemberInfoAfterExecArray = ${verifyMemberInfoAfterExecArray}`);
    expect(verifyMemberInfoAfterExecArray.length).toEqual(0);
    const callManageDetail = await getQueryResult(`select * from gvk_manage_call_master where member_id= ${recordId} order by id desc limit 1`);

    const verifyCallMasterRespnseAfterExecArray = await verifyDataInDb(
        `select * from gvk_manage_call_master where member_id= ${recordId} order by id desc limit 1`,
        testData.beneficiaryService002.callMasterResponse.data);
    console.log(`verifyCallMasterRespnseAfterExecArray = ${verifyCallMasterRespnseAfterExecArray}`);
    expect(verifyCallMasterRespnseAfterExecArray.length).toEqual(0);

    testData.beneficiaryService001.verifyMemberRespnseAfterExec.data.manage_call_master_id = callManageDetail.id;
    const verifyMemberRespnseAfterExecArray = await verifyDataInDb(
        `select * from gvk_anm_verification_response where member_id=${recordId} order by id desc limit 1`,
        testData.beneficiaryService002.verifyMemberRespnseAfterExec.data);
    console.log(`verifyMemberRespnseAfterExecArray = ${verifyMemberRespnseAfterExecArray}`);
    expect(verifyMemberRespnseAfterExecArray.length).toEqual(0);


    // delete the record
    await executeQuery(`delete from gvk_anm_verification_info where member_id = ${recordId};`);
    return executeQuery(`delete from gvk_anm_verification_response where member_id = ${recordId};`);

}

/**
 * Beneficiary Service Verification with Delivery in On The Way verification 003
 */
async function deliveryDoneOnTheWay() {

    const recordId = 117612488;

    await executeQuery(`delete from gvk_anm_verification_info where member_id = ${recordId};`);
    await executeQuery(`delete from gvk_anm_verification_response where member_id = ${recordId};`);

    // verify the member state
    const memberStateVerificationQueryResult = await getQueryResult(`select * from imt_member where (basic_state ='VERIFIED' or basic_state='NEW') and id = ${recordId}`);
    expect(memberStateVerificationQueryResult).toBeDefined();

    // insert record into imt_member_duplicate_member_detail
    await executeQuery(`INSERT INTO public.gvk_anm_verification_info
        (member_id, anm_id, asha_id, location_id, service_type, reference_id, schedule_date, 
        call_attempt, gvk_call_status, created_by, created_on, modified_by, modified_on, load_date, priority, service_date, wpd_mother_reference_id)
        VALUES(${recordId}, 66314, 34813, 214706, '${gvkCallTypeConstnant.FHW_CHILD_SERVICE_PREGNANCY_VERIFICATION}', 127130663, (current_date - 1), 0,        
        '${gvkVerificationConstant.callToBeProccessed}', 59220, (current_date - 1), 59220,
        (current_date - 1), '2019-12-01 00:00:00', null, '2019-04-24 00:00:00', '145058');`);


    await startCallVerification();

    const verifyMemberInfoBeforeExecArray = await verifyDataInDb(
        `select * from gvk_anm_verification_info where member_id= ${recordId} order by id desc limit 1`,
        testData.beneficiaryService003.verifyMemberInfoBeforeExec.data);
    console.log(`verifyMemberInfoBeforeExecArray = ${verifyMemberInfoBeforeExecArray}`);
    expect(verifyMemberInfoBeforeExecArray.length).toEqual(0);

    // page title verification
    let pageTitle = element(by.binding('gvkreverification.pageTitle'));
    expect(pageTitle.getText()).toBe(testData.beneficiaryServicePageTitle);


    // call 
    await clickOnElement(callMother);
    await checkToaster('error');


    await clickOnElement(cssIdentifiers.babyDeliveredRadioBtn['yes']);

    await element(cssIdentifiers.totalChildInput).clear().sendKeys('1');
    await element(cssIdentifiers.maleChildCountInput).clear().sendKeys('1');
    await element(cssIdentifiers.femaleChildCountInput).clear().sendKeys('0');

    // select 'Not Reachable' drop down 
    await dropdownListSelector(cssIdentifiers.deliveryPlaceDropDown, 'ON THE WAY');

    await clickOnElement(cssIdentifiers.childServiceVaccinationStatusRadioBtn['yes']);


    await clickOnElement(submitButton);
    await checkToaster('success');

    const verifyMemberInfoAfterExecArray = await verifyDataInDb(
        `select * from gvk_anm_verification_info where member_id= ${recordId} order by id desc limit 1`,
        testData.beneficiaryService003.verifyMemberInfoAfterExec.data);
    console.log(`verifyMemberInfoAfterExecArray = ${verifyMemberInfoAfterExecArray}`);
    expect(verifyMemberInfoAfterExecArray.length).toEqual(0);
    const callManageDetail = await getQueryResult(`select * from gvk_manage_call_master where member_id= ${recordId} order by id desc limit 1`);

    const verifyCallMasterRespnseAfterExecArray = await verifyDataInDb(
        `select * from gvk_manage_call_master where member_id= ${recordId} order by id desc limit 1`,
        testData.beneficiaryService003.callMasterResponse.data);
    console.log(`verifyCallMasterRespnseAfterExecArray = ${verifyCallMasterRespnseAfterExecArray}`);
    expect(verifyCallMasterRespnseAfterExecArray.length).toEqual(0);

    testData.beneficiaryService001.verifyMemberRespnseAfterExec.data.manage_call_master_id = callManageDetail.id;
    const verifyMemberRespnseAfterExecArray = await verifyDataInDb(
        `select * from gvk_anm_verification_response where member_id=${recordId} order by id desc limit 1`,
        testData.beneficiaryService003.verifyMemberRespnseAfterExec.data);
    console.log(`verifyMemberRespnseAfterExecArray = ${verifyMemberRespnseAfterExecArray}`);
    expect(verifyMemberRespnseAfterExecArray.length).toEqual(0);


    // delete the record
    await executeQuery(`delete from gvk_anm_verification_info where member_id = ${recordId};`);
    return executeQuery(`delete from gvk_anm_verification_response where member_id = ${recordId};`);

}

/**
 * Beneficiary Service Verification with Delivery in On The Way verification 004
 */
async function noDelivery() {
    const recordId = 54123074;
    await executeQuery(`delete from gvk_anm_verification_info where member_id = ${recordId};`);
    await executeQuery(`delete from gvk_anm_verification_response where member_id = ${recordId};`);

    // verify the member state
    const memberStateVerificationQueryResult = await getQueryResult(`select * from imt_member where (basic_state ='VERIFIED' or basic_state='NEW') and id = ${recordId}`);
    expect(memberStateVerificationQueryResult).toBeDefined();

    // insert record into imt_member_duplicate_member_detail
    await executeQuery(`INSERT INTO public.gvk_anm_verification_info
    (member_id, anm_id, asha_id, location_id, service_type, reference_id, schedule_date, 
    call_attempt, gvk_call_status, created_by, created_on, modified_by, modified_on, load_date, priority, service_date, wpd_mother_reference_id)
    VALUES(${recordId}, 65872, 68023, 233155, '${gvkCallTypeConstnant.FHW_CHILD_SERVICE_PREGNANCY_VERIFICATION}', 127412893, (current_date - 1), 0,        
    '${gvkVerificationConstant.callToBeProccessed}', 59220, (current_date - 1), 59220,
    (current_date - 1), '2019-12-01 00:00:00', null, '2019-03-27 00:00:00', '2068605');`);

    await startCallVerification();

    const verifyMemberInfoBeforeExecArray = await verifyDataInDb(
        `select * from gvk_anm_verification_info where member_id= ${recordId} order by id desc limit 1`,
        testData.beneficiaryService004.verifyMemberInfoBeforeExec.data);
    console.log(`verifyMemberInfoBeforeExecArray = ${verifyMemberInfoBeforeExecArray}`);
    expect(verifyMemberInfoBeforeExecArray.length).toEqual(0);

    // page title verification
    let pageTitle = element(by.binding('gvkreverification.pageTitle'));
    expect(pageTitle.getText()).toBe(testData.beneficiaryServicePageTitle);


    // call 
    await clickOnElement(callMother);
    await checkToaster('error');


    await clickOnElement(cssIdentifiers.babyDeliveredRadioBtn['no']);

    await clickOnElement(submitButton);
    await checkToaster('success');

    const verifyMemberInfoAfterExecArray = await verifyDataInDb(
        `select * from gvk_anm_verification_info where member_id= ${recordId} order by id desc limit 1`,
        testData.beneficiaryService004.verifyMemberInfoAfterExec.data);
    console.log(`verifyMemberInfoAfterExecArray = ${verifyMemberInfoAfterExecArray}`);
    expect(verifyMemberInfoAfterExecArray.length).toEqual(0);
    const callManageDetail = await getQueryResult(`select * from gvk_manage_call_master where member_id= ${recordId} order by id desc limit 1`);

    const verifyCallMasterRespnseAfterExecArray = await verifyDataInDb(
        `select * from gvk_manage_call_master where member_id= ${recordId} order by id desc limit 1`,
        testData.beneficiaryService004.callMasterResponse.data);
    console.log(`verifyCallMasterRespnseAfterExecArray = ${verifyCallMasterRespnseAfterExecArray}`);
    expect(verifyCallMasterRespnseAfterExecArray.length).toEqual(0);

    testData.beneficiaryService001.verifyMemberRespnseAfterExec.data.manage_call_master_id = callManageDetail.id;
    const verifyMemberRespnseAfterExecArray = await verifyDataInDb(
        `select * from gvk_anm_verification_response where member_id=${recordId} order by id desc limit 1`,
        testData.beneficiaryService004.verifyMemberRespnseAfterExec.data);
    console.log(`verifyMemberRespnseAfterExecArray = ${verifyMemberRespnseAfterExecArray}`);
    expect(verifyMemberRespnseAfterExecArray.length).toEqual(0);


    // delete the record
    await executeQuery(`delete from gvk_anm_verification_info where member_id = ${recordId};`);
    return executeQuery(`delete from gvk_anm_verification_response where member_id = ${recordId};`);

}

/**
 * Beneficiary Service Verification with TT verification 005
 */
async function withTTVerification() {

    const recordId = 128098;
    await executeQuery(`delete from gvk_anm_verification_info where member_id = ${recordId};`);
    await executeQuery(`delete from gvk_anm_verification_response where member_id = ${recordId};`);

    // verify the member state
    const memberStateVerificationQueryResult = await getQueryResult(`select * from imt_member where (basic_state ='VERIFIED' or basic_state='NEW') and id = ${recordId}`);
    expect(memberStateVerificationQueryResult).toBeDefined();

    // insert record into imt_member_duplicate_member_detail
    await executeQuery(`INSERT INTO public.gvk_anm_verification_info
    (member_id, anm_id, asha_id, location_id, service_type, reference_id, schedule_date, 
    call_attempt, gvk_call_status, created_by, created_on, modified_by, modified_on, load_date, priority, service_date)
    VALUES(${recordId}, 1059, 3564, 2133, 'FHW_TT_VERI', 127522978, (current_date - 1), 0,        
    '${gvkVerificationConstant.callToBeProccessed}', 59220, (current_date - 1), 59220,
    (current_date - 1), '2019-08-01 00:00:00', null, '2019-05-08 00:00:00');`);


    await startCallVerification();

    const verifyMemberInfoBeforeExecArray = await verifyDataInDb(
        `select * from gvk_anm_verification_info where member_id= ${recordId} order by id desc limit 1`,
        testData.beneficiaryService005.verifyMemberInfoBeforeExec.data);
    console.log(`verifyMemberInfoBeforeExecArray = ${verifyMemberInfoBeforeExecArray}`);
    expect(verifyMemberInfoBeforeExecArray.length).toEqual(0);

    // page title verification
    let pageTitle = element(by.binding('gvkreverification.pageTitle'));
    expect(pageTitle.getText()).toBe(testData.beneficiaryServicePageTitle);


    // call 
    await clickOnElement(callBeneficiary);
    await checkToaster('error');


    await clickOnElement(cssIdentifiers.ttInjectionReceivedStatusRadioBtn['yes']);

    await clickOnElement(submitButton);
    await checkToaster('success');

    const verifyMemberInfoAfterExecArray = await verifyDataInDb(
        `select * from gvk_anm_verification_info where member_id= ${recordId} order by id desc limit 1`,
        testData.beneficiaryService005.verifyMemberInfoAfterExec.data);
    console.log(`verifyMemberInfoAfterExecArray = ${verifyMemberInfoAfterExecArray}`);
    expect(verifyMemberInfoAfterExecArray.length).toEqual(0);
    const callManageDetail = await getQueryResult(`select * from gvk_manage_call_master where member_id= ${recordId} order by id desc limit 1`);

    const verifyCallMasterRespnseAfterExecArray = await verifyDataInDb(
        `select * from gvk_manage_call_master where member_id= ${recordId} order by id desc limit 1`,
        testData.beneficiaryService005.callMasterResponse.data);
    console.log(`verifyCallMasterRespnseAfterExecArray = ${verifyCallMasterRespnseAfterExecArray}`);
    expect(verifyCallMasterRespnseAfterExecArray.length).toEqual(0);

    testData.beneficiaryService001.verifyMemberRespnseAfterExec.data.manage_call_master_id = callManageDetail.id;
    const verifyMemberRespnseAfterExecArray = await verifyDataInDb(
        `select * from gvk_anm_verification_response where member_id=${recordId} order by id desc limit 1`,
        testData.beneficiaryService005.verifyMemberRespnseAfterExec.data);
    console.log(`verifyMemberRespnseAfterExecArray = ${verifyMemberRespnseAfterExecArray}`);
    expect(verifyMemberRespnseAfterExecArray.length).toEqual(0);


    // delete the record
    await executeQuery(`delete from gvk_anm_verification_info where member_id = ${recordId};`);
    return executeQuery(`delete from gvk_anm_verification_response where member_id = ${recordId};`);

}

/**
 * Beneficiary Service Verification without TT verification 006
 */
async function withoutTTVerification() {

    const recordId = 128098;

    await executeQuery(`delete from gvk_anm_verification_info where member_id = ${recordId};`);
    await executeQuery(`delete from gvk_anm_verification_response where member_id = ${recordId};`);

    // verify the member state
    const memberStateVerificationQueryResult = await getQueryResult(`select * from imt_member where (basic_state ='VERIFIED' or basic_state='NEW') and id = ${recordId}`);
    expect(memberStateVerificationQueryResult).toBeDefined();

    // insert record into imt_member_duplicate_member_detail
    await executeQuery(`INSERT INTO public.gvk_anm_verification_info
    (member_id, anm_id, asha_id, location_id, service_type, reference_id, schedule_date, 
    call_attempt, gvk_call_status, created_by, created_on, modified_by, modified_on, load_date, priority, service_date)
    VALUES(${recordId}, 1060, null, 2127, 'FHW_TT_VERI', 127450596, (current_date - 1), 0,        
    '${gvkVerificationConstant.callToBeProccessed}', 59220, (current_date - 1), 59220,
    (current_date - 1), '2019-08-01 00:00:00', null, '2019-04-08 00:00:00');`);


    await startCallVerification();

    const verifyMemberInfoBeforeExecArray = await verifyDataInDb(
        `select * from gvk_anm_verification_info where member_id= ${recordId} order by id desc limit 1`,
        testData.beneficiaryService006.verifyMemberInfoBeforeExec.data);
    console.log(`verifyMemberInfoBeforeExecArray = ${verifyMemberInfoBeforeExecArray}`);
    expect(verifyMemberInfoBeforeExecArray.length).toEqual(0);

    // page title verification
    let pageTitle = element(by.binding('gvkreverification.pageTitle'));
    expect(pageTitle.getText()).toBe(testData.beneficiaryServicePageTitle);


    // call 
    await clickOnElement(callBeneficiary);
    await checkToaster('error');


    await clickOnElement(cssIdentifiers.ttInjectionReceivedStatusRadioBtn['no']);

    await clickOnElement(submitButton);
    await checkToaster('success');

    const verifyMemberInfoAfterExecArray = await verifyDataInDb(
        `select * from gvk_anm_verification_info where member_id= ${recordId} order by id desc limit 1`,
        testData.beneficiaryService006.verifyMemberInfoAfterExec.data);
    console.log(`verifyMemberInfoAfterExecArray = ${verifyMemberInfoAfterExecArray}`);
    expect(verifyMemberInfoAfterExecArray.length).toEqual(0);
    const callManageDetail = await getQueryResult(`select * from gvk_manage_call_master where member_id= ${recordId} order by id desc limit 1`);

    const verifyCallMasterRespnseAfterExecArray = await verifyDataInDb(
        `select * from gvk_manage_call_master where member_id= ${recordId} order by id desc limit 1`,
        testData.beneficiaryService006.callMasterResponse.data);
    console.log(`verifyCallMasterRespnseAfterExecArray = ${verifyCallMasterRespnseAfterExecArray}`);
    expect(verifyCallMasterRespnseAfterExecArray.length).toEqual(0);

    testData.beneficiaryService001.verifyMemberRespnseAfterExec.data.manage_call_master_id = callManageDetail.id;
    const verifyMemberRespnseAfterExecArray = await verifyDataInDb(
        `select * from gvk_anm_verification_response where member_id=${recordId} order by id desc limit 1`,
        testData.beneficiaryService006.verifyMemberRespnseAfterExec.data);
    console.log(`verifyMemberRespnseAfterExecArray = ${verifyMemberRespnseAfterExecArray}`);
    expect(verifyMemberRespnseAfterExecArray.length).toEqual(0);


    // delete the record
    await executeQuery(`delete from gvk_anm_verification_info where member_id = ${recordId};`);
    return executeQuery(`delete from gvk_anm_verification_response where member_id = ${recordId};`);

}

/**
 * Beneficiary Service Verification with Can't Determine TT verification 007
 */
async function notDetermineTTVerification() {

    const recordId = 118822672;
    await executeQuery(`delete from gvk_anm_verification_info where member_id = ${recordId};`);
    await executeQuery(`delete from gvk_anm_verification_response where member_id = ${recordId};`);

    // verify the member state
    const memberStateVerificationQueryResult = await getQueryResult(`select * from imt_member where (basic_state ='VERIFIED' or basic_state='NEW') and id = ${recordId}`);
    expect(memberStateVerificationQueryResult).toBeDefined();

    // insert record into imt_member_duplicate_member_detail
    await executeQuery(`INSERT INTO public.gvk_anm_verification_info
    (member_id, anm_id, asha_id, location_id, service_type, reference_id, schedule_date, 
    call_attempt, gvk_call_status, created_by, created_on, modified_by, modified_on, load_date, priority, service_date)
    VALUES(${recordId}, 1051, 3745, 2103, 'FHW_TT_VERI', 127545360, (current_date - 1), 0,        
    '${gvkVerificationConstant.callToBeProccessed}', 59220, (current_date - 1), 59220,
    (current_date - 1), '2019-08-01 00:00:00', null, '2019-05-08 00:00:00');`);


    await startCallVerification();

    const verifyMemberInfoBeforeExecArray = await verifyDataInDb(
        `select * from gvk_anm_verification_info where member_id= ${recordId} order by id desc limit 1`,
        testData.beneficiaryService007.verifyMemberInfoBeforeExec.data);
    console.log(`verifyMemberInfoBeforeExecArray = ${verifyMemberInfoBeforeExecArray}`);
    expect(verifyMemberInfoBeforeExecArray.length).toEqual(0);

    // page title verification
    let pageTitle = element(by.binding('gvkreverification.pageTitle'));
    expect(pageTitle.getText()).toBe(testData.beneficiaryServicePageTitle);


    // call 
    await clickOnElement(callBeneficiary);
    await checkToaster('error');


    await clickOnElement(cssIdentifiers.ttInjectionReceivedStatusRadioBtn['cant-derermine']);

    await clickOnElement(submitButton);
    await checkToaster('success');

    const verifyMemberInfoAfterExecArray = await verifyDataInDb(
        `select * from gvk_anm_verification_info where member_id= ${recordId} order by id desc limit 1`,
        testData.beneficiaryService007.verifyMemberInfoAfterExec.data);
    console.log(`verifyMemberInfoAfterExecArray = ${verifyMemberInfoAfterExecArray}`);
    expect(verifyMemberInfoAfterExecArray.length).toEqual(0);
    const callManageDetail = await getQueryResult(`select * from gvk_manage_call_master where member_id= ${recordId} order by id desc limit 1`);

    const verifyCallMasterRespnseAfterExecArray = await verifyDataInDb(
        `select * from gvk_manage_call_master where member_id= ${recordId} order by id desc limit 1`,
        testData.beneficiaryService007.callMasterResponse.data);
    console.log(`verifyCallMasterRespnseAfterExecArray = ${verifyCallMasterRespnseAfterExecArray}`);
    expect(verifyCallMasterRespnseAfterExecArray.length).toEqual(0);

    testData.beneficiaryService001.verifyMemberRespnseAfterExec.data.manage_call_master_id = callManageDetail.id;
    const verifyMemberRespnseAfterExecArray = await verifyDataInDb(
        `select * from gvk_anm_verification_response where member_id=${recordId} order by id desc limit 1`,
        testData.beneficiaryService007.verifyMemberRespnseAfterExec.data);
    console.log(`verifyMemberRespnseAfterExecArray = ${verifyMemberRespnseAfterExecArray}`);
    expect(verifyMemberRespnseAfterExecArray.length).toEqual(0);


    // delete the record
    await executeQuery(`delete from gvk_anm_verification_info where member_id = ${recordId};`);
    return executeQuery(`delete from gvk_anm_verification_response where member_id = ${recordId};`);

}

/**
 * Beneficiary Service Verification with unsuccessfull call 008
 */
async function unSuccessfullVerification() {

    const recordId = 121004;
    await executeQuery(`delete from gvk_anm_verification_info where member_id = ${recordId};`);
    await executeQuery(`delete from gvk_anm_verification_response where member_id = ${recordId};`);

    // verify the member state
    const memberStateVerificationQueryResult = await getQueryResult(`select * from imt_member where (basic_state ='VERIFIED' or basic_state='NEW') and id = ${recordId}`);
    expect(memberStateVerificationQueryResult).toBeDefined();

    // insert record into imt_member_duplicate_member_detail
    await executeQuery(`INSERT INTO public.gvk_anm_verification_info
                    (member_id, anm_id, asha_id, location_id, service_type, reference_id, schedule_date, 
                    call_attempt, gvk_call_status, created_by, created_on, modified_by, modified_on, load_date, priority, service_date)
                    VALUES(${recordId}, 1056, 3551, 2127, 'FHW_TT_VERI', 126362535, (current_date - 1), 0,        
                    '${gvkVerificationConstant.callToBeProccessed}', 59220, (current_date - 1), 59220,
                    (current_date - 1), '2019-08-01 00:00:00', null, '2019-05-01 00:00:00');`);

    let index = 0;
    while (index < 3) {

        console.log(`Verification iteration : ${index + 1}`);

        await startCallVerification();

        const verifyMemberInfoBeforeExecArray = await verifyDataInDb(
            `select * from gvk_anm_verification_info where member_id= ${recordId} order by id desc limit 1`,
            testData.beneficiaryService008.verifyMemberInfoBeforeExec.data);
        console.log(`verifyMemberInfoBeforeExecArray = ${verifyMemberInfoBeforeExecArray}`);
        expect(verifyMemberInfoBeforeExecArray.length).toEqual(0);

        // page title verification
        let pageTitle = element(by.binding('gvkreverification.pageTitle'));
        expect(pageTitle.getText()).toBe(testData.beneficiaryServicePageTitle);


        await dropdownListSelector(callStatusDropDown, 'Not Reachable');

        // call 
        await clickOnElement(callBeneficiary);
        await checkToaster('error');



        await clickOnElement(submitButton);
        await checkToaster('success');

        testData.beneficiaryService008.verifyMemberInfoBeforeExec.data.call_attempt = `${index + 1}`;
        testData.beneficiaryService008.verifyMemberInfoAfterExec.data.call_attempt = `${index + 1}`;

        const verifyMemberInfoAfterExecArray = await verifyDataInDb(
            `select * from gvk_anm_verification_info where member_id= ${recordId} order by id desc limit 1`,
            testData.beneficiaryService008.verifyMemberInfoAfterExec.data);
        console.log(`verifyMemberInfoAfterExecArray = ${verifyMemberInfoAfterExecArray}`);
        expect(verifyMemberInfoAfterExecArray.length).toEqual(0);
        const callManageDetail = await getQueryResult(`select * from gvk_manage_call_master where member_id= ${recordId} order by id desc limit 1`);

        const verifyCallMasterRespnseAfterExecArray = await verifyDataInDb(
            `select * from gvk_manage_call_master where member_id= ${recordId} order by id desc limit 1`,
            testData.beneficiaryService008.callMasterResponse.data);
        console.log(`verifyCallMasterRespnseAfterExecArray = ${verifyCallMasterRespnseAfterExecArray}`);
        expect(verifyCallMasterRespnseAfterExecArray.length).toEqual(0);

        testData.beneficiaryService008.verifyMemberRespnseAfterExec.data.manage_call_master_id = callManageDetail.id;
        const verifyMemberRespnseAfterExecArray = await verifyDataInDb(
            `select * from gvk_anm_verification_response where member_id=${recordId} order by id desc limit 1`,
            testData.beneficiaryService008.verifyMemberRespnseAfterExec.data);
        console.log(`verifyMemberRespnseAfterExecArray = ${verifyMemberRespnseAfterExecArray}`);
        expect(verifyMemberRespnseAfterExecArray.length).toEqual(0);

        await executeQuery(`update gvk_anm_verification_info set schedule_date = localtimestamp - interval '1 day', 
        modified_on = (localtimestamp - interval '1 day' ) where member_id = ${recordId}`);
        index++;
    }

    await startCallVerification();
    await browser.wait(EC.visibilityOf(element(noRecordsToVerify)), 10000, 'Next record to verify not visible.');

    // delete the record
    await executeQuery(`delete from gvk_anm_verification_info where member_id = ${recordId};`);
    return executeQuery(`delete from gvk_anm_verification_response where member_id = ${recordId};`);

}

/**
 * Beneficiary Service Verification with Delivery in Hospital verification with total child born with minimum value 009
 */
async function mininumChildCountVerification() {

    const recordId = 118677534;

    // verify the member state
    const memberStateVerificationQueryResult = await getQueryResult(`select * from imt_member where (basic_state ='VERIFIED' or basic_state='NEW') and id = ${recordId}`);
    expect(memberStateVerificationQueryResult).toBeDefined();

    // insert record into imt_member_duplicate_member_detail
    await executeQuery(`INSERT INTO public.gvk_anm_verification_info
    (member_id, anm_id, asha_id, location_id, service_type, reference_id, schedule_date, 
    call_attempt, gvk_call_status, created_by, created_on, modified_by, modified_on, load_date, priority, service_date, wpd_mother_reference_id)
    VALUES(${recordId}, 64531, 8263, 32335, '${gvkCallTypeConstnant.FHW_CHILD_SERVICE_PREGNANCY_VERIFICATION}', 126769575, (current_date - 1), 0,        
    '${gvkVerificationConstant.callToBeProccessed}', 59220, (current_date - 1), 59220,
    (current_date - 1), '2019-12-01 00:00:00', null, '2019-05-01 00:00:00', '9250371');`);


    await startCallVerification();

    const verifyMemberInfoBeforeExecArray = await verifyDataInDb(
        `select * from gvk_anm_verification_info where member_id= ${recordId} order by id desc limit 1`,
        testData.beneficiaryService001.verifyMemberInfoBeforeExec.data);
    console.log(`verifyMemberInfoBeforeExecArray = ${verifyMemberInfoBeforeExecArray}`);
    expect(verifyMemberInfoBeforeExecArray.length).toEqual(0);

    // page title verification
    let pageTitle = element(by.binding('gvkreverification.pageTitle'));
    expect(pageTitle.getText()).toBe(testData.beneficiaryServicePageTitle);


    // call 
    await clickOnElement(callMother);
    await checkToaster('error');


    await clickOnElement(cssIdentifiers.babyDeliveredRadioBtn['yes']);

    await element(cssIdentifiers.totalChildInput).clear().sendKeys('0');
    let minChildCountError = element(cssIdentifiers.childCountValidationError);
    expect(minChildCountError.getText()).toBe(testData.minChildCountError);

    await element(cssIdentifiers.maleChildCountInput).clear().sendKeys('0');
    await element(cssIdentifiers.femaleChildCountInput).clear().sendKeys('1');

    // select 'Not Reachable' drop down 
    await dropdownListSelector(cssIdentifiers.deliveryPlaceDropDown, 'HOSPITAL');

    await clickOnElement(cssIdentifiers.childServiceVaccinationStatusRadioBtn['yes']);


    await clickOnElement(submitButton);
    
    // delete the record
    await executeQuery(`delete from gvk_anm_verification_info where member_id = ${recordId};`);
    return executeQuery(`delete from gvk_anm_verification_response where member_id = ${recordId};`);

}

/**
 * Beneficiary Service Verification with Delivery in Hospital verification with total child born with maximum value 010
 */
async function maximumChildCountVerification() {

    const recordId = 73535237;

    // verify the member state
    const memberStateVerificationQueryResult = await getQueryResult(`select * from imt_member where (basic_state ='VERIFIED' or basic_state='NEW') and id = ${recordId}`);
    expect(memberStateVerificationQueryResult).toBeDefined();

    // insert record into imt_member_duplicate_member_detail
    await executeQuery(`INSERT INTO public.gvk_anm_verification_info
    (member_id, anm_id, asha_id, location_id, service_type, reference_id, schedule_date, 
    call_attempt, gvk_call_status, created_by, created_on, modified_by, modified_on, load_date, priority, service_date, wpd_mother_reference_id)
    VALUES(${recordId}, 64531, 8263, 32335, '${gvkCallTypeConstnant.FHW_CHILD_SERVICE_PREGNANCY_VERIFICATION}', 126769575, (current_date - 1), 0,        
    '${gvkVerificationConstant.callToBeProccessed}', 59220, (current_date - 1), 59220,
    (current_date - 1), '2019-12-01 00:00:00', null, '2019-05-01 00:00:00', '9250371');`);


    await startCallVerification();

    const verifyMemberInfoBeforeExecArray = await verifyDataInDb(
        `select * from gvk_anm_verification_info where member_id= ${recordId} order by id desc limit 1`,
        testData.beneficiaryService001.verifyMemberInfoBeforeExec.data);
    console.log(`verifyMemberInfoBeforeExecArray = ${verifyMemberInfoBeforeExecArray}`);
    expect(verifyMemberInfoBeforeExecArray.length).toEqual(0);

    // page title verification
    let pageTitle = element(by.binding('gvkreverification.pageTitle'));
    expect(pageTitle.getText()).toBe(testData.beneficiaryServicePageTitle);


    // call 
    await clickOnElement(callFamilyMember);
    await checkToaster('error');


    await clickOnElement(cssIdentifiers.babyDeliveredRadioBtn['yes']);

    await element(cssIdentifiers.totalChildInput).clear().sendKeys('14');
    await element(cssIdentifiers.maleChildCountInput).clear().sendKeys('7');
    await element(cssIdentifiers.femaleChildCountInput).clear().sendKeys('7');

    let maxChildCountError = element(cssIdentifiers.childCountValidationError);
    expect(maxChildCountError.getText()).toBe(testData.maxChildCountError);

    // select 'Not Reachable' drop down 
    await dropdownListSelector(cssIdentifiers.deliveryPlaceDropDown, 'HOSPITAL');

    await clickOnElement(cssIdentifiers.childServiceVaccinationStatusRadioBtn['yes']);


    await clickOnElement(submitButton);
    // await checkToaster('error');

    // delete the record
    await executeQuery(`delete from gvk_anm_verification_info where member_id = ${recordId};`);
    return executeQuery(`delete from gvk_anm_verification_response where member_id = ${recordId};`);

}

/**
 * Beneficiary Service Verification with Delivery in Hospital verification with total child born not equal to no. of male and female child 011
 */
async function equalChildCountVerification() {

    const recordId = 118677534;

    // verify the member state
    const memberStateVerificationQueryResult = await getQueryResult(`select * from imt_member where (basic_state ='VERIFIED' or basic_state='NEW') and id = ${recordId}`);
    expect(memberStateVerificationQueryResult).toBeDefined();

    // insert record into imt_member_duplicate_member_detail
    await executeQuery(`INSERT INTO public.gvk_anm_verification_info
    (member_id, anm_id, asha_id, location_id, service_type, reference_id, schedule_date, 
    call_attempt, gvk_call_status, created_by, created_on, modified_by, modified_on, load_date, priority, service_date, wpd_mother_reference_id)
    VALUES(${recordId}, 64531, 8263, 32335, '${gvkCallTypeConstnant.FHW_CHILD_SERVICE_PREGNANCY_VERIFICATION}', 126769575, (current_date - 1), 0,        
    '${gvkVerificationConstant.callToBeProccessed}', 59220, (current_date - 1), 59220,
    (current_date - 1), '2019-12-01 00:00:00', null, '2019-05-01 00:00:00', '9250371');`);


    await startCallVerification();

    const verifyMemberInfoBeforeExecArray = await verifyDataInDb(
        `select * from gvk_anm_verification_info where member_id= ${recordId} order by id desc limit 1`,
        testData.beneficiaryService001.verifyMemberInfoBeforeExec.data);
    console.log(`verifyMemberInfoBeforeExecArray = ${verifyMemberInfoBeforeExecArray}`);
    expect(verifyMemberInfoBeforeExecArray.length).toEqual(0);

    // page title verification
    let pageTitle = element(by.binding('gvkreverification.pageTitle'));
    expect(pageTitle.getText()).toBe(testData.beneficiaryServicePageTitle);


    // call 
    await clickOnElement(callMother);
    await checkToaster('error');


    await clickOnElement(cssIdentifiers.babyDeliveredRadioBtn['yes']);

    await element(cssIdentifiers.totalChildInput).clear().sendKeys('4');
    await element(cssIdentifiers.maleChildCountInput).clear().sendKeys('2');
    await element(cssIdentifiers.femaleChildCountInput).clear().sendKeys('3');

    // select 'Not Reachable' drop down 
    await dropdownListSelector(cssIdentifiers.deliveryPlaceDropDown, 'HOSPITAL');

    await clickOnElement(cssIdentifiers.childServiceVaccinationStatusRadioBtn['yes']);


    await clickOnElement(submitButton);
    await checkToaster('error');

    // delete the record
    await executeQuery(`delete from gvk_anm_verification_info where member_id = ${recordId};`);
    return executeQuery(`delete from gvk_anm_verification_response where member_id = ${recordId};`);

}

/**
 * Beneficiary Service Verification with Delivery in Hospital verification with wrong details 012
 */
async function wrongDetailChildDeliveryVerification() {

    const recordId = 118404655;

    // verify the member state
    const memberStateVerificationQueryResult = await getQueryResult(`select * from imt_member where (basic_state ='VERIFIED' or basic_state='NEW') and id = ${recordId}`);
    expect(memberStateVerificationQueryResult).toBeDefined();

    // insert record into imt_member_duplicate_member_detail
    await executeQuery(`INSERT INTO public.gvk_anm_verification_info
    (member_id, anm_id, asha_id, location_id, service_type, reference_id, schedule_date, 
    call_attempt, gvk_call_status, created_by, created_on, modified_by, modified_on, load_date, priority, service_date, wpd_mother_reference_id)
    VALUES(${recordId}, 64531, 8263, 32335, '${gvkCallTypeConstnant.FHW_CHILD_SERVICE_PREGNANCY_VERIFICATION}', 126769575, (current_date - 1), 0,        
    '${gvkVerificationConstant.callToBeProccessed}', 59220, (current_date - 1), 59220,
    (current_date - 1), '2019-12-01 00:00:00', null, '2019-05-01 00:00:00', '9250371' );`);


    await startCallVerification();

    const verifyMemberInfoBeforeExecArray = await verifyDataInDb(
        `select * from gvk_anm_verification_info where member_id= ${recordId} order by id desc limit 1`,
        testData.beneficiaryService009.verifyMemberInfoBeforeExec.data);
    console.log(`verifyMemberInfoBeforeExecArray = ${verifyMemberInfoBeforeExecArray}`);
    expect(verifyMemberInfoBeforeExecArray.length).toEqual(0);

    // page title verification
    let pageTitle = element(by.binding('gvkreverification.pageTitle'));
    expect(pageTitle.getText()).toBe(testData.beneficiaryServicePageTitle);


    // call 
    await clickOnElement(callMother);
    await checkToaster('error');


    await clickOnElement(cssIdentifiers.babyDeliveredRadioBtn['yes']);

    await element(cssIdentifiers.totalChildInput).clear().sendKeys('2');
    await element(cssIdentifiers.maleChildCountInput).clear().sendKeys('1');
    await element(cssIdentifiers.femaleChildCountInput).clear().sendKeys('1');

    // select 'Not Reachable' drop down 
    await dropdownListSelector(cssIdentifiers.deliveryPlaceDropDown, 'HOME');

    await clickOnElement(cssIdentifiers.childServiceVaccinationStatusRadioBtn['no']);


    await clickOnElement(submitButton);
    await checkToaster('success');

    const verifyMemberInfoAfterExecArray = await verifyDataInDb(
        `select * from gvk_anm_verification_info where member_id= ${recordId} order by id desc limit 1`,
        testData.beneficiaryService009.verifyMemberInfoAfterExec.data);
    console.log(`verifyMemberInfoAfterExecArray = ${verifyMemberInfoAfterExecArray}`);
    expect(verifyMemberInfoAfterExecArray.length).toEqual(0);
    const callManageDetail = await getQueryResult(`select * from gvk_manage_call_master where member_id= ${recordId} order by id desc limit 1`);

    const verifyCallMasterRespnseAfterExecArray = await verifyDataInDb(
        `select * from gvk_manage_call_master where member_id= ${recordId} order by id desc limit 1`,
        testData.beneficiaryService009.callMasterResponse.data);
    console.log(`verifyCallMasterRespnseAfterExecArray = ${verifyCallMasterRespnseAfterExecArray}`);
    expect(verifyCallMasterRespnseAfterExecArray.length).toEqual(0);

    testData.beneficiaryService009.verifyMemberRespnseAfterExec.data.manage_call_master_id = callManageDetail.id;
    const verifyMemberRespnseAfterExecArray = await verifyDataInDb(
        `select * from gvk_anm_verification_response where member_id=${recordId} order by id desc limit 1`,
        testData.beneficiaryService009.verifyMemberRespnseAfterExec.data);
    console.log(`verifyMemberRespnseAfterExecArray = ${verifyMemberRespnseAfterExecArray}`);
    expect(verifyMemberRespnseAfterExecArray.length).toEqual(0);


    // delete the record
    await executeQuery(`delete from gvk_anm_verification_info where member_id = ${recordId};`);
    return executeQuery(`delete from gvk_anm_verification_response where member_id = ${recordId};`);

}