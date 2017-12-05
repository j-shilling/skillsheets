angular
	.module('resources')
	.factory('ResourcesService', ['$resource', 'UserService',
		function($resource, userService) {
			var self = $resource('/api/my/:type', {}, {
				getUserResources: {
					method: 'POST',
					hasBody: true,
					isArray: true
				},
				
				createUserResource: {
					method: 'PUT',
					hasBody: true,
					isArray: false
				}
			});
			
			self.getResources = function (val) {
				if (val == undefined || val == null)
					return $q(function(resolve, reject) {
						resolve();
					})
				else
					return self.getUserResources({
						type: val.toLowerCase()
					}, userService.getRequestBody());
			}
			
			return self;
	}]);