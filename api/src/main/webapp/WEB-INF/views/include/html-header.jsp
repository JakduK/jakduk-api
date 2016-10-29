<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<sec:authorize access="isAuthenticated()">
	<sec:authentication property="principal.providerId" var="userType"/>
	<c:choose>
		<c:when test="${userType == 'JAKDUK'}">
			<sec:authentication property="principal.nickname" var="accountName"/>
		</c:when>
		<c:when test="${userType == 'FACEBOOK' || userType == 'DAUM'}">
			<sec:authentication property="principal.username" var="accountName"/>
		</c:when>
	</c:choose>
</sec:authorize>

<meta charset="utf-8">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta content="width=device-width, initial-scale=1.0" name="viewport">
<meta content="K LEAGUE JAKDU KING" name="description">
<meta content="pio." name="author">

<link rel="shortcut icon" href="<%=request.getContextPath()%>/resources/jakduk/img/logo_16.png" sizes="16x16">
<link rel="shortcut icon" href="<%=request.getContextPath()%>/resources/jakduk/img/logo_32.png" sizes="32x32">
<link rel="shortcut icon" href="<%=request.getContextPath()%>/resources/jakduk/img/logo_196.png" sizes="196x196">

<script>
	window.Jakduk = {
		userId: '${accountName}'
	};
</script>

<!--[if lt IE 9]>
<script src="<%=request.getContextPath()%>/resources/unify/assets/plugins/respond.js"></script>
<script src="<%=request.getContextPath()%>/resources/unify/assets/plugins/html5shiv.js"></script>
<script src="<%=request.getContextPath()%>/resources/unify/assets/plugins/placeholder-IE-fixes.js"></script>
<![endif]-->