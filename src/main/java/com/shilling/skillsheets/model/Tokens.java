package com.shilling.skillsheets.model;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A class to wrap a Google Token ID
 * 
 * @author Jake Shilling
 *
 */
public class Tokens {
	
	private final Optional<String> id_token;
	private final Optional<String> auth_code;
	
	@JsonCreator
	public Tokens(@JsonProperty("id_token") String id_token,
			@JsonProperty("auth_code") String auth_code) {
		this.id_token = Optional.ofNullable(id_token);
		this.auth_code = Optional.ofNullable(auth_code);
	}
	
	@JsonProperty ("id_token")
	private String getIdTokenPriv() {
		return this.id_token.isPresent() ? this.id_token.get() : null;
	}
	
	@JsonProperty ("auth_code")
	private String getAuthCodePriv() {
		return this.auth_code.isPresent() ? this.auth_code.get() : null;
	}
	
	public Optional<String> getIdToken() {
		return this.id_token;
	}
	
	public Optional<String> getAuthCode() {
		return this.auth_code;
	}
	

}
