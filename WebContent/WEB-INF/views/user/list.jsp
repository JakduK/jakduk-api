<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html ng-app>
<head>
	<jsp:include page="../include/html-header.jsp"/>
</head>
<body>
	<jsp:include page="../include/navigation-header.jsp"/>
	<div class="container">
		<h2>User List</h2>
		<ul>
			<c:forEach items="${list}" var="member">
				<li>${member.email} / ${member.username} / ${member.password}</li>
			</c:forEach>
		</ul>
	</div>
	<!-- Bootstrap core JavaScript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
    <script src="../web-resources/bootstrap/js/bootstrap.min.js"></script>    
	<script src="../web-resources/bootstrap/js/offcanvas.js"></script>
</body>
</html>