const EC = protractor.ExpectedConditions;
const {
    clickOnElement,
    dateTimeSelector,
    dropdownListSelector,
    checkToaster,
    executeQuery,
    getQueryResult,
    startCallVerification,
    verifyDataInDb,
    waitForLoading,

} = require('../../common/utils');

const {
    gvkVerificationConstant
} = require('../../common/gvk_constant')

const {
    callStatusDropDown,
    noRecordsToVerify,
    submitButton
} = require('../../common/cssIdentifiers');

const cssIdentifiers = require('./immunization.es');

const {
    deleteQuery,
    immunizationResponseQuery,
    immunizationSelectQuery,
    imtMemberSelectQuery,
    recordId
} = require('./immunization.td');

module.exports = {
    fillImmunizationVerificationForm,
    executeVerificationAndSubmission,
    verifyUnsuccessfulMax
};

/**
 * Fills the Immunization verification  form with given details
 * NOTE : Don't send callStatus for successfull status
 *
 * @param {Object} formDetailsObj Object containing form detail
 */
async function fillImmunizationVerificationForm(formDetailsObj) {
    // For call status except successful
    if (formDetailsObj.callStatus) {
        await dropdownListSelector(callStatusDropDown, formDetailsObj.callStatus);

        await clickOnElement(submitButton, 'Could not submit for not reachable.');
        await waitForLoading();
        return checkToaster('success');
    }

    // Answer for healthworker visited
    const healthworkerVisitedChoice = formDetailsObj.healthworkerVisited
        ? cssIdentifiers.healthWorkerVisitedYesBtn
        : cssIdentifiers.healthWorkerVisitedNoBtn;
    await clickOnElement(healthworkerVisitedChoice, 'Could not click on health worker visited choice.');

    // Answer for asked for child vaccination visited
    if (formDetailsObj.healthworkerVisited) {
        const askedChildVaccinationChoice = formDetailsObj.askedChildVaccination
            ? cssIdentifiers.askedChildVaccinationYesBtn
            : cssIdentifiers.askedChildVaccinationNoBtn;
        await clickOnElement(askedChildVaccinationChoice, 'Could not click on asked child vaccination choice.');
    }

    // Date and time of vaccination
    if (formDetailsObj.askedChildVaccination) {
        formDetailsObj.dontKnow
            ? await clickOnElement(cssIdentifiers.haventDecidedCheckbox)
            : await dateTimeSelector(formDetailsObj.dateTimeObject);
    }

    await clickOnElement(submitButton, 'Could not submit for not reachable.');
    await waitForLoading();
    return checkToaster('success');
}

/**
 * Executed one complete scenario for immunization form fillup with data verification
 *
 * @param {Object} detailsObject Details with queries, verification objects
 */
async function executeVerificationAndSubmission(detailsObject, isDelete = true) {

    // Verifying member exists
    const imtMemberData = await getQueryResult(imtMemberSelectQuery);
    expect(imtMemberData).toBeTruthy();

    // Entering record
    if (detailsObject.insertQuery) {
        await executeQuery(detailsObject.insertQuery);
    }

    await startCallVerification();

    // Verifying data before filling form
    const verifyImmunizationBeforeExecArray = await verifyDataInDb(immunizationSelectQuery, detailsObject.verifyImmunizationBeforeExec);
    console.log(`verifyImmunizationBeforeExecArray : ${verifyImmunizationBeforeExecArray}`);
    expect(verifyImmunizationBeforeExecArray.length).toEqual(0);

    await fillImmunizationVerificationForm(detailsObject.formDetailsObj);

    // Verifying info and response after form fillup
    const verifyImmunizationAfterExecArray = await verifyDataInDb(immunizationSelectQuery, detailsObject.verifyImmunizationAfterExec);
    console.log(`verifyImmunizationAfterExecArray : ${verifyImmunizationAfterExecArray}`);
    expect(verifyImmunizationAfterExecArray.length).toEqual(0);

    const verifyImmunizationResponseAfterExecArray = await verifyDataInDb(immunizationResponseQuery, detailsObject.verifyImmunizationResponseAfterExec);
    console.log(`verifyImmunizationResponseAfterExecArray : ${verifyImmunizationResponseAfterExecArray}`);
    expect(verifyImmunizationResponseAfterExecArray.length).toEqual(0);

    // Deleting record
    return isDelete ? executeQuery(deleteQuery) : true;
}

/**
 * Verifies the gvk immunization verification for 5 unsuccessful attempts
 *
 * @param {Object} immunizationObject Object having details of the form and verification object
 */
async function verifyUnsuccessfulMax(immunizationObject) {
    const modifiedOnDiffQuery = `update gvk_immunisation_verification set modified_on = (localtimestamp - interval'1 hour') where member_id = ${recordId}`;
    const modifiedScheduledDiffQuery = `update gvk_immunisation_verification set (schedule_date, modified_on) = (localtimestamp, (localtimestamp - interval '2 hour')) where member_id = ${recordId}`;
    let index = 0;

    while (index < 5) {
        console.log(`Iteration : ${index + 1}`);

        await executeVerificationAndSubmission(immunizationObject, false);

        immunizationObject.verifyImmunizationBeforeExec.call_attempt = `${index + 1}`;
        immunizationObject.verifyImmunizationAfterExec.call_attempt = `${index + 2}`;


        delete immunizationObject.insertQuery;

        if (index++ !== 4) {
            await executeQuery(modifiedScheduledDiffQuery);
        }
    }

    await executeQuery(modifiedOnDiffQuery);
    await startCallVerification();
    await browser.wait(EC.visibilityOf(element(noRecordsToVerify)), 10000, 'No records to verify message not visible.');

    return executeQuery(deleteQuery);
}
