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
	
	<link href="<%=request.getContextPath()%>/resources/font-awesome/css/font-awesome.min.css" rel="stylesheet">
	<link href="<%=request.getContextPath()%>/resources/summernote/css/summernote.css" rel="stylesheet">
	
	<script src="<%=request.getContextPath()%>/resources/jquery/js/jquery.min.js"></script>
</head>
<body>
<div class="container jakduk-board">
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
	
	
	<button type="button" class="btn btn-default" onclick="location.href='${listUrl}'">
		<spring:message code="board.list"/>
	</button>
	
	<c:choose>
		<c:when test="${!empty prevUrl}">
			<button type="button" class="btn btn-default" onclick="location.href='${prevUrl}'">
				<span class="glyphicon glyphicon-chevron-left"></span>
			</button>		
		</c:when>
		<c:otherwise>
			<button type="button" class="btn btn-default" disabled="disabled">
				<span class="glyphicon glyphicon-chevron-left"></span>
			</button>		
		</c:otherwise>
	</c:choose>
	<c:choose>
		<c:when test="${!empty nextUrl}">
			<button type="button" class="btn btn-default" onclick="location.href='${nextUrl}'">
				<span class="glyphicon glyphicon-chevron-right"></span>
			</button>		
		</c:when>
		<c:otherwise>
			<button type="button" class="btn btn-default" disabled="disabled">
				<span class="glyphicon glyphicon-chevron-right"></span>
			</button>		
		</c:otherwise>
	</c:choose>	
	
	<c:if test="${authRole != 'ANNONYMOUS' && accountId == post.writer.userId}">
		<button type="button" class="btn btn-info" onclick="location.href='<c:url value="/board/free/edit/${post.seq}"/>'">
			<span class="glyphicon glyphicon-edit hidden-xs"></span> <spring:message code="common.button.edit"/>
		</button>
		<button type="button" class="btn btn-danger" onclick="confirmDelete();">
			<span class="glyphicon glyphicon-trash hidden-xs"></span> <spring:message code="common.button.delete"/>
		</button>	
	</c:if>
	<c:choose>
		<c:when test="${authAdminRole == 'ROOT' && post.status.notice != 'notice' && post.status.delete != 'delete'}">
			<button type="button" class="btn btn-default" onclick="location.href='<c:url value="/board/notice/set/${post.seq}"/>'">
				<spring:message code="common.button.set.as.notice"/>
			</button>
		</c:when>
		<c:when test="${authAdminRole == 'ROOT' && post.status.notice == 'notice' && post.status.delete != 'delete'}">
			<button type="button" class="btn btn-default" onclick="location.href='<c:url value="/board/notice/cancel/${post.seq}"/>'">
				<spring:message code="common.button.cancel.notice"/>
			</button>		
		</c:when>
	</c:choose>

	<p></p>
	
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
	
	<!-- Begin page content -->
	<div class="panel panel-info" ng-controller="boardFreeCtrl">
	  <div class="panel-heading">
	  	<h4 class="panel-title">	  	
				<c:if test="${post.status.device == 'mobile'}"><i class="fa fa-mobile fa-lg"></i></c:if>
				<c:if test="${post.status.device == 'tablet'}"><i class="fa fa-tablet fa-lg"></i></c:if>
				<c:if test="${galleries != null}"><i class="fa fa-file-image-o"></i></c:if>
				<c:choose>
					<c:when test="${post.status.delete == 'delete'}">
						<spring:message code="board.msg.deleted"/>
					</c:when>
					<c:otherwise>
						${post.subject}
					</c:otherwise>
				</c:choose>
	  		<c:if test="${!empty category}">&nbsp;<small><spring:message code="${category.resName}"/></small></c:if>
	  	</h4>
	  	<div class="row">
	  		<div class="col-sm-2">
		  		<h4><small><span class="glyphicon glyphicon-user"></span> ${post.writer.username}</small></h4>
	  		</div>
	  		<div class="col-md-5">
	  			<h4>
	  				<small>
							<span class="glyphicon glyphicon-time"></span>
							{{dateFromObjectId("${post.id}") | date:"${dateTimeFormat.dateTime}"}}
			    		&nbsp;<span class="glyphicon glyphicon-eye-open"></span> ${post.views}
					</small>
				</h4>		    		 
	  		</div>	
	  	</div>
	  </div>
	  
		<div class="panel-body">
			<c:choose>
				<c:when test="${post.status.delete == 'delete'}">
					<p><spring:message code="board.msg.deleted"/></p>
				</c:when>
				<c:otherwise>
					<p>${post.content}</p>
				</c:otherwise>
			</c:choose>
		</div>
		
	<c:if test="${galleries != null}">
	<ul class="list-group">
	  <li class="list-group-item">
	  <strong><spring:message code="board.gallery.list"/></strong>
			<c:forEach items="${galleries}" var="gallery">
				<div>
					<small>
						<a href="<%=request.getContextPath()%>/gallery/${gallery.id}">${gallery.name}</a> | 
						<fmt:formatNumber value="${gallery.size/1024}" pattern=".00"/> KB
					</small>
				</div>
			</c:forEach>    
	  </li>
	</ul>	
	</c:if>		
	  
		<div class="panel-footer text-center">
			<button type="button" class="btn btn-default" ng-click="btnFeeling('like')">
				<spring:message code="common.like"/>			
				<span class="text-primary" ng-init="numberOfLike=${fn:length(post.usersLiking)}">
					<i class="fa fa-thumbs-o-up fa-lg"></i>
				</span>
				<span class="text-primary" ng-hide="likeConn == 'connecting'">{{numberOfLike}}</span>
				<span class="text-primary"><i class="fa fa-circle-o-notch fa-spin" ng-show="likeConn == 'connecting'"></i></span>
			</button>
			<button type="button" class="btn btn-default" ng-click="btnFeeling('dislike')">		
				<spring:message code="common.dislike"/>
				<span class="text-danger" ng-init="numberOfDislike=${fn:length(post.usersDisliking)}">
					<i class="fa fa-thumbs-o-down fa-lg"></i>
				</span>
				<span class="text-danger" ng-hide="dislikeConn == 'connecting'">{{numberOfDislike}}</span>
				<span class="text-danger"><i class="fa fa-circle-o-notch fa-spin" ng-show="dislikeConn == 'connecting'"></i></span>				
			</button>
			<div class="alert {{alert.classType}}" role="alert" ng-show="alert.msg">{{alert.msg}}</div>
		</div>
	</div> <!-- /panel -->
	
	<!-- comment -->		
	<div ng-controller="commentCtrl">
		<input type="hidden" id="commentCount" value="{{commentCount}}">
		<div class="panel panel-default" infinite-scroll="initComment()" infinite-scroll-disabled="infiniteDisabled">
			<!-- Default panel contents -->	  
			<div class="panel-heading">
		  	<spring:message code="board.msg.comment.count" arguments="{{commentCount}}"/>
		  	&nbsp;
				<button type="button" class="btn btn-default" ng-click="btnRefreshComment()">
			  	<i class="fa fa-refresh" ng-class="{'fa-spin':loadCommentConn == 'connecting'}"></i>
				</button>					
		  </div>
				
			<!-- comment list -->
			<ul class="list-group">
				<li class="list-group-item" ng-repeat="comment in commentList">
		 			<div class="row">			
	 					<div class="col-xs-12 visible-xs">
	 						<strong><span class="glyphicon glyphicon-user"></span> {{comment.writer.username}}</strong>
							&nbsp;
							<span class="glyphicon glyphicon-time"></span>
							<span ng-if="${timeNow} > intFromObjectId(comment.id)">{{dateFromObjectId(comment.id) | date:"${dateTimeFormat.date}"}}</span>
							<span ng-if="${timeNow} <= intFromObjectId(comment.id)">{{dateFromObjectId(comment.id) | date:"${dateTimeFormat.time}"}}</span>
	 					</div>
	 					<div class="col-xs-12 visible-sm visible-md visible-lg">
	 						<strong><span class="glyphicon glyphicon-user"></span> {{comment.writer.username}}</strong>
	 						&nbsp;
	 						<span class="glyphicon glyphicon-time"></span> 
	 						<span>{{dateFromObjectId(comment.id) | date:"${dateTimeFormat.dateTime}"}}</span>
	 					</div>
	 					<div class="col-xs-12">
	 						<p>
	 							<span ng-if="comment.status.device == 'mobile'"><i class="fa fa-mobile fa-lg"></i></span>
	 							<span ng-if="comment.status.device == 'tablet'"><i class="fa fa-tablet fa-lg"></i></span>
	 							<span ng-bind-html="comment.content"></span>
	 						</p>
	 					</div>
	 					<div class="col-xs-12">
							<button type="button" class="btn btn-default btn-xs" ng-click="btnCommentFeeling(comment.id, 'like')">
							  <span class="text-primary" ng-init="numberOfCommentLike[comment.id]=comment.usersLiking.length">
								  <i class="fa fa-thumbs-o-up fa-lg"></i>
								  {{numberOfCommentLike[comment.id]}}
							  </span>
							</button> 					
							<button type="button" class="btn btn-default btn-xs" ng-click="btnCommentFeeling(comment.id, 'dislike')">
								<span class="text-danger" ng-init="numberOfCommentDislike[comment.id]=comment.usersDisliking.length">
							  	<i class="fa fa-thumbs-o-down fa-lg"></i>
							  	{{numberOfCommentDislike[comment.id]}}
							  </span>
							</button>
							<div>
							<span class="text-danger" ng-show="commentFeelingConn[comment.id]"><small>{{commentFeelingAlert[comment.id]}}</small></span>
							</div>
	 					</div>
		 			</div>			
				</li>
			</ul>
		  
		<div class="panel-footer text-center" ng-show="commentCount || commentAlert.msg">
			<button type="button" class="btn btn-default btn-block" ng-click="btnMoreComment()" ng-show="commentCount">
				<spring:message code="common.button.load.comment"/> <span class="glyphicon glyphicon-chevron-down"></span>
				<i class="fa fa-circle-o-notch fa-spin" ng-show="loadCommentConn == 'connecting'"></i>
			</button>
			<div class="alert {{commentAlert.classType}}" role="alert" ng-show="commentAlert.msg">{{commentAlert.msg}}</div>
		</div>
		</div>
		
		<div class="panel panel-default">
			<div class="panel-body">
				<summernote config="options" on-keydown="keydown(evt)" 
				ng-model="summernote.content" ng-init="summernote={content:'♪', seq:'${post.seq}'}"></summernote>
				<span class="{{summernoteAlert.classType}}" ng-show="summernoteAlert.msg">{{summernoteAlert.msg}}</span>
			</div>
			<div class="panel-footer">
				<c:choose>
					<c:when test="${authRole == 'ANNONYMOUS'}">
						<button type="button" class="btn btn-default" disabled="disabled">
							<span class="glyphicon glyphicon-pencil"></span> <spring:message code="common.button.write.comment"/>
						</button>	
					</c:when>
					<c:when test="${authRole == 'USER'}">
						<button type="button" class="btn btn-default" ng-click="btnWriteComment()">
							<span class="glyphicon glyphicon-pencil"></span> <spring:message code="common.button.write.comment"/>
						</button>				
					</c:when>
				</c:choose>
				<div>
					<i class="fa fa-circle-o-notch fa-spin" ng-show="writeCommentConn == 'connecting'"></i>
					<span class="{{writeCommentAlert.classType}}" ng-show="writeCommentAlert.msg">{{writeCommentAlert.msg}}</span>
				</div>				
			</div>
		</div>	
	</div>
	
	<button type="button" class="btn btn-default" onclick="location.href='${listUrl}'">
		<spring:message code="board.list"/>
	</button>

	<c:choose>
		<c:when test="${!empty prevUrl}">
			<button type="button" class="btn btn-default" onclick="location.href='${prevUrl}'">
				<span class="glyphicon glyphicon-chevron-left"></span>
			</button>		
		</c:when>
		<c:otherwise>
			<button type="button" class="btn btn-default" disabled="disabled">
				<span class="glyphicon glyphicon-chevron-left"></span>
			</button>		
		</c:otherwise>
	</c:choose>
	<c:choose>
		<c:when test="${!empty nextUrl}">
			<button type="button" class="btn btn-default" onclick="location.href='${nextUrl}'">
				<span class="glyphicon glyphicon-chevron-right"></span>
			</button>		
		</c:when>
		<c:otherwise>
			<button type="button" class="btn btn-default" disabled="disabled">
				<span class="glyphicon glyphicon-chevron-right"></span>
			</button>		
		</c:otherwise>
	</c:choose>	

	<c:if test="${authRole != 'ANNONYMOUS' && accountId == post.writer.userId}">
		<button type="button" class="btn btn-info" onclick="location.href='<c:url value="/board/free/edit/${post.seq}"/>'">
			<span class="glyphicon glyphicon-edit hidden-xs"></span> <spring:message code="common.button.edit"/>
		</button>
		<button type="button" class="btn btn-danger" onclick="confirmDelete();">
			<span class="glyphicon glyphicon-trash hidden-xs"></span> <spring:message code="common.button.delete"/>
		</button>	
	</c:if>
	<c:choose>
		<c:when test="${authAdminRole == 'ROOT' && post.status.notice != 'notice' && post.status.delete != 'delete'}">
			<button type="button" class="btn btn-default" onclick="location.href='<c:url value="/board/notice/set/${post.seq}"/>'">
				<spring:message code="common.button.set.as.notice"/>
			</button>
		</c:when>
		<c:when test="${authAdminRole == 'ROOT' && post.status.notice == 'notice' && post.status.delete != 'delete'}">
			<button type="button" class="btn btn-default" onclick="location.href='<c:url value="/board/notice/cancel/${post.seq}"/>'">
				<spring:message code="common.button.cancel.notice"/>
			</button>		
		</c:when>
	</c:choose>	
	
	<jsp:include page="../include/footer.jsp"/>
</div> <!-- /.container -->

<script src="<%=request.getContextPath()%>/resources/bootstrap/js/bootstrap.min.js"></script>
<script src="<%=request.getContextPath()%>/resources/summernote/js/summernote.min.js"></script>
<!--angular-summernote dependencies -->
<script src="<%=request.getContextPath()%>/resources/angular-summernote/js/angular-summernote.min.js"></script>
<script src="<%=request.getContextPath()%>/resources/infinite-scroll/js/ng-infinite-scroll.min.js"></script>
<script src="<%=request.getContextPath()%>/resources/angular/js/angular-sanitize.min.js"></script>
<script src="<%=request.getContextPath()%>/resources/jakduk/js/jakduk.js"></script>
<c:if test="${fn:contains('ko', pageContext.response.locale.language)}">
	<script src="<%=request.getContextPath()%>/resources/summernote/lang/summernote-ko-KR.js"></script>
	<c:set var="summernoteLang" value="ko-KR"/>
</c:if>
<script type="text/javascript">

var jakdukApp = angular.module("jakdukApp", ["summernote", "infinite-scroll", "ngSanitize"]);

jakdukApp.controller("boardFreeCtrl", function($scope, $http) {
	$scope.alert = {};
	$scope.likeConn = "none";
	$scope.dislikeConn = "none";
	
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
			} else if (type == "dislike") {
				$scope.dislikeConn = "connecting";
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
				} else if (type == "dislike") {
					$scope.dislikeConn = "success";
				}
				
			});
			reqPromise.error(function(data, status, headers, config) {				
				$scope.alert.msg = '<spring:message code="common.msg.error.network.unstable"/>';
				$scope.alert.classType = "alert-danger";
				
				if (type == "like") {
					$scope.likeConn = "none";
				} else if (type == "dislike") {
					$scope.dislikeConn = "none";
				}
			});
		}
	};	
});


jakdukApp.controller("commentCtrl", function($scope, $http) {
	$scope.commentList = [];
	$scope.commentAlert = {};
	$scope.commentPage = 1;
	$scope.infiniteDisabled = false;
	$scope.btnWriteCommentHide = false;
	$scope.summernoteAlert = {};
	$scope.commentFeelingConn = {};
	$scope.commentFeelingAlert = {};
	$scope.numberOfCommentLike = {};
	$scope.numberOfCommentDislike = {};
	$scope.loadCommentConn = "none";
	$scope.writeCommentConn = "none";
	$scope.writeCommentAlert = {};

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
			$scope.writeCommentAlert = {"classType":"text-info", "msg":'<spring:message code="common.msg.be.cummunicating.server"/>'};
			
			reqPromise.success(function(data, status, headers, config) {
				$scope.summernote.content = "♪";
				$scope.loadComments("btnWriteComment", 1, 100);
				
				var page = parseInt($scope.commentCount / Jakduk.BoardCommentSize);
				if ($scope.commentCount % Jakduk.BoardCommentSize > 0) {
					page++;
				}
				
				$scope.commentPage = page;			
				$scope.commentAlert = {};
				$scope.summernoteAlert = {};
				$scope.writeCommentAlert = {};
				$scope.writeCommentConn = "none";
			});
			reqPromise.error(function(data, status, headers, config) {
				$scope.writeCommentAlert = {"classType":"text-danger", "msg":'<spring:message code="common.msg.error.network.unstable"/>'};
				$scope.writeCommentConn = "none";				
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
	
	$scope.initComment = function() {
		
		$scope.loadComments("init", $scope.commentPage, Jakduk.BoardCommentSize);
		$scope.infiniteDisabled = true;
	}
	
	// 현재 안씀.
	$scope.loadCommentCount = function() {
		var bUrl = '<c:url value="/board/free/comment/count/${post.seq}"/>';
		
		var reqPromise = $http.get(bUrl);
		
		reqPromise.success(function(data, status, headers, config) {
			$scope.commentCount = data.count;
		});
		reqPromise.error(function(data, status, headers, config) {
			
		});
	};
	
	$scope.loadComments = function(type, page, size) {
		var bUrl = '<c:url value="/board/free/comment/${post.seq}?page=' + page + '&size=' + size + '"/>';
		
		if ($scope.loadCommentConn == "none") {
			var reqPromise = $http.get(bUrl);
			
			$scope.loadCommentConn = "connecting";
			
			reqPromise.success(function(data, status, headers, config) {
				
				$scope.commentCount = data.count;
							
				if (data.comments.length < 1) { // 댓글이 없을때
					if (type == "init") {					
					} else {
						$scope.commentAlert.msg = '<spring:message code="board.msg.there.is.no.new.comment"/>';
						$scope.commentAlert.classType = "alert-warning";				
					}				
				} else { // 댓글이 1개 이상일때
					
					if (type == "btnWriteComment") {
						$scope.commentList = data.comments;	
					} else {
						if ($scope.commentPage == page) { // 
							$scope.commentList = data.comments;					
						} else if ($scope.commentPage < page) { // 기존 댓글이 있고, 로드한 데이터는 추가
							
							if (data.comments.length == size) { // 로드한 데이터가 size에 딱 맞을때
								$scope.commentList = $scope.commentList.concat(data.comments);
								$scope.commentPage++;
							} else { // 로드한 데이터가 size보다 작을때에는 기존 추가되지 않은 data만 추가한다.
								var remainder = $scope.commentList.length % size;
								var newComments = data.comments.slice(remainder, size - 1);
								
								if (newComments.length < 1) {
									$scope.commentAlert.msg = '<spring:message code="board.msg.there.is.no.new.comment"/>';
									$scope.commentAlert.classType = "alert-info";
								} else {
									$scope.commentList = $scope.commentList.concat(newComments);
								}
							}
						}
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
		$scope.loadComments("btnMoreComment", $scope.commentPage + 1, Jakduk.BoardCommentSize);
	};
	
	$scope.btnRefreshComment = function() {
		$scope.commentAlert = {};
		$scope.commentList = [];
		$scope.commentPage = 1;
		$scope.loadComments("btnRefreshComment", $scope.commentPage, Jakduk.BoardCommentSize);
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