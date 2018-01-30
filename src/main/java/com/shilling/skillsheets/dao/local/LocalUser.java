package com.shilling.skillsheets.dao.local;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiConsumer;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Preconditions;
import com.shilling.skillsheets.dao.User;

/**
 * Represents a user account saved in local file storage.
 * 
 * @author Jake Shilling
 *
 */
class LocalUser extends LocalResource<LocalUser.Data> implements User {

	static class Data {
		private @Nullable String name;
		private @Nullable String id;
		private @Nullable String email;
		private @Nullable Boolean teacher;

		private Collection<UUID> skillsheets = new HashSet<>();
		private Collection<UUID> groups = new HashSet<>();

		private Data() {
		}

		@JsonCreator
		private Data (
				@JsonProperty ("name") String name,
				@JsonProperty ("id") String id,
				@JsonProperty ("email") String email,
				@JsonProperty ("teacher") Boolean teacher,
				@JsonProperty ("skillSheets") Collection<UUID> skillSheets,
				@JsonProperty ("userGroups") Collection<UUID> userGroups) {
			this.name = name;
			this.id = id;
			this.email = email;
			this.teacher = teacher;
			
			this.skillsheets = new HashSet<>();
			this.groups = new HashSet<>();
			
			if (skillSheets != null)
				this.skillsheets.addAll(skillSheets);
				
				if (userGroups != null)
					this.groups.addAll(userGroups);
		}

		public Optional<String> getName() {
			return Optional.ofNullable(this.name);
		}

		public void setName(@Nullable String name) {
			this.name = name;
		}

		public Optional<String> getId() {
			return Optional.ofNullable(this.id);
		}

		public void setId(@Nullable String id) {
			this.id = id;
		}

		public Optional<String> getEmail() {
			return Optional.ofNullable(this.email);
		}

		public void setEmail(@Nullable String email) {
			this.email = email;
		}

		public boolean isTeacher() {
			return this.teacher == null ? false : this.teacher;
		}

		public void setTeacher(boolean val) {
			this.teacher = val;
		}

		public Collection<UUID> getSkillSheets() {
			return this.skillsheets;
		}

		public Collection<UUID> getUserGroups() {
			return this.groups;
		}
	}

	private final Collection<BiConsumer<Optional<String>, Optional<String>>> onIdChange;
	private final Collection<BiConsumer<Optional<String>, Optional<String>>> onEmailChange;

	public LocalUser(UUID uuid, File file) {
		super(uuid, file);

		this.onIdChange = new HashSet<>();
		this.onEmailChange = new HashSet<>();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized boolean isTeacher() throws IOException {
		return this.read().isTeacher();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized User setName(String name) throws IOException {
		Data data = this.read();
		data.setName(name);
		this.write(data);

		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized Optional<String> getName() throws IOException {
		return this.read().getName();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized User setId(String id) throws IOException {
		Data data = this.read();

		Optional<String> oldVal = data.getId();
		data.setId(id);
		this.write(data);

		Optional<String> newVal = data.getId();

		if (!oldVal.equals(newVal)) {
			for (BiConsumer<Optional<String>, Optional<String>> consumer : this.onIdChange) {
				consumer.accept(oldVal, newVal);
			}
		}

		return this;
	}

	public synchronized void addIdListener(BiConsumer<Optional<String>, Optional<String>> consumer) {
		if (consumer != null)
			this.onIdChange.add(consumer);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized Optional<String> getId() throws IOException {
		return this.read().getId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized User setEmail(String email) throws IOException {
		Data data = this.read();

		Optional<String> oldVal = data.getEmail();
		data.setEmail(email);
		this.write(data);

		Optional<String> newVal = data.getEmail();

		if (!oldVal.equals(newVal)) {
			for (BiConsumer<Optional<String>, Optional<String>> consumer : this.onEmailChange) {
				consumer.accept(oldVal, newVal);
			}
		}

		return this;
	}

	public synchronized void addEmailListener(BiConsumer<Optional<String>, Optional<String>> consumer) {
		if (consumer != null)
			this.onEmailChange.add(consumer);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized Optional<String> getEmail() throws IOException {
		return this.read().getEmail();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized Collection<UUID> getSkillSheets() throws IOException {
		return this.read().getSkillSheets();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized Collection<UUID> getUserGroups() throws IOException {
		return this.read().getUserGroups();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized User addSkillSheet(UUID uuid) throws IOException {
		Preconditions.checkNotNull(uuid);

		Data data = this.read();
		data.getSkillSheets().add(uuid);
		this.write(data);

		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized User delSkillSheet(UUID uuid) throws IOException {
		Preconditions.checkNotNull(uuid);

		Data data = this.read();
		data.getSkillSheets().remove(uuid);
		this.write(data);

		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized User addUserGroup(UUID uuid) throws IOException {
		Preconditions.checkNotNull(uuid);

		Data data = this.read();
		data.getUserGroups().add(uuid);
		this.write(data);

		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized User delUserGroup(UUID uuid) throws IOException {
		Preconditions.checkNotNull(uuid);

		Data data = this.read();
		data.getUserGroups().remove(uuid);
		this.write(data);

		return this;
	}

	@Override
	protected Data initial() {
		return new Data();
	}

}
