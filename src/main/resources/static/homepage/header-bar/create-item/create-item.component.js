angular
	.module('headerBar')
	.component('createItem', {
		templateUrl: 'homepage/header-bar/create-item/create-item.template.html',
		controller: ['$scope',
			function HeaderController($scope) {
				$scope.onClick = function() {
					var content = document.getElementById("create-item-dropdown");
					
					if (content.className.indexOf ("w3-show") == -1) {
						content.className += " w3-show";
					} else {
						content.className = content.className.replace(" w3-show", "");
					}
				};
			}]
	});