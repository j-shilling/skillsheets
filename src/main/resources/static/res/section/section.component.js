angular
	.module('resources')
	.component('resourcesSection', {
		templateUrl: 'res/section/section.template.html',
		bindings: {
			type: '@'
		},
		controller: ['$scope', '$interval', 'ResourcesService', 'ViewFilter',
			function HeaderController($scope, $interval, res, filter) {
				var self = this;
				
				$scope.filter = filter;
				self.$onInit = function() {
					$scope.title = self.type.charAt(0).toUpperCase()
						+ self.type.slice(1).toLowerCase()
						+ " Resources";
					
					$scope.res = [];
					
					self.res_check = $interval(function() {
						$scope.res = res.getResources(self.type).then($scope.$apply());
					}, 10000);
					
					$scope.show = function () {
						return filter.viewing.toLowerCase() == 'all'
							|| filter.viewing.toLowerCase() == self.type.toLowerCase();
					}
				}
				
				self.$onDestroy = function() {
					$interval.cancel(self.res_check);
					$scope.title = undefined;
					$scope.show = undefined;
				}
			}]
	});