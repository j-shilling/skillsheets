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
import com.google.common.collect.ImmutableSet;
import com.shilling.skillsheets.dao.Account;
import com.shilling.skillsheets.dao.AccountGroup;
import com.shilling.skillsheets.dao.Dao;
import com.shilling.skillsheets.services.Team;
import java.io.IOException;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

/**
 *
 * @author jake
 */
public class Teacher extends Student {
    
    private final Dao<AccountGroup> groups;
    private final TeamFactory teams;

    public Teacher(
            Dao<AccountGroup> groups, 
            TeamFactory teams,
            Account account) {
        
        super (account);
        
        Preconditions.checkNotNull(groups);
        Preconditions.checkNotNull(teams);
        
        this.groups = groups;
        this.teams = teams;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Team newTeam() throws IllegalAccessException {
        try {
            AccountGroup group = this.groups.create().setTeam(true);
            this.getAccount().addOwnedTeam (group.getUuid());
            
            return null;
        } catch (IOException e) {
            throw new RuntimeException (e);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Team team(UUID uuid) throws NoSuchElementException, IllegalAccessException {
        Preconditions.checkNotNull(uuid);
        
        try {
            Optional<AccountGroup> result = this.groups.read(uuid);
            if (!result.isPresent())
                throw new NoSuchElementException();
            
            if (this.owns (result.get()))
                return this.teams.owned(result.get());
            if (this.edits (result.get()))
                return this.teams.editable(result.get());
            if (this.views(result.get()))
                return this.teams.viewable(result.get());
            
            throw new NoSuchElementException();
        } catch (IOException e) {
            throw new RuntimeException (e);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Team> teams() throws IllegalAccessException {
        
        Collection<UUID> res;
        
        try {
            res = this.getAccount().getKnownResources();
        } catch (IOException e) {
            throw new RuntimeException (e);
        }
        
        ImmutableSet.Builder<Team> ret = new ImmutableSet.Builder<>();
        
        for (UUID uuid : res) {
            try {
                ret.add(this.team(uuid));
            } catch (NoSuchElementException e) {
                try {
                    this.getAccount().delKnownResource(uuid);
                } catch (IOException e1) {
                    throw new RuntimeException (e1);
                }
            }
        }
        
        return ret.build();
        
    }
    
}
