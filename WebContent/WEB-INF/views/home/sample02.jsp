<!DOCTYPE html>
<html ng-app="summernoteDemo">
<head>
  <title>Angular-summernote Demo</title>


  <link href="<%=request.getContextPath()%>/resources/bootstrap/css/bootstrap.css" rel="stylesheet">
  
  <link href="//netdna.bootstrapcdn.com/font-awesome/4.0.3/css/font-awesome.min.css" rel="stylesheet">

  <!--summernote-->
  <link href="<%=request.getContextPath()%>/resources/summernote/css/summernote.css" rel="stylesheet">


  <style>
    body {
      margin: 10px 20px;
    }
  </style>
</head>
<body>
  <h1>angular-summernote</h1>
  <p>angular directive for <a href="http://hackerwins.github.io/summernote/">Summernote</a></p>

  <h2>summernote directive</h2>

  <h4>use element</h4>
  <summernote></summernote>

  <h4>use attribute</h4>
  <div summernote></div>

  <h2>summernote options</h2>

  <h4>height</h4>
  <summernote height="300" lang="en-US"></summernote>

  <h4>focus</h4>
  <summernote focus></summernote>

  <div ng-controller="OptCtrl">
    <h4>options object</h4>
    <summernote config="options"></summernote>
  </div>

  <div ng-controller="CodeCtrl">
    <h4>code : HTML string in summernote</h4>
    <summernote ng-model="text"></summernote>

    <br>
    <textarea type="text" ng-model="text" style="width:100%;"></textarea></br>
    text : {{text}}
  </div>

  <div ng-controller="CallbacksCtrl">
    <h4>use callbacks</h4>
    <summernote on-init="init()" on-enter="enter()" on-focus="focus(evt)"
                on-blur="blur(evt)" on-paste="paste()" on-keyup="keyup(evt)" on-keydown="keydown(evt)"
                on-image-upload="imageUpload(files, editor, welEditable);"></summernote>
  </div>
  <!--summernote dependencies-->
  <script src="//ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/resources/bootstrap/js/bootstrap.min.js"></script>
  <script src="<%=request.getContextPath()%>/resources/summernote/js/summernote.min.js"></script>
  <script src="<%=request.getContextPath()%>/resources/summernote/lang/summernote-ko-KR.js"></script>

  <!--angular-summernote dependencies -->
  <script src="<%=request.getContextPath()%>/resources/angular/js/angular.js"></script>
  <script src="<%=request.getContextPath()%>/resources/angular-summernote/js/angular-summernote.min.js"></script>

  <script>
    angular.module('summernoteDemo', ['summernote'])
        .controller('OptCtrl', function($scope) {
          $scope.options = {
            height: 150,
            toolbar: [
              ['style', ['bold', 'italic', 'underline', 'clear']],
              ['fontsize', ['fontsize']],
              ['color', ['color']],
              ['para', ['ul', 'ol', 'paragraph']],
              ['height', ['height']]
            ]
          };
        })
        .controller('CodeCtrl', function($scope) {
          $scope.text = "Hello World";
        })
        .controller('CallbacksCtrl', function($scope) {
          $scope.init = function() { console.log('Summernote is launched'); }
          $scope.enter = function() { console.log('Enter/Return key pressed'); }
          $scope.focus = function(e) { console.log('Editable area is focused'); }
          $scope.blur = function(e) { console.log('Editable area loses focus'); }
          $scope.paste = function() { console.log('Called event paste'); }
          $scope.keyup = function(e) { console.log('Key is released:', e.keyCode); }
          $scope.keydown = function(e) { console.log('Key is pressed:', e.keyCode); }
        });
  </script>
</body>
</html>