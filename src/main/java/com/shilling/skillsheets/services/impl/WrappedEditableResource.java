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

/**
 *
 * @author jake
 */
class WrappedEditableResource<T extends Resource>
       extends WrappedViewableResource<T> {
    
    public WrappedEditableResource(T resource) {
        super(resource);
    }
    
    @Override
    public final WrappedResource<T> letEdit(Account account)  throws IllegalAccessException{
        this.writeLock().lock();
        try {
            if (!account.isTeacher())
                throw new IllegalAccessException 
                    ("Cannot give a student editing permissions");
            this.getResource().addEditor(account.getUuid());
            account.addKnownResource(this.getUuid());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            this.writeLock().unlock();
        }
        
        return this;
    }
    
    @Override
    public final WrappedResource<T> letEdit(AccountGroup group)  throws IllegalAccessException{
        this.writeLock().lock();
        try {
            if (!group.isTeam())
                throw new IllegalAccessException 
                    ("Cannot give a student group editing permissions");
            this.getResource().addEditor(group.getUuid());
            group.addKnownResource(this.getUuid());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            this.writeLock().unlock();
        }
        
        return this;
    }

    @Override
    public final WrappedResource<T> letView(Account account)  throws IllegalAccessException{
        this.writeLock().lock();
        try {
            this.getResource().addViewer(account.getUuid());
            account.addKnownResource(this.getUuid());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            this.writeLock().unlock();
        }
        
        return this;
    }
    
    @Override
    public final WrappedResource<T> letView(AccountGroup group)  throws IllegalAccessException{
        this.writeLock().lock();
        try {
            this.getResource().addViewer(group.getUuid());
            group.addKnownResource(this.getUuid());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            this.writeLock().unlock();
        }
        
        return this;
    }
    
}
