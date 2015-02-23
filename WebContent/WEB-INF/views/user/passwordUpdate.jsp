<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>  
  
<!DOCTYPE html>
<html ng-app="jakdukApp">
<head>
	<title><spring:message code="user.password.change"/> &middot; <spring:message code="common.jakduk"/></title>
	<jsp:include page="../include/html-header.jsp"></jsp:include>
	
	<link href="<%=request.getContextPath()%>/resources/font-awesome/css/font-awesome.min.css" rel="stylesheet">
</head>
<body>
<div class="container" ng-controller="writeCtrl">
	<jsp:include page="../include/navigation-header.jsp"/>
	
	<c:set var="contextPath" value="<%=request.getContextPath()%>"/>
	<form:form commandName="userPasswordUpdate" name="userPasswordUpdate" action="${contextPath}/user/password/update" method="POST" cssClass="form-horizontal"
	ng-submit="onSubmit($event)">
		<legend><spring:message code="user.password.change"/> </legend>
		
		<div class="form-group has-feedback" 
		ng-class="{'has-success':userPasswordUpdate.oldPassword.$valid, 'has-error':userPasswordUpdate.oldPassword.$invalid}">
			<label class="col-sm-2 control-label" for="oldPassword">
				<abbr title='<spring:message code="common.msg.required"/>'>*</abbr> <spring:message code="user.password.old"/>
			</label>
			<div class="col-sm-3">
				<input type="password" name="oldPassword" class="form-control" placeholder='<spring:message code="user.placeholder.old.password"/>'
				ng-model="oldPassword" ng-blur="checkOldPassword()" ng-required="true" ng-minlength="4" ng-maxlength="20"/>
				<span class="glyphicon form-control-feedback" ng-class="{'glyphicon-ok':userPasswordUpdate.oldPassword.$valid, 
				'glyphicon-remove':userPasswordUpdate.oldPassword.$invalid}"></span>
				<form:errors path="oldPassword" cssClass="text-danger" element="span" ng-hide="oldPasswordAlert.msg"/>
				<span class="{{oldPasswordAlert.classType}}" ng-show="oldPasswordAlert.msg">{{oldPasswordAlert.msg}}</span>	
			</div>
		</div>
		
		<div class="form-group has-feedback" 
		ng-class="{'has-success':userPasswordUpdate.newPassword.$valid, 'has-error':userPasswordUpdate.newPassword.$invalid}">
			<label class="col-sm-2 control-label" for="newPassword">
				<abbr title='<spring:message code="common.msg.required"/>'>*</abbr> <spring:message code="user.password.new"/>
			</label>
			<div class="col-sm-3">
				<input type="password" name="newPassword" class="form-control" placeholder='<spring:message code="user.placeholder.new.password"/>'
				ng-model="newPassword" ng-blur="checkNewPassword()" ng-required="true" ng-minlength="4" ng-maxlength="20"/>
				<span class="glyphicon form-control-feedback" ng-class="{'glyphicon-ok':userPasswordUpdate.newPassword.$valid, 
				'glyphicon-remove':userPasswordUpdate.newPassword.$invalid}"></span>
				<form:errors path="newPassword" cssClass="text-danger" element="span" ng-hide="newPasswordAlert.msg"/>
				<span class="{{newPasswordAlert.classType}}" ng-show="newPasswordAlert.msg">{{newPasswordAlert.msg}}</span>	
			</div>
		</div>	
		
		<div class="form-group has-feedback" ng-class="{'has-success':userPasswordUpdate.newPasswordConfirm.$valid,	
		'has-error':userPasswordUpdate.newPasswordConfirm.$invalid || newPasswordConfirm.length > 0 && newPassword != newPasswordConfirm}">
			<label class="col-sm-2 control-label" for="newPasswordConfirm">
				<abbr title='<spring:message code="common.msg.required"/>'>*</abbr> <spring:message code="user.password.new.confirm"/>
			</label>
			<div class="col-sm-3">
			<input type="password" name="newPasswordConfirm" class="form-control" placeholder='<spring:message code="user.placeholder.confirm.new.password"/>'
				ng-model="newPasswordConfirm" ng-blur="checkNewPasswordConfirm()" ng-required="true" ng-minlength="4" ng-maxlength="20"/>
				<span class="glyphicon form-control-feedback" ng-class="{'glyphicon-ok':userPasswordUpdate.newPasswordConfirm.$valid, 
				'glyphicon-remove':userPasswordUpdate.newPasswordConfirm.$invalid || newPasswordConfirm.length > 0 && newPassword != newPasswordConfirm}"></span>
				<form:errors path="newPasswordConfirm" cssClass="text-danger" element="span" 
				ng-hide="newPasswordConfirmAlert.msg || (newPasswordConfirm.length > 0 && newPassword == newPasswordConfirm)"/>
				<span class="{{newPasswordConfirmAlert.classType}}" ng-show="newPasswordConfirmAlert.msg">{{newPasswordConfirmAlert.msg}}</span>								
			</div>
		</div>	
		
		<div class="form-group">
			<div class="col-sm-offset-2 col-sm-4">
				<button type="submit" class="btn btn-success">
					<span class="glyphicon glyphicon-upload"></span> <spring:message code="common.button.submit"/>
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
	
	<jsp:include page="../include/footer.jsp"/>
</div><!-- /.container -->

<!-- Bootstrap core JavaScript
  ================================================== -->
<!-- Placed at the end of the document so the pages load faster -->
<script src="<%=request.getContextPath()%>/resources/jquery/dist/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/resources/bootstrap/dist/js/bootstrap.min.js"></script>    

<script type="text/javascript">

window.onbeforeunload = function(e) {
	if (!submitted) {
		(e || window.event).returnValue = '<spring:message code="common.msg.are.you.sure.leave.page"/>';
		return '<spring:message code="common.msg.are.you.sure.leave.page"/>';
	}
};

var submitted = false;
var jakdukApp = angular.module("jakdukApp", []);

jakdukApp.controller("writeCtrl", function($scope) {
	$scope.oldPasswordAlert = {};
	$scope.newPasswordAlert = {};
	$scope.newPasswordConfirmAlert = {};
	
	$scope.onSubmit = function(event) {
		if ($scope.userPasswordUpdate.$valid && $scope.newPassword == $scope.newPasswordConfirm) {
			submitted = true;
			$scope.submitConn = "connecting";
			$scope.buttonAlert = {"classType":"text-info", "msg":'<spring:message code="common.msg.be.cummunicating.server"/>'};
		} else {
			$scope.checkOldPassword();
			$scope.checkNewPassword();
			$scope.checkNewPasswordConfirm();

			$scope.submitConn = "none";
			$scope.buttonAlert = {"classType":"text-danger", "msg":'<spring:message code="common.msg.need.form.validation"/>'};
			event.preventDefault();
		}
	};
	
	$scope.checkOldPassword = function() {
		if ($scope.userPasswordUpdate.oldPassword.$invalid) {
			if ($scope.userPasswordUpdate.oldPassword.$error.required) {
				$scope.oldPasswordAlert = {"classType":"text-danger", "msg":'<spring:message code="common.msg.required"/>'};
			} else if ($scope.userPasswordUpdate.oldPassword.$error.minlength || $scope.userPasswordUpdate.oldPassword.$error.maxlength) {
				$scope.oldPasswordAlert = {"classType":"text-danger", "msg":'<spring:message code="Size.userWrite.password"/>'};
			}
		} else {
			$scope.oldPasswordAlert = {};
		}
	};
	
	$scope.checkNewPassword = function() {
		if ($scope.userPasswordUpdate.newPassword.$invalid) {
			if ($scope.userPasswordUpdate.newPassword.$error.required) {
				$scope.newPasswordAlert = {"classType":"text-danger", "msg":'<spring:message code="common.msg.required"/>'};
			} else if ($scope.userPasswordUpdate.newPassword.$error.minlength || $scope.userPasswordUpdate.newPassword.$error.maxlength) {
				$scope.newPasswordAlert = {"classType":"text-danger", "msg":'<spring:message code="Size.userWrite.password"/>'};
			}
		} else {
			$scope.newPasswordAlert = {};
		}
	};
	
	$scope.checkNewPasswordConfirm = function() {
		if ($scope.userPasswordUpdate.newPasswordConfirm.$invalid || $scope.newPassword != $scope.newPasswordConfirm) {
			if ($scope.userPasswordUpdate.newPasswordConfirm.$error.required) {
				$scope.newPasswordConfirmAlert = {"classType":"text-danger", "msg":'<spring:message code="common.msg.required"/>'};
			} else if ($scope.userPasswordUpdate.newPasswordConfirm.$error.minlength || $scope.userPasswordUpdate.newPasswordConfirm.$error.maxlength) {
				$scope.newPasswordConfirmAlert = {"classType":"text-danger", "msg":'<spring:message code="Size.userWrite.password"/>'};
			} else if ($scope.newPassword != $scope.newPasswordConfirm) {
				$scope.newPasswordConfirmAlert = {"classType":"text-danger", "msg":'<spring:message code="user.msg.password.mismatch"/>'};
			}
		} else {
			$scope.newPasswordConfirmAlert = {};
		}
	};	
	
});
</script>

<jsp:include page="../include/body-footer.jsp"/>

</body>
</html>