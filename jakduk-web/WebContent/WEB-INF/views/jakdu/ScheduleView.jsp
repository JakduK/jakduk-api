<%--
  Created by IntelliJ IDEA.
  User: pyohwan
  Date: 16. 2. 15
  Time: 오후 11:52
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<!DOCTYPE html>
<!--[if IE 9]> <html lang="ko" class="ie9" ng-app="jakdukApp"> <![endif]-->
<!--[if !IE]><!--> <html lang="ko" ng-app="jakdukApp"> <!--<![endif]-->
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title><spring:message code="jakdu.view"/> &middot; <spring:message code="jakdu"/> &middot; <spring:message code="common.jakduk"/></title>

    <jsp:include page="../include/html-header.jsp"></jsp:include>
</head>

<body class="header-fixed">

<div class="wrapper" ng-controller="jakduCtrl">
    <jsp:include page="../include/navigation-header.jsp"/>

    <!--=== Breadcrumbs ===-->
    <div class="breadcrumbs">
        <div class="container">
            <h1 class="pull-left"><a href="<c:url value="/jakdu/schedule/refresh"/>"><spring:message code="jakdu.view"/></a></h1>
        </div><!--/container-->
    </div><!--/breadcrumbs-->
    <!--=== End Breadcrumbs ===-->

    <!--=== Content Part ===-->
    <div class="container content">

        <div class="row">
            <div class="col-xs-6 content-boxes-v6 md">
                <i class="rounded-x icon-link"></i>
                <h1 class="title-v3-md text-uppercase margin-bottom-10">{{jakduSchedule.home.name}}</h1>
                <p>At vero eos et accusato odio dignissimos ducimus qui blanditiis praesentium voluptatum.</p>
            </div>
            <div class="col-xs-6 content-boxes-v6 md-margin-bottom-50">
                <i class="rounded-x icon-paper-plane"></i>
                <h2 class="title-v3-md text-uppercase margin-bottom-10">{{jakduSchedule.away.name}}</h2>
                <p>At vero eos et accusato odio dignissimos ducimus qui blanditiis praesentium voluptatum.</p>
            </div>
        </div>

    </div> <!--=== End Content Part ===-->

    <jsp:include page="../include/footer.jsp"/>
</div>

<script src="<%=request.getContextPath()%>/resources/jquery/dist/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/resources/angular-bootstrap/ui-bootstrap-tpls.min.js"></script>
<script type="text/javascript">
    var jakdukApp = angular.module("jakdukApp", ["ui.bootstrap"]);

    jakdukApp.controller('jakduCtrl', function($scope, $http) {
        $scope.dataScheduleConn = "none";
        $scope.jakduSchedule = {};

        angular.element(document).ready(function() {
            $scope.getDataSchedule();

            App.init();
        });

        $scope.getDataSchedule = function() {
            var bUrl = '<c:url value="/jakdu/data/schedule/${id}" />';

            if ($scope.dataScheduleConn == "none") {

                var reqPromise = $http.get(bUrl);

                $scope.dataScheduleConn = "loading";

                reqPromise.success(function(data, status, headers, config) {

                    if (data.jakduSchedule != null) {
                        $scope.jakduSchedule = data.jakduSchedule;
                    }

                    $scope.dataScheduleConn = "none";
                });
                reqPromise.error(function(data, status, headers, config) {
                    $scope.dataScheduleConn = "none";
                    $scope.error = '<spring:message code="common.msg.error.network.unstable"/>';
                });
            }
        };

    });
</script>

<jsp:include page="../include/body-footer.jsp"/>

</body>
</html>
