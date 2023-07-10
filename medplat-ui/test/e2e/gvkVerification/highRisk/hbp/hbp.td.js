const {
    getDateAfterNumberOfDaysHoursMinutes
} = require('../../../common/utils');

const {
    gvkVerificationConstant
} = require('../../../common/gvk_constant')

/* eslint-disable camelcase */
const hbp001 = {
    verifyMemberInfoBeforeExec: {
        modified_on: 'now',
        gvk_call_state: gvkVerificationConstant.pickedupSchedule,
        gvk_call_status: gvkVerificationConstant.callProcessing,
        treatment_performed_info: '',
    },
    verifyMemberInfoAfterExec: {
        gvk_call_previous_state: gvkVerificationConstant.pickedupSchedule,
        gvk_call_state: gvkVerificationConstant.beneficiaryPickedupScheduleForNextVisit ,
        treatment_performed_info: 'લાભાર્થીને હાઇ બીપી માટેની દવાઓ મળી ગઈ છે.',
        gvk_call_status: gvkVerificationConstant.callProcessing,
        schedule_date: getDateAfterNumberOfDaysHoursMinutes(18, 0, 0),
        pickup_schedule_date: getDateAfterNumberOfDaysHoursMinutes(17, 10, 10),
    },
    verifyResponseAfterExec: {
        is_108_pickedup_beneficiary : 'true',
        is_beneficiary_visited_phc : 'true',
        is_beneficiary_received_drugs_for_high_bp : 'true',
        is_beneficiary_droped_at_home_by_108 : 'true',
        is_beneficiary_pickedup_date_for_next_schedule : 'true',
    }
};



module.exports = {
    hbp001,
};
