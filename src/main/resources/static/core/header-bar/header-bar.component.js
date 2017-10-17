angular
	.module('headerBar')
	.component('headerBar', {
		templateUrl: 'core/header-bar/header-bar.template.html',
		controller: ['$scope', '$interval', 'UserService', 'MsgService',
			function HeaderController($scope, $interval, userService, msgService) {
				userService.init().then(function () { $scope.$apply(); });
				
				$scope.user = userService;
				$scope.msgs = msgService.getUserMsgs({}, userService.getIdToken());
				
				$interval(function() {
					$scope.msgs = msgService.getUserMsgs({}, userService.getIdToken());
				}, 30000);
				
				this.viewing = 'all';
		
				$scope.logIn = function() {
					userService.signIn().then(function () { $scope.$apply(); });
				};
				
				$scope.logOut = function() {
					userService.signOut().then(function () { $scope.$apply(); });
				};
			}]
	});