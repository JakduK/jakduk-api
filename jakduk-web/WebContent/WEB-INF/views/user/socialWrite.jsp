<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<!DOCTYPE html>
<!--[if IE 9]> <html lang="ko" class="ie9" ng-app="jakdukApp"> <![endif]-->
<!--[if !IE]><!--> <html lang="ko" ng-app="jakdukApp"> <!--<![endif]-->
<head>
	<title><spring:message code="user.register"/> &middot; <spring:message code="common.jakduk"/></title>
	<jsp:include page="../include/html-header.jsp"></jsp:include>
	
	<!-- CSS Page Style -->    
	<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/unify/assets/css/pages/page_log_reg_v1.css">	
	<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/unify/assets/plugins/ladda-buttons/css/custom-lada-btn.css">	
</head>

<body class="header-fixed">

	<c:set var="contextPath" value="<%=request.getContextPath()%>"/>

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

			<div class="row">
				<div class="col-md-6 col-md-offset-3 col-sm-8 col-sm-offset-2">

					<form:form commandName="userProfileForm" name="userProfileForm" action="${contextPath}/user/social" method="POST" cssClass="reg-page"
							   ng-submit="onSubmit($event)">

						<form:input path="usernameStatus" cssClass="hidden" size="0" ng-init="usernameStatus='${userProfileForm.usernameStatus}'" ng-model="usernameStatus"/>

						<div class="reg-header">
							<h2><spring:message code="oauth.register.header"/></h2>
						</div>

						<div class="form-group">
							<label class="control-label">
								<abbr title='<spring:message code="common.msg.required"/>'>*</abbr> <spring:message code="user.email"/>
							</label>
							<input type="email" name="email" class="form-control" placeholder='<spring:message code="user.placeholder.email"/>'
								   ng-init="email='${userProfileForm.email}'" ng-model="email" disabled="disabled"/>
						</div>

						<div class="form-group has-feedback" ng-class="{'has-success':userProfileForm.username.$valid,
						'has-error':userProfileForm.username.$invalid || usernameStatus != 'ok'}">
							<label class="control-label">
								<abbr title='<spring:message code="common.msg.required"/>'>*</abbr> <spring:message code="user.nickname"/>
							</label>
							<input type="text" name="username" class="form-control" placeholder='<spring:message code="user.placeholder.username"/>'
								   ng-model="username" ng-init="username='${userProfileForm.username}'"
								   ng-blur="onUsername()" ng-change="validationUsername()"
								   ng-required="true" ng-minlength="usernameLengthMin" ng-maxlength="usernameLengthMax"/>

							<span class="glyphicon form-control-feedback" ng-class="{'glyphicon-ok':userProfileForm.username.$valid,
							'glyphicon-remove':userProfileForm.username.$invalid || usernameStatus != 'ok'}"></span>
							<i class="fa fa-spinner fa-spin" ng-show="usernameConn == 'connecting'"></i>
							<form:errors path="username" cssClass="text-danger" element="span" ng-hide="usernameAlert.msg"/>

							<!-- 초기화 시 onUsername()를 호출 -->
							<span class="{{usernameAlert.classType}}" ng-show="usernameAlert.msg" ng-init="onUsername()">{{usernameAlert.msg}}</span>
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
							<textarea name="about" class="form-control" cols="40" rows="3" placeholder='<spring:message code="user.placeholder.about"/>'></textarea>
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

	<script src="<%=request.getContextPath()%>/resources/jquery/dist/jquery.min.js"></script>
	<script src="<%=request.getContextPath()%>/resources/unify/assets/plugins/ladda-buttons/js/spin.min.js"></script>
	<script src="<%=request.getContextPath()%>/resources/unify/assets/plugins/ladda-buttons/js/ladda.min.js"></script>
	<script src="<%=request.getContextPath()%>/resources/angular-ladda/dist/angular-ladda.min.js"></script>
	<script src="<%=request.getContextPath()%>/resources/jakduk/js/jakduk.js"></script>

	<script type="text/javascript">

		window.onbeforeunload = function(e) {
			if (!submitted) {
				(e || window.event).returnValue = '<spring:message code="common.msg.are.you.sure.leave.page"/>';
				return '<spring:message code="common.msg.are.you.sure.leave.page"/>';
			}
		};

		var submitted = false;
		var jakdukApp = angular.module("jakdukApp", ["angular-ladda"]);

		jakdukApp.controller("writeCtrl", function($scope, $http) {
			$scope.usernameLengthMin = Jakduk.FormUsernameLengthMin;
			$scope.usernameLengthMax = Jakduk.FormUsernameLengthMax;

			$scope.usernameConn = "none";
			$scope.usernameAlert = {};
			$scope.buttonAlert = {};

			angular.element(document).ready(function() {
			});

			$scope.onSubmit = function(event) {
				if ($scope.userProfileForm.$valid && $scope.usernameStatus == "ok") {
					submitted = true;
					$scope.btnSubmit = true;
				} else {
					if ($scope.userProfileForm.username.$invalid) {
						$scope.validationUsername();
					}

					$scope.buttonAlert = {"classType":"text-danger", "msg":'<spring:message code="common.msg.need.form.validation"/>'};
					event.preventDefault();
				}
			};

			$scope.onUsername = function() {
				if ($scope.userProfileForm.username.$valid) {
					var bUrl = '<c:url value="/api/user/exist/username?username=' + $scope.username + '"/>';
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

	</script>

	<jsp:include page="../include/body-footer.jsp"/>

	<script type="text/javascript">
		$(document).ready(function () {
			App.init();
		});
	</script>

</body>
</html>