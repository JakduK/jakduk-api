<%--
  Created by IntelliJ IDEA.
  User: pyohwan
  Date: 16. 1. 2
  Time: 오후 3:20
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html>
<!--[if IE 9]> <html lang="ko" class="ie9" ng-app="jakdukApp"> <![endif]-->
<!--[if !IE]><!--> <html lang="ko" ng-app="jakdukApp"> <!--<![endif]-->
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title><spring:message code="jakdu.write"/> &middot; <spring:message code="jakdu"/> &middot; <spring:message code="common.jakduk"/></title>

    <jsp:include page="../include/html-header.jsp"></jsp:include>

</head>

<body>
<div class="wrapper" ng-controller="jakduCtrl">
    <jsp:include page="../include/navigation-header.jsp"/>

    <c:set var="contextPath" value="<%=request.getContextPath()%>"/>

    <!--=== Breadcrumbs ===-->
    <div class="breadcrumbs">
        <div class="container">
            <h1 class="pull-left"><spring:message code="jakdu.write"/></h1>
        </div><!--/container-->
    </div><!--/breadcrumbs-->
    <!--=== End Breadcrumbs ===-->

    <!--=== Content Part ===-->
    <div class="container content">

        <form:form modelAttribute="jakdus" name="jakdus" action="${contextPath}/jakdu/write" method="POST" cssClass="form-horizontal">
            <c:forEach items="${jakdus}" var="jakdu" varStatus="status">

                <div class="row">
                    <div class="col-sm-4">
                            <label class="col-sm-2 control-label"><spring:message code="common.date"/></label>
                            <div class="col-sm-10">
                                <p class="form-control-static"><fmt:formatDate value="${jakdu.schedule.date}" pattern="${dateTimeFormat.dateTime}" /></p>
                            </div>
                    </div>
                    <div class="col-sm-4">
                            <label class="col-sm-2 control-label"><spring:message code="common.competition"/></label>
                            <div class="col-sm-10">
                                <p class="form-control-static">${competitionNames[jakdu.schedule.competition.id].fullName}</p>
                            </div>
                    </div>
                    <div class="col-sm-4">
                            <label class="col-sm-2 control-label"><spring:message code="jakdu.match"/></label>
                            <div class="col-sm-10">
                                <p class="form-control-static">${fcNames[jakdu.schedule.home.id].fullName} VS ${fcNames[jakdu.schedule.away.id].fullName}</p>
                            </div>
                    </div>
                </div>

                <div class="form-group">
                    <label class="sr-only" for="exampleInputEmail3">Email address</label>
                    <input type="email" class="form-control" id="exampleInputEmail3" placeholder="Email">
                </div>
                <div class="form-group">
                    <label class="sr-only" for="exampleInputPassword3">Password</label>
                    <input type="password" class="form-control" id="exampleInputPassword3" placeholder="Password">
                </div>

                <div class="form-group">
                    <input class="form-control" name="jakdus[${status.index}].homeScore" value="${jakdu.homeScore}"/>
                </div>
            </c:forEach>

            <button type="submit">Submit</button>

        </form:form>



    </div> <!--=== End Content Part ===-->

    <jsp:include page="../include/footer.jsp"/>
</div>

<script src="<%=request.getContextPath()%>/resources/jquery/dist/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/resources/angular-bootstrap/ui-bootstrap-tpls.min.js"></script>
<script type="text/javascript">
    var jakdukApp = angular.module("jakdukApp", ["ui.bootstrap"]);

    jakdukApp.controller('jakduCtrl', function($scope, $http) {

        angular.element(document).ready(function() {
            App.init();
        });

    });
</script>

<jsp:include page="../include/body-footer.jsp"/>
</body>
</html>
