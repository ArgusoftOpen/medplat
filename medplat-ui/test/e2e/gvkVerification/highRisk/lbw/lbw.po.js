const EC = protractor.ExpectedConditions;

const {
    gvkVerificationConstant,
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
} = require('../../../common/utils');
const cssIdentifiers = require('../common/common.es');
const testData = require('./lbw.td');

let record_id = 110078747;
const userInfoSeletQuery = `select * from gvk_high_risk_follow_up_usr_info where member_id = ${record_id} order by id desc limit 1`;
const responseSelectQuery = `select * from gvk_high_risk_follow_up_responce where member_id = ${record_id} order by id desc limit 1`;
const deleteQuery = `delete from gvk_high_risk_follow_up_usr_info where member_id = ${record_id};
                        delete from gvk_high_risk_follow_up_responce where member_id = ${record_id};`;
const beneficiaryFollowupPageTitle = 'High Risk follow up - Follow up call for checking';
const memberStateVerificationQuery = `select * from imt_member where (basic_state ='VERIFIED' or basic_state='NEW') and id = ${record_id}`

module.exports = {
    verifyBeneficiaryTreatmentAdmittedInHospital,
    verifyBeneficiaryTreatmentNotAdmittedInHospitalPickedupSchedule,
    verifyBeneficiaryTreatmentNotAdmittedInHospitalPickedupSchedulePending,
}

/**
 *  beneficiary admitted in the hospital
 */
async function verifyBeneficiaryTreatmentAdmittedInHospital() {
    console.log('High Risk Follow Up – Follow Up call for checking LBW child hospitalized 001')

    const memberStateVerificationQueryResult = await getQueryResult(memberStateVerificationQuery);
    expect(memberStateVerificationQueryResult).toBeDefined();

    // Deleting record from user info table and response table
    await executeQuery(deleteQuery);

    // insert record
    await executeQuery(`insert into gvk_high_risk_follow_up_usr_info
    (member_id, gvk_call_state, gvk_call_previous_state, gvk_call_status,schedule_date,
    call_attempt,diseases,highrisk_reason,location_id, pickup_schedule_date,  
    created_by,created_on,modified_by,modified_on, identify_date)
    values
    (${record_id},'${gvkVerificationConstant.pickedupSchedule}', '${gvkVerificationConstant.highRiskConditionConfirmationWithYes}', '${gvkVerificationConstant.callProcessing}', (localtimestamp),
    1, 'Very Low Birth Weight', 'Birth weight: 1.70000005 kg', 137402, (localtimestamp - interval '1 day'),
    -1,(localtimestamp - interval '2 hour'),-1, (localtimestamp - interval '2 hour'), '2019-03-01')`);

    await startCallVerification();

    // check the state
    const verifyMemberInfoBeforeExecArray = await verifyDataInDb(userInfoSeletQuery, testData.lbw001.verifyMemberInfoBeforeExec);
    console.log(`verifyMemberInfoBeforeExecArray = ${verifyMemberInfoBeforeExecArray}`);
    expect(verifyMemberInfoBeforeExecArray.length).toEqual(0);

    let pageTitle = element(by.binding('gvkreverification.pageTitle'));
    expect(pageTitle.getText()).toBe(beneficiaryFollowupPageTitle);

    await clickOnElement(callFamilyHead, 'Calling family head button not visible.');
    await checkToaster('error');

    await clickOnElement(cssIdentifiers.is108PickedupBeneficiaryRadioBtn[true], '108 picked up button is not visible.');
    await clickOnElement(cssIdentifiers.isBeneficiaryVisitedPHCRadioBtn[true], 'Beneficiary visited phc button is not visible.');

    // treatment question
    await clickOnElement(cssIdentifiers.isBeneficiaryChildAdmittedForLowBirthWeightRadioBtn[true], 'child admitted is hospital button is not visible.');

    await clickOnElement(cssIdentifiers.isBeneficiaryDropedAtHomeBy108RadioBtn[true], '108dropped Button is not visible');
    await clickOnElement(submitButton, 'submit response is not visible')
    await checkToaster('success');

    // Verifying followup member info after submission 
    const verifyMemberInfoAfterExecArray = await verifyDataInDb(userInfoSeletQuery, testData.lbw001.verifyMemberInfoAfterExec);
    console.log(`verifyMemberInfoAfterExecArray = ${verifyMemberInfoAfterExecArray}`);
    expect(verifyMemberInfoAfterExecArray.length).toEqual(0);

    const verifyResponseAfterExecArray = await verifyDataInDb(responseSelectQuery, testData.lbw001.verifyResponseAfterExec);
    console.log(`verifyResponseAfterExecArray = ${verifyResponseAfterExecArray}`);
    expect(verifyResponseAfterExecArray.length).toEqual(0);


    // delete information from the table.
    return executeQuery(deleteQuery);


}

/**
 *  beneficiary not admitted to hospital pickedup schedule for next visit
 */
async function verifyBeneficiaryTreatmentNotAdmittedInHospitalPickedupSchedule() {
    console.log('High Risk Follow Up – Follow Up call for checking LBW child not hospitalized but willing to hospitalized 002')

    const memberStateVerificationQueryResult = await getQueryResult(memberStateVerificationQuery);
    expect(memberStateVerificationQueryResult).toBeDefined();

    // Deleting record from user info table and response table
    await executeQuery(deleteQuery);

    // insert record
    await executeQuery(`insert into gvk_high_risk_follow_up_usr_info
        (member_id, gvk_call_state, gvk_call_previous_state, gvk_call_status,schedule_date,
        call_attempt,diseases,highrisk_reason,location_id, pickup_schedule_date,  
        created_by,created_on,modified_by,modified_on, identify_date)
        values
        (${record_id},'${gvkVerificationConstant.pickedupSchedule}', '${gvkVerificationConstant.highRiskConditionConfirmationWithYes}', '${gvkVerificationConstant.callProcessing}', (localtimestamp),
        1, 'Very Low Birth Weight', 'Birth weight: 1.70000005 kg', 137402, (localtimestamp - interval '1 day'),
        -1,(localtimestamp - interval '2 hour'),-1, (localtimestamp - interval '2 hour'), '2019-03-01')`);

    await startCallVerification();

    // check the state
    const verifyMemberInfoBeforeExecArray = await verifyDataInDb(userInfoSeletQuery, testData.lbw002.verifyMemberInfoBeforeExec);
    console.log(`verifyMemberInfoBeforeExecArray = ${verifyMemberInfoBeforeExecArray}`);
    expect(verifyMemberInfoBeforeExecArray.length).toEqual(0);

    let pageTitle = element(by.binding('gvkreverification.pageTitle'));
    expect(pageTitle.getText()).toBe(beneficiaryFollowupPageTitle);

    await clickOnElement(callFamilyHead, 'Calling family head button not visible.');
    await checkToaster('error');

    await clickOnElement(cssIdentifiers.is108PickedupBeneficiaryRadioBtn[true], '108 picked up button is not visible.');
    await clickOnElement(cssIdentifiers.isBeneficiaryVisitedPHCRadioBtn[true], 'Beneficiary visited phc button is not visible.');

    // treatment question
    await clickOnElement(cssIdentifiers.isBeneficiaryChildAdmittedForLowBirthWeightRadioBtn[false], 'child admitted is hospital button is not visible.');
    await clickOnElement(cssIdentifiers.isBeneficiaryWantReschedulePickupDateRadioBtn[true], 'Beneficiary pickedup date for next schedule is not visible');

    let selectedDate = new Date(new Date().setDate(new Date().getDate() + 16)).getDate();


    await dateTimeSelector({ hours: 10, minutes: 10, date: selectedDate });
    await clickOnElement(submitButton, 'submit response is not visible');
    await checkToaster('success');

    // Verifying followup member info after submission 
    const verifyMemberInfoAfterExecArray = await verifyDataInDb(userInfoSeletQuery, testData.lbw002.verifyMemberInfoAfterExec);
    console.log(`verifyMemberInfoAfterExecArray = ${verifyMemberInfoAfterExecArray}`);
    expect(verifyMemberInfoAfterExecArray.length).toEqual(0);

    const verifyResponseAfterExecArray = await verifyDataInDb(responseSelectQuery, testData.lbw002.verifyResponseAfterExec);
    console.log(`verifyResponseAfterExecArray = ${verifyResponseAfterExecArray}`);
    expect(verifyResponseAfterExecArray.length).toEqual(0);

    // delete information from the table.
    return executeQuery(deleteQuery);

}


/**
 *  beneficiary not admitted to hospital pickedup schedule pending
 */
async function verifyBeneficiaryTreatmentNotAdmittedInHospitalPickedupSchedulePending() {
    console.log('High Risk Follow Up – Follow Up call for checking LBW child not hospitalized and not willing to hospitalized 003')

    const memberStateVerificationQueryResult = await getQueryResult(memberStateVerificationQuery);
    expect(memberStateVerificationQueryResult).toBeDefined();

    // Deleting record from user info table and response table
    await executeQuery(deleteQuery);

    // insert record
    await executeQuery(`insert into gvk_high_risk_follow_up_usr_info
        (member_id, gvk_call_state, gvk_call_previous_state, gvk_call_status,schedule_date,
        call_attempt,diseases,highrisk_reason,location_id, pickup_schedule_date,  
        created_by,created_on,modified_by,modified_on, identify_date)
        values
        (${record_id},'${gvkVerificationConstant.pickedupSchedule}', '${gvkVerificationConstant.highRiskConditionConfirmationWithYes}', '${gvkVerificationConstant.callProcessing}', (localtimestamp),
        1, 'Very Low Birth Weight', 'Birth weight: 1.70000005 kg', 137402, (localtimestamp - interval '1 day'),
        -1,(localtimestamp - interval '2 hour'),-1, (localtimestamp - interval '2 hour'), '2019-03-01')`);

    await startCallVerification();

    // check the state
    const verifyMemberInfoBeforeExecArray = await verifyDataInDb(userInfoSeletQuery, testData.lbw003.verifyMemberInfoBeforeExec);
    console.log(`verifyMemberInfoBeforeExecArray = ${verifyMemberInfoBeforeExecArray}`);
    expect(verifyMemberInfoBeforeExecArray.length).toEqual(0);

    let pageTitle = element(by.binding('gvkreverification.pageTitle'));
    expect(pageTitle.getText()).toBe(beneficiaryFollowupPageTitle);

    await clickOnElement(callFamilyHead, 'Calling family head button not visible.');
    await checkToaster('error');

    await clickOnElement(cssIdentifiers.is108PickedupBeneficiaryRadioBtn[true], '108 picked up button is not visible.');
    await clickOnElement(cssIdentifiers.isBeneficiaryVisitedPHCRadioBtn[true], 'Beneficiary visited phc button is not visible.');

    // treatment question
    await clickOnElement(cssIdentifiers.isBeneficiaryChildAdmittedForLowBirthWeightRadioBtn[false], 'child admitted is hospital button is not visible.');

    await clickOnElement(cssIdentifiers.isBeneficiaryWantReschedulePickupDateRadioBtn[false], 'Beneficiary pickedup date for next schedule is not visible');



    await clickOnElement(submitButton, 'submit response is not visible');
    await checkToaster('success');

    // Verifying followup member info after submission 
    const verifyMemberInfoAfterExecArray = await verifyDataInDb(userInfoSeletQuery, testData.lbw003.verifyMemberInfoAfterExec);
    console.log(`verifyMemberInfoAfterExecArray = ${verifyMemberInfoAfterExecArray}`);
    expect(verifyMemberInfoAfterExecArray.length).toEqual(0);

    const verifyResponseAfterExecArray = await verifyDataInDb(responseSelectQuery, testData.lbw003.verifyResponseAfterExec);
    console.log(`verifyResponseAfterExecArray = ${verifyResponseAfterExecArray}`);
    expect(verifyResponseAfterExecArray.length).toEqual(0);


    // update query to pop up desire screen
    await executeQuery(`update gvk_high_risk_follow_up_usr_info set modified_on = (localtimestamp - interval'2 hour'), schedule_date = localtimestamp
    where member_id = ${record_id}`);

    // Going on call verification page and start calling
    await startCallVerification();

    const beneficiaryFollowupSchedulePendingPageTitle = 'High Risk follow up - Follow up call for checking- schedule pending';
    let SchedulePendinPageTitle = element(by.binding('gvkreverification.pageTitle'));
    expect(SchedulePendinPageTitle.getText()).toBe(beneficiaryFollowupSchedulePendingPageTitle);


    await clickOnElement(callFamilyHead, 'Calling FHW button not visible.');
    await checkToaster('error');

    let selectedPickedScheduleDate = new Date(new Date().setDate(new Date().getDate() + 3)).getDate();

    await dateTimeSelector({ hours: 10, minutes: 40, date: selectedPickedScheduleDate });
    await clickOnElement(submitButton, 'submit response is not visible');
    await checkToaster('success');

    // Verifying followup member info after submission 
    const afterUpdateVerifyMemberInfoAfterExecArray = await verifyDataInDb(userInfoSeletQuery, testData.lbw003.afterUpdateVerifyMemberInfoAfterExec);
    console.log(`afterUpdateVerifyMemberInfoAfterExecArray = ${afterUpdateVerifyMemberInfoAfterExecArray}`);
    expect(afterUpdateVerifyMemberInfoAfterExecArray.length).toEqual(0);

    // delete information from the table.
    return executeQuery(deleteQuery);


}
