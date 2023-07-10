const EC = protractor.ExpectedConditions;

const {
    gvkVerificationConstant,
    gvkFeatureConstant,
} = require('../../../common/gvk_constant')

const {
    submitButton,
    callFamilyHead,
} = require('../../../common/cssIdentifiers');
const {
    checkToaster,
    clickOnElement,
    dateTimeSelector,
    getQueryResult,
    executeQuery,
    startCallVerification,
    verifyDataInDb,
    selectDeselectFeatureAccess,
} = require('../../../common/utils');
const cssIdentifiers = require('../common/common.es');
const testData = require('./anemia.td');

let record_id = 114475601;
const memberStateVerificationQuery = `select * from imt_member where (basic_state ='VERIFIED' or basic_state='NEW') and id = ${record_id}`
const userInfoSeletQuery = `select * from gvk_high_risk_follow_up_usr_info where member_id = ${record_id} order by id desc limit 1`;
const responseSelectQuery = `select * from gvk_high_risk_follow_up_responce where member_id = ${record_id} order by id desc limit 1`;
const deleteQuery = `delete from gvk_high_risk_follow_up_usr_info where member_id = ${record_id};
                        delete from gvk_high_risk_follow_up_responce where member_id = ${record_id};`;

module.exports = {
    verifyBeneficiaryTreatmentGivenBloodGivenFCMNotGivenIronSucroseInjection,
    verifyBeneficiaryTreatmentGivenBloodNotGivenFCMGivenIronSucroseThreeInjection,
    verifyBeneficiaryTreatmentGivenBloodNotGivenFCMNotGivenIronSucroseInjection,
    verifyBeneficiaryTreatmentNotGivenBloodNotGivenFCMTwoIronSucroseInjectionPickedUpScheduleForNextVisit,
    verifyBeneficiaryTreatmentNotGivenBloodNotGivenFCMNotGivenIronSucroseInjectionPickedUpScheduleForNextVisit,
    verifyBeneficiaryTreatmentNotGivenBloodNotGivenFCMNotGivenIronSucroseInjectionPickedUpScheduleForNextVisitPending,
};

/**
 *  beneficiary has given blood and FCM, not given iron sucrose injection
 */
async function verifyBeneficiaryTreatmentGivenBloodGivenFCMNotGivenIronSucroseInjection() {

    console.log('High Risk Anemia -> 1 -> High Risk Follow Up – Follow Up call for checking with FCM 001');
    await selectDeselectFeatureAccess({
        deSelect: [gvkFeatureConstant.canHighriskFollowupVerificationForFhw],
        select: [gvkFeatureConstant.canHighriskFollowupVerification]
    });

    const memberStateVerificationQueryResult = await getQueryResult(memberStateVerificationQuery);
    expect(memberStateVerificationQueryResult).toBeDefined();

    // delete information from the table.
    await executeQuery(deleteQuery);

    // insert record
    await executeQuery(`insert into gvk_high_risk_follow_up_usr_info
        (member_id, gvk_call_state, gvk_call_status,schedule_date,
        call_attempt,diseases,highrisk_reason,location_id, pickup_schedule_date,  
        created_by,created_on,modified_by,modified_on, identify_date)
        values
        (${record_id},'${gvkVerificationConstant.pickedupSchedule}', '${gvkVerificationConstant.callProcessing}', (localtimestamp),
         1, 'Severe Maternal Anemia', 'Hemog
         lobin count:4', 137402, (localtimestamp - interval '1 day'),
        -1,(localtimestamp - interval '2 hour'),-1, (localtimestamp - interval '2 hour'), '2019-03-01')`);

    await startCallVerification();

    // check the state
    const verifyMemberInfoBeforeExecArray = await verifyDataInDb(userInfoSeletQuery, testData.anemia001.verifyMemberInfoBeforeExec);
    console.log(`verifyMemberInfoBeforeExecArray = ${verifyMemberInfoBeforeExecArray}`);
    expect(verifyMemberInfoBeforeExecArray.length).toEqual(0);

    const beneficiaryFollowupPageTitle = 'High Risk follow up - Follow up call for checking';
    let pageTitle = element(by.binding('gvkreverification.pageTitle'));
    expect(pageTitle.getText()).toBe(beneficiaryFollowupPageTitle);

    await clickOnElement(callFamilyHead, 'Calling family head button not visible.');
    await checkToaster('error');

    await clickOnElement(cssIdentifiers.is108PickedupBeneficiaryRadioBtn[true], '108 picked up button is not visible.');
    await clickOnElement(cssIdentifiers.isBeneficiaryVisitedPHCRadioBtn[true], 'Beneficiary visited phc button is not visible.');


    await clickOnElement(cssIdentifiers.isBeneficiaryReceivedBloodLastWeekRadioBtn[true], 'injection flag button is not visible.');
    await clickOnElement(cssIdentifiers.isBeneficiaryReceivedFCMLastWeekRadioBtn[true], 'injection flag button is not visible.');
    await clickOnElement(cssIdentifiers.isBeneficiaryReceivedIronSucroseInjectionRadioBtn['no'], 'injection flag button is not visible.');

    await clickOnElement(cssIdentifiers.isBeneficiaryDropedAtHomeBy108RadioBtn[true], '108dropped Button is not visible');

    await clickOnElement(submitButton, 'submit response is not visible');
    await checkToaster('success');

    // Verifying followup member info after submission 
    const verifyMemberInfoAfterExecArray = await verifyDataInDb(userInfoSeletQuery, testData.anemia001.verifyMemberInfoAfterExec);
    console.log(`verifyMemberInfoAfterExecArray = ${verifyMemberInfoAfterExecArray}`);
    expect(verifyMemberInfoAfterExecArray.length).toEqual(0);

    const verifyResponseAfterExecArray = await verifyDataInDb(responseSelectQuery, testData.anemia001.verifyResponseAfterExec);
    console.log(`verifyResponseAfterExecArray = ${verifyResponseAfterExecArray}`);
    expect(verifyResponseAfterExecArray.length).toEqual(0);

    // Deleting record from user info table and response table
    return executeQuery(deleteQuery);

}

/**
 *  beneficiary has given blood, not given FCM, given three iron sucrose injection
 */
async function verifyBeneficiaryTreatmentGivenBloodNotGivenFCMGivenIronSucroseThreeInjection() {

    console.log('High Risk Anemia -> 2 -> High Risk Follow Up – Follow Up call for checking with Iron 002');


    const memberStateVerificationQueryResult = await getQueryResult(memberStateVerificationQuery);
    expect(memberStateVerificationQueryResult).toBeDefined();

    // Deleting record from user info table and response table
    await executeQuery(deleteQuery);

    // insert record
    await executeQuery(`insert into gvk_high_risk_follow_up_usr_info
    (member_id, gvk_call_state, gvk_call_status,schedule_date,
    call_attempt,diseases,highrisk_reason,location_id, pickup_schedule_date,  
    created_by,created_on,modified_by,modified_on, identify_date)
    values
    (${record_id},'${gvkVerificationConstant.pickedupSchedule}', '${gvkVerificationConstant.callProcessing}', (localtimestamp),
    1, 'Severe Maternal Anemia', 'Hemoglobin count:4', 137402, (localtimestamp - interval '1 day'),
    -1,(localtimestamp - interval '2 hour'),-1, (localtimestamp - interval '2 hour'), '2019-03-01') `);

    await startCallVerification();

    // check the query result
    const verifyMemberInfoBeforeExecArray = await verifyDataInDb(userInfoSeletQuery, testData.anemia002.verifyMemberInfoBeforeExec);
    console.log(`verifyMemberInfoBeforeExecArray = ${verifyMemberInfoBeforeExecArray}`);
    expect(verifyMemberInfoBeforeExecArray.length).toEqual(0);



    const beneficiaryFollowupPageTitle = 'High Risk follow up - Follow up call for checking';
    let pageTitle = element(by.binding('gvkreverification.pageTitle'));
    expect(pageTitle.getText()).toBe(beneficiaryFollowupPageTitle);

    await clickOnElement(callFamilyHead, 'Calling family head button not visible.');
    await checkToaster('error');

    await clickOnElement(cssIdentifiers.is108PickedupBeneficiaryRadioBtn[true], '108 picked up button is not visible.');
    await clickOnElement(cssIdentifiers.isBeneficiaryVisitedPHCRadioBtn[true], 'Beneficiary visited phc button is not visible.');


    await clickOnElement(cssIdentifiers.isBeneficiaryReceivedBloodLastWeekRadioBtn[true], 'injection flag button is not visible.');
    await clickOnElement(cssIdentifiers.isBeneficiaryReceivedFCMLastWeekRadioBtn[false], 'injection flag button is not visible.');
    await clickOnElement(cssIdentifiers.isBeneficiaryReceivedIronSucroseInjectionRadioBtn['yes'], 'injection flag button is not visible.');

    await clickOnElement(cssIdentifiers.injectionCountRadioBtn['3'], 'injection count is not visible.');

    await clickOnElement(cssIdentifiers.isBeneficiaryDropedAtHomeBy108RadioBtn[true], '108dropped Button is not visible');

    await clickOnElement(submitButton, 'submit response is not visible');
    await checkToaster('success');

    // Verifying followup member info after submission 
    const verifyMemberInfoAfterExecArray = await verifyDataInDb(userInfoSeletQuery, testData.anemia002.verifyMemberInfoAfterExec);
    console.log(`verifyMemberInfoAfterExecArray = ${verifyMemberInfoAfterExecArray}`);
    expect(verifyMemberInfoAfterExecArray.length).toEqual(0);

    const verifyResponseAfterExecArray = await verifyDataInDb(responseSelectQuery, testData.anemia002.verifyResponseAfterExec);
    console.log(`verifyResponseAfterExecArray = ${verifyResponseAfterExecArray}`);
    expect(verifyResponseAfterExecArray.length).toEqual(0);

    // Deleting record from user info table and response table
    return executeQuery(deleteQuery);

}

/**
 *  beneficiary has given blood, not given FCM, not given iron sucrose injection
 */
async function verifyBeneficiaryTreatmentGivenBloodNotGivenFCMNotGivenIronSucroseInjection() {

    console.log('High Risk Anemia -> 3 -> High Risk Follow Up – Follow Up call for checking with no FCM/Iron blood 003');


    // Deleting record from user info table and response table
    await executeQuery(deleteQuery);

    const memberStateVerificationQueryResult = await getQueryResult(memberStateVerificationQuery);
    expect(memberStateVerificationQueryResult).toBeDefined();

    // insert record
    await executeQuery(`insert into gvk_high_risk_follow_up_usr_info
        (member_id, gvk_call_state, gvk_call_status,schedule_date,
        call_attempt,diseases,highrisk_reason,location_id, pickup_schedule_date,  
        created_by,created_on,modified_by,modified_on, identify_date)
        values
        (${record_id},'${gvkVerificationConstant.pickedupSchedule}', '${gvkVerificationConstant.callProcessing}', (localtimestamp),
        1, 'Severe Maternal Anemia', 'Hemoglobin count:4', 137402, (localtimestamp - interval '1 day'),
        -1,(localtimestamp - interval '2 hour'),-1, (localtimestamp - interval '2 hour'), '2019-03-01')`);

    await startCallVerification();

    // check the state
    const verifyMemberInfoBeforeExecArray = await verifyDataInDb(userInfoSeletQuery, testData.anemia003.verifyMemberInfoBeforeExec);
    console.log(`verifyMemberInfoBeforeExecArray = ${verifyMemberInfoBeforeExecArray}`);
    expect(verifyMemberInfoBeforeExecArray.length).toEqual(0);


    const beneficiaryFollowupPageTitle = 'High Risk follow up - Follow up call for checking';
    let pageTitle = element(by.binding('gvkreverification.pageTitle'));
    expect(pageTitle.getText()).toBe(beneficiaryFollowupPageTitle);

    await clickOnElement(callFamilyHead, 'Calling family head button not visible.');
    await checkToaster('error');

    await clickOnElement(cssIdentifiers.is108PickedupBeneficiaryRadioBtn[true], '108 picked up button is not visible.');
    await clickOnElement(cssIdentifiers.isBeneficiaryVisitedPHCRadioBtn[true], 'Beneficiary visited phc button is not visible.');


    await clickOnElement(cssIdentifiers.isBeneficiaryReceivedBloodLastWeekRadioBtn[true], 'injection flag button is not visible.');
    await clickOnElement(cssIdentifiers.isBeneficiaryReceivedFCMLastWeekRadioBtn[false], 'injection flag button is not visible.');
    await clickOnElement(cssIdentifiers.isBeneficiaryReceivedIronSucroseInjectionRadioBtn['no'], 'injection flag button is not visible.');

    await clickOnElement(cssIdentifiers.isBeneficiaryDropedAtHomeBy108RadioBtn[true], '108dropped Button is not visible');

    await clickOnElement(submitButton, 'submit response is not visible');
    await checkToaster('success');

    // Verifying followup member info after submission 
    const verifyMemberInfoAfterExecArray = await verifyDataInDb(userInfoSeletQuery, testData.anemia003.verifyMemberInfoAfterExec);
    console.log(`verifyMemberInfoAfterExecArray = ${verifyMemberInfoAfterExecArray}`);
    expect(verifyMemberInfoAfterExecArray.length).toEqual(0);

    const verifyResponseAfterExecArray = await verifyDataInDb(responseSelectQuery, testData.anemia003.verifyResponseAfterExec);
    console.log(`verifyResponseAfterExecArray = ${verifyResponseAfterExecArray}`);
    expect(verifyResponseAfterExecArray.length).toEqual(0);

    // Deleting record from user info table and response table
    return executeQuery(deleteQuery);
}

/**
 *  beneficiary has not given blood, not given FCM, not given iron sucrose injection, picked schedule for next visit
 */
async function verifyBeneficiaryTreatmentNotGivenBloodNotGivenFCMNotGivenIronSucroseInjectionPickedUpScheduleForNextVisit() {
    console.log('High Risk Anemia -> 4 -> High Risk Follow Up – Follow Up call for checking with no Blood, FCM/Iron blood 004');


    // Deleting record from user info table and response table
    await executeQuery(deleteQuery);

    const memberStateVerificationQueryResult = await getQueryResult(memberStateVerificationQuery);
    expect(memberStateVerificationQueryResult).toBeDefined();

    // insert record
    await executeQuery(`insert into gvk_high_risk_follow_up_usr_info
        (member_id, gvk_call_state, gvk_call_status,schedule_date,
        call_attempt,diseases,highrisk_reason,location_id, pickup_schedule_date,
        created_by,created_on,modified_by,modified_on, identify_date)
        values
        (${record_id},'${gvkVerificationConstant.pickedupSchedule}', '${gvkVerificationConstant.callProcessing}', (localtimestamp),
        1, 'Severe Maternal Anemia', 'Hemoglobin count:4', 137402, (localtimestamp - interval '1 day'),
        -1,(localtimestamp - interval '2 hour'),-1, (localtimestamp - interval '2 hour'), '2019-03-01')`);

    await startCallVerification();

    // check the state
    const verifyMemberInfoBeforeExecArray = await verifyDataInDb(userInfoSeletQuery, testData.anemia004.verifyMemberInfoBeforeExec);
    console.log(`verifyMemberInfoBeforeExecArray = ${verifyMemberInfoBeforeExecArray}`);
    expect(verifyMemberInfoBeforeExecArray.length).toEqual(0);

    const beneficiaryFollowupPageTitle = 'High Risk follow up - Follow up call for checking';
    let pageTitle = element(by.binding('gvkreverification.pageTitle'));
    expect(pageTitle.getText()).toBe(beneficiaryFollowupPageTitle);

    await clickOnElement(callFamilyHead, 'Calling family head button not visible.');
    await checkToaster('error');

    await clickOnElement(cssIdentifiers.is108PickedupBeneficiaryRadioBtn[true], '108 picked up button is not visible.');
    await clickOnElement(cssIdentifiers.isBeneficiaryVisitedPHCRadioBtn[true], 'Beneficiary visited phc button is not visible.');


    await clickOnElement(cssIdentifiers.isBeneficiaryReceivedBloodLastWeekRadioBtn[false], 'injection flag button is not visible.');
    await clickOnElement(cssIdentifiers.isBeneficiaryReceivedFCMLastWeekRadioBtn[false], 'injection flag button is not visible.');
    await clickOnElement(cssIdentifiers.isBeneficiaryReceivedIronSucroseInjectionRadioBtn['no'], 'injection flag button is not visible.');

    await clickOnElement(cssIdentifiers.isBeneficiaryDropedAtHomeBy108RadioBtn[false], '108dropped Button is not visible');
    await clickOnElement(cssIdentifiers.isBeneficiaryWantReschedulePickupDateRadioBtn[true], 'beneficiary pickedup date for next schedule is not visible');

    let selectedDate = new Date(new Date().setDate(new Date().getDate() + 12)).getDate();

    // await setPickupScheduleDate(selectedDate, 10, 10);
    await dateTimeSelector({ hours: 10, minutes: 10, date: selectedDate });
    await clickOnElement(submitButton, 'submit response is not visible');
    await checkToaster('success');

    // Verifying followup member info after submission 
    const verifyMemberInfoAfterExecArray = await verifyDataInDb(userInfoSeletQuery, testData.anemia004.verifyMemberInfoAfterExec);
    console.log(`verifyMemberInfoAfterExecArray = ${verifyMemberInfoAfterExecArray}`);
    expect(verifyMemberInfoAfterExecArray.length).toEqual(0);

    const verifyResponseAfterExecArray = await verifyDataInDb(responseSelectQuery, testData.anemia004.verifyResponseAfterExec);
    console.log(`verifyResponseAfterExecArray = ${verifyResponseAfterExecArray}`);
    expect(verifyResponseAfterExecArray.length).toEqual(0);


    // Deleting record from user info table and response table
    return executeQuery(deleteQuery);


}

/**
 *  beneficiary has not given blood, not given FCM, not given iron sucrose injection, picked schedule pending next visit
 */
async function verifyBeneficiaryTreatmentNotGivenBloodNotGivenFCMNotGivenIronSucroseInjectionPickedUpScheduleForNextVisitPending() {

    console.log('High Risk Anemia -> 5 -> High Risk Follow Up – Follow Up call for checking for Anemia without schedule date 005');


    // delete information from the table.   
    await executeQuery(`delete from gvk_high_risk_follow_up_usr_info where member_id = ${record_id};
    delete from gvk_high_risk_follow_up_responce where member_id = ${record_id}`);

    const memberStateVerificationQueryResult = await getQueryResult(memberStateVerificationQuery);
    expect(memberStateVerificationQueryResult).toBeDefined();

    // insert record
    await executeQuery(`insert into gvk_high_risk_follow_up_usr_info
    (member_id, gvk_call_state, gvk_call_status,schedule_date,
    call_attempt,diseases,highrisk_reason,location_id, pickup_schedule_date,
    created_by,created_on,modified_by,modified_on, identify_date)
    values
    (${record_id},'${gvkVerificationConstant.pickedupSchedule}', '${gvkVerificationConstant.callProcessing}', (localtimestamp),
    1, 'Severe Maternal Anemia', 'Hemoglobin count:4', 137402, (localtimestamp - interval '1 day'),
    -1,(localtimestamp - interval '2 hour'),-1, (localtimestamp - interval '2 hour'), '2019-03-01')`);


    await startCallVerification();

    // check the state
    const verifyMemberInfoBeforeExecArray = await verifyDataInDb(userInfoSeletQuery, testData.anemia005.verifyMemberInfoBeforeExec);
    console.log(`verifyMemberInfoBeforeExecArray = ${verifyMemberInfoBeforeExecArray}`);
    expect(verifyMemberInfoBeforeExecArray.length).toEqual(0);

    const beneficiaryFollowupPageTitle = 'High Risk follow up - Follow up call for checking';
    let pageTitle = element(by.binding('gvkreverification.pageTitle'));
    expect(pageTitle.getText()).toBe(beneficiaryFollowupPageTitle);

    await clickOnElement(callFamilyHead, 'Calling family head button not visible.');
    await checkToaster('error');

    await clickOnElement(cssIdentifiers.is108PickedupBeneficiaryRadioBtn[true], '108 picked up button is not visible.');
    await clickOnElement(cssIdentifiers.isBeneficiaryVisitedPHCRadioBtn[true], 'Beneficiary visited phc button is not visible.');


    await clickOnElement(cssIdentifiers.isBeneficiaryReceivedBloodLastWeekRadioBtn[false], 'injection flag button is not visible.');
    await clickOnElement(cssIdentifiers.isBeneficiaryReceivedFCMLastWeekRadioBtn[false], 'injection flag button is not visible.');
    await clickOnElement(cssIdentifiers.isBeneficiaryReceivedIronSucroseInjectionRadioBtn['no'], 'injection flag button is not visible.');

    await clickOnElement(cssIdentifiers.isBeneficiaryDropedAtHomeBy108RadioBtn[false], '108dropped Button is not visible');
    await clickOnElement(cssIdentifiers.isBeneficiaryWantReschedulePickupDateRadioBtn[false], 'beneficiary pickedup date for next schedule is not visible');

    await clickOnElement(submitButton, 'submit response is not visible');
    await checkToaster('success');

    // Verifying followup member info after submission 
    const verifyMemberInfoAfterExecArray = await verifyDataInDb(userInfoSeletQuery, testData.anemia005.verifyMemberInfoAfterExec);
    console.log(`verifyMemberInfoAfterExecArray = ${verifyMemberInfoAfterExecArray}`);
    expect(verifyMemberInfoAfterExecArray.length).toEqual(0);

    const verifyResponseAfterExecArray = await verifyDataInDb(responseSelectQuery, testData.anemia005.verifyResponseAfterExec);
    console.log(`verifyResponseAfterExecArray = ${verifyResponseAfterExecArray}`);
    expect(verifyResponseAfterExecArray.length).toEqual(0);

    console.log('High Risk Anemia -> 6 -> High Risk follow up - Follow up call for checking- schedule pending 006');

    await executeQuery(`update gvk_high_risk_follow_up_usr_info set schedule_date = (localtimestamp - interval '1 day'), modified_on = (localtimestamp - interval '2 hour') 
        where member_id  =  ${record_id}`);


    await startCallVerification();

    // check the state
    const afterUpdateVerifyMemberInfoBeforeExecArray = await verifyDataInDb(userInfoSeletQuery, testData.anemia006.verifyMemberInfoBeforeExec);
    console.log(`afterUpdateVerifyMemberInfoBeforeExecArray = ${afterUpdateVerifyMemberInfoBeforeExecArray}`);
    expect(afterUpdateVerifyMemberInfoBeforeExecArray.length).toEqual(0);

    const beneficiaryFollowupPageTitleSecondScreen = 'High Risk follow up - Follow up call for checking- schedule pending';
    pageTitle = element(by.binding('gvkreverification.pageTitle'));
    expect(pageTitle.getText()).toBe(beneficiaryFollowupPageTitleSecondScreen);

    await clickOnElement(callFamilyHead, 'Calling family head button not visible.');
    await checkToaster('error');

    let selectedPickedScheduleDate = new Date(new Date().setDate(new Date().getDate() + 4)).getDate();

    await dateTimeSelector({ hours: 15, minutes: 18, date: selectedPickedScheduleDate });
    await clickOnElement(submitButton, 'submit response is not visible');
    await checkToaster('success');

    // Verifying followup member info after submission 
    const afterUpdateVerifyMemberInfoAfterExecArray = await verifyDataInDb(userInfoSeletQuery, testData.anemia006.verifyMemberInfoAfterExec);
    console.log(`afterUpdateVerifyMemberInfoAfterExecArray = ${afterUpdateVerifyMemberInfoAfterExecArray}`);
    expect(afterUpdateVerifyMemberInfoAfterExecArray.length).toEqual(0);

    const afterUpdateVerifyResponseAfterExecArray = await verifyDataInDb(responseSelectQuery, testData.anemia006.verifyResponseAfterExec);
    console.log(`afterUpdateVerifyResponseAfterExecArray = ${afterUpdateVerifyResponseAfterExecArray}`);
    expect(afterUpdateVerifyResponseAfterExecArray.length).toEqual(0);


    // Deleting record from user info table and response table
    return executeQuery(deleteQuery);

}
/**
 *  beneficiary has not given blood, not given FCM, two iron sucrose injection  given, picked schedule for next visit
 */
async function verifyBeneficiaryTreatmentNotGivenBloodNotGivenFCMTwoIronSucroseInjectionPickedUpScheduleForNextVisit() {
    console.log('High Risk Anemia -> 7 -> High Risk Follow Up – Follow Up call for checking with no Blood, FCM/Iron blood 007');


    // delete information from the table.
    await executeQuery(`delete from gvk_high_risk_follow_up_usr_info where member_id = ${record_id};
    delete from gvk_high_risk_follow_up_responce where member_id = ${record_id}`);

    const memberStateVerificationQueryResult = await getQueryResult(memberStateVerificationQuery);
    expect(memberStateVerificationQueryResult).toBeDefined();

    // insert record
    await executeQuery(`insert into gvk_high_risk_follow_up_usr_info
        (member_id, gvk_call_state, gvk_call_status,schedule_date,
        call_attempt,diseases,highrisk_reason,location_id, pickup_schedule_date,
        created_by,created_on,modified_by,modified_on, identify_date)
        values
        (${record_id},'${gvkVerificationConstant.pickedupSchedule}', '${gvkVerificationConstant.callProcessing}', (localtimestamp),
        1, 'Severe Maternal Anemia', 'Hemoglobin count:4', 137402, (localtimestamp - interval '1 day'),
        -1,(localtimestamp - interval '2 hour'),-1, (localtimestamp - interval '2 hour'), '2019-03-01')`);

    await startCallVerification();

    // check the state
    const verifyMemberInfoBeforeExecArray = await verifyDataInDb(userInfoSeletQuery, testData.anemia007.verifyMemberInfoBeforeExec);
    console.log(`verifyMemberInfoBeforeExecArray = ${verifyMemberInfoBeforeExecArray}`);
    expect(verifyMemberInfoBeforeExecArray.length).toEqual(0);

    const beneficiaryFollowupPageTitle = 'High Risk follow up - Follow up call for checking';
    let pageTitle = element(by.binding('gvkreverification.pageTitle'));
    expect(pageTitle.getText()).toBe(beneficiaryFollowupPageTitle);

    await clickOnElement(callFamilyHead, 'Calling family head button not visible.');
    await checkToaster('error');

    await clickOnElement(cssIdentifiers.is108PickedupBeneficiaryRadioBtn[true], '108 picked up button is not visible.');
    await clickOnElement(cssIdentifiers.isBeneficiaryVisitedPHCRadioBtn[true], 'Beneficiary visited phc button is not visible.');


    await clickOnElement(cssIdentifiers.isBeneficiaryReceivedBloodLastWeekRadioBtn[false], 'injection flag button is not visible.');
    await clickOnElement(cssIdentifiers.isBeneficiaryReceivedFCMLastWeekRadioBtn[false], 'injection flag button is not visible.');
    await clickOnElement(cssIdentifiers.isBeneficiaryReceivedIronSucroseInjectionRadioBtn['yes'], 'injection flag button is not visible.');

    await clickOnElement(cssIdentifiers.injectionCountRadioBtn['2'], 'injection count is not visible.');

    await clickOnElement(cssIdentifiers.isBeneficiaryDropedAtHomeBy108RadioBtn[false], '108dropped Button is not visible');
    await clickOnElement(cssIdentifiers.isBeneficiaryWantReschedulePickupDateRadioBtn[true], 'beneficiary pickedup date for next schedule is not visible');

    let selectedDate = new Date(new Date().setDate(new Date().getDate() + 14)).getDate();


    // await setPickupScheduleDate(selectedDate, 10, 10);
    await dateTimeSelector({ hours: 0, minutes: 0, date: selectedDate });
    await clickOnElement(submitButton, 'submit response is not visible');
    await checkToaster('success');

    // Verifying followup member info after submission 
    const verifyMemberInfoAfterExecArray = await verifyDataInDb(userInfoSeletQuery, testData.anemia007.verifyMemberInfoAfterExec);
    console.log(`verifyMemberInfoAfterExecArray = ${verifyMemberInfoAfterExecArray}`);
    expect(verifyMemberInfoAfterExecArray.length).toEqual(0);

    const verifyResponseAfterExecArray = await verifyDataInDb(responseSelectQuery, testData.anemia007.verifyResponseAfterExec);
    console.log(`verifyResponseAfterExecArray = ${verifyResponseAfterExecArray}`);
    expect(verifyResponseAfterExecArray.length).toEqual(0);


    await executeQuery(`update gvk_high_risk_follow_up_usr_info set schedule_date = (localtimestamp - interval '1 day'), modified_on = (localtimestamp - interval '2 hour') 
        where member_id  =  ${record_id}`);


    await startCallVerification();

    // check the state
    const afterUpdateVerifyMemberInfoBeforeExecArray = await verifyDataInDb(userInfoSeletQuery, testData.anemia007.afterUpdateVerifyMemberInfoBeforeExec);
    console.log(`afterUpdateVerifyMemberInfoBeforeExecArray = ${afterUpdateVerifyMemberInfoBeforeExecArray}`);
    expect(afterUpdateVerifyMemberInfoBeforeExecArray.length).toEqual(0);

    pageTitle = element(by.binding('gvkreverification.pageTitle'));
    expect(pageTitle.getText()).toBe(beneficiaryFollowupPageTitle);

    await clickOnElement(callFamilyHead, 'Calling family head button not visible.');
    await checkToaster('error');

    await clickOnElement(cssIdentifiers.is108PickedupBeneficiaryRadioBtn[true], '108 picked up button is not visible.');
    await clickOnElement(cssIdentifiers.isBeneficiaryVisitedPHCRadioBtn[true], 'Beneficiary visited phc button is not visible.');


    await clickOnElement(cssIdentifiers.isBeneficiaryReceivedBloodLastWeekRadioBtn[false], 'injection flag button is not visible.');
    await clickOnElement(cssIdentifiers.isBeneficiaryReceivedFCMLastWeekRadioBtn[false], 'injection flag button is not visible.');
    await clickOnElement(cssIdentifiers.isBeneficiaryReceivedIronSucroseInjectionRadioBtn['yes'], 'injection flag button is not visible.');

    await clickOnElement(cssIdentifiers.injectionCountRadioBtn['3'], 'injection count is not visible.');

    await clickOnElement(cssIdentifiers.isBeneficiaryDropedAtHomeBy108RadioBtn[false], '108dropped Button is not visible');

    await clickOnElement(submitButton, 'submit response is not visible');
    await checkToaster('success');

    // Verifying followup member info after submission 
    const afterUpdateVerifyMemberInfoAfterExecArray = await verifyDataInDb(userInfoSeletQuery, testData.anemia007.afterUpdateVerifyMemberInfoAfterExec);
    console.log(`afterUpdateVerifyMemberInfoAfterExecArray = ${afterUpdateVerifyMemberInfoAfterExecArray}`);
    expect(afterUpdateVerifyMemberInfoAfterExecArray.length).toEqual(0);

    const afterUpdateVerifyResponseAfterExecArray = await verifyDataInDb(responseSelectQuery, testData.anemia007.afterUpdateVerifyResponseAfterExec);
    console.log(`afterUpdateVerifyResponseAfterExecArray = ${afterUpdateVerifyResponseAfterExecArray}`);
    expect(afterUpdateVerifyResponseAfterExecArray.length).toEqual(0);

    // Deleting record from user info table and response table
    return executeQuery(deleteQuery);

}


