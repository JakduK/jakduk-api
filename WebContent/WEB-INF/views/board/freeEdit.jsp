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
	<form:textarea path="content" class="hidden" ng-model="content"/>
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
			<form:errors path="categoryName" cssClass="text-danger" element="span" ng-hide="categoryAlert.msg"/>
			<span class="{{categoryAlert.classType}}" ng-show="categoryAlert.msg">{{categoryAlert.msg}}</span>
			</div>
		</div>
	</div>
	
	<div class="form-group has-feedback" ng-class="{'has-success':boardFree.subject.$valid, 'has-error':boardFree.subject.$invalid}">
		<label for="subject" class="control-label"><abbr title="required">*</abbr> <spring:message code="board.subject"/></label>
		<input type="text" name="subject" class="form-control" placeholder='<spring:message code="board.placeholder.subject"/>'
		ng-model="subject" ng-init="subject='${boardFree.subject}'" 
		ng-change="validationSubject()" ng-model-options="{ debounce: 400 }"
		ng-required="true" ng-minlength="3" ng-maxlength="60"/>
		<span class="glyphicon form-control-feedback" ng-class="{'glyphicon-ok':boardFree.subject.$valid, 
		'glyphicon-remove':boardFree.subject.$invalid}"></span>
		<form:errors path="subject" cssClass="text-danger" element="span" ng-hide="subjectAlert.msg"/>
		<span class="{{subjectAlert.classType}}" ng-show="subjectAlert.msg">{{subjectAlert.msg}}</span>		
	</div>
  
  <div class="form-group" ng-class="{'has-success':content.length >= 5, 'has-error':content.length < 5}">
		<div class="row">
			<div class="col-sm-12">
				<label for="content" class="control-label"><abbr title="required">*</abbr> <spring:message code="board.content"/></label>
				<summernote config="options" ng-model="content" ng-model-options="{ debounce: 400 }"></summernote>
				<form:errors path="content" cssClass="text-danger" element="span" ng-hide="contentAlert.msg"/>
				<span class="{{contentAlert.classType}}" ng-show="contentAlert.msg">{{contentAlert.msg}}</span>
			</div>
		</div>	
  </div>
  
	<div class="form-group">
		<input class="btn btn-default" name="commit" type="submit" value="<spring:message code="common.button.submit"/>">
		<a class="btn btn-danger" href="<c:url value="/board/free/${boardFree.seq}"/>"><spring:message code="common.button.cancel"/></a>
	</div>
	<div>
		<i class="fa fa-circle-o-notch fa-spin" ng-show="submitConn == 'connecting'"></i>
		<span class="{{buttonAlert.classType}}" ng-show="buttonAlert.msg">{{buttonAlert.msg}}</span>
	</div>	  
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
	$scope.submitConn = "none";
	$scope.categoryAlert = {};
	$scope.subjectAlert = {};
	$scope.contentAlert = {};
	$scope.buttonAlert = {};
	
	$scope.options = {
			height: 0,
			toolbar: [
//	      ['style', ['style']],
	      ['font', ['bold', 'italic', 'underline', /*'superscript', 'subscript', */'strikethrough', 'clear']],
	      ['fontname', ['fontname']],
//	      ['fontsize', ['fontsize']], // Still buggy
	      ['color', ['color']],
	      ['para', ['ul', 'ol', 'paragraph']],
	      ['height', ['height']],
	      ['table', ['table']],
	      ['insert', ['link',/* 'picture', 'video',*/ 'hr']],
	      ['view', ['fullscreen', 'codeview']],
	      ['help', ['help']]			          
				]
		};
	
	$scope.content = "${boardFree.content}";
	
	$scope.onSubmit = function(event) {
		console.log($scope.content.length);
		console.log(Jakduk.SummernoteContentsMinSize);
		if ($scope.boardFree.$valid && $scope.content.length >= Jakduk.SummernoteContentsMinSize) {
			$scope.submitConn = "connecting";
			$scope.buttonAlert = {"classType":"text-info", "msg":'<spring:message code="common.msg.be.cummunicating.server"/>'};			
		} else {
			$scope.validationCategory();
			$scope.validationSubject();
			$scope.validationContent();
			
			$scope.submitConn = "none";
			$scope.buttonAlert = {"classType":"text-danger", "msg":'<spring:message code="common.msg.need.form.validation"/>'};
			event.preventDefault();
		}
	};
	
	$scope.validationCategory = function() {
		if ($scope.boardFree.categoryName.$invalid) {
			$scope.categoryAlert = {"classType":"text-danger", "msg":'<spring:message code="common.msg.required"/>'};
		} else {
			$scope.categoryAlert = {"classType":"text-success", "msg":'<spring:message code="user.msg.avaliable.data"/>'};
		}
	};
	
	$scope.validationSubject = function() {
		if ($scope.boardFree.subject.$invalid) {
			if ($scope.boardFree.subject.$error.required) {
				$scope.subjectAlert = {"classType":"text-danger", "msg":'<spring:message code="common.msg.required"/>'};
			} else if ($scope.boardFree.subject.$error.minlength || $scope.boardFree.subject.$error.maxlength) {
				$scope.subjectAlert = {"classType":"text-danger", "msg":'<spring:message code="Size.boardFree.subject"/>'};
			}				
		} else {
			$scope.subjectAlert = {"classType":"text-success", "msg":'<spring:message code="user.msg.avaliable.data"/>'};
		}
	};		
	
	$scope.validationContent = function() {
		if ($scope.content.length < Jakduk.SummernoteContentsMinSize) {
			$scope.contentAlert = {"classType":"text-danger", "msg":'<spring:message code="Size.boardFree.content"/>'};
		} else {
			$scope.contentAlert = {"classType":"text-success", "msg":'<spring:message code="user.msg.avaliable.data"/>'};
		}
	};	
	
});
</script>

<jsp:include page="../include/body-footer.jsp"/>

</body>
</html>