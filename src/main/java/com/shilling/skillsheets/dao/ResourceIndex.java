package com.shilling.skillsheets.dao;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class ResourceIndex {
	
	private final Map<UUID, ResourceDao<?>> index;
	
	private ResourceIndex () {
		this.index = new HashMap<>();
	}
	
	public void put (UUID uuid, ResourceDao<?> dao) {
		this.index.put(uuid, dao);
	}
	
	public Optional<ResourceDao<?>> get (UUID uuid) {
		return Optional.ofNullable(this.index.get (uuid));
	}
	
	public boolean remove (UUID uuid) {
		return null != this.index.remove (uuid);
	}
	
}