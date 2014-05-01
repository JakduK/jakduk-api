<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>    
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<jsp:include page="../include/html-header.jsp"></jsp:include>

<!-- Bootstrap core CSS -->
<link href="<%=request.getContextPath()%>/web-resources/bootstrap/css/bootstrap.min.css" rel="stylesheet">
<!-- Bootstrap theme -->
<link href="<%=request.getContextPath()%>/web-resources/bootstrap/css/bootstrap-theme.min.css" rel="stylesheet">

</head>
<body>
<jsp:include page="../include/navigation-header.jsp"/>
<div class="container">

<spring:message code="board.number"/>
<spring:message code="board.subject"/>

<p><a href="<c:url value="/board/free/write"/>" class="btn btn-primary" role="button"><spring:message code="board.write"/></a></p>

<!-- Single button -->
<div class="btn-group">
  <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
    <spring:message code="board.category"/> <span class="caret"></span>
  </button>
  <ul class="dropdown-menu" role="menu">
  <c:forEach items="${categorys}" var="category">
   <li><a href="#"><spring:message code="${category.name}"/></a></li>	
	</c:forEach>
  </ul>
</div>

            <div class="row">
        <div class="col-sm-12">
          <h4>Most Basic - Small Columns</h4>
        </div>
      </div>
      <c:forEach items="${posts}" var="post">
		<div class="row">
			<div class="col-sm-1">${post.seq}</div>
        <div class="col-sm-1"><strong>${post.subject}</strong></div>
        <div class="col-sm-5">
        ${post.writer.username}
        |
<%@page import="java.util.Date"%>
<%
Date CurrentDate = new Date();
%>
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
       
        <hr>
      </div>
      </c:forEach>

    
<jsp:include page="../include/footer.jsp"/>
</div>

    <!-- Bootstrap core JavaScript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
    <script src="<%=request.getContextPath()%>/web-resources/bootstrap/js/bootstrap.min.js"></script>    
		<script src="<%=request.getContextPath()%>/web-resources/bootstrap/js/offcanvas.js"></script>
		
</body>
</html>