const {
    gvkVerificationConstant,
    memberState,
    gvkCallTypeConstnant
} = require('../../../../common/gvk_constant')

/* eslint-disable camelcase */

const memberIQ = `INSERT INTO public.migration_master(
    member_id, reported_by, reported_on, location_migrated_to, 
    location_migrated_from, migrated_location_not_known, confirmed_by, confirmed_on, 
    type, created_by, created_on, modified_by, modified_on, 
    state, other_information, family_migrated_from, family_migrated_to, 
    no_family, out_of_state, has_children, is_temporary, area_migrated_to, area_migrated_from, 
    nearest_loc_id, village_name, fhw_asha_name, fhw_asha_phone, 
    mobile_data, 
    similar_member_verified, status, 
    gvk_call_status, call_attempt, migration_reason, schedule_date, reported_location_id)
    VALUES ( null, 59220, (current_date - 1), 36475, 
    null, true, null, null,            
    'IN', 59220, (current_date - 1), 59220, (current_date - 1),
    'REPORTED', null, null, null, 
    true, false, null, true, 508553, null,
    36308, 'ભાથલા', 'અમૃતાબેન', 7567864790, 
    '{"outOfState":false, "nearestLocId":36308, "villageName":"ભાથલા", "fhwOrAshaName":"અમૃતાબેન", "fhwOrAshaPhone":"7567864790", "firstname":"રમિલા", "middleName":"ચંદન", "lastName":"સોલંકી", "gender":"F", "dob":799485063985, "phoneNumber":"8980093996", "lmp":1537686742999, "isPregnant":true, "stayingWithFamily":true, "familyId":14492736, "reportedOn":1557299615212}',
    null, null,
    '${gvkVerificationConstant.callToBeProccessed}', 0, null, (current_date - 1), 36475);`;

const memberInfoSQ = `select * from migration_master where type='IN' order by id desc limit 1`;
const callMasterSQ = `select * from gvk_manage_call_master where call_type = '${gvkCallTypeConstnant.MEMBER_MIGRATION_IN_VERIFICATION}' order by id desc limit 1`;
const mmiPageTitle = 'Migrate In – Resolution';

const mmi001 = {
    verifyMemberInfoBeforeExec: {
        data: {
            location_migrated_to: '36475',
            type: 'IN',
            state: 'REPORTED',
            call_attempt: '0',
            gvk_call_status: gvkVerificationConstant.callProcessing,
            migrated_location_not_known: 'true',
        }

    },
    verifyMemberInfoAfterExec: {
        data: {
            gvk_call_status: gvkVerificationConstant.callProcessed,
            schedule_date: 'now',
            confirmed_on: 'now',
            state: 'CONFIRMED',
            migrated_location_not_known: 'true',
            location_migrated_to: '36475',
            call_attempt: '1',
            // member_id: select id from imt_member where basic_state ='TEMPORARY' order by id desc limit 1",
        }
    },
    callMasterResponse: {
        data: {
            // id
            call_type: gvkCallTypeConstnant.MEMBER_MIGRATION_IN_VERIFICATION,
            call_response: gvkVerificationConstant.callSuccess,
            modified_on: 'now',
            created_on: 'now',
            // mobile_number' ,
        }
    },
    verifyMemberRespnseAfterExec: {
        data: {
            gvk_call_response_status: gvkVerificationConstant.callSuccess,
            performed_action: 'create_temp_member',
            modified_on: 'now',
            // manage_call_master_id: ${callmasterId},
        }
    },
    verifyMemberInfo: {
        // query:
        data: {
            basic_state: 'TEMPORARY',
            state: memberState.temporary,
            modified_on: 'now',
        }
    }
};

const mmi002 = {
    verifyMemberInfoBeforeExec: {
        data: {
            location_migrated_to: '36475',
            type: 'IN',
            state: 'REPORTED',
            call_attempt: '0',
            gvk_call_status: gvkVerificationConstant.callProcessing,
            migrated_location_not_known: 'true',
        }

    },
    verifyMemberInfoAfterExec: {
        data: {
            gvk_call_status: gvkVerificationConstant.callProcessed,
            schedule_date: 'now',
            confirmed_on: 'now',
            state: 'REPORTED',
            migrated_location_not_known: 'false',
            location_migrated_to: '36475',
            call_attempt: '1',
            member_id: '118848967'
        }
    },
    callMasterResponse: {
        data: {
            // id
            call_type: gvkCallTypeConstnant.MEMBER_MIGRATION_IN_VERIFICATION,
            call_response: gvkVerificationConstant.callSuccess,
            modified_on: 'now',
            created_on: 'now',
            // mobile_number' ,
        }
    },
    verifyMemberRespnseAfterExec: {
        data: {
            gvk_call_response_status: gvkVerificationConstant.callSuccess,
            performed_action: 'search_for_member',
            modified_on: 'now',
            // manage_call_master_id: ${callmasterId},
        }
    }
};

const mmi003 = {
    verifyMemberInfoBeforeExec: {
        data: {
            location_migrated_to: '36475',
            type: 'IN',
            state: 'REPORTED',
            call_attempt: '0',
            gvk_call_status: gvkVerificationConstant.callProcessing,
            migrated_location_not_known: 'true',
        }

    },
    verifyMemberInfoAfterExec: {
        data: {
            gvk_call_status: gvkVerificationConstant.callProcessed,
            schedule_date: 'now',
            confirmed_on: 'now',
            state: 'REPORTED',
            migrated_location_not_known: 'false',
            location_migrated_to: '36475',
            call_attempt: '1',
            member_id: '162050'
        }
    },
    callMasterResponse: {
        data: {
            // id
            call_type: gvkCallTypeConstnant.MEMBER_MIGRATION_IN_VERIFICATION,
            call_response: gvkVerificationConstant.callSuccess,
            modified_on: 'now',
            created_on: 'now',
            // mobile_number' ,
        }
    },
    verifyMemberRespnseAfterExec: {
        data: {
            gvk_call_response_status: gvkVerificationConstant.callSuccess,
            performed_action: 'search_for_member',
            modified_on: 'now',
            // manage_call_master_id: ${callmasterId},
        }
    }
};

const mmi004 = {
    verifyMemberInfoBeforeExec: {
        data: {
            location_migrated_to: '36475',
            type: 'IN',
            state: 'REPORTED',
            call_attempt: '0',
            gvk_call_status: gvkVerificationConstant.callProcessing,
            migrated_location_not_known: 'true',
        }

    },
    verifyMemberInfoAfterExec: {
        data: {
            gvk_call_status: gvkVerificationConstant.callProcessed,
            schedule_date: 'now',
            confirmed_on: 'now',
            state: 'REPORTED',
            migrated_location_not_known: 'false',
            location_migrated_to: '36475',
            call_attempt: '1',
            member_id: '162049'
        }
    },
    callMasterResponse: {
        data: {
            // id
            call_type: gvkCallTypeConstnant.MEMBER_MIGRATION_IN_VERIFICATION,
            call_response: gvkVerificationConstant.callSuccess,
            modified_on: 'now',
            created_on: 'now',
            // mobile_number' ,
        }
    },
    verifyMemberRespnseAfterExec: {
        data: {
            gvk_call_response_status: gvkVerificationConstant.callSuccess,
            performed_action: 'search_for_member',
            modified_on: 'now',
            // manage_call_master_id: ${callmasterId},
        }
    }
};

const mmi006 = {
    verifyMemberInfoBeforeExec: {
        data: {
            location_migrated_to: '36475',
            type: 'IN',
            state: 'REPORTED',
            call_attempt: '0',
            gvk_call_status: gvkVerificationConstant.callProcessing,
            migrated_location_not_known: 'true',
        }
    },
    updateLocation: {
        state: 'Gujarat',
        region: 'AHMEDABAD Region',
        district: 'Ahmedabad',
        block: 'Daskroi',
        phc: 'ASALALI',
    },
    verifyMemberInfoAfterExec: {
        data: {
            gvk_call_status: gvkVerificationConstant.callProcessed,
            schedule_date: 'now',
            confirmed_on: 'now',
            state: 'REPORTED',
            migrated_location_not_known: 'false',
            location_migrated_to: '36475',
            call_attempt: '1',
            member_id: '19611513'
        }
    },
    callMasterResponse: {
        data: {
            // id
            call_type: gvkCallTypeConstnant.MEMBER_MIGRATION_IN_VERIFICATION,
            call_response: gvkVerificationConstant.callSuccess,
            modified_on: 'now',
            created_on: 'now',
            // mobile_number' ,
        }
    },
    verifyMemberRespnseAfterExec: {
        data: {
            gvk_call_response_status: gvkVerificationConstant.callSuccess,
            performed_action: 'search_for_member',
            modified_on: 'now',
            // manage_call_master_id: ${callmasterId},
        }
    }
};

const mmi007 = {
    verifyMemberInfoBeforeExec: {
        data: {
            location_migrated_to: '36475',
            type: 'IN',
            state: 'REPORTED',
            call_attempt: '0',
            gvk_call_status: gvkVerificationConstant.callProcessing,
            migrated_location_not_known: 'true',
        }

    },
    verifyMemberInfoAfterExec: {
        data: {
            gvk_call_status: gvkVerificationConstant.callToBeProccessed,
            schedule_date: 'now',
            confirmed_on: 'now',
            state: 'REPORTED',
            migrated_location_not_known: 'true',
            location_migrated_to: '36475',
            call_attempt: '1',
            // member_id: select id from imt_member where basic_state ='TEMPORARY' order by id desc limit 1",
        }
    },
    callMasterResponse: {
        data: {
            // id
            call_type: gvkCallTypeConstnant.MEMBER_MIGRATION_IN_VERIFICATION,
            call_response: gvkVerificationConstant.callNotReachable,
            modified_on: 'now',
            created_on: 'now',
            // mobile_number' ,
        }
    },
    verifyMemberRespnseAfterExec: {
        data: {
            gvk_call_response_status: gvkVerificationConstant.callNotReachable,
            performed_action: '',
            modified_on: 'now',
            // manage_call_master_id: ${callmasterId},
        }
    }
};





module.exports = {
    // page title
    mmiPageTitle,

    // Test data objects
    mmi001,
    mmi002,
    mmi003,
    mmi004,
    mmi006,
    mmi007,
    
    // insert query
    memberIQ,

    // call master record
    callMasterSQ,

    // select query
    memberInfoSQ,

};
