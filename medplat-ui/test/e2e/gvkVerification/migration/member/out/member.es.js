module.exports = {
    // fhwCallButton: by.dataTest('fhw-call-btn'),
    // ashaCallButton: by.dataTest('asha-call-btn'),
    // memberCallButton: by.dataTest('member-call-btn'),
    migrationOutResolution: by.cssContainingText('.card-title', 'Migrate Out – Resolution'),
    noRecordsToVerify: by.cssContainingText('.card-title', 'No Records to Verify'),

    // Dropdown selectors
    resolutionDropdown: by.dataTest('select-resolution'),

    locationSelector: {
        state: by.dataTest('select-State'),
        region: by.dataTest('select-Region'),
        district: by.dataTest('select-District/Corporation'),
        block: by.dataTest('select-Block'),
        phc: by.dataTest('select-PHC'),
        subCenter: by.dataTest('select-Sub Center'),
        village: by.dataTest('select-villages'),
        ashaArea: by.dataTest('select-asha-area'),
    },

    markOutOfStateCheckbox: by.dataTest('mark-out-of-state'),

};
