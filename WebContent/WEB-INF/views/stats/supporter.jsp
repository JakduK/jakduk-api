<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>    
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>    

<!DOCTYPE html>
<html ng-app="jakdukApp">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title><spring:message code="stats"/> &middot; <spring:message code="common.jakduk"/></title>
	<jsp:include page="../include/html-header.jsp"></jsp:include>
</head>
<body>
<div class="container" ng-controller="statsCtrl">
<jsp:include page="../include/navigation-header.jsp"/>

<div class="page-header">
  <h4>
	  <a href="<c:url value="/stats"/>"><spring:message code="stats"/></a>
	  <small><spring:message code="stats.about"/></small>
  </h4>
</div>

<div google-chart chart=supporters></div>
</div>

<!-- Bootstrap core JavaScript
  ================================================== -->
<!-- Placed at the end of the document so the pages load faster -->
<script src="<%=request.getContextPath()%>/resources/jquery/js/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/resources/bootstrap/js/bootstrap.min.js"></script>    
<script src="<%=request.getContextPath()%>/resources/angular-google-chart/js/ng-google-chart.js"></script>

<script type="text/javascript">
var jakdukApp = angular.module("jakdukApp", ["googlechart"]);

jakdukApp.controller('statsCtrl', function($scope, $http) {
	$scope.supporters = {};
	$scope.supportersConn = "none";
	
	angular.element(document).ready(function() {
		$scope.getSupporters();
	});

    $scope.onions = [
        {v: "Onions"},
        {v: 3},
    ];

    $scope.supporters.data = {"cols": [
        {id: "t", label: "Topping", type: "string"},
        {id: "s", label: "지지자 수", type: "number"}
    ], "rows": []};

    // $routeParams.chartType == BarChart or PieChart or ColumnChart...
    $scope.supporters.type = "BarChart";
    $scope.supporters.options = {
        'title': 'Supporters'
    }
    
	$scope.getSupporters = function() {
		var bUrl = '<c:url value="/stats/data/supporter.json"/>';
		
		if ($scope.supportersConn == "none") {
			
			var reqPromise = $http.get(bUrl);
			
			$scope.supportersConn = "loading";
			
			reqPromise.success(function(data, status, headers, config) {
				
				var supporters = data.supporters;
				
				supporters.forEach(function(supporter) {
					console.log(supporter);
					var item = {c:[{v:supporter.id.names[0].shortName}, {v:supporter.count}]};
					$scope.supporters.data.rows.push(item);
				});
				
				$scope.supportersConn = "none";
				
			});
			reqPromise.error(function(data, status, headers, config) {
				$scope.supportersConn = "none";
				$scope.error = '<spring:message code="common.msg.error.network.unstable"/>';
			});
		}
	};
	
});
</script>

<jsp:include page="../include/body-footer.jsp"/>
</body>
</html>