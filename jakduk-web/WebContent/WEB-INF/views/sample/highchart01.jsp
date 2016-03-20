<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html ng-app="jakdukApp">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Highchart Test 01 &middot; <spring:message code="common.jakduk"/></title>
	<jsp:include page="../include/html-header.jsp"/>
</head>
<body>
<div class="wrapper" ng-controller="sampleCtrl">
	<jsp:include page="../include/navigation-header.jsp"/>
	
	<!--=== Content Part ===-->
	<div class="container content">
	
		<highchart id="chart1" config="chartConfig" class="margin-bottom-10"></highchart>
	
	</div>

	<jsp:include page="../include/footer.jsp"/>	
</div>

<script src="<%=request.getContextPath()%>/resources/jquery/dist/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/resources/highcharts/highcharts.js"></script>
<script src="<%=request.getContextPath()%>/resources/highcharts-ng/dist/highcharts-ng.min.js"></script>
<script type="text/javascript">
var jakdukApp = angular.module("jakdukApp", ['highcharts-ng']);

jakdukApp.controller('sampleCtrl', function($scope, $http) {
	$scope.attendancesConn = "none";
	$scope.league = "KL";
	
	angular.element(document).ready(function() {
		
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
					labels:{
						x: 0,
						rotation: -30,
						formatter: function() {
							return Highcharts.numberFormat(this.value,0);
						}					
					},
				}, 
				{ // Average yAxis
					labels: {
						x: 0,						                	
						formatter: function() {
							return Highcharts.numberFormat(this.value,0);
						}
					},
					title: {
						text: '<spring:message code="stats.attendance.average"/>'
					},
					opposite: true
				},
				{ // Games yAxis
					labels: {
						x: 0,						                	
						formatter: function() {
							return Highcharts.numberFormat(this.value,0);
						}
				 	},
					title: {
						text: '<spring:message code="stats.attendance.matches"/>'
					}								
				},                 
				{ // Number Of Clubs yAxis
					labels: {
						x: 0,						                	
						formatter: function() {
							return Highcharts.numberFormat(this.value,0);
						}
				 	},
					title: {
						text: '<spring:message code="stats.attendance.number.of.clubs"/>'
					},
					opposite: true				
				}],       			
				series: [{
					name: '<spring:message code="stats.attendance.total"/>',
					xAxis: 0,
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
				}],
				
	    loading: true,
	    credits:{enabled:true}
			};
			
	    
		$scope.getAttendance();
	    
	    App.init();
	    
	    //console.log($scope.chartConfig);
		
	});
	
	$scope.getAttendance = function() {
		var bUrl = '<c:url value="/stats/data/attendance/season.json?season=2012' + '&league=' + $scope.league + '"/>';
		
		if ($scope.attendancesConn == "none") {
			
			$scope.chartConfig.loading = true;
			var reqPromise = $http.get(bUrl);
			
			$scope.attendancesConn = "loading";
			
			reqPromise.success(function(data, status, headers, config) {
				
				console.log(data);
				
				$scope.chartConfig.loading = false;
				var attendances = data.attendances;
				$scope.chartConfig.options.chart.height = 300 + (attendances.length * 30);
				
				attendances.forEach(function(attendance) {
					//console.log(attendance);
					var itemTotal = [attendance.club.name, attendance.total];
					var itemAverage = [attendance.club.name, attendance.average];
					
					$scope.chartConfig.series[0].data.push(itemTotal);					
					$scope.chartConfig.series[1].data.push(itemAverage);
				});
				
				$scope.attendancesConn = "none";				
			});
			
			reqPromise.error(function(data, status, headers, config) {
				$scope.attendancesConn = "none";
				$scope.error = '<spring:message code="common.msg.error.network.unstable"/>';
			});
		}
	};
});
</script>

<jsp:include page="../include/body-footer.jsp"/>
</body>
</html>