<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>    
    
<!DOCTYPE html>
<html ng-app="jakdukApp">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<jsp:include page="../include/html-header.jsp"/>

<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/unify/assets/plugins/fancybox/source/jquery.fancybox.css">

<script src="<%=request.getContextPath()%>/resources/jquery/dist/jquery.min.js"></script>
</head>
<body>
<div class="container" ng-controller="GalleryCtrl">
<jsp:include page="../include/navigation-header.jsp"/>

    <ul>
      <li ng-repeat="phone in phones">
        <p> - </p>
        <a href ng-click="open_fancybox()">See more</a>
        <div class="wrapper" fancybox>
        <div>
          <p>This will be the content injected in fancybox.</p>
          <a href="#" ng-click="alert(phone)">Now links are connected to the scope.</a>
        </div>
        </div>

      </li>
    </ul>
<a href="http://farm8.staticflickr.com/7367/16426879675_e32ac817a8_b.jpg" title="Codirosso spazzacamino (Massimo Greco _Foligno)">
	<img src="http://farm8.staticflickr.com/7367/16426879675_e32ac817a8_m.jpg" alt="" />
</a>

<div ng-repeat="gallery in galleries">
<a class="ng-fancybox" img="<%=request.getContextPath()%>/gallery/{{gallery.id}}" thumbnail="<%=request.getContextPath()%>/gallery/thumbnail/{{gallery.id}}">
</a>
</div>

    
<ul class="list-unstyled row portfolio-box">
                <li class="col-sm-4 md-margin-bottom-50">
                    <a class="thumbnail fancybox-button zoomer" data-rel="fancybox-button" title="Project Title" href="http://farm8.staticflickr.com/7367/16426879675_e32ac817a8_b.jpg">
                        <img class="full-width img-responsive" src="<%=request.getContextPath()%>/resources/unify/assets/img/main/21.jpg" alt="">
                        <span class="portfolio-box-in">
                            <i class="rounded-x icon-magnifier-add"></i>
                        </span>
                    </a>    
                    <div class="headline-left margin-bottom-10"><h3 class="headline-brd">Project One</h3></div>
                    <small class="project-tag"><i class="fa fa-tag"></i><a href="#">Technology</a>, <a href="#">Business</a></small>
                    <p>Donec id elit non mi porta gravida at eget metus. Fusce dapibus, justo sit amet </p>
                </li>
                <li class="col-sm-4 md-margin-bottom-50">
                    <a class="thumbnail fancybox-button zoomer" data-rel="fancybox-button" title="Project Title" href="assets/img/main/22.jpg">
                        <img class="full-width img-responsive" src="assets/img/main/22.jpg" alt="">
                        <span class="portfolio-box-in">
                            <i class="rounded-x icon-magnifier-add"></i>
                        </span>
                    </a>    
                    <div class="headline-left margin-bottom-10"><h3 class="headline-brd">Project Two</h3></div>
                    <small class="project-tag"><i class="fa fa-tag"></i><a href="#">Technology</a>, <a href="#">Business</a></small>
                    <p>Donec id elit non mi porta gravida at eget metus. Fusce dapibus, justo sit amet </p>
                </li>
                <li class="col-sm-4">
                    <a class="thumbnail fancybox-button zoomer" data-rel="fancybox-button" title="Project Title" href="assets/img/main/23.jpg">
                        <img class="full-width img-responsive" src="assets/img/main/23.jpg" alt="">
                        <span class="portfolio-box-in">
                            <i class="rounded-x icon-magnifier-add"></i>
                        </span>
                    </a>    
                    <div class="headline-left margin-bottom-10"><h3 class="headline-brd">Project Three</h3></div>
                    <small class="project-tag"><i class="fa fa-tag"></i><a href="#">Technology</a>, <a href="#">Business</a></small>
                    <p>Donec id elit non mi porta gravida at eget metus. Fusce dapibus, justo sit amet </p>
                </li>
            </ul>    

<ul class="list-unstyled row portfolio-box-v2">
        <li class="col-sm-3" ng-repeat="gallery in galleries">
        
<a href="<%=request.getContextPath()%>/gallery/{{gallery.id}}" title="Codirosso spazzacamino (Massimo Greco _Foligno)">
	<img ng-src="<%=request.getContextPath()%>/gallery/thumbnail/{{gallery.id}}" alt="" />
</a>
                
        </li>
        <li class="col-sm-3">
            <div class="fancybox-button" data-rel="fancybox-button" title="Project Title Two">
                <img class="img-responsive" ng-src="<%=request.getContextPath()%>/resources/unify/assets/img/main/26.jpg" alt="">
                <span class="portfolio-box-v2-in">
                    <i class="rounded-x icon-magnifier-add"></i>
                </span>
            </div>    
        </li>
    </ul>

    

<div class="row">
<div class="media col-md-3" ng-repeat="gallery in galleries">
  <div class="media-left media-middle" fancybox>
      <img ng-click="open_fancybox()" class="media-object" ng-src="<%=request.getContextPath()%>/gallery/thumbnail/{{gallery.id}}" style="width:50px; height:50px;">
  </div>
  <div class="media-body">
    <h5 class="media-heading">{{gallery.name}}</h5>
		<input type="text" class="form-control input-sm col-md-2" placeholder="Input name">
  </div>
</div>
</div>

</div>

<script type="text/javascript" src="<%=request.getContextPath()%>/resources/jquery-migrate/jquery-migrate.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/resources/unify/assets/plugins/fancybox/source/jquery.fancybox.pack.js"></script>
<script type="text/javascript">

var jakdukApp = angular.module("jakdukApp", []);

/*
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
*/

jakdukApp.directive("ngFancybox", [function () {
	return {
		templateUrl: "template01.html",
		restrict: "EAC",
		transclude:true,
		scope: {
			img : "@",
			thumbnail : "@"
		},
		link: function(scope, iElement, iAttrs) {
			jQuery(iElement).fancybox();
		}
	}
}]);

jakdukApp.controller('GalleryCtrl', function($scope, $http) {
		$scope.galleriesConn = "none";
		$scope.galleries = [];    
		
		angular.element(document).ready(function() {
			$scope.getGalleries();
		})
		
        $scope.phones = [{
            'name': 'Nexus S',
            'snippet': 'Fast just got faster with Nexus S.',
            'age': 1
          }, {
            'name': 'Motorola XOOMâ¢ with Wi-Fi',
            'snippet': 'The Next, Next Generation tablet.',
            'age': 2
          }, {
            'name': 'MOTOROLA XOOMâ¢',
            'snippet': 'The Next, Next Generation tablet.',
            'age': 3
          }];

          $scope.alert = function (phone) { window.alert(phone.name); };
    
	$scope.getGalleries = function() {
		var bUrl = '<c:url value="/gallery/data/list.json"/>';
		
		if ($scope.galleriesConn == "none") {
			
			var reqPromise = $http.get(bUrl);
			
			$scope.galleriesConn = "loading";
			
			reqPromise.success(function(data, status, headers, config) {
				
				$scope.galleries = data.galleries;
				//console.log(data);
				
				$scope.galleriesConn = "none";
				
			});
			reqPromise.error(function(data, status, headers, config) {
				$scope.galleriesConn = "none";
				$scope.error = '<spring:message code="common.msg.error.network.unstable"/>';
			});
		}
	};		    
    
});
</script>

<jsp:include page="../include/body-footer.jsp"/>
</body>
</html>