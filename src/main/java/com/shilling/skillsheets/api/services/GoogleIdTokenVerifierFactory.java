package com.shilling.skillsheets.api.services;

import java.util.Collections;

import org.springframework.beans.factory.FactoryBean;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

public class GoogleIdTokenVerifierFactory implements FactoryBean<GoogleIdTokenVerifier> {
	
	private static final String client_id =
			"407997016708-o3kmbrmnodmqtfmvp2j0hsu9uvh9ittn.apps.googleusercontent.com";

	@Override
	public GoogleIdTokenVerifier getObject() throws Exception {
		return new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new JacksonFactory())
				.setAudience(Collections.singletonList(client_id))
				.build();
	}

	@Override
	public Class<?> getObjectType() {
		return GoogleIdTokenVerifier.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

}
