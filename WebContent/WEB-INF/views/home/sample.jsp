<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html ng-app="jakdukApp">
  <head>
    <title>Starter Template for Bootstrap</title>
<jsp:include page="../include/html-header.jsp"/>

<!-- include summernote css/js-->
<link href="<%=request.getContextPath()%>/resources/summernote/css/summernote.css" rel="stylesheet">

<link href="<%=request.getContextPath()%>/resources/font-awesome/css/font-awesome.min.css" rel="stylesheet">
<script src="<%=request.getContextPath()%>/resources/jquery/js/jquery.min.js"></script>
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
 <summernote></summernote>
</div>	
	
<jsp:include page="../include/footer.jsp"/>
</div><!-- /.container -->

<!-- Bootstrap core JavaScript ================================================== -->
<!-- Placed at the end of the document so the pages load faster -->
<script src="<%=request.getContextPath()%>/resources/bootstrap/js/bootstrap.min.js"></script>
<script src="<%=request.getContextPath()%>/resources/summernote/js/summernote.min.js"></script>
<script src="<%=request.getContextPath()%>/resources/angular-summernote/js/angular-summernote.min.js"></script>
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
