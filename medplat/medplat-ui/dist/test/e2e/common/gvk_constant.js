const gvkVerificationConstant = {

    callSuccess: 'com.argusoft.imtecho.gvk.call.success',
    callToBeProccessed: 'com.argusoft.imtecho.gvk.call.to-be-processed',
    callProcessing: 'com.argusoft.imtecho.gvk.call.processing',
    callProcessed: 'com.argusoft.imtecho.gvk.call.processed',
    callUnresponsive: 'com.argusoft.imtecho.gvk.call.unresponsive',
    callCantTalk: 'com.argusoft.imtecho.gvk.call.cant-talk',
    callNotReachable: 'com.argusoft.imtecho.gvk.call.not-reachable',
    wrongMobileNumber: 'com.argusoft.imtecho.gvk.call.wrong-mobile-number',

    highRiskConditionConfirmation: 'com.argusoft.imtecho.gvk.call.confirmation-for-high-risk',
    highRiskConditionConfirmationWithYes: 'com.argusoft.imtecho.gvk.call.confirmaed-highrisk-with-yes',
    highRiskConditionConfirmationWithNo: 'com.argusoft.imtecho.gvk.call.confirmaed-highrisk-with-no',
    highRiskConditionConfirmationPending: 'com.argusoft.imtecho.gvk.call.high-risk-condition-confirmation-pending',

    beneficiaryNotWillingToHelped: 'com.argusoft.imtecho.gvk.call.beneficiary-not-willing-to-helped',
    schedulePending: 'com.argusoft.imtecho.gvk.call.schedule-pending',
    pickedupSchedule: 'com.argusoft.imtecho.gvk.call.pickedup-schedule',
    beneficiaryNotPickedupBy108PickedupSchedule: 'com.argusoft.imtecho.gvk.call.beneficiary-not-pickedup-by-108-pickedup-schedule',
    beneficiaryNotVisitedPhcPickedupSchedule: 'com.argusoft.imtecho.gvk.call.beneficiary-not-visited-phc-pickedup-schedule',
    beneficiaryReceivedBloodLastWeek: 'com.argusoft.imtecho.gvk.call.beneficiary-received-blood-lastweek',
    beneficiaryReceivedFCMLastWeek: 'com.argusoft.imtecho.gvk.call.beneficiary-received-fcm-lastweek',
    beneficiaryReceivedDrugsForHighBP: 'com.argusoft.imtecho.gvk.call.beneficiary-received-drugs-for-highbp',
    schedulePendingForNextVisit: 'com.argusoft.imtecho.gvk.call.schedule-pending-for-next-visit',
    pickedupSchedulePending: 'com.argusoft.imtecho.gvk.call.pickedup-schedule-pending',
    numberBelongsToBeneficiaryHusband: 'com.argusoft.imtecho.gvk.call.number-belongs-to-beneficiary-husband',
    numberBelongsToBeneficiary: 'com.argusoft.imtecho.gvk.call.number-belongs-to-beneficiary',
    numberBelongsToOtherFamilyMember: 'com.argusoft.imtecho.gvk.call.number-belongs-to-other-family-member',
    numberBelongsToAshaOrAshaHasband: 'com.argusoft.imtecho.gvk.call.number-belongs-to-ashah-or-asha-husband',
    numberBelognsToANMOrANMhasband: 'com.argusoft.imtecho.gvk.call.number-belongs-to-anm-or-anm-husband',
    noNuberAvailable: 'com.argusoft.imtecho.gvk.call.no-number-available',

    childAdmittedToSNCU: 'com.argusoft.imtecho.gvk.call.processed-child-addmited-to-sncu-sam',
    beneficiaryPickedupScheduleForNextVisit: 'com.argusoft.imtecho.gvk.call.pickedup-schedule-for-next-visit',
    beneficiaryNotInterestedToReschedulePickupDate: 'com.argusoft.imtecho.gvk.call.beneficiary-not-interested-to-reschedule-pickup-date',
    childAdmittedToCMTC: 'com.argusoft.imtecho.gvk.call.processed-child-addmited-to-cmtc-lbw',

    processedAnemia: 'com.argusoft.imtecho.gvk.call.processed-anemia',
};

const gvkFeatureConstant = {
    canFamilyVerification: 'canFamilyVerification',
    canAbsentVerification: 'canAbsentVerification',
    canImmunisationVerification: 'canGvkImmunisationVerification',
    canHighriskFollowupVerification: 'canHighriskFollowupVerification',
    canHighriskFollowupVerificationForFhw: 'canHighriskFollowupVerificationFowFhw',
    canPregnancyRegistrationsVerification: 'canPregnancyRegistrationsVerification',
    canPregnancyRegistrationsPhoneNumberVerification: 'canPregnancyRegistrationsPhoneNumberVerification',
    canMemberMigrationOutVerification: 'canMemberMigrationOutVerification',
    canMemberMigrationInVerification: 'canMemberMigrationInVerification',
    canDuplicateMemberVerification: 'canDuplicateMemberVerification',
    canPerformEligibleCoupleCounselling: 'canPerformEligibleCoupleCounselling',
    canPerformAnmVerification: 'canBeneficiaryServiceVerification',
    canFamilyMigrationOutVerification: 'canFamilyMigrationOutVerification',
}

const memberState = {
    archived: 'com.argusoft.imtecho.member.state.archived',
    verified: 'com.argusoft.imtecho.member.state.verified',
    new: 'com.argusoft.imtecho.member.state.new',
    migrated: 'com.argusoft.imtecho.member.state.migrated',
    temporary: 'com.argusoft.imtecho.member.state.temporary',
}

const familyState = {
    migrated: 'com.argusoft.imtecho.family.state.migrated',
}

// GVK feature wise constant for manage_call_master table.
const gvkCallTypeConstnant = {
    FAMILY_VERIFICATION: 'FAM_VERI',
    ABSENT_VERIFICATION: 'ABS_VERI',
    IMMUNISATION_VERIFICATION: 'IMMUN_VERI',
    HIGHRISK_FOLLOWUP_VERIFICATION: 'HIGH_RISK_VERI',
    HIGHRISK_FOLLOWUP_VERIFICATION_FOR_FHW: 'FHW_HIGH_RISK_CONF',
    PREGNANCY_REGISTRATIONS_VERIFICATION: 'PREG_REGI_VERI',
    PREGNANCY_REGISTRATIONS_PHONE_NUMBER_VERIFICATION: 'PREG_REGI_PHONE_NUM_VERI',
    MEMBER_MIGRATION_OUT_VERIFICATION: 'MIG_OUT_VERI',
    FAMILY_MIGRATION_OUT_VERIFICATION: 'FAMILY_MIG_VERI',
    MEMBER_MIGRATION_IN_VERIFICATION: 'MIG_IN_VERI',
    DUPLICATE_MEMBER_VERIFICATION: 'DUP_MEM_VERI',
    PERFORM_ELIGIBLE_COUPLE_COUNSELLING: 'ELIG_COUP_COUN',
    FHW_TT_VERIFICATION: 'FHW_TT_VERI',
    FHW_DELIVERY_VERIFICATION: 'FHW_DEL_VERI',
    FHW_CHILD_SERVICE_PREGNANCY_VERIFICATION:'FHW_CH_SER_PREG_VERI'
}

module.exports = {
    gvkVerificationConstant,
    gvkFeatureConstant,
    memberState,
    familyState,
    gvkCallTypeConstnant,
}