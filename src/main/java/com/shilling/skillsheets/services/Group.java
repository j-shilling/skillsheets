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
import java.util.Collection;
import java.util.UUID;

/**
 * Common interface for service layer objects manipulating an
 * {@link AccountGroup}.
 *
 * @author Jake Shilling
 * @param <T> Return type used for method chaining.
 */
public interface Group<T extends Group> extends HasUuid, Service<T> {
    
    /**
     * Checks whether the underlying {@link AccountGroup} is a group
     * of teacher accounts.
     * 
     * @return
     */
    public boolean isTeam();
    
    public T addKnownResource(UUID uuid) throws IllegalAccessException;
    
    public Collection<Group> getParents();
    public T addParent(Group group);
    public T delParent(Group group);

    /**
     * Adds an account to this group. If the account is already in the group,
     * this method should have no effect. If the underlying group is a team, only
     * teacher accounts may be added.
     * 
     * @param user                      New member
     * @return                          <tt>this</tt> for chaining
     * @throws IllegalAccessException   If the requesting user does not have
     *                                  editing permissions.
     */
    public T add(User user) throws IllegalAccessException;
    
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
    public T add(Group group) throws IllegalAccessException;

    /**
     * Remove a user account from this group. If the account is not in the group,
     * this method has no effect.
     * 
     * @param user                      Removed member
     * @return                          <tt>this</tt> for chaining
     * @throws IllegalAccessException   If the requesting user does not have
     *                                  editing permissions.
     */
    public T remove(User user) throws IllegalAccessException;
    
    /**
     * Remove a subgroup account from this group. If the subgroup is not in the group,
     * this method has no effect.
     * 
     * @param group                     Removed group
     * @return                          <tt>this</tt> for chaining
     * @throws IllegalAccessException   If the requesting user does not have
     *                                  editing permissions.
     */
    public T remove(Group group) throws IllegalAccessException;

    /**
     * Check if the user is in this group.
     *  
     * @param user
     * @return  
     */
    public boolean contains(User user);
    
}
