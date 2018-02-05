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
import com.shilling.skillsheets.dao.AccountGroup;
import com.shilling.skillsheets.dao.AccountGroupDao;
import com.shilling.skillsheets.services.Team;
import java.io.IOException;

/**
 *
 * @author jake
 */
public class Teacher extends Student {
    
    private final AccountGroupDao accountGroupDao;

    public Teacher(AccountGroupDao accountGroupDao, Account account) {
        super (account);
        this.accountGroupDao = accountGroupDao;
    }
    
    /**
    * {@inheritDoc}
    */
    @Override
    public Team newTeam() throws IllegalAccessException {
        try {
            AccountGroup group = this.accountGroupDao.create().setTeam(true);
            this.getAccount().addOwnedTeam (group.getUuid());
            
            return null;
        } catch (IOException e) {
            throw new RuntimeException (e);
        }
    }
    
}
