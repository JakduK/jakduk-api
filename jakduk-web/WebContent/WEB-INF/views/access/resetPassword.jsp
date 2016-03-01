<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!DOCTYPE html>
<!--[if IE 9]><html lang="ko" class="ie9" ng-app="jakdukApp"><![endif]-->
<!--[if !IE]> --><html lang="ko" ng-app="jakdukApp"><!-- <![endif]-->
<head>
	<title><spring:message code="user.sign.in"/> &middot; <spring:message code="common.jakduk"/></title>

	<!-- CSS Page Style -->
	<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/unify/assets/css/pages/page_log_reg_v1.css">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/unify/assets/plugins/ladda-buttons/css/custom-lada-btn.css">

	<jsp:include page="../include/html-header.jsp"></jsp:include>
</head>

<body class="header-fixed" ng-controller="resetPasswordCtrl">
	<div class="wrapper">
		<jsp:include page="../include/navigation-header.jsp"/>

		<div class="container content">

			<div class="row">
				<div class="col-md-4 col-md-offset-4 col-sm-6 col-sm-offset-3">
					<form action="<c:out value="${action}"/>" class="reg-page" name="resetForm" method="POST" ng-submit="onSubmit($event)">
						<div class="reg-header">
							<h2><spring:message code="${title}"/></h2>
						</div>

						<c:choose>
							<c:when test="${result eq 'SEND_OK'}">
								<div class="form-group">
									<h5 class="text-center"><strong><%= request.getParameter("j_useremail") %></strong></h5>
									<p><spring:message code="user.msg.reset.password.sendok"/></p>
								</div>
							</c:when>
							<c:when test="${result eq 'CODE_OK'}">
								<div class="form-group">
									<h5 class="text-center"><strong><c:out value="${user_email}"/></strong></h5>
									<input id="code" name="code" type="hidden" value="<%=request.getParameter("code")%>">
									<div class="input-group has-feedback" ng-class="{'has-success':resetForm.j_password.$valid,
						'has-error':resetForm.j_password.$invalid}">
										<span class="input-group-addon"><i class="fa fa-lock fa-fw"></i></span>
										<input type="password" class="form-control" id="j_password" name="j_password" placeholder='<spring:message code="user.password.new"/>' ng-model="password" ng-required="true" ng-minlength="passwordLengthMin" ng-maxlength="passwordLengthMax" autofocus>
										<span class="glyphicon form-control-feedback" ng-class="{'glyphicon-ok':resetForm.j_password.$valid, 'glyphicon-remove':resetForm.j_password.$invalid}"></span>
									</div>
									<p class="text-danger" ng-model="errorPassword" ng-show="errorPassword" ng-bind="errorPassword"></p>
								</div>
								<button type="submit" class="btn btn-u rounded btn-block ladda-button" ladda="btnSubmit" data-style="expand-right">
									<spring:message code="user.password.change"/>
								</button>
							</c:when>
							<c:when test="${result eq 'CHANGE_OK'}">
								<div class="form-group">
									<h5 class="text-center"><strong><c:out value="${user_email}"/></strong></h5>
									<p><spring:message code="user.mgs.success.change.password"/></p>
								</div>
							</c:when>
							<c:when test="${result eq 'INVALID'}">
							<div class="form-group">
								<strong><spring:message code="user.msg.reset.password.invalid"/></strong>
							</div>
							</c:when>
							<c:otherwise>
								<div class="form-group">
									<div class="input-group has-feedback" ng-class="{'has-success':resetForm.j_useremail.$valid,
						'has-error':resetForm.j_useremail.$invalid}">
										<span class="input-group-addon"><i class="fa fa-at fa-fw"></i></span>
										<input type="email" class="form-control" id="j_useremail" name="j_useremail" placeholder='<spring:message code="user.placeholder.email"/>' ng-model="email" ng-required="true" ng-minlength="emailLengthMin" ng-maxlength="emailLengthMax" autofocus>
										<span class="glyphicon form-control-feedback" ng-class="{'glyphicon-ok':resetForm.j_useremail.$valid, 'glyphicon-remove':resetForm.j_useremail.$invalid}"></span>
									</div>
									<p class="text-danger" ng-model="errorEmail" ng-show="errorEmail" ng-bind="errorEmail"></p>
								</div>
								<button type="submit" class="btn btn-u rounded btn-block ladda-button" ladda="btnSubmit" data-style="expand-right">
									<spring:message code="user.sign.reset.password"/>
								</button>
							</c:otherwise>
						</c:choose>

						<a class="btn btn-info btn-block rounded ladda-button" type="button" href="<%=request.getContextPath()%>/login">
							<spring:message code="user.sign.back.to.login"/>
						</a>
					</form>
				</div>
			</div><!--/row-->
		</div>

		<jsp:include page="../include/footer.jsp"/>
	</div><!-- /.container -->

	<script src="<%=request.getContextPath()%>/resources/jquery/dist/jquery.min.js"></script>
	<script src="<%=request.getContextPath()%>/resources/angular-cookies/angular-cookies.min.js"></script>
	<script src="<%=request.getContextPath()%>/resources/unify/assets/plugins/ladda-buttons/js/spin.min.js"></script>
	<script src="<%=request.getContextPath()%>/resources/unify/assets/plugins/ladda-buttons/js/ladda.min.js"></script>
	<script src="<%=request.getContextPath()%>/resources/angular-ladda/dist/angular-ladda.min.js"></script>
	<script src="<%=request.getContextPath()%>/resources/jakduk/js/jakduk.js"></script>

	<script type="text/javascript">
		angular.module("jakdukApp", ["ngCookies", "angular-ladda"])
			.controller("resetPasswordCtrl", function ($scope, $cookies) {
				$scope.emailLengthMin = Jakduk.FormEmailLengthMin;
				$scope.emailLengthMax = Jakduk.FormEmailLengthMax;
				$scope.passwordLengthMin = Jakduk.FormPasswordLengthMin;
				$scope.passwordLengthMax = Jakduk.FormPasswordLengthMax;

				// angular.ready()함수에서는 안된다. 마우스 클릭하니 그제서야 이메일이 들어간다.
				var email = $cookies.get("email");
				var remember = $cookies.get("remember");
				var result = "<c:out value="${result}"/>";

				if (!isEmpty(email) && !isEmpty(remember) && remember == "1") {
					$scope.email = email;
					$scope.remember = true;
				}

				angular.element(document).ready(function () {
					App.init();
				});

				$scope.onSubmit = function (event) {
					if ($scope.resetForm.$valid) {
						$scope.btnSubmit = true;
					} else {
						if (!result) {
							$scope.onEmail();
						}

						if ($scope.resetForm.j_password.$error.required) {
							$scope.errorPassword = '<spring:message code="common.msg.required"/>';
						} else if ($scope.resetForm.j_password.$error.minlength || $scope.resetForm.j_password.$error.maxlength) {
							$scope.errorPassword = '<spring:message code="Size.userWrite.password"/>';
						}
						event.preventDefault();
					}
				};

				$scope.onEmail = function () {
					if ($scope.resetForm.j_useremail.$invalid) {
						if ($scope.resetForm.j_useremail.$error.required) {
							$scope.errorEmail = '<spring:message code="common.msg.required"/>';
						} else if ($scope.resetForm.j_useremail.$error.minlength || $scope.resetForm.j_useremail.$error.maxlength) {
							$scope.errorEmail = '<spring:message code="Size.userWrite.email"/>';
						} else {
							$scope.errorEmail = '<spring:message code="user.msg.check.mail.format"/>';
						}
					} else {
						$scope.errorEmail = "";
					}
				};
			});
	</script>
	<jsp:include page="../include/body-footer.jsp"/>

</body>
</html>
