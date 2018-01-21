package com.shilling.skillsheets.model;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nullable;

import com.google.common.base.Preconditions;

/**
 * A class to model user account information.
 * 
 * @author Jake Shilling
 *
 */
public class User {
	
	/**
	 * A mutable version of {@link#User} to make it easier to
	 * construct.
	 * 
	 * @author Jake Shilling
	 *
	 */
	public static class Builder {
		private final String id;
		
		private String name = null;
		private String firstName = null;
		private String familyName = null;
		private String email = null;
		private boolean teacher = false;
		
		public Builder (String id) {
			Preconditions.checkNotNull(id);
			Preconditions.checkArgument(!id.isEmpty());
			
			this.id = id;
		}
		
		public Builder setName(String name) {
			this.name = name;
			return this;
		}
		public Builder setFirstName(String firstName) {
			this.firstName = firstName;
			return this;
		}
		public Builder setFamilyName(String familyName) {
			this.familyName = familyName;
			return this;
		}
		public Builder setEmail(String email) {
			this.email = email;
			return this;
		}
		
		public Builder setTeacher (boolean teacher) {
			this.teacher = teacher;
			return this;
		}
		
		/** Construct an instance of User */
		public User build() {
			return new User (
					this.id, 
					this.name,
					this.firstName,
					this.familyName, 
					this.email,
					this.teacher);
		}
	}
	
	private final String id;
	private final String name;
	private final String firstName;
	private final String familyName;
	private final String email;
	
	private final boolean teacher;
	
	public User (
			String id,
			
			@Nullable String name, 
			@Nullable String firstName,
			@Nullable String familyName, 
			@Nullable String email,
			
			boolean teacher) {
		
		Preconditions.checkNotNull(id);
		Preconditions.checkArgument(!id.isEmpty());
		
		this.id = id;
		this.name = name;
		this.firstName = firstName;
		this.familyName = familyName;
		this.email = email;
		this.teacher = teacher;
	
	}

	public String getId() {
		return id;
	}

	public Optional<String> getName() {
		return Optional.ofNullable(name);
	}

	public Optional<String> getFirstName() {
		return Optional.ofNullable(firstName);
	}

	public Optional<String> getFamilyName() {
		return Optional.ofNullable(familyName);
	}

	public Optional<String> getEmail() {
		return Optional.ofNullable(email);
	}
	
	public boolean isTeacher() {
		return this.teacher;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		if (this.getName().isPresent())
			sb.append(this.getName().get());
		else
			sb.append(this.getId());
		
		if (this.getEmail().isPresent())
			sb.append(" <" + this.getEmail().get() + ">");
		
		return sb.toString();
	}
	
	@Override
	public boolean equals (Object obj) {
		if (obj instanceof User) {
			return this.getId().equals(((User) obj).getId());
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(this.getId());
	}

}
