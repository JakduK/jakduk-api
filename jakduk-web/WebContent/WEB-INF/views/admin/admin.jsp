<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>    
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> 
 
<!DOCTYPE html>
<html ng-app="jakdukApp">
	<head>
		<base href="<%=request.getContextPath()%>/">
		<title>ADMIN PAGE &middot; <spring:message code="common.jakduk"/></title>
		<jsp:include page="../include/html-header.jsp"/>
		<link rel="stylesheet" href="bundles/admin.css">
	</head>
	<body>
		<nav class="navbar navbar-inverse navbar-fixed-top">
			<div class="container-fluid">
				<div class="navbar-header">
					<a class="navbar-brand" href="admin/settings">JakduK Admin Page</a>
				</div>
				<ul class="nav navbar-nav navbar-right">
					<li><a href="home" target="_self">Go JakduK</a></li>
				</ul>
			</div>
		</nav>

		<div class="container-fluid">
			<div class="row" ui-view></div>
		</div>

		<script src="bundles/admin.js"></script>
		<script src="resources/jakduk/js/admin.js"></script>
		<script type="text/javascript">
			angular.module("jakdukApp", [
				'jakdukCommon', 'jakdukAdmin', 'ui.bootstrap', 'ngAnimate'
			]).constant('BASE_URL', '<%=request.getContextPath()%>');
		</script>
	</body>
</html>
