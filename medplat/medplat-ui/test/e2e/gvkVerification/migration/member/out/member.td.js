const {
    gvkVerificationConstant,
    memberState,
} = require('../../../../common/gvk_constant')

/* eslint-disable camelcase */
const recordId = 64472901;

// Select Queries
const callResponseSQ = `select * from gvk_member_migration_call_response where member_id = ${recordId} order by id desc limit 1`;
const imtMemberSQ = `select * from imt_member where (basic_state ='VERIFIED' or basic_state='NEW') and id = ${recordId}`;
const migrationMasterSQ = `select * from migration_master where member_id = ${recordId} order by id desc limit 1`;
const mobileNotificationPendingSQ = `select * from event_mobile_notification_pending where member_id = ${recordId} order by id desc limit 1`;
const notificationMasterSQ = `select * from techo_notification_master where member_id = ${recordId}`;
const stateDetailSQ = `select * from imt_member_state_detail where member_id = ${recordId} order by 1 desc limit 1`;

// Insert Queries
const migrationMasterIQ = `INSERT INTO public.migration_master(
    member_id, reported_by, reported_on, location_migrated_to,
    location_migrated_from, migrated_location_not_known, confirmed_by, confirmed_on,
    type, created_by, created_on, modified_by, modified_on,
    state, other_information, family_migrated_from, family_migrated_to,
    no_family, out_of_state, has_children, is_temporary, area_migrated_to, area_migrated_from,
    nearest_loc_id, village_name, fhw_asha_name, fhw_asha_phone,
    mobile_data,
    similar_member_verified, status,
    gvk_call_status, call_attempt, migration_reason, schedule_date, reported_location_id)
    VALUES ( ${recordId}, 59220, (current_date - 1), null,
    205322, true, null, null,
    'OUT', 59220, (current_date - 1), 59220, (current_date - 1),
    'REPORTED', 'ઓઢવ રહેવા ગયા છે પણ સરનામુ ખબર નથી.', 'FM/2009/8340925', null,
    false, false, null, false, null, 205324,
    null, null, null, null,
    '{memberId:${recordId}, fromFamilyId:10177951, fromLocationId:205322, outOfState:false, childrensUnder5:[], reportedOn:1573105187988}',
    null, null,
    '${gvkVerificationConstant.callToBeProccessed}', 0, null, (current_date - 1), 205322);`;
const mobileNotificationPendingIQ = `INSERT INTO public.event_mobile_notification_pending
    (notification_configuration_type_id, base_date, user_id, family_id, member_id, created_by, created_on, modified_by, modified_on, is_completed, state, ref_code)
    VALUES(null, null, 0, '10177951', ${recordId}, 4108348, (current_date - 1), 0, null, false, 'MARK_AS_MIGRATED_PENDING', 0);`;
const notificationMasterIQ1 = `INSERT INTO public.techo_notification_master(
    notification_type_id, location_id, other_details, schedule_date, state, migration_id, member_id,
    created_by, created_on, modified_by, modified_on)
    VALUES(4, 137222, null, (current_date - 1), 'MARK_AS_MIGRATED_PENDING', null, ${recordId}, 59220, (current_date - 1), 59220,(current_date - 1));`;
const notificationMasterIQ2 = `INSERT INTO public.techo_notification_master(
    notification_type_id, location_id, other_details, schedule_date, state, migration_id, member_id,
    created_by, created_on, modified_by, modified_on)
    VALUES(5, 137222, null, (current_date - 1), 'MARK_AS_MIGRATED_RESCHEDULE', null, ${recordId}, 59220, (current_date - 1), 59220, (current_date - 1));`;
const stateDetailIQ = `INSERT INTO public.imt_member_state_detail
    ("comment", from_state, to_state, created_on, oldid, old_parent, parent, old_created_by,
    created_by, modified_by, modified_on, old_member_id, member_id)
    VALUES(null, '${memberState.new}', '${memberState.migrated}',
    (current_date - 2), null, null, null, null, 35774, 35774, (current_date - 2), null, ${recordId});`;

// Update Queries
const migrationMasterUQ = `update migration_master set (schedule_date, modified_on) = (localtimestamp, (localtimestamp - interval '2 hour') ) where member_id = ${recordId}`;

// Delete Queries
const callResponseDQ = `delete from gvk_member_migration_call_response where member_id = ${recordId};`;
const migrationMasterDQ = `delete from migration_master where member_id = ${recordId};`;
const mobileNotificationPendingDQ = `delete from event_mobile_notification_pending where member_id = ${recordId};`;
const notificationMasterDQ = `delete from techo_notification_master where member_id = ${recordId};`;
const stateDetailDQ = `delete from imt_member_state_detail where id in (select id from imt_member_state_detail where member_id = ${recordId});`;

// Test data objects
const mmo001 = {
    imtMemberSQ,
    insertQuery: migrationMasterIQ,
    verifyBeforeExec: {
        migrationDetails: {
            query: migrationMasterSQ,
            data: {
                location_migrated_to: '',
                type: 'OUT',
                state: 'REPORTED',
                status: '',
                gvk_call_status: gvkVerificationConstant.callProcessing,
                migrated_location_not_known: 'true'
            }
        }
    },
    formDetailsObj: {
        resolution: 'Update Location',
        toCheckToaster: true,
        updateLocation: {
            state: 'Gujarat',
            region: 'AHMEDABAD Region',
            district: 'Ahmedabad',
            block: 'Daskroi',
            phc: 'ASALALI',
            subCenter: 'ASALALI',
            village: 'અસલાલી',
            ashaArea: 'ભોઈ વાસ',
        }
    },
    verifyAfterExec: {
        migrationDetails: {
            query: migrationMasterSQ,
            data: {
                gvk_call_status: gvkVerificationConstant.callProcessed,
                schedule_date: 'now',
                confirmed_on: 'now',
                state: 'REPORTED',
                migrated_location_not_known: 'false',
                location_migrated_to: '183153',
                status: 'PENDING',
            }
        },
        migrationResponse: {
            query: callResponseSQ,
            data: {
                performed_action: 'UPDATE_LOCATION',
                gvk_call_response_status: gvkVerificationConstant.callSuccess,
                migration_type: 'OUT',
                modified_on: 'now'
            }
        },
        notification: {
            query: notificationMasterSQ,
            data: {
                location_id: '183153',
                state: 'PENDING',
                modified_on: 'now'
            }
        }
    },
    deleteQuery: [migrationMasterDQ, notificationMasterDQ, callResponseDQ].join('')
};

const mmo002 = {
    imtMemberSQ,
    insertQuery: migrationMasterIQ,
    verifyBeforeExec: {
        migrationDetails: {
            query: migrationMasterSQ,
            data: {
                location_migrated_to: '',
                type: 'OUT',
                state: 'REPORTED',
                status: '',
                gvk_call_status: gvkVerificationConstant.callProcessing,
                migrated_location_not_known: 'true',
                out_of_state: 'false'
            }
        }
    },
    formDetailsObj: {
        resolution: 'Mark as LFU',
        toCheckToaster: true,
        markOutOfState: true
    },
    verifyAfterExec: {
        migrationDetails: {
            query: migrationMasterSQ,
            data: {
                gvk_call_status: gvkVerificationConstant.callProcessed,
                schedule_date: 'now',
                confirmed_on: 'now',
                migrated_location_not_known: 'true',
                location_migrated_to: '',
                status: '',
                state: 'LFU',
                out_of_state: 'true'
            }
        },
        migrationResponse: {
            query: callResponseSQ,
            data: {
                performed_action: 'LFU',
                gvk_call_response_status: gvkVerificationConstant.callSuccess,
                migration_type: 'OUT',
                modified_on: 'now'
            }
        }
    },
    deleteQuery: [migrationMasterDQ, callResponseDQ].join('')
};

const mmo003 = JSON.parse(JSON.stringify(mmo002));
delete mmo003.formDetailsObj.markOutOfState;
mmo003.verifyAfterExec.migrationDetails.data.out_of_state = '';

const mmo004 = {
    imtMemberSQ,
    insertQuery: migrationMasterIQ,
    verifyBeforeExec: {
        migrationDetails: {
            query: migrationMasterSQ,
            data: {
                location_migrated_to: '',
                type: 'OUT',
                state: 'REPORTED',
                status: '',
                gvk_call_status: gvkVerificationConstant.callProcessing,
                migrated_location_not_known: 'true',
                out_of_state: 'false',
                call_attempt: '0'
            }
        },
    },
    formDetailsObj: {
        callStatus: 'Not Reachable'
    },
    verifyAfterExec: {
        migrationDetails: {
            query: migrationMasterSQ,
            data: {
                gvk_call_status: gvkVerificationConstant.callToBeProccessed,
                schedule_date: 'next',
                modified_on: 'now',
                migrated_location_not_known: 'true',
                location_migrated_to: '',
                state: 'REPORTED',
                out_of_state: 'false',
                call_attempt: '1'
            }
        },
        migrationResponse: {
            query: callResponseSQ,
            data: {
                performed_action: '',
                gvk_call_response_status: gvkVerificationConstant.callNotReachable,
                migration_type: 'OUT',
                modified_on: 'now'
            }
        }
    },
    deleteQuery: [migrationMasterDQ, callResponseDQ].join('')
};

const mmo005 = {
    imtMemberSQ,
    insertQuery: [migrationMasterIQ, mobileNotificationPendingIQ, notificationMasterIQ1, notificationMasterIQ2, stateDetailIQ].join(''),
    verifyBeforeExec: {
        migrationDetails: {
            query: migrationMasterSQ,
            data: {
                location_migrated_to: '',
                type: 'OUT',
                state: 'REPORTED',
                status: '',
                gvk_call_status: gvkVerificationConstant.callProcessing,
                migrated_location_not_known: 'true',
                out_of_state: 'false'
            }
        },
        mobileNotificationPending: {
            query: mobileNotificationPendingSQ,
            data: {
                state: 'MARK_AS_MIGRATED_PENDING'
            }
        },
        notification: {
            query: notificationMasterSQ,
            isMulti: true,
            data: {
                state: ['MARK_AS_MIGRATED_PENDING', 'MARK_AS_MIGRATED_RESCHEDULE']
            }
        },
        imtMember: {
            query: imtMemberSQ,
            data: {
                state: memberState.verified
            }
        },
        stateDetail: {
            query: stateDetailSQ,
            data: {
                from_state: memberState.new,
                to_state: memberState.migrated,
                member_id: `${recordId}`
            }
        }
    },
    formDetailsObj: {
        resolution: 'Roll Back',
        toCheckToaster: true
    },
    verifyAfterExec: {
        migrationDetails: {
            query: migrationMasterSQ,
            data: {
                gvk_call_status: gvkVerificationConstant.callProcessed,
                schedule_date: 'now',
                confirmed_on: 'now',
                migrated_location_not_known: 'true',
                location_migrated_to: '',
                status: '',
                state: 'ROLLBACK',
                out_of_state: 'false'
            }
        },
        callResponse: {
            query: callResponseSQ,
            data: {
                performed_action: 'ROLLBACK',
                gvk_call_response_status: gvkVerificationConstant.callSuccess,
                migration_type: 'OUT',
                modified_on: 'now'
            }
        },
        notification: {
            query: notificationMasterSQ,
            isMulti: true,
            data: {
                notification_type_id: [4, 5, 25],
                state: ['PENDING', 'MARK_AS_MIGRATED_RESCHEDULE', 'PENDING']
            }
        },
        mobileNotificationPending: {
            query: mobileNotificationPendingSQ,
            data: {
                state: 'PENDING',
                modified_on: 'now'
            }
        },
        // TODO : Check feasibility for verification
        // https://docs.google.com/spreadsheets/d/1R-y4cc5BnX1ucSg-JWGXxYyZHEu18NBL4iNLe5GM4mU/edit?ts=5da5569a&pli=1#gid=987203203&range=D357
        // imtMember: {
        //     query: imtMemberSQ,
        //     data: {
        //         state: stateDetailSQ
        //     }
        // }
    },
    deleteQuery: [migrationMasterDQ, mobileNotificationPendingDQ, notificationMasterDQ, stateDetailDQ, callResponseDQ].join('')
};

const mmo006 = {
    imtMemberSQ,
    insertQuery: migrationMasterIQ,
    verifyBeforeExec: {
        migrationDetails: {
            query: migrationMasterSQ,
            data: {
                location_migrated_to: '',
                type: 'OUT',
                state: 'REPORTED',
                status: '',
                gvk_call_status: gvkVerificationConstant.callProcessing,
                migrated_location_not_known: 'true',
                out_of_state: 'false',
                call_attempt: '0',
            }
        }
    },
    formDetailsObj: {
        callStatus: 'Not Reachable'
    },
    verifyAfterExec: {
        migrationDetails: {
            query: migrationMasterSQ,
            data: {
                gvk_call_status: gvkVerificationConstant.callToBeProccessed,
                schedule_date: 'next',
                modified_on: 'now',
                migrated_location_not_known: 'true',
                location_migrated_to: '',
                call_attempt: '1',
                state: 'REPORTED',
                out_of_state: 'false',
            }
        }
    },
    deleteQuery: migrationMasterDQ
};

module.exports = {
    recordId,

    // Select Queries
    imtMemberSQ,
    callResponseSQ,
    migrationMasterSQ,
    mobileNotificationPendingSQ,
    notificationMasterSQ,
    stateDetailSQ,

    // Inset Queries
    migrationMasterIQ,
    mobileNotificationPendingIQ,
    notificationMasterIQ1,
    notificationMasterIQ2,
    stateDetailIQ,

    // Update Queries
    migrationMasterUQ,

    // Delete Queries
    callResponseDQ,
    migrationMasterDQ,
    mobileNotificationPendingDQ,
    notificationMasterDQ,
    stateDetailDQ,

    // Test Data Objects
    mmo001,
    mmo002,
    mmo003,
    mmo004,
    mmo005,
    mmo006
};
