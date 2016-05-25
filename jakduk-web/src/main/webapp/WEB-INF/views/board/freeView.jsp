<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<!DOCTYPE html>
<!--[if IE 9]> <html lang="ko" class="ie9" ng-app="jakdukApp"> <![endif]-->
<!--[if !IE]><!-->
<html lang="ko" ng-app="jakdukApp"> <!--<![endif]-->
	<head>
		<title>
			${post.subject} - <spring:message code="board.name.free"/> &middot;
			<spring:message code="common.jakduk"/>
		</title>
		<link href='https://jakduk.com/board/free/${post.seq}' rel='canonical'/>
		<jsp:include page="../include/html-header.jsp"/>
		<link rel="stylesheet" href="<%=request.getContextPath()%>/bundles/board.view.css">
	</head>

	<body class="header-fixed">

		<c:set var="summernoteLang" value="en-US"/>

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

		<c:url var="listUrl" value="/board/free">
			<c:if test="${!empty listInfo.page}">
				<c:param name="page" value="${listInfo.page}"/>
			</c:if>
			<c:if test="${!empty listInfo.category}">
				<c:param name="category" value="${listInfo.category}"/>
			</c:if>
		</c:url>

		<c:if test="${!empty prev}">
			<c:url var="prevUrl" value="/board/free/${prev.seq}">
				<c:if test="${!empty listInfo.page}">
					<c:param name="page" value="${listInfo.page}"/>
				</c:if>
				<c:if test="${!empty listInfo.category}">
					<c:param name="category" value="${listInfo.category}"/>
				</c:if>
			</c:url>
		</c:if>

		<c:if test="${!empty next}">
			<c:url var="nextUrl" value="/board/free/${next.seq}">
				<c:if test="${!empty listInfo.page}">
					<c:param name="page" value="${listInfo.page}"/>
				</c:if>
				<c:if test="${!empty listInfo.category}">
					<c:param name="category" value="${listInfo.category}"/>
				</c:if>
			</c:url>
		</c:if>

		<div class="wrapper">
			<jsp:include page="../include/navigation-header.jsp"/>

			<!--=== Breadcrumbs ===-->
			<div class="breadcrumbs">
				<div class="container">
					<h1 class="pull-left">
						<a href="<c:url value="/board/free/refresh"/>"><spring:message code="board.name.free"/></a></h1>
					<ul class="pull-right breadcrumb">
						<li><a href="<c:url value="/board/free/posts"/>"><spring:message code="board.free.breadcrumbs.posts"/></a>
						</li>
						<li>
							<a href="<c:url value="/board/free/comments"/>"><spring:message code="board.free.breadcrumbs.comments"/></a>
						</li>
					</ul>
				</div><!--/container-->
			</div><!--/breadcrumbs-->
			<!--=== End Breadcrumbs ===-->

			<!--=== Content Part ===-->
			<div class="container content blog-page blog-item">

				<!-- Buttons -->
				<div class="margin-bottom-10">
					<button type="button" class="btn-u btn-brd rounded" onclick="location.href='${listUrl}'"
						tooltip-popup-close-delay='300' uib-tooltip='<spring:message code="board.list"/>'>
						<i class="fa fa-list"></i>
					</button>

					<c:choose>
						<c:when test="${!empty prevUrl}">
							<button type="button" class="btn-u btn-brd rounded" onclick="location.href='${prevUrl}'"
								tooltip-popup-close-delay='300' uib-tooltip='<spring:message code="common.button.prev"/>'>
								<i class="fa fa-chevron-left"></i>
							</button>
						</c:when>
						<c:otherwise>
							<button type="button" class="btn-u btn-brd rounded btn-u-default disabled" disabled="disabled">
								<i class="fa fa-chevron-left text-muted"></i></button>
						</c:otherwise>
					</c:choose>
					<c:choose>
						<c:when test="${!empty nextUrl}">
							<button type="button" class="btn-u btn-brd rounded" onclick="location.href='${nextUrl}'"
								tooltip-popup-close-delay='300' uib-tooltip='<spring:message code="common.button.next"/>'>
								<i class="fa fa-chevron-right"></i>
							</button>
						</c:when>
						<c:otherwise>
							<button type="button" class="btn-u btn-brd rounded btn-u-default disabled" disabled="disabled">
								<i class="fa fa-chevron-right text-muted"></i></button>
						</c:otherwise>
					</c:choose>

					<c:if test="${authRole != 'ANNONYMOUS' && accountId == post.writer.userId}">
						<button type="button" class="btn-u rounded" onclick="confirmEdit();">
							<i class="fa fa-pencil-square-o"></i> <spring:message code="common.button.edit"/>
						</button>
						<button type="button" class="btn-u btn-u-default rounded" onclick="confirmDelete();">
							<i class="fa fa-trash-o"></i> <spring:message code="common.button.delete"/>
						</button>
					</c:if>

					<c:choose>
						<c:when test="${authAdminRole == 'ROOT' && post.status.notice != 'notice' && post.status.delete != 'delete'}">
							<button type="button" class="btn-u rounded" onclick="location.href='<c:url value="/board/notice/set/${post.seq}"/>'">
								<spring:message code="common.button.set.as.notice"/>
							</button>
						</c:when>
						<c:when test="${authAdminRole == 'ROOT' && post.status.notice == 'notice' && post.status.delete != 'delete'}">
							<button type="button" class="btn-u btn-u-default rounded" onclick="location.href='<c:url value="/board/notice/cancel/${post.seq}"/>'">
								<spring:message code="common.button.cancel.notice"/>
							</button>
						</c:when>
					</c:choose>
				</div> <!-- End Buttons -->

				<c:choose>
					<c:when test="${result == 'setNotice'}">
						<div class="contex-bg"><p class="bg-success rounded"><spring:message code="board.msg.set.as.notice"/></p>
						</div>
					</c:when>
					<c:when test="${result == 'cancelNotice'}">
						<div class="contex-bg"><p class="bg-success rounded"><spring:message code="board.msg.cancel.notice"/></p>
						</div>
					</c:when>
					<c:when test="${result == 'alreadyNotice'}">
						<div class="contex-bg"><p class="bg-danger rounded">
							<spring:message code="board.msg.error.already.notice"/></p></div>
					</c:when>
					<c:when test="${result == 'alreadyNotNotice'}">
						<div class="contex-bg"><p class="bg-danger rounded">
							<spring:message code="board.msg.error.already.not.notice"/></p></div>
					</c:when>
					<c:when test="${result == 'existComment'}">
						<div class="contex-bg"><p class="bg-danger rounded">
							<spring:message code="board.msg.error.can.not.delete.post"/></p></div>
					</c:when>
					<c:when test="${result == 'emptyComment'}">
						<div class="contex-bg"><p class="bg-danger rounded">
							<spring:message code="board.msg.error.can.not.delete.post.except.comment"/></p></div>
					</c:when>
				</c:choose>

				<!--Blog Post-->
				<div class="blog margin-bottom-20" ng-controller="boardFreeCtrl">
					<input type="hidden" id="subject" value="${post.subject}">
					<h2>
						<small>
							<c:if test="${post.status.device == 'mobile'}"><span aria-hidden="true" class=" icon-screen-smartphone"></span></c:if>
							<c:if test="${post.status.device == 'tablet'}"><span aria-hidden="true" class=" icon-screen-tablet"></span></c:if>
							<c:if test="${galleries != null}"><span aria-hidden="true" class="icon-picture"></span></c:if>
						</small>
						<c:choose>
							<c:when test="${post.status.delete == 'delete'}">
								<spring:message code="board.msg.deleted"/>
							</c:when>
							<c:otherwise>
								${post.subject}
							</c:otherwise>
						</c:choose>
						<c:if test="${!empty category}">
							<small>
								<span aria-hidden="true" class="icon-directions"></span><spring:message code="${category.resName}"/>
							</small>
						</c:if>
					</h2>
					<div class="blog-post-tags">
						<ul class="list-unstyled list-inline blog-info">
							<li><span aria-hidden="true" class="icon-user"></span> ${post.writer.username}</li>
							<li ng-bind="dateFromObjectId('${post.id}') | date:'${dateTimeFormat.dateTime}'"></li>
							<li><span aria-hidden="true" class="icon-eye"></span> ${post.views}</li>
						</ul>
					</div>

					<c:choose>
						<c:when test="${post.status.delete == 'delete'}">
							<p><spring:message code="board.msg.deleted"/></p>
						</c:when>
						<c:otherwise>
							<p>${post.content}</p>
						</c:otherwise>
					</c:choose>

					<!-- galleries -->
					<c:if test="${galleries != null}">
						<ul class="list-group">
							<li class="list-group-item">
								<strong><spring:message code="board.gallery.list"/></strong>
								<c:forEach items="${galleries}" var="gallery">
									<div>
										<span aria-hidden="true" class="icon-paper-clip"></span>
										<a href="<c:url value="/gallery/view/${gallery.id}"/>">${gallery.name}</a> |
										<fmt:formatNumber value="${gallery.size/1024}" pattern=".00"/> KB
									</div>
								</c:forEach>
							</li>
						</ul>
					</c:if>

					<!-- buttons -->
					<div class="ladda-btn margin-bottom-10">
						<div class="row">
							<div class="col-xs-6">
								<button type="button" class="btn-u btn-u-sm btn-u-blue rounded ladda-button"
									ng-click="btnFeeling('LIKE')" ng-init="numberOfLike=${fn:length(post.usersLiking)}"
									ladda="btnLike" data-style="expand-right" data-spinner-color="Gainsboro"
									tooltip-popup-close-delay='300' uib-tooltip='<spring:message code="common.button.like"/>'>
									<i class="fa fa-thumbs-o-up fa-lg"></i>
									<span ng-hide="likeConn == 'connecting'" ng-bind="numberOfLike"></span>
								</button>
								<button type="button" class="btn-u btn-u-sm btn-u-red rounded ladda-button"
									ng-click="btnFeeling('DISLIKE')" ng-init="numberOfDislike=${fn:length(post.usersDisliking)}"
									ladda="btnDislike" data-style="expand-right" data-spinner-color="Gainsboro"
									tooltip-popup-close-delay='300' uib-tooltip='<spring:message code="common.button.dislike"/>'>
									<i class="fa fa-thumbs-o-down fa-lg"></i>
									<span ng-hide="dislikeConn == 'connecting'" ng-bind="numberOfDislike"></span>
								</button>
							</div>
							<div class="col-xs-6 text-right">
								<button class="btn btn-u-sm rounded btn-android" type="button" ng-click="btnUrlCopy()"
									tooltip-popup-close-delay='300' uib-tooltip='<spring:message code="common.msg.copy.to.clipboard"/>'>
									<i class="icon-link"></i>
								</button>
								<a id="kakao-link-btn" href="javascript:;"
									tooltip-popup-close-delay='300' uib-tooltip='<spring:message code="common.msg.send.to.kakao"/>'>
									<img src="<%=request.getContextPath()%>/resources/kakao/icon/kakaolink_btn_small.png"/>
								</a>
							</div>
						</div>
					</div>

					<div class="alert fade in rounded ng-cloak" ng-class="alert.classType" ng-show="alert.msg">
						<span ng-bind="alert.msg"></span>
						<a class="alert-link" ng-href="{{alert.linkUrl}}" ng-show="alert.linkUrl" ng-bind="alert.linkLabel"></a>
					</div>

				</div> <!--End Blog Post-->

				<hr class="padding-10"/>

				<!-- comment -->
				<div ng-controller="commentCtrl">

					<input type="hidden" id="commentCount" value="{{commentCount}}">

					<!-- 댓글 목록  -->
					<div class="media margin-bottom-10">

						<h2 class="heading-sm text-primary">
							<i class="fa fa-comments"></i>
							<span infinite-scroll="initComment()" infinite-scroll-disabled="infiniteDisabled">
								<spring:message code="board.msg.comment.count" arguments="<span ng-bind=\"commentCount\"></span>"/>
							</span>
							<button type="button" class="btn btn-link" ng-click="btnRefreshComment()"
								tooltip-popup-close-delay='300' uib-tooltip='<spring:message code="common.button.refresh.comments"/>'>
								<i class="fa fa-refresh text-muted" ng-class="{'fa-spin':loadCommentConn == 'connecting'}"></i>
							</button>
						</h2>

						<div class="media-body ng-cloak">
							<div ng-repeat="comment in comments">
								<h6 class="clearfix">
									<i aria-hidden="true" class="icon-user"></i>
									<span ng-bind="comment.writer.username"></span>
									<span class="pull-right" ng-bind="dateFromObjectId(comment.id) | date:'${dateTimeFormat.dateTime}'"></span>
								</h6>
								<p>
									<span aria-hidden="true" class="icon-screen-smartphone" ng-if="comment.status.device == 'mobile'"></span>
									<span aria-hidden="true" class="icon-screen-tablet" ng-if="comment.status.device == 'tablet'"></span>
									<span ng-bind-html="comment.content"></span>
								</p>

								<button type="button" class="btn btn-xs rounded btn-dropbox" ng-click="btnCommentFeeling(comment.id, 'LIKE')"
									tooltip-popup-close-delay='300' uib-tooltip='<spring:message code="common.button.like"/>'>
									<span ng-init="numberOfCommentLike[comment.id]=comment.usersLiking.length">
										<i class="fa fa-thumbs-o-up fa-lg"></i>
										<span ng-bind="numberOfCommentLike[comment.id]"></span>
									</span>
								</button>
								<button type="button" class="btn btn-xs rounded btn-weibo" ng-click="btnCommentFeeling(comment.id, 'DISLIKE')"
									tooltip-popup-close-delay='300' uib-tooltip='<spring:message code="common.button.dislike"/>'>
									<span ng-init="numberOfCommentDislike[comment.id]=comment.usersDisliking.length">
										<i class="fa fa-thumbs-o-down fa-lg"></i>
										<span ng-bind="numberOfCommentDislike[comment.id]"></span>
									</span>
								</button>
								<div class="text-danger" ng-show="commentFeelingConn[comment.id]" ng-bind="commentFeelingAlert[comment.id]"></div>
								<hr class="padding-5">
							</div>

							<div class="margin-bottom-10" ng-show="commentCount || commentAlert.msg">
								<button type="button" class="btn-u btn-brd rounded btn-block btn-u-dark"
									ng-click="btnMoreComment()" ng-show="commentCount">
									<spring:message code="common.button.more.comments"/> <i class="fa fa-angle-down"></i>
									<i class="fa fa-circle-o-notch fa-spin" ng-show="loadCommentConn == 'connecting'"></i>
								</button>
							</div>

							<div class="contex-bg" ng-show="commentAlert.msg">
								<p class="{{commentAlert.classType}} rounded" ng-bind="commentAlert.msg"></p>
							</div>
						</div>
					</div>

					<!-- 댓글 남기기 -->
					<div class="post-comment">
						<h4 class="text-primary"><spring:message code="board.comment.leave.comment"/></h4>
						<div class="margin-bottom-10">
							<summernote config="options" on-keyUp="focus(evt)"
								ng-model="summernote.contents" ng-init="summernote={contents:'', seq:'${post.seq}'}"></summernote>
							<span class="{{summernoteAlert.classType}}" ng-show="summernoteAlert.msg" ng-bind="summernoteAlert.msg"></span>
						</div>

						<div class="margin-bottom-10">
							<c:choose>
								<c:when test="${authRole == 'ANNONYMOUS'}">
									<button type="button" class="btn-u btn-brd rounded btn-u-default disabled" disabled="disabled">
										<span aria-hidden="true" class="icon-pencil"></span>
										<spring:message code="common.button.write.comment"/>
									</button>
								</c:when>
								<c:when test="${authRole == 'USER'}">
									<button type="button" class="btn-u btn-brd rounded btn-u-sm ladda-button"
										ng-click="btnWriteComment()"
										ladda="writeComment" data-style="expand-right" data-spinner-color="Gainsboro">
										<span aria-hidden="true" class="icon-pencil"></span>
										<spring:message code="common.button.write.comment"/>
									</button>
								</c:when>
							</c:choose>
							<span ng-bind="summernote.contents.length"></span> / <span ng-bind="boardCommentContentLengthMax"></span>
						</div>
						<p ng-show="writeCommentAlert.msg" ng-class="writeCommentAlert.classType" ng-bind="writeCommentAlert.msg"></p>
					</div>

				</div> <!-- End Comment -->

				<!-- Buttons -->
				<div class="margin-bottom-10">
					<button type="button" class="btn-u btn-brd rounded" onclick="location.href='${listUrl}'"
						tooltip-popup-close-delay='300' uib-tooltip='<spring:message code="board.list"/>'>
						<i class="fa fa-list"></i>
					</button>

					<c:choose>
						<c:when test="${!empty prevUrl}">
							<button type="button" class="btn-u btn-brd rounded" onclick="location.href='${prevUrl}'"
								tooltip-popup-close-delay='300' uib-tooltip='<spring:message code="common.button.prev"/>'>
								<i class="fa fa-chevron-left"></i>
							</button>
						</c:when>
						<c:otherwise>
							<button type="button" class="btn-u btn-brd rounded btn-u-default disabled" disabled="disabled">
								<i class="fa fa-chevron-left text-muted"></i></button>
						</c:otherwise>
					</c:choose>
					<c:choose>
						<c:when test="${!empty nextUrl}">
							<button type="button" class="btn-u btn-brd rounded" onclick="location.href='${nextUrl}'"
								tooltip-popup-close-delay='300' uib-tooltip='<spring:message code="common.button.next"/>'>
								<i class="fa fa-chevron-right"></i>
							</button>
						</c:when>
						<c:otherwise>
							<button type="button" class="btn-u btn-brd rounded btn-u-default disabled" disabled="disabled">
								<i class="fa fa-chevron-right text-muted"></i></button>
						</c:otherwise>
					</c:choose>

					<c:if test="${authRole != 'ANNONYMOUS' && accountId == post.writer.userId}">
						<button type="button" class="btn-u rounded" onclick="confirmEdit();">
							<i class="fa fa-pencil-square-o"></i> <spring:message code="common.button.edit"/>
						</button>
						<button type="button" class="btn-u btn-u-default rounded" onclick="confirmDelete();">
							<i class="fa fa-trash-o"></i> <spring:message code="common.button.delete"/>
						</button>
					</c:if>

					<c:choose>
						<c:when test="${authAdminRole == 'ROOT' && post.status.notice != 'notice' && post.status.delete != 'delete'}">
							<button type="button" class="btn-u rounded" onclick="location.href='<c:url value="/board/notice/set/${post.seq}"/>'">
								<spring:message code="common.button.set.as.notice"/>
							</button>
						</c:when>
						<c:when test="${authAdminRole == 'ROOT' && post.status.notice == 'notice' && post.status.delete != 'delete'}">
							<button type="button" class="btn-u btn-u-default rounded" onclick="location.href='<c:url value="/board/notice/cancel/${post.seq}"/>'">
								<spring:message code="common.button.cancel.notice"/>
							</button>
						</c:when>
					</c:choose>
				</div> <!-- End Buttons -->

			</div>  <!-- End Content Part -->

			<jsp:include page="../include/footer.jsp"/>
		</div> <!-- /.container -->

		<script src="<%=request.getContextPath()%>/bundles/board.view.js"></script>
		<c:if test="${fn:contains('ko', pageContext.response.locale.language)}">
			<script src="<%=request.getContextPath()%>/resources/summernote/lang/summernote-ko-KR.js"></script>
			<c:set var="summernoteLang" value="ko-KR"/>
		</c:if>
		<script type="text/javascript">

			var jakdukApp = angular.module("jakdukApp", ["summernote", "infinite-scroll", "ngSanitize", "angular-ladda", 'ui.bootstrap', 'jakdukCommon']);

			jakdukApp.controller("boardFreeCtrl", function ($scope, $http) {
				$scope.alert = {};
				$scope.likeConn = "none";
				$scope.dislikeConn = "none";
				$scope.subject = document.getElementById("subject").value;

				angular.element(document).ready(function () {

					// 사용할 앱의 Javascript 키를 설정해 주세요.
					Kakao.init('${kakaoKey}');

					var label = $scope.subject + '\r<spring:message code="board.name.free"/> · <spring:message code="common.jakduk"/>';

					var kakaoLinkContent = {};
					kakaoLinkContent.container = '#kakao-link-btn';
					kakaoLinkContent.label = label;

					if (!Jakduk.isEmpty("${galleries}")) {
						kakaoLinkContent.image = {
							src: 'https://jakduk.com/gallery/${galleries[0].id}',
							width: '300',
							height: '200'
						};
					}

					kakaoLinkContent.webLink = {
						text: 'https://jakduk.com/board/free/${post.seq}',
						url: 'https://jakduk.com/board/free/${post.seq}'
					};

					// 카카오톡 링크 버튼을 생성합니다. 처음 한번만 호출하면 됩니다.
					Kakao.Link.createTalkLinkButton(kakaoLinkContent);
				});

				$scope.objectIdFromDate = function (date) {
					return Math.floor(date.getTime() / 1000).toString(16) + "0000000000000000";
				};

				$scope.dateFromObjectId = function (objectId) {
					return new Date(parseInt(objectId.substring(0, 8), 16) * 1000);
				};

				// 글 감정 표현.
				$scope.btnFeeling = function (type) {
					if ("${authRole}" == "ANNONYMOUS") {
						$scope.alert.msg = '<spring:message code="board.msg.need.login.for.feel"/>';
						$scope.alert.linkUrl = "<c:url value='/login'/>";
						$scope.alert.linkLabel = '<spring:message code="common.button.login"/>';
						$scope.alert.classType = "alert-warning";
						return;
					} else if ("${accountId}" == "${post.writer.userId}") {
						$scope.alert.msg = "<spring:message code='board.msg.you.are.writer'/>";
						$scope.alert.classType = "alert-warning";
						return;
					}

					var bUrl = '<c:url value="/api/board/free/${post.seq}/' + type + '"/>';

					if ($scope.likeConn == "none" && $scope.dislikeConn == "none") {

						var reqPromise = $http.post(bUrl);

						if (type == "LIKE") {
							$scope.likeConn = "connecting";
							$scope.btnLike = true;
						} else if (type == "DISLIKE") {
							$scope.dislikeConn = "connecting";
							$scope.btnDislike = true;
						}

						reqPromise.success(function (data, status, headers, config) {
							$scope.numberOfLike = data.numberOfLike;
							$scope.numberOfDislike = data.numberOfDislike;

							var message = "";

							if (data.feeling == 'LIKE') {
								message = '<spring:message code="board.msg.select.like"/>';

								$scope.likeConn = "success";
								$scope.btnLike = false;
							} else if (data.feeling == 'DISLIKE') {
								message = "<spring:message code='board.msg.select.dislike'/>";

								$scope.dislikeConn = "success";
								$scope.btnDislike = false;
							}

							$scope.alert.msg = message;
							$scope.alert.classType = "alert-success";
						});
						reqPromise.error(function (data, status, headers, config) {
							$scope.alert.msg = data.message;
							$scope.alert.classType = "alert-danger";

							if (type == "LIKE") {
								$scope.likeConn = "none";
								$scope.btnLike = false;
							} else if (type == "DISLIKE") {
								$scope.dislikeConn = "none";
								$scope.btnDislike = false;
							}
						});
					}
				};

				$scope.btnUrlCopy = function () {
					var url = "https://jakduk.com/board/free/${post.seq}";

					if (window.clipboardData) {
						// IE처리
						// 클립보드에 문자열 복사
						window.clipboardData.setData('text', url);

						// 클립보드의 내용 가져오기
						// window.clipboardData.getData('Text');

						// 클립보드의 내용 지우기
						// window.clipboardData.clearData("Text");
					} else {
						// 비IE 처리
						window.prompt('<spring:message code="common.msg.copy.to.clipboard"/>', url);
					}
				};
			});


			jakdukApp.controller("commentCtrl", function ($scope, $http) {
				$scope.boardCommentContentLengthMin = Jakduk.BoardCommentContentLengthMin;
				$scope.boardCommentContentLengthMax = Jakduk.BoardCommentContentLengthMax;

				$scope.comments = [];
				$scope.commentAlert = {};
				$scope.summernoteAlert = {};
				$scope.commentFeelingConn = {};
				$scope.commentFeelingAlert = {};
				$scope.numberOfCommentLike = {};
				$scope.numberOfCommentDislike = {};
				$scope.loadCommentConn = "none";
				$scope.writeCommentConn = "none";
				$scope.writeCommentAlert = {};
				$scope.infiniteDisabled = false;

				angular.element(document).ready(function () {
				});

				// summernote config
				$scope.options = {
					height: 0,
					//placeholder: '<spring:message code="board.msg.write.text.here"/>',
					lang: "${summernoteLang}",
					toolbar: [
						['font', ['bold']],
						// ['fontsize', ['fontsize']], // Still buggy
						['color', ['color']],
						['insert', ['link']],
						['help', ['help']]
					]
				};

				$scope.focus = function (e) {
					if ("${authRole}" == "ANNONYMOUS") {
						if (confirm('<spring:message code="board.msg.need.login.for.write"/>') == true) {
							location.href = "<c:url value='/login'/>";
						}
					}
				};

				if ("${authRole}" == "ANNONYMOUS") {
					$scope.summernoteAlert = {
						"classType": "text-danger",
						"msg": '<spring:message code="board.msg.need.login.for.write"/>'
					};
				}

				$scope.objectIdFromDate = function (date) {
					return Math.floor(date.getTime() / 1000).toString(16) + "0000000000000000";
				};

				$scope.dateFromObjectId = function (objectId) {
					return new Date(parseInt(objectId.substring(0, 8), 16) * 1000);
				};

				$scope.intFromObjectId = function (objectId) {
					return parseInt(objectId.substring(0, 8), 16) * 1000;
				};

				// 댓글 달기
				$scope.btnWriteComment = function (status) {
					var bUrl = '<c:url value="/api/board/free/comment"/>';

					if ($scope.summernote.contents.length < Jakduk.BoardCommentContentLengthMin
						|| $scope.summernote.contents.length > Jakduk.BoardCommentContentLengthMax) {
						$scope.summernoteAlert = {
							"classType": "text-danger",
							"msg": '<spring:message code="Size.board.comment.content"/>'
						};
						return;
					}

					if ($scope.writeCommentConn == "none") {
						var reqPromise = $http.post(bUrl, $scope.summernote);
						$scope.writeCommentConn = "connecting";
						$scope.writeComment = true;

						reqPromise.success(function (data, status, headers, config) {
							$scope.btnMoreComment();

							$scope.summernote.contents = "";
							$scope.commentAlert = {};
							$scope.summernoteAlert = {};
							$scope.writeCommentAlert = {};
							$scope.writeCommentConn = "none";
							$scope.writeComment = false;
						});
						reqPromise.error(function (data, status, headers, config) {
							$scope.writeCommentAlert = {"classType":"text-danger", "msg":data.message};
							$scope.writeCommentConn = "none";
							$scope.writeComment = false;
						});
					}
				};

				// infinite 초기 설정
				$scope.initComment = function () {
					$scope.loadComments("init", "");
					$scope.infiniteDisabled = true;
				};

				// 댓글 목록 가져오기.
				$scope.loadComments = function (type, commentId) {
					var bUrl = '<c:url value="/api/board/free/comments/${post.seq}?commentId=' + commentId + '"/>';

					if ($scope.loadCommentConn == "none") {
						var reqPromise = $http.get(bUrl);

						$scope.loadCommentConn = "connecting";

						reqPromise.success(function (data, status, headers, config) {

							$scope.commentCount = data.count;

							if (data.comments.length < 1) { // 댓글이 하나도 없을때
								if (type == "init") {
								} else {
									$scope.commentAlert.msg = '<spring:message code="board.msg.there.is.no.new.comment"/>';
									$scope.commentAlert.classType = "bg-warning";
								}
							} else {	// 댓글을 1개 이상 가져왔을 때
								if (type == "init" || type == "btnRefreshComment") {
									$scope.comments = data.comments;
								} else if (type == "btnMoreComment" || type == "btnWriteComment") {
									$scope.comments = $scope.comments.concat(data.comments);
								}
							}

							$scope.loadCommentConn = "none";
						});
						reqPromise.error(function (data, status, headers, config) {
							$scope.loadCommentConn = "none";
						});
					}
				};

				// 댓글 더보기 단추.
				$scope.btnMoreComment = function () {
					if ($scope.comments.length > 0) {
						var lastComment = $scope.comments[$scope.comments.length - 1];
						$scope.loadComments("btnMoreComment", lastComment.id);
					} else {
						$scope.loadComments("btnMoreComment", "");
					}
				};

				// 댓글 새로고침 단추.
				$scope.btnRefreshComment = function () {
					$scope.commentAlert = {};
					$scope.comments = [];
					$scope.loadComments("btnRefreshComment", "");
				};

				// 댓글 감정 표현
				$scope.btnCommentFeeling = function (commentId, status) {
					var bUrl = '<c:url value="/api/board/comment/' + commentId + '/' + status + '"/>';
					var conn = $scope.commentFeelingConn[commentId];

					if (conn == "none" || conn == null) {
						var reqPromise = $http.post(bUrl);

						$scope.commentFeelingConn[commentId] = "loading";

						reqPromise.success(function (data, status, headers, config) {

							if (data.feeling == 'LIKE') {
								$scope.numberOfCommentLike[commentId] = data.numberOfLike;
							} else if (data.feeling == 'DISLIKE') {
								$scope.numberOfCommentDislike[commentId] = data.numberOfDislike;
							}

							$scope.commentFeelingAlert[commentId] = '';
							$scope.commentFeelingConn[commentId] = "ok";

						});
						reqPromise.error(function (data, status, headers, config) {
							$scope.commentFeelingConn[commentId] = "none";
							$scope.commentFeelingAlert[commentId] = data.message;
						});
					}
				};
			});

			function confirmDelete() {
				var commentCount = document.getElementById("commentCount").value;

				if (commentCount > 0) {
					if (confirm('<spring:message code="board.msg.confirm.delete.post.except.comment"/>') == true) {
						location.href = '<c:url value="/board/free/delete/${post.seq}?type=postonly"/>';
					}
				} else {
					if (confirm('<spring:message code="board.msg.confirm.delete.post"/>') == true) {
						location.href = '<c:url value="/board/free/delete/${post.seq}?type=all"/>';
					}
				}
			}

			function confirmEdit() {
				if (confirm('<spring:message code="board.msg.confirm.edit.post"/>') == true) {
					location.href = '<c:url value="/board/free/edit/${post.seq}"/>';
				}
			}

			$(document).ready(function () {
				App.init();
			});
		</script>
	</body>
</html>