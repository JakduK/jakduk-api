<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!DOCTYPE html>
<!--[if IE 9]> <html lang="ko" class="ie9" ng-app="jakdukApp"> <![endif]-->
<!--[if !IE]><!-->
<html lang="ko" ng-app="jakdukApp"> <!--<![endif]-->
	<head>
		<title>
			<spring:message code="stats.attendance.season.title"/> &middot; <spring:message code="stats"/> &middot;
			<spring:message code="common.jakduk"/>
		</title>
		<jsp:include page="../include/html-header.jsp"/>
		<link rel="stylesheet" href="<%=request.getContextPath()%>/bundles/stats.css">
	</head>

	<body class="header-fixed">

		<div class="wrapper" ng-controller="statsCtrl">
			<jsp:include page="../include/navigation-header.jsp"/>

			<!--=== Breadcrumbs ===-->
			<div class="breadcrumbs">
				<div class="container">
					<h1 class="pull-left">
						<a href="<c:url value="/stats/attendance/season/refresh"/>"><spring:message code="stats.attendance"/></a>
					</h1>
					<ul class="pull-right breadcrumb">
						<li>
							<a href="<c:url value="/stats/attendance/league"/>"><spring:message code="stats.attendance.breadcrumbs.league"/></a>
						</li>
						<li>
							<a href="<c:url value="/stats/attendance/club"/>"><spring:message code="stats.attendance.breadcrumbs.club"/></a>
						</li>
						<li class="active"><spring:message code="stats.attendance.breadcrumbs.season"/></li>
					</ul>
				</div><!--/container-->
			</div><!--/breadcrumbs-->
			<!--=== End Breadcrumbs ===-->

			<!--=== Content Part ===-->
			<div class="container content">

				<div class="alert fade in rounded ng-cloak" ng-class="alert.classType" ng-show="alert.msg">
					{{alert.msg}}
				</div>

				<div class="row">
					<div class="col-xs-8 col-sm-4 col-md-3">
						<select class="form-control" ng-model="season" ng-change="changeSeason()"
							ng-options='season for season in seasons'>
							<option value=""><spring:message code="stats.select.season"/></option>
						</select>
						<span class="color-blue" ng-class="{'hidden':season}"><spring:message code="stats.msg.choose.season"/></span>
					</div>
					<div class="col-xs-8 col-sm-4 col-md-3">
						<select class="form-control" ng-model="leagueId" ng-change="changeLeague()"
							ng-options='key as value.name disable when value.disable for (key, value) in leagues[season]'>
							<option value=""><spring:message code="stats.select.league"/></option>
						</select>
						<span class="color-blue" ng-class="{'hidden':leagueId}"><spring:message code="stats.msg.choose.league"/></span>
					</div>
				</div>

				<highchart id="chart1" config="chartConfig" class="margin-bottom-10"></highchart>

				<div class="tag-box tag-box-v4 margin-bottom-20">
					<h2>{{chartConfig.title.text}}</h2>
					<p>
						<spring:message code="stats.msg.total.number.of.clubs" arguments="<strong>{{chartConfig.series[0].data.length | number:0}}</strong>"/></p>
					<p>
						<spring:message code="stats.msg.total.number.of.attendance.matches" arguments="<strong>{{gamesSum | number:0}}</strong>"/></p>
					<p>
						<spring:message code="stats.msg.total.number.of.attendance.total" arguments="<strong>{{totalSum | number:0}}</strong>"/></p>
					<p>
						<spring:message code="stats.msg.total.number.of.attendance.average" arguments="<strong>{{average | number:0}}</strong>"/></p>
				</div>

				<div class="text-right">
					<button class="btn-u btn-brd rounded btn-u-xs" type="button" ng-click="btnUrlCopy()"
						tooltip-popup-close-delay='300' uib-tooltip='<spring:message code="common.msg.copy.to.clipboard"/>'>
						<i class="icon-link"></i>
					</button>
					<a id="kakao-link-btn" href="" ng-click="sendLink()"
						tooltip-popup-close-delay='300' uib-tooltip='<spring:message code="common.msg.send.to.kakao"/>'>
						<img src="<%=request.getContextPath()%>/resources/kakao/icon/kakaolink_btn_small.png"/>
					</a>
				</div>
			</div> <!-- End Content Part -->

			<jsp:include page="../include/footer.jsp"/>
		</div> <!-- End Wrapper -->

		<script src="<%=request.getContextPath()%>/bundles/stats.js"></script>
		<script type="text/javascript">
			var jakdukApp = angular.module("jakdukApp", ["highcharts-ng", "ui.bootstrap", 'jakdukCommon']);

			jakdukApp.controller('statsCtrl', function ($scope, $http) {
				var KLId = 'KL';
				var KLCLId = 'KLCL';
				var KLCHId = 'KLCH';
				var KLname = '<spring:message code="stats.attendance.filter.league"/>';
				var KLCLname = '<spring:message code="stats.attendance.filter.league.classic"/>';
				var KLCHname = '<spring:message code="stats.attendance.filter.league.challenge"/>';

				$scope.leagues = {
					2012: {
						'KL': {name: KLname},
						'KLCL': {name: KLCLname, disable: true},
						'KLCH': {name: KLCHname, disable: true}
					},
					2013: {'KL': {name: KLname}, 'KLCL': {name: KLCLname}, 'KLCH': {name: KLCHname}},
					2014: {'KL': {name: KLname}, 'KLCL': {name: KLCLname}, 'KLCH': {name: KLCHname}}
				};
				$scope.seasons = [2012, 2013, 2014];

				$scope.attendancesConn = "none";
				$scope.fcNames = JSON.parse('${fcNames}');
				$scope.attendances = {};
				$scope.alert = {};
				$scope.rememberLeague = null;
				$scope.totalSum = 0;
				$scope.gamesSum = 0;
				$scope.average = 0;

				angular.element(document).ready(function () {
					var season = Number("${season}");
					var league = "${league}";

					if ($scope.seasons.indexOf(season) >= 0) {
						$scope.season = season;
					} else {
						$scope.season = 2014;
					}

					var leagueKeys = [];
					for (var key1 in $scope.leagues[$scope.season]) {
						var key2 = $scope.leagues[$scope.season][key1];

						if (key2.disable != true) {
							leagueKeys.push(key1);
						}
					}

					if (leagueKeys.indexOf(league) >= 0) {
						$scope.leagueId = league;
					} else {
						$scope.leagueId = KLId;
					}

					Highcharts.setOptions({
						lang: {
							thousandsSep: ','
						}
					});

					$scope.chartConfig = {
						options: {
							chart: {
								type: 'bar',
								height: 200
							},
							tooltip: {
								//pointFormat: '<spring:message code="stats.attendance.total"/> : <b>{point.y:,.0f}</b> <spring:message code="stats.attendance.people"/>',
								shared: true
							}
						},
						title: {
							text: '<spring:message code="stats.attendance"/>'
						},
						subtitle: {
							text: 'Source: http://www.kleague.com'
						},
						xAxis: {
							type: 'category',
							labels: {
								style: {
									fontSize: '13px'
								}
							},
							crosshair: true
						},
						yAxis: [{ // Total yAxis
							min: 0,
							title: {
								text: '<spring:message code="stats.attendance.total"/>',
								align: 'middle'
							},
							labels: {
								x: 0,
								rotation: -30,
								formatter: function () {
									return Highcharts.numberFormat(this.value, 0);
								}
							},
							opposite: true
						},
							{ // Average yAxis
								min: 0,
								title: {
									text: '<spring:message code="stats.attendance.average"/>'
								},
								labels: {
									x: 0,
									rotation: -30,
									formatter: function () {
										return Highcharts.numberFormat(this.value, 0);
									}
								}
							}],
						series: [{
							name: '<spring:message code="stats.attendance.total"/>',
							yAxis: 0,
							data: [],
							dataLabels: {
								enabled: true,
								color: '#FFFFFF',
								align: 'right',
								format: '{point.y:,.0f} <spring:message code="stats.attendance.people"/>',
								style: {
									fontSize: '13px'
								}
							}
						},
							{
								name: '<spring:message code="stats.attendance.average"/>',
								yAxis: 1,
								data: [],
								dataLabels: {
									enabled: true,
									color: '#FFFFFF',
									align: 'right',
									format: '{point.y:,.0f} <spring:message code="stats.attendance.people"/>',
									style: {
										fontSize: '13px'
									}
								}
							}],
						loading: true,
						credits: {enabled: true}
					};

					// 사용할 앱의 Javascript 키를 설정해 주세요.
					Kakao.init('${kakaoKey}');

					$scope.getAttendance();
				});

				$scope.getAttendance = function () {

					var bUrl = '<c:url value="/api/stats/attendance/season/' + $scope.season + '?league=' + KLId + '"/>';

					if ($scope.attendancesConn == "none") {

						$scope.chartConfig.loading = true;
						var reqPromise = $http.get(bUrl);

						$scope.attendancesConn = "loading";

						reqPromise.success(function (data, status, headers, config) {
							$scope.attendances[$scope.season] = data;

							$scope.attendancesConn = "none";
							$scope.chartConfig.loading = false;
							$scope.refreshData();
						});

						reqPromise.error(function (data, status, headers, config) {
							$scope.attendancesConn = "none";
							$scope.alert.msg = data.message;
							$scope.alert.classType = "alert-danger";
						});
					}
				};

				$scope.refreshData = function () {
					if (Jakduk.isEmpty($scope.leagueId)) return;

					$scope.chartConfig.series.forEach(function (series) {
						series.data = [];
					});

					var attendances = $scope.attendances[$scope.season];
					var totalSum = 0;
					var gamesSum = 0;

					attendances.forEach(function (attendance) {
						if ($scope.leagueId == KLId || $scope.leagueId == attendance.league) {
							var itemTotal = [$scope.fcNames[attendance.club.id], attendance.total];
							var itemAverage = [$scope.fcNames[attendance.club.id], attendance.average];
							totalSum += attendance.total;
							gamesSum += attendance.games;

							$scope.chartConfig.series[0].data.push(itemTotal);
							$scope.chartConfig.series[1].data.push(itemAverage);
						}
						$scope.totalSum = totalSum;
						$scope.average = totalSum / gamesSum;
						$scope.gamesSum = gamesSum;
					});

					$scope.chartConfig.title.text = $scope.season + ' ' + $scope.leagues[$scope.season][$scope.leagueId].name
						+ ' <spring:message code="stats.attendance"/>';
					$scope.chartConfig.options.chart.height = 300 + ($scope.chartConfig.series[0].data.length * 35);

				};

				$scope.changeSeason = function () {
					$scope.rememberLeague = $scope.leagueId;

					if ($scope.season != null) {
						if (Jakduk.isEmpty($scope.attendances[$scope.season])) {
							$scope.getAttendance();
						} else {
							$scope.refreshData();
						}
					}
				};

				$scope.changeLeague = function () {

					if ($scope.leagueId != null) {
						$scope.refreshData();
					}
				};

				$scope.btnUrlCopy = function () {
					var url = "https://jakduk.com/stats/attendance/season?season=" + $scope.season + '&league=' + $scope.leagueId;

					if (window.clipboardData) {
						// IE처리
						// 클립보드에 문자열 복사
						window.clipboardData.setData('text', url);

						// 클립보드의 내용 가져오기
						// window.clipboardData.getData('Text');

						// 클립보드의 내용 지우기
						// window.clipboardData.clearData("Text");
					} else {
						// 비IE 처리
						window.prompt('<spring:message code="common.msg.copy.to.clipboard"/>', url);
					}
				};

				$scope.sendLink = function () {
					var label = $scope.chartConfig.title.text
						+ '\r<spring:message code="stats"/> · <spring:message code="common.jakduk"/>';
					var url = "https://jakduk.com/stats/attendance/season?season=" + $scope.season + '&league=' + $scope.leagueId;

					Kakao.Link.sendTalkLink({
						label: label,
						webLink: {
							text: url,
							url: url
						}
					});
				};

			});

			$(document).ready(function () {
				App.init();
			});
		</script>
	</body>
</html>