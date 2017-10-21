angular
	.module('core')
	.factory('UserService', function() {
		var userService = {
				
			access_scopes: 'profile https://www.googleapis.com/auth/drive.appfolder',
			
			auth2 : null,
			googleUser : null,
			auth_code : null,
			
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
																		scope : self.access_scopes
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
																				.get()) {
																			self.googleUser = self.auth2.currentUser
																					.get();
																			self.requestCode();
																		}
			
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
									self.requestCode();
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
			
			requestCode : function () {
				var self = this;
				
				return new Promise(function (resolve, reject) {
					
					var afterInit = function () {
						self.auth2.grantOfflineAccess({
							scope: self.access_scopes
						}).then (function (resp) {
							self.auth_code = resp.code;
							resolve();
						}, function (error) {
							reject (error);
						});
					};
					
					if (!self.isInitialized())
						self.init().then(afterInit);
					else
						afterInit();
				});
			},
			
			signOut : function() {
				var self = this;
				
				return new Promise (
						function(resolve, reject) {
							
							var onResolve = function() {
								console.log ('Sign out successfull.');
								self.googleUser = null;
								self.auth_code = null;
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
			
			getAuthCode : function() {
				return this.auth_code;
			},
			
			profile : function() {
				if (this.isSignedIn())
					return this.googleUser.getBasicProfile();
				else
					return null;
			},
			
			getRequestBody : function() {
				
				var self = this;
				var obj = {
						id_token : self.getIdToken(),
						auth_code : self.getAuthCode()
				}
				var body = JSON.stringify (obj);
				
				return body;
			}
		};
		
		return userService;
	});