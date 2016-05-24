<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<!DOCTYPE html>
<html ng-app="summernoteDemo">
<head>
<title>Angular-summernote Demo</title>



  <!--summernote dependencies-->
<script src="<%=request.getContextPath()%>/resources/jquery/dist/jquery.min.js"></script>
<link href="<%=request.getContextPath()%>/resources/bootstrap/dist/css/bootstrap.css" rel="stylesheet">
<script src="<%=request.getContextPath()%>/resources/bootstrap/dist/js/bootstrap.min.js"></script>
<link href="<%=request.getContextPath()%>/resources/font-awesome/css/font-awesome.min.css" rel="stylesheet">
  
<!--summernote-->
<link href="<%=request.getContextPath()%>/resources/summernote/dist/summernote.css" rel="stylesheet">
<script src="<%=request.getContextPath()%>/resources/summernote/dist/summernote.min.js"></script>

 <!--angular-summernote dependencies -->
<script src="<%=request.getContextPath()%>/resources/angular/angular.min.js"></script>
<script src="<%=request.getContextPath()%>/resources/angular-summernote/dist/angular-summernote.min.js"></script>

<style>
body {
	margin: 10px 20px;
}
</style>
</head>
<body>
	<h1>angular-summernote</h1>
	<p>
		angular directive for <a
			href="http://hackerwins.github.io/summernote/">Summernote</a>
	</p>

	<h2>summernote directive</h2>

	<h4>use element</h4>
	<summernote></summernote>

	<h4>use attribute</h4>
	<div summernote></div>

	<h2>summernote options</h2>

	<h4>height</h4>
	<summernote height="300"></summernote>

	<h4>focus</h4>
	<summernote focus></summernote>

	<h4>airmode</h4>
	<summernote airMode></summernote>

	<div ng-controller="OptCtrl">
		<h4>options object</h4>
		<summernote config="options"></summernote>
	</div>


	<script>
    angular.module('summernoteDemo', ['summernote'])
        .controller('OptCtrl', function($scope) {
          $scope.options = {
            height: 150,
            toolbar: [
              ['style', ['bold', 'italic', 'underline', 'clear']],
              ['fontsize', ['fontsize']],
              ['color', ['color']],
              ['para', ['ul', 'ol', 'paragraph']],
              ['height', ['height']]
            ]
          };
        });
  </script>
</body>
</html>