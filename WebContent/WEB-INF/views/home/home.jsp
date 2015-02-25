<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
   
<!DOCTYPE html>
<html ng-app="jakdukApp">
<head>
	<title><spring:message code="common.home"/> &middot; <spring:message code="common.jakduk"/></title>
	<jsp:include page="../include/html-header.jsp"/>
</head>

<body>
<div class="wrapper jakduk-home" ng-controller="homeCtrl">
	
	<jsp:include page="../include/navigation-header.jsp"/>
	
	<!--=== Content Part ===-->
	<div class="container content">
	
	<!-- 백과사전 -->
	<div class="jumbotron">
		<h4>{{encyclopedia.subject}} <small><span class="label label-primary">{{encyclopedia.kind}}</span></small></h4>
		<h5>{{encyclopedia.content}}</h5>
	 	<button type="button" class="btn btn-default" ng-click="refreshEncyclopedia()">
			<span class="glyphicon glyphicon-refresh"></span>
		</button>
	</div>  
	
	<!-- 최근 사진 -->
	<div class="headline"><h2><spring:message code="home.pictures.latest"/></h2></div>
	<div class="row margin-bottom-20">
		<div class="col-md-3 col-sm-6" ng-repeat="gallery in galleriesLatest">
	   <div class="thumbnails thumbnail-style thumbnail-kenburn">
	   	<div class="thumbnail-img">
	      <div class="overflow-hidden">
						<img ng-src="<%=request.getContextPath()%>/gallery/thumbnail/{{gallery.id}}" alt="{{gallery.name}}">
	      </div>
					<a class="btn-more hover-effect" href="<%=request.getContextPath()%>/gallery/view/{{gallery.id}}">read more +</a>					
				</div>
	     <div class="caption">
	      <h3><a class="hover-effect" href="<%=request.getContextPath()%>/gallery/view/{{gallery.id}}">{{gallery.name}}</a></h3>
	      <p><span class="glyphicon glyphicon-user"></span> {{gallery.writer.username}}</p>
	     </div>
	   </div>
		</div>
	</div>
	
	<div class="row margin-bottom-30">
		<!-- 최근 글 -->
		<div class="col-6 col-sm-6 col-lg-6 md-margin-bottom-40">
	   <!-- Latest -->
			<div class="posts">
			    <div class="headline"><h2><spring:message code="home.posts.latest"/></h2></div>
			    <ul class="list-unstyled latest-list">
	        <li ng-repeat="post in postsLatest">
							<span ng-switch="post.status.delete">
								<a ng-switch-when="delete" href="<c:url value="/board/free/{{post.seq}}"/>"><spring:message code="board.msg.deleted"/></a>
								<a ng-switch-default href="<c:url value="/board/free/{{post.seq}}"/>">{{post.subject}}</a>
           </span>
            <small ng-if="${timeNow} > intFromObjectId(post.id)">{{dateFromObjectId(post.id) | date:"${dateTimeFormat.date}"}}</small>
            <small ng-if="${timeNow} <= intFromObjectId(post.id)">{{dateFromObjectId(post.id) | date:"${dateTimeFormat.time}"}}</small>								
	        </li>
			    </ul>
			</div>
	 		<!-- End Latest -->		
		</div>
		
		<!-- 최근 가입 회원 -->
		<div class="col-6 col-sm-6 col-lg-6">
			<div class="panel panel-grey">
				<div class="panel-heading">
					<h3 class="panel-title"><i class="fa fa-tasks"></i> 
				 		<spring:message code="home.members.registered.latest"/>
			 		</h3>
				</div>			
				<table class="table table-hover table-condensed">
				<thead>
					<tr>
						<th class="col-xs-1"><spring:message code="user.nickname"/></th>
						<th class="col-xs-5"><spring:message code="user.comment"/></th>
					</tr>
				</thead>
				<tbody>
					<tr ng-repeat="user in usersLatest">
						<td>{{user.username}}</td>
						<td>{{user.about}}</td>
					</tr>	
				</tbody>
				</table>				
			</div>
		</div><!--/span-->
	</div><!--/row-->
	
	</div>
	
	<jsp:include page="../include/footer.jsp"/>
</div><!-- /.container -->

<!-- Bootstrap core JavaScript ================================================== -->
<!-- Placed at the end of the document so the pages load faster -->
<script src="<%=request.getContextPath()%>/resources/jquery/dist/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/resources/bootstrap/dist/js/bootstrap.min.js"></script>
<script type="text/javascript">

var jakdukApp = angular.module("jakdukApp", []);

jakdukApp.controller("homeCtrl", function($scope, $http) {
	$scope.encyclopedia = {};
	$scope.encyclopediaConn = "none";
	$scope.dataLatestConn = "none";
	$scope.postsLatest = [];
	$scope.usersLatest = [];
	$scope.galleriesLatest = [];
	
	$scope.refreshEncyclopedia = function() {
		var bUrl = '<c:url value="/home/jumbotron.json?lang=${pageContext.response.locale}"/>';
		
		if ($scope.encyclopediaConn == "none") {
			
			var reqPromise = $http.get(bUrl);
			
			$scope.encyclopediaConn = "loading";
			
			reqPromise.success(function(data, status, headers, config) {
				if (data.encyclopedia != null) {
					if (data.encyclopedia.kind == "player") {
						$scope.encyclopedia.kind = '<spring:message code="home.kind.best.player"/>';
					} else if (data.encyclopedia.kind == "book") {
						$scope.encyclopedia.kind = '<spring:message code="home.kind.recommend.book"/>';
					}
					
					$scope.encyclopedia.subject = data.encyclopedia.subject;
					$scope.encyclopedia.content = data.encyclopedia.content;
				}
				
				$scope.encyclopediaConn = "none";
				
			});
			reqPromise.error(function(data, status, headers, config) {
				$scope.encyclopediaConn = "none";
				$scope.error = '<spring:message code="common.msg.error.network.unstable"/>';
			});
		}
	};
	
	$scope.getDataLatest = function() {
		var bUrl = '<c:url value="/home/data/latest.json"/>';
		
		if ($scope.dataLatestConn == "none") {
			
			var reqPromise = $http.get(bUrl);
			
			$scope.dataLatestConn = "loading";
			
			reqPromise.success(function(data, status, headers, config) {
				
				$scope.postsLatest = data.posts;
				$scope.usersLatest = data.users;
				$scope.galleriesLatest = data.galleries;
				
				$scope.dataLatestConn = "none";
				
			});
			reqPromise.error(function(data, status, headers, config) {
				$scope.dataLatestConn = "none";
				$scope.error = '<spring:message code="common.msg.error.network.unstable"/>';
			});
		}
	};	
	
	$scope.objectIdFromDate = function(date) {
		return Math.floor(date.getTime() / 1000).toString(16) + "0000000000000000";
	};
	
	$scope.dateFromObjectId = function(objectId) {
		return new Date(parseInt(objectId.substring(0, 8), 16) * 1000);
	};
	
	$scope.intFromObjectId = function(objectId) {
		return parseInt(objectId.substring(0, 8), 16) * 1000;
	};
	
	$scope.refreshEncyclopedia();
	$scope.getDataLatest();

});
</script>

<jsp:include page="../include/body-footer.jsp"/>

</body>
</html>