<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<jsp:include page="../include/html-header.jsp"></jsp:include>

<!-- Custom styles for this template -->
<!--  
<link href="<%=request.getContextPath()%>/web-resources/bootstrap/css/signin.css" rel="stylesheet">
-->

<link href="<%=request.getContextPath()%>/web-resources/bootstrap/css/bootstrapApp.css" rel="stylesheet">

</head>
<body>
    <div class="container-fluid">
    
			<div class="row-fluid">
				<div class="span10">
					<form accept-charset="UTF-8" action="" class="form-horizontal" id="addQuestion" method="post">
						<legend>Ask a Question</legend>
						<div class="control-group string required">
							<label class="string required control-label" for="subject">
								<abbr title="required">*</abbr> Subject
							</label>
							<div class="controls">
								<input class="string required span12" id="subject" name="subject" type="text">
							</div>
						</div>
						<div class="control-group text required">
							<label class="text required control-label" for="content"> 
								<abbr title="required">*</abbr> Content
							</label>
							<div class="controls">
								<textarea class="text required span12" id="content" name="content" rows="15" ></textarea>
							</div>
						</div>
						<div class="control-group string required">
							<label class="string required control-label" for="tags">
								<abbr title="required">*</abbr> Tags
							</label>
							<div class="controls">
								<div id="taglist" class="taglist">
								    <span>
								    	<input type="text" id="tags" size="10" name="tags" value="" placeholder="Input Tag" />
								    </span>
								</div>
								<a href="#" id="addTag">Add Tag</a>
							</div>
						</div>
						<div class="form-actions">
							<input class="btn btn-primary" name="commit" type="submit" value="Post Question">
							<a class="btn btn-danger" href="<c:url value="/"/>">Cancel</a> 
						</div>
					</form>
				</div><!--/span-->
			</div><!--/row-->    
    
<jsp:include page="../include/footer.jsp"/>

    </div> <!-- /container -->
</body>
</html>