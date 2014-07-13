<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html ng-app>
<head>  	
	<jsp:include page="../include/html-header.jsp"/>
</head>

<body>
<div class="container">

<jsp:include page="../include/navigation-header.jsp"/>
  <div class="jumbotron" ng-controller="homeCtrl" ng-init="refreshEncyclopedia()">
    <h2>{{encyclopedia.subject}} <span class="label label-default">{{encyclopedia.kind}}</span></h2>
    <p>
    {{encyclopedia.content}}
    </p>
 <p>
 	<button type="button" class="btn btn-default" ng-click="refreshEncyclopedia()">
 		<span class="glyphicon glyphicon-refresh"></span>
 	</button>
 </p>
  </div>
          
 <div class="row">
   <div class="col-6 col-sm-6 col-lg-4">
     <h3>최신 게시물</h3>
     <p>안녕하세요.   2014-07-05(토) 성남팬</p>
     <p>월드컵 예상 성적   2014-07-05(토) 수원팬</p>
     <p><a class="btn btn-default" href="<c:url value="/board"/>" role="button">View details &raquo;</a></p>
   </div><!--/span-->
   <div class="col-6 col-sm-6 col-lg-4">
     <h3>가입 회원</h3>
     <p>성남팬</p>
     <p>수원팬</p>
     <p><a class="btn btn-default" href="<c:url value="/about"/>" role="button">View details &raquo;</a></p>
   </div><!--/span-->
 </div><!--/row-->

<jsp:include page="../include/footer.jsp"/>
</div><!-- /.container -->

<!-- Bootstrap core JavaScript ================================================== -->
<!-- Placed at the end of the document so the pages load faster -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/web-resources/bootstrap/js/bootstrap.min.js"></script>
<script type="text/javascript">
function homeCtrl($scope, $http) {
	$scope.encyclopedia = {};
	$scope.result = 0;
	
	$scope.refreshEncyclopedia = function() {
		var bUrl = '<c:url value="/home/jumbotron.json?lang=${pageContext.response.locale}"/>';
		
		if ($scope.result == 0) {
			
			var reqPromise = $http.get(bUrl);
			
			$scope.result = 1;
			
			reqPromise.success(function(data, status, headers, config) {
				if (data.encyclopedia != null) {
					if (data.encyclopedia.kind == 1) {
						$scope.encyclopedia.kind = '<spring:message code="home.kind.best.player"/>';
					} else if (data.encyclopedia.kind == 2) {
						$scope.encyclopedia.kind = '<spring:message code="home.kind.recommend.book"/>';
					}
					
					$scope.encyclopedia.subject = data.encyclopedia.subject;
					$scope.encyclopedia.content = data.encyclopedia.content;
				}
				
				$scope.result = 0;
				
			});
			reqPromise.error(error);
		}
	};
	
	function error(data, status, headers, config) {
		$scope.result = 0;
		$scope.error = '<spring:message code="common.msg.error.network.unstable"/>';
	}
}
</script>
</body>
</html>