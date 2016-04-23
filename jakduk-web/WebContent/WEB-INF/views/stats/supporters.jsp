<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!DOCTYPE html>
<!--[if IE 9]> <html lang="ko" class="ie9" ng-app="jakdukApp"> <![endif]-->
<!--[if !IE]><!-->
<html lang="ko" ng-app="jakdukApp"> <!--<![endif]-->
	<head>
		<title>
			<spring:message code="stats.supporters"/> &middot; <spring:message code="stats"/> &middot;
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
						<a href="<c:url value="/stats/supporters/refresh"/>"><spring:message code="stats.supporters"/></a></h1>
				</div><!--/container-->
			</div><!--/breadcrumbs-->
			<!--=== End Breadcrumbs ===-->

			<!--=== Content Part ===-->
			<div class="container content">

				<div class="cube-portfolio">
					<div id="filters-container" class="cbp-l-filters-text content-xs">
						<div class="cbp-filter-item"
							ng-class="{'cbp-filter-item-active':chartConfig.options.chart.type == 'bar'}" ng-click="changeChartType('bar')">
							<spring:message code="stats.chart.bar"/>
						</div>
						|
						<div class="cbp-filter-item"
							ng-class="{'cbp-filter-item-active':chartConfig.options.chart.type == 'pie'}" ng-click="changeChartType('pie')">
							<spring:message code="stats.chart.pie"/>
						</div>
					</div><!--/end Filters Container-->
				</div>

				<highchart id="chart1" config="chartConfig" class="margin-bottom-10"></highchart>

				<div class="tag-box tag-box-v4 margin-bottom-20">
					<h2>{{chartConfig.title.text}}</h2>
					<p><spring:message code="stats.msg.total.number.of.members" arguments="<strong>{{usersTotal}}</strong>"/></p>
					<p>
						<spring:message code="stats.msg.total.number.of.supporters" arguments="<strong>{{supportersTotal}}</strong>"/></p>
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
				$scope.supportersConn = "none";
				$scope.chartSeriesData = [];
				$scope.supportersTotal = 0;
				$scope.usersTotal = 0;

				angular.element(document).ready(function () {
					var chartType = "${chartType}";

					if (chartType != "pie") {
						chartType = "bar";
					}

					$scope.chartConfig = {
						options: {
							chart: {
								type: chartType,
								height: 100
							},
							tooltip: {
								//pointFormat: '<spring:message code="stats.number.of.supporter"/> : <b>{point.y:1f}</b> <spring:message code="stats.attendance.people"/>'
							},
							plotOptions: {
								pie: {
									allowPointSelect: true,
									cursor: 'pointer'
								}
							}
						},
						title: {
							text: '<spring:message code="stats.supporters.title"/>'
						},
						subtitle: {
							text: 'Source: https://jakduk.com'
						},
						xAxis: {
							type: 'category',
							labels: {
								style: {
									fontSize: '13px'
								}
							}
						},
						yAxis: {
							min: 0,
							title: {
								text: '<spring:message code="stats.number.of.supporter"/>'
							}
						},
						series: [{
							name: '<spring:message code="stats.number.of.supporter"/>',
							data: $scope.chartSeriesData,
							dataLabels: {
								enabled: true,
								align: 'right',
								format: '{point.name} <b>{point.y:1f}</b>',
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

					$scope.getSupporters();

					App.init();
				});

				$scope.getSupporters = function () {
					var bUrl = '<c:url value="/stats/data/supporters.json"/>';

					if ($scope.supportersConn == "none") {

						$scope.chartConfig.loading = true;
						var reqPromise = $http.get(bUrl);

						$scope.supportersConn = "loading";

						reqPromise.success(function (data, status, headers, config) {

							$scope.chartConfig.loading = false;
							$scope.supportersTotal = data.supportersTotal;
							$scope.usersTotal = data.usersTotal;
							var supporters = data.supporters;
							$scope.chartConfig.options.chart.height = 200 + (supporters.length * 30);

							supporters.forEach(function (supporter) {
								var item = [supporter.supportFC.names[0].shortName, supporter.count];
								$scope.chartSeriesData.push(item);
							});

							$scope.supportersConn = "none";
						});
						reqPromise.error(function (data, status, headers, config) {
							$scope.supportersConn = "none";
							$scope.error = '<spring:message code="common.msg.error.network.unstable"/>';
						});
					}
				};

				$scope.changeChartType = function (chartName) {
					$scope.chartConfig.options.chart.type = chartName;
				};

				$scope.btnUrlCopy = function () {
					var url = "https://jakduk.com/stats/supporters?chartType=" + $scope.chartConfig.options.chart.type;

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
					var label = '<spring:message code="stats.supporters.title"/>\r<spring:message code="stats"/> · <spring:message code="common.jakduk"/>';
					var url = "https://jakduk.com/stats/supporters?chartType=" + $scope.chartConfig.options.chart.type;

					Kakao.Link.sendTalkLink({
						label: label,
						webLink: {
							text: url,
							url: url
						}
					});
				};
			});
		</script>
	</body>
</html>