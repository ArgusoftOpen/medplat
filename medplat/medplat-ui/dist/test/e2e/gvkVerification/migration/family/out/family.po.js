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
    familyState
} = require('../../../../common/gvk_constant')

const {
    callStatusDropDown,
    noRecordsToVerify,
    submitButton,
    fhwCallButton
} = require('../../../../common/cssIdentifiers');
const cssIdentifiers = require('./family.es');
const {
    familyId,
    recordId,
    familyMigrationPageTitle,
} = require('./family.td');

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
        await clickOnElement(fhwCallButton);
        await checkToaster('error');
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
 * Executed one complete scenario for family migration out form fillup with data verification
 *
 * @param {Object} detailsObject Details with queries, verification objects
 */
async function executeVerificationAndSubmission(detailsObject, isDelete = true) {
    // Deleting record
    if (isDelete) {
        await executeQuery(detailsObject.deleteQuery.gvkFamilyInfoDQ);
        await executeQuery(detailsObject.deleteQuery.gvkFamilyResDQ);
        
        await executeQuery(detailsObject.deleteQuery.familyMigrationMasterDQ);
    }

    // Verifying Family exists
    if (detailsObject.imtFamilyVerifictionSQ) {
        const imtFamilyVerifiction = await getQueryResult(detailsObject.imtFamilyVerifictionSQ);
        expect(imtFamilyVerifiction).toBeTruthy();
    }

    // Entering record
    if (detailsObject.insertQuery) {
        await executeQuery(detailsObject.insertQuery);
    }

    // update the record
    if (detailsObject.updateQuery) {
        await executeQuery(detailsObject.updateQuery);
    }

    await startCallVerification();

    let pageTitle = element(by.binding('gvkreverification.pageTitle'));
    expect(pageTitle.getText()).toBe(familyMigrationPageTitle);

    const familyMigrationInfo = await getQueryResult(`select id from imt_family_migration_master where family_id = ${recordId}`);
    detailsObject.verifyBeforeExec.gvkFamilyInfoDetail.data.family_migration_id = familyMigrationInfo.id;

    let current_state;
    if (detailsObject.formDetailsObj.resolution == 'Roll Back') {
        const familyDetailInfo = await getQueryResult(`select * from imt_member where family_id = '${familyId}'`);
        current_state = familyDetailInfo.current_state;
    }

    // Verifying data before filling form
    await _verifyAllDetails(detailsObject.verifyBeforeExec);

    await fillMigrationOutForm(detailsObject.formDetailsObj);

    const callManageDetail = await getQueryResult(`select * from gvk_manage_call_master where family_id= '${familyId}' order by id desc limit 1`);


    detailsObject.verifyAfterExec.gvkFamilyResponse.data.manage_call_master_id = callManageDetail.id;
    detailsObject.verifyAfterExec.callMasterResponse.data.id = callManageDetail.id;

    if (detailsObject.formDetailsObj.resolution == 'Update Location') {
        const notificationInfo = await getQueryResult(`select * from notification_type_master where code='FMI'`);
        detailsObject.verifyAfterExec.notification.data.notification_type_id = notificationInfo.id;
        detailsObject.verifyAfterExec.notification.data.migration_id = familyMigrationInfo.id;
    }

    if (detailsObject.formDetailsObj.resolution == 'Roll Back') {

        const stateInfo = await getQueryResult(`select * from imt_family_state_detail where family_id = '${familyId}' and id = ${current_state} ;`);
        detailsObject.verifyAfterExec.familyDetailedInfo.state = stateInfo.from_state;

        const notificationInfo = await getQueryResult(`select id from notification_type_master where code='READ_ONLY'`);
        detailsObject.verifyAfterExec.notification.data.notification_type_id = notificationInfo.id;
        detailsObject.verifyAfterExec.notification.data.migration_id = familyMigrationInfo.id;
    }

    // Verifying info after form fillup
    await _verifyAllDetails(detailsObject.verifyAfterExec);

    if (detailsObject.formDetailsObj.resolution == 'Roll Back') {
        await executeQuery(detailsObject.familyStateUQ);

        const familyInfo = await getQueryResult(detailsObject.verifyAfterExec.familyDetailedInfo.query);
        expect(familyInfo.state).toBe(familyState.migrated);
    }

    // Deleting record
    if (isDelete) {
        await executeQuery(detailsObject.deleteQuery.gvkFamilyInfoDQ);
        await executeQuery(detailsObject.deleteQuery.gvkFamilyResDQ);
        
        await executeQuery(detailsObject.deleteQuery.familyMigrationMasterDQ);
    }

    return true;
}

/**
 * Verifies not resolved after 2 unsuccessful attemps
 *
 * @param {Object} detailsObject Details with queries, verification objects
 */
async function notResolvedAfterUnsuccessfulAttempt(detailsObject) {

    await executeQuery(detailsObject.deleteQuery.gvkFamilyInfoDQ);
    await executeQuery(detailsObject.deleteQuery.gvkFamilyResDQ);
    
    await executeQuery(detailsObject.deleteQuery.familyMigrationMasterDQ);

    let index = 0;
    while (index < 3) {
        console.log(`Verification iteration : ${index + 1}`);

        await executeVerificationAndSubmission(detailsObject, false);

        delete detailsObject.insertQuery;
        delete detailsObject.imtFamilyVerifictionSQ;


        // await executeQuery(familyMigrationUQ);
        detailsObject.verifyBeforeExec.gvkFamilyInfoDetail.data.call_attempt = `${index + 1}`;
        detailsObject.verifyAfterExec.gvkFamilyInfoDetail.data.call_attempt = `${index + 2}`;


        if (index++ === 1) {
            detailsObject.formDetailsObj = {
                resolution: 'Not Yet Resolved'
            };

            delete detailsObject.verifyAfterExec.callMasterResponse.data.call_response;


            detailsObject.verifyAfterExec.callMasterResponse.data.call_response = gvkVerificationConstant.callSuccess;
            detailsObject.verifyAfterExec.familyMigrationMasterDetails.data.state = 'NOT_RESOLVED';
            detailsObject.verifyAfterExec.gvkFamilyInfoDetail.data.gvk_call_status = gvkVerificationConstant.callProcessing;
            detailsObject.verifyAfterExec.gvkFamilyResponse.data.performed_action = 'NOT_RESOLVED';
            detailsObject.verifyAfterExec.gvkFamilyResponse.data.gvk_call_response_status = gvkVerificationConstant.callSuccess;
        }
    }

    if (detailsObject.updateQuery) {
        await executeQuery(detailsObject.updateQuery);
    }

    await startCallVerification();
    await browser.wait(EC.visibilityOf(element(by.binding('gvkreverification.pageTitle'))), 10000, 'No record is visible.');

    // delete the record
    await executeQuery(detailsObject.deleteQuery.gvkFamilyInfoDQ);
    await executeQuery(detailsObject.deleteQuery.gvkFamilyResDQ);
    
    return executeQuery(detailsObject.deleteQuery.familyMigrationMasterDQ);
}

/**
 * Verifies scenario for unsuccessful call
 *
 * @param {Object} detailsObject Object with all details
 */
async function executeVerificationUnsuccessful(detailsObject) {

    await executeQuery(detailsObject.deleteQuery.gvkFamilyInfoDQ);
    await executeQuery(detailsObject.deleteQuery.gvkFamilyResDQ);
    await executeQuery(detailsObject.deleteQuery.familyMigrationMasterDQ);

    let index = 0;
    while (index < 3) {
        console.log(`Verification iteration : ${++index}`);

        await executeVerificationAndSubmission(detailsObject, false);

        delete detailsObject.insertQuery;
        delete detailsObject.imtFamilyVerifictionSQ;

        detailsObject.verifyBeforeExec.gvkFamilyInfoDetail.data.call_attempt = `${index}`;
        detailsObject.verifyAfterExec.gvkFamilyInfoDetail.data.call_attempt = `${index + 1}`;
    }

    await startCallVerification();
    await browser.wait(EC.visibilityOf(element(noRecordsToVerify)), 10000, 'No records to verify message not visible.');

    await executeQuery(detailsObject.deleteQuery.gvkFamilyInfoDQ);
    await executeQuery(detailsObject.deleteQuery.gvkFamilyResDQ);
    
    return executeQuery(detailsObject.deleteQuery.familyMigrationMasterDQ);
}
