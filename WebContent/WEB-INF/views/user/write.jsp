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
<jsp:include page="../include/navigation-header.jsp"/>
<c:set var="contextPath" value="<%=request.getContextPath()%>"/>

<div class="container" ng-controller="writeCtrl">
	<form:form commandName="userWrite" name="userWrite" action="${contextPath}/user/write" method="POST" cssClass="form-horizontal"	>
		<legend><spring:message code="user.register"/> </legend>
		<div class="form-group">
			<label class="col-sm-2 control-label" for="email">
				<abbr title='<spring:message code="user.msg.required"/>'>*</abbr> <spring:message code="user.email"/>
			</label>
			<div class="col-sm-3">
				<input type="email" name="email" id="email" class="form-control" size="50" placeholder="Email" 
				ng-model="email" ng-required="true" ng-minlength="6" ng-maxlength="20" ng-pattern="/^[\w]{3,}@[\w]+(\.[\w-]+){1,3}$/"/>
				<form:errors path="email" cssClass="alert alert-warning" element="div"/>
				<div id="alert-mail" class="alert alert-warning hidden" role="alert"></div>
				<span class="text-danger" ng-show="userWrite.email.$error.required">
					<spring:message code="user.msg.required"/>
				</span>
				<span class="text-danger" ng-show="userWrite.email.$error.minlength || userWrite.email.$error.maxlength">
					<spring:message code="Size.user.email"/>
				</span>
				<span class="text-danger" ng-show="userWrite.email.$error.pattern">
					<spring:message code="user.msg.check.mail.format"/>
				</span>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label" for="username">
				<abbr title='<spring:message code="user.msg.required"/>'>*</abbr> <spring:message code="user.nickname"/>
			</label>
			<div class="col-sm-3">
				<form:input path="username" cssClass="form-control" size="50" placeholder="Nickname"
				ng-model="username" ng-required="true" ng-minlength="2" ng-maxlength="20"/>
				<form:errors path="username" cssClass="alert alert-warning" element="div"/>
				<span class="text-danger" ng-show="userWrite.username.$error.required">
					<spring:message code="user.msg.required"/>
				</span>
				<span class="text-danger" ng-show="userWrite.username.$error.minlength || userWrite.username.$error.maxlength">
					<spring:message code="Size.user.username"/>
				</span>
			</div>
		</div>
		<div class="form-group">
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
					<spring:message code="Size.user.password"/>
				</span>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label" for="passwordConfirm">
				<abbr title='<spring:message code="user.msg.required"/>'>*</abbr> <spring:message code="user.password.confirm"/>
			</label>
			<div class="col-sm-3">
				<form:password path="passwordConfirm" cssClass="form-control" size="50" placeholder="Confirm password"
				ng-model="passwordConfirm" ng-required="true" ng-minlength="4" ng-maxlength="20"/>
				<form:errors path="passwordConfirm" cssClass="alert alert-warning" element="div"/>
				<span class="text-danger" ng-show="userWrite.passwordConfirm.$error.required">
					<spring:message code="user.msg.required"/>
				</span>
				<span class="text-danger" ng-show="userWrite.passwordConfirm.$error.minlength || userWrite.passwordConfirm.$error.maxlength">
					<spring:message code="Size.user.password"/>
				</span>
				<span class="text-danger" ng-show="password != passwordConfirm">
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
				<input type="submit" value="<spring:message code="common.button.submit"/>" class="btn btn-default" ng-disabled="userWrite.$invalid"/>
				<a class="btn btn-danger" href="<c:url value="/"/>"><spring:message code="common.button.cancel"/></a>
			</div> 
		</div>
	</form:form>
	
	<jsp:include page="../include/footer.jsp"/>
</div>
	
	<!-- Bootstrap core JavaScript
  ================================================== -->
<!-- Placed at the end of the document so the pages load faster -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/web-resources/bootstrap/js/bootstrap.min.js"></script>    

<script type="text/javascript">
function writeCtrl($scope) {
	$scope.login = {
			submit: function() {
				alert("11");
			}
	}
}
</script>

</body>
</html>