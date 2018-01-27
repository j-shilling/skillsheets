package com.shilling.skillsheets.dao.local;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.base.Preconditions;
import com.shilling.skillsheets.dao.SkillSheet;
import com.shilling.skillsheets.dao.User;

/**
 * Represents a user account saved in local file storage.
 * 
 * @author Jake Shilling
 *
 */
class LocalUser implements User {

	private final Logger logger = LogManager.getLogger(LocalUser.class);
	private final UUID uuid;
	private final File file;
	private final LocalUserDao dao;
	private final ReentrantLock lock;

	public LocalUser(LocalUserDao dao, File file, UUID uuid) {
		Preconditions.checkNotNull(file, "This user does not have a file");
		Preconditions.checkArgument(file.exists(), "This user's file does not exist");
		Preconditions.checkArgument(file.isFile(), "This user's file is not a normal file");

		this.uuid = uuid;
		this.file = file;
		this.dao = dao;
		this.lock = new ReentrantLock();
	}

	private void lock() {
		this.lock.lock();
	}

	private void unlock() {
		this.lock.unlock();
	}

	private File getFile() {
		Preconditions.checkState(this.file.exists(), "This user has been deleted");
		return this.file;
	}

	private Properties getProperties() throws IOException {
		Properties ret = new Properties();

		try {
			ret.load(new FileInputStream(this.getFile()));
		} catch (IOException e) {
			throw new IOException("Could not load properties file", e);
		}

		return ret;
	}

	private void setProperty(String key, String value) {

		try {
			Properties props = this.getProperties();
			props.setProperty(key, value);
			props.store(new FileWriter(this.getFile()), "Setting " + key + " to " + value);
		} catch (IOException e) {
			this.logger.error("Could not update user: " + e.getMessage());
			return;
		}

	}

	private void addProperty(String key, String item) {

		try {
			Properties props = this.getProperties();
			String value = props.getProperty(key) + "," + item;
			props.setProperty(key, value);
			props.store(new FileWriter(this.getFile()), "Setting " + key + " to " + value);
		} catch (IOException e) {
			this.logger.error("Could not update user: " + e.getMessage());
			return;
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isTeacher() throws IOException {
		this.lock();

		try {
			return Boolean.valueOf(this.getProperties().getProperty("teacher"));
		} finally {
			this.unlock();
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public User setName(String name) {

		new Thread(() -> {
			LocalUser.this.lock();
			try {
				LocalUser.this.setProperty("name", name);
			} finally {
				LocalUser.this.unlock();
			}
		}).start();

		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<String> getName() throws IOException {
		this.lock();
		try {
			return Optional.ofNullable(this.getProperties().getProperty("name"));
		} finally {
			this.unlock();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public User setId(String id) {
		this.lock();

		try {
			String old = this.getProperties().getProperty("id");
				new Thread(() -> {
					LocalUser.this.dao.updateKey(old, id, LocalUser.this);
				}).start();
			
			new Thread(() -> {
				try {
					LocalUser.this.setProperty("id", id);
				} finally {
					if (LocalUser.this.lock.isLocked())
						LocalUser.this.unlock();
				}
			}).start();
		} catch (IOException e) {
			this.logger.error("Error trying to read id: " + e.getMessage());
		}

		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<String> getId() throws IOException {
		this.lock();
		try {
			return Optional.ofNullable(this.getProperties().getProperty("id"));
		} finally {
			this.unlock();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public User setEmail(String email) {
		this.lock();

		try {
			Optional<String> old = Optional.ofNullable(this.getProperties().getProperty("email"));
			if (old.isPresent()) {
				new Thread(() -> {
					LocalUser.this.dao.updateKey(old.get(), email, LocalUser.this);
				}).start();
			}
			
			new Thread(() -> {
				try {
					LocalUser.this.setProperty("email", email);
				} finally {
					LocalUser.this.unlock();
				}
			}).start();
		} catch (IOException e) {
			this.logger.error("Error trying to read id: " + e.getMessage());
		}

		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<String> getEmail() throws IOException {
		this.lock();
		try {
			return Optional.ofNullable(this.getProperties().getProperty("email"));
		} finally {
			this.unlock();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public User addSkillSheet(SkillSheet skillsheet) {
		new Thread(() -> {
			LocalUser.this.lock();
			try {
				LocalUser.this.addProperty("skillsheets", skillsheet.getUuid());
			} finally {
				LocalUser.this.unlock();
			}
		}).start();

		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<String> getSkillSheets() throws IOException {
		this.lock();
		try {
			String values = this.getProperties().getProperty("skillsheets");
			return new HashSet<>(Arrays.asList(values.split(",")));
		} finally {
			this.unlock();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void delete() {
		this.lock();
		try {
			this.getFile().delete();
		} finally {
			this.unlock();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof LocalUser)
			return this.uuid.equals(((LocalUser) obj).uuid);
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.uuid);
	}

	@Override
	public UUID getUuid() {
		return this.uuid;
	}

}
