const _ = require('lodash');
const Intercept = require('protractor-intercept');
const intercept = new Intercept(browser);
const loginPage = require('../login/login.po');

module.exports = {
    beforeEach,
    afterEach,
    afterAll,
};

async function beforeEach() {
    await browser.manage().logs().get('browser');
    await intercept.addListener();
}

async function afterEach() {

    let failTest = false;
    const logErr = [];
    const xhrErr = [];
    const browserLogs = await browser.manage().logs().get('browser');

    // it will silent 'http request failed' & external application's API errors
    _.remove(browserLogs, browserLog => {
        let match = false;
        if (
            browserLog.message &&
            (browserLog.message.indexOf('http request failed') > -1
                || browserLog.message.indexOf('imtecho-ui/styles/css') > 1
                || browserLog.message.indexOf('angularjs-datepicker/angular-datepicker.js') > -1)
        ) {
            match = true;
        }
        return match;
    });

    // It will silent errors in negative scenario test cases
    if (this.silents && this.silents.length > 0) {
        this.silents.push(40);
    } else {
        this.silents = [40];
    }

    _.remove(browserLogs, browserLog => {
        let match = false;
        this.silents.forEach((code) => {
            if (browserLog.message && browserLog.message.indexOf(`the server responded with a status of ${code}`) > -1) {
                match = true;
            }
        });
        return match;
    });

    browserLogs.forEach((log) => {
        if ((log.level.value > 900) && !(this.silents && this.silents.length > 0)) { // it's an error log
            logErr.push(log.message);
            failTest = true;
        }
    });

    const reqs = await intercept.removeListener();

    _.forEach(reqs, function (req) {
        const reqFinal = _.omit(req, ['responseText', 'response']);
        if ((reqFinal.status < 200 || reqFinal.status > 299) && reqFinal.status !== 0 && this.silents && this.silents.indexOf(String(reqFinal.status)) === -1) {
            failTest = true;
            xhrErr.push(`Ajax request fail : Requested URL = '${reqFinal.responseURL}' Status = '${reqFinal.status}'`);
        }
    });

    if (failTest) {
        let failMsg = [];
        let msgPrefix = '';
        let msgSuffix = '';
        if (logErr.length > 0) {
            failMsg.push(`Log Errors\n\t' ${logErr.join('\n\t')}`);
            msgPrefix = ' liTagOpen Failed: ';
            msgSuffix = ' liTagClose ';
        }
        if (xhrErr.length > 0) {
            failMsg.push(`${msgPrefix} 'XHR Requests\n\t' ${xhrErr.join('\n\t')} ${msgSuffix}`);
        }
        failMsg = failMsg.join('\n');
        fail(failMsg);
    }
}

async function afterAll() {
    return loginPage.logout();
}
