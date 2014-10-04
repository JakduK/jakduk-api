<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html ng-app="jakdukApp">
<head>
<jsp:include page="../include/html-header.jsp"></jsp:include>
</head>
<body>
<div class="container">
<jsp:include page="../include/navigation-header.jsp"/>
<c:set var="contextPath" value="<%=request.getContextPath()%>"/>
<div class="container" ng-controller="writeCtrl">
	<form:form commandName="userWrite" name="userWrite" action="${contextPath}/user/write" method="POST" cssClass="form-horizontal" 
	ng-submit="onSubmit(userWrite, $event)">
		<form:input path="existEmail" cssClass="hidden" size="0" ng-init="existEmail='${userWrite.existEmail}'" ng-model="existEmail"/>
		<form:input path="existUsername" cssClass="hidden" size="0" ng-init="existUsername='${userWrite.existUsername}'" ng-model="existUsername"/>
		<legend><spring:message code="user.register"/> </legend>
		<div class="form-group" ng-class="{'has-success':userWrite.email.$valid, 'has-error':userWrite.email.$invalid || existEmail != 2}">
			<label class="col-sm-2 control-label" for="email">
				<abbr title='<spring:message code="common.msg.required"/>'>*</abbr> <spring:message code="user.email"/>
			</label>
			<div class="col-sm-3">
					<input type="email" name="email" class="form-control" size="50" placeholder="Email" 
					ng-init="email='${userWrite.email}'" ng-model="email" ng-blur="onEmail(userWrite)" 
					ng-required="true" ng-minlength="6" ng-maxlength="20" ng-pattern="/^[\w]{3,}@[\w]+(\.[\w-]+){1,3}$/"/>
				<form:errors path="email" cssClass="text-danger" element="span" ng-hide="emailAlert.msg"/>
				<span class="{{emailAlert.classType}}" ng-show="emailAlert.msg">{{emailAlert.msg}}</span>
			</div>	
		</div>
		<div class="form-group" ng-class="{'has-success':userWrite.username.$valid, 'has-error':userWrite.username.$invalid || existUsername != 2}">
			<label class="col-sm-2 control-label" for="username">
				<abbr title='<spring:message code="common.msg.required"/>'>*</abbr> <spring:message code="user.nickname"/>
			</label>
			<div class="col-sm-3">
					<form:input path="username" cssClass="form-control" size="50" placeholder="Nickname" 
					ng-init="username='${userWrite.username}'" ng-model="username" ng-blur="onUsername(userWrite)"
					ng-required="true" ng-minlength="2" ng-maxlength="20"/>
				<form:errors path="username" cssClass="text-danger" element="span" ng-hide="usernameAlert.msg"/>
				<span class="{{usernameAlert.classType}}" ng-show="usernameAlert.msg">{{usernameAlert.msg}}</span>
			</div>
		</div>
		<div class="form-group" ng-class="{'has-success':userWrite.password.$valid, 'has-error':userWrite.password.$invalid}">
			<label class="col-sm-2 control-label" for="password">
				<abbr title='<spring:message code="common.msg.required"/>'>*</abbr> <spring:message code="user.password"/>
			</label>
			<div class="col-sm-3">
				<form:password path="password" cssClass="form-control" size="50" placeholder="Password"
				ng-model="password" ng-blur="checkPassword(userWrite)" ng-required="true" ng-minlength="4" ng-maxlength="20"/>
				<form:errors path="password" cssClass="text-danger" element="span" ng-hide="passwordAlert.msg"/>
				<span class="{{passwordAlert.classType}}" ng-show="passwordAlert.msg">{{passwordAlert.msg}}</span>	
			</div>
		</div>
		<div class="form-group" 
		ng-class="{'has-success':userWrite.passwordConfirm.$valid, 'has-error':userWrite.passwordConfirm.$invalid || passwordConfirm.length > 0 && password != passwordConfirm}">
			<label class="col-sm-2 control-label" for="passwordConfirm">
				<abbr title='<spring:message code="common.msg.required"/>'>*</abbr> <spring:message code="user.password.confirm"/>
			</label>
			<div class="col-sm-3">
				<form:password path="passwordConfirm" cssClass="form-control" size="50" placeholder="Confirm password"
				ng-model="passwordConfirm" ng-blur="checkPasswordConfirm(userWrite)" ng-required="true" ng-minlength="4" ng-maxlength="20"/>
				<form:errors path="passwordConfirm" cssClass="text-danger" element="span" ng-hide="passwordConfirmAlert.msg || (passwordConfirm.length > 0 && password == passwordConfirm)"/>
				<span class="{{passwordConfirmAlert.classType}}" ng-show="passwordConfirmAlert.msg">{{passwordConfirmAlert.msg}}</span>								
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
</div>
</div><!-- /.container -->
	
<!-- Bootstrap core JavaScript
 ================================================== -->
<!-- Placed at the end of the document so the pages load faster -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/web-resources/bootstrap/js/bootstrap.min.js"></script>    
<script type="text/javascript">

var jakdukApp = angular.module("jakdukApp", []);

jakdukApp.controller("writeCtrl", function($scope, $http) {
	$scope.emailConn = 0;
	$scope.usernameConn = 0;
	$scope.emailAlert = {};
	$scope.usernameAlert = {};
	$scope.passwordAlert = {};
	$scope.passwordConfirmAlert = {};
	
	$scope.onSubmit = function(userWrite, event) {
		if (userWrite.$valid && $scope.existEmail == 2 && $scope.existUsername == 2 && $scope.password == $scope.passwordConfirm) {
		} else {
			if (userWrite.email.$invalid) {
				checkEmail(userWrite);
			} else if ($scope.existEmail != 2) {
				$scope.emailAlert = {"classType":"text-danger", "msg":'<spring:message code="common.msg.error.shoud.check.redudancy"/>'};
			}
			
			if (userWrite.username.$invalid) {
				checkUsername(userWrite);
			} else if ($scope.existUsername != 2) {
				$scope.usernameAlert = {"classType":"text-danger", "msg":'<spring:message code="common.msg.error.shoud.check.redudancy"/>'};
			}
			
			$scope.checkPassword(userWrite);
			$scope.checkPasswordConfirm(userWrite);

			event.preventDefault();
		}
	};
	
	$scope.onEmail = function(userWrite) {
		if (userWrite.email.$valid) {
			var bUrl = '<c:url value="/check/user/email.json?email=' + $scope.email + '"/>';
			if ($scope.emailConn == 0) {
				var reqPromise = $http.get(bUrl);
				$scope.emailConn = 1;
				reqPromise.success(function(data, status, headers, config) {
					if (data.existEmail != null) {
						if (data.existEmail == false) {
							$scope.existEmail = 2;
							$scope.emailAlert = {"classType":"text-success", "msg":'<spring:message code="user.msg.avaliable.data"/>'};
						} else {
							$scope.existEmail = 1;
							$scope.emailAlert = {"classType":"text-danger", "msg":'<spring:message code="user.msg.replicated.data"/>'};
						}
					}
					$scope.emailConn = 0;					
				});
				reqPromise.error(emailError);
			}
		} else {
			$scope.existEmail = 1;
			checkEmail(userWrite);
		}
	};
	
	function emailError(data, status, headers, config) {
		$scope.emailConn = 0;
		$scope.emailAlert = {"classType":"text-danger", "msg":'<spring:message code="common.msg.error.network.unstable"/>'};
	}
	
	$scope.onUsername = function(userWrite) {
		if (userWrite.username.$valid) {
			var bUrl = '<c:url value="/check/user/username.json?username=' + $scope.username + '"/>';
			if ($scope.usernameConn == 0) {
				var reqPromise = $http.get(bUrl);
				$scope.usernameConn = 1;
				reqPromise.success(function(data, status, headers, config) {
					if (data.existUsername != null) {
						if (data.existUsername == false) {
							$scope.existUsername = 2;
							$scope.usernameAlert = {"classType":"text-success", "msg":'<spring:message code="user.msg.avaliable.data"/>'};
						} else {
							$scope.existUsername = 1;
							$scope.usernameAlert = {"classType":"text-danger", "msg":'<spring:message code="user.msg.replicated.data"/>'};
						}
					}
					$scope.usernameConn = 0;
				});
				reqPromise.error(usernameError);
			}
		} else {
			$scope.existUsername = 1;
			checkUsername(userWrite);
		}
	};
	
	function usernameError(data, status, headers, config) {
		$scope.usernameConn = 0;
		$scope.usernameAlert = {"classType":"text-danger", "msg":'<spring:message code="common.msg.error.network.unstable"/>'};
	}
	
	function checkEmail(userWrite) {
		if (userWrite.email.$error.required) {
			$scope.emailAlert = {"classType":"text-danger", "msg":'<spring:message code="common.msg.required"/>'};
		} else if (userWrite.email.$error.minlength || userWrite.email.$error.maxlength) {
			$scope.emailAlert = {"classType":"text-danger", "msg":'<spring:message code="Size.userWrite.email"/>'};
		} else if (userWrite.email.$error.pattern) {
			$scope.emailAlert = {"classType":"text-danger", "msg":'<spring:message code="user.msg.check.mail.format"/>'};
		}
	}
		
	function checkUsername(userWrite) {
		if (userWrite.username.$error.required) {
			$scope.usernameAlert = {"classType":"text-danger", "msg":'<spring:message code="common.msg.required"/>'};
		} else if (userWrite.username.$error.minlength || userWrite.username.$error.maxlength) {
			$scope.usernameAlert = {"classType":"text-danger", "msg":'<spring:message code="Size.userWrite.username"/>'};
		}
	}
	
	$scope.checkPassword = function(userWrite) {
		if (userWrite.password.$invalid) {
			if (userWrite.password.$error.required) {
				$scope.passwordAlert = {"classType":"text-danger", "msg":'<spring:message code="common.msg.required"/>'};
			} else if (userWrite.password.$error.minlength || userWrite.password.$error.maxlength) {
				$scope.passwordAlert = {"classType":"text-danger", "msg":'<spring:message code="Size.userWrite.password"/>'};
			}
		} else {
			$scope.passwordAlert = {};
		}
	};
	
	$scope.checkPasswordConfirm = function(userWrite) {
		if (userWrite.passwordConfirm.$invalid || $scope.password != $scope.passwordConfirm) {
			if (userWrite.passwordConfirm.$error.required) {
				$scope.passwordConfirmAlert = {"classType":"text-danger", "msg":'<spring:message code="common.msg.required"/>'};
			} else if (userWrite.passwordConfirm.$error.minlength || userWrite.passwordConfirm.$error.maxlength) {
				$scope.passwordConfirmAlert = {"classType":"text-danger", "msg":'<spring:message code="Size.userWrite.password"/>'};
			} else if ($scope.password != $scope.passwordConfirm) {
				$scope.passwordConfirmAlert = {"classType":"text-danger", "msg":'<spring:message code="user.msg.password.mismatch"/>'};
			}
		} else {
			$scope.passwordConfirmAlert = {};
		}
	};	
});
</script>

<!-- This script should be under the AngularJS which is creating jakdukApp module. -->
<script src="<%=request.getContextPath()%>/web-resources/jakduk/navigation-header.js"></script>

</body>
</html>