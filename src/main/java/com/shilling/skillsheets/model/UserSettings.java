package com.shilling.skillsheets.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.api.client.repackaged.com.google.common.base.Preconditions;

@JsonAutoDetect(fieldVisibility=JsonAutoDetect.Visibility.NONE)
public class UserSettings {
	
	private enum View {
		ALL,
		STUDENT,
		TEACHER,
		OBJSERVER
	};
	
	private final View viewing;
	
	@JsonCreator
	public UserSettings(
			@JsonProperty ("viewing") View viewing) {
		Preconditions.checkNotNull(viewing);
		
		this.viewing = viewing;
	}
	
	@JsonProperty ("viewing")
	public View getView () {
		return this.viewing;
	}

}
