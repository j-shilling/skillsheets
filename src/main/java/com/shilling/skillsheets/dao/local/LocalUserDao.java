package com.shilling.skillsheets.dao.local;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import com.google.common.base.Preconditions;
import com.shilling.skillsheets.dao.User;
import com.shilling.skillsheets.dao.UserDao;

/**
 * @author Jake Shilling
 *
 */
public class LocalUserDao extends LocalDao<User> implements UserDao {
	
	private final Map<String, UUID> keys = new HashMap<>();
	
	protected LocalUserDao(File dir) {
		super(dir);
	}
	
	private void updateKey (Optional<String> oldVal, Optional<String> newVal, UUID uuid) {
		Preconditions.checkNotNull(uuid);
		if (oldVal.isPresent())
			Preconditions.checkArgument(uuid.equals(this.keys.get(oldVal.get())));
		if (newVal.isPresent())
			Preconditions.checkArgument(!this.keys.containsKey(newVal.get()));
		
		if (oldVal.isPresent())
			this.keys.remove(oldVal.get());
		if (newVal.isPresent())
			this.keys.put(newVal.get(), uuid);
	}

	@Override
	public Optional<User> read(String id) throws IOException {
		UUID uuid = null;
		
		try {
			uuid = UUID.fromString(id);
		} catch (IllegalArgumentException e) {
			uuid = this.keys.get(id);
		}
		
		if (uuid == null)
			return Optional.empty();
		
		return this.read(uuid);
	}
	
	@Override
	public User create () throws IOException {
		User ret = super.create();
		
		if (ret instanceof LocalUser) {
			LocalUser user = (LocalUser) ret;
			
			user.addIdListener((x, y) -> LocalUserDao.this.updateKey(x, y, user.getUuid()));
			user.addEmailListener((x, y) -> LocalUserDao.this.updateKey(x, y, user.getUuid()));
		}
		
		return ret;
	}

	@Override
	public User createWithId(String id) throws IOException {
		User ret = this.create();
		ret.setId(id);
		return ret;
	}

	@Override
	public User createWithEmail(String email) throws IOException {
		User ret = this.create();
		ret.setEmail(email);
		return ret;
	}

	@Override
	protected String getFileExtension() {
		return ".user.xml";
	}

	@Override
	protected User getInstance(UUID uuid, File file) {
		return new LocalUser (uuid, file);
	}

}
