/*
 * Copyright (C) 2018 Pivotal Software, Inc.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package com.shilling.skillsheets.services.impl;

import com.google.api.client.util.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.shilling.skillsheets.dao.AccountGroup;
import com.shilling.skillsheets.services.Group;
import com.shilling.skillsheets.services.User;
import java.io.IOException;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

/**
 *
 * @author jake
 */
class GroupImpl <T extends GroupImpl>
        extends AbstractService<AccountGroup, T> 
        implements Group<T> {
    
    private final GroupFactory factory;
    
    protected GroupImpl (
            GroupFactory factory,
            User user,
            AccountGroup group) {
        super (user, group);
        
        Preconditions.checkNotNull (factory);
        
        this.factory = factory;
    }
    
    @Override
    public final boolean isTeam() {
        this.readLock().lock();
        try {
            return this.getResource().isTeam();
        } catch (IOException e) {
            throw new RuntimeException (e);
        } finally {
            this.readLock().unlock();
        }
    }

    @Override
    public final String serialize() {
        
        throw new UnsupportedOperationException ("this method has not been written");
        
    }
    
    @Override
    public T addKnownResource(UUID uuid) throws IllegalAccessException {
        if (!this.isWritable())
            throw new IllegalAccessException
                    ("You do not have permission to edit this group.");
        
        this.writeLock().lock();
        try {
            this.getResource().addKnownResource(uuid);
        } catch (IOException e) {
            throw new RuntimeException (e);
        } finally {
            this.writeLock().unlock();
        }
        
        return (T) this;
    }

    @Override
    public T add(User user) throws IllegalAccessException {
        if (!this.isWritable())
            throw new IllegalAccessException
                    ("You do not have permission to edit this group.");
        
        this.writeLock().lock();
        try {
            this.getResource().addMember(user.getUuid());
            user.addGroup (this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            this.writeLock().unlock();
        }
        
        return (T) this;
    }
    
    @Override
    public T add(Group group) throws IllegalAccessException {
        if (!this.isWritable())
            throw new IllegalAccessException
                    ("You do not have permission to edit this group.");
        
        this.writeLock().lock();
        try {
            this.getResource().addChild(group.getUuid());
            group.addParent(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            this.writeLock().unlock();
        }
        
        return (T) this;
    }

    @Override
    public T remove(User user) throws IllegalAccessException  {
        if (!this.isWritable())
            throw new IllegalAccessException
                    ("You do not have permission to edit this group.");
        
        this.writeLock().lock();
        try {
            this.getResource().delMember(user.getUuid());
            user.delGroup(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            this.writeLock().unlock();
        }
        
        return (T) this;
    }
    
    @Override
    public T remove(Group group) throws IllegalAccessException  {
        if (!this.isWritable())
            throw new IllegalAccessException
                    ("You do not have permission to edit this group.");
        
        this.writeLock().lock();
        try {
            this.getResource().addChild(group.getUuid());
            group.delParent(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            this.writeLock().unlock();
        }
        
        return (T) this;
    }
    
    @Override
    public T addParent(Group group) {
        this.writeLock().lock();
        try {
            this.getResource().addParent(group.getUuid());
        } catch (IOException e) {
            throw new RuntimeException (e);
        } finally {
            this.writeLock().unlock();
        }
        
        return (T) this;
    }

    @Override
    public T delParent(Group group) {
        this.writeLock().lock();
        try {
            this.getResource().delParent(group.getUuid());
        } catch (IOException e) {
            throw new RuntimeException (e);
        } finally {
            this.writeLock().unlock();
        }
        
        return (T) this;
    }
    
    @Override
    public Collection<Group> getParents() {
        ImmutableSet.Builder<Group> ret = new ImmutableSet.Builder<>();
        this.readLock().lock();
        Collection<UUID> uuids;
        try {
            uuids = this.getResource().getParents();
        } catch (IOException e) {
            throw new RuntimeException (e);
        } finally {
            this.readLock().unlock();
        }
        
        for (UUID uuid : uuids) {
            Optional<Group> group = this.factory.group (this.getUser(), uuid);
            if (group.isPresent())
                ret.add(group.get());
        }
        
        return ret.build();
    }
    
    @Override
    public final boolean contains(User user) {
        
        this.readLock().lock();
        try {
            Collection<UUID> members = this.getResource().getMembers();
            for (UUID uuid : members) {
                if (user.getUuid().equals(uuid))
                    return true;
            }
            
            Collection<UUID> children = this.getResource().getChildren();
            for (UUID uuid : children) {
                Optional<Group> group = this.factory.group(this.getUser(), uuid);
                if (group.isPresent()) {
                    if (group.get().contains(user))
                        return false;
                }
            }
            
            return false;
        } catch (IOException e) {
            throw new RuntimeException (e);
        } finally {
            this.readLock().unlock();
        }
        
    }
    
}
