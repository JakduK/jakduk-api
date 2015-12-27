<%--
  Created by IntelliJ IDEA.
  User: pyohwan
  Date: 15. 12. 26
  Time: 오후 7:42
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<!DOCTYPE html>
<html ng-app="jakdukApp">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title><spring:message code="jakdu.schedule"/> &middot; <spring:message code="jakdu"/> &middot; <spring:message code="common.jakduk"/></title>

    <jsp:include page="../include/html-header.jsp"></jsp:include>
</head>

<body>
<div class="wrapper" ng-controller="jakduCtrl">
    <jsp:include page="../include/navigation-header.jsp"/>

    <!--=== Breadcrumbs ===-->
    <div class="breadcrumbs">
        <div class="container">
            <h1 class="pull-left"><a href="<c:url value="/jakdu/schdule/refresh"/>"><spring:message code="jakdu.schedule"/></a></h1>
        </div><!--/container-->
    </div><!--/breadcrumbs-->
    <!--=== End Breadcrumbs ===-->

    <div class="alert alert-info"><spring:message code="common.msg.test.version"/></div>

    <!--=== Content Part ===-->
    <div class="container content">

        <div class="panel panel-u">
            <div class="panel-heading hidden-xs">
                <div class="row">
                    <div class="col-sm-4"><spring:message code="common.date"/></div>
                    <div class="col-sm-4"><spring:message code="common.competition"/></div>
                    <div class="col-sm-4"><spring:message code="jakdu.match"/></div>
                </div>
            </div> <!-- /panel-heading -->

            <ul class="list-group">
                <li class="list-group-item" ng-repeat="schedule in schedules">
                    <div class="row">
                        <div class="col-sm-4">
                            {{schedule.date | date:"${dateTimeFormat.dateTime}"}}
                        </div>
                        <div class="col-sm-4">
                            {{competitionNames[schedule.competition.id].fullName}}
                        </div>
                        <div class="col-sm-4">
                            {{fcNames[schedule.home.id]}}
                            <span ng-if="!schedule.timeUp">VS</span>
                            <span ng-if="schedule.timeUp">
                                <span ng-if="schedule.score">
                                    {{schedule.score.homeFullTime}} : {{schedule.score.awayFullTime}}
                                </span>
                            </span>
                            {{fcNames[schedule.away.id]}}
                        </div>
                    </div>
                </li>
            </ul>

        </div>


    </div> <!--=== End Content Part ===-->

    <jsp:include page="../include/footer.jsp"/>
</div>

<script src="<%=request.getContextPath()%>/resources/jquery/dist/jquery.min.js"></script>
<script type="text/javascript">
    var jakdukApp = angular.module("jakdukApp", []);

    jakdukApp.controller('jakduCtrl', function($scope, $http) {
        $scope.dataSchedulesConn = "none";
        $scope.schedules = [];
        $scope.fcNames = [];
        $scope.competitionNames = [];

        angular.element(document).ready(function() {
            var page = 1;
            var size = 20;

            $scope.getDataSchedulesList(page, size);

            App.init();
        });

        $scope.getDataSchedulesList = function(page, size) {
            var bUrl = '<c:url value="/jakdu/data/schedule.json?page=' + page + '&size=' + size + '"/>';

            if ($scope.dataSchedulesConn == "none") {

                var reqPromise = $http.get(bUrl);

                $scope.dataSchedulesConn = "loading";

                reqPromise.success(function(data, status, headers, config) {
                    //console.log(data);
                    $scope.schedules = data.schedules;

                    if (data.schedules != null) {
                        $scope.schedules = data.schedules;
                    }

                    if (data.schedules != null) {
                        $scope.fcNames = JSON.parse(data.fcNames);
                    }

                    if (data.competitionNames != null) {
                        $scope.competitionNames = JSON.parse(data.competitionNames);
                    }

                    $scope.dataSchedulesConn = "none";
                });
                reqPromise.error(function(data, status, headers, config) {
                    $scope.dataSchedulesConn = "none";
                    $scope.error = '<spring:message code="common.msg.error.network.unstable"/>';
                });
            }
        };

    });
</script>

<jsp:include page="../include/body-footer.jsp"/>
</body>
</html>