<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="org.springframework.security.core.context.SecurityContextHolder" %>    
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>    
    
        <div class="navbar navbar-fixed-top navbar-inverse" role="navigation">
      <div class="container">
        <div class="navbar-header">
          <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <a class="navbar-brand" href="#"><spring:message code="common.jakduk"/></a>
        </div>
        <div class="collapse navbar-collapse">
          <ul class="nav navbar-nav">
            <li class="active"><a href="<c:url value="/home"/>"><spring:message code="common.home"/></a></li>
            <li><a href="<c:url value="/board"/>"><spring:message code="board"/></a></li>
            <li><a href="<c:url value="/user"/>">User</a></li>
            <li><a href="#contact">Contact</a></li>
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
