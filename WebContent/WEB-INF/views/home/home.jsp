<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%> 
   
<!DOCTYPE html>
<html ng-app="jakdukApp">
<head>  	
	<jsp:include page="../include/html-header.jsp"/>
	
</head>

<body>
<div class="container" ng-controller="homeCtrl">

<jsp:include page="../include/navigation-header.jsp"/>

<div class="jumbotron">
	<h4>{{encyclopedia.subject}} <small><span class="label label-primary">{{encyclopedia.kind}}</span></small></h4>
	<h5>{{encyclopedia.content}}</h5>
 	<button type="button" class="btn btn-default" ng-click="refreshEncyclopedia()">
		<span class="glyphicon glyphicon-refresh"></span>
	</button>
</div>  

<div class="row">
	<div class="col-6 col-sm-6 col-lg-6">
		<div class="panel panel-info">
			<div class="panel-heading"><strong><a href="<c:url value="/board/free"/>"><spring:message code="home.posts.latest"/></a></strong></div>		
			<table class="table table-hover table-condensed">
				<tr>
					<th><spring:message code="board.subject"/></th>
					<th><spring:message code="board.date"/></th>
				</tr>		
				<tr ng-repeat="post in postsLatest">
					<td><a href="<c:url value="/board/free/{{post.seq}}"/>">{{post.subject}}</a></td>
					<td ng-if="${timeNow} > intFromObjectId(post.id)">{{dateFromObjectId(post.id) | date:"${dateTimeFormat.date}"}}</td>
					<td ng-if="${timeNow} <= intFromObjectId(post.id)">{{dateFromObjectId(post.id) | date:"${dateTimeFormat.time}"}}</td>
				</tr>
			</table>
		</div>
	</div>
	
	<div class="col-6 col-sm-6 col-lg-6">
		<div class="panel panel-info">
			<div class="panel-heading"><strong><spring:message code="home.members.registered.latest"/></strong></div>
			<table class="table table-hover table-condensed">
				<tr>
					<th><spring:message code="user.nickname"/></th>
					<th><spring:message code="user.comment"/></th>
				</tr>
				<tr ng-repeat="user in usersLatest">
					<td>{{user.username}}</td>
					<td>{{user.about}}</td>
				</tr>				
			</table>				
		</div>
	</div><!--/span-->
</div><!--/row-->

<jsp:include page="../include/footer.jsp"/>
</div><!-- /.container -->

<!-- Bootstrap core JavaScript ================================================== -->
<!-- Placed at the end of the document so the pages load faster -->
<script src="<%=request.getContextPath()%>/resources/jquery/js/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/resources/bootstrap/js/bootstrap.min.js"></script>
<script type="text/javascript">

var jakdukApp = angular.module("jakdukApp", []);

jakdukApp.controller("homeCtrl", function($scope, $http) {
	$scope.encyclopedia = {};
	$scope.encyclopediaConn = "none";
	$scope.dataLatestConn = "none";
	$scope.postsLatest = [];
	$scope.usersLatest = [];
	
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