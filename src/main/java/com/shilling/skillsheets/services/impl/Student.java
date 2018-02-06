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

import com.google.api.client.repackaged.com.google.common.base.Preconditions;
import com.shilling.skillsheets.AbstractHasUuid;
import com.shilling.skillsheets.dao.Account;
import com.shilling.skillsheets.dao.Resource;
import com.shilling.skillsheets.services.Team;
import com.shilling.skillsheets.services.User;
import java.io.IOException;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.UUID;

/**
 *
 * @author jake
 */
public class Student extends AbstractHasUuid implements User {
    
    private final Account account;

    public Student(Account account) {
        super (account.getUuid());
        
        this.account = account;
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
        if (this.owns (resource))
            return true;
        
        try {
            if (resource.isEditor(this.getUuid()))
                return true;
            
            Collection<UUID> groups = this.account.getGroups();
            for (UUID group : groups) {
                if (resource.isEditor (group))
                    return true;
            }
            
            return false;
        } catch (IOException e) {
            throw new RuntimeException (e);
        }
    }
    
    protected final boolean views (Resource<?> resource) {
        if (this.edits (resource))
            return true;
        
        try {
            if (resource.isViewer(this.getUuid()))
                return true;
            
            Collection<UUID> groups = this.account.getGroups();
            for (UUID group : groups) {
                if (resource.isViewer (group))
                    return true;
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
