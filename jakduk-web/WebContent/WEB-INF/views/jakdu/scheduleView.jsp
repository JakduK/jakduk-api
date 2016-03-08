<%--
  Created by IntelliJ IDEA.
  User: pyohwan
  Date: 16. 2. 15
  Time: 오후 11:52
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!DOCTYPE html>
<!--[if IE 9]> <html lang="ko" class="ie9" ng-app="jakdukApp"> <![endif]-->
<!--[if !IE]><!--> <html lang="ko" ng-app="jakdukApp"> <!--<![endif]-->
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title><spring:message code="jakdu.view"/> &middot; <spring:message code="jakdu"/> &middot;
			<spring:message code="common.jakduk"/></title>

		<jsp:include page="../include/html-header.jsp"></jsp:include>

		<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/unify/assets/plugins/line-icons-pro/styles.css">
		<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/unify/assets/plugins/ladda-buttons/css/custom-lada-btn.css">
	</head>

	<body class="header-fixed">

		<div class="wrapper">
			<jsp:include page="../include/navigation-header.jsp"/>

			<!--=== Breadcrumbs ===-->
			<div class="breadcrumbs">
				<div class="container">
					<h1 class="pull-left">
						<a href="<c:url value="/jakdu/schedule/refresh"/>"><spring:message code="jakdu.view"/></a></h1>
				</div><!--/container-->
			</div><!--/breadcrumbs-->
			<!--=== End Breadcrumbs ===-->

			<!--=== Content Part ===-->
			<div class="container content" ng-controller="jakduCtrl">

				<div class="row margin-bottom-30">
					<div class="col-xs-4 content-boxes-v6">
						<i class="rounded-x  icon-sport-011 "></i>
						<h1 class="title-v3-md text-uppercase margin-bottom-10" ng-bind="jakduSchedule.home.name"></h1>
						<p>Home</p>
					</div>
					<div class="col-xs-4">
						<div class="service-block-v1">
							<i class="rounded-x icon-sport-119"></i>
							<h3 class="title-v3-bg text-uppercase" ng-bind="jakduSchedule.score.homeFullTime + ':' + jakduSchedule.score.awayFullTime"></h3>
							<p>Score</p>
						</div>
					</div>
					<div class="col-xs-4 content-boxes-v6">
						<i class="rounded-x  icon-sport-011 "></i>
						<h2 class="title-v3-md text-uppercase margin-bottom-10" ng-bind="jakduSchedule.away.name"></h2>
						<p>Away</p>
					</div>
				</div>

				<div class="row">
					<div class="col-xs-6">
						<div class="panel panel-info margin-bottom-5">
							<div class="panel-heading text-center">
								<h4 class="panel-title">
									<spring:message code="jakdu.expect.score"/> : <span ng-bind="myHomeScore"></span>
								</h4>
							</div>
							<div class="panel-body">
								<div class="center-block jakduk-number-pad">
									<div class="jakduk-number-pad-row">
										<label class="btn btn-primary" ng-model="myHomeScore" uib-btn-radio="0">0</label>
										<label class="btn btn-primary" ng-model="myHomeScore" uib-btn-radio="1">1</label>
										<label class="btn btn-primary" ng-model="myHomeScore" uib-btn-radio="2">2</label>
									</div>
									<div class="jakduk-number-pad-row">
										<label class="btn btn-primary" ng-model="myHomeScore" uib-btn-radio="3">3</label>
										<label class="btn btn-primary" ng-model="myHomeScore" uib-btn-radio="4">4</label>
										<label class="btn btn-primary" ng-model="myHomeScore" uib-btn-radio="5">5</label>
									</div>
									<div class="jakduk-number-pad-row">
										<label class="btn btn-primary" ng-model="myHomeScore" uib-btn-radio="6">6</label>
										<label class="btn btn-primary" ng-model="myHomeScore" uib-btn-radio="7">7</label>
										<label class="btn btn-primary" ng-model="myHomeScore" uib-btn-radio="8">8</label>
									</div>
									<div class="jakduk-number-pad-row">
										<label class="btn btn-primary" ng-model="myHomeScore" uib-btn-radio="9">9</label>
										<label class="btn btn-primary" ng-model="myHomeScore" uib-btn-radio="10">10</label>
										<label class="btn btn-primary" ng-model="myHomeScore" uib-btn-radio="11">11</label>
									</div>
								</div>
							</div>
						</div>
					</div>
					<div class="col-xs-6">
						<div class="panel panel-success margin-bottom-5">
							<div class="panel-heading text-center">
								<h4 class="panel-title">
									<spring:message code="jakdu.expect.score"/> : <span ng-bind="myAwayScore"></span>
								</h4>
							</div>
							<div class="panel-body">
								<div class="center-block jakduk-number-pad">
									<div class="jakduk-number-pad-row">
										<label class="btn btn-success" ng-model="myAwayScore" uib-btn-radio="0">0</label>
										<label class="btn btn-success" ng-model="myAwayScore" uib-btn-radio="1">1</label>
										<label class="btn btn-success" ng-model="myAwayScore" uib-btn-radio="2">2</label>
									</div>
									<div class="jakduk-number-pad-row">
										<label class="btn btn-success" ng-model="myAwayScore" uib-btn-radio="3">3</label>
										<label class="btn btn-success" ng-model="myAwayScore" uib-btn-radio="4">4</label>
										<label class="btn btn-success" ng-model="myAwayScore" uib-btn-radio="5">5</label>
									</div>
									<div class="jakduk-number-pad-row">
										<label class="btn btn-success" ng-model="myAwayScore" uib-btn-radio="6">6</label>
										<label class="btn btn-success" ng-model="myAwayScore" uib-btn-radio="7">7</label>
										<label class="btn btn-success" ng-model="myAwayScore" uib-btn-radio="8">8</label>
									</div>
									<div class="jakduk-number-pad-row">
										<label class="btn btn-success" ng-model="myAwayScore" uib-btn-radio="9">9</label>
										<label class="btn btn-success" ng-model="myAwayScore" uib-btn-radio="10">10</label>
										<label class="btn btn-success" ng-model="myAwayScore" uib-btn-radio="11">11</label>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>

				<hr>

				<div class="row text-center">
					<button type="button" class="btn-u btn-u-blue rounded ladda-button"
						ng-click="btnGoJakdu()"
						ladda="goJakdu" data-style="expand-right" data-spinner-color="Gainsboro">
						<strong>
							<i aria-hidden="true" class="fa fa-hand-scissors-o fa-lg"></i>
							<spring:message code="common.button.go.jakdu"/>
						</strong>

					</button>
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
			angular.module("jakdukApp", ["ui.bootstrap", "angular-ladda"])
				.controller('jakduCtrl', function ($scope, $http) {
					// http config
					var headers = {
						"Content-Type": "application/json"
					};

					var config = {
						headers: headers
					};

					$scope.btnGoJakdu = btnGoJakdu;
					$scope.getDataSchedule = getDataSchedule;

					$scope.dataScheduleConn = "none";   // 작두 데이터 커넥션 상태
					$scope.jakduSchedule = {};          // 작두 데이터

					getDataSchedule();

					function btnGoJakdu() {
						var bUrl = '<c:url value="/jakdu/myJakdu"/>';
						var reqData = {};

						if (isEmpty($scope.myHomeScore) || isEmpty($scope.myAwayScore)) {
							return;
						}

						reqData.homeScore = $scope.myHomeScore;
						reqData.awayScore = $scope.myAwayScore;
						reqData.jakduScheduleId = "${id}";

						var reqPromise = $http.post(bUrl, reqData, config);
						$scope.goJakdu = true;

						reqPromise.success(function (data, status, headers, config) {
							console.log("success");
							$scope.goJakdu = false;
						});
						reqPromise.error(function (data, status, headers, config) {
							alert(data.message);
							$scope.goJakdu = false;
						});
					}

					function getDataSchedule() {
						var bUrl = '<c:url value="/jakdu/data/schedule/${id}" />';

						if ($scope.dataScheduleConn == "none") {

							var reqPromise = $http.get(bUrl);

							$scope.dataScheduleConn = "loading";

							reqPromise.success(function (data, status, headers, config) {

								if (data.jakduSchedule != null) {
									$scope.jakduSchedule = data.jakduSchedule;
								}

								$scope.dataScheduleConn = "none";
							});
							reqPromise.error(function (data, status, headers, config) {
								$scope.dataScheduleConn = "none";
								$scope.error = '<spring:message code="common.msg.error.network.unstable"/>';
							});
						}
					};

				});
		</script>

		<jsp:include page="../include/body-footer.jsp"/>

		<script type="text/javascript">
			$(document).ready(function () {
				App.init();
			});
		</script>

	</body>
</html>
