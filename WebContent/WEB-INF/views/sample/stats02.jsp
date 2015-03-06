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
	    
/*	    
	    $(function () {
	        $('#container').highcharts({
	            chart: {
	                type: 'column'
	            },
	            title: {
	                text: 'World\'s largest cities per 2014'
	            },
	            subtitle: {
	                text: 'Source: <a href="http://en.wikipedia.org/wiki/List_of_cities_proper_by_population">Wikipedia</a>'
	            },
	            xAxis: {
	                type: 'category',
	                labels: {
	                    rotation: -45,
	                    style: {
	                        fontSize: '13px',
	                        fontFamily: 'Verdana, sans-serif'
	                    }
	                }
	            },
	            yAxis: {
	                min: 0,
	                title: {
	                    text: 'Population (millions)'
	                }
	            },
	            legend: {
	                enabled: false
	            },
	            tooltip: {
	                pointFormat: 'Population in 2008: <b>{point.y:.1f} millions</b>'
	            },
	            series: [{
	                name: 'Population',
	                data: [
	                    ['Shanghai', 23.7],
	                    ['Lagos', 16.1],
	                    ['Instanbul', 14.2],
	                    ['Karachi', 14.0],
	                    ['Mumbai', 12.5],
	                    ['Moscow', 12.1],
	                    ['São Paulo', 11.8],
	                    ['Beijing', 11.7],
	                    ['Guangzhou', 11.1],
	                    ['Delhi', 11.1],
	                    ['Shenzhen', 10.5],
	                    ['Seoul', 10.4],
	                    ['Jakarta', 10.0],
	                    ['Kinshasa', 9.3],
	                    ['Tianjin', 9.3],
	                    ['Tokyo', 9.0],
	                    ['Cairo', 8.9],
	                    ['Dhaka', 8.9],
	                    ['Mexico City', 8.9],
	                    ['Lima', 8.9]
	                ],
	                dataLabels: {
	                    enabled: true,
	                    rotation: -90,
	                    color: '#FFFFFF',
	                    align: 'right',
	                    format: '{point.y:.1f}', // one decimal
	                    y: 10, // 10 pixels down from the top
	                    style: {
	                        fontSize: '13px',
	                        fontFamily: 'Verdana, sans-serif'
	                    }
	                }
	            }]
	        });
	    }); 
*/	    
	    

	    $scope.chartConfig = {
	        options: {
	            chart: {
	                type: 'bar',
	                height: 700
	            }
	        },
	        title: {
	            text: 'Hello'
	        },	        
	        subtitle: {
                text: 'Source: <a href="http://en.wikipedia.org/wiki/List_of_cities_proper_by_population">Wikipedia</a>'
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
                    text: 'Population (millions)'
                }
            },
            legend: {
                enabled: false
            },
            tooltip: {
                pointFormat: 'Population in 2008: <b>{point.y:.1f} millions</b>'
            },            
            series: [{
                name: 'Population',
                data: [
                    ['Shanghai', 23.7],
                    ['Lagos', 16.1],
                    ['Instanbul', 14.2],
                    ['Karachi', 14.0],
                    ['Mumbai', 12.5],
                    ['Moscow', 12.1],
                    ['São Paulo', 11.8],
                    ['Beijing', 11.7],
                    ['Guangzhou', 11.1],
                    ['Delhi', 11.1],
                    ['Shenzhen', 10.5],
                    ['Seoul', 10.4],
                    ['Jakarta', 10.0],
                    ['Kinshasa', 9.3],
                    ['Tianjin', 9.3],
                    ['Tokyo', 9.0],
                    ['Cairo', 8.9],
                    ['Dhaka', 8.9],
                    ['Mexico City', 8.9],
                    ['Lima', 8.9]
                ],
                dataLabels: {
                    enabled: true,
                    //rotation: -90,
                    color: '#FFFFFF',
                    align: 'right',
                    //format: '{point.y:.1f}', // one decimal
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
	
	
  
});
</script>
<jsp:include page="../include/body-footer.jsp"/>
</body>
</html>