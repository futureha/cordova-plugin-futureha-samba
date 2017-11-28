
var exec = require('cordova/exec');

var PLUGIN_NAME = 'SambaPlugin';

cordova.plugins.SambaPlugin = {
  echo: function (phrase, cb) {
    exec(cb, null, PLUGIN_NAME, 'echo', [phrase]);
  },
  getDate: function (cb) {
    exec(cb, null, PLUGIN_NAME, 'getDate', []);
  },
  getFiles: function (array, successCallback, errorCallback) {
    cordova.exec(successCallback, errorCallback, PLUGIN_NAME, "getFiles", array);
  }
};

module.exports = SambaPlugin;
