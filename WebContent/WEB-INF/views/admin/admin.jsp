<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>    
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> 
 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html ng-app>
<head>
	<jsp:include page="../include/html-header.jsp"/>
</head>
<body>
	<jsp:include page="../include/navigation-header.jsp"/>
	<div class="container">
		<p><a href="<c:url value="/admin/init"/>" class="btn btn-primary" role="button">Init Data</a></p>
		<c:if test="${not empty message}">
			<div class="span6 offset2 alert">${message}</div>
		</c:if>
		
		<p><a href="<c:url value="/admin/encyclopedia/write"/>" class="btn btn-primary" role="button">Encyclopedia Write</a></p>
		
		<p><a href="<c:url value="/admin/footballclub/origin/write"/>" class="btn btn-primary" role="button">Football Club Origin Write</a></p>

		<p><a href="<c:url value="/admin/footballclub/write"/>" class="btn btn-primary" role="button">Football Club Write</a></p>
		
		<jsp:include page="../include/footer.jsp"/>
	</div>
	<!-- Bootstrap core JavaScript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
    <script src="../web-resources/bootstrap/js/bootstrap.min.js"></script>    
	<script src="../web-resources/bootstrap/js/offcanvas.js"></script>
</body>
</html>