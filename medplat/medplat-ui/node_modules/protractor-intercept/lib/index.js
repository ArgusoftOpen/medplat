'use strict';

var _createClass = require('babel-runtime/helpers/create-class')['default'];

var _classCallCheck = require('babel-runtime/helpers/class-call-check')['default'];

Object.defineProperty(exports, '__esModule', {
  value: true
});

var _browserScriptsJs = require('./browser-scripts.js');

var Intercept = (function () {
  function Intercept(browser) {
    _classCallCheck(this, Intercept);

    this._browser = browser;
  }

  _createClass(Intercept, [{
    key: 'addListener',
    value: function addListener() {
      return this._browser.executeScript(_browserScriptsJs.openListener);
    }
  }, {
    key: 'getRequests',
    value: function getRequests() {
      return this._browser.executeScript(_browserScriptsJs.getRequests);
    }
  }, {
    key: 'removeListener',
    value: function removeListener() {
      return this._browser.executeScript(_browserScriptsJs.removeListener);
    }
  }]);

  return Intercept;
})();

exports['default'] = Intercept;
module.exports = exports['default'];
//# sourceMappingURL=index.js.map