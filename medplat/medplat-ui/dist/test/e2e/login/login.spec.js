const loginPage = require('./login.po');
const browserFunctions = require('../common/browser_functions');
const config = require('../config');

describe('Login', () => {

    beforeEach(browserFunctions.beforeEach);

    afterEach(browserFunctions.afterEach);

    it('Login to Techo+ webapp', async function () {
        this.silents = [404, 400];
        await loginPage.login(config.QA_SUPER_ADMIN_USER, config.QA_SUPER_ADMIN_PASSWORD);
    });
});
