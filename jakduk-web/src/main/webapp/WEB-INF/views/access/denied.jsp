<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<!--[if IE 9]> <html lang="ko" class="ie9" ng-app="jakdukApp"> <![endif]-->
<!--[if !IE]><!--> <html lang="ko" ng-app="jakdukApp"> <!--<![endif]-->
	<head>
		<jsp:include page="../include/html-header.jsp"/>
		<link rel="stylesheet" href="<%=request.getContextPath()%>/bundles/base.css">
	</head>
	<body class="header-fixed">
		<h1 id="banner">Unauthorized</h1>
		<hr/>

		<p class="message">Access denied!</p>
		<jsp:include page="../include/footer.jsp"/>
	</body>
</html>