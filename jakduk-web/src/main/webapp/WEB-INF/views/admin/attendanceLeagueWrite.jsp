<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html ng-app="jakdukApp">
	<head>
		<jsp:include page="../include/html-header.jsp"/>
		<link rel="stylesheet" href="<%=request.getContextPath()%>/bundles/admin.css">
	</head>
	<body>
		<div class="container" ng-controller="adminCtrl">
			<h4>Write attendanceLeague.</h4>
			<c:set var="contextPath" value="<%=request.getContextPath()%>"/>
			<form:form commandName="attendanceLeague" action="${contextPath}/admin/attendance/league/write" method="POST">
				<form:hidden path="id"/>

				<p>
					<label for="league" class="control-label">LEAGUE</label>
					<select id="league" name="league" class="form-control">
						<option value="KL">K LEAGUE</option>
						<option value="KLCL">K LEAGUE CLASSIC</option>
						<option value="KLCH">K LEAGUE CHALLENGE</option>
					</select>
				</p>

				<p>
					<label for="season" class="control-label">SEASON</label>
					<form:input path="season" cssClass="form-control" placeholder="Season"/>
				</p>
				<form:errors path="season"/>
				<p>ex) 40</p>
				<p>
					GAMES :
					<form:input path="games" cssClass="form-control" placeholder="Games"
						ng-model="games" ng-init="games='${attendanceLeague.games}'" ng-blur="calcAve()"/>
				</p>
				<form:errors path="games"/>
				<p>ex) 419478</p>
				<p>
					TOTAL : <form:input path="total" cssClass="form-control" placeholder="Total"
					ng-model="total" ng-init="total='${attendanceLeague.total}'" ng-blur="calcAve()"/>
				</p>
				<form:errors path="total"/>
				<p>
				<p>
					AVERAGE : <form:input path="average" cssClass="form-control" placeholder="Average"
					ng-model="average" ng-init="average='${attendanceLeague.average}'"/>
				</p>
				<form:errors path="average"/>
				<p>
					<label for="numberOfClubs" class="control-label">NUMBER OF CLUBS</label>
					<form:input path="numberOfClubs" cssClass="form-control" placeholder="Number Of Clubs"/>
				</p>
				<form:errors path="numberOfClubs"/>
				<p>
					<input type="submit" value="<spring:message code="common.button.write"/>" class="btn btn-default"/>
					<c:if test="${!empty attendanceLeague}">
						<button type="button" class="btn btn-default" onclick="confirmDelete();">
							<i class="fa fa-trash-o"></i> <spring:message code="common.button.delete"/>
						</button>
					</c:if>
				</p>
			</form:form>
		</div>
		<jsp:include page="../include/footer.jsp"/>
		<script src="<%=request.getContextPath()%>/bundles/admin.js"></script>
		<script type="text/javascript">

			var jakdukApp = angular.module("jakdukApp", ['jakdukCommon']);

			jakdukApp.controller("adminCtrl", function ($scope, $filter) {

				$scope.calcAve = function () {
					$scope.average = Math.round($scope.total / $scope.games);
				};

			});

			function confirmDelete() {

				if (confirm('delete?') == true) {
					location.href = '<c:url value="/admin/attendance/league/delete/${attendanceLeague.id}"/>';
				}

			}

		</script>
	</body>
</html>
