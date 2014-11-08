<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>    
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html ng-app="jakdukApp">
<head>
	<jsp:include page="../include/html-header.jsp"></jsp:include>
</head>
<body>

<div class="container">
<jsp:include page="../include/navigation-header.jsp"/>

<div class="page-header">
  <h4><spring:message code="board.name.free"/> <small><spring:message code="board.name.free.about"/></small></h4>
</div>

<!-- Single button -->
<div class="btn-group">
  <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
		<spring:message code="board.category"/> <span class="caret"></span>
  </button>
  <ul class="dropdown-menu" role="menu">
	  <li><a href="?category=14"><spring:message code="board.category.all"/></a></li>	
	  <c:forEach items="${categorys}" var="category">
	   <li><a href="?category=${category.categoryId}"><spring:message code="${category.name}"/></a></li>	
		</c:forEach>
  </ul>
</div>

<sec:authorize access="isAnonymous()">
	<a href="javascript:needLogin();" class="btn btn-primary" role="button">
		<span class="glyphicon glyphicon-pencil"></span> <spring:message code="board.write"/>
	</a>
</sec:authorize>
<sec:authorize access="hasAnyRole('ROLE_USER_01', 'ROLE_USER_02', 'ROLE_USER_03')">
	<a href="<c:url value="/board/free/write"/>" class="btn btn-primary" role="button">
		<span class="glyphicon glyphicon-pencil"></span> <spring:message code="board.write"/>
	</a>
</sec:authorize>
<p></p>
<div class="panel panel-default">
  <!-- Default panel contents -->
  <div class="panel-heading visible-sm visible-md visible-lg">
  	<div class="row">
  		<div class="col-sm-2"><spring:message code="board.number"/> | <spring:message code="board.category"/></div>
  		<div class="col-sm-3"><spring:message code="board.subject"/></div>
  		<div class="col-sm-3"><spring:message code="board.writer"/> | <spring:message code="board.date"/></div>
  		<div class="col-sm-4"><spring:message code="board.views"/> | <spring:message code="board.like"/> | <spring:message code="board.dislike"/></div>
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
		<c:if test="${!empty post.categoryId}">
			<fmt:message key="${usingCategoryNames[post.categoryId]}"/>
		</c:if>
	</div>
	<a href="<c:url value="/board/free/${post.seq}?page=${listInfo.page}&category=${listInfo.category}"/>">
		<div class="col-sm-3"><strong>${post.subject}</strong></div>
	</a>			
	<div class="col-sm-3">
		${post.writer.username}
		|
		<%@page import="java.util.Date"%>
		<%Date CurrentDate = new Date();%>
		<fmt:formatDate var="nowDate" value="<%=CurrentDate %>" pattern="yyyy-MM-dd" />
		<fmt:formatDate var="postDate" value="${createDate[post.id]}" pattern="yyyy-MM-dd" />
	
		<c:choose>
			<c:when test="${postDate < nowDate}">
				<fmt:formatDate value="${createDate[post.id]}" pattern="yyyy-MM-dd" />
			</c:when>
			<c:otherwise>
				<fmt:formatDate value="${createDate[post.id]}" pattern="hh:mm (a)" />
			</c:otherwise>
		</c:choose>
	</div>
	<div class="col-sm-4">
		<spring:message code="board.views"/><strong> ${post.views}</strong>
		| <span class="text-primary"><span class="glyphicon glyphicon-thumbs-up"></span><strong> ${fn:length(post.usersLiking)}</strong></span>
		| <span class="text-danger"><span class="glyphicon glyphicon-thumbs-down"></span><strong> ${fn:length(post.usersDisliking)}</strong></span>
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
 		<li><a href="?page=${pageInfo.prevPage}&category=${listInfo.category}">&laquo;</a></li>
 	</c:otherwise>
 </c:choose>
 <c:forEach begin="${pageInfo.startPage}" end="${pageInfo.endPage}" var="pageIdx">
 	<c:choose>
 		<c:when test="${listInfo.page == pageIdx}">
 			<li class="active"><a href="?page=${pageIdx}&category=${listInfo.category}">${pageIdx}</a></li>
 		</c:when>
 		<c:otherwise>
 			<li><a href="?page=${pageIdx}&category=${listInfo.category}">${pageIdx}</a></li>
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
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/resources/bootstrap/js/bootstrap.min.js"></script>    
<script type="text/javascript">

var jakdukApp = angular.module("jakdukApp", []);

function needLogin() {
	if (confirm('<spring:message code="board.msg.need.login"/>') == true) {
		location.href = "<c:url value="/board/free/write"/>";
	}
}
</script>

<jsp:include page="../include/body-footer.jsp"/>

</body>
</html>