<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
    
<!DOCTYPE html>
<html ng-app="jakdukApp">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Pager test &middot; <spring:message code="common.jakduk"/></title>
<jsp:include page="../include/html-header.jsp"/>
</head>
<body>
<div class="wrapper" ng-controller="sampleCtrl">
	<jsp:include page="../include/navigation-header.jsp"/>

	<c:set var="contextPath" value="<%=request.getContextPath()%>"/>
	


<div>
    <h4>Default</h4>
    <pagination total-items="totalItems" ng-model="currentPage" ng-change="pageChanged()"></pagination>
    <pagination boundary-links="true" total-items="totalItems" ng-model="currentPage" class="pagination-sm" previous-text="&lsaquo;" next-text="&rsaquo;" first-text="&laquo;" last-text="&raquo;"></pagination>
    <pagination direction-links="false" boundary-links="true" total-items="totalItems" ng-model="currentPage"></pagination>
    <pagination direction-links="false" total-items="totalItems" ng-model="currentPage" num-pages="smallnumPages"></pagination>
    <pre>The selected page no: {{currentPage}}</pre>
    <button type="button" class="btn btn-info" ng-click="setPage(3)">Set current page to: 3</button>

    <hr />
    <h4>Pager</h4>
    <pager total-items="totalItems" ng-model="currentPage"></pager>

    <hr />
    <h4>Limit the maximum visible buttons</h4>
    <pagination total-items="bigTotalItems" ng-model="bigCurrentPage" max-size="10" class="pagination-sm" previous-text="&lsaquo;" next-text="&rsaquo;"></pagination>
    <pagination total-items="bigTotalItems" ng-model="bigCurrentPage" max-size="maxSize" class="pagination-sm" boundary-links="true" ></pagination>
    <pagination total-items="bigTotalItems" ng-model="bigCurrentPage" max-size="maxSize" class="pagination-sm" boundary-links="true" rotate="false" num-pages="numPages"></pagination>
    <pre>Page: {{bigCurrentPage}} / {{numPages}}</pre>
</div>

	
</div>
<!-- Bootstrap core JavaScript
  ================================================== -->
<!-- Placed at the end of the document so the pages load faster -->
<script src="<%=request.getContextPath()%>/resources/jquery/dist/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/resources/angular-animate/angular-animate.min.js"></script>
<script src="<%=request.getContextPath()%>/resources/angular-bootstrap/ui-bootstrap-tpls.min.js"></script>

<script type="text/javascript">
var jakdukApp = angular.module("jakdukApp", ["ngAnimate", "ui.bootstrap"]);

jakdukApp.controller('sampleCtrl', function($scope, $log) {
	
	  $scope.totalItems = 64;
	  $scope.currentPage = 4;

	  $scope.setPage = function (pageNo) {
	    $scope.currentPage = pageNo;
	  };

	  $scope.pageChanged = function() {
	    $log.log('Page changed to: ' + $scope.currentPage);
	  };

	  $scope.maxSize = 5;
	  $scope.bigTotalItems = 175;
	  $scope.bigCurrentPage = 1;
	
});
</script>

<jsp:include page="../include/body-footer.jsp"/>
</body>
</html>