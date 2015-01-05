<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>    
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<!DOCTYPE html>
<html ng-app="jakdukApp">
<head>
	<jsp:include page="../include/html-header.jsp"></jsp:include>
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
  <div class="panel-heading visible-sm visible-md visible-lg">
  	<div class="row">
  		<div class="col-sm-2"><spring:message code="board.number"/> | <spring:message code="board.category"/></div>
  		<div class="col-sm-4"><spring:message code="board.subject"/></div>
  		<div class="col-sm-3"><spring:message code="board.writer"/> | <spring:message code="board.date"/></div>
  		<div class="col-sm-3"><spring:message code="board.view"/> | <spring:message code="board.like"/> | <spring:message code="board.dislike"/></div>
  	</div>  	
  </div> <!-- /panel-heading -->

  <!-- List group -->
  <ul class="list-group">
<c:forEach items="${posts}" var="post">
	<li class="list-group-item">
	<div class="row">
	<div class="col-sm-2">	
		${post.seq}
		|
		<c:if test="${!empty post.categoryName}">
			<fmt:message key="${categorys[post.categoryName]}"/>
		</c:if>
	</div>
	<div class="col-sm-4">
	<a href="<c:url value="/board/free/${post.seq}?page=${boardListInfo.page}&category=${boardListInfo.category}"/>">
		<strong>${post.subject}</strong>
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
		<spring:message code="board.view"/><strong> ${post.views}</strong> |
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
 		<li><a href="?page=0">&raquo;</a></li>
 	</c:otherwise>
 </c:choose> 
</ul>
 
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
	if (confirm('<spring:message code="board.msg.need.login"/>') == true) {
		location.href = "<c:url value="/board/free/write"/>";
	}
}
</script>

<jsp:include page="../include/body-footer.jsp"/>

</body>
</html>