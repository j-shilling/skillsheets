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
	
	public LocalUserDao () {
		this.users = new HashMap<>();
		
		File userdir = this.getUserDir();
		File[] files = userdir.listFiles((dir, name) -> name.endsWith(".properties"));
		
		for (File file : files) {
			try {
				User user = new LocalUser (this, file);
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
	
	private File getUserDir () {
		Path path = Paths.get(System.getProperty("user.home"),
				".skillsheets",
				"users");
		File ret = path.toFile();
		if (!ret.exists())
			ret.mkdirs();
		return ret;
	}
	
	private File newUserFile () throws IOException {
		Path path = Paths.get(this.getUserDir().getAbsolutePath(),
				UUID.randomUUID().toString() + ".properties");
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
		
		File file = this.newUserFile();
		User user = new LocalUser(this, file).setId(id);
		this.users.put(id, user);
		return user;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public User createWithEmail (String email) throws IOException {
		Preconditions.checkNotNull(email);
		Preconditions.checkArgument(!email.isEmpty());
		Preconditions.checkState(this.users.get(email) == null);
		
		File file = this.newUserFile();
		User user = new LocalUser(this, file).setEmail(email);
		this.users.put(email, user);
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
	
	public void updateKey (String oldVal, String newVal) {
		User user = this.users.remove(oldVal);
		if (user != null)
			this.users.put(newVal, user);
	}

}
