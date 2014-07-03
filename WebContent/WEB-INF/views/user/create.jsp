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
	console.log(email, emailPattern.test(email));
	
	//return false;
}
</script>
</head>
<body>
<jsp:include page="../include/navigation-header.jsp"/>
<c:set var="contextPath" value="<%=request.getContextPath()%>"/>

<div class="container">
	<form:form commandName="user" action="${contextPath}/user/create" method="POST" cssClass="form-horizontal" onsubmit="return formSubmit();">
		<legend><spring:message code="user.register"/> </legend>
		<div class="form-group">
			<label class="col-sm-2 control-label" for="email">
				<abbr title="required">*</abbr> <spring:message code="user.email"/>
			</label>
			<div class="col-sm-4">
				<div class="input-group">
					<form:input path="email" cssClass="form-control" size="50" placeholder="Email"/>
					<span class="input-group-btn">
						<button class="btn btn-default" type="button"><spring:message code="common.button.check"/></button>
					</span>
				</div>
				<spring:hasBindErrors name="user">
				${errors}
				<form:errors path="email"/>
				</spring:hasBindErrors>
				
				
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
			<label class="col-sm-2 control-label" for="password-confirm">
				<abbr title="required">*</abbr> <spring:message code="user.password.confirm"/>
			</label>
			<div class="col-sm-3">
				<input type="password" name="password-confirm" class="string required span6"/>
			</div>
		</div>			
		<div class="form-group">
			<label class="col-sm-2 control-label" for="comments"> <spring:message code="user.comment"/></label>
			<div class="col-sm-4">
				<textarea class="form-control" cols="40" id="comments" name="comments" rows="5"></textarea>
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