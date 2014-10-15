/**
 * navagation-header에서 쓰임. 
 */
jakdukApp.controller("headerCtrl", function($scope, $location) {
	$scope.isActive = function(viewLocation) {
		return $location.absUrl().match(viewLocation);
	}
});
