<%--
  Created by IntelliJ IDEA.
  User: pyohwan
  Date: 16. 3. 6
  Time: 오후 10:44
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!DOCTYPE html>
<html ng-app="jakdukApp">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Button Test 01 &middot; <spring:message code="common.jakduk"/></title>
    <jsp:include page="../include/html-header.jsp"/>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/resources/unify/assets/plugins/brand-buttons/css/brand-buttons.min.css">
</head>

<body>
<div class="wrapper">
    <jsp:include page="../include/navigation-header.jsp"/>

    <div class="container content" ng-controller="sampleCtrl">

        <button type="button" class="btn btn-xs rounded btn-weibo">
            <i class="fa fa-thumbs-o-down fa-lg"></i>
        </button>

        <div class="btn-group">
            <label class="btn btn-success" ng-model="radioModel" uib-btn-radio="'Left'" uncheckable>Left</label>
            <label class="btn btn-success" ng-model="radioModel" uib-btn-radio="'Middle'" uncheckable>Middle</label>
            <label class="btn btn-success" ng-model="radioModel" uib-btn-radio="'Right'" uib-uncheckable="uncheckable">Right</label>
        </div>


    </div>

</div>
<script src="<%=request.getContextPath()%>/resources/jquery/dist/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/resources/angular-bootstrap/ui-bootstrap-tpls.min.js"></script>

<script type="text/javascript">

    var jakdukApp = angular.module('jakdukApp', ['ui.bootstrap']);

    jakdukApp.controller('sampleCtrl', function($scope) {

        angular.element(document).ready(function() {
        });
    });
</script>

<jsp:include page="../include/body-footer.jsp"/>

</body>
</html>
