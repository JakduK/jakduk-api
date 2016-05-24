(function() {
	'use strict';

	var Jakduk = window.Jakduk;
	if (!Jakduk) {
		Jakduk = window.Jakduk = {};
	}

	Jakduk.BoardCommentSize = 30;
	Jakduk.BoardCommentContentLengthMin = 3; // 게시판 댓글 입력 가능한 최소한의 문자열 수
	Jakduk.BoardCommentContentLengthMax = 800; // 게시판 댓글 입력 가능한 최대 문자열 수
	Jakduk.SummernoteContentsMinSize = 5;
	Jakduk.FormEmailLengthMin = 6;
	Jakduk.FormEmailLengthMax = 30;
	Jakduk.FormPasswordLengthMin = 4;
	Jakduk.FormPasswordLengthMax = 20;
	Jakduk.FormUsernameLengthMin = 2;
	Jakduk.FormUsernameLengthMax = 20;
	Jakduk.ItemsPerPageOnSearch = 10; 			// 찾기에서 페이지 당 아이템 수
	Jakduk.ItemsPerPageOnSearchGallery = 12; 	// 찾기에서 사진첩의 페이지 당 아이템 수
	Jakduk.ItemsPerPageOnGallery = 24;  		// 사진첩에서 한번 로딩할때 가져오는 그림의 수
	Jakduk.ItemsPerPageOnBoardComments = 10;
	Jakduk.isEmpty = function (str) {
		var obj = String(str);
		return !!(obj == null || obj == undefined || obj == 'null' || obj == 'undefined' || obj == '');
	};
}());

(function() {
	'use strict';

	angular.module('jakdukCommon', [])
		// mongodb id의 앞 8자리(16진수)로 Date 객체 생성.
		.filter('dateFromObjectId', function() {
			return function(objectId) {
				return new Date(parseInt(objectId.substring(0, 8), 16) * 1000);
			}
		})
		// mongodb id의 앞 8자리(16진수)를 int로 변환.
		.filter('intFromObjectId', function() {
			return function(objectId) {
				return parseInt(objectId.substring(0, 8), 16) * 1000;
			}
		})
		.controller("headerCtrl", ['$scope', '$location', function($scope, $location) {

			var port = $location.port();
			var prefix = $location.protocol() + '://' + $location.host();
			var prefixPath = (port == 80 || port == 443) ? prefix : prefix + ':' + $location.port();
			var absUrl = $location.absUrl();
			var path = absUrl.slice(prefixPath.length);

			$scope.isActive = function(viewLocation) {
				return path.match(viewLocation);
			};

			$scope.btnEnterOnHeaderSearch = function(url) {
				if ($scope.searchOnHeader.trim() < 1) {
					return;
				}

				location.href = url + '?q=' + $scope.searchOnHeader.trim() + '&w=PO;CO;GA;';
			};

			$scope.btnSearchOnHeader = function() {
				$scope.searchFocusOnHeader = true;
			};
		}])
		.directive('focus', ['$timeout', '$parse', function($timeout, $parse) {
			/**
			 * focus directive
			 * http://fiddle.jshell.net/ubenzer/9FSL4/8/
			 */
			return {
				restrict: 'A',
				link: function(scope, element, attrs) {
					scope.$watch(attrs.focus, function(newValue, oldValue) {
						if (newValue) { element[0].focus(); }
					});
					element.bind("blur", function(e) {
						$timeout(function() {
							scope.$apply(attrs.focus + "=false");
						}, 0);
					});
					element.bind("focus", function(e) {
						$timeout(function() {
							scope.$apply(attrs.focus + "=true");
						}, 0);
					})
				}
			}
		}]);

}());

// GA
(function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
		(i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
	m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
})(window,document,'script','//www.google-analytics.com/analytics.js','ga');

(function() {
	if (Jakduk.isEmpty(Jakduk.userId)) {
		ga('create', 'UA-59051176-1', 'auto');
	} else {
		ga('create', 'UA-59051176-1', {'userId' : Jakduk.userId});
	}
	ga('send', 'pageview');
}());
