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
                    <form action="${contextPath}/password/reset" class="reg-page" name="resetForm" method="POST" ng-submit="onSubmit($event)">
                        <input id="code" name="code" type="hidden" value="${code}">

                        <div class="reg-header">
                            <h2><spring:message code="user.msg.reset.your.password.title"/></h2>
                        </div>

                        <div class="form-group">
                            <h4 class="text-center">${subject}</h4>
                            <p><spring:message code="user.msg.input.new.password"/></p>

                            <div class="input-group has-feedback"
                                 ng-class="{'has-success':resetForm.password.$valid, 'has-error':resetForm.password.$invalid}">
                                <span class="input-group-addon"><i class="fa fa-lock fa-fw"></i></span>

                                <input type="password" name="password" class="form-control" placeholder='<spring:message code="user.placeholder.new.password"/>'
                                       ng-model="password" ng-change="validationPassword()"
                                       ng-required="true" ng-minlength="passwordLengthMin" ng-maxlength="passwordLengthMax" autofocus/>

                                <span class="glyphicon form-control-feedback" ng-class="{'glyphicon-ok':resetForm.password.$valid, 'glyphicon-remove':resetForm.password.$invalid}"></span>
                            </div>
                            <span class="{{passwordAlert.classType}}" ng-show="passwordAlert.msg">{{passwordAlert.msg}}</span>
                        </div>

                        <button type="submit" class="btn btn-u rounded btn-block ladda-button" ladda="btnSubmit" data-style="expand-right">
                            <spring:message code="common.button.user.password.update"/>
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
                    $scope.passwordLengthMin = Jakduk.FormPasswordLengthMin;
                    $scope.passwordLengthMax = Jakduk.FormPasswordLengthMax;

                    $scope.passwordAlert = {};

                    angular.element(document).ready(function () {
                    });

                    $scope.onSubmit = function (event) {
                        if ($scope.resetForm.$valid) {
                            $scope.btnSubmit = true;
                        } else {
                            event.preventDefault();
                        }
                    };

                    $scope.validationPassword = function () {
                        if ($scope.resetForm.password.$invalid) {
                            if ($scope.resetForm.password.$error.required) {
                                $scope.passwordAlert = {
                                    "classType": "text-danger",
                                    "msg": '<spring:message code="common.msg.required"/>'
                                };
                            } else if ($scope.resetForm.password.$error.minlength || $scope.resetForm.password.$error.maxlength) {
                                $scope.passwordAlert = {
                                    "classType": "text-danger",
                                    "msg": '<spring:message code="Size.userWrite.password"/>'
                                };
                            }
                        } else {
                            $scope.passwordAlert = {
                                "classType": "text-success",
                                "msg": '<spring:message code="user.msg.avaliable.data"/>'
                            };
                        }
                    };
                });

        $(document).ready(function () {
            App.init();
        });
    </script>
</body>
</html>
