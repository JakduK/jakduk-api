<%--
  Created by IntelliJ IDEA.
  User: pyohwan
  Date: 15. 12. 23
  Time: 오후 11:47
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!DOCTYPE html>
<html ng-app="jakdukApp">
	<head>
		<title>Jakdu Schedule Write &middot; Admin &middot; <spring:message code="common.jakduk"/></title>
		<jsp:include page="../include/html-header.jsp"/>
		<link rel="stylesheet" href="<%=request.getContextPath()%>/bundles/admin.css">
	</head>
	<body>
		<div class="container" ng-controller="adminCtrl">

			<div class="page-header">
				<h4>Write JakduSchedule.</h4>
			</div>

			<c:set var="contextPath" value="<%=request.getContextPath()%>"/>
			<form:form commandName="jakduScheduleWrite" action="${contextPath}/admin/jakdu/schedule/write" method="POST">
				<form:hidden path="id"/>

				<div class="row">
					<div class="col-sm-6">
						<label class="control-label">DATE</label>
						<div class="input-group">
							<input type="text" class="form-control" uib-datepicker-popup="{{format}}" ng-model="mytime" is-open="status.opened" min-date="minDate" max-date="maxDate"
								datepicker-options="dateOptions" date-disabled="disabled(date, mode)" ng-required="true" close-text="Close"/>
                        <span class="input-group-btn">
                            <button type="button" class="btn btn-default" ng-click="open($event)"><i class="glyphicon glyphicon-calendar"></i></button>
                        </span>
						</div>
					</div>
					<div class="col-sm-6">
						<label class="control-label">TIME</label>
						<uib-timepicker ng-model="mytime" ng-change="changed()" hour-step="hstep" minute-step="mstep" show-meridian="ismeridian" show-spinners="false"/>
					</div>

					<div class="col-sm-12">
						<div class="form-group">
							<form:input path="date" ng-model="mytime" cssClass="form-control"/>
						</div>
					</div>
					<form:errors path="date"/>

					<div class="col-sm-6">
						<div class="form-group">
							<label for="home" class="control-label">HOME TEAM</label>
							<form:select path="home" cssClass="form-control">
								<c:forEach items="${footballClubs}" var="club">
									<form:option value="${club.id}" label="${club.name}"/>
								</c:forEach>
							</form:select>
							<form:errors path="home"/>
						</div>
					</div>
					<div class="col-sm-6">
						<div class="form-group">
							<label for="away" class="control-label">AWAY TEAM</label>
							<form:select path="away" cssClass="form-control">
								<c:forEach items="${footballClubs}" var="club">
									<form:option value="${club.id}" label="${club.name}"/>
								</c:forEach>
							</form:select>
							<form:errors path="away"/>
						</div>
					</div>
					<div class="col-sm-6">
						<div class="form-group">
							<label for="groupSeq" class="control-label">GROUP SEQ</label>
							<form:input path="groupSeq" cssClass="form-control" placeholder="GROUP SEQ"/>
							<form:errors path="groupSeq"/>
						</div>
					</div>
					<div class="col-sm-6">
						<div class="form-group">
							<label for="competition" class="control-label">COMPETITION</label>
							<form:select path="competition" cssClass="form-control">
								<c:forEach items="${competitions}" var="competition">
									<form:option value="${competition.id}" label="${competition.code}"/>
								</c:forEach>
							</form:select>
							<form:errors path="competition"/>
						</div>
					</div>
				</div>

				<div class="row">
					<div class="col-sm-4">
						<label class="control-label">FULL TIME SCORE</label>
						<div class="row">
							<div class="col-sm-6">
								<form:input path="homeFullTime" cssClass="form-control" placeholder="HOME"/>
							</div>
							<div class="col-sm-6">
								<form:input path="awayFullTime" cssClass="form-control" placeholder="AWAY"/>
							</div>
						</div>
					</div>
					<div class="col-sm-4">
						<label class="control-label">OVER TIME SCORE</label>
						<div class="row">
							<div class="col-sm-6">
								<form:input path="homeOverTime" cssClass="form-control" placeholder="HOME"/>
							</div>
							<div class="col-sm-6">
								<form:input path="awayOverTime" cssClass="form-control" placeholder="AWAY"/>
							</div>
						</div>
					</div>
					<div class="col-sm-4">
						<label class="control-label">PENALTY SHOOTOUT SCORE</label>
						<div class="row">
							<div class="col-sm-6">
								<form:input path="homePenaltyShootout" cssClass="form-control" placeholder="HOME"/>
							</div>
							<div class="col-sm-6">
								<form:input path="awayPenaltyShootout" cssClass="form-control" placeholder="AWAY"/>
							</div>
						</div>
					</div>
				</div>

				<div class="row">
					<div class="col-sm-12">
						<div class="checkbox">
							<label>
								<form:checkbox path="timeUp"/>
								isTimeUp?
							</label>
						</div>
					</div>
				</div>

				<input type="submit" value="<spring:message code="common.button.write"/>" class="btn btn-default"/>
				<c:if test="${!empty jakduScheduleWrite.id}">
					<button type="button" class="btn btn-default" onclick="confirmDelete();">
						<i class="fa fa-trash-o"></i> <spring:message code="common.button.delete"/>
					</button>
				</c:if>
				<button type="button" class="btn btn-default" onclick="location.href='<c:url value="/admin/settings?open=jakduSchedule"/>'">
					<span class="glyphicon glyphicon-ban-circle"></span> <spring:message code="common.button.cancel"/>
				</button>
			</form:form>
		</div>

		<script src="<%=request.getContextPath()%>/bundles/admin.js"></script>
		<script type="text/javascript">

			var jakdukApp = angular.module("jakdukApp", ['ui.bootstrap', 'jakdukCommon']);

			jakdukApp.controller("adminCtrl", function ($scope) {
				var time = '${jakduScheduleWrite.date.time}';

				if (!Jakduk.isEmpty(time))
					$scope.mytime = new Date(parseInt(time));
				else
					$scope.mytime = new Date();

				$scope.mytime.setSeconds(0);

				$scope.format = 'yyyy/MM/dd';
				$scope.status = {
					opened: false
				};

				$scope.hstep = 1;
				$scope.mstep = 15;

				angular.element(document).ready(function () {
				});

				$scope.open = function ($event) {
					$scope.status.opened = true;
				};
			});

			function confirmDelete() {
				if (confirm('delete?') == true) {
					location.href = '<c:url value="/admin/jakdu/schedule/delete/${jakduScheduleWrite.id}"/>';
				}
			}

		</script>

		<jsp:include page="../include/body-footer.jsp"/>

	</body>
</html>
