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
	
	<link href="<%=request.getContextPath()%>/resources/font-awesome/css/font-awesome.min.css" rel="stylesheet">
</head>

<body>
<div class="container jakduk-gallery" ng-controller="galleryCtrl">
<jsp:include page="../include/navigation-header.jsp"/>

<div class="page-header">
  <h4>
	  <a href="<c:url value="/gallery"/>"><spring:message code="gallery"/></a>
	  <small><spring:message code="gallery.about"/></small>
  </h4>
</div>

<div class="row">
	<div class="col-xs-6 col-sm-4 col-md-3 col-lg-2" ng-repeat="gallery in galleries">
		<a href="<%=request.getContextPath()%>/gallery/view/{{gallery.id}}" class="thumbnail">
			<img ng-src="<%=request.getContextPath()%>/gallery/thumbnail/{{gallery.id}}">
		</a>
		<div class="text-overflow">
 			<h5>{{gallery.name}}</h5>
			<h5><small>{{gallery.writer.username}}</small></h5>  			
		</div>
		<h5>
			<i class="fa fa-comments-o"></i><strong> {{gallery.views}}</strong>
			<span class="text-primary">
				<i class="fa fa-thumbs-o-up"></i><strong> {{gallery.views}}</strong>
			</span>
			<span class="text-danger">
				<i class="fa fa-thumbs-o-down"></i><strong> {{gallery.views}}</strong>
			</span>
		</h5>
	</div>
</div>

<jsp:include page="../include/footer.jsp"/>

</div><!-- /.container -->

<!-- Bootstrap core JavaScript
  ================================================== -->
<!-- Placed at the end of the document so the pages load faster -->
<script src="<%=request.getContextPath()%>/resources/jquery/js/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/resources/bootstrap/js/bootstrap.min.js"></script>    

<script type="text/javascript">
var jakdukApp = angular.module("jakdukApp", []);

jakdukApp.controller("galleryCtrl", function($scope, $http) {
	$scope.galleriesConn = "none";
	$scope.galleries = [];
	
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