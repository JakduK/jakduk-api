<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>    
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<!DOCTYPE html>
<html ng-app="jakdukApp">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<jsp:include page="../include/html-header.jsp"></jsp:include>
</head>
<body>
<div class="container">
<jsp:include page="../include/navigation-header.jsp"/>

<div class="page-header">
  <h4>
	  <a href="<c:url value="/gallery"/>"><spring:message code="gallery"/></a>
	  <small><spring:message code="gallery.about"/></small>
  </h4>
</div>

<p class="bg-info"><spring:message code="common.msg.test.version"/></p>
<div class="row">
	<c:forEach items="${galleries}" var="gallery">
		<div class="col-xs-6 col-md-3">
			<div class="thumbnail">
				<a href="<%=request.getContextPath()%>/gallery/${gallery.id}"><img src="<%=request.getContextPath()%>/gallery/thumbnail/${gallery.id}" alt="..."></a>
				<div class="caption">
					<div class="text-overflow">${posts[gallery.boardItem.id].subject}</div>
					<div><small>${gallery.writer.username}</small></div>
				</div>
			</div>  
		</div>
	</c:forEach>
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
</script>

<jsp:include page="../include/body-footer.jsp"/>

</body>