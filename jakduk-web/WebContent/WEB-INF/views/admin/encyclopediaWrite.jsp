<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>    
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>    

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<c:set var="contextPath" value="<%=request.getContextPath()%>"/>
<form:form commandName="encyclopedia" action="${contextPath}/admin/encyclopedia/write" method="POST">
<form:hidden path="id"/>
<form:hidden path="seq"/>
<p>
LANGUAGE : 	<form:input path="language" cssClass="form-control" placeholder="language"/>
</p>
<p>
KIND :
<form:select path="kind" cssClass="form-control">
	<form:option value="player">Player</form:option>
	<form:option value="book">Book</form:option>
</form:select> 	
</p>
<p>
SUBJECT : 	<form:input path="subject" cssClass="form-control" placeholder="subject"/>
</p>
<p>
CONTENT : <form:textarea path="content" cols="40" rows="5"/>
</p>
<p>
<input type="submit" value="<spring:message code="common.button.write"/>" class="btn btn-default"/>
</p>
</form:form>
</body>
</html>