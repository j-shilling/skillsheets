angular
	.module('core')
	.factory('UserService', function() {
		var userService = {
			auth2 : null,
			googleUser : null,
			
			init : function() {
				var self = this;
				
				return new Promise(
						function(resolve, reject) {
							if (!self.isInitialized()) {
								gapi
										.load(
												'client:auth2',
												function() {
													gapi.auth2
															.init(
																	{
																		client_id : '407997016708-o3kmbrmnodmqtfmvp2j0hsu9uvh9ittn.apps.googleusercontent.com',
																		fetch_basic_profile : true,
																		scope : 'profile'
																	})
															.then(
																	function() {
																		self.auth2 = gapi.auth2
																				.getAuthInstance();
																		self.auth2.isSignedIn.listen(function (val) {
																			if (!val)
																				userService.googleUser = null;
																		});
																		self.auth2.currentUser.listen(function (user) {
																			self.googleUser = user;
																		});
			
																		if (self.auth2.isSignedIn
																				.get())
																			self.googleUser = self.auth2.currentUser
																					.get();
			
																		resolve();
																	}, function(error) {
																		reject(error);
																	});
												});
							} else {
								resolve();
							}
						});
			},
			
			isInitialized : function() {
				return this.auth2 != null;
			},
			
			isSignedIn : function() {
				return this.googleUser != null;
			},
			
			signIn : function() {
				var self = this;
				
				return new Promise (
						function(resolve, reject) {
							
							if (self.isSignedIn()) {
								console.log("User already signed in.");
								resolve();
							} else {
								var onSuccess = function () {
									self.googleUser = self.auth2.currentUser.get();
									console.log("Logged in as " + self.profile().getName());
									resolve();
								};
								
								var onFailure = function (error) {
									console.log("Login failed! " + error.message);
									reject();
								}
								
								if (self.isInitialized()) {
									self.auth2.signIn().then(onSuccess, onFailure);
								} else {
									self.init().then(function () {
										self.signIn().then(resolve, reject);
									});
								}
							}
						});
			},
			
			signOut : function() {
				var self = this;
				
				return new Promise (
						function(resolve, reject) {
							
							var onResolve = function() {
								console.log ('Sign out successfull.');
								self.googleUser = null;
								resolve();
							}
							
							var onReject = function(error) {
								console.log ('Sign out error:' + error);
								reject();
							}
							
							if (self.isInitialized())
								self.auth2.signOut().then(onResolve, onReject);
							else
								self.init().then(self.signOut(resolve,reject));
						});
			},
			
			getIdToken : function() {
				if (this.isInitialized() && this.isSignedIn())
					return this.googleUser.getAuthResponse().id_token;
				else
					return null;
			},
			
			profile : function() {
				if (this.isSignedIn())
					return this.googleUser.getBasicProfile();
				else
					return null;
			}
		};
		
		return userService;
	});