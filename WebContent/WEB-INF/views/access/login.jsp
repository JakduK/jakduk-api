<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<!DOCTYPE html>
<html ng-app="jakdukApp">
<head>
<jsp:include page="../include/html-header.jsp"></jsp:include>

<link href="<%=request.getContextPath()%>/resources/font-awesome/css/font-awesome.min.css" rel="stylesheet">

<!-- Custom styles for this template -->
<link href="<%=request.getContextPath()%>/resources/bootstrap/css/signin.css" rel="stylesheet">
</head>
<body>
<div class="container" ng-controller="loginCtrl">
	<div class="row">
		<div class="col-md-4 col-md-offset-4">
			<h4><a class="text-muted" href="<c:url value="/home"/>"><spring:message code="common.jakduk"/></a></h4>
			<div class="login-panel panel panel-default">
				<div class="panel-heading">
					<h3 class="panel-title"><spring:message code="user.sign.in"/></h3>
				</div>
		  	<div class="panel-body">
				<c:choose>
					<c:when test="${result == 'failure'}">
						<div class="alert alert-danger" role="alert"><spring:message code="user.msg.login.failure"/></div>
					</c:when>
					<c:when test="${result == 'locked'}">
						<div class="alert alert-danger" role="alert"><spring:message code="user.msg.login.failure.locked"/></div>
					</c:when>					
					<c:when test="${result == 'sucess'}">
						<div class="alert alert-success" role="alert"><spring:message code="user.msg.register.success"/></div>
					</c:when>                		
				</c:choose>
				<form action="j_spring_security_check" name="loginForm" method="post" ng-submit="onSubmit($event)">
				<input type="hidden" name="loginRedirect" value="${loginRedirect}"/>
					<fieldset>
						<div class="form-group has-feedback" ng-class="{'has-success':loginForm.j_username.$valid, 
						'has-error':loginForm.j_username.$invalid}">
							<label class="control-label sr-only" for="j_username">Hidden label</label>
							<input type="email" class="form-control" id="j_username" name="j_username" placeholder='<spring:message code="user.placeholder.email"/>'
							ng-model="email" ng-required="true" ng-minlength="6" ng-maxlength="20" 
							ng-pattern="/^[\w]{3,}@[\w]+(\.[\w-]+){1,3}$/" autofocus>
							<span class="glyphicon form-control-feedback" 
							ng-class="{'glyphicon-ok':loginForm.j_username.$valid, 'glyphicon-remove':loginForm.j_username.$invalid}"></span>
							<span class="text-danger" ng-model="errorEmail" ng-show="errorEmail">{{errorEmail}}</span>
						</div>
					
						<div class="form-group has-feedback"
						ng-class="{'has-success':loginForm.j_password.$valid, 'has-error':loginForm.j_password.$invalid}">
							<label class="control-label sr-only" for="password">Hidden label</label>
							<input type="password" class="form-control" id="j_password" name="j_password" placeholder='<spring:message code="user.placeholder.password"/>'
							ng-model="password" ng-required="true" ng-minlength="4" ng-maxlength="20">
							<span class="glyphicon form-control-feedback" 
							ng-class="{'glyphicon-ok':loginForm.j_password.$valid, 'glyphicon-remove':loginForm.j_password.$invalid}"></span>							
							<span class="text-danger" ng-model="errorPassword" ng-show="errorPassword">{{errorPassword}}</span>                                    
						</div>
						<div class="checkbox">
							<label>
								<input type="checkbox" name="remember" ng-model="remember">
								<spring:message code="user.email.remember"/>
							</label>
						</div>
						<button type="submit" class="btn btn-lg btn-success btn-block">
							<span class="glyphicon glyphicon-log-in"></span> <spring:message code="user.sign.in"/>
						</button>
					</fieldset>
				</form>
		
				<p/>
		
				<div class="list-group">
					<div class="list-group-item">
						<spring:message code="user.msg.not.user"/> 
						<a href="<c:url value="/user/write"/>"><strong><spring:message code="user.msg.register.here"/></strong></a>
					</div>
					<div class="list-group-item">
						<h5 class="list-group-item-heading"><spring:message code="user.msg.register.oauth"/></h5>
						<div class="list-group-item-text">
							<div class="row">
								<div class="col-xs-3 col-md-3">
									<a href="<c:url value="/oauth/callback?type=facebook&loginRedirect=${loginRedirect}"/>">
										<i class="fa fa-facebook-square fa-3x"></i>
									</a>
								</div>
								<div class="col-xs-3 col-md-3">
									<a href="<c:url value="/oauth/callback?type=daum&loginRedirect=${loginRedirect}"/>">
										<img src="<%=request.getContextPath()%>/resources/jakduk/icon/daum_bt.png" style="width: 40px; height: 40px;"  alt="Daum">
									</a>
									</div>  
								</div> 	
							</div>
						</div>
					</div>
				</div> <!-- /.panel-body -->
			</div>
			<jsp:include page="../include/footer.jsp"/>      
		</div>		
	</div>
	
</div><!-- /.container -->

<script src="<%=request.getContextPath()%>/resources/angular/js/angular.min.js"></script>
<script src="<%=request.getContextPath()%>/resources/angular/js/angular-cookies.min.js"></script>
<script type="text/javascript">

var jakdukApp = angular.module("jakdukApp", ["ngCookies"]);

jakdukApp.controller("loginCtrl", function($scope, $cookieStore) {
	var email = $cookieStore.get("email");
	var remember = $cookieStore.get("remember");
	
	if (remember != null && email != null && remember == "1") {
		$scope.email = email;
		$scope.remember = true;
	}
	
	$scope.onSubmit = function(event){
		if ($scope.loginForm.$valid) {
		} else {
			$scope.onEmail();
			
			if ($scope.loginForm.j_password.$error.required) {
				$scope.errorPassword = '<spring:message code="common.msg.required"/>'; 
			} else if ($scope.loginForm.j_password.$error.minlength || $scope.loginForm.j_password.$error.maxlength) {
				$scope.errorPassword = '<spring:message code="Size.userWrite.password"/>';
			}
			event.preventDefault();
		}
	};
	
	$scope.onEmail = function() {
		if ($scope.loginForm.j_username.$invalid) {
			if ($scope.loginForm.j_username.$error.required) {
				$scope.errorEmail = '<spring:message code="common.msg.required"/>'; 
			} else if ($scope.loginForm.j_username.$error.minlength || $scope.loginForm.j_username.$error.maxlength) {
				$scope.errorEmail = '<spring:message code="Size.userWrite.email"/>';
			} else if ($scope.loginForm.j_username.$error.pattern) {
				$scope.errorEmail = '<spring:message code="user.msg.check.mail.format"/>';
			}
		} else {
			$scope.errorEmail = "";
		}
	};
	
});
</script>
</body>
</html>