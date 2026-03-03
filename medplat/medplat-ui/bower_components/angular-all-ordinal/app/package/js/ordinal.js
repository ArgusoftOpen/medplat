/* global angular */

angular
  .module('ordinal', [])
  .factory('ordinalService', function () {
    var ordinal = function (input) {
      var n = input % 100;
      return n === 0 ? 'th' : (n < 11 || n > 13) ?
        ['st', 'nd', 'rd', 'th'][Math.min((n - 1) % 10, 3)] : 'th';
    };
    return {
      ordinal: ordinal
    };
  })
  .filter('ordinal', ['ordinalService', function (ordinalService) {
    return function (input) {
      return input + ordinalService.ordinal(input);
    };
  }])
  .filter('ordinalOnly', ['ordinalService', function (ordinalService) {
    return function (input) {
      return ordinalService.ordinal(input);
    };
  }]);
