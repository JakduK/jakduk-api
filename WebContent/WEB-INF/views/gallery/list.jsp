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

	<!-- CSS Page Style -->    
	<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/blueimp-gallery/css/blueimp-gallery.min.css">
	
	<jsp:include page="../include/html-header.jsp"></jsp:include>
</head>

<body>
<div class="wrapper">
<jsp:include page="../include/navigation-header.jsp"/>

	<!--=== Breadcrumbs ===-->
	<div class="breadcrumbs">
		<div class="container">
			<h1 class="pull-left"><a href="<c:url value="/gallery/list/refresh"/>"><spring:message code="gallery.list"/></a></h1>
		</div><!--/container-->
	</div><!--/breadcrumbs-->
	<!--=== End Breadcrumbs ===-->

<div class="container content" ng-controller="galleryCtrl">
	<div id="links" class="row"> 
		<div class="col-md-4 margin-bottom-10-n" ng-repeat="gallery in galleries">
	   		<a ng-href="<%=request.getContextPath()%>/gallery/{{gallery.id}}" class="fancybox img-hover-v1" title="{{gallery.name}}">
				<span><img class="img-responsive" ng-src="<%=request.getContextPath()%>/gallery/thumbnail/{{gallery.id}}" alt="{{gallery.name}}"></span>
			</a>		
		</div>
	</div><!--/row-->

<!-- The Gallery as lightbox dialog, should be a child element of the document body -->
<div id="blueimp-gallery" class="blueimp-gallery">
    <div class="slides"></div>
    <h3 class="title"></h3>
    <a class="prev">‹</a>
    <a class="next">›</a>
    <a class="close">×</a>
    <a class="play-pause"></a>
</div>	
	
<div infinite-scroll="getGalleries()" infinite-scroll-disabled="infiniteDisabled">
</div>        
</div>

<jsp:include page="../include/footer.jsp"/>

</div><!-- /.container -->

<!-- Bootstrap core JavaScript
  ================================================== -->
<!-- Placed at the end of the document so the pages load faster -->
<script src="<%=request.getContextPath()%>/resources/jquery/dist/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/resources/angular-lazy-img/release/angular-lazy-img.js"></script>
<script src="<%=request.getContextPath()%>/resources/ng-infinite-scroller-origin/build/ng-infinite-scroll.min.js"></script>
<script src="<%=request.getContextPath()%>/resources/blueimp-gallery/js/blueimp-gallery.min.js"></script>

<script type="text/javascript">
var jakdukApp = angular.module("jakdukApp", ["infinite-scroll", "angularLazyImg"]);

jakdukApp.controller("galleryCtrl", function($scope, $http) {
	$scope.galleriesConn = "none";
	$scope.galleries = [];
	$scope.usersLikingCount = [];
	$scope.usersDislikingCount = [];
	$scope.infiniteDisabled = false;
	
	angular.element(document).ready(function() {
		//$scope.getGalleries();
		
		document.getElementById('links').onclick = function (event) {
		    event = event || window.event;
		    var target = event.target || event.srcElement,
		        link = target.children[0].src ? target.parentNode : target,
		        options = {index: link, event: event},
		        links = this.getElementsByTagName('a');
		    
		    blueimp.Gallery(links, options);
		};
		
		App.init();
	});	
	
	$scope.getGalleries = function() {
		
		var bUrl;
		if ($scope.galleries.length > 0) {
			var lastGallery = $scope.galleries[$scope.galleries.length - 1].id;
			bUrl = '<c:url value="/gallery/data/list.json?id=' + lastGallery + '"/>';
		} else {
			bUrl = '<c:url value="/gallery/data/list.json"/>';
		}
		
		if ($scope.galleriesConn == "none") {
			
			var reqPromise = $http.get(bUrl);
			
			$scope.galleriesConn = "loading";
			
			reqPromise.success(function(data, status, headers, config) {
				
				data.galleries.forEach(function(gallery) {
					$scope.galleries.push(gallery);
				});

				
				for (var key in data.usersLikingCount) {
					var value = data.usersLikingCount[key];
					$scope.usersLikingCount[key] = value;
				}
				
				for (var key in data.usersDislikingCount) {
					var value = data.usersDislikingCount[key];
					$scope.usersDislikingCount[key] = value;					
				}
				
				$scope.galleriesConn = "none";
				
				if (data.galleries.length < 12) {
					$scope.infiniteDisabled = true;
				}
				
			});
			reqPromise.error(function(data, status, headers, config) {
				$scope.galleriesConn = "none";
				$scope.error = '<spring:message code="common.msg.error.network.unstable"/>';
			});
		}
	};
	
	$scope.btnMore = function() {
		
	};
	
});
</script>

<jsp:include page="../include/body-footer.jsp"/>

</body>

</html>