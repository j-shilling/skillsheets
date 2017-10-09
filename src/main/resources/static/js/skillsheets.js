var skillsheets = {
	auth2 : null,
	googleUser : null,

	initAuthentication : function() {
		return new Promise(
				function(resolve, reject) {
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
															skillsheets.auth2 = gapi.auth2
																	.getAuthInstance();
															skillsheets.auth2.isSignedIn.listen(function (val) {
																if (!val)
																	skillsheets.googleUser = null;
															});
															skillsheets.auth2.currentUser.listen(function (user) {
																skillsheets.googleUser = user;
															});

															if (skillsheets.auth2.isSignedIn
																	.get())
																skillsheets.googleUser = skillsheets.auth2.currentUser
																		.get();

															resolve();
														}, function(error) {
															reject(error);
														});
									});
				});
	},

	authenticationIsInitialized : function() {
		return this.auth2 != null;
	},
	
	isSignedIn : function() {
		return this.googleUser != null;
	},

	signIn : function() {
		return new Promise (
				function(resolve, reject) {
					
					if (skillsheets.isSignedIn()) {
						resolve();
					} else {
						var afterSignIn = function () {
							skillsheets.googleUser = skillsheets.auth2.currentUser.get();
							resolve();
						};
						
						if (skillsheets.authenticationIsInitialized()) {
							skillsheets.auth2.signIn().then(afterSignIn, reject);
						} else {
							skillsheets.initAuthentication().then(function () {
								skillsheets.auth2.signIn().then(afterSignIn, reject);
							});
						}
					}
				});
	},
	
	getIdToken : function() {
		if (this.authenticationIsInitialized() && this.isSignedIn())
			return this.googleUser.getAuthResponse().id_token;
		else
			return null;
	},
	
	redirectWithToken : function (url) {
		var form = document.createElement('form');
		form.method = 'post';
		form.action = url;

		var input = document.createElement('input');
		input.type = 'hidden';
		input.name = 'tokenid';
		input.value = this.getIdToken();
		form.appendChild(input);

		form.submit();
	},
	
	profile : function() {
		if (this.isSignedIn())
			return this.googleUser.getBasicProfile();
		else
			return null;
	}
};