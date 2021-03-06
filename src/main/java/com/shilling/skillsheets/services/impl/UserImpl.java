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

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.collect.ImmutableSet;
import com.shilling.skillsheets.AbstractHasUuid;
import com.shilling.skillsheets.dao.Account;
import com.shilling.skillsheets.services.Group;
import com.shilling.skillsheets.services.Team;
import com.shilling.skillsheets.services.User;
import java.io.IOException;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

/**
 *
 * @author jake
 */
@JsonSerialize(using = UserSerializer.class)
public class UserImpl 
        extends AbstractHasUuid 
        implements User {
    
    private final Account account;
    private final GroupFactory groups;

    public UserImpl(GroupFactory groups, Account account) {
        super (account.getUuid());
        
        this.account = account;
        this.groups = groups;
    }
    
    protected final Account getAccount() {
        return this.account;
    }
    
    @Override
    public boolean isTeacher() {
        this.account.readLock().lock();
        try {
            return this.account.isTeacher();
        } catch (IOException e) {
            throw new RuntimeException (e);
        } finally {
            this.account.readLock().unlock();
        }
    }
    
    @Override
    public User setTeacher (boolean val) {
        this.account.writeLock().lock();
        try {
            this.account.setTeacher(val);
        } catch (IOException e) {
            throw new RuntimeException (e);
        } finally {
            this.account.writeLock().unlock();
        }
        
        return this;
    }
    
    @Override
    public User addKnownResource(UUID uuid) {
        this.account.writeLock().lock();
        try {
            this.account.addKnownResource(uuid);
        } catch (IOException e) {
            throw new RuntimeException (e);
        } finally {
            this.account.writeLock().unlock();
        }
        
        return this;
    }
    
    @Override
    public Collection<Group> getGroups () {
        
        this.account.readLock().lock();
        Collection<UUID> uuids;
        try {
            uuids = this.account.getGroups();
        } catch (IOException e) {
            throw new RuntimeException (e);
        } finally {
            this.account.readLock().unlock();
        }
        
        ImmutableSet.Builder<Group> ret = new ImmutableSet.Builder<>();
        for (UUID uuid : uuids) {
            Optional<Group> group = this.groups.group(this, uuid);
            if (group.isPresent())
                ret.add(group.get());
        }
        
        return ret.build();
    }
    
    @Override
    public User addGroup (Group group) {
        
        this.account.readLock().lock();
        try {
            this.account.addGroup(group.getUuid());
        } catch (IOException e) {
            throw new RuntimeException (e);
        } finally {
            this.account.readLock().unlock();
        }
        
        return this;
    }
    
    @Override
    public User delGroup (Group group) {
        
        this.account.readLock().lock();
        try {
            this.account.delGroup(group.getUuid());
        } catch (IOException e) {
            throw new RuntimeException (e);
        } finally {
            this.account.readLock().unlock();
        }
        
        return this;
    }
    
    @Override
    public User setId(String id) {
        
        this.account.writeLock().lock();
        try {
            this.account.setId(id);
        } catch (IOException e) {
            throw new RuntimeException (e);
        } finally {
            this.account.writeLock().unlock();
        }
        
        return this;
        
    }
    
    @Override
    public Optional<String> getId() {
        
        this.account.readLock().lock();
        try {
            return this.account.getId();
        } catch (IOException e) {
            throw new RuntimeException (e);
        } finally {
            this.account.readLock().unlock();
        }
        
    }

    @Override
    public User setEmail(String email) {
        
        this.account.writeLock().lock();
        try {
            this.account.setEmail(email);
        } catch (IOException e) {
            throw new RuntimeException (e);
        } finally {
            this.account.writeLock().unlock();
        }
        
        return this;
        
    }
    
    @Override
    public Optional<String> getEmail() {
        
        this.account.readLock().lock();
        try {
            return this.account.getEmail();
        } catch (IOException e) {
            throw new RuntimeException (e);
        } finally {
            this.account.readLock().unlock();
        }
        
    }

    @Override
    public User setDisplayName(String name) {
        
        this.account.writeLock().lock();
        try {
            this.account.setDisplayName(name);
        } catch (IOException e) {
            throw new RuntimeException (e);
        } finally {
            this.account.writeLock().unlock();
        }
        
        return this;
        
    }
    
    @Override
    public Optional<String> getDisplayName() {
        
        this.account.readLock().lock();
        try {
            return this.account.getDisplayName();
        } catch (IOException e) {
            throw new RuntimeException (e);
        } finally {
            this.account.readLock().unlock();
        }
        
    }

    @Override
    public Team newTeam() throws IllegalAccessException {
        throw new IllegalAccessException ("Student accounts cannot create teams");
    }

    @Override
    public Group team(UUID uuid) throws NoSuchElementException, IllegalAccessException {
        Collection<UUID> known;
        this.account.readLock().lock();
        try {
            known = this.account.getKnownResources();
        } catch (IOException e) {
            throw new RuntimeException (e);
        } finally {
            this.account.readLock().unlock();
        }
        
        if (!known.contains(uuid))
            throw new NoSuchElementException ("There is no such group in your inventory");
        
        Optional<Group> group = this.groups.group(this, uuid);
        if (group.isPresent())
            return group.get();
        else
            throw new NoSuchElementException ("There is no such group");
    }

    @Override
    public Collection<Team> teams() throws IllegalAccessException {
        throw new IllegalAccessException ("Student accounts cannot view teams");
    }

    
    
}
