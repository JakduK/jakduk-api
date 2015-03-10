<!DOCTYPE html>
<html ng-app="jakdukApp">

  <head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<jsp:include page="../include/html-header.jsp"/>

    <link data-require="fancybox@2.1.4" data-semver="2.1.4" rel="stylesheet" href="//cdnjs.cloudflare.com/ajax/libs/fancybox/2.1.4/jquery.fancybox.css" />
<script src="<%=request.getContextPath()%>/resources/jquery/dist/jquery.min.js"></script>
  </head>

<div class="container jakduk" ng-controller="MainCtrl">
<jsp:include page="../include/navigation-header.jsp"/>  

    <ul>
      <li ng-repeat="phone in phones">
        <p> - </p>
        <a href ng-click="open_fancybox()">See more</a>
        <div class="wrapper" fancybox style="display: none;">
        <div class="future_fancybox_content">
          <p>This will be the content injected in fancybox.</p>
          <a href="#" ng-click="alert(phone)">Now links are connected to the scope.</a>
        </div>
        </div>

      </li>
    </ul>
</div>    
<script type="text/javascript" src="<%=request.getContextPath()%>/resources/jquery-migrate/jquery-migrate.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/resources/unify/assets/plugins/fancybox/source/jquery.fancybox.pack.js"></script>
    <script type="text/javascript">
    var jakdukApp = angular.module('jakdukApp', []);

    jakdukApp.directive('fancybox', function($compile) {
      return {
        restrict: 'A',
        replace: false,
        link: function($scope, element, attrs) {

          $scope.open_fancybox = function() {

            var el = angular.element(element.html()),

            compiled = $compile(el);

            $.fancybox.open(el);

            compiled($scope);

          };
        }
      };
    });

    jakdukApp.controller('MainCtrl', ['$scope',
      function ControllerZero($scope) {

        $scope.phones = [{
          'name': 'Nexus S',
          'snippet': 'Fast just got faster with Nexus S.',
          'age': 1
        }, {
          'name': 'Motorola XOOM™ with Wi-Fi',
          'snippet': 'The Next, Next Generation tablet.',
          'age': 2
        }, {
          'name': 'MOTOROLA XOOM™',
          'snippet': 'The Next, Next Generation tablet.',
          'age': 3
        }];

        $scope.alert = function (phone) { window.alert(phone.name); };

      }
    ]);        
    </script>
    <jsp:include page="../include/body-footer.jsp"/>
  </body>

</html>
