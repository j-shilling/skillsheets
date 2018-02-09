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
import java.util.NoSuchElementException;
import java.util.UUID;

/**
 * Performs operations upon service layer objects with a particular identity.
 * Implementations wrap an {@link Account} object and are used to create and
 * manipulate resources viewable to the contained account.
 * 
 * @author Jake Shilling
 */
public interface User extends HasUuid {
    
    public boolean isTeacher();
    
    public User addKnownResource (UUID uuid);
    
    public Collection<Group> getGroups();
    public User addGroup (Group group);
    public User delGroup (Group group);
    
    /**
     * Creates a new {@link Team} to manipulate a collection of teacher
     * accounts.
     * 
     * @return                          Result. Never null.
     * @throws IllegalAccessException   Thrown if the operation is performed by
     *                                  a student account.
     */
    public Team newTeam() throws IllegalAccessException;
    
    /**
     * Get a {@link Team} objects to manipulate a previously created collection
     * of teacher accounts.
     * 
     * @param uuid                      Identifies the resource
     * @return                          The {@link Team} object. Never null.
     * @throws NoSuchElementException   Thrown if the UUID does not correspond
     *                                  to a resource viewable to this user.
     * @throws IllegalAccessException   Thrown if the operation is performed by
     *                                  a student account.
     */
    public Team team (UUID uuid) throws NoSuchElementException, IllegalAccessException;
    
    /**
     * Get a {@link Team} object for each existing {@link AccountGroup} visible
     * to this user.
     * 
     * @return                          A {@link Collection} of results. Never null.
     *                                  Maybe empty.
     * @throws IllegalAccessException   Thrown if the operation is performed by
     *                                  a student account.
     */
    public Collection<Team> teams () throws IllegalAccessException;

}
