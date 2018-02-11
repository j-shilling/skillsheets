/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shilling.skillsheets.dao.memory;

import com.shilling.skillsheets.dao.Account;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 *
 * @author Jake Shilling <shilling.jake@gmail.com>
 */
public class MemoryAccount 
        extends AbstractResource<Account>
        implements Account {
    
    private boolean teacher = false;
    private String id = null;
    private String email = null;
    
    private Set<UUID> known = new HashSet<>();
    private Set<UUID> groups = new HashSet<>();
    
    private ReadWriteLock lock = new ReentrantReadWriteLock();
    
    private MemoryAccountDao dao;
    
    public MemoryAccount (MemoryAccountDao dao, UUID uuid) {
        super (uuid);
        this.dao = dao;
    }

    @Override
    public Account setId(String id) throws IOException {
        this.id = id;
        this.dao.getKeys().put(id, this.getUuid());
        return this;
    }

    @Override
    public Account setEmail(String email) throws IOException {
        this.email = email;
        this.dao.getKeys().put(email, this.getUuid());
        return this;
    }

    @Override
    public boolean isTeacher() throws IOException {
        return this.teacher;
    }
    
    @Override
    public Account setTeacher (boolean val) throws IOException {
        this.teacher = val;
        return this;
    }

    @Override
    public Account addKnownResource(UUID uuid) throws IOException {
        this.known.add(uuid);
        return this;
    }

    @Override
    public Account delKnownResource(UUID uuid) throws IOException {
        this.known.remove(uuid);
        return this;
    }

    @Override
    public Collection<UUID> getGroups() throws IOException {
        return this.groups;
    }

    @Override
    public Collection<UUID> getKnownResources() throws IOException {
        return this.known;
    }

    @Override
    public Account addGroup(UUID uuid) throws IOException {
        this.groups.add(uuid);
        return this;
    }

    @Override
    public Account delGroup(UUID uuid) throws IOException {
        this.groups.remove(uuid);
        return this;
    }

    @Override
    public Optional<String> getId() throws IOException {
        return Optional.ofNullable (this.id);
    }

    @Override
    public Optional<String> getEmail() throws IOException {
        return Optional.ofNullable (this.email);
    }

    @Override
    public Lock readLock() {
        return this.lock.readLock();
    }

    @Override
    public Lock writeLock() {
        return this.lock.writeLock();
    }
    
}
