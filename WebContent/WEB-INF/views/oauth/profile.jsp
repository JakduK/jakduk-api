<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>  
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html ng-app="jakdukApp">
<head>
	<jsp:include page="../include/html-header.jsp"></jsp:include>
</head>
<body>
<div class="container">
<jsp:include page="../include/navigation-header.jsp"/>

<legend><spring:message code="user.profile"/></legend>

<form class="form-horizontal" role="form">
  <div class="form-group">
    <label class="col-sm-2 control-label"><spring:message code="user.nickname"/></label>
    <div class="col-sm-3">
      <p class="form-control-static">${oauthProfile.username}</p>
    </div>
  </div>
  <div class="form-group">
    <label class="col-sm-2 control-label"><spring:message code="user.support.football.club"/></label>
    <div class="col-sm-3">
			<c:choose>
				<c:when test="${not empty oauthProfile.footballClubName}">
					<p class="form-control-static visible-sm visible-md visible-lg">${oauthProfile.footballClubName.fullName}</p>
					<p class="form-control-static visible-xs">${oauthProfile.footballClubName.shortName}</p>
				</c:when>
				<c:otherwise>
					<p class="form-control-static"><spring:message code="common.none"/></p>
				</c:otherwise>
			</c:choose>
    </div>
  </div>
	<div class="form-group">
    <label class="col-sm-2 control-label"><spring:message code="user.comment"/></label>
    <div class="col-sm-3">
      <p class="form-control-static">${oauthProfile.about}</p>
    </div>
  </div>    
	<hr>  
		<div class="form-group">
			<div class="col-sm-offset-2 col-sm-4">
				<a class="btn btn-info" href="<c:url value="/oauth/profile/update"/>"><spring:message code="common.button.user.profile.update"/></a>				
			</div> 
		</div>  
</form>

<jsp:include page="../include/footer.jsp"/>
</div><!-- /.container -->

<!-- Bootstrap core JavaScript ================================================== -->
<!-- Placed at the end of the document so the pages load faster -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/resources/bootstrap/js/bootstrap.min.js"></script>
<script type="text/javascript">
var jakdukApp = angular.module("jakdukApp", []);
</script>

<jsp:include page="../include/body-footer.jsp"/>

</body>
</html>