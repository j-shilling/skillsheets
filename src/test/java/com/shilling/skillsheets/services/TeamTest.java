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
package com.shilling.skillsheets.services;

import com.shilling.skillsheets.dao.Account;
import com.shilling.skillsheets.dao.AccountDao;
import com.shilling.skillsheets.dao.AccountGroup;
import com.shilling.skillsheets.dao.Dao;
import com.shilling.skillsheets.services.impl.UserFactory;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 *
 * @author jake
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes={SeriveLayerTest.class})
public class TeamTest {
    
    @Autowired
    private UserFactory factory;
    @Autowired
    private AccountDao accounts;
    @Autowired
    private Dao<AccountGroup> groups;

    @Test
    public void addAccountTest () throws Exception {
        User user = this.factory.teacher(this.accounts.create().setTeacher(true));
        Account account = this.accounts.create().setTeacher(true);
        Team team = user.newTeam();
        
        assertFalse (account.getGroups().contains(team.getUuid()));
        assertFalse (team.contains(account));
        
        team.add(account);
        
        assertTrue (account.getGroups().contains(team.getUuid()));
        assertTrue (team.contains(account));
    }
    
    @Test (expected = IllegalAccessException.class)
    public void addAccountStudentTest () throws Exception {
        User user = this.factory.teacher(this.accounts.create().setTeacher(true));
        Account account = this.accounts.create().setTeacher(false);
        Team team = user.newTeam();
        
        team.add(account);
    }
    
    @Test
    public void addAccountGroupTest () throws Exception {
        User user = this.factory.teacher(this.accounts.create().setTeacher(true));
        AccountGroup group = this.groups.create().setTeam(true);
        Team team = user.newTeam();
        
        assertFalse (group.getGroups().contains(team.getUuid()));
        assertFalse (team.contains(group));
        
        team.add(group);
        
        assertTrue (group.getGroups().contains(team.getUuid()));
        assertTrue (team.contains(group));
    }
    
    @Test (expected = IllegalAccessException.class)
    public void addAccountGroupStudentTest () throws Exception {
        User user = this.factory.teacher(this.accounts.create().setTeacher(true));
        AccountGroup group = this.groups.create().setTeam(false);
        Team team = user.newTeam();
        
        team.add(group);
    }
    
    @Test
    public void removeAccountTest () throws Exception {
        User user = this.factory.teacher(this.accounts.create().setTeacher(true));
        Account account = this.accounts.create().setTeacher(true);
        Team team = user.newTeam();
        
        team.add(account);
        
        assertTrue (account.getGroups().contains(team.getUuid()));
        assertTrue (team.contains(account));
        
        team.remove(account);
        
        assertFalse (account.getGroups().contains(team.getUuid()));
        assertFalse (team.contains(account));
    }
    
    @Test
    public void removeAccountGroupTest () throws Exception {
        User user = this.factory.teacher(this.accounts.create().setTeacher(true));
        AccountGroup group = this.groups.create().setTeam(true);
        Team team = user.newTeam();
        
        team.add(group);
        
        assertTrue (group.getGroups().contains(team.getUuid()));
        assertTrue (team.contains(group));
        
        team.remove(group);
        
        assertFalse (group.getGroups().contains(team.getUuid()));
        assertFalse (team.contains(group));
    }
    
}
