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

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.collect.ImmutableSet;
import com.shilling.skillsheets.dao.AccountGroup;
import com.shilling.skillsheets.services.Group;
import com.shilling.skillsheets.services.User;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 *
 * @author jake
 */
@JsonSerialize (using = ResourceSerializer.class)
class GroupImpl <T extends GroupImpl>
        extends AbstractService<AccountGroup, T> 
        implements Group<T> {
    
    protected GroupImpl (
            UserFactory users,
            GroupFactory groups,
            User user,
            AccountGroup group) {
        super (users, groups, user, group);
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
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        
        try (JsonGenerator generator = new JsonFactory ().createGenerator(out, JsonEncoding.UTF8)) {
            
            generator.writeStartObject();
            
            Optional<User> owner = this.getOwner();
            if (this.isWritable()) {
                if (owner.isPresent()) {
                    generator.writeObjectField("owner", owner.get());
                } else {
                    generator.writeNullField("owner");
                }
                
                generator.writeObjectField("editing_accounts", this.getEditingAccounts());
                generator.writeObjectField("editing_groups", this.getEditingGroups());
                generator.writeObjectField("viewing_accounts", this.getViewingAccounts());
                generator.writeObjectField("viewing_groups", this.getViewingGroups());
            }
            
            generator.writeObjectField ("members", this.getMembers());
            generator.writeObjectField ("children", this.getChildren());
            generator.writeBooleanField ("team", this.isTeam());
            
            generator.writeEndObject();
            
        } catch (IOException e) {
            throw new RuntimeException (e);
        }
        
        return new String(out.toByteArray());
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
        
        if (this.isTeam() && !user.isTeacher())
            throw new IllegalAccessException
                    ("This user does not have permission to be in this group.");
        
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
        if (this.isTeam() && !group.isTeam())
            throw new IllegalAccessException
                    ("This group does not have permission to be in this group.");
        
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
        
        uuids.stream()
                .map((uuid) -> this.groups().group (this.getUser(), uuid))
                .filter((group) -> (group.isPresent()))
                .forEachOrdered((group) -> {
            ret.add(group.get());
        });
        
        return ret.build();
    }
    
    @Override
    public final boolean contains(User user) {
        
        this.readLock().lock();
        try {
            Collection<UUID> members = this.getResource().getMembers();
            if (members.stream().anyMatch((uuid) -> (user.getUuid().equals(uuid)))) {
                return true;
            }
            
            Collection<UUID> children = this.getResource().getChildren();
            return children.stream()
                    .map((uuid) -> this.groups().group(this.getUser(), uuid))
                    .filter((group) -> (group.isPresent()))
                    .anyMatch((group) -> (group.get().contains(user)));
        } catch (IOException e) {
            throw new RuntimeException (e);
        } finally {
            this.readLock().unlock();
        }
        
    }
    
    @Override
    public Collection<User> getMembers() {
        this.readLock().lock();
        try {
            Collection<UUID> uuids = this.getResource().getMembers();
            Collection<User> members = uuids.stream()
                    .map((uuid) -> this.users().user(uuid))
                    .filter ((o) -> o.isPresent())
                    .map((o) -> o.get())
                    .collect(Collectors.toSet());
            return new ImmutableSet.Builder<User>()
                    .addAll (members)
                    .build();
        } catch (IOException e) {
            throw new RuntimeException (e);
        } finally {
            this.readLock().unlock();
        }
    }
    
    @Override
    public Collection<Group> getChildren() {
        this.readLock().lock();
        try {
            Collection<UUID> uuids = this.getResource().getChildren();
            Collection<Group> children = uuids.stream()
                    .map((uuid) -> this.groups().group(this.getUser(), uuid))
                    .filter ((o) -> o.isPresent())
                    .map((o) -> o.get())
                    .collect(Collectors.toSet());
            return new ImmutableSet.Builder<Group>()
                    .addAll (children)
                    .build();
        } catch (IOException e) {
            throw new RuntimeException (e);
        } finally {
            this.readLock().unlock();
        }
    }
    
}
