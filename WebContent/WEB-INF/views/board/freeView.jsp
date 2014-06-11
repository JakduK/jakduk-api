<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>    
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>    
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html ng-app>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<jsp:include page="../include/html-header.jsp"></jsp:include>

</head>
<body>
<jsp:include page="../include/navigation-header.jsp"/>

    <!-- Begin page content -->
    <div class="container">
<div class="panel panel-default">
  <!-- Default panel contents -->
  <div class="panel-heading">
  	<h4>${post.subject}&nbsp;<small><fmt:message key="${category.name}"/></small></h4>
  	<div class="row">
  		<div class="col-sm-1">
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
		
    <!-- List group -->
  <ul class="list-group">
    <li class="list-group-item">
    	<small>
    	${post.writer.username} | <spring:message code="board.views"/> ${post.views} | <fmt:formatDate value="${createDate}" pattern="yyyy-MM-dd hh:mm (a)" />
    	</small>
    </li>
    <li class="list-group-item">Dapibus ac facilisis in</li>
    <li class="list-group-item">Morbi leo risus</li>
    <li class="list-group-item">Porta ac consectetur ac</li>
    <li class="list-group-item">Vestibulum at eros</li>
  </ul>
  <div class="panel-body">
    <p>...</p>
  </div>
  


</div>

    </div>

    
</body>
</html>