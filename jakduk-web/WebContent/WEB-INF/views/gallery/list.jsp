<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<!--[if IE 9]> <html lang="ko" class="ie9" ng-app="jakdukApp"> <![endif]-->
<!--[if !IE]><!-->
<html lang="ko" ng-app="jakdukApp"> <!--<![endif]-->
	<head>
		<title><spring:message code="gallery"/> &middot; <spring:message code="common.jakduk"/></title>
		<jsp:include page="../include/html-header.jsp"/>
		<link rel="stylesheet" href="<%=request.getContextPath()%>/bundles/gallery.list.css">
	</head>

	<body class="header-fixed">

		<div class="wrapper">
			<jsp:include page="../include/navigation-header.jsp"/>

			<!--=== Breadcrumbs ===-->
			<div class="breadcrumbs">
				<div class="container">
					<h1 class="pull-left">
						<a href="<c:url value="/gallery/list/refresh"/>"><spring:message code="gallery.list"/></a></h1>
				</div><!--/container-->
			</div><!--/breadcrumbs-->
			<!--=== End Breadcrumbs ===-->

			<div class="container content" ng-controller="galleryCtrl">

				<div class="row">
					<div class="col-sm-6">
						<div class="input-group">
							<input type="text" class="form-control" ng-model="searchWords" ng-init="searchWords=''"
								ng-keypress="($event.which === 13)?btnEnter():return"
								placeholder='<spring:message code="search.placeholder.words"/>'>
						   <span class="input-group-btn">
							   <button class="btn-u" type="button" ng-click="btnEnter();"><i class="fa fa-search"></i></button>
						   </span>
						</div>
						<span class="text-danger" ng-show="enterAlert">{{enterAlert}}</span>
					</div>
				</div>

				<hr class="padding-5"/>

				<!-- search results of gallery -->
				<div ng-show="galleries != null">
					<div class="row">
						<!-- Begin Easy Block v2 -->
						<div class="col-md-3 col-sm-4 col-xs-6 md-margin-bottom-10" ng-repeat="gallery in galleries">
							<div class="simple-block">
								<img class="img-responsive img-bordered" ng-src="<%=request.getContextPath()%>/gallery/thumbnail/{{gallery.id}}" alt="{{gallery.name}}"
									ng-click="openLightboxModal($index)">
								<p class="text-overflow max-width-360">{{gallery.name}}</p>
							</div>
						</div>
						<!-- End Simple Block -->
					</div>
				</div>

				<div infinite-scroll="infiniteScroll()" infinite-scroll-disabled="infiniteDisabled">
				</div>

				<div class="margin-bottom-10" ng-show="galleries.length > 0">
					<button type="button" class="btn-u btn-brd rounded btn-block btn-u-dark"
							ng-click="btnMoreGalleries()">
						<spring:message code="common.button.more.galleries"/> <i class="fa fa-angle-down"></i>
						<i class="fa fa-circle-o-notch fa-spin" ng-show="galleriesConn == 'connecting'"></i>
					</button>
				</div>

				<div class="contex-bg" ng-show="moreAlert.msg">
					<p class="{{moreAlert.classType}} rounded" ng-bind="moreAlert.msg"></p>
				</div>
			</div> <!-- container end -->

			<jsp:include page="../include/footer.jsp"/>

		</div><!-- /.wrapper -->

		<script src="<%=request.getContextPath()%>/bundles/gallery.list.js"></script>
		<script type="text/javascript">
			var jakdukApp = angular.module("jakdukApp", ["infinite-scroll", "angularLazyImg", "ui.bootstrap", "bootstrapLightbox", 'jakdukCommon']);

			jakdukApp.config(function (LightboxProvider) {
				LightboxProvider.getImageUrl = function (image) {
					return '<c:url value="/gallery/' + image.id + '"/>';
				};

				LightboxProvider.getImageCaption = function (image) {
					return image.name;
				};

				LightboxProvider.calculateImageDimensionLimits = function (dimensions) {
					if (dimensions.windowWidth >= 768) {
						return {
							// 92px = 2 * (30px margin of .modal-dialog
							//             + 1px border of .modal-content
							//             + 15px padding of .modal-body)
							// with the goal of 30px side margins; however, the actual side margins
							// will be slightly less (at 22.5px) due to the vertical scrollbar
							'maxWidth': dimensions.windowWidth - 92,
							'maxHeight': dimensions.windowHeight - 180
						};
					} else {
						return {
							// 52px = 2 * (10px margin of .modal-dialog
							//             + 1px border of .modal-content
							//             + 15px padding of .modal-body)
							'maxWidth': dimensions.windowWidth - 52,
							'maxHeight': dimensions.windowHeight - 130
						};
					}
				};

				// the modal height calculation has to be changed since our custom template is
				// taller than the default template
				LightboxProvider.calculateModalDimensions = function (dimensions) {
					var width = Math.max(400, dimensions.imageDisplayWidth + 32);

					if (width >= dimensions.windowWidth - 20 || dimensions.windowWidth < 768) {
						width = 'auto';
					}

					return {
						'width': width,                             // default
						'height': 'auto'                            // custom
					};
				};

				LightboxProvider.templateUrl = "<%=request.getContextPath()%>/resources/jakduk/template/lightbox02.jsp";
			});

			jakdukApp.controller("galleryCtrl", function ($scope, $http, Lightbox) {
				$scope.galleriesConn = "none";
				$scope.galleries = [];
				$scope.usersLikingCount = [];
				$scope.usersDislikingCount = [];
				$scope.infiniteDisabled = false;
				$scope.enterAlert = "";
				$scope.moreAlert = {};

				angular.element(document).ready(function () {
				});

				// infiniteScroll
				$scope.infiniteScroll = function () {
					$scope.getGalleries("init");
					$scope.infiniteDisabled = true;
				};

				// 사진 가져오기.
				$scope.getGalleries = function (type, galleryId) {

					if (Jakduk.isEmpty(galleryId)) {
						var bUrl = '<c:url value="/api/galleries?size=' + Jakduk.ItemsPerPageOnGallery + '"/>';
					} else {
						var bUrl = '<c:url value="/api/galleries?id=' + galleryId + '&size=' + Jakduk.ItemsPerPageOnGallery + '"/>';
					}

					if ($scope.galleriesConn == "none") {

						var reqPromise = $http.get(bUrl);

						$scope.galleriesConn = "connecting";

						reqPromise.success(function (data, status, headers, config) {

							if (data.galleries.length < 1) { // 댓글이 하나도 없을때
								if (type == "init") {
								} else {
									$scope.moreAlert.msg = '<spring:message code="gallery.msg.there.are.no.new.galleries"/>';
									$scope.moreAlert.classType = "bg-warning";
								}
							} else {
								data.galleries.forEach(function (gallery) {
									$scope.galleries.push(gallery);
								});

								for (var key in data.usersLikingCount) {
									var value = data.usersLikingCount[key];
									$scope.usersLikingCount[key] = value;
								}

								for (var key in data.usersDislikingCount) {
									var value = data.usersDislikingCount[key];
									$scope.usersDislikingCount[key] = value;
								}
							}

							$scope.galleriesConn = "none";

						});
						reqPromise.error(function (data, status, headers, config) {
							$scope.galleriesConn = "none";
							console.error(data.message);
						});
					}
				};

				// 사진 더보기 단추.
				$scope.btnMoreGalleries = function () {

					if ($scope.galleries.length > 0) {
						var lastGallery = $scope.galleries[$scope.galleries.length - 1].id;
						$scope.getGalleries("more", lastGallery);
					} else {
						$scope.getGalleries("more");
					}
				};

				$scope.openLightboxModal = function (index) {
					Lightbox.openModal($scope.galleries, index);
				};

				$scope.btnEnter = function () {
					var isValid = true;

					if ($scope.searchWords.trim() < 1) {
						$scope.enterAlert = '<spring:message code="search.msg.enter.words.you.want.search.words"/>';
						isValid = false;
					}

					if (isValid) {
						location.href = '<c:url value="/search?q=' + $scope.searchWords.trim() + '&w=GA;"/>';
					}
				};
			});

			//Lightbox
			jakdukApp.controller("LightboxCtrl", function ($scope, $window, Lightbox) {

				$scope.dateTimeFormat = JSON.parse('${dateTimeFormat}');

				$scope.objectIdFromDate = function (date) {
					return Math.floor(date.getTime() / 1000).toString(16) + "0000000000000000";
				};

				$scope.dateFromObjectId = function (objectId) {
					if (!Jakduk.isEmpty(objectId)) {
						return new Date(parseInt(objectId.substring(0, 8), 16) * 1000);
					}
				};

				$scope.intFromObjectId = function (objectId) {
					return parseInt(objectId.substring(0, 8), 16) * 1000;
				};

				$scope.openNewTab = function () {
					$window.open(Lightbox.imageUrl);
				};
			});

			$(document).ready(function () {
				App.init();
			});
		</script>
	</body>

</html>