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
package com.shilling.skillsheets.dao;

import java.io.IOException;
import java.util.UUID;
import javax.annotation.Nullable;

/**
 *
 * @author jake
 */
public interface Account extends Resource {
    
    public Account setName (@Nullable String name) throws IOException;
    public Account setId (String id) throws IOException;
    public Account setEmail (String email) throws IOException;
    
    public boolean isTeacher () throws IOException;

    public void addOwnedTeam(UUID uuid) throws IOException;
    
}
