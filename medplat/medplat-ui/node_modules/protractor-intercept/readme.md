# protractor-intercept [![Build Status](https://travis-ci.org/bassettsj/protractor-intercept.svg?branch=master)](https://travis-ci.org/bassettsj/protractor-intercept)

> Simple Interception of the XMLHttpRequest on the current context.


## Install

```
$ npm install --save protractor-intercept
```


## Usage

```js
var Intercept = require('protractor-intercept');

var intercept = new Intercept(browser);

describe('Intecept XHttpRequests', function() {
  it('can add the listener to your page', function() {
    browser.get('https://docs.angularjs.org/api');
    intercept.addListener();
    element(by.linkText('angular.isFunction')).click();
    intercept.getRequests().then(function(reqs) {
      //make some assertions about what happened here
    });
  });
});
```

## License

MIT © [Steven Bassett](http://bassettsj.me)
