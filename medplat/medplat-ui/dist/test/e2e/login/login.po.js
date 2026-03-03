const EC = protractor.ExpectedConditions;
const config = require('../config');
const cssIdentifiers = require('./login.es');
const {
    getQueryResult,
    clickOnElement,
    waitForLoading
} = require('../common/utils');

module.exports = {
    login,
    logout
};

/**
 * Login to the techo webapp with provided details
 *
 * @param {String} username Username
 * @param {String} password Password
 */
async function login(username, password) {
    await browser.get(config.TEST_TARGET);
    await browser.wait(EC.visibilityOf(element(cssIdentifiers.usernameElement)), 10000, 'Username element not visible.');
    await element(cssIdentifiers.usernameElement).clear().sendKeys(username);
    await element(cssIdentifiers.passwordElement).clear().sendKeys(password);
    await browser.wait(EC.elementToBeClickable(element(cssIdentifiers.loginBtn)), 20000, 'Login button not clickable.');
    await element(cssIdentifiers.loginBtn).click();
    await waitForLoading();
    return browser.wait(EC.urlIs(`${config.TEST_TARGET}techo/dashboard/webtasks/`), 30000, 'Webtasks page is not open.');
}

/**
 * Logout from the system
 */
async function logout() {
    await clickOnElement(cssIdentifiers.userDropdown);
    await clickOnElement(cssIdentifiers.logoutBtn);
    return browser.wait(EC.visibilityOf(element(cssIdentifiers.usernameElement)), 20000, 'Unable to logout properly.');
}
