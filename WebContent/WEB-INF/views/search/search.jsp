<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>    
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>    
<!DOCTYPE html>
<html ng-app="jakdukApp">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title><spring:message code="search"/> &middot; <spring:message code="common.jakduk"/></title>
	<jsp:include page="../include/html-header.jsp"></jsp:include>
	
    <!-- CSS Page Style -->    
    <link rel="stylesheet" href="<%=request.getContextPath()%>/resources/unify/assets/css/pages/page_search_inner.css">	
</head>
<body>

<div class="wrapper" ng-controller="searchCtrl">
	<jsp:include page="../include/navigation-header.jsp"/>
	
	<!--=== Breadcrumbs ===-->
	<div class="breadcrumbs">
		<div class="container">
			<h1 class="pull-left"><a href="<c:url value="/search/refresh"/>"><spring:message code="search"/></a></h1>
		</div><!--/container-->
	</div><!--/breadcrumbs-->
	<!--=== End Breadcrumbs ===-->	
	
<div class="search-block-v2">
        <div class="container">
            <div class="col-md-6 col-md-offset-3">
                <div class="input-group">
                    <input type="text" class="form-control" ng-model="searchWords" ng-init="searchWords='${q}'" 
                    ng-keypress="($event.which === 13)?btnEnter():return" placeholder='<spring:message code="search.placeholder.words"/>'>
                    <span class="input-group-btn">
                        <button class="btn-u" type="button" ng-click="btnEnter();"><i class="fa fa-search"></i></button>
                    </span>
                </div>
            </div>
        </div>    
    </div>

<div class="container s-results margin-bottom-50" ng-if="results.hits.total > 0">
        <span class="results-number"><spring:message code="search.about.results" arguments="{{results.hits.total}}"/></span>
        <!-- Begin Inner Results -->
        <div ng-repeat="hit in results.hits.hits">
	        <div class="inner-results">
	            <h3 ng-if="hit.highlight.subject.length > 0"><a href='<c:url value="/board/free/{{hit._source.seq}}"/>' ng-bind-html="hit.highlight.subject[0]"></a></h3>
	            <h3 ng-if="hit.highlight.subject.length == null"><a href="<c:url value="/board/free/{{hit._source.seq}}"/>">{{hit._source.subject}}</a></h3>
	            <p ng-if="hit.highlight.content.length > 0" ng-bind-html="hit.highlight.content[0]"></p>
	            <p ng-if="hit.highlight.content.length == null">{{hit._source.contentPreview}}</p>
	            <ul class="list-inline down-ul">
	                <li><i class="fa fa-user"></i> {{hit._source.writer.username}}</li>
	                <li><i class="fa fa-clock-o"></i> {{dateFromObjectId(hit._id) | date:"${dateTimeFormat.dateTime}"}}</li>
	                <li></li>
	            </ul>    
	        </div>
	        <hr>
        </div>
        
        <div class="margin-bottom-30"></div>

        <div class="text-left">
            <ul class="pagination">
                <li><a href="#">«</a></li>
                <li class="active"><a href="#">1</a></li>
                <li><a href="#">2</a></li>
                <li><a href="#">3</a></li>
                <li><a href="#">...</a></li>
                <li><a href="#">157</a></li>
                <li><a href="#">158</a></li>
                <li><a href="#">»</a></li>
            </ul>                                                            
        </div>
    </div>


	
	<jsp:include page="../include/footer.jsp"/>

</div><!-- /.container -->

<!-- Bootstrap core JavaScript
  ================================================== -->
<!-- Placed at the end of the document so the pages load faster -->
<script src="<%=request.getContextPath()%>/resources/jquery/dist/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/resources/angular-sanitize/angular-sanitize.min.js"></script>

<script type="text/javascript">
var jakdukApp = angular.module("jakdukApp", ["ngSanitize"]);

jakdukApp.controller("searchCtrl", function($scope, $http) {
	$scope.resultsConn = "none";
	$scope.results = {};
	
	angular.element(document).ready(function() {
		$scope.getResults();
		App.init();
	});
	
	$scope.objectIdFromDate = function(date) {
		return Math.floor(date.getTime() / 1000).toString(16) + "0000000000000000";
	};
	
	$scope.dateFromObjectId = function(objectId) {
		return new Date(parseInt(objectId.substring(0, 8), 16) * 1000);
	};
	
	$scope.intFromObjectId = function(objectId) {
		return parseInt(objectId.substring(0, 8), 16) * 1000;
	};
	
	$scope.btnEnter = function() {
		if ($scope.resultsConn == "none") {
			$scope.getResults();
		}
	};
	
	$scope.getResults = function() {
		
		if ($scope.searchWords.trim() < 1) {
			return;
		}
		
		var bUrl = '<c:url value="/search/data/board.json?q=' + $scope.searchWords + '"/>';
		
		if ($scope.resultsConn == "none") {
			
			var reqPromise = $http.get(bUrl);
			
			$scope.resultsConn = "loading";
			
			reqPromise.success(function(data, status, headers, config) {

				$scope.results = JSON.parse(data.results);
				
				console.log($scope.results);
				
				$scope.resultsConn = "none";
				
			});
			reqPromise.error(function(data, status, headers, config) {
				$scope.resultsConn = "none";
				$scope.error = '<spring:message code="common.msg.error.network.unstable"/>';
			});
		}
	};	
	
});
</script>

<jsp:include page="../include/body-footer.jsp"/>

</body>
</html>