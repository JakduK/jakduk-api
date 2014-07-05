<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html ng-app>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<jsp:include page="../include/html-header.jsp"></jsp:include>
</head>
<body>
<div class="container">
<jsp:include page="../include/navigation-header.jsp"/>
<div class="container">

<div class="page-header">
  <h3><spring:message code="about"/> <small><spring:message code="about"/></small></h3>
</div>

<h3>K리그 작두왕</h3>
<p>작두타기란 무당의 신내림 과정에서 나온 말입니다. 무당이 굿할 때 신의 영력을 보여주기 위해서 맨발로 작두 위에 올라서서 춤을 추고 공수를 내리는 제차입니다. </p>
<p>무당은 자신이 이어 받은 신령의 신통력을 사람들 앞에서 과시하는데 그 목적이 있습니다. 관중들은 그 아찔한 순간을 보고 놀라며 그 무당을 <strong>"신 들렸다."</strong>라고 표현합니다.</p> 
<p>즉, 어떤 사람이 무아지경의 상태에 빠져서 마치 신들린 사람처럼 보일 경우 <strong>"저 사람이 작두를 탄다."</strong>라고 합니다.
</p>
<p>K리그 작두왕이란 K리그의 경기결과를 신들린 사람처럼 잘 맞추는것을 말합니다.</p>
<hr/>
<h3>K리그 커뮤니티</h3>
<p>K리그 작두왕은 K리그 중심의 커뮤니티입니다.  K리그 컨텐츠를 활용하여 게시판, 경기일정, 승무패, 통계 등의 기능을 서비스할 예정입니다.</p>
<p>향후 K리그 뿐만 아니라, 내셔널리그, 챌린저스리그, WK리그를 비롯한 국내축구 중심의 컨텐츠를 아우르는것이 궁극적인 목표입니다.</p>
<hr/>
<h3>오픈 소스</h3>
<p>K리그 작두왕은 오픈 소스를 이용하여 개발되었으며, K리그 작두왕의 소스 코드도 모두 공개되어 있습니다. </p>
<p>또한,  K리그 작두왕의 개발에 직접 참여할수 있습니다.</p>
<p><a href="https://github.com/Pyohwan/JakduK">https://github.com/Pyohwan/JakduK</a></p>

<jsp:include page="../include/footer.jsp"/>
</div>
</div><!-- /.container -->

<!-- Bootstrap core JavaScript
  ================================================== -->
<!-- Placed at the end of the document so the pages load faster -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/web-resources/bootstrap/js/bootstrap.min.js"></script>    

</body>
</html>