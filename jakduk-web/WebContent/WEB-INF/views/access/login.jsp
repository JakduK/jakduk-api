<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!DOCTYPE html>
<!--[if IE 9]><html lang="ko" class="ie9" ng-app="jakdukApp"><![endif]-->
<!--[if !IE]> --><html lang="ko" ng-app="jakdukApp"><!-- <![endif]-->
	<head>
		<title><spring:message code="user.sign.in"/> &middot; <spring:message code="common.jakduk"/></title>
		<jsp:include page="../include/html-header.jsp"/>
		<link rel="stylesheet" href="<%=request.getContextPath()%>/bundles/login.css">
	</head>

	<body class="header-fixed">
		<div class="wrapper">
			<jsp:include page="../include/navigation-header.jsp"/>

			<!--=== Breadcrumbs ===-->
			<div class="breadcrumbs">
				<div class="container">
					<h1 class="pull-left"><spring:message code="user.sign.in"/></h1>
				</div><!--/container-->
			</div><!--/breadcrumbs-->
			<!--=== End Breadcrumbs ===-->

			<div class="container content" ng-controller="loginCtrl">
				<c:choose>
					<c:when test="${result == 'failure'}">
						<div class="alert alert-danger" role="alert"><spring:message code="user.msg.login.failure"/></div>
					</c:when>
					<c:when test="${result == 'locked'}">
						<div class="alert alert-danger" role="alert"><spring:message code="user.msg.login.failure.locked"/></div>
					</c:when>
					<c:when test="${result == 'success'}">
						<div class="alert alert-success" role="alert"><spring:message code="user.msg.register.success"/></div>
					</c:when>
				</c:choose>

				<div class="row">
					<div class="col-md-4 col-md-offset-4 col-sm-6 col-sm-offset-3">
						<div class="reg-page">
							<form action="login" name="loginForm" method="POST" ng-submit="onSubmit($event)">
								<input type="hidden"
									   name="${_csrf.parameterName}"
									   value="${_csrf.token}"/>
								<input type="hidden" name="loginRedirect" value="${loginRedirect}"/>
								<div class="reg-header">
									<h2><spring:message code="user.sign.in.header"/></h2>
								</div>

								<div class="form-group">
									<div class="input-group has-feedback"
										 ng-class="{'has-success':loginForm.j_username.$valid, 'has-error':loginForm.j_username.$invalid}">
										<span class="input-group-addon"><i class="fa fa-at fa-fw"></i></span>
										<input type="email" class="form-control" id="j_username" name="j_username" placeholder='<spring:message code="user.placeholder.email"/>'
											   ng-model="email" ng-required="true" ng-minlength="emailLengthMin" ng-maxlength="emailLengthMax" autofocus>
									<span class="glyphicon form-control-feedback"
										  ng-class="{'glyphicon-ok':loginForm.j_username.$valid, 'glyphicon-remove':loginForm.j_username.$invalid}"></span>
									</div>
									<p class="text-danger" ng-model="errorEmail" ng-show="errorEmail" ng-bind="errorEmail"></p>
								</div>

								<div class="form-group">
									<div class="input-group has-feedback"
										 ng-class="{'has-success':loginForm.j_password.$valid, 'has-error':loginForm.j_password.$invalid}">
										<span class="input-group-addon"><i class="fa fa-lock fa-fw"></i></span>
										<input type="password" class="form-control" id="j_password" name="j_password" placeholder='<spring:message code="user.placeholder.password"/>'
											   ng-model="password" ng-required="true" ng-minlength="passwordLengthMin" ng-maxlength="passwordLengthMax">
									<span class="glyphicon form-control-feedback"
										  ng-class="{'glyphicon-ok':loginForm.j_password.$valid, 'glyphicon-remove':loginForm.j_password.$invalid}"></span>
									</div>
									<p class="text-danger" ng-model="errorPassword" ng-show="errorPassword" ng-bind="errorPassword"></p>
								</div>

								<div class="form-group">
									<!--
                                    <input id = "remember_me" name ="remember-me" type = "checkbox"/>Remember me
                                    -->
									<div class="checkbox">
										<label>
											<input type="checkbox" name="remember" ng-model="remember">
											<spring:message code="user.email.remember"/>
										</label>
									</div>
								</div>

								<button type="submit" class="btn btn-u rounded btn-block ladda-button" ladda="btnSubmit" data-style="expand-right">
									<spring:message code="user.sign.in"/>
								</button>

								<div class="checkbox">
									<ul class="list-inline">
										<li>
											<a href="<c:url value="/user/write"/>">
												<span aria-hidden="true" class="icon-user-follow"></span> <spring:message code="user.register"/>
											</a>
										</li>
										|
										<li>
											<a href="<%=request.getContextPath()%>/reset_password">
												<span aria-hidden="true" class="icon-real-estate-056"></span> <spring:message code="user.sign.forgot.password"/>
											</a>
										</li>
									</ul>
								</div>
							</form>

							<hr/>

							<button class="btn btn-facebook btn-block" onclick="javascript:document.facebook.submit();">
								<i class="fa fa-facebook"></i> <spring:message code="common.button.connect.with.facebook"/> <i class="fa fa-angle-right"></i>
							</button>

							<button class="btn btn-dropbox btn-block" onclick="javascript:document.daum.submit();">
								<spring:message code="common.button.connect.with.daum"/> <i class="fa fa-angle-right"></i>
							</button>

							<form action="auth/facebook" name="facebook">
								<input type="hidden" name="scope" value="email"/>
							</form>

							<form action="auth/daum" name="daum">
							</form>
						</div>
					</div>
				</div><!--/row-->
			</div>

			<jsp:include page="../include/footer.jsp"/>
		</div><!-- /.container -->

		<script src="<%=request.getContextPath()%>/bundles/login.js"></script>
		<script type="text/javascript">
			angular.module("jakdukApp", ["ngCookies", "angular-ladda", "jakdukCommon"])
				.controller("loginCtrl", function ($scope, $cookies) {
					$scope.emailLengthMin = Jakduk.FormEmailLengthMin;
					$scope.emailLengthMax = Jakduk.FormEmailLengthMax;
					$scope.passwordLengthMin = Jakduk.FormPasswordLengthMin;
					$scope.passwordLengthMax = Jakduk.FormPasswordLengthMax;

					// angular.ready()함수에서는 안된다. 마우스 클릭하니 그제서야 이메일이 들어간다.
					var email = $cookies.get("email");
					var remember = $cookies.get("remember");

					if (!Jakduk.isEmpty(email) && !Jakduk.isEmpty(remember) && remember == "1") {
						$scope.email = email;
						$scope.remember = true;
					}

					angular.element(document).ready(function () {
					});

					$scope.onSubmit = function (event) {
						if ($scope.loginForm.$valid) {
							$scope.btnSubmit = true;
						} else {
							$scope.onEmail();

							if ($scope.loginForm.j_password.$error.required) {
								$scope.errorPassword = '<spring:message code="common.msg.required"/>';
							} else if ($scope.loginForm.j_password.$error.minlength || $scope.loginForm.j_password.$error.maxlength) {
								$scope.errorPassword = '<spring:message code="Size.userWrite.password"/>';
							}
							event.preventDefault();
						}
					};

					$scope.onEmail = function () {
						if ($scope.loginForm.j_username.$invalid) {
							if ($scope.loginForm.j_username.$error.required) {
								$scope.errorEmail = '<spring:message code="common.msg.required"/>';
							} else if ($scope.loginForm.j_username.$error.minlength || $scope.loginForm.j_username.$error.maxlength) {
								$scope.errorEmail = '<spring:message code="Size.userWrite.email"/>';
							} else {
								$scope.errorEmail = '<spring:message code="user.msg.check.mail.format"/>';
							}
						} else {
							$scope.errorEmail = "";
						}
					};
				});

			$(document).ready(function () {
				App.init();
			});
		</script>
	</body>
</html>
