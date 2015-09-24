<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
    
<!DOCTYPE html>
<html ng-app="jakdukApp">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Slick Test &middot; <spring:message code="common.jakduk"/></title>
<jsp:include page="../include/html-header.jsp"/>

<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/slick-carousel/slick/slick.css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/slick-carousel/slick/slick-theme.css">

</head>
<body>
<div class="wrapper" ng-controller="sampleCtrl">
	<jsp:include page="../include/navigation-header.jsp"/>

<slick lazy-load="ondemand" slides-to-show="3" slides-to-scroll="1" class="slider lazy ng-isolate-scope slick-initialized slick-slider">
	<div class="slick-list draggable" tabindex="0">
	<div class="slick-track" style="width: 2244px; transform: translate3d(-561px, 0px, 0px); opacity: 1;">
	<div class="slick-slide slick-cloned" style="width: 187px;" ng-repeat="image in galleries">
		<div class="image">
			<img ng-src="<%=request.getContextPath()%>/gallery/thumbnail/{{image.id}}" class="slick-loading">
		</div>
	</div>
	
	<div class="slick-slide slick-cloned" style="width: 187px;">
	<div class="image"><img data-lazy="images/lazyfonz5.png" class="slick-loading"></div></div>
	

	</div></div>
      
    <button type="button" class="slick-prev" tabindex="-1" style="display: block;">Previous</button><button type="button" class="slick-next" tabindex="-1" style="display: block;">Next</button></slick>
    <button type="button" class="slick-prev" tabindex="-1" style="display: block;">Previous</button><button type="button" class="slick-next" tabindex="-1" style="display: block;">Next</button>
</slick>	

	
</div>
<!-- Bootstrap core JavaScript
  ================================================== -->
<!-- Placed at the end of the document so the pages load faster -->
<script src="<%=request.getContextPath()%>/resources/jquery/dist/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/resources/slick-carousel/slick/slick.min.js"></script>
<script src="<%=request.getContextPath()%>/resources/angular-slick/dist/slick.min.js"></script>

<script type="text/javascript">
var jakdukApp = angular.module('jakdukApp', ['slick']);

jakdukApp.controller('sampleCtrl', function($scope, $http) {
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
});
</script>

<jsp:include page="../include/body-footer.jsp"/>
</body>
</html>