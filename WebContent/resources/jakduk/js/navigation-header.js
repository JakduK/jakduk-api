/**
 * navagation-header에서 쓰임. 
 */
jakdukApp.controller("headerCtrl", function($scope, $location) {
	$scope.isActive = function(viewLocation) {
		return $location.absUrl().match(viewLocation);
	}
	
	$scope.btnEnterOnHeaderSearch = function(url) {
		if ($scope.searchOnHeader.trim() < 1) {
			return;
		}
		
		location.href = url + '?q=' + $scope.searchOnHeader.trim();
	};
	
	$scope.btnSearchOnHeader = function() {
		$scope.searchFocusOnHeader = true;
	};
});

/**
 * focus directive
 * http://fiddle.jshell.net/ubenzer/9FSL4/8/
 */
jakdukApp.directive('focus', function($timeout, $parse) {
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
