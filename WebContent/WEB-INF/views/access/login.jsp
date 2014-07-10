<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ page import="org.springframework.security.core.context.SecurityContextHolder" %>    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<jsp:include page="../include/html-header.jsp"></jsp:include>

<!-- Custom styles for this template -->
<link href="<%=request.getContextPath()%>/web-resources/bootstrap/css/signin.css" rel="stylesheet">
</head>
<body>


<c:if test="${not empty message}">
	<div class="span6 offset2 alert">${message}</div>
</c:if>

    <div class="container">
        <div class="row">
            <div class="col-md-4 col-md-offset-4">
                <div class="login-panel panel panel-default">
                    <div class="panel-heading">
                        <h3 class="panel-title"><spring:message code="user.sign.in"/></h3>
                    </div>
                    <div class="panel-body">
                        <form action="j_spring_security_check" method="post">
                            <fieldset>
                                <div class="form-group">
                                    <input class="form-control" id="j_username" name="j_username" placeholder="E-mail" autofocus>
                                </div>
                                <div class="form-group">
                                    <input class="form-control" id="j_password" name="j_password" placeholder="Password">
                                </div>
                                <div class="checkbox">
                                    <label>
                                        <input name="remember" type="checkbox" value="Remember Me">Remember Me
                                    </label>
                                </div>
                                <!-- Change this to a button or input when using this as a form -->
                                <button type="submit" class="btn btn-lg btn-success btn-block"><spring:message code="user.sign.in"/></button>
                            </fieldset>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>

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