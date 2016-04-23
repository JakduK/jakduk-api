<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<!--[if IE 9]> <html lang="ko" class="ie9" ng-app="jakdukApp"> <![endif]-->
<!--[if !IE]><!-->
<html lang="ko" ng-app="jakdukApp"> <!--<![endif]-->
	<head>
		<title><spring:message code="user.profile"/> &middot; <spring:message code="common.jakduk"/></title>
		<jsp:include page="../include/html-header.jsp"/>
		<link rel="stylesheet" href="<%=request.getContextPath()%>/bundles/user.css">
	</head>

	<body class="header-fixed">

		<div class="wrapper" ng-controller="userCtrl">

			<jsp:include page="../include/navigation-header.jsp"/>

			<!--=== Breadcrumbs ===-->
			<div class="breadcrumbs">
				<div class="container">
					<h1 class="pull-left"><a href="<c:url value="/user/refresh"/>"><spring:message code="user.profile"/></a></h1>
				</div><!--/container-->
			</div><!--/breadcrumbs-->
			<!--=== End Breadcrumbs ===-->

			<!--=== Content Part ===-->
			<div class="container content">

				<c:choose>
					<c:when test="${status == 1}">
						<div class="alert alert-success" role="alert"><spring:message code="user.msg.success.update.profile"/></div>
					</c:when>
					<c:when test="${status == 2}">
						<div class="alert alert-success" role="alert">
							<spring:message code="user.mgs.success.change.password"/></div>
					</c:when>
				</c:choose>

				<form class="form-horizontal" role="form">
					<div class="form-group">
						<label class="col-sm-2 control-label"><spring:message code="user.email"/></label>
						<div class="col-sm-3">
							<p class="form-control-static">${userProfile.email}</p>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label"><spring:message code="user.nickname"/></label>
						<div class="col-sm-3">
							<p class="form-control-static">${userProfile.username}</p>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label"><spring:message code="user.support.football.club"/></label>
						<div class="col-sm-3">
							<c:choose>
								<c:when test="${not empty userProfile.footballClubName}">
									<p class="form-control-static">${userProfile.footballClubName.fullName}</p>
								</c:when>
								<c:otherwise>
									<p class="form-control-static"><spring:message code="common.none"/></p>
								</c:otherwise>
							</c:choose>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label"><spring:message code="user.comment"/></label>
						<div class="col-sm-3">
							<p class="form-control-static">${userProfile.about}</p>
						</div>
					</div>
					<hr>
					<div class="form-group">
						<div class="col-sm-offset-2 col-sm-4">
							<a class="btn btn-default" href="<c:url value="/user/profile/update"/>"><spring:message code="common.button.user.profile.update"/></a>
							<a class="btn btn-default" href="<c:url value="/user/password/update"/>"><spring:message code="common.button.user.password.update"/></a>
						</div>
					</div>
				</form>

			</div> <!--=== End Content Part ===-->

			<jsp:include page="../include/footer.jsp"/>

		</div>

		<script src="<%=request.getContextPath()%>/bundles/user.js"></script>
		<script type="text/javascript">
			var jakdukApp = angular.module("jakdukApp", ['jakdukCommon']);

			jakdukApp.controller("userCtrl", function ($scope) {

				angular.element(document).ready(function () {
				});

			});

			$(document).ready(function () {
				App.init();
			});
		</script>
	</body>
</html>