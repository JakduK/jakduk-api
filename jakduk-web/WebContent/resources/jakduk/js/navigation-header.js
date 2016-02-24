/**
 * navagation-header에서 쓰임. 
 */
angular.module('jakdukApp').controller("headerCtrl", function($scope, $location) {

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
});

/**
 * focus directive
 * http://fiddle.jshell.net/ubenzer/9FSL4/8/
 */
angular.module('jakdukApp').directive('focus', function($timeout, $parse) {
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
});
