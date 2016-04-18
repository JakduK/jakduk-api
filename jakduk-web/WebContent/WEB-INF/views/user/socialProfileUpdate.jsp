<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>   
 
<!DOCTYPE html>
<!--[if IE 9]> <html lang="ko" class="ie9" ng-app="jakdukApp"> <![endif]-->
<!--[if !IE]><!--> <html lang="ko" ng-app="jakdukApp"> <!--<![endif]-->
<head>
	<title><spring:message code="user.profile.update"/> &middot; <spring:message code="common.jakduk"/></title>
	<jsp:include page="../include/html-header.jsp"></jsp:include>
	
	<link href="<%=request.getContextPath()%>/resources/font-awesome/css/font-awesome.min.css" rel="stylesheet">
</head>

<body class="header-fixed">

	<c:set var="contextPath" value="<%=request.getContextPath()%>"/>

	<div class="wrapper">
		<jsp:include page="../include/navigation-header.jsp"/>

		<!--=== Breadcrumbs ===-->
		<div class="breadcrumbs">
			<div class="container">
				<h1 class="pull-left"><a href="<c:url value="/user/social/refresh"/>"><spring:message code="user.profile.update"/></a></h1>
			</div><!--/container-->
		</div><!--/breadcrumbs-->
		<!--=== End Breadcrumbs ===-->

		<div class="container content" ng-controller="writeCtrl">

			<form:form commandName="socialUserForm" name="socialUserForm" action="${contextPath}/user/social/profile/update" method="POST" cssClass="form-horizontal"
					   ng-submit="onSubmit($event)">

				<form:input path="emailStatus" cssClass="hidden" size="0" ng-init="emailStatus='${socialUserForm.emailStatus}'" ng-model="emailStatus"/>
				<form:input path="usernameStatus" cssClass="hidden" size="0" ng-init="usernameStatus='${socialUserForm.usernameStatus}'" ng-model="usernameStatus"/>

				<div class="form-group has-feedback" ng-class="{'has-success':socialUserForm.email.$valid, 'has-error':socialUserForm.email.$invalid || emailStatus != 'ok'}">
					<label class="col-sm-2 control-label">
						<abbr title='<spring:message code="common.msg.required"/>'>*</abbr> <spring:message code="user.email"/>
					</label>
					<div class="col-sm-4">
						<input type="email" name="email" class="form-control" placeholder='<spring:message code="user.placeholder.email"/>'
							   ng-init="email='${socialUserForm.email}'" ng-model="email"
							   ng-blur="onEmail()" ng-change="validationEmail()"
							   ng-required="true" ng-minlength="emailLengthMin" ng-maxlength="emailLengthMax"/>

						<span class="glyphicon form-control-feedback" ng-class="{'glyphicon-ok':socialUserForm.email.$valid,
						'glyphicon-remove':socialUserForm.email.$invalid || emailStatus != 'ok'}"></span>

						<i class="fa fa-spinner fa-spin" ng-show="emailConn == 'connecting'"></i>
						<form:errors path="email" cssClass="text-danger" element="span" ng-hide="emailAlert.msg"/>

						<!-- 초기화 시 onEmail()를 호출 -->
						<span class="{{emailAlert.classType}}" ng-show="emailAlert.msg" ng-init="onEmail()">{{emailAlert.msg}}</span>
					</div>
				</div>

				<div class="form-group has-feedback" ng-class="{'has-success':socialUserForm.username.$valid, 'has-error':socialUserForm.username.$invalid || usernameStatus != 'ok'}">
					<label class="col-sm-2 control-label">
						<abbr title='<spring:message code="common.msg.required"/>'>*</abbr> <spring:message code="user.nickname"/>
					</label>
					<div class="col-sm-4">
						<input type="text" name="username" class="form-control" placeholder='<spring:message code="user.placeholder.username"/>'
							   ng-model="username" ng-init="username='${socialUserForm.username}'"
							   ng-blur="onUsername()" ng-change="validationUsername()"
							   ng-required="true" ng-minlength="usernameLengthMin" ng-maxlength="usernameLengthMax"/>
						<span class="glyphicon form-control-feedback" ng-class="{'glyphicon-ok':socialUserForm.username.$valid,
						'glyphicon-remove':socialUserForm.username.$invalid || usernameStatus != 'ok'}"></span>

						<i class="fa fa-spinner fa-spin" ng-show="usernameConn == 'connecting'"></i>
						<form:errors path="username" cssClass="text-danger" element="span" ng-hide="usernameAlert.msg"/>

						<!-- 초기화 시 onUsername()를 호출 -->
						<span class="{{usernameAlert.classType}}" ng-show="usernameAlert.msg" ng-init="onUsername()">{{usernameAlert.msg}}</span>
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
						<form:textarea path="about" cssClass="form-control" cols="40" rows="5" placeholder='<spring:message code="user.placeholder.about"/>'/>
					</div>
				</div>

				<div class="form-group">
					<div class="col-sm-offset-2 col-sm-4">
						<button type="submit" class="btn btn-success">
							<span class="glyphicon glyphicon-upload"></span> <spring:message code="common.button.write"/>
						</button>
						<button type="button" class="btn btn-warning" onclick="location.href='<c:url value="/social/user/profile"/>'">
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

	<script src="<%=request.getContextPath()%>/resources/jquery/dist/jquery.min.js"></script>
	<script src="<%=request.getContextPath()%>/resources/bootstrap/dist/js/bootstrap.min.js"></script>
	<script src="<%=request.getContextPath()%>/resources/jakduk/js/jakduk.js"></script>
	<script type="text/javascript">

		window.onbeforeunload = function(e) {
			if (!submitted) {
				(e || window.event).returnValue = '<spring:message code="common.msg.are.you.sure.leave.page"/>';
				return '<spring:message code="common.msg.are.you.sure.leave.page"/>';
			}
		};

		var submitted = false;
		var jakdukApp = angular.module("jakdukApp", []);

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
			});

			$scope.onSubmit = function(event) {
				if ($scope.socialUserForm.$valid && $scope.emailStatus == 'ok' && $scope.usernameStatus == "ok") {
					submitted = true;
					$scope.submitConn = "connecting";
					$scope.buttonAlert = {"classType":"text-info", "msg":'<spring:message code="common.msg.be.cummunicating.server"/>'};
				} else {
					if ($scope.socialUserForm.email.$invalid) {
						$scope.validationEmail();
					} else if ($scope.socialUserForm.username.$invalid) {
						$scope.validationUsername();
					}

					$scope.submitConn = "none";
					$scope.buttonAlert = {"classType":"text-danger", "msg":'<spring:message code="common.msg.need.form.validation"/>'};
					event.preventDefault();
				}
			};

			$scope.onEmail = function() {
				if ($scope.socialUserForm.email.$valid) {
					var bUrl = '<c:url value="/api/user/exist/email/update?email=' + $scope.email + '"/>';

					if ($scope.emailConn == "none") {
						var reqPromise = $http.get(bUrl);
						$scope.emailConn = "connecting";
						reqPromise.success(function(data, status, headers, config) {

							if (data == false) {
								$scope.emailStatus = "ok";
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
					$scope.emailStatus = "invalid";
					$scope.validationEmail();
				}
			};

			$scope.onUsername = function() {
				if ($scope.socialUserForm.username.$valid) {
					var bUrl = '<c:url value="/api/user/exist/username/update?username=' + $scope.username + '"/>';
					if ($scope.usernameConn == "none") {
						var reqPromise = $http.get(bUrl);
						$scope.usernameConn = "connecting";
						reqPromise.success(function(data, status, headers, config) {

							if (data == false) {
								$scope.usernameStatus = "ok";
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
					$scope.usernameStatus = 'invalid';
					$scope.validationUsername();
				}
			};

			$scope.validationEmail = function() {
				if ($scope.socialUserForm.email.$invalid) {
					if ($scope.socialUserForm.email.$error.required) {
						$scope.emailAlert = {"classType":"text-danger", "msg":'<spring:message code="common.msg.required"/>'};
					} else if ($scope.socialUserForm.email.$error.minlength || $scope.socialUserForm.email.$error.maxlength) {
						$scope.emailAlert = {"classType":"text-danger", "msg":'<spring:message code="Size.userWrite.email"/>'};
					} else {
						$scope.emailAlert = {"classType":"text-danger", "msg":'<spring:message code="user.msg.check.mail.format"/>'};
					}
				} else {
					$scope.emailAlert = {"classType":"text-info", "msg":'<spring:message code="common.msg.error.shoud.check.redudancy"/>'};
				}
			};

			$scope.validationUsername = function () {
				if ($scope.socialUserForm.username.$invalid) {
					if ($scope.socialUserForm.username.$error.required) {
						$scope.usernameAlert = {"classType":"text-danger", "msg":'<spring:message code="common.msg.required"/>'};
					} else if ($scope.socialUserForm.$error.minlength || $scope.socialUserForm.username.$error.maxlength) {
						$scope.usernameAlert = {"classType":"text-danger", "msg":'<spring:message code="Size.userWrite.username"/>'};
					}
				} else {
					$scope.usernameAlert = {"classType":"text-info", "msg":'<spring:message code="common.msg.error.shoud.check.redudancy"/>'};
				}
			};

		});

	</script>

	<jsp:include page="../include/body-footer.jsp"/>

	<script type="text/javascript">
		$(document).ready(function () {
			App.init();
		});
	</script>
</body>
</html>