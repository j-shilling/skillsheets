/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shilling.skillsheets.services.impl;

import com.google.api.client.util.Preconditions;
import com.shilling.skillsheets.AbstractHasUuid;
import com.shilling.skillsheets.dao.Resource;
import com.shilling.skillsheets.services.Group;
import com.shilling.skillsheets.services.Service;
import com.shilling.skillsheets.services.User;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.locks.Lock;

/**
 *
 * @author Jake Shilling <shilling.jake@gmail.com>
 */
abstract class AbstractService<T extends Resource, R extends AbstractService>
        extends AbstractHasUuid
        implements Service<R> {
    
    private final T resource;
    private final User user;
    
    protected AbstractService (User user, T resource) {
        super (resource.getUuid());
        
        Preconditions.checkNotNull(user);
        
        this.resource = resource;
        this.user = user;
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
    
}
