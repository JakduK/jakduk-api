<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>    
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>     
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<h4>Write FootballClub Origin.</h4>
<c:set var="contextPath" value="<%=request.getContextPath()%>"/>
<form:form commandName="footballClubOrigin" action="${contextPath}/admin/footballclub/origin/write" method="POST">
<form:hidden path="id"/>
<p>
NAME : <form:input path="name" cssClass="form-control" placeholder="football club origin name"/>
</p>
<input type="submit" value="<spring:message code="common.button.submit"/>" class="btn btn-default"/>
</form:form>
</body>
</html>