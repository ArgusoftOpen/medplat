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
    verifyQueryResultDataWithDB,
    waitForLoading
} = require('../../../../common/utils');

const {
    gvkVerificationConstant,
} = require('../../../../common/gvk_constant')

const {
    callStatusDropDown,
    noRecordsToVerify,
    submitButton,
    fhwCallButton,
    updateBtn,
    searchBtn,
    confirmationBtn
} = require('../../../../common/cssIdentifiers');
const cssIdentifiers = require('./member.es');
const testData = require('./member.td');

module.exports = {
    createTemporaryMember,
    searchForMeberWithMemberId,
    searchForMeberWithFamilyId,
    searchForMeberWithMobileNumber,
    searchForMeberWithMobileNumberInFamily,
    searchForMeberWithOrganizationUnit,
    unsuccessfullCall
};

/**
 * Member Migration In with Create Temporary Member 001
 */
async function createTemporaryMember() {

    // insert record into migration master
    await executeQuery(testData.memberIQ);

    await startCallVerification();

    const verifyMemberInfoBeforeExec = await getQueryResult(testData.memberInfoSQ);
    const verifyMemberInfoBeforeExecArray = await verifyQueryResultDataWithDB(
        verifyMemberInfoBeforeExec,
        testData.mmi001.verifyMemberInfoBeforeExec.data);
    console.log(`verifyMemberInfoBeforeExecArray = ${verifyMemberInfoBeforeExecArray}`);
    expect(verifyMemberInfoBeforeExecArray.length).toEqual(0);

    // page title verification
    let pageTitle = element(by.binding('gvkreverification.pageTitle'));
    expect(pageTitle.getText()).toBe(testData.mmiPageTitle);


    // call 
    await clickOnElement(fhwCallButton);
    await checkToaster('error');

    // await dropdownListSelector(cssIdentifiers.resolutionDropdown, 'Create Temporary Member');

    await clickOnElement(submitButton);
    await checkToaster('success');

    const verifyMemberInfoAfterExec = await getQueryResult(`select * from migration_master where id= ${verifyMemberInfoBeforeExec.id}`);
    const verifyMemberInfoAfterExecArray = await verifyQueryResultDataWithDB(
        verifyMemberInfoAfterExec,
        testData.mmi001.verifyMemberInfoAfterExec.data);
    console.log(`verifyMemberInfoAfterExecArray = ${verifyMemberInfoAfterExecArray}`);
    expect(verifyMemberInfoAfterExecArray.length).toEqual(0);

    const callManageDetail = await getQueryResult(testData.callMasterSQ);
    const verifyCallMasterRespnseAfterExecArray = await verifyQueryResultDataWithDB(
        callManageDetail,
        testData.mmi001.callMasterResponse.data);
    console.log(`verifyCallMasterRespnseAfterExecArray = ${verifyCallMasterRespnseAfterExecArray}`);
    expect(verifyCallMasterRespnseAfterExecArray.length).toEqual(0);

    testData.mmi001.verifyMemberRespnseAfterExec.data.manage_call_master_id = callManageDetail.id;
    const verifyMemberRespnseAfterExecArray = await verifyDataInDb(
        `select * from gvk_member_migration_call_response where migration_id= ${verifyMemberInfoBeforeExec.id} order by id desc limit 1`,
        testData.mmi001.verifyMemberRespnseAfterExec.data);
    console.log(`verifyMemberRespnseAfterExecArray = ${verifyMemberRespnseAfterExecArray}`);
    expect(verifyMemberRespnseAfterExecArray.length).toEqual(0);

    const verifyMemberInfoArray = await verifyDataInDb(
        `select * from imt_member where basic_state ='TEMPORARY' and id = ${verifyMemberInfoAfterExec.member_id} order by id desc limit 1`,
        testData.mmi001.verifyMemberInfo.data);
    console.log(`verifyMemberInfoArray = ${verifyMemberInfoArray}`);
    expect(verifyMemberInfoArray.length).toEqual(0);


    // delete the record
    await executeQuery(`delete from migration_master where id= ${verifyMemberInfoBeforeExec.id};`);
    return executeQuery(`delete from gvk_member_migration_call_response where migration_id= ${verifyMemberInfoBeforeExec.id}`);
}


/**
 * Member Migration In with Search For Member with member id 002
 */
async function searchForMeberWithMemberId() {

    // insert record into migration master
    await executeQuery(testData.memberIQ);

    await startCallVerification();

    const verifyMemberInfoBeforeExec = await getQueryResult(testData.memberInfoSQ)
    const verifyMemberInfoBeforeExecArray = await verifyQueryResultDataWithDB(
        verifyMemberInfoBeforeExec,
        testData.mmi002.verifyMemberInfoBeforeExec.data);
    console.log(`verifyMemberInfoBeforeExecArray = ${verifyMemberInfoBeforeExecArray}`);
    expect(verifyMemberInfoBeforeExecArray.length).toEqual(0);

    // page title verification
    let pageTitle = element(by.binding('gvkreverification.pageTitle'));
    expect(pageTitle.getText()).toBe(testData.mmiPageTitle);


    // call 
    await clickOnElement(fhwCallButton);
    await checkToaster('error');

    await dropdownListSelector(cssIdentifiers.resolutionDropdown, 'Search For Member');

    await clickOnElement(cssIdentifiers.searchByMemberIdRadioBtn, 'search by member id is not visible');
    await element(cssIdentifiers.memberIdInput).clear().click().sendKeys('A107309139N');
    await clickOnElement(searchBtn);

    await clickOnElement(updateBtn);
    await checkToaster('error');

    await clickOnElement(cssIdentifiers.selectMemberCheckbox, 'select member checkbox is not visible')
    await clickOnElement(updateBtn);
    await clickOnElement(confirmationBtn['yes'], 'confirmation yes model is not visible');
    await checkToaster('success');

    const verifyMemberInfoAfterExec = await getQueryResult(`select * from migration_master where id= ${verifyMemberInfoBeforeExec.id}`);
    const verifyMemberInfoAfterExecArray = await verifyQueryResultDataWithDB(
        verifyMemberInfoAfterExec,
        testData.mmi002.verifyMemberInfoAfterExec.data);
    console.log(`verifyMemberInfoAfterExecArray = ${verifyMemberInfoAfterExecArray}`);
    expect(verifyMemberInfoAfterExecArray.length).toEqual(0);

    const callManageDetail = await getQueryResult(testData.callMasterSQ);
    const verifyCallMasterRespnseAfterExecArray = await verifyQueryResultDataWithDB(
        callManageDetail,
        testData.mmi002.callMasterResponse.data);
    console.log(`verifyCallMasterRespnseAfterExecArray = ${verifyCallMasterRespnseAfterExecArray}`);
    expect(verifyCallMasterRespnseAfterExecArray.length).toEqual(0);

    testData.mmi002.verifyMemberRespnseAfterExec.data.manage_call_master_id = callManageDetail.id;
    const verifyMemberRespnseAfterExecArray = await verifyDataInDb(
        `select * from gvk_member_migration_call_response where migration_id= ${verifyMemberInfoBeforeExec.id} order by id desc limit 1`,
        testData.mmi002.verifyMemberRespnseAfterExec.data);
    console.log(`verifyMemberRespnseAfterExecArray = ${verifyMemberRespnseAfterExecArray}`);
    expect(verifyMemberRespnseAfterExecArray.length).toEqual(0);

    // delete the record
    await executeQuery(`delete from migration_master where id= ${verifyMemberInfoBeforeExec.id};`);
    return executeQuery(`delete from gvk_member_migration_call_response where migration_id= ${verifyMemberInfoBeforeExec.id}`);

}

/**
 * Member Migration In with Search For Member with family id 003
 */
async function searchForMeberWithFamilyId() {

    // insert record into migration master
    await executeQuery(testData.memberIQ);

    await startCallVerification();

    const verifyMemberInfoBeforeExec = await getQueryResult(testData.memberInfoSQ)
    const verifyMemberInfoBeforeExecArray = await verifyQueryResultDataWithDB(
        verifyMemberInfoBeforeExec,
        testData.mmi003.verifyMemberInfoBeforeExec.data);
    console.log(`verifyMemberInfoBeforeExecArray = ${verifyMemberInfoBeforeExecArray}`);
    expect(verifyMemberInfoBeforeExecArray.length).toEqual(0);

    // page title verification
    let pageTitle = element(by.binding('gvkreverification.pageTitle'));
    expect(pageTitle.getText()).toBe(testData.mmiPageTitle);


    // call 
    await clickOnElement(fhwCallButton);
    await checkToaster('error');

    await dropdownListSelector(cssIdentifiers.resolutionDropdown, 'Search For Member');


    await clickOnElement(cssIdentifiers.searchByFamilyIdRadioBtn, 'search by family id is not visible');
    await element(cssIdentifiers.familyIdInput).clear().click().sendKeys('FM/2009/107328');

    await clickOnElement(searchBtn);

    await clickOnElement(updateBtn);
    await checkToaster('error');

    await clickOnElement(cssIdentifiers.selectMemberCheckbox, 'select member checkbox is not visible')
    await clickOnElement(updateBtn);
    await clickOnElement(confirmationBtn['yes'], 'confirmation yes model is not visible');
    await checkToaster('success');

    const verifyMemberInfoAfterExec = await getQueryResult(`select * from migration_master where id= ${verifyMemberInfoBeforeExec.id}`);
    const verifyMemberInfoAfterExecArray = await verifyQueryResultDataWithDB(
        verifyMemberInfoAfterExec,
        testData.mmi003.verifyMemberInfoAfterExec.data);
    console.log(`verifyMemberInfoAfterExecArray = ${verifyMemberInfoAfterExecArray}`);
    expect(verifyMemberInfoAfterExecArray.length).toEqual(0);

    const callManageDetail = await getQueryResult(testData.callMasterSQ);
    const verifyCallMasterRespnseAfterExecArray = await verifyQueryResultDataWithDB(
        callManageDetail,
        testData.mmi003.callMasterResponse.data);
    console.log(`verifyCallMasterRespnseAfterExecArray = ${verifyCallMasterRespnseAfterExecArray}`);
    expect(verifyCallMasterRespnseAfterExecArray.length).toEqual(0);

    testData.mmi003.verifyMemberRespnseAfterExec.data.manage_call_master_id = callManageDetail.id;
    const verifyMemberRespnseAfterExecArray = await verifyDataInDb(
        `select * from gvk_member_migration_call_response where migration_id= ${verifyMemberInfoBeforeExec.id} order by id desc limit 1`,
        testData.mmi003.verifyMemberRespnseAfterExec.data);
    console.log(`verifyMemberRespnseAfterExecArray = ${verifyMemberRespnseAfterExecArray}`);
    expect(verifyMemberRespnseAfterExecArray.length).toEqual(0);

    // delete the record
    await executeQuery(`delete from migration_master where id= ${verifyMemberInfoBeforeExec.id};`);
    return executeQuery(`delete from gvk_member_migration_call_response where migration_id= ${verifyMemberInfoBeforeExec.id}`);

}

/**
 * Member Migration In with Search For Member with Mobile Number 004
 */
async function searchForMeberWithMobileNumber() {

    // insert record into migration master
    await executeQuery(testData.memberIQ);

    await startCallVerification();

    const verifyMemberInfoBeforeExec = await getQueryResult(testData.memberInfoSQ)
    const verifyMemberInfoBeforeExecArray = await verifyQueryResultDataWithDB(
        verifyMemberInfoBeforeExec,
        testData.mmi004.verifyMemberInfoBeforeExec.data);
    console.log(`verifyMemberInfoBeforeExecArray = ${verifyMemberInfoBeforeExecArray}`);
    expect(verifyMemberInfoBeforeExecArray.length).toEqual(0);

    // page title verification
    let pageTitle = element(by.binding('gvkreverification.pageTitle'));
    expect(pageTitle.getText()).toBe(testData.mmiPageTitle);


    // call 
    await clickOnElement(fhwCallButton);
    await checkToaster('error');

    await dropdownListSelector(cssIdentifiers.resolutionDropdown, 'Search For Member');


    await clickOnElement(cssIdentifiers.searchByMobileNumberRadioBtn, 'search by mobile number is not visible');
    await element(cssIdentifiers.mobileNumberInput).clear().click().sendKeys('9537931751');

    await clickOnElement(searchBtn);

    await clickOnElement(updateBtn);
    await checkToaster('error');

    await clickOnElement(cssIdentifiers.selectMemberCheckbox, 'select member checkbox is not visible')
    await clickOnElement(updateBtn);
    await clickOnElement(confirmationBtn['yes'], 'confirmation yes model is not visible');
    await checkToaster('success');

    const verifyMemberInfoAfterExec = await getQueryResult(`select * from migration_master where id= ${verifyMemberInfoBeforeExec.id}`);
    const verifyMemberInfoAfterExecArray = await verifyQueryResultDataWithDB(
        verifyMemberInfoAfterExec,
        testData.mmi004.verifyMemberInfoAfterExec.data);
    console.log(`verifyMemberInfoAfterExecArray = ${verifyMemberInfoAfterExecArray}`);
    expect(verifyMemberInfoAfterExecArray.length).toEqual(0);

    const callManageDetail = await getQueryResult(testData.callMasterSQ);
    const verifyCallMasterRespnseAfterExecArray = await verifyQueryResultDataWithDB(
        callManageDetail,
        testData.mmi004.callMasterResponse.data);
    console.log(`verifyCallMasterRespnseAfterExecArray = ${verifyCallMasterRespnseAfterExecArray}`);
    expect(verifyCallMasterRespnseAfterExecArray.length).toEqual(0);

    testData.mmi004.verifyMemberRespnseAfterExec.data.manage_call_master_id = callManageDetail.id;
    const verifyMemberRespnseAfterExecArray = await verifyDataInDb(
        `select * from gvk_member_migration_call_response where migration_id= ${verifyMemberInfoBeforeExec.id} order by id desc limit 1`,
        testData.mmi004.verifyMemberRespnseAfterExec.data);
    console.log(`verifyMemberRespnseAfterExecArray = ${verifyMemberRespnseAfterExecArray}`);
    expect(verifyMemberRespnseAfterExecArray.length).toEqual(0);

    // delete the record
    await executeQuery(`delete from migration_master where id= ${verifyMemberInfoBeforeExec.id};`);
    return executeQuery(`delete from gvk_member_migration_call_response where migration_id= ${verifyMemberInfoBeforeExec.id}`);

}

/**
 * Member Migration In with Search For Member with Mobile Number search in family 005
 */
async function searchForMeberWithMobileNumberInFamily() {

    // insert record into migration master
    await executeQuery(testData.memberIQ);

    await startCallVerification();

    const verifyMemberInfoBeforeExec = await getQueryResult(testData.memberInfoSQ)
    const verifyMemberInfoBeforeExecArray = await verifyQueryResultDataWithDB(
        verifyMemberInfoBeforeExec,
        testData.mmi004.verifyMemberInfoBeforeExec.data);
    console.log(`verifyMemberInfoBeforeExecArray = ${verifyMemberInfoBeforeExecArray}`);
    expect(verifyMemberInfoBeforeExecArray.length).toEqual(0);

    // page title verification
    let pageTitle = element(by.binding('gvkreverification.pageTitle'));
    expect(pageTitle.getText()).toBe(testData.mmiPageTitle);


    // call 
    await clickOnElement(fhwCallButton);
    await checkToaster('error');

    await dropdownListSelector(cssIdentifiers.resolutionDropdown, 'Search For Member');


    await clickOnElement(cssIdentifiers.searchByMobileNumberRadioBtn, 'search by mobile number is not visible');
    await element(cssIdentifiers.mobileNumberInput).clear().click().sendKeys('9537931751');
    await clickOnElement(cssIdentifiers.searchMobileNumberInFamilyCheckbox, 'mobile number search in family is not visible');


    await clickOnElement(searchBtn);

    await clickOnElement(updateBtn);
    await checkToaster('error');

    await clickOnElement(cssIdentifiers.selectMemberCheckbox, 'select member checkbox is not visible')
    await clickOnElement(updateBtn);
    await clickOnElement(confirmationBtn['yes'], 'confirmation yes model is not visible');
    await checkToaster('success');

    const verifyMemberInfoAfterExec = await getQueryResult(`select * from migration_master where id= ${verifyMemberInfoBeforeExec.id}`);
    const verifyMemberInfoAfterExecArray = await verifyQueryResultDataWithDB(
        verifyMemberInfoAfterExec,
        testData.mmi004.verifyMemberInfoAfterExec.data);
    console.log(`verifyMemberInfoAfterExecArray = ${verifyMemberInfoAfterExecArray}`);
    expect(verifyMemberInfoAfterExecArray.length).toEqual(0);

    const callManageDetail = await getQueryResult(testData.callMasterSQ);
    const verifyCallMasterRespnseAfterExecArray = await verifyQueryResultDataWithDB(
        callManageDetail,
        testData.mmi004.callMasterResponse.data);
    console.log(`verifyCallMasterRespnseAfterExecArray = ${verifyCallMasterRespnseAfterExecArray}`);
    expect(verifyCallMasterRespnseAfterExecArray.length).toEqual(0);

    testData.mmi004.verifyMemberRespnseAfterExec.data.manage_call_master_id = callManageDetail.id;
    const verifyMemberRespnseAfterExecArray = await verifyDataInDb(
        `select * from gvk_member_migration_call_response where migration_id= ${verifyMemberInfoBeforeExec.id} order by id desc limit 1`,
        testData.mmi004.verifyMemberRespnseAfterExec.data);
    console.log(`verifyMemberRespnseAfterExecArray = ${verifyMemberRespnseAfterExecArray}`);
    expect(verifyMemberRespnseAfterExecArray.length).toEqual(0);

    // delete the record
    await executeQuery(`delete from migration_master where id= ${verifyMemberInfoBeforeExec.id};`);
    return executeQuery(`delete from gvk_member_migration_call_response where migration_id= ${verifyMemberInfoBeforeExec.id}`);

}

/**
 * Member Migration In with Search For Member with Organization Unit 006
 */
async function searchForMeberWithOrganizationUnit() {

    // insert record into migration master
    await executeQuery(testData.memberIQ);

    await startCallVerification();

    const verifyMemberInfoBeforeExec = await getQueryResult(testData.memberInfoSQ)
    const verifyMemberInfoBeforeExecArray = await verifyQueryResultDataWithDB(
        verifyMemberInfoBeforeExec,
        testData.mmi006.verifyMemberInfoBeforeExec.data);
    console.log(`verifyMemberInfoBeforeExecArray = ${verifyMemberInfoBeforeExecArray}`);
    expect(verifyMemberInfoBeforeExecArray.length).toEqual(0);

    // page title verification
    let pageTitle = element(by.binding('gvkreverification.pageTitle'));
    expect(pageTitle.getText()).toBe(testData.mmiPageTitle);


    // call 
    await clickOnElement(fhwCallButton);
    await checkToaster('error');

    await dropdownListSelector(cssIdentifiers.resolutionDropdown, 'Search For Member');

    await clickOnElement(cssIdentifiers.searchByOrgUnitRadioBtn, 'search by orgunit is not visible');

    for (const location in testData.mmi006.updateLocation) {
        await dropdownListSelector(cssIdentifiers.locationSelector[location], testData.mmi006.updateLocation[location]);
    }


    await clickOnElement(searchBtn);
    await waitForLoading();

    await clickOnElement(updateBtn);
    await checkToaster('error');

    await clickOnElement(cssIdentifiers.selectMemberCheckbox, 'select member checkbox is not visible')
    await clickOnElement(updateBtn);
    await clickOnElement(confirmationBtn['yes'], 'confirmation yes model is not visible');
    await checkToaster('success');

    const verifyMemberInfoAfterExec = await getQueryResult(`select * from migration_master where id= ${verifyMemberInfoBeforeExec.id}`);
    const verifyMemberInfoAfterExecArray = await verifyQueryResultDataWithDB(
        verifyMemberInfoAfterExec,
        testData.mmi006.verifyMemberInfoAfterExec.data);
    console.log(`verifyMemberInfoAfterExecArray = ${verifyMemberInfoAfterExecArray}`);
    expect(verifyMemberInfoAfterExecArray.length).toEqual(0);

    const callManageDetail = await getQueryResult(testData.callMasterSQ);
    const verifyCallMasterRespnseAfterExecArray = await verifyQueryResultDataWithDB(
        callManageDetail,
        testData.mmi006.callMasterResponse.data);
    console.log(`verifyCallMasterRespnseAfterExecArray = ${verifyCallMasterRespnseAfterExecArray}`);
    expect(verifyCallMasterRespnseAfterExecArray.length).toEqual(0);

    testData.mmi006.verifyMemberRespnseAfterExec.data.manage_call_master_id = callManageDetail.id;
    const verifyMemberRespnseAfterExecArray = await verifyDataInDb(
        `select * from gvk_member_migration_call_response where migration_id= ${verifyMemberInfoBeforeExec.id} order by id desc limit 1`,
        testData.mmi006.verifyMemberRespnseAfterExec.data);
    console.log(`verifyMemberRespnseAfterExecArray = ${verifyMemberRespnseAfterExecArray}`);
    expect(verifyMemberRespnseAfterExecArray.length).toEqual(0);

    // delete the record
    await executeQuery(`delete from migration_master where id= ${verifyMemberInfoBeforeExec.id};`);
    return executeQuery(`delete from gvk_member_migration_call_response where migration_id= ${verifyMemberInfoBeforeExec.id}`);

}

/**
 * Member Migration In without successfull call 007
 */
async function unsuccessfullCall() {

    // insert record into migration master
    await executeQuery(testData.memberIQ);

    let index = 0;
    let verifyMemberInfoBeforeExec;
    while (index < 3) {

        console.log(`Verification iteration : ${index + 1}`);

        await startCallVerification();

        verifyMemberInfoBeforeExec = await getQueryResult(testData.memberInfoSQ);
        const verifyMemberInfoBeforeExecArray = await verifyQueryResultDataWithDB(
            verifyMemberInfoBeforeExec,
            testData.mmi007.verifyMemberInfoBeforeExec.data);
        console.log(`verifyMemberInfoBeforeExecArray = ${verifyMemberInfoBeforeExecArray}`);
        expect(verifyMemberInfoBeforeExecArray.length).toEqual(0);

        // page title verification
        let pageTitle = element(by.binding('gvkreverification.pageTitle'));
        expect(pageTitle.getText()).toBe(testData.mmiPageTitle);


        // call 
        await clickOnElement(fhwCallButton);
        await checkToaster('error');

        await dropdownListSelector(callStatusDropDown, 'Not Reachable');

        await clickOnElement(submitButton);
        await checkToaster('success');

        testData.mmi007.verifyMemberInfoBeforeExec.data.call_attempt = `${index + 1}`;
        testData.mmi007.verifyMemberInfoAfterExec.data.call_attempt = `${index + 1}`;

        const verifyMemberInfoAfterExec = await getQueryResult(`select * from migration_master where id= ${verifyMemberInfoBeforeExec.id}`);
        const verifyMemberInfoAfterExecArray = await verifyQueryResultDataWithDB(
            verifyMemberInfoAfterExec,
            testData.mmi007.verifyMemberInfoAfterExec.data);
        console.log(`verifyMemberInfoAfterExecArray = ${verifyMemberInfoAfterExecArray}`);
        expect(verifyMemberInfoAfterExecArray.length).toEqual(0);

        const callManageDetail = await getQueryResult(testData.callMasterSQ);
        const verifyCallMasterRespnseAfterExecArray = await verifyQueryResultDataWithDB(
            callManageDetail,
            testData.mmi007.callMasterResponse.data);
        console.log(`verifyCallMasterRespnseAfterExecArray = ${verifyCallMasterRespnseAfterExecArray}`);
        expect(verifyCallMasterRespnseAfterExecArray.length).toEqual(0);

        testData.mmi007.verifyMemberRespnseAfterExec.data.manage_call_master_id = callManageDetail.id;
        const verifyMemberRespnseAfterExecArray = await verifyDataInDb(
            `select * from gvk_member_migration_call_response where migration_id= ${verifyMemberInfoBeforeExec.id} order by id desc limit 1`,
            testData.mmi007.verifyMemberRespnseAfterExec.data);
        console.log(`verifyMemberRespnseAfterExecArray = ${verifyMemberRespnseAfterExecArray}`);
        expect(verifyMemberRespnseAfterExecArray.length).toEqual(0);

        await executeQuery(`update migration_master 
        set modified_on=(localtimestamp - interval '2 hour'), schedule_date=(localtimestamp - interval '2 hour') 
        where id= ${verifyMemberInfoBeforeExec.id}`);
        index++;
    }

    await startCallVerification();
    await browser.wait(EC.visibilityOf(element(noRecordsToVerify)), 10000, 'Next record to verify not visible.');

    // delete the record
    await executeQuery(`delete from migration_master where id= ${verifyMemberInfoBeforeExec.id};`);
    return executeQuery(`delete from gvk_member_migration_call_response where migration_id= ${verifyMemberInfoBeforeExec.id}`);
}
