<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
    
<!DOCTYPE html>
<html ng-app="jakdukApp">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Lightbox Test &middot; <spring:message code="common.jakduk"/></title>
<jsp:include page="../include/html-header.jsp"/>

<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/angular-loading-bar/build/loading-bar.min.css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/angular-bootstrap-lightbox/dist/angular-bootstrap-lightbox.min.css">	

</head>
<body>
<div class="wrapper" ng-controller="sampleCtrl">
	<jsp:include page="../include/navigation-header.jsp"/>
	
	<div class="row margin-bottom-40">
                <!-- Begin Easy Block v2 -->                
                <div class="col-md-3 col-sm-6 md-margin-bottom-20" ng-repeat="image in galleries">
                    <div class="simple-block">
    	<img class="img-responsive img-bordered margin-bottom-10" ng-src="<%=request.getContextPath()%>/gallery/thumbnail/{{image.id}}" alt="{{image.name}}"
    	ng-click="openLightboxModal($index)">
                        <p>{{image.name}}</p>
                    </div>  
                </div>
                <!-- End Simple Block -->
            </div>
	
</div>
<!-- Bootstrap core JavaScript
  ================================================== -->
<!-- Placed at the end of the document so the pages load faster -->
<script src="<%=request.getContextPath()%>/resources/jquery/dist/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/resources/angular-bootstrap/ui-bootstrap-tpls.min.js"></script>
<script src="<%=request.getContextPath()%>/resources/angular-loading-bar/build/loading-bar.min.js"></script>
<script src="<%=request.getContextPath()%>/resources/angular-bootstrap-lightbox/dist/angular-bootstrap-lightbox.min.js"></script>

<script type="text/javascript">
var jakdukApp = angular.module('jakdukApp', ['bootstrapLightbox']);

jakdukApp.config(function (LightboxProvider) {
	  LightboxProvider.getImageUrl = function (image) {
		  console.log(image);
		    return '<c:url value="/gallery/' + image.id + '"/>';
		  };

		  LightboxProvider.getImageCaption = function (image) {
		    return image.name;
		  };	
});

jakdukApp.controller('sampleCtrl', function($scope, $http, Lightbox) {
	$scope.galleriesConn = "none";
	$scope.galleries = [];    
	
	angular.element(document).ready(function() {
		$scope.getGalleries();
	});
	
	$scope.getGalleries = function() {
		var bUrl = '<c:url value="/gallery/data/list.json"/>';
		
		if ($scope.galleriesConn == "none") {
			
			var reqPromise = $http.get(bUrl);
			
			$scope.galleriesConn = "loading";
			
			reqPromise.success(function(data, status, headers, config) {
				
				$scope.galleries = data.galleries;
				console.log(data);
				
				$scope.galleriesConn = "none";
				
			});
			reqPromise.error(function(data, status, headers, config) {
				$scope.galleriesConn = "none";
				$scope.error = '<spring:message code="common.msg.error.network.unstable"/>';
			});
		}
	};	
	
	  $scope.openLightboxModal = function (index) {
		    Lightbox.openModal($scope.galleries, index);
		  };	
});
</script>

<jsp:include page="../include/body-footer.jsp"/>
</body>
</html>