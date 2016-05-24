<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>    
    
<!DOCTYPE html>
<html ng-app="jakdukApp">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<jsp:include page="../include/html-header.jsp"/>
<style type="text/css">
.photoHolder {
    display: inline-block;
    border: 4px solid #fff;
    text-align: center;
    box-sizing: border-box;
}

.photoClass {
    display: inline-block;
    background: #eee;
    box-sizing: border-box;    
    background-position: center center;
    background-repeat: no-repeat;
    background-size: cover;
    width: 100%;
    height: 100%;
    border: 4px solid #dfe2e2;
}

.caption1 {
    overflow: hidden;
    font-size: 9px;
    white-space: nowrap;
    text-overflow: ellipsis;
}
</style>
</head>
<body>
<div class="container jakduk" ng-controller="GalleryCtrl">
<jsp:include page="../include/navigation-header.jsp"/>

<div id="imageGalleryPage">    
    <div id="imageGallery">
        <div class="photoHolder" ng-repeat="photo in photos" ng-attr-style="width: {{thumbWidth}}px; height: {{thumbWidth}}px;">
            <div class="photoClass thumbnail" data-id="{{photo.id}}" ng-attr-style="background-image:url({{zoomSize < 5 ? photo.thumb_url : photo.medium_url}})"></div>
            <div class="caption1">{{photo.caption}}</div>
            <div class="caption1">{{photo.date}}</div>
        </div>
    </div>
</div>

    <div class="row">
    <div class="col-xs-4 col-sm-2 col-md-2 col-lg-2" ng-repeat="gallery in galleries">
    	<a href="<%=request.getContextPath()%>/gallery/{{gallery.id}}" class="thumbnail">
    		<img ng-src="<%=request.getContextPath()%>/gallery/thumbnail/{{gallery.id}}">
    		<div class="caption text-overflow">
    			{{gallery.name}}
    			<p><small>{{gallery.writer.username}}</small></p>
    		</div>
    	</a>
    </div>
    </div>  
<div class="row">
<div class="media col-md-3" ng-repeat="gallery in galleries">
  <div class="media-left media-middle">
      <img class="media-object" ng-src="<%=request.getContextPath()%>/gallery/thumbnail/{{gallery.id}}" style="width:50px; height:50px;">
  </div>
  <div class="media-body">
    <h5 class="media-heading">{{gallery.name}}</h5>
		<input type="text" class="form-control input-sm col-md-2" placeholder="Input name">
  </div>
</div>
</div>

</div>

<script src="<%=request.getContextPath()%>/resources/jquery/dist/jquery.min.js"></script>
<script type="text/javascript">

function rand(min, max) {
	  return Math.round(Math.random() * (max - min) + min);
	}

var jakdukApp = angular.module("jakdukApp", []);

jakdukApp.controller('GalleryCtrl', function($scope, $http) {
    var caption = '';
    var photos = [];
		$scope.galleriesConn = "none";
		$scope.galleries = [];    
		
		angular.element(document).ready(function() {
			$scope.getGalleries();
		})
    
    var tmp = new Date(1310000000000 + (rand(0,2317410007)));
    var dateString = tmp.toDateString();
    
	$scope.getGalleries = function() {
		var bUrl = '<c:url value="/gallery/list.json"/>';
		
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
    
    photos.push({id: 0, date: dateString, caption: "test01",
        thumb_url : '<%=request.getContextPath()%>/gallery/thumbnail/54cb15d0e4b0030ea3c1dfe6',
        medium_url : '<%=request.getContextPath()%>/gallery/54cb15d0e4b0030ea3c1dfe6'
     });

    photos.push({id: 1, date: dateString, caption: "test02",
        thumb_url : '<%=request.getContextPath()%>/gallery/thumbnail/54d07584e4b0ce01a5aaf1be',
        medium_url : '<%=request.getContextPath()%>/gallery/54d07584e4b0ce01a5aaf1be'
     });

    photos.push({id: 2, date: dateString, caption: "test03",
        thumb_url : '<%=request.getContextPath()%>/gallery/thumbnail/54cb15d0e4b0030ea3c1dfe6',
        medium_url : '<%=request.getContextPath()%>/gallery/54cb15d0e4b0030ea3c1dfe6'
     });
    
    photos.push({id: 3, date: dateString, caption: "test04",
        thumb_url : '<%=request.getContextPath()%>/gallery/thumbnail/54cb15d0e4b0030ea3c1dfe6',
        medium_url : '<%=request.getContextPath()%>/gallery/54cb15d0e4b0030ea3c1dfe6'
     });      
    
    
    $scope.photos = photos;
    $scope.zoomSize = 4;
    $scope.thumbWidth = 100;
    
    window.onresize = function(){
        $scope.$apply();
    };
    $scope.query = '';    
    $scope.orderProp = 'caption'
});
</script>

<jsp:include page="../include/body-footer.jsp"/>
</body>
</html>