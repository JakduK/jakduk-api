<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
   
<!DOCTYPE html>
<html ng-app="jakdukApp">

<head>
	<title><spring:message code="common.home"/> &middot; <spring:message code="common.jakduk"/></title>
	
	<script src="<%=request.getContextPath()%>/resources/jquery/dist/jquery.min.js"></script>

	<!-- CSS Implementing Plugins -->
	<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/unify/assets/plugins/scrollbar/css/jquery.mCustomScrollbar.css">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/slick-carousel/slick/slick.css">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/slick-carousel/slick/slick-theme.css">
	
    <!-- CSS Page Style -->
    <link rel="stylesheet" href="<%=request.getContextPath()%>/resources/unify/assets/css/pages/page_job_inner.css">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/resources/unify/assets/css/pages/profile.css">
    
    <jsp:include page="../include/html-header.jsp"/>
</head>

<body>
<div class="wrapper" ng-controller="homeCtrl">
	
	<jsp:include page="../include/navigation-header.jsp"/>
	
	<div class="image-block">
		<div class="container mCustomScrollbar" data-mcs-theme="minimal-dark">
			<div class="company-description" ng-bind-html="homeDescription"></div>   
		</div>    
	</div>	
	
	<!--=== Content Part ===-->
	<div class="container content">

<div class="row">
    
<div class="col-md-9">
	<div class="row ">
	                    
		<!--  최근 글 -->                    
		<div class="col-sm-6 sm-margin-bottom-30">
			<div class="headline">
				<h2><spring:message code="home.posts.latest"/></h2> 
				<button class="btn-u btn-u-xs btn-u-default rounded" type="button" onclick="location.href='<c:url value="/board/free"/>'">
					<spring:message code="common.button.more"/>
				</button>
			</div>                        
		
			<!-- Trending -->
			<ul class="list-unstyled blog-trending">
				<li ng-repeat="post in postsLatest">
					<h3 ng-switch="post.status.delete">
						<a ng-switch-when="delete" href="<c:url value="/board/free/{{post.seq}}"/>"><spring:message code="board.msg.deleted"/></a>
						<a ng-switch-default href="<c:url value="/board/free/{{post.seq}}"/>">{{post.subject}}</a>
					</h3>
					<small>
						<span aria-hidden="true" class="icon-user"></span> {{post.writer.username}}
						&nbsp;
						<span ng-if="${timeNow} > intFromObjectId(post.id)">{{dateFromObjectId(post.id) | date:"${dateTimeFormat.date}"}}</span>
						<span ng-if="${timeNow} <= intFromObjectId(post.id)">{{dateFromObjectId(post.id) | date:"${dateTimeFormat.time}"}}</span>
						&nbsp;
						<span aria-hidden="true" class="icon-eye"></span> {{post.views}}							
					</small>                        
				</li>
			</ul>
			<!-- End Trending -->   
		</div>                    
                        
		<!-- 최근 댓글  -->
		<div class="col-sm-6 sm-margin-bottom-30">
			<div class="headline"><h2><spring:message code="home.comments.latest"/></h2></div>
		
			<div class="blog-twitter">
				<div class="blog-twitter-inner" ng-repeat="comment in commentsLatest">
					<strong><span aria-hidden="true" class="icon-user"></span> {{comment.writer.username}}</strong>
					<a href='<c:url value="/board/free/{{comment.boardItem.seq}}"/>'>{{comment.content}}</a>
					<span class="twitter-time"> {{dateFromObjectId(comment.id) | date:"${dateTimeFormat.dateTime}"}}</span>
				</div>
		    </div>                			
		</div> 
		
               </div>
                <!--End Blog Post-->        

            </div>
            <!-- End Left Sidebar -->

            <!-- Right Sidebar -->
        	<div class="col-md-3">
        	
        	<div class="row">
        	
<!-- 최근 가입 회원 -->         
	 <div class="margin-bottom-30 col-md-12 col-sm-6">
    			<div class="headline"><h2><spring:message code="home.members.registered.latest"/></h2></div>
    			
<div class="carousel slide testimonials testimonials-v2" id="testimonials-1">
                    <div class="carousel-inner">
                        <div class="item" ng-repeat="user in usersLatest" ng-class="{'active':$index == 0}">
                            <p>{{user.about}}</p>
                            <div class="testimonial-info">
                                <span class="testimonial-author">
                                    <i aria-hidden="true" class="icon-user-follow"></i> {{user.username}}
                                    <em><i class="fa fa-futbol-o"></i>
                                    {{user.supportFC.names[0].fullName}}</em>
                                </span>
                            </div>
                        </div>                    
                    </div>
                
                    <div class="carousel-arrow">
                        <a data-slide="prev" href="#testimonials-1" class="left carousel-control">
                            <i class="fa fa-angle-left"></i>
                        </a>
                        <a data-slide="next" href="#testimonials-1" class="right carousel-control">
                            <i class="fa fa-angle-right"></i>
                        </a>
                    </div>
                </div>  		
                
                </div>     
                
                
	<!--  백과사전  -->                
	<div class="shadow-wrapper col-md-12 col-sm-6">
		<div class="tag-box tag-box-v1 box-shadow shadow-effect-2">
			<h2>{{encyclopedia.subject}} <span class="label rounded label-orange">{{encyclopedia.kind}}</span></h2>
			<p>{{encyclopedia.content}}</p>
			<!-- 		   
			<button class="btn-u btn-brd btn-brd-hover rounded btn-u-sea" type="button" ng-click="refreshEncyclopedia()">
				<i class="fa fa-refresh"></i>
			</button>
			-->					   
		</div>
	</div>                    
                           
	<!-- 명언 -->	
	<div class="col-sm-12">
		<blockquote class="hero">
			<p><em>"인간의 도덕과 의무에 대해 내가 알고 있는 것은 모두 축구에서 배웠다."</em></p>
			<small><em>알베르 카뮈 , 노벨문학상 수상자</em></small>
		</blockquote>   
	</div>        	
</div>
       
            </div>
            <!-- End Right Sidebar -->
            
	<!-- 최근 사진 -->
	<div class="owl-carousel-v1 owl-work-v1 col-sm-12">
		<div class="headline">
			<h2 class="pull-left"><spring:message code="home.pictures.latest"/></h2>
			<div class="owl-navigation">
				<button style="margin:6px 0px 0px 6px;" class="btn-u btn-u-xs btn-u-default rounded pull-left" type="button" onclick="location.href='<c:url value="/gallery/list"/>'">
    				<spring:message code="common.button.more"/>
    			</button>
    				                     
				<div class="customNavigation">
					<a class="owl-btn prev-v2" ng-click="slickConfig.method.slickPrev()"><i class="fa fa-angle-left"></i></a>
					<a class="owl-btn next-v2" ng-click="slickConfig.method.slickNext()"><i class="fa fa-angle-right"></i></a>
				</div>
			</div><!--/navigation-->
		</div>
		
		<slick settings="slickConfig" ng-if="dataLoaded">
			<div ng-repeat="image in galleriesLatest">
    			<div class="simple-block">
	    			<a ng-href="<%=request.getContextPath()%>/gallery/view/{{image.id}}">
						<img style="width:225px; margin:5px;" ng-src="<%=request.getContextPath()%>/gallery/thumbnail/{{image.id}}">
					</a>
					<p class="img-responsive">{{image.name}}</p>
				</div>  
				
			</div>	
		</slick>			
       </div> 
            
	</div>	

    </div><!--/container-->		
    <!--=== End Content Part ===-->				

	<jsp:include page="../include/footer.jsp"/>
</div><!-- /.container -->

<!-- Placed at the end of the document so the pages load faster -->
<script src="<%=request.getContextPath()%>/resources/angular-sanitize/angular-sanitize.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/resources/unify/assets/plugins/smoothScroll.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/resources/unify/assets/plugins/scrollbar/js/jquery.mCustomScrollbar.concat.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/resources/slick-carousel/slick/slick.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/resources/angular-slick-carousel/dist/angular-slick.min.js"></script>

<script type="text/javascript">

var jakdukApp = angular.module('jakdukApp', ['ngSanitize', 'slickCarousel']);

jakdukApp.controller("homeCtrl", function($scope, $http) {
	$scope.encyclopedia = {};
	$scope.encyclopediaConn = "none";
	$scope.dataLatestConn = "none";
	$scope.postsLatest = [];
	$scope.usersLatest = [];
	$scope.commentsLatest = [];
	$scope.galleriesLatest = [];
	$scope.homeDescription = {};
	
	$scope.slickConfig = {
		infinite: false,
		autoplay: true,
		draggable: true,
		autoplaySpeed: 3000,
		//slidesToScroll : 2,
		arrows : false,
		variableWidth: true,
		lazyLoad : 'ondemand',
		//centerPadding: '60px',    
		method: {},
		event: {
			beforeChange: function (event, slick, currentSlide, nextSlide) {},
			afterChange: function (event, slick, currentSlide, nextSlide) {}
		}
	};
	
	angular.element(document).ready(function() {
		$scope.refreshEncyclopedia();
		$scope.getDataLatest();		
		
		App.init();
		App.initScrollBar();
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
		var bUrl = '<c:url value="/home/data/latest.json" />';
		
		if ($scope.dataLatestConn == "none") {
			
			var reqPromise = $http.get(bUrl);
			
			$scope.dataLatestConn = "loading";
			
			reqPromise.success(function(data, status, headers, config) {
				
				$scope.postsLatest = data.posts;
				$scope.usersLatest = data.users;
				$scope.commentsLatest = data.comments;
				$scope.galleriesLatest = data.galleries;
				$scope.homeDescription = data.homeDescription.desc;
				
				$scope.dataLatestConn = "none";
				$scope.dataLoaded = true;
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