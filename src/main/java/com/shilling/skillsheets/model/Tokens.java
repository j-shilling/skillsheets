package com.shilling.skillsheets.model;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A class to wrap a Google Token ID
 * 
 * @author Jake Shilling
 *
 */
public class Tokens {
	
	@JsonProperty ("id_token")
	private final @Nullable String id_token;
	
	@JsonCreator
	public Tokens(@JsonProperty("id_token") @Nullable String id_token) {
		
		if (id_token != null && id_token.isEmpty())
			this.id_token = null;
		else
			this.id_token = id_token;
	
	}
	
	@JsonIgnore
	public Optional<String> getIdToken() {
		return Optional.ofNullable(this.id_token);
	}
	
	@Override
	public String toString() {
		if (this.getIdToken().isPresent())
			return this.getIdToken().get();
		else
			return Optional.empty().toString();
	}
	
	@Override
	public boolean equals (Object obj) {
		if (this.getIdToken().isPresent()) {
			if (obj instanceof String)
				return this.getIdToken().get().equals(obj);
		}
		
		if (obj instanceof Optional<?>)
			return this.getIdToken().equals(obj);
		if (obj instanceof Tokens)
			return this.getIdToken().equals(((Tokens) obj).getIdToken());
		
		return false;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(this.getIdToken());
	}
}
