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

import com.shilling.skillsheets.HasUuid;
import java.io.IOException;
import java.util.Collection;
import java.util.UUID;

/**
 *
 * @author jake
 */
public interface GroupMember<T extends GroupMember> extends HasUuid {
    
    public T addGroup (UUID uuid) throws IOException;
    public T delGroup(UUID uuid) throws IOException;
    public Collection<UUID> getGroups () throws IOException;
    default public boolean isInGroup (UUID uuid) throws IOException {
        return this.getGroups().contains(uuid);
    }

    default public boolean isTeacherOrTeam() throws IOException {
        if (this instanceof Account)
            return ((Account)this).isTeacher();
        else
            return ((AccountGroup)this).isTeam();
    }
}
