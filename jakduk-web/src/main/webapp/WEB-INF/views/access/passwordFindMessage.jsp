<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!DOCTYPE html>
<!--[if IE 9]><html lang="ko" class="ie9" ng-app="jakdukApp"><![endif]-->
<!--[if !IE]> --><html lang="ko" ng-app="jakdukApp"><!-- <![endif]-->
<head>
    <title><spring:message code="user.sign.in"/> &middot; <spring:message code="common.jakduk"/></title>
    <jsp:include page="../include/html-header.jsp"/>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/bundles/password.find.css">
</head>

<body class="header-fixed" ng-controller="resetPasswordCtrl">

    <div class="wrapper">
        <jsp:include page="../include/navigation-header.jsp"/>

        <!--=== Breadcrumbs ===-->
        <div class="breadcrumbs">
            <div class="container">
                <h1 class="pull-left"><spring:message code="user.sign.forgot.password"/></h1>
            </div><!--/container-->
        </div><!--/breadcrumbs-->
        <!--=== End Breadcrumbs ===-->

        <div class="container content">

            <div class="row">
                <div class="col-md-4 col-md-offset-4 col-sm-6 col-sm-offset-3">
                    <div class="reg-page">
                        <div class="reg-header">
                            <h2><spring:message code="user.sign.reset.password"/></h2>
                        </div>

                        <div class="form-group">
                            <h4 class="text-center">${subject}</h4>
                            <p>${message}</p>
                        </div>

                        <button type="button" class="btn btn-info btn-block rounded" onclick="location.href='<c:url value="/login"/>'">
                            <spring:message code="common.button.back.to.login"/>
                        </button>
                    </div>
                </div>
            </div><!--/row-->
        </div>

        <jsp:include page="../include/footer.jsp"/>
    </div><!-- /.container -->

    <script src="<%=request.getContextPath()%>/bundles/password.find.js"></script>
    <script type="text/javascript">
        angular.module("jakdukApp", ['jakdukCommon'])
                .controller("resetPasswordCtrl", function ($scope) {
                    angular.element(document).ready(function () {
                    });
                });

        $(document).ready(function () {
            App.init();
        });
    </script>
</body>
</html>
