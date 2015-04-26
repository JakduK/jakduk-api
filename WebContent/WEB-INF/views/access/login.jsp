<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<!DOCTYPE html>
<html ng-app="jakdukApp">
<head>
	<title><spring:message code="user.sign.in"/> &middot; <spring:message code="common.jakduk"/></title>

    <!-- CSS Page Style -->    
    <link rel="stylesheet" href="<%=request.getContextPath()%>/resources/unify/assets/css/pages/page_log_reg_v1.css">  
    <link rel="stylesheet" href="<%=request.getContextPath()%>/resources/unify/assets/plugins/ladda-buttons/css/custom-lada-btn.css">

   	<jsp:include page="../include/html-header.jsp"></jsp:include>
</head>

<body>
<div class="wrapper">
	<jsp:include page="../include/navigation-header.jsp"/>
	
	<!--=== Breadcrumbs ===-->
	<div class="breadcrumbs">
		<div class="container">
			<h1 class="pull-left"><spring:message code="user.sign.in"/></h1>
		</div><!--/container-->
	</div><!--/breadcrumbs-->
	<!--=== End Breadcrumbs ===-->		
	
<div class="container content" ng-controller="loginCtrl">		
				<c:choose>
					<c:when test="${result == 'failure'}">
						<div class="alert alert-danger" role="alert"><spring:message code="user.msg.login.failure"/></div>
					</c:when>
					<c:when test="${result == 'locked'}">
						<div class="alert alert-danger" role="alert"><spring:message code="user.msg.login.failure.locked"/></div>
					</c:when>					
					<c:when test="${result == 'success'}">
						<div class="alert alert-success" role="alert"><spring:message code="user.msg.register.success"/></div>
					</c:when>                		
				</c:choose>

    	<div class="row">
            <div class="col-md-4 col-md-offset-4 col-sm-6 col-sm-offset-3">
		<form action="login" class="reg-page" name="loginForm" method="POST" ng-submit="onSubmit($event)">		
			<input type="hidden" name="loginRedirect" value="${loginRedirect}"/>
                    <div class="reg-header">            
                        <h2><spring:message code="user.sign.in.header"/></h2>
                    </div>

						<div class="input-group has-feedback" ng-class="{'has-success':loginForm.j_username.$valid, 
						'has-error':loginForm.j_username.$invalid}">
						<span class="input-group-addon"><i class="fa fa-user"></i></span>
							<input type="email" class="form-control" id="j_username" name="j_username" placeholder='<spring:message code="user.placeholder.email"/>'
							ng-model="email" ng-required="true" ng-minlength="6" ng-maxlength="20" 
							ng-pattern="/^[\w]{3,}@[\w]+(\.[\w-]+){1,3}$/" autofocus>
							<span class="glyphicon form-control-feedback" 
							ng-class="{'glyphicon-ok':loginForm.j_username.$valid, 'glyphicon-remove':loginForm.j_username.$invalid}"></span>
						</div>
						<div class="margin-bottom-20">
							<span class="text-danger" ng-model="errorEmail" ng-show="errorEmail">{{errorEmail}}</span>
						</div>
						
						<div class="input-group has-feedback"
						ng-class="{'has-success':loginForm.j_password.$valid, 'has-error':loginForm.j_password.$invalid}">
							<span class="input-group-addon"><i class="fa fa-lock"></i></span>
							<input type="password" class="form-control" id="j_password" name="j_password" placeholder='<spring:message code="user.placeholder.password"/>'
							ng-model="password" ng-required="true" ng-minlength="4" ng-maxlength="20">
							<span class="glyphicon form-control-feedback" 
							ng-class="{'glyphicon-ok':loginForm.j_password.$valid, 'glyphicon-remove':loginForm.j_password.$invalid}"></span>							
						</div>			
						<div class="margin-bottom-20">
							<span class="text-danger" ng-model="errorPassword" ng-show="errorPassword">{{errorPassword}}</span>	
						</div>									
                        
                    <div class="row">
                        <div class="col-md-8 checkbox">
							<label>
								<input type="checkbox" name="remember" ng-model="remember">
								<spring:message code="user.email.remember"/>
							</label>                  
                        </div>
                        <div class="col-md-4">
						<button type="submit" class="btn-u rounded pull-right ladda-button"
						ladda="btnSubmit" data-style="expand-right">
							<spring:message code="user.sign.in"/>
						</button>                          
                        </div>
                    </div>

                    <hr>

<p>
						<spring:message code="user.msg.not.user"/> 
						<a href="<c:url value="/user/write"/>">
							<strong><i class="fa fa-user-plus"></i> <spring:message code="user.msg.register.here"/></strong>
						</a>
</p>
						<h5><spring:message code="user.msg.register.oauth"/></h5>
							<div class="row">
								<div class="col-xs-3 col-md-3">
									<a href="<c:url value="/oauth/callback?type=facebook&loginRedirect=${loginRedirect}"/>">
										<i class="fa fa-facebook-square fa-3x text-primary"></i>
									</a>
								</div>
								<div class="col-xs-3 col-md-3">
									<a href="<c:url value="/oauth/callback?type=daum&loginRedirect=${loginRedirect}"/>">
										<img src="<%=request.getContextPath()%>/resources/jakduk/icon/daum_bt.png" style="width: 40px; height: 40px;"  alt="Daum">
									</a>
									</div>  
								</div> 	

                </form>            
            </div>
        </div><!--/row-->
    </div>
    	
	<jsp:include page="../include/footer.jsp"/>
</div><!-- /.container -->

<script src="<%=request.getContextPath()%>/resources/jquery/dist/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/resources/angular-cookies/angular-cookies.min.js"></script>

<!-- JS Implementing Plugins -->
<script src="<%=request.getContextPath()%>/resources/unify/assets/plugins/ladda-buttons/js/spin.min.js"></script>
<script src="<%=request.getContextPath()%>/resources/unify/assets/plugins/ladda-buttons/js/ladda.min.js"></script>

<script src="<%=request.getContextPath()%>/resources/angular-ladda/dist/angular-ladda.min.js"></script>

<script type="text/javascript">

var jakdukApp = angular.module("jakdukApp", ["ngCookies", "angular-ladda"]);

jakdukApp.controller("loginCtrl", function($scope, $cookieStore) {
	var email = $cookieStore.get("email");
	var remember = $cookieStore.get("remember");
	
	if (remember != null && email != null && remember == "1") {
		$scope.email = email;
		$scope.remember = true;
	}
	
	$scope.onSubmit = function(event){
		if ($scope.loginForm.$valid) {
			$scope.btnSubmit = true;
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
<jsp:include page="../include/body-footer.jsp"/>

</body>
</html>
