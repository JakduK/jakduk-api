;(function(app) {
'use strict';

/* Before Unload Service */
app.provider('BeforeUnload', function() {
  var _leavingPageText = "You'll lose your changes if you leave";
  var _leavingPageText2 = "Are you sure you want to leave this page?";
  var _turnOffConfirm = false;
  var _debugCallback = angular.noop;

  this.development = function(config) {
    _turnOffConfirm = config;
    return this;
  };

  this.setLeavingText = function(text1, text2) {
    _leavingPageText = text1;
    _leavingPageText2 = text2;
    return this;
  };

  this.debugCallback = function(callback) {
    _debugCallback = callback;
    return this;
  };

  this.$get = ['$window', function($window) {
    return {
      init: function(top, bottom) {
        _leavingPageText = top || _leavingPageText;
        _leavingPageText2 = bottom || _leavingPageText2;

        if (!_turnOffConfirm) {
          $window.onbeforeunload = function(e){
            return _leavingPageText;
          }
        }
        return function(event, confirmCallback, cancelCallback) {
          if (_turnOffConfirm) {
            confirmCallback();
            _debugCallback();
            $window.onbeforeunload = null;
            return;
          }
          if (confirm(leavingPageText + "\n\n"+leavingPageText2)) {
            // OK
            $window.onbeforeunload = null;
            callback();
          } else {
            // Cancel
            event.preventDefault();
            cancelCallback();
          }
        }; // return func
      } // end init
    } // end return

  }];
});


}(angular.module('angular-beforeunload')));