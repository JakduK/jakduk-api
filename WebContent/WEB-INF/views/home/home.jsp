<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
   
<!DOCTYPE html>
<html ng-app="jakdukApp">

<head>
	<title><spring:message code="common.home"/> &middot; <spring:message code="common.jakduk"/></title>

	<!-- CSS Implementing Plugins -->
	<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/unify/assets/plugins/owl-carousel/owl-carousel/owl.carousel.css">
 <link rel="stylesheet" href="<%=request.getContextPath()%>/resources/unify/assets/plugins/layer-slider/layerslider/css/layerslider.css">	
	<!-- CSS Page Style -->    
    <link rel="stylesheet" href="<%=request.getContextPath()%>/resources/unify/assets/css/pages/blog_magazine.css">	
	
	<jsp:include page="../include/html-header.jsp"/>
</head>

<body>
<div class="wrapper">
	
	<jsp:include page="../include/navigation-header.jsp"/>
	
    <!--=== Slider ===-->
    <div id="layerslider" style="width: 100%; height: 440px; margin: 0px auto;">
        <!-- First slide -->
        <div class="ls-slide" data-ls="slidedelay:10000;transition2d:5;">
            <img src="<%=request.getContextPath()%>/resources/jakduk/img/bg01.jpg" class="ls-bg" alt="Slide background"/>

<p class="ls-l" style="font-size:35px; background: rgba(0,0,0,.2); color:#fff; top:50px; left: 100px;"
	data-ls="offsetxin: -10; fadein: true; durationin : 1000; durationout : 350; delayin : 1000;">
    K리그 클래식 1R
</p>
<p class="ls-l" style="top:120px; left:100px; background: rgba(0,0,0,.2); font-size:25px; line-height:37px; color:#fff;" 
	data-ls="offsetxin: -10; fadein: true; durationin : 1000; durationout : 350; delayin : 1000;">
	2015-03-07(토)<br>
	15:00 전북 vs 성남, 전주 월드컵<br>
	14:00 인천 vs 광주, 인천 전용<br>
	16:00 부산 vs 대전, 부산 아시아드<br>
</p>
<p class="ls-l" style="top:120px; left:500px; background: rgba(0,0,0,.2); font-size:25px; line-height:37px; color:#fff;" 
	data-ls="offsetxin: -10; fadein: true; durationin : 1000; durationout : 350; delayin : 1000;">
	2015-03-08(일)<br>
	14:00 전남 vs 제주, 광양 전용<br>
	14:00 수원 vs 포항, 수원 월드컵<br>
	16:00 울산 vs 서울, 울산 문수<br>
</p>
<p class="ls-l" style="top:350px; left:650px; background: rgba(0,0,0,.2); font-size:18px; line-height:37px; color:#fff;" 
	data-ls="offsetxin: -10; fadein: true; durationin : 1000; durationout : 350; delayin : 1000;">
	사진제공 : 상암뒤
</p>
        </div>	
        
      <!--Second Slide-->
        <div class="ls-slide" data-ls="slidedelay:10000;transition2d:5;">
            <img src="<%=request.getContextPath()%>/resources/jakduk/img/bg.jpg" class="ls-bg" alt="Slide background"/>

<p class="ls-l" style="font-size:35px; background: rgba(0,0,0,.2); color:#fff; top:50px; left: 70px;"
	data-ls="offsetxin: -10; fadein: true; durationin : 1000; durationout : 350; delayin : 1000;">
    AFC 챔피언스리그 3R
</p>
<p class="ls-l" style="top:120px; left:70px; background: rgba(0,0,0,.2); font-size:22px; line-height:37px; color:#fff;" 
	data-ls="offsetxin: -10; fadein: true; durationin : 1000; durationout : 350; delayin : 1000;">
	2015-03-17(화)<br>
	18:30 수원 vs 브리즈번, 로비나 스타디움<br>
	19:00 전북 vs 빈즈엉, 전주 월드컵<br>		
</p>
<p class="ls-l" style="top:120px; left:480px; background: rgba(0,0,0,.2); font-size:22px; line-height:37px; color:#fff;" 
	data-ls="offsetxin: -10; fadein: true; durationin : 1000; durationout : 350; delayin : 1000;">
	2015-03-18(수)<br>
	19:30 서울 vs 웨스턴 시드니, 서울 월드컵<br>
	20:30 광저우 푸리 vs 성남, 유시유샨 스타디움<br>
</p>
 
        </div>                
	
	</div>	
	
	<!--=== Content Part ===-->
	<div class="container content" ng-controller="homeCtrl">
	


<div class="row blog-page">    
            <!-- Left Sidebar -->
        	<div class="col-md-9 md-margin-bottom-30">
                <!--Blog Post-->
                <div class="row blog blog-medium magazine-page margin-bottom-30">
                    
<!--  최근 글 -->                    
                        <div class="col-md-6 margin-bottom-30">
<div class="headline">
	<h2><spring:message code="home.posts.latest"/></h2>
    	<button class="btn-u btn-u-xs btn-u-default rounded" type="button" onclick="location.href='<c:url value="/board/free"/>'">
    		<spring:message code="common.button.more"/>
    	</button>	
</div>
        
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
                        
<!-- 최근 댓글  -->
<div class="col-md-6">
                    <div class="headline">
                    	<h2><spring:message code="home.comments.latest"/></h2>
                    </div>
<div class="blog-twitter">
                    <div class="blog-twitter-inner" ng-repeat="comment in commentsLatest">
                        <strong>{{comment.writer.username}}</strong>
                        <a href="<c:url value="/board/free/{{comment.boardItem.seq}}"/>">{{comment.content}}</a>
					<span class="twitter-time" ng-if="${timeNow} > intFromObjectId(comment.id)">{{dateFromObjectId(comment.id) | date:"${dateTimeFormat.date}"}}</span>
					<span class="twitter-time" ng-if="${timeNow} <= intFromObjectId(comment.id)">{{dateFromObjectId(comment.id) | date:"${dateTimeFormat.time}"}}</span>                        
                    </div>
                </div>                			
 
            </div> 

               </div>
                <!--End Blog Post-->                 
   <!-- 최근 사진 -->
   
        <div class="owl-carousel-v1 owl-work-v1">
            <div class="headline">
            <h2 class="pull-left"><spring:message code="home.pictures.latest"/>
            </h2>
            
                <div class="owl-navigation">

    	<button style="margin:6px 0px 0px 6px;" class="btn-u btn-u-xs btn-u-default rounded pull-left" type="button" onclick="location.href='<c:url value="/gallery/list"/>'">
    		<spring:message code="common.button.more"/>
    	</button>	                     
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


            </div>
            <!-- End Left Sidebar -->

            <!-- Right Sidebar -->
        	<div class="col-md-3">
        	
<!-- 최근 가입 회원 -->         
	 <div class="margin-bottom-30">
    			<div class="headline"><h2><spring:message code="home.members.registered.latest"/></h2></div>
    			
<div class="carousel slide testimonials testimonials-v2" id="testimonials-1">
                    <div class="carousel-inner">
                        <div class="item" ng-repeat="user in usersLatest" ng-class="{'active':$index == 0}">
                            <p>{{user.about}}</p>
                            <div class="testimonial-info">
                                <span class="testimonial-author">
                                    {{user.username}}
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
		<div class="shadow-wrapper">
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
<blockquote class="hero">
                    <p><em>"인간의 도덕과 의무에 대해 내가 알고 있는 것은 모두 축구에서 배웠다."</em></p>
                    <small><em>알베르 카뮈 , 노벨문학상 수상자</em></small>
                </blockquote>   

        	
       
            </div>
            <!-- End Right Sidebar -->
        </div>	

    </div><!--/container-->		
    <!--=== End Content Part ===-->				

	
	<jsp:include page="../include/footer.jsp"/>
</div><!-- /.container -->

<!-- Placed at the end of the document so the pages load faster -->
<script src="<%=request.getContextPath()%>/resources/jquery/dist/jquery.min.js"></script>
<!-- JS Implementing Plugins -->
<script type="text/javascript" src="<%=request.getContextPath()%>/resources/unify/assets/plugins/owl-carousel/owl-carousel/owl.carousel.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/resources/unify/assets/plugins/layer-slider/layerslider/js/greensock.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/resources/unify/assets/plugins/layer-slider/layerslider/js/layerslider.transitions.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/resources/unify/assets/plugins/layer-slider/layerslider/js/layerslider.kreaturamedia.jquery.js"></script>
<!-- JS Page Level -->           
<script type="text/javascript" src="<%=request.getContextPath()%>/resources/unify/assets/js/plugins/owl-recent-works.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/resources/unify/assets/js/plugins/layer-slider.js"></script>
<script type="text/javascript">

var jakdukApp = angular.module("jakdukApp", []);

jakdukApp.controller("homeCtrl", function($scope, $http) {
	$scope.encyclopedia = {};
	$scope.encyclopediaConn = "none";
	$scope.dataLatestConn = "none";
	$scope.postsLatest = [];
	$scope.usersLatest = [];
	$scope.commentsLatest = [];
	//$scope.galleriesLatest = [];
	
	angular.element(document).ready(function() {
		$scope.refreshEncyclopedia();
		$scope.getDataLatest();		
		
		App.init();
		
		LayerSlider.initLayerSlider();
		OwlRecentWorks.initOwlRecentWorksV2();
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
				$scope.commentsLatest = data.comments;
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