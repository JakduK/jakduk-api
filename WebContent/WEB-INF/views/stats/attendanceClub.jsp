<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>    
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%> 
    
<!DOCTYPE html>
<html ng-app="jakdukApp">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title><spring:message code="stats.attendance.club.title"/> - <spring:message code="stats"/> &middot; <spring:message code="common.jakduk"/></title>
	
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
			      <li class="active"><spring:message code="stats.attendance.breadcrumbs.club"/></li>
			      <li><a href="<c:url value="/stats/attendance/league"/>"><spring:message code="stats.attendance.breadcrumbs.league"/></a></li>
		      </ul>			
		</div><!--/container-->
	</div><!--/breadcrumbs-->
	<!--=== End Breadcrumbs ===-->
	
	<!--=== Content Part ===-->
	<div class="container content">

<div class="row">
	<div class="col-xs-6 col-sm-4 col-md-2">	
		<select class="form-control" ng-model="footballClub" ng-options="opt.names[0].fullName for opt in footballClubs"
		ng-change="changeFootballClub()">
			<option value=""><spring:message code="stats.select.football.club"/></option>
		</select>
	</div>
</div>
<span class="color-blue" ng-class="{'hidden':clubOrigin != ''}"><spring:message code="stats.msg.choose.football.club"/></span>
	
		<highchart id="chart1" config="chartConfig" class="margin-bottom-10"></highchart>
		
		<div class="tag-box tag-box-v4 margin-bottom-20">
			<p><spring:message code="stats.msg.total.number.of.attendance.total" arguments="<strong>{{totalSum | number:0}}</strong>"/></p>
			<p><spring:message code="stats.msg.total.number.of.attendance.average" arguments="<strong>{{average | number:0}}</strong>"/></p>	
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
	
	$scope.footballClubsConn = "none";
	$scope.attendancesConn = "none";
	$scope.footballClubs = [];
	$scope.clubOrigin = "";
	$scope.totalSum = 0;
	$scope.gamesSum = 0;
	$scope.average = 0;
	
	angular.element(document).ready(function() {
		
		var clubOrigin = "${clubOrigin}";
		$scope.clubOrigin = clubOrigin;
		
		Highcharts.setOptions({
			lang: {
				thousandsSep: ','
		    }
		});
		
		$scope.chartConfig = {
			options: {
				chart: {
					type: 'column',
					height: 500		                
				},
				tooltip: {
					//pointFormat: '<spring:message code="stats.attendance.total"/> : <b>{point.y:,.0f}</b> <spring:message code="stats.attendance.people"/>',
					shared: true
				}			
			},
			title: {
				text: '<spring:message code="stats.attendance.club.chart.title"/>'
			},	        
			subtitle: {
				text: 'Source: blog.daum.net/vhgksl'
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
				data: [],
				dataLabels: {
					enabled: true,
					color: '#FFFFFF',
					format: '{point.y:,.0f} <spring:message code="stats.attendance.people"/>',    
					style: {
						fontSize: '13px'
					}
				}
			},
			{
				name: '<spring:message code="stats.attendance.average"/>',	
				yAxis: 1,
				type: 'column',
				data: [],
				dataLabels: {
					enabled: true,
					color: '#FFFFFF',
					format: '{point.y:,.0f} <spring:message code="stats.attendance.people"/>',
					style: {
						fontSize: '13px'
					}					
				}
			},
			{
				name: '<spring:message code="stats.attendance.games"/>',
				yAxis: 2,
				type: 'spline',
				data: [],
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
		
		$scope.getFootballClubs(function() {
			$scope.footballClubs.forEach(function(value, index) {
				
				if (value.id == $scope.clubOrigin) {
					$scope.footballClub = $scope.footballClubs[index];
					$scope.chartConfig.title.text = $scope.footballClub.names[0].fullName + ' <spring:message code="stats.attendance.club.title"/>';
				}
			});
			
		});
		
		if ($scope.clubOrigin != "") {
			$scope.getAttendance();			
		}
	});
	
	$scope.getFootballClubs = function(cb) {
		var bUrl = '<c:url value="/data/footballClubs.json"/>';
		
		if ($scope.footballClubsConn == "none") {
			
			$scope.chartConfig.loading = true;
			var reqPromise = $http.get(bUrl);
			
			$scope.footballClubsConn = "loading";
			
			reqPromise.success(function(data, status, headers, config) {
				
				$scope.footballClubs = data.footballClubs;
				$scope.footballClubsConn = "none";
				cb();
			});
			
			reqPromise.error(function(data, status, headers, config) {
				$scope.footballClubsConn = "none";
				$scope.error = '<spring:message code="common.msg.error.network.unstable"/>';
			});
		}
	};
	
	$scope.changeFootballClub = function() {
		
		if ($scope.footballClub != null && $scope.clubOrigin != $scope.footballClub.origin.name) {
			$scope.chartConfig.series.forEach(function(series) {
				series.data = [];
			}) ;
			
			$scope.clubOrigin = $scope.footballClub.origin.name;
			$scope.chartConfig.title.text = $scope.footballClub.names[0].fullName + ' <spring:message code="stats.attendance.club.title"/>';
			$scope.getAttendance();			
		}
	};
	
	$scope.getAttendance = function() {
		var bUrl = '<c:url value="/stats/data/attendance/club.json?clubOrigin=' + $scope.clubOrigin + '"/>';
		
		if ($scope.attendancesConn == "none") {
			
			$scope.chartConfig.loading = true;
			var reqPromise = $http.get(bUrl);
			
			$scope.attendancesConn = "loading";
			
			reqPromise.success(function(data, status, headers, config) {
				
				$scope.chartConfig.loading = false;
				$scope.totalSum = data.totalSum;
				$scope.gamesSum = data.gamesSum;
				$scope.average = data.average;
				var attendances = data.attendances;
				
				if (attendances != null) {
					attendances.forEach(function(attendance) {
						var itemTotal = [attendance.season, attendance.total];
						var itemAverage = [attendance.season, attendance.average];
						var itemGames = [attendance.season, attendance.games];
						
						$scope.chartConfig.series[0].data.push(itemTotal);
						$scope.chartConfig.series[1].data.push(itemAverage);
						$scope.chartConfig.series[2].data.push(itemGames);
					});
				}
				
				$scope.attendancesConn = "none";				
			});
			
			reqPromise.error(function(data, status, headers, config) {
				$scope.attendancesConn = "none";
				$scope.error = '<spring:message code="common.msg.error.network.unstable"/>';
			});
		}
	};
	
	$scope.btnUrlCopy = function() {
		var url = "https://jakduk.com/stats/attendance/club?clubOrigin=" + $scope.clubOrigin;
		
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
		var url = "https://jakduk.com/stats/attendance/club?clubOrigin=" + $scope.clubOrigin;
		
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