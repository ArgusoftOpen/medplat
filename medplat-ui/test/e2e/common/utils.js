const EC = protractor.ExpectedConditions;
const cssIdentifiers = require('./cssIdentifiers');

module.exports = {
    checkToaster,
    clickOnElement,
    dateTimeSelector,
    dropdownListSelector,
    executeQuery,
    getQueryResult,
    navToFeature,
    openSelectQueryTab,
    selectDeselectFeatureAccess,
    sendKeys,
    startCallVerification,
    switchToTabNumber,
    verifyDataInDb,
    verifyQueryResultDataWithDB,
    waitForLoading,
    getDateAfterNumberOfDaysHoursMinutes,
};

/**
 * This function helps in returning the ElementFinder For a particular locator.
 *
 * @param {Object} selector selector to be wrapped in ElementFinder class
 * @returns {ElementFinder}
 */
async function getElementFinder(selector) {
    if (selector instanceof protractor.ElementFinder) {
        return selector;
    }
    if (await element.all(selector).count() > 1) {
        return element.all(selector).first();
    }
    return element(selector);
}

/**
 * Scrolls to given location of element and clicks on it
 *
 * @param {ProtractorLocator | ElementFinder} elem element to be clicked
 * @param {Number=} timeout wait timeout for element
 * @param {String=} failureMessage message to be displayed on failure
 * @param {Number|String=} scrollYAxis Y Axis/Location of element to scroll
 */
async function clickOnElement(elem, failureMessage, scrollYAxis, timeout = 30000) {
    elem = await getElementFinder(elem);
    failureMessage = failureMessage || `Could not click on ${elem.locator()}`;
    if (typeof scrollYAxis !== 'undefined') {
        if (scrollYAxis === 'self') {
            scrollYAxis = (await elem.getLocation()).y - 100;
        }
        await browser.executeScript(`window.scrollTo(0,${scrollYAxis});`);
        await browser.sleep(2000);
    }
    await browser.wait(EC.elementToBeClickable(elem), timeout, failureMessage);
    return elem.click();
}

/**
 * Waits for loading to finish
 *
 * @param {Number=} time Timeout to wait for loading to finish
 * @param {String=} failureMessage Failure message if loading not finished
 */
async function waitForLoading(time = 60000, failureMessage = 'Process is not finished yet.') {
    return browser.wait(EC.invisibilityOf(element(cssIdentifiers.loadingIcon)), time, failureMessage);
}

/*
 * Gets list of all open browser tabs and switches to the tab at the index provided
 *
 * @param {Number} index Index of tab to focus
 */
async function switchToTabNumber(index) {
    await browser.sleep(1500);
    const handles = await browser.getAllWindowHandles();
    handles && handles[index] && await browser.switchTo().window(handles[index]);
    return browser.sleep(1500);
}

/**
 * Opens the tab for executing select query and swtich back to first tab.
 */
async function openSelectQueryTab() {
    await browser.actions().mouseMove(element(cssIdentifiers.navMenuAdmin)).perform();
    await element.all(cssIdentifiers.navSubMenuReports).filter(async elem => {
        return elem.isDisplayed();
    }).click();
    await waitForLoading();
    await browser.wait(EC.invisibilityOf(element(cssIdentifiers.menuRepeater)), 30000, 'Side menu not closed');
    await element(cssIdentifiers.searchBox).clear().click().sendKeys('Report for selection');
    await waitForLoading();
    await clickOnElement(cssIdentifiers.reportForSelection);
    await browser.wait(EC.invisibilityOf(element(cssIdentifiers.errorToaster)), 15000, 'Error toast not closed.');
    return switchToTabNumber(0);
}

// /**
//  * Opens the tab for executing insert, update and delete query and swtich back to first tab.
//  */
// async function openExeculteQueryTab() {
//     await switchToTabNumber(1);
//     await browser.executeScript("return window.open(arguments[0], '_blank')", 'http://localhost:8181/imtecho-ui/#!/techo/manage/query');
//     await waitForLoading();
//     await switchToTabNumber(2);
//     if (await EC.urlIs('http://localhost:8181/imtecho-ui/#!/techo/manage/query')()) {
//         await browser.refresh();
//     } else {
//         await navToFeature(cssIdentifiers.navMenuAdmin, cssIdentifiers.navSubMenuQueryBuilder);
//     }
//     return switchToTabNumber(0);
// }

/**
 * Returns object of the result for the query executed
 *
 * @param {String} query Select query to execute
 * @param {Boolean} isMulti To get multiple records from query
 */
async function getQueryResult(query, isMulti = false) {
    await switchToTabNumber(1);

    // Refreshing the page to remove previous result
    await browser.refresh();
    await waitForLoading();
    await browser.wait(EC.invisibilityOf(element(cssIdentifiers.errorToaster)), 15000, 'Error toast not closed.');

    await clickOnElement(cssIdentifiers.filterToggleBtn);

    await browser.wait(EC.visibilityOf(element(cssIdentifiers.selectQueryInput)), 5000, 'Query input not visible.');
    await element(cssIdentifiers.selectQueryInput).clear().sendKeys(query);
    await clickOnElement(cssIdentifiers.searchBtn);
    await waitForLoading();

    const header = await element.all(cssIdentifiers.tableHeadIdentifier).getText();
    const data = await element.all(cssIdentifiers.tableRowIdentifier).getText();
    const result = header.reduce((acc, curr, index) => {
        if (isMulti) {
            acc[curr] = [];
            let i = index;
            while (i < data.length) {
                acc[curr].push(data[i]);
                i += header.length;
            }
        } else {
            acc[curr] = data[index];
        }
        return acc;
    }, {});

    await switchToTabNumber(0);
    return result;
}

/**
 * execute query (insert, update, delete) .
 *
 * @param {String} query Select query to execute
 */
async function executeQuery(query) {

    if (await EC.urlIs('http://localhost:8181/imtecho-ui/#!/techo/manage/query')()) {
        await browser.refresh();
    } else {
        await navToFeature(cssIdentifiers.navMenuAdmin, cssIdentifiers.navSubMenuQueryBuilder);
    }

    await waitForLoading();
    await clickOnElement(cssIdentifiers.addQueryButton);
    await waitForLoading();
    await element(cssIdentifiers.enterQueryTextBox).clear().sendKeys(query);
    await clickOnElement(cssIdentifiers.runQueryButton);
    await waitForLoading();
    await clickOnElement(cssIdentifiers.closeQueryTabButton);
    return checkToaster('success');
}

/**
 * Navigates to the feature provided
 *
 * @param {ElementFinder} navParent parent menu item to navigate to
 * @param {ElementFinder} navChild child menu item to navigate to
 * @param {ElementFinder} navSubChild child of the child menu item to navigate to
 */
async function navToFeature(navParent, navChild, navSubChild) {
    await browser.actions().mouseMove(element(navParent)).perform();
    if (navSubChild) {
        await browser.actions().mouseMove(element(navChild)).perform();
        await clickOnElement(navSubChild);
    } else {
        await clickOnElement(navChild);
    }
    return waitForLoading();
}

/**
 * Checks for success or error toaster and waits for it to go.
 *
 * @param {Boolean} error To check for error toaster
 */
async function checkToaster(type = 'success') {
    let verb, elem;
    switch (type.toLowerCase()) {
        case 'error':
            verb = 'Error';
            elem = cssIdentifiers.errorToaster;
            break;
        case 'warning':
            verb = 'Warning';
            elem = cssIdentifiers.warningToaster;
            break;
        default:
            verb = 'Success';
            elem = cssIdentifiers.successToaster;
            break;
    }
    await browser.wait(EC.visibilityOf(element(elem)), 10000, `${verb} toaster not found.`);
    return browser.wait(EC.invisibilityOf(element(elem)), 20000, `${verb} toaster is still visible.`);
}

/**
 * Selects the specified option from the given dropdown
 *
 * @param {String} selector Selector to specify the dropdown
 * @param {String} optionValue Text of the option to be selected
 */
async function dropdownListSelector(selector, optionValue) {
    selector = await getElementFinder(selector);
    const optionsFinder = selector.all(cssIdentifiers.optionElementSelector);
    await browser.wait(EC.visibilityOf(selector), 15000, `${selector.locator()} is not present.`);
    await browser.wait(EC.presenceOf(optionsFinder.first()), 20000, `No Options available for selector ${selector.locator()}`);
    await optionsFinder.filter(async (option) => {
        let text = await option.getText();
        text = text.trim();
        return text === optionValue;
    }).first().click();


    //     // console.log('value:', text + ':' + optionValue)
    //     return text == optionValue;

    return browser.sleep(2000);
}

/**
 * Enters the given text in the element
 *
 * @param {ElementFinder|Selector} elem Element in which to enter the text
 * @param {String} text The text to be entered
 * @param {Boolean} clearFirst Whether to clear the element before entering the text or not
 * @param {Number} sleepDuration Number of seconds to wait after entering the text
 */
async function sendKeys(elem, text, clearFirst, sleepDuration = 50) {
    await clickOnElement(elem);
    await browser.switchTo().activeElement().sendKeys(protractor.Key.END);
    await browser.sleep(500);
    if (clearFirst) {
        await browser.switchTo().activeElement().sendKeys(protractor.Key.chord(protractor.Key.CONTROL, 'a'), protractor.Key.BACK_SPACE);
        await browser.sleep(500);
    }
    await browser.switchTo().activeElement().sendKeys(text);
    return browser.sleep(sleepDuration);
}

/**
 * Go to FHS -> Call Centre Verification and start calling
 */
async function startCallVerification() {
    // Going to call verification page and start calling
    await navToFeature(cssIdentifiers.navMenuFhs, cssIdentifiers.navSubMenuCCV);
    await browser.refresh();
    await waitForLoading();
    await clickOnElement(cssIdentifiers.toggleCallingButton, 'Start call button not visible.');
    return waitForLoading();
}

/**
 * Verifies the data in db for the given object
 *
 * @param {String} query Select query to get data
 * @param {Object} data Object containing data to verify
 * @param {Boolean} isMulti To verify for multiple records
 *
 * @returns Array of mismatched entries
 */
async function verifyDataInDb(query, data, isMulti = false) {
    const queryData = await getQueryResult(query, isMulti);
    return verifyQueryResultDataWithDB(queryData, data)

    // const misMatch = [];

    // const currentDate = new Date();
    // let diff;
    // let scheduledDate;

    // for (const column in data) {
    //     switch (data[column]) {
    //         case 'undefined':
    //             if (queryData.hasOwnProperty(column)) {
    //                 misMatch.push(`For Undefined : ${column} Value : ${queryData[column]} TypeOf : ${typeof queryData[column]}`);
    //             }
    //             break;
    //         case 'now':
    //             diff = ((currentDate.getTime() - +queryData.modified_on) / 60000) - 330;
    //             if (diff > 4) {
    //                 misMatch.push(`Modified before ${diff} minutes.`);
    //             }
    //             break;
    //         case 'next':
    //             scheduledDate = new Date(+queryData.modified_on).getDate();
    //             if (currentDate.getDate() !== scheduledDate) {
    //                 misMatch.push(`Pickup is scheduled on ${scheduledDate}.`);

    //             }
    //             break;
    //         case 'truthy':
    //             if (!queryData[column]) {
    //                 misMatch.push(`No value for ${column}.`);

    //             }
    //             break;
    //         default:
    //             if (Array.isArray(data[column])) {
    //                 if (!queryData[column]
    //                     || data[column].toString() !== queryData[column].toString()) {
    //                     misMatch.push(`Expected : ${data[column]} Value : ${queryData[column]}`);
    //                 }
    //             } else if (data[column] !== queryData[column]) {
    //                 misMatch.push(`Expected : ${data[column]} Value : ${queryData[column]}`);
    //             }
    //             break;
    //     }
    // }
    // return misMatch;
}

/**
 * Verifies the data in db for the given object
 *
 * @param {String} query Select query to get data
 * @param {Object} testData Object containing data to verify
 *
 * @returns Array of mismatched entries
 */
async function verifyQueryResultDataWithDB(queryData, data) {
    const misMatch = [];
    const currentDate = new Date();
    let diff;
    let scheduledDate;

    for (const column in data) {
        switch (data[column]) {
            case 'undefined':
                if (queryData.hasOwnProperty(column)) {
                    misMatch.push(`For Undefined : ${column} Value : ${queryData[column]} TypeOf : ${typeof queryData[column]}`);
                }
                break;
            case 'now':
                diff = ((currentDate.getTime() - +queryData.modified_on) / 60000) - 330;
                if (diff > 4) {
                    misMatch.push(`Modified before ${diff} minutes.`);
                }
                break;
            case 'next':
                scheduledDate = new Date(+queryData.modified_on).getDate();
                if (currentDate.getDate() !== scheduledDate) {
                    misMatch.push(`Pickup is scheduled on ${scheduledDate}.`);

                }
                break;
            case 'truthy':
                if (!queryData[column]) {
                    misMatch.push(`No value for ${column}.`);

                }
                break;
            default:
                if (Array.isArray(data[column])) {
                    if (!queryData[column]
                        || data[column].toString() !== queryData[column].toString()) {
                        misMatch.push(`Expected : ${data[column]} Value : ${queryData[column]}`);
                    }
                } else if (data[column] !== queryData[column]) {
                    misMatch.push(`Expected : ${data[column]} Value : ${queryData[column]}`);
                }
                break;
        }
    }
    return misMatch;
}

/**
 * Helper function for year selection in datetimepicker
 *
 * @param {String} yearToSelect Year to select in datepicker
 */
async function _selectYear(year) {
    const yearToSelect = year && `${year}`;
    if (!yearToSelect) {
        return true;
    }

    await clickOnElement(element(cssIdentifiers.dateTableTitle), 'Cannot switch to month view');
    await browser.sleep(1000);
    await clickOnElement(element(cssIdentifiers.dateTableTitle), 'Cannot switch to year view');
    const currentRange = await element(cssIdentifiers.dateTableTitle).getText();
    const maxYear = currentRange.split(' - ')[1];
    if (yearToSelect > +maxYear) {
        await clickOnElement(cssIdentifiers.nextRangeBtn);
    }

    return clickOnElement(by.cssContainingText(cssIdentifiers.yearSelector, yearToSelect), 'Unable to select year.');
}

/**
 * Helper function for month selection in datetimepicker
 *
 * @param {String} monthToSelect month to select in datepicker
 */
async function _selectMonth(month) {
    const months = ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'];

    if (!month) {
        return true;
    }
    if (!months.includes(month)) {
        return fail('Incorrect month for selection');
    }

    const isDateDisplayed = await EC.visibilityOf(element(by.css(cssIdentifiers.dateSelector)))();
    if (isDateDisplayed) {
        await clickOnElement(element(cssIdentifiers.dateTableTitle), 'Cannot switch to month view');
        await browser.sleep(1000);
    }

    return clickOnElement(element(by.cssContainingText(cssIdentifiers.monthSelector, month)), 'Unable to select month.');
}

/**
 * Helper function for date selection in datetimepicker
 *
 * @param {String} dateToSelect date to select in datepicker
 */
async function _selectDate(date) {
    const datesElements = element.all(by.css(cssIdentifiers.dateSelector));
    let dateToSelect = date && `${date}`;

    if (!dateToSelect) {
        dateToSelect = new Date().getDate() + 1;
        dateToSelect = dateToSelect < 10 ? `0${dateToSelect}` : dateToSelect;
    }

    const datesAvailable = await datesElements.getText();
    const indexOfFirst = datesAvailable.indexOf('01');
    const indexOfDateToSelect = +dateToSelect + indexOfFirst - 1;
    const dateElem = datesElements.get(indexOfDateToSelect);

    return clickOnElement(dateElem);
}

/**
 * Selectes provided date and enter the given time. Selects next day if no date provided,
 *
 * @param {Object} dateTimeObject Object having details of time and date to input
 * @param {Number=} dateTimeObject.year full year for input e.g. 2019
 * @param {Number=} dateTimeObject.month full month for input e.g. October
 * @param {Number=} dateTimeObject.date date for input
 * @param {Number=} dateTimeObject.hours hours for input
 * @param {Number=} dateTimeObject.minutes minutes for input
 */
async function dateTimeSelector(dateTimeObject) {
    // Open datepicker
    await browser.wait(EC.presenceOf(element(cssIdentifiers.datepicker)), 10000, 'Datepicker not present');
    await element(cssIdentifiers.datepicker).click();

    // Select year
    await _selectYear(dateTimeObject.year);

    // Select month
    await _selectMonth(dateTimeObject.month);

    // Select Date
    await _selectDate(dateTimeObject.date);

    // Input hours and minutes
    if (dateTimeObject.hours) {
        await sendKeys(cssIdentifiers.hoursInput, dateTimeObject.hours, true, 1000);
        return sendKeys(cssIdentifiers.minutesInput, dateTimeObject.minutes, true, 1000);
    }

    return true;
}

/**
 * Selects or deselects the checkbox as per provided params.
 *
 * @param {String} userName Username to change the feature rights
 * @param {Boolean} isSelection To check for final selected status
 * @param {Array} selectorNameArray Array containing features
 */
async function clickCheckbox(userName, isSelection, selectorNameArray) {
    for (const feature of selectorNameArray) {
        const selector = element(by.dataTest(['tab-fhs', `${userName} ${feature}`]));
        const isChecked = await selector.element(cssIdentifiers.inputTag).isSelected();

        // XOR logic
        // For clicking the checkbox if not checked for select
        // clicking checkbox if checked for deselect
        if (isSelection ^ isChecked) {
            await selector.click();
            await waitForLoading();
        }
    }

    return true;
}

/**
 * Selects/Deselects the feature in Admin -> Features
 *
 * @param {Object} featureNamesObj Object containing details for selection
 * @param {String[]} featureNamesObj.deSelect Array containing the feature names to deselect
 * @param {String[]} featureNamesObj.select Array containing the feature names to select
 */
async function selectDeselectFeatureAccess(featureNamesObj) {
    const userName = featureNamesObj.userName || 'Test Automation';

    await navToFeature(cssIdentifiers.navMenuAdmin, cssIdentifiers.navSubMenuFeatures);

    if (featureNamesObj.deSelect) {
        await clickCheckbox(userName, false, featureNamesObj.deSelect);
    }

    if (featureNamesObj.select) {
        await clickCheckbox(userName, true, featureNamesObj.select);
    }

    // const selectedFeature = await getQueryResult(`select feature_json from user_menu_item where menu_config_id = (select id from menu_config where navigation_state = 'techo.dashboard.gvkverification')
    // and user_id = (select id from um_user where user_name = 'qasuperadmin')`);
    // // let featureJson = JSON.parse(selectedFeature.feature_json);

    // if (!!featureNamesObj.select) {
    //     // expect(featureJson[featureNamesObj.select[0]]).toEqual(true);
    // }

    return true;
}

/**
 * this function will give the date after number of days, hour, minutes from the current-timestamp.
 */
function getDateAfterNumberOfDaysHoursMinutes(day, hour = 0, minutes = 0) {
    let date = new Date(new Date().setDate(new Date().getDate() + day));
    date.setHours(hour);
    date.setMinutes(minutes);
    date.setMilliseconds(0);
    date.setSeconds(0);
    return date.getTime().toString();
}
