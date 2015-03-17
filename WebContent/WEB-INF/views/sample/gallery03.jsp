<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html ng-app="jakdukApp">

  <head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<script src="<%=request.getContextPath()%>/resources/jquery/dist/jquery.min.js"></script>
<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/blueimp-gallery/css/blueimp-gallery.min.css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/unify/assets/plugins/fancybox/source/jquery.fancybox.css">

<jsp:include page="../include/html-header.jsp"/>
  </head>

<body>
<div class="wrapper">
<jsp:include page="../include/navigation-header.jsp"/>  

<div class="container content" ng-controller="GalleryCtrl">

<div class="container content">
        <div class="text-center margin-bottom-50">
            <h2 class="title-v2 title-center">THREE COLUMNS</h2>
            <p class="space-lg-hor">If you are going to use a <span class="color-green">passage of Lorem Ipsum</span>, you need to be sure there isn't anything embarrassing hidden in the middle of text. All the Lorem Ipsum generators on the Internet tend to repeat predefined chunks as necessary, making <span class="color-green">this the first</span> true generator on the Internet.</p>
        </div>

        <div id="links" class="row margin-bottom-30">
            <div class="col-sm-4 sm-margin-bottom-30" ng-repeat="gallery in galleries">
                <a ng-href="<%=request.getContextPath()%>/gallery/{{gallery.id}}" class="fancybox img-hover-v1" title="{{gallery.name}}">
                    <span><img class="img-responsive" ng-src="<%=request.getContextPath()%>/gallery/thumbnail/{{gallery.id}}" alt="{{gallery.name}}"></span>
                </a>
            </div>
        </div>

        <div class="row margin-bottom-30">
            <div class="col-sm-4 sm-margin-bottom-30">
                <a href="assets/img/main/img6.jpg" rel="gallery1" class="fancybox img-hover-v1" title="Image 4">
                    <span><img class="img-responsive" src="assets/img/main/img6.jpg" alt=""></span>
                </a>
            </div>
            <div class="col-sm-4 sm-margin-bottom-30">
                <a href="assets/img/main/img7.jpg" rel="gallery1" class="fancybox img-hover-v1" title="Image 5">
                    <span><img class="img-responsive" src="assets/img/main/img7.jpg" alt=""></span>
                </a>
            </div>
            <div class="col-sm-4">
                <a href="assets/img/main/img8.jpg" rel="gallery1" class="fancybox img-hover-v1" title="Image 6">
                    <span><img class="img-responsive" src="assets/img/main/img8.jpg" alt=""></span>
                </a>
            </div>
        </div>
    </div>


	<div id="links2" class="row margin-bottom-30">
	<div class="col-sm-4 sm-margin-bottom-30" ng-repeat="gallery in galleries">
	    <a ng-href="<%=request.getContextPath()%>/gallery/{{gallery.id}}" title="{{gallery.name}}" class="fancybox img-hover-v1">
	        <span><img ng-src="<%=request.getContextPath()%>/gallery/thumbnail/{{gallery.id}}" style="width:250px; height:150px;" alt="{{gallery.name}}"></span>
	    </a>
    </div>
	    <a href="images/apple.jpg" title="Apple">
	        <img src="images/thumbnails/apple.jpg" alt="Apple">
	    </a>
	</div>
	<!-- The Gallery as lightbox dialog, should be a child element of the document body -->
<div id="blueimp-gallery" class="blueimp-gallery">
    <div class="slides"></div>
    <h3 class="title"></h3>
    <a class="prev">‹</a>
    <a class="next">›</a>
    <a class="close">×</a>
    <a class="play-pause"></a>
</div>


</div>
</div>

<script type="text/javascript" src="<%=request.getContextPath()%>/resources/jquery-migrate/jquery-migrate.min.js"></script>
<script src="<%=request.getContextPath()%>/resources/blueimp-gallery/js/blueimp-gallery.min.js"></script>

<script type="text/javascript">

var jakdukApp = angular.module('jakdukApp', []);

jakdukApp.controller('GalleryCtrl', function($scope, $http) {
	$scope.galleriesConn = "none";
	$scope.galleries = [];
	
	angular.element(document).ready(function() {
		$scope.getGalleries();
		
		document.getElementById('links').onclick = function (event) {
		    event = event || window.event;
		    var target = event.target || event.srcElement,
		        link = target.children[0].src ? target.parentNode : target,
		        options = {index: link, event: event},
		        links = this.getElementsByTagName('a');
		    blueimp.Gallery(links, options);
		};		
	});
	
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
