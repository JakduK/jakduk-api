<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<jsp:include page="../include/html-header.jsp"></jsp:include>

<link href="<%=request.getContextPath()%>/web-resources/bootstrap/css/bootstrapApp.css" rel="stylesheet">
</head>
<body>
<a href="<c:url value='/user/list'/>">List</a>
	<div class="container">
		<form:form commandName="user" action="/jakduk/user/create" method="POST" cssClass="form-horizontal">
			<legend><spring:message code="user.register"/> </legend>
			<div class="control-group string required">
				<label class="string required control-label" for="email"><abbr title="required">*</abbr> <spring:message code="user.email"/></label>
				<div class="controls">
					<form:input path="email" cssClass="string required span6" size="50"/>
					<form:errors path="email" cssClass="smdis-error-message"/>
				</div>
			</div>
			<div class="control-group string required">
				<label class="string required control-label" for="username"><abbr title="required">*</abbr> <spring:message code="user.nickname"/></label>
				<div class="controls">
					<form:input path="username" cssClass="string required span6" size="50"/>
					<form:errors path="username" cssClass="smdis-error-message"/>
				</div>
			</div>
			<div class="control-group string required">
				<label class="string required control-label" for="password"><abbr title="required">*</abbr> <spring:message code="user.password"/></label>
				<div class="controls">
					<form:password path="password" cssClass="string required span6" size="50"/>
					<form:errors path="password" cssClass="smdis-error-message"/>
				</div>
			</div>
			<div class="control-group string required">
				<label class="string required control-label" for="password-confirm"><abbr title="required">*</abbr> <spring:message code="user.password.confirm"/></label>
				<div class="controls">
					<input type="password" name="password-confirm" class="string required span6"/>
				</div>
			</div>			
			<div class="control-group text optional">
				<label class="text optional control-label" for="comments"> <spring:message code="user.comment"/></label>
				<div class="controls">
					<textarea class="text optional span6" cols="40" id="comments" name="comments" rows="5"></textarea>
				</div>
			</div>
			<div class="form-actions">
				<input type="submit" value="<spring:message code="common.button.submit"/>" class="btn btn-primary"/>
				<a class="btn btn-danger" href="<c:url value="/"/>"><spring:message code="common.button.cancel"/></a> 
			</div>
		</form:form>
		
		<jsp:include page="../include/footer.jsp"/>
	</div>
</body>
</html>