const {
    gvkVerificationConstant
} = require('../../common/gvk_constant')

/* eslint-disable camelcase */
const recordId = 100187236;
const imtMemberSelectQuery = `select * from imt_member where (basic_state ='VERIFIED' or basic_state='NEW') and id = ${recordId}`;
const immunizationSelectQuery = `select * from gvk_immunisation_verification where member_id= ${recordId} order by id desc limit 1`;
const immunizationResponseQuery = `select * from gvk_immunisation_verification_response where member_id = ${recordId} order by id desc limit 1`;
const deleteQuery = `delete from gvk_immunisation_verification where member_id = ${recordId};delete from gvk_immunisation_verification_response where member_id = ${recordId}`;

const immunization001 = {
    insertQuery: `INSERT INTO public.gvk_immunisation_verification(
        member_id, gvk_state, schedule_date, call_attempt, created_by,
        created_on, modified_by, modified_on, location_id)
        VALUES (${recordId}, '${gvkVerificationConstant.callToBeProccessed}', (select localtimestamp - interval '1 day'), 0, -1,
        (select localtimestamp - interval '1 day'), -1,(select localtimestamp - interval '1 day'), 541260)`,
    verifyImmunizationBeforeExec: {
        gvk_state: gvkVerificationConstant.callProcessing,
        call_attempt: '0'
    },
    formDetailsObj: {
        healthworkerVisited: false
    },
    verifyImmunizationAfterExec: {
        gvk_state: gvkVerificationConstant.callProcessed,
        schedule_date: 'now',
        modified_on: 'now',
        call_attempt: '1'
    },
    verifyImmunizationResponseAfterExec: {
        gvk_call_status: gvkVerificationConstant.callSuccess,
        healthworker_visited: 'false',
        asked_child_vaccination: '',
        date_asked_to_come: '',
        date_known_checkbox: ''
    }
};

const immunization002 = JSON.parse(JSON.stringify(immunization001));
immunization002.formDetailsObj = {
    healthworkerVisited: true,
    askedChildVaccination: false
};
immunization002.verifyImmunizationResponseAfterExec.healthworker_visited = 'true';
immunization002.verifyImmunizationResponseAfterExec.asked_child_vaccination = 'false';

const immunization003 = JSON.parse(JSON.stringify(immunization002));
immunization003.formDetailsObj = {
    healthworkerVisited: true,
    askedChildVaccination: true,
    dateTimeObject: {}
};
immunization003.verifyImmunizationResponseAfterExec.asked_child_vaccination = 'true';
immunization003.verifyImmunizationResponseAfterExec.date_asked_to_come = 'now';

const immunization004 = JSON.parse(JSON.stringify(immunization003));
immunization004.formDetailsObj.dontKnow = true;
immunization004.verifyImmunizationResponseAfterExec.date_asked_to_come = '';
immunization004.verifyImmunizationResponseAfterExec.date_known_checkbox = 'true';

const immunization005 = JSON.parse(JSON.stringify(immunization001));
immunization005.formDetailsObj = {
    callStatus: 'Not Reachable'
};
immunization005.verifyImmunizationResponseAfterExec.gvk_call_status = gvkVerificationConstant.callNotReachable;
immunization005.verifyImmunizationAfterExec.gvk_state = gvkVerificationConstant.callToBeProccessed;
immunization005.verifyImmunizationResponseAfterExec.healthworker_visited = '';

module.exports = {
    recordId,

    // Queries
    deleteQuery,
    immunizationResponseQuery,
    immunizationSelectQuery,
    imtMemberSelectQuery,

    // Data Objects
    immunization001,
    immunization002,
    immunization003,
    immunization004,
    immunization005
};
