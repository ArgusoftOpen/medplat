module.exports = {
    ttInjectionReceivedStatusRadioBtn: {
        'yes': by.dataTest('tt-injection-received-status-yes-radio-btn'),
        'no': by.dataTest('tt-injection-received-status-no-radio-btn'),
        'cant-derermine': by.dataTest('tt-injection-received-status-cant-derermine')
    },
    childServiceVaccinationStatusRadioBtn: {
        'yes': by.dataTest('child-service-vaccination-status-yes-radio-btn'),
        'no': by.dataTest('child-service-vaccination-status-no-radio-btn')
    },
    babyDeliveredRadioBtn: {
        'yes': by.dataTest('baby-delivered-yes-radio-btn'),
        'no': by.dataTest('baby-delivered-no-radio-btn')
    },
    totalChildInput: by.dataTest('total-child-input'),
    maleChildCountInput: by.dataTest('male-child-count-input'),
    femaleChildCountInput: by.dataTest('female-child-count-input'),
    callPersonNameInput:by.dataTest('call-person-name-input'),

    // selector of delevery place
    deliveryPlaceDropDown: by.dataTest('delivery-place-status'),

    // maximum-minimum error
    childCountValidationError: by.dataTest('input-Error'),

}

