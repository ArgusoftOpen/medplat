const {
    getDateAfterNumberOfDaysHoursMinutes
} = require('../../../common/utils');

const {
    gvkVerificationConstant
} = require('../../../common/gvk_constant')

/* eslint-disable camelcase */
const lbw001 = {
    verifyMemberInfoBeforeExec: {
        modified_on: 'now',
        gvk_call_state: gvkVerificationConstant.pickedupSchedule,
        gvk_call_status: gvkVerificationConstant.callProcessing,
        treatment_performed_info: '',
    },
    verifyMemberInfoAfterExec: {
        gvk_call_previous_state: gvkVerificationConstant.pickedupSchedule,
        gvk_call_state: gvkVerificationConstant.childAdmittedToCMTC,
        treatment_performed_info: 'લાભાર્થી બાળકને ઓછા વજનને કારણે દવાખાનામાં સારવાર માટે દાખલ કરેલ છે.',
        gvk_call_status: gvkVerificationConstant.callProcessed
    },
    verifyResponseAfterExec: {
        is_108_pickedup_beneficiary : 'true',
        is_beneficiary_visited_phc : 'true',
        is_new_birth_child_admitted_to_hospital_for_low_birth_weight : 'true',
        is_beneficiary_droped_at_home_by_108 : 'true',
    }
};

const lbw002 = {
    verifyMemberInfoBeforeExec: {
        modified_on: 'now',
        gvk_call_state: gvkVerificationConstant.pickedupSchedule,
        gvk_call_status: gvkVerificationConstant.callProcessing,
        treatment_performed_info: '',
    },
    verifyMemberInfoAfterExec: {
        gvk_call_previous_state: gvkVerificationConstant.pickedupSchedule,
        gvk_call_state: gvkVerificationConstant.beneficiaryPickedupScheduleForNextVisit,
        treatment_performed_info: '',
        gvk_call_status: gvkVerificationConstant.callProcessing,
        schedule_date: getDateAfterNumberOfDaysHoursMinutes(17, 0, 0),
        pickup_schedule_date: getDateAfterNumberOfDaysHoursMinutes(16, 10, 10),
    },
    verifyResponseAfterExec: {
        is_108_pickedup_beneficiary : 'true',
        is_beneficiary_visited_phc : 'true',
        is_new_birth_child_admitted_to_hospital_for_low_birth_weight : 'false',
        is_beneficiary_droped_at_home_by : undefined,
        is_beneficiary_pickedup_date_for_next_schedule : 'true',
    }
};

const lbw003 = {
    verifyMemberInfoBeforeExec: {
        modified_on: 'now',
        gvk_call_state: gvkVerificationConstant.pickedupSchedule,
        gvk_call_status: gvkVerificationConstant.callProcessing,
        treatment_performed_info: '',
    },
    verifyMemberInfoAfterExec: {
        gvk_call_previous_state: gvkVerificationConstant.pickedupSchedule,
        gvk_call_state: gvkVerificationConstant.schedulePendingForNextVisit,
        treatment_performed_info: '',
        gvk_call_status: gvkVerificationConstant.callProcessing,
        schedule_date : getDateAfterNumberOfDaysHoursMinutes(14, 0, 0),
    },
    verifyResponseAfterExec: {
        is_108_pickedup_beneficiary : 'true',
        is_beneficiary_visited_phc : 'true',
        is_new_birth_child_admitted_to_hospital_for_low_birth_weight : 'false',
        is_beneficiary_pickedup_date_for_next_schedule : 'false',
    },
    afterUpdateVerifyMemberInfoAfterExec: {
        gvk_call_previous_state: gvkVerificationConstant.schedulePendingForNextVisit,
        gvk_call_state: gvkVerificationConstant.pickedupSchedule,
        gvk_call_status: gvkVerificationConstant.callProcessing,
        schedule_date: getDateAfterNumberOfDaysHoursMinutes(4, 0, 0),
        pickup_schedule_date: getDateAfterNumberOfDaysHoursMinutes(3, 10, 40),
    },
};

module.exports = {
    lbw001,
    lbw002,
    lbw003,
    
};
