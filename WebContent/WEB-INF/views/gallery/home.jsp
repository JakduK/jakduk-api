<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>    
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<!DOCTYPE html>
<html ng-app="jakdukApp">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title><spring:message code="gallery"/> &middot; <spring:message code="common.jakduk"/></title>
	<jsp:include page="../include/html-header.jsp"></jsp:include>
	
	<!-- CSS Page Style -->    
	<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/unify/assets/css/pages/portfolio-v2.css">	
</head>

<body>
<div class="wrapper">
<jsp:include page="../include/navigation-header.jsp"/>

	<!--=== Breadcrumbs ===-->
	<div class="breadcrumbs">
		<div class="container">
			<h1 class="pull-left"><spring:message code="gallery"/></h1>
		</div><!--/container-->
	</div><!--/breadcrumbs-->
	<!--=== End Breadcrumbs ===-->

<div class="container content" ng-controller="galleryCtrl">

<div class="sorting-block">
            <ul style="   " class="row sorting-grid">
                <li style=" display: inline-block; opacity: 1;" class="col-md-3 col-sm-6 col-xs-12 mix category_1 category_3 mix_all" data-cat="1" ng-repeat="gallery in galleries">
                    <a href="<%=request.getContextPath()%>/gallery/view/{{gallery.id}}">
                        <img ng-src="<%=request.getContextPath()%>/gallery/thumbnail/{{gallery.id}}" alt="{{gallery.name}}">
                        <span class="sorting-cover">
                            <span>{{gallery.name}}</span>
                            <p>
			<i class="fa fa-user"></i> {{gallery.writer.username}}
			| <i class="fa fa-eye"></i><strong> {{gallery.views}}</strong>
			| <i class="fa fa-thumbs-o-up text-primary"></i>
			<strong class="text-primary" ng-if="usersLikingCount[gallery.id]">{{usersLikingCount[gallery.id]}}</strong>				
			<strong class="text-primary" ng-if="usersLikingCount[gallery.id] == null">0</strong>
			| <i class="fa fa-thumbs-o-down text-danger"></i>
			<strong class="text-danger" ng-if="usersDislikingCount[gallery.id]">{{usersDislikingCount[gallery.id]}}</strong>				
			<strong class="text-danger" ng-if="usersDislikingCount[gallery.id] == null">0</strong>				
			</p>
                        </span>
                    </a>
                </li>
            </ul>
        
            <div class="clearfix"></div>
        </div>

</div>

<jsp:include page="../include/footer.jsp"/>

</div><!-- /.container -->

<!-- Bootstrap core JavaScript
  ================================================== -->
<!-- Placed at the end of the document so the pages load faster -->
<script src="<%=request.getContextPath()%>/resources/jquery/dist/jquery.min.js"></script>
<!-- JS Implementing Plugins -->
<!-- JS Page Level -->           
<script type="text/javascript" src="<%=request.getContextPath()%>/resources/unify/assets/js/app.js"></script>

<script type="text/javascript">
var jakdukApp = angular.module("jakdukApp", []);

jakdukApp.controller("galleryCtrl", function($scope, $http) {
	$scope.galleriesConn = "none";
	$scope.galleries = [];
	$scope.usersLikingCount = [];
	$scope.usersDislikingCount = [];
	
	angular.element(document).ready(function() {
		$scope.getGalleries();
	});	
	
	$scope.getGalleries = function() {
		var bUrl = '<c:url value="/gallery/list.json"/>';
		
		if ($scope.galleriesConn == "none") {
			
			var reqPromise = $http.get(bUrl);
			
			$scope.galleriesConn = "loading";
			
			reqPromise.success(function(data, status, headers, config) {
				
				$scope.galleries = data.galleries;
				$scope.usersLikingCount = data.usersLikingCount;
				$scope.usersDislikingCount = data.usersDislikingCount;
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