const {
    gvkVerificationConstant,
    memberState,
} = require('../../common/gvk_constant')

/* eslint-disable camelcase */

const duplicateMember001 = {
    verifyMemberInfoBeforeExec: {
        gvk_call_status: gvkVerificationConstant.callProcessing,
        is_member1_valid: '',
        is_member2_valid: '',
        is_active: 'true',
        remarks: '',
    },
    verifyFirstMemberEventMobileNotificationInfoBeforeExec: {
        state: 'PENDING',
        is_completed: 'false',
    },
    verifySecondMemberEventMobileNotificationInfoBeforeExec: {
        state: 'PENDING',
        is_completed: 'false',
    },
    verifyFirstMemberTechoNotificationInfoBeforeExec: {
        state: 'PENDING',
    },
    verifyMemberInfoAfterExec: {
        gvk_call_status: gvkVerificationConstant.callProcessed,
        schedule_date: 'now',
        modifed_on: 'now',
        is_member1_valid: 'false',
        is_member2_valid: 'true',
        is_active: 'false',
        // remarks: 'Member 1 detail is invalid',
    },
    verifyFirstMemberTechoNotificationInfoAfterExec: {
        state: 'MARK_AS_DUPLICATE',
        modifed_on: 'now'
    },
    verifyFirstMemberEventNotificationInfoAfterExec: {
        state: 'MARK_AS_DUPLICATE',
        modifed_on: 'now'
    },
    verifyFirstMemberInfoAfterExec: {
        state: memberState.archived,
        modifed_on: 'now'
    }
};

const duplicateMember002 = {
    verifyMemberInfoBeforeExec: {
        gvk_call_status: gvkVerificationConstant.callProcessing,
        is_member1_valid: '',
        is_member2_valid: '',
        is_active: 'true',
        remarks: '',
    },
    verifyFirstMemberEventMobileNotificationInfoBeforeExec: {
        state: 'PENDING',
        is_completed: 'false',
    },
    verifySecondMemberEventMobileNotificationInfoBeforeExec: {
        state: 'PENDING',
        is_completed: 'false',
    },
    verifySecondMemberTechoNotificationInfoBeforeExec: {
        state: 'RESCHEDULE',
    },
    verifyMemberInfoAfterExec: {
        gvk_call_status: gvkVerificationConstant.callProcessed,
        schedule_date: 'now',
        modifed_on: 'now',
        is_member1_valid: 'true',
        is_member2_valid: 'false',
        is_active: 'false',
        // remarks: 'Member 1 detail is invalid',
    },
    verifySecondMemberTechoNotificationInfoAfterExec: {
        state: 'MARK_AS_DUPLICATE',
        modifed_on: 'now'
    },
    // verifySecondMemberEventNotificationInfoAfterExec:{
    //     state : 'MARK_AS_DUPLICATE',
    //     modifed_on: 'now'
    // },
    verifySecondMemberInfoAfterExec: {
        state: memberState.archived,
        modifed_on: 'now'
    }
};

const duplicateMember003 = {
    verifyMemberInfoBeforeExec: {
        gvk_call_status: gvkVerificationConstant.callProcessing,
        is_member1_valid: '',
        is_member2_valid: '',
        is_active: 'true',
        remarks: '',
    },

    verifyMemberInfoAfterExec: {
        gvk_call_status: gvkVerificationConstant.callProcessed,
        is_member1_valid: 'true',
        is_member2_valid: 'true',
        is_active: 'false',
        remarks: 'Both Member 1 and Member 2 details are valid.',
    },
};


const duplicateMember004 = {
    verifyMemberInfoBeforeExec: {
        gvk_call_status: gvkVerificationConstant.callProcessing,
        is_member1_valid: '',
        is_member2_valid: '',
        is_active: 'true',
        remarks: '',
    },

    verifyMemberInfoAfterExec: {
        gvk_call_status: gvkVerificationConstant.callProcessing,
        is_member1_valid: '',
        is_member2_valid: '',
        is_active: 'true',
    },
};

const duplicateMember005 = {
    verifyMemberInfoBeforeExec: {
        gvk_call_status: gvkVerificationConstant.callProcessing,
        is_member1_valid: '',
        is_member2_valid: '',
        is_active: 'true',
        remarks: '',
    },

    verifyMemberInfoAfterExec: {
        gvk_call_status: gvkVerificationConstant.callProcessing,
        is_member1_valid: '',
        is_member2_valid: '',
        is_active: 'true',
    },
};

const duplicateMember006 = {
    // first time execution data
    verifyMemberInfoBeforeFirstExec: {
        gvk_call_status: gvkVerificationConstant.callProcessing,
        is_member1_valid: '',
        is_member2_valid: '',
        is_active: 'true',
        remarks: '',
        call_attempt: '0',
        modifed_on: 'now',
    },

    verifyMemberInfoAfterFirstExec: {
        gvk_call_status: gvkVerificationConstant.callToBeProccessed,
        is_member1_valid: '',
        is_member2_valid: '',
        is_active: 'true',
        remarks: 'This is first time remark for not reachable.',
        call_attempt: '1',
    },
    verifyResponseAfterFirstExec: {
        gvk_call_response_status: gvkVerificationConstant.callNotReachable,
        modifed_on: 'now',
    },
    //second time execution data
    verifyMemberInfoBeforeSecondExec: {
        gvk_call_status: gvkVerificationConstant.callProcessing,
        is_member1_valid: '',
        is_member2_valid: '',
        is_active: 'true',
        remarks: 'This is first time remark for not reachable.',
        call_attempt: '1',
        modifed_on: 'now',
    },

    verifyMemberInfoAfterSecondExec: {
        gvk_call_status: gvkVerificationConstant.callToBeProccessed,
        is_member1_valid: '',
        is_member2_valid: '',
        is_active: 'true',
        remarks: 'This is first time remark for not reachable. This is Second time remark for not reachable.',
        call_attempt: '2',
    },
    verifyResponseAfterSecondExec: {
        gvk_call_response_status: gvkVerificationConstant.callNotReachable,
        modifed_on: 'now',
    },
    //third time execution data
    verifyMemberInfoBeforeThirdExec: {
        gvk_call_status: gvkVerificationConstant.callProcessing,
        is_member1_valid: '',
        is_member2_valid: '',
        is_active: 'true',
        remarks: 'This is first time remark for not reachable. This is Second time remark for not reachable.',
        call_attempt: '2',
        modifed_on: 'now',
    },

    verifyMemberInfoAfterThirdExec: {
        gvk_call_status: gvkVerificationConstant.callToBeProccessed,
        is_member1_valid: '',
        is_member2_valid: '',
        is_active: 'true',
        remarks: 'This is first time remark for not reachable. This is Second time remark for not reachable. This is Third time remark for not reachable.',
        call_attempt: '3',
    },
    verifyResponseAfterThirdExec: {
        gvk_call_response_status: gvkVerificationConstant.callNotReachable,
        modifed_on: 'now',
    },

};



module.exports = {
    // Test data objects
    duplicateMember001,
    duplicateMember002,
    duplicateMember003,
    duplicateMember004,
    duplicateMember005,
    duplicateMember006,

};
