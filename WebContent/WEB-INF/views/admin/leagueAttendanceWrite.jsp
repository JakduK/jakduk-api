<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>    
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>    

<!DOCTYPE html>
<html ng-app="jakdukApp">
<head>
<jsp:include page="../include/html-header.jsp"/>
</head>
<body>
<div class="container" ng-controller="adminCtrl">
<h4>Write LeagueAttendance.</h4>
<c:set var="contextPath" value="<%=request.getContextPath()%>"/>
<form:form commandName="leagueAttendance" action="${contextPath}/admin/attendance/league/write" method="POST">
<form:hidden path="id"/>
<p>ex) 1983</p>
<p>
SEASON : 	<form:input path="season" cssClass="form-control" placeholder="Season"/>
</p>
<form:errors path="season"/>
<p>ex) 40</p>
<p>
GAMES : 	
<form:input path="games" cssClass="form-control" placeholder="Games" 
ng-model="games" ng-init="games='${leagueAttendance.games}'" ng-blur="calcAve()"/>
</p>
<form:errors path="games"/>
<p>ex) 419478</p>
<p>
TOTAL : <form:input path="total" cssClass="form-control" placeholder="Total" 
ng-model="total" ng-init="total='${leagueAttendance.total}'" ng-blur="calcAve()"/>
</p>
<form:errors path="total"/>
<p>
<p>
AVERAGE : <form:input path="average" cssClass="form-control" placeholder="Average" 
ng-model="average" ng-init="average='${leagueAttendance.average}'"/>
</p>
<form:errors path="average"/>
<p>
<input type="submit" value="<spring:message code="common.button.write"/>" class="btn btn-default"/>
</p>
</form:form>
</div>
<jsp:include page="../include/footer.jsp"/>
<!-- Bootstrap core JavaScript================================================== -->
<!-- Placed at the end of the document so the pages load faster -->
<script src="<%=request.getContextPath()%>/resources/jquery/dist/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/resources/bootstrap/dist/js/bootstrap.min.js"></script>    
<script src="<%=request.getContextPath()%>/resources/angular/angular.min.js"></script>
<script type="text/javascript">

var jakdukApp = angular.module("jakdukApp", []);

jakdukApp.controller("adminCtrl", function($scope, $filter) {
	
	$scope.calcAve = function() {
		$scope.average = Math.floor($scope.total / $scope.games); 
	};
	
});

</script>
</body>
</html>
