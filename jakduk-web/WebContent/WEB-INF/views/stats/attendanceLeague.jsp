<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>    
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%> 
    
<!DOCTYPE html>
<html ng-app="jakdukApp">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title><spring:message code="stats.attendance.league.title"/> - <spring:message code="stats"/> &middot; <spring:message code="common.jakduk"/></title>
	
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
			<h1 class="pull-left"><a href="<c:url value="/stats/attendance/refresh"/>"><spring:message code="stats.attendance"/></a></h1>
				<ul class="pull-right breadcrumb">
			      <li><a href="<c:url value="/stats/attendance/club"/>"><spring:message code="stats.attendance.breadcrumbs.club"/></a></li>
			      <li class="active"><spring:message code="stats.attendance.breadcrumbs.league"/></li>
      			</ul>			
		</div><!--/container-->
	</div><!--/breadcrumbs-->
	<!--=== End Breadcrumbs ===-->
	
	<!--=== Content Part ===-->
	<div class="container content">
	
<div class="cube-portfolio">	
		<div id="filters-container" class="cbp-l-filters-text content-xs">
			<div class="cbp-filter-item"
			ng-class="{'cbp-filter-item-active':league == 'KL'}" ng-click="changeLeague('KL')"> 
				<spring:message code="stats.attendance.filter.league"/> 
			</div> |
			<div class="cbp-filter-item"
			ng-class="{'cbp-filter-item-active':league == 'KLCL'}" ng-click="changeLeague('KLCL')"> 
				<spring:message code="stats.attendance.filter.league.classic"/> 
			</div> |
			<div class="cbp-filter-item"
			ng-class="{'cbp-filter-item-active':league == 'KLCH'}" ng-click="changeLeague('KLCH')"> 
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
			<button class="btn-u btn-brd rounded btn-u-xs" type="button" ng-click="btnUrlCopy()">
				<spring:message code="common.button.copy.url.to.clipboard"/>
			</button>
		    <a id="kakao-link-btn" href="" ng-click="sendLink()">
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
	$scope.league = "KL";
	$scope.totalSum = 0;
	$scope.gamesSum = 0;
	
	angular.element(document).ready(function() {
		
		var league = "${league}";
		
		if (league == "KLCL" || league == "KLCH") {			
			$scope.league = league;
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
					text: '<spring:message code="stats.attendance.games"/>'
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
				name: '<spring:message code="stats.attendance.games"/>',
				yAxis: 2,
				visible: false,
				type: 'spline',
				data: [],
				dataLabels: {
					enabled: true,
					format: '{point.y:,.0f} <spring:message code="stats.attendance.game"/>',
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
    credits:{enabled:true}
		};		

		 // 사용할 앱의 Javascript 키를 설정해 주세요.
		Kakao.init('${kakaoKey}');
		
		$scope.getAttendance();
		
		App.init();
	});
	
	$scope.getAttendance = function() {
		var bUrl = '<c:url value="/stats/data/attendance/league.json?league=' + $scope.league + '"/>';
		
		if ($scope.attendancesConn == "none") {
			
			$scope.chartConfig.loading = true;
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
					var itemNumberOfClubs = [attendance.season, attendance.numberOfClubs];
					
					$scope.chartConfig.series[0].data.push(itemTotal);
					$scope.chartConfig.series[1].data.push(itemAverage);
					$scope.chartConfig.series[2].data.push(itemGames);
					$scope.chartConfig.series[3].data.push(itemNumberOfClubs);
				});
				
				$scope.attendancesConn = "none";				
			});
			
			reqPromise.error(function(data, status, headers, config) {
				$scope.attendancesConn = "none";
				$scope.error = '<spring:message code="common.msg.error.network.unstable"/>';
			});
		}
	};
	
	$scope.changeLeague = function(league) {
		
		if ($scope.league != league) {
			$scope.chartConfig.series.forEach(function(series) {
				series.data = [];
			}) ;
			
			if (league == "KL") {
				$scope.chartConfig.title.text = '<spring:message code="stats.attendance.league.title"/>';			
			} else if (league == "KLCL") {
				$scope.chartConfig.title.text = '<spring:message code="stats.attendance.league.classic.title"/>';			
			} else if (league == "KLCH") {
				$scope.chartConfig.title.text = '<spring:message code="stats.attendance.league.challenge.title"/>';
			}
			
			$scope.league = league;
			$scope.getAttendance();			
		}
	};	
	
	$scope.btnUrlCopy = function() {
		var url = "https://jakduk.com/stats/attendance/league?league=" + $scope.league;
		
		if (window.clipboardData){
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
	};
	
	$scope.sendLink = function() {
		var label = $scope.chartConfig.title.text + '\r<spring:message code="stats"/> · <spring:message code="common.jakduk"/>';
		var url = "https://jakduk.com/stats/attendance/league?league=" + $scope.league;
		
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

<jsp:include page="../include/body-footer.jsp"/>

</body>
</html>