<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>


<!--=== Header ===-->    
<div class="header" ng-controller="headerCtrl">
	<!-- Topbar -->
	<div class="topbar">
		<div class="container">
			<!-- Topbar Navigation -->
			<ul class="loginbar pull-right">
				<li>
					<i class="fa fa-globe"></i>
					<a><spring:message code="common.language"/></a>
					<ul class="languages">
						<li><a href="?lang=ko"><spring:message code="common.language.korean"/></a></li>
						<li><a href="?lang=en"><spring:message code="common.language.english"/></a></li>
					</ul>
				</li>
				<li class="topbar-devider"></li>
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
					<li class="topbar-devider"></li>
					<li><a href="<c:url value="/logout"/>"><spring:message code="common.logout"/></a></li>
		 		</sec:authorize>	  		
			</ul>
			<!-- End Topbar Navigation -->
		</div>
	</div>
	<!-- End Topbar -->

	<!-- Navbar -->
	<div class="navbar navbar-default" role="navigation">
		<div class="container">
			<!-- Brand and toggle get grouped for better mobile display -->
			<div class="navbar-header">
				<button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-responsive-collapse">
				    <span class="sr-only">Toggle navigation</span>
				    <span class="fa fa-bars"></span>
				</button>
				<a class="navbar-brand" href="<c:url value="/"/>">
					<spring:message code="common.jakduk"/>
				</a>
			</div>
			
			<!-- Collect the nav links, forms, and other content for toggling -->
			<div class="collapse navbar-collapse navbar-responsive-collapse">
				<ul class="nav navbar-nav">                
					<li ng-class="{active:isActive('<c:url value="/home"/>')}">
						<a href="<c:url value="/home"/>"><spring:message code="common.home"/></a>
					</li>
					<li ng-class="{active:isActive('<c:url value="/about"/>')}">
						<a href="<c:url value="/about"/>"><spring:message code="about"/></a>
					</li>
					<li ng-class="{active:isActive('<c:url value="/board"/>')}">
						<a href="<c:url value="/board"/>"><spring:message code="board"/></a>
					</li>
					<li ng-class="{active:isActive('<c:url value="/gallery"/>')}">
						<a href="<c:url value="/gallery"/>"><spring:message code="gallery"/></a>
					</li>
					<li ng-class="{active:isActive('<c:url value="/stats"/>')}">
						<a href="<c:url value="/stats"/>"><spring:message code="stats"/></a>
					</li>
					
					<!-- Search Block -->
					<li>
					    <i class="search fa fa-search search-btn"></i>
					    <div class="search-open">
					        <div class="input-group animated fadeInDown">
					            <input type="text" class="form-control" placeholder="Search">
					            <span class="input-group-btn">
					                <button class="btn-u" type="button">Go</button>
					            </span>
					        </div>
					    </div>    
					</li>
					<!-- End Search Block -->
				</ul>
			</div><!--/navbar-collapse-->
		</div>    
	</div>            
	<!-- End Navbar -->
</div>
<!--=== End Header ===-->

<script src="<%=request.getContextPath()%>/resources/angular/angular.min.js"></script>
<script type="text/javascript">

</script>