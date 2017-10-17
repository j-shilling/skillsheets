angular
	.module('core')
	.factory('MsgService', ['$resource', 'UserService',
		function($resource, userService) {
			return $resource('/api/messages', {}, {
				getUserMsgs: {
					method: 'POST',
					hasBody: true,
					isArray: true
				}
			});
	}]);