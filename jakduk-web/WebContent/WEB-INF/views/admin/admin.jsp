<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>    
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> 
 
<!DOCTYPE html>
<html ng-app="jakdukApp">
<head>
	<title>ADMIN PAGE &middot; <spring:message code="common.jakduk"/></title>

	<jsp:include page="../include/html-header.jsp"/>
</head>
<body>
	<div class="container" ng-controller="adminCtrl">
	
	<div class="page-header">
  <h4>JakduK Admin Page.</h4>
</div>
<div class="btn-group">
  <button class="btn btn-default dropdown-toggle" type="button" data-toggle="dropdown">
    Init Data
    <span class="caret"></span>
  </button>  
  <ul class="dropdown-menu" role="menu">
    <li><a href="<c:url value="/admin/board/category/init"/>">Init Board Category</a></li>
    <li><a href="<c:url value="/admin/search/index/init"/>">Init Search Index</a></li>
    <li><a href="<c:url value="/admin/search/type/init"/>">Init Search Type</a></li>
    <li><a href="<c:url value="/admin/search/data/init"/>">Init Search Data</a></li>
  </ul>
</div>

<div class="btn-group">
  <button class="btn btn-default dropdown-toggle" type="button" data-toggle="dropdown">
    Write Data
    <span class="caret"></span>
  </button>  
  <ul class="dropdown-menu" role="menu">
    <li><a href="<c:url value="/admin/encyclopedia/write"/>">Encyclopedia Write</a></li>
    <li><a href="<c:url value="/admin/footballclub/origin/write"/>">Football Club Origin Write</a></li>
    <li><a href="<c:url value="/admin/footballclub/write"/>">Football Club Write</a></li>
    <li><a href="<c:url value="/admin/board/category/write"/>">Board Category Write</a></li>
    <li><a href="<c:url value="/admin/thumbnail/size/write"/>">Thumbnail Size Write</a></li>
    <li><a href="<c:url value="/admin/home/description/write"/>">Home Description Write</a></li>
    <li><a href="<c:url value="/admin/attendance/league/write"/>">Attendance League Write</a></li>
    <li><a href="<c:url value="/admin/attendance/club/write"/>">Attendance Club Write</a></li>
	<li><a href="<c:url value="/admin/jakdu/schedule/write"/>">Jakdu Schedule Write</a></li>
  </ul>
</div>

<div class="btn-group">
  <button class="btn btn-default dropdown-toggle" type="button" data-toggle="dropdown">
    Get Data
    <span class="caret"></span>
  </button>  
  <ul class="dropdown-menu" role="menu">
    <li><a ng-click="getData('encyclopedia')">Get Encyclopedia</a></li>
    <li><a ng-click="getData('fcOrigin')"/>Get Football Club Origin</a></li>
    <li><a ng-click="getData('fc')">Get Football Club</a></li>
    <li><a ng-click="getData('boardCategory')">Get Board Category</a></li>
    <li><a ng-click="getData('homeDescription')">Get Home Description</a></li>
    <li><a ng-click="getData('attendanceLeague')">Get Attendance League</a></li>
    <li><a ng-click="getData('attendanceClub')">Get Attendance Club</a></li>
  </ul>
</div>

<c:if test="${not empty message}">
	<div class="span6 offset2 alert">${message}</div>
</c:if>

<div ng-show="encyclopedias.length > 0">
<h4>Encyclopedia</h4>
<table class="table">
<tr>
	<th>Seq</th><th>kind</th><th>Language</th><th>Subject</th><th>Content</th>
</tr>
<tr ng-repeat="encyclopedia in encyclopedias">
	<td><a href="<c:url value="/admin/encyclopedia/write/{{encyclopedia.seq}}?lang={{encyclopedia.language}}"/>">{{encyclopedia.seq}}</a></td>
	<td>{{encyclopedia.kind}}</td>
	<td>{{encyclopedia.language}}</td>
	<td>{{encyclopedia.subject}}</td>
	<td>{{encyclopedia.content}}</td>
</tr>
</table>
</div>

<div ng-show="fcOrigins.length > 0">
<h4>Football Club Origin</h4>
<table class="table">
<tr>
	<th>Name</th>
</tr>
<tr ng-repeat="fcOrigin in fcOrigins">
	<td><a href="<c:url value="/admin/footballclub/origin/write/{{fcOrigin.id}}"/>">{{fcOrigin.name}}</a></td>
</tr>
</table>
</div>

<div ng-show="fcs.length > 0">
<h4>Football Club</h4>
<table class="table">
<tr>
	<th>Origin</th><th>Active</th><th>Names</th>
</tr>
<tr ng-repeat="fc in fcs">
	<td><a href="<c:url value="/admin/footballclub/write/{{fc.id}}"/>">{{fc.origin.name}}</a></td>
	<td>{{fc.active}}</td>
	<td><div ng-repeat="name in fc.names">Lang={{name.language}} F.N.={{name.fullName}} S.N.={{name.shortName}}</div>
	</td>
</tr>
</table>
</div>

<div ng-show="boardCategorys.length > 0">
<h4>Board Category</h4>
<table class="table">
<tr>
	<th>Name</th><th>ResName</th><th>UsingBoard</th>
</tr>
<tr ng-repeat="boardCategory in boardCategorys">
	<td><a href="<c:url value="/admin/board/category/write/{{boardCategory.id}}"/>">{{boardCategory.name}}</a></td>
	<td>{{boardCategory.resName}}</td>
	<td><div ng-repeat="usingBoard in boardCategory.usingBoard">{{usingBoard}}</div>
	</td>
</tr>
</table>
</div>

<div ng-show="attendanceLeagues.length > 0">
<h4>Attendance League</h4>
<div class="btn-group">
  <button class="btn btn-default dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown">
    LEAGUE
    <span class="caret"></span>
  </button>  
  <ul class="dropdown-menu" role="menu">
    <li><a ng-click="getDataLeague('KL')">K LEAGUE</a></li>
    <li><a ng-click="getDataLeague('KLCL')"/>K LEAGUE CLASSIC</a></li>
    <li><a ng-click="getDataLeague('KLCH')">K LEAGUE CHALLENGE</a></li>
  </ul>
</div>
<table class="table">
<tr>
	<th>League</th><th>Season</th><th>Games</th><th>Total</th><th>Average</th><th>Number Of Clubs</th>
</tr>
<tr ng-repeat="attendanceLeague in attendanceLeagues">
	<td>{{attendanceLeague.league}}</td>
	<td><a href="<c:url value="/admin/attendance/league/write/{{attendanceLeague.id}}"/>">{{attendanceLeague.season}}</a></td>
	<td>{{attendanceLeague.games}}</td>
	<td>{{attendanceLeague.total}}</td>
	<td>{{attendanceLeague.average}}</td>
	<td>{{attendanceLeague.numberOfClubs}}</td>
</tr>
</table>
</div>

<div ng-show="attendanceClubs.length > 0">
<h4>Attendance Clubs</h4>
<table class="table">
<tr>
	<th>Club</th><th>Season</th><th>League</th><th>Games</th><th>Total</th><th>Average</th>
</tr>
<tr ng-repeat="attendanceClub in attendanceClubs">
	<td><a href="<c:url value="/admin/attendance/club/write/{{attendanceClub.id}}"/>">{{attendanceClub.club.name}}</a></td>
	<td>{{attendanceClub.season}}</td>	
	<td>{{attendanceClub.league}}</td>
	<td>{{attendanceClub.games}}</td>
	<td>{{attendanceClub.total}}</td>
	<td>{{attendanceClub.average}}</td>	
</tr>
</table>
</div>

<div ng-show="homeDescriptions.length > 0">
<h4>Home Descriptions</h4>
<table class="table">
<tr>
	<th>id</th><th>Priority</th><th>Description</th>
</tr>
<tr ng-repeat="homeDescription in homeDescriptions">
	<td><a href="<c:url value="/admin/home/description/write/{{homeDescription.id}}"/>">{{homeDescription.id}}</a></td>
	<td>{{homeDescription.priority}}</td>
	<td>{{homeDescription.desc}}</td>
</tr>
</table>
</div>
		
	</div>
<!-- Bootstrap core JavaScript================================================== -->
<!-- Placed at the end of the document so the pages load faster -->
<script src="<%=request.getContextPath()%>/resources/jquery/dist/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/resources/bootstrap/dist/js/bootstrap.min.js"></script>    
<script src="<%=request.getContextPath()%>/resources/angular/angular.min.js"></script>
<script type="text/javascript">

var jakdukApp = angular.module("jakdukApp", []);

jakdukApp.controller("adminCtrl", function($scope, $http) {
	$scope.dataConn = "none";
	$scope.dataLeagueConn = "none";
	$scope.encyclopedias = [];
	$scope.fcOrigins = [];
	$scope.fcs = [];
	$scope.boardCategorys = [];
	$scope.attendanceLeagues = [];
	$scope.attendanceClubs = [];
	$scope.homeDescriptions = [];
	
	angular.element(document).ready(function() {
		if ("${open}" != null && "${open}" != "") {
			$scope.getData("${open}");
		}		
	});
	
	$scope.getData = function(type) {
		var bUrl;
		
		if (type == "encyclopedia") {
			bUrl = '<c:url value="/admin/encyclopedia.json"/>';
		} else if (type == "fcOrigin") {
			bUrl = '<c:url value="/admin/footballclub/origin.json"/>';
		} else if (type == "fc") {
			bUrl = '<c:url value="/admin/footballclub.json"/>';
		} else if (type == "boardCategory") {
			bUrl = '<c:url value="/admin/board/category.json"/>';
		} else if (type == "attendanceLeague") {
			bUrl = '<c:url value="/admin/data/attendance/league.json"/>';
		} else if (type == "attendanceClub") {
			bUrl = '<c:url value="/admin/data/attendance/club.json"/>';
		} else if (type == "homeDescription") {
			bUrl = '<c:url value="/admin/data/home/description.json"/>';
		}
		
		if ($scope.dataConn == "none") {
			
			var reqPromise = $http.get(bUrl);
			
			$scope.dataConn = "loading";
			
			reqPromise.success(function(data, status, headers, config) {

				$scope.clearData();
				
				if (type == "encyclopedia") {
					$scope.encyclopedias = data.encyclopedias;
				} else if (type == "fcOrigin") {
					$scope.fcOrigins = data.fcOrigins;
				} else if (type == "fc") {
					$scope.fcs = data.fcs;
				} else if (type == "boardCategory") {
					$scope.boardCategorys = data.boardCategorys;
				} else if (type == "attendanceLeague") {
					$scope.attendanceLeagues = data.attendanceLeagues;
				} else if (type == "attendanceClub") {
					$scope.attendanceClubs = data.attendanceClubs;
				} else if (type == "homeDescription") {
					$scope.homeDescriptions = data.homeDescriptions;
				}
				
				$scope.dataConn = "none";
				
			});
			reqPromise.error(function(data, status, headers, config) {
				$scope.dataConn = "none";
				alert("get data error");
			});
		}
	};
	
	$scope.getDataLeague = function(league) {
		var bUrl = '<c:url value="/admin/attendance/league.json?league=' + league + '"/>';
		
		if ($scope.dataLeagueConn == "none") {
			
			var reqPromise = $http.get(bUrl);
			
			$scope.dataLeagueConn = "loading";
			
			reqPromise.success(function(data, status, headers, config) {

				$scope.clearData();
				$scope.attendanceLeagues = data.attendanceLeagues;
				
				$scope.dataLeagueConn = "none";				
			});
			reqPromise.error(function(data, status, headers, config) {
				$scope.dataLeagueConn = "none";
				alert("get data league error");
			});
		}
	};	
	
	$scope.clearData = function() {
		$scope.encyclopedias = [];
		$scope.fcOrigins = [];
		$scope.fcs = [];
		$scope.boardCategorys = [];
		$scope.attendanceLeagues = [];
		$scope.attendanceClubs = [];
		$scope.homeDescriptions = [];
	};
	
});

</script>
</body>
</html>