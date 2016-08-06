<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<!DOCTYPE html>
<html>
	<head>
		<title>Insert title here</title>
		<jsp:include page="../include/html-header.jsp"/>
		<link rel="stylesheet" href="<%=request.getContextPath()%>/bundles/admin.css">
	</head>
	<body>
		<h4>Thumbnail Size Write.</h4>
		<c:set var="contextPath" value="<%=request.getContextPath()%>"/>
		<form:form commandName="thumbnailSizeWrite" action="${contextPath}/admin/thumbnail/size/write" method="POST">
			<p>
				IN RESOURCE. WIDTH : ${resWidth}, HEIGHT : ${resHeight}
			</p>
			<p>
				WIDTH : <form:input path="width" cssClass="form-control" placeholder="width" size="30"/>
			</p>
			<p>
				HEIGHT : <form:input path="height" cssClass="form-control" placeholder="height" size="30"/>
			</p>

			<p>
				<label for="galleryId" class="control-label">GALLERY ID</label>
				<form:input path="galleryId" cssClass="form-control" placeholder="Gallery ID"/>
			</p>

			<input type="submit" value="<spring:message code="common.button.write"/>" class="btn btn-default"/>
		</form:form>
	</body>
</html>