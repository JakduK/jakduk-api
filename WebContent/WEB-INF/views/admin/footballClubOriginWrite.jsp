<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>    
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>     
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<h4>Write FootballClub Origin.</h4>
<c:set var="contextPath" value="<%=request.getContextPath()%>"/>
<form:form commandName="footballClubOriginWrite" action="${contextPath}/admin/footballclub/origin/write" method="POST">
<p>
NAME : <form:input path="name" cssClass="form-control" placeholder="football club origin name"/>
</p>
<input type="submit" value="<spring:message code="common.button.submit"/>" class="btn btn-default"/>
</form:form>
</body>
</html>