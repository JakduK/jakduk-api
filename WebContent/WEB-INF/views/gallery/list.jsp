<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>    
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<!DOCTYPE html>
<html ng-app="jakdukApp">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<jsp:include page="../include/html-header.jsp"></jsp:include>
	
</head>
<body>
<div class="container jakduk" ng-controller="galleryCtrl">
<jsp:include page="../include/navigation-header.jsp"/>

<div class="page-header">
  <h4>
	  <a href="<c:url value="/gallery"/>"><spring:message code="gallery"/></a>
	  <small><spring:message code="gallery.about"/></small>
  </h4>
</div>

<div class="row">
    <div class="col-xs-4 col-sm-2 col-md-2 col-lg-2" ng-repeat="gallery in galleries">
    	<a href="<%=request.getContextPath()%>/gallery/{{gallery.id}}" class="thumbnail">
    		<img ng-src="<%=request.getContextPath()%>/gallery/thumbnail/{{gallery.id}}">
    		<div class="caption text-overflow">
    			{{gallery.name}}
    			<p><small>{{gallery.writer.username}}</small></p>
    		</div>
    	</a>
    </div>
</div>

<!--  
    <div id="myCarousel" class="carousel slide" data-ride="carousel">
      <ol class="carousel-indicators">
					<c:forEach begin="0" end="${fn:length(galleries) - 1}" var="index">
						<c:choose>
							<c:when test="${index == 0}">
								<li data-target="#myCarousel" data-slide-to="${index}" class="active"></li>
							</c:when>
							<c:otherwise>
								<li data-target="#myCarousel" data-slide-to="${index}"></li>
							</c:otherwise>
						</c:choose>
					</c:forEach>
      </ol>
      <div class="carousel-inner" role="listbox">
        <c:forEach items="${galleries}" var="gallery" varStatus="status">
        <div class="item" ng-class="{'active':'${status.count}' == 1}">
          <img src="<%=request.getContextPath()%>/gallery/${gallery.id}">
          <div class="container">
            <div class="carousel-caption">
              <h3>${gallery.name}</h3>
              <p>${gallery.writer.username}</p>
            </div>
          </div>
        </div>
        </c:forEach>
      </div>
      <a class="left carousel-control" href="#myCarousel" role="button" data-slide="prev">
        <span class="glyphicon glyphicon-chevron-left" aria-hidden="true"></span>
        <span class="sr-only">Previous</span>
      </a>
      <a class="right carousel-control" href="#myCarousel" role="button" data-slide="next">
        <span class="glyphicon glyphicon-chevron-right" aria-hidden="true"></span>
        <span class="sr-only">Next</span>
      </a>
    </div>
    -->

<jsp:include page="../include/footer.jsp"/>
</div><!-- /.container -->

<!-- Bootstrap core JavaScript
  ================================================== -->
<!-- Placed at the end of the document so the pages load faster -->
<script src="<%=request.getContextPath()%>/resources/jquery/js/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/resources/bootstrap/js/bootstrap.min.js"></script>    

<script type="text/javascript">
var jakdukApp = angular.module("jakdukApp", []);

jakdukApp.controller("galleryCtrl", function($scope, $http) {
	$scope.galleriesConn = "none";
	$scope.galleries = [];
	
	angular.element(document).ready(function() {
		$scope.getGalleries();
	});	
	
	$scope.getGalleries = function() {
		var bUrl = '<c:url value="/gallery/list.json"/>';
		
		if ($scope.galleriesConn == "none") {
			
			var reqPromise = $http.get(bUrl);
			
			$scope.galleriesConn = "loading";
			
			reqPromise.success(function(data, status, headers, config) {
				
				$scope.galleries = data.galleries;
				console.log(data);
				
				$scope.galleriesConn = "none";
				
			});
			reqPromise.error(function(data, status, headers, config) {
				$scope.galleriesConn = "none";
				$scope.error = '<spring:message code="common.msg.error.network.unstable"/>';
			});
		}
	};		

	
});
</script>

<jsp:include page="../include/body-footer.jsp"/>

</body>

</html>