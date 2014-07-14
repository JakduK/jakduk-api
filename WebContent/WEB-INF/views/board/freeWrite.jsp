<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>    
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<jsp:include page="../include/html-header.jsp"></jsp:include>

</head>
<body>
<div class="container">
<jsp:include page="../include/navigation-header.jsp"/>
<c:set var="contextPath" value="<%=request.getContextPath()%>"/>
<form:form commandName="boardFree" action="${contextPath}/board/free/write" method="POST">
	<legend><spring:message code="board.write"/></legend>
	<div class="form-group">
		<div class="row">	
			<div class="col-sm-3">
			<label for="categoryId"><abbr title="required">*</abbr> <spring:message code="board.category"/></label>
			<form:select path="categoryId" cssClass="form-control">
				<form:option value=""><fmt:message key="board.category.init"/></form:option>
				<c:forEach items="${boardCategorys}" var="category">
					<form:option value="${category.categoryId}"><fmt:message key="${category.name}"/></form:option>
				</c:forEach>
			</form:select>
			<form:errors path="categoryId" cssClass="text-danger" element="span"/>
			</div>
		</div>
	</div>

  <div class="form-group">
		<div class="row">
			<div class="col-sm-12">
				<label for="subject"><abbr title="required">*</abbr> <spring:message code="board.subject"/></label>
				<form:input path="subject" cssClass="form-control" placeholder="Subject"/>
				<form:errors path="subject" cssClass="text-danger" element="span"/>
			</div>
		</div>	
  </div>
  
  <div class="form-group">
		<div class="row">
			<div class="col-sm-12">
				<label for="content"><abbr title="required">*</abbr> <spring:message code="board.content"/></label>
				<form:textarea path="content" rows="15" cssClass="form-control" placeholder="Content"/>
				<form:errors path="content" cssClass="text-danger" element="span"/>
			</div>
		</div>	
  </div>
  
  <div class="form-group">
		<input class="btn btn-default" name="commit" type="submit" value="<spring:message code="common.button.submit"/>">
		<a class="btn btn-danger" href="<c:url value="/board"/>"><spring:message code="common.button.cancel"/></a>
  </div>    
  
</form:form>
    
<jsp:include page="../include/footer.jsp"/>
</div> <!-- /.container -->

<!-- Bootstrap core JavaScript
  ================================================== -->
<!-- Placed at the end of the document so the pages load faster -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/web-resources/bootstrap/js/bootstrap.min.js"></script> 
</body>
</html>