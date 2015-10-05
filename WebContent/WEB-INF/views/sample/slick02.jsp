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

<script src="<%=request.getContextPath()%>/resources/jquery/dist/jquery.min.js"></script>

<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/slick-carousel/slick/slick.css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/slick-carousel/slick/slick-theme.css">

<style>
.slick-prev:before, 
.slick-next:before {
    color: black;    
}
.slick-prev {left:-10px;}
</style>

</head>
<body>
<div class="wrapper">
	<jsp:include page="../include/navigation-header.jsp"/>
	
	<div class="container content" ng-controller="sampleCtrl">

<div class="owl-navigation">
    <div class="customNavigation">
        <a class="owl-btn prev-v1"><i class="fa fa-angle-left"></i></a>
        <a class="owl-btn next-v1"><i class="fa fa-angle-right"></i></a>
    </div>
</div>
                    
    <slick settings="slickConfig" ng-if="dataLoaded">
    
    <div ng-repeat="image in galleries">
    	<div class="simple-block">
			<img style="width:250px; margin:5px;" ng-src="<%=request.getContextPath()%>/gallery/thumbnail/{{image.id}}">
			<p>abcdef</p>
		</div>  
</div>	
    </slick>
<button ng-click="slickConfig.method.slickPrev()">slickPrev()</button>
<button ng-click="slickConfig.method.slickNext()">slickNext()</button>    
	
</div>
	
</div>
<script src="//cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.5/js/bootstrap.min.js"></script>
<script src="//cdnjs.cloudflare.com/ajax/libs/angular.js/1.4.3/angular.min.js"></script>
<script src="<%=request.getContextPath()%>/resources/slick-carousel/slick/slick.min.js"></script>
<script src="<%=request.getContextPath()%>/resources/angular-slick-carousel/dist/angular-slick.min.js"></script>

<script type="text/javascript">

var jakdukApp = angular.module('jakdukApp', ['slickCarousel']);

jakdukApp.controller('sampleCtrl', function($scope, $http) {
	$scope.galleriesConn = "none";
	$scope.galleries = [];
	
	$scope.slickConfig = {
		infinite: false,
		    autoplay: false,
		    draggable: true,
		    //autoplaySpeed: 3000,
		    slidesToScroll : 2,
		    arrows : false,
		    variableWidth: true,
		    //lazyLoad : 'ondemand',
		    //centerPadding: '60px',    
		    method: {},
		    event: {
		        beforeChange: function (event, slick, currentSlide, nextSlide) {
		        },
		        afterChange: function (event, slick, currentSlide, nextSlide) {
		        }
		    }
		};
	
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