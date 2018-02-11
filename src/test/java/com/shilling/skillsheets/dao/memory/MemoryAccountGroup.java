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
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 *
 * @author Jake Shilling <shilling.jake@gmail.com>
 */
public class MemoryAccountGroup 
        extends AbstractResource<AccountGroup> 
        implements AccountGroup {
    
    private final Set<UUID> parents = new HashSet<>();
    private final Set<UUID> members = new HashSet<>();
    private final Set<UUID> children = new HashSet<>();
    private final Set<UUID> known = new HashSet<>();
    private boolean team = false;
    
    private ReadWriteLock lock = new ReentrantReadWriteLock();
    
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
        return this.members.contains(uuid) || this.children.contains (uuid);
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

    @Override
    public AccountGroup addChild(UUID uuid) throws IOException {
        this.children.add (uuid);
        return this;
    }

    @Override
    public AccountGroup delChild(UUID uuid) throws IOException {
        this.children.remove (uuid);
        return this;
    }

    @Override
    public AccountGroup addParent(UUID uuid) throws IOException {
        this.parents.add(uuid);
        return this;
    }

    @Override
    public AccountGroup delParent(UUID uuid) throws IOException {
        this.parents.remove (uuid);
        return this;
    }

    @Override
    public Collection<UUID> getParents() throws IOException {
        return this.parents;
    }

    @Override
    public Lock readLock() {
        return this.lock.readLock();
    }

    @Override
    public Lock writeLock() {
        return this.lock.writeLock();
    }

    @Override
    public Collection<UUID> getMembers() throws IOException {
        return this.members;
    }

    @Override
    public Collection<UUID> getChildren() throws IOException {
        return this.children;
    }
    
}
