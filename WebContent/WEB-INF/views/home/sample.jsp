<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html ng-app="jakdukApp">
  <head>
    <title>Starter Template for Bootstrap</title>
<jsp:include page="../include/html-header.jsp"/>

<!-- include summernote css/js-->
<link href="<%=request.getContextPath()%>/web-resources/summernote/css/summernote.css" rel="stylesheet">

<link href="//netdna.bootstrapcdn.com/font-awesome/4.0.3/css/font-awesome.min.css" rel="stylesheet">
<script src="//ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
  </head>

<body>
<div class="container">

<jsp:include page="../include/navigation-header.jsp"/>

<div ng-controller="sampleCtrl">
	<div class="starter-template">
		<h1>Bootstrap starter template</h1>
		<p class="lead">Use this document as a way to quickly start any new project.<br> All you get is this text and a mostly barebones HTML document.</p>
	</div>

<!-- 
<div id="summernote">Hello Summernote</div>
 -->
 <summernote height="300"></summernote>
</div>	
	
<jsp:include page="../include/footer.jsp"/>
</div><!-- /.container -->

<!-- Bootstrap core JavaScript ================================================== -->
<!-- Placed at the end of the document so the pages load faster -->
<script src="<%=request.getContextPath()%>/web-resources/bootstrap/js/bootstrap.min.js"></script>
<script src="<%=request.getContextPath()%>/web-resources/summernote/js/summernote.min.js"></script>
<script src="<%=request.getContextPath()%>/web-resources/angular-summernote/js/angular-summernote.min.js"></script>
<script type="text/javascript">
var jakdukApp = angular.module("jakdukApp", ["summernote"]);

jakdukApp.controller("sampleCtrl", function($scope) {
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
<jsp:include page="../include/body-footer.jsp"/>
</body>
</html>
