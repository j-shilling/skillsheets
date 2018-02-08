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

import com.shilling.skillsheets.AbstractHasUuid;
import com.shilling.skillsheets.dao.Account;
import com.shilling.skillsheets.dao.AccountGroup;
import com.shilling.skillsheets.dao.Dao;
import com.shilling.skillsheets.dao.Resource;
import com.shilling.skillsheets.services.Team;
import com.shilling.skillsheets.services.User;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Queue;
import java.util.UUID;

/**
 *
 * @author jake
 */
public class Student extends AbstractHasUuid implements User {
    
    private final Account account;
    private final Dao<AccountGroup> groups;

    public Student(Dao<AccountGroup> groups, Account account) {
        super (account.getUuid());
        
        this.account = account;
        this.groups = groups;
    }
    
    protected final Account getAccount() {
        return this.account;
    }
    
    protected final boolean owns (Resource<?> resource) {
        try {
            return resource.isOwner(this.getUuid());
        } catch (IOException e) {
            throw new RuntimeException (e);
        }
    }
    
    protected final boolean edits (Resource<?> resource) {
        /* If we own, we edit */
        if (this.owns (resource))
            return true;
        
        try {
            /* Maybe we are and editor */
            if (resource.isEditor(this.getUuid()))
                return true;
            
            /* Maybe we are an editing group */
            Queue<UUID> uuids = new LinkedList<>();
            uuids.addAll(this.account.getGroups());
            while (!uuids.isEmpty()) {
                UUID uuid = uuids.remove();
                
                /* Is this group an editing group */
                if (resource.isEditor(uuid))
                    return true;
                
                /* This group might be a subgroup of an editing group */
                Optional<AccountGroup> group = this.groups.read(uuid);
                if (group.isPresent()) {
                    uuids.addAll(group.get().getParents());
                }
            }
            
            return false;
        } catch (IOException e) {
            throw new RuntimeException (e);
        }
    }
    
    protected final boolean views (Resource<?> resource) {
        /* If we edit, we view */
        if (this.edits (resource))
            return true;
        
        try {
            /* Maybe we are a viewer */
            if (resource.isViewer(this.getUuid()))
                return true;
            
            /* Maybe we are in a viewing group */
            Queue<UUID> uuids = new LinkedList<>();
            uuids.addAll(this.account.getGroups());
            while (!uuids.isEmpty()) {
                UUID uuid = uuids.remove();
                
                /* Is this group a viewing group */
                if (resource.isViewer(uuid))
                    return true;
                
                /* This group might be a subgroup of a viewing group */
                Optional<AccountGroup> group = this.groups.read(uuid);
                if (group.isPresent()) {
                    uuids.addAll(group.get().getParents());
                }
            }
            
            return false;
        } catch (IOException e) {
            throw new RuntimeException (e);
        }
    }

    @Override
    public Team newTeam() throws IllegalAccessException {
        throw new IllegalAccessException ("Student accounts cannot create teams");
    }

    @Override
    public Team team(UUID uuid) throws NoSuchElementException, IllegalAccessException {
       throw new IllegalAccessException ("Student accounts cannot view teams");
    }

    @Override
    public Collection<Team> teams() throws IllegalAccessException {
        throw new IllegalAccessException ("Student accounts cannot view teams");
    }
    
}
