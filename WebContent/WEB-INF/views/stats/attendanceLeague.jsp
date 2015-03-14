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
			<h1 class="pull-left"><a href="<c:url value="/stats/attendance/league/refresh"/>"><spring:message code="stats.attendance"/></a></h1>
				<ul class="pull-right breadcrumb">
	      <li ><a href="<c:url value="/stats/attendance/league"/>"><spring:message code="stats.attendance.breadcrumbs.league"/></a></li>
	      <li ><a href=""><spring:message code="stats.attendance.breadcrumbs.club"/></a></li>
      </ul>			
		</div><!--/container-->
	</div><!--/breadcrumbs-->
	<!--=== End Breadcrumbs ===-->
	
	<!--=== Content Part ===-->
	<div class="container content">
	
<div class="cube-portfolio">	
		<div id="filters-container" class="cbp-l-filters-text content-xs">
			<div class="cbp-filter-item"
			ng-class="{'cbp-filter-item-active':chartConfig.options.chart.type == 'bar'}" ng-click="changeChartType('bar')"> 
				<spring:message code="stats.attendance.filter.league"/> 
			</div> |
			<div class="cbp-filter-item"
			ng-class="{'cbp-filter-item-active':chartConfig.options.chart.type == 'pie'}" ng-click="changeChartType('pie')"> 
				<spring:message code="stats.attendance.filter.league.classic"/> 
			</div>
			<div class="cbp-filter-item"
			ng-class="{'cbp-filter-item-active':chartConfig.options.chart.type == 'pie'}" ng-click="changeChartType('pie')"> 
				<spring:message code="stats.attendance.filter.league.challenge"/> 
			</div>			
		</div><!--/end Filters Container-->
</div>  	
	
		<highchart id="chart1" config="chartConfig" class="margin-bottom-10"></highchart>
		
		<div class="tag-box tag-box-v4 margin-bottom-20">
			<p><spring:message code="stats.msg.total.number.of.attendance.total" arguments="<strong>{{totalSum | number:0}}</strong>"/></p>
			<p><spring:message code="stats.msg.total.number.of.attendance.games" arguments="<strong>{{gamesSum | number:0}}</strong>"/></p>	
		</div>
		
		<div class="text-right">
		<button class="btn-u btn-brd rounded" type="button" ng-click="btnUrlCopy()">
			<spring:message code="common.button.copy.url.to.clipboard"/>
		</button>
		    <a id="kakao-link-btn" href="javascript:;">
		      <img src="<%=request.getContextPath()%>/resources/kakao/icon/kakaolink_btn_small.png" />
		    </a>
		</div>		
	</div>
	
	<jsp:include page="../include/footer.jsp"/>	
</div>

<!-- Bootstrap core JavaScript
  ================================================== -->
<!-- Placed at the end of the document so the pages load faster -->
<script src="<%=request.getContextPath()%>/resources/jquery/dist/jquery.min.js"></script>

<script src="<%=request.getContextPath()%>/resources/kakao/js/kakao.min.js"></script>

<script src="<%=request.getContextPath()%>/resources/highcharts/highcharts.js"></script>
<script src="<%=request.getContextPath()%>/resources/highcharts/modules/exporting.js"></script>
<script src="<%=request.getContextPath()%>/resources/highcharts-ng/dist/highcharts-ng.min.js"></script>

<script type="text/javascript">
var jakdukApp = angular.module("jakdukApp", ["highcharts-ng"]);

jakdukApp.controller('statsCtrl', function($scope, $http, $filter) {
	$scope.attendancesConn = "none";
	$scope.seriesTotal = [];
	$scope.seriesAverage = [];	
	$scope.seriesGames = [];
	$scope.totalSum = 0;
	$scope.gamesSum = 0;
	
	angular.element(document).ready(function() {
		$scope.getAttendance();
		
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
				text: '<spring:message code="stats.attendance.league.title"/>'
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
				}          
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
					text: '<spring:message code="stats.attendance.games"/>'
				}
			}],                                 
			series: [{
				name: '<spring:message code="stats.attendance.total"/>',
				yAxis: 0,
				type: 'column',
				data: $scope.seriesTotal,
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
      data: $scope.seriesAverage,          
				dataLabels: {
					enabled: true,
					format: '{point.y:,.0f} <spring:message code="stats.attendance.people"/>'
				}
			},
			{
				name: '<spring:message code="stats.attendance.games"/>',
				yAxis: 2,
				visible: false,
				type: 'spline',
				data: $scope.seriesGames,
				dataLabels: {
					enabled: true,
					format: '{point.y:,.0f} <spring:message code="stats.attendance.game"/>',
				}
			}],
    loading: true,
    credits:{enabled:true}
		};		

		 // 사용할 앱의 Javascript 키를 설정해 주세요.
		Kakao.init('${kakaoKey}');
		
		// 카카오톡 링크 버튼을 생성합니다. 처음 한번만 호출하면 됩니다.
		Kakao.Link.createTalkLinkButton({
			container: '#kakao-link-btn',
			label: '<spring:message code="stats.attendance.league.title"/>\r<spring:message code="stats"/> · <spring:message code="common.jakduk"/>',
			webLink: {
				text: "https://jakduk.com/stats/supporters",
				url: "https://jakduk.com/stats/supporters"	    	  
			}
		});	   		   
	});
	
	$scope.getAttendance = function() {
		var bUrl = '<c:url value="/stats/data/attendance/league.json"/>';
		
		if ($scope.attendancesConn == "none") {
			
			var reqPromise = $http.get(bUrl);
			
			$scope.attendancesConn = "loading";
			
			reqPromise.success(function(data, status, headers, config) {
				
				$scope.chartConfig.loading = false;
				$scope.totalSum = data.totalSum;
				$scope.gamesSum = data.gamesSum;
				var attendances = data.attendances;
				$scope.chartConfig.options.chart.height = 300 + (attendances.length * 30);
				
				attendances.forEach(function(attendance) {
					var itemTotal = [attendance.season, attendance.total];
					var itemAverage = [attendance.season, attendance.average];
					var itemGames = [attendance.season, attendance.games];
					
					$scope.seriesTotal.push(itemTotal);
					$scope.seriesAverage.push(itemAverage);
					$scope.seriesGames.push(itemGames);
				});
				
				$scope.attendancesConn = "none";				
			});
			
			reqPromise.error(function(data, status, headers, config) {
				$scope.attendancesConn = "none";
				$scope.error = '<spring:message code="common.msg.error.network.unstable"/>';
			});
		}
	};
	
	$scope.btnUrlCopy = function() {
		
		var url = "https://jakduk.com/stats/attendance/league";
		
		if(window.clipboardData){
		    // IE처리
		    // 클립보드에 문자열 복사
		    window.clipboardData.setData('text', url);
		    
		    // 클립보드의 내용 가져오기
		    // window.clipboardData.getData('Text');
		 
		    // 클립보드의 내용 지우기
		    // window.clipboardData.clearData("Text");
		  }  else {                     
		    // 비IE 처리    
		    window.prompt ('<spring:message code="common.msg.copy.to.clipboard"/>', url);  
		  }
	}	
	
});
</script>

<jsp:include page="../include/body-footer.jsp"/>

</body>
</html>