<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<!DOCTYPE html>
<html>
<head>
<title>Angular-summernote Demo</title>



  <!--summernote dependencies-->
<script src="<%=request.getContextPath()%>/resources/jquery/dist/jquery.min.js"></script>
 <!--angular-summernote dependencies -->
<script src="<%=request.getContextPath()%>/resources/angular/angular.min.js"></script>

<script src="<%=request.getContextPath()%>/resources/infinite-scroll/js/ng-infinite-scroll.min.js"></script>

</head>
<body>
<p>
a
</p>
<p>
a
</p>
<p>
a
</p>
<p>
a
</p>
<p>
a
</p>
<p>
a
</p>
<p>
a
</p>
<p>
a
</p>
<p>
a
</p>
<p>
a
</p>
<p>
a
</p>
<p>
a
</p>
<p>
a
</p>
<p>
a
</p>
<p>
a
</p>
<p>
a
</p>
<p>
a
</p>
<p>
a
</p>
<p>
a
</p>
<p>
a
</p>
<p>
a
</p>
<p>
a
</p>
<p>
a
</p>
<p>
a
</p>
<p>
a
</p>
<p>
a
</p>
<p>
a
</p>
<p>
a
</p>

<div ng-app='myApp' ng-controller='DemoController'>
  <div infinite-scroll='loadMore()' infinite-scroll-disabled='value'>
    <img ng-repeat='image in images' ng-src='http://placehold.it/225x250&text={{image}}'>
  </div>
</div>


	<script>	
	var myApp = angular.module('myApp', ['infinite-scroll']);
	myApp.controller('DemoController', function($scope) {
	  $scope.images = [1, 2, 3, 4, 5, 6, 7, 8];
	  $scope.value = false;

	  $scope.loadMore = function() {		  
	    var last = $scope.images[$scope.images.length - 1];
	    for(var i = 1; i <= 8; i++) {
	      $scope.images.push(last + i);
	    }
	    $scope.value = true;
	  };
	});
  </script>
</body>
</html>