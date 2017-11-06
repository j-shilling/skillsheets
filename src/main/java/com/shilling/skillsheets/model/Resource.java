package com.shilling.skillsheets.model;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.api.client.util.Preconditions;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE)
public class Resource {

	public static enum Type {
		CONTACT_GROUP, SKILL_SHEET
	}

	private final UUID uuid;
	private final Type type;

	@Nullable
	@JsonProperty("name")
	private final String name;
	
	public Resource(
			UUID uuid,
			@Nullable String name,
			Type type) {
		
		Preconditions.checkNotNull(uuid);
		Preconditions.checkNotNull(type);
		
		this.uuid = uuid;
		this.type = type;
		
		if (name != null && !name.isEmpty())
			this.name = name;
		else
			this.name = null;
	}

	@JsonCreator
	public Resource(
			@JsonProperty("uuid") @Nullable String uuid, 
			@JsonProperty("name") @Nullable String name,
			@JsonProperty("type") Type type) {
		
		this (uuid == null ? UUID.randomUUID() : UUID.fromString(uuid),
				name,
				type);
	}

	public Resource(String uuid, Type type) {
		this(uuid, null, type);
	}
	
	public Resource(UUID uuid, Type type) {
		this(uuid, null, type);
	}
	
	public Resource(Type type) {
		this (UUID.randomUUID(), null, type);
	}
	
	@JsonIgnore
	public UUID getUUID() {
		return this.uuid;
	}

	@JsonProperty("uuid")
	public String getId() {
		return this.uuid.toString();
	}

	@JsonProperty("type")
	public Type getType() {
		return this.type;
	}

	@JsonIgnore
	public Optional<String> getName() {
		return Optional.ofNullable(this.name);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append(this.getType().toString() + ": ");
		if (this.getName().isPresent())
			sb.append(this.getName().get());
		else
			sb.append(this.getId());

		return sb.toString();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Resource) {
			return this.uuid.equals(((Resource) obj).uuid);
		}

		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.uuid);
	}
	
	public Resource setName (String name) {
		Preconditions.checkNotNull(name);
		Preconditions.checkArgument(!name.isEmpty());
		
		return new Resource (this.getId(), name, this.getType());
	}

}
