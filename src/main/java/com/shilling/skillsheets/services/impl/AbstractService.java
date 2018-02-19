/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shilling.skillsheets.services.impl;

import com.google.api.client.util.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.shilling.skillsheets.AbstractHasUuid;
import com.shilling.skillsheets.dao.Resource;
import com.shilling.skillsheets.services.Group;
import com.shilling.skillsheets.services.Service;
import com.shilling.skillsheets.services.User;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.locks.Lock;
import java.util.stream.Collectors;

/**
 *
 * @author Jake Shilling <shilling.jake@gmail.com>
 */
abstract class AbstractService<T extends Resource, R extends AbstractService>
        extends AbstractHasUuid
        implements Service<R> {
    
    private final T resource;
    private final User user;
    private final UserFactory users;
    private final GroupFactory groups;
    
    protected AbstractService (
            UserFactory users, 
            GroupFactory groups, 
            User user, 
            T resource) {
        super (resource.getUuid());
        
        Preconditions.checkNotNull (users);
        Preconditions.checkNotNull(user);
        
        this.resource = resource;
        this.user = user;
        this.users = users;
        this.groups = groups;
    }
    
    protected Lock readLock() {
        return this.resource.readLock();
    }
    
    protected Lock writeLock() {
        return this.resource.writeLock();
    }
    
    protected T getResource() {
        Preconditions.checkState(!this.resource.isDeleted(),
                "Cannot access a deleted resource.");
        return this.resource;
    }
    
    protected final User getUser() {
        return this.user;
    }
    
    protected final UserFactory users() {
        return this.users;
    }
    
    protected final GroupFactory groups() {
        return this.groups;
    }
    
    @Override
    public final boolean isOwned() {
        this.readLock().lock();
        try {
            return this.resource.isOwner(this.user.getUuid());
        } catch (IOException e) {
            throw new RuntimeException (e);
        } finally {
            this.readLock().unlock();
        }
    }
    
    @Override
    public final boolean isWritable () {
        if (this.isOwned())
            return true;
        
        this.readLock().lock();
        try {
            Queue<Group> groups = new LinkedList<>();
            groups.addAll(this.user.getGroups());
            
            while (!groups.isEmpty()) {
                Group group = groups.remove();
                if (this.resource.isEditor(group.getUuid()))
                    return true;
                
                groups.addAll(group.getParents());
            }
            
            return false;
        } catch (IOException e) {
            throw new RuntimeException (e);
        } finally {
            this.readLock().unlock();
        }
    }

    @Override
    public Optional<String> getDisplayName() {
        this.readLock().lock();
        try {
            return this.getResource().getDisplayName();
        } catch (IOException e) {
            throw new RuntimeException (e);
        } finally {
            this.readLock().unlock();
        }
    }

    @Override
    public R setDisplayName(String displayName) throws IllegalAccessException {
        if (!this.isWritable())
            throw new IllegalAccessException 
                (this.user + " does not have permission to edit this resource");
        
        this.writeLock().lock();
        try {
            this.getResource().setDisplayName(displayName);
        } catch (IOException e) {
            throw new RuntimeException (e);
        } finally {
            this.writeLock().unlock();
        }
        
        return (R) this;
    }

    @Override
    public R giveTo(User user) throws IllegalAccessException {
        if (!this.isWritable())
            throw new IllegalAccessException 
                (this.user + " does not have permission to edit this resource");
        
        this.writeLock().lock();
        try {
            this.getResource().setOwner(user.getUuid());
        } catch (IOException e) {
            throw new RuntimeException (e);
        } finally {
            this.writeLock().unlock();
        }
        
        return (R) this;
    }

    @Override
    public R letEdit(User user) throws IllegalAccessException {
        if (!this.isWritable())
            throw new IllegalAccessException 
                (this.user + " does not have permission to edit this resource");
        
        this.writeLock().lock();
        try {
            this.getResource().addEditor(user.getUuid());
        } catch (IOException e) {
            throw new RuntimeException (e);
        } finally {
            this.writeLock().unlock();
        }
        
        return (R) this;
    }

    @Override
    public R letEdit(Group group) throws IllegalAccessException {
        if (!this.isWritable())
            throw new IllegalAccessException 
                (this.user + " does not have permission to edit this resource");
        
        this.writeLock().lock();
        try {
            this.getResource().addEditor(group.getUuid());
        } catch (IOException e) {
            throw new RuntimeException (e);
        } finally {
            this.writeLock().unlock();
        }
        
        return (R) this;
    }

    @Override
    public R letView(User user) throws IllegalAccessException {
        user.addKnownResource(this.getUuid());
        return (R) this;
    }

    @Override
    public R letView(Group group) throws IllegalAccessException {
        group.addKnownResource(this.getUuid());
        return (R) this;
    }

    @Override
    public void delete() throws IllegalAccessException {
        if (!this.isWritable())
            throw new IllegalAccessException 
                (this.user + " does not have permission to delete this resource");
        
        this.writeLock().lock();
        try {
            this.getResource().delete();
        } catch (IOException e) {
            throw new RuntimeException (e);
        } finally {
            this.writeLock().unlock();
        }
    }
    
    @Override
    public Optional<User> getOwner() {
        this.readLock().lock();
        try {
            Optional<UUID> owner = this.getResource().getOwner();
            if (owner.isPresent()) {
                return this.users.user(owner.get());
            } else {
                return Optional.empty();
            }
        } catch (IOException e) {
            throw new RuntimeException (e);
        } finally {
            this.readLock().unlock();
        }
    }
    
    @Override
    public Collection<User> getEditingAccounts() {
        this.readLock().lock();
        try {
            Collection<UUID> uuids = this.getResource().getEditors();
            Collection<User> editors = uuids.stream()
                    .map((uuid) -> this.users.user(uuid))
                    .filter((u) -> u.isPresent())
                    .map((o) -> o.get())
                    .collect(Collectors.toSet());
            return new ImmutableSet.Builder<User>()
                    .addAll(editors)
                    .build();
        } catch (IOException e) {
            throw new RuntimeException (e);
        } finally {
            this.readLock().unlock();
        }
    }
    
    @Override
    public Collection<Group> getEditingGroups() {
        this.readLock().lock();
        try {
            Collection<UUID> uuids = this.getResource().getEditors();
            Collection<Group> editors = uuids.stream()
                    .map((uuid) -> this.groups.group(this.user, uuid))
                    .filter((u) -> u.isPresent())
                    .map((o) -> o.get())
                    .collect(Collectors.toSet());
            return new ImmutableSet.Builder<Group>()
                    .addAll(editors)
                    .build();
        } catch (IOException e) {
            throw new RuntimeException (e);
        } finally {
            this.readLock().unlock();
        }
    }
    
    @Override
    public Collection<User> getViewingAccounts() {
        this.readLock().lock();
        try {
            Collection<UUID> uuids = this.getResource().getViewers();
            Collection<User> editors = uuids.stream()
                    .map((uuid) -> this.users.user(uuid))
                    .filter((u) -> u.isPresent())
                    .map((o) -> o.get())
                    .collect(Collectors.toSet());
            return new ImmutableSet.Builder<User>()
                    .addAll(editors)
                    .build();
        } catch (IOException e) {
            throw new RuntimeException (e);
        } finally {
            this.readLock().unlock();
        }
    }
    
    @Override
    public Collection<Group> getViewingGroups() {
        this.readLock().lock();
        try {
            Collection<UUID> uuids = this.getResource().getViewers();
            Collection<Group> editors = uuids.stream()
                    .map((uuid) -> this.groups.group(this.user, uuid))
                    .filter((u) -> u.isPresent())
                    .map((o) -> o.get())
                    .collect(Collectors.toSet());
            return new ImmutableSet.Builder<Group>()
                    .addAll(editors)
                    .build();
        } catch (IOException e) {
            throw new RuntimeException (e);
        } finally {
            this.readLock().unlock();
        }
    }
}
