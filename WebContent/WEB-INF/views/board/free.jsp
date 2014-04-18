<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<jsp:include page="../include/html-header.jsp"></jsp:include>

<!-- Bootstrap core CSS -->
<link href="<%=request.getContextPath()%>/web-resources/bootstrap/css/bootstrap.min.css" rel="stylesheet">
<!-- Bootstrap theme -->
<link href="<%=request.getContextPath()%>/web-resources/bootstrap/css/bootstrap-theme.min.css" rel="stylesheet">

</head>
<body>
<jsp:include page="../include/navigation-header.jsp"/>
<div class="container">


<p><a href="<c:url value="/board/free/write"/>" class="btn btn-primary" role="button">Write</a></p>

<div class="panel panel-default">
      <!-- Default panel contents -->
      <div class="panel-heading">Panel heading</div>
      <div class="panel-body">
        <p>Some default panel content here. Nulla vitae elit libero, a pharetra augue. Aenean lacinia bibendum nulla sed consectetur. Aenean eu leo quam. Pellentesque ornare sem lacinia quam venenatis vestibulum. Nullam id dolor id nibh ultricies vehicula ut id elit.</p>
      </div>
      <!-- Table -->
      <table class="table">
        <thead>
          <tr>
            <th>#</th>
            <th>First Name</th>
            <th>Last Name</th>
            <th>Username</th>
          </tr>
        </thead>
        <tbody>
          <tr>
            <td>1</td>
            <td>Mark</td>
            <td>Otto</td>
            <td>@mdo</td>
          </tr>
          <tr>
            <td>2</td>
            <td>Jacob</td>
            <td>Thornton</td>
            <td>@fat</td>
          </tr>
          <tr>
            <td>3</td>
            <td>Larry</td>
            <td>the Bird</td>
            <td>@twitter</td>
          </tr>
        </tbody>
      </table>
    </div>
    
<jsp:include page="../include/footer.jsp"/>
</div>

    <!-- Bootstrap core JavaScript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
    <script src="<%=request.getContextPath()%>/web-resources/bootstrap/js/bootstrap.min.js"></script>    
		<script src="<%=request.getContextPath()%>/web-resources/bootstrap/js/offcanvas.js"></script>
		
</body>
</html>