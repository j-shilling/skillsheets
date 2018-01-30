package com.shilling.skillsheets.api.model;

import java.util.Optional;
import java.util.UUID;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UserModel extends ResourceModel {
	
	@JsonProperty ("id")
	private final @Nullable String id;
	@JsonProperty ("email")
	private final @Nullable String email;
	@JsonProperty ("name")
	private final @Nullable String name;
	
	public UserModel (
			UUID uuid,
			@Nullable String id,
			@Nullable String email,
			@Nullable String name) {
		
		super (uuid);
		
		this.id = id;
		this.email = email;
		this.name = name;
	}
	
	public UserModel (
			UUID uuid,
			Optional<String> id,
			Optional<String> email,
			Optional<String> name) {
		
		this (
				uuid,
				id.isPresent() ? id.get() : null,
				email.isPresent() ? email.get() : null,
				name.isPresent() ? name.get() : null);
	}
	
	@JsonCreator
	private UserModel (
			String uuid,
			@Nullable String id,
			@Nullable String email,
			@Nullable String name) {
		this (UUID.fromString(uuid), id, email, name);
	}
	
	@JsonIgnore
	public Optional<String> getId() {
		return Optional.ofNullable(this.id);
	}
	
	@JsonIgnore
	public Optional<String> getEmail() {
		return Optional.ofNullable(this.email);
	}
	
	@JsonIgnore
	public Optional<String> getName() {
		return Optional.ofNullable(this.name);
	}

}
