const EC = protractor.ExpectedConditions;

const {
    gvkVerificationConstant,
    gvkFeatureConstant,
} = require('../../../common/gvk_constant')

const {
    callStatusDropDown,
    haventDecidedCheckbox,
    noRecordsToVerify,
    submitButton,
    submitSchedule,
    fhwCallButton,
    callContactPerson,
    callFamilyHead
} = require('../../../common/cssIdentifiers');

const {
    checkToaster,
    clickOnElement,
    dateTimeSelector,
    getQueryResult,
    dropdownListSelector,
    executeQuery,
    startCallVerification,
    verifyDataInDb,
    selectDeselectFeatureAccess,
    waitForLoading
} = require('../../../common/utils');
const cssIdentifiers = require('../common/common.es');
const testData = require('./sam.td');

const {
    recordId,
    deleteQuery,
    memberStateVerificationQuery,
    modifiedOnHourDiffQuery,
    modifiedScheduledDayDiffQuery,
    modifiedScheduledHourDiffQuery,
    responseSelectQuery,
    userInfoSelectQuery
} = testData;

module.exports = {
    executeBeneficiaryVerificationPositiveSteps,
    executeFollowupVerificationStepsPositive,
    executeVerificationStepsForNotReachableCall,
    executeVerificationStepsForNoVisit,
    fillBeneficiaryVerificationForm,
    verifyNoVisitTwice,
    verifyVisitOnSecondCall,
    verifyVisitWithScheduleNotDecided,
    submitFhwVerificationForm,
    verifyBeneficiaryTreatmentNo108ServicePickedUpSchedule,
    verifyBeneficiaryTreatmentNo108ServicePickUpSchedulePending,
    verifyBeneficiaryTreatmentNo108ServiceNotWillngToHelped,
    verifyBeneficiaryTreatmentNoHospitalVisitPickedUpSchedule,
    verifyBeneficiaryTreatmentNoHospitalVisitNotWillngToHelped,
    verifyBeneficiaryTreatmentNotAdmittedInHospitalPickedUpSchedule,
    verifyBeneficiaryTreatmentNotAddmitedInHospitalPickUpSchedulePending,
};

/**
 * Submit the FHW verification form and verifies data in database
 *
 * @param {Object} formDetails Object with details for the fhw verification form
 */
async function submitFhwVerificationForm(formDetails) {
    const userInfoSeletQ = formDetails.userInfoSelectQuery || userInfoSelectQuery;
    const responseSelectQ = formDetails.responseSelectQuey || responseSelectQuery;
    const formRecordId = formDetails.recordId || recordId;

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
        select: [
            gvkFeatureConstant.canHighriskFollowupVerificationForFhw
        ]
    });

    const queryResult = await getQueryResult(`select * from imt_member where (basic_state ='VERIFIED' or basic_state='NEW') and id = ${formRecordId}`);
    expect(queryResult).toBeDefined();

    // Deleting record from user info table and response table
    await executeQuery(deleteQuery);

    // Inserting record
    await executeQuery(`insert into gvk_high_risk_follow_up_usr_info
       (member_id, gvk_call_state, gvk_call_status, schedule_date, call_attempt,
          diseases, highrisk_reason, location_id, created_by,
          created_on, modified_by, modified_on, identify_date)
      values
          (${formRecordId}, '${gvkVerificationConstant.highRiskConditionConfirmation}', '${gvkVerificationConstant.callProcessing}',
          (localtimestamp), 1, '${formDetails.desease}', 'SD Score:SD3, Mid Arm Circumference:14cm, Have Pedal Edema:false',
          137402, -1, (localtimestamp - interval '2 hour'),
          -1, (localtimestamp - interval '2 hour'), '2019-03-01')`);

    await startCallVerification();

    // Verifying state
    const verifyUserInfoBeforeExecArray = await verifyDataInDb(userInfoSeletQ, formDetails.verifyUserInfoBeforeExec);
    console.log(`verifyUserInfoBeforeExecArray = ${verifyUserInfoBeforeExecArray}`);
    expect(verifyUserInfoBeforeExecArray.length).toEqual(0);

    await clickOnElement(cssIdentifiers.verifiedByFhwButton[formDetails.isHighRisk], 'FHW response button not visible.');

    await clickOnElement(fhwCallButton, 'Calling FHW button not visible.');
    await checkToaster('error');
    await clickOnElement(submitButton, 'Submit button for the FHW decision not visible.');
    await waitForLoading();
    await checkToaster('success');

    // Verifying followup response
    const verifyResponseAfterExecArray = await verifyDataInDb(responseSelectQ, formDetails.verifyResponseAfterExec);
    console.log(`verifyResponseAfterExecArray = ${verifyResponseAfterExecArray}`);
    expect(verifyResponseAfterExecArray.length).toEqual(0);

    // Verifying followup user info
    const verifyUserInfoAfterExecArray = await verifyDataInDb(userInfoSeletQ, formDetails.verifyUserInfoAfterExec);
    console.log(`verifyUserInfoAfterExecArray = ${verifyUserInfoAfterExecArray}`);
    expect(verifyUserInfoAfterExecArray.length).toEqual(0);
}

/**
 * Executes steps for positive verification from beneficiary
 */
async function executeBeneficiaryVerificationPositiveSteps() {
    await selectDeselectFeatureAccess({
        deSelect: [gvkFeatureConstant.canHighriskFollowupVerificationForFhw],
        select: [gvkFeatureConstant.canHighriskFollowupVerification]
    });

    await executeQuery(modifiedOnHourDiffQuery);

    await startCallVerification();

    const verifyUserInfoBeforeExecArray = await verifyDataInDb(userInfoSelectQuery, testData.sam002.verifyUserInfoBeforeExec);
    console.log(`verifyUserInfoBeforeExecArray = ${verifyUserInfoBeforeExecArray}`);
    expect(verifyUserInfoBeforeExecArray.length).toEqual(0);

    await clickOnElement(callContactPerson);
    await checkToaster('error');
    await element(cssIdentifiers.beneficiaryWillingToHelpedButton[true]).click();

    await dateTimeSelector({ hours: 10, minutes: 10 });
    await clickOnElement(submitSchedule);
    await waitForLoading();
    await checkToaster('success');

    const verifyUserInfoAfterExecArray = await verifyDataInDb(userInfoSelectQuery, testData.sam002.verifyUserInfoAfterExec);
    console.log(`verifyUserInfoAfterExecArray = ${verifyUserInfoAfterExecArray}`);
    expect(verifyUserInfoAfterExecArray.length).toEqual(0);
}

/**
 * Executes steps for followup positive verification from beneficiary
 */
async function executeFollowupVerificationStepsPositive() {
    await executeQuery(modifiedScheduledHourDiffQuery);
    await startCallVerification();

    // Verifying user info before update
    const verifyUserInfoBeforeExecArray = await verifyDataInDb(userInfoSelectQuery, testData.sam003.verifyUserInfoBeforeExec);
    console.log(`verifyUserInfoBeforeExecArray = ${verifyUserInfoBeforeExecArray}`);
    expect(verifyUserInfoBeforeExecArray.length).toEqual(0);

    // Verifying response before update
    const verifyResponseBeforeExecArray = await verifyDataInDb(responseSelectQuery, testData.sam003.verifyResponseBeforeExec);
    console.log(`verifyResponseBeforeExecArray = ${verifyResponseBeforeExecArray}`);
    expect(verifyResponseBeforeExecArray.length).toEqual(0);

    // Submitting form
    await fillBeneficiaryVerificationForm(testData.sam003.formDetailsObj);

    // Verifying user info after update
    const verifyUserInfoAfterExecArray = await verifyDataInDb(userInfoSelectQuery, testData.sam003.verifyUserInfoAfterExec);
    console.log(`verifyUserInfoAfterExecArray = ${verifyUserInfoAfterExecArray}`);
    expect(verifyUserInfoAfterExecArray.length).toEqual(0);

    // Verifying response after update
    const verifyResponseAfterExecArray = await verifyDataInDb(responseSelectQuery, testData.sam003.verifyResponseAfterExec);
    console.log(`verifyResponseAfterExecArray = ${verifyResponseAfterExecArray}`);
    expect(verifyResponseAfterExecArray.length).toEqual(0);

    // Deleting record from user info table and response table
    return executeQuery(deleteQuery);
}

/**
 * Verifies the scenario for not visiting hospital with second try
 */
async function verifyNoVisitTwice() {
    const queryResult = await getQueryResult(`select * from imt_member where (basic_state ='VERIFIED' or basic_state='NEW') and id = 111923824`);
    expect(queryResult).toBeTruthy();

    // Deleting record from user info table and response table
    await executeQuery(deleteQuery);

    await executeQuery(`insert into gvk_high_risk_follow_up_usr_info
    (member_id, gvk_call_state, gvk_call_previous_state, gvk_call_status, schedule_date,call_attempt,diseases,highrisk_reason,location_id,
        created_by,created_on,modified_by,modified_on, identify_date)
        values
        (${recordId},'${gvkVerificationConstant.highRiskConditionConfirmationWithYes}',
        '${gvkVerificationConstant.highRiskConditionConfirmation}',
        '${gvkVerificationConstant.callProcessing}',now(),0,'SAM',
        'SD Score:SD3, Mid Arm Circumference:14cm, Have Pedal Edema:false', 137402,
        -1,(current_timestamp - interval '1 hour'),-1, (current_timestamp - interval '1 hour'), '2019-03-01');`);

    await startCallVerification();

    // Verifying user info before update
    const verifyUserInfoBeforeExecArray = await verifyDataInDb(userInfoSelectQuery, testData.sam011.verifyUserInfoBeforeExec);
    console.log(`verifyUserInfoBeforeExecArray = ${verifyUserInfoBeforeExecArray}`);
    expect(verifyUserInfoBeforeExecArray.length).toEqual(0);

    await clickOnElement(callContactPerson);
    await checkToaster('error');
    await element(cssIdentifiers.beneficiaryWillingToHelpedButton[false]).click();

    await clickOnElement(submitSchedule);
    await waitForLoading();
    await checkToaster('success');

    // Verifying user info after update before execution
    const verifyUserInfoAfterExecArray = await verifyDataInDb(userInfoSelectQuery, testData.sam011.verifyUserInfoAfterExec);
    console.log(`verifyUserInfoAfterExecArray = ${verifyUserInfoAfterExecArray}`);
    expect(verifyUserInfoAfterExecArray.length).toEqual(0);

    await executeQuery(modifiedScheduledHourDiffQuery);

    await startCallVerification();

    const verifyUserInfoAfterUpdateBeforeExec = await verifyDataInDb(userInfoSelectQuery, testData.sam011AfterUpdate.verifyUserInfoBeforeExec);
    console.log(`verifyUserInfoAfterUpdateBeforeExec = ${verifyUserInfoAfterUpdateBeforeExec}`);
    expect(verifyUserInfoAfterUpdateBeforeExec.length).toEqual(0);

    await clickOnElement(callContactPerson);
    await checkToaster('error');
    await element(cssIdentifiers.beneficiaryWillingToHelpedButton[false]).click();

    await clickOnElement(submitSchedule);
    await waitForLoading();
    await checkToaster('success');

    const verifyUserInfoAfterUpdateAfterExec = await verifyDataInDb(userInfoSelectQuery, testData.sam011AfterUpdate.verifyUserInfoAfterExec);
    console.log(`verifyUserInfoAfterUpdateAfterExec = ${verifyUserInfoAfterUpdateAfterExec}`);
    expect(verifyUserInfoAfterUpdateAfterExec.length).toEqual(0);

    // Deleting record from user info table and response table
    return executeQuery(deleteQuery);
}

/**
 * Verifies scenario for willing to visit after second call
 */
async function verifyVisitOnSecondCall() {
    const queryResult = await getQueryResult(`select * from imt_member where (basic_state ='VERIFIED' or basic_state='NEW') and id = 111923824`);
    expect(queryResult).toBeTruthy();

    // Deleting record from user info table and response table
    await executeQuery(deleteQuery);

    await executeQuery(`insert into gvk_high_risk_follow_up_usr_info
        (member_id, gvk_call_state, gvk_call_previous_state, gvk_call_status, schedule_date,call_attempt,diseases,highrisk_reason,location_id,
        created_by,created_on,modified_by,modified_on, identify_date)
        values
        (${recordId},'${gvkVerificationConstant.highRiskConditionConfirmationWithYes}',
        '${gvkVerificationConstant.highRiskConditionConfirmation}',
        '${gvkVerificationConstant.callProcessing}',(localtimestamp - interval '1 day'),0,'SAM',
        'SD Score:SD3, Mid Arm Circumference:14cm, Have Pedal Edema:false', 137402,
        -1,(current_timestamp - interval '1 hour'),-1, (current_timestamp - interval '1 hour'), '2019-03-01');`);

    await startCallVerification();

    // Verifying user info before update
    const verifyUserInfoBeforeExecArray = await verifyDataInDb(userInfoSelectQuery, testData.sam012.verifyUserInfoBeforeExec);
    console.log(`verifyUserInfoBeforeExecArray = ${verifyUserInfoBeforeExecArray}`);
    expect(verifyUserInfoBeforeExecArray.length).toEqual(0);

    await clickOnElement(callContactPerson);
    await checkToaster('error');
    await element(cssIdentifiers.beneficiaryWillingToHelpedButton[false]).click();

    await clickOnElement(submitSchedule);
    await checkToaster('success');
    await waitForLoading();

    const verifyUserInfoAfterExecArray = await verifyDataInDb(userInfoSelectQuery, testData.sam012.verifyUserInfoAfterExec);
    console.log(`verifyUserInfoAfterExecArray = ${verifyUserInfoAfterExecArray}`);
    expect(verifyUserInfoAfterExecArray.length).toEqual(0);

    await executeQuery(modifiedScheduledHourDiffQuery);

    await startCallVerification();

    const verifyUserInfoAfterUpdateBeforeExecArray = await verifyDataInDb(userInfoSelectQuery, testData.sam012AfterUpdate.verifyUserInfoBeforeExec);
    console.log(`verifyUserInfoAfterUpdateBeforeExecArray = ${verifyUserInfoAfterUpdateBeforeExecArray}`);
    expect(verifyUserInfoAfterUpdateBeforeExecArray.length).toEqual(0);

    await clickOnElement(callContactPerson);
    await checkToaster('error');
    await element(cssIdentifiers.beneficiaryWillingToHelpedButton[true]).click();

    await dateTimeSelector({ hours: 10, minutes: 10 });

    await clickOnElement(submitSchedule);
    await waitForLoading();
    await checkToaster('success');

    const verifyUserInfoAfterUpdateAfterExecArray = await verifyDataInDb(userInfoSelectQuery, testData.sam012AfterUpdate.verifyUserInfoAfterExec);
    console.log(`verifyUserInfoAfterUpdateAfterExecArray = ${verifyUserInfoAfterUpdateAfterExecArray}`);
    expect(verifyUserInfoAfterUpdateAfterExecArray.length).toEqual(0);

    // Deleting record from user info table and response table
    return executeQuery(deleteQuery);
}

/**
 * Verifies the scenario for the visit with time not decided
 */
async function verifyVisitWithScheduleNotDecided() {
    const queryResult = await getQueryResult(`select * from imt_member where (basic_state ='VERIFIED' or basic_state='NEW') and id = 111923824`);
    expect(queryResult).toBeTruthy();

    // Deleting record from user info table and response table
    await executeQuery(deleteQuery);

    await executeQuery(`insert into gvk_high_risk_follow_up_usr_info
        (member_id, gvk_call_state, gvk_call_previous_state, gvk_call_status, schedule_date,call_attempt,diseases,highrisk_reason,location_id,
        created_by,created_on,modified_by,modified_on, identify_date)
        values
        (${recordId},'${gvkVerificationConstant.highRiskConditionConfirmationWithYes}',
        '${gvkVerificationConstant.highRiskConditionConfirmation}',
        '${gvkVerificationConstant.callProcessing}',(localtimestamp - interval '1 day'),0,'SAM',
        'SD Score:SD3, Mid Arm Circumference:14cm, Have Pedal Edema:false', 137402,
        -1,(current_timestamp - interval '1 hour'),-1, (current_timestamp - interval '1 hour'), '2019-03-01');`);

    await startCallVerification();

    const verifyUserInfoBeforeExecArray = await verifyDataInDb(userInfoSelectQuery, testData.sam013.verifyUserInfoBeforeExec);
    console.log(`verifyUserInfoBeforeExecArray = ${verifyUserInfoBeforeExecArray}`);
    expect(verifyUserInfoBeforeExecArray.length).toEqual(0);

    await clickOnElement(callContactPerson);
    await checkToaster('error');
    await element(cssIdentifiers.beneficiaryWillingToHelpedButton[false]).click();

    await clickOnElement(submitSchedule);
    await checkToaster('success');
    await waitForLoading();

    const verifyUserInfoAfterExecArray = await verifyDataInDb(userInfoSelectQuery, testData.sam013.verifyUserInfoAfterExec);
    console.log(`verifyUserInfoAfterExecArray = ${verifyUserInfoAfterExecArray}`);
    expect(verifyUserInfoAfterExecArray.length).toEqual(0);

    await executeQuery(modifiedScheduledHourDiffQuery);

    await startCallVerification();

    const verifyUserInfoAfterUpdateBeforeExecArray = await verifyDataInDb(userInfoSelectQuery, testData.sam013AfterUpdate.verifyUserInfoBeforeExec);
    console.log(`verifyUserInfoAfterUpdateBeforeExecArray = ${verifyUserInfoAfterUpdateBeforeExecArray}`);
    expect(verifyUserInfoAfterUpdateBeforeExecArray.length).toEqual(0);

    await clickOnElement(callContactPerson);
    await checkToaster('error');
    await element(cssIdentifiers.beneficiaryWillingToHelpedButton[true]).click();

    await clickOnElement(cssIdentifiers.dateNotDecidedCheckBox);

    await clickOnElement(submitSchedule);
    await waitForLoading();
    await checkToaster('success');

    const verifyUserInfoAfterUpdateAfterExecArray = await verifyDataInDb(userInfoSelectQuery, testData.sam013AfterUpdate.verifyUserInfoAfterExec);
    console.log(`verifyUserInfoAfterUpdateAfterExecArray = ${verifyUserInfoAfterUpdateAfterExecArray}`);
    expect(verifyUserInfoAfterUpdateAfterExecArray.length).toEqual(0);

    await executeQuery(modifiedScheduledHourDiffQuery);

    await startCallVerification();

    const verifyUserInfoAfterUpdate2BeforeExecArray = await verifyDataInDb(userInfoSelectQuery, testData.sam013AfterUpdate2.verifyUserInfoBeforeExec);
    console.log(`verifyUserInfoAfterUpdate2BeforeExecArray = ${verifyUserInfoAfterUpdate2BeforeExecArray}`);
    expect(verifyUserInfoAfterUpdate2BeforeExecArray.length).toEqual(0);

    await clickOnElement(callContactPerson);
    await checkToaster('error');
    await element(cssIdentifiers.beneficiaryWillingToHelpedButton[true]).click();

    await dateTimeSelector({ hours: 10, minutes: 30 });
    await clickOnElement(submitSchedule);
    await waitForLoading();
    await checkToaster('success');

    const verifyUserInfoAfterUpdate2AfterExecArray = await verifyDataInDb(userInfoSelectQuery, testData.sam013AfterUpdate2.verifyUserInfoAfterExec);
    console.log(`verifyUserInfoAfterUpdate2AfterExecArray = ${verifyUserInfoAfterUpdate2AfterExecArray}`);
    expect(verifyUserInfoAfterUpdate2AfterExecArray.length).toEqual(0);

    // Deleting record from user info table and response table
    return executeQuery(deleteQuery);
}

/**
 * Verifies for the fhw marking false and no visit after yes
 */
async function executeVerificationStepsForNoVisit() {
    await selectDeselectFeatureAccess({
        deSelect: [gvkFeatureConstant.canHighriskFollowupVerification],
        select: [gvkFeatureConstant.canHighriskFollowupVerificationForFhw]
    });

    // Deleting record from user info table and response table
    await executeQuery(deleteQuery);

    await executeQuery(`insert into gvk_high_risk_follow_up_usr_info
    (member_id, gvk_call_state, gvk_call_status, schedule_date, call_attempt,
       diseases, highrisk_reason, location_id, created_by,
       created_on, modified_by, modified_on, identify_date)
   values
       (${recordId}, '${gvkVerificationConstant.highRiskConditionConfirmation}', '${gvkVerificationConstant.callProcessing}',
       (localtimestamp), 1, 'SAM', 'SD Score:SD3, Mid Arm Circumference:14cm, Have Pedal Edema:false',
       137402, -1, (localtimestamp - interval '2 hour'),
       -1, (localtimestamp - interval '2 hour'), '2019-03-01')`);

    await startCallVerification();

    // Verifying state
    const verifyUserInfoBeforeExecArray = await verifyDataInDb(userInfoSelectQuery, testData.sam014.verifyUserInfoBeforeExec);
    console.log(`verifyUserInfoBeforeExecArray = ${verifyUserInfoBeforeExecArray}`);
    expect(verifyUserInfoBeforeExecArray.length).toEqual(0);

    await clickOnElement(cssIdentifiers.verifiedByFhwButton[false], 'No button for FHW response not visible.');
    await clickOnElement(fhwCallButton, 'Calling FHW button not visible.');
    await checkToaster('error');
    await clickOnElement(submitButton, 'Submit button for the FHW decision not visible.');
    await waitForLoading();
    await checkToaster('success');

    // Verifying user info after update
    const verifyUserInfoAfterExecArray = await verifyDataInDb(userInfoSelectQuery, testData.sam014.verifyUserInfoAfterExec);
    console.log(`verifyUserInfoAfterExecArray = ${verifyUserInfoAfterExecArray}`);
    expect(verifyUserInfoAfterExecArray.length).toEqual(0);

    // Verifying response after update
    const verifyResponseAfterExecArray = await verifyDataInDb(responseSelectQuery, testData.sam014.verifyResponseAfterExec);
    console.log(`verifyResponseAfterExecArray = ${verifyResponseAfterExecArray}`);
    expect(verifyResponseAfterExecArray.length).toEqual(0);

    // Deleting record from user info table and response table
    return executeQuery(deleteQuery);
}

/**
 * Verifies the steps for 3 fail attemp to contact beneficiary
 */
async function executeVerificationStepsForNotReachableCall() {
    await selectDeselectFeatureAccess({
        deSelect: [gvkFeatureConstant.canHighriskFollowupVerificationForFhw],
        select: [gvkFeatureConstant.canHighriskFollowupVerification]
    });

    // Deleting record from user info table and response table
    await executeQuery(deleteQuery);

    await executeQuery(`insert into gvk_high_risk_follow_up_usr_info
    (member_id, gvk_call_state, gvk_call_status, schedule_date, call_attempt,
       diseases, highrisk_reason, location_id, created_by,
       created_on, modified_by, modified_on, identify_date)
   values
       (${recordId}, '${gvkVerificationConstant.highRiskConditionConfirmationWithYes}', '${gvkVerificationConstant.callProcessing}',
       (localtimestamp), 0, 'SAM', 'SD Score:SD3, Mid Arm Circumference:14cm, Have Pedal Edema:false',
       137402, -1, (localtimestamp - interval '2 hour'),
       -1, (localtimestamp - interval '2 hour'), '2019-03-01')`);
    await startCallVerification();

    // Verifying user info before update
    const verifyUserInfoBeforeExecArray = await verifyDataInDb(userInfoSelectQuery, testData.sam017.verifyUserInfoBeforeExec);
    console.log(`verifyUserInfoBeforeExecArray = ${verifyUserInfoBeforeExecArray}`);
    expect(verifyUserInfoBeforeExecArray.length).toEqual(0);

    // Submitting form
    await clickOnElement(callFamilyHead, 'Could not call family head.');
    await checkToaster('error');

    await dropdownListSelector(cssIdentifiers.callStatusDropDown, 'Not Reachable');

    await clickOnElement(submitButton, 'Could not submit form.');
    await waitForLoading();
    await checkToaster('success');

    // Verifying user info after not reachable
    const verifyUserInfoAfterExecArray = await verifyDataInDb(userInfoSelectQuery, testData.sam017.verifyUserInfoAfterExec);
    console.log(`verifyUserInfoAfterExecArray = ${verifyUserInfoAfterExecArray}`);
    expect(verifyUserInfoAfterExecArray.length).toEqual(0);

    // Verifying response after update for not reachable
    const verifyResponseAfterExecArray = await verifyDataInDb(responseSelectQuery, testData.sam017.verifyResponseAfterExec);
    console.log(`verifyResponseAfterExecArray = ${verifyResponseAfterExecArray}`);
    expect(verifyResponseAfterExecArray.length).toEqual(0);

    await executeQuery(modifiedScheduledDayDiffQuery);

    await startCallVerification();

    // Verifying user info before update
    const verifyUserInfoAfterUpdateBeforeExecArray = await verifyDataInDb(userInfoSelectQuery, testData.sam017AfterUpdate.verifyUserInfoBeforeExec);
    console.log(`verifyUserInfoAfterUpdateBeforeExecArray = ${verifyUserInfoAfterUpdateBeforeExecArray}`);
    expect(verifyUserInfoAfterUpdateBeforeExecArray.length).toEqual(0);

    // Submitting form
    await clickOnElement(callFamilyHead, 'Could not call family head.');
    await checkToaster('error');

    await dropdownListSelector(cssIdentifiers.callStatusDropDown, 'Not Reachable');

    await clickOnElement(submitButton, 'Could not submit form.');
    await waitForLoading();
    await checkToaster('success');

    // Verifying user info after not reachable
    const verifyUserInfoAfterUpdateAfterExecArray = await verifyDataInDb(userInfoSelectQuery, testData.sam017AfterUpdate.verifyUserInfoAfterExec);
    console.log(`verifyUserInfoAfterUpdateAfterExecArray = ${verifyUserInfoAfterUpdateAfterExecArray}`);
    expect(verifyUserInfoAfterUpdateAfterExecArray.length).toEqual(0);

    // Verifying response after update for not reachable
    const verifyResponseAfterUpdateAfterExecArray = await verifyDataInDb(responseSelectQuery, testData.sam017AfterUpdate.verifyResponseAfterExec);
    console.log(`verifyResponseAfterUpdateAfterExecArray = ${verifyResponseAfterUpdateAfterExecArray}`);
    expect(verifyResponseAfterUpdateAfterExecArray.length).toEqual(0);

    await executeQuery(modifiedScheduledDayDiffQuery);

    await startCallVerification();

    // Verifying user info before update
    const verifyUserInfoAfterUpdate2BeforeExecArray = await verifyDataInDb(userInfoSelectQuery, testData.sam017AfterUpdate2.verifyUserInfoBeforeExec);
    console.log(`verifyUserInfoAfterUpdate2BeforeExecArray = ${verifyUserInfoAfterUpdate2BeforeExecArray}`);
    expect(verifyUserInfoAfterUpdate2BeforeExecArray.length).toEqual(0);

    await dropdownListSelector(cssIdentifiers.callStatusDropDown, 'Not Reachable');

    await clickOnElement(callFamilyHead, 'Could not call family head.');
    await checkToaster('error');

    // Submitting form
    await clickOnElement(submitButton, 'Could not submit form.');
    await waitForLoading();
    await checkToaster('success');

    // Verifying user info after not reachable
    const verifyUserInfoAfterUpdate2AfterExecArray = await verifyDataInDb(userInfoSelectQuery, testData.sam017AfterUpdate2.verifyUserInfoAfterExec);
    console.log(`verifyUserInfoAfterUpdate2AfterExecArray = ${verifyUserInfoAfterUpdate2AfterExecArray}`);
    expect(verifyUserInfoAfterUpdate2AfterExecArray.length).toEqual(0);

    // Verifying response after update for not reachable
    const verifyResponseAfterUpdate2AfterExec = await verifyDataInDb(responseSelectQuery, testData.sam017AfterUpdate2.verifyResponseAfterExec);
    console.log(`verifyResponseAfterUpdate2AfterExec = ${verifyResponseAfterUpdate2AfterExec}`);
    expect(verifyResponseAfterUpdate2AfterExec.length).toEqual(0);

    await executeQuery(modifiedScheduledDayDiffQuery);

    await startCallVerification();

    await browser.wait(EC.visibilityOf(element(noRecordsToVerify)), 10000, 'No records to verify message not visible.');

    // Deleting record from user info table and response table
    await executeQuery(deleteQuery);

    return selectDeselectFeatureAccess({
        deSelect: [gvkFeatureConstant.canHighriskFollowupVerification]
    });
}

/**
 *  beneficiary not picked up by 108 and pickedup schedule for next visit
 */
async function verifyBeneficiaryTreatmentNo108ServicePickedUpSchedule() {

    console.log('High Risk Follow Up – Follow Up call for checking SAM with No 108 service and willing to schedule-004');

    await selectDeselectFeatureAccess({
        deSelect: [gvkFeatureConstant.canHighriskFollowupVerificationForFhw],
        select: [gvkFeatureConstant.canHighriskFollowupVerification]
    });

    // Deleting record from user info table and response table
    await executeQuery(deleteQuery);

    let record_id = recordId;

    const memberStateVerificationQueryResult = await getQueryResult(memberStateVerificationQuery);
    expect(memberStateVerificationQueryResult).toBeDefined();

    // insert record
    await executeQuery(`insert into gvk_high_risk_follow_up_usr_info
        (member_id, gvk_call_state, gvk_call_previous_state, gvk_call_status, schedule_date,pickup_schedule_date,call_attempt,diseases,highrisk_reason,location_id,
        created_by,created_on,modified_by,modified_on, identify_date)
        values
        (${record_id},'${gvkVerificationConstant.pickedupSchedule}', 
        '${gvkVerificationConstant.highRiskConditionConfirmationWithYes}',
        '${gvkVerificationConstant.callProcessing}',localtimestamp, (localtimestamp - interval '1 day'),0,'SAM',
        'SD Score:SD3, Mid Arm Circumference:14cm, Have Pedal Edema:false', 137402,
        -1,(current_timestamp - interval '2 hour'),-1, (current_timestamp - interval '2 hour'), '2019-03-01');`);

    await startCallVerification();

    // check the state
    const verifyMemberInfoBeforeExecArray = await verifyDataInDb(userInfoSelectQuery, testData.sam004.verifyMemberInfoBeforeExec);
    console.log(`verifyMemberInfoBeforeExecArray = ${verifyMemberInfoBeforeExecArray}`);
    expect(verifyMemberInfoBeforeExecArray.length).toEqual(0);

    const beneficiaryFollowupPageTitle = 'High Risk follow up - Follow up call for checking';
    let pageTitle = element(by.binding('gvkreverification.pageTitle'));
    expect(pageTitle.getText()).toBe(beneficiaryFollowupPageTitle);

    await clickOnElement(callFamilyHead, 'Could not call family head.');
    await checkToaster('error');

    await clickOnElement(cssIdentifiers.is108PickedupBeneficiaryRadioBtn[false], '108 picked up button is not visible.');
    await clickOnElement(cssIdentifiers.isBeneficiaryWantReschedulePickupDateRadioBtn[true],
        'reschedule pick up date radio button is not visible');

    await dateTimeSelector({ hours: 10, minutes: 10 });

    // Submitting form
    await clickOnElement(submitButton, 'Could not submit form.');
    await checkToaster('success');

    // Verifying followup member info after submission 
    const verifyMemberInfoAfterExecArray = await verifyDataInDb(userInfoSelectQuery, testData.sam004.verifyMemberInfoAfterExec);
    console.log(`verifyMemberInfoAfterExecArray = ${verifyMemberInfoAfterExecArray}`);
    expect(verifyMemberInfoAfterExecArray.length).toEqual(0);

    const verifyResponseAfterExecArray = await verifyDataInDb(responseSelectQuery, testData.sam004.verifyResponseAfterExec);
    console.log(`verifyResponseAfterExecArray = ${verifyResponseAfterExecArray}`);
    expect(verifyResponseAfterExecArray.length).toEqual(0);

    // Deleting record from user info table and response table
    return executeQuery(deleteQuery);

}

/**
 *  beneficiary not picked up by 108 and pickedup schedule pending
 */
async function verifyBeneficiaryTreatmentNo108ServicePickUpSchedulePending() {

    console.log('High Risk Follow Up – Follow Up call for checking SAM with No 108 service and no decision but willing to schedule 108 service 005');

    let record_id = recordId;

    // Deleting record from user info table and response table
    await executeQuery(deleteQuery);

    const memberStateVerificationQueryResult = await getQueryResult(memberStateVerificationQuery);
    expect(memberStateVerificationQueryResult).toBeDefined();

    // insert record

    await executeQuery(`insert into gvk_high_risk_follow_up_usr_info
    (member_id, gvk_call_state, gvk_call_previous_state, gvk_call_status, schedule_date, pickup_schedule_date,call_attempt,diseases,highrisk_reason,location_id,
    created_by,created_on,modified_by,modified_on, identify_date)
    values
    (${record_id},'${gvkVerificationConstant.pickedupSchedule}', 
    '${gvkVerificationConstant.highRiskConditionConfirmationWithYes}',
    '${gvkVerificationConstant.callProcessing}', localtimestamp, (localtimestamp - interval '1 day'),0,'SAM',
    'SD Score:SD3, Mid Arm Circumference:14cm, Have Pedal Edema:false', 137402,
    -1,(current_timestamp - interval '2 hour'),-1, (current_timestamp - interval '2 hour'), '2019-03-01')`);

    await startCallVerification();

    // check the query result
    const verifyMemberInfoBeforeExecArray = await verifyDataInDb(userInfoSelectQuery, testData.sam005.verifyMemberInfoBeforeExec);
    console.log(`verifyMemberInfoBeforeExecArray = ${verifyMemberInfoBeforeExecArray}`);
    expect(verifyMemberInfoBeforeExecArray.length).toEqual(0);



    const beneficiaryFollowupPageTitle = 'High Risk follow up - Follow up call for checking';
    let pageTitle = element(by.binding('gvkreverification.pageTitle'));
    expect(pageTitle.getText()).toBe(beneficiaryFollowupPageTitle);

    await clickOnElement(callFamilyHead, 'Calling FHW button not visible.');
    await checkToaster('error');

    await clickOnElement(cssIdentifiers.is108PickedupBeneficiaryRadioBtn[false], '108 picked up button is not visible.');
    await clickOnElement(cssIdentifiers.isBeneficiaryWantReschedulePickupDateRadioBtn[true],
        'reschedule pick up date radio button is not visible');
    await clickOnElement(haventDecidedCheckbox, 'schedule pending checkbox is not visible');

    // Submitting form
    await clickOnElement(submitButton, 'Could not submit form.');
    await checkToaster('success');

    // Verifying followup member info after submission 
    const verifyMemberInfoAfterExecArray = await verifyDataInDb(userInfoSelectQuery, testData.sam005.verifyMemberInfoAfterExec);
    console.log(`verifyMemberInfoAfterExecArray = ${verifyMemberInfoAfterExecArray}`);
    expect(verifyMemberInfoAfterExecArray.length).toEqual(0);

    const verifyResponseAfterExecArray = await verifyDataInDb(responseSelectQuery, testData.sam005.verifyResponseAfterExec);
    console.log(`verifyResponseAfterExecArray = ${verifyResponseAfterExecArray}`);
    expect(verifyResponseAfterExecArray.length).toEqual(0);

    // query to run test in sequence
    await executeQuery(modifiedScheduledHourDiffQuery);

    // await browser.refresh();

    await startCallVerification();


    // check the state
    const afterUpdateVerifyMemberInfoBeforeExecArray = await verifyDataInDb(userInfoSelectQuery, testData.sam005.afterUpdateVerifyMemberInfoBeforeExec);
    console.log(`afterUpdateVerifyMemberInfoBeforeExecArray = ${afterUpdateVerifyMemberInfoBeforeExecArray}`);
    expect(afterUpdateVerifyMemberInfoBeforeExecArray.length).toEqual(0);

    const beneficiaryFollowupSchedulePendingPageTitle = 'High Risk follow up - Follow up call for checking- schedule pending';
    let SchedulePendinPageTitle = element(by.binding('gvkreverification.pageTitle'));
    expect(SchedulePendinPageTitle.getText()).toBe(beneficiaryFollowupSchedulePendingPageTitle);


    await clickOnElement(callFamilyHead, 'Calling FHW button not visible.');
    await checkToaster('error');

    let selectedPickedScheduleDate = new Date(new Date().setDate(new Date().getDate() + 3)).getDate();

    // await setPickupScheduleDate(selectedPickedScheduleDate, 10, 20);
    await dateTimeSelector({ hours: 10, minutes: 10, date: selectedPickedScheduleDate });
    // await dateTimeSelector(10, 20, selectedPickedScheduleDate)
    await clickOnElement(submitButton, 'submit response is not visible');
    await checkToaster('success');

    // Verifying followup member info after submission 
    const afterUpdateVerifyMemberInfoAfterExecArray = await verifyDataInDb(userInfoSelectQuery, testData.sam005.afterUpdateVerifyMemberInfoAfterExec);
    console.log(`afterUpdateVerifyMemberInfoAfterExecArray = ${afterUpdateVerifyMemberInfoAfterExecArray}`);
    expect(afterUpdateVerifyMemberInfoAfterExecArray.length).toEqual(0);

    // Deleting record from user info table and response table
    return executeQuery(deleteQuery);

}

/**
 * beneficiary not picked up by 108 and not want to be helped.
 */
async function verifyBeneficiaryTreatmentNo108ServiceNotWillngToHelped() {

    console.log('High Risk follow up - Follow up call for checking SAM-108 not came and 108 not required 006');

    let record_id = recordId;

    const memberStateVerificationQueryResult = await getQueryResult(memberStateVerificationQuery);
    expect(memberStateVerificationQueryResult).toBeDefined();

    // Deleting record from user info table and response table
    await executeQuery(deleteQuery);

    // insert record
    await executeQuery(`insert into gvk_high_risk_follow_up_usr_info
        (member_id, gvk_call_state, gvk_call_previous_state, gvk_call_status, schedule_date, pickup_schedule_date,call_attempt,diseases,highrisk_reason,location_id,
        created_by,created_on,modified_by,modified_on, identify_date)
        values
        (${record_id},'${gvkVerificationConstant.pickedupSchedule}', 
        '${gvkVerificationConstant.highRiskConditionConfirmationWithYes}',
        '${gvkVerificationConstant.callProcessing}', localtimestamp, (localtimestamp - interval '1 day'),0,'SAM',
        'SD Score:SD3, Mid Arm Circumference:14cm, Have Pedal Edema:false', 137402,
        -1,(current_timestamp - interval '2 hour'),-1, (current_timestamp - interval '2 hour'), '2019-03-01')`);

    await startCallVerification();

    // check the state
    const verifyMemberInfoBeforeExecArray = await verifyDataInDb(userInfoSelectQuery, testData.sam006.verifyMemberInfoBeforeExec);
    console.log(`verifyMemberInfoBeforeExecArray = ${verifyMemberInfoBeforeExecArray}`);
    expect(verifyMemberInfoBeforeExecArray.length).toEqual(0);


    const beneficiaryFollowupPageTitle = 'High Risk follow up - Follow up call for checking';
    let pageTitle = element(by.binding('gvkreverification.pageTitle'));
    expect(pageTitle.getText()).toBe(beneficiaryFollowupPageTitle);

    await clickOnElement(callFamilyHead, 'Calling FHW button not visible.');
    await checkToaster('error');

    await clickOnElement(cssIdentifiers.is108PickedupBeneficiaryRadioBtn[false], '108 picked up button is not visible.');
    await clickOnElement(cssIdentifiers.isBeneficiaryWantReschedulePickupDateRadioBtn[false],
        'reschedule pick up date radio button is not visible');

    await clickOnElement(submitButton, 'submit response is not visible');
    await checkToaster('success');

    // Verifying followup member info after submission 
    const verifyMemberInfoAfterExecArray = await verifyDataInDb(userInfoSelectQuery, testData.sam006.verifyMemberInfoAfterExec);
    console.log(`verifyMemberInfoAfterExecArray = ${verifyMemberInfoAfterExecArray}`);
    expect(verifyMemberInfoAfterExecArray.length).toEqual(0);

    const verifyResponseAfterExecArray = await verifyDataInDb(responseSelectQuery, testData.sam006.verifyResponseAfterExec);
    console.log(`verifyResponseAfterExecArray = ${verifyResponseAfterExecArray}`);
    expect(verifyResponseAfterExecArray.length).toEqual(0);

    // Deleting record from user info table and response table
    return executeQuery(deleteQuery);
}

/**
 * beneficiary not visited hospital and pickedup schedule for next
 */
async function verifyBeneficiaryTreatmentNoHospitalVisitPickedUpSchedule() {

    console.log('High Risk Follow Up – Follow Up call for checking SAM no hospital visit but need to visit 007');

    let record_id = recordId;

    const memberStateVerificationQueryResult = await getQueryResult(memberStateVerificationQuery);
    expect(memberStateVerificationQueryResult).toBeDefined();

    // Deleting record from user info table and response table
    await executeQuery(deleteQuery);

    // insert record
    await executeQuery(`insert into gvk_high_risk_follow_up_usr_info
        (member_id, gvk_call_state, gvk_call_previous_state, gvk_call_status, schedule_date, pickup_schedule_date,call_attempt,diseases,highrisk_reason,location_id,
        created_by,created_on,modified_by,modified_on, identify_date)
        values
        (${record_id},'${gvkVerificationConstant.pickedupSchedule}', 
        '${gvkVerificationConstant.highRiskConditionConfirmationWithYes}',
        '${gvkVerificationConstant.callProcessing}', localtimestamp, (localtimestamp - interval '1 day'),0,'SAM',
        'SD Score:SD3, Mid Arm Circumference:14cm, Have Pedal Edema:false', 137402,
        -1,(current_timestamp - interval '2 hour'),-1, (current_timestamp - interval '2 hour'), '2019-03-01');`);

    await startCallVerification();

    // check the state
    const verifyMemberInfoBeforeExecArray = await verifyDataInDb(userInfoSelectQuery, testData.sam007.verifyMemberInfoBeforeExec);
    console.log(`verifyMemberInfoBeforeExecArray = ${verifyMemberInfoBeforeExecArray}`);
    expect(verifyMemberInfoBeforeExecArray.length).toEqual(0);

    const beneficiaryFollowupPageTitle = 'High Risk follow up - Follow up call for checking';
    let pageTitle = element(by.binding('gvkreverification.pageTitle'));
    expect(pageTitle.getText()).toBe(beneficiaryFollowupPageTitle);

    await clickOnElement(callFamilyHead, 'Calling FHW button not visible.');
    await checkToaster('error');

    await clickOnElement(cssIdentifiers.is108PickedupBeneficiaryRadioBtn[true], '108 picked up button is not visible.');
    await clickOnElement(cssIdentifiers.isBeneficiaryVisitedPHCRadioBtn[false], 'Beneficiary visited phc button is not visible.');
    await clickOnElement(cssIdentifiers.isBeneficiaryWantReschedulePickupDateRadioBtn[true],
        'reschedule pick up date radio button is not visible');

    let selectedDate = new Date(new Date().setDate(new Date().getDate() + 2)).getDate();

    // await dateTimeSelector(18, 40, selectedDate)
    await dateTimeSelector({ hours: 5, minutes: 40, date: selectedDate });
    await clickOnElement(submitButton, 'submit response is not visible');
    await checkToaster('success');

    // Verifying followup member info after submission 
    const verifyMemberInfoAfterExecArray = await verifyDataInDb(userInfoSelectQuery, testData.sam007.verifyMemberInfoAfterExec);
    console.log(`verifyMemberInfoAfterExecArray = ${verifyMemberInfoAfterExecArray}`);
    expect(verifyMemberInfoAfterExecArray.length).toEqual(0);

    const verifyResponseAfterExecArray = await verifyDataInDb(responseSelectQuery, testData.sam007.verifyResponseAfterExec);
    console.log(`verifyResponseAfterExecArray = ${verifyResponseAfterExecArray}`);
    expect(verifyResponseAfterExecArray.length).toEqual(0);


    // Deleting record from user info table and response table
    return executeQuery(deleteQuery);
}

/**
 * beneficiary not visited hospital and not willing to be helped
 */
async function verifyBeneficiaryTreatmentNoHospitalVisitNotWillngToHelped() {

    console.log('High Risk Follow Up – Follow Up call for checking SAM no hospital visit and not willing to visit 008');

    let record_id = recordId;

    // Deleting record from user info table and response table
    await executeQuery(deleteQuery);

    const memberStateVerificationQueryResult = await getQueryResult(memberStateVerificationQuery);
    expect(memberStateVerificationQueryResult).toBeDefined();

    // insert record
    await executeQuery(`insert into gvk_high_risk_follow_up_usr_info
        (member_id, gvk_call_state, gvk_call_previous_state, gvk_call_status, schedule_date, pickup_schedule_date,call_attempt,diseases,highrisk_reason,location_id,
        created_by,created_on,modified_by,modified_on, identify_date)
        values
        (${record_id},'${gvkVerificationConstant.pickedupSchedule}', 
        '${gvkVerificationConstant.highRiskConditionConfirmationWithYes}',
        '${gvkVerificationConstant.callProcessing}', localtimestamp, (localtimestamp - interval '1 day'),0,'SAM',
        'SD Score:SD3, Mid Arm Circumference:14cm, Have Pedal Edema:false', 137402,
        -1,(current_timestamp - interval '2 hour'),-1, (current_timestamp - interval '2 hour'), '2019-03-01');`);

    await startCallVerification();

    // check the state
    const verifyMemberInfoBeforeExecArray = await verifyDataInDb(userInfoSelectQuery, testData.sam008.verifyMemberInfoBeforeExec);
    console.log(`verifyMemberInfoBeforeExecArray = ${verifyMemberInfoBeforeExecArray}`);
    expect(verifyMemberInfoBeforeExecArray.length).toEqual(0);

    const beneficiaryFollowupPageTitle = 'High Risk follow up - Follow up call for checking';
    let pageTitle = element(by.binding('gvkreverification.pageTitle'));
    expect(pageTitle.getText()).toBe(beneficiaryFollowupPageTitle);

    await clickOnElement(callFamilyHead, 'Calling FHW button not visible.');
    await checkToaster('error');

    await clickOnElement(cssIdentifiers.is108PickedupBeneficiaryRadioBtn[true], '108 picked up button is not visible.');
    await clickOnElement(cssIdentifiers.isBeneficiaryVisitedPHCRadioBtn[false], 'Beneficiary visited phc button is not visible.');
    await clickOnElement(cssIdentifiers.isBeneficiaryWantReschedulePickupDateRadioBtn[false],
        'reschedule pick up date radio button is not visible');

    await clickOnElement(submitButton, 'submit response is not visible');
    await checkToaster('success');

    // Verifying followup member info after submission 
    const verifyMemberInfoAfterExecArray = await verifyDataInDb(userInfoSelectQuery, testData.sam008.verifyMemberInfoAfterExec);
    console.log(`verifyMemberInfoAfterExecArray = ${verifyMemberInfoAfterExecArray}`);
    expect(verifyMemberInfoAfterExecArray.length).toEqual(0);

    const verifyResponseAfterExecArray = await verifyDataInDb(responseSelectQuery, testData.sam008.verifyResponseAfterExec);
    console.log(`verifyResponseAfterExecArray = ${verifyResponseAfterExecArray}`);
    expect(verifyResponseAfterExecArray.length).toEqual(0);

    // Deleting record from user info table and response table
    return executeQuery(deleteQuery);
}

/**
 * beneficiary not admitted to the hospital and pickedup schedule for next
 */
async function verifyBeneficiaryTreatmentNotAdmittedInHospitalPickedUpSchedule() {

    console.log('High Risk Follow Up – Follow Up call for checking SAM no child centre visit but need to visit 009');

    let record_id = recordId;

    // Deleting record from user info table and response table
    await executeQuery(deleteQuery);

    const memberStateVerificationQueryResult = await getQueryResult(memberStateVerificationQuery);
    expect(memberStateVerificationQueryResult).toBeDefined();

    // insert record
    await executeQuery(`insert into gvk_high_risk_follow_up_usr_info
        (member_id, gvk_call_state, gvk_call_previous_state, gvk_call_status, schedule_date, pickup_schedule_date,call_attempt,diseases,highrisk_reason,location_id,
        created_by,created_on,modified_by,modified_on, identify_date)
        values
        (${record_id},'${gvkVerificationConstant.pickedupSchedule}', 
        '${gvkVerificationConstant.highRiskConditionConfirmationWithYes}',
        '${gvkVerificationConstant.callProcessing}', localtimestamp, (localtimestamp - interval '1 day'),0,'SAM',
        'SD Score:SD3, Mid Arm Circumference:14cm, Have Pedal Edema:false', 137402,
        -1,(current_timestamp - interval '2 hour'),-1, (current_timestamp - interval '2 hour'), '2019-03-01');`);

    await startCallVerification();

    // check the state
    const verifyMemberInfoBeforeExecArray = await verifyDataInDb(userInfoSelectQuery, testData.sam009.verifyMemberInfoBeforeExec);
    console.log(`verifyMemberInfoBeforeExecArray = ${verifyMemberInfoBeforeExecArray}`);
    expect(verifyMemberInfoBeforeExecArray.length).toEqual(0);

    await clickOnElement(callFamilyHead, 'Calling family head button not visible.');
    await checkToaster('error');

    await clickOnElement(cssIdentifiers.is108PickedupBeneficiaryRadioBtn[true], '108 picked up button is not visible.');
    await clickOnElement(cssIdentifiers.isBeneficiaryVisitedPHCRadioBtn[true], 'Beneficiary visited phc button is not visible.');

    // treatment question
    await clickOnElement(cssIdentifiers.isBeneficiaryChildAdmittedRadioBtn[false], 'child admitted is hospital button is not visible.');

    await clickOnElement(cssIdentifiers.isBeneficiaryWantReschedulePickupDateRadioBtn[true], 'Beneficiary pickedup date for next schedule is not visible');


    let rescheduleDate = new Date(new Date().setDate(new Date().getDate() + 17)).getDate();

    // await setPickupScheduleDate(selectedDate, 10, 10);
    await dateTimeSelector({ hours: 10, minutes: 20, date: rescheduleDate });

    await clickOnElement(submitButton, 'submit response is not visible');
    await checkToaster('success');

    // Verifying followup member info after submission 
    const verifyMemberInfoAfterExecArray = await verifyDataInDb(userInfoSelectQuery, testData.sam009.verifyMemberInfoAfterExec);
    console.log(`verifyMemberInfoAfterExecArray = ${verifyMemberInfoAfterExecArray}`);
    expect(verifyMemberInfoAfterExecArray.length).toEqual(0);

    const verifyResponseAfterExecArray = await verifyDataInDb(responseSelectQuery, testData.sam009.verifyResponseAfterExec);
    console.log(`verifyResponseAfterExecArray = ${verifyResponseAfterExecArray}`);
    expect(verifyResponseAfterExecArray.length).toEqual(0);

    // Deleting record from user info table and response table
    return executeQuery(deleteQuery);
}

/**
 * 
 */
async function verifyBeneficiaryTreatmentNotAddmitedInHospitalPickUpSchedulePending() {
    console.log('High Risk Follow Up – Follow Up call for checking SAM no child centre visit and no to visit 010')

    let record_id = recordId;

    const memberStateVerificationQueryResult = await getQueryResult(memberStateVerificationQuery);
    expect(memberStateVerificationQueryResult).toBeDefined();

    // Deleting record from user info table and response table
    await executeQuery(deleteQuery);

    // insert record
    await executeQuery(`insert into gvk_high_risk_follow_up_usr_info
        (member_id, gvk_call_state, gvk_call_previous_state, gvk_call_status, schedule_date, pickup_schedule_date,call_attempt,diseases,highrisk_reason,location_id,
        created_by,created_on,modified_by,modified_on, identify_date)
        values
        (${record_id},'${gvkVerificationConstant.pickedupSchedule}', 
        '${gvkVerificationConstant.highRiskConditionConfirmationWithYes}',
        '${gvkVerificationConstant.callProcessing}', localtimestamp, (localtimestamp - interval '1 day'),0,'SAM',
        'SD Score:SD3, Mid Arm Circumference:14cm, Have Pedal Edema:false', 137402,
        -1,(current_timestamp - interval '2 hour'),-1, (current_timestamp - interval '2 hour'), '2019-03-01');`);

    // Going on call verification page and start calling
    await startCallVerification();

    const beneficiaryFollowupPageTitle = 'High Risk follow up - Follow up call for checking';
    let pageTitle = element(by.binding('gvkreverification.pageTitle'));
    expect(pageTitle.getText()).toBe(beneficiaryFollowupPageTitle);

    // check the state
    const verifyMemberInfoBeforeExecArray = await verifyDataInDb(userInfoSelectQuery, testData.sam010.verifyMemberInfoBeforeExec);
    console.log(`verifyMemberInfoBeforeExecArray = ${verifyMemberInfoBeforeExecArray}`);
    expect(verifyMemberInfoBeforeExecArray.length).toEqual(0);

    await clickOnElement(callFamilyHead, 'Calling family head button not visible.');
    await checkToaster('error');

    await clickOnElement(cssIdentifiers.is108PickedupBeneficiaryRadioBtn[true], '108 picked up button is not visible.');
    await clickOnElement(cssIdentifiers.isBeneficiaryVisitedPHCRadioBtn[true], 'Beneficiary visited phc button is not visible.');

    // treatment question
    await clickOnElement(cssIdentifiers.isBeneficiaryChildAdmittedRadioBtn[false], 'child admitted is hospital button is not visible.');
    await clickOnElement(cssIdentifiers.isBeneficiaryWantReschedulePickupDateRadioBtn[false], 'Beneficiary pickedup date for next schedule is not visible');

    await clickOnElement(submitButton, 'submit response is not visible');
    await checkToaster('success');

    // Verifying followup member info after submission 
    const verifyMemberInfoAfterExecArray = await verifyDataInDb(userInfoSelectQuery, testData.sam010.verifyMemberInfoAfterExec);
    console.log(`verifyMemberInfoAfterExecArray = ${verifyMemberInfoAfterExecArray}`);
    expect(verifyMemberInfoAfterExecArray.length).toEqual(0);

    const verifyResponseAfterExecArray = await verifyDataInDb(responseSelectQuery, testData.sam010.verifyResponseAfterExec);
    console.log(`verifyResponseAfterExecArray = ${verifyResponseAfterExecArray}`);
    expect(verifyResponseAfterExecArray.length).toEqual(0);

    // Deleting record from user info table and response table
    return executeQuery(deleteQuery);
}


/**
 * Fills up beneficiary verification form. (SAM screen 3)
 *
 * @param {Object} formDetailsObj Object with form details
 * @param {String=} formDetailsObj.callStatus Call status if for non successful call.
 * @param {Boolean} formDetailsObj.has108PickedupBeneficiary Has 108 picked beneficiary on scheduled date?
 * @param {Boolean=} formDetailsObj.hasBeneficiaryVisitedPhc Has beneficiary visited PHC?
 * @param {Boolean=} formDetailsObj.isBeneficiaryChildAdmitted Is child admitted?
 * @param {Boolean=} formDetailsObj.isBeneficiaryDropedAtHome Is beneficiary droped back at home?
 * @param {Boolean=} formDetailsObj.isBeneficiaryReschedulingVisit Is beneficiary rescheduling visit?
 * @param {Object=} formDetailsObj.dateTimeObject Object with reschedule date and time.
 */
async function fillBeneficiaryVerificationForm(formDetailsObj) {
    // For call status except successful
    if (formDetailsObj.callStatus) {
        await dropdownListSelector(callStatusDropDown, formDetailsObj.callStatus);

        await clickOnElement(submitButton, `Could not submit for ${formDetailsObj.callStatus}.`);
        await waitForLoading();
        return checkToaster('success');
    }

    let rescheduleVisit = false;

    // Calling Family head
    await clickOnElement(callFamilyHead, 'Could not call family head.');
    await checkToaster('error');

    // 108 picked beneficiary
    await clickOnElement(cssIdentifiers.is108PickedupBeneficiaryRadioBtn[formDetailsObj.has108PickedupBeneficiary], 'Could not answer for has 108 picked beneficiary');

    // Beneficiary visited PHC
    if (formDetailsObj.has108PickedupBeneficiary) {
        await clickOnElement(cssIdentifiers.isBeneficiaryVisitedPHCRadioBtn[formDetailsObj.hasBeneficiaryVisitedPhc], 'Could not answer for has beneficiary visited PHC.');

        // Beneficiary's child admitted
        if (formDetailsObj.hasBeneficiaryVisitedPhc) {
            await clickOnElement(cssIdentifiers.isBeneficiaryChildAdmittedRadioBtn[formDetailsObj.isBeneficiaryChildAdmitted], `Could not answer for beneficiary's child admitted.`);

            // Beneficiary dropped at home by 108
            if (formDetailsObj.isBeneficiaryChildAdmitted) {
                await clickOnElement(cssIdentifiers.isBeneficiaryDropedAtHomeBy108RadioBtn[formDetailsObj.isBeneficiaryDropedAtHome], 'Could not answer for is beneficiary dropped at home by 108.');
            } else {
                rescheduleVisit = true;

            }
        } else {
            rescheduleVisit = true;

        }
    } else {
        rescheduleVisit = true;

    }

    if (rescheduleVisit) {
        // Choice for reschedule
        await clickOnElement(cssIdentifiers.isBeneficiaryWantReschedulePickupDateRadioBtn[formDetailsObj.isBeneficiaryReschedulingVisit], 'Could not click on choice to reschedule.');

        // Time selection for reschedule
        if (formDetailsObj.isBeneficiaryReschedulingVisit) {
            formDetailsObj.dateTimeObject
                ? await dateTimeSelector(formDetailsObj.dateTimeObject)
                : await clickOnElement(haventDecidedCheckbox, `Could not click on haven't decided.`);
        }
    }

    await clickOnElement(submitButton, 'Could not submit form.');
    await waitForLoading();
    return checkToaster('success');
}
