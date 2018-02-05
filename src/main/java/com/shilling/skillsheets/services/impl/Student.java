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

import com.shilling.skillsheets.AbstractHasUuid;
import com.shilling.skillsheets.dao.Account;
import com.shilling.skillsheets.services.Team;
import com.shilling.skillsheets.services.User;

/**
 *
 * @author jake
 */
public class Student extends AbstractHasUuid implements User {
    
    private final Account account;

    public Student(Account account) {
        super (account.getUuid());
        
        this.account = account;
    }
    
    protected final Account getAccount() {
        return this.account;
    }

    @Override
    public Team newTeam() throws IllegalAccessException {
        throw new IllegalAccessException ("Student accounts cannot create teams");
    }
    
}