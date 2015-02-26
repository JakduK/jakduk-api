<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
   
<!DOCTYPE html>
<html ng-app="jakdukApp">

<head>
	<title><spring:message code="common.home"/> &middot; <spring:message code="common.jakduk"/></title>
	<jsp:include page="../include/html-header.jsp"/>
	
	<!-- CSS Implementing Plugins -->
	<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/unify/assets/plugins/owl-carousel/owl-carousel/owl.carousel.css">
	<!-- CSS Page Style -->    
    <link rel="stylesheet" href="<%=request.getContextPath()%>/resources/unify/assets/css/pages/blog_magazine.css">	
</head>

<body>
<div class="wrapper jakduk-home" ng-controller="homeCtrl">
	
	<jsp:include page="../include/navigation-header.jsp"/>
	
	<!--=== Content Part ===-->
	<div class="container content">
	
		<!-- 백과사전 -->
<!-- 		
		<div class="jumbotron">
			<h4>{{encyclopedia.subject}} <small><span class="label label-primary">{{encyclopedia.kind}}</span></small></h4>
			<h5>{{encyclopedia.content}}</h5>
		 	<button type="button" class="btn btn-default" ng-click="refreshEncyclopedia()">
				<span class="glyphicon glyphicon-refresh"></span>
			</button>
		</div>  
 -->		
   
		<div class="shadow-wrapper margin-bottom-30">
			<div class="tag-box tag-box-v1 box-shadow shadow-effect-2">
		   <h2>{{encyclopedia.subject}}</h2>
		   <p>{{encyclopedia.content}}</p>
			</div>
		</div>
		
		<div class="row magazine-page margin-bottom-30">
		
            <!-- Begin Content -->
            <div class="col-6 col-sm-6 col-lg-6 md-margin-bottom-40">
                <div class="tag-box tag-box-v1">
                    <div class="heading heading-v3">
                        <h2><spring:message code="home.posts.latest"/></h2>
                    </div>
                    	<div ng-repeat="post in postsLatest">
                        <div class="magazine-mini-news">
								<h3 ng-switch="post.status.delete">
									<a ng-switch-when="delete" href="<c:url value="/board/free/{{post.seq}}"/>"><spring:message code="board.msg.deleted"/></a>
									<a ng-switch-default href="<c:url value="/board/free/{{post.seq}}"/>">{{post.subject}}</a>
							    </h3>                        
                            <div class="post-author">
						<i class="fa fa-user"></i> {{post.writer.username}}		
						/						
						<i class="fa fa-eye"></i>
						<span ng-if="${timeNow} > intFromObjectId(post.id)">{{dateFromObjectId(post.id) | date:"${dateTimeFormat.date}"}}</span>
						<span ng-if="${timeNow} <= intFromObjectId(post.id)">{{dateFromObjectId(post.id) | date:"${dateTimeFormat.time}"}}</span>							
                            </div>
                        </div>        
           			   <hr>
           			   </div>
                </div>
            </div>
            <!-- End Content -->
            
            	<!-- 최근 글 -->
       <!--Info Block-->
          <div class="col-6 col-sm-6 col-lg-6 md-margin-bottom-40">
              <div class="funny-boxes funny-boxes-top-sea">
                                  <div class="heading heading-v3">
                        <h2><spring:message code="home.posts.latest"/></h2>
                    </div>
              <h2><a href='<c:url value="/board/free/"/>'><spring:message code="home.posts.latest"/></a></h2>
					<!-- Latest -->
					<div class="posts">
					    <ul class="list-unstyled latest-list">
					      <li ng-repeat="post in postsLatest">
									<span ng-switch="post.status.delete">
										<a ng-switch-when="delete" href="<c:url value="/board/free/{{post.seq}}"/>"><spring:message code="board.msg.deleted"/></a>
					<a ng-switch-default href="<c:url value="/board/free/{{post.seq}}"/>">{{post.subject}}</a>
					    </span>
					<small>
						<i class="fa fa-user"></i> {{post.writer.username}}								
						<i class="fa fa-eye"></i>
						<span ng-if="${timeNow} > intFromObjectId(post.id)">{{dateFromObjectId(post.id) | date:"${dateTimeFormat.date}"}}</span>
					<span ng-if="${timeNow} <= intFromObjectId(post.id)">{{dateFromObjectId(post.id) | date:"${dateTimeFormat.time}"}}</span>							
									</small>
					      </li>
					    </ul>
					</div>
						<!-- End Latest -->	                         
              </div>
              
          </div>
          <!--End Info Block-->		
          	
          	<!-- 최근 가입 회원 -->
          <div class="funny-boxes funny-boxes-top-yellow">
             <h2><a href='<c:url value="/gallery/home/"/>'><spring:message code="home.members.registered.latest"/></a></h2>
		   <!-- Latest -->
				<div class="posts">
				    <ul class="list-unstyled latest-list">
		        <li ng-repeat="user in usersLatest">
		        {{user.about}}
								<small>
									<i class="fa fa-user"></i> {{user.username}}								
									<i class="fa fa-eye"></i>
									<span ng-if="${timeNow} > intFromObjectId(user.id)">{{dateFromObjectId(user.id) | date:"${dateTimeFormat.date}"}}</span>
									<span ng-if="${timeNow} <= intFromObjectId(user.id)">{{dateFromObjectId(user.id) | date:"${dateTimeFormat.time}"}}</span>							
								</small>
		        </li>
				    </ul>
				</div>
		 		<!-- End Latest -->
          </div>		
         
        </div><!--/row-->
        
   <!-- 최근 사진 -->
        <div class="owl-carousel-v1 owl-work-v1 margin-bottom-40">
            <div class="headline"><h2 class="pull-left"><i class="icon-custom icon-sm icon-bg-u fa fa-lightbulb-o"></i><spring:message code="home.pictures.latest"/></h2>
                <div class="owl-navigation">
                    <div class="customNavigation">
                        <a class="owl-btn prev-v2"><i class="fa fa-angle-left"></i></a>
                        <a class="owl-btn next-v2"><i class="fa fa-angle-right"></i></a>
                    </div>
                </div><!--/navigation-->
            </div>



            <div class="owl-recent-works-v1">
            	<c:forEach var="gallery" items="${galleries}">
                <div class="item">                
					<a href="<%=request.getContextPath()%>/gallery/view/${gallery.id}">
                        <em class="overflow-hidden">
                            <img class="img-responsive" src="<%=request.getContextPath()%>/gallery/thumbnail/${gallery.id}" alt="${gallery.name}">
                        </em>    
                        <span>
                            <strong class="text-overflow">${gallery.name}</strong>
								<i class="fa fa-user"></i> <i>${gallery.writer.username}</i>
                        </span>
                    </a>    
                </div>
				</c:forEach>                
            </div>
        </div>    
        <!-- End Recent Works -->           
    </div><!--/container-->		
    <!--=== End Content Part ===-->				

	
	<jsp:include page="../include/footer.jsp"/>
</div><!-- /.container -->

<!-- Bootstrap core JavaScript ================================================== -->
<!-- Placed at the end of the document so the pages load faster -->
<script src="<%=request.getContextPath()%>/resources/jquery/dist/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/resources/bootstrap/dist/js/bootstrap.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/resources/unify/assets/plugins/owl-carousel/owl-carousel/owl.carousel.js"></script>
<!-- JS Page Level -->           
<script type="text/javascript" src="<%=request.getContextPath()%>/resources/unify/assets/js/app.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/resources/unify/assets/js/plugins/owl-recent-works.js"></script>
<script type="text/javascript">

var jakdukApp = angular.module("jakdukApp", []);

jakdukApp.controller("homeCtrl", function($scope, $http) {
	$scope.encyclopedia = {};
	$scope.encyclopediaConn = "none";
	$scope.dataLatestConn = "none";
	$scope.postsLatest = [];
	$scope.usersLatest = [];
	//$scope.galleriesLatest = [];
	
	angular.element(document).ready(function() {
		$scope.refreshEncyclopedia();
		$scope.getDataLatest();		
		App.init();
		OwlRecentWorks.initOwlRecentWorksV1();	    
	});	
	
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
				//$scope.galleriesLatest = data.galleries;
				
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
});

</script>

<jsp:include page="../include/body-footer.jsp"/>

</body>
</html>