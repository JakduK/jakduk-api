<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>    
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>    

<!DOCTYPE html>
<html ng-app="jakdukApp">
<head>
<jsp:include page="../include/html-header.jsp"/>
</head>
<body>
<div class="container" ng-controller="adminCtrl">
<h4>Write HomeDescription.</h4>
<c:set var="contextPath" value="<%=request.getContextPath()%>"/>
<form:form commandName="homeDescription" action="${contextPath}/admin/home/description/write" method="POST">
<form:hidden path="id"/>
<p>
<label for="desc" class="control-label">DESCRIPTION :</label>
<form:textarea path="desc" cssClass="form-control" placeholder="Description"/>
</p>
<form:errors path="desc"/>
<p>
<input type="submit" value="<spring:message code="common.button.write"/>" class="btn btn-default"/>
</p>
</form:form>
</div>
<jsp:include page="../include/footer.jsp"/>
<!-- Bootstrap core JavaScript================================================== -->
<!-- Placed at the end of the document so the pages load faster -->
<script src="<%=request.getContextPath()%>/resources/jquery/dist/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/resources/bootstrap/dist/js/bootstrap.min.js"></script>    
<script src="<%=request.getContextPath()%>/resources/angular/angular.min.js"></script>
<script type="text/javascript">

var jakdukApp = angular.module("jakdukApp", []);

jakdukApp.controller("adminCtrl", function($scope, $filter) {
});

</script>
</body>
</html>
