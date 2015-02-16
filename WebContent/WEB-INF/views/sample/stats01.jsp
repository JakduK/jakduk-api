<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html ng-app="jakdukApp">
<head>
	<jsp:include page="../include/html-header.jsp"/>
		
</head>
<body>
<div class="container" ng-controller="StatsCtrl">
<jsp:include page="../include/navigation-header.jsp"/>

<div google-chart chart="chartObject"></div>

</div>
<script src="<%=request.getContextPath()%>/resources/angular-google-chart/js/ng-google-chart.js"></script>
<script type="text/javascript">
var jakdukApp = angular.module("jakdukApp", ["googlechart"]);

jakdukApp.controller('StatsCtrl', function($scope) {
    $scope.chartObject = {};

    $scope.onions = [
        {v: "Onions"},
        {v: 3},
    ];

    $scope.chartObject.data = {"cols": [
        {id: "t", label: "Topping", type: "string"},
        {id: "s", label: "하하", type: "number"}
    ], "rows": [
        {c: [
            {v: "Mushrooms"},
            {v: 3},
        ]},
        {c: $scope.onions},
        {c: [
            {v: "Olives"},
            {v: 31}
        ]},
        {c: [
            {v: "Zucchini"},
            {v: 1},
        ]},
        {c: [
            {v: "Pepperoni"},
            {v: 2},
        ]}
    ]};


    // $routeParams.chartType == BarChart or PieChart or ColumnChart...
    $scope.chartObject.type = "BarChart";
    $scope.chartObject.options = {
        'title': 'How Much Pizza I Ate Last Night'
    }
	
});
</script>
<jsp:include page="../include/body-footer.jsp"/>
</body>
</html>