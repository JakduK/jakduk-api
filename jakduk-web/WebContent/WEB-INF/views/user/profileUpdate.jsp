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

<div class="wrapper" ng-controller="writeCtrl">
	<jsp:include page="../include/navigation-header.jsp"/>

	<!--=== Breadcrumbs ===-->
	<div class="breadcrumbs">
		<div class="container">
			<h1 class="pull-left"><a href="<c:url value="/user/refresh"/>"><spring:message code="user.profile.update"/></a></h1>
		</div><!--/container-->
	</div><!--/breadcrumbs-->
	<!--=== End Breadcrumbs ===-->

	<!--=== Content Part ===-->
	<div class="container content">

		<form:form commandName="userProfileWrite" name="userProfileWrite" action="${contextPath}/user/profile/update" method="POST" cssClass="form-horizontal"
				   ng-submit="onSubmit($event)">

			<form:input path="usernameStatus" cssClass="hidden" size="0" ng-init="usernameStatus='${userProfileWrite.usernameStatus}'" ng-model="usernameStatus"/>

			<div class="form-group">
				<label class="col-sm-2 control-label"><spring:message code="user.email"/></label>
				<div class="col-sm-4">
					<input type="email" name="email" class="form-control" placeholder='<spring:message code="user.placeholder.email"/>'
						   ng-init="email='${userProfileWrite.email}'" ng-model="email" disabled="disabled"/>
				</div>
			</div>

			<div class="form-group has-feedback" ng-class="{'has-success':userProfileWrite.username.$valid,
					'has-error':userProfileWrite.username.$invalid || usernameStatus != 'ok'}">
				<label class="col-sm-2 control-label">
					<abbr title='<spring:message code="common.msg.required"/>'>*</abbr> <spring:message code="user.nickname"/>
				</label>
				<div class="col-sm-4">
					<input type="text" name="username" class="form-control" placeholder='<spring:message code="user.placeholder.username"/>'
						   ng-model="username" ng-init="username='${userProfileWrite.username}'"
						   ng-blur="onUsername()" ng-change="validationUsername()"
						   ng-required="true" ng-minlength="usernameLengthMin" ng-maxlength="usernameLengthMax"/>

					<span class="glyphicon form-control-feedback" ng-class="{'glyphicon-ok':userProfileWrite.username.$valid,
								'glyphicon-remove':userProfileWrite.username.$invalid || usernameStatus != 'ok'}"></span>
					<i class="fa fa-spinner fa-spin" ng-show="usernameConn == 'connecting'"></i>
					<form:errors path="username" cssClass="text-danger" element="span" ng-hide="usernameAlert.msg"/>
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
					<textarea name="about" class="form-control" cols="40" rows="5" placeholder='<spring:message code="user.placeholder.about"/>'></textarea>
				</div>
			</div>

			<div class="form-group">
				<div class="col-sm-offset-2 col-sm-4">
					<button type="submit" class="btn btn-success">
						<span class="glyphicon glyphicon-upload"></span> <spring:message code="common.button.write"/>
					</button>
					<button type="button" class="btn btn-warning" onclick="location.href='<c:url value="/user/profile"/>'">
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

<!-- Bootstrap core JavaScript ================================================== -->
<!-- Placed at the end of the document so the pages load faster -->
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
		$scope.usernameLengthMin = Jakduk.FormUsernameLengthMin;
		$scope.usernameLengthMax = Jakduk.FormUsernameLengthMax;

		$scope.usernameConn = "none";
		$scope.submitConn = "none";
		$scope.usernameAlert = {};
		$scope.buttonAlert = {};

		angular.element(document).ready(function() {
		});

		$scope.onSubmit = function(event) {
			if ($scope.userProfileWrite.$valid && $scope.usernameStatus == "ok") {
				submitted = true;
				$scope.submitConn = "connecting";
				$scope.buttonAlert = {"classType":"text-info", "msg":'<spring:message code="common.msg.be.cummunicating.server"/>'};
			} else {
				if ($scope.userProfileWrite.username.$invalid) {
					$scope.validationUsername();
				} else if ($scope.usernameStatus != 'ok') {
				}

				$scope.submitConn = "none";
				$scope.buttonAlert = {"classType":"text-danger", "msg":'<spring:message code="common.msg.need.form.validation"/>'};
				event.preventDefault();
			}
		};

		$scope.onUsername = function() {
			if ($scope.userProfileWrite.username.$valid) {
				var bUrl = '<c:url value="/api/user/exist/username/update?username=' + $scope.username + '/"/>';

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
			if ($scope.userProfileWrite.username.$invalid) {
				if ($scope.userProfileWrite.username.$error.required) {
					$scope.usernameAlert = {"classType":"text-danger", "msg":'<spring:message code="common.msg.required"/>'};
				} else if ($scope.userProfileWrite.username.$error.minlength || $scope.userProfileWrite.username.$error.maxlength) {
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