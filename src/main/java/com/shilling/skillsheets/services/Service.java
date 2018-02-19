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
import java.util.Optional;
import javax.annotation.Nullable;

/**
 * Common methods for all service layer objects used to manipulate resources
 * in persistent storage.
 * 
 * @author Jake Shilling
 * @param <T>               Return type for method chaining.
 */
public interface Service<T extends Service> extends HasUuid {
    
    /**
     * Get the resources display name.
     * 
     * @return          Saved display name or Optional.empty()
     */
    public Optional<String> getDisplayName ();
    
    /**
     * Set the display name. If the display name is set to <tt>null</tt> the
     * old display name will simply be deleted.
     * 
     * @param displayName               New display name
     * @return                          <tt>this</tt> for chaining
     * @throws IllegalAccessException   If this operation is performed without
     *                                  editing permissions.
     */
    public T setDisplayName (@Nullable String displayName) throws IllegalAccessException;
    
    /**
     * Change the owner of this resource. This operation must be performed by the
     * current owner.
     * 
     * @param account                   New owner
     * @return                          <tt>this</tt> for chaining
     * @throws IllegalAccessException   If this operation is performed by someone
     *                                  other than the owner, or if account is not
     *                                  a teacher account.
     */
    public T giveTo (User user) throws IllegalAccessException;
    
    /**
     * Grant editing permissions to another user account. This operation must
     * be performed by someone who already has editing permissions.
     * 
     * @param account                   New editor
     * @return                          <tt>this</tt> for chaining
     * @throws IllegalAccessException   If this operation is performed without
     *                                  editing permissions, or if account is not
     *                                  a teacher account.
     */
    public T letEdit (User user) throws IllegalAccessException;
    
    /**
     * Grant editing permissions to another user group. This operation must
     * be performed by someone who already has editing permissions.
     * 
     * @param group                     New editor
     * @return                          <tt>this</tt> for chaining
     * @throws IllegalAccessException   If this operation is performed without
     *                                  editing permissions, or if group is not
     *                                  a team.
     */
    public T letEdit (Group group) throws IllegalAccessException;
    
    /**
     * Grant viewing permissions to another user account. This operation must
     * be performed by someone who already has editing permissions.
     * 
     * @param account                   New viewer
     * @return                          <tt>this</tt> for chaining
     * @throws IllegalAccessException   If this operation is performed without
     *                                  editing permissions.
     */
    public T letView (User user) throws IllegalAccessException;
    
    /**
     * Grant viewing permissions to another user group. This operation must
     * be performed by someone who already has editing permissions.
     * 
     * @param group                     New viewer
     * @return                          <tt>this</tt> for chaining
     * @throws IllegalAccessException   If this operation is performed without
     *                                  editing permissions.
     */
    public T letView (Group group) throws IllegalAccessException;
    
    /**
     * Delete this resource from persistent storage. This operation can only
     * be performed by its owner.
     * 
     * @throws IllegalAccessException   If this operation is performed by someone
     *                                  other than the owner.
     */
    public void delete () throws IllegalAccessException;
    
    /**
     * Serialize this resource to a JSON string.
     * 
     * @return                          JSON string.
     */
    public String serialize();
    
    public boolean isOwned();
    public boolean isWritable();
    
    public Optional<User> getOwner();
    public Collection<User> getEditingAccounts();
    public Collection<Group> getEditingGroups();
    public Collection<User> getViewingAccounts();
    public Collection<Group> getViewingGroups();
    
}
