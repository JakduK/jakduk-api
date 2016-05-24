<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html>
	<head>
		<title>Insert title here</title>
		<jsp:include page="../include/html-header.jsp"/>
		<link rel="stylesheet" href="<%=request.getContextPath()%>/bundles/admin.css">
	</head>
	<body>
		<h4>Write BoardCategory.</h4>
		<c:set var="contextPath" value="<%=request.getContextPath()%>"/>
		<form:form commandName="boardCategoryWrite" action="${contextPath}/admin/board/category/write" method="POST">
			<form:hidden path="id"/>
			<p>ex) free</p>
			<p>
				NAME : <form:input path="name" cssClass="form-control" placeholder="name"/>
			</p>
			<form:errors path="name"/>
			<p>ex) board.category.free</p>
			<p>
				RESOURCE NAME : <form:input path="resName" cssClass="form-control" placeholder="resName"/>
			</p>
			<form:errors path="resName"/>
			<p>
				USING BOARD :
				<form:checkbox path="usingBoard" value="freeBoard" label="freeBoard" cssClass="form-control"/>
			</p>
			<form:errors path="usingBoard"/>
			<p>
				<input type="submit" value="<spring:message code="common.button.write"/>" class="btn btn-default"/>
			</p>
		</form:form>
	</body>
</html>
