<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="org.springframework.security.core.context.SecurityContextHolder" %>    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<jsp:include page="../include/html-header.jsp"></jsp:include>

<link href="<%=request.getContextPath()%>/web-resources/bootstrap/css/bootstrapApp.css" rel="stylesheet">
</head>
<body>

  	<div class="container">
		<c:if test="${not empty message}">
			<div class="span6 offset2 alert">${message}</div>
		</c:if>
  		<div class="span6 offset2 well">
	  		<form class="form-horizontal" action="j_spring_security_check" method="post">
			  <legend>Sign In</legend>
			  <div class="control-group">
			    <label class="control-label" for="j_username">Username</label>
			    <div class="controls">
			      <input type="text" id="j_username" name="j_username" placeholder="User Name">
			    </div>
			  </div>
			  <div class="control-group">
			    <label class="control-label" for="j_password">Password</label>
			    <div class="controls">
			      <input type="password" id="j_password" name="j_password" placeholder="Password">
			    </div>
			  </div>
			  <div class="control-group">
			    <div class="controls">
			      <button type="submit" class="btn btn-primary">Sign in</button>
			      <button type="submit" class="btn btn-danger">Reset</button>
			    </div>
			  </div>
			  <div class="control-group">
			    <div class="controls">
			      Not a User? <a href="<c:url value="/user/create"/>">Register Here</a>
			    </div>
			  </div>
			</form>
  		</div>
  	</div>

</body>
</html>