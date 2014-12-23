<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>    
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>    
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>    
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<!DOCTYPE html>
<html ng-app="jakdukApp">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<jsp:include page="../include/html-header.jsp"></jsp:include>

<link href="<%=request.getContextPath()%>/resources/font-awesome/css/font-awesome.min.css" rel="stylesheet">

<!--summernote-->
<link href="<%=request.getContextPath()%>/resources/summernote/css/summernote.css" rel="stylesheet">

<script src="<%=request.getContextPath()%>/resources/jquery/js/jquery.min.js"></script>

</head>
<body>
<div class="container">
<jsp:include page="../include/navigation-header.jsp"/>

<c:url var="listUrl" value="/board/free">
	<c:if test="${!empty listInfo.page}">
		<c:param name="page" value="${listInfo.page}"/>
	</c:if>
	<c:if test="${!empty listInfo.category}">
		<c:param name="category" value="${listInfo.category}"/>
	</c:if>
</c:url>

<button type="button" class="btn btn-default" onclick="location.href='${listUrl}'">
	<spring:message code="board.list"/>
</button>

<sec:authorize access="isAnonymous()">
	<c:set var="authRole" value="ANNONYMOUS"/>
</sec:authorize>
<sec:authorize access="hasAnyRole('ROLE_USER_01', 'ROLE_USER_02', 'ROLE_USER_03')">
	<c:set var="authRole" value="USER"/>
</sec:authorize>

<c:choose>
	<c:when test="${authRole == 'ANNONYMOUS'}">
	<button type="button" class="btn btn-default" onclick="needLogin();">
		<span class="glyphicon glyphicon-pencil"></span> <spring:message code="board.write"/>
	</button>	
	</c:when>
	<c:when test="${authRole == 'USER'}">
	<button type="button" class="btn btn-default" onclick="location.href='<c:url value="/board/free/write"/>'">
		<span class="glyphicon glyphicon-pencil"></span> <spring:message code="board.write"/>
	</button>	
	</c:when>	
</c:choose>

<p></p>

<!-- Begin page content -->
<div class="panel panel-info" ng-controller="boardFreeCtrl">
  <!-- Default panel contents -->
  <div class="panel-heading">
  	<h4 class="panel-title">
  		${post.subject}
  		<c:if test="${!empty category}">&nbsp;<small><fmt:message key="${category.resName}"/></small></c:if>
  	</h4>
  	<div class="row">
  		<div class="col-sm-2">
	  		<small>${post.writer.username}</small>
  		</div>
  		
  		<div class="col-md-5 visible-xs">
	  		<small>
				{{dateFromObjectId("${post.id}") | date:"${datePattern}"}}
	    		| <spring:message code="board.views"/> ${post.views} 
	    	</small>
  		</div>
  		<div class="col-md-5 visible-sm visible-md visible-lg">
	  		<small>
	    		{{dateFromObjectId("${post.id}") | date:"${datePattern}"}}
	    		| <spring:message code="board.views"/> ${post.views}
	    	</small>
  		</div>  		
  	</div>
  </div>
  
  <div class="panel-body">
    <p>${post.content}</p>
  </div>
  
	<div class="panel-footer text-center">
		<button type="button" class="btn btn-primary" ng-click="btnFeeling('like')">
			<spring:message code="board.like" />
			<span ng-init="numberOfLike=${fn:length(post.usersLiking)}">{{numberOfLike}}</span>
			<span class="glyphicon glyphicon-thumbs-up"></span>
		</button>
		<button type="button" class="btn btn-danger" ng-click="btnFeeling('dislike')">		
			<spring:message code="board.dislike"/>
			<span ng-init="numberOfDislike=${fn:length(post.usersDisliking)}">{{numberOfDislike}}</span>
			<span class="glyphicon glyphicon-thumbs-down"></span>
		</button>
		<p></p>
		<div class="alert {{alert.classType}}" role="alert" ng-show="alert.msg">{{alert.msg}}</div>
	</div>
</div> <!-- /panel -->

<div ng-controller="commentCtrl">
	<div class="panel panel-default" infinite-scroll="initComment()" infinite-scroll-disabled="infiniteDisabled">
	  <!-- Default panel contents -->	  
	  <div class="panel-heading">
	  	<spring:message code="board.msg.comment.count" arguments="{{commentCount}}"/>
	  	&nbsp;
		<button type="button" class="btn btn-default" ng-click="btnRefreshComment()">
		  <span class=" glyphicon glyphicon-refresh" aria-hidden="true"></span>
		</button>	  	
	  </div>
		<!-- List group -->
		<ul class="list-group">
			<li class="list-group-item" ng-repeat="comment in commentList">
	 			<div class="row">
 					<div class="col-xs-12"><strong>{{comment.writer.username}}</strong> | 
 					
 					{{dateFromObjectId(comment.id) | date:"${datePattern}"}}</div>
 					<div class="col-xs-12">{{comment.content}}</div>
	 			</div>			
			</li>
		</ul>
	  
	<div class="panel-footer text-center" ng-show="commentCount">
		<button type="button" class="btn btn-default btn-block" ng-click="btnMoreComment()" ng-show="commentCount">
			<spring:message code="common.button.load.comment"/> <span class="glyphicon glyphicon-chevron-down"></span>
		</button>
		<p></p>
		<div class="alert {{commentAlert.classType}}" role="alert" ng-show="commentAlert.msg">{{commentAlert.msg}}</div>
	</div>
	</div>
	
	<div class="panel panel-default">
	<div class="panel-body">
	<summernote config="options" on-focus="focus(evt)" 
	ng-model="commentWrite.content" ng-init="commentWrite.seq=${post.seq}"></summernote>
	<span class="{{commentWriteAlert.classType}}" ng-show="commentWriteAlert.msg">{{commentWriteAlert.msg}}</span>
	</div>
	<div class="panel-footer">
	<a class="btn btn-primary" href="#" role="button" ng-click="btnWriteComment()">
		<span class="glyphicon glyphicon-pencil"></span> <spring:message code="common.button.write.comment"/>
	</a>
	</div>
	</div>	
</div>

<button type="button" class="btn btn-default" onclick="location.href='${listUrl}'">
	<spring:message code="board.list"/>
</button>

<c:choose>
	<c:when test="${authRole == 'ANNONYMOUS'}">
	<button type="button" class="btn btn-default" onclick="needLogin();">
		<span class="glyphicon glyphicon-pencil"></span> <spring:message code="board.write"/>
	</button>	
	</c:when>
	<c:when test="${authRole == 'USER'}">
	<button type="button" class="btn btn-default" onclick="location.href='<c:url value="/board/free/write"/>'">
		<span class="glyphicon glyphicon-pencil"></span> <spring:message code="board.write"/>
	</button>	
	</c:when>	
</c:choose>

</div> <!-- /.container -->

<script src="<%=request.getContextPath()%>/resources/bootstrap/js/bootstrap.min.js"></script>
<script src="<%=request.getContextPath()%>/resources/summernote/js/summernote.min.js"></script>
<script src="<%=request.getContextPath()%>/resources/summernote/lang/summernote-ko-KR.js"></script>
<!--angular-summernote dependencies -->
<script src="<%=request.getContextPath()%>/resources/angular-summernote/js/angular-summernote.min.js"></script>
<script src="<%=request.getContextPath()%>/resources/infinite-scroll/js/ng-infinite-scroll.min.js"></script>

<script type="text/javascript">

var jakdukApp = angular.module("jakdukApp", ["summernote", "infinite-scroll"]);

jakdukApp.controller("boardFreeCtrl", function($scope, $http) {
	$scope.alert = {};
	$scope.conn = "none";
	
	$scope.objectIdFromDate = function(date) {
		return Math.floor(date.getTime() / 1000).toString(16) + "0000000000000000";
	};
	
	$scope.dateFromObjectId = function(objectId) {
		return new Date(parseInt(objectId.substring(0, 8), 16) * 1000);
	};
	
	$scope.btnFeeling = function(status) {
		
		var bUrl = '<c:url value="/board/' + status + '/${post.seq}.json"/>';
		
		if ($scope.conn == "none") {
			
			var reqPromise = $http.get(bUrl);
			
			$scope.conn = "loading";
			
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
					message = '<spring:message code="board.msg.need.login"/>';
					mType = "alert-warning";
				} else if (data.errorCode == "writer") {
					message = '<spring:message code="board.msg.you.are.writer"/>';
					mType = "alert-warning";
				}
				
				$scope.alert.msg = message;
				$scope.alert.classType = mType;
				$scope.conn = "ok";
				
			});
			reqPromise.error(error);
		}
	};	
			
	function error(data, status, headers, config) {
		$scope.conn = "none";
		$scope.error = '<spring:message code="common.msg.error.network.unstable"/>';
	}
});


jakdukApp.controller("commentCtrl", function($scope, $http) {
	$scope.commentList = [];
	$scope.commentAlert = {};
	$scope.commentPage = 1;
	$scope.infiniteDisabled = false;
	$scope.btnWriteCommentHide = false;
	$scope.commentWriteAlert = {};

	// summernote config
	$scope.options = {
			height: 100,
			toolbar: [
	      ['font', ['bold']],
	      // ['fontsize', ['fontsize']], // Still buggy
	      ['color', ['color']],
	      ['help', ['help']]			          
				]};	
	
	$scope.focus = function(e) { 
		if ("${authRole}" == "ANNONYMOUS") {
			if (confirm('<spring:message code="board.msg.need.login"/>') == true) {
				location.href = "<c:url value="/login"/>";
			}
		}	
	}
	
	if ("${authRole}" == "ANNONYMOUS") {
		$scope.commentWriteAlert = {"classType":"text-danger", "msg":'<spring:message code="board.msg.need.login"/>'};
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
		
		var reqPromise = $http.post(bUrl, $scope.commentWrite, config);
		
		reqPromise.success(function(data, status, headers, config) {
			$scope.commentWrite.content = "";
			$scope.loadComments("btnWriteComment", 1, 100);
			$scope.loadCommentCount();
			
			var page = parseInt($scope.commentCount / 5);
			if ($scope.commentCount % 5 > 0) {
				page++;
			}
			
			$scope.commentPage = page;			
			$scope.commentAlert = {};
		});
		reqPromise.error(function(data, status, headers, config) {
			
		});
		
	};
	
	$scope.objectIdFromDate = function(date) {
		return Math.floor(date.getTime() / 1000).toString(16) + "0000000000000000";
	};
	
	$scope.dateFromObjectId = function(objectId) {
		return new Date(parseInt(objectId.substring(0, 8), 16) * 1000);
	};
	
	$scope.initComment = function() {
		
		$scope.loadComments("init", $scope.commentPage, 5);
		$scope.loadCommentCount();
		$scope.infiniteDisabled = true;
	}
	
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
		
		var reqPromise = $http.get(bUrl);
		
		reqPromise.success(function(data, status, headers, config) {
						
			if (data.comments.length < 1) { // 댓글이 1개 미만일때
				if (type == "init") {					
				} else if (type =="btnRefreshComment") {
				} else {
					$scope.commentAlert.msg = '<spring:message code="board.msg.there.is.no.new.comment"/>';
					$scope.commentAlert.classType = "alert-info";				
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
		});
		reqPromise.error(function(data, status, headers, config) {
			
		});
	};
	
	$scope.btnMoreComment = function() {
		$scope.loadComments("btnMoreComment", $scope.commentPage + 1, 5);
		$scope.loadCommentCount();
	};
	
	$scope.btnRefreshComment = function() {
		$scope.commentAlert = {};
		$scope.commentList = [];
		$scope.commentPage = 1;
		$scope.loadComments("btnRefreshComment", $scope.commentPage, 5);
		$scope.loadCommentCount();
	}

});

function needLogin() {
	if (confirm('<spring:message code="board.msg.need.login"/>') == true) {
		location.href = "<c:url value="/board/free/write"/>";
	}
}	
</script>

<jsp:include page="../include/body-footer.jsp"/>

</body>
</html>