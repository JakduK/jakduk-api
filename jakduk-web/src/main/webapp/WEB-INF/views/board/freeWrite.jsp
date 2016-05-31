<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<!--[if IE 9]> <html lang="ko" class="ie9" ng-app="jakdukApp"> <![endif]-->
<!--[if !IE]><!-->
<html lang="ko" ng-app="jakdukApp"> <!--<![endif]-->
	<head>
		<title><spring:message code="board.write"/> &middot; <spring:message code="common.jakduk"/></title>
		<jsp:include page="../include/html-header.jsp"/>
		<link rel="stylesheet" href="<%=request.getContextPath()%>/bundles/board.write.css">
	</head>

	<body class="header-fixed">
		<div class="wrapper">
			<jsp:include page="../include/navigation-header.jsp"/>

			<c:set var="contextPath" value="<%=request.getContextPath()%>"/>
			<c:set var="summernoteLang" value="en-US"/>

			<!--=== Breadcrumbs ===-->
			<div class="breadcrumbs">
				<div class="container">
					<h1 class="pull-left"><spring:message code="board.write"/></h1>
				</div><!--/container-->
			</div><!--/breadcrumbs-->
			<!--=== End Breadcrumbs ===-->

			<div class="container content" ng-controller="FreeWriteCtrl">

				<form:form commandName="boardFreeWrite" name="boardFreeWrite" action="${contextPath}/board/free/write" method="POST"
					ng-submit="onSubmit($event)">
					<form:textarea path="content" class="hidden" ng-bind="content" value="${boardFreeWrite.content}"/>
					<form:textarea path="images" class="hidden" ng-model="images" ng-init="images='${boardFreeWrite.images}'"/>
					<textarea id="subject_temp" hidden="hidden">${boardFreeWrite.subject}</textarea>

					<div class="form-group" ng-class="{'has-success':boardFreeWrite.categoryName.$valid, 'has-error':boardFreeWrite.categoryName.$invalid}">
						<div class="row">
							<div class="col-sm-3">
								<label for="categoryName" class="control-label"><abbr title="required">*</abbr>
									<spring:message code="board.category"/></label>
								<form:select path="categoryName" cssClass="form-control"
									ng-model="categoryName" ng-init="categoryName='${boardFreeWrite.categoryName}'" ng-change="validationCategory()" ng-required="true">
									<form:option value=""><spring:message code="board.category.init"/></form:option>
									<c:forEach items="${boardCategories}" var="category">
										<form:option value="${category.name}"><spring:message code="${category.resName}"/></form:option>
									</c:forEach>
								</form:select>
								<form:errors path="categoryName" cssClass="text-danger" element="span" ng-hide="categoryAlert.msg"/>
								<span class="{{categoryAlert.classType}}" ng-show="categoryAlert.msg" ng-bind="categoryAlert.msg"></span>
							</div>
						</div>
					</div>

					<div class="form-group has-feedback" ng-class="{'has-success':boardFreeWrite.subject.$valid, 'has-error':boardFreeWrite.subject.$invalid}">
						<label for="subject" class="control-label"><abbr title="required">*</abbr>
							<spring:message code="board.subject"/></label>
						<input type="text" name="subject" class="form-control" placeholder='<spring:message code="board.placeholder.subject"/>'
							ng-model="subject" ng-change="validationSubject()" ng-model-options="{ debounce: 400 }"
							ng-required="true" ng-minlength="3" ng-maxlength="60"/>
						<span class="glyphicon form-control-feedback" ng-class="{'glyphicon-ok':boardFreeWrite.subject.$valid,
						'glyphicon-remove':boardFreeWrite.subject.$invalid}"></span>
						<form:errors path="subject" cssClass="text-danger" element="span" ng-hide="subjectAlert.msg"/>
						<span class="{{subjectAlert.classType}}" ng-show="subjectAlert.msg" ng-bind="subjectAlert.msg"></span>
					</div>

					<div class="form-group" ng-class="{'has-success':content.length >= 5, 'has-error':content.length < 5}">
						<label for="content" class="control-label"><abbr title="required">*</abbr>
							<spring:message code="board.content"/></label>
						<summernote config="options" ng-model="content" on-image-upload="imageUpload(files, editor)" editable="editable"></summernote>
						<form:errors path="content" cssClass="text-danger" element="span" ng-hide="contentAlert.msg"/>
						<span class="{{contentAlert.classType}}" ng-show="contentAlert.msg" ng-bind="contentAlert.msg"></span>
					</div>

					<h4 class="text-primary ng-cloak" ng-show="storedImages.length > 0 || uploader.queue.length > 0">
						<spring:message code="board.gallery.list"/>
					</h4>

					<div class="row ng-cloak">
						<!-- sotred Images -->
						<div class="media col-xs-12 col-sm-4" ng-repeat="item in storedImages">
							<div class="media-left media-middle">
								<img class="media-object" ng-src="<%=request.getContextPath()%>/gallery/thumbnail/{{item.uid}}" style="width:50px; height:50px;">
							</div>
							<div class="media-body">
								<h5 class="media-heading">
									<button type="button" class="btn btn-success btn-xs" ng-click="insertImage(item)">
										<span class="glyphicon glyphicon-upload"></span>
									</button>
									<button type="button" class="btn btn-danger btn-xs" ng-click="removeStoredItem(item)">
										<span class="glyphicon glyphicon-remove-circle"></span>
									</button>
									<small>{{item.size/1024|number:1}} KB</small>
								</h5>
								<div class="form-group has-feedback"
									 ng-class="{'has-success':item.name.length >= 2, 'has-warning':item.name.length < 2 || item.name == null}">
									<input type="text" class="form-control input-sm col-md-2 has-error" placeholder='<spring:message code="gallery.placeholder.name"/>'
										ng-model="item.name" ng-blur="onGalleryItem(item, 'stored')">
									<span class="glyphicon form-control-feedback"
										  ng-class="{'glyphicon-ok':item.name.length >= 2, 'glyphicon-warning-sign':item.name.length < 2 || item.name == null}"/>
								</div>
							</div>
						</div>
						<!-- queue Images -->
						<div class="media col-xs-12 col-sm-4" ng-repeat="item in uploader.queue">
							<div class="media-left media-middle">
								<img class="media-object" ng-src="<%=request.getContextPath()%>/gallery/thumbnail/{{item.uid}}" style="width:50px; height:50px;">
							</div>
							<div class="media-body">
								<h5 class="media-heading">
									<button type="button" class="btn btn-success btn-xs" ng-click="insertImage(item)">
										<span class="glyphicon glyphicon-upload"></span>
									</button>
									<button type="button" class="btn btn-danger btn-xs" ng-click="removeQueueItem(item)">
										<span class="glyphicon glyphicon-remove-circle"></span>
									</button>
									<small>{{item.file.size/1024|number:1}} KB
										<code>{{item.progress}}%</code>
									</small>
								</h5>
								<div class="form-group has-feedback" ng-class="{'has-success':item.newName.length >= 2, 'has-warning':item.newName.length < 2 || item.newName == null}">
									<input type="text" class="form-control input-sm col-md-2 has-error" placeholder='<spring:message code="gallery.placeholder.name"/>'
										ng-model="item.newName" ng-blur="onGalleryItem(item, 'queue')">
									<span class="glyphicon form-control-feedback"
										  ng-class="{'glyphicon-ok':item.newName.length >= 2,'glyphicon-warning-sign':item.newName.length < 2 || item.newName == null}"/>
								</div>
							</div>
						</div>
					</div>

					<hr class="padding-5" ng-show="storedImages.length > 0 || uploader.queue.length > 0"/>

					<div class="form-group">
						<button type="submit" class="btn-u rounded ladda-button" ladda="btnSubmit" data-style="expand-right">
							<span class="glyphicon glyphicon-upload"></span> <spring:message code="common.button.write"/>
						</button>
						<button type="button" class="btn-u btn-u-default rounded" ng-click="onDiscard()">
							<span class="glyphicon glyphicon-ban-circle"></span> <spring:message code="common.button.cancel"/>
						</button>
						<div>
							<span class="{{buttonAlert.classType}}" ng-show="buttonAlert.msg" ng-bind="buttonAlert.msg"></span>
						</div>
					</div>
				</form:form>

			</div>

			<jsp:include page="../include/footer.jsp"/>
		</div> <!-- /.container -->

		<script src="<%=request.getContextPath()%>/bundles/board.write.js"></script>
		<c:if test="${fn:contains('ko', pageContext.response.locale.language)}">
			<script src="<%=request.getContextPath()%>/resources/summernote/lang/summernote-ko-KR.js"></script>
			<c:set var="summernoteLang" value="ko-KR"/>
		</c:if>
		<script type="text/javascript">
			angular.module("jakdukApp", ["summernote", "angularFileUpload", "angular-ladda", 'jakdukCommon'])
				.controller('FreeWriteCtrl', function ($scope, $http, $window, $document, FileUploader) {
					var document = $document[0],
						isSubmitted;

					$scope.onSubmit = onSubmit;
					$scope.onDiscard = onDiscard;

					$scope.categoryAlert = {};
					$scope.subjectAlert = {};
					$scope.contentAlert = {};
					$scope.buttonAlert = {};
					$scope.storedImages = [];
					$scope.subject = document.getElementById("subject_temp").value;

					$window.onbeforeunload = function (e) {
						if (!isSubmitted && $scope.boardFreeWrite.$dirty) {
							(e || $window.event).returnValue = '<spring:message code="common.msg.are.you.sure.leave.page"/>';
							return '<spring:message code="common.msg.are.you.sure.leave.page"/>';
						}
					};

					angular.element(document).ready(function () {
						if (!Jakduk.isEmpty($scope.images)) {
							var objImages = JSON.parse($scope.images);
							objImages.forEach(function (entry) {
								$scope.storedImages.push(entry);
								$scope.$apply();
							});
						}
					});

					var contentValue = document.getElementById("content").value;
					$scope.content = contentValue ? contentValue : '';

					$scope.options = {
						height: 0,
						//placeholder: '<spring:message code="board.msg.write.text.here"/>',
						lang: "${summernoteLang}",
						toolbar: [
							['style', ['style']],
							['font', ['bold', 'italic', 'underline', /*'superscript', 'subscript', */'strikethrough', 'clear']],
							['fontname', ['fontname']],
							// ['fontsize', ['fontsize']], // Still buggy
							['color', ['color']],
							['para', ['ul', 'ol', 'paragraph']],
							// ['height', ['height']],
							['table', ['table']],
							['insert', ['link', 'picture', 'video', 'hr']],
							['view', ['fullscreen', 'codeview']],
							['help', ['help']]
						]
					};

					$scope.uploader = new FileUploader({
						url: '<c:url value="/api/gallery"/>',
						autoUpload: true,
						method: "POST"
					});

					// 이미 등록된 이미지들 가져오기.
					$scope.onGalleryItem = function (fileItem, type) {
						if (!Jakduk.isEmpty($scope.images)) {
							var tempImages = JSON.parse($scope.images);
							var rmIdx = -1;

							tempImages.forEach(function (entry, index) {
								if (entry.uid == fileItem.uid) {
									rmIdx = index;
								}
							});

							if (rmIdx != -1) {
								if (type == 'queue') {
									var imageInfo = {
										uid: fileItem.uid,
										name: fileItem.newName,
										fileName: fileItem.file.name,
										size: fileItem.file.size
									};
								} else if (type == 'stored') {
									var imageInfo = {
										uid: fileItem.uid,
										name: fileItem.name,
										fileName: fileItem.fileName,
										size: fileItem.size
									};
								}

								tempImages.splice(rmIdx, 1, imageInfo);
								$scope.images = JSON.stringify(tempImages);
								console.log("fileItem updated(queue). fileInfo=", imageInfo);
							}
						}
					};

					// 이미 등록된 이미지 삭제.
					$scope.removeStoredItem = function (fileItem) {
						if (fileItem.uid != null) {
							var bUrl = '<c:url value="/api/gallery/' + fileItem.uid + '"/>';

							var reqPromise = $http.delete(bUrl);

							reqPromise.success(function (data, status, headers, config) {

								if (!Jakduk.isEmpty($scope.storedImages)) {
									var rmIdx = -1;

									$scope.storedImages.forEach(function (entry, index) {
										if (entry.uid == fileItem.uid) {
											rmIdx = index;
										}
									});

									if (rmIdx != -1) {
										$scope.storedImages.splice(rmIdx, 1);
									}
								}

								if (!Jakduk.isEmpty($scope.images)) {
									var tempImages = JSON.parse($scope.images);
									var rmIdx = -1;

									tempImages.forEach(function (entry, index) {
										if (entry.uid == fileItem.uid) {
											rmIdx = index;
										}
									});

									if (rmIdx != -1) {
										tempImages.splice(rmIdx, 1);
										$scope.images = JSON.stringify(tempImages);
									}
								}

								console.log("fileItem removed(stored). status=" + status);
							});
							reqPromise.error(function (data, status, headers, config) {
								console.error(data.message);
							});
						}
					};

					// 지금 업로드한 이미지를 큐에서 삭제.
					$scope.removeQueueItem = function (fileItem) {

						if (fileItem.uid != null) {

							var bUrl = '<c:url value="/api/gallery/' + fileItem.uid + '"/>';

							var reqPromise = $http.delete(bUrl);

							reqPromise.success(function (data, status, headers, config) {

								fileItem.remove();

								if (!Jakduk.isEmpty($scope.images)) {
									var tempImages = JSON.parse($scope.images);
									var rmIdx = -1;

									tempImages.forEach(function (entry, index) {
										if (entry.uid == fileItem.uid) {
											rmIdx = index;
										}
									});

									if (rmIdx != -1) {
										tempImages.splice(rmIdx, 1);
										$scope.images = JSON.stringify(tempImages);
									}
									console.log("fileItem removed(queue). status=" + status);
								}

							});
							reqPromise.error(function (data, status, headers, config) {
								console.error(data.message);
							});
						}
					};

					// 이미지 업로드를 완료.
					$scope.uploader.onCompleteItem = function (fileItem, response, status, headers) {

						if (status == 200) {
							var imageUrl = "<%=request.getContextPath()%>/gallery/" + response.id;

							fileItem.uid = response.id;

							var imageInfo = {
								uid: fileItem.uid,
								name: fileItem.newName,
								fileName: fileItem.file.name,
								size: fileItem.file.size
							};

							if (!Jakduk.isEmpty($scope.images)) {
								var tempImages = JSON.parse($scope.images);
								tempImages.push(imageInfo);
								$scope.images = JSON.stringify(tempImages);
							} else {
								var tempImages = [];
								tempImages.push(imageInfo);
								$scope.images = JSON.stringify(tempImages);
							}

							$(".summernote").summernote("insertImage", imageUrl, function ($image) {
								$image.addClass("img-responsive");
							});
						} else {
							console.error(response.message);
						}
					};

					// 에디터 아래의 이미지 업로드 버튼을 누를 때.
					$scope.insertImage = function (item) {
						var imageUrl = "<%=request.getContextPath()%>/gallery/" + item.uid;

						$(".summernote").summernote("insertImage", imageUrl, function ($image) {
							$image.addClass("img-responsive");
						});
					};

					$scope.imageUpload = function (files, editor) {
						$scope.uploader.addToQueue(files);
					};

					$scope.validationCategory = function () {
						if ($scope.boardFreeWrite.categoryName.$invalid) {
							$scope.categoryAlert = {
								"classType": "text-danger",
								"msg": '<spring:message code="common.msg.required"/>'
							};
						} else {
							$scope.categoryAlert = {
								"classType": "text-success",
								"msg": '<spring:message code="user.msg.avaliable.data"/>'
							};
						}
					};

					$scope.validationSubject = function () {
						if ($scope.boardFreeWrite.subject.$invalid) {
							if ($scope.boardFreeWrite.subject.$error.required) {
								$scope.subjectAlert = {
									"classType": "text-danger",
									"msg": '<spring:message code="common.msg.required"/>'
								};
							} else if ($scope.boardFreeWrite.subject.$error.minlength || $scope.boardFreeWrite.subject.$error.maxlength) {
								$scope.subjectAlert = {
									"classType": "text-danger",
									"msg": '<spring:message code="Size.boardFreeWrite.subject"/>'
								};
							}
						} else {
							$scope.subjectAlert = {
								"classType": "text-success",
								"msg": '<spring:message code="user.msg.avaliable.data"/>'
							};
						}
					};

					$scope.validationContent = function () {
						if ($scope.content.length < Jakduk.SummernoteContentsMinSize) {
							$scope.contentAlert = {
								"classType": "text-danger",
								"msg": '<spring:message code="Size.boardFreeWrite.content"/>'
							};
						} else {
							$scope.contentAlert = {
								"classType": "text-success",
								"msg": '<spring:message code="user.msg.avaliable.data"/>'
							};
						}
					};

					$scope.validationGalleries = function () {
						if (!Jakduk.isEmpty($scope.images)) {
							var tempImages = JSON.parse($scope.images);
							var rmIdx = -1;

							tempImages.forEach(function (entry, index) {
								if (entry.name != null && (entry.name.length < 2 || entry.name.length > 50)) {
									entry.name = "";
								}
							});

							$scope.images = JSON.stringify(tempImages);
						}
					};

					function onSubmit(event) {
						if ($scope.boardFreeWrite.$valid && $scope.content.length >= Jakduk.SummernoteContentsMinSize) {
							isSubmitted = true;
							$scope.btnSubmit = true;
							$scope.validationGalleries();
						} else {
							isSubmitted = false;
							$scope.validationCategory();
							$scope.validationSubject();
							$scope.validationContent();

							$scope.buttonAlert = {
								"classType": "text-danger",
								"msg": '<spring:message code="common.msg.need.form.validation"/>'
							};
							event.preventDefault();
						}
					}

					function onDiscard() {
						$window.location.href = '<c:url value="/board"/>';
					}
				});

			$(document).ready(function () {
				App.init();
			});
		</script>
	</body>
</html>