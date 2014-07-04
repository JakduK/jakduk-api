<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
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
			  <legend><spring:message code="user.sign.in"/></legend>
			  <div class="control-group">
			    <label class="control-label" for="j_username"><spring:message code="user.email"/></label>
			    <div class="controls">
			      <input type="text" id="j_username" name="j_username" placeholder="User Name">
			    </div>
			  </div>
			  <div class="control-group">
			    <label class="control-label" for="j_password"><spring:message code="user.password"/></label>
			    <div class="controls">
			      <input type="password" id="j_password" name="j_password" placeholder="Password">
			    </div>
			  </div>
			  <div class="control-group">
			    <div class="controls">
			      <button type="submit" class="btn btn-primary"><spring:message code="user.sign.in"/></button>
			      <button type="submit" class="btn btn-danger">Reset</button>
			    </div>
			  </div>
			  <div class="control-group">
			    <div class="controls">
			      <spring:message code="user.msg.not.user"/> 
			      <a href="<c:url value="/user/write"/>"><spring:message code="user.msg.register.here"/></a>
			    </div>
			  </div>
			</form>
  		</div>
  	</div>

</body>
</html>