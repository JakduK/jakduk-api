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
                    <div class="col-sm-2"><spring:message code="board.number"/> | <spring:message code="board.category"/></div>
                    <div class="col-sm-4"><spring:message code="board.subject"/></div>
                    <div class="col-sm-3"><spring:message code="board.writer"/> | <spring:message code="board.date"/></div>
                    <div class="col-sm-3"><spring:message code="board.views"/> | <spring:message code="common.like"/> | <spring:message code="common.dislike"/></div>
                </div>
            </div> <!-- /panel-heading -->

        </div>


    </div> <!--=== End Content Part ===-->

    <jsp:include page="../include/footer.jsp"/>
</div>

<script src="<%=request.getContextPath()%>/resources/jquery/dist/jquery.min.js"></script>
<script type="text/javascript">
    var jakdukApp = angular.module("jakdukApp", []);

    jakdukApp.controller('jakduCtrl', function($scope) {

        angular.element(document).ready(function() {
            App.init();
        });
    });
</script>

<jsp:include page="../include/body-footer.jsp"/>
</body>
</html>