module.exports = {

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
    

    // search member

    searchByMemberIdRadioBtn: by.dataTest('search-member-by-member-id-radio-btn'),
    searchByFamilyIdRadioBtn: by.dataTest('search-member-by-family-id-radio-btn'),
    searchByMobileNumberRadioBtn: by.dataTest('search-member-by-mobile-number-radio-btn'),
    searchByOrgUnitRadioBtn: by.dataTest('search-member-by-organization-unit-radio-btn'),

    // input value
    memberIdInput: by.dataTest('memberId-input'),
    familyIdInput: by.dataTest('familyId-input'),
    mobileNumberInput: by.dataTest('mobileNumber-input'),
    // aadharInput: by.dataTest('aadhar-input'),
    
    searchMobileNumberInFamilyCheckbox: by.dataTest('search-mobile-number-in-family-checkbox'),
    

    // selectMemberCheckbox

    selectMemberCheckbox : by.dataTest('select-member-checkbox'),
    
};
