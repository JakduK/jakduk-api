<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>    
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>    
<!DOCTYPE html>
<html ng-app="jakdukApp">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title><spring:message code="search.results"/> &middot; <spring:message code="common.jakduk"/></title>
	<jsp:include page="../include/html-header.jsp"></jsp:include>
	
    <!-- CSS Page Style -->    
    <link rel="stylesheet" href="<%=request.getContextPath()%>/resources/unify/assets/css/pages/page_search_inner.css">	
</head>
<body>

<div class="wrapper">
	<jsp:include page="../include/navigation-header.jsp"/>
	
	<!--=== Breadcrumbs ===-->
	<div class="breadcrumbs">
		<div class="container">
			<h1 class="pull-left"><a href="<c:url value="/about/intro/refresh"/>"><spring:message code="search.results"/></a></h1>
		</div><!--/container-->
	</div><!--/breadcrumbs-->
	<!--=== End Breadcrumbs ===-->	

<div class="search-block-v2">
        <div class="container">
            <div class="col-md-6 col-md-offset-3">
                <div class="input-group">
                    <input class="form-control" placeholder="Search words with regular expressions ..." type="text">
                    <span class="input-group-btn">
                        <button class="btn-u" type="button"><i class="fa fa-search"></i></button>
                    </span>
                </div>
            </div>
        </div>    
    </div>

<div class="container s-results margin-bottom-50" ng-controller="searchCtrl">
        <span class="results-number">About {{results.hits.total}} results</span>
        <!-- Begin Inner Results -->
        <div ng-if="results.hits.total > 0">
	        <div class="inner-results" ng-repeat="hit in results.hits.hits">
	            <h3><a href="#">{{hit._source.subject}}</a></h3>
	            <ul class="list-inline up-ul">
	                <li>en.wikipedia.org/wiki/Web_design‎</li>
	                <li class="btn-group">
	                    <button data-toggle="dropdown" class="btn btn-default dropdown-toggle" type="button">
	                        More<i class="fa fa-caret-down margin-left-5"></i>
	                        <span class="sr-only">Toggle Dropdown</span>                            
	                    </button>
	                    <ul role="menu" class="dropdown-menu">
	                        <li><a href="#">Share</a></li>
	                        <li><a href="#">Similar</a></li>
	                        <li><a href="#">Advanced search</a></li>
	                    </ul>
	                </li>
	                <li><a href="#">Wrapbootstrap</a></li>
	                <li><a href="#">Dribbble</a></li>
	            </ul>
	            <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vestibulum ut orci urna. Morbi blandit enim eget risus posuere dapibus. Vestibulum velit nisi, tempus in placerat non, auctor eu purus. Morbi suscipit porta libero, ac tempus tellus consectetur non. Praesent eget consectetur nunc. Aliquam erat volutpat. Suspendisse ultrices eros eros, consectetur facilisis urna posuere id.</p>
	            <ul class="list-inline down-ul">
	                <li>
	                    <ul class="list-inline star-vote">
	                        <li><i class="color-green fa fa-star"></i></li>
	                        <li><i class="color-green fa fa-star"></i></li>
	                        <li><i class="color-green fa fa-star"></i></li>
	                        <li><i class="color-green fa fa-star"></i></li>
	                        <li><i class="color-green fa fa-star-half-o"></i></li>
	                    </ul>
	                </li>
	                <li>3 years ago - By Anthon Brandley</li>
	                <li>234,034 views</li>
	                <li><a href="#">Web designer</a></li>
	            </ul>    
	        </div>
        <!-- Begin Inner Results -->
        </div>

        <hr>

        <!-- Begin Inner Results -->
        <div class="inner-results">
            <h3><a href="#">Web Design - Website Design Tutorials, Articles</a></h3>
            <ul class="list-inline up-ul">
                <li>www.webdesign.org/</li>
                <li class="btn-group">
                    <button data-toggle="dropdown" class="btn btn-default dropdown-toggle" type="button">
                        More<i class="fa fa-caret-down margin-left-5"></i>
                        <span class="sr-only">Toggle Dropdown</span>                            
                    </button>
                    <ul role="menu" class="dropdown-menu">
                        <li><a href="#">Share</a></li>
                        <li><a href="#">Similar</a></li>
                        <li><a href="#">Advanced search</a></li>
                    </ul>
                </li>
            </ul>
            <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vestibulum ut orci urna. Morbi blandit enim eget risus posuere dapibus. Vestibulum velit nisi, tempus in placerat non, auctor eu purus. Morbi suscipit porta libero, ac tempus tellus consectetur non. Praesent eget consectetur nunc. Aliquam erat volutpat. Suspendisse ultrices eros eros, consectetur facilisis urna posuere id.</p>
            <ul class="list-inline down-ul">
                <li>By Alice Emilsson</li>
                <li>98,298 views</li>
            </ul>
        </div>
        <!-- Begin Inner Results -->

        <hr>

        <!-- Begin Inner Results -->
        <div class="inner-results">
            <h3><a href="#">Unify - Responsive Website Template</a></h3>
            <ul class="list-inline up-ul">
                <li>https://wrapbootstrap.com/theme/unify-responsive-website-template-WB0412697</li>
                <li class="btn-group">
                    <button data-toggle="dropdown" class="btn btn-default dropdown-toggle" type="button">
                        More<i class="fa fa-caret-down margin-left-5"></i>
                        <span class="sr-only">Toggle Dropdown</span>                            
                    </button>
                    <ul role="menu" class="dropdown-menu">
                        <li><a href="#">Share</a></li>
                        <li><a href="#">Similar</a></li>
                        <li><a href="#">Advanced search</a></li>
                    </ul>
                </li>
                <li><a href="#">Wrapbootstrap</a></li>
                <li><a href="#">Dribbble</a></li>
            </ul>
            <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vestibulum ut orci urna. Morbi blandit enim eget risus posuere dapibus. Vestibulum velit nisi, tempus in placerat non, auctor eu purus. Morbi suscipit porta libero, ac tempus tellus consectetur non. Praesent eget consectetur nunc. Aliquam erat volutpat. Suspendisse ultrices eros eros, consectetur facilisis urna posuere id.</p>
            <ul class="list-inline down-ul">
                <li>
                    <ul class="list-inline star-vote">
                        <li><i class="color-green fa fa-star"></i></li>
                        <li><i class="color-green fa fa-star"></i></li>
                        <li><i class="color-green fa fa-star"></i></li>
                        <li><i class="color-green fa fa-star"></i></li>
                        <li><i class="color-green fa fa-star-half-o"></i></li>
                    </ul>
                </li>
                <li>3 years ago - By Anthon Brandley</li>
                <li>234,034 views</li>
                <li><a href="#">Web designer</a></li>
            </ul>    
        </div>
        <!-- Begin Inner Results -->
        
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

<script type="text/javascript">
var jakdukApp = angular.module("jakdukApp", []);

jakdukApp.controller("searchCtrl", function($scope, $http) {
	$scope.results = JSON.parse('${result}');
	
	console.log($scope.results);
	
	angular.element(document).ready(function() {
	});	
	
});
</script>

<jsp:include page="../include/body-footer.jsp"/>

</body>
</html>