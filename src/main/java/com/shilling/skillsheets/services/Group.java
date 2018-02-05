/*
 * Copyright (C) 2018 Jake Shilling
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
package com.shilling.skillsheets.services;

import com.shilling.skillsheets.HasUuid;
import com.shilling.skillsheets.dao.Account;
import com.shilling.skillsheets.dao.AccountGroup;
import java.util.UUID;

/**
 *
 * @author jake
 */
public interface Group<T extends Group> extends HasUuid, ResourceWrapper<T> {

    public T add (Account account) throws IllegalAccessException ;
    public T add (AccountGroup group) throws IllegalAccessException ;
    public T remove (Account account) throws IllegalAccessException ;
    public T remove (AccountGroup group) throws IllegalAccessException ;
    
    public boolean contains (UUID uuid);
    default public boolean contains (HasUuid hasUuid) {
        return this.contains (hasUuid.getUuid());
    }
    
}
