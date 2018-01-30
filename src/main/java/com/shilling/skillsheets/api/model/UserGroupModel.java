package com.shilling.skillsheets.api.model;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;

public class UserGroupModel extends ResourceModel {

	@JsonProperty ("name")
	private final @Nullable String name;
	@JsonProperty ("members")
	private final Collection<UserModel> members;
	
	public UserGroupModel (
			UUID uuid, 
			@Nullable String name,
			Iterable<UserModel> members) {
		
		super (uuid);
		
		Preconditions.checkNotNull(members);
		
		this.name = name;
		
		this.members = new ImmutableSet.Builder<UserModel>()
				.addAll(members)
				.build();
	}
	
	public UserGroupModel (
			UUID uuid, 
			Optional<String> name,
			Iterable<UserModel> members) {
		this (uuid, name.isPresent() ? name.get() : null, members);
	}
			
	
	@JsonCreator
	private UserGroupModel (
			@JsonProperty ("uuid") String uuid,
			@JsonProperty ("name") @Nullable String name,
			@JsonProperty ("members") Collection<UserModel> members) {
		this (UUID.fromString(uuid), name, members);
	}
	
	@JsonIgnore
	public Optional<String> getName () {
		return Optional.ofNullable(this.name);
	}
	
	@JsonIgnore
	public Collection<UserModel> getMembers() {
		return this.members;
	}
}
