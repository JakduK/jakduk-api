<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>   
 
<!DOCTYPE html>
<!--[if IE 9]> <html lang="ko" class="ie9" ng-app="jakdukApp"> <![endif]-->
<!--[if !IE]><!--> <html lang="ko" ng-app="jakdukApp"> <!--<![endif]-->
<head>
	<title><spring:message code="user.profile.update"/> &middot; <spring:message code="common.jakduk"/></title>
	<jsp:include page="../include/html-header.jsp"></jsp:include>

	<link href="<%=request.getContextPath()%>/bundles/user.css" rel="stylesheet">
</head>

<body class="header-fixed">

	<c:set var="contextPath" value="<%=request.getContextPath()%>"/>

	<div class="wrapper" ng-controller="writeCtrl">
		<jsp:include page="../include/navigation-header.jsp"/>

		<!--=== Breadcrumbs ===-->
		<div class="breadcrumbs">
			<div class="container">
				<h1 class="pull-left"><a href="<c:url value="/user/social/refresh"/>"><spring:message code="user.profile.update"/></a></h1>
			</div><!--/container-->
		</div><!--/breadcrumbs-->
		<!--=== End Breadcrumbs ===-->

		<div class="container content">

			<form:form commandName="userProfileForm" name="userProfileForm" action="${contextPath}/user/social/profile/update" method="POST" cssClass="form-horizontal"
					   ng-submit="onSubmit($event)">

				<form:input path="emailStatus" cssClass="hidden" size="0" ng-init="emailStatus='${userProfileForm.emailStatus}'" ng-model="emailStatus"/>
				<form:input path="usernameStatus" cssClass="hidden" size="0" ng-init="usernameStatus='${userProfileForm.usernameStatus}'" ng-model="usernameStatus"/>

				<div class="form-group has-feedback"
					 ng-class="{'has-success':userProfileForm.email.$valid, 'has-error':userProfileForm.email.$invalid || emailStatus != 'OK'}">
					<label class="col-sm-2 control-label">
						<abbr title='<spring:message code="common.msg.required"/>'>*</abbr> <spring:message code="user.email"/>
					</label>
					<div class="col-sm-4">
						<input type="email" name="email" class="form-control" placeholder='<spring:message code="user.placeholder.email"/>'
							   ng-init="email='${userProfileForm.email}'" ng-model="email"
							   ng-blur="onEmail()" ng-keyup="validationEmail()"
							   ng-required="true" ng-minlength="emailLengthMin" ng-maxlength="emailLengthMax"/>

						<span class="glyphicon form-control-feedback" ng-class="{'glyphicon-ok':userProfileForm.email.$valid,
						'glyphicon-remove':userProfileForm.email.$invalid || emailStatus != 'OK'}"></span>

						<i class="fa fa-spinner fa-spin" ng-show="emailConn == 'connecting'"></i>
						<form:errors path="email" cssClass="text-danger" element="span" ng-hide="emailAlert.msg"/>

						<span class="{{emailAlert.classType}}" ng-show="emailAlert.msg">{{emailAlert.msg}}</span>
					</div>
				</div>

				<div class="form-group has-feedback"
					 ng-class="{'has-success':userProfileForm.username.$valid, 'has-error':userProfileForm.username.$invalid || usernameStatus != 'OK'}">
					<label class="col-sm-2 control-label">
						<abbr title='<spring:message code="common.msg.required"/>'>*</abbr> <spring:message code="user.nickname"/>
					</label>
					<div class="col-sm-4">
						<input type="text" name="username" class="form-control" placeholder='<spring:message code="user.placeholder.username"/>'
							   ng-model="username" ng-init="username='${userProfileForm.username}'"
							   ng-blur="onUsername()" ng-keyup="validationUsername()"
							   ng-required="true" ng-minlength="usernameLengthMin" ng-maxlength="usernameLengthMax"/>
						<span class="glyphicon form-control-feedback" ng-class="{'glyphicon-ok':userProfileForm.username.$valid,
						'glyphicon-remove':userProfileForm.username.$invalid || usernameStatus != 'OK'}"></span>

						<i class="fa fa-spinner fa-spin" ng-show="usernameConn == 'connecting'"></i>
						<form:errors path="username" cssClass="text-danger" element="span" ng-hide="usernameAlert.msg"/>

						<span class="{{usernameAlert.classType}}" ng-show="usernameAlert.msg">{{usernameAlert.msg}}</span>
					</div>
				</div>

				<div class="form-group">
					<label class="col-sm-2 control-label">
						<spring:message code="user.support.football.club"/>
					</label>
					<div class="col-sm-4">
						<form:select path="footballClub" cssClass="form-control">
							<form:option value=""><spring:message code="common.none"/></form:option>
							<c:forEach items="${footballClubs}" var="club">
								<c:forEach items="${club.names}" var="name">
									<form:option value="${club.id}" label="${name.fullName}"/>
								</c:forEach>
							</c:forEach>
						</form:select>
					</div>
				</div>

				<div class="form-group">
					<label class="col-sm-2 control-label"> <spring:message code="user.comment"/></label>
					<div class="col-sm-4">
						<!-- form:textarea 태그를 사용하면서 placeholder에 spring:message를 넣으면 제대로 안나온다. -->
						<form:textarea path="about" cssClass="form-control" cols="40" rows="3" />
					</div>
				</div>

				<div class="form-group">
					<div class="col-sm-offset-2 col-sm-4">
						<button type="submit" class="btn btn-success">
							<span class="glyphicon glyphicon-upload"></span> <spring:message code="common.button.write"/>
						</button>
						<button type="button" class="btn btn-warning" onclick="location.href='<c:url value="/user/social/profile"/>'">
							<span class="glyphicon glyphicon-ban-circle"></span> <spring:message code="common.button.cancel"/>
						</button>
						<div>
							<i class="fa fa-circle-o-notch fa-spin" ng-show="submitConn == 'connecting'"></i>
							<span class="{{buttonAlert.classType}}" ng-show="buttonAlert.msg">{{buttonAlert.msg}}</span>
						</div>
					</div>
				</div>

			</form:form>

		</div> <!--=== End Content Part ===-->

		<jsp:include page="../include/footer.jsp"/>
	</div>

	<script src="<%=request.getContextPath()%>/bundles/user.js"></script>
	<script type="text/javascript">

		window.onbeforeunload = function(e) {
			if (!submitted) {
				(e || window.event).returnValue = '<spring:message code="common.msg.are.you.sure.leave.page"/>';
				return '<spring:message code="common.msg.are.you.sure.leave.page"/>';
			}
		};

		var submitted = false;
		var jakdukApp = angular.module("jakdukApp", ['jakdukCommon']);

		jakdukApp.controller("writeCtrl", function($scope, $http) {
			$scope.emailLengthMin = Jakduk.FormEmailLengthMin;
			$scope.emailLengthMax = Jakduk.FormEmailLengthMax;
			$scope.usernameLengthMin = Jakduk.FormUsernameLengthMin;
			$scope.usernameLengthMax = Jakduk.FormUsernameLengthMax;

			$scope.emailConn = "none";
			$scope.usernameConn = "none";
			$scope.submitConn = "none";
			$scope.emailAlert = {};
			$scope.usernameAlert = {};
			$scope.buttonAlert = {};

			angular.element(document).ready(function() {

				// spring form validation에서 검증 실패 될 경우, 처음에 메시지를 출력한다.
				var emailErrors = document.getElementById("email.errors");
				var usernameErrors = document.getElementById("username.errors");

				if (Jakduk.isEmpty(emailErrors) == true)
					$scope.onEmail();

				if (Jakduk.isEmpty(usernameErrors) == true)
					$scope.onUsername();
			});

			$scope.onSubmit = function(event) {
				if ($scope.userProfileForm.$valid && $scope.emailStatus == 'OK' && $scope.usernameStatus == "OK") {
					submitted = true;
					$scope.submitConn = "connecting";
					$scope.buttonAlert = {
						"classType": "text-info",
						"msg": '<spring:message code="common.msg.be.cummunicating.server"/>'
					};
				} else {
					if ($scope.userProfileForm.email.$invalid) {
						$scope.validationEmail();
					} else if ($scope.userProfileForm.username.$invalid) {
						$scope.validationUsername();
					}

					$scope.submitConn = "none";
					$scope.buttonAlert = {
						"classType": "text-danger",
						"msg": '<spring:message code="common.msg.need.form.validation"/>'
					};
					event.preventDefault();
				}
			};

			$scope.onEmail = function() {
				if ($scope.userProfileForm.email.$valid) {
					var bUrl = '<c:url value="/api/user/exist/email/update?email=' + $scope.email + '"/>';

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
							$scope.emailConn = "none";
							$scope.emailAlert = {"classType":"text-danger", "msg":data.message};
						});
					}
				} else {
					$scope.emailStatus = "INVALID";
					$scope.validationEmail();
				}
			};

			$scope.onUsername = function() {
				if ($scope.userProfileForm.username.$valid) {
					var bUrl = '<c:url value="/api/user/exist/username/update?username=' + $scope.username + '"/>';
					if ($scope.usernameConn == "none") {
						var reqPromise = $http.get(bUrl);
						$scope.usernameConn = "connecting";
						reqPromise.success(function(data, status, headers, config) {

							if (data == false) {
								$scope.usernameStatus = "OK";
								$scope.usernameAlert = {"classType":"text-success", "msg":'<spring:message code="user.msg.avaliable.data"/>'};
							}

							$scope.usernameConn = "none";
						});
						reqPromise.error(function(data, status, headers, config) {
							$scope.usernameConn = "none";
							$scope.usernameAlert = {"classType":"text-danger", "msg":data.message};
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
						$scope.usernameAlert = {"classType":"text-danger", "msg":'<spring:message code="common.msg.required"/>'};
					} else if ($scope.userProfileForm.$error.minlength || $scope.userProfileForm.username.$error.maxlength) {
						$scope.usernameAlert = {"classType":"text-danger", "msg":'<spring:message code="Size.userWrite.username"/>'};
					}
				} else {
					$scope.usernameAlert = {"classType":"text-info", "msg":'<spring:message code="common.msg.error.shoud.check.redudancy"/>'};
				}
			};

		});

		$(document).ready(function () {
			App.init();
		});

	</script>

</body>
</html>