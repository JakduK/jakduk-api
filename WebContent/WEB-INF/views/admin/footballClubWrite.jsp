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
<h4>Write FootballClub.</h4>
<c:set var="contextPath" value="<%=request.getContextPath()%>"/>
<form:form commandName="footballClubWrite" action="${contextPath}/admin/footballclub/write" method="POST">
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
	<form:option value="10">None</form:option>
	<form:option value="11">Active</form:option>
	<form:option value="12">Past</form:option>
</form:select> 	
</p>
<p>
<p>
KOR SHORTNAME : 	<form:input path="shortNameKr" cssClass="form-control" placeholder="KOR shortName"/>
</p>
<p>
KOR FULLNAME : 	<form:input path="fullNameKr" cssClass="form-control" placeholder="KOR fullName"/>
</p>
<p>
ENG SHORTNAME : 	<form:input path="shortNameEn" cssClass="form-control" placeholder="ENG shortName"/>
</p>
<p>
ENG FULLNAME : 	<form:input path="fullNameEn" cssClass="form-control" placeholder="ENG fullName"/>
</p>
<p>
<input type="submit" value="<spring:message code="common.button.submit"/>" class="btn btn-default"/>
</p>
</form:form>
</body>
</html>