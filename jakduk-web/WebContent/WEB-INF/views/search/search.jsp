<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!DOCTYPE html>
<!--[if IE 9]> <html lang="ko" class="ie9" ng-app="jakdukApp"> <![endif]-->
<!--[if !IE]><!-->
<html lang="ko" ng-app="jakdukApp"> <!--<![endif]-->
	<head>
		<title><spring:message code="search"/> &middot; <spring:message code="common.jakduk"/></title>
		<jsp:include page="../include/html-header.jsp"/>
		<link rel="stylesheet" href="<%=request.getContextPath()%>/bundles/search.css">
	</head>

	<body class="header-fixed">

		<div class="wrapper" ng-controller="searchCtrl">
			<jsp:include page="../include/navigation-header.jsp"/>

			<!--=== Breadcrumbs ===-->
			<div class="breadcrumbs">
				<div class="container">
					<h1 class="pull-left"><a href="<c:url value="/search/refresh"/>"><spring:message code="search"/></a></h1>
				</div><!--/container-->
			</div><!--/breadcrumbs-->
			<!--=== End Breadcrumbs ===-->

			<div class="search-block-v2">
				<div class="container">
					<div class="col-md-6 col-md-offset-3 margin-bottom-10">
						<div class="input-group">
							<label class="checkbox-inline">
								<input type="checkbox" ng-model="where.posts"><spring:message code="search.post"/>
							</label>
							<label class="checkbox-inline">
								<input type="checkbox" ng-model="where.comments"><spring:message code="search.comment"/>
							</label>
							<label class="checkbox-inline">
								<input type="checkbox" ng-model="where.galleries"><spring:message code="search.gallery"/>
							</label>
						</div>
						<span class="text-danger" ng-show="whereAlert">{{whereAlert}}</span>
					</div>
					<div class="col-md-6 col-md-offset-3">
						<div class="input-group">
							<input type="text" class="form-control" ng-model="searchWords" ng-init="searchWords='${q}'"
								ng-keypress="($event.which === 13)?btnEnter():return" placeholder='<spring:message code="search.placeholder.words"/>'>
                    <span class="input-group-btn">
                        <button class="btn-u ladda-button" type="button" ng-click="btnEnter();" ladda="btnSearch" data-style="expand-right">
							<i class="fa fa-search"></i>
                        </button>
                    </span>
						</div>
						<span class="text-danger" ng-show="enterAlert">{{enterAlert}}</span>
					</div>
				</div>
			</div>

			<!--=== Content Part ===-->
			<div class="container s-results">
				<!-- search results of post -->
				<div class="margin-bottom-10" ng-show="posts.hits != null">
					<span class="results-number"><spring:message code="search.post.results" arguments="{{posts.hits.total}}"/></span>

					<!-- Begin Inner Results -->
					<div ng-repeat="hit in posts.hits.hits">
						<div class="inner-results">
							<h3 ng-if="hit.highlight.subject.length > 0">
								<a href='<c:url value="/board/free/{{hit._source.seq}}"/>' ng-bind-html="hit.highlight.subject[0]"></a>
							</h3>
							<h3 ng-if="hit.highlight.subject.length == null">
								<a href="<c:url value="/board/free/{{hit._source.seq}}"/>">{{hit._source.subject}}</a></h3>
							<p ng-if="hit.highlight.content.length > 0" ng-bind-html="hit.highlight.content[0]"></p>
							<p ng-if="hit.highlight.content.length == null">{{hit.fields.content_preview[0]}}</p>
							<ul class="list-inline down-ul">
								<li><i aria-hidden="true" class="icon-user"></i> {{hit._source.writer.username}}</li>
								<li>{{dateFromObjectId(hit._id) | date:dateTimeFormat.dateTime}}</li>
								<li></li>
							</ul>
						</div>
						<hr class="padding-5"/>
					</div>
				</div>

				<div class="text-left" ng-show="posts.hits.total > 0">
					<uib-pagination ng-model="currentPage" total-items="posts.hits.total" max-size="10" items-per-page="itemsPerPage"
						previous-text="&lsaquo;" next-text="&rsaquo;" ng-change="pageChanged()" ng-show="whereSize == 1"></uib-pagination>

					<div class="text-right col-md-12 margin-bottom-10" ng-show="whereSize > 1">
						<ul class="list-unstyled">
							<li><a href='<c:url value="/search?q=${q}&w=PO;"/>'>
								<spring:message code="search.more.post.results"/> <i class="fa fa-chevron-right"></i>
							</a></li>
						</ul>
					</div>
				</div>

				<!-- search results of post -->
				<div class="margin-bottom-10" ng-show="comments.hits != null">
					<span class="results-number"><spring:message code="search.comment.results" arguments="{{comments.hits.total}}"/></span>

					<div ng-repeat="hit in comments.hits.hits">
						<div class="inner-results">
							<ul class="list-inline up-ul">
								<li><i aria-hidden="true" class="icon-user"></i> {{hit._source.writer.username}}</li>
								<li>{{dateFromObjectId(hit._id) | date:dateTimeFormat.dateTime}}</li>
							</ul>
							<p>
								<a href='<c:url value="/board/free/{{hit._source.boardItem.seq}}"/>' ng-bind-html="hit.highlight.content[0]"></a>
							</p>
							<ul class="list-inline up-ul text-overflow">
								<li>
									<a href='<c:url value="/board/free/{{hit._source.boardItem.seq}}"/>'>
										<spring:message code="board.subject"/>
										:
										{{postsHavingComments[hit._source.boardItem.id].subject}}
									</a>
								</li>
							</ul>

						</div>
						<hr class="padding-5"/>
					</div>
				</div>

				<div class="text-left" ng-show="comments.hits.total > 0">
					<uib-pagination ng-model="currentPage" total-items="comments.hits.total" max-size="10" items-per-page="itemsPerPage"
						previous-text="&lsaquo;" next-text="&rsaquo;" ng-change="pageChanged()" ng-show="whereSize == 1"></uib-pagination>

					<div class="text-right col-md-12 margin-bottom-10" ng-show="whereSize > 1">
						<ul class="list-unstyled">
							<li><a href='<c:url value="/search?q=${q}&w=CO;"/>'>
								<spring:message code="search.more.comment.results"/> <i class="fa fa-chevron-right"></i>
							</a></li>
						</ul>
					</div>
				</div>

				<!-- search results of gallery -->
				<div ng-show="galleries.hits != null">
					<span class="results-number"><spring:message code="search.gallery.results" arguments="{{galleries.hits.total}}"/></span>

					<div class="row">
						<!-- Begin Easy Block v2 -->
						<div class="col-md-3 col-sm-4 col-xs-6 md-margin-bottom-10" ng-repeat="hit in galleries.hits.hits">
							<div class="simple-block">
								<img class="img-responsive img-bordered" ng-src="<%=request.getContextPath()%>/gallery/thumbnail/{{hit._id}}" alt="{{hit._source.name}}"
									ng-click="openLightboxModal($index)">
								<p ng-bind-html="hit.highlight.name[0]"></p>
							</div>
						</div>
						<!-- End Simple Block -->
					</div>
				</div>

				<div class="text-left" ng-show="galleries.hits.total > 0">
					<uib-pagination ng-model="currentPage" total-items="galleries.hits.total" max-size="10" items-per-page="itemsPerPageGallery"
						previous-text="&lsaquo;" next-text="&rsaquo;" ng-change="pageChanged()" ng-show="whereSize == 1"></uib-pagination>

					<div class="text-right col-md-12 margin-bottom-10" ng-show="whereSize > 1">
						<ul class="list-unstyled">
							<li><a href='<c:url value="/search?q=${q}&w=GA;"/>'>
								<spring:message code="search.more.gallery.results"/> <i class="fa fa-chevron-right"></i>
							</a></li>
						</ul>
					</div>
				</div>

			</div><!--/container-->
			<!--=== End Content Part ===-->

			<jsp:include page="../include/footer.jsp"/>

		</div><!-- /.container -->

		<script src="<%=request.getContextPath()%>/bundles/search.js"></script>
		<script type="text/javascript">
			var jakdukApp = angular.module("jakdukApp", ["ngSanitize", "ui.bootstrap", "bootstrapLightbox", "angular-ladda", 'jakdukCommon']);

			jakdukApp.config(function (LightboxProvider) {
				LightboxProvider.getImageUrl = function (image) {
					return '<c:url value="/gallery/' + image._id + '"/>';
				};

				LightboxProvider.getImageCaption = function (image) {
					return image._source.name;
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

				LightboxProvider.templateUrl = "<%=request.getContextPath()%>/resources/jakduk/template/lightbox01.jsp";
			});

			jakdukApp.controller("searchCtrl", function ($scope, $http, $location, Lightbox) {
				$scope.resultsConn = "none";
				$scope.galleriesConn = "none";
				$scope.where = {};
				$scope.whereSize = 0;
				$scope.posts = {};
				$scope.comments = {};
				$scope.postsHavingComments = {};
				$scope.galleries = {};
				$scope.itemsPerPage = Jakduk.ItemsPerPageOnSearch;
				$scope.itemsPerPageGallery = Jakduk.ItemsPerPageOnSearchGallery;
				$scope.isGalleryOnly = false;
				$scope.whereAlert = "";
				$scope.enterAlert = "";
				$scope.dateTimeFormat = JSON.parse('${dateTimeFormat}');
				$scope.btnSearch = false;

				angular.element(document).ready(function () {
					var from = parseInt("${from}");
					var where = "${w}";
					var size = $scope.itemsPerPage;

					if (!Jakduk.isEmpty(where)) {
						var arrW = where.split(";");

						$scope.whereSize = arrW.length - 1;

						for (var i = 0; i < arrW.length; i++) {
							var tempW = arrW[i];

							if (tempW == "PO") {
								$scope.where.posts = true;
							}
							if (tempW == "CO") {
								$scope.where.comments = true;
							}
							if (tempW == "GA") {
								$scope.where.galleries = true;
							}
						}
						$scope.$apply();
					} else {
						$scope.where = {posts: false, comments: false, galleries: false};
					}

					if ($scope.where.galleries == true && $scope.where.posts == null && $scope.where.comments == null) {
						$scope.isGalleryOnly = true;
					}

					if (from > 0) {
						if ($scope.isGalleryOnly == true) {
							$scope.currentPage = (from + $scope.itemsPerPageGallery) / $scope.itemsPerPageGallery;
						} else {
							$scope.currentPage = (from + $scope.itemsPerPage) / $scope.itemsPerPage;
						}
					} else {
						$scope.currentPage = 1;
					}

					if ($scope.whereSize == 1) {
						if ($scope.isGalleryOnly == true) {
							size = $scope.itemsPerPageGallery;
						} else {
							size = $scope.itemsPerPage;
						}
					} else if ($scope.whereSize == 2) {
						size = 5;
					} else if ($scope.whereSize >= 3) {
						size = 3;
					}

					$scope.getResults(where, from, size);

					App.init();
				});

				$scope.objectIdFromDate = function (date) {
					return Math.floor(date.getTime() / 1000).toString(16) + "0000000000000000";
				};

				$scope.dateFromObjectId = function (objectId) {
					return new Date(parseInt(objectId.substring(0, 8), 16) * 1000);
				};

				$scope.intFromObjectId = function (objectId) {
					return parseInt(objectId.substring(0, 8), 16) * 1000;
				};

				$scope.btnEnter = function () {
					var isValid = true;

					if ($scope.where.posts != true && $scope.where.comments != true && $scope.where.galleries != true) {
						$scope.whereAlert = '<spring:message code="search.msg.you.should.select.at.least.one"/>';
						isValid = false;
					}

					if ($scope.searchWords.trim() < 1) {
						$scope.enterAlert = '<spring:message code="search.msg.enter.words.you.want.search.words"/>';
						isValid = false;
					}

					if (isValid) {
						var where = "";

						if ($scope.where.posts == true) {
							where += "PO;";
						}
						if ($scope.where.comments == true) {
							where += "CO;";
						}
						if ($scope.where.galleries == true) {
							where += "GA;";
						}

						location.href = '<c:url value="/search?q=' + $scope.searchWords.trim() + '&w=' + where + '"/>';
					}
				};

				$scope.getResults = function (where, from, size) {

					if ($scope.searchWords.trim() < 1 ||
						($scope.where.posts != true && $scope.where.comments != true && $scope.where.galleries != true)) {
						return;
					} else {
						$scope.whereAlert = "";
						$scope.enterAlert = "";
					}

					var bUrl = '<c:url value="/search/data.json?q=' + encodeURIComponent($scope.searchWords) + '&w=' + where + '&from=' + from + '&size=' + size + '"/>';

					if ($scope.resultsConn == "none") {

						var reqPromise = $http.get(bUrl);

						$scope.resultsConn = "loading";
						$scope.btnSearch = true;

						reqPromise.success(function (data, status, headers, config) {

							if (data.posts != null) {
								//console.log("posts=" + data.posts);
								$scope.posts = JSON.parse(data.posts);
							}

							if (data.comments != null) {
								$scope.comments = JSON.parse(data.comments);
							}

							if (data.postsHavingComments != null) {
								$scope.postsHavingComments = data.postsHavingComments;
							}

							if (data.galleries != null) {
								$scope.galleries = JSON.parse(data.galleries);
							}

							$scope.resultsConn = "none";
							$scope.btnSearch = false;

						});
						reqPromise.error(function (data, status, headers, config) {
							$scope.resultsConn = "none";
							$scope.error = '<spring:message code="common.msg.error.network.unstable"/>';
							$scope.btnSearch = false;
						});
					}
				};

				$scope.pageChanged = function () {
					var from = $scope.currentPage;

					if (from > 1) {
						if ($scope.isGalleryOnly == true) {
							from = (from - 1) * $scope.itemsPerPageGallery;
						} else {
							from = (from - 1) * $scope.itemsPerPage;
						}
					} else {
						from = 0;
					}

					location.href = '<c:url value="/search?q=' + $scope.searchWords.trim() + '&w=' + '${w}' + '&from=' + from + '"/>';
				};

				$scope.openLightboxModal = function (index) {
					Lightbox.openModal($scope.galleries.hits.hits, index);
				};
			});

			// Lightbox
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
		</script>
	</body>
</html>