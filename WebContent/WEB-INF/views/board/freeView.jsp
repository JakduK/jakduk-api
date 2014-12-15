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
	<button type="button" class="btn btn-default" onclick="needLogin();">
		<span class="glyphicon glyphicon-pencil"></span> <spring:message code="board.write"/>
	</button>
</sec:authorize>
<sec:authorize access="hasAnyRole('ROLE_USER_01', 'ROLE_USER_02', 'ROLE_USER_03')">
	<button type="button" class="btn btn-default" onclick="location.href='<c:url value="/board/free/write"/>'">
		<span class="glyphicon glyphicon-pencil"></span> <spring:message code="board.write"/>
	</button>
</sec:authorize>

<p></p>

<!-- Begin page content -->
<div class="panel panel-info">
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
  		
	<%@page import="java.util.Date"%>
	<%Date CurrentDate = new Date();%>
	<fmt:formatDate var="nowDate" value="<%=CurrentDate %>" pattern="yyyy-MM-dd" />
	<fmt:formatDate var="postDate" value="${createDate}" pattern="yyyy-MM-dd" />
  		
  		<div class="col-md-5 visible-xs">
	  		<small>
			<c:choose>
				<c:when test="${postDate < nowDate}">
					<fmt:formatDate value="${createDate}" pattern="yyyy-MM-dd" />
				</c:when>
				<c:otherwise>
					<fmt:formatDate value="${createDate}" pattern="hh:mm (a)" />
				</c:otherwise>
			</c:choose> 
	    	| <spring:message code="board.views"/> ${post.views} 
	    	</small>
  		</div>
  		<div class="col-md-5 visible-sm visible-md visible-lg">
	  		<small>
	    	<fmt:formatDate value="${createDate}" pattern="yyyy-MM-dd hh:mm (a)" />
	    	| <spring:message code="board.views"/> ${post.views}
	    	</small>
  		</div>  		
  	</div>
  </div>
  
  <div class="panel-body">
    <p>${post.content}</p>
  </div>
  
	<div class="panel-footer text-center" ng-controller="boardFreeCtrl">
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
	  <div class="panel-heading"><spring:message code="board.msg.comment.count" arguments="{{commentCount}}"/></div>
	
	  <!-- List group -->
	  <ul class="list-group">
				<li class="list-group-item" ng-repeat="comment in commentList">
	    			<div class="row">
	    					<div class="col-xs-12"><strong>{{comment.writer.username}}</strong> | 
	    					
	    					{{dateFromObjectId(comment.id) | date:'yyyy-MM-dd hh:mm (a)'}}</div>
	    					<div class="col-xs-12">{{comment.content}}</div>
	    			</div>			
				</li>
	  </ul>
	  
		<div class="panel-footer text-center">
			<button type="button" class="btn btn-default btn-lg btn-block" ng-click="btnMoreComment()"><spring:message code="common.button.load.comment"/></button>
			<p></p>
			<div class="alert {{commentAlert.classType}}" role="alert" ng-show="commentAlert.msg">{{commentAlert.msg}}</div>
		</div>
	</div>
	
	<div class="panel panel-default">
	<div class="panel-body">
	<summernote config="options" ng-model="commentWrite.content" ng-init="commentWrite.seq=${post.seq}"></summernote>
	</div>
	<div class="panel-footer">
	<a class="btn btn-primary btn-lg" href="#" role="button" ng-click="btnWriteComment()"><spring:message code="common.button.write.comment"/></a>
	</div>
	</div>	
</div>

<button type="button" class="btn btn-default" onclick="location.href='${listUrl}'">
	<spring:message code="board.list"/>
</button>

<sec:authorize access="isAnonymous()">
	<button type="button" class="btn btn-default" onclick="needLogin();">
		<span class="glyphicon glyphicon-pencil"></span> <spring:message code="board.write"/>
	</button>
</sec:authorize>
<sec:authorize access="hasAnyRole('ROLE_USER_01', 'ROLE_USER_02', 'ROLE_USER_03')">
	<button type="button" class="btn btn-default" onclick="location.href='<c:url value="/board/free/write"/>'">
		<span class="glyphicon glyphicon-pencil"></span> <spring:message code="board.write"/>
	</button>
</sec:authorize>

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
	
	$scope.options = {
			height: 100,
			toolbar: [
	      ['font', ['bold', 'italic', 'underline', 'strikethrough', 'clear']],
	      ['fontname', ['fontname']],
	      // ['fontsize', ['fontsize']], // Still buggy
	      ['color', ['color']],
	      ['para', ['ul', 'ol']],
	      ['insert', ['link']],
	      ['help', ['help']]			          
				]
		};	

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
			$scope.loadComments("writeComment", $scope.commentPage + 1);
			$scope.loadCommentCount();
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
		
		$scope.loadComments("init", $scope.commentPage);
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
	
	$scope.loadComments = function(type, page) {
		var bUrl = '<c:url value="/board/free/comment/${post.seq}?page=' + page + '"/>';
		
		var reqPromise = $http.get(bUrl);
		
		reqPromise.success(function(data, status, headers, config) {
			
			if (data.comments.length < 1) {
				$scope.aaa1 = "aaa";
				if (type == "init") {
					$scope.commentAlert.msg = '<spring:message code="board.msg.there.is.no.comment"/>';
					$scope.commentAlert.classType = "alert-info";
				} else {
					$scope.commentAlert.msg = '<spring:message code="board.msg.there.is.no.new.comment"/>';
					$scope.commentAlert.classType = "alert-info";				
				}
			} else {
				if ($scope.commentPage == page) {
					$scope.commentList = data.comments;
				} else if ($scope.commentPage < page) {
					$scope.commentList = $scope.commentList.concat(data.comments);
					$scope.commentPage++;
				}
			}			
		});
		reqPromise.error(function(data, status, headers, config) {
			
		});
	};
	
	$scope.btnMoreComment = function() {
		$scope.loadComments("btnMoreComment", $scope.commentPage + 1);
		$scope.loadCommentCount();
	};

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