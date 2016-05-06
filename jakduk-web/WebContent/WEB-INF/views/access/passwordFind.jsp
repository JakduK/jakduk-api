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

        <c:set var="contextPath" value="<%=request.getContextPath()%>"/>

        <div class="container content">

            <div class="row">
                <div class="col-md-4 col-md-offset-4 col-sm-6 col-sm-offset-3">
                    <form action="${contextPath}/password/find" name="resetForm" class="reg-page" method="POST" ng-submit="onSubmit($event)">
                        <div class="reg-header">
                            <h2><spring:message code="user.msg.find.your.password.title"/></h2>
                        </div>
                        <p><spring:message code="user.msg.find.your.password"/></p>

                        <div class="form-group">
                            <div class="input-group has-feedback"
                                 ng-class="{'has-success':resetForm.email.$valid, 'has-error':resetForm.email.$invalid}">
                                <span class="input-group-addon"><i class="fa fa-at fa-fw"></i></span>

                                <input type="email" class="form-control" name="email" placeholder='<spring:message code="user.placeholder.email"/>'
                                       ng-model="email" ng-keyup="validationEmail()"
                                       ng-required="true" ng-minlength="emailLengthMin" ng-maxlength="emailLengthMax"/>

                                <span class="glyphicon form-control-feedback" ng-class="{'glyphicon-ok':resetForm.email.$valid, 'glyphicon-remove':resetForm.email.$invalid}"></span>
                            </div>
                            <span class="{{emailAlert.classType}}" ng-show="emailAlert.msg">{{emailAlert.msg}}</span>
                        </div>

                        <button type="submit" class="btn btn-u rounded btn-block ladda-button" ladda="btnSubmit" data-style="expand-right">
                            <spring:message code="common.button.send.mail"/>
                        </button>

                        <button type="button" class="btn btn-info btn-block rounded" onclick="location.href='<c:url value="/login"/>'">
                            <spring:message code="common.button.back.to.login"/>
                        </button>
                    </form>
                </div>
            </div><!--/row-->
        </div>

        <jsp:include page="../include/footer.jsp"/>
    </div><!-- /.container -->


<script src="<%=request.getContextPath()%>/bundles/password.find.js"></script>
<script type="text/javascript">
    angular.module("jakdukApp", ["angular-ladda", 'jakdukCommon'])
            .controller("resetPasswordCtrl", function ($scope) {
                $scope.emailLengthMin = Jakduk.FormEmailLengthMin;
                $scope.emailLengthMax = Jakduk.FormEmailLengthMax;

                $scope.emailAlert = {};

                angular.element(document).ready(function () {
                });

                $scope.onSubmit = function (event) {
                    if ($scope.resetForm.$valid) {
                        $scope.btnSubmit = true;
                    } else {
                        event.preventDefault();
                    }
                };

                $scope.validationEmail = function () {
                    console.log("123");

                    if ($scope.resetForm.email.$invalid) {
                        if ($scope.resetForm.email.$error.required) {
                            $scope.emailAlert = {"classType": "text-danger", "msg": '<spring:message code="common.msg.required"/>'};
                        } else if ($scope.resetForm.email.$error.minlength || $scope.resetForm.email.$error.maxlength) {
                            $scope.emailAlert = {"classType": "text-danger", "msg": '<spring:message code="Size.userWrite.email"/>'};
                        } else {
                            $scope.emailAlert = {
                                "classType": "text-danger",
                                "msg": '<spring:message code="user.msg.check.mail.format"/>'
                            };
                        }
                    } else {
                        $scope.emailAlert = {};
                    }
                };
            });

    $(document).ready(function () {
        App.init();
    });
</script>
</body>
</html>
