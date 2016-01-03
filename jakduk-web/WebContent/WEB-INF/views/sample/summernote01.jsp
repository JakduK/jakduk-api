<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>    
<!DOCTYPE html>
<html ng-app="jakdukApp">
  <head>
    <title>Starter Template for Bootstrap</title>
<jsp:include page="../include/html-header.jsp"/>

  <!--summernote dependencies-->
<script src="<%=request.getContextPath()%>/resources/jquery/dist/jquery.min.js"></script>
<link href="<%=request.getContextPath()%>/resources/bootstrap/dist/css/bootstrap.css" rel="stylesheet">
<link href="<%=request.getContextPath()%>/resources/summernote/dist/summernote.css" rel="stylesheet">
<link href="<%=request.getContextPath()%>/resources/font-awesome/css/font-awesome.min.css" rel="stylesheet">

</head>

<body>
<div class="container">

<jsp:include page="../include/navigation-header.jsp"/>

<div ng-controller="sampleCtrl">
	<div class="starter-template">
		<h1>Bootstrap starter template</h1>
		<p class="lead">Use this document as a way to quickly start any new project.<br> All you get is this text and a mostly barebones HTML document.</p>
	</div>

 <summernote on-image-upload="imageUpload(files, editor)" editable="editable" config="options"></summernote>

<input type="file" nv-file-select uploader="uploader"/><br/>
<ul>
    <li ng-repeat="item in uploader.queue">
        Name: <span ng-bind="item.file.name"></span><br/>
        <button ng-click="item.upload()">upload</button>
    </li>
</ul>

<table class="table table-bordered">
<tr><td class="col-xs-3">	Dapibus ac facilisis in</td><td class="col-xs-5">  	<div class="progress">
  <div class="progress-bar progress-bar-success" role="progressbar" aria-valuenow="40" aria-valuemin="0" aria-valuemax="100" style="width: 40%">
    <span class="sr-only">40% Complete (success)</span>
  </div>
</div></td></tr>
</table>

<dl class="dl-horizontal">
  <dt>.dddd.</dt>
  <dd><div class="progress">    
  <div class="progress-bar progress-bar-success" role="progressbar" aria-valuenow="40" aria-valuemin="0" aria-valuemax="100" style="width: 40%">
    <span class="sr-only">40% Complete (success)</span>
  </div>
</div></dd>
</dl>

<div class="row" uploader="uploader">
  <div class="col-xs-4 col-sm-2 col-md-2" ng-repeat="item in uploader.queue">
    <div class="thumbnail">
      <div class="caption">
        <p><a href="#" class="btn btn-primary" role="button">Button</a></p>
      </div>
      <small>{{item.file.name}}</small>
      
<img ng-src="{{item.test}}">      
      
      
    </div>
  </div>
</div>

<form action="<c:url value="/image/upload"/>" name="login" method="post" enctype="multipart/form-data">
      <input type="file" name="file">
      <input type="submit">
</form>  

</div>
	
<jsp:include page="../include/footer.jsp"/>
</div><!-- /.container -->

<script src="<%=request.getContextPath()%>/resources/summernote/dist/summernote.min.js"></script>
<script src="<%=request.getContextPath()%>/resources/angular-summernote/dist/angular-summernote.min.js"></script>
<script src="<%=request.getContextPath()%>/resources/angular-file-upload/dist/angular-file-upload.min.js"></script>
<script src="<%=request.getContextPath()%>/resources/angular-sanitize/angular-sanitize.min.js"></script>

<script type="text/javascript">
var jakdukApp = angular.module("jakdukApp", ["summernote", "angularFileUpload", "ngSanitize"]);

jakdukApp.controller("sampleCtrl", function($scope, $http, FileUploader) {
	$scope.count = 0;
	$scope.imageList = {};
	
    $scope.options = {
            height: 0,
            toolbar: [
              ['style', ['bold', 'italic', 'underline', 'clear']],
              ['fontsize', ['fontsize']],
              ['color', ['color']],
              ['para', ['ul', 'ol', 'paragraph']],
              ['height', ['height']]
            ]
          };
    
    $scope.uploader = new FileUploader({
    	url:'<c:url value="/gallery/upload.json"/>',
    	autoUpload:true,
    	method:"POST"
    });
   
	var test1 = [];
	var test2;
	
	test1.push("dddd");
	test1.push("cccc");
	
	test2 = JSON.stringify(test1);
	
	var test3 = JSON.parse(test2);
	
    
	$scope.uploader.filters.push({
    	name: 'imageFilter',
    	fn: function(item /*{File|FileLikeObject}*/, options) {
    	var type = '|' + item.type.slice(item.type.lastIndexOf('/') + 1) + '|';
    	return '|jpg|png|jpeg|bmp|gif|'.indexOf(type) !== -1;
    		}
    	});
	
	$scope.uploader.onBeforeUploadItem = function(item) {
		 console.info('onBeforeUploadItem', item);
		 item.test = $scope.count++;
		 //item.url = "";
		 
		 //$scope.imageList[fileItem.test] = "/resources/jakduk/icon/daum_bt.png"; 
		 
		 };
	
	$scope.uploader.onCompleteItem = function(fileItem, response, status, headers) {
		console.log('onCompleteItem fileItem=', fileItem);
		console.log('onCompleteItem status=', status);
		console.log('onCompleteItem headers=', headers);

		var imageUrl = "<%=request.getContextPath()%>/gallery/" + response.image.id;
		var thumbnailUrl = "<%=request.getContextPath()%>/gallery/thumbnail/" + response.image.id;
		//alert(url);
		//$scope.imageList[fileItem.test] = url;
		//fileItem.test = "<img src='" + url + "'>";
		fileItem.test = thumbnailUrl;
		
		console.log("$scope.imageList" + $scope.imageList[fileItem.test]);
		
		$scope.editor.insertImage($scope.editable, imageUrl);		 
		 
		 };	
    
	$scope.imageUpload = function(files, editor) {
	 console.log('image upload:', files);
	 console.log('image upload:', editor);
	 console.log('image upload\'s editable:', $scope.editable);
	 
		//var url = "<%=request.getContextPath()%>/resources/jakduk/icon/daum_bt.png";       	
		//editor.insertImage($scope.editable, url);
		$scope.editor = editor;
		var bUrl = '<c:url value="/image/upload"/>';
		
		$scope.uploader.addToQueue(files);
      };
    
});
</script>
<jsp:include page="../include/body-footer.jsp"/>
</body>
</html>
