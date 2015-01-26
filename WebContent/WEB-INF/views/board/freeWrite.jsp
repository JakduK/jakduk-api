<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>    
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<!DOCTYPE html>
<html ng-app="jakdukApp">
<head>
<jsp:include page="../include/html-header.jsp"></jsp:include>

<script src="<%=request.getContextPath()%>/resources/jquery/js/jquery.min.js"></script>

<link href="<%=request.getContextPath()%>/resources/font-awesome/css/font-awesome.min.css" rel="stylesheet">
<link href="<%=request.getContextPath()%>/resources/summernote/css/summernote.css" rel="stylesheet">

</head>
<body>
<div class="container" ng-controller="FreeWriteCtrl">
<jsp:include page="../include/navigation-header.jsp"/>

<c:set var="contextPath" value="<%=request.getContextPath()%>"/>
<c:set var="summernoteLang" value="en-US"/>

<form:form commandName="boardFreeWrite" name="boardFreeWrite" action="${contextPath}/board/free/write" method="POST"
	ng-submit="onSubmit($event)">
	<form:textarea path="content" class="hidden" ng-bind="content" value="${boardFreeWrite.content}"/>
	<form:textarea path="images" class="hidden" ng-model="images" ng-init="images='${boardFreeWrite.images}'"/>
	
	<legend><spring:message code="board.write"/></legend>
	<div class="form-group" ng-class="{'has-success':boardFreeWrite.categoryName.$valid, 'has-error':boardFreeWrite.categoryName.$invalid}">
		<div class="row">	
			<div class="col-sm-3">
			<label for="categoryName" class="control-label"><abbr title="required">*</abbr> <spring:message code="board.category"/></label>
			<form:select path="categoryName" cssClass="form-control" 
			ng-model="categoryName" ng-init="categoryName='${boardFreeWrite.categoryName}'" ng-change="validationCategory()" ng-required="true">
				<form:option value=""><spring:message code="board.category.init"/></form:option>
				<c:forEach items="${boardCategorys}" var="category">
					<form:option value="${category.name}"><spring:message code="${category.resName}"/></form:option>
				</c:forEach>
			</form:select>
			<form:errors path="categoryName" cssClass="text-danger" element="span" ng-hide="categoryAlert.msg"/>
			<span class="{{categoryAlert.classType}}" ng-show="categoryAlert.msg">{{categoryAlert.msg}}</span>
			</div>
		</div>
	</div>
	
	<div class="form-group has-feedback" ng-class="{'has-success':boardFreeWrite.subject.$valid, 'has-error':boardFreeWrite.subject.$invalid}">
		<label for="subject" class="control-label"><abbr title="required">*</abbr> <spring:message code="board.subject"/></label>
		<input type="text" name="subject" class="form-control" placeholder='<spring:message code="board.placeholder.subject"/>'
		ng-model="subject" ng-init="subject='${boardFreeWrite.subject}'"
		ng-change="validationSubject()" ng-model-options="{ debounce: 400 }"
		ng-required="true" ng-minlength="3" ng-maxlength="60"/>
		<span class="glyphicon form-control-feedback" ng-class="{'glyphicon-ok':boardFreeWrite.subject.$valid, 
		'glyphicon-remove':boardFreeWrite.subject.$invalid}"></span>
		<form:errors path="subject" cssClass="text-danger" element="span" ng-hide="subjectAlert.msg"/>
		<span class="{{subjectAlert.classType}}" ng-show="subjectAlert.msg">{{subjectAlert.msg}}</span>		
	</div>
  
  <div class="form-group" ng-class="{'has-success':content.length >= 5, 'has-error':content.length < 5}">
		<div class="row">
			<div class="col-sm-12">
				<label for="content" class="control-label"><abbr title="required">*</abbr> <spring:message code="board.content"/></label>
				<summernote config="options" ng-model="content" on-image-upload="imageUpload(files, editor)" editable="editable"></summernote>
				<form:errors path="content" cssClass="text-danger" element="span" ng-hide="contentAlert.msg"/>
				<span class="{{contentAlert.classType}}" ng-show="contentAlert.msg">{{contentAlert.msg}}</span>
			</div>
		</div>	
  </div>

<h4 ng-show="storedImages.length > 0 || uploader.queue.length > 0"><spring:message code="board.gallery.list"/></h4>
<div class="row">
  <div class="col-xs-4 col-sm-2 col-md-2" ng-repeat="image in storedImages">
    <div class="thumbnail">
      <div class="caption text-overflow">
					<button type="button" class="btn btn-success btn-xs" onclick="location.href='<c:url value="/board"/>'">
						<span class="glyphicon glyphicon-upload"></span>
					</button>		
					<button type="button" class="btn btn-danger btn-xs" ng-click="removeStoredItem(image)">
						<span class="glyphicon glyphicon-remove-circle"></span>
					</button>
						<small>
						{{image.name}} | {{image.size/1024|number:1}} KB 
					</small>
	    </div>
	    <img ng-src="<%=request.getContextPath()%>/gallery/thumbnail/{{image.uid}}" width="60px;" height="60px;">
  	</div>
	</div>
  <div class="col-xs-4 col-sm-2 col-md-2" ng-repeat="item in uploader.queue">
    <div class="thumbnail">
				<div class="caption">
					<button type="button" class="btn btn-success btn-xs" onclick="location.href='<c:url value="/board"/>'">
						<span class="glyphicon glyphicon-upload"></span>
					</button>		
					<button type="button" class="btn btn-danger btn-xs" ng-click="removeQueueItem(item)">
						<span class="glyphicon glyphicon-remove-circle"></span>
					</button>
					<h5 class="text-overflow">
						<small>
						{{item.file.name}} | {{item.file.size/1024|number:1}} KB 
						<code>{{item.progress}}%</code>
					</small>
					</h5>
				</div>
				<img ng-src="<%=request.getContextPath()%>/gallery/thumbnail/{{item.uid}}" width="60px;" height="60px;">      
  	</div>
	</div>  
</div>  

	<div class="form-group">
		<button type="submit" class="btn btn-success">
			<span class="glyphicon glyphicon-upload"></span> <spring:message code="common.button.submit"/>
		</button>		
		<button type="button" class="btn btn-warning" onclick="location.href='<c:url value="/board"/>'">
			<span class="glyphicon glyphicon-ban-circle"></span> <spring:message code="common.button.cancel"/>
		</button>
		<div>
		<i class="fa fa-circle-o-notch fa-spin" ng-show="submitConn == 'connecting'"></i>
		<span class="{{buttonAlert.classType}}" ng-show="buttonAlert.msg">{{buttonAlert.msg}}</span>
		</div>	
	</div>	  
</form:form>

<jsp:include page="../include/footer.jsp"/>
</div> <!-- /.container -->
<!-- Bootstrap core JavaScript
  ================================================== -->
<!-- Placed at the end of the document so the pages load faster -->
<script src="<%=request.getContextPath()%>/resources/bootstrap/js/bootstrap.min.js"></script> 
<script src="<%=request.getContextPath()%>/resources/summernote/js/summernote.min.js"></script>
<script src="<%=request.getContextPath()%>/resources/angular-summernote/js/angular-summernote.min.js"></script>
<script src="<%=request.getContextPath()%>/resources/angular-file-upload/js/angular-file-upload.js"></script>
<script src="<%=request.getContextPath()%>/resources/jakduk/js/jakduk.js"></script>
<c:if test="${fn:contains('ko', pageContext.response.locale.language)}">
	<script src="<%=request.getContextPath()%>/resources/summernote/lang/summernote-ko-KR.js"></script>
	<c:set var="summernoteLang" value="ko-KR"/>
</c:if>

<script type="text/javascript">

window.onbeforeunload = function(e) {
	if (!submitted) {
		(e || window.event).returnValue = '<spring:message code="common.msg.are.you.sure.leave.page"/>';
		return '<spring:message code="common.msg.are.you.sure.leave.page"/>';
	}
};

var submitted = false;
var jakdukApp = angular.module("jakdukApp", ["summernote", "angularFileUpload"]);

function isEmpty(str) {
	obj = String(str);

	if(obj == null || obj == undefined || obj == 'null' || obj == 'undefined' || obj == '' ) return true;

	else return false;
}

jakdukApp.controller('FreeWriteCtrl', function($scope, $http, FileUploader) {
	$scope.submitConn = "none";
	$scope.categoryAlert = {};
	$scope.subjectAlert = {};
	$scope.contentAlert = {};
	$scope.buttonAlert = {};
	$scope.storedImages = [];
	
	angular.element(document).ready(function() {
		if (!isEmpty($scope.images)) {
			var objImages = JSON.parse($scope.images);
			objImages.forEach(function(entry) {
				$scope.storedImages.push(entry);
				$scope.$apply();
			}) ;
		}
	});
	
	var contentValue = document.getElementById("content").value
	$scope.content = contentValue ? contentValue : '♪';
	
	$scope.options = {
		height: 0,
		lang : "${summernoteLang}",
		toolbar: [
//      ['style', ['style']],
      ['font', ['bold', 'italic', 'underline', /*'superscript', 'subscript', */'strikethrough', 'clear']],
      ['fontname', ['fontname']],
//      ['fontsize', ['fontsize']], // Still buggy
      ['color', ['color']],
      ['para', ['ul', 'ol', 'paragraph']],
//      ['height', ['height']],
      ['table', ['table']],
      ['insert', ['link', 'picture', 'video', 'hr']],
      ['view', ['fullscreen', 'codeview']],
      ['help', ['help']]			          
			]
	};
	
	$scope.uploader = new FileUploader({		
		url:'<c:url value="/gallery/upload.json"/>',
		autoUpload:true,
		method:"POST"
	});
	
	$scope.removeStoredItem = function(fileItem) {
		if (fileItem.uid != null) {
			var bUrl = '<c:url value="/gallery/remove/' + fileItem.uid + '"/>';
			
			var reqPromise = $http.get(bUrl);
			
			reqPromise.success(function(data, status, headers, config) {

				if (!isEmpty($scope.storedImages)) {
					var rmIdx = -1;
					
					$scope.storedImages.forEach(function(entry, index) {
						if (entry.uid == fileItem.uid) {
							rmIdx = index;							
						}
					});
					
					if (rmIdx != -1) {
						$scope.storedImages.splice(rmIdx, 1);
					}
				}				
				
				if (!isEmpty($scope.images)) {
					var tempImages = JSON.parse($scope.images);
					var rmIdx = -1;
					
					tempImages.forEach(function(entry, index) {
						if (entry.uid == fileItem.uid) {
							rmIdx = index;							
						}
					});
					
					if (rmIdx != -1) {
						tempImages.splice(rmIdx, 1);
						$scope.images = JSON.stringify(tempImages);
					}
				}				
				
				console.log("fileItem removed(stored). status="+status);
			});
			reqPromise.error(function(data, status, headers, config) {
				console.log("remove(stored) image failed.");
			});
		}
	};
	
	$scope.removeQueueItem = function(fileItem) {
		
		if (fileItem.uid != null) {
			var bUrl = '<c:url value="/gallery/remove/' + fileItem.uid + '"/>';
			
			var reqPromise = $http.get(bUrl);
			
			reqPromise.success(function(data, status, headers, config) {
				
				fileItem.remove();
				
				if (!isEmpty($scope.images)) {
					var tempImages = JSON.parse($scope.images);
					var rmIdx = -1;
					
					tempImages.forEach(function(entry, index) {
						if (entry.uid == fileItem.uid) {
							rmIdx = index;							
						}
					});
					
					if (rmIdx != -1) {
						tempImages.splice(rmIdx, 1);
						$scope.images = JSON.stringify(tempImages);
					}
					console.log("fileItem removed(queue). status="+status);
				}
				
			});
			reqPromise.error(function(data, status, headers, config) {
				console.log("remove(queue) image failed.");
			});
		}
	};
		 
		 
	$scope.uploader.onCompleteItem = function(fileItem, response, status, headers) {
		//console.log('onCompleteItem fileItem=', fileItem);
		//console.log('onCompleteItem status=', status);
		//console.log('onCompleteItem headers=', headers);
		
		if (status == 200) {
			var imageUrl = "<%=request.getContextPath()%>/gallery/" + response.image.id;

			fileItem.uid = response.image.id;
			
			var imageInfo = {uid:fileItem.uid, name:fileItem.file.name, size:fileItem.file.size};
			
			if (!isEmpty($scope.images)) {
				var tempImages = JSON.parse($scope.images);
				tempImages.push(imageInfo);
				$scope.images = JSON.stringify(tempImages);
			} else {
				var tempImages = [];
				tempImages.push(imageInfo);
				$scope.images = JSON.stringify(tempImages);
			}
			
			$scope.editor.insertImage($scope.editable, imageUrl);		
		} else {
			console.log("status=" + status)
			console.log("upload image failed.");
		}
	};	

	$scope.imageUpload = function(files, editor) {
		 console.log('image upload:', files);
		 console.log('image upload:', editor);
		 console.log('image upload\'s editable:', $scope.editable);
		 
			//var url = "<%=request.getContextPath()%>/resources/jakduk/icon/daum_bt.png";       	
			//editor.insertImage($scope.editable, url);
			$scope.editor = editor;
/*		
			$scope.images.push("adasassad");
			$scope.boardFreeWrite.images = "sddddd";
			console.log("subject=", $scope.subject);
			$scope.abc = "sdfsdfsdfsdf";
			console.log("ddddd",$scope.images);
			console.log("ddddd",$scope.abc);
*/

//			$scope.$apply();
			$scope.uploader.addToQueue(files);
	      };	
	
	$scope.onSubmit = function(event) {
		if ($scope.boardFreeWrite.$valid && $scope.content.length >= Jakduk.SummernoteContentsMinSize) {
			submitted = true;
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
		if ($scope.boardFreeWrite.categoryName.$invalid) {
			$scope.categoryAlert = {"classType":"text-danger", "msg":'<spring:message code="common.msg.required"/>'};
		} else {
			$scope.categoryAlert = {"classType":"text-success", "msg":'<spring:message code="user.msg.avaliable.data"/>'};
		}
	};
	
	$scope.validationSubject = function() {
		if ($scope.boardFreeWrite.subject.$invalid) {
			if ($scope.boardFreeWrite.subject.$error.required) {
				$scope.subjectAlert = {"classType":"text-danger", "msg":'<spring:message code="common.msg.required"/>'};
			} else if ($scope.boardFreeWrite.subject.$error.minlength || $scope.boardFreeWrite.subject.$error.maxlength) {
				$scope.subjectAlert = {"classType":"text-danger", "msg":'<spring:message code="Size.boardFreeWrite.subject"/>'};
			}				
		} else {
			$scope.subjectAlert = {"classType":"text-success", "msg":'<spring:message code="user.msg.avaliable.data"/>'};
		}
	};		
	
	$scope.validationContent = function() {
		if ($scope.content.length < Jakduk.SummernoteContentsMinSize) {
			$scope.contentAlert = {"classType":"text-danger", "msg":'<spring:message code="Size.boardFreeWrite.content"/>'};
		} else {
			$scope.contentAlert = {"classType":"text-success", "msg":'<spring:message code="user.msg.avaliable.data"/>'};
		}
	};	
	
});
</script>

<jsp:include page="../include/body-footer.jsp"/>

</body>
</html>