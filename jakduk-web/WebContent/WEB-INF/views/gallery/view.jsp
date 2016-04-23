<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<!DOCTYPE html>
<!--[if IE 9]> <html lang="ko" class="ie9" ng-app="jakdukApp"> <![endif]-->
<!--[if !IE]><!-->
<html lang="ko" ng-app="jakdukApp"> <!--<![endif]-->
	<head>
		<title>${gallery.name} - <spring:message code="gallery"/> &middot; <spring:message code="common.jakduk"/></title>
		<jsp:include page="../include/html-header.jsp"/>
		<link rel="stylesheet" href="<%=request.getContextPath()%>/bundles/gallery.view.css">
	</head>

	<body class="header-fixed">

		<div class="wrapper">
			<jsp:include page="../include/navigation-header.jsp"/>

			<sec:authorize access="isAnonymous()">
				<c:set var="authRole" value="ANNONYMOUS"/>
			</sec:authorize>
			<sec:authorize access="hasAnyRole('ROLE_USER_01', 'ROLE_USER_02', 'ROLE_USER_03')">
				<c:set var="authRole" value="USER"/>
				<sec:authentication property="principal.id" var="accountId"/>
			</sec:authorize>
			<sec:authorize access="hasAnyRole('ROLE_ROOT')">
				<c:set var="authAdminRole" value="ROOT"/>
			</sec:authorize>

			<!--=== Breadcrumbs ===-->
			<div class="breadcrumbs">
				<div class="container">
					<h1 class="pull-left"><a href="<c:url value="/gallery/list/refresh"/>"><spring:message code="gallery"/></a>
					</h1>
				</div><!--/container-->
			</div><!--/breadcrumbs-->
			<!--=== End Breadcrumbs ===-->

			<!--=== Content Part ===-->
			<div class="container content blog-page blog-item">

				<div class="margin-bottom-10">
					<button type="button" class="btn-u btn-brd rounded" onclick="location.href='<c:url value="/gallery"/>'">
						<i class="fa fa-th"></i></button>

					<c:choose>
						<c:when test="${!empty prev}">
							<button type="button" class="btn-u btn-brd rounded" onclick="location.href='
								<c:url value="/gallery/view/${prev.id}"/>'"><i class="fa fa-chevron-left"></i></button>
						</c:when>
						<c:otherwise>
							<button type="button" class="btn-u btn-brd rounded btn-u-default disabled" disabled="disabled">
								<i class="fa fa-chevron-left text-muted"></i></button>
						</c:otherwise>
					</c:choose>
					<c:choose>
						<c:when test="${!empty next}">
							<button type="button" class="btn-u btn-brd rounded" onclick="location.href='
								<c:url value="/gallery/view/${next.id}"/>'"><i class="fa fa-chevron-right"></i></button>
						</c:when>
						<c:otherwise>
							<button type="button" class="btn-u btn-brd rounded btn-u-default disabled" disabled="disabled">
								<i class="fa fa-chevron-right text-muted"></i></button>
						</c:otherwise>
					</c:choose>
				</div>

				<div class="blog margin-bottom-20" ng-controller="galleryController">
					<input type="hidden" id="galleryName" value="${gallery.name}">
					<h2>${gallery.name}</h2>
					<div class="blog-post-tags">
						<ul class="list-unstyled list-inline blog-info">
							<li><i class="fa fa-user"></i> ${gallery.writer.username}</li>
							<li><i class="fa fa-clock-o"></i> {{dateFromObjectId("${gallery.id}") | date:"${dateTimeFormat.dateTime}"}}
							</li>
							<li><i class="fa fa-eye"></i> ${gallery.views}</li>
						</ul>
					</div>

					<p>
						<img class="img-responsive" src="<%=request.getContextPath()%>/gallery/${gallery.id}" alt="${gallery.name}">
					</p>

					<!-- 엮인 글 -->
					<ul class="list-group">
						<li class="list-group-item">
							<strong><spring:message code="gallery.linked.posts"/></strong>
							<c:forEach items="${linkedPosts}" var="post">
								<div>
									<a href="<c:url value="/board/free/${post.seq}"/>">${post.subject}</a> |
									&nbsp;<span class="glyphicon glyphicon-user"></span> ${post.writer.username}
									&nbsp;<span class="glyphicon glyphicon-time"></span> {{dateFromObjectId("${post.id}") | date:"${dateTimeFormat.dateTime}"}}
								</div>
							</c:forEach>
						</li>
					</ul>

					<div class="ladda-btn margin-bottom-10">
						<div class="row">
							<div class="col-xs-6">
								<button class="btn-u btn-brd btn-brd-hover rounded btn-u-blue btn-u-sm ladda-button" type="button"
									ng-click="btnFeeling('LIKE')" ng-init="numberOfLike=${fn:length(post.usersLiking)}"
									ladda="btnLike" data-style="expand-right" data-spinner-color="Gainsboro">
									<i class="fa fa-thumbs-o-up fa-lg"></i>
									<span ng-hide="likeConn == 'connecting'">{{numberOfLike}}</span>
								</button>
								<button class="btn-u btn-brd btn-brd-hover rounded btn-u-red btn-u-sm ladda-button" type="button"
									ng-click="btnFeeling('DISLIKE')" ng-init="numberOfDislike=${fn:length(post.usersDisliking)}"
									ladda="btnDislike" data-style="expand-right" data-spinner-color="Gainsboro">
									<i class="fa fa-thumbs-o-down fa-lg"></i>
									<span ng-hide="dislikeConn == 'connecting'">{{numberOfDislike}}</span>
								</button>
							</div>
							<div class="col-xs-6 text-right">
								<a id="kakao-link-btn" href="javascript:;">
									<img src="<%=request.getContextPath()%>/resources/kakao/icon/kakaolink_btn_small.png"/>
								</a>
							</div>
						</div>
					</div>
					<div class="alert {{feelingAlert.classType}}" role="alert" ng-show="feelingAlert.msg">
						<span class="glyphicon {{feelingAlert.icon}}" aria-hidden="true"></span>
						{{feelingAlert.msg}}
					</div>
				</div>

				<hr/>

				<div class="margin-bottom-10">
					<button type="button" class="btn-u btn-brd rounded" onclick="location.href='<c:url value="/gallery"/>'">
						<i class="fa fa-th"></i></button>

					<c:choose>
						<c:when test="${!empty prev}">
							<button type="button" class="btn-u btn-brd rounded" onclick="location.href='
								<c:url value="/gallery/view/${prev.id}"/>'"><i class="fa fa-chevron-left"></i></button>
						</c:when>
						<c:otherwise>
							<button type="button" class="btn-u btn-brd rounded btn-u-default disabled" disabled="disabled">
								<i class="fa fa-chevron-left text-muted"></i></button>
						</c:otherwise>
					</c:choose>
					<c:choose>
						<c:when test="${!empty next}">
							<button type="button" class="btn-u btn-brd rounded" onclick="location.href='
								<c:url value="/gallery/view/${next.id}"/>'"><i class="fa fa-chevron-right"></i></button>
						</c:when>
						<c:otherwise>
							<button type="button" class="btn-u btn-brd rounded btn-u-default disabled" disabled="disabled">
								<i class="fa fa-chevron-right text-muted"></i></button>
						</c:otherwise>
					</c:choose>
				</div>

			</div>

			<jsp:include page="../include/footer.jsp"/>
		</div><!-- /.container -->

		<script src="<%=request.getContextPath()%>/bundles/gallery.view.js"></script>
		<script type="text/javascript">
			var jakdukApp = angular.module("jakdukApp", ["angular-ladda", 'jakdukCommon']);

			jakdukApp.controller("galleryController", function ($scope, $http) {
				$scope.feelingAlert = {};
				$scope.likeConn = "none";
				$scope.dislikeConn = "none";
				$scope.galleryName = document.getElementById("galleryName").value;

				angular.element(document).ready(function () {
					App.init();

					// 사용할 앱의 Javascript 키를 설정해 주세요.
					Kakao.init('${kakaoKey}');

					var label = $scope.galleryName + '\r<spring:message code="gallery"/> · <spring:message code="common.jakduk"/>';

					// 카카오톡 링크 버튼을 생성합니다. 처음 한번만 호출하면 됩니다.
					Kakao.Link.createTalkLinkButton({
						container: '#kakao-link-btn',
						label: label,
						image: {
							src: 'https://jakduk.com/gallery/thumbnail/${gallery.id}',
							width: '360',
							height: '230'
						},
						webLink: {
							text: "https://jakduk.com/gallery/view/${gallery.id}",
							url: "https://jakduk.com/gallery/view//${gallery.id}"
						}
					});
				});

				$scope.btnFeeling = function (type) {

					if ("${authRole}" == "ANNONYMOUS") {
						$scope.feelingAlert.msg = '<spring:message code="gallery.msg.need.login.for.feel"/>';
						$scope.feelingAlert.icon = "glyphicon-warning-sign";
						$scope.feelingAlert.classType = "alert-warning";
						return;
					} else if ("${accountId}" == "${post.writer.userId}") {
						$scope.feelingAlert.msg = '<spring:message code="gallery.msg.you.are.writer"/>';
						$scope.feelingAlert.icon = "glyphicon-warning-sign";
						$scope.feelingAlert.classType = "alert-warning";
						return;
					}

					var bUrl = '<c:url value="/gallery/${gallery.id}/' + type + '.json"/>';

					if ($scope.likeConn == "none" && $scope.dislikeConn == "none") {

						var reqPromise = $http.get(bUrl);

						if (type == "like") {
							$scope.likeConn = "connecting";
							$scope.btnLike = true;
						} else if (type == "dislike") {
							$scope.dislikeConn = "connecting";
							$scope.btnDislike = true;
						}

						reqPromise.success(function (data, status, headers, config) {
							var message = "";
							var icon = "";
							var mType = "";

							if (data.errorCode == "like") {
								message = '<spring:message code="gallery.msg.select.like"/>';
								icon = "glyphicon-thumbs-up";
								mType = "alert-success";
								$scope.numberOfLike = data.numberOfLike;
							} else if (data.errorCode == "dislike") {
								message = '<spring:message code="gallery.msg.select.dislike"/>';
								icon = "glyphicon-thumbs-down";
								mType = "alert-success";
								$scope.numberOfDislike = data.numberOfDislike;
							} else if (data.errorCode == "already") {
								message = '<spring:message code="gallery.msg.select.already.like"/>';
								icon = "glyphicon-warning-sign";
								mType = "alert-warning";
							} else if (data.errorCode == "anonymous") {
								message = '<spring:message code="gallery.msg.need.login.for.feel"/>';
								icon = "glyphicon-warning-sign";
								mType = "alert-warning";
							} else if (data.errorCode == "writer") {
								message = '<spring:message code="gallery.msg.you.are.writer"/>';
								icon = "glyphicon-warning-sign";
								mType = "alert-warning";
							}

							$scope.feelingAlert.msg = message;
							$scope.feelingAlert.icon = icon;
							$scope.feelingAlert.classType = mType;

							if (type == "like") {
								$scope.likeConn = "success";
								$scope.btnLike = false;
							} else if (type == "dislike") {
								$scope.dislikeConn = "success";
								$scope.btnDislike = false;
							}

						});
						reqPromise.error(function (data, status, headers, config) {
							$scope.feelingAlert.msg = '<spring:message code="common.msg.error.network.unstable"/>';
							$scope.feelingAlert.icon = "glyphicon-exclamation-sign";
							$scope.feelingAlert.classType = "alert-danger";

							if (type == "like") {
								$scope.likeConn = "none";
								$scope.btnLike = false;
							} else if (type == "dislike") {
								$scope.dislikeConn = "none";
								$scope.btnDislike = false;
							}
						});
					}
				};

				$scope.objectIdFromDate = function (date) {
					return Math.floor(date.getTime() / 1000).toString(16) + "0000000000000000";
				};

				$scope.dateFromObjectId = function (objectId) {
					return new Date(parseInt(objectId.substring(0, 8), 16) * 1000);
				};

				$scope.intFromObjectId = function (objectId) {
					return parseInt(objectId.substring(0, 8), 16) * 1000;
				};

			});
		</script>

	</body>

</html>