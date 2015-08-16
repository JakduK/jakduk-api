<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
    
<!DOCTYPE html>
<html ng-app="jakdukApp">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Search posts of JakduK with Elasticsearch &middot; <spring:message code="common.jakduk"/></title>
<jsp:include page="../include/html-header.jsp"/>
</head>
<body>
<div class="wrapper" ng-controller="sampleCtrl">
	<jsp:include page="../include/navigation-header.jsp"/>

	<c:set var="contextPath" value="<%=request.getContextPath()%>"/>
	
<div class="col-md-6 col-md-offset-3">
                <h2>Search sample</h2>
                <div class="input-group">
                    <input class="form-control" placeholder="Search words with regular expressions ..." type="text" ng-model="searchText">
                    <span class="input-group-btn">
                        <button class="btn-u" type="button" ng-click="onSearchBtn();"><i class="fa fa-search"></i></button>
                    </span>
                </div>
            </div>
	
</div>
<!-- Bootstrap core JavaScript
  ================================================== -->
<!-- Placed at the end of the document so the pages load faster -->
<script src="<%=request.getContextPath()%>/resources/jquery/dist/jquery.min.js"></script>

<script type="text/javascript">
var jakdukApp = angular.module("jakdukApp", []);

jakdukApp.controller('sampleCtrl', function($scope) {
	
	$scope.onSearchBtn = function() {
		location.href = '<c:url value="/search"/>' + '?q=' + $scope.searchText;
		//location.href = '<c:url value="/search/' + $scope.searchText + '"></c:url>';
	};
	
});
</script>

<jsp:include page="../include/body-footer.jsp"/>
</body>
</html>