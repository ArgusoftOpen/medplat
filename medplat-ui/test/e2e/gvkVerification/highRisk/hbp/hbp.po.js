const EC = protractor.ExpectedConditions;


const {
    gvkVerificationConstant,
} = require('../../../common/gvk_constant')

const {
    submitButton,
    fhwCallButton,
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
const testData = require('./hbp.td');

let record_id = 60611775;
const userInfoSeletQuery = `select * from gvk_high_risk_follow_up_usr_info where member_id = ${record_id} order by id desc limit 1`;
const responseSelectQuery = `select * from gvk_high_risk_follow_up_responce where member_id = ${record_id} order by id desc limit 1`;
const deleteQuery = `delete from gvk_high_risk_follow_up_usr_info where member_id = ${record_id};
                        delete from gvk_high_risk_follow_up_responce where member_id = ${record_id};`;
const memberStateVerificationQuery = `select * from imt_member where (basic_state ='VERIFIED' or basic_state='NEW') and id = ${record_id}`

module.exports = {
    highBloodPressureFunction,
}

/**
 *  beneficiary high blood pressure
 */
async function highBloodPressureFunction() {

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
        1, 'High BP', 'diastolic_bp:140, systolic_bp:90', 137402, (localtimestamp - interval '1 day'),
        -1,(localtimestamp - interval '2 hour'),-1, (localtimestamp - interval '2 hour'), '2019-03-01')`);

    // Going on call verification page and start calling
    await startCallVerification();

    // check the state
    const verifyMemberInfoBeforeExecArray = await verifyDataInDb(userInfoSeletQuery, testData.hbp001.verifyMemberInfoBeforeExec);
    console.log(`verifyMemberInfoBeforeExecArray = ${verifyMemberInfoBeforeExecArray}`);
    expect(verifyMemberInfoBeforeExecArray.length).toEqual(0);

    const beneficiaryFollowupPageTitle = 'High Risk follow up - Follow up call for checking';
    let pageTitle = element(by.binding('gvkreverification.pageTitle'));
    expect(pageTitle.getText()).toBe(beneficiaryFollowupPageTitle);


    await clickOnElement(fhwCallButton, 'Calling family head button not visible.');
    await checkToaster('error');

    await clickOnElement(cssIdentifiers.is108PickedupBeneficiaryRadioBtn[true], '108 picked up button is not visible.');
    await clickOnElement(cssIdentifiers.isBeneficiaryVisitedPHCRadioBtn[true], 'Beneficiary visited phc button is not visible.');

    await clickOnElement(cssIdentifiers.isBeneficiaryReceivedDrugsForHighBPRadioBtn[true], 'beneficiary received medicine button is not visible');

    await clickOnElement(cssIdentifiers.isBeneficiaryDropedAtHomeBy108RadioBtn[true], '108dropped Button is not visible');
    await clickOnElement(cssIdentifiers.isBeneficiaryWantReschedulePickupDateRadioBtn[true], 'beneficiary pickedup date for next schedule is not visible');

    let selectedDate = new Date(new Date().setDate(new Date().getDate() + 17)).getDate();

    await dateTimeSelector({ hours: 10, minutes: 10, date: selectedDate });
    await clickOnElement(submitButton, 'submit response is not visible');
    await checkToaster('success');

    // Verifying followup member info after submission 
    const afterUpdateVerifyMemberInfoAfterExecArray = await verifyDataInDb(userInfoSeletQuery, testData.hbp001.verifyMemberInfoAfterExec);
    console.log(`afterUpdateVerifyMemberInfoAfterExecArray = ${afterUpdateVerifyMemberInfoAfterExecArray}`);
    expect(afterUpdateVerifyMemberInfoAfterExecArray.length).toEqual(0);

    const afterUpdateVerifyResponseAfterExecArray = await verifyDataInDb(responseSelectQuery, testData.hbp001.verifyResponseAfterExec);
    console.log(`afterUpdateVerifyResponseAfterExecArray = ${afterUpdateVerifyResponseAfterExecArray}`);
    expect(afterUpdateVerifyResponseAfterExecArray.length).toEqual(0);

    // Deleting record from user info table and response table
    return executeQuery(deleteQuery);


}
