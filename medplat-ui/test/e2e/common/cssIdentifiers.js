module.exports = {
    loadingIcon: by.css('.loadmask'),
    toggleCallingButton: by.dataTest('toggle-calling'),
    haventDecidedCheckbox: by.dataTest('havent-decided-checkbox'),
    submitButton: by.dataTest('submit-btn'),
    submitSchedule: by.dataTest('submit-schedule'),
    noRecordsToVerify: by.cssContainingText('.card-title', 'No Records to Verify'),
    inputTag: by.tagName('input'),

    // call btn
    fhwCallButton: by.dataTest('fhw-call-btn'),
    ashaCallButton: by.dataTest('asha-call-btn'),

    callContactPerson: by.dataTest('contact-person-call-btn'),
    callFamilyHead: by.dataTest('family-head-call-btn'),
    callMother: by.dataTest('mother-call-btn'),
    callBeneficiary: by.dataTest('member-call-btn'), // same for callBeneficiary
    callFamilyMember: by.dataTest('family-member-call-btn'),
    

    // Date Time identifiers
    nextRangeBtn: by.css('.btn.uib-left'),
    dateTableTitle: by.css('.btn.uib-title'),
    yearSelector: 'td.uib-year',
    monthSelector: 'td.uib-month',
    dateSelector: 'td.uib-day',
    hoursInput: by.model('hours'),
    minutesInput: by.model('minutes'),
    datepicker: by.dataTest('date-picker'),

    // Menu identifiers
    navMenuAdmin: by.dataTest('menu-admin'),
    navMenuFhs: by.dataTest('menu-fhs'),

    // Sub menu identifiers
    navSubMenuQueryBuilder: by.dataTest('sub-menu-Query Builder'),
    navSubMenuCCV: by.dataTest('sub-menu-Call Centre Verification'),
    navSubMenuReports: by.dataTest('sub-menu-Reports'),
    navSubMenuFeatures: by.dataTest('sub-menu-Features'),

    // execute query identifiers
    addQueryButton: by.dataTest('add-query-btn'),
    enterQueryTextBox: by.dataTest('enter-query-textbox'),
    runQueryButton: by.dataTest('run-query-btn'),
    closeQueryTabButton: by.dataTest('close-query-tab-btn'),

    // toaster identifiers
    successToaster: by.className('toast-success'),
    warningToaster: by.className('toast-warning'),
    errorToaster: by.className('toast-error'),

    // Others
    filterToggleBtn: by.dataTest('filter-toggle'),
    menuRepeater: by.repeater('menu in layout.menuList.training'),
    reportForSelection: by.dataTest('report-Report for selection'),
    searchBox: by.dataTest('search-report'),
    searchBtn: by.dataTest('search-btn'),
    selectQueryInput: by.dataTest('query-input'),
    tableHeadIdentifier: by.css(`#printableReport thead tr th`),
    tableRowIdentifier: by.css(`#printableReport tbody tr td`),
    optionElementSelector: by.tagName('option'),
    callStatusDropDown: by.id('callStatus'),

    // confirmation model
    confirmationBtn: {
        'no': by.dataTest('confimation-modal-no-btn'),
        'yes': by.dataTest('confimation-modal-yes-btn'),
    },

    // common button
    closeBtn: by.dataTest('close-btn'),
    updateBtn: by.dataTest('update-btn'),
    cancelBtn: by.dataTest('cancel-btn'),
};
