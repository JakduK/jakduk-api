<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="org.springframework.security.core.context.SecurityContextHolder" %>    
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<div class="navbar navbar-default" role="navigation">
    <div class="container-fluid">
      <!-- Brand and toggle get grouped for better mobile display -->
      <div class="navbar-header">
        <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
          <span class="sr-only">Toggle navigation</span>
          <span class="icon-bar"></span>
          <span class="icon-bar"></span>
          <span class="icon-bar"></span>
        </button>
        <a class="navbar-brand" href="<c:url value="/home"/>"><spring:message code="common.jakduk"/></a>
      </div>
      <div class="collapse navbar-collapse" ng-controller="headerCtrl">
        <ul class="nav navbar-nav">
          <li ng-class="{active:isActive('<c:url value="/about"/>')}">
          	<a href="<c:url value="/about"/>"><spring:message code="about"/></a>
          </li>
          <li ng-class="{active:isActive('<c:url value="/board"/>')}">
          	<a href="<c:url value="/board"/>"><spring:message code="board"/></a>
          </li>
          
          <li class="dropdown" ng-class="{active:isActive('<c:url value="/admin|/user"/>')}">
          	<a href="#" class="dropdown-toggle" data-toggle="dropdown"><spring:message code="common.admin"/><b class="caret"></b></a>
          	<ul class="dropdown-menu">
          		<li><a href="<c:url value="/admin/settings"/>"><spring:message code="common.setting"/></a></li>
          		<li><a href="<c:url value="/user"/>"><spring:message code="common.userlist"/></a></li>
          	</ul>            	
          </li>
        </ul>
        
	<ul class="nav navbar-nav navbar-right">
	  <sec:authorize access="isAnonymous()">
	  	<li><a href="<c:url value="/login"/>"><span class="glyphicon glyphicon-log-in"></span> <spring:message code="common.login"/></a></li>
	  	<li><a href="<c:url value="/user/write"/>"><spring:message code="user.register"/></a></li>
	  </sec:authorize>
		<sec:authorize access="isAuthenticated()">
			<sec:authentication property="principal.username" var="userName"/>
			<li class="dropdown">
				<a href="#" class="dropdown-toggle" data-toggle="dropdown">
					<span class="glyphicon glyphicon-user"></span> ${userName} <span class="caret"></span>
				</a>
				<ul class="dropdown-menu" role="menu">
					<li><a href="<c:url value="/logout"/>"><span class="glyphicon glyphicon-log-out"></span> <spring:message code="common.logout"/></a></li>
				</ul>
			</li>           
    </sec:authorize>
			<li class="dropdown">
				<a href="#" class="dropdown-toggle" data-toggle="dropdown">
					<span class="glyphicon glyphicon-globe"></span> <spring:message code="common.language"/> <span class="caret"></span>
				</a>
				<ul class="dropdown-menu" role="menu">
					<li><a href="?lang=ko"><spring:message code="common.language.korean"/></a></li>
					<li><a href="?lang=en"><spring:message code="common.language.english"/></a></li>
				</ul>
			</li>          							
	</ul>
      </div><!-- /.nav-collapse -->
    </div><!-- /.container -->
</div><!-- /.navbar -->

<script src="<%=request.getContextPath()%>/web-resources/angular/js/angular.js"></script>
<script type="text/javascript">
function headerCtrl($scope, $location) {
	$scope.isActive = function(viewLocation) {
		return $location.absUrl().match(viewLocation);
	}
}
</script>