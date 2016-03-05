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

    <link rel="stylesheet" href="<%=request.getContextPath()%>/resources/unify/assets/plugins/line-icons-pro/styles.css">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/resources/unify/assets/plugins/ladda-buttons/css/custom-lada-btn.css">

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
            <div class="col-xs-4 content-boxes-v6">
                <i class="rounded-x  icon-sport-011 "></i>
                <h1 class="title-v3-md text-uppercase margin-bottom-10">{{jakduSchedule.home.name}}</h1>
                <p>Home</p>
            </div>
            <div class="col-xs-4">
                <div class="service-block-v1">
                    <i class="rounded-x icon-sport-119"></i>
                    <h3 class="title-v3-bg text-uppercase">{{jakduSchedule.score.homeFullTime}} : {{jakduSchedule.score.awayFullTime}}</h3>
                    <p>Score</p>
                </div>
            </div>
            <div class="col-xs-4 content-boxes-v6">
                <i class="rounded-x  icon-sport-011 "></i>
                <h2 class="title-v3-md text-uppercase margin-bottom-10">{{jakduSchedule.away.name}}</h2>
                <p>Away</p>
            </div>
            <div class="col-xs-12">
                <!--Tag Box v6-->
                <div class="tag-box tag-box-v6">
                    <h2><spring:message code="jakdu.expect.score"/></h2>
                    <div class="row">
                        <div class="col-xs-6 margin-bottom-10">
                            <select class="form-control" ng-model="myJakdu.homeScore">
                                <option value=""><spring:message code="board.placeholder.expect.home.score"/></option>
                                <option ng-repeat="opt in rangeScore" value="{{opt}}">{{opt}}</option>
                            </select>
                        </div>
                        <div class="col-xs-6">
                            <select class="form-control" ng-model="myJakdu.awayScore">
                                <option value=""><spring:message code="board.placeholder.expect.away.score"/></option>
                                <option ng-repeat="opt in rangeScore" value="{{opt}}">{{opt}}</option>
                            </select>
                        </div>
                    </div>
                    <button type="button" class="btn-u btn-u-blue rounded ladda-button"
                            ng-click="btnGoJakdu()"
                            ladda="goJakdu" data-style="expand-right" data-spinner-color="Gainsboro">
                        <span aria-hidden="true" class="icon-pencil"></span> <spring:message code="common.button.go.jakdu"/>
                    </button>
                </div>
                <!--End Tag Box v6-->
            </div>
        </div>

    </div> <!--=== End Content Part ===-->

    <jsp:include page="../include/footer.jsp"/>
</div>

<script src="<%=request.getContextPath()%>/resources/jquery/dist/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/resources/angular-bootstrap/ui-bootstrap-tpls.min.js"></script>
<script src="<%=request.getContextPath()%>/resources/unify/assets/plugins/ladda-buttons/js/spin.min.js"></script>
<script src="<%=request.getContextPath()%>/resources/unify/assets/plugins/ladda-buttons/js/ladda.min.js"></script>
<script src="<%=request.getContextPath()%>/resources/angular-ladda/dist/angular-ladda.min.js"></script>
<script type="text/javascript">
    var jakdukApp = angular.module("jakdukApp", ["ui.bootstrap", "angular-ladda"]);

    jakdukApp.controller('jakduCtrl', function($scope, $http) {
        $scope.rangeScore = [];             // 선택할 수 있는 점수 목록
        $scope.dataScheduleConn = "none";   // 작두 데이터 커넥션 상태
        $scope.jakduSchedule = {};          // 작두 데이터
        $scope.myJakdu = {};                // 내 작두 데이터

        for (i = 0 ; i < 19 ; i++) {
            $scope.rangeScore.push(i);
        }

        // http config
        var headers = {
            "Content-Type" : "application/json"
        };

        var config = {
            headers:headers
        };

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

        $scope.btnGoJakdu = function() {
            var bUrl = '<c:url value="/jakdu/myJakdu"/>';
            var reqData = {};

            if (isEmpty($scope.myJakdu.homeScore) || isEmpty($scope.myJakdu.awayScore)) {
                return;
            }

            reqData.homeScore = $scope.myJakdu.homeScore;
            reqData.awayScore = $scope.myJakdu.awayScore;
            reqData.jakduScheduleId = "${id}";

            var reqPromise = $http.post(bUrl, reqData, config);
            $scope.goJakdu = true;

            reqPromise.success(function(data, status, headers, config) {
                console.log("success");
                $scope.goJakdu = false;
            });
            reqPromise.error(function(data, status, headers, config) {
                alert(data.message);
                $scope.goJakdu = false;
            });
        };

    });
</script>

<jsp:include page="../include/body-footer.jsp"/>

</body>
</html>
