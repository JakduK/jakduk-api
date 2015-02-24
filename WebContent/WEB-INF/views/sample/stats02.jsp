<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<style>


.chart rect {
  fill: steelblue;
}

.chart text {
  fill: white;
  font: 10px sans-serif;
  text-anchor: middle;
}


</style>
</head>
<body>

<svg width="720" height="120">
  <circle cx="30" cy="60" r="5.656854249492381" style="fill:steelblue;"></circle>
  <circle cx="130" cy="60" r="7.54983443527075" style="fill:steelblue;"></circle>
</svg>

<script src="<%=request.getContextPath()%>/resources/d3/d3.min.js"></script>
<script type="text/javascript">
var svg = d3.select("svg");

var circle = svg.selectAll("circle")
    .data([32, 57, 112, 293]);

var circleEnter = circle.enter().append("circle");
</script>
</body>
</html>