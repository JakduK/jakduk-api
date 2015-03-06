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
			<h1 class="pull-left"><spring:message code="stats.number.of.supporter"/></h1>
<ul class="pull-right breadcrumb">
                <li ><a href="" ng-click="changeChartType('bar')"><spring:message code="stats.chart.bar"/></a></li>
                <li ><a href="" ng-click="changeChartType('pie')"><spring:message code="stats.chart.pie"/></a></li>
            </ul>			
		</div><!--/container-->
	</div><!--/breadcrumbs-->
	<!--=== End Breadcrumbs ===-->		
	
	<!--=== Content Part ===-->
	<div class="container content">
	<!-- 	
	<div google-chart chart=chartObject></div>
	 -->
	 <highchart id="chart1" config="chartConfig" class="span10"></highchart>
	</div>
</div>

<!-- Bootstrap core JavaScript
  ================================================== -->
<!-- Placed at the end of the document so the pages load faster -->
<script src="<%=request.getContextPath()%>/resources/jquery/dist/jquery.min.js"></script>

<script src="<%=request.getContextPath()%>/resources/highcharts/highcharts.js"></script>
<script src="<%=request.getContextPath()%>/resources/highcharts-ng/dist/highcharts-ng.min.js"></script>

<script type="text/javascript">
var jakdukApp = angular.module("jakdukApp", ["highcharts-ng"]);

jakdukApp.controller('statsCtrl', function($scope, $http) {
	$scope.supportersConn = "none";
	$scope.chartType = "BarChart";
	
	angular.element(document).ready(function() {
		$scope.getSupporters();
	});
	
	$scope.chartSeriesData = [];
	
    $scope.chartConfig = {
	        options: {
	            chart: {
	                type: 'bar',
	                height: 100
	            },
	            tooltip: {
	                pointFormat: 'Supports: <b>{point.y:1f} millions</b>'
	            },
	            legend: {
	                enabled: false
	            }
	        },
	        title: {
	            text: '<spring:message code="stats.number.of.each.club.supporter"/>'
	        },	        
	        subtitle: {
                text: 'Source: K LEAGUE JAKDU KING'
            },
            xAxis: {
                type: 'category',
                labels: {
                    //rotation: -45,
                    style: {
                        fontSize: '13px',
                        fontFamily: 'Verdana, sans-serif'
                    }
                }
            },
            yAxis: {
                min: 0,
                title: {
                    text: '<spring:message code="stats.number"/>'
                }
            },                                 
            series: [{
                name: 'Supporters',
                data: $scope.chartSeriesData,
                dataLabels: {
                    enabled: true,
                    //rotation: -90,
                    color: '#FFFFFF',
                    align: 'right',
                    format: '{point.y:1f}', // one decimal
                    //y: 10, // 10 pixels down from the top
                    style: {
                        fontSize: '13px',
                        fontFamily: 'Verdana, sans-serif'
                    }
                }
            }],

	        loading: false,
	        credits:{enabled:true}
	    }	

	/*
    $scope.chartObject.data = {"cols": [
        {id: "t", label: "Topping", type: "string"},
        {id: "s", label: '<spring:message code="stats.number"/>', type: "number"}
    ], "rows": []};

    // $routeParams.chartType == BarChart or PieChart or ColumnChart...
	$scope.chartObject.type = "BarChart";
	$scope.chartObject.options = {
		'title': '<spring:message code="stats.number.of.each.club.supporter"/>',
    };
	*/
	
	$scope.getSupporters = function() {
		var bUrl = '<c:url value="/stats/data/supporter.json"/>';
		
		if ($scope.supportersConn == "none") {
			
			var reqPromise = $http.get(bUrl);
			
			$scope.supportersConn = "loading";
			
			reqPromise.success(function(data, status, headers, config) {
				
				var supporters = data.supporters;
				$scope.chartConfig.options.chart.height = 150 + (supporters.length * 30);
				
				supporters.forEach(function(supporter) {
					console.log(supporter);
					//var item = {c:[{v:supporter.supportFC.names[0].shortName}, {v:supporter.count}]};
					var item = [supporter.supportFC.names[0].shortName, supporter.count];
					$scope.chartSeriesData.push(item);
				});
				
				$scope.supportersConn = "none";
				
			});
			reqPromise.error(function(data, status, headers, config) {
				$scope.supportersConn = "none";
				$scope.error = '<spring:message code="common.msg.error.network.unstable"/>';
			});
		}
	};
	
	$scope.changeChartType = function(chartName) {
		$scope.chartConfig.options.chart.type = chartName;
	};
	
});
</script>

<jsp:include page="../include/body-footer.jsp"/>
</body>
</html>