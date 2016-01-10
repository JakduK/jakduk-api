<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>    

<sec:authorize access="isAuthenticated()">
	<sec:authentication property="principal.username" var="accountName"/>
</sec:authorize>

<script src="<%=request.getContextPath()%>/resources/bootstrap/dist/js/bootstrap.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/resources/unify/assets/plugins/back-to-top.js"></script>
<script src="<%=request.getContextPath()%>/resources/unify/assets/plugins/smoothScroll.js"></script>

<!--[if lt IE 9]>
<script src="<%=request.getContextPath()%>/resources/unify/assets/plugins/respond.js"></script>
<script src="<%=request.getContextPath()%>/resources/unify/assets/plugins/html5shiv.js"></script>
<script src="<%=request.getContextPath()%>/resources/unify/assets/plugins/placeholder-IE-fixes.js"></script>
<![endif]-->

<!-- JS Page Level -->
<script type="text/javascript" src="<%=request.getContextPath()%>/resources/unify/assets/js/app.js"></script>

<!-- This script should be under the AngularJS which is creating jakdukApp module. -->
<script src="<%=request.getContextPath()%>/resources/jakduk/js/navigation-header.js"></script>
<script src="<%=request.getContextPath()%>/resources/jakduk/js/jakduk.js"></script>

<script>

</script>