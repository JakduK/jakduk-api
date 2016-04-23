<%--
  Created by IntelliJ IDEA.
  User: pyohwan
  Date: 16. 1. 10
  Time: 오후 9:03
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!DOCTYPE html>
<html ng-app="jakdukApp">
	<head>
		<title>Jakdu Schedule Group Write &middot; Admin &middot; <spring:message code="common.jakduk"/></title>
		<jsp:include page="../include/html-header.jsp"/>
		<link rel="stylesheet" href="<%=request.getContextPath()%>/bundles/admin.css">
	</head>
	<body>
		<div class="container" ng-controller="adminCtrl">

			<div class="page-header">
				<h4>Write JakduScheduleGroup.</h4>
			</div>

			<c:set var="contextPath" value="<%=request.getContextPath()%>"/>
			<form:form commandName="jakduScheduleGroupWrite" action="${contextPath}/admin/jakdu/schedule/group/write" method="POST">
				<form:hidden path="id"/>

				<div class="row">
					<div class="col-sm-6">
						<label class="control-label">OPEN DATE</label>
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
							<form:input path="openDate" ng-model="mytime" cssClass="form-control"/>
						</div>
					</div>
					<form:errors path="openDate"/>

					<div class="col-sm-6">
						<div class="form-group">
							<label for="seq" class="control-label">SEQ</label>
							<form:input path="seq" cssClass="form-control disable" placeholder="SEQ"
								ng-disabled="nextSeq"/>
							<div class="checkbox">
								<label>
									<form:checkbox path="nextSeq" ng-model="nextSeq"/>
									nextSeq?
								</label>
							</div>
							<form:errors path="seq"/>
						</div>
					</div>

					<div class="col-sm-6">
						<div class="form-group">
							<label for="state" class="control-label">STATE</label>
							<form:select path="state" cssClass="form-control">
								<form:option value="SCHEDULE"/>
								<form:option value="STANDBY"/>
								<form:option value="PLAYING"/>
								<form:option value="TIMEUP"/>
							</form:select>
							<form:errors path="state"/>
						</div>
					</div>
				</div>

				<input type="submit" value="<spring:message code="common.button.write"/>" class="btn btn-default"/>
				<c:if test="${!empty jakduScheduleGroupWrite.id}">
					<button type="button" class="btn btn-default" onclick="confirmDelete();">
						<i class="fa fa-trash-o"></i> <spring:message code="common.button.delete"/>
					</button>
				</c:if>
				<button type="button" class="btn btn-default" onclick="location.href='
					<c:url value="/admin/settings?open=jakduScheduleGroup"/>'">
					<span class="glyphicon glyphicon-ban-circle"></span> <spring:message code="common.button.cancel"/>
				</button>
			</form:form>
		</div>

		<script src="<%=request.getContextPath()%>/bundles/admin.js"></script>
		<script type="text/javascript">

			var jakdukApp = angular.module("jakdukApp", ['ui.bootstrap', 'jakdukCommon']);

			jakdukApp.controller("adminCtrl", function ($scope) {
				var time = '${jakduScheduleGroupWrite.openDate.time}';

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
					location.href = '<c:url value="/admin/jakdu/schedule/group/delete/${jakduScheduleGroupWrite.id}"/>';
				}
			}

		</script>

		<jsp:include page="../include/body-footer.jsp"/>

	</body>
</html>
