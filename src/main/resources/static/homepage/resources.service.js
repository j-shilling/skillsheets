angular
	.module('homepage')
	.factory('ResourcesService', ['$resource', 'UserService',
		
		function($resource, userService) {
		
			var ResService = { 
					teacher: [], 
					student: [], 
					observer: [],
					
					getapi: $resource('/api/my/:type', {}, {
						getUserResources: {
							method: 'POST',
							hasBody: true,
							isArray: true
						}
					}),
					
					createapi: $resource('/api/create/:type', {}, {
						createUserResource: {
							method: 'POST',
							hasBody: true
						}
					}),
					
					getTeacherResources: function () {
						
						return new Promise (function (resolve, reject) {
							
							ResService.teacher = ResService.getapi.getUserResources ({
								type: 'teacher'
							}, userService.getRequestBody());
							
							resolve();
						});
						
					},
					
					getStudentResources: function () {
						
						return new Promise (function (resolve, reject) {
							
							ResService.student = ResService.getapi.getUserResources ({
								type: 'student'
							}, userService.getRequestBody());
							
							resolve();
						});
						
					},
					
					getObserverResources: function () {
						
						return new Promise (function (resolve, reject) {
							
							ResService.observer = ResService.getapi.getUserResources ({
								type: 'observer'
							}, userService.getRequestBody());
							
							resolve();
						});
						
					},
					
					get: function(val) {
						
						return new Promise (function (resolve, reject) {
							ResService[val.toLowerCase()] =
								ResService.getapi.getUserResources ({
									type: val.toLowerCase()
								}, userService.getRequestBody());
						});
						
					},
					
					createSkillSheet: function() {
						
						return new Promise (function (resolve, reject) {
							var ref = ResService.createapi.createUserResource({type:'sheet'}, userService.getRequestBody())
							ResService.teacher.push (ref);
							
							resolve();
						});
						
					},
					
					createUserGroup: function() {
						
						return new Promise (function (resolve, reject) {
							var ref = ResService.createapi.createUserResource({type:'group'}, userService.getRequestBody())
							ResService.teacher.push (ref);
							
							resolve();
						});
						
					}
			};
			
			return ResService;
	}]);