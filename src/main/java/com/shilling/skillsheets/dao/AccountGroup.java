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
package com.shilling.skillsheets.dao;

import java.io.IOException;
import java.util.Collection;
import java.util.UUID;

/**
 *
 * @author jake
 */
public interface AccountGroup extends Resource<AccountGroup> {
    
    public AccountGroup setTeam (boolean val) throws IOException;
    public boolean isTeam () throws IOException;

    public AccountGroup addKnownResource(UUID uuid) throws IOException;
    public boolean contains (UUID uuid) throws IOException;
    public Collection<UUID> getMembers () throws IOException;
    
    public AccountGroup addMember (UUID uuid) throws IOException;
    public AccountGroup delMember (UUID uuid) throws IOException;
    
    public AccountGroup addChild (UUID uuid) throws IOException;
    public AccountGroup delChild (UUID uuid) throws IOException;
    
    public AccountGroup addParent (UUID uuid) throws IOException;
    public AccountGroup delParent (UUID uuid) throws IOException;
    public Collection<UUID> getParents () throws IOException;
    public Collection<UUID> getChildren() throws IOException;
    
    public AccountGroup delKnownResource (UUID uuid) throws IOException;

    
}
