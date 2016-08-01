<%--
  Created by IntelliJ IDEA.
  User: pyohwan
  Date: 16. 1. 2
  Time: 오후 3:20
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<!--[if IE 9]> <html lang="ko" class="ie9" ng-app="jakdukApp"> <![endif]-->
<!--[if !IE]><!-->
<html lang="ko" ng-app="jakdukApp"> <!--<![endif]-->
	<head>
		<title>
			<spring:message code="jakdu.write"/> &middot; <spring:message code="jakdu"/> &middot;
			<spring:message code="common.jakduk"/>
		</title>
		<jsp:include page="../include/html-header.jsp"/>
		<link rel="stylesheet" href="<%=request.getContextPath()%>/bundles/jakdu.css">
	</head>

	<body class="header-fixed">

		<div class="wrapper" ng-controller="jakduCtrl">
			<jsp:include page="../include/navigation-header.jsp"/>

			<c:set var="contextPath" value="<%=request.getContextPath()%>"/>

			<!--=== Breadcrumbs ===-->
			<div class="breadcrumbs">
				<div class="container">
					<h1 class="pull-left"><spring:message code="jakdu.write"/></h1>
				</div><!--/container-->
			</div><!--/breadcrumbs-->
			<!--=== End Breadcrumbs ===-->

			<!--=== Content Part ===-->
			<div class="container content">

				<div class="form-horizontal">

					<div class="row" ng-repeat="jakdu in jakdus">

						<div class="row">
							<div class="col-sm-4">
								<label class="col-sm-3 control-label"><spring:message code="common.date"/></label>
								<div class="col-sm-9">
									<p class="form-control-static">{{jakdu.schedule.date | date:dateTimeFormat.dateTime}}</p>
								</div>
							</div>
							<div class="col-sm-4">
								<label class="col-sm-3 control-label"><spring:message code="common.competition"/></label>
								<div class="col-sm-9">
									<p class="form-control-static">{{competitionNames[jakdu.schedule.competition.id].fullName}}</p>
								</div>
							</div>
							<div class="col-sm-4">
								<label class="col-sm-3 control-label"><spring:message code="jakdu.match"/></label>
								<div class="col-sm-9">
									<p class="form-control-static">{{fcNames[jakdu.schedule.home.id].shortName}} VS {{fcNames[jakdu.schedule.away.id].shortName}}</p>
								</div>
							</div>
						</div>

						<div class="row">
							<div class="col-sm-6">
								<label class="col-sm-2 control-label"><spring:message code="jakdu.expect.score"/></label>
								<div class="col-sm-5">
									<select class="form-control" ng-model="jakdu.homeScore">
										<option value=""><spring:message code="board.placeholder.expect.home.score"/></option>
										<option ng-repeat="opt in rangeScore" value="{{opt}}">{{opt}}</option>
									</select>
								</div>
								<div class="col-sm-5">
									<select class="form-control" ng-model="jakdu.awayScore">
										<option value=""><spring:message code="board.placeholder.expect.away.score"/></option>
										<option ng-repeat="opt in rangeScore" value="{{opt}}">{{opt}}</option>
									</select>
								</div>

							</div>
						</div>
					</div>

					<button class="btn-u" type="button" ng-click="btnTest()">Button Default</button>

				</div>

				</form> <!--=== End Content Part ===-->

			<jsp:include page="../include/footer.jsp"/>
		</div>

		<script src="<%=request.getContextPath()%>/bundles/jakdu.js"></script>
		<script type="text/javascript">
			var jakdukApp = angular.module("jakdukApp", ["ui.bootstrap", 'jakdukCommon']);

			jakdukApp.controller('jakduCtrl', function ($scope, $http) {
				$scope.rangeScore = [];
				$scope.dataJakdusConn = "none";
				$scope.dateTimeFormat = {};
				$scope.jakdus = [];
				$scope.competitionNames = {};
				$scope.fcNames = {};

				for (i = 0; i < 19; i++) {
					$scope.rangeScore.push(i);
				}

				// http config
				var headers = {
					"Content-Type": "application/json"
				};

				var config = {
					headers: headers
				};

				angular.element(document).ready(function () {
					$scope.getDataJakdus();

					App.init();
				});

				$scope.btnTest = function () {
					var bUrl = '<c:url value="/sample/rest01"/>';
					var reqData = {};
					reqData.jakdus = $scope.jakdus;

					var reqPromise = $http.post(bUrl, reqData, config);

					reqPromise.success(function (data, status, headers, config) {
						console.log("success");
					});
					reqPromise.error(function (data, status, headers, config) {
						console.log("error=" + status);
					});
				};

				$scope.getDataJakdus = function () {
					var bUrl = '<c:url value="/jakdu/data" />';

					if ($scope.dataJakdusConn == "none") {

						var reqPromise = $http.get(bUrl);

						$scope.dataJakdusConn = "loading";

						reqPromise.success(function (data, status, headers, config) {

							console.log(data);

							if (data.dateTimeFormat != null) {
								$scope.dateTimeFormat = data.dateTimeFormat;
							}

							if (data.jakdus != null) {
								$scope.jakdus = data.jakdus;
							}

							if (data.competitionNames != null) {
								$scope.competitionNames = data.competitionNames;
							}

							if (data.fcNames != null) {
								$scope.fcNames = data.fcNames;
							}

							$scope.dataJakdusConn = "none";
						});
						reqPromise.error(function (data, status, headers, config) {
							$scope.dataJakdusConn = "none";
							$scope.error = '<spring:message code="common.msg.error.network.unstable"/>';
						});
					}
				};
			});
		</script>
	</body>
</html>
