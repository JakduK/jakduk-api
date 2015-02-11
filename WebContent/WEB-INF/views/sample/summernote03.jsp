<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" /> 
  <title>summernote</title>

<script src="<%=request.getContextPath()%>/resources/jquery/js/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/resources/bootstrap/js/bootstrap.min.js"></script>
<link href="<%=request.getContextPath()%>/resources/bootstrap/css/bootstrap.css" rel="stylesheet">
<link href="<%=request.getContextPath()%>/resources/summernote/css/summernote.css" rel="stylesheet">
<link href="<%=request.getContextPath()%>/resources/font-awesome/css/font-awesome.min.css" rel="stylesheet">	


<script src="<%=request.getContextPath()%>/resources/summernote/js/summernote.js"></script>
<script src="<%=request.getContextPath()%>/resources/summernote/plugin/summernote-ext-hello.js"></script>


  <script type="text/javascript">
  var editor = $.summernote.eventHandler.getEditor();
  
  $(document).ready(function() {
	  $('#summernote').summernote({
		  toolbar : [
		             ['group', ['hello']]   // add button name
		           ]		  
	  });
	});
  </script>
</head>
<body>
<div id="summernote">Hello Summernote</div>
</body>
</html>
