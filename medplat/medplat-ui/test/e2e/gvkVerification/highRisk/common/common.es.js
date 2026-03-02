
module.exports = {
    
    callStatusDropDown: by.id('callStatus'),
    dateNotDecidedCheckBox: by.dataTest('date-not-decided-checkbox'),


    beneficiaryWillingToHelpedButton: {
        true: by.dataTest('beneficiary-willing-to-helped-yes-btn'),
        false: by.dataTest('beneficiary-willing-to-helped-no-btn')
    },
    verifiedByFhwButton: {
        true: by.dataTest('verified-by-fhw-yes-btn'),
        false: by.dataTest('verified-by-fhw-no-btn')
    },

    // For 3rd Screen
    scheduleDateInput: by.dataTest('schedule-date-input'),
    is108PickedupBeneficiaryRadioBtn: {
        true: by.dataTest('is-108-pickedup-beneficiary-true-radio-btn'),
        false: by.dataTest('is-108-pickedup-beneficiary-false-radio-btn'),
    },
    isBeneficiaryWantReschedulePickupDateRadioBtn: {
        true: by.dataTest('is-beneficiary-want-reschedule-pickup-date-true-radio-btn'),
        false: by.dataTest('is-beneficiary-want-reschedule-pickup-date-false-radio-btn')
    },
    isBeneficiaryVisitedPHCRadioBtn: {
        true: by.dataTest('is-beneficiary-visited-phc-true-radio-btn'),
        false: by.dataTest('is-beneficiary-visited-phc-false-radio-btn')
    },

    // for sam
    isBeneficiaryChildAdmittedRadioBtn: {
        true: by.dataTest('is-beneficiary-child-admitted-true-radio-btn'),
        false: by.dataTest('is-beneficiary-child-admitted-false-radio-btn')
    },

    // for anemia

    isBeneficiaryReceivedBloodLastWeekRadioBtn: {
        true: by.dataTest('is-beneficiary-received-boold-last-week-true-radio-btn'),
        false: by.dataTest('is-beneficiary-received-boold-last-week-false-radio-btn'),
    },

    isBeneficiaryReceivedFCMLastWeekRadioBtn: {
        true: by.dataTest('is-beneficiary-received-fcm-last-week-true-radio-btn'),
        false: by.dataTest('is-beneficiary-received-fcm-last-week-false-radio-btn'),
    },

    isBeneficiaryReceivedIronSucroseInjectionRadioBtn: {
        'yes': by.dataTest('is-beneficiary-received-iron-sucrose-injection-yes-radio-btn'),
        'no': by.dataTest('is-beneficiary-received-iron-sucrose-injection-no-radio-btn'),
        'donotKnow': by.dataTest('is-beneficiary-received-fcm-lastweek-dont-know-radio-btn'),
    },

    injectionCountRadioBtn: {
        '1': by.dataTest('injection-count-1-radio-btn'),
        '2': by.dataTest('injection-count-2-radio-btn'),
        '3': by.dataTest('injection-count-3-radio-btn'),
        '4': by.dataTest('injection-count-4-radio-btn'),
        '5': by.dataTest('injection-count-5-radio-btn'),
        '5+': by.dataTest('injection-count-5+-radio-btn'),
    },

    // for high bp 
    isBeneficiaryReceivedDrugsForHighBPRadioBtn: {
        true: by.dataTest('is-beneficiary-received-drugs-for-high-bp-true-radio-btn'),
        false: by.dataTest('is-beneficiary-received-drugs-for-high-bp-false-radio-btn'),
        'donotKnow': by.dataTest('is-beneficiary-received-drugs-for-high-bp-dont-know-radio-btn'),
    },

    // for Low Birth Weight
    isBeneficiaryChildAdmittedForLowBirthWeightRadioBtn: {
        true: by.dataTest('is-beneficiary-child-admitted-for-low-birth-weight-true-radio-btn'),
        false: by.dataTest('is-beneficiary-child-admitted-for-low-birth-weight-false-radio-btn'),
        'donotKnow': by.dataTest('is-beneficiary-child-admitted-for-low-birth-weight-dont-know-radio-btn'),
    },


    isBeneficiaryDropedAtHomeBy108RadioBtn: {
        true: by.dataTest('is-beneficiary-droped-at-home-by-108-yes-radio-btn'),
        false: by.dataTest('is-beneficiary-droped-at-home-by-108-no-radio-btn')
    }
};
