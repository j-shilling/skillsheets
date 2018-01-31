package com.shilling.skillsheets.dao.local;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

import javax.annotation.Nullable;

import com.shilling.skillsheets.api.model.ResourceModel;
import com.shilling.skillsheets.dao.SkillSheet;
import com.shilling.skillsheets.dao.User;

/**
 * @author Jake Shilling
 *
 */
class LocalSkillSheet extends LocalResource<LocalSkillSheet.Data> implements SkillSheet {

	class Data extends LocalResource.Data {
		private @Nullable String name = null;
		private Collection<UUID> teachers = new HashSet<>();
		private Collection<UUID> students = new HashSet<>();
		
		public Optional<String> getName () {
			return Optional.ofNullable(this.name);
		}
		
		public void setName (@Nullable String name) {
			this.name = name;
		}
		
		public Collection<UUID> getTeacher () {
			return this.teachers;
		}
		
		public Collection<UUID> getStudents () {
			return this.students;
		}
	}
	
	public LocalSkillSheet(UUID uuid, File file) {
		super(uuid, file);
	}

	@Override
	public SkillSheet addTeacher(User user) throws IOException {
		Data data = this.read();
		data.getTeacher().add(user.getUuid());
		this.addViewer(user.getUuid());
		this.write(data);
		
		return this;
	}

	@Override
	public boolean isTeacher(User user) throws IOException {
		return this.read().getTeacher().contains(user.getUuid());
	}

	@Override
	public boolean isStudent(User user) throws IOException {
		return this.read().getStudents().contains(user.getUuid());
	}

	@Override
	protected Data initial() {
		return new Data();
	}

	@Override
	public ResourceModel getModel(UUID uuid) {
		// TODO Auto-generated method stub
		return null;
	}

}
