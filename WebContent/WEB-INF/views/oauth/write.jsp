<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<!DOCTYPE html>
<html ng-app="jakdukApp">
<head>
<jsp:include page="../include/html-header.jsp"></jsp:include>

<link href="<%=request.getContextPath()%>/resources/font-awesome/css/font-awesome.min.css" rel="stylesheet">

</head>
<body>
<div class="container">
	<jsp:include page="../include/navigation-header.jsp"/>
	
	<c:set var="contextPath" value="<%=request.getContextPath()%>"/>
	
	<div class="container" ng-controller="writeCtrl">
	<form:form commandName="OAuthUserWrite" name="OAuthUserWrite" action="${contextPath}/oauth/write" method="POST" cssClass="form-horizontal"
	ng-submit="onSubmit($event)">
		<form:input path="usernameStatus" cssClass="hidden" size="0" ng-init="usernameStatus='${OAuthUserWrite.usernameStatus}'" ng-model="usernameStatus"/>
		<legend><spring:message code="oauth.register"/> </legend>

		<div class="form-group has-feedback" 	ng-class="{'has-success':OAuthUserWrite.username.$valid, 
		'has-error':OAuthUserWrite.username.$invalid || usernameStatus == 'duplication'}">
			<label class="col-sm-2 control-label" for="username">
				<abbr title='<spring:message code="common.msg.required"/>'>*</abbr> <spring:message code="user.nickname"/>
			</label>
			<div class="col-sm-4">
				<input type="text" name="username" class="form-control" placeholder='<spring:message code="user.placeholder.username"/>' 
				ng-model="username" ng-init="username='${OAuthUserWrite.username}'" 
				ng-blur="onUsername()" ng-change="validationUsername()"
				ng-required="true" ng-minlength="2" ng-maxlength="20"/>
				<span class="glyphicon form-control-feedback" ng-class="{'glyphicon-ok':OAuthUserWrite.username.$valid, 
				'glyphicon-remove':OAuthUserWrite.username.$invalid || usernameStatus == 'duplication'}"></span>
				<i class="fa fa-spinner fa-spin" ng-show="usernameConn == 'loading'"></i>					
				<form:errors path="username" cssClass="text-danger" element="span" ng-hide="usernameAlert.msg"/>
				<span class="{{usernameAlert.classType}}" ng-show="usernameAlert.msg" ng-init="onUsername()">{{usernameAlert.msg}}</span>
			</div>
		</div>

		<div class="form-group">
			<label class="col-sm-2 control-label" for="supportFC">
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
			<label class="col-sm-2 control-label" for="about"> <spring:message code="user.comment"/></label>
			<div class="col-sm-4">
				<textarea name="about" class="form-control" cols="40" rows="5" placeholder='<spring:message code="user.placeholder.about"/>'></textarea>
			</div>
		</div>
		<div class="form-group">
			<div class="col-sm-offset-2 col-sm-4">
				<input type="submit" value="<spring:message code="common.button.submit"/>" class="btn btn-default"/>
				<a class="btn btn-danger" href="<c:url value="/"/>"><spring:message code="common.button.cancel"/></a>
				<div>
					<i class="fa fa-circle-o-notch fa-spin" ng-show="submitConn == 'connecting'"></i>
					<span class="{{buttonAlert.classType}}" ng-show="buttonAlert.msg">{{buttonAlert.msg}}</span>
				</div>
			</div> 
		</div>		
	</form:form>
	</div>
</div><!-- /.container -->

<!-- Bootstrap core JavaScript
 ================================================== -->
<!-- Placed at the end of the document so the pages load faster -->
<script src="<%=request.getContextPath()%>/resources/jquery/js/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/resources/bootstrap/js/bootstrap.min.js"></script>

<script type="text/javascript">

var jakdukApp = angular.module("jakdukApp", []);

jakdukApp.controller("writeCtrl", function($scope, $http) {
	$scope.usernameConn = "none";
	$scope.submitConn = "none";
	$scope.usernameAlert = {};
	$scope.buttonAlert = {};
	
	$scope.onSubmit = function(event) {
		if ($scope.OAuthUserWrite.$valid && $scope.usernameStatus == "ok") {
			$scope.submitConn = "connecting";
			$scope.buttonAlert = {"classType":"text-info", "msg":'<spring:message code="common.msg.be.cummunicating.server"/>'};
		} else {			
			if ($scope.OAuthUserWrite.username.$invalid) {
				$scope.validationUsername();
			} else if ($scope.usernameStatus != 'ok') {
				//document.OAuthUserWrite.username.focus();
			}

			$scope.submitConn = "none";
			$scope.buttonAlert = {"classType":"text-danger", "msg":'<spring:message code="common.msg.need.form.validation"/>'};
			event.preventDefault();
		}
	};
	
	$scope.onUsername = function() {	
		if ($scope.OAuthUserWrite.username.$valid) {
			var bUrl = '<c:url value="/check/oauth/update/username.json?username=' + $scope.username + '"/>';
			if ($scope.usernameConn == "none") {
				var reqPromise = $http.get(bUrl);
				$scope.usernameConn = "loading";
				reqPromise.success(function(data, status, headers, config) {
					if (data.existUsername != null) {
						if (data.existUsername == false) {
							$scope.usernameStatus = "ok";
							$scope.usernameAlert = {"classType":"text-success", "msg":'<spring:message code="user.msg.avaliable.data"/>'};
						} else {
							$scope.usernameStatus = "duplication";
							$scope.usernameAlert = {"classType":"text-danger", "msg":'<spring:message code="user.msg.already.username"/>'};
						}
					}
					$scope.usernameConn = "none";
				});
				reqPromise.error(function(data, status, headers, config) {
					$scope.usernameConn = "none";
					$scope.usernameAlert = {"classType":"text-danger", "msg":'<spring:message code="common.msg.error.network.unstable"/>'};					
				});
			}
		} else {
			$scope.usernameStatus = 'invalid';
			$scope.validationUsername();
		}
	};

	$scope.validationUsername = function () {
		if ($scope.OAuthUserWrite.username.$invalid) {
			if ($scope.OAuthUserWrite.username.$error.required) {
				$scope.usernameAlert = {"classType":"text-danger", "msg":'<spring:message code="common.msg.required"/>'};
			} else if ($scope.OAuthUserWrite.$error.minlength || $scope.OAuthUserWrite.username.$error.maxlength) {
				$scope.usernameAlert = {"classType":"text-danger", "msg":'<spring:message code="Size.userWrite.username"/>'};
			}
		} else {
			$scope.usernameAlert = {"classType":"text-info", "msg":'<spring:message code="common.msg.error.shoud.check.redudancy"/>'};
		}
	};
});

</script>

<jsp:include page="../include/body-footer.jsp"/>

</body>
</html>