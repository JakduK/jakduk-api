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

<link href="<%=request.getContextPath()%>/web-resources/bootstrap/css/bootstrapApp.css" rel="stylesheet">

</head>
<body>
    <div class="container-fluid">
    
			<div class="row-fluid">
				<div class="span10">
	<form:form commandName="boardFree" action="/jakduk/board/free/write" method="POST">
						<legend>Write</legend>

			<div class="control-group select optional">
				<label class="select optional control-label" for="categoryId"> <spring:message code="board.category"/></label>
				<div class="controls">
					<form:select path="categoryId" cssClass="select optional">
  <c:forEach items="${categorys}" var="category">
  	<form:option value="${category.categoryId}"><fmt:message key="${category.name}"/></form:option>
	</c:forEach>
					</form:select>
				</div>
			</div>
						<div class="control-group string required">
							<label class="string required control-label" for="subject">
								<abbr title="required">*</abbr> <spring:message code="board.subject"/>
							</label>
							<div class="controls">
								<form:input path="subject" cssClass="string required span12"/>
								<form:errors path="subject" cssClass="smdis-error-message"/>								
							</div>
						</div>
						<div class="control-group text required">
							<label class="text required control-label" for="content"> 
								<abbr title="required">*</abbr> <spring:message code="board.content"/>
							</label>
							<div class="controls">
								<form:textarea path="content" rows="15" cssClass="text required span12"/>
								<form:errors path="content" cssClass="smdis-error-message"/>
							</div>
						</div>
						<div class="form-actions">
							<input class="btn btn-primary" name="commit" type="submit" value="<spring:message code="common.button.submit"/>">
							<a class="btn btn-danger" href="<c:url value="/board"/>"><spring:message code="common.button.cancel"/></a> 
						</div>
	</form:form>
				</div><!--/span-->
			</div><!--/row-->    
    
<jsp:include page="../include/footer.jsp"/>

    </div> <!-- /container -->
</body>
</html>