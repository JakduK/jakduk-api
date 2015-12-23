<%--
  Created by IntelliJ IDEA.
  User: pyohwan
  Date: 15. 12. 23
  Time: 오후 11:47
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<!DOCTYPE html>
<html ng-app="jakdukApp">
<head>
    <title>Jakdu Schedule Write &middot; Admin &middot; <spring:message code="common.jakduk"/></title>

    <jsp:include page="../include/html-header.jsp"/>
</head>
<body>
<div class="wrapper">
    <jsp:include page="../include/navigation-header.jsp"/>
    <div class="container content" ng-controller="adminCtrl">
        <h4>Write JakduSchedule.</h4>
        <c:set var="contextPath" value="<%=request.getContextPath()%>"/>
        <form:form commandName="jakduScheduleWrite" action="${contextPath}/admin/jakdu/schedule/write" method="POST">
            <form:hidden path="id"/>

            <label for="home" class="control-label">HOME TEAM</label>
            <form:select path="home" cssClass="form-control">
                <c:forEach items="${footballClubs}" var="club">
                    <form:option value="${club.id}" label="${club.name}"/>
                </c:forEach>
            </form:select>

            <label for="home" class="control-label">AWAY TEAM</label>
            <form:select path="away" cssClass="form-control">
                <c:forEach items="${footballClubs}" var="club">
                    <form:option value="${club.id}" label="${club.name}"/>
                </c:forEach>
            </form:select>

            <input type="submit" value="<spring:message code="common.button.write"/>" class="btn btn-default"/>
        </form:form>
    </div>
<jsp:include page="../include/footer.jsp"/>
</div><!-- /.wrapper -->

<!-- Bootstrap core JavaScript================================================== -->
<!-- Placed at the end of the document so the pages load faster -->
<script src="<%=request.getContextPath()%>/resources/jquery/dist/jquery.min.js"></script>
<script type="text/javascript">

    var jakdukApp = angular.module("jakdukApp", []);

    jakdukApp.controller("adminCtrl", function($scope, $filter) {

        $scope.calcAve = function() {
            $scope.average = Math.round($scope.total / $scope.games);
        };

    });

</script>

<jsp:include page="../include/body-footer.jsp"/>

</body>
</html>
