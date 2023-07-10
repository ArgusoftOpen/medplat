/* eslint-disable require-atomic-updates,camelcase */
const EC = protractor.ExpectedConditions;
const {
    checkToaster,
    clickOnElement,
    dropdownListSelector,
    executeQuery,
    getQueryResult,
    startCallVerification,
    verifyDataInDb,
    waitForLoading
} = require('../../../../common/utils');

const {
    gvkVerificationConstant,
} = require('../../../../common/gvk_constant')

const {
    callStatusDropDown,
    noRecordsToVerify,
    submitButton,
    fhwCallButton
} = require('../../../../common/cssIdentifiers');
const cssIdentifiers = require('./member.es');
const {
    imtMemberSQ,
    migrationMasterUQ
} = require('./member.td');

module.exports = {
    executeVerificationAndSubmission,
    executeVerificationUnsuccessful,
    fillMigrationOutForm,
    notResolvedAfterUnsuccessfulAttempt
};

/**
 * @typedef updateLocation
 * @type {Object}
 * @property {String} state State to select
 * @property {String} region Region to select
 * @property {String} district District to select
 * @property {String} block Block to select
 * @property {String} phc Phc Center to select
 * @property {String} subCenter Sub Center to select
 * @property {String} village Village to select
 * @property {String} ashaArea Asha area to select
 */
/**
 * Fills form for member migration out
 *
 * @param {Object} formDetailsObj Object containing form details
 * @param {String} formDetailsObj.resolution Resolution type for member record
 * @param {Boolean=} formDetailsObj.markOutOfState To mark out of state for `Mark as LFU`
 * @param {Object=} formDetailsObj.updateLocation Object containing location details
 * @param {String=} formDetailsObj.callStatus Call status for type unsuccessful call
 */
async function fillMigrationOutForm(formDetailsObj) {
    // For call status except successful
    if (formDetailsObj.callStatus) {
        await dropdownListSelector(callStatusDropDown, formDetailsObj.callStatus);
    } else {
        await clickOnElement(fhwCallButton);
        await checkToaster('error');

        await dropdownListSelector(cssIdentifiers.resolutionDropdown, formDetailsObj.resolution);

        if (formDetailsObj.markOutOfState) {
            await clickOnElement(cssIdentifiers.markOutOfStateCheckbox);
        }

        if (formDetailsObj.updateLocation) {
            for (const location in formDetailsObj.updateLocation) {
                await dropdownListSelector(cssIdentifiers.locationSelector[location], formDetailsObj.updateLocation[location]);
            }
        }
    }

    await clickOnElement(submitButton);
    await waitForLoading();
    return formDetailsObj.toCheckToaster ? checkToaster('success') : true;
}

/**
 * Helper function for verifying records
 *
 * @param {Object} verificationObjects Object of verification objects
 */
async function _verifyAllDetails(verificationObjects) {
    console.log('\n-------x-------');

    for (const verificationObjName in verificationObjects) {
        const verificationResultArray = await verifyDataInDb(verificationObjects[verificationObjName].query, verificationObjects[verificationObjName].data, verificationObjects[verificationObjName].isMulti);
        console.log(`${verificationObjName} : ${verificationResultArray}`);
        expect(verificationResultArray.length).toEqual(0);
    }

    console.log('-------x-------\n');
    return true;
}

/**
 * Executed one complete scenario for member migration out form fillup with data verification
 *
 * @param {Object} detailsObject Details with queries, verification objects
 */
async function executeVerificationAndSubmission(detailsObject, isDelete = true) {
    // Verifying member exists
    if (detailsObject.imtMemberSQ) {
        const imtMemberData = await getQueryResult(imtMemberSQ);
        expect(imtMemberData).toBeTruthy();
    }

    // Entering record
    if (detailsObject.insertQuery) {
        await executeQuery(detailsObject.insertQuery);
    }

    await startCallVerification();

    // Verifying data before filling form
    await _verifyAllDetails(detailsObject.verifyBeforeExec);

    await fillMigrationOutForm(detailsObject.formDetailsObj);

    // Verifying info after form fillup
    await _verifyAllDetails(detailsObject.verifyAfterExec);

    // Deleting record
    if (isDelete) {
        await executeQuery(detailsObject.deleteQuery);
    }

    return true;
}

/**
 * Verifies not resolved after 2 unsuccessful attemps
 *
 * @param {Object} detailsObject Details with queries, verification objects
 */
async function notResolvedAfterUnsuccessfulAttempt(detailsObject) {
    let index = 0;
    while (index < 3) {
        console.log(`Verification iteration : ${index + 1}`);

        await executeVerificationAndSubmission(detailsObject, false);

        delete detailsObject.insertQuery;
        delete detailsObject.verifyMemberExist;

        await executeQuery(migrationMasterUQ);
        detailsObject.verifyBeforeExec.migrationDetails.data.call_attempt = `${index + 1}`;
        detailsObject.verifyAfterExec.migrationDetails.data.call_attempt = `${index + 2}`;

        if (index++ === 1) {
            detailsObject.formDetailsObj = {
                resolution: 'Not Yet Resolved'
            };
            detailsObject.verifyAfterExec.migrationDetails.data.gvk_call_status = gvkVerificationConstant.callProcessing;
            detailsObject.verifyAfterExec.migrationDetails.data.state = 'NOT_RESOLVED';
            detailsObject.verifyAfterExec.migrationResponse.data.performed_action = 'NOT_RESOLVED';
            detailsObject.verifyAfterExec.migrationResponse.data.gvk_call_response_status = gvkVerificationConstant.callSuccess;
        }
    }

    await startCallVerification();
    await browser.wait(EC.visibilityOf(element(cssIdentifiers.migrationOutResolution)), 10000, 'Next record to verify not visible.');

    return executeQuery(detailsObject.deleteQuery);
}

/**
 * Verifies scenario for unsuccessful call
 *
 * @param {Object} detailsObject Object with all details
 */
async function executeVerificationUnsuccessful(detailsObject) {
    let index = 0;
    while (index < 3) {
        console.log(`Verification iteration : ${++index}`);

        await executeVerificationAndSubmission(detailsObject, false);

        delete detailsObject.insertQuery;
        delete detailsObject.verifyMemberExist;

        await executeQuery(migrationMasterUQ);

        detailsObject.verifyBeforeExec.migrationDetails.data.call_attempt = `${index}`;
        detailsObject.verifyAfterExec.migrationDetails.data.call_attempt = `${index + 1}`;
    }

    await startCallVerification();
    await browser.wait(EC.visibilityOf(element(noRecordsToVerify)), 10000, 'No records to verify message not visible.');

    return executeQuery(detailsObject.deleteQuery);
}
