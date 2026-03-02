const {
    gvkVerificationConstant,
    familyState,
    gvkCallTypeConstnant,
} = require('../../../../common/gvk_constant')

/* eslint-disable camelcase */
const familyId = 'FM/2009/446086';
const recordId = 7551;

// Select Queries
const imtFamilyVerifictionSQ = `select * from imt_family where (basic_state ='VERIFIED' or basic_state='NEW') and  family_id='${familyId}'`;
const familyDetailedSQ = `select * from imt_family where family_id = '${familyId}'`;

const familyMigrationMasterSQ = `select * from imt_family_migration_master where family_id= ${recordId} `;

const gvkFamilyInfoSQ = `select * from gvk_family_migration_info where family_migration_id=(select id from imt_family_migration_master where family_id = ${recordId} limit 1) order by id desc limit 1`;
const gvkFamilyResponseSQ = `select * from gvk_family_migration_response where family_id= '${familyId}' order by id desc limit 1`;
const callMasterSQ = `select * from gvk_manage_call_master where family_id= '${familyId}' order by id desc limit 1`;

const techoNotificationSQ = `select * from techo_notification_master where family_id=${recordId} order by id desc limit 1`;

// Insert Queries
const familyMigrationMasterIQ = `INSERT INTO public.imt_family_migration_master(
    family_id, is_split_family, split_family_id, is_current_location, member_ids, state, type, out_of_state, migrated_location_not_known, location_migrated_to, location_migrated_from, area_migrated_to, area_migrated_from, nearest_loc_id, village_name, fhw_asha_name, fhw_asha_phone, other_information, reported_on, reported_by, 
    reported_location_id, confirmed_on, confirmed_by, mobile_data, created_on, created_by, modified_on, modified_by)
VALUES (${recordId}, null, null, false, null, 'REPORTED', 'OUT', false, true, null, 2078, null, 
    2090, null, null, null, null, null, (current_date - 3), 59920, 
    2090, (current_date - 2), 59920, null, (current_date - 1), 59920, (current_date - 1), 59920);`;

// Update Queries
const familyMigrationUQ = `update gvk_family_migration_info set modified_on=(localtimestamp - interval '3 hour'), schedule_date=(localtimestamp - interval '3 hour') where family_migration_id= (select id from imt_family_migration_master where family_id = ${recordId});`;
const familyStateUQ = `update imt_family set state = '${familyState.migrated}' where family_id = '${familyId}'`;

// Delete Queries
const familyMigrationMasterDQ = `delete from imt_family_migration_master where family_id = ${recordId};`;
const techoNotificationMasaterDQ = `delete from techo_notification_master where family_id = ${recordId};`;
const gvkFamilyInfoDQ = `delete from gvk_family_migration_info where family_migration_id=(select id from imt_family_migration_master where family_id = ${recordId} limit 1);`;
const gvkFamilyResDQ = `delete from gvk_family_migration_response where family_id = '${familyId}';`;

// page title
const familyMigrationPageTitle = 'Family Migrate Out – Resolution';



// Test data objects
const familyMigratinOut001 = {
    imtFamilyVerifictionSQ,
    insertQuery: familyMigrationMasterIQ,
    updateQuery: familyMigrationUQ,
    verifyBeforeExec: {
        familyMigrationMasterDetails: {
            query: familyMigrationMasterSQ,
            data: {
                location_migrated_to: '',
                area_migrated_to: '',
                type: 'OUT',
                state: 'REPORTED',
                migrated_location_not_known: 'true'
            }
        },
        gvkFamilyInfoDetail: {
            query: gvkFamilyInfoSQ,
            data: {
                // family_migration_id,
                modified_on: 'now',
                gvk_call_status: gvkVerificationConstant.callProcessing,
                call_attempt: '0',
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
        familyMigrationMasterDetails: {
            query: familyMigrationMasterSQ,
            data: {
                location_migrated_to: '183151',
                area_migrated_to: '183153',
                type: 'OUT',
                state: 'REPORTED',
                migrated_location_not_known: 'false',
                confirmed_on: 'now',
                modified_on: 'now',
            }
        },
        gvkFamilyInfoDetail: {
            query: gvkFamilyInfoSQ,
            data: {
                gvk_call_status: gvkVerificationConstant.callProcessed,
                call_attempt: '1',
                modified_on: 'now'
            }
        },
        callMasterResponse: {
            query: callMasterSQ,
            data: {
                // id
                call_type: gvkCallTypeConstnant.FAMILY_MIGRATION_OUT_VERIFICATION,
                call_response: gvkVerificationConstant.callSuccess,
                modified_on: 'now',
                created_on: 'now',
                // mobile_number' ,
            }
        },
        gvkFamilyResponse: {
            query: gvkFamilyResponseSQ,
            data: {
                performed_action: 'UPDATE_LOCATION',
                gvk_call_response_status: gvkVerificationConstant.callSuccess,
                migration_type: 'OUT',
                modified_on: 'now',
                // manage_call_master_id
            }
        },
        notification: {
            query: techoNotificationSQ,
            data: {
                location_id: '183151',
                state: 'PENDING',
                modified_on: 'now',
                // migration_id
                //notification_type_id
            }
        }
    },
    deleteQuery: { familyMigrationMasterDQ, gvkFamilyInfoDQ, gvkFamilyResDQ, techoNotificationMasaterDQ },
};

const familyMigratinOut002 = {
    imtFamilyVerifictionSQ,
    insertQuery: familyMigrationMasterIQ,
    updateQuery: familyMigrationUQ,
    verifyBeforeExec: {
        familyMigrationMasterDetails: {
            query: familyMigrationMasterSQ,
            data: {
                location_migrated_to: '',
                area_migrated_to: '',
                type: 'OUT',
                state: 'REPORTED',
                migrated_location_not_known: 'true'
            }
        },
        gvkFamilyInfoDetail: {
            query: gvkFamilyInfoSQ,
            data: {
                // family_migration_id,
                modified_on: 'now',
                gvk_call_status: gvkVerificationConstant.callProcessing,
                call_attempt: '0',
            }
        }
    },
    formDetailsObj: {
        resolution: 'Mark as LFU',
        toCheckToaster: true,
        markOutOfState: true
    },
    verifyAfterExec: {
        familyMigrationMasterDetails: {
            query: familyMigrationMasterSQ,
            data: {
                type: 'OUT',
                state: 'LFU',
                out_of_state: 'true',
                migrated_location_not_known: 'true',
                confirmed_on: 'now',
                modified_on: 'now',
            }
        },
        gvkFamilyInfoDetail: {
            query: gvkFamilyInfoSQ,
            data: {
                gvk_call_status: gvkVerificationConstant.callProcessed,
                call_attempt: '1',
                modified_on: 'now'
            }
        },
        callMasterResponse: {
            query: callMasterSQ,
            data: {
                // id
                call_type: gvkCallTypeConstnant.FAMILY_MIGRATION_OUT_VERIFICATION,
                call_response: gvkVerificationConstant.callSuccess,
                modified_on: 'now',
                created_on: 'now',
                // mobile_number' ,
            }
        },
        gvkFamilyResponse: {
            query: gvkFamilyResponseSQ,
            data: {
                performed_action: 'LFU',
                gvk_call_response_status: gvkVerificationConstant.callSuccess,
                migration_type: 'OUT',
                created_on: 'now',
                modified_on: 'now',
                // manage_call_master_id
            }
        },

    },
    deleteQuery: { familyMigrationMasterDQ, gvkFamilyInfoDQ, gvkFamilyResDQ },
};

const familyMigratinOut003 = JSON.parse(JSON.stringify(familyMigratinOut002));
delete familyMigratinOut003.formDetailsObj.markOutOfState;
familyMigratinOut003.verifyAfterExec.familyMigrationMasterDetails.data.out_of_state = '';

const familyMigratinOut004 = {
    imtFamilyVerifictionSQ,
    insertQuery: familyMigrationMasterIQ,
    updateQuery: familyMigrationUQ,
    verifyBeforeExec: {
        familyMigrationMasterDetails: {
            query: familyMigrationMasterSQ,
            data: {
                location_migrated_to: '',
                area_migrated_to: '',
                type: 'OUT',
                state: 'REPORTED',
                migrated_location_not_known: 'true'
            }
        },
        gvkFamilyInfoDetail: {
            query: gvkFamilyInfoSQ,
            data: {
                // family_migration_id,
                modified_on: 'now',
                gvk_call_status: gvkVerificationConstant.callProcessing,
                call_attempt: '0',
            }
        }
    },
    formDetailsObj: {
        callStatus: 'Not Reachable'
    },
    verifyAfterExec: {
        familyMigrationMasterDetails: {
            query: familyMigrationMasterSQ,
            data: {
                type: 'OUT',
                location_migrated_to: '',
                area_migrated_to: '',
                migrated_location_not_known: 'true',
                state: 'REPORTED'

            }
        },
        gvkFamilyInfoDetail: {
            query: gvkFamilyInfoSQ,
            data: {
                gvk_call_status: gvkVerificationConstant.callToBeProccessed,
                call_attempt: '1',
                modified_on: 'now',
                schedule_date: 'next'
            }
        },
        callMasterResponse: {
            query: callMasterSQ,
            data: {
                // id
                call_type: gvkCallTypeConstnant.FAMILY_MIGRATION_OUT_VERIFICATION,
                call_response: gvkVerificationConstant.callNotReachable,
                modified_on: 'now',
                created_on: 'now',
                // mobile_number' ,
            }
        },
        gvkFamilyResponse: {
            query: gvkFamilyResponseSQ,
            data: {
                performed_action: '',
                gvk_call_response_status: gvkVerificationConstant.callNotReachable,
                migration_type: 'OUT',
                modified_on: 'now',
                // manage_call_master_id
            }
        },

    },
    deleteQuery: { familyMigrationMasterDQ, gvkFamilyInfoDQ, gvkFamilyResDQ },
};


const familyMigratinOut005 = {
    imtFamilyVerifictionSQ,
    insertQuery: familyMigrationMasterIQ,
    updateQuery: familyMigrationUQ,
    verifyBeforeExec: {
        familyMigrationMasterDetails: {
            query: familyMigrationMasterSQ,
            data: {
                location_migrated_to: '',
                area_migrated_to: '',
                type: 'OUT',
                state: 'REPORTED',
                migrated_location_not_known: 'true'
            }
        },
        gvkFamilyInfoDetail: {
            query: gvkFamilyInfoSQ,
            data: {
                // family_migration_id,
                modified_on: 'now',
                gvk_call_status: gvkVerificationConstant.callProcessing,
                call_attempt: '0',
            }
        }
    },
    formDetailsObj: {
        resolution: 'Roll Back',
        toCheckToaster: true,
    },
    verifyAfterExec: {
        familyMigrationMasterDetails: {
            query: familyMigrationMasterSQ,
            data: {
                location_migrated_to: '',
                area_migrated_to: '',
                type: 'OUT',
                state: 'ROLLBACK',
                migrated_location_not_known: 'true',
                confirmed_on: 'now',
                modified_on: 'now',
            }
        },
        gvkFamilyInfoDetail: {
            query: gvkFamilyInfoSQ,
            data: {
                gvk_call_status: gvkVerificationConstant.callProcessed,
                call_attempt: '1',
                modified_on: 'now'
            }
        },
        callMasterResponse: {
            query: callMasterSQ,
            data: {
                // id
                call_type: gvkCallTypeConstnant.FAMILY_MIGRATION_OUT_VERIFICATION,
                call_response: gvkVerificationConstant.callSuccess,
                modified_on: 'now',
                created_on: 'now',
                // mobile_number' ,
            }
        },
        gvkFamilyResponse: {
            query: gvkFamilyResponseSQ,
            data: {
                performed_action: 'ROLLBACK',
                gvk_call_response_status: gvkVerificationConstant.callSuccess,
                migration_type: 'OUT',
                modified_on: 'now',
                // manage_call_master_id
            }
        },
        notification: {
            query: techoNotificationSQ,
            data: {
                state: 'PENDING',
                modified_on: 'now',
                // migration_id
                //notification_type_id
            }
        },
        familyDetailedInfo: {
            query: familyDetailedSQ,
            data: {
                // state:,
                modified_on: 'now',
            }
        }
    },
    familyStateUQ,
    deleteQuery: { familyMigrationMasterDQ, gvkFamilyInfoDQ, gvkFamilyResDQ },
};

const familyMigratinOut006 = {
    imtFamilyVerifictionSQ,
    insertQuery: familyMigrationMasterIQ,
    updateQuery: familyMigrationUQ,
    verifyBeforeExec: {
        familyMigrationMasterDetails: {
            query: familyMigrationMasterSQ,
            data: {
                location_migrated_to: '',
                area_migrated_to: '',
                type: 'OUT',
                state: 'REPORTED',
                migrated_location_not_known: 'true'
            }
        },
        gvkFamilyInfoDetail: {
            query: gvkFamilyInfoSQ,
            data: {
                // migration_id
                modified_on: 'now',
                gvk_call_status: gvkVerificationConstant.callProcessing,
                call_attempt: '0',
            }
        }
    },
    formDetailsObj: {
        callStatus: 'Not Reachable'
    },
    verifyAfterExec: {
        familyMigrationMasterDetails: {
            query: familyMigrationMasterSQ,
            data: {
                type: 'OUT',
                location_migrated_to: '',
                area_migrated_to: '',
                migrated_location_not_known: 'true',
                state: 'REPORTED'

            }
        },
        gvkFamilyInfoDetail: {
            query: gvkFamilyInfoSQ,
            data: {
                gvk_call_status: gvkVerificationConstant.callToBeProccessed,
                call_attempt: '1',
                modified_on: 'now',
                schedule_date: 'next'
            }
        },
        callMasterResponse: {
            query: callMasterSQ,
            data: {
                // id
                call_type: gvkCallTypeConstnant.FAMILY_MIGRATION_OUT_VERIFICATION,
                call_response: gvkVerificationConstant.callNotReachable,
                modified_on: 'now',
                created_on: 'now',
                // mobile_number' ,
            }
        },
        gvkFamilyResponse: {
            query: gvkFamilyResponseSQ,
            data: {
                performed_action: '',
                gvk_call_response_status: gvkVerificationConstant.callNotReachable,
                migration_type: 'OUT',
                modified_on: 'now',
                // manage_call_master_id
            }
        },

    },
    deleteQuery: { familyMigrationMasterDQ, gvkFamilyInfoDQ, gvkFamilyResDQ },
};

module.exports = {
    // record Id
    recordId,
    familyId,

    // page title
    familyMigrationPageTitle,

    // Select Queries
    imtFamilyVerifictionSQ,
    familyMigrationMasterSQ,
    gvkFamilyInfoSQ,
    gvkFamilyResponseSQ,
    techoNotificationSQ,

    // Inset Queries
    familyMigrationMasterIQ,

    // Update Queries
    familyMigrationUQ,
    familyStateUQ,

    // Delete Queries
    familyMigrationMasterDQ,
    techoNotificationMasaterDQ,
    gvkFamilyInfoDQ,
    gvkFamilyResDQ,

    // Test Data Objects
    familyMigratinOut001,
    familyMigratinOut002,
    familyMigratinOut003,
    familyMigratinOut004,
    familyMigratinOut005,
    familyMigratinOut006
};
