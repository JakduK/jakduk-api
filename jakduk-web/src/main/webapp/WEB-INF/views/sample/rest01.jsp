<%--
  Created by IntelliJ IDEA.
  User: pyohwan
  Date: 16. 1. 3
  Time: 오후 9:17
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html ng-app="jakdukApp">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Restful Test 01 &middot; <spring:message code="common.jakduk"/></title>
    <jsp:include page="../include/html-header.jsp"/>
</head>
<body>
<div class="wrapper">
    <jsp:include page="../include/navigation-header.jsp"/>

    <div class="container content" ng-controller="sampleCtrl">

        <button class="btn btn-default" ng-click="btnWriteComment()">
            <span aria-hidden="true" class="icon-pencil"></span> <spring:message code="common.button.write.comment"/>
        </button>


    </div>

</div>
<script src="<%=request.getContextPath()%>/resources/jquery/dist/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/resources/angular-bootstrap/ui-bootstrap-tpls.min.js"></script>

<script type="text/javascript">

    var jakdukApp = angular.module('jakdukApp', ['ui.bootstrap']);

    jakdukApp.controller('sampleCtrl', function($scope, $http) {

        // http config
        var headers = {
            "Content-Type" : "application/json"
        };

        var config = {
            headers:headers
        };

        angular.element(document).ready(function() {
        });

        $scope.btnWriteComment = function(status) {
            var bUrl = '<c:url value="/sample/rest01"/>';
            var reqPromise = $http.post(bUrl, {test:1}, config);

            reqPromise.success(function(data, status, headers, config) {
               console.log("success");
            });
            reqPromise.error(function(data, status, headers, config) {
                console.log("error=" + status);
            });
        };
    });
</script>

<jsp:include page="../include/body-footer.jsp"/>
</body>
</html>
