<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>    
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>     
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<c:set var="contextPath" value="<%=request.getContextPath()%>"/>
<form:form commandName="footballClub" action="${contextPath}/admin/footballclub/write" method="POST">
<p>
LANGUAGE :
<form:select path="language" cssClass="form-control">
	<form:option value="10">En</form:option>
	<form:option value="11">Kr</form:option>
</form:select> 	
</p>
<p>
FULLNAME : 	<form:input path="fullName" cssClass="form-control" placeholder="fullName"/>
</p>
<p>
SHORTNAME : 	<form:input path="shortName" cssClass="form-control" placeholder="shortName"/>
</p>
<p>
<input type="submit" value="<spring:message code="common.button.submit"/>" class="btn btn-default"/>
</p>
</form:form>
</body>
</html>