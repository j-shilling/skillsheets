angular
	.module('headerBar')
	.component('accountItem', {
		templateUrl: 'core/header-bar/account-item/account-item.template.html',
		controller: ['$scope', 'UserService',
			function HeaderController($scope, user) {
				$scope.user = user;
				
				$scope.logOut = function() {
					user.signOut().then (function() { 
						$scope.$apply(); 
						var content = document.getElementById("account-item-dropdown");
						content.className = content.className.replace(" w3-show", "");
					});
				}
				
				$scope.onClick = function() {
					var content = document.getElementById("account-item-dropdown");
					
					if (content.className.indexOf ("w3-show") == -1) {
						content.className += " w3-show";
					} else {
						content.className = content.className.replace(" w3-show", "");
					}
				};
			}]
	});