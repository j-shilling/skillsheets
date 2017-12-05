 angular
	.module('resources')
	.component('resourcesSection', {
		templateUrl: 'homepage/res/section/section.template.html',
		bindings: {
			type: '@'
		},
		controller: ['$scope', 'ResourcesService', 'ViewFilter', 'UserService',
			function HeaderController($scope, res, filter, user) {
				var self = this;
				
				$scope.filter = filter;
				self.$onInit = function() {
					$scope.title = self.type.charAt(0).toUpperCase()
						+ self.type.slice(1).toLowerCase()
						+ " Resources";
					
					user.onSignIn (function () {
						res.get(self.type).then (function () { $scope.$apply(); });
						$scope.res = res[self.type.toLowerCase()];
					}); 
					
					$scope.show = function () {
						return filter.viewing.toLowerCase() == 'all'
							|| filter.viewing.toLowerCase() == self.type.toLowerCase();
					};
				}
				
				self.$onDestroy = function() {
					$scope.title = undefined;
					$scope.show = undefined;
				}
			}]
	});