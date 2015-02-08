<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>   
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
 
<!DOCTYPE html>
<html ng-app="jakdukApp">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>${gallery.name} - <spring:message code="gallery"/> &middot; <spring:message code="common.jakduk"/></title>
	<jsp:include page="../include/html-header.jsp"></jsp:include>
	
	<link href="http://getbootstrap.com/examples/carousel/carousel.css" rel="stylesheet">
	
</head>
<body>
<div class="container">
<jsp:include page="../include/navigation-header.jsp"/>

<div class="form-group">
	<button type="button" class="btn btn-default" onclick="location.href='<c:url value="/gallery"/>'">
		<span class="glyphicon glyphicon-th-large"></span>
	</button>
	<button type="button" class="btn btn-default" onclick="location.href='<c:url value="/gallery"/>'">
		<span class="glyphicon glyphicon-chevron-left"></span>
	</button>	
	<button type="button" class="btn btn-default" onclick="location.href='<c:url value="/gallery"/>'">
		<span class="glyphicon glyphicon-chevron-right"></span>
	</button>	
</div>

<hr/>

<img class="img-responsive" src="<%=request.getContextPath()%>/gallery/${gallery.id}">

<jsp:include page="../include/footer.jsp"/>
</div><!-- /.container -->

<!-- Bootstrap core JavaScript
  ================================================== -->
<!-- Placed at the end of the document so the pages load faster -->
<script src="<%=request.getContextPath()%>/resources/jquery/js/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/resources/bootstrap/js/bootstrap.min.js"></script>    

<script type="text/javascript">
var jakdukApp = angular.module("jakdukApp", []);
</script>

<jsp:include page="../include/body-footer.jsp"/>

</body>

</html>