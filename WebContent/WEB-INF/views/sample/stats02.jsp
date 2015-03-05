<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html ng-app="jakdukApp">
<head>
	<jsp:include page="../include/html-header.jsp"/>
		
</head>
<body>
<div class="container" ng-controller="StatsCtrl">
<jsp:include page="../include/navigation-header.jsp"/>

 <div class="span12">
            <input ng-model="chartConfig.title.text">
            <button ng-click="addSeries()">Add Series</button>
            <button ng-click="addPoints()">Add Points to Random Series</button>
            <button ng-click="removeRandomSeries()">Remove Random Series</button>
            <button ng-click="swapChartType()">Line/Bar</button>
            <button ng-click="toggleLoading()">Loading?</button>
        </div>
        <div class="row">
            <highchart id="chart1" config="chartConfig" class="span10"></highchart>
        </div>

</div>
<script src="<%=request.getContextPath()%>/resources/jquery/dist/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/resources/highcharts/highcharts.js"></script>
<script src="<%=request.getContextPath()%>/resources/highcharts-ng/dist/highcharts-ng.min.js"></script>
<script type="text/javascript">
var jakdukApp = angular.module("jakdukApp", ["highcharts-ng"]);

jakdukApp.controller('StatsCtrl', function($scope) {
	
	   $scope.addPoints = function () {
	        var seriesArray = $scope.chartConfig.series
	        var rndIdx = Math.floor(Math.random() * seriesArray.length);
	        seriesArray[rndIdx].data = seriesArray[rndIdx].data.concat([1, 10, 20])
	    };

	    $scope.addSeries = function () {
	        var rnd = []
	        for (var i = 0; i < 10; i++) {
	            rnd.push(Math.floor(Math.random() * 20) + 1)
	        }
	        $scope.chartConfig.series.push({
	            data: rnd
	        })
	    }

	    $scope.removeRandomSeries = function () {
	        var seriesArray = $scope.chartConfig.series
	        var rndIdx = Math.floor(Math.random() * seriesArray.length);
	        seriesArray.splice(rndIdx, 1)
	    }

	    $scope.swapChartType = function () {
	        if (this.chartConfig.options.chart.type === 'line') {
	            this.chartConfig.options.chart.type = 'bar'
	        } else {
	            this.chartConfig.options.chart.type = 'line'
	            this.chartConfig.options.chart.zoomType = 'x'
	        }
	    }

	    $scope.toggleLoading = function () {
	        this.chartConfig.loading = !this.chartConfig.loading
	    }

	    $scope.chartConfig = {
	        options: {
	            chart: {
	                type: 'bar'
	            }
	        },
	        series: [{
	            data: [10, 15, 12, 8, 7]
	        }],
	        title: {
	            text: 'Hello'
	        },

	        loading: false
	    }
	
	
  
});
</script>
<jsp:include page="../include/body-footer.jsp"/>
</body>
</html>