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
import com.shilling.skillsheets.dao.Dao;
import com.shilling.skillsheets.dao.Resource;
import java.io.IOException;

/**
 *
 * @author jake
 */
abstract class AbstractOwnedResource<T extends Resource, W extends AbstractOwnedResource> 
        extends AbstractEditableResource<T, W> {
    
    private final Dao<T> dao;

    public AbstractOwnedResource(Dao<T> dao, T resource) {
        super(resource);
        this.dao = dao;
    }

    @Override
    public final W giveTo(Account account)  throws IllegalAccessException {
        try {
            if (!account.isTeacher())
                throw new IllegalAccessException 
                    ("Cannot give a student ownership of a resource");
            this.getResource().setOwner(account.getUuid());
        } catch (IOException e) {
            throw new RuntimeException (e);
        }
        
        return (W) this;
    }
    
    @Override
    public final void delete() throws IllegalAccessException {
        try {
            this.dao.remove(this.getUuid());
            this.getResource().delete();
        } catch (IOException e) {
            throw new RuntimeException (e);
        }
    }
    
}
