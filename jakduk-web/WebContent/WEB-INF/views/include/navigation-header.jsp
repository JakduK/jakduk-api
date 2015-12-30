<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<!--=== Header ===-->    
<div class="header-v5" ng-controller="headerCtrl">
	<!-- Topbar v3 -->
	<div class="topbar-v3">
	    <div class="search-open">
	        <div class="container">
	            <input type="text" class="form-control" ng-model="searchOnHeader" ng-init="searchOnHeader=''"
	            ng-keypress="($event.which === 13)?btnEnterOnHeaderSearch('<c:url value="/search"/>'):return" 
	            placeholder='<spring:message code="search.placeholder.words"/>'
	            focus="searchFocusOnHeader">
	            <div class="search-close"><i class="icon-close"></i></div>
	        </div>    
	    </div>
	
		<div class="container">
			<div class="row">
				<div class="col-sm-6">
					<!-- Topbar Navigation -->
					<ul class="left-topbar">
						<li>
							<a><i class="fa fa-globe"></i> <spring:message code="common.language"/></a>
							<ul class="language">
								<li><a href="?lang=ko_kr"><spring:message code="common.language.korean"/></a></li>
								<li><a href="?lang=en_US"><spring:message code="common.language.english"/></a></li>
							</ul>
						</li>                        
					</ul> <!--/end left-topbar-->
				</div>
				<div class="col-sm-6">
					<ul class="list-inline right-topbar pull-right">
						<sec:authorize access="isAnonymous()">
							<li><a href="<c:url value="/login"/>"><spring:message code="common.login"/></a></li>
		  				</sec:authorize>
						<sec:authorize access="isAuthenticated()">
							<sec:authentication property="principal.username" var="accountName"/>
							<sec:authentication property="principal.type" var="userType"/>
							<c:choose>
								<c:when test="${userType == 'jakduk'}">
									<li>
										<i class="fa fa-user"></i>
										<a href="<c:url value="/user/profile"/>">${accountName}</a>
									</li>
								</c:when>
								<c:when test="${userType == 'facebook' || userType == 'daum'}">
									<li>
										<i class="fa fa-user"></i>
										<a href="<c:url value="/oauth/profile"/>">${accountName}</a>
									</li>
								</c:when>
							</c:choose>
							<li><a href="<c:url value="/logout"/>"><spring:message code="common.logout"/></a></li>
				 		</sec:authorize>				  			              
						<li ng-click="btnSearchOnHeader()"><i class="search fa fa-search search-button"></i></li>
					</ul>
				</div>
			</div>
		</div> <!--/container-->
	</div>
	<!-- End Topbar v3 -->

	<!-- Navbar -->
	<div class="navbar navbar-default mega-menu" role="navigation">
		<div class="container">
			<!-- Brand and toggle get grouped for better mobile display -->
			<div class="navbar-header">
				<button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-responsive-collapse">
					<span class="sr-only">Toggle navigation</span>
					<span class="icon-bar"></span>
					<span class="icon-bar"></span>
					<span class="icon-bar"></span>
				</button>
				<a class="navbar-brand" href="<c:url value="/"/>">
					<c:choose>
						<c:when test="${fn:contains(pageContext.response.locale.language, 'ko')}">
							<img id="logo-header" src="<%=request.getContextPath()%>/resources/jakduk/img/logo_type_A_kr.png" alt="Logo">
						</c:when>
						<c:otherwise>
							<img id="logo-header" src="<%=request.getContextPath()%>/resources/jakduk/img/logo_type_A_en.png" alt="Logo">
						</c:otherwise>
					</c:choose>
				</a>
			</div>

			<div class="collapse navbar-collapse navbar-responsive-collapse">
				<ul class="nav navbar-nav">                
					<li ng-class="{active:isActive('<c:url value="/home"/>')}">
						<a href="<c:url value="/home"/>"><spring:message code="common.home"/></a>
					</li>
					<li ng-class="{active:isActive('<c:url value="/board"/>')}">
						<a href="<c:url value="/board"/>"><spring:message code="board"/></a>
					</li>
					<li ng-class="{active:isActive('<c:url value="/gallery"/>')}">
						<a href="<c:url value="/gallery"/>"><spring:message code="gallery"/></a>
					</li>
					<li class="dropdown" ng-class="{active:isActive('<c:url value="/stats"/>')}">
						<a href="<c:url value="/stats"/>" class="dropdown-toggle" data-toggle="dropdown">
							<spring:message code="stats"/>						
						</a>					
						<ul class="dropdown-menu">
							<li ng-class="{active:isActive('<c:url value="/stats/supporters"/>')}"><a href="<c:url value="/stats/supporters"/>"><spring:message code="stats.supporters"/></a></li>
							<li ng-class="{active:isActive('<c:url value="/stats/attendance"/>')}"><a href="<c:url value="/stats/attendance"/>"><spring:message code="stats.attendance"/></a></li>
						</ul>					
					</li>
					<li ng-class="{active:isActive('<c:url value="/jakdu"/>')}">
						<a href="<c:url value="/jakdu"/>"><spring:message code="jakdu"/></a>
					</li>
				</ul>
			</div>		
		</div> <!-- Navbar container -->
	</div> <!-- End Navbar -->            
</div>
<!--=== End Header v5 ===-->

<script src="<%=request.getContextPath()%>/resources/angular/angular.min.js"></script>