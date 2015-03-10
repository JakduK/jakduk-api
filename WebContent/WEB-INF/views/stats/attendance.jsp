<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>    
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%> 
    
<!DOCTYPE html>
<html ng-app="jakdukApp">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title><spring:message code="stats"/> &middot; <spring:message code="common.jakduk"/></title>
	<jsp:include page="../include/html-header.jsp"></jsp:include>
</head>

<div class="wrapper" ng-controller="statsCtrl">
	<jsp:include page="../include/navigation-header.jsp"/>
	
		<!--=== Breadcrumbs ===-->
	<div class="breadcrumbs">
		<div class="container">
			<h1 class="pull-left"><spring:message code="stats.attendance"/></h1>
<ul class="pull-right breadcrumb">
                <li ><a href="" ng-click="changeChartType('bar')"><spring:message code="stats.chart.bar"/></a></li>
                <li ><a href="" ng-click="changeChartType('pie')"><spring:message code="stats.chart.pie"/></a></li>
            </ul>			
		</div><!--/container-->
	</div><!--/breadcrumbs-->
	<!--=== End Breadcrumbs ===-->
	
	<!--=== Content Part ===-->
	<div class="container content">	
		<highchart id="chart1" config="chartConfig" class="span10"></highchart>
	</div>
</div>
<!-- Bootstrap core JavaScript
  ================================================== -->
<!-- Placed at the end of the document so the pages load faster -->
<script src="<%=request.getContextPath()%>/resources/jquery/dist/jquery.min.js"></script>

<script src="<%=request.getContextPath()%>/resources/highcharts/highcharts.src.js"></script>
<script src="<%=request.getContextPath()%>/resources/highcharts-ng/dist/highcharts-ng.min.js"></script>

<script type="text/javascript">
var jakdukApp = angular.module("jakdukApp", ["highcharts-ng"]);

jakdukApp.controller('statsCtrl', function($scope, $http, $filter) {
	$scope.attendancesConn = "none";
	$scope.chartSeriesData = [];	
	
	angular.element(document).ready(function() {
		Highcharts.setOptions({
		    lang: {
		        thousandsSep: ','
		    }
		});
		
		$scope.getAttendance();
		
		   $scope.chartConfig = {
			        options: {
			            chart: {
			                type: 'bar',
			                height: 100
			            },
			            tooltip: {
			            	//pointFormat: '<spring:message code="stats.number.of.supporter"/>: <b>{point.y:1f}</b>'
			            	pointFormat: 'Value: {point.y:,.0f}'
			            },
			            legend: {
			                enabled: false
			            }
			        },
			        
			        title: {
			            text: '<spring:message code="stats.attendance.title"/>'
			        },	        
			        subtitle: {
		                text: 'Source: K LEAGUE'
		            },
		            xAxis: {
		                type: 'category',
		                labels: {
		                    style: {
		                        fontSize: '13px',
		                        fontFamily: 'Verdana, sans-serif'
		                    }
		                }          
		            },
		            yAxis: {
		                min: 0,
		                title: {
		                    text: '<spring:message code="stats.number"/>',
		                    align: 'high'
		                },
						labels:{
							x: 0,
							y: 24,
							rotation: -45,
							formatter: function() {
								return Highcharts.numberFormat(this.value,0);
							}					
						},                
		            },                                 
		            series: [{
		                name: 'Attendance',
		                data: $scope.chartSeriesData,
		                dataLabels: {
		                    enabled: true,
		                    //rotation: -90,
		                    color: '#FFFFFF',
		                    align: 'right',
		                    format: '<b>{point.name}</b> {point.y:,.0f}', // one decimal    
		                    //y: 10, // 10 pixels down from the top
		                    style: {
		                        fontSize: '13px',
		                        fontFamily: 'Verdana, sans-serif'
		                    }
		                }
		            }],

			        loading: false,
			        credits:{enabled:true}
			    };					
	});
	
	$scope.getAttendance = function() {
		var bUrl = '<c:url value="/stats/data/attendance/league.json"/>';
		
		if ($scope.attendancesConn == "none") {
			
			var reqPromise = $http.get(bUrl);
			
			$scope.attendancesConn = "loading";
			
			reqPromise.success(function(data, status, headers, config) {
				
				var attendances = data.attendances;
				$scope.chartConfig.options.chart.height = 150 + (attendances.length * 30);
				
				attendances.forEach(function(attendance) {
					var item = [attendance.season, attendance.total];
					$scope.chartSeriesData.push(item);
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