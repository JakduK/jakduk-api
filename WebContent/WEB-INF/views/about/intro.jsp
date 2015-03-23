<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<!DOCTYPE html>
<html ng-app="jakdukApp">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title><spring:message code="about.site"/> &middot; <spring:message code="common.jakduk"/></title>
	<jsp:include page="../include/html-header.jsp"></jsp:include>
</head>

<body>
<div class="wrapper">
	<jsp:include page="../include/navigation-header.jsp"/>
	
	<!--=== Breadcrumbs ===-->
	<div class="breadcrumbs">
		<div class="container">
			<h1 class="pull-left"><a href="<c:url value="/about/intro/refresh"/>"><spring:message code="about.site"/></a></h1>
		</div><!--/container-->
	</div><!--/breadcrumbs-->
	<!--=== End Breadcrumbs ===-->	

	<!--=== Content Part ===-->
	<div class="container content">
		<c:choose>
			<c:when test="${lang == 'ko'}">
				<jsp:include page="about-us-ko.jsp"/>
			</c:when>
			<c:when test="${lang == 'en'}">
				<jsp:include page="about-us-en.jsp"/>
			</c:when>
			<c:otherwise>
				<jsp:include page="about-us-en.jsp"/>
			</c:otherwise>
		</c:choose>
	</div>
	
	<jsp:include page="../include/footer.jsp"/>

</div><!-- /.container -->

<!-- Bootstrap core JavaScript
  ================================================== -->
<!-- Placed at the end of the document so the pages load faster -->
<script src="<%=request.getContextPath()%>/resources/jquery/dist/jquery.min.js"></script>

<script type="text/javascript">
var jakdukApp = angular.module("jakdukApp", []);
</script>

<jsp:include page="../include/body-footer.jsp"/>

</body>
</html>