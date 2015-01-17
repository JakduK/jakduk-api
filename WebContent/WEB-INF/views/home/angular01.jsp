<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
	<jsp:include page="../include/html-header.jsp"/>
</head>
<body>
<jsp:include page="../include/navigation-header.jsp"/>

<div ng-app="spicyApp1">


<div ng-controller="SpicyController">
 <button ng-click="chiliSpicy()">Chili</button>
 <button ng-click="jalapenoSpicy()">Jalapeño</button>
 <p>The food is {{spice}} spicy!</p>
</div>


</div>

<script type="text/javascript">

	var myApp = angular.module('spicyApp1', []);

	myApp.controller('SpicyController', function($scope) {
		$scope.spice = 'very';

		$scope.chiliSpicy = function() {
			$scope.spice = 'chili';
			submitted = true;
		};

		$scope.jalapenoSpicy = function() {
			$scope.spice = 'jalapeño';
		};
	});
</script>

</body>
</html>