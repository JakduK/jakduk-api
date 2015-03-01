<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>    
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<!DOCTYPE html>
<html ng-app="jakdukApp">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>${post.subject} - <spring:message code="board.name.free"/> &middot; <spring:message code="common.jakduk"/></title>
	<jsp:include page="../include/html-header.jsp"></jsp:include>
	
    <!-- CSS Page Style -->    
    <link rel="stylesheet" href="<%=request.getContextPath()%>/resources/unify/assets/css/pages/blog.css">	
	<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/summernote/dist/summernote.css" rel="stylesheet">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/unify/assets/plugins/ladda-buttons/css/custom-lada-btn.css">
	
	<script src="<%=request.getContextPath()%>/resources/jquery/dist/jquery.min.js"></script>
</head>
<body>
<div class="wrapper">
	<jsp:include page="../include/navigation-header.jsp"/>
	
	<c:set var="summernoteLang" value="en-US"/>
	
	<sec:authorize access="isAnonymous()">
		<c:set var="authRole" value="ANNONYMOUS"/>
	</sec:authorize>
	<sec:authorize access="hasAnyRole('ROLE_USER_01', 'ROLE_USER_02', 'ROLE_USER_03')">
		<c:set var="authRole" value="USER"/>
		<sec:authentication property="principal.id" var="accountId"/>
	</sec:authorize>
	<sec:authorize access="hasAnyRole('ROLE_ROOT')">
		<c:set var="authAdminRole" value="ROOT"/>
	</sec:authorize>		
	
	<c:url var="listUrl" value="/board/free">
		<c:if test="${!empty listInfo.page}">
			<c:param name="page" value="${listInfo.page}"/>
		</c:if>
		<c:if test="${!empty listInfo.category}">
			<c:param name="category" value="${listInfo.category}"/>
		</c:if>
	</c:url>
	
	<c:if test="${!empty prev}">
		<c:url var="prevUrl" value="/board/free/${prev.seq}">
			<c:if test="${!empty listInfo.page}">
				<c:param name="page" value="${listInfo.page}"/>
			</c:if>
			<c:if test="${!empty listInfo.category}">
				<c:param name="category" value="${listInfo.category}"/>
			</c:if>
		</c:url>	
	</c:if>
	
	<c:if test="${!empty next}">
		<c:url var="nextUrl" value="/board/free/${next.seq}">
			<c:if test="${!empty listInfo.page}">
				<c:param name="page" value="${listInfo.page}"/>
			</c:if>
			<c:if test="${!empty listInfo.category}">
				<c:param name="category" value="${listInfo.category}"/>
			</c:if>
		</c:url>	
	</c:if>
	
	<!--=== Breadcrumbs ===-->
	<div class="breadcrumbs">
		<div class="container">
			<h1 class="pull-left"><spring:message code="board.name.free"/></h1>
		</div><!--/container-->
	</div><!--/breadcrumbs-->
	<!--=== End Breadcrumbs ===-->		
	
	<!--=== Content Part ===-->
	<div class="container content blog-page blog-item">	
	
	<div class="margin-bottom-10">
	<button type="button" class="btn-u btn-brd rounded" onclick="location.href='${listUrl}'"><i class="fa fa-list"></i></button>
	
	<c:choose>
		<c:when test="${!empty prevUrl}">
			<button type="button" class="btn-u btn-brd rounded" onclick="location.href='${prevUrl}'"><i class="fa fa-chevron-left"></i></button>		
		</c:when>
		<c:otherwise>
			<button type="button" class="btn-u btn-brd rounded" disabled="disabled"><i class="fa fa-chevron-left"></i></button>		
		</c:otherwise>
	</c:choose>
	<c:choose>
		<c:when test="${!empty nextUrl}">
			<button type="button" class="btn-u btn-brd rounded" onclick="location.href='${nextUrl}'"><i class="fa fa-chevron-right"></i></button>		
		</c:when>
		<c:otherwise>
			<button type="button" class="btn-u btn-brd rounded" disabled="disabled"><i class="fa fa-chevron-right"></i></button>		
		</c:otherwise>
	</c:choose>	
	
	<c:if test="${authRole != 'ANNONYMOUS' && accountId == post.writer.userId}">
		<button type="button" class="btn-u rounded" onclick="location.href='<c:url value="/board/free/edit/${post.seq}"/>'">
			<i class="fa fa-pencil-square-o"></i> <spring:message code="common.button.edit"/>
		</button>
		<button type="button" class="btn-u btn-u-default rounded" onclick="confirmDelete();">
			<i class="fa fa-trash-o"></i> <spring:message code="common.button.delete"/>
		</button>	
	</c:if>
	
	<c:choose>
		<c:when test="${authAdminRole == 'ROOT' && post.status.notice != 'notice' && post.status.delete != 'delete'}">
			<button type="button" class="btn-u rounded" onclick="location.href='<c:url value="/board/notice/set/${post.seq}"/>'">
				<spring:message code="common.button.set.as.notice"/>
			</button>
		</c:when>
		<c:when test="${authAdminRole == 'ROOT' && post.status.notice == 'notice' && post.status.delete != 'delete'}">
			<button type="button" class="btn-u btn-u-default rounded" onclick="location.href='<c:url value="/board/notice/cancel/${post.seq}"/>'">
				<spring:message code="common.button.cancel.notice"/>
			</button>		
		</c:when>
	</c:choose>
	
	<c:choose>
		<c:when test="${result == 'setNotice'}">
			<div class="alert alert-success" role="alert"><spring:message code="board.msg.set.as.notice"/></div>
		</c:when>	
		<c:when test="${result == 'cancelNotice'}">
			<div class="alert alert-success" role="alert"><spring:message code="board.msg.cancel.notice"/></div>
		</c:when>
		<c:when test="${result == 'alreadyNotice'}">
			<div class="alert alert-danger" role="alert"><spring:message code="board.msg.error.already.notice"/></div>
		</c:when>
		<c:when test="${result == 'alreadyNotNotice'}">
			<div class="alert alert-danger" role="alert"><spring:message code="board.msg.error.already.not.notice"/></div>
		</c:when>		
		<c:when test="${result == 'existComment'}">
			<div class="alert alert-danger" role="alert"><spring:message code="board.msg.error.can.not.delete.post"/></div>
		</c:when>
		<c:when test="${result == 'emptyComment'}">
			<div class="alert alert-danger" role="alert"><spring:message code="board.msg.error.can.not.delete.post.except.comment"/></div>
		</c:when>					
	</c:choose>
	
</div>	
        <!--Blog Post-->        
    	<div class="blog margin-bottom-20" ng-controller="boardFreeCtrl">
        	<h2>
        	<small>
				<c:if test="${post.status.device == 'mobile'}"><i class="fa fa-mobile fa-lg"></i></c:if>
				<c:if test="${post.status.device == 'tablet'}"><i class="fa fa-tablet fa-lg"></i></c:if>
				<c:if test="${galleries != null}"><i class="fa fa-file-image-o"></i></c:if>
				</small>
				<c:choose>
					<c:when test="${post.status.delete == 'delete'}">
						<spring:message code="board.msg.deleted"/>
					</c:when>
					<c:otherwise>
						${post.subject}
					</c:otherwise>
				</c:choose>
				
	  		<c:if test="${!empty category}">&nbsp;<small><spring:message code="${category.resName}"/></small></c:if>        	
        	</h2>
            <div class="blog-post-tags">
                <ul class="list-unstyled list-inline blog-info">
                    <li><i class="fa fa-user"></i> ${post.writer.username}</li>
                    <li><i class="fa fa-clock-o"></i> {{dateFromObjectId("${post.id}") | date:"${dateTimeFormat.dateTime}"}}</li>
                    <li><i class="fa fa-eye"></i> ${post.views}</li>
                </ul>                    
            </div>
            
			<c:choose>
				<c:when test="${post.status.delete == 'delete'}">
					<p><spring:message code="board.msg.deleted"/></p>
				</c:when>
				<c:otherwise>
					<p>${post.content}</p>
				</c:otherwise>
			</c:choose>            
			
	<c:if test="${galleries != null}">
	<ul class="list-group">
	  <li class="list-group-item">
	  <strong><spring:message code="board.gallery.list"/></strong>
			<c:forEach items="${galleries}" var="gallery">
				<div>
						<a href="<%=request.getContextPath()%>/gallery/view/${gallery.id}">${gallery.name}</a> | 
						<fmt:formatNumber value="${gallery.size/1024}" pattern=".00"/> KB
				</div>
			</c:forEach>    
	  </li>
	</ul>	
	</c:if>					
	
<div class="ladda-btn margin-bottom-10">
   
<button class="btn-u btn-brd btn-brd-hover rounded btn-u-blue btn-u-sm ladda-button" type="button"
	ng-click="btnFeeling('like')" ng-init="numberOfLike=${fn:length(post.usersLiking)}"
	ladda="btnLike" data-style="expand-right" data-spinner-color="Gainsboro">
	<i class="fa fa-thumbs-o-up fa-lg"></i>
   <span ng-hide="likeConn == 'connecting'">{{numberOfLike}}</span>      
</button>
<button class="btn-u btn-brd btn-brd-hover rounded btn-u-red btn-u-sm ladda-button" type="button" 
	ng-click="btnFeeling('dislike')" ng-init="numberOfDislike=${fn:length(post.usersDisliking)}"
	ladda="btnDislike" data-style="expand-right" data-spinner-color="Gainsboro">
	<i class="fa fa-thumbs-o-down fa-lg"></i>
   <span ng-hide="dislikeConn == 'connecting'">{{numberOfDislike}}</span>      
</button>
</div>
<div class="alert {{alert.classType}}" role="alert" ng-show="alert.msg">{{alert.msg}}</div>	
        </div>
        <!--End Blog Post-->   	
	
	<hr />
	
	<!-- comment -->		
	<div ng-controller="commentCtrl">
	
	<input type="hidden" id="commentCount" value="{{commentCount}}">

<!-- 댓글 목록  -->	
<div class="media margin-bottom-10">
                	<h4 class="text-primary" infinite-scroll="initComment()" infinite-scroll-disabled="infiniteDisabled">		  
<spring:message code="board.msg.comment.count" arguments="{{commentCount}}"/>

<button type="button" class="btn btn-link" ng-click="btnRefreshComment()">
			  			  	<i class="fa fa-refresh text-muted" ng-class="{'fa-spin':loadCommentConn == 'connecting'}"></i>
				</button>
</h4>

<div class="media-body">
<div ng-repeat="comment in commentList">
    <h5 class="media-heading">
			<i class="fa fa-user"></i>{{comment.writer.username}} 
			<span><i class="fa fa-clock-o"></i> {{dateFromObjectId(comment.id) | date:"${dateTimeFormat.dateTime}"}}</span>
    </h5>    
    <p>
	 							<span ng-if="comment.status.device == 'mobile'"><i class="fa fa-mobile fa-lg"></i></span>
	 							<span ng-if="comment.status.device == 'tablet'"><i class="fa fa-tablet fa-lg"></i></span>
	 							<span ng-bind-html="comment.content"></span>
		</p>
	 							
    							<button type="button" class="btn-u btn-brd btn-brd-hover rounded btn-u-blue btn-u-xs" ng-click="btnCommentFeeling(comment.id, 'like')">
							  <span ng-init="numberOfCommentLike[comment.id]=comment.usersLiking.length">
								  <i class="fa fa-thumbs-o-up fa-lg"></i>
								  {{numberOfCommentLike[comment.id]}}
							  </span>
							</button>
							<button type="button" class="btn-u btn-brd btn-brd-hover rounded btn-u-red btn-u-xs" ng-click="btnCommentFeeling(comment.id, 'dislike')">
								<span ng-init="numberOfCommentDislike[comment.id]=comment.usersDisliking.length">
							  	<i class="fa fa-thumbs-o-down fa-lg"></i>
							  	{{numberOfCommentDislike[comment.id]}}
							  </span>
							</button>							
<span class="text-danger" ng-show="commentFeelingConn[comment.id]">{{commentFeelingAlert[comment.id]}}</span>							 

    <hr class="padding-5">
</div>    
		<div class="margin-bottom-10" ng-show="commentCount || commentAlert.msg">
			<button type="button" class="btn-u btn-brd rounded btn-block btn-u-dark" 
			ng-click="btnMoreComment()" ng-show="commentCount">
				<spring:message code="common.button.load.comment"/> <i class="fa fa-angle-down"></i>
				<i class="fa fa-circle-o-notch fa-spin" ng-show="loadCommentConn == 'connecting'"></i>
			</button>
		</div>
		<div class="alert {{commentAlert.classType}}" role="alert" ng-show="commentAlert.msg">{{commentAlert.msg}}</div>
</div>
                </div>		
                
<!-- 댓글 남기기 -->                
<div class="post-comment">
            <h4 class="text-primary"><spring:message code="board.comment.leave.comment"/></h4>
<div class="margin-bottom-10">            
				<summernote config="options" on-keydown="keydown(evt)" 
				ng-model="summernote.content" ng-init="summernote={content:'♪', seq:'${post.seq}'}"></summernote>
				<span class="{{summernoteAlert.classType}}" ng-show="summernoteAlert.msg">{{summernoteAlert.msg}}</span>
</div>				  

<p>
				<c:choose>
					<c:when test="${authRole == 'ANNONYMOUS'}">
						<button type="button" class="btn-u btn-brd rounded btn-u-sm disabled" disabled="disabled">
							<i class="fa fa-pencil"></i> <spring:message code="common.button.write.comment"/>
						</button>	
					</c:when>
					<c:when test="${authRole == 'USER'}">
						<button type="button" class="btn-u btn-brd btn-brd-hover rounded btn-u-sm ladda-button" 
						ng-click="btnWriteComment()" 
						ladda="writeComment" data-style="expand-right" data-spinner-color="Gainsboro">
							<i class="fa fa-pencil"></i> <spring:message code="common.button.write.comment"/>
						</button>				
					</c:when>
				</c:choose>				          
</p>
				<div>
					<span class="{{writeCommentAlert.classType}}" ng-show="writeCommentAlert.msg">{{writeCommentAlert.msg}}</span>
				</div>	
</div>                
	
	</div>
	
	<button type="button" class="btn-u btn-brd rounded" onclick="location.href='${listUrl}'"><i class="fa fa-list"></i></button>
	
	<c:choose>
		<c:when test="${!empty prevUrl}">
			<button type="button" class="btn-u btn-brd rounded" onclick="location.href='${prevUrl}'"><i class="fa fa-chevron-left"></i></button>		
		</c:when>
		<c:otherwise>
			<button type="button" class="btn-u btn-brd rounded" disabled="disabled"><i class="fa fa-chevron-left"></i></button>		
		</c:otherwise>
	</c:choose>
	<c:choose>
		<c:when test="${!empty nextUrl}">
			<button type="button" class="btn-u btn-brd rounded" onclick="location.href='${nextUrl}'"><i class="fa fa-chevron-right"></i></button>		
		</c:when>
		<c:otherwise>
			<button type="button" class="btn-u btn-brd rounded" disabled="disabled"><i class="fa fa-chevron-right"></i></button>		
		</c:otherwise>
	</c:choose>	
	
	<c:if test="${authRole != 'ANNONYMOUS' && accountId == post.writer.userId}">
		<button type="button" class="btn-u rounded" onclick="location.href='<c:url value="/board/free/edit/${post.seq}"/>'">
			<i class="fa fa-pencil-square-o"></i> <spring:message code="common.button.edit"/>
		</button>
		<button type="button" class="btn-u btn-u-default rounded" onclick="confirmDelete();">
			<i class="fa fa-trash-o"></i> <spring:message code="common.button.delete"/>
		</button>	
	</c:if>
	
	<c:choose>
		<c:when test="${authAdminRole == 'ROOT' && post.status.notice != 'notice' && post.status.delete != 'delete'}">
			<button type="button" class="btn-u rounded" onclick="location.href='<c:url value="/board/notice/set/${post.seq}"/>'">
				<spring:message code="common.button.set.as.notice"/>
			</button>
		</c:when>
		<c:when test="${authAdminRole == 'ROOT' && post.status.notice == 'notice' && post.status.delete != 'delete'}">
			<button type="button" class="btn-u btn-u-default rounded" onclick="location.href='<c:url value="/board/notice/cancel/${post.seq}"/>'">
				<spring:message code="common.button.cancel.notice"/>
			</button>		
		</c:when>
	</c:choose>
	
	</div>	
	
	<jsp:include page="../include/footer.jsp"/>
</div> <!-- /.container -->

<script src="<%=request.getContextPath()%>/resources/bootstrap/dist/js/bootstrap.min.js"></script>
<script src="<%=request.getContextPath()%>/resources/summernote/dist/summernote.min.js"></script>
<!--angular-summernote dependencies -->
<script src="<%=request.getContextPath()%>/resources/angular-summernote/dist/angular-summernote.min.js"></script>
<script src="<%=request.getContextPath()%>/resources/ng-infinite-scroller-origin/build/ng-infinite-scroll.min.js"></script>
<script src="<%=request.getContextPath()%>/resources/angular-sanitize/angular-sanitize.min.js"></script>
<script src="<%=request.getContextPath()%>/resources/jakduk/js/jakduk.js"></script>
<c:if test="${fn:contains('ko', pageContext.response.locale.language)}">
	<script src="<%=request.getContextPath()%>/resources/summernote/lang/summernote-ko-KR.js"></script>
	<c:set var="summernoteLang" value="ko-KR"/>
</c:if>

<!-- JS Implementing Plugins -->
<script src="<%=request.getContextPath()%>/resources/unify/assets/plugins/ladda-buttons/js/spin.min.js"></script>
<script src="<%=request.getContextPath()%>/resources/unify/assets/plugins/ladda-buttons/js/ladda.min.js"></script>

<script src="<%=request.getContextPath()%>/resources/angular-ladda/dist/angular-ladda.min.js"></script>

<script type="text/javascript">

var jakdukApp = angular.module("jakdukApp", ["summernote", "infinite-scroll", "ngSanitize", "angular-ladda"]);

jakdukApp.controller("boardFreeCtrl", function($scope, $http) {
	$scope.alert = {};
	$scope.likeConn = "none";
	$scope.dislikeConn = "none";
	
	angular.element(document).ready(function() {
	});		
	
	$scope.objectIdFromDate = function(date) {
		return Math.floor(date.getTime() / 1000).toString(16) + "0000000000000000";
	};
	
	$scope.dateFromObjectId = function(objectId) {
		return new Date(parseInt(objectId.substring(0, 8), 16) * 1000);
	};
	
	$scope.btnFeeling = function(type) {
		
		if ("${authRole}" == "ANNONYMOUS") {
			$scope.alert.msg = '<spring:message code="board.msg.need.login.for.feel"/>';
			$scope.alert.classType = "alert-warning";
			return;
		} else if ("${accountId}" == "${post.writer.userId}") {
			$scope.alert.msg = '<spring:message code="board.msg.you.are.writer"/>';
			$scope.alert.classType = "alert-warning";
			return;
		}
		
		var bUrl = '<c:url value="/board/' + type + '/${post.seq}.json"/>';
		
		if ($scope.likeConn == "none" && $scope.dislikeConn == "none") {
			
			var reqPromise = $http.get(bUrl);
			
			if (type == "like") {
				$scope.likeConn = "connecting";
				$scope.btnLike = true;
			} else if (type == "dislike") {
				$scope.dislikeConn = "connecting";
				$scope.btnDislike = true;
			}
			
			reqPromise.success(function(data, status, headers, config) {
				var message = "";
				var mType = "";
				
				if (data.errorCode == "like") {
					message = '<spring:message code="board.msg.select.like"/>';
					mType = "alert-success";
					$scope.numberOfLike = data.numberOfLike;
				} else if (data.errorCode == "dislike") {
					message = '<spring:message code="board.msg.select.dislike"/>';
					mType = "alert-success";
					$scope.numberOfDislike = data.numberOfDislike;
				} else if (data.errorCode == "already") {
					message = '<spring:message code="board.msg.select.already.like"/>';
					mType = "alert-warning";
				} else if (data.errorCode == "anonymous") {
					message = '<spring:message code="board.msg.need.login.for.feel"/>';
					mType = "alert-warning";
				} else if (data.errorCode == "writer") {
					message = '<spring:message code="board.msg.you.are.writer"/>';
					mType = "alert-warning";
				}
				
				$scope.alert.msg = message;
				$scope.alert.classType = mType;
				
				if (type == "like") {
					$scope.likeConn = "success";
					$scope.btnLike = false;
				} else if (type == "dislike") {
					$scope.dislikeConn = "success";
					$scope.btnDislike = false;
				}
				
			});
			reqPromise.error(function(data, status, headers, config) {				
				$scope.alert.msg = '<spring:message code="common.msg.error.network.unstable"/>';
				$scope.alert.classType = "alert-danger";
				
				if (type == "like") {
					$scope.likeConn = "none";
					$scope.btnLike = false;
				} else if (type == "dislike") {
					$scope.dislikeConn = "none";
					$scope.btnDislike = false;
				}
			});
		}
	};	
});


jakdukApp.controller("commentCtrl", function($scope, $http) {
	$scope.commentList = [];
	$scope.commentAlert = {};
	$scope.summernoteAlert = {};
	$scope.commentFeelingConn = {};
	$scope.commentFeelingAlert = {};
	$scope.numberOfCommentLike = {};
	$scope.numberOfCommentDislike = {};
	$scope.loadCommentConn = "none";
	$scope.writeCommentConn = "none";
	$scope.writeCommentAlert = {};
	$scope.infiniteDisabled = false;
	
	angular.element(document).ready(function() {
	});		

	// summernote config
	$scope.options = {
			height: 0,
			lang : "${summernoteLang}",
			toolbar: [
	      ['font', ['bold']],
	      // ['fontsize', ['fontsize']], // Still buggy
	      ['color', ['color']],
	      ['insert', ['link']],
	      ['help', ['help']]			          
				]};	
	
	$scope.keydown = function(e) { 
		if ("${authRole}" == "ANNONYMOUS") {
			if (confirm('<spring:message code="board.msg.need.login.for.write"/>') == true) {
				location.href = "<c:url value='/login'/>";
			}
		}	
	}
	
	if ("${authRole}" == "ANNONYMOUS") {
		$scope.summernoteAlert = {"classType":"text-danger", "msg":'<spring:message code="board.msg.need.login.for.write"/>'};
	}

	// http config
	var headers = {
			"Content-Type" : "application/x-www-form-urlencoded"
	};
	
	var config = {
			headers:headers,
			transformRequest: function(obj) {
		        var str = [];
		        for(var p in obj)
		        str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
		        return str.join("&");
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
	
	$http.defaults.headers.post["Content-Type"] = "application/x-www-form-urlencoded";
	
	$scope.btnWriteComment = function(status) {
		var bUrl = '<c:url value="/board/free/comment/write"/>';
		
		if ($scope.summernote.content.length < 3 || $scope.summernote.content.length > 800) {
			$scope.summernoteAlert = {"classType":"text-danger", "msg":'<spring:message code="Size.board.comment.content"/>'};
			return;
		}
		
		if ($scope.writeCommentConn == "none") {
			var reqPromise = $http.post(bUrl, $scope.summernote, config);
			$scope.writeCommentConn = "connecting";
			//$scope.writeCommentAlert = {"classType":"text-info", "msg":'<spring:message code="common.msg.be.cummunicating.server"/>'};
			$scope.writeComment = true;
			
			reqPromise.success(function(data, status, headers, config) {
				$scope.btnMoreComment();
				
				$scope.summernote.content = "♪";
				$scope.commentAlert = {};
				$scope.summernoteAlert = {};
				$scope.writeCommentAlert = {};
				$scope.writeCommentConn = "none";
				$scope.writeComment = false;
			});
			reqPromise.error(function(data, status, headers, config) {
				$scope.writeCommentAlert = {"classType":"text-danger", "msg":'<spring:message code="common.msg.error.network.unstable"/>'};
				$scope.writeCommentConn = "none";		
				$scope.writeComment = false;
			});			
		}
	};
	
	$scope.initComment = function() {
		$scope.loadComments("init", "");
		$scope.infiniteDisabled = true;
	}
	
	$scope.loadComments = function(type, commentId) {
		var bUrl = '<c:url value="/board/free/comment/${post.seq}?commentId=' + commentId + '"/>';
		
		if ($scope.loadCommentConn == "none") {
			var reqPromise = $http.get(bUrl);
			
			$scope.loadCommentConn = "connecting";
			
			reqPromise.success(function(data, status, headers, config) {
				
				$scope.commentCount = data.count;
							
				if (data.comments.length < 1) { // 댓글이 하나도 없을때
					if (type == "init") {					
					} else {
						$scope.commentAlert.msg = '<spring:message code="board.msg.there.is.no.new.comment"/>';
						$scope.commentAlert.classType = "alert-warning";				
					}				
				} else {	// 댓글을 1개 이상 가져왔을 때
					if (type == "init" || type == "btnRefreshComment") {
						$scope.commentList = data.comments;
					} else if (type == "btnMoreComment" || type == "btnWriteComment") {
						$scope.commentList = $scope.commentList.concat(data.comments);
					}
				}
				
				$scope.loadCommentConn = "none";
			});
			reqPromise.error(function(data, status, headers, config) {
				$scope.loadCommentConn = "none";
			});			
		}
	};
	
	$scope.btnMoreComment = function() {
		if ($scope.commentList.length > 0) {
			var lastComment = $scope.commentList[$scope.commentList.length - 1];
			$scope.loadComments("btnMoreComment", lastComment.id);
		} else {
			$scope.loadComments("btnMoreComment", "");			
		}
	};
	
	$scope.btnRefreshComment = function() {
		$scope.commentAlert = {};
		$scope.commentList = [];
		$scope.loadComments("btnRefreshComment", "");
	};
	
	$scope.btnCommentFeeling = function(commentId, status) {
		
		var bUrl = '<c:url value="/board/comment/' + status + '/${post.seq}.json?id=' + commentId + '"/>';
		var conn = $scope.commentFeelingConn[commentId];
		
		if (conn == "none" || conn == null) {
			var reqPromise = $http.get(bUrl);
			
			$scope.commentFeelingConn[commentId] = "loading";
			
			reqPromise.success(function(data, status, headers, config) {
				var message = "";
				
				if (data.errorCode == "like") {
					$scope.numberOfCommentLike[commentId] = data.numberOfLike;
				} else if (data.errorCode == "dislike") {
					$scope.numberOfCommentDislike[commentId] = data.numberOfDislike;
				} else if (data.errorCode == "already") {
					message = '<spring:message code="board.msg.select.already.like"/>';
				} else if (data.errorCode == "anonymous") {
					message = '<spring:message code="board.msg.need.login.for.feel"/>';
				} else if (data.errorCode == "writer") {
					message = '<spring:message code="board.msg.you.are.writer"/>';
				}
				
				$scope.commentFeelingAlert[commentId] = message;
				$scope.commentFeelingConn[commentId] = "ok";
				
			});
			reqPromise.error(function(data, status, headers, config) {
				$scope.commentFeelingConn[commentId] = "none";
				$scope.commentFeelingAlert[commentId] = '<spring:message code="common.msg.error.network.unstable"/>';				
			});
		}
	};
});

function confirmDelete() {
	var commentCount = document.getElementById("commentCount").value;
	
	if (commentCount > 0) {
		if (confirm('<spring:message code="board.msg.confirm.delete.post.except.comment"/>') == true) {
			location.href = '<c:url value="/board/free/delete/${post.seq}?type=postonly"/>';
		}	
	} else {
		if (confirm('<spring:message code="board.msg.confirm.delete.post"/>') == true) {
			location.href = '<c:url value="/board/free/delete/${post.seq}?type=all"/>';
		}
	}
}
</script>

<jsp:include page="../include/body-footer.jsp"/>

</body>
</html>