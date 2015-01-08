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
<div class="container" ng-controller="writeCtrl">
	<jsp:include page="../include/navigation-header.jsp"/>
	
	<c:set var="contextPath" value="<%=request.getContextPath()%>"/>
	
		<form:form commandName="userWrite" name="userWrite" action="${contextPath}/user/write" method="POST" cssClass="form-horizontal" 
		ng-submit="onSubmit($event)">
			<form:input path="emailStatus" cssClass="hidden" size="0" ng-init="emailStatus='${userWrite.emailStatus}'" ng-model="emailStatus"/>
			<form:input path="usernameStatus" cssClass="hidden" size="0" ng-init="usernameStatus='${userWrite.usernameStatus}'" ng-model="usernameStatus"/>
			<legend><spring:message code="user.register"/> </legend>
			<div class="form-group has-feedback" ng-class="{'has-success':userWrite.email.$valid, 
			'has-error':userWrite.email.$invalid || emailStatus != 'ok'}">
				<label class="col-sm-2 control-label" for="email">
					<abbr title='<spring:message code="common.msg.required"/>'>*</abbr> <spring:message code="user.email"/>
				</label>
				<div class="col-sm-3">
					<input type="email" name="email" class="form-control" size="50" placeholder="Email" 
					ng-init="email='${userWrite.email}'" ng-model="email" 
					ng-blur="onEmail()" ng-keyup="validationEmail()" 
					ng-required="true" ng-minlength="6" ng-maxlength="30" ng-pattern="/^[\w]{3,}@[\w]+(\.[\w-]+){1,3}$/"/>
					<span class="glyphicon form-control-feedback" 
					ng-class="{'glyphicon-ok':userWrite.email.$valid, 'glyphicon-remove':userWrite.email.$invalid || emailStatus != 'ok'}"></span>					
					<i class="fa fa-spinner fa-spin" ng-show="emailConn == 'loading'"></i>
					<form:errors path="email" cssClass="text-danger" element="span" ng-hide="emailAlert.msg"/>
					<span class="{{emailAlert.classType}}" ng-show="emailAlert.msg">{{emailAlert.msg}}</span>
				</div>	
			</div>
			<div class="form-group has-feedback" ng-class="{'has-success':userWrite.username.$valid, 
			'has-error':userWrite.username.$invalid || usernameStatus != 'ok'}">
				<label class="col-sm-2 control-label" for="username">
					<abbr title='<spring:message code="common.msg.required"/>'>*</abbr> <spring:message code="user.nickname"/>
				</label>
				<div class="col-sm-3">
					<form:input path="username" cssClass="form-control" size="50" placeholder="Nickname" 
					ng-init="username='${userWrite.username}'" ng-model="username" 
					ng-blur="onUsername()" ng-keyup="validationUsername()"
					ng-required="true" ng-minlength="2" ng-maxlength="20"/>
					<span class="glyphicon form-control-feedback" ng-class="{'glyphicon-ok':userWrite.username.$valid, 
					'glyphicon-remove':userWrite.username.$invalid || usernameStatus != 'ok'}"></span>
					<i class="fa fa-spinner fa-spin" ng-show="usernameConn == 'loading'"></i>
					<form:errors path="username" cssClass="text-danger" element="span" ng-hide="usernameAlert.msg"/>
					<span class="{{usernameAlert.classType}}" ng-show="usernameAlert.msg">{{usernameAlert.msg}}</span>
				</div>
			</div>
			<div class="form-group has-feedback" ng-class="{'has-success':userWrite.password.$valid, 
			'has-error':userWrite.password.$invalid}">
				<label class="col-sm-2 control-label" for="password">
					<abbr title='<spring:message code="common.msg.required"/>'>*</abbr> <spring:message code="user.password"/>
				</label>
				<div class="col-sm-3">
					<form:password path="password" cssClass="form-control" size="50" placeholder="Password"
					ng-model="password" ng-keyup="vlidationPassword()" ng-blur="eaualPasswordConfirm()"
					ng-required="true" ng-minlength="4" ng-maxlength="20"/>
					<span class="glyphicon form-control-feedback" ng-class="{'glyphicon-ok':userWrite.password.$valid, 
					'glyphicon-remove':userWrite.password.$invalid}"></span>
					<form:errors path="password" cssClass="text-danger" element="span" ng-hide="passwordAlert.msg"/>
					<span class="{{passwordAlert.classType}}" ng-show="passwordAlert.msg">{{passwordAlert.msg}}</span>	
				</div>
			</div>
			<div class="form-group has-feedback" 	ng-class="{'has-success':userWrite.passwordConfirm.$valid, 
			'has-error':userWrite.passwordConfirm.$invalid || equalPasswordStatus != 'true'}">
				<label class="col-sm-2 control-label" for="passwordConfirm">
					<abbr title='<spring:message code="common.msg.required"/>'>*</abbr> <spring:message code="user.password.confirm"/>
				</label>
				<div class="col-sm-3">
					<form:password path="passwordConfirm" cssClass="form-control" size="50" placeholder="Confirm password"
					ng-model="passwordConfirm" ng-keyup="validationPasswordConfirm()" ng-blur="eaualPasswordConfirm()"
					ng-required="true" ng-minlength="4" ng-maxlength="20"/>
					<span class="glyphicon form-control-feedback" ng-class="{'glyphicon-ok':userWrite.passwordConfirm.$valid, 
					'glyphicon-remove':userWrite.passwordConfirm.$invalid || equalPasswordStatus != 'true'}"></span>
					<form:errors path="passwordConfirm" cssClass="text-danger" element="span" ng-hide="passwordConfirmAlert.msg || (passwordConfirm.length > 0 && password == passwordConfirm)"/>
					<span class="{{passwordConfirmAlert.classType}}" ng-show="passwordConfirmAlert.msg">{{passwordConfirmAlert.msg}}</span>								
				</div>
			</div>
			
			<div class="form-group">
				<label class="col-sm-2 control-label" for="supportFC">
					<spring:message code="user.support.football.club"/>
				</label>
				<div class="col-sm-3">
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
					<form:textarea path="about" cssClass="form-control" cols="40" rows="5" placeholder="About"/>
				</div>
			</div>
			<div class="form-group">
				<div class="col-sm-offset-2 col-sm-4">
					<input type="submit" value="<spring:message code="common.button.submit"/>" class="btn btn-default"/>
					<a class="btn btn-danger" href="<c:url value="/"/>"><spring:message code="common.button.cancel"/></a>
				</div> 
			</div>
		</form:form>
		
		<jsp:include page="../include/footer.jsp"/>
</div><!-- /.container -->
	
<!-- Bootstrap core JavaScript
 ================================================== -->
<!-- Placed at the end of the document so the pages load faster -->
<script src="<%=request.getContextPath()%>/resources/jquery/js/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/resources/bootstrap/js/bootstrap.min.js"></script>    
<script type="text/javascript">

var jakdukApp = angular.module("jakdukApp", []);

jakdukApp.controller("writeCtrl", function($scope, $http) {
	$scope.emailConn = "none";
	$scope.usernameConn = "none";
	$scope.emailAlert = {};
	$scope.usernameAlert = {};
	$scope.passwordAlert = {};
	$scope.passwordConfirmAlert = {};
	$scope.equalPasswordStatus = "false";
	
	$scope.onSubmit = function(event) {
		if ($scope.userWrite.$valid && $scope.emailStatus == 'ok' && $scope.usernameStatus == 'ok' 
				&& $scope.equalPasswordStatus == "true") {
		} else {
			if ($scope.userWrite.email.$invalid) {
				$scope.validationEmail();				
			} else if ($scope.emailStatus != 'ok') {
				//$scope.emailAlert = {"classType":"text-danger", "msg":'<spring:message code="common.msg.error.shoud.check.redudancy"/>'};
				document.userWrite.email.focus();
			}
			
			if ($scope.userWrite.username.$invalid) {
				$scope.validationUsername();
			} else if ($scope.usernameStatus != 'ok') {
				//$scope.usernameAlert = {"classType":"text-danger", "msg":'<spring:message code="common.msg.error.shoud.check.redudancy"/>'};
				document.userWrite.username.focus();
			}
			
			$scope.eaualPasswordConfirm();

			event.preventDefault();
		}
	};
	
	$scope.onEmail = function() {		
		if ($scope.userWrite.email.$valid) {
			var bUrl = '<c:url value="/check/user/email.json?email=' + $scope.email + '"/>';
			if ($scope.emailConn == "none") {
				var reqPromise = $http.get(bUrl);
				$scope.emailConn = "loading";
				reqPromise.success(function(data, status, headers, config) {
					if (data.existEmail != null) {
						if (data.existEmail == false) {
							$scope.emailStatus = "ok";
							$scope.emailAlert = {"classType":"text-success", "msg":'<spring:message code="user.msg.avaliable.data"/>'};
						} else {
							$scope.emailStatus = "duplication";
							$scope.emailAlert = {"classType":"text-danger", "msg":'<spring:message code="user.msg.replicated.data"/>'};
						}
					}
					$scope.emailConn = "none";					
				});
				reqPromise.error(function(data, status, headers, config) {
					$scope.emailConn = "none";
					$scope.emailAlert = {"classType":"text-danger", "msg":'<spring:message code="common.msg.error.network.unstable"/>'};					
				});
			}
		} else {
			$scope.emailStatus = "invalid";
			$scope.validationEmail();
		}
	};
	
	$scope.onUsername = function() {
		if ($scope.userWrite.username.$valid) {
			var bUrl = '<c:url value="/check/user/username.json?username=' + $scope.username + '"/>';
			if ($scope.usernameConn == "none") {
				var reqPromise = $http.get(bUrl);
				$scope.usernameConn = "loading";
				reqPromise.success(function(data, status, headers, config) {
					if (data.existUsername != null) {
						if (data.existUsername == false) {
							$scope.usernameStatus = "ok";
							$scope.usernameAlert = {"classType":"text-success", "msg":'<spring:message code="user.msg.avaliable.data"/>'};
						} else {
							$scope.usernameStatus = 'duplication';
							$scope.usernameAlert = {"classType":"text-danger", "msg":'<spring:message code="user.msg.replicated.data"/>'};
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
	
	$scope.validationEmail = function() {
		if ($scope.userWrite.email.$error.required) {
			$scope.emailAlert = {"classType":"text-danger", "msg":'<spring:message code="common.msg.required"/>'};
		} else if ($scope.userWrite.email.$error.minlength || $scope.userWrite.email.$error.maxlength) {
			$scope.emailAlert = {"classType":"text-danger", "msg":'<spring:message code="Size.userWrite.email"/>'};
		} else if ($scope.userWrite.email.$error.pattern) {
			$scope.emailAlert = {"classType":"text-danger", "msg":'<spring:message code="user.msg.check.mail.format"/>'};
		}
	}
		
	$scope.validationUsername = function () {
		if ($scope.userWrite.username.$error.required) {
			$scope.usernameAlert = {"classType":"text-danger", "msg":'<spring:message code="common.msg.required"/>'};
		} else if ($scope.userWrite.username.$error.minlength || $scope.userWrite.username.$error.maxlength) {
			$scope.usernameAlert = {"classType":"text-danger", "msg":'<spring:message code="Size.userWrite.username"/>'};
		}
	}
	
	$scope.vlidationPassword = function() {
		$scope.equalPasswordStatus = "false";
		$scope.passwordConfirmAlert = {};
		
		if ($scope.userWrite.password.$invalid) {
			if ($scope.userWrite.password.$error.required) {
				$scope.passwordAlert = {"classType":"text-danger", "msg":'<spring:message code="common.msg.required"/>'};
			} else if ($scope.userWrite.password.$error.minlength || $scope.userWrite.password.$error.maxlength) {
				$scope.passwordAlert = {"classType":"text-danger", "msg":'<spring:message code="Size.userWrite.password"/>'};
			}
		} else {
			$scope.passwordAlert = {"classType":"text-success", "msg":'<spring:message code="user.msg.avaliable.data"/>'};
		}
	};
	
	$scope.eaualPasswordConfirm = function() {
		
		if ($scope.userWrite.password.$invalid) {
			$scope.passwordConfirmAlert = {"classType":"text-danger", "msg":'<spring:message code="user.msg.password.mismatch"/>'};
		}
		
		if ($scope.userWrite.passwordConfirm.$invalid) return;
		
		if ($scope.password == $scope.passwordConfirm) {
			$scope.equalPasswordStatus = "true";
			$scope.passwordConfirmAlert = {"classType":"text-success", "msg":'<spring:message code="user.msg.avaliable.data"/>'};
		} else {
			//$scope.equalPasswordStatus = "false";
			$scope.passwordConfirmAlert = {"classType":"text-danger", "msg":'<spring:message code="user.msg.password.mismatch"/>'};
		}
	}
	
	$scope.validationPasswordConfirm = function() {
		$scope.equalPasswordStatus = "false";
		
		if ($scope.userWrite.passwordConfirm.$invalid) {
			if ($scope.userWrite.passwordConfirm.$error.required) {
				$scope.passwordConfirmAlert = {"classType":"text-danger", "msg":'<spring:message code="common.msg.required"/>'};
			} else if ($scope.userWrite.passwordConfirm.$error.minlength || $scope.userWrite.passwordConfirm.$error.maxlength) {
				$scope.passwordConfirmAlert = {"classType":"text-danger", "msg":'<spring:message code="Size.userWrite.password"/>'};
			}
		} else {
			$scope.passwordConfirmAlert = {"classType":"text-info", "msg":'<spring:message code="Size.userWrite.password"/>'};
		}
	};	
});
</script>

<jsp:include page="../include/body-footer.jsp"/>

</body>
</html>