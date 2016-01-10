<%@ page language="java" isErrorPage="true" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<!DOCTYPE html>
<!--[if IE 9]> <html lang="ko" class="ie9" ng-app="jakdukApp"> <![endif]-->
<!--[if !IE]><!--> <html lang="ko" ng-app="jakdukApp"> <!--<![endif]-->
<head>
	<title><spring:message code="common.error"/> &middot; <spring:message code="common.jakduk"/></title>

	<jsp:include page="../include/html-header.jsp"></jsp:include>
</head>

<body class="header-fixed">
<div class="wrapper">
	<jsp:include page="../include/navigation-header.jsp"/>

	<!--=== Breadcrumbs ===-->
	<div class="breadcrumbs">
		<div class="container">
			<h1 class="pull-left"><a href="<c:url value="/home"/>"><spring:message code="common.error"/></a></h1>
		</div><!--/container-->
	</div><!--/breadcrumbs-->
	<!--=== End Breadcrumbs ===-->

	<div class="container content">
		<c:choose>
			<c:when test="${code == '400'}">
				<h3><spring:message code="common.msg.error.400"/></h3>
			</c:when>		
			<c:when test="${code == '401'}">
				<h3><spring:message code="common.msg.error.401"/></h3>
			</c:when>
			<c:when test="${code == '404'}">
				<h3><spring:message code="common.msg.error.404"/></h3>
			</c:when>
			<c:when test="${code == '406'}">
				<h3><spring:message code="common.msg.error.406"/></h3>
			</c:when>
			<c:when test="${code == 'denied'}">
				<h3><spring:message code="common.msg.error.denied"/></h3>
			</c:when>
			<c:when test="${code == 'maxSession'}">
				<h3><spring:message code="common.msg.error.max.session"/></h3>
			</c:when>						
			<c:otherwise>
				<h3><spring:message code="common.msg.error.default"/></h3>
			</c:otherwise>						
		</c:choose>

		<%@page import="java.util.Date"%>
		<%Date currentDate = new Date();%>
		<p><small>Host : <code>${pageContext.request.scheme}://${header.host}</code></small></p>
		<p><small>URI : <code>${pageContext.errorData.requestURI}</code></small></p>
		<p><small>ErrorCode : <code>${pageContext.errorData.statusCode}</code></small></p>
		<p><small>Name : <code>${code}</code></small></p>
		<p><small>Date : <code><%=currentDate%></code></small></p>
		
		<p class="text-info"><strong><spring:message code="common.msg.error.show.error.to.administrator"/></strong></p>
		<button class="btn btn-default" onclick="location.href='<c:url value="/home"/>'"><spring:message code="common.button.home"/></button>
		<button class="btn btn-default" onclick="history.back()"><spring:message code="common.button.previous.page"/></button>
	</div>
	<jsp:include page="../include/footer.jsp"/>
</div><!-- /.wrapper -->
</body>
</html>