/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shilling.skillsheets.dao.memory;

import com.shilling.skillsheets.AbstractHasUuid;
import com.shilling.skillsheets.dao.Resource;
import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 *
 * @author Jake Shilling <shilling.jake@gmail.com>
 */
abstract class AbstractResource<T extends Resource>
        extends AbstractHasUuid
        implements Resource<T> {
    
    private String displayName = null;
    private Set<UUID> editors = new HashSet<>();
    private Set<UUID> viewers = new HashSet<>();
    private UUID owner = null;
    
    protected AbstractResource(UUID uuid) {
        super (uuid);
    }

    @Override
    public Optional<String> getDisplayName() throws IOException {
        return Optional.ofNullable (this.displayName);
    }

    @Override
    public T setDisplayName(String displayName) throws IOException {
        this.displayName = displayName;
        return (T) this;
    }

    @Override
    public T addEditor(UUID uuid) throws IOException {
        this.editors.add(uuid);
        return (T) this;
    }

    @Override
    public T addViewer(UUID uuid) throws IOException {
        this.viewers.add (uuid);
        return (T) this;
    }

    @Override
    public T setOwner(UUID uuid) throws IOException {
        this.owner = uuid;
        return (T) this;
    }

    @Override
    public void delete() throws IOException {
    }

    @Override
    public boolean isEditor(UUID uuid) throws IOException {
        return this.editors.contains(uuid);
    }

    @Override
    public boolean isViewer(UUID uuid) throws IOException {
        return this.viewers.contains(uuid);
    }

    @Override
    public boolean isOwner(UUID uuid) throws IOException {
        return this.owner.equals(this.owner);
    }
    
}
