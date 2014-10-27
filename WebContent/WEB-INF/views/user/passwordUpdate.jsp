<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
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
<form:form commandName="userPasswordUpdate" name="userPasswordUpdate" action="${contextPath}/user/password/update" method="POST" cssClass="form-horizontal"
ng-submit="onSubmit(userPasswordUpdate, $event)">
	<legend><spring:message code="user.password.change"/> </legend>
	<div class="form-group" ng-class="{'has-success':userWrite.password.$valid, 'has-error':userWrite.password.$invalid}">
		<label class="col-sm-2 control-label" for="oldPassword">
			<abbr title='<spring:message code="common.msg.required"/>'>*</abbr> <spring:message code="user.password.old"/>
		</label>
		<div class="col-sm-3">
			<form:password path="oldPassword" cssClass="form-control" size="50" placeholder="Old password"
		ng-model="oldPassword" ng-blur="checkPassword(userPasswordUpdate)" ng-required="true" ng-minlength="4" ng-maxlength="20"/>
			<form:errors path="oldPassword" cssClass="text-danger" element="span" ng-hide="passwordAlert.msg"/>
			<span class="{{passwordAlert.classType}}" ng-show="passwordAlert.msg">{{passwordAlert.msg}}</span>	
		</div>
	</div>
	<div class="form-group" ng-class="{'has-success':userWrite.password.$valid, 'has-error':userWrite.password.$invalid}">
		<label class="col-sm-2 control-label" for="newPassword">
			<abbr title='<spring:message code="common.msg.required"/>'>*</abbr> <spring:message code="user.password.new"/>
		</label>
		<div class="col-sm-3">
			<form:password path="newPassword" cssClass="form-control" size="50" placeholder="New password"
		ng-model="newPassword" ng-blur="checkPassword(userPasswordUpdate)" ng-required="true" ng-minlength="4" ng-maxlength="20"/>
			<form:errors path="newPassword" cssClass="text-danger" element="span" ng-hide="passwordAlert.msg"/>
			<span class="{{passwordAlert.classType}}" ng-show="passwordAlert.msg">{{passwordAlert.msg}}</span>	
		</div>
	</div>	
	<div class="form-group" 
	ng-class="{'has-success':userWrite.passwordConfirm.$valid, 'has-error':userWrite.passwordConfirm.$invalid || passwordConfirm.length > 0 && password != passwordConfirm}">
		<label class="col-sm-2 control-label" for="newPasswordConfirm">
			<abbr title='<spring:message code="common.msg.required"/>'>*</abbr> <spring:message code="user.password.new.confirm"/>
	</label>
	<div class="col-sm-3">
		<form:password path="newPasswordConfirm" cssClass="form-control" size="50" placeholder="Confirm new password"
	ng-model="newPasswordConfirm" ng-blur="checkPasswordConfirm(userWrite)" ng-required="true" ng-minlength="4" ng-maxlength="20"/>
	<form:errors path="newPasswordConfirm" cssClass="text-danger" element="span" ng-hide="passwordConfirmAlert.msg || (passwordConfirm.length > 0 && password == passwordConfirm)"/>
			<span class="{{passwordConfirmAlert.classType}}" ng-show="passwordConfirmAlert.msg">{{passwordConfirmAlert.msg}}</span>								
		</div>
	</div>	
	<div class="form-group">
		<div class="col-sm-offset-2 col-sm-4">
			<input type="submit" value="<spring:message code="common.button.submit"/>" class="btn btn-default"/>
			<a class="btn btn-danger" href="<c:url value="/user/profile"/>"><spring:message code="common.button.cancel"/></a>
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
<script src="<%=request.getContextPath()%>/resources/bootstrap/js/bootstrap.min.js"></script>    

<script type="text/javascript">
var jakdukApp = angular.module("jakdukApp", []);

jakdukApp.controller("writeCtrl", function($scope) {
	
	$scope.onSubmit = function(userPasswordUpdate, event) {
		if (userProfileWrite.$valid && ($scope.usernameStatus == "ok" || $scope.usernameStatus == "original")) {
		} else {			
			if (userProfileWrite.username.$invalid) {
				checkUsername(userProfileWrite);
			} else if ($scope.usernameStatus == "duplication") {
				$scope.usernameAlert = {"classType":"text-danger", "msg":'<spring:message code="user.msg.replicated.data"/>'};
			} else {
				$scope.usernameAlert = {"classType":"text-danger", "msg":'<spring:message code="common.msg.error.shoud.check.redudancy"/>'};
			}

			event.preventDefault();
		}
	};	
	
});
</script>

<jsp:include page="../include/body-footer.jsp"/>

</body>
</html>