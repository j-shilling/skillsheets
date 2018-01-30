package com.shilling.skillsheets.api.model;

import java.util.Objects;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class ResourceModel {

	@JsonIgnore
	private final UUID uuid;
	
	protected ResourceModel (UUID uuid) {
		this.uuid = uuid;
	}
	
	@JsonProperty ("uuid")
	public String getUuid() {
		return this.uuid.toString();
	}
	
	@Override
	public boolean equals (Object obj) {
		if (obj instanceof ResourceModel)
			return this.uuid.equals(((ResourceModel) obj).uuid);
		return false;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(this.uuid);
	}
}
