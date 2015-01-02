<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>    
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> 
 
<!DOCTYPE html>
<html ng-app="jakdukApp">
<head>
	<jsp:include page="../include/html-header.jsp"/>
</head>
<body>
	<div class="container" ng-controller="adminCtrl">
	
	<div class="page-header">
  <h4>JakduK Admin Page.</h4>
</div>
<button type="button" class="btn btn-default" onclick="location.href='<c:url value="/admin/init"/>'">Init Data</button>
<c:if test="${not empty message}">
	<div class="span6 offset2 alert">${message}</div>
</c:if>

<div class="btn-group">
  <button class="btn btn-default dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown">
    Write Data
    <span class="caret"></span>
  </button>  
  <ul class="dropdown-menu" role="menu">
    <li><a href="<c:url value="/admin/encyclopedia/write"/>">Encyclopedia Write</a></li>
    <li><a href="<c:url value="/admin/footballclub/origin/write"/>">Football Club Origin Write</a></li>
    <li><a href="<c:url value="/admin/footballclub/write"/>">Football Club Write</a></li>
    <li><a href="<c:url value="/admin/board/category/write"/>">Board Category Write</a></li>
  </ul>
</div>

<div class="btn-group">
  <button class="btn btn-default dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown">
    Get Data
    <span class="caret"></span>
  </button>  
  <ul class="dropdown-menu" role="menu">
    <li><a ng-click="getEncyclopedia()">Get Encyclopedia</a></li>
    <li><a ng-click="getfcOrigin()"/>Get Football Club Origin</a></li>
    <li><a ng-click="getfc()">Get Football Club</a></li>
    <li><a href="<c:url value="/admin/board/category/write"/>">Board Category Write</a></li>
  </ul>
</div>

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
		
		<jsp:include page="../include/footer.jsp"/>
	</div>
<!-- Bootstrap core JavaScript================================================== -->
<!-- Placed at the end of the document so the pages load faster -->
<script src="<%=request.getContextPath()%>/resources/jquery/js/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/resources/bootstrap/js/bootstrap.min.js"></script>    
<script src="<%=request.getContextPath()%>/resources/angular/js/angular.min.js"></script>
<script type="text/javascript">

var jakdukApp = angular.module("jakdukApp", []);

jakdukApp.controller("adminCtrl", function($scope, $http) {
	$scope.encyclopediaConn = "none";
	$scope.fcOriginConn = "none";
	$scope.fcConn = "none";
	$scope.encyclopedias = [];
	$scope.fcOrigins = [];
	$scope.fcs = [];
	
	$scope.getEncyclopedia = function() {
		var bUrl = '<c:url value="/admin/encyclopedia.json"/>';
		
		if ($scope.encyclopediaConn == "none") {
			
			var reqPromise = $http.get(bUrl);
			
			$scope.encyclopediaConn = "loading";
			
			reqPromise.success(function(data, status, headers, config) {

				$scope.encyclopedias = data.encyclopedias;
				
				$scope.encyclopediaConn = "none";
				
			});
			reqPromise.error(function(data, status, headers, config) {
				$scope.encyclopediaConn = "none";
				alert("encyclopedia error");
			});
		}
	};
	
	$scope.getfcOrigin = function() {
		var bUrl = '<c:url value="/admin/footballclub/origin.json"/>';
		
		if ($scope.fcOriginConn == "none") {
			
			var reqPromise = $http.get(bUrl);
			
			$scope.fcOriginConn = "loading";
			
			reqPromise.success(function(data, status, headers, config) {

				$scope.fcOrigins = data.fcOrigins;
				
				$scope.fcOriginConn = "none";
				
			});
			reqPromise.error(function(data, status, headers, config) {
				$scope.fcOriginConn = "none";
				alert("football club orgin error");
			});
		}
	};
	
	$scope.getfc = function() {
		var bUrl = '<c:url value="/admin/footballclub.json"/>';
		
		if ($scope.fcConn == "none") {
			
			var reqPromise = $http.get(bUrl);
			
			$scope.fcConn = "loading";
			
			reqPromise.success(function(data, status, headers, config) {

				$scope.fcs = data.fcs;
				
				$scope.fcConn = "none";
				
			});
			reqPromise.error(function(data, status, headers, config) {
				$scope.fcConn = "none";
				alert("football club orgin error");
			});
		}
	};
	
	if ("${open}" != null) {
		if ("${open}" == "encyclopedia") {
			$scope.getEncyclopedia();
		} else if ("${open}" == "fcOrigin") {
			$scope.getfcOrigin();
		} else if ("${open}" == "fc") {
			$scope.getfc();
		}
	}
	
});

</script>
</body>
</html>