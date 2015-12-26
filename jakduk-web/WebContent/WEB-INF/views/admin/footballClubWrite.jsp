<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<!DOCTYPE html>
<html ng-app="jakdukApp">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Write FootballClub &middot; Admin &middot; <spring:message code="common.jakduk"/></title>

	<jsp:include page="../include/html-header.jsp"/>
</head>
<body>
	<div class="container">
		<div class="page-header">
			<h4>Write FootballClub.</h4>
		</div>

		<c:set var="contextPath" value="<%=request.getContextPath()%>"/>
		<form:form commandName="footballClubWrite" action="${contextPath}/admin/footballclub/write" method="POST">
			<form:hidden path="id"/>

			<div class="row">
				<div class="col-sm-6">
					<label for="origin" class="control-label">ORIGIN</label>
					<div class="form-group">
						<form:select path="origin" cssClass="form-control">
							<c:forEach items="${footballClubs}" var="club">
								<form:option value="${club.id}" label="${club.name}"/>
							</c:forEach>
						</form:select>
						<form:errors path="origin"/>
					</div>
				</div>
				<div class="col-sm-6">
					<label for="active" class="control-label">ACTIVE</label>
					<div class="form-group">
						<form:select path="active" cssClass="form-control">
							<form:option value="none">None</form:option>
							<form:option value="active">Active</form:option>
							<form:option value="inactive">Inactive</form:option>
						</form:select>
						<form:errors path="active"/>
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
			<button type="button" class="btn btn-default" onclick="location.href='<c:url value="/admin/settings?open=fc"/>'">
				<span class="glyphicon glyphicon-ban-circle"></span> <spring:message code="common.button.cancel"/>
			</button>
		</form:form>
	</div>
</body>
</html>