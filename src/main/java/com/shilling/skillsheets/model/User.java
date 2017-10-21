package com.shilling.skillsheets.model;

import java.util.Optional;

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
		private String id = null;
		private String name = null;
		private String firstName = null;
		private String familyName = null;
		private String email = null;
		
		private final Tokens tokens;
		
		public Builder (Tokens tokens) {
			Preconditions.checkNotNull(tokens);
			this.tokens = tokens;
		}
		
		public Builder setId(String id) {
			this.id = id;
			return this;
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
		
		/** Construct an instance of User */
		public User build() {
			return new User (
					this.id, 
					this.name,
					this.firstName,
					this.familyName, 
					this.email,
					this.tokens);
		}
	}
	
	private final Optional<String> id;
	private final Optional<String> name;
	private final Optional<String> firstName;
	private final Optional<String> familyName;
	private final Optional<String> email;
	
	private final Tokens tokens;
	
	public User (
			String id,
			String name, 
			String firstName,
			String familyName, 
			String email,
			
			Tokens tokens) {
		
		Preconditions.checkNotNull(tokens);
		this.tokens = tokens;
		
		this.id = Optional.ofNullable(id);
		this.name = Optional.ofNullable(name);
		this.firstName = Optional.ofNullable(firstName);
		this.familyName = Optional.ofNullable(familyName);
		this.email = Optional.ofNullable(email);
	
	}

	public Optional<String> getId() {
		return id;
	}

	public Optional<String> getName() {
		return name;
	}

	public Optional<String> getFirstName() {
		return firstName;
	}

	public Optional<String> getFamilyName() {
		return familyName;
	}

	public Optional<String> getEmail() {
		return email;
	}
	
	public Optional<String> getIdToken() {
		return this.tokens.getIdToken();
	}
	
	public Optional<String> getAuthCode() {
		return this.tokens.getAuthCode();
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		if (this.getName().isPresent())
			sb.append(this.getName().get());
		else if (this.getId().isPresent())
			sb.append(this.getId().get());
		else
			return Optional.empty().toString();
		
		if (this.getEmail().isPresent())
			sb.append(" <" + this.getEmail().get() + ">");
		
		return sb.toString();
	}

}
