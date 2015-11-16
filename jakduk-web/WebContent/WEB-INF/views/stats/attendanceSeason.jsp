<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>    
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%> 
    
<!DOCTYPE html>
<html ng-app="jakdukApp">    
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title><spring:message code="stats.attendance.season.title"/> &middot; <spring:message code="stats"/> &middot; <spring:message code="common.jakduk"/></title>
	
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
			      <li><a href="<c:url value="/stats/attendance/league"/>"><spring:message code="stats.attendance.breadcrumbs.league"/></a></li>
			      <li><a href="<c:url value="/stats/attendance/club"/>"><spring:message code="stats.attendance.breadcrumbs.club"/></a></li>
			      <li class="active"><spring:message code="stats.attendance.breadcrumbs.season"/></li>
      			</ul>			
		</div><!--/container-->
	</div><!--/breadcrumbs-->
	<!--=== End Breadcrumbs ===-->
	
	<!--=== Content Part ===-->
	<div class="container content">
	
		<div class="row">
			<div class="col-xs-8 col-sm-4 col-md-3">	
				<select class="form-control" ng-model="league" ng-change="changeFootballClub()">
					<option value="KL"><spring:message code="stats.attendance.filter.league"/></option>
					<option value="KLCL"><spring:message code="stats.attendance.filter.league.classic"/></option>
					<option value="KLCH"><spring:message code="stats.attendance.filter.league.challenge"/></option>
				</select>
				<span class="color-blue"><spring:message code="stats.msg.choose.league"/></span>
			</div>
			<div class="col-xs-8 col-sm-4 col-md-3">	
				<select class="form-control" ng-model="season" ng-change="changeFootballClub()">
					<option value="2014">2014</option>
					<option value="2013">2013</option>
					<option value="2012">2012</option>
				</select>
				<span class="color-blue"><spring:message code="stats.msg.choose.season"/></span>
			</div>			
		</div>
				
		
		<highchart id="chart1" config="chartConfig" class="margin-bottom-10"></highchart>
		
		<div class="text-right">
			<button class="btn-u btn-brd rounded btn-u-xs" type="button" ng-click="btnUrlCopy()">
				<spring:message code="common.button.copy.url.to.clipboard"/>
			</button>
		    <a id="kakao-link-btn" href="" ng-click="sendLink()">
		      <img src="<%=request.getContextPath()%>/resources/kakao/icon/kakaolink_btn_small.png" />
		    </a>
		</div>		
	</div> <!-- End Content Part -->

	<jsp:include page="../include/footer.jsp"/>	
</div> <!-- End Wrapper -->

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
	$scope.season = "2014";
	$scope.fcNames = JSON.parse('${fcNames}');
	
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
					min: 0,
					title: {
						text: '<spring:message code="stats.attendance.average"/>'
					},
					labels: {
						x: 0,			
						rotation: -30,
						formatter: function() {
							return Highcharts.numberFormat(this.value,0);
						}
					},					
					opposite: true
				}],       			
				series: [{
					name: '<spring:message code="stats.attendance.total"/>',
					yAxis: 0,
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
		var bUrl = '<c:url value="/stats/data/attendance/season.json?season=2012' + '&league=' + $scope.league + '"/>';
		
		if ($scope.attendancesConn == "none") {
			
			$scope.chartConfig.loading = true;
			var reqPromise = $http.get(bUrl);
			
			$scope.attendancesConn = "loading";
			
			reqPromise.success(function(data, status, headers, config) {
				
				console.log($scope.fcNames);
				console.log(data);
				
				$scope.chartConfig.loading = false;
				var attendances = data.attendances;
				$scope.chartConfig.options.chart.height = 300 + (attendances.length * 30);
				
				attendances.forEach(function(attendance) {
					var itemTotal = [$scope.fcNames[attendance.club.id], attendance.total];
					var itemAverage = [$scope.fcNames[attendance.club.id], attendance.average];
					
					$scope.chartConfig.series[0].data.push(itemAverage);
					$scope.chartConfig.series[1].data.push(itemTotal);			
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
			
			if (league == "KLCL") {
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