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
    <li><a ng-click="getData('encyclopedia')">Get Encyclopedia</a></li>
    <li><a ng-click="getData('fcOrigin')"/>Get Football Club Origin</a></li>
    <li><a ng-click="getData('fc')">Get Football Club</a></li>
    <li><a ng-click="getData('boardCategory')">Get Board Category</a></li>
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
		
		<jsp:include page="../include/footer.jsp"/>
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
	$scope.encyclopedias = [];
	$scope.fcOrigins = [];
	$scope.fcs = [];
	$scope.boardCategorys = [];
	
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
				}
				
				$scope.dataConn = "none";
				
			});
			reqPromise.error(function(data, status, headers, config) {
				$scope.dataConn = "none";
				alert("get data error");
			});
		}
	};
	
	if ("${open}" != null && "${open}" != "") {
		$scope.getData("${open}");
	}
	
	$scope.clearData = function() {
		$scope.encyclopedias = [];
		$scope.fcOrigins = [];
		$scope.fcs = [];
		$scope.boardCategorys = [];
	};
	
});

</script>
</body>
</html>