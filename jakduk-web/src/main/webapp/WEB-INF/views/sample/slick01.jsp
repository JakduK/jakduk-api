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
<div class="wrapper">
	<jsp:include page="../include/navigation-header.jsp"/>
	
	<div class="container content" ng-controller="sampleCtrl">

<slick infinite="true" slides-to-show="3" slides-to-scroll="3" init-onload="true" data="dataLoaded">
  	<div ng-repeat="image in galleries">
		<img ng-src="<%=request.getContextPath()%>/gallery/thumbnail/{{image.id}}">
	</div>
</slick>
	
 <slick autoplay=true autoplaySpeed=1000 init-onload="true"
 responsive="miniGalleryResponsive" centerMode=true variableWidth=true centerPadding="20px" data="dataLoaded">  
	<div ng-repeat="image in galleries">
		<img ng-src="<%=request.getContextPath()%>/gallery/thumbnail/{{image.id}}">
	</div>
</slick>

</div>
	
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
	
    $scope.miniGalleryResponsive = [
                                    {
                                        breakpoint: 1240,
                                        settings: {
                                            slidesToShow: 4,
                                            slidesToScroll: 4
                                        }
                                    },
                                    {
                                        breakpoint: 800,
                                        settings: {
                                            slidesToShow: 3,
                                            slidesToScroll: 1
                                        }
                                    },
                                    {
                                        breakpoint: 600,
                                        settings: {
                                            slidesToShow: 2,
                                            slidesToScroll: 1
                                        }
                                    },
                                    {
                                        breakpoint: 480,
                                        settings: {
                                            slidesToShow: 1,
                                            slidesToScroll: 1
                                        }
                                    }
                                ];
	
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
				$scope.dataLoaded = true;
				
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