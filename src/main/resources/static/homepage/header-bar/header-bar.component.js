angular
	.module('headerBar')
	.component('headerBar', {
		templateUrl: 'homepage/header-bar/header-bar.template.html',
		controller: ['$scope', '$interval', 'UserService', 'MsgService', 'ViewFilter',
			function HeaderController($scope, $interval, userService, msgService, filter) {
				userService.init().then(function () { $scope.$apply(); });
				$scope.user = userService;
				
				$scope.logIn = function () {
					userService.signIn().then (function() { $scope.$apply(); });
				}
			}]
	});