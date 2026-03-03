const {
    gvkVerificationConstant,
    memberState,
    gvkCallTypeConstnant,
} = require('../../common/gvk_constant')

/* eslint-disable camelcase */
// 73535237,86005083
let recordId = 73535237;

const beneficiaryServicePageTitle = 'Beneficiary Service Verification';
const minChildCountError = 'Minimum allowed value is 1';
const maxChildCountError = 'Maximum allowed value is 6';

const beneficiaryService001 = {
    verifyMemberInfoBeforeExec: {
        data: {
            gvk_call_status: gvkVerificationConstant.callProcessing,
            call_attempt: '0',
            service_type: gvkCallTypeConstnant.FHW_CHILD_SERVICE_PREGNANCY_VERIFICATION,
        }

    },
    verifyMemberInfoAfterExec: {
        data: {
            gvk_call_status: gvkVerificationConstant.callProcessed,
            call_attempt: '1',
            modifed_on: 'now',
            service_type: gvkCallTypeConstnant.FHW_CHILD_SERVICE_PREGNANCY_VERIFICATION,
        }
    },
    callMasterResponse: {
        data: {
            // id
            call_type: gvkCallTypeConstnant.FHW_CHILD_SERVICE_PREGNANCY_VERIFICATION,
            call_response: gvkVerificationConstant.callSuccess,
            modified_on: 'now',
            created_on: 'now',
            // mobile_number' ,
        }
    },
    verifyMemberRespnseAfterExec: {
        data: {
            service_type: gvkCallTypeConstnant.FHW_CHILD_SERVICE_PREGNANCY_VERIFICATION,
            gvk_call_response_status: gvkVerificationConstant.callSuccess,
            child_service_vaccination_status: 'true',
            delivery_place_status: 'HOSPITAL',
            is_beneficiary_called: 'true',
            no_of_male_child: '0',
            no_of_female_child: '1',
            total_child: '1',
            is_verified: 'true',
            is_delivery_happened: 'true',
            no_of_child_gender_verification: 'true',
            delivery_place_verification: 'true',
            modified_on: 'now',
            // manage_call_master_id: ${ callmasterId },
        }
    },
};

const beneficiaryService002 = JSON.parse(JSON.stringify(beneficiaryService001));
delete beneficiaryService002.verifyMemberRespnseAfterExec;
beneficiaryService002.verifyMemberRespnseAfterExec = {
    data: {
        service_type: gvkCallTypeConstnant.FHW_CHILD_SERVICE_PREGNANCY_VERIFICATION,
        gvk_call_response_status: gvkVerificationConstant.callSuccess,
        child_service_vaccination_status: 'true',
        delivery_place_status: 'HOME',
        is_beneficiary_called: 'true',
        no_of_male_child: '1',
        no_of_female_child: '0',
        total_child: '1',
        is_verified: 'true',
        is_delivery_happened: 'true',
        no_of_child_gender_verification: 'true',
        delivery_place_verification: 'true',
        modified_on: 'now',
        // manage_call_master_id: ${ callmasterId },
    }
}

const beneficiaryService003 = JSON.parse(JSON.stringify(beneficiaryService002));
beneficiaryService003.verifyMemberRespnseAfterExec.data.delivery_place_status = 'ON_THE_WAY';

const beneficiaryService004 = JSON.parse(JSON.stringify(beneficiaryService001));
delete beneficiaryService004.verifyMemberRespnseAfterExec;
beneficiaryService004.verifyMemberRespnseAfterExec = {
    data: {
        service_type: gvkCallTypeConstnant.FHW_CHILD_SERVICE_PREGNANCY_VERIFICATION,
        gvk_call_response_status: gvkVerificationConstant.callSuccess,
        child_service_vaccination_status: 'false',
        delivery_place_status: '',
        is_beneficiary_called: 'true',
        no_of_male_child: '',
        no_of_female_child: '',
        total_child: '',
        is_verified: 'false',
        is_delivery_happened: 'false',
        no_of_child_gender_verification: 'false',
        delivery_place_verification: 'false',
        modified_on: 'now',
        // 'manage_call_master_id': ${callmasterId}
    }
}

const beneficiaryService005 = {
    verifyMemberInfoBeforeExec: {
        data: {
            gvk_call_status: gvkVerificationConstant.callProcessing,
            call_attempt: '0',
            service_type: 'FHW_TT_VERI',
        }
    },
    verifyMemberInfoAfterExec: {
        data: {
            gvk_call_status: gvkVerificationConstant.callProcessed,
            call_attempt: '1',
            modifed_on: 'now',
            service_type: 'FHW_TT_VERI',
        }
    },
    callMasterResponse: {
        data: {
            // id
            call_type: gvkCallTypeConstnant.FHW_TT_VERIFICATION,
            call_response: gvkVerificationConstant.callSuccess,
            modified_on: 'now',
            created_on: 'now',
            // mobile_number' ,
        }
    },
    verifyMemberRespnseAfterExec: {
        data: {
            service_type: 'FHW_TT_VERI',
            gvk_call_response_status: gvkVerificationConstant.callSuccess,
            tt_injection_received_status: 'YES',
            is_verified: 'true',
            modified_on: 'now',
        }
    },
};

const beneficiaryService006 = JSON.parse(JSON.stringify(beneficiaryService005));
beneficiaryService006.verifyMemberRespnseAfterExec.data.tt_injection_received_status = 'NO';
beneficiaryService006.verifyMemberRespnseAfterExec.data.is_verified = 'false';

const beneficiaryService007 = JSON.parse(JSON.stringify(beneficiaryService006));
beneficiaryService007.verifyMemberRespnseAfterExec.data.tt_injection_received_status = 'CANNOT_DETERMINE';

const beneficiaryService008 = {
    verifyMemberInfoBeforeExec: {
        data: {
            gvk_call_status: gvkVerificationConstant.callProcessing,
            call_attempt: '0',
            service_type: 'FHW_TT_VERI',
        }
    },
    verifyMemberInfoAfterExec: {
        data: {
            gvk_call_status: gvkVerificationConstant.callProcessing,
            call_attempt: '1',
            modifed_on: 'now',
            service_type: 'FHW_TT_VERI',
        }
    },
    callMasterResponse: {
        data: {
            // id
            call_type: gvkCallTypeConstnant.FHW_TT_VERIFICATION,
            call_response: gvkVerificationConstant.callNotReachable,
            modified_on: 'now',
            created_on: 'now',
            // mobile_number' ,
        }
    },
    verifyMemberRespnseAfterExec: {
        data: {
            service_type: 'FHW_TT_VERI',
            gvk_call_response_status: gvkVerificationConstant.callNotReachable,
            tt_injection_received_status: '',
            is_verified: '',
            modified_on: 'now',
        }
    },
};

const beneficiaryService009 = JSON.parse(JSON.stringify(beneficiaryService001));
delete beneficiaryService009.verifyMemberRespnseAfterExec;
beneficiaryService009.verifyMemberRespnseAfterExec = {
    data: {
        service_type: gvkCallTypeConstnant.FHW_CHILD_SERVICE_PREGNANCY_VERIFICATION,
        gvk_call_response_status: gvkVerificationConstant.callSuccess,
        child_service_vaccination_status: 'false',
        delivery_place_status: 'HOME',
        is_beneficiary_called: 'true',
        no_of_male_child: '1',
        no_of_female_child: '1',
        total_child: '2',
        is_verified: 'false',
        is_delivery_happened: 'true',
        no_of_child_gender_verification: 'false',
        delivery_place_verification: 'false',
        modified_on: 'now',
    }
}



module.exports = {
    // page title
    beneficiaryServicePageTitle,

    // Test data objects
    beneficiaryService001,
    beneficiaryService002,
    beneficiaryService003,
    beneficiaryService004,
    beneficiaryService005,
    beneficiaryService006,
    beneficiaryService007,
    beneficiaryService008,
    beneficiaryService009,

    //error message
    minChildCountError,
    maxChildCountError

};
