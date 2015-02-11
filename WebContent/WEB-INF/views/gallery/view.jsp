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
	<title>${gallery.name} - <spring:message code="gallery"/> &middot; <spring:message code="common.jakduk"/></title>
	<jsp:include page="../include/html-header.jsp"></jsp:include>
</head>

<body>
<div class="container jakduk-gallery">
<jsp:include page="../include/navigation-header.jsp"/>

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

<div class="form-group">
	<button type="button" class="btn btn-default" onclick="location.href='<c:url value="/gallery"/>'">
		<span class="glyphicon glyphicon-th-large"></span>
	</button>
	<c:choose>
		<c:when test="${!empty prev}">
			<button type="button" class="btn btn-default" onclick="location.href='<c:url value="/gallery/view/${prev.id}"/>'">
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
		<c:when test="${!empty next}">
			<button type="button" class="btn btn-default" onclick="location.href='<c:url value="/gallery/view/${next.id}"/>'">
				<span class="glyphicon glyphicon-chevron-right"></span>
			</button>		
		</c:when>
		<c:otherwise>
			<button type="button" class="btn btn-default" disabled="disabled">
				<span class="glyphicon glyphicon-chevron-right"></span>
			</button>		
		</c:otherwise>
	</c:choose>	
</div>

<%@page import="java.util.Date"%>
<%Date CurrentDate = new Date();%>
<fmt:formatDate var="nowDate" value="<%=CurrentDate %>" pattern="yyyy-MM-dd" />
<fmt:formatDate var="postDate" value="${createDate[gallery.id]}" pattern="yyyy-MM-dd" />
					
<blockquote>
  ${gallery.name}
  <h5><span class="glyphicon glyphicon-user"></span> ${gallery.writer.username}</h5>
  <h5>
  	<span class="glyphicon glyphicon-time"></span>
		<c:choose>
			<c:when test="${postDate < nowDate}">
				<fmt:formatDate value="${createDate[gallery.id]}" pattern="${dateTimeFormat.date}" />
			</c:when>
			<c:otherwise>
				<fmt:formatDate value="${createDate[gallery.id]}" pattern="${dateTimeFormat.time}" />
			</c:otherwise>
		</c:choose>
		&nbsp;
  	<span class="glyphicon glyphicon-eye-open"></span> ${gallery.views}
  </h5>
</blockquote>

<img class="img-responsive" src="<%=request.getContextPath()%>/gallery/${gallery.id}">

<hr/>

<!-- 기분 표시 -->
<div ng-controller="galleryController">
	<button type="button" class="btn btn-default" ng-click="btnFeeling('like')">
		<spring:message code="common.like"/>			
		<span class="text-primary" ng-init="numberOfLike=${fn:length(gallery.usersLiking)}">
			<i class="fa fa-thumbs-o-up fa-lg"></i>
		</span>
		<span class="text-primary" ng-hide="likeConn == 'connecting'">{{numberOfLike}}</span>
		<span class="text-primary"><i class="fa fa-circle-o-notch fa-spin" ng-show="likeConn == 'connecting'"></i></span>
	</button>
	<button type="button" class="btn btn-default" ng-click="btnFeeling('dislike')">		
		<spring:message code="common.dislike"/>
		<span class="text-danger" ng-init="numberOfDislike=${fn:length(gallery.usersDisliking)}">
			<i class="fa fa-thumbs-o-down fa-lg"></i>
		</span>
		<span class="text-danger" ng-hide="dislikeConn == 'connecting'">{{numberOfDislike}}</span>
		<span class="text-danger"><i class="fa fa-circle-o-notch fa-spin" ng-show="dislikeConn == 'connecting'"></i></span>				
	</button>
	<p class="{{feelingAlert.classType}}" ng-show="feelingAlert.msg">
		<span class="glyphicon {{feelingAlert.icon}}" aria-hidden="true"></span> 
		{{feelingAlert.msg}}
		</p>
</div>

<hr/>

<!-- 엮인 글 -->
<h4><spring:message code="gallery.linked.posts"/></h4>
<ul>
	<c:forEach items="${linkedPosts}" var="post">
	<fmt:formatDate var="postDate" value="${createDate[post.id]}" pattern="yyyy-MM-dd" />
		<li>
			<a href="<c:url value="/board/free/${post.seq}"/>">${post.subject}</a> 
			&nbsp;<span class="glyphicon glyphicon-user"></span> ${post.writer.username}
			&nbsp;<span class="glyphicon glyphicon-time"></span>
			<c:choose>
				<c:when test="${postDate < nowDate}">
					<fmt:formatDate value="${createDate[post.id]}" pattern="${dateTimeFormat.date}" />
				</c:when>
				<c:otherwise>
					<fmt:formatDate value="${createDate[post.id]}" pattern="${dateTimeFormat.time}" />
				</c:otherwise>
			</c:choose>			
		</li>
	</c:forEach>
</ul>

<hr/>

<div class="form-group">
	<button type="button" class="btn btn-default" onclick="location.href='<c:url value="/gallery"/>'">
		<span class="glyphicon glyphicon-th-large"></span>
	</button>
	<c:choose>
		<c:when test="${!empty prev}">
			<button type="button" class="btn btn-default" onclick="location.href='<c:url value="/gallery/view/${prev.id}"/>'">
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
		<c:when test="${!empty next}">
			<button type="button" class="btn btn-default" onclick="location.href='<c:url value="/gallery/view/${next.id}"/>'">
				<span class="glyphicon glyphicon-chevron-right"></span>
			</button>		
		</c:when>
		<c:otherwise>
			<button type="button" class="btn btn-default" disabled="disabled">
				<span class="glyphicon glyphicon-chevron-right"></span>
			</button>		
		</c:otherwise>
	</c:choose>	
</div>

<jsp:include page="../include/footer.jsp"/>
</div><!-- /.container -->

<!-- Bootstrap core JavaScript
  ================================================== -->
<!-- Placed at the end of the document so the pages load faster -->
<script src="<%=request.getContextPath()%>/resources/jquery/js/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/resources/bootstrap/js/bootstrap.min.js"></script>    

<script type="text/javascript">
var jakdukApp = angular.module("jakdukApp", []);

jakdukApp.controller("galleryController", function($scope, $http) {
	$scope.feelingAlert = {};
	$scope.likeConn = "none";
	$scope.dislikeConn = "none";
	
	$scope.btnFeeling = function(type) {
		
		if ("${authRole}" == "ANNONYMOUS") {
			$scope.feelingAlert.msg = '<spring:message code="gallery.msg.need.login.for.feel"/>';
			$scope.feelingAlert.icon = "glyphicon-warning-sign";
			$scope.feelingAlert.classType = "text-warning";
			return;
		} else if ("${accountId}" == "${post.writer.userId}") {
			$scope.feelingAlert.msg = '<spring:message code="gallery.msg.you.are.writer"/>';
			$scope.feelingAlert.icon = "glyphicon-warning-sign";
			$scope.feelingAlert.classType = "text-warning";
			return;
		}
		
		var bUrl = '<c:url value="/gallery/' + type + '/${gallery.id}.json"/>';
		
		if ($scope.likeConn == "none" && $scope.dislikeConn == "none") {
			
			var reqPromise = $http.get(bUrl);
			
			if (type == "like") {
				$scope.likeConn = "connecting";
			} else if (type == "dislike") {
				$scope.dislikeConn = "connecting";
			}
			
			reqPromise.success(function(data, status, headers, config) {
				var message = "";
				var icon = "";
				var mType = "";
				
				if (data.errorCode == "like") {
					message = '<spring:message code="gallery.msg.select.like"/>';
					icon = "glyphicon-thumbs-up";
					mType = "text-success";
					$scope.numberOfLike = data.numberOfLike;
				} else if (data.errorCode == "dislike") {
					message = '<spring:message code="gallery.msg.select.dislike"/>';
					icon = "glyphicon-thumbs-down";
					mType = "text-success";
					$scope.numberOfDislike = data.numberOfDislike;
				} else if (data.errorCode == "already") {
					message = '<spring:message code="gallery.msg.select.already.like"/>';
					icon = "glyphicon-warning-sign";
					mType = "text-warning";
				} else if (data.errorCode == "anonymous") {
					message = '<spring:message code="gallery.msg.need.login.for.feel"/>';
					icon = "glyphicon-warning-sign";
					mType = "text-warning";
				} else if (data.errorCode == "writer") {
					message = '<spring:message code="gallery.msg.you.are.writer"/>';
					icon = "glyphicon-warning-sign";
					mType = "text-warning";
				}
				
				$scope.feelingAlert.msg = message;
				$scope.feelingAlert.icon = icon;
				$scope.feelingAlert.classType = mType;
				
				if (type == "like") {
					$scope.likeConn = "success";
				} else if (type == "dislike") {
					$scope.dislikeConn = "success";
				}
				
			});
			reqPromise.error(function(data, status, headers, config) {				
				$scope.feelingAlert.msg = '<spring:message code="common.msg.error.network.unstable"/>';
				$scope.feelingAlert.icon = "glyphicon-exclamation-sign";
				$scope.feelingAlert.classType = "text-danger";
				
				if (type == "like") {
					$scope.likeConn = "none";
				} else if (type == "dislike") {
					$scope.dislikeConn = "none";
				}
			});
		}
	};		
	
});
</script>

<jsp:include page="../include/body-footer.jsp"/>

</body>

</html>