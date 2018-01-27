package com.shilling.skillsheets.dao.local;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.google.common.base.Preconditions;
import com.shilling.skillsheets.dao.User;
import com.shilling.skillsheets.dao.UserDao;

/**
 * @author Jake Shilling
 *
 */
public class LocalUserDao implements UserDao {
	
	private final Logger logger = LogManager.getLogger(LocalUserDao.class);
	private final Map<String, User> users;
	private final File userdir;
	
	public LocalUserDao (File userdir) {
		this.users = new HashMap<>();
		
		this.userdir = userdir;
		if (!this.userdir.exists())
			this.userdir.mkdirs();
		
		Preconditions.checkArgument(this.userdir.isDirectory());
		
		File[] files = userdir.listFiles((dir, name) -> name.endsWith(".user.properties"));
		
		for (File file : files) {
			try {
				UUID uuid = UUID.fromString(file.getName().substring(0, file.getName().indexOf(".user.properties")));
				User user = new LocalUser (this, file, uuid);
				Optional<String> id = user.getId();
				if (id.isPresent())
					this.users.put(id.get(), user);
				Optional<String> email = user.getEmail();
				if (email.isPresent())
					this.users.put(email.get(), user);
			} catch (Exception e) {
				this.logger.error("Could not add user from file \'" 
						+ file.getAbsolutePath() + "\': "
						+ e.getMessage());
			}
		}
	}
	
	private File newUserFile (UUID uuid) throws IOException {
		Path path = Paths.get(this.userdir.getAbsolutePath(),
				uuid.toString() + ".user.properties");
		File ret = path.toFile();
		ret.createNewFile();
		return ret;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public User createWithId (String id) throws IOException {
		Preconditions.checkNotNull(id);
		Preconditions.checkArgument(!id.isEmpty());
		Preconditions.checkState(this.users.get(id) == null);
		
		UUID uuid = UUID.randomUUID();
		File file = this.newUserFile(uuid);
		User user = new LocalUser(this, file, uuid);
		
		this.users.put(id, user);
		
		return user.setId(id);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public User createWithEmail (String email) throws IOException {
		Preconditions.checkNotNull(email);
		Preconditions.checkArgument(!email.isEmpty());
		Preconditions.checkState(this.users.get(email) == null);
		
		UUID uuid = UUID.randomUUID();
		File file = this.newUserFile(uuid);
		User user = new LocalUser(this, file, uuid).setEmail(email);
		return user;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<User> read(String id) {
		return Optional.ofNullable(this.users.get(id));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void delete(User user) {
		Preconditions.checkNotNull(user);
		user.delete();
		this.users.values().removeIf (val -> user.equals(val));
	}
	
	public void updateKey (String oldVal, String newVal, User user) {
		this.users.remove(oldVal);
		this.users.put(newVal, user);
	}

}
