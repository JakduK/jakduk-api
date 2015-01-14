<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<jsp:include page="../include/html-header.jsp"></jsp:include>

<link href="<%=request.getContextPath()%>/resources/bootstrap/css/bootstrapApp.css" rel="stylesheet">
</head>
<body>
	<h1 id="banner">Unauthorized</h1>
	<hr/>
	
	<p class="message">Access denied!</p>
	<jsp:include page="../include/footer.jsp"/>
</body>
</html>