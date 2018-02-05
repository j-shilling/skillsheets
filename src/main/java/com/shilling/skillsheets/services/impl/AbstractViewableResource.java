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
import com.shilling.skillsheets.dao.Resource;
import com.shilling.skillsheets.services.ResourceWrapper;
import java.io.IOException;
import java.util.Optional;

/**
 *
 * @author jake
 */
abstract class AbstractViewableResource<T extends Resource, W extends AbstractViewableResource> 
        extends AbstractHasUuid
        implements ResourceWrapper<W> {
    
    private final T resource;
    
    protected AbstractViewableResource (T resource) {
        super (resource.getUuid());
        
        this.resource = resource;
    }
    
    protected final T getResource() {
        return this.resource;
    }

    @Override
    public final Optional<String> getDisplayName() {
        try {
            return this.getResource().getDisplayName();
        } catch (IOException e) {
            throw new RuntimeException (e);
        }
    }

    @Override
    public W setDisplayName(String displayName) throws IllegalAccessException{
        throw new IllegalAccessException 
            ("You do not have permission to edit this resource.");
    }

    @Override
    public W giveTo(Account account)  throws IllegalAccessException{
        throw new IllegalAccessException 
            ("You do not have permission to edit the owner of this resource.");
    }

    @Override
    public W letEdit(Account account)  throws IllegalAccessException{
        throw new IllegalAccessException 
            ("You do not have permission to edit this resource.");
    }
    
    @Override
    public W letEdit(AccountGroup group)  throws IllegalAccessException{
        throw new IllegalAccessException 
            ("You do not have permission to edit this resource.");
    }

    @Override
    public W letView(Account account)  throws IllegalAccessException{
        throw new IllegalAccessException 
            ("You do not have permission to edit this resource.");
    }
    
    @Override
    public W letView(AccountGroup group)  throws IllegalAccessException{
        throw new IllegalAccessException 
            ("You do not have permission to edit this resource.");
    }

    @Override
    public void delete()  throws IllegalAccessException{
        throw new IllegalAccessException 
            ("You do not have permission to delete this resource.");
    }
    
}
