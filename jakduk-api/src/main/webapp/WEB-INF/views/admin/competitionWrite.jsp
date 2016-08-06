<%--
  Created by IntelliJ IDEA.
  User: pyohwan
  Date: 15. 12. 26
  Time: 오후 10:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!DOCTYPE html>
<html ng-app="jakdukApp">
	<head>
		<title>Write Competition &middot; Admin &middot; <spring:message code="common.jakduk"/></title>
		<jsp:include page="../include/html-header.jsp"/>
		<link rel="stylesheet" href="<%=request.getContextPath()%>/bundles/admin.css">
	</head>
	<body>
		<div class="container">
			<div class="page-header">
				<h4>Write Competition.</h4>
			</div>

			<c:set var="contextPath" value="<%=request.getContextPath()%>"/>
			<form:form commandName="competitionWrite" action="${contextPath}/admin/competition/write" method="POST">
				<form:hidden path="id"/>

				<div class="row">
					<div class="col-sm-6">
						<label for="code" class="control-label">CODE</label>
						<div class="form-group">
							<form:input path="code" cssClass="form-control" placeholder="Code" size="30"/>
							<form:errors path="code"/>
						</div>
					</div>
				</div>

				<div class="row">
					<div class="col-sm-6">
						<label for="shortNameKr" class="control-label">KOR SHORT NAME</label>
						<div class="form-group">
							<form:input path="shortNameKr" cssClass="form-control" placeholder="KOR shortName" size="30"/>
							<form:errors path="shortNameKr"/>
						</div>
					</div>
					<div class="col-sm-6">
						<label for="fullNameKr" class="control-label">KOR FULL NAME</label>
						<div class="form-group">
							<form:input path="fullNameKr" cssClass="form-control" placeholder="KOR fullName" size="30"/>
							<form:errors path="fullNameKr"/>
						</div>
					</div>
				</div>

				<div class="row">
					<div class="col-sm-6">
						<label for="shortNameEn" class="control-label">ENG SHORT NAME</label>
						<div class="form-group">
							<form:input path="shortNameEn" cssClass="form-control" placeholder="ENG shortName" size="30"/>
							<form:errors path="shortNameEn"/>
						</div>
					</div>
					<div class="col-sm-6">
						<label for="fullNameEn" class="control-label">ENG FULL NAME</label>
						<div class="form-group">
							<form:input path="fullNameEn" cssClass="form-control" placeholder="ENG fullName" size="30"/>
							<form:errors path="fullNameEn"/>
						</div>
					</div>
				</div>

				<input type="submit" value="<spring:message code="common.button.write"/>" class="btn btn-default"/>
				<button type="button" class="btn btn-default" onclick="location.href='
					<c:url value="/admin/settings?open=fc"/>'">
					<span class="glyphicon glyphicon-ban-circle"></span> <spring:message code="common.button.cancel"/>
				</button>
			</form:form>
		</div>
		<script src="<%=request.getContextPath()%>/bundles/admin.js"></script>
		<script type="text/javascript">
			angular.module('jakdukApp', ['jakdukCommon']);
		</script>
	</body>
</html>