package com.shilling.skillsheets.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Preconditions;

@JsonAutoDetect(fieldVisibility=JsonAutoDetect.Visibility.NONE)
public class Resources {
	
	private final Map<UUID, Resource> teacher;
	private final Map<UUID, Resource> student;
	private final Map<UUID, Resource> observer;
	
	public Resources() {
		this.teacher = new HashMap<>();
		this.student = new HashMap<>();
		this.observer = new HashMap<>();
	}
	
	@JsonCreator
	public Resources (
			@JsonProperty ("teacher") Iterable<Resource> teacher,
			@JsonProperty ("student") Iterable<Resource> student,
			@JsonProperty ("observer") Iterable<Resource> observer) {
		
		this();
		
		for (Resource r : teacher)
			this.teacher.put(r.getUUID(), r);
		for (Resource r : student)
			this.student.put(r.getUUID(), r);
		for (Resource r : observer)
			this.observer.put(r.getUUID(), r);
	}
	
	@JsonProperty ("teacher")
	public Collection<Resource> getTeacherResources() {
		return this.teacher.values();
	}
	
	@JsonProperty ("student")
	public Collection<Resource> getStudentResources() {
		return this.student.values();
	}
	
	@JsonProperty ("observer")
	public Collection<Resource> getObserverResources() {
		return this.observer.values();
	}
	
	public boolean addTeacherResource (Resource resource) {
		Preconditions.checkNotNull(resource);
		return null != this.teacher.put(resource.getUUID(), resource);
	}
	
	public boolean addStudentResource (Resource resource) {
		Preconditions.checkNotNull(resource);
		return null != this.student.put(resource.getUUID(), resource);
	}
	
	public boolean addObserverResource (Resource resource) {
		Preconditions.checkNotNull(resource);
		return null != this.observer.put(resource.getUUID(), resource);
	}
	
	public Optional<Resource> get (UUID uuid) {
		Optional<Resource> ret = 
				Optional.ofNullable(this.teacher.get(uuid));
		if (ret.isPresent())
			return ret;
		
		ret =
				Optional.ofNullable(this.student.get(uuid));
		if (ret.isPresent())
			return ret;
		
		ret =
				Optional.ofNullable(this.observer.get(uuid));
		return ret;
	}
	
	public Optional<Resource> get (String uuid) {
		try {
			return this.get(UUID.fromString(uuid));
		} catch (IllegalArgumentException e) {
			return Optional.empty();
		}
	}
	
	public boolean del (UUID uuid) {
		return null != this.teacher.remove(uuid)
				|| null != this.student.remove(uuid)
				|| null != this.observer.remove(uuid);
	}
	
	public boolean del (String uuid) {
		try {
			return this.del(UUID.fromString(uuid));
		} catch (IllegalArgumentException e) {
			return false;
		}
	}
	
	public boolean del (Resource resource) {
		return this.del(resource.getUUID());
	}
	
}
