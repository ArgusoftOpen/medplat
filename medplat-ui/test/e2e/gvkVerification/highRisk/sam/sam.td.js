const {
    getDateAfterNumberOfDaysHoursMinutes
} = require('../../../common/utils');

const {
    gvkVerificationConstant
} = require('../../../common/gvk_constant')

/* eslint-disable camelcase */

const recordId = 111923824;
const userInfoSelectQuery = `select * from gvk_high_risk_follow_up_usr_info where member_id = ${recordId} order by id desc limit 1`;
const responseSelectQuery = `select * from gvk_high_risk_follow_up_responce where member_id = ${recordId} order by id desc limit 1`;
const modifiedOnHourDiffQuery = `update gvk_high_risk_follow_up_usr_info set modified_on = (localtimestamp - interval '1 hour') where member_id = ${recordId}`;
const modifiedScheduledHourDiffQuery = `update gvk_high_risk_follow_up_usr_info set (schedule_date,modified_on) = (localtimestamp, (localtimestamp - interval '2 hour') ) where member_id = ${recordId}`;
const modifiedScheduledDayDiffQuery = `update gvk_high_risk_follow_up_usr_info set (modified_on,schedule_date) = ((localtimestamp - interval '1 day'), localtimestamp) where member_id = ${recordId}`;
const deleteQuery = `delete from gvk_high_risk_follow_up_usr_info where member_id = ${recordId};delete from gvk_high_risk_follow_up_responce where member_id = ${recordId};`;
const memberStateVerificationQuery = `select * from imt_member where (basic_state ='VERIFIED' or basic_state='NEW') and id = ${recordId}`

const sam001 = {
    verifyUserInfoBeforeExec: {
        modified_on: 'now',
        gvk_call_state: gvkVerificationConstant.highRiskConditionConfirmation
    },
    verifyResponseAfterExec: {
        gvk_call_response_status: gvkVerificationConstant.callSuccess,
        is_high_risk_condition_confirmed: 'true',
        called_for: 'highriskuserverificationdata'
    },
    verifyUserInfoAfterExec: {
        modified_on: 'now',
        gvk_call_state: gvkVerificationConstant.highRiskConditionConfirmationWithYes,
        gvk_call_status: gvkVerificationConstant.callProcessing
    }
};

const sam002 = {
    verifyUserInfoBeforeExec: {
        pickup_schedule_date: '',
        modified_on: 'now',
        gvk_call_state: gvkVerificationConstant.highRiskConditionConfirmationWithYes,
    },
    verifyUserInfoAfterExec: {
        pickup_schedule_date: 'next',
        schedule_date: 'next',
        modified_on: 'now',
        gvk_call_state: gvkVerificationConstant.pickedupSchedule,
        gvk_call_previous_state: gvkVerificationConstant.highRiskConditionConfirmationWithYes
    }
};

const sam003 = {
    verifyUserInfoBeforeExec: {
        treatment_performed_info: '',
        gvk_call_state: gvkVerificationConstant.pickedupSchedule,
        gvk_call_status: gvkVerificationConstant.callProcessing,
        gvk_call_previous_state: gvkVerificationConstant.highRiskConditionConfirmationWithYes
    },
    verifyResponseBeforeExec: {
        is_108_pickedup_beneficiary: '',
        is_beneficiary_visited_phc: '',
        is_beneficiary_child_admitted_to_cmtc_sam: '',
    },
    verifyUserInfoAfterExec: {
        treatment_performed_info: 'લાભાર્થી બાળકને બાળ સેવા કેન્દ્ર માં દાખલ કરવામાં આવેલ છે.',
        gvk_call_status: gvkVerificationConstant.callProcessed,
        gvk_call_previous_state: gvkVerificationConstant.pickedupSchedule,
        gvk_call_state: gvkVerificationConstant.childAdmittedToSNCU,
    },
    verifyResponseAfterExec: {
        is_108_pickedup_beneficiary: 'true',
        is_beneficiary_visited_phc: 'true',
        is_beneficiary_child_admitted_to_cmtc_sam: 'true'
    },
    formDetailsObj: {
        has108PickedupBeneficiary: true,
        hasBeneficiaryVisitedPhc: true,
        isBeneficiaryChildAdmitted: true,
        isBeneficiaryDropedAtHome: true
    }
};

const sam004 = {
    verifyMemberInfoBeforeExec: {
        modified_on: 'now',
        gvk_call_state: gvkVerificationConstant.pickedupSchedule,
        gvk_call_previous_state: gvkVerificationConstant.highRiskConditionConfirmationWithYes,
        gvk_call_status: gvkVerificationConstant.callProcessing,
    },
    verifyMemberInfoAfterExec: {
        gvk_call_previous_state: gvkVerificationConstant.pickedupSchedule,
        gvk_call_state: gvkVerificationConstant.beneficiaryNotPickedupBy108PickedupSchedule,
        schedule_date: getDateAfterNumberOfDaysHoursMinutes(2, 0, 0),
        pickup_schedule_date: getDateAfterNumberOfDaysHoursMinutes(1, 10, 10),
        gvk_call_status: gvkVerificationConstant.callProcessing,
    },
    verifyResponseAfterExec: {
        is_108_pickedup_beneficiary: 'false',
        confimation_for_reschelue_pickup_date_again: 'true',

    }
};

const sam005 = {
    verifyMemberInfoBeforeExec: {
        modified_on: 'now',
        gvk_call_state: gvkVerificationConstant.pickedupSchedule,
        gvk_call_previous_state: gvkVerificationConstant.highRiskConditionConfirmationWithYes,
        gvk_call_status: gvkVerificationConstant.callProcessing,
    },

    verifyMemberInfoAfterExec: {
        gvk_call_previous_state: gvkVerificationConstant.pickedupSchedule,
        gvk_call_state: gvkVerificationConstant.pickedupSchedulePending,
        schedule_date: getDateAfterNumberOfDaysHoursMinutes(1, 0, 0),
        gvk_call_status: gvkVerificationConstant.callProcessing,
    },
    verifyResponseAfterExec: {
        is_108_pickedup_beneficiary: 'false',
        is_beneficiary_visited_phc: '',
        is_beneficiary_child_admitted_to_cmtc_sam: '',
        confimation_for_reschelue_pickup_date_again: 'true',
        is_schedule_pending: 'true',
        modified_on: 'now',
    },
    afterUpdateVerifyMemberInfoBeforeExec: {
        modified_on: 'now',
        gvk_call_previous_state: gvkVerificationConstant.pickedupSchedule,
        gvk_call_state: gvkVerificationConstant.pickedupSchedulePending,
        gvk_call_status: gvkVerificationConstant.callProcessing,
    },

    afterUpdateVerifyMemberInfoAfterExec: {
        gvk_call_previous_state: gvkVerificationConstant.pickedupSchedulePending,
        gvk_call_state: gvkVerificationConstant.pickedupSchedule,
        pickup_schedule_date: getDateAfterNumberOfDaysHoursMinutes(3, 10, 10),
        schedule_date: getDateAfterNumberOfDaysHoursMinutes(4, 0, 0),
        gvk_call_status: gvkVerificationConstant.callProcessing,
        modified_on: 'now',
    }

};

const sam006 = {
    verifyMemberInfoBeforeExec: {
        modified_on: 'now',
        gvk_call_state: gvkVerificationConstant.pickedupSchedule,
        gvk_call_previous_state: gvkVerificationConstant.highRiskConditionConfirmationWithYes,
        gvk_call_status: gvkVerificationConstant.callProcessing,
    },

    verifyMemberInfoAfterExec: {
        gvk_call_previous_state: gvkVerificationConstant.pickedupSchedule,
        gvk_call_state: gvkVerificationConstant.beneficiaryNotInterestedToReschedulePickupDate,
        gvk_call_status: gvkVerificationConstant.callProcessed,
        is_beneficiary_visited_phc: 'undefined',
        is_beneficiary_child_admitted_to_cmtc_sam: 'undefined',
        modified_on: 'now',
    },
    verifyResponseAfterExec: {
        is_108_pickedup_beneficiary: 'false',
        confimation_for_reschelue_pickup_date_again: 'false',
        modified_on: 'now',
    },
};

const sam007 = {
    verifyMemberInfoBeforeExec: {
        modified_on: 'now',
        gvk_call_state: gvkVerificationConstant.pickedupSchedule,
        gvk_call_previous_state: gvkVerificationConstant.highRiskConditionConfirmationWithYes,
        gvk_call_status: gvkVerificationConstant.callProcessing,
    },
    verifyMemberInfoAfterExec: {
        gvk_call_previous_state: gvkVerificationConstant.pickedupSchedule,
        gvk_call_state: gvkVerificationConstant.beneficiaryNotVisitedPhcPickedupSchedule,
        gvk_call_status: gvkVerificationConstant.callProcessing,
        pickup_schedule_date: getDateAfterNumberOfDaysHoursMinutes(2, 5, 40),
        schedule_date: getDateAfterNumberOfDaysHoursMinutes(3, 0, 0),
    },
    verifyResponseAfterExec: {
        is_108_pickedup_beneficiary: 'true',
        is_beneficiary_visited_phc: 'false',
        confimation_for_reschelue_pickup_date_again: 'true',
        is_beneficiary_child_admitted_to_cmtc_sam: '',
    }
};

const sam008 = {
    verifyMemberInfoBeforeExec: {
        modified_on: 'now',
        gvk_call_state: gvkVerificationConstant.pickedupSchedule,
        gvk_call_status: gvkVerificationConstant.callProcessing,
        gvk_call_previous_state: gvkVerificationConstant.highRiskConditionConfirmationWithYes,
    },
    verifyMemberInfoAfterExec: {
        gvk_call_previous_state: gvkVerificationConstant.pickedupSchedule,
        gvk_call_state: gvkVerificationConstant.beneficiaryNotInterestedToReschedulePickupDate,
        gvk_call_status: gvkVerificationConstant.callProcessed,
        modified_on: 'now',
        schedule_date: 'now',

    },
    verifyResponseAfterExec: {
        is_108_pickedup_beneficiary: 'true',
        is_beneficiary_visited_phc: 'false',
        confimation_for_reschelue_pickup_date_again: 'false',
        is_beneficiary_child_admitted_to_cmtc_sam: '',
    }
};

const sam009 = {
    verifyMemberInfoBeforeExec: {
        modified_on: 'now',
        gvk_call_state: gvkVerificationConstant.pickedupSchedule,
        gvk_call_status: gvkVerificationConstant.callProcessing,
        gvk_call_previous_state: gvkVerificationConstant.highRiskConditionConfirmationWithYes,
    },

    verifyMemberInfoAfterExec: {
        gvk_call_previous_state: gvkVerificationConstant.pickedupSchedule,
        gvk_call_state: gvkVerificationConstant.beneficiaryPickedupScheduleForNextVisit,
        treatment_performed_info: '',
        gvk_call_status: gvkVerificationConstant.callProcessing,
        pickup_schedule_date: getDateAfterNumberOfDaysHoursMinutes(17, 10, 20),
        schedule_date: getDateAfterNumberOfDaysHoursMinutes(18, 0, 0),
    },
    verifyResponseAfterExec: {
        is_108_pickedup_beneficiary: 'true',
        is_beneficiary_visited_phc: 'true',
        is_beneficiary_child_admitted_to_cmtc_sam: 'false',
        is_beneficiary_droped_at_home_by_108: '',
        is_beneficiary_pickedup_date_for_next_schedule: 'true',
    }
};

const sam010 = {
    verifyMemberInfoBeforeExec: {
        modified_on: 'now',
        gvk_call_state: gvkVerificationConstant.pickedupSchedule,
        gvk_call_status: gvkVerificationConstant.callProcessing,
        gvk_call_previous_state: gvkVerificationConstant.highRiskConditionConfirmationWithYes,
    },
    verifyMemberInfoAfterExec: {
        gvk_call_previous_state: gvkVerificationConstant.pickedupSchedule,
        gvk_call_state: gvkVerificationConstant.schedulePendingForNextVisit,
        treatment_performed_info: '',
        gvk_call_status: gvkVerificationConstant.callProcessing,
        schedule_date: getDateAfterNumberOfDaysHoursMinutes(14, 0, 0),
    },
    verifyResponseAfterExec: {
        is_108_pickedup_beneficiary: 'true',
        is_beneficiary_visited_phc: 'true',
        is_beneficiary_child_admitted_to_cmtc_sam: 'false',
        is_beneficiary_pickedup_date_for_next_schedule: 'false',
        schedule_date: getDateAfterNumberOfDaysHoursMinutes(13, 0, 0),
    }
};

const sam011 = {
    verifyUserInfoBeforeExec: {
        gvk_call_state: gvkVerificationConstant.highRiskConditionConfirmationWithYes,
        gvk_call_previous_state: gvkVerificationConstant.highRiskConditionConfirmation,
        pickup_schedule_date: ''
    },
    verifyUserInfoAfterExec: {
        gvk_call_state: gvkVerificationConstant.beneficiaryNotWillingToHelped,
        gvk_call_previous_state: gvkVerificationConstant.highRiskConditionConfirmationWithYes,
        pickup_schedule_date: '',
        modified_on: 'now'
    }
};

const sam011AfterUpdate = {
    verifyUserInfoBeforeExec: sam011.verifyUserInfoAfterExec,
    verifyUserInfoAfterExec: {
        pickup_schedule_date: '',
        gvk_call_state: gvkVerificationConstant.beneficiaryNotWillingToHelped,
        gvk_call_previous_state: gvkVerificationConstant.beneficiaryNotWillingToHelped,
    },
};

const sam012 = {
    verifyUserInfoBeforeExec: {
        gvk_call_state: gvkVerificationConstant.highRiskConditionConfirmationWithYes,
        gvk_call_previous_state: gvkVerificationConstant.highRiskConditionConfirmation,
        pickup_schedule_date: ''
    },
    verifyUserInfoAfterExec: {
        gvk_call_state: gvkVerificationConstant.beneficiaryNotWillingToHelped,
        gvk_call_previous_state: gvkVerificationConstant.highRiskConditionConfirmationWithYes,
        pickup_schedule_date: ''
    }
};

const sam012AfterUpdate = {
    verifyUserInfoBeforeExec: sam012.verifyUserInfoAfterExec,
    verifyUserInfoAfterExec: {
        pickupScheduleDate: 'undefined',
        gvk_call_state: gvkVerificationConstant.pickedupSchedule,
        gvk_call_previous_state: gvkVerificationConstant.beneficiaryNotWillingToHelped
    }
};

const sam013 = {
    verifyUserInfoBeforeExec: {
        gvk_call_state: gvkVerificationConstant.highRiskConditionConfirmationWithYes,
        gvk_call_previous_state: gvkVerificationConstant.highRiskConditionConfirmation,
        pickup_schedule_date: ''
    },
    verifyUserInfoAfterExec: {
        gvk_call_state: gvkVerificationConstant.beneficiaryNotWillingToHelped,
        gvk_call_previous_state: gvkVerificationConstant.highRiskConditionConfirmationWithYes,
        pickup_schedule_date: ''
    }
};

const sam013AfterUpdate = {
    verifyUserInfoBeforeExec: sam013.verifyUserInfoAfterExec,
    verifyUserInfoAfterExec: {
        pickup_schedule_date: '',
        gvk_call_state: gvkVerificationConstant.schedulePending,
        gvk_call_previous_state: gvkVerificationConstant.beneficiaryNotWillingToHelped
    }
};

const sam013AfterUpdate2 = {
    verifyUserInfoBeforeExec: sam013AfterUpdate.verifyUserInfoAfterExec,
    verifyUserInfoAfterExec: {
        pickup_schedule_date: 'now',
        schedule_date: 'next',
        gvk_call_state: gvkVerificationConstant.pickedupSchedule,
        gvk_call_previous_state: gvkVerificationConstant.schedulePending,
    }
};

const sam014 = {
    verifyUserInfoBeforeExec: {
        modified_on: 'now',
        gvk_call_state: gvkVerificationConstant.highRiskConditionConfirmation
    },
    verifyUserInfoAfterExec: {
        modified_on: 'now',
        gvk_call_state: gvkVerificationConstant.highRiskConditionConfirmationWithNo,
    },
    verifyResponseAfterExec: {
        gvk_call_response_status: gvkVerificationConstant.callSuccess,
        is_high_risk_condition_confirmed: 'false',
        called_for: 'highriskuserverificationdata'
    }
};

const sam017 = {
    verifyUserInfoBeforeExec: {
        pickup_schedule_date: '',
        gvk_call_state: gvkVerificationConstant.highRiskConditionConfirmationWithYes,
        gvk_call_previous_state: '',
        call_attempt: '0',
        schedule_date: 'truthy'
    },
    verifyUserInfoAfterExec: {
        pickup_schedule_date: '',
        gvk_call_state: gvkVerificationConstant.highRiskConditionConfirmationWithYes,
        call_attempt: '1',
        schedule_date: 'truthy'
    },
    verifyResponseAfterExec: {
        gvk_call_response_status: gvkVerificationConstant.callNotReachable
    }
};

const sam017AfterUpdate = JSON.parse(JSON.stringify(sam017));
delete sam017AfterUpdate.verifyUserInfoBeforeExec.gvk_call_previous_state;
sam017AfterUpdate.verifyUserInfoBeforeExec.call_attempt = '1';
sam017AfterUpdate.verifyUserInfoAfterExec.call_attempt = '2';

const sam017AfterUpdate2 = JSON.parse(JSON.stringify(sam017));
delete sam017AfterUpdate2.verifyUserInfoBeforeExec.gvk_call_previous_state;
sam017AfterUpdate2.verifyUserInfoBeforeExec.call_attempt = '2';
sam017AfterUpdate2.verifyUserInfoAfterExec.call_attempt = '3';

module.exports = {
    // constants
    recordId,

    // Queries
    deleteQuery,
    memberStateVerificationQuery,
    modifiedOnHourDiffQuery,
    modifiedScheduledDayDiffQuery,
    modifiedScheduledHourDiffQuery,
    responseSelectQuery,
    userInfoSelectQuery,

    // Test data objects
    sam001,
    sam002,
    sam003,
    sam004,
    sam005,
    sam006,
    sam007,
    sam008,
    sam009,
    sam010,
    sam011,
    sam011AfterUpdate,
    sam012,
    sam012AfterUpdate,
    sam013,
    sam013AfterUpdate,
    sam013AfterUpdate2,
    sam014,
    sam017,
    sam017AfterUpdate,
    sam017AfterUpdate2
};
