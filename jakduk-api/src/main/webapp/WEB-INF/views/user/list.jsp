<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html ng-app>
	<head>
		<jsp:include page="../include/html-header.jsp"/>
		<link rel="stylesheet" href="<%=request.getContextPath()%>/bundles/user.css">
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

		<script src="<%=request.getContextPath()%>/bundles/user.js"></script>
	</body>
</html>