<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html ng-app>
<head>
<jsp:include page="../include/html-header.jsp"></jsp:include>

<!--  
<link href="<%=request.getContextPath()%>/web-resources/bootstrap/css/bootstrapApp.css" rel="stylesheet">
-->
<script type="text/javascript">
function formSubmit() {
	
	var email = document.getElementById("email").value;
	var emailPattern = /^[\w]{4,}@[\w]+(\.[\w-]+){1,3}$/;
	var emailCheck = emailPattern.test(email);
	
	if (emailCheck == true) {
		$("#alert-mail").text("").addClass("hidden");
		return true;
	} else {
		$("#alert-mail").text("<spring:message code='user.msg.check.mail.format'/>").removeClass("hidden");
		return false;	
	}	
}
</script>
</head>
<body>
<jsp:include page="../include/navigation-header.jsp"/>
<c:set var="contextPath" value="<%=request.getContextPath()%>"/>

<div class="container">
	<form:form commandName="userWrite" name="userWrite" action="${contextPath}/user/write" method="POST" cssClass="form-horizontal" onsubmit="return formSubmit();">
		<legend><spring:message code="user.register"/> </legend>
		<div class="form-group">
			<label class="col-sm-2 control-label" for="email">
				<abbr title='<spring:message code="user.msg.required"/>'>*</abbr> <spring:message code="user.email"/>
			</label>
			<div class="col-sm-3">
				<form:input path="email" cssClass="form-control" size="50" placeholder="Email" ng-model="email" ng-required="true" ng-minlength="6" ng-maxlength="20"/>
				<form:errors path="email" cssClass="alert alert-warning" element="div"/>
				<div id="alert-mail" class="alert alert-warning hidden" role="alert"></div>
				<span class="text-danger" ng-show="userWrite.email.$error.required"><spring:message code="user.msg.required"/></span>
				<span class="text-danger" ng-show="userWrite.email.$error.minlength || userWrite.email.$error.maxlength"><spring:message code="Size.user.email"/></span>
				{{userWrite.email.$error}}
			</div>
		</div>
		
		<div class="form-group">
			<label class="col-sm-2 control-label" for="username">
				<abbr title="required">*</abbr> <spring:message code="user.nickname"/>
			</label>
			<div class="col-sm-3">
				<form:input path="username" cssClass="form-control" size="50" placeholder="Nickname"/>
				<form:errors path="username" cssClass="alert alert-warning" element="div"/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label" for="password">
				<abbr title="required">*</abbr> <spring:message code="user.password"/>
			</label>
			<div class="col-sm-3">
				<form:password path="password" cssClass="form-control" size="50" placeholder="Password"/>
				<form:errors path="password" cssClass="alert alert-warning" element="div"/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label" for="passwordConfirm">
				<abbr title="required">*</abbr> <spring:message code="user.password.confirm"/>
			</label>
			<div class="col-sm-3">
				<form:password path="passwordConfirm" cssClass="form-control" size="50" placeholder="Confirm password"/>
				<form:errors path="passwordConfirm" cssClass="alert alert-warning" element="div"/>
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
	
	<!-- Bootstrap core JavaScript
  ================================================== -->
<!-- Placed at the end of the document so the pages load faster -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/web-resources/bootstrap/js/bootstrap.min.js"></script>    
<script src="<%=request.getContextPath()%>/web-resources/bootstrap/js/offcanvas.js"></script>

</body>
</html>