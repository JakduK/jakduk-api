<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>    
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html ng-app="jakdukApp">
<head>
<jsp:include page="../include/html-header.jsp"></jsp:include>

<link href="<%=request.getContextPath()%>/resources/font-awesome/css/font-awesome.min.css" rel="stylesheet">

<!--summernote-->
<link href="<%=request.getContextPath()%>/resources/summernote/css/summernote.css" rel="stylesheet">

<script src="<%=request.getContextPath()%>/resources/jquery/js/jquery.min.js"></script>

</head>
<body>
<div class="container" ng-controller="FreeWriteCtrl">
<jsp:include page="../include/navigation-header.jsp"/>

<c:set var="contextPath" value="<%=request.getContextPath()%>"/>
<form:form commandName="boardFree" name="boardFree" action="${contextPath}/board/free/edit" method="POST"
	ng-submit="onSubmit($event)">
	<form:hidden path="id"/>
	<legend><spring:message code="board.edit"/></legend>
	<div class="form-group" ng-class="{'has-success':boardFree.categoryName.$valid, 'has-error':boardFree.categoryName.$invalid}">
		<div class="row">	
			<div class="col-sm-3">
			<label for="categoryName" class="control-label"><abbr title="required">*</abbr> <spring:message code="board.category"/></label>
			<form:select path="categoryName" cssClass="form-control" 
			ng-model="categoryName" ng-init="categoryName='${boardFree.categoryName}'" ng-blur="onCategoryName()" ng-required="true">
				<form:option value=""><fmt:message key="board.category.init"/></form:option>
				<c:forEach items="${boardCategorys}" var="category">
					<form:option value="${category.name}"><fmt:message key="${category.resName}"/></form:option>
				</c:forEach>
			</form:select>
			<form:errors path="categoryName" cssClass="text-danger" element="span" ng-hide="errorCategoryName"/>
			<span class="text-danger" ng-model="errorCategoryName" ng-show="errorCategoryName">{{errorCategoryName}}</span>
			</div>
		</div>
	</div>
	
	<div class="form-group has-feedback" ng-class="{'has-success':boardFree.subject.$valid, 'has-error':boardFree.subject.$invalid}">
		<label for="subject" class="control-label"><abbr title="required">*</abbr> <spring:message code="board.subject"/></label>
		<form:input path="subject" cssClass="form-control" placeholder="Subject"
		ng-model="subject" ng-init="subject='${boardFree.subject}'" ng-blur="onSubject()"
		ng-required="true" ng-minlength="3" ng-maxlength="60"/>
		<span class="glyphicon form-control-feedback" ng-class="{'glyphicon-ok':boardFree.subject.$valid, 
		'glyphicon-remove':boardFree.subject.$invalid}"></span>
		<form:errors path="subject" cssClass="text-danger" element="span" ng-hide="errorSubject"/>
		<span class="text-danger" ng-model="errorSubject" ng-show="errorSubject">{{errorSubject}}</span>				
	</div>
  
  <div class="form-group" ng-class="{'has-success':content.length >= 5, 'has-error':content.length < 5}">
		<div class="row">
			<div class="col-sm-12">
				<label for="content" class="control-label"><abbr title="required">*</abbr> <spring:message code="board.content"/></label>
				<summernote config="options" ng-model="content"></summernote>
				<form:errors path="content" cssClass="text-danger" element="span" ng-hide="errorContent"/>
				<span class="text-danger" ng-model="errorContent" ng-show="errorContent">{{errorContent}}</span>
			</div>
		</div>	
  </div>
  
  <div class="form-group">
		<input class="btn btn-default" name="commit" type="submit" value="<spring:message code="common.button.submit"/>">
		<a class="btn btn-danger" href="<c:url value="/board/free/${boardFree.seq}"/>"><spring:message code="common.button.cancel"/></a>
  </div>

<form:textarea path="content" ng-model="content" ng-init="content='${boardFree.content}'" class="hidden"/>  
</form:form>
    
<jsp:include page="../include/footer.jsp"/>
</div> <!-- /.container -->

<!-- Bootstrap core JavaScript
  ================================================== -->
<!-- Placed at the end of the document so the pages load faster -->
<script src="<%=request.getContextPath()%>/resources/bootstrap/js/bootstrap.min.js"></script> 
<script src="<%=request.getContextPath()%>/resources/summernote/js/summernote.min.js"></script>
<script src="<%=request.getContextPath()%>/resources/summernote/lang/summernote-ko-KR.js"></script>
<!--angular-summernote dependencies -->
<script src="<%=request.getContextPath()%>/resources/angular-summernote/js/angular-summernote.min.js"></script>
<script src="<%=request.getContextPath()%>/resources/jakduk/js/jakduk.js"></script>
<script type="text/javascript">

var jakdukApp = angular.module("jakdukApp", ["summernote"]);

jakdukApp.controller('FreeWriteCtrl', function($scope) {
		$scope.options = {
			height: 200,
			toolbar: [
	      ['style', ['style']],
	      ['font', ['bold', 'italic', 'underline', 'superscript', 'subscript', 'strikethrough', 'clear']],
	      ['fontname', ['fontname']],
	      // ['fontsize', ['fontsize']], // Still buggy
	      ['color', ['color']],
	      ['para', ['ul', 'ol', 'paragraph']],
	      ['height', ['height']],
	      ['table', ['table']],
	      ['insert', ['link',/* 'picture', 'video',*/ 'hr']],
	      ['view', ['fullscreen', 'codeview']],
	      ['help', ['help']]			          
				]
		};
		$scope.onSubmit = function(event) {
			if ($scope.boardFree.$valid && $scope.content.length >= Jakduk.SummernoteContentsSize) {
				
			} else {
				$scope.onCategoryName();
				$scope.onSubject();
				$scope.onContent();
				event.preventDefault();
			}
		};
		
		$scope.onCategoryName = function() {
			if ($scope.boardFree.categoryName.$invalid) {
				$scope.errorCategoryName = '<spring:message code="common.msg.required"/>';
			} else {
				$scope.errorCategoryName = "";
			}
		};
		
		$scope.onSubject = function() {
			if ($scope.boardFree.subject.$invalid) {
				if ($scope.boardFree.subject.$error.required) {
					$scope.errorSubject = '<spring:message code="common.msg.required"/>';
				} else if ($scope.boardFree.subject.$error.minlength || $scope.boardFree.subject.$error.maxlength) {
					$scope.errorSubject = '<spring:message code="Size.boardFree.subject"/>';
				}				
			} else {
				$scope.errorSubject = "";
			}
		};		
		
		$scope.onContent = function() {
			if ($scope.content.length < Jakduk.SummernoteContentsSize) {
				$scope.errorContent = '<spring:message code="Size.boardFree.content"/>';
			} else {
				$scope.errorContent = "";
			}
		};	
	});
</script>

<jsp:include page="../include/body-footer.jsp"/>

</body>
</html>