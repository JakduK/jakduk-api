<!doctype html>
<html ng-app="ui.bootstrap.demo">
  <head>
    <script src="//ajax.googleapis.com/ajax/libs/angularjs/1.2.16/angular.js"></script>
    <script src="//angular-ui.github.io/bootstrap/ui-bootstrap-tpls-0.12.0.js"></script>
    <link href="//netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap.min.css" rel="stylesheet">
  </head>
  <body>

<div ng-controller="PopoverDemoCtrl">
    <h4>Dynamic</h4>
    <div class="form-group">
      <label>Popup Text:</label>
      <input type="text" ng-model="dynamicPopover" class="form-control">
    </div>
    <div class="form-group">
      <label>Popup Title:</label>
      <input type="text" ng-model="dynamicPopoverTitle" class="form-control">
    </div>
    <button popover="{{dynamicPopover}}" popover-title="{{dynamicPopoverTitle}}" class="btn btn-default">Dynamic Popover</button>
    
    <hr />
    <h4>Positional</h4>
    <button popover-placement="top" popover="On the Top!" class="btn btn-default">Top</button>
    <button popover-placement="left" popover="On the Left!" class="btn btn-default">Left</button>
    <button popover-placement="right" popover="On the Right!" class="btn btn-default">Right</button>
    <button popover-placement="bottom" popover="On the Bottom!" class="btn btn-default">Bottom</button>
    
    <hr />
    <h4>Triggers</h4>
    <p>
      <button popover="I appeared on mouse enter!" popover-trigger="mouseenter" class="btn btn-default">Mouseenter</button>
    </p>
    <input type="text" value="Click me!" popover="I appeared on focus! Click away and I'll vanish..."  popover-trigger="focus" class="form-control">

    <hr />
    <h4>Other</h4>
    <button Popover-animation="true" popover="I fade in and out!" class="btn btn-default">fading</button>
    <button popover="I have a title!" popover-title="The title." class="btn btn-default">title</button>
</div>
<script type="text/javascript">
angular.module('ui.bootstrap.demo', ['ui.bootstrap']);
angular.module('ui.bootstrap.demo').controller('PopoverDemoCtrl', function ($scope) {
  $scope.dynamicPopover = 'Hello, World!';
  $scope.dynamicPopoverTitle = 'Title';
});
</script>
  </body>
</html>
