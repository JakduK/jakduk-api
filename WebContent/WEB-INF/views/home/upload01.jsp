<!doctype html>
<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<title>Angular file upload sample</title>
<script type="text/javascript">
FileAPI = {
	debug: true,
	//forceLoad: true, html5: false //to debug flash in HTML5 browsers
	//wrapInsideDiv: true, //experimental for fixing css issues
	//only one of jsPath or jsUrl.
    //jsPath: '/js/FileAPI.min.js/folder/', 
    //jsUrl: 'yourcdn.com/js/FileAPI.min.js',

    //only one of staticPath or flashUrl.
    //staticPath: '/flash/FileAPI.flash.swf/folder/'
    //flashUrl: 'yourcdn.com/js/FileAPI.flash.swf'
};
</script>
<!-- <script src="//code.jquery.com/jquery-1.9.0.min.js"></script> -->
<script type="text/javascript">
	// load angularjs specific version
	var angularVersion = window.location.hash.substring(1);
	if (angularVersion.indexOf('/') == 0) angularVersion = angularVersion.substring(1); 
	document.write('<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/angularjs/' + 
			(angularVersion || '1.2.20') + '/angular.js"><\/script>');
</script>
<script src="<%=request.getContextPath()%>/resources/angular-file-upload/js/angular-file-upload-shim.min.js"></script>
<script src="<%=request.getContextPath()%>/resources/angular-file-upload/js/angular-file-upload.min.js"></script>

<link rel="stylesheet" href="//cdnjs.cloudflare.com/ajax/libs/codemirror/4.8.0/codemirror.min.css">
<script src="//cdnjs.cloudflare.com/ajax/libs/codemirror/4.8.0/codemirror.min.js"></script>
<script src="//cdnjs.cloudflare.com/ajax/libs/codemirror/4.8.0/mode/htmlmixed/htmlmixed.min.js"></script>
<script src="//cdnjs.cloudflare.com/ajax/libs/codemirror/4.8.0/mode/htmlembedded/htmlembedded.min.js"></script>
<script src="//cdnjs.cloudflare.com/ajax/libs/codemirror/4.8.0/mode/xml/xml.min.js"></script>
<script src="//cdnjs.cloudflare.com/ajax/libs/codemirror/4.8.0/mode/javascript/javascript.min.js"></script>
</head>

<body ng-app="fileUpload" ng-controller="MyCtrl">
	<div class="ng-v">
		Angular Version: <input type="text" ng-model="angularVersion"> 
		<input type="button" data-ng-click="changeAngularVersion()" value="Go">
	</div>
	<h1>Angular file upload Demo</h1>
	<h3>
		Visit <a href="https://github.com/danialfarid/angular-file-upload">angular-file-upload</a> on github
	</h3>
	<div class="upload-div">
		<div class="edit-div">
			<a ng-click="showEdit = !showEdit" href="javascript:void(0)">+ edit upload html</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<a ng-show="showEdit" ng-click="confirm() && (editHtml = defaultHtml)" href="javascript:void(0)">reset to default</a><br/><br/>
			<div ng-show="showEdit" id="htmlEdit"></div>
		</div>

		<div class="upload-buttons">
			<div id="editArea">
				<form name="myForm">
				  	<fieldset><legend>Upload on form submit</legend>
					    Username: <input type="text" name="userName" ng-model="username" size="48" required> 
					      			<i ng-show="myForm.userName.$error.required">*required</i><br>
					    Profile Picture: <input type="file" ng-file-select ng-model="picFile" name="file" accept="image/*" 
					                   ng-file-change="generateThumb(picFile[0], $files)" required>
									<i ng-show="myForm.file.$error.required">*required</i>
					  	<br/>
					  	
						<button ng-disabled="!myForm.$valid" ng-click="uploadPic(picFile)">Submit</button>
					    <img ng-show="picFile[0].dataUrl != null" ng-src="{{picFile[0].dataUrl}}" class="thumb">
						<span class="progress" ng-show="picFile[0].progress >= 0">						
							<div style="width:{{picFile[0].progress}}%" ng-bind="picFile[0].progress + '%'"></div>
						</span>	
						<span ng-show="picFile[0].result">Upload Successful</span>
				  	</fieldset>
					<br/>
				</form>
				<fieldset><legend>Upload right away</legend>
					<div class="up-buttons">
						<div ng-file-select ng-model="files" class="upload-button" 
						       ng-file-change="upload(files)" ng-multiple="false" ng-accept="'image/*,application/pdf'" tabindex="0">Attach an Image or PDF</div><br/>
						<button ng-file-select ng-model="files" ng-multiple="true">Attach Any File</button>
					</div>
					<div ng-file-drop ng-file-select ng-model="files" class="drop-box" 
						drag-over-class="{accept:'dragover', reject:'dragover-err', delay:100}"
						ng-multiple="true" allow-dir="true" accept="image/*,*pdf">
							Drop Images or PDFs<div>here</div>
					</div>
					<div ng-no-file-drop class="drop-box">File Farg&Drop not supported on your browser</div>
				</fieldset>
				<br/>
			</div>
		</div>
		
		<ul ng-show="files.length > 0" class="response">
			<li class="sel-file" ng-repeat="f in files">
				<img ng-show="f.dataUrl" ng-src="{{f.dataUrl}}" class="thumb">
				<span class="progress" ng-show="f.progress >= 0">						
					<div style="width:{{f.progress}}%">{{f.progress}}%</div>
				</span>				
				<button class="button" ng-click="f.upload.abort();f.upload.aborted=true" 
						ng-show="f.upload != null && f.progress < 100 && !f.upload.aborted">Abort</button>
				{{f.name}} - size: {{f.size}}B - type: {{f.type}}
				<a ng-show="f.result" href="javascript:void(0)" ng-click="f.showDetail = !f.showDetail">details</a>
				<div ng-show="f.showDetail">
					<br/>
					<div data-ng-show="f.result.result == null">{{f.result}}</div>
					<ul>
						<li ng-repeat="item in f.result.result">
							<div data-ng-show="item.name">file name: {{item.name}}</div>
							<div data-ng-show="item.fieldName">name: {{item.fieldName}}</div>
							<div data-ng-show="item.size">size on the serve: {{item.size}}</div>
							<div data-ng-show="item.value">value: {{item.value}}</div>
						</li>
					</ul>
					<div data-ng-show="f.result.requestHeaders" class="reqh">request headers: {{f.result.requestHeaders}}</div>
				</div>
			</li>
		</ul>

		<br/>
		<div class="err" ng-show="errorMsg != null">{{errorMsg}}</div>
	</div>

	<div class="server">
		<div class="srv-title">How to upload to the server:</div>
		
		<div class="edit-div">
			<a ng-click="showEditScript  = !showEditScript" href="javascript:void(0)">+ edit upload script</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<a ng-show="showEditScript" ng-click="confirm() && (uploadScript = defaultUploadScript[howToSend])" href="javascript:void(0)">reset to default</a><br/><br/>
			<div ng-show="showEditScript" id="jsEdit"></div>
		</div>
		
		<div class="howto">
			<label><input type="radio" name="howToSend" ng-model="howToSend" value="1" ng-init="howToSend = 1">$upload.upload(): multipart/form-data upload cross browser</label>
			<br/>
			<label><input type="radio" name="howToSend" ng-model="howToSend" value="2" ng-disabled="!fileReaderSupported || usingFlash">$upload.http(): binary content with file's Content-Type</label>
			<div class="sub">Can be used to upload files directory into<a href="https://github.com/danialfarid/angular-file-upload/issues/88">CouchDB</a>, 
				<a href="https://github.com/danialfarid/angular-file-upload/issues/87">imgur</a>, etc... without multipart form data (HTML5 FileReader browsers only)<br/>
			</div>
			<label><input type="radio" name="howToSend" ng-model="howToSend" value="3">Amazon S3 bucket upload</label>
			
			<form ng-show="howToSend==3" class="s3" id="s3form" action="http://localhost:8888" onsubmit="return false">
				<br/>
				Provide S3 upload parameters. <a href="http://aws.amazon.com/articles/1434/" target="_blank">Click here for detailed documentation</a><br/></br>
				<label>S3 upload url including bucket name:</label> <input type="text" ng-model="s3url"> <br/>
				<label>AWSAccessKeyId:</label> <input type="Text" name="AWSAccessKeyId" ng-model="AWSAccessKeyId"> <br/>
				<label>acl (private or public):</label> <input type="text" ng-model="acl" value="private"><br/>
				<label>success_action_redirect:</label> <input type="text" ng-model="success_action_redirect"><br/>
				<label>policy:</label> <input type="text" ng-model="policy"><br/>
				<label>signature:</label> <input type="text" ng-model="signature"><br/>
				<br/>
				<button ng-click="showHelper=!showHelper">S3 Policy signing helper (Optional)</button><br/><br/>
				<div class="helper" ng-show="showHelper">
					If you don't have your policy and signature you can use this tool to generate them by providing these two fields and clicking on sign<br/>	      
					<label>AWS Secret Key:</label> <input type="text" ng-model="AWSSecretKey"><br/>
					<label>JSON policy:</label><br/> <textarea type="text" ng-model="jsonPolicy" rows=10 cols=70></textarea>
					<button ng-click="generateSignature()">Sign</button>
				</div>
		   	</form>
		   	<br/>
		   	<br/>
			<div ng-show="howToSend != 3">
				<input type="checkbox" ng-model="generateErrorOnServer">Return server error with http code: <input type="text" ng-model="serverErrorCode" size="5"> and message: <input type="text" ng-model="serverErrorMsg">
				<br/>
			</div>
		   	<br/>
		</div>
	</div>	
	<a style="position:fixed;bottom:10px;right:10px;font-size:smaller;" href="https://github.com/danialfarid/angular-file-upload/issues/new">Feedback/Issues</a>
	<div style="position:fixed;bottom:30px;right:10px;font-size:smaller;color:#777">Last update: 2015-01-06</div>
	
	<script type="text/javascript">
	var app = angular.module('fileUpload', [ 'angularFileUpload' ]);
	var version = '2.0.4';

	app.controller('MyCtrl', [ '$scope', '$http', '$timeout', '$compile', '$upload', function($scope, $http, $timeout, $compile, $upload) {
		$scope.usingFlash = FileAPI && FileAPI.upload != null;
		$scope.fileReaderSupported = window.FileReader != null && (window.FileAPI == null || FileAPI.html5 != false);

		$scope.changeAngularVersion = function() {
			window.location.hash = $scope.angularVersion;
			window.location.reload(true);
		};
		
		$scope.angularVersion = window.location.hash.length > 1 ? (window.location.hash.indexOf('/') === 1 ? 
				window.location.hash.substring(2): window.location.hash.substring(1)) : '1.2.20';
				
		$scope.$watch('files', function(files) {
			$scope.formUpload = false;
			if (files != null) {
				for (var i = 0; i < files.length; i++) {
					$scope.errorMsg = null;
					(function(file) {
						$scope.generateThumb(file);
						eval($scope.uploadScript);
					})(files[i]);
				}
			}
			storeS3UploadConfigInLocalStore();
		});
		
		$scope.uploadPic = function(files) {
			$scope.formUpload = true;
			if (files != null) {
				var file = files[0];
				$scope.generateThumb(file);
				$scope.errorMsg = null;
				eval($scope.uploadScript);	
			}
		}
		
		$scope.generateThumb = function(file) {
			if (file != null) {
				if ($scope.fileReaderSupported && file.type.indexOf('image') > -1) {
					$timeout(function() {
						var fileReader = new FileReader();
						fileReader.readAsDataURL(file);
						fileReader.onload = function(e) {
							$timeout(function() {
								file.dataUrl = e.target.result;
							});
						}
					});
				}
			}
		}
		
		$scope.generateSignature = function() {
			$http.post('/s3sign?aws-secret-key=' + encodeURIComponent($scope.AWSSecretKey), $scope.jsonPolicy).
				success(function(data) {
					$scope.policy = data.policy;
					$scope.signature = data.signature;
				});
		}
		
		if (localStorage) {
			$scope.s3url = localStorage.getItem("s3url");
			$scope.AWSAccessKeyId = localStorage.getItem("AWSAccessKeyId");
			$scope.acl = localStorage.getItem("acl");
			$scope.success_action_redirect = localStorage.getItem("success_action_redirect");
			$scope.policy = localStorage.getItem("policy");
			$scope.signature = localStorage.getItem("signature");
		}
		
		$scope.success_action_redirect = $scope.success_action_redirect || window.location.protocol + "//" + window.location.host;
		$scope.jsonPolicy = $scope.jsonPolicy || '{\n  "expiration": "2020-01-01T00:00:00Z",\n  "conditions": [\n    {"bucket": "angular-file-upload"},\n    ["starts-with", "$key", ""],\n    {"acl": "private"},\n    ["starts-with", "$Content-Type", ""],\n    ["starts-with", "$filename", ""],\n    ["content-length-range", 0, 524288000]\n  ]\n}';
		$scope.acl = $scope.acl || 'private';
		
		function storeS3UploadConfigInLocalStore() {
			if ($scope.howToSend == 3 && localStorage) {
				localStorage.setItem("s3url", $scope.s3url);
				localStorage.setItem("AWSAccessKeyId", $scope.AWSAccessKeyId);
				localStorage.setItem("acl", $scope.acl);
				localStorage.setItem("success_action_redirect", $scope.success_action_redirect);
				localStorage.setItem("policy", $scope.policy);
				localStorage.setItem("signature", $scope.signature);
			}
		}
		
		(function handleDynamicEditingOfScriptsAndHtml($scope, $http) {
			$scope.defaultUploadScript = [];
			$http.get('js/upload-upload.js').success(function(data) {
				$scope.defaultUploadScript[1] = data; $scope.uploadScript = $scope.uploadScript || data
			});
			$http.get('js/upload-http.js').success(function(data) {$scope.defaultUploadScript[2] = data});
			$http.get('js/upload-s3.js').success(function(data) {$scope.defaultUploadScript[3] = data});
			
			$scope.defaultHtml = document.getElementById('editArea').innerHTML.replace(/\t\t\t\t/g, '');
			
			$scope.editHtml = (localStorage && localStorage.getItem("editHtml" + version)) || $scope.defaultHtml;
			function htmlEdit(update) {
				document.getElementById("editArea").innerHTML = $scope.editHtml;
				$compile(document.getElementById("editArea"))($scope);
				$scope.editHtml && localStorage && localStorage.setItem("editHtml" + version, $scope.editHtml);
				if ($scope.editHtml != $scope.htmlEditor.getValue()) $scope.htmlEditor.setValue($scope.editHtml);
			}
			$scope.$watch("editHtml", htmlEdit);
			
			$scope.$watch("howToSend", function(newVal, oldVal) {
				$scope.uploadScript && localStorage && localStorage.setItem("uploadScript" + oldVal + version, $scope.uploadScript);
				$scope.uploadScript = (localStorage && localStorage.getItem("uploadScript" + newVal + version)) || $scope.defaultUploadScript[newVal]; 
			});
			
			function jsEdit(update) {
				$scope.uploadScript && localStorage && localStorage.setItem("uploadScript" + $scope.howToSend + version, $scope.uploadScript);
				if ($scope.uploadScript != $scope.jsEditor.getValue()) $scope.jsEditor.setValue($scope.uploadScript);
			}
			$scope.$watch("uploadScript", jsEdit);

			$scope.htmlEditor = CodeMirror(document.getElementById('htmlEdit'), {
				lineNumbers: true, indentUnit: 4,
				mode:  "htmlmixed"
			});
			$scope.htmlEditor.on('change', function() {
				if ($scope.editHtml != $scope.htmlEditor.getValue()) {
					$scope.editHtml = $scope.htmlEditor.getValue();
					htmlEdit();
				}
			});
			$scope.jsEditor = CodeMirror(document.getElementById('jsEdit'), {
				lineNumbers: true, indentUnit: 4,
				mode:  "javascript"
			});
			$scope.jsEditor.on('change', function() {
				if ($scope.uploadScript != $scope.jsEditor.getValue()) {
					$scope.uploadScript = $scope.jsEditor.getValue();
					jsEdit();
				}
			});
		})($scope, $http);
		
		$scope.confirm = function() {
			return confirm('Are you sure? Your local changes will be lost.');
		}
		
		$scope.getReqParams = function() {
			return $scope.generateErrorOnServer ? "?errorCode=" + $scope.serverErrorCode + 
					"&errorMessage=" + $scope.serverErrorMsg : "";
		}

		window.addEventListener("dragover", function(e) {
			e.preventDefault();
		}, false);
		window.addEventListener("drop", function(e) {
			e.preventDefault();
		}, false);
		
	} ]);	
	</script>
</body>
</html>
