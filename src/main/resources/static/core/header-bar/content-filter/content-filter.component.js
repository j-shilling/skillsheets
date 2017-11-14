angular
	.module('headerBar')
	.component('contentFilter', {
		templateUrl: 'core/header-bar/content-filter/content-filter.template.html',
		controller: ['$scope', 'UserService', 'ViewFilter',
			function HeaderController($scope, user, filter) {
				$scope.filter = filter;
				$scope.user = user;
			}]
	});