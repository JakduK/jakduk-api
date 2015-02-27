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
    <link rel="stylesheet" href="<%=request.getContextPath()%>/resources/unify/assets/plugins/flexslider/flexslider.css">  
    <link rel="stylesheet" href="<%=request.getContextPath()%>/resources/unify/assets/plugins/parallax-slider/css/parallax-slider.css">
</head>

<body>
<div class="wrapper jakduk-home" ng-controller="homeCtrl">
	
	<jsp:include page="../include/navigation-header.jsp"/>
	
    <!--=== Slider ===-->
    <div class="slider-inner">
        <div id="da-slider" class="da-slider">
            <div class="da-slide">
                <h2>K LEAGUE JAKDU KING</h2>
                <p><i>Lorem ipsum dolor amet</i> <br /> <i>tempor incididunt ut</i> <br /> <i>veniam omnis </i></p>
                <div class="da-img"><img class="img-responsive" src="<%=request.getContextPath()%>/resources/unify/assets/plugins/parallax-slider/img/1.png" alt=""></div>
            </div>
            <div class="da-slide">
                <h2><i>RESPONSIVE VIDEO</i> <br /> <i>SUPPORT AND</i> <br /> <i>MANY MORE</i></h2>
                <p><i>Lorem ipsum dolor amet</i> <br /> <i>tempor incididunt ut</i></p>
                <div class="da-img">
                    <iframe src="http://player.vimeo.com/video/47911018" width="530" height="300" frameborder="0" webkitAllowFullScreen mozallowfullscreen allowFullScreen></iframe> 
                </div>
            </div>
            <div class="da-slide">
                <h2><i>USING BEST WEB</i> <br /> <i>SOLUTIONS WITH</i> <br /> <i>HTML5/CSS3</i></h2>
                <p><i>Lorem ipsum dolor amet</i> <br /> <i>tempor incididunt ut</i> <br /> <i>veniam omnis </i></p>
                <div class="da-img"><img src="assets/plugins/parallax-slider/img/html5andcss3.png" alt="image01" /></div>
            </div>
            <div class="da-arrows">
                <span class="da-arrows-prev"></span>
                <span class="da-arrows-next"></span>        
            </div>
        </div>
    </div><!--/slider-->
    <!--=== End Slider ===-->	
	
	<!--=== Content Part ===-->
	<div class="container content">
	
<blockquote class="hero">
                    <p><em>"인간의 도덕과 의무에 대해 내가 알고 있는 것은 모두 축구에서 배웠다. (After many years during which I saw many things, what I know most surely about morality and the duty of man I owe to sport and learned it in the RUA.)"</em></p>
                    <small><em>알베르 카뮈 , 노벨문학상 수상자</em></small>
                </blockquote>
	
		<div class="row magazine-page">
		<div class="col-md-9">
            
<div class="magazine-news">
                    <div class="row">
                        <div class="col-md-6">

<div class="headline"><h2><spring:message code="home.posts.latest"/></h2></div>
        
					<div ng-repeat="post in postsLatest">
					<div class="magazine-mini-news">
					<h3 ng-switch="post.status.delete">
						<a ng-switch-when="delete" href="<c:url value="/board/free/{{post.seq}}"/>"><spring:message code="board.msg.deleted"/></a>
					<a ng-switch-default href="<c:url value="/board/free/{{post.seq}}"/>">{{post.subject}}</a>
						    </h3>                        
					<div class="post-author">
					<i class="fa fa-user"></i> {{post.writer.username}}		
					<i class="fa fa-clock-o"></i>
					<span ng-if="${timeNow} > intFromObjectId(post.id)">{{dateFromObjectId(post.id) | date:"${dateTimeFormat.date}"}}</span>
					<span ng-if="${timeNow} <= intFromObjectId(post.id)">{{dateFromObjectId(post.id) | date:"${dateTimeFormat.time}"}}</span>							
					</div>
					</div>        
					</div>							
                      
                        </div>
                        
<div class="col-md-6">
                
                    <div class="headline"><h2>Latest Tweets</h></div>
<div class="blog-twitter">
                    <div class="blog-twitter-inner">
                        <i class="icon-twitter"></i>
                        <a href="#">@htmlstream</a> 
                        At vero seos etodela ccusamus et iusto odio dignissimos. 
                        <a href="#">http://t.co/sBav7dm</a> 
                        <span class="twitter-time">5 hours ago</span>
                    </div>
                    <div class="blog-twitter-inner">
                        <i class="icon-twitter"></i>
                        <a href="#">@htmlstream</a> 
                        At vero eos et accusamus et iusto odio dignissimos. 
                        <a href="#">http://t.co/sBav7dm</a> 
                        <span class="twitter-time">5 hours ago</span>
                    </div>
                    <div class="blog-twitter-inner">
                        <i class="icon-twitter"></i>
                        <a href="#">@htmlstream</a> 
                        At vero eos et accusamus et iusto odio dignissimos. 
                        <a href="#">http://t.co/sBav7dm</a> 
                        <span class="twitter-time">5 hours ago</span>
                    </div>
                    <div class="blog-twitter-inner">
                        <i class="icon-twitter"></i>
                        <a href="#">@htmlstream</a> 
                        At vero eos et accusamus et iusto odio dignissimos. 
                        <a href="#">http://t.co/sBav7dm</a> 
                        <span class="twitter-time">5 hours ago</span>
                    </div>
                </div>                			
 
            </div>
                                    
                    </div>
                </div>            
		 
		 </div>
		 
		 <div class="col-md-3">
		 
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
		 
		 <div class="margin-bottom-30">
    			<div class="headline"><h2><spring:message code="home.members.registered.latest"/></h2></div>
    			
<div class="carousel slide testimonials testimonials-v1" id="testimonials-1">
                    <div class="carousel-inner">
                        <div class="item" ng-repeat="user in usersLatest" ng-class="{'active':$index == 0}">
                            <p>{{user.about}}</p>
                            <div class="testimonial-info">
                                <span class="testimonial-author">
                                    {{user.username}}
                                    <em>Web Developer, Unify Theme.</em>
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
                
		<div class="shadow-wrapper margin-bottom-30">
			<div class="tag-box tag-box-v1 box-shadow shadow-effect-2">
		<div class="heading">
			<h2><spring:message code="home.encyclopedia"/></h2>
		</div> 			
			
		   <h2>{{encyclopedia.subject}} <span class="label rounded label-sea">{{encyclopedia.kind}}</span></h2>
		   <p>{{encyclopedia.content}}</p>
		   
			<button class="btn-u btn-brd btn-brd-hover rounded btn-u-sea" type="button" ng-click="refreshEncyclopedia()">
				<i class="fa fa-refresh"></i>
			</button>		   
			</div>
		</div>                
                
		 
		 </div>		 
         
        </div>
        
   <!-- 최근 사진 -->
        <div class="owl-carousel-v1 owl-work-v1 margin-bottom-40">
            <div class="headline"><h2 class="pull-left"><spring:message code="home.pictures.latest"/></h2>
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
<!-- JS Implementing Plugins -->
<script type="text/javascript" src="<%=request.getContextPath()%>/resources/unify/assets/plugins/back-to-top.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/resources/unify/assets/plugins/flexslider/jquery.flexslider-min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/resources/unify/assets/plugins/parallax-slider/js/modernizr.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/resources/unify/assets/plugins/parallax-slider/js/jquery.cslider.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/resources/unify/assets/plugins/owl-carousel/owl-carousel/owl.carousel.js"></script>
<!-- JS Page Level -->           
<script type="text/javascript" src="<%=request.getContextPath()%>/resources/unify/assets/js/app.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/resources/unify/assets/js/plugins/owl-recent-works.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/resources/unify/assets/js/plugins/parallax-slider.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/resources/unify/assets/js/plugins/style-switcher.js"></script>
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
		App.initSliders();
        StyleSwitcher.initStyleSwitcher();      
        ParallaxSlider.initParallaxSlider();
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