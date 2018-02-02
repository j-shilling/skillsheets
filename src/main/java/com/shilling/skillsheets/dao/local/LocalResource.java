package com.shilling.skillsheets.dao.local;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.google.common.base.Preconditions;
import com.shilling.skillsheets.dao.Resource;

abstract class LocalResource<T extends LocalResource.Data> implements Resource {
	
	static class Data {
		private @Nullable String name;
		private UUID owner;
		private Collection<UUID> editors;
		private Collection<UUID> viewers;
		
		protected Data() {
			this.name = null;
			this.owner = null;
			this.editors = new HashSet<>();
			this.viewers = new HashSet<>();
		}
		
		@JsonCreator
		protected Data (
				@JsonProperty ("name") String name,
				@JsonProperty ("owner") UUID owner,
				@JsonProperty ("editors") Collection<UUID> editors,
				@JsonProperty ("viewers") Collection<UUID> viewers) {
			this.name = name;
			this.owner = owner;
			this.editors = new HashSet<>();
			this.viewers = new HashSet<>();
			
			if (editors != null)
				this.editors.addAll(editors);
			if (viewers != null)
				this.viewers.addAll(viewers);
		}

		public Optional<String> getName() {
			return Optional.ofNullable(name);
		}

		public void setName(String name) {
			this.name = name;
		}

		public Optional<UUID> getOwner() {
			return Optional.ofNullable(owner);
		}

		public void setOwner(UUID owner) {
			this.owner = owner;
		}

		public Collection<UUID> getEditors() {
			return editors;
		}

		public Collection<UUID> getViewers() {
			return viewers;
		}
	}

	private final UUID uuid;
	private final File file;
	
	private final Class<T> type;
	
	@SuppressWarnings("unchecked")
	public LocalResource (UUID uuid, File file) {
		Preconditions.checkNotNull(uuid);
		Preconditions.checkNotNull(file);
		
		this.uuid = uuid;
		this.file = file;
		
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
	
        
	
    @Override
    public UUID getUuid () {
        return this.uuid;
    }
        
    @Override
    public synchronized boolean isOwner(@Nullable UUID uuid) throws IOException {
        return this.read().getOwner().equals(Optional.ofNullable (uuid));
    }
    
    @Override
    public synchronized void setOwner(UUID uuid) throws IOException {
	T data = this.read();
	data.setOwner(uuid);
	this.write(data);
    }
    
    @Override
    public synchronized Optional<String> getName() throws IOException {
	return this.read().getName();
    }

    @Override
    public synchronized void setName(String name) throws IOException {
	T data = this.read();
	data.setName(name);
	this.write(data);
    }
    
    @Override
    public synchronized boolean canEdit(UUID uuid) throws IOException {
	return this.read().getEditors().contains(uuid);
    }
    
    @Override
    public synchronized void addEditor(UUID uuid) throws IOException {
        T data = this.read();
	data.getEditors().add(uuid);
	this.write(data);
    }
        
    @Override
    public synchronized void delEditor (UUID uuid) throws IOException {
        T data = this.read();
        data.getEditors().remove(uuid);
        this.write (data);
    }

    @Override
    public synchronized void clearEditors() throws IOException {
        T data = this.read();
        data.getEditors().clear();
        this.write (data);
    }
    
    @Override
    public synchronized boolean canView(UUID uuid) throws IOException {
	return this.read().getViewers().contains(uuid)
		|| this.canEdit(uuid);
    }
    
    @Override
    public synchronized void addViewer(UUID uuid) throws IOException {
	T data = this.read();
	data.getViewers().add(uuid);
	this.write(data);
    }
        
    @Override
    public synchronized void delViewer (UUID uuid) throws IOException {
        T data = this.read();
        data.getViewers().remove(uuid);
        this.write (data);
    }

    @Override
    public synchronized void clearViewers() throws IOException {
        T data = this.read();
        data.getViewers().clear();
        this.write (data);
    }
    
    @Override
    public synchronized void copy (Resource other) throws IOException {
	if (other instanceof LocalResource<?>) {
            LocalResource<?> local = (LocalResource<?>) other;
            if (this.type.equals(local.type)) {
                @SuppressWarnings("unchecked")
		LocalResource<T> that = (LocalResource<T>) local;
		that.write(this.read());
		return;
            }
        }
			
	throw new IOException ("Cannot copy to incompatible type");
    }
    
    @Override
    public synchronized void delete () {
        if (this.file.exists()) {
            this.file.delete();
	}
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
