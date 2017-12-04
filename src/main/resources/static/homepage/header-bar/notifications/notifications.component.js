angular
	.module('headerBar')
	.component('notifications', {
		templateUrl: 'homepage/header-bar/notifications/notifications.template.html',
		controller: ['$scope', '$interval', 'UserService', 'MsgService',
			function HeaderController($scope, $interval, userService, msgService) {
				$scope.user = userService;
				$scope.msgs = msgService.getMsgs();
				
				$scope.respond = function (id, value) {
					msgService.respond (id, value).then(function() {
						$scope.msgs = msgService.getMsgs();
					});
				}
				
				$scope.onClick = function() {
					var content = document.getElementById("notifications-item-dropdown");
					
					if (content.className.indexOf ("w3-show") == -1) {
						content.className += " w3-show";
					} else {
						content.className = content.className.replace(" w3-show", "");
					}
				};
				
				var self = this;
				
				self.$onInit = function() {
					self.msg_check = $interval (function () {
						$scope.msgs = msgService.getMsgs();
					}, 10000);
				};
				
				self.$onDestroy = function() {
					$interval.cancel(self.msg_check);
				}
			}]
	});