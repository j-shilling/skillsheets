package com.shilling.skillsheets.model;

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
		private String id = "";
		private String name = "";
		private String firstName = "";
		private String familyName = "";
		private String email = "";
		
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
			return new User (this.id, this.name, this.firstName,
					this.familyName, this.email);
		}
	}
	
	private final String id;
	private final String name;
	private final String firstName;
	private final String familyName;
	private final String email;
	
	public User (String id, String name, String firstName,
			String familyName, String email) {
		
		this.id = id != null ? id : "";
		this.name = name != null ? name : "";
		this.firstName = firstName != null ? firstName : "";
		this.familyName = familyName != null ? familyName : "";
		this.email = email != null ? email : "";
	
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getFamilyName() {
		return familyName;
	}

	public String getEmail() {
		return email;
	}

}
