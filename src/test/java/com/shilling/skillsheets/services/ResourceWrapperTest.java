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

import com.shilling.skillsheets.dao.Account;
import com.shilling.skillsheets.dao.AccountDao;
import com.shilling.skillsheets.dao.AccountGroup;
import com.shilling.skillsheets.dao.Dao;
import com.shilling.skillsheets.dao.Resource;
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
public class ResourceWrapperTest {
    
    @Autowired
    private UserFactory factory;
    @Autowired
    private AccountDao accounts;
    @Autowired
    private Dao<AccountGroup> groups;
    
    @Test
    public void giveToTest () throws Exception {
        User user = this.factory.teacher(this.accounts.create().setTeacher(true));
        Account account = this.accounts.create().setTeacher(true);
        
        Service<?> wrap = user.newTeam();
        Resource res = groups.read(wrap.getUuid()).get();
        
        assertTrue (res.isOwner(user.getUuid()));
        
        wrap.giveTo(account);
        
        assertFalse (res.isOwner(user.getUuid()));
        assertTrue (res.isOwner(account.getUuid()));
    }
    
    @Test(expected = IllegalAccessException.class)
    public void giveToStudentTest () throws Exception {
        User user = this.factory.teacher(this.accounts.create().setTeacher(true));
        Account account = this.accounts.create().setTeacher(false);
        
        Service<?> wrap = user.newTeam();
        wrap.giveTo(account);
    }
    
    @Test
    public void letEditTest () throws Exception {
        User user = this.factory.teacher(this.accounts.create().setTeacher(true));
        Account account = this.accounts.create().setTeacher(true);
        
        Service<?> wrap = user.newTeam();
        Resource res = groups.read(wrap.getUuid()).get();
        
        assertFalse (res.isEditor(account.getUuid()));
        
        wrap.letEdit(account);
        
        assertTrue (res.isEditor(account.getUuid()));
    }
    
    @Test(expected = IllegalAccessException.class)
    public void letEditStudentTest () throws Exception {
        User user = this.factory.teacher(this.accounts.create().setTeacher(true));
        Account account = this.accounts.create().setTeacher(false);
        
        Service<?> wrap = user.newTeam();
        wrap.letEdit(account);
    }
    
    @Test
    public void letEditTeamTest () throws Exception {
        User user = this.factory.teacher(this.accounts.create().setTeacher(true));
        AccountGroup group = this.groups.create().setTeam(true);
        
        Service<?> wrap = user.newTeam();
        Resource res = groups.read(wrap.getUuid()).get();
        
        assertFalse (res.isEditor(group.getUuid()));
        
        wrap.letEdit(group);
        
        assertTrue (res.isEditor(group.getUuid()));
    }
    
    @Test(expected = IllegalAccessException.class)
    public void letEditRosterTest () throws Exception {
        User user = this.factory.teacher(this.accounts.create().setTeacher(true));
        AccountGroup group = this.groups.create().setTeam(false);
        
        Service<?> wrap = user.newTeam();
        wrap.letEdit(group);
    }
    
    @Test
    public void letViewTest () throws Exception {
        User user = this.factory.teacher(this.accounts.create().setTeacher(true));
        Account account = this.accounts.create().setTeacher(true);
        
        Service<?> wrap = user.newTeam();
        Resource res = groups.read(wrap.getUuid()).get();
        
        assertFalse (res.isViewer(account.getUuid()));
        
        wrap.letView(account);
        
        assertTrue (res.isViewer(account.getUuid()));
    }
    
    @Test
    public void letViewTeamTest () throws Exception {
        User user = this.factory.teacher(this.accounts.create().setTeacher(true));
        AccountGroup group = this.groups.create().setTeam(true);
        
        Service<?> wrap = user.newTeam();
        Resource res = groups.read(wrap.getUuid()).get();
        
        assertFalse (res.isViewer(group.getUuid()));
        
        wrap.letView(group);
        
        assertTrue (res.isViewer(group.getUuid()));
    }
}
