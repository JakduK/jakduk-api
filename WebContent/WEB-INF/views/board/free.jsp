<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<!DOCTYPE html>
<html ng-app="jakdukApp">
<head>
	<jsp:include page="../include/html-header.jsp"></jsp:include>
	
	<link href="<%=request.getContextPath()%>/resources/font-awesome/css/font-awesome.min.css" rel="stylesheet">
</head>
<body>

<div class="container">
<jsp:include page="../include/navigation-header.jsp"/>

<sec:authorize access="isAnonymous()">
	<c:set var="authRole" value="ANNONYMOUS"/>
</sec:authorize>
<sec:authorize access="hasAnyRole('ROLE_USER_01', 'ROLE_USER_02', 'ROLE_USER_03')">
	<c:set var="authRole" value="USER"/>
</sec:authorize>

<div class="page-header">
  <h4>
	  <a href="<c:url value="/board"/>"><spring:message code="board.name.free"/></a>
	  <small><spring:message code="board.name.free.about"/></small>
  </h4>
</div>

<div class="btn-group">
	<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
		<c:choose>
			<c:when test="${boardListInfo.category != 'none'}">
				<spring:message code="${categorys[boardListInfo.category]}"/>
				<span class="caret"></span>
			</c:when>
			<c:otherwise>
				<spring:message code="board.category"/> <span class="caret"></span>
			</c:otherwise>
		</c:choose>		
	</button>
	<ul class="dropdown-menu" role="menu">
		<c:forEach items="${categorys}" var="category">
			<li><a href="?category=${category.key}"><spring:message code="${category.value}"/></a></li>	
		</c:forEach>
	</ul>
</div>

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

<div class="panel panel-info" ng-controller="boardCtrl">
  <!-- Default panel contents -->
  <div class="panel-heading hidden-xs">
  	<div class="row">
  		<div class="col-sm-2"><spring:message code="board.number"/> | <spring:message code="board.category"/></div>
  		<div class="col-sm-4"><spring:message code="board.subject"/></div>
  		<div class="col-sm-3"><spring:message code="board.writer"/> | <spring:message code="board.date"/></div>
  		<div class="col-sm-3"><spring:message code="board.views"/> | <spring:message code="board.like"/> | <spring:message code="board.dislike"/></div>
  	</div>  	
  </div> <!-- /panel-heading -->

	<ul class="list-group">
		<!-- posts as notice -->
		<c:forEach items="${notices}" var="notice">
			<li class="list-group-item list-group-item-warning">
				<div class="row">
					<div class="col-sm-2">
						<spring:message code="board.notice"/>
					</div>
					<div class="col-sm-4">
						<c:if test="${notice.status.device == 'mobile'}"><small><i class="fa fa-mobile fa-lg"></i></small></c:if>
						<c:if test="${notice.status.device == 'tablet'}"><small><i class="fa fa-tablet fa-lg"></i></small></c:if>
						<c:if test="${!empty galleriesCount[notice.id]}"><small><span class="glyphicon glyphicon-picture"></span></small></c:if>
						<a href="<c:url value="/board/free/${notice.seq}?page=${boardListInfo.page}&category=${boardListInfo.category}"/>">
							<c:choose>
								<c:when test="${notice.status.delete == 'delete'}">
									<strong><spring:message code="board.msg.deleted"/></strong>
								</c:when>
								<c:otherwise>
									<strong>${notice.subject}</strong>
								</c:otherwise>
							</c:choose>
							<c:if test="${!empty commentCount[notice.id]}">
								<span class="text-success">&nbsp;[${commentCount[notice.id]}]</span>
							</c:if>		
						</a>
					</div>
					<div class="col-sm-3">
						${notice.writer.username}
						|
						<%@page import="java.util.Date"%>
						<%Date CurrentDate = new Date();%>
						<fmt:formatDate var="nowDate" value="<%=CurrentDate %>" pattern="yyyy-MM-dd" />
						<fmt:formatDate var="postDate" value="${createDate[notice.id]}" pattern="yyyy-MM-dd" />
					
						<c:choose>
							<c:when test="${postDate < nowDate}">
								<fmt:formatDate value="${createDate[notice.id]}" pattern="${dateTimeFormat.date}" />
							</c:when>
							<c:otherwise>
								<fmt:formatDate value="${createDate[notice.id]}" pattern="${dateTimeFormat.time}" />
							</c:otherwise>
						</c:choose>					
					</div>
					<div class="col-sm-3 hidden-xs">
						<spring:message code="board.views"/><strong> ${notice.views}</strong> |
						<span class="text-primary">
							<span class="glyphicon glyphicon-thumbs-up"></span>
							<strong>
								<c:choose>
									<c:when test="${!empty usersLikingCount[notice.id]}">${usersLikingCount[notice.id]}</c:when>
									<c:otherwise>0</c:otherwise>
								</c:choose>
							</strong>
						</span> |
						<span class="text-danger">
							<span class="glyphicon glyphicon-thumbs-down"></span>
							<strong>
								<c:choose>
									<c:when test="${!empty usersDislikingCount[notice.id]}">${usersDislikingCount[notice.id]}</c:when>
									<c:otherwise>0</c:otherwise>
								</c:choose>
							</strong>
						</span>						
					</div>
				</div>
			</li>
		</c:forEach>
		<!-- posts -->
		<c:forEach items="${posts}" var="post">
			<li class="list-group-item">
				<div class="row">
					<div class="col-sm-2">	
						${post.seq}
						|
						<c:if test="${!empty post.categoryName}">
							<spring:message code="${categorys[post.categoryName]}"/>
						</c:if>
					</div>
					<div class="col-sm-4">
						<c:if test="${!empty galleriesCount[post.id]}"><small><span class="glyphicon glyphicon-picture"></span></small></c:if>
						<c:if test="${post.status.device == 'mobile'}"><small><i class="fa fa-mobile fa-lg"></i></small></c:if>
						<c:if test="${post.status.device == 'tablet'}"><small><i class="fa fa-tablet fa-lg"></i></small></c:if>
						<a href="<c:url value="/board/free/${post.seq}?page=${boardListInfo.page}&category=${boardListInfo.category}"/>">
							<c:choose>
								<c:when test="${post.status.delete == 'delete'}">
									<strong><spring:message code="board.msg.deleted"/></strong>
								</c:when>
								<c:otherwise>
									<strong>${post.subject}</strong>
								</c:otherwise>
							</c:choose>
							<c:if test="${!empty commentCount[post.id]}">
								<span class="text-success">&nbsp;[${commentCount[post.id]}]</span>
							</c:if>		
						</a>
					</div>
					
					<div class="col-sm-3">
						${post.writer.username}
						|
						<%@page import="java.util.Date"%>
						<%Date CurrentDate = new Date();%>
						<fmt:formatDate var="nowDate" value="<%=CurrentDate %>" pattern="yyyy-MM-dd" />
						<fmt:formatDate var="postDate" value="${createDate[post.id]}" pattern="yyyy-MM-dd" />
					
						<c:choose>
							<c:when test="${postDate < nowDate}">
								<fmt:formatDate value="${createDate[post.id]}" pattern="${dateTimeFormat.date}" />
							</c:when>
							<c:otherwise>
								<fmt:formatDate value="${createDate[post.id]}" pattern="${dateTimeFormat.time}" />
							</c:otherwise>
						</c:choose>		
					</div>
					<div class="col-sm-3">
						<spring:message code="board.views"/><strong> ${post.views}</strong> |
						<span class="text-primary">
							<span class="glyphicon glyphicon-thumbs-up"></span>
							<strong>
								<c:choose>
									<c:when test="${!empty usersLikingCount[post.id]}">${usersLikingCount[post.id]}</c:when>
									<c:otherwise>0</c:otherwise>
								</c:choose>
							</strong>
						</span> |
						<span class="text-danger">
							<span class="glyphicon glyphicon-thumbs-down"></span>
							<strong>
								<c:choose>
									<c:when test="${!empty usersDislikingCount[post.id]}">${usersDislikingCount[post.id]}</c:when>
									<c:otherwise>0</c:otherwise>
								</c:choose>
							</strong>
						</span>		
					</div>	
				</div> <!-- /row -->	
			</li>
		</c:forEach>
	</ul>
</div>

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

	<div>
		<ul class="pagination">
		 <c:choose>
		 	<c:when test="${pageInfo.prevPage == -1}">
		 		<li class="disabled"><a href="#">&laquo;</a></li>
		 	</c:when>
		 	<c:otherwise>
		 		<li><a href="?page=${pageInfo.prevPage}&category=${boardListInfo.category}">&laquo;</a></li>
		 	</c:otherwise>
		 </c:choose>
		 <c:forEach begin="${pageInfo.startPage}" end="${pageInfo.endPage}" var="pageIdx">
		 	<c:choose>
		 		<c:when test="${boardListInfo.page == pageIdx}">
		 			<li class="active"><a href="?page=${pageIdx}&category=${boardListInfo.category}">${pageIdx}</a></li>
		 		</c:when>
		 		<c:otherwise>
		 			<li><a href="?page=${pageIdx}&category=${boardListInfo.category}">${pageIdx}</a></li>
		 		</c:otherwise>
		 	</c:choose>
		 </c:forEach>
		 <c:choose>
		 	<c:when test="${pageInfo.nextPage == -1}">
		 		<li class="disabled"><a href="#">&raquo;</a></li>
		 	</c:when>
		 	<c:otherwise>
		 		<li><a href="?page=${pageInfo.nextPage}&category=${boardListInfo.category}">&raquo;</a></li>
		 	</c:otherwise>
		 </c:choose> 
		</ul>
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

jakdukApp.controller("boardCtrl", function($scope) {
});

function needLogin() {
	if (confirm('<spring:message code="board.msg.need.login.for.write"/>') == true) {
		location.href = "<c:url value="/board/free/write"/>";
	}
}
</script>

<jsp:include page="../include/body-footer.jsp"/>

</body>
</html>