package com.shilling.skillsheets.model;

import java.io.IOException;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.shilling.skillsheets.dao.User;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE)
public class SkillSheet {
	
	private static enum Access {
		TEACHER,
		STUDENT
	}
	
	public static class Builder {
		/* Available, but hidden */
		private final UUID uuid;
		private final Map<User, EnumSet<Access>> accessors;
		
		/* Available to everyone */
		private @Nullable String name;
		private int numberOfSkills;
		private @Nullable Date dueDate;
		
		/* Available to teachers */
		private @Nullable Boolean visible;
		private @Nullable Date availableFrom;
		private @Nullable Date availableUntil;
		private @Nullable Float averageGrade;
		
		/* Available to students */
		private @Nullable Float grade;
		
		public Builder () {
			this.uuid = UUID.randomUUID();
			
			this.name = null;
			this.numberOfSkills = 0;
			this.dueDate = null;
			
			this.visible = false;
			this.availableFrom = null;
			this.availableUntil = null;
			this.averageGrade = (float) 0.0;
			
			this.grade = (float) 0.0;
			
			this.accessors = new HashMap<>();
		}
		
		public Builder (UUID uuid) {
			this.uuid = uuid;
			
			this.name = null;
			this.numberOfSkills = 0;
			this.dueDate = null;
			
			this.visible = false;
			this.availableFrom = null;
			this.availableUntil = null;
			this.averageGrade = (float) 0.0;
			
			this.grade = (float) 0.0;
			
			this.accessors = new HashMap<>();
		}
		
		public Builder (SkillSheet skillsheet) {
			this.uuid = skillsheet.uuid;
			this.name = skillsheet.name;
			this.numberOfSkills = skillsheet.numberOfSkills;
			this.dueDate = skillsheet.dueDate;
			this.visible = skillsheet.visible;
			this.availableFrom = skillsheet.availableFrom;
			this.availableUntil = skillsheet.availableUntil;
			this.averageGrade = skillsheet.averageGrade;
			this.grade = skillsheet.grade;
			this.accessors = new HashMap<>();
			this.accessors.putAll(skillsheet.accessors);
		}
		
		public Builder setName (@Nullable String name) {
			this.name = name;
			return this;
		}
		
		public Builder setNumberOfSkills (int numberOfSkills) {
			Preconditions.checkArgument(numberOfSkills >= 0);
			
			this.numberOfSkills = numberOfSkills;
			return this;
		}
		
		public Builder setDueDate (@Nullable Date dueDate) {
			this.dueDate = dueDate;
			return this;
		}
		
		public Builder setVisible (@Nullable Boolean visible) {
			this.visible = visible;
			return this;
		}
		
		public Builder setAvailableFrom (@Nullable Date availableFrom) {
			this.availableFrom = availableFrom;
			return this;
		}
		
		public Builder setAvailableUntil (@Nullable Date availableUntil) {
			this.availableUntil = availableUntil;
			return this;
		}
		
		public Builder setAverageGrade (@Nullable Float averageGrade) {
			this.averageGrade = averageGrade;
			return this;
		}
		
		public Builder setGrade (@Nullable Float grade) {
			this.grade = grade;
			return this;
		}
		
		public Builder addTeacher (User user) {
			Preconditions.checkNotNull(user);
			try {
				Preconditions.checkArgument(user.isTeacher());
			} catch (IOException e) {
				return this;
			}
			
			EnumSet<Access> access = null;
			if (this.accessors.containsKey(user)) {
				access = this.accessors.get(user);
			} else {
				access = EnumSet.noneOf(Access.class);
			}
			access.add(Access.TEACHER);
			
			this.accessors.put(user, access);
			return this;
		}
		
		public Builder delTeacher (User user) {
			Preconditions.checkNotNull(user);
			try {
				Preconditions.checkArgument(user.isTeacher());
			} catch (IOException e) {
				return this;
			}
			
			EnumSet<Access> access = this.accessors.get(user);
			if (access != null) {
				access.remove(Access.TEACHER);
				this.accessors.put(user, access);
			}
			
			return this;
		}
		
		public Builder addStudent (User user) {
			Preconditions.checkNotNull(user);
			
			EnumSet<Access> access = null;
			if (this.accessors.containsKey(user)) {
				access = this.accessors.get(user);
			} else {
				access = EnumSet.noneOf(Access.class);
			}
			access.add(Access.STUDENT);
			
			this.accessors.put(user, access);
			return this;
		}
		
		public Builder delStudent (User user) {
			Preconditions.checkNotNull(user);
			
			EnumSet<Access> access = this.accessors.get(user);
			if (access != null) {
				access.remove(Access.STUDENT);
				this.accessors.put(user, access);
			}
			
			return this;
		}
		
		public SkillSheet build() {
			return new SkillSheet (
					this.uuid,
					this.name,
					this.numberOfSkills,
					this.dueDate,
					this.visible,
					this.availableFrom,
					this.availableUntil,
					this.averageGrade,
					this.grade,
					this.accessors);
		}
	}

	/* Available, but hidden */
	@JsonIgnore
	private final UUID uuid;
	@JsonProperty ("accessors")
	private final Map<User, EnumSet<Access>> accessors;
	
	/* Available to everyone */
	@JsonProperty ("name")
	private final @Nullable String name;
	@JsonProperty ("numberOfSkills")
	private final int numberOfSkills;
	@JsonProperty ("dueDate")
	private final @Nullable Date dueDate;
	
	/* Available to teachers */
	@JsonProperty ("visible")
	private final @Nullable Boolean visible;
	@JsonProperty ("availableFrom")
	private final @Nullable Date availableFrom;
	@JsonProperty ("availableUntil")
	private final @Nullable Date availableUntil;
	@JsonProperty ("averageGrade")
	private final @Nullable Float averageGrade;
	
	/* Available to students */
	@JsonProperty ("grade")
	private final @Nullable Float grade;
	
	private SkillSheet (
			UUID uuid,
			@Nullable String name, 
			int numberOfSkills, 
			@Nullable Date dueDate, 
			@Nullable Boolean visible, 
			@Nullable Date availableFrom,
			@Nullable Date availableUntil, 
			@Nullable Float averageGrade,
			@Nullable Float grade,
			Map<User, EnumSet<Access>> accessors) {
		
		Preconditions.checkNotNull(uuid);
		Preconditions.checkArgument(numberOfSkills >= 0);
		
		this.uuid = uuid;
		
		this.name = name;
		this.numberOfSkills = numberOfSkills;
		this.dueDate = dueDate;
		
		this.visible = visible;
		this.availableFrom = availableFrom;
		this.availableUntil = availableUntil;
		this.averageGrade = averageGrade;
		
		this.grade = grade;
		
		this.accessors = new ImmutableMap.Builder<User, EnumSet<Access>>()
				.putAll(accessors)
				.build();
	}
	
	@JsonCreator
	private SkillSheet (
			@JsonProperty ("uuid") String uuidStr,
			@JsonProperty ("name") String name,
			@JsonProperty ("numberOfSkills") int numberOfSkills,
			@JsonProperty ("dueDate") Date dueDate,
			@JsonProperty ("visible") Boolean visible,
			@JsonProperty ("availableFrom") Date availableFrom,
			@JsonProperty ("availableUntil") Date availableUntil,
			@JsonProperty ("averageGrade") Float averageGrade,
			@JsonProperty ("grade") Float grade,
			@JsonProperty ("accessors") Map<User, EnumSet<Access>> accessors) {
		
		this (UUID.fromString(uuidStr),
				name,
				numberOfSkills,
				dueDate,
				visible,
				availableFrom,
				availableUntil,
				averageGrade,
				grade,
				accessors);
	}

	@JsonProperty("uuid")
	public String getUuid() {
		return this.uuid.toString();
	}

	@JsonIgnore
	public Optional<String> getName() {
		return Optional.ofNullable(this.name);
	}

	@JsonIgnore
	public int getNumberOfSkills() {
		return this.numberOfSkills;
	}

	@JsonIgnore
	public Optional<Date> getDueDate() {
		return Optional.ofNullable(this.dueDate);
	}

	@JsonIgnore
	public Optional<Boolean> getVisible() {
		return Optional.ofNullable(visible);
	}

	@JsonIgnore
	public Optional<Date> getAvailableFrom() {
		return Optional.ofNullable(availableFrom);
	}

	@JsonIgnore
	public Optional<Date> getAvailableUntil() {
		return Optional.ofNullable(availableUntil);
	}

	@JsonIgnore
	public Optional<Float> getAverageGrade() {
		return Optional.ofNullable(averageGrade);
	}

	@JsonIgnore
	public Optional<Float> getGrade() {
		return Optional.ofNullable(grade);
	}
	
	@JsonIgnore
	public boolean isTeacher(User user) {
		EnumSet<Access> access = this.accessors.get(user);
		if (access == null)
			return false;
		
		return access.contains(Access.TEACHER);
	}
	
	@JsonIgnore
	public boolean isStudent(User user) {
		EnumSet<Access> access = this.accessors.get(user);
		if (access == null)
			return false;
		
		return access.contains(Access.STUDENT);
	}
	
	@Override
	public String toString() {
		if (this.getName().isPresent())
			return this.getName().get();
		else
			return "SkillSheet: " + this.getUuid();
	}
	
	@Override
	public boolean equals (Object obj) {
		if (obj instanceof SkillSheet)
			return this.uuid.equals(((SkillSheet) obj).uuid);
		if (obj instanceof UUID)
			return this.uuid.equals(obj);
		if (obj instanceof String)
			return this.uuid.equals(UUID.fromString((String) obj));
			
		return false;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(this.uuid);
	}
	
}
