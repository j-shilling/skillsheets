package com.shilling.skillsheets.model;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import javax.annotation.Nullable;

public class SkillSheet {

	/* Available, but hidden */
	private final UUID uuid;
	
	/* Available to everyone */
	private final @Nullable String name;
	private final int numberOfSkills;
	private final @Nullable Date dueDate;
	
	/* Available to teachers */
	private final @Nullable Boolean visible;
	private final @Nullable Date availableFrom;
	private final @Nullable Date availableUntil;
	private final @Nullable Float averageGrade;
	
	/* Available to students */
	private final @Nullable Float grade;
	
	/** Create new SkillSheet with default values */
	public SkillSheet () {
		this.uuid = UUID.randomUUID();
		
		this.name = null;
		this.numberOfSkills = 0;
		this.dueDate = null;
		
		this.visible = null;
		this.availableFrom = null;
		this.availableUntil = null;
		this.averageGrade = null;
		
		this.grade = null;
	}

	public String getUuid() {
		return this.uuid.toString();
	}

	public Optional<String> getName() {
		return Optional.ofNullable(this.name);
	}

	public int getNumberOfSkills() {
		return this.numberOfSkills;
	}

	public Optional<Date> getDueDate() {
		return Optional.ofNullable(this.dueDate);
	}

	public Optional<Boolean> getVisible() {
		return Optional.ofNullable(visible);
	}

	public Optional<Date> getAvailableFrom() {
		return Optional.ofNullable(availableFrom);
	}

	public Optional<Date> getAvailableUntil() {
		return Optional.ofNullable(availableUntil);
	}

	public Optional<Float> getAverageGrade() {
		return Optional.ofNullable(averageGrade);
	}

	public Optional<Float> getGrade() {
		return Optional.ofNullable(grade);
	}
	
	
	
}
