<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>    
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
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
	<form:form commandName="board" action="/jakduk/board/free/write" method="POST">
						<legend>Write</legend>

			<div class="control-group select optional">
				<label class="select optional control-label" for="category"> Category</label>
				<div class="controls">
					<form:select path="content" cssClass="select optional">
						<form:option value="None" label="None"/>
						<form:option value="Free" label="Free"/>
						<form:option value="Football" label="Football"/>
					</form:select>
				</div>
			</div>
						
						<div class="control-group string required">
							<label class="string required control-label" for="subject">
								<abbr title="required">*</abbr> Subject
							</label>
							<div class="controls">
								<form:input path="subject" cssClass="string required span12"/>
							</div>
						</div>
						<div class="control-group text required">
							<label class="text required control-label" for="content"> 
								<abbr title="required">*</abbr> Content
							</label>
							<div class="controls">
								<form:textarea path="content" rows="15" cssClass="text required span12"/>
							</div>
						</div>
						<div class="form-actions">
							<input class="btn btn-primary" name="commit" type="submit" value="Submit">
							<a class="btn btn-danger" href="<c:url value="/board"/>">Cancel</a> 
						</div>
	</form:form>
				</div><!--/span-->
			</div><!--/row-->    
    
<jsp:include page="../include/footer.jsp"/>

    </div> <!-- /container -->
</body>
</html>