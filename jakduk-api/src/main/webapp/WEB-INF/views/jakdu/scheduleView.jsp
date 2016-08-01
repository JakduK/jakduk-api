<%--
  Created by IntelliJ IDEA.
  User: pyohwan
  Date: 16. 2. 15
  Time: 오후 11:52
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<!DOCTYPE html>
<!--[if IE 9]> <html lang="ko" class="ie9" ng-app="jakdukApp"> <![endif]-->
<!--[if !IE]><!--> <html lang="ko" ng-app="jakdukApp"> <!--<![endif]-->
	<head>
		<title>
			<spring:message code="jakdu.view"/> &middot; <spring:message code="jakdu"/> &middot;
			<spring:message code="common.jakduk"/>
		</title>
		<link href='https://jakduk.com/jakdu/schedule/${id}' rel='canonical' />
		<jsp:include page="../include/html-header.jsp"/>
		<link rel="stylesheet" href="<%=request.getContextPath()%>/bundles/jakdu.css">
	</head>

	<body class="header-fixed">

		<c:set var="summernoteLang" value="en-US"/>

		<sec:authorize access="isAnonymous()">
			<c:set var="authRole" value="ANNONYMOUS"/>
		</sec:authorize>
		<sec:authorize access="hasAnyRole('ROLE_USER_01', 'ROLE_USER_02', 'ROLE_USER_03')">
			<c:set var="authRole" value="USER"/>
		</sec:authorize>

		<div class="wrapper">
				<jsp:include page="../include/navigation-header.jsp"/>

				<!--=== Breadcrumbs ===-->
				<div class="breadcrumbs">
					<div class="container">
						<h1 class="pull-left">
							<a href="<c:url value="/jakdu/schedule/refresh"/>"><spring:message code="jakdu.view"/></a></h1>
					</div><!--/container-->
				</div><!--/breadcrumbs-->
				<!--=== End Breadcrumbs ===-->

				<!--=== Content Part ===-->
				<div class="container content" ng-controller="jakduCtrl">

					<div class="row margin-bottom-30">
						<div class="col-xs-4 content-boxes-v6">
							<i class="rounded-x  icon-sport-011 "></i>
							<h1 class="title-v3-md text-uppercase margin-bottom-10" ng-bind="jakduSchedule.home.name"></h1>
							<p>Home</p>
						</div>
						<div class="col-xs-4">
							<div class="service-block-v1">
								<i class="rounded-x icon-sport-119"></i>
								<h3 class="title-v3-bg text-uppercase" ng-bind="jakduSchedule.score.homeFullTime + ':' + jakduSchedule.score.awayFullTime"></h3>
								<p>Score</p>
							</div>
						</div>
						<div class="col-xs-4 content-boxes-v6">
							<i class="rounded-x  icon-sport-011 "></i>
							<h2 class="title-v3-md text-uppercase margin-bottom-10" ng-bind="jakduSchedule.away.name"></h2>
							<p>Away</p>
						</div>
					</div>

					<div class="row">
						<div class="col-xs-6">
							<div class="panel panel-info margin-bottom-5">
								<div class="panel-heading text-center">
									<h4 class="panel-title">
										<spring:message code="jakdu.expect.home.score"/> : <span ng-bind="myHomeScore"></span>
									</h4>
								</div>
								<div class="panel-body">
									<div class="center-block jakduk-number-pad">
										<div class="jakduk-number-pad-row">
											<label class="btn btn-primary" ng-model="myHomeScore" uib-btn-radio="0">0</label>
											<label class="btn btn-primary" ng-model="myHomeScore" uib-btn-radio="1">1</label>
											<label class="btn btn-primary" ng-model="myHomeScore" uib-btn-radio="2">2</label>
										</div>
										<div class="jakduk-number-pad-row">
											<label class="btn btn-primary" ng-model="myHomeScore" uib-btn-radio="3">3</label>
											<label class="btn btn-primary" ng-model="myHomeScore" uib-btn-radio="4">4</label>
											<label class="btn btn-primary" ng-model="myHomeScore" uib-btn-radio="5">5</label>
										</div>
										<div class="jakduk-number-pad-row">
											<label class="btn btn-primary" ng-model="myHomeScore" uib-btn-radio="6">6</label>
											<label class="btn btn-primary" ng-model="myHomeScore" uib-btn-radio="7">7</label>
											<label class="btn btn-primary" ng-model="myHomeScore" uib-btn-radio="8">8</label>
										</div>
										<div class="jakduk-number-pad-row">
											<label class="btn btn-primary" ng-model="myHomeScore" uib-btn-radio="9">9</label>
											<label class="btn btn-primary" ng-model="myHomeScore" uib-btn-radio="10">10</label>
											<label class="btn btn-primary" ng-model="myHomeScore" uib-btn-radio="11">11</label>
										</div>
									</div>
								</div>
							</div>
						</div>
						<div class="col-xs-6">
							<div class="panel panel-success margin-bottom-5">
								<div class="panel-heading text-center">
									<h4 class="panel-title">
										<spring:message code="jakdu.expect.away.score"/> : <span ng-bind="myAwayScore"></span>
									</h4>
								</div>
								<div class="panel-body">
									<div class="center-block jakduk-number-pad">
										<div class="jakduk-number-pad-row">
											<label class="btn btn-success" ng-model="myAwayScore" uib-btn-radio="0">0</label>
											<label class="btn btn-success" ng-model="myAwayScore" uib-btn-radio="1">1</label>
											<label class="btn btn-success" ng-model="myAwayScore" uib-btn-radio="2">2</label>
										</div>
										<div class="jakduk-number-pad-row">
											<label class="btn btn-success" ng-model="myAwayScore" uib-btn-radio="3">3</label>
											<label class="btn btn-success" ng-model="myAwayScore" uib-btn-radio="4">4</label>
											<label class="btn btn-success" ng-model="myAwayScore" uib-btn-radio="5">5</label>
										</div>
										<div class="jakduk-number-pad-row">
											<label class="btn btn-success" ng-model="myAwayScore" uib-btn-radio="6">6</label>
											<label class="btn btn-success" ng-model="myAwayScore" uib-btn-radio="7">7</label>
											<label class="btn btn-success" ng-model="myAwayScore" uib-btn-radio="8">8</label>
										</div>
										<div class="jakduk-number-pad-row">
											<label class="btn btn-success" ng-model="myAwayScore" uib-btn-radio="9">9</label>
											<label class="btn btn-success" ng-model="myAwayScore" uib-btn-radio="10">10</label>
											<label class="btn btn-success" ng-model="myAwayScore" uib-btn-radio="11">11</label>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>

					<div class="row text-center">
						<c:choose>
							<c:when test="${authRole == 'ANNONYMOUS'}">
								<button type="button" class="btn-u btn-u-blue rounded" onclick="needLogin();">
									<strong>
										<i aria-hidden="true" class="fa fa-hand-scissors-o fa-lg"></i>
										<spring:message code="common.button.go.jakdu"/>
									</strong>
								</button>
							</c:when>
							<c:when test="${authRole == 'USER'}">
								<button type="button" class="btn-u btn-u-blue rounded ladda-button"
										ng-click="btnGoJakdu()"
										ladda="goJakdu" data-style="expand-right" data-spinner-color="Gainsboro">
									<strong>
										<i aria-hidden="true" class="fa fa-hand-scissors-o fa-lg"></i>
										<spring:message code="common.button.go.jakdu"/>
									</strong>
								</button>
							</c:when>
						</c:choose>
						<div class="{{goJakduAlert.classType}}" ng-show="goJakduAlert.msg" ng-bind="goJakduAlert.msg"></div>
					</div>

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
										<span class="pull-right" ng-bind="comment.id | dateFromObjectId | date:dateTimeFormat.dateTime"></span>
									</h6>

									<p>
										<span aria-hidden="true" class="icon-screen-smartphone" ng-if="comment.status.device == 'mobile'"></span>
										<span aria-hidden="true" class="icon-screen-tablet" ng-if="comment.status.device == 'tablet'"></span>
										<span ng-bind-html="comment.contents"></span>
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
											ng-model="summernote.contents" ng-init="summernote={contents:'',id:'${id}'}"></summernote>
								<span class="{{summernoteAlert.classType}}" ng-show="summernoteAlert.msg" ng-bind="summernoteAlert.msg"></span>
							</div>

							<div class="margin-bottom-10">
								<c:choose>
									<c:when test="${authRole == 'ANNONYMOUS'}">
										<button type="button" class="btn-u btn-brd rounded btn-u-default disabled" disabled="disabled">
											<span aria-hidden="true" class="icon-pencil"></span> <spring:message code="common.button.write.comment"/>
										</button>
									</c:when>
									<c:when test="${authRole == 'USER'}">
										<button type="button" class="btn-u btn-brd rounded btn-u-sm ladda-button"
												ng-click="btnWriteComment()"
												ladda="writeComment" data-style="expand-right" data-spinner-color="Gainsboro">
											<span aria-hidden="true" class="icon-pencil"></span> <spring:message code="common.button.write.comment"/>
										</button>
									</c:when>
								</c:choose>
								<span class="ng-cloak">{{summernote.contents.length}} / {{boardCommentContentLengthMax}}</span>
							</div>
							<div>
								<span class="{{writeCommentAlert.classType}}" ng-show="writeCommentAlert.msg" ng-bind="writeCommentAlert.msg"></span>
							</div>
						</div>

					</div> <!-- End Comment -->

				</div> <!--=== End Content Part ===-->

				<jsp:include page="../include/footer.jsp"/>
			</div> <!-- End wrapper -->

		<script src="<%=request.getContextPath()%>/bundles/jakdu.js"></script>
		<c:if test="${fn:contains('ko', pageContext.response.locale.language)}">
			<script src="<%=request.getContextPath()%>/resources/summernote/lang/summernote-ko-KR.js"></script>
			<c:set var="summernoteLang" value="ko-KR"/>
		</c:if>

		<script type="text/javascript">
			angular.module("jakdukApp", ['ui.bootstrap', 'angular-ladda', 'summernote', 'infinite-scroll', 'jakdukCommon', 'ngSanitize'])
					.controller('jakduCtrl', function ($scope, $http) {
						$scope.btnGoJakdu = btnGoJakdu;
						$scope.getDataSchedule = getDataSchedule;

						$scope.dateTimeFormat = JSON.parse('${dateTimeFormat}');
						$scope.dataScheduleConn = "none";   // 작두 데이터 커넥션 상태
						$scope.jakduSchedule = {};          // 작두 데이터
						$scope.goJakduAlert = {};		// 댓글 남기기 버튼 상태 문구

						getDataSchedule();

						// 작두 타기 단추.
						function btnGoJakdu() {
							var bUrl = '<c:url value="/api/jakdu/myJakdu"/>';
							var reqData = {};

							if (Jakduk.isEmpty($scope.myHomeScore) || Jakduk.isEmpty($scope.myAwayScore)) {
								$scope.goJakduAlert = {"classType":"text-danger", "msg":'<spring:message code="jakdu.choose.expected.score"/>'};
								return;
							}



							if (confirm('<spring:message code="board.msg.need.login.for.write"/>') == false) {
								return;
							}

							reqData.homeScore = $scope.myHomeScore;
							reqData.awayScore = $scope.myAwayScore;
							reqData.jakduScheduleId = "${id}";

							var reqPromise = $http.post(bUrl, reqData);
							$scope.goJakdu = true;

							reqPromise.success(function (data, status, headers, config) {
								console.log("success");

								$scope.goJakdu = false;
								$scope.goJakduAlert = {};
							});
							reqPromise.error(function (data, status, headers, config) {
								alert(data.message);
								$scope.goJakdu = false;
							});
						}

						// 작두 정보.
						function getDataSchedule() {
							var bUrl = '<c:url value="/api/jakdu/schedule/${id}" />';

							if ($scope.dataScheduleConn == "none") {

								var reqPromise = $http.get(bUrl);

								$scope.dataScheduleConn = "loading";

								reqPromise.success(function (data, status, headers, config) {

									if (data.jakduSchedule != null) {
										$scope.jakduSchedule = data.jakduSchedule;
									}

									if (data.myJakdu != null) {
										$scope.myHomeScore = data.myJakdu.homeScore;
										$scope.myAwayScore = data.myJakdu.awayScore;
									}

									$scope.dataScheduleConn = "none";
								});
								reqPromise.error(function (data, status, headers, config) {
									$scope.dataScheduleConn = "none";
									$scope.error = '<spring:message code="common.msg.error.network.unstable"/>';
								});
							}
						};
					})
					.controller('commentCtrl', function ($scope, $http) {
						$scope.dateTimeFormat = JSON.parse('${dateTimeFormat}');
						$scope.boardCommentContentLengthMin = Jakduk.BoardCommentContentLengthMin;
						$scope.boardCommentContentLengthMax = Jakduk.BoardCommentContentLengthMax;

						$scope.comments = [];					// 댓글들
						$scope.commentAlert = {};				// 댓글 상태 문구
						$scope.summernoteAlert = {};			// summernote 상태 문구
						$scope.commentFeelingConn = {};			// 댓글들 좋아요 싫어요 커넥션
						$scope.commentFeelingAlert = {};		// 댓글들 좋아요 싫어요 상태 문구
						$scope.numberOfCommentLike = {};		// 댓글들 좋아요 수
						$scope.numberOfCommentDislike = {};		// 댓글들 싫어요 수
						$scope.loadCommentConn = "none";		// 댓글 가져오기 커넥션
						$scope.writeCommentConn = "none";		// 댓글 남기기 커넥션
						$scope.writeCommentAlert = {};			// 댓글 남기기 버튼 상태 문구
						$scope.infiniteDisabled = false;		// infinite disabled 여부

						angular.element(document).ready(function() {
						});

						// summernote config
						$scope.options = {
							height : 0,
							//placeholder: '<spring:message code="board.msg.write.text.here"/>',
							lang : "${summernoteLang}",
							toolbar: [
								['font', ['bold']],
								// ['fontsize', ['fontsize']], // Still buggy
								['color', ['color']],
								['insert', ['link']],
								['help', ['help']]
							]
						};

						$scope.focus = function(e) {
							if ("${authRole}" == "ANNONYMOUS") {
								if (confirm('<spring:message code="board.msg.need.login.for.write"/>') == true) {
									location.href = "<c:url value='/login'/>";
								}
							}
						};

						if ("${authRole}" == "ANNONYMOUS") {
							$scope.summernoteAlert = {"classType":"text-danger", "msg":'<spring:message code="board.msg.need.login.for.write"/>'};
						}

						// 댓글 남기기
						$scope.btnWriteComment = function() {
							var bUrl = '<c:url value="/api/jakdu/schedule/comment"/>';

							if ($scope.summernote.contents.length < Jakduk.BoardCommentContentLengthMin
									|| $scope.summernote.contents.length > Jakduk.BoardCommentContentLengthMax) {
								$scope.summernoteAlert = {"classType":"text-danger", "msg":'<spring:message code="Size.board.comment.content"/>'};
								return;
							}

							if ($scope.writeCommentConn == "none") {
								var reqPromise = $http.post(bUrl, $scope.summernote);
								$scope.writeCommentConn = "connecting";
								$scope.writeComment = true;

								reqPromise.success(function(data, status, headers, config) {
									$scope.btnMoreComment();

									$scope.summernote.contents = "";
									$scope.commentAlert = {};
									$scope.summernoteAlert = {};
									$scope.writeCommentAlert = {};
									$scope.writeCommentConn = "none";
									$scope.writeComment = false;
								});
								reqPromise.error(function(data, status, headers, config) {
									$scope.writeCommentAlert = {"classType":"text-danger", "msg":data.message};
									$scope.writeCommentConn = "none";
									$scope.writeComment = false;
								});
							}
						};

						// infinite 초기 설정
						$scope.initComment = function() {
							$scope.loadComments("init", "");
							$scope.infiniteDisabled = true;
						};

						// 댓글 목록 가져오기.
						$scope.loadComments = function(type, commentId) {
							var bUrl = '<c:url value="/api/jakdu/schedule/comments/${id}?commentId=' + commentId + '"/>';

							if ($scope.loadCommentConn == "none") {
								var reqPromise = $http.get(bUrl);

								$scope.loadCommentConn = "connecting";

								reqPromise.success(function(data, status, headers, config) {

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
								reqPromise.error(function(data, status, headers, config) {
									$scope.loadCommentConn = "none";
								});
							}
						};

						// 댓글 더보기 단추.
						$scope.btnMoreComment = function() {
							if ($scope.comments.length > 0) {
								var lastComment = $scope.comments[$scope.comments.length - 1];
								$scope.loadComments("btnMoreComment", lastComment.id);
							} else {
								$scope.loadComments("btnMoreComment", "");
							}
						};

						// 댓글 새로고침 단추.
						$scope.btnRefreshComment = function() {
							$scope.commentAlert = {};
							$scope.comments = [];
							$scope.loadComments("btnRefreshComment", "");
						};

						// 댓글 감정 표현
						$scope.btnCommentFeeling = function(commentId, status) {

							var bUrl = '<c:url value="/api/jakdu/schedule/comment/' + commentId + '/' + status + '"/>';
							var conn = $scope.commentFeelingConn[commentId];

							if (conn == "none" || conn == null) {
								var reqPromise = $http.post(bUrl);

								$scope.commentFeelingConn[commentId] = "loading";

								reqPromise.success(function(data, status, headers, config) {

									if (data.feeling == 'LIKE') {
										$scope.numberOfCommentLike[commentId] = data.numberOfLike;
									} else if (data.feeling == 'DISLIKE') {
										$scope.numberOfCommentDislike[commentId] = data.numberOfDislike;
									}

									$scope.commentFeelingAlert[commentId] = '';
									$scope.commentFeelingConn[commentId] = "ok";

								});
								reqPromise.error(function(data, status, headers, config) {
									$scope.commentFeelingConn[commentId] = "none";
									$scope.commentFeelingAlert[commentId] = data.message;
								});
							}
						};
					});

			function needLogin() {
				if (confirm('<spring:message code="jakdu.msg.need.login.to.go.jakdu"/>') == true) {
					location.href = '<c:url value="/login"/>';
				}
			}

			$(document).ready(function () {
				App.init();
			});
		</script>

	</body>
</html>
