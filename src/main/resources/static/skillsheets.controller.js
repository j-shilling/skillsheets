angular
	.module('skillSheetsApp')
	.controller('skillSheets',
			['$scope', 'UserService', 
				function($scope, userService) {
				
				userService.init().then(function () { $scope.$apply(); });
		
				$scope.user = userService;
	}]);