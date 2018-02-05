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
import com.shilling.skillsheets.dao.Account;
import com.shilling.skillsheets.dao.AccountGroup;
import com.shilling.skillsheets.services.Group;
import com.shilling.skillsheets.services.Serializer;
import java.io.IOException;
import java.util.UUID;

/**
 *
 * @author jake
 */
public class AbstractViewableGroup <T extends AbstractViewableGroup>
        extends AbstractViewableResource<AccountGroup, T> 
        implements Group<T> {
    
    private final Serializer<AccountGroup> serializer;
    
    protected AbstractViewableGroup (
            Serializer<AccountGroup> serializer,
            AccountGroup group) {
        super (group);
        
        Preconditions.checkNotNull (serializer);
        
        this.serializer = serializer;
    }

    @Override
    public final String serialize() {
        
        return this.serializer.writeValueAsString(this.getResource());
        
    }

    @Override
    public final T add(Account account) throws IllegalAccessException {
        throw new IllegalAccessException
                ("You do not have permission to edit this group.");
    }

    @Override
    public final T add(AccountGroup group) throws IllegalAccessException  {
        throw new IllegalAccessException
                ("You do not have permission to edit this group.");
    }

    @Override
    public final T remove(Account account) throws IllegalAccessException  {
        throw new IllegalAccessException
                ("You do not have permission to edit this group.");
    }

    @Override
    public final T remove(AccountGroup group)  throws IllegalAccessException {
        throw new IllegalAccessException
                ("You do not have permission to edit this group.");
    }

    @Override
    public final boolean contains(UUID uuid) {
        try {
            return this.getResource().contains(uuid);
        } catch (IOException e) {
            throw new RuntimeException (e);
        }
    }
    
}
