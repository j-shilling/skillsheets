angular
	.module('headerBar')
	.component('headerBar', {
		templateUrl: 'homepage/header-bar/header-bar.template.html',
		controller: ['$scope', 'UserService',
			function HeaderController($scope, userService) {
				userService.init().then(function () { $scope.$apply(); });
				$scope.user = userService;
				
				$scope.logIn = function () {
					userService.signIn().then (function() { $scope.$apply(); });
				}
			}]
	});