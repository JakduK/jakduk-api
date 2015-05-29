<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<!DOCTYPE html>
<html ng-app="jakdukApp">
<head>
	<title><spring:message code="user.register"/> &middot; <spring:message code="common.jakduk"/></title>
	
	<!-- CSS Page Style -->    
	<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/unify/assets/css/pages/page_log_reg_v1.css">	
	<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/unify/assets/plugins/ladda-buttons/css/custom-lada-btn.css">
	
	<jsp:include page="../include/html-header.jsp"></jsp:include>	
</head>

<body>
<div class="wrapper">
	<jsp:include page="../include/navigation-header.jsp"/>
	
	<c:set var="contextPath" value="<%=request.getContextPath()%>"/>
	
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

 <!-- Reg-Form -->
 <form:form commandName="userWrite" name="userWrite" action="${contextPath}/user/write" method="POST" cssClass="reg-page" 
	ng-submit="onSubmit($event)">
	
		<form:input path="emailStatus" cssClass="hidden" size="0" ng-init="emailStatus='${userWrite.emailStatus}'" ng-model="emailStatus"/>
		<form:input path="usernameStatus" cssClass="hidden" size="0" ng-init="usernameStatus='${userWrite.usernameStatus}'" ng-model="usernameStatus"/>
		
		<div class="reg-header">
		    <h2><spring:message code="user.register.header"/></h2>
		</div>

		<div class="form-group has-feedback" ng-class="{'has-success':userWrite.email.$valid, 
		'has-error':userWrite.email.$invalid || emailStatus != 'ok'}">
			<label class="control-label" for="email">
				<abbr title='<spring:message code="common.msg.required"/>'>*</abbr> <spring:message code="user.email"/>
			</label>
				<input type="email" name="email" class="form-control" placeholder='<spring:message code="user.placeholder.email"/>' 
				ng-init="email='${userWrite.email}'" ng-model="email" 
				ng-blur="onEmail()" ng-change="validationEmail()"
				ng-required="true" ng-minlength="emailLengthMin" ng-maxlength="emailLengthMax"
				ng-pattern="/^((?:(?:(?:[a-zA-Z0-9][\.\-\+_]?)*)[a-zA-Z0-9])+){3,}\@((?:(?:(?:[a-zA-Z0-9][\.\-_]?){1,20})[a-zA-Z0-9])+)\.([a-zA-Z0-9]{2,6})$/"/>
				<span class="glyphicon form-control-feedback" 
				ng-class="{'glyphicon-ok':userWrite.email.$valid, 'glyphicon-remove':userWrite.email.$invalid || emailStatus != 'ok'}"></span>					
				<i class="fa fa-spinner fa-spin" ng-show="emailConn == 'connecting'"></i>
				<form:errors path="email" cssClass="text-danger" element="span" ng-hide="emailAlert.msg"/>
				<span class="{{emailAlert.classType}}" ng-show="emailAlert.msg">{{emailAlert.msg}}</span>
		</div>
		
		<div class="form-group has-feedback" ng-class="{'has-success':userWrite.username.$valid, 
		'has-error':userWrite.username.$invalid || usernameStatus != 'ok'}">
			<label class="control-label" for="username">
				<abbr title='<spring:message code="common.msg.required"/>'>*</abbr> <spring:message code="user.nickname"/>
			</label>
				<input type="text" name="username" class="form-control" placeholder='<spring:message code="user.placeholder.username"/>'
				ng-init="username='${userWrite.username}'" ng-model="username" 
				ng-blur="onUsername()" ng-change="validationUsername()"
				ng-required="true" ng-minlength="usernameLengthMin" ng-maxlength="usernameLengthMax"/>
				<span class="glyphicon form-control-feedback" ng-class="{'glyphicon-ok':userWrite.username.$valid, 
				'glyphicon-remove':userWrite.username.$invalid || usernameStatus != 'ok'}"></span>
				<i class="fa fa-spinner fa-spin" ng-show="usernameConn == 'connecting'"></i>
				<form:errors path="username" cssClass="text-danger" element="span" ng-hide="usernameAlert.msg"/>
				<span class="{{usernameAlert.classType}}" ng-show="usernameAlert.msg">{{usernameAlert.msg}}</span>
			</div>
			
<div class="row">
    <div class="col-sm-6">
		<div class="form-group has-feedback" ng-class="{'has-success':userWrite.password.$valid, 
		'has-error':userWrite.password.$invalid}">
			<label class="control-label" for="password">
				<abbr title='<spring:message code="common.msg.required"/>'>*</abbr> <spring:message code="user.password"/>
			</label>
				<input type="password" name="password" class="form-control" placeholder='<spring:message code="user.placeholder.password"/>'
				ng-model="password" ng-change="vlidationPassword()" ng-blur="eaualPasswordConfirm()"
				ng-required="true" ng-minlength="passwordLengthMin" ng-maxlength="passwordLengthMax"/>
				<span class="glyphicon form-control-feedback" ng-class="{'glyphicon-ok':userWrite.password.$valid, 
				'glyphicon-remove':userWrite.password.$invalid}"></span>
				<form:errors path="password" cssClass="text-danger" element="span" ng-hide="passwordAlert.msg"/>
				<span class="{{passwordAlert.classType}}" ng-show="passwordAlert.msg">{{passwordAlert.msg}}</span>	
		</div>	
    </div>
    <div class="col-sm-6">
		<div class="form-group has-feedback" 	ng-class="{'has-success':userWrite.passwordConfirm.$valid, 
		'has-error':userWrite.passwordConfirm.$invalid || equalPasswordStatus != 'true'}">
			<label class="control-label" for="passwordConfirm">
				<abbr title='<spring:message code="common.msg.required"/>'>*</abbr> <spring:message code="user.password.confirm"/>
			</label>
				<input type="password" name="passwordConfirm" class="form-control" placeholder='<spring:message code="user.placeholder.password.confirm"/>'
				ng-model="passwordConfirm" ng-change="validationPasswordConfirm()" ng-blur="eaualPasswordConfirm()"
				ng-required="true" ng-minlength="passwordLengthMin" ng-maxlength="passwordLengthMax"/>
				<span class="glyphicon form-control-feedback" ng-class="{'glyphicon-ok':userWrite.passwordConfirm.$valid, 
				'glyphicon-remove':userWrite.passwordConfirm.$invalid || equalPasswordStatus != 'true'}"></span>
				<form:errors path="passwordConfirm" cssClass="text-danger" element="span" ng-hide="passwordConfirmAlert.msg || (passwordConfirm.length > 0 && password == passwordConfirm)"/>
				<span class="{{passwordConfirmAlert.classType}}" ng-show="passwordConfirmAlert.msg">{{passwordConfirmAlert.msg}}</span>								
		</div>		
    </div>
</div>			
		
	<div class="form-group">
			<label class="control-label" for="supportFC">
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
			<label class="control-label" for="about"> <spring:message code="user.comment"/></label>
				<textarea name="about" class="form-control" cols="40" rows="3" placeholder='<spring:message code="user.placeholder.about"/>'></textarea>
		</div>		
                    <hr>

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
		
	<jsp:include page="../include/footer.jsp"/>
</div><!-- /.container -->
	
<!-- Bootstrap core JavaScript
 ================================================== -->
<!-- Placed at the end of the document so the pages load faster -->
<script src="<%=request.getContextPath()%>/resources/jquery/dist/jquery.min.js"></script>
<!-- JS Implementing Plugins -->
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
	$scope.emailLengthMin = Jakduk.FormEmailLengthMin;
	$scope.emailLengthMax = Jakduk.FormEmailLengthMax;
	$scope.passwordLengthMin = Jakduk.FormPasswordLengthMin;
	$scope.passwordLengthMax = Jakduk.FormPasswordLengthMax;
	$scope.usernameLengthMin = Jakduk.FormUsernameLengthMin;
	$scope.usernameLengthMax = Jakduk.FormUsernameLengthMax;
	
	$scope.emailConn = "none";
	$scope.usernameConn = "none";
	$scope.emailAlert = {};
	$scope.usernameAlert = {};
	$scope.passwordAlert = {};
	$scope.passwordConfirmAlert = {};
	$scope.buttonAlert = {};
	$scope.equalPasswordStatus = "false";
	
	$scope.onSubmit = function(event) {
		if ($scope.userWrite.$valid && $scope.emailStatus == 'ok' && $scope.usernameStatus == 'ok' 
				&& $scope.equalPasswordStatus == "true") {
			submitted = true;
			$scope.btnSubmit = true;
		} else {
			if ($scope.userWrite.email.$invalid) {
				$scope.validationEmail();				
			} else if ($scope.emailStatus != 'ok') {
				//$scope.emailAlert = {"classType":"text-danger", "msg":'<spring:message code="common.msg.error.shoud.check.redudancy"/>'};
				//document.userWrite.email.focus();
			}
			
			if ($scope.userWrite.username.$invalid) {
				$scope.validationUsername();
			} else if ($scope.usernameStatus != 'ok') {
				//$scope.usernameAlert = {"classType":"text-danger", "msg":'<spring:message code="common.msg.error.shoud.check.redudancy"/>'};
				//document.userWrite.username.focus();
			}
			
			$scope.eaualPasswordConfirm();
			$scope.submitConn = "none";
			$scope.buttonAlert = {"classType":"text-danger", "msg":'<spring:message code="common.msg.need.form.validation"/>'};
			event.preventDefault();
		}
	};
	
	$scope.onEmail = function() {
		if ($scope.userWrite.email.$valid) {
			var bUrl = '<c:url value="/check/user/email.json?email=' + $scope.email + '"/>';
			if ($scope.emailConn == "none") {
				var reqPromise = $http.get(bUrl);
				$scope.emailConn = "connecting";
				reqPromise.success(function(data, status, headers, config) {
					if (data.existEmail != null) {
						if (data.existEmail == false) {
							$scope.emailStatus = "ok";
							$scope.emailAlert = {"classType":"text-success", "msg":'<spring:message code="user.msg.avaliable.data"/>'};
						} else {
							$scope.emailStatus = "duplication";
							$scope.emailAlert = {"classType":"text-danger", "msg":'<spring:message code="user.msg.already.email"/>'};
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
				$scope.usernameConn = "connecting";
				reqPromise.success(function(data, status, headers, config) {
					if (data.existUsername != null) {
						if (data.existUsername == false) {
							$scope.usernameStatus = "ok";
							$scope.usernameAlert = {"classType":"text-success", "msg":'<spring:message code="user.msg.avaliable.data"/>'};
						} else {
							$scope.usernameStatus = 'duplication';
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

	$scope.validationEmail = function() {
		if ($scope.userWrite.email.$invalid) {
			if ($scope.userWrite.email.$error.required) {
				$scope.emailAlert = {"classType":"text-danger", "msg":'<spring:message code="common.msg.required"/>'};
			} else if ($scope.userWrite.email.$error.minlength || $scope.userWrite.email.$error.maxlength) {
				$scope.emailAlert = {"classType":"text-danger", "msg":'<spring:message code="Size.userWrite.email"/>'};
			} else if ($scope.userWrite.email.$error.pattern) {
				$scope.emailAlert = {"classType":"text-danger", "msg":'<spring:message code="user.msg.check.mail.format"/>'};
			}
		} else {
			$scope.emailAlert = {"classType":"text-info", "msg":'<spring:message code="common.msg.error.shoud.check.redudancy"/>'};
		}
	}
		
	$scope.validationUsername = function () {
		if ($scope.userWrite.username.$invalid) {
			if ($scope.userWrite.username.$error.required) {
				$scope.usernameAlert = {"classType":"text-danger", "msg":'<spring:message code="common.msg.required"/>'};
			} else if ($scope.userWrite.username.$error.minlength || $scope.userWrite.username.$error.maxlength) {
				$scope.usernameAlert = {"classType":"text-danger", "msg":'<spring:message code="Size.userWrite.username"/>'};
			}
		} else {
			$scope.usernameAlert = {"classType":"text-info", "msg":'<spring:message code="common.msg.error.shoud.check.redudancy"/>'};
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
			$scope.passwordConfirmAlert = {"classType":"text-info", "msg":'<spring:message code="common.msg.error.shoud.check.redudancy"/>'};
		}
	};	
});
</script>

<jsp:include page="../include/body-footer.jsp"/>

</body>
</html>