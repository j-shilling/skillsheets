angular
	.module('headerBar')
	.component('headerBar', {
		templateUrl: 'core/header-bar/header-bar.template.html',
		controller: ['$scope', '$interval', 'UserService', 'MsgService', 'ViewFilter',
			function HeaderController($scope, $interval, userService, msgService, filter) {
				userService.init().then(function () { $scope.$apply(); });
				
				$scope.user = userService;
				$scope.msgs = msgService.getMsgs();
				
				$interval(function() {
					$scope.msgs = msgService.getMsgs();
				}, 10000);
				
				$scope.filter = filter;
		
				$scope.logIn = function() {
					userService.signIn().then(function () { $scope.$apply(); });
				};
				
				$scope.logOut = function() {
					userService.signOut().then(function () { $scope.$apply(); });
				};
				
				$scope.respond = function (id, value) {
					msgService.respond (id, value);
				}
			}]
	});