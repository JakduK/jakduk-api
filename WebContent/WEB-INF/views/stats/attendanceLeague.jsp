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

<body>
<div class="wrapper" ng-controller="statsCtrl">
	<jsp:include page="../include/navigation-header.jsp"/>
	
		<!--=== Breadcrumbs ===-->
	<div class="breadcrumbs">
		<div class="container">
			<h1 class="pull-left"><a href="<c:url value="/stats/attendance/league/refresh"/>"><spring:message code="stats.attendance"/></a></h1>
<ul class="pull-right breadcrumb">
                <li ><a href="<c:url value="/stats/attendance/league"/>"><spring:message code="stats.attendance.league.breadcrumbs"/></a></li>
                <li ><a href=""><spring:message code="stats.attendance.club.breadcrumbs"/></a></li>
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

<script src="<%=request.getContextPath()%>/resources/highcharts/highcharts.js"></script>
<script src="<%=request.getContextPath()%>/resources/highcharts/modules/exporting.js"></script>
<script src="<%=request.getContextPath()%>/resources/highcharts-ng/dist/highcharts-ng.min.js"></script>

<script type="text/javascript">
var jakdukApp = angular.module("jakdukApp", ["highcharts-ng"]);

jakdukApp.controller('statsCtrl', function($scope, $http, $filter) {
	$scope.attendancesConn = "none";
	$scope.seriesTotal = [];
	$scope.seriesAverage = [];	
	
	angular.element(document).ready(function() {
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
		            	//pointFormat: '<spring:message code="stats.number.of.supporter"/>: <b>{point.y:1f}</b>'
		            	//pointFormat: '<spring:message code="stats.attendance.total"/> : <b>{point.y:,.0f}</b> <spring:message code="stats.attendance.people"/>',
		            	shared: true
		            }
		        },
		        
		        title: {
		            text: '<spring:message code="stats.attendance.league.title"/>'
		        },	        
		        subtitle: {
	                text: 'Source: http://www.kleague.com'
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
	            yAxis: [{
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
	            { // Primary yAxis
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
	            }
	            ],                                 
	            series: [{
	                name: '<spring:message code="stats.attendance.total"/>',	    
	                type: 'bar',
	                data: $scope.seriesTotal,
	                dataLabels: {
	                    enabled: true,
	                    //rotation: -90,
	                    color: '#FFFFFF',
	                    align: 'right',
	                    format: '{point.y:,.0f} <spring:message code="stats.attendance.people"/>', // one decimal    
	                    //y: 10, // 10 pixels down from the top
	                    style: {
	                        fontSize: '13px',
	                        fontFamily: 'Verdana, sans-serif'
	                    }
	                }
	            },
	            {
	                name: '<spring:message code="stats.attendance.average"/>',	
	                yAxis: 1,	                
	                type: 'spline',
	                data: $scope.seriesAverage,
	                color: Highcharts.getOptions().colors[3],
	                marker: {
	                    lineWidth: 2,
	                    lineColor: Highcharts.getOptions().colors[3],
	                    fillColor: 'white'
	                },	                
	                dataLabels: {
	                    enabled: true,
	                    align: 'right',
	                    format: '{point.y:,.0f} <spring:message code="stats.attendance.people"/>'
	                }
	            }
	            ],

		        loading: false,
		        credits:{enabled:true}
		    };		
		   
		   $scope.getAttendance();		   
	});
	
	$scope.getAttendance = function() {
		var bUrl = '<c:url value="/stats/data/attendance/league.json"/>';
		
		if ($scope.attendancesConn == "none") {
			
			var reqPromise = $http.get(bUrl);
			
			$scope.attendancesConn = "loading";
			
			reqPromise.success(function(data, status, headers, config) {
				
				var attendances = data.attendances;
				$scope.chartConfig.options.chart.height = 300 + (attendances.length * 30);
				
				attendances.forEach(function(attendance) {
					var itemTotal = [attendance.season, attendance.total];
					var itemAverage = [attendance.season, attendance.average];
					
					$scope.seriesTotal.push(itemTotal);
					$scope.seriesAverage.push(itemAverage);
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