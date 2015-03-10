<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>    
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>   
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>  

<!DOCTYPE html>
<html ng-app="jakdukApp">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<h4>Write FootballClub.</h4>
<c:set var="contextPath" value="<%=request.getContextPath()%>"/>
<form:form commandName="footballClubWrite" action="${contextPath}/admin/footballclub/write" method="POST">
<form:hidden path="id"/>
<p>
ORIGIN : 
				<form:select path="origin" cssClass="form-control">
				<c:forEach items="${footballClubs}" var="club">
					<form:option value="${club.id}" label="${club.name}"/>
				</c:forEach>
				</form:select>
</p>
<p>
ACTIVE :
<form:select path="active" cssClass="form-control">
	<form:option value="none">None</form:option>
	<form:option value="active">Active</form:option>
	<form:option value="inactive">Inactive</form:option>
</form:select> 	
</p>
<p>
<p>
KOR SHORTNAME : 	<form:input path="shortNameKr" cssClass="form-control" placeholder="KOR shortName" size="30"/>
</p>
<p>
KOR FULLNAME : 	<form:input path="fullNameKr" cssClass="form-control" placeholder="KOR fullName" size="30"/>
</p>
<p>
ENG SHORTNAME : 	<form:input path="shortNameEn" cssClass="form-control" placeholder="ENG shortName" size="30"/>
</p>
<p>
ENG FULLNAME : 	<form:input path="fullNameEn" cssClass="form-control" placeholder="ENG fullName" size="30"/>
</p>
<p>
<input type="submit" value="<spring:message code="common.button.submit"/>" class="btn btn-default"/>
</p>
</form:form>
</body>
</html>