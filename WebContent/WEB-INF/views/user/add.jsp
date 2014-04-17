<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<a href="<c:url value='/user/list'/>">List</a>
<form:form commandName="user" action="/jakduk/user" method="POST">
	<p>
		<h3>Name</h3>
		<form:input path="userName" size="50"/>
		<form:errors path="userName" cssClass="smdis-error-message"/>
	</p>
	<input type="submit" value="save"/>
</form:form>

</body>
</html>