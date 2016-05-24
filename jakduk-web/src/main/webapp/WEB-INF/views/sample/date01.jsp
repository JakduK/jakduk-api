<%--
  Created by IntelliJ IDEA.
  User: pyohwan
  Date: 15. 12. 24
  Time: 오후 4:57
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!DOCTYPE html>
<html ng-app="jakdukApp">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Date Test 01 &middot; <spring:message code="common.jakduk"/></title>
    <jsp:include page="../include/html-header.jsp"/>
</head>
<body>
<div class="wrapper">
    <jsp:include page="../include/navigation-header.jsp"/>

    <div class="container content" ng-controller="sampleCtrl">

        <div class="row">
            <div class="col-md-12">
                <p class="input-group">
                    <input type="text" class="form-control" uib-datepicker-popup="{{format}}" ng-model="mytime" is-open="status.opened" min-date="minDate" max-date="maxDate" datepicker-options="dateOptions" date-disabled="disabled(date, mode)" ng-required="true" close-text="Close" />
              <span class="input-group-btn">
                <button type="button" class="btn btn-default" ng-click="open($event)"><i class="glyphicon glyphicon-calendar"></i></button>
              </span>
                </p>
            </div>
            <div class="col-md-12">
                <uib-timepicker ng-model="mytime" ng-change="changed()" hour-step="hstep" minute-step="mstep" show-meridian="ismeridian"></uib-timepicker>
            </div>

            <pre class="alert alert-info">Time is: {{mytime | date:'short' }}</pre>
        </div>


        {{clientId}}

    </div>

</div>
<script src="<%=request.getContextPath()%>/resources/jquery/dist/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/resources/angular-bootstrap/ui-bootstrap-tpls.min.js"></script>

<script type="text/javascript">

    var jakdukApp = angular.module('jakdukApp', ['ui.bootstrap']);

    jakdukApp.factory('clientId', function() {
        return 'a12345654321x';
    });

    jakdukApp.controller('sampleCtrl', function($scope, clientId) {
        $scope.clientId = clientId;
        $scope.format = 'yyyy/MM/dd';
        $scope.mytime = new Date();
        $scope.status = {
            opened: false
        };

        $scope.hstep = 1;
        $scope.mstep = 15;

        angular.element(document).ready(function() {
        });

        $scope.open = function($event) {
            $scope.status.opened = true;
        };
    });
</script>

<jsp:include page="../include/body-footer.jsp"/>
</body>
</html>
