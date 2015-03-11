<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>    
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>    

<!DOCTYPE html>
<html ng-app="jakdukApp">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title><spring:message code="stats"/> &middot; <spring:message code="common.jakduk"/></title>
	
	<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/unify/assets/plugins/cube-portfolio/cubeportfolio/css/cubeportfolio.min.css">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/unify/assets/plugins/cube-portfolio/cubeportfolio/custom/custom-cubeportfolio.css">
	
	<jsp:include page="../include/html-header.jsp"></jsp:include>
</head>

<body>
<div class="wrapper" ng-controller="statsCtrl">
	<jsp:include page="../include/navigation-header.jsp"/>
	
	<!--=== Breadcrumbs ===-->
	<div class="breadcrumbs">
		<div class="container">
			<h1 class="pull-left"><a href="<c:url value="/stats/supporters/refresh"/>"><spring:message code="stats.supporters"/></a></h1>
		</div><!--/container-->
	</div><!--/breadcrumbs-->
	<!--=== End Breadcrumbs ===-->		
	

<div class="cube-portfolio">	
	<div class="content-xs">
		<div id="filters-container" class="cbp-l-filters-text content-xs">
			<div class="cbp-filter-item"
			ng-class="{'cbp-filter-item-active':chartConfig.options.chart.type == 'bar'}" ng-click="changeChartType('bar')"> 
				<spring:message code="stats.chart.bar"/> 
			</div> |
			<div class="cbp-filter-item"
			ng-class="{'cbp-filter-item-active':chartConfig.options.chart.type == 'pie'}" ng-click="changeChartType('pie')"> 
				<spring:message code="stats.chart.pie"/> 
			</div>
		</div><!--/end Filters Container-->
	</div>	
</div>   	
	
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

jakdukApp.controller('statsCtrl', function($scope, $http) {
	$scope.supportersConn = "none";
	$scope.chartSeriesData = [];
	
	angular.element(document).ready(function() {
		
	    $scope.chartConfig = {
		        options: {
		            chart: {
		                type: 'bar',
		                height: 100
		            },
		            tooltip: {
		                pointFormat: '<spring:message code="stats.number.of.supporter"/> : <b>{point.y:1f}</b> <spring:message code="stats.attendance.people"/>'
		            },
		            legend: {
		                layout: 'vertical',
		                align: 'right',
		                verticalAlign: 'top',
		                x: -10,
		                y: 30,
		                floating: true,
		                borderWidth: 1,
		                backgroundColor: ((Highcharts.theme && Highcharts.theme.legendBackgroundColor) || '#FFFFFF'),
		                shadow: true
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
	                    text: '<spring:message code="stats.number"/>',
	                    	align: 'high'
	                }							
	            },                                 
	            series: [{
	                name: '<spring:message code="stats.supporters"/>',
	                data: $scope.chartSeriesData,
	                dataLabels: {
	                    enabled: true,
	                    //rotation: -90,
	                    color: '#FFFFFF',
	                    align: 'right',
	                    format: '{point.name} <b>{point.y:1f}</b>', // one decimal
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
		
		$scope.getSupporters();
	});
	
	$scope.getSupporters = function() {
		var bUrl = '<c:url value="/stats/data/supporters.json"/>';
		
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