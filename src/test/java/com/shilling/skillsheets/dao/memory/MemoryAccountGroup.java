/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shilling.skillsheets.dao.memory;

import com.shilling.skillsheets.dao.AccountGroup;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 *
 * @author Jake Shilling <shilling.jake@gmail.com>
 */
public class MemoryAccountGroup 
        extends AbstractResource<AccountGroup> 
        implements AccountGroup {
    
    private Set<UUID> members = new HashSet<>();
    private Set<UUID> known = new HashSet<>();
    private boolean team = false;
    
    public MemoryAccountGroup (UUID uuid) {
        super (uuid);
    }

    @Override
    public AccountGroup setTeam(boolean val) throws IOException {
        this.team = val;
        return this;
    }

    @Override
    public boolean isTeam() throws IOException {
        return this.team;
    }

    @Override
    public AccountGroup addKnownResource(UUID uuid) throws IOException {
        this.known.add(uuid);
        return this;
    }

    @Override
    public boolean contains(UUID uuid) throws IOException {
        return this.members.contains(uuid);
    }

    @Override
    public Collection<UUID> getAll() throws IOException {
        return this.members;
    }

    @Override
    public AccountGroup addMember(UUID uuid) throws IOException {
        this.members.add (uuid);
        return this;
    }

    @Override
    public AccountGroup delMember(UUID uuid) throws IOException {
        this.members.remove(uuid);
        return this;
    }

    @Override
    public AccountGroup delKnownResource(UUID uuid) throws IOException {
        this.known.remove(uuid);
        return this;
    }
    
}
