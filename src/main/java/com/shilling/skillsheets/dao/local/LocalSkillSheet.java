package com.shilling.skillsheets.dao.local;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.google.common.base.Preconditions;
import com.shilling.skillsheets.dao.SkillSheet;
import com.shilling.skillsheets.dao.User;

/**
 * @author Jake Shilling
 *
 */
class LocalSkillSheet implements SkillSheet {
	
	private class Serialized {
		public String name = null;
		public Collection<String> teachers = new HashSet<>();
		public Collection<String> students = new HashSet<>();
	}

	private final UUID uuid;
	private final File file;
	
	public LocalSkillSheet (UUID uuid, File file) {
		Preconditions.checkNotNull(uuid);
		Preconditions.checkNotNull(file);
		Preconditions.checkArgument(file.isFile());
		
		this.uuid = uuid;
		this.file = file;
	}
	
	private synchronized Serialized getSerialized() throws IOException {
		Preconditions.checkState(this.file.exists(), "This Skill Sheet has been deleted");
		
		ObjectMapper mapper = new XmlMapper();
		try {
			return mapper.readValue(this.file, Serialized.class);
		} catch (IOException e) {
			throw new IOException ("Could not read skill sheet from " + this.file.getAbsolutePath(),e);
		} 
	}
	
	private synchronized void save (Serialized data) throws IOException {
		Preconditions.checkNotNull(data);
		Preconditions.checkState(this.file.exists(), "This Skill Sheet has been deleted");
		
		ObjectMapper mapper = new XmlMapper();
		try {
			mapper.writeValue(this.file, data);
		} catch (IOException e) {
			throw new IOException ("Could not write skill sheet to " + this.file.getAbsolutePath(), e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getUuid() {
		return this.uuid.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void addTeacher(User user) throws IOException {
		Preconditions.checkNotNull(user);
		
		Serialized data = this.getSerialized();
		data.teachers.add(user.getUuid().toString());
		this.save(data);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized boolean isTeacher(User user) throws IOException {
		Preconditions.checkNotNull(user);
		
		return this.getSerialized().teachers.contains(user.getUuid().toString());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized boolean isStudent(User user) throws IOException {
		Preconditions.checkNotNull(user);
		
		return this.getSerialized().students.contains(user.getUuid().toString());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void setName(String name) throws IOException {
		Serialized data = this.getSerialized();
		data.name = name;
		this.save(data);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals (Object obj) {
		if (obj instanceof LocalSkillSheet)
			return this.uuid.equals(((LocalSkillSheet) obj).uuid);
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.uuid);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void delete() {
		if (this.file.exists())
			this.file.delete();
	}

}
