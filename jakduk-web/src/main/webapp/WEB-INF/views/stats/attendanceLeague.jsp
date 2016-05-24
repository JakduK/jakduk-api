<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!DOCTYPE html>
<!--[if IE 9]> <html lang="ko" class="ie9" ng-app="jakdukApp"> <![endif]-->
<!--[if !IE]><!-->
<html lang="ko" ng-app="jakdukApp"> <!--<![endif]-->
	<head>
		<title>
			<spring:message code="stats.attendance.league.title"/> &middot; <spring:message code="stats"/> &middot;
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
						<a href="<c:url value="/stats/attendance/refresh"/>"><spring:message code="stats.attendance"/></a></h1>
					<ul class="pull-right breadcrumb">
						<li class="active"><spring:message code="stats.attendance.breadcrumbs.league"/></li>
						<li>
							<a href="<c:url value="/stats/attendance/club"/>"><spring:message code="stats.attendance.breadcrumbs.club"/></a>
						</li>
						<li>
							<a href="<c:url value="/stats/attendance/season"/>"><spring:message code="stats.attendance.breadcrumbs.season"/></a>
						</li>
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
						<select class="form-control" ng-model="leagueId" ng-change="changeLeague()"
							ng-options='key as value.name disable when value.disable for (key, value) in leagues'>
							<option value=""><spring:message code="stats.select.league"/></option>
						</select>
						<span class="color-blue" ng-class="{'hidden':leagueId}"><spring:message code="stats.msg.choose.league"/></span>
					</div>
				</div>

				<highchart id="chart1" config="chartConfig" class="margin-bottom-10"></highchart>

				<div class="tag-box tag-box-v4 margin-bottom-20">
					<h2>{{chartConfig.title.text}}</h2>
					<p>
						<spring:message code="stats.msg.total.number.of.attendance.alltime.matches" arguments="<strong>{{gamesSum | number:0}}</strong>"/></p>
					<p>
						<spring:message code="stats.msg.total.number.of.attendance.alltime.total" arguments="<strong>{{totalSum | number:0}}</strong>"/></p>
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
			</div>

			<jsp:include page="../include/footer.jsp"/>
		</div>

		<script src="<%=request.getContextPath()%>/bundles/stats.js"></script>
		<script type="text/javascript">
			var jakdukApp = angular.module("jakdukApp", ["highcharts-ng", "ui.bootstrap", 'jakdukCommon']);

			jakdukApp.controller('statsCtrl', function ($scope, $http) {
				var KLCLId = 'KLCL';
				var KLCHId = 'KLCH';
				var KLname = '<spring:message code="stats.attendance.filter.league"/>';
				var KLCLname = '<spring:message code="stats.attendance.filter.league.classic"/>';
				var KLCHname = '<spring:message code="stats.attendance.filter.league.challenge"/>';

				$scope.leagues = {'KLCL': {name: KLCLname}, 'KLCH': {name: KLCHname}};

				$scope.attendancesConn = "none";
				$scope.attendances = {};
				$scope.alert = {};
				$scope.totalSum = 0;
				$scope.gamesSum = 0;

				angular.element(document).ready(function () {
					var league = "${league}";
					var leagueKeys = Object.keys($scope.leagues);

					if (leagueKeys.indexOf(league) >= 0) {
						$scope.leagueId = league;
					} else {
						$scope.leagueId = KLCLId;
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
							text: '<spring:message code="stats.attendance.league.classic.title"/>'
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
								labels: {
									x: 0,
									formatter: function () {
										return Highcharts.numberFormat(this.value, 0);
									}
								},
								title: {
									text: '<spring:message code="stats.attendance.average"/>'
								}
							},
							{ // Games yAxis
								labels: {
									x: 0,
									formatter: function () {
										return Highcharts.numberFormat(this.value, 0);
									}
								},
								title: {
									text: '<spring:message code="stats.attendance.matches"/>'
								}
							},
							{ // Number Of Clubs yAxis
								labels: {
									x: 0,
									formatter: function () {
										return Highcharts.numberFormat(this.value, 0);
									}
								},
								title: {
									text: '<spring:message code="stats.attendance.number.of.clubs"/>'
								},
								opposite: true
							}],
						series: [{
							name: '<spring:message code="stats.attendance.total"/>',
							yAxis: 0,
							type: 'column',
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
								type: 'spline',
								data: [],
								dataLabels: {
									enabled: true,
									format: '{point.y:,.0f} <spring:message code="stats.attendance.people"/>'
								}
							},
							{
								name: '<spring:message code="stats.attendance.matches"/>',
								yAxis: 2,
								visible: false,
								type: 'spline',
								data: [],
								dataLabels: {
									enabled: true,
									format: '{point.y:,.0f} <spring:message code="stats.attendance.match"/>',
								}
							},
							{
								name: '<spring:message code="stats.attendance.number.of.clubs"/>',
								yAxis: 3,
								visible: false,
								type: 'spline',
								data: [],
								dataLabels: {
									enabled: true,
									format: '{point.y:,.0f}',
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
					var bUrl = '<c:url value="/api/stats/attendance/league/' + $scope.leagueId + '"/>';

					if ($scope.attendancesConn == "none") {

						$scope.chartConfig.loading = true;
						var reqPromise = $http.get(bUrl);

						$scope.attendancesConn = "loading";

						reqPromise.success(function (data, status, headers, config) {
							$scope.attendances[$scope.leagueId] = data;

							$scope.chartConfig.loading = false;
							$scope.attendancesConn = "none";
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

					var attendances = $scope.attendances[$scope.leagueId];
					var totalSum = 0;
					var gamesSum = 0;

					attendances.forEach(function (attendance) {
						var itemTotal = [attendance.season, attendance.total];
						var itemAverage = [attendance.season, attendance.average];
						var itemGames = [attendance.season, attendance.games];
						var itemNumberOfClubs = [attendance.season, attendance.numberOfClubs];
						totalSum += attendance.total;
						gamesSum += attendance.games;

						$scope.chartConfig.series[0].data.push(itemTotal);
						$scope.chartConfig.series[1].data.push(itemAverage);
						$scope.chartConfig.series[2].data.push(itemGames);
						$scope.chartConfig.series[3].data.push(itemNumberOfClubs);
					});

					$scope.totalSum = totalSum;
					$scope.gamesSum = gamesSum;

					if ($scope.leagueId == KLCLId) {
						$scope.chartConfig.title.text = '<spring:message code="stats.attendance.league.classic.title"/>';
					} else if ($scope.leagueId == KLCHId) {
						$scope.chartConfig.title.text = '<spring:message code="stats.attendance.league.challenge.title"/>';
					}

					$scope.chartConfig.options.chart.height = 300 + (attendances.length * 30);

				};

				$scope.changeLeague = function () {
					if ($scope.leagueId != null) {
						if (Jakduk.isEmpty($scope.attendances[$scope.leagueId])) {
							$scope.getAttendance();
						} else {
							$scope.refreshData();
						}
					}
				};

				$scope.btnUrlCopy = function () {
					var url = "https://jakduk.com/stats/attendance/league?league=" + $scope.leagueId;

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
					var label = $scope.chartConfig.title.text + '\r<spring:message code="stats"/> · <spring:message code="common.jakduk"/>';
					var url = "https://jakduk.com/stats/attendance/league?league=" + $scope.leagueId;

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