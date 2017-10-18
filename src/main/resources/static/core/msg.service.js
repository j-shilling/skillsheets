angular
	.module('core')
	.factory('MsgService', ['$resource', 'UserService',
		function($resource, userService) {
			var self = $resource('/api/messages/:msgid/:action', {}, {
				getUserMsgs: {
					method: 'POST',
					hasBody: true,
					isArray: true
				},
				
				respondToMsg: {
					method: 'PUT',
					hasBody: true,
					isArray: false
				}
			});
			
			self.getMsgs = function () {
				return self.getUserMsgs({}, userService.getIdToken());
			}
			
			self.respond = function (id, value) {
				return self.respondToMsg(
						{ 
							msgid: id,
							action: value
						},
						
						userService.getIdToken());
			}
			
			return self;
	}]);