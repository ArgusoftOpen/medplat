const {
    getDateAfterNumberOfDaysHoursMinutes
} = require('../../../common/utils');

const {
    gvkVerificationConstant
} = require('../../../common/gvk_constant')

/* eslint-disable camelcase */
const anemia001 = {
    verifyMemberInfoBeforeExec: {
        modified_on: 'now',
        gvk_call_state: gvkVerificationConstant.pickedupSchedule,
        gvk_call_status: gvkVerificationConstant.callProcessing,
        treatment_performed_info: '',
    },
    verifyMemberInfoAfterExec: {
        gvk_call_previous_state: gvkVerificationConstant.pickedupSchedule,
        gvk_call_state: gvkVerificationConstant.processedAnemia,
        treatment_performed_info: 'લાભાર્થીને લોહી મળી ગયું/લોહી ચઢાવાયુ છે. લાભાર્થીને લોહતત્વના બાટલા ચઢાવેલા છે.',
        gvk_call_status: gvkVerificationConstant.callProcessed,
    },
    verifyResponseAfterExec: {
        is_108_pickedup_beneficiary: 'true',
        is_beneficiary_visited_phc: 'true',
        is_beneficiary_received_blood_lastweek_anemia: 'true',
        is_beneficiary_received_fcm_lastweek_anemia: 'true',
        is_beneficiary_droped_at_home_by_108: 'true',
    }
};

const anemia002 = {
    verifyMemberInfoBeforeExec: {
        modified_on: 'now',
        gvk_call_state: gvkVerificationConstant.pickedupSchedule,
        gvk_call_status: gvkVerificationConstant.callProcessing,
        treatment_performed_info: '',
    },
    verifyMemberInfoAfterExec: {
        gvk_call_previous_state: gvkVerificationConstant.pickedupSchedule,
        gvk_call_state: gvkVerificationConstant.processedAnemia,
        treatment_performed_info: 'લાભાર્થીને લોહી મળી ગયું/લોહી ચઢાવાયુ છે. લાભાર્થીને લોહતત્વના 3 બાટલા ચઢાવેલા છે.',
        gvk_call_status: gvkVerificationConstant.callProcessed,
    },
    verifyResponseAfterExec: {
        is_108_pickedup_beneficiary: 'true',
        is_beneficiary_visited_phc: 'true',
        is_beneficiary_received_blood_lastweek_anemia: 'true',
        is_beneficiary_received_fcm_lastweek_anemia: 'false',
        injection_count_anemia: '3',
        is_beneficiary_droped_at_home_by_108: 'true',
    }
};

const anemia003 = {
    verifyMemberInfoBeforeExec: {
        modified_on: 'now',
        gvk_call_state: gvkVerificationConstant.pickedupSchedule,
        gvk_call_status: gvkVerificationConstant.callProcessing,
        treatment_performed_info: '',
    },
    verifyMemberInfoAfterExec: {
        gvk_call_previous_state: gvkVerificationConstant.pickedupSchedule,
        gvk_call_state: gvkVerificationConstant.processedAnemia,
        treatment_performed_info: 'લાભાર્થીને લોહી મળી ગયું/લોહી ચઢાવાયુ છે.',
        gvk_call_status: gvkVerificationConstant.callProcessed,
    },
    verifyResponseAfterExec: {
        is_108_pickedup_beneficiary: 'true',
        is_beneficiary_visited_phc: 'true',
        is_beneficiary_received_blood_lastweek_anemia: 'true',
        is_beneficiary_received_fcm_lastweek_anemia: 'false',
        injection_count_anemia: '-1',
        is_beneficiary_droped_at_home_by_108: 'true',
    }
};

const anemia004 = {
    verifyMemberInfoBeforeExec: {
        modified_on: 'now',
        gvk_call_state: gvkVerificationConstant.pickedupSchedule,
        gvk_call_status: gvkVerificationConstant.callProcessing,
        treatment_performed_info: '',
    },
    verifyMemberInfoAfterExec: {
        gvk_call_previous_state: gvkVerificationConstant.pickedupSchedule,
        gvk_call_state: gvkVerificationConstant.beneficiaryPickedupScheduleForNextVisit,
        pickup_schedule_date: getDateAfterNumberOfDaysHoursMinutes(12, 10, 10),
        schedule_date: getDateAfterNumberOfDaysHoursMinutes(13, 0, 0),
        gvk_call_status: gvkVerificationConstant.callProcessing
    },
    verifyResponseAfterExec: {
        is_108_pickedup_beneficiary: 'true',
        is_beneficiary_visited_phc: 'true',
        is_beneficiary_received_blood_lastweek_anemia: 'false',
        is_beneficiary_received_fcm_lastweek_anemia: 'false',
        injection_count_anemia: '-1',
        is_beneficiary_droped_at_home_by_108: 'false',
        is_beneficiary_pickedup_date_for_next_schedule: 'true',
    }
};

const anemia005 = {
    verifyMemberInfoBeforeExec: {
        modified_on: 'now',
        gvk_call_state: gvkVerificationConstant.pickedupSchedule,
        gvk_call_status: gvkVerificationConstant.callProcessing,
        treatment_performed_info: '',
    },
    verifyMemberInfoAfterExec: {
        gvk_call_previous_state: gvkVerificationConstant.pickedupSchedule,
        gvk_call_state: gvkVerificationConstant.schedulePendingForNextVisit,
        schedule_date: getDateAfterNumberOfDaysHoursMinutes(14, 0, 0),
        gvk_call_status: gvkVerificationConstant.callProcessing
    },
    verifyResponseAfterExec: {
        is_108_pickedup_beneficiary: 'true',
        is_beneficiary_visited_phc: 'true',
        is_beneficiary_received_blood_lastweek_anemia: 'false',
        is_beneficiary_received_fcm_lastweek_anemia: 'false',
        injection_count_anemia: '-1',
        is_beneficiary_droped_at_home_by_108: 'false',
        is_beneficiary_pickedup_date_for_next_schedule: 'false',
        is_schedule_pending: 'true'
    }
};

const anemia006 = {
    verifyMemberInfoBeforeExec: {
        modified_on: 'now',
        gvk_call_state: gvkVerificationConstant.schedulePendingForNextVisit,
        gvk_call_status: gvkVerificationConstant.callProcessing,
        treatment_performed_info: '',
    },
    verifyMemberInfoAfterExec: {
        gvk_call_previous_state: gvkVerificationConstant.schedulePendingForNextVisit,
        gvk_call_state: gvkVerificationConstant.pickedupSchedule,
        schedule_date: getDateAfterNumberOfDaysHoursMinutes(5, 0, 0),
        pickup_schedule_date: getDateAfterNumberOfDaysHoursMinutes(4, 15, 18),
        gvk_call_status: gvkVerificationConstant.callProcessing,
    },
};

const anemia007 = {
    verifyMemberInfoBeforeExec: {
        modified_on: 'now',
        gvk_call_state: gvkVerificationConstant.pickedupSchedule,
        gvk_call_status: gvkVerificationConstant.callProcessing,
        treatment_performed_info: '',
    },
    verifyMemberInfoAfterExec: {
        gvk_call_previous_state: gvkVerificationConstant.pickedupSchedule,
        gvk_call_state: gvkVerificationConstant.beneficiaryPickedupScheduleForNextVisit,
        treatment_performed_info: 'લાભાર્થીને લોહતત્વના 2 બાટલા ચઢાવેલા છે.',
        gvk_call_status: gvkVerificationConstant.callProcessing,
        schedule_date: getDateAfterNumberOfDaysHoursMinutes(15, 0, 0),
        pickup_schedule_date: getDateAfterNumberOfDaysHoursMinutes(14, 0, 0),
    },
    verifyResponseAfterExec: {
        is_108_pickedup_beneficiary: 'true',
        is_beneficiary_visited_phc: 'true',
        is_beneficiary_received_blood_lastweek_anemia: 'false',
        is_beneficiary_received_fcm_lastweek_anemia: 'false',
        injection_count_anemia: '2',
        is_beneficiary_droped_at_home_by_108: 'false',
    },
    afterUpdateVerifyMemberInfoBeforeExec: {
        modified_on: 'now',
        gvk_call_state: gvkVerificationConstant.beneficiaryPickedupScheduleForNextVisit,
        gvk_call_status: gvkVerificationConstant.callProcessing,
        treatment_performed_info: 'લાભાર્થીને લોહતત્વના 2 બાટલા ચઢાવેલા છે.',
    },
    afterUpdateVerifyMemberInfoAfterExec: {
        gvk_call_previous_state: gvkVerificationConstant.beneficiaryPickedupScheduleForNextVisit,
        gvk_call_state: gvkVerificationConstant.processedAnemia,
        treatment_performed_info: 'લાભાર્થીને લોહતત્વના 2 બાટલા ચઢાવેલા છે. લાભાર્થીને લોહતત્વના 3 બાટલા ચઢાવેલા છે.',
        gvk_call_status: gvkVerificationConstant.callProcessed,
    },
    afterUpdateVerifyResponseAfterExec: {
        is_108_pickedup_beneficiary: 'true',
        is_beneficiary_visited_phc: 'true',
        is_beneficiary_received_blood_lastweek_anemia: 'false',
        is_beneficiary_received_fcm_lastweek_anemia: 'false',
        injection_count_anemia: '3',
        is_beneficiary_droped_at_home_by_108: 'false',
    },
}

module.exports = {
    anemia001,
    anemia002,
    anemia003,
    anemia004,
    anemia005,
    anemia006,
    anemia007,
};
