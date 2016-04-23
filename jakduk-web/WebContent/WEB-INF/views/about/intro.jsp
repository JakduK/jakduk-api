<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!DOCTYPE html>
<!--[if IE 9]><html lang="ko" class="ie9" ng-app="jakdukApp"><![endif]-->
<!--[if !IE]><!--><html lang="ko" ng-app="jakdukApp"><!--<![endif]-->
	<head>
		<title><spring:message code="about.site"/> &middot; <spring:message code="common.jakduk"/></title>
		<jsp:include page="../include/html-header.jsp"/>
		<link rel="stylesheet" href="<%=request.getContextPath()%>/bundles/intro.css">
	</head>

	<body class="header-fixed">
		<div class="wrapper">
			<jsp:include page="../include/navigation-header.jsp"/>

			<!--=== Breadcrumbs ===-->
			<div class="breadcrumbs">
				<div class="container">
					<h1 class="pull-left"><a href="<c:url value="/about/intro/refresh"/>"><spring:message code="about.site"/></a>
					</h1>
				</div><!--/container-->
			</div><!--/breadcrumbs-->
			<!--=== End Breadcrumbs ===-->

			<!--=== Content Part ===-->
			<div class="container content">
				<c:choose>
					<c:when test="${lang == 'ko'}">
						<jsp:include page="about-us-ko.jsp"/>
					</c:when>
					<c:when test="${lang == 'en'}">
						<jsp:include page="about-us-en.jsp"/>
					</c:when>
					<c:otherwise>
						<jsp:include page="about-us-en.jsp"/>
					</c:otherwise>
				</c:choose>
			</div>

			<jsp:include page="../include/footer.jsp"/>

		</div><!-- /.wrapper -->

		<script src="<%=request.getContextPath()%>/bundles/intro.js"></script>
		<script type="text/javascript">
			angular.module("jakdukApp", ['jakdukCommon']);
			angular.element(document).ready(function () {
				App.init();
			});
		</script>
	</body>
</html>