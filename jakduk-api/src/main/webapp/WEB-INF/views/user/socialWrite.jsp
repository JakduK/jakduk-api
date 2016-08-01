<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<!--[if IE 9]> <html lang="ko" class="ie9" ng-app="jakdukApp"> <![endif]-->
<!--[if !IE]><!-->
<html lang="ko" ng-app="jakdukApp"> <!--<![endif]-->
	<head>
		<title><spring:message code="user.register"/> &middot; <spring:message code="common.jakduk"/></title>
		<jsp:include page="../include/html-header.jsp"/>
		<link rel="stylesheet" href="<%=request.getContextPath()%>/bundles/user.write.css">
	</head>

	<body class="header-fixed">

		<c:set var="contextPath" value="<%=request.getContextPath()%>"/>
		<c:set var="existId" value="${userProfileForm.id ne null}"/>

		<div class="wrapper">
			<jsp:include page="../include/navigation-header.jsp"/>

			<!--=== Breadcrumbs ===-->
			<div class="breadcrumbs">
				<div class="container">
					<h1 class="pull-left"><spring:message code="user.register"/></h1>
				</div><!--/container-->
			</div><!--/breadcrumbs-->
			<!--=== End Breadcrumbs ===-->

			<div class="container content" ng-controller="writeCtrl">

				<c:if test="${existId eq true}">
					<div class="alert alert-info" role="alert"><spring:message code="user.msg.new.update.social.signup"/></div>
				</c:if>

				<div class="row">
					<div class="col-md-6 col-md-offset-3 col-sm-8 col-sm-offset-2">

						<form:form commandName="userProfileForm" name="userProfileForm" action="${contextPath}/user/social" method="POST" cssClass="reg-page"
							ng-submit="onSubmit($event)">

							<!-- Version 0.6.0 이전, User 데이터의 하위 호환성 유지를 위함이다. https://github.com/Pyohwan/JakduK/issues/53 -->
							<form:hidden path="id"/>

							<form:input path="emailStatus" cssClass="hidden" size="0" ng-init="emailStatus='${userProfileForm.emailStatus}'" ng-model="emailStatus"/>
							<form:input path="usernameStatus" cssClass="hidden" size="0" ng-init="usernameStatus='${userProfileForm.usernameStatus}'" ng-model="usernameStatus"/>

							<div class="reg-header">
								<h2><spring:message code="oauth.register.header"/></h2>
							</div>

							<div class="form-group has-feedback"
								 ng-class="{'has-success':userProfileForm.email.$valid && emailStatus == 'OK', 'has-error':userProfileForm.email.$invalid || emailStatus != 'OK'}">
								<label class="control-label">
									<abbr title='<spring:message code="common.msg.required"/>'>*</abbr>
									<spring:message code="user.email"/>
								</label>
								<input type="email" name="email" class="form-control" placeholder='<spring:message code="user.placeholder.email"/>'
									   ng-init="email='${userProfileForm.email}'" ng-model="email"
									   ng-blur="onEmail()" ng-keyup="validationEmail()"
									   ng-required="true" ng-minlength="emailLengthMin" ng-maxlength="emailLengthMax"/>

								<span class="glyphicon form-control-feedback"
									  ng-class="{'glyphicon-ok':userProfileForm.email.$valid && emailStatus == 'OK', 'glyphicon-remove':userProfileForm.email.$invalid || emailStatus != 'OK'}">
								</span>

								<i class="fa fa-spinner fa-spin" ng-show="emailConn == 'connecting'"></i>
								<form:errors path="email" cssClass="text-danger" element="span" ng-hide="emailAlert.msg" />

								<span class="{{emailAlert.classType}}" ng-show="emailAlert.msg">{{emailAlert.msg}}</span>
							</div>

							<div class="form-group has-feedback"
								 ng-class="{'has-success':userProfileForm.username.$valid && usernameStatus == 'OK', 'has-error':userProfileForm.username.$invalid || usernameStatus != 'OK'}">
								<label class="control-label">
									<abbr title='<spring:message code="common.msg.required"/>'>*</abbr>
									<spring:message code="user.nickname"/>
								</label>
								<input type="text" name="username" class="form-control" placeholder='<spring:message code="user.placeholder.username"/>'
									ng-model="username" ng-init="username='${userProfileForm.username}'"
									ng-blur="onUsername()" ng-keyup="validationUsername()"
									ng-required="true" ng-minlength="usernameLengthMin" ng-maxlength="usernameLengthMax"/>

								<span class="glyphicon form-control-feedback"
									  ng-class="{'glyphicon-ok':userProfileForm.username.$valid && usernameStatus == 'OK', 'glyphicon-remove':userProfileForm.username.$invalid || usernameStatus != 'OK'}">
								</span>

								<i class="fa fa-spinner fa-spin" ng-show="usernameConn == 'connecting'"></i>
								<form:errors path="username" cssClass="text-danger" element="span" ng-hide="usernameAlert.msg"/>

								<span class="{{usernameAlert.classType}}" ng-show="usernameAlert.msg">{{usernameAlert.msg}}</span>
							</div>

							<div class="form-group">
								<label class="control-label">
									<spring:message code="user.support.football.club"/>
								</label>
								<form:select path="footballClub" cssClass="form-control">
									<form:option value=""><spring:message code="common.none"/></form:option>
									<c:forEach items="${footballClubs}" var="club">
										<c:forEach items="${club.names}" var="name">
											<form:option value="${club.id}" label="${name.fullName}"/>
										</c:forEach>
									</c:forEach>
								</form:select>
							</div>

							<div class="form-group">
								<label class="control-label"> <spring:message code="user.comment"/></label>

								<!-- form:textarea 태그를 사용하면서 placeholder에 spring:message를 넣으면 제대로 안나온다. -->
								<form:textarea path="about" cssClass="form-control" cols="40" rows="3" />
							</div>

							<div class="text-right">
								<button type="submit" class="btn-u rounded ladda-button"
									ladda="btnSubmit" data-style="expand-right">
									<span class="glyphicon glyphicon-upload"></span> <spring:message code="common.button.write"/>
								</button>
								<button type="button" class="btn-u btn-u-default rounded" onclick="location.href='<c:url value="/"/>'">
									<span class="glyphicon glyphicon-ban-circle"></span> <spring:message code="common.button.cancel"/>
								</button>
								<div>
									<span class="{{buttonAlert.classType}}" ng-show="buttonAlert.msg">{{buttonAlert.msg}}</span>
								</div>
							</div>

						</form:form>
					</div>
				</div>
			</div>
		</div><!-- /.container -->


		<script src="<%=request.getContextPath()%>/bundles/user.write.js"></script>
		<script type="text/javascript">

			window.onbeforeunload = function (e) {
				if (!submitted) {
					(e || window.event).returnValue = '<spring:message code="common.msg.are.you.sure.leave.page"/>';
					return '<spring:message code="common.msg.are.you.sure.leave.page"/>';
				}
			};

			var submitted = false;
			var jakdukApp = angular.module("jakdukApp", ["angular-ladda", 'jakdukCommon']);

			jakdukApp.controller("writeCtrl", function ($scope, $http) {
				$scope.emailLengthMin = Jakduk.FormEmailLengthMin;
				$scope.emailLengthMax = Jakduk.FormEmailLengthMax;
				$scope.usernameLengthMin = Jakduk.FormUsernameLengthMin;
				$scope.usernameLengthMax = Jakduk.FormUsernameLengthMax;

				$scope.emailConn = "none";
				$scope.usernameConn = "none";
				$scope.emailAlert = {};
				$scope.usernameAlert = {};
				$scope.buttonAlert = {};

				angular.element(document).ready(function () {

					// 서버의 spring form validation에서 검증 실패 될 경우, 처음에 메시지를 출력한다.
					var emailErrors = document.getElementById("email.errors");
					var usernameErrors = document.getElementById("username.errors");

					if (Jakduk.isEmpty(emailErrors) == true)
						$scope.onEmail();

					if (Jakduk.isEmpty(usernameErrors) == true)
						$scope.onUsername();

				});

				$scope.onSubmit = function (event) {
					if ($scope.userProfileForm.$valid && $scope.emailStatus == 'OK' && $scope.usernameStatus == "OK") {
						submitted = true;
						$scope.btnSubmit = true;
					} else {
						if ($scope.userProfileForm.email.$invalid) {
							$scope.validationEmail();
						} else if ($scope.userProfileForm.username.$invalid) {
							$scope.validationUsername();
						}

						$scope.buttonAlert = {
							"classType": "text-danger",
							"msg": '<spring:message code="common.msg.need.form.validation"/>'
						};
						event.preventDefault();
					}
				};

				$scope.onEmail = function() {
					if ($scope.userProfileForm.email.$valid) {
						var existId = Boolean("${existId}");
						var id = "${userProfileForm.id}";

						// Version 0.6.0 이전, User 데이터의 하위 호환성 유지를 위함이다. https://github.com/Pyohwan/JakduK/issues/53
						if (existId == true) {
							var bUrl = '<c:url value="/api/user/exist/email/anonymous?id=' + id + '&email=' + $scope.email + '"/>';
						} else {
							var bUrl = '<c:url value="/api/user/exist/email/?email=' + $scope.email + '"/>';
						}

						if ($scope.emailConn == "none") {
							var reqPromise = $http.get(bUrl);
							$scope.emailConn = "connecting";
							reqPromise.success(function(data, status, headers, config) {

								if (data == false) {
									$scope.emailStatus = "OK";
									$scope.emailAlert = {"classType":"text-success", "msg":'<spring:message code="user.msg.avaliable.data"/>'};
								}

								$scope.emailConn = "none";
							});
							reqPromise.error(function(data, status, headers, config) {
								$scope.emailStatus = "INVALID";
								$scope.emailConn = "none";
								$scope.emailAlert = {"classType":"text-danger", "msg":data.message};
							});
						}
					} else {
						$scope.emailStatus = "INVALID";
						$scope.validationEmail();
					}
				};

				$scope.onUsername = function () {
					if ($scope.userProfileForm.username.$valid) {

						var existId = Boolean("${existId}");
						var id = "${userProfileForm.id}";

						// Version 0.6.0 이전, User 데이터의 하위 호환성 유지를 위함이다. https://github.com/Pyohwan/JakduK/issues/53
						if (existId == true) {
							var bUrl = '<c:url value="/api/user/exist/username/anonymous?id=' + id + '&username=' + $scope.username + '"/>';
						} else {
							var bUrl = '<c:url value="/api/user/exist/username?username=' + $scope.username + '"/>';
						}

						if ($scope.usernameConn == "none") {
							var reqPromise = $http.get(bUrl);
							$scope.usernameConn = "connecting";
							reqPromise.success(function (data, status, headers, config) {

								if (data == false) {
									$scope.usernameStatus = "OK";
									$scope.usernameAlert = {
										"classType": "text-success",
										"msg": '<spring:message code="user.msg.avaliable.data"/>'
									};
								}

								$scope.usernameConn = "none";
							});
							reqPromise.error(function (data, status, headers, config) {
								$scope.usernameStatus = 'INVALID';
								$scope.usernameConn = "none";
								$scope.usernameAlert = {"classType": "text-danger", "msg": data.message};
							});
						}
					} else {
						$scope.usernameStatus = 'INVALID';
						$scope.validationUsername();
					}
				};

				$scope.validationEmail = function() {
					if ($scope.userProfileForm.email.$invalid) {
						if ($scope.userProfileForm.email.$error.required) {
							$scope.emailAlert = {"classType":"text-danger", "msg":'<spring:message code="common.msg.required"/>'};
						} else if ($scope.userProfileForm.email.$error.minlength || $scope.userProfileForm.email.$error.maxlength) {
							$scope.emailAlert = {"classType":"text-danger", "msg":'<spring:message code="Size.userWrite.email"/>'};
						} else {
							$scope.emailAlert = {"classType":"text-danger", "msg":'<spring:message code="user.msg.check.mail.format"/>'};
						}
					} else {
						$scope.emailAlert = {"classType":"text-info", "msg":'<spring:message code="common.msg.error.shoud.check.redudancy"/>'};
					}
				};

				$scope.validationUsername = function () {
					if ($scope.userProfileForm.username.$invalid) {
						if ($scope.userProfileForm.username.$error.required) {
							$scope.usernameAlert = {
								"classType": "text-danger",
								"msg": '<spring:message code="common.msg.required"/>'
							};
						} else if ($scope.userProfileForm.$error.minlength || $scope.userProfileForm.username.$error.maxlength) {
							$scope.usernameAlert = {
								"classType": "text-danger",
								"msg": '<spring:message code="Size.userWrite.username"/>'
							};
						}
					} else {
						$scope.usernameAlert = {
							"classType": "text-info",
							"msg": '<spring:message code="common.msg.error.shoud.check.redudancy"/>'
						};
					}
				};
			});

			$(document).ready(function () {
				App.init();
			});
		</script>

	</body>
</html>
