<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="org.springframework.security.core.context.SecurityContextHolder" %>    
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>    

<div class="navbar navbar-default navbar-fixed-top" role="navigation">
      <div class="container">
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
            <li ng-class="{active:isActive('<c:url value="/home"/>')}">
            	<a href="<c:url value="/home"/>"><spring:message code="common.home"/></a>
            </li>
            <li ng-class="{active:isActive('<c:url value="/board"/>')}">
            	<a href="<c:url value="/board"/>"><spring:message code="board"/></a>
            </li>
            <li ng-class="{active:isActive('<c:url value="/user"/>')}">
            	<a href="<c:url value="/user"/>">User</a>
            </li>
            <sec:authorize access="isAnonymous()">
            	<li><a href="<c:url value="/login"/>">Login</a></li>
            </sec:authorize>
            <sec:authorize access="isAuthenticated()">
            	<sec:authentication property="principal.Username" var="userName"/>
            	<li><a href="<c:url value="/logout"/>">${userName} Hello!</a></li>
            	<sec:authentication property="principal.userid" var="aaa"/>
            	 	<li><a href="<c:url value="/logout"/>">${aaa}</a></li>
            </sec:authorize>
           
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