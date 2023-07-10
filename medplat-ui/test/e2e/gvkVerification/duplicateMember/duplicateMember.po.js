const EC = protractor.ExpectedConditions;

const {
    gvkVerificationConstant,
    gvkFeatureConstant,
} = require('../../common/gvk_constant')

const {
    submitButton,
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
const cssIdentifiers = require('./duplicateMember.es');
const commonCssIdentifiers = require('../../common/cssIdentifiers');
const testData = require('./duplicateMember.td');

module.exports = {
    invalidFirstMemberRecord,
    invalidSecondMemberRecord,
    validBothMemberRecord,
    invalidBothMemberRecord,
    invalidFirstMemberRecordWithNoConfirmation,
    unscussessfullCall,
};


let recordId1 = 53164952;
let recordId2 = 86473908;

const firstMemberStateVerificationSQ = `select * from imt_member where (basic_state ='VERIFIED' or basic_state='NEW') and id = ${recordId1}`;
const secondMemberStateVerificationSQ = `select * from imt_member where (basic_state ='VERIFIED' or basic_state='NEW') and id = ${recordId2}`;

const firstMemberInfoSQ = `select * from imt_member where id=${recordId1}`;
const secondMemberInfoSQ = `select * from imt_member where id=${recordId2}`;

const memberInfoSQ = `select * from imt_member_duplicate_member_detail where member1_id=${recordId1} order by id desc limit 1`;
const firstMemberEventMobileNotificationSQ = `select * from event_mobile_notification_pending where member_id=${recordId1} order by id desc limit 1`;
const secondMemberEventMobileNotificationSQ = `select * from event_mobile_notification_pending where member_id=${recordId2} order by id desc limit 1`;
const firstMemberTechoNoticiationSQ = `select * from techo_notification_master where member_id= ${recordId1} order by id desc limit 1`;
const secondMemberTechoNoticiationSQ = `select * from techo_notification_master where member_id= ${recordId2} order by id desc limit 1`;

/**
 * invalid first member record - 001
 */
async function invalidFirstMemberRecord() {

    // verify the member state
    const firstMemberStateVerificationQueryResult = await getQueryResult(firstMemberStateVerificationSQ);
    expect(firstMemberStateVerificationQueryResult).toBeDefined();

    const secondMemberStateVerificationQueryResult = await getQueryResult(secondMemberStateVerificationSQ);
    expect(secondMemberStateVerificationQueryResult).toBeDefined();

    // insert record into imt_member_duplicate_member_detail
    await executeQuery(`INSERT INTO public.imt_member_duplicate_member_detail
    (member1_id, member2_id, is_member1_valid, is_member2_valid, remarks, is_adhar_number_match, is_mobile_number_match, is_name_dob_match, is_lmp_name_match, is_active, 
    created_on, modified_by, modified_on, combination, member1_loc_id, member2_loc_id, loc_id, similarity, gvk_call_status, call_attempt, schedule_date)
    VALUES(${recordId1}, ${recordId2}, null, null, null, false, false, false, true, true,
    (current_date - 1), 59220, (current_date - 1), '${recordId1}, ${recordId2}', 377869, 377869, 377869, 1, '${gvkVerificationConstant.callToBeProccessed}', 0, (current_date - 1));`);

    // insert record into event_mobile_notification_pending

    await executeQuery(`INSERT INTO public.event_mobile_notification_pending
    (notification_configuration_type_id, base_date, user_id, family_id, member_id, created_by, created_on, modified_by, modified_on, is_completed, state, ref_code)
    VALUES('ab633f2d-d4f9-4b66-a94b-0476f7174198', (current_date - 5), 0, 4784511, ${recordId1}, 59220, (current_date - 1), 0, null, false, 'PENDING', 0);`);


    await executeQuery(`INSERT INTO public.event_mobile_notification_pending
    (notification_configuration_type_id, base_date, user_id, family_id, member_id, created_by, created_on, modified_by, modified_on, is_completed, state, ref_code)
    VALUES('28a9d911-4a3d-45d8-ae9d-02d241348302', (current_date - 5), 0, 5173733, ${recordId2}, 59220, (current_date - 1), 0, null, false, 'PENDING', 0);`);


    // insert record into techo_notification_master
    await executeQuery(`INSERT INTO public.techo_notification_master(
        notification_type_id, location_id, other_details, schedule_date, state, migration_id, member_id, 
        created_by, created_on, modified_by, modified_on)
        VALUES(4, 377869, null, (current_date - 1), 'PENDING', null, ${recordId1}, 59220, (current_date - 1), 59220, (current_date - 1));`);

    await executeQuery(`INSERT INTO public.techo_notification_master(
        notification_type_id, location_id, other_details, schedule_date, state, migration_id, member_id, 
        created_by, created_on, modified_by, modified_on) 
        VALUES(5, 377869, null, (current_date - 1), 'RESCHEDULE', null, ${recordId2}, 59220, (current_date - 1), 59220, (current_date - 1));`);


    await startCallVerification();

    const verifyMemberInfoBeforeExecArray = await verifyDataInDb(memberInfoSQ, testData.duplicateMember001.verifyMemberInfoBeforeExec);
    console.log(`verifyMemberInfoBeforeExecArray = ${verifyMemberInfoBeforeExecArray}`);
    expect(verifyMemberInfoBeforeExecArray.length).toEqual(0);


    const verifyFirstMemberEventMobileNotificationInfoBeforeExecArray = await verifyDataInDb(firstMemberEventMobileNotificationSQ, testData.duplicateMember001.verifyFirstMemberEventMobileNotificationInfoBeforeExec);
    console.log(`verifyFirstMemberEventMobileNotificationInfoBeforeExecArray = ${verifyFirstMemberEventMobileNotificationInfoBeforeExecArray}`);
    expect(verifyFirstMemberEventMobileNotificationInfoBeforeExecArray.length).toEqual(0);


    const verifySecondMemberEventMobileNotificationInfoBeforeExecArray = await verifyDataInDb(secondMemberEventMobileNotificationSQ, testData.duplicateMember001.verifySecondMemberEventMobileNotificationInfoBeforeExec);
    console.log(`verifySecondMemberEventMobileNotificationInfoBeforeExecArray = ${verifySecondMemberEventMobileNotificationInfoBeforeExecArray}`);
    expect(verifySecondMemberEventMobileNotificationInfoBeforeExecArray.length).toEqual(0);


    const verifyFirstMemberTechoNotificationInfoBeforeExecArray = await verifyDataInDb(firstMemberTechoNoticiationSQ, testData.duplicateMember001.verifyFirstMemberTechoNotificationInfoBeforeExec);
    console.log(`verifyFirstMemberTechoNotificationInfoBeforeExecArray = ${verifyFirstMemberTechoNotificationInfoBeforeExecArray}`);
    expect(verifyFirstMemberTechoNotificationInfoBeforeExecArray.length).toEqual(0);

    const secondMemberInfoBeforeOperation = await getQueryResult(secondMemberInfoSQ);

    // page title verification
    const duplicateMemberVerifictionPageTitle = 'Duplicate Member Verification';
    let pageTitle = element(by.binding('gvkreverification.pageTitle'));
    expect(pageTitle.getText()).toBe(duplicateMemberVerifictionPageTitle);

    // mark first Member detail as invalid 
    await clickOnElement(cssIdentifiers.invalidFirstMemberCheckbox, 'invalid first member checkbox is not visible');

    await clickOnElement(submitButton);
    await clickOnElement(commonCssIdentifiers.confirmationBtn['yes'], 'confirmation yes model is not visible');
    await checkToaster('success');

    const verifyMemberInfoAfterExecArray = await verifyDataInDb(memberInfoSQ, testData.duplicateMember001.verifyMemberInfoAfterExec);
    console.log(`verifyMemberInfoAfterExecArray = ${verifyMemberInfoAfterExecArray}`);
    expect(verifyMemberInfoAfterExecArray.length).toEqual(0);

    const verifyFirstMemberTechoNotificationInfoAfterExecArray = await verifyDataInDb(firstMemberTechoNoticiationSQ, testData.duplicateMember001.verifyFirstMemberTechoNotificationInfoAfterExec);
    console.log(`verifyFirstMemberTechoNotificationInfoAfterExecArray = ${verifyFirstMemberTechoNotificationInfoAfterExecArray}`);
    expect(verifyFirstMemberTechoNotificationInfoAfterExecArray.length).toEqual(0);

    const verifyFirstMemberEventNotificationInfoAfterExecArray = await verifyDataInDb(firstMemberEventMobileNotificationSQ, testData.duplicateMember001.verifyFirstMemberEventNotificationInfoAfterExec);
    console.log(`verifyFirstMemberEventNotificationInfoAfterExecArray = ${verifyFirstMemberEventNotificationInfoAfterExecArray}`);
    expect(verifyFirstMemberEventNotificationInfoAfterExecArray.length).toEqual(0);

    const verifyFirstMemberInfoAfterExecArray = await verifyDataInDb(firstMemberInfoSQ, testData.duplicateMember001.verifyFirstMemberInfoAfterExec);
    console.log(`verifyFirstMemberInfoAfterExecArray = ${verifyFirstMemberInfoAfterExecArray}`);
    expect(verifyFirstMemberInfoAfterExecArray.length).toEqual(0);

    const secondMemberInfoAfterOperation = await getQueryResult(secondMemberInfoSQ);
    expect(secondMemberInfoAfterOperation.state).toBe(secondMemberInfoBeforeOperation.state);


    // delete the record

    await executeQuery(`delete from imt_member_duplicate_member_detail where member1_id = ${recordId1}`);

    await executeQuery(`delete from event_mobile_notification_pending where member_id = ${recordId1}`);

    await executeQuery(`delete from event_mobile_notification_pending where member_id = ${recordId2}`);

    await executeQuery(`delete from techo_notification_master where member_id = ${recordId1}`);

    return executeQuery(`delete from techo_notification_master where member_id = ${recordId2}`);

}


/**
 * invalid second member record - 002
 */
async function invalidSecondMemberRecord() {

    // verify the member state
    const firstMemberStateVerificationQueryResult = await getQueryResult(firstMemberStateVerificationSQ);
    expect(firstMemberStateVerificationQueryResult).toBeDefined();

    const secondMemberStateVerificationQueryResult = await getQueryResult(secondMemberStateVerificationSQ);
    expect(secondMemberStateVerificationQueryResult).toBeDefined();

    // insert record into imt_member_duplicate_member_detail
    await executeQuery(`INSERT INTO public.imt_member_duplicate_member_detail
    (member1_id, member2_id, is_member1_valid, is_member2_valid, remarks, is_adhar_number_match, is_mobile_number_match, is_name_dob_match, is_lmp_name_match, is_active, 
    created_on, modified_by, modified_on, combination, member1_loc_id, member2_loc_id, loc_id, similarity, gvk_call_status, call_attempt, schedule_date)
    VALUES(${recordId1}, ${recordId2}, null, null, null, false, false, false, true, true,
    (current_date - 1), 59220, (current_date - 1), '${recordId1}, ${recordId2}', 377869, 377869, 377869, 1, '${gvkVerificationConstant.callToBeProccessed}', 0, (current_date - 1));`);

    // insert record into event_mobile_notification_pending

    await executeQuery(`INSERT INTO public.event_mobile_notification_pending
    (notification_configuration_type_id, base_date, user_id, family_id, member_id, created_by, created_on, modified_by, modified_on, is_completed, state, ref_code)
    VALUES('ab633f2d-d4f9-4b66-a94b-0476f7174198', (current_date - 5), 0, 4784511, ${recordId1}, 59220, (current_date - 1), 0, null, false, 'PENDING', 0);`);


    await executeQuery(`INSERT INTO public.event_mobile_notification_pending
    (notification_configuration_type_id, base_date, user_id, family_id, member_id, created_by, created_on, modified_by, modified_on, is_completed, state, ref_code)
    VALUES('28a9d911-4a3d-45d8-ae9d-02d241348302', (current_date - 5), 0, 5173733, ${recordId2}, 59220, (current_date - 1), 0, null, false, 'PENDING', 0);`);


    // insert record into techo_notification_master
    await executeQuery(`INSERT INTO public.techo_notification_master(
        notification_type_id, location_id, other_details, schedule_date, state, migration_id, member_id, 
        created_by, created_on, modified_by, modified_on)
        VALUES(4, 377869, null, (current_date - 1), 'PENDING', null, ${recordId1}, 59220, (current_date - 1), 59220, (current_date - 1));`);

    await executeQuery(`INSERT INTO public.techo_notification_master(
        notification_type_id, location_id, other_details, schedule_date, state, migration_id, member_id, 
        created_by, created_on, modified_by, modified_on) 
        VALUES(5, 377869, null, (current_date - 1), 'RESCHEDULE', null, ${recordId2}, 59220, (current_date - 1), 59220, (current_date - 1));`);


    await startCallVerification();

    const verifyMemberInfoBeforeExecArray = await verifyDataInDb(memberInfoSQ, testData.duplicateMember002.verifyMemberInfoBeforeExec);
    console.log(`verifyMemberInfoBeforeExecArray = ${verifyMemberInfoBeforeExecArray}`);
    expect(verifyMemberInfoBeforeExecArray.length).toEqual(0);


    const verifyFirstMemberEventMobileNotificationInfoBeforeExecArray = await verifyDataInDb(firstMemberEventMobileNotificationSQ, testData.duplicateMember002.verifyFirstMemberEventMobileNotificationInfoBeforeExec);
    console.log(`verifyFirstMemberEventMobileNotificationInfoBeforeExecArray = ${verifyFirstMemberEventMobileNotificationInfoBeforeExecArray}`);
    expect(verifyFirstMemberEventMobileNotificationInfoBeforeExecArray.length).toEqual(0);


    const verifySecondMemberEventMobileNotificationInfoBeforeExecArray = await verifyDataInDb(secondMemberEventMobileNotificationSQ, testData.duplicateMember002.verifySecondMemberEventMobileNotificationInfoBeforeExec);
    console.log(`verifySecondMemberEventMobileNotificationInfoBeforeExecArray = ${verifySecondMemberEventMobileNotificationInfoBeforeExecArray}`);
    expect(verifySecondMemberEventMobileNotificationInfoBeforeExecArray.length).toEqual(0);


    const verifySecondMemberTechoNotificationInfoBeforeExecArray = await verifyDataInDb(secondMemberTechoNoticiationSQ, testData.duplicateMember002.verifySecondMemberTechoNotificationInfoBeforeExec);
    console.log(`verifySecondMemberTechoNotificationInfoBeforeExecArray = ${verifySecondMemberTechoNotificationInfoBeforeExecArray}`);
    expect(verifySecondMemberTechoNotificationInfoBeforeExecArray.length).toEqual(0);

    const firstMemberInfoBeforeOperation = await getQueryResult(firstMemberInfoSQ);

    // page title verification
    const duplicateMemberVerifictionPageTitle = 'Duplicate Member Verification';
    let pageTitle = element(by.binding('gvkreverification.pageTitle'));
    expect(pageTitle.getText()).toBe(duplicateMemberVerifictionPageTitle);

    // click on second Member  detail as invalid 
    await clickOnElement(cssIdentifiers.invalidSecondMemberCheckbox, 'invalid secondMember checkbox is not visible');

    await clickOnElement(submitButton);
    await clickOnElement(commonCssIdentifiers.confirmationBtn['yes'], 'confirmation yes model is not visible');
    await checkToaster('success');

    const verifyMemberInfoAfterExecArray = await verifyDataInDb(`select * from imt_member_duplicate_member_detail where member2_id=${recordId2} order by id desc limit 1`, testData.duplicateMember002.verifyMemberInfoAfterExec);
    console.log(`verifyMemberInfoAfterExecArray = ${verifyMemberInfoAfterExecArray}`);
    expect(verifyMemberInfoAfterExecArray.length).toEqual(0);

    const verifySecondMemberTechoNotificationInfoAfterExecArray = await verifyDataInDb(secondMemberTechoNoticiationSQ, testData.duplicateMember002.verifySecondMemberTechoNotificationInfoAfterExec);
    console.log(`verifySecondMemberTechoNotificationInfoAfterExecArray = ${verifySecondMemberTechoNotificationInfoAfterExecArray}`);
    expect(verifySecondMemberTechoNotificationInfoAfterExecArray.length).toEqual(0);

    const verifySecondMemberEventNotificationInfoAfterExecArray = await verifyDataInDb(firstMemberEventMobileNotificationSQ, testData.duplicateMember002.verifySecondMemberEventNotificationInfoAfterExec);
    console.log(`verifySecondMemberEventNotificationInfoAfterExecArray = ${verifySecondMemberEventNotificationInfoAfterExecArray}`);
    expect(verifySecondMemberEventNotificationInfoAfterExecArray.length).toEqual(0);

    const verifySecondMemberInfoAfterExecArray = await verifyDataInDb(secondMemberInfoSQ, testData.duplicateMember002.verifySecondMemberInfoAfterExec);
    console.log(`verifySecondMemberInfoAfterExecArray = ${verifySecondMemberInfoAfterExecArray}`);
    expect(verifySecondMemberInfoAfterExecArray.length).toEqual(0);

    const firstMemberInfoAfterOperation = await getQueryResult(firstMemberInfoSQ);
    expect(firstMemberInfoAfterOperation.state).toBe(firstMemberInfoBeforeOperation.state);


    // delete the record

    await executeQuery(`delete from imt_member_duplicate_member_detail where member1_id = ${recordId1}`);

    await executeQuery(`delete from event_mobile_notification_pending where member_id = ${recordId1}`);

    await executeQuery(`delete from event_mobile_notification_pending where member_id = ${recordId2}`);

    await executeQuery(`delete from techo_notification_master where member_id = ${recordId1}`);

    return executeQuery(`delete from techo_notification_master where member_id = ${recordId2}`);

}

/**
 * first and second member as valid record - 003
 */
async function validBothMemberRecord() {

    // delete the record
    await executeQuery(`delete from imt_member_duplicate_member_detail where member1_id = ${recordId1};`);

    // verify the member state
    const firstMemberStateVerificationQueryResult = await getQueryResult(firstMemberStateVerificationSQ);
    expect(firstMemberStateVerificationQueryResult).toBeDefined();

    const secondMemberStateVerificationQueryResult = await getQueryResult(secondMemberStateVerificationSQ);
    expect(secondMemberStateVerificationQueryResult).toBeDefined();

    // insert record into imt_member_duplicate_member_detail
    await executeQuery(`INSERT INTO public.imt_member_duplicate_member_detail
    (member1_id, member2_id, is_member1_valid, is_member2_valid, remarks, is_adhar_number_match, is_mobile_number_match, is_name_dob_match, is_lmp_name_match, is_active, 
    created_on, modified_by, modified_on, combination, member1_loc_id, member2_loc_id, loc_id, similarity, gvk_call_status, call_attempt, schedule_date)
    VALUES(${recordId1}, ${recordId2}, null, null, null, false, false, false, true, true,
    (current_date - 1), 59220, (current_date - 1), '${recordId1}, ${recordId2}', 377869, 377869, 377869, 1, '${gvkVerificationConstant.callToBeProccessed}', 0, (current_date - 1));`);


    await startCallVerification();

    const verifyMemberInfoBeforeExecArray = await verifyDataInDb(memberInfoSQ, testData.duplicateMember003.verifyMemberInfoBeforeExec);
    console.log(`verifyMemberInfoBeforeExecArray = ${verifyMemberInfoBeforeExecArray}`);
    expect(verifyMemberInfoBeforeExecArray.length).toEqual(0);

    // page title verification
    const duplicateMemberVerifictionPageTitle = 'Duplicate Member Verification';
    let pageTitle = element(by.binding('gvkreverification.pageTitle'));
    expect(pageTitle.getText()).toBe(duplicateMemberVerifictionPageTitle);

    // temporary taking the status of member to check it after update
    const firstMemberInfoBeforeOperation = await getQueryResult(firstMemberInfoSQ);
    const secondMemberInfoBeforeOperation = await getQueryResult(secondMemberInfoSQ);

    await element(cssIdentifiers.remarkTextBox).clear().sendKeys('Both Member 1 and Member 2 details are valid.');

    // submit the record
    await clickOnElement(submitButton);
    await clickOnElement(commonCssIdentifiers.confirmationBtn['yes'], 'confirmation yes model is not visible');
    await checkToaster('success');

    const verifyMemberInfoAfterExecArray = await verifyDataInDb(`select * from imt_member_duplicate_member_detail where member2_id=${recordId2} order by id desc limit 1`, testData.duplicateMember003.verifyMemberInfoAfterExec);
    console.log(`verifyMemberInfoAfterExecArray = ${verifyMemberInfoAfterExecArray}`);
    expect(verifyMemberInfoAfterExecArray.length).toEqual(0);

    // check the first member status
    const firstMemberInfoAfterOperation = await getQueryResult(firstMemberInfoSQ);
    expect(firstMemberInfoAfterOperation.state).toBe(firstMemberInfoBeforeOperation.state);

    // check the second member status
    const secondMemberInfoAfterOperation = await getQueryResult(secondMemberInfoSQ);
    expect(secondMemberInfoAfterOperation.state).toBe(secondMemberInfoBeforeOperation.state);


    // delete the record
    await executeQuery(`delete from imt_member_duplicate_member_detail where member1_id = ${recordId1}`);

}

/**
 * first and second member as invalid record - 004
 */
async function invalidBothMemberRecord() {

    // delete the record
    await executeQuery(`delete from imt_member_duplicate_member_detail where member1_id = ${recordId1};`);

    // verify the member state
    const firstMemberStateVerificationQueryResult = await getQueryResult(firstMemberStateVerificationSQ);
    expect(firstMemberStateVerificationQueryResult).toBeDefined();

    const secondMemberStateVerificationQueryResult = await getQueryResult(secondMemberStateVerificationSQ);
    expect(secondMemberStateVerificationQueryResult).toBeDefined();

    // insert record into imt_member_duplicate_member_detail
    await executeQuery(`INSERT INTO public.imt_member_duplicate_member_detail
    (member1_id, member2_id, is_member1_valid, is_member2_valid, remarks, is_adhar_number_match, is_mobile_number_match, is_name_dob_match, is_lmp_name_match, is_active, 
    created_on, modified_by, modified_on, combination, member1_loc_id, member2_loc_id, loc_id, similarity, gvk_call_status, call_attempt, schedule_date)
    VALUES(${recordId1}, ${recordId2}, null, null, null, false, false, false, true, true,
    (current_date - 1), 59220, (current_date - 1), '${recordId1}, ${recordId2}', 377869, 377869, 377869, 1, '${gvkVerificationConstant.callToBeProccessed}', 0, (current_date - 1));`);


    await startCallVerification();

    const verifyMemberInfoBeforeExecArray = await verifyDataInDb(memberInfoSQ, testData.duplicateMember004.verifyMemberInfoBeforeExec);
    console.log(`verifyMemberInfoBeforeExecArray = ${verifyMemberInfoBeforeExecArray}`);
    expect(verifyMemberInfoBeforeExecArray.length).toEqual(0);

    // page title verification
    const duplicateMemberVerifictionPageTitle = 'Duplicate Member Verification';
    let pageTitle = element(by.binding('gvkreverification.pageTitle'));
    expect(pageTitle.getText()).toBe(duplicateMemberVerifictionPageTitle);

    // temporary taking the status of member to check it after update
    const firstMemberInfoBeforeOperation = await getQueryResult(firstMemberInfoSQ);
    const secondMemberInfoBeforeOperation = await getQueryResult(secondMemberInfoSQ);

    // mark first memeber as invalid
    await clickOnElement(cssIdentifiers.invalidFirstMemberCheckbox, 'invalid first member checkbox is not visible');

    // mark second memeber as invalid
    await clickOnElement(cssIdentifiers.invalidSecondMemberCheckbox, 'invalid secondMember checkbox is not visible');

    // submit the record
    await clickOnElement(submitButton);
    await checkToaster('error');

    const verifyMemberInfoAfterExecArray = await verifyDataInDb(`select * from imt_member_duplicate_member_detail where member2_id=${recordId2} order by id desc limit 1`, testData.duplicateMember004.verifyMemberInfoAfterExec);
    console.log(`verifyMemberInfoAfterExecArray = ${verifyMemberInfoAfterExecArray}`);
    expect(verifyMemberInfoAfterExecArray.length).toEqual(0);

    // check the first member status
    const firstMemberInfoAfterOperation = await getQueryResult(firstMemberInfoSQ);
    expect(firstMemberInfoAfterOperation.state).toBe(firstMemberInfoBeforeOperation.state);

    // check the second member status
    const secondMemberInfoAfterOperation = await getQueryResult(secondMemberInfoSQ);
    expect(secondMemberInfoAfterOperation.state).toBe(secondMemberInfoBeforeOperation.state);


    // delete the record
    await executeQuery(`delete from imt_member_duplicate_member_detail where member1_id = ${recordId1}`);

}

/**
 * first member as invalid record with no confirmation - 005
 */
async function invalidFirstMemberRecordWithNoConfirmation() {

    // delete the record
    await executeQuery(`delete from imt_member_duplicate_member_detail where member1_id = ${recordId1};`);

    // verify the member state
    const firstMemberStateVerificationQueryResult = await getQueryResult(firstMemberStateVerificationSQ);
    expect(firstMemberStateVerificationQueryResult).toBeDefined();

    const secondMemberStateVerificationQueryResult = await getQueryResult(secondMemberStateVerificationSQ);
    expect(secondMemberStateVerificationQueryResult).toBeDefined();

    // insert record into imt_member_duplicate_member_detail
    await executeQuery(`INSERT INTO public.imt_member_duplicate_member_detail
    (member1_id, member2_id, is_member1_valid, is_member2_valid, remarks, is_adhar_number_match, is_mobile_number_match, is_name_dob_match, is_lmp_name_match, is_active, 
    created_on, modified_by, modified_on, combination, member1_loc_id, member2_loc_id, loc_id, similarity, gvk_call_status, call_attempt, schedule_date)
    VALUES(${recordId1}, ${recordId2}, null, null, null, false, false, false, true, true,
    (current_date - 1), 59220, (current_date - 1), '${recordId1}, ${recordId2}', 377869, 377869, 377869, 1, '${gvkVerificationConstant.callToBeProccessed}', 0, (current_date - 1));`);


    await startCallVerification();

    const verifyMemberInfoBeforeExecArray = await verifyDataInDb(memberInfoSQ, testData.duplicateMember005.verifyMemberInfoBeforeExec);
    console.log(`verifyMemberInfoBeforeExecArray = ${verifyMemberInfoBeforeExecArray}`);
    expect(verifyMemberInfoBeforeExecArray.length).toEqual(0);

    // page title verification
    const duplicateMemberVerifictionPageTitle = 'Duplicate Member Verification';
    let pageTitle = element(by.binding('gvkreverification.pageTitle'));
    expect(pageTitle.getText()).toBe(duplicateMemberVerifictionPageTitle);

    // temporary taking the status of member to check it after update
    const firstMemberInfoBeforeOperation = await getQueryResult(firstMemberInfoSQ);
    const secondMemberInfoBeforeOperation = await getQueryResult(secondMemberInfoSQ);

    // mark first memeber as invalid
    await clickOnElement(cssIdentifiers.invalidFirstMemberCheckbox, 'invalid first member checkbox is not visible');

    // submit the record
    await clickOnElement(submitButton);
    await clickOnElement(commonCssIdentifiers.confirmationBtn['no'], 'confirmation yes model is not visible');


    const verifyMemberInfoAfterExecArray = await verifyDataInDb(memberInfoSQ, testData.duplicateMember005.verifyMemberInfoAfterExec);
    console.log(`verifyMemberInfoAfterExecArray = ${verifyMemberInfoAfterExecArray}`);
    expect(verifyMemberInfoAfterExecArray.length).toEqual(0);

    // check the first member status
    const firstMemberInfoAfterOperation = await getQueryResult(firstMemberInfoSQ);
    expect(firstMemberInfoAfterOperation.state).toBe(firstMemberInfoBeforeOperation.state);

    // check the second member status
    const secondMemberInfoAfterOperation = await getQueryResult(secondMemberInfoSQ);
    expect(secondMemberInfoAfterOperation.state).toBe(secondMemberInfoBeforeOperation.state);


    // delete the record
    await executeQuery(`delete from imt_member_duplicate_member_detail where member1_id = ${recordId1}`);

}

/**
 * unsuccessful attempt - 006
 */
async function unscussessfullCall() {

    // delete the record
    await executeQuery(`delete from imt_member_duplicate_member_detail where member1_id = ${recordId1};`);

    // verify the member state
    const firstMemberStateVerificationQueryResult = await getQueryResult(firstMemberStateVerificationSQ);
    expect(firstMemberStateVerificationQueryResult).toBeDefined();

    const secondMemberStateVerificationQueryResult = await getQueryResult(secondMemberStateVerificationSQ);
    expect(secondMemberStateVerificationQueryResult).toBeDefined();

    // insert record into imt_member_duplicate_member_detail
    await executeQuery(`INSERT INTO public.imt_member_duplicate_member_detail
    (member1_id, member2_id, is_member1_valid, is_member2_valid, remarks, is_adhar_number_match, is_mobile_number_match, is_name_dob_match, is_lmp_name_match, is_active, 
    created_on, modified_by, modified_on, combination, member1_loc_id, member2_loc_id, loc_id, similarity, gvk_call_status, call_attempt, schedule_date)
    VALUES(${recordId1}, ${recordId2}, null, null, null, false, false, false, true, true,
    (current_date - 1), 59220, (current_date - 1), '${recordId1}, ${recordId2}', 377869, 377869, 377869, 1, '${gvkVerificationConstant.callToBeProccessed}', 0, (current_date - 1));`);


    await startCallVerification();

    const verifyMemberInfoBeforeFirstExecArray = await verifyDataInDb(memberInfoSQ, testData.duplicateMember006.verifyMemberInfoBeforeFirstExec);
    console.log(`verifyMemberInfoBeforeFirstExecArray = ${verifyMemberInfoBeforeFirstExecArray}`);
    expect(verifyMemberInfoBeforeFirstExecArray.length).toEqual(0);

    // page title verification
    const duplicateMemberVerifictionPageTitle = 'Duplicate Member Verification';
    let pageTitle = element(by.binding('gvkreverification.pageTitle'));
    expect(pageTitle.getText()).toBe(duplicateMemberVerifictionPageTitle);

    await element(cssIdentifiers.remarkTextBox).clear().sendKeys('This is first time remark for not reachable.');

    // select 'Not Reachable' drop down 
    await dropdownListSelector(commonCssIdentifiers.callStatusDropDown, 'Not Reachable');

    // submit the record
    await clickOnElement(submitButton);

    const verifyMemberInfoAfterFirstExecArray = await verifyDataInDb(memberInfoSQ, testData.duplicateMember006.verifyMemberInfoAfterFirstExec);
    console.log(`verifyMemberInfoAfterFirstExecArray = ${verifyMemberInfoAfterFirstExecArray}`);
    expect(verifyMemberInfoAfterFirstExecArray.length).toEqual(0);

    const verifyResponseAfterFirstExecArray = await verifyDataInDb(`select * from gvk_duplicate_member_verification_response where duplicate_member_verification_id= (select id from imt_member_duplicate_member_detail where member1_id= ${recordId1}) order by modified_on desc limit 1`, testData.duplicateMember006.verifyResponseAfterFirstExec);
    console.log(`verifyResponseAfterFirstExecArray = ${verifyResponseAfterFirstExecArray}`);
    expect(verifyResponseAfterFirstExecArray.length).toEqual(0);


    // update the record
    await executeQuery(`update imt_member_duplicate_member_detail set schedule_date = localtimestamp - interval '1 day', modified_on = (localtimestamp - interval '1 day' ) where member1_id = ${recordId1}`);


    // make a second call

    await startCallVerification();

    const verifyMemberInfoBeforeSecondExecArray = await verifyDataInDb(memberInfoSQ, testData.duplicateMember006.verifyMemberInfoBeforeSecondExec);
    console.log(`verifyMemberInfoBeforeSecondExecArray = ${verifyMemberInfoBeforeSecondExecArray}`);
    expect(verifyMemberInfoBeforeSecondExecArray.length).toEqual(0);

    // page title verification
    pageTitle = element(by.binding('gvkreverification.pageTitle'));
    expect(pageTitle.getText()).toBe(duplicateMemberVerifictionPageTitle);

    await element(cssIdentifiers.remarkTextBox).clear().sendKeys('This is Second time remark for not reachable.');

    // select 'Not Reachable' drop down 
    await dropdownListSelector(commonCssIdentifiers.callStatusDropDown, 'Not Reachable');

    // submit the record
    await clickOnElement(submitButton);

    const verifyMemberInfoAfterSecondExecArray = await verifyDataInDb(memberInfoSQ, testData.duplicateMember006.verifyMemberInfoAfterSecondExec);
    console.log(`verifyMemberInfoAfterSecondExecArray = ${verifyMemberInfoAfterSecondExecArray}`);
    expect(verifyMemberInfoAfterSecondExecArray.length).toEqual(0);

    const verifyResponseAfterSecondExecArray = await verifyDataInDb(`select * from gvk_duplicate_member_verification_response where duplicate_member_verification_id= (select id from imt_member_duplicate_member_detail where member1_id= ${recordId1}) order by modified_on desc limit 1`, testData.duplicateMember006.verifyResponseAfterSecondExec);
    console.log(`verifyResponseAfterSecondExecArray = ${verifyResponseAfterSecondExecArray}`);
    expect(verifyResponseAfterSecondExecArray.length).toEqual(0);

    // update the record
    await executeQuery(`update imt_member_duplicate_member_detail set schedule_date = localtimestamp - interval '1 day', modified_on = (localtimestamp - interval '1 day' ) where member1_id = ${recordId1}`);


    // make a third call

    await startCallVerification();

    const verifyMemberInfoBeforeThirdExecArray = await verifyDataInDb(memberInfoSQ, testData.duplicateMember006.verifyMemberInfoBeforeThirdExec);
    console.log(`verifyMemberInfoBeforeThirdExecArray = ${verifyMemberInfoBeforeThirdExecArray}`);
    expect(verifyMemberInfoBeforeThirdExecArray.length).toEqual(0);

    // page title verification
    pageTitle = element(by.binding('gvkreverification.pageTitle'));
    expect(pageTitle.getText()).toBe(duplicateMemberVerifictionPageTitle);

    await element(cssIdentifiers.remarkTextBox).clear().sendKeys(' This is Third time remark for not reachable.');

    // select 'Not Reachable' drop down 
    await dropdownListSelector(commonCssIdentifiers.callStatusDropDown, 'Not Reachable');

    // submit the record
    await clickOnElement(submitButton);

    const verifyMemberInfoAfterThirdExecArray = await verifyDataInDb(memberInfoSQ, testData.duplicateMember006.verifyMemberInfoAfterThirdExec);
    console.log(`verifyMemberInfoAfterThirdExecArray = ${verifyMemberInfoAfterThirdExecArray}`);
    expect(verifyMemberInfoAfterThirdExecArray.length).toEqual(0);

    const verifyResponseAfterThirdExecArray = await verifyDataInDb(`select * from gvk_duplicate_member_verification_response where duplicate_member_verification_id= (select id from imt_member_duplicate_member_detail where member1_id= ${recordId1}) order by modified_on desc limit 1`, testData.duplicateMember006.verifyResponseAfterThirdExec);
    console.log(`verifyResponseAfterThirdExecArray = ${verifyResponseAfterThirdExecArray}`);
    expect(verifyResponseAfterThirdExecArray.length).toEqual(0);


    // update the record
    await executeQuery(`update imt_member_duplicate_member_detail set schedule_date = localtimestamp - interval '1 day', modified_on = (localtimestamp - interval '1 day' ) where member1_id = ${recordId1}`);


    // male a Fourth call

    await startCallVerification();

    await browser.wait(EC.visibilityOf(element(commonCssIdentifiers.noRecordsToVerify)), 10000, 'No records to verify message not visible.');

    // Deleting record from user info table and response table
    await executeQuery(`delete from imt_member_duplicate_member_detail where member1_id = ${recordId1}`);




}