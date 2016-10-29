<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>    
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>    

<!DOCTYPE html>
<html ng-app="jakdukApp">
	<head>
		<jsp:include page="../include/html-header.jsp"/>
		<link rel="stylesheet" href="<%=request.getContextPath()%>/bundles/admin.css">
	</head>
	<body>
		<div class="container" ng-controller="adminCtrl">
			<h4>Write AttendanceClub.</h4>
			<c:set var="contextPath" value="<%=request.getContextPath()%>"/>
			<form:form commandName="attendanceClubWrite" action="${contextPath}/admin/attendance/club/write" method="POST">
			<form:hidden path="id"/>

			<p>
			<label for="league" class="control-label">ORIGIN CLUB</label>
							<form:select path="origin" cssClass="form-control">
							<c:forEach items="${footballClubs}" var="club">
								<form:option value="${club.id}" label="${club.name}"/>
							</c:forEach>
							</form:select>
			</p>

			<p>
			<label for="season" class="control-label">SEASON</label>
			<form:input path="season" cssClass="form-control" placeholder="Season"/>
			</p>
			<form:errors path="season"/>

			<p>
			<label for="league" class="control-label">LEAGUE</label>
			<form:select path="league" cssClass="form-control">
				<form:option value="KL">K LEAGUE</form:option>
				<form:option value="KLCL">K LEAGUE CLASSIC</form:option>
				<form:option value="KLCH">K LEAGUE CHALLENGE</form:option>
			</form:select>
			</p>

			<p>
			<label for="games" class="control-label">GAMES</label>
			<form:input path="games" cssClass="form-control" placeholder="Games"
			ng-model="games" ng-init="games='${attendanceClubWrite.games}'" ng-blur="calcAve()"/>
			</p>
			<form:errors path="games"/>

			<p>
			<label for="total" class="control-label">TOTAL</label>
			<form:input path="total" cssClass="form-control" placeholder="Total"
			ng-model="total" ng-init="total='${attendanceClubWrite.total}'" ng-blur="calcAve()"/>
			</p>
			<form:errors path="total"/>

			<p>
			<label for="average" class="control-label">AVERAGE</label>
			<form:input path="average" cssClass="form-control" placeholder="Average"
			ng-model="average" ng-init="average='${attendanceClubWrite.average}'"/>
			</p>
			<form:errors path="average"/>

			<input type="submit" value="<spring:message code="common.button.write"/>" class="btn btn-default"/>
			</form:form>
		</div>
		<jsp:include page="../include/footer.jsp"/>
		<script src="<%=request.getContextPath()%>/bundles/admin.js"></script>
		<script type="text/javascript">
			angular.module("jakdukApp", ['jakdukCommon'])
				.controller("adminCtrl", function($scope) {
					$scope.calcAve = function() {
						$scope.average = Math.round($scope.total / $scope.games);
					};
				});
		</script>
	</body>
</html>
