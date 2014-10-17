<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
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
	<form:form commandName="userWrite" name="userWrite" action="${contextPath}/oauth/write" method="POST" cssClass="form-horizontal">
		<form:input path="existUsername" cssClass="hidden" size="0" ng-init="existUsername='${userWrite.existUsername}'" ng-model="existUsername"/>
		<legend><spring:message code="oauth.register"/> </legend>
		
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

		<div class="form-group">
			<label class="col-sm-2 control-label" for="supportFC">
				<spring:message code="user.support.football.club"/>
			</label>
			<div class="col-sm-3">
				<form:select path="footballClub" cssClass="form-control">
					<form:option value="-1"><spring:message code="common.none"/></form:option>
				<c:forEach items="${footballClubs}" var="club">
					<c:forEach items="${club.names}" var="name">
						<form:option value="${club.id}" label="${name.shortName}" class="visible-xs"/>
						<form:option value="${club.id}" label="${name.fullName}" class="visible-sm visible-md visible-lg"/>
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
	</div>
</div><!-- /.container -->

<!-- Bootstrap core JavaScript
 ================================================== -->
<!-- Placed at the end of the document so the pages load faster -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/resources/bootstrap/js/bootstrap.min.js"></script>

<script type="text/javascript">
var jakdukApp = angular.module("jakdukApp", []);

jakdukApp.controller("writeCtrl", function($scope, $http) {
	$scope.usernameConn = 0;
	$scope.usernameAlert = {};
	
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
	
});
</script>

<jsp:include page="../include/body-footer.jsp"/>

</body>
</html>