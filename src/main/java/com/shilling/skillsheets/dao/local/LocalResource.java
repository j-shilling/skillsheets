package com.shilling.skillsheets.dao.local;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;
import java.util.UUID;
import java.util.function.Consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.google.common.base.Preconditions;
import com.shilling.skillsheets.dao.Resource;

abstract class LocalResource<T> implements Resource {

	private final UUID uuid;
	private final File file;
	
	private final Queue<Consumer<UUID>> onDelete;
	private final Class<T> type;
	
	@SuppressWarnings("unchecked")
	public LocalResource (UUID uuid, File file) {
		Preconditions.checkNotNull(uuid);
		Preconditions.checkNotNull(file);
		
		this.uuid = uuid;
		this.file = file;
		this.onDelete = new LinkedList<>();
		
		T data = this.initial();
		
		this.type = (Class<T>) data.getClass();
		
		if (!this.file.exists()) {

			try {
				
				this.file.createNewFile();
				this.write(data);
				
			} catch (IOException e) {
				
				throw new RuntimeException (e);
				
			}
			
		}
	}
	
	private ObjectMapper getMapper() {
		ObjectMapper mapper = new XmlMapper();
		
		mapper.registerModule(new Jdk8Module());
		
		return mapper;
	}
	
	abstract protected T initial ();
	
	protected synchronized T read () throws IOException {
		Preconditions.checkState(this.file.exists(), "This resource has been deleted");
		
		try {
			
			return this.getMapper().readValue(this.file, this.type);
			
		} catch (Exception e) {
			
			throw new IOException ("Could not read saved data", e);
			
		}
		
	}
	
	protected synchronized void write (T data) throws IOException {
		Preconditions.checkState(this.file.exists(), "This resource has been deleted");
		
		try {
			
			this.getMapper().writeValue(this.file, data);
			
		} catch (Exception e) {
			
			throw new IOException ("Could not save data", e);
			
		}
		
	}
	
	public synchronized void delete () {
		if (this.file.exists()) {
			this.file.delete();
			
			while (!this.onDelete.isEmpty())
				this.onDelete.poll().accept(this.getUuid());
		}
	}
	
	public synchronized void addDeleteListener (Consumer<UUID> onDelete) {
		this.onDelete.add(onDelete);
	}
	
	public UUID getUuid () {
		return this.uuid;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals (Object obj) {
		if (obj instanceof LocalResource)
			return this.uuid.equals(((LocalResource<?>) obj).uuid);
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.uuid);
	}
}
