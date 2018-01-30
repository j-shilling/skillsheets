package com.shilling.skillsheets.dao.local;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.base.Preconditions;
import com.shilling.skillsheets.dao.Resource;

abstract class LocalDao<T extends Resource> {
	
	private final File dir;
	private final Map<UUID, T> values;
	private final Logger logger = LogManager.getLogger(LocalDao.class);
	
	protected LocalDao (File dir) {
		if (!dir.exists())
			dir.mkdirs();
		
		Preconditions.checkArgument(dir.isDirectory());
		Preconditions.checkArgument(dir.canRead());
		Preconditions.checkArgument(dir.canWrite());
		
		this.dir = dir;
		this.values = new HashMap<>();
		
		File[] files = this.dir.listFiles((parent, name) -> name.endsWith(this.getFileExtension()));
		for (File file : files) {
			try {
				
				String name = 
						file.getName().substring(0, file.getName().indexOf(this.getFileExtension()));
				UUID uuid = UUID.fromString(name);
				
				T item = this.getInstance(uuid, file);
				this.values.put(uuid, item);
				
			} catch (IllegalArgumentException | IndexOutOfBoundsException e) {
				this.logger.warn("Illegal filename in dao directory \'" + file.getName() + "\'");
			}
		}
	}
	
	abstract protected String getFileExtension();
	abstract protected T getInstance (UUID uuid, File file);
	
	public T create () throws IOException {
		UUID uuid = UUID.randomUUID();
		File file = 
				Paths.get(this.dir.getAbsolutePath(), uuid.toString() + this.getFileExtension()).toFile();
		
		T item = this.getInstance(uuid, file);
		if (item instanceof LocalResource)
			((LocalResource<?>) item).addDeleteListener(x -> LocalDao.this.delete(x));
		
		this.values.put(uuid, item);
		
		return item;
	}
	
	public Optional<T> read (UUID uuid) {
		return Optional.ofNullable(this.values.get(uuid));
	}
	
	public void delete (UUID uuid) {
		T item = this.values.remove(uuid);
		if (item != null)
			item.delete();
	}
	
	public void delete (T item) {
		this.delete(item.getUuid());
	}
	
	public void delete (String uuid) {
		this.delete(UUID.fromString(uuid));
	}

}
