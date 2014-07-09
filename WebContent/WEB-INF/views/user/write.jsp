<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html ng-app>
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
		<form:hidden path="checkEmail" ng-init="checkEmail='${userWrite.checkEmail}'" ng-model="checkEmail"/>
		<form:hidden path="checkUsername" ng-init="checkUsername='${userWrite.checkUsername}'" ng-model="checkUsername"/>
		<legend><spring:message code="user.register"/> </legend>
		<div class="form-group" ng-class="{'has-success':userWrite.email.$valid, 'has-error':userWrite.email.$invalid}">
			<label class="col-sm-2 control-label" for="email">
				<abbr title='<spring:message code="user.msg.required"/>'>*</abbr> <spring:message code="user.email"/>
			</label>
			<div class="col-sm-4">
			    <div class="input-group">
					<input type="email" name="email" class="form-control" size="50" placeholder="Email" 
					ng-init="email='${userWrite.email}'" ng-model="email" 
					ng-disabled="checkEmail === false" 
					ng-required="true" ng-minlength="6" ng-maxlength="20" ng-pattern="/^[\w]{3,}@[\w]+(\.[\w-]+){1,3}$/"/>
			      <span class="input-group-btn">
			        <button type="button" class="btn btn-default" ng-click="onCheckEmail(userWrite)">
			        	<spring:message code="common.button.redundancy.check"/>
			        </button>
			      </span>
			    </div><!-- /input-group -->				
				<form:errors path="email" cssClass="text-danger" element="span"/>
				<span class="text-danger" ng-show="userWrite.email.$error.required">
					<spring:message code="user.msg.required"/>
				</span>
				<span class="text-danger" ng-show="userWrite.email.$error.minlength || userWrite.email.$error.maxlength">
					<spring:message code="Size.userWrite.email"/>
				</span>
				<span class="text-danger" ng-show="userWrite.email.$error.pattern">
					<spring:message code="user.msg.check.mail.format"/>
				</span>
				<span class="text-danger" ng-show="checkEmail != null && checkEmail === true">
					<spring:message code="user.msg.replicated.data"/>
				</span>
				<span class="text-success" ng-show="checkEmail != null && checkEmail === false">
					<spring:message code="user.msg.avaliable.data"/>
				</span>
				<span class="text-danger" ng-model="errorEmail" ng-show="errorEmail">{{errorEmail}}</span>
			</div>	
		</div>
		<div class="form-group" ng-class="{'has-success':userWrite.username.$valid, 'has-error':userWrite.username.$invalid}">
			<label class="col-sm-2 control-label" for="username">
				<abbr title='<spring:message code="user.msg.required"/>'>*</abbr> <spring:message code="user.nickname"/>
			</label>
			<div class="col-sm-4">
				<div class="input-group">
					<form:input path="username" cssClass="form-control" size="50" placeholder="Nickname" 
					ng-init="username='${userWrite.username}'" ng-model="username" ng-disabled="checkUsername === false"
					ng-required="true" ng-minlength="2" ng-maxlength="20"/>
			      <span class="input-group-btn">
			        <button type="button" class="btn btn-default" ng-click="onCheckUsername(userWrite)">
			        	<spring:message code="common.button.redundancy.check"/>
			        </button>
			      </span>
			    </div><!-- /input-group -->	
				
				<form:errors path="username" cssClass="text-danger" element="span"/>
				<span class="text-danger" ng-show="userWrite.username.$error.required">
					<spring:message code="user.msg.required"/>
				</span>
				<span class="text-danger" ng-show="userWrite.username.$error.minlength || userWrite.username.$error.maxlength">
					<spring:message code="Size.userWrite.username"/>
				</span>
				<span class="text-danger" ng-show="checkUsername != null && checkUsername === true">
					<spring:message code="user.msg.replicated.data"/>
				</span>
				<span class="text-success" ng-show="checkUsername != null && checkUsername === false">
					<spring:message code="user.msg.avaliable.data"/>
				</span>
				<span class="text-danger" ng-model="errorUsername" ng-show="errorUsername">{{errorUsername}}</span>				
			</div>
		</div>
		<div class="form-group" ng-class="{'has-success':userWrite.password.$valid, 'has-error':userWrite.password.$invalid}">
			<label class="col-sm-2 control-label" for="password">
				<abbr title='<spring:message code="user.msg.required"/>'>*</abbr> <spring:message code="user.password"/>
			</label>
			<div class="col-sm-3">
				<form:password path="password" cssClass="form-control" size="50" placeholder="Password"
				ng-model="password" ng-required="true" ng-minlength="4" ng-maxlength="20"/>
				<form:errors path="password" cssClass="alert alert-warning" element="div"/>
				<span class="text-danger" ng-show="userWrite.password.$error.required">
					<spring:message code="user.msg.required"/>
				</span>
				<span class="text-danger" ng-show="userWrite.password.$error.minlength || userWrite.password.$error.maxlength">
					<spring:message code="Size.userWrite.password"/>
				</span>
			</div>
		</div>
		<div class="form-group" 
		ng-class="{'has-success':userWrite.passwordConfirm.$valid, 'has-error':userWrite.passwordConfirm.$invalid || passwordConfirm.length > 0 && password != passwordConfirm}">
			<label class="col-sm-2 control-label" for="passwordConfirm">
				<abbr title='<spring:message code="user.msg.required"/>'>*</abbr> <spring:message code="user.password.confirm"/>
			</label>
			<div class="col-sm-3">
				<form:password path="passwordConfirm" cssClass="form-control" size="50" placeholder="Confirm password"
				ng-model="passwordConfirm" ng-required="true"/>
				<form:errors path="passwordConfirm" cssClass="alert alert-warning" element="div"/>
				<span class="text-danger" ng-show="userWrite.passwordConfirm.$error.required">
					<spring:message code="user.msg.required"/>
				</span>				
				<span class="text-danger" ng-show="passwordConfirm.length > 0 && password != passwordConfirm">
					<spring:message code="user.msg.password.mismatch"/>
				</span>								
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
function writeCtrl($scope, $http) {
	$scope.emailConn = 0;
	$scope.usernameConn = 0;
	
	$scope.onSubmit = function(userWrite, event) {
		
		if (userWrite.$valid && $scope.checkEmail === false && $scope.checkUsername === false) {
		} else {
			if ($scope.checkEmail !== false) {
				$scope.errorEmail = '<spring:message code="common.msg.error.shoud.check.redudancy"/>';
			}
			
			if ($scope.checkUsername !== false) {
				$scope.errorUsername = '<spring:message code="common.msg.error.shoud.check.redudancy"/>';
			}
			event.preventDefault();
		}
	};
	
	$scope.onCheckEmail = function(userWrite) {
		if (userWrite.email.$valid) {
			var bUrl = '<c:url value="/check/user/email.json?email=' + $scope.email + '"/>';
			if ($scope.emailConn == 0) {
				var reqPromise = $http.get(bUrl);
				$scope.emailConn = 1;
				reqPromise.success(function(data, status, headers, config) {
					if (data.existEmail != null) {
						if (data.existEmail == false) {
							$scope.checkEmail = false;
						} else {
							$scope.checkEmail = true;
						}
					}
					$scope.emailConn = 0;
					$scope.errorEmail = "";
				});
				reqPromise.error(checkEmailError);
			}
		}
	};
	
	function checkEmailError(data, status, headers, config) {
		$scope.emailConn = 0;
		$scope.errorEmail = '<spring:message code="common.msg.error.network.unstable"/>';
	}
	
	$scope.onCheckUsername = function(userWrite) {
		if (userWrite.username.$valid) {
			var bUrl = '<c:url value="/check/user/username.json?username=' + $scope.username + '"/>';
			if ($scope.usernameConn == 0) {
				var reqPromise = $http.get(bUrl);
				$scope.usernameConn = 1;
				reqPromise.success(function(data, status, headers, config) {
					if (data.existUsername != null) {
						if (data.existUsername == false) {
							$scope.checkUsername = false;
						} else {
							$scope.checkUsername = true;
						}
					}
					$scope.usernameConn = 0;
					$scope.errorUsername = "";
				});
				reqPromise.error(checkUsernameError);
			}
		}
	};
	
	function checkUsernameError(data, status, headers, config) {
		$scope.checkUsernameError = 0;
		$scope.errorUsername = '<spring:message code="common.msg.error.network.unstable"/>';
	}
	
}
</script>

</body>
</html>