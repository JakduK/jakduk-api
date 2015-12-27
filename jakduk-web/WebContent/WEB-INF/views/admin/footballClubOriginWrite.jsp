<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Write FootballClub Origin &middot; Admin &middot; <spring:message code="common.jakduk"/></title>

    <jsp:include page="../include/html-header.jsp"/>
</head>
<body>
    <div class="container">
        <div class="page-header">
            <h4>Write FootballClub Origin.</h4>
        </div>

        <c:set var="contextPath" value="<%=request.getContextPath()%>"/>
        <form:form commandName="footballClubOrigin" action="${contextPath}/admin/footballclub/origin/write" method="POST">
            <form:hidden path="id"/>

            <div class="row">
                <div class="col-sm-12">
                    <label for="name" class="control-label">CODE NAME</label>
                    <div class="form-group">
                        <form:input path="name" cssClass="form-control" placeholder="Code Name"/>
                        <form:errors path="name"/>
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="col-sm-4">
                    <label for="name" class="control-label">CLUB TYPE</label>
                    <div class="form-group">
                        <form:select path="clubType" cssClass="form-control">
                            <form:option value="FOOTBALL_CLUB">FOOTBALL_CLUB</form:option>
                            <form:option value="NATIONAL_TEAM">NATIONAL_TEAM</form:option>
                        </form:select>
                        <form:errors path="clubType"/>
                    </div>
                </div>
                <div class="col-sm-4">
                    <label for="age" class="control-label">AGE</label>
                    <div class="form-group">
                        <form:select path="age" cssClass="form-control">
                            <form:option value="UNDER_14">UNDER_14</form:option>
                            <form:option value="UNDER_17">UNDER_17</form:option>
                            <form:option value="UNDER_20">UNDER_20</form:option>
                            <form:option value="UNDER_23">UNDER_23</form:option>
                            <form:option value="SENIOR">SENIOR</form:option>
                        </form:select>
                        <form:errors path="age"/>
                    </div>
                </div>
                <div class="col-sm-4">
                    <label for="sex" class="control-label">SEX</label>
                    <div class="form-group">
                        <form:select path="sex" cssClass="form-control">
                            <form:option value="MEN">MEN</form:option>
                            <form:option value="WOMEN">WOMEN</form:option>
                        </form:select>
                        <form:errors path="sex"/>
                    </div>
                </div>
            </div>

            <input type="submit" value="<spring:message code="common.button.write"/>" class="btn btn-default"/>
            <button type="button" class="btn btn-default" onclick="location.href='<c:url value="/admin/settings?open=fcOrigin"/>'">
                <span class="glyphicon glyphicon-ban-circle"></span> <spring:message code="common.button.cancel"/>
            </button>
        </form:form>
    </div>
</body>
</html>