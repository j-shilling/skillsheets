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

import com.shilling.skillsheets.dao.Account;
import com.shilling.skillsheets.dao.AccountGroup;
import com.shilling.skillsheets.dao.Resource;
import java.io.IOException;
import java.util.Optional;

/**
 *
 * @author jake
 */
class WrappedViewableResource<T extends Resource>
        extends WrappedResource<T> {
    
    protected WrappedViewableResource (T resource) {
        super (resource);
    }

    @Override
    public final Optional<String> getDisplayName() {
        this.readLock().lock();
        try {
            return this.getResource().getDisplayName();
        } catch (IOException e) {
            throw new RuntimeException (e);
        } finally {
            this.readLock().unlock();;
        }
    }

    @Override
    public WrappedResource<T> setDisplayName(String displayName) throws IllegalAccessException{
        throw new IllegalAccessException 
            ("You do not have permission to edit this resource.");
    }

    @Override
    public WrappedResource<T> giveTo(Account account)  throws IllegalAccessException{
        throw new IllegalAccessException 
            ("You do not have permission to edit the owner of this resource.");
    }

    @Override
    public WrappedResource<T> letEdit(Account account)  throws IllegalAccessException{
        throw new IllegalAccessException 
            ("You do not have permission to edit this resource.");
    }
    
    @Override
    public WrappedResource<T> letEdit(AccountGroup group)  throws IllegalAccessException{
        throw new IllegalAccessException 
            ("You do not have permission to edit this resource.");
    }

    @Override
    public WrappedResource<T> letView(Account account)  throws IllegalAccessException{
        throw new IllegalAccessException 
            ("You do not have permission to edit this resource.");
    }
    
    @Override
    public WrappedResource<T> letView(AccountGroup group)  throws IllegalAccessException{
        throw new IllegalAccessException 
            ("You do not have permission to edit this resource.");
    }

    @Override
    public void delete()  throws IllegalAccessException{
        throw new IllegalAccessException 
            ("You do not have permission to delete this resource.");
    }
    
}
