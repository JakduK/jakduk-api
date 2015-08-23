<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>    
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>    
<!DOCTYPE html>
<html ng-app="jakdukApp">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title><spring:message code="search"/> &middot; <spring:message code="common.jakduk"/></title>
	
    <!-- CSS Page Style -->    
    <link rel="stylesheet" href="<%=request.getContextPath()%>/resources/unify/assets/css/pages/page_search_inner.css">
    
    <jsp:include page="../include/html-header.jsp"></jsp:include>	
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
	        <div class="col-md-6 col-md-offset-3 margin-bottom-10">
	        	<div class="input-group">
					<label class="checkbox-inline">
					  <input type="checkbox" ng-model="where.posts"><spring:message code="search.post"/>
					</label>
					<label class="checkbox-inline">
					  <input type="checkbox" ng-model="where.comments"><spring:message code="search.comment"/>
					</label>
					<label class="checkbox-inline">
					  <input type="checkbox" ng-model="where.galleries"><spring:message code="search.gallery"/>
					</label>
	        	</div>
	        </div>
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
    
    	<!--=== Content Part ===-->
	<div class="container s-results">
<div class=" margin-bottom-30" ng-show="posts.hits.total > 0">
	<span class="results-number"><spring:message code="search.post.results" arguments="{{posts.hits.total}}"/></span>
        
        <!-- Begin Inner Results -->
        <div ng-repeat="hit in posts.hits.hits">
	        <div class="inner-results">
	            <h3 ng-if="hit.highlight.subject.length > 0"><a href='<c:url value="/board/free/{{hit._source.seq}}"/>' ng-bind-html="hit.highlight.subject[0]"></a></h3>
	            <h3 ng-if="hit.highlight.subject.length == null"><a href="<c:url value="/board/free/{{hit._source.seq}}"/>">{{hit._source.subject}}</a></h3>
	            <p ng-if="hit.highlight.content.length > 0" ng-bind-html="hit.highlight.content[0]"></p>
	            <p ng-if="hit.highlight.content.length == null">{{hit.fields.content_preview[0]}}</p>
	            <ul class="list-inline down-ul">
	                <li><i aria-hidden="true" class="icon-user"></i> {{hit._source.writer.username}}</li>
	                <li>{{dateFromObjectId(hit._id) | date:"${dateTimeFormat.dateTime}"}}</li>
	                <li></li>
	            </ul>    
	        </div>
	        <hr class="padding-5"/>
        </div>
    </div>
    
		<div class="text-left" ng-show="posts.hits.total > 0">
        	<pagination ng-model="currentPage" total-items="posts.hits.total" max-size="10" items-per-page="10"
        	previous-text="&lsaquo;" next-text="&rsaquo;" ng-change="pageChanged()" ng-show="whereSize == 1"></pagination>
        	
			<div class="text-right col-md-12" ng-show="whereSize > 1">
				<ul class="list-unstyled">
				    <li><a href='<c:url value="/search?q=${q}&w=PO;"/>'>
				    	<spring:message code="search.more.post.results"/> <i class="fa fa-chevron-right"></i>
				    </a></li>   
				</ul>
			</div>
		</div>
    
    </div><!--/container-->		
    <!--=== End Content Part ===-->
    
	<jsp:include page="../include/footer.jsp"/>

</div><!-- /.container -->

<!-- Bootstrap core JavaScript
  ================================================== -->
<!-- Placed at the end of the document so the pages load faster -->
<script src="<%=request.getContextPath()%>/resources/jquery/dist/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/resources/angular-sanitize/angular-sanitize.min.js"></script>
<script src="<%=request.getContextPath()%>/resources/angular-animate/angular-animate.min.js"></script>
<script src="<%=request.getContextPath()%>/resources/angular-bootstrap/ui-bootstrap-tpls.min.js"></script>

<script src="<%=request.getContextPath()%>/resources/jakduk/js/jakduk.js"></script>

<script type="text/javascript">
var jakdukApp = angular.module("jakdukApp", ["ngSanitize", "ngAnimate", "ui.bootstrap"]);

jakdukApp.controller("searchCtrl", function($scope, $http, $location) {
	$scope.resultsConn = "none";
	$scope.posts = {};
	$scope.comments = {};
	$scope.where = {};
	$scope.whereSize = 0;
	
	angular.element(document).ready(function() {
		var from = parseInt("${from}");
		var where = "${w}";
		var size = 10;
		
		if (from > 0) {
			$scope.currentPage = (from + 10) / 10;
		} else {
			$scope.currentPage = 1;			
		}
		
		if (!isEmpty(where)) {
			var arrW = where.split(";");
			
			$scope.whereSize = arrW.length - 1;
			
			for (var i = 0 ; i < arrW.length ; i++) {
				var tempW = arrW[i];
				
				if (tempW == "PO") {
					$scope.where.posts = true;
				}
				if (tempW == "CO") {
					$scope.where.comments = true;
				}
				if (tempW == "GA") {
					$scope.where.galleries = true;
				}
			}
		} else {
			$scope.where = {posts : false, comments : false, galleries : false};
		}
		
		if ($scope.whereSize == 1) {
			size = 10;
		} else if ($scope.whereSize == 2) {
			size = 5;
		} else if ($scope.whereSize >= 3) {
			size = 3;
		}
		
		$scope.getResults(where, from, size);
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
		if ($scope.searchWords.trim() < 1 || ($scope.where.posts == false && $scope.where.comments == false && $scope.where.galleries == false)) {
			return;
		}
		
		var where = "";
		
		if ($scope.where.posts == true) {
			where += "PO;";
		}
		if ($scope.where.comments == true) {
			where += "CO;";
		}
		if ($scope.where.galleries == true) {
			where += "GA;";
		}
		
		console.log(where);
		
		location.href = '<c:url value="/search?q=' + $scope.searchWords.trim() + '&w=' + where + '"/>';
	};
	
	$scope.getResults = function(where, from, size) {
		
		if ($scope.searchWords.trim() < 1) {
			return;
		}
		
		var bUrl = '<c:url value="/search/data.json?q=' + $scope.searchWords + '&w=' + where + '&from=' + from + '&size=' + size + '"/>';
		
		if ($scope.resultsConn == "none") {
			
			var reqPromise = $http.get(bUrl);
			
			$scope.resultsConn = "loading";

			reqPromise.success(function(data, status, headers, config) {
				
				if (data.posts != null) {
					$scope.posts = JSON.parse(data.posts);
					console.log($scope.posts);
				}
				
				if (data.comments != null) {
					$scope.comments = JSON.parse(data.comments);
					console.log($scope.comments);
				}
				
				$scope.resultsConn = "none";
				
			});
			reqPromise.error(function(data, status, headers, config) {
				$scope.resultsConn = "none";
				$scope.error = '<spring:message code="common.msg.error.network.unstable"/>';
			});
		}
	};
	
	$scope.pageChanged = function() {
		var from = $scope.currentPage;
		
		if (from > 1) {
			from = (from - 1) * 10;
		} else {
			from = 0;
		}
		
		location.href = '<c:url value="/search?q=' + $scope.searchWords.trim() + '&w=' + '${w}' + '&from=' + from + '"/>';
	};
	
});
</script>

<jsp:include page="../include/body-footer.jsp"/>

</body>
</html>