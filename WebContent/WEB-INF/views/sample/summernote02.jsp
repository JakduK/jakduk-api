<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<!DOCTYPE html>
<html ng-app="jakdukApp">
  <head>
    <title>Starter Template for Bootstrap</title>
<jsp:include page="../include/html-header.jsp"/>

<!-- include summernote css/js-->
<link href="<%=request.getContextPath()%>/resources/summernote/dist/summernote.css" rel="stylesheet">

<link href="<%=request.getContextPath()%>/resources/font-awesome/css/font-awesome.min.css" rel="stylesheet">
<script src="<%=request.getContextPath()%>/resources/jquery/jquery.min.js"></script>
  </head>

<body>
<div class="container">

<jsp:include page="../include/navigation-header.jsp"/>

<div>
	<div class="starter-template">
		<h1>Bootstrap starter template</h1>
		<p class="lead">Use this document as a way to quickly start any new project.<br> All you get is this text and a mostly barebones HTML document.</p>
	</div>

<div id="summernote"></div>
</div>	
	
<jsp:include page="../include/footer.jsp"/>
</div><!-- /.container -->

<!-- Bootstrap core JavaScript ================================================== -->
<!-- Placed at the end of the document so the pages load faster -->
<script src="<%=request.getContextPath()%>/resources/bootstrap/dist/js/bootstrap.min.js"></script>
<script src="<%=request.getContextPath()%>/resources/summernote/dist/summernote.min.js"></script>
<script type="text/javascript">
$(document).ready(function() {
	  $('#summernote').summernote();
	});


var jakdukApp = angular.module("jakdukApp", []);


</script>
<jsp:include page="../include/body-footer.jsp"/>
</body>
</html>
