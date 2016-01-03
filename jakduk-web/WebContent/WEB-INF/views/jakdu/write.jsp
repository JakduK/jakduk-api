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

        <form:form commandName="jakduWriteList" name="jakduWriteList" action="${contextPath}/jakdu/write" method="POST" cssClass="form-horizontal"
                   ng-submit="onSubmit($event)">
            <c:forEach items="${jakduWriteList.jakdus}" var="jakdu" varStatus="status">

                <div class="row">
                    <div class="col-sm-4">
                            <label class="col-sm-3 control-label"><spring:message code="common.date"/></label>
                            <div class="col-sm-9">
                                <p class="form-control-static"><fmt:formatDate value="${jakdu.schedule.date}" pattern="${dateTimeFormat.dateTime}" /></p>
                            </div>
                    </div>
                    <div class="col-sm-4">
                            <label class="col-sm-3 control-label"><spring:message code="common.competition"/></label>
                            <div class="col-sm-9">
                                <p class="form-control-static">${competitionNames[jakdu.schedule.competition.id].fullName}</p>
                            </div>
                    </div>
                    <div class="col-sm-4">
                            <label class="col-sm-3 control-label"><spring:message code="jakdu.match"/></label>
                            <div class="col-sm-9">
                                <p class="form-control-static">${fcNames[jakdu.schedule.home.id].shortName} VS ${fcNames[jakdu.schedule.away.id].shortName}</p>
                            </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-sm-12">
                        <label class="col-sm-1 control-label"><spring:message code="jakdu.expect.score"/></label>
                        <div class="col-sm-3">
                            <select class="form-control" name="jakduWriteList.jakdus[${status.index}].homeScore" value="${jakdu.homeScore}">
                                <option value=""><spring:message code="board.placeholder.expect.home.score"/></option>
                                <option ng-repeat="opt in rangeScore" value="{{opt}}">{{opt}}</option>
                            </select>
                        </div>
                        <div class="col-sm-3">
                            <select class="form-control" name="jakduWriteList.jakdus[${status.index}].awayScore" value="${jakdu.awayScore}">
                                <option value=""><spring:message code="board.placeholder.expect.away.score"/></option>
                                <option ng-repeat="opt in rangeScore" value="{{opt}}">{{opt}}</option>
                            </select>
                        </div>

                    </div>
                </div>
                <hr/>
            </c:forEach>

            <button type="submit">Submit</button>

        </form:form>


        {{jakduWriteList}}

    </div> <!--=== End Content Part ===-->

    <jsp:include page="../include/footer.jsp"/>
</div>

<script src="<%=request.getContextPath()%>/resources/jquery/dist/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/resources/angular-bootstrap/ui-bootstrap-tpls.min.js"></script>
<script type="text/javascript">
    var jakdukApp = angular.module("jakdukApp", ["ui.bootstrap"]);

    jakdukApp.controller('jakduCtrl', function($scope) {
        $scope.rangeScore = [];

        for (i = 0 ; i < 19 ; i++) {
            $scope.rangeScore.push(i);
        }

        angular.element(document).ready(function() {

            App.init();
        });

        $scope.onSubmit = function(event) {

            console.log($scope.jakduWriteList.$valid);
        };
    });
</script>

<jsp:include page="../include/body-footer.jsp"/>
</body>
</html>
