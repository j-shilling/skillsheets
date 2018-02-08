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
import java.util.Collection;
import java.util.UUID;

/**
 * Common interface for service layer objects manipulating an
 * {@link AccountGroup}.
 *
 * @author Jake Shilling
 * @param <T> Return type used for method chaining.
 */
public interface Group<T extends Group> extends HasUuid, ResourceWrapper<T> {

    /**
     * Adds an account to this group. If the account is already in the group,
     * this method should have no effect. If the underlying group is a team, only
     * teacher accounts may be added.
     * 
     * @param account                   New member
     * @return                          <tt>this</tt> for chaining
     * @throws IllegalAccessException   If the requesting user does not have
     *                                  editing permissions.
     */
    public T add(Account account) throws IllegalAccessException;
    
    /**
     * Adds a subgroup to this group. If the subgroup is already in the group,
     * this method should have no effect. If the underlying group is a team, only
     * teacher accounts may be added.
     * 
     * @param group                     New member
     * @return                          <tt>this</tt> for chaining
     * @throws IllegalAccessException   If the requesting user does not have
     *                                  editing permissions.
     */
    public T add(AccountGroup group) throws IllegalAccessException;

    /**
     * Remove a user account from this group. If the account is not in the group,
     * this method has no effect.
     * 
     * @param account                   Removed member
     * @return                          <tt>this</tt> for chaining
     * @throws IllegalAccessException   If the requesting user does not have
     *                                  editing permissions.
     */
    public T remove(Account account) throws IllegalAccessException;
    
    /**
     * Remove a subgroup account from this group. If the subgroup is not in the group,
     * this method has no effect.
     * 
     * @param group                     Removed group
     * @return                          <tt>this</tt> for chaining
     * @throws IllegalAccessException   If the requesting user does not have
     *                                  editing permissions.
     */
    public T remove(AccountGroup group) throws IllegalAccessException;

    /**
     * Check if the UUID is in this group.
     *  
     * @param uuid
     * @return  
     */
    public boolean contains(UUID uuid);

    /**
     * Convenience method which calls {@link #contains(java.util.UUID)}
     * 
     * @param hasUuid
     * @return 
     */
    default public boolean contains(HasUuid hasUuid) {
        return this.contains(hasUuid.getUuid());
    }

}
