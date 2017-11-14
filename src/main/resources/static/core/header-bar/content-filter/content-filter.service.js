angular
	.module('headerBar')
	.factory('ViewFilter', function() {
		
		return {
			viewing: 'all',
			query: ''
		};
		
	});