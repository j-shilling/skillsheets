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

import com.shilling.skillsheets.dao.AccountDao;
import com.shilling.skillsheets.dao.AccountGroup;
import com.shilling.skillsheets.dao.Dao;
import com.shilling.skillsheets.dao.Resource;
import com.shilling.skillsheets.services.impl.Student;
import com.shilling.skillsheets.services.impl.UserFactory;
import java.lang.reflect.Method;
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
public class UserPermissionsTest {
    
    @Autowired
    private UserFactory factory;
    @Autowired
    private AccountDao accounts;
    @Autowired
    private Dao<AccountGroup> groups;
    
    private boolean owns (User user, Resource<?>res) {
        try {
            Class<Student> klass = Student.class;
            Method method = klass.getDeclaredMethod("owns", Resource.class);
            method.setAccessible(true);
            
            Boolean ret = (Boolean) method.invoke(user, res);
            return ret;
        } catch (Exception e) {
            throw new RuntimeException (e);
        }
    }
    
    private boolean edits (User user, Resource<?>res) {
        try {
            Class<Student> klass = Student.class;
            Method method = klass.getDeclaredMethod("edits", Resource.class);
            method.setAccessible(true);
            
            Boolean ret = (Boolean) method.invoke(user, res);
            return ret;
        } catch (Exception e) {
            throw new RuntimeException (e);
        }
    }
    
    private boolean views (User user, Resource<?>res) {
        try {
            Class<Student> klass = Student.class;
            Method method = klass.getDeclaredMethod("views", Resource.class);
            method.setAccessible(true);
            
            Boolean ret = (Boolean) method.invoke(user, res);
            return ret;
        } catch (Exception e) {
            throw new RuntimeException (e);
        }
    }

    @Test
    public void testOwner () throws Exception {
        User user = this.factory.teacher(this.accounts.create().setTeacher(true));
        Team team = user.newTeam();
        Resource<?> res = this.groups.read(team.getUuid()).get();
        
        assertTrue (this.owns (user, res));
        assertTrue (this.edits (user, res));
        assertTrue (this.views(user, res));
        assertFalse (this.owns (this.factory.teacher(this.accounts.create().setTeacher(true)), res));
    }
    
    @Test
    public void testEdits () throws Exception {
        User creator = this.factory.teacher(this.accounts.create().setTeacher(true));
        Team team = creator.newTeam();
        Resource<?> res = this.groups.read(team.getUuid()).get();
        
        User user = this.factory.teacher(this.accounts.create().setTeacher(true));
        team.letEdit(this.accounts.read(user.getUuid()).get());
        
        assertFalse (this.owns (user, res));
        assertTrue (this.edits (user, res));
        assertTrue (this.views(user, res));
        
        assertFalse (this.edits (this.factory.teacher(this.accounts.create().setTeacher(true)), res));
    }
    
    @Test
    public void testViews () throws Exception {
        User creator = this.factory.teacher(this.accounts.create().setTeacher(true));
        Team team = creator.newTeam();
        Resource<?> res = this.groups.read(team.getUuid()).get();
        
        User user = this.factory.teacher(this.accounts.create().setTeacher(true));
        team.letView (this.accounts.read(user.getUuid()).get());
        
        assertFalse (this.owns (user, res));
        assertFalse (this.edits (user, res));
        assertTrue (this.views(user, res));
        
        assertFalse (this.views (this.factory.teacher(this.accounts.create().setTeacher(true)), res));
    }
    
    @Test
    public void testGroupEdits () throws Exception {
        User creator = this.factory.teacher(this.accounts.create().setTeacher(true));
        Team team = creator.newTeam();
        Team group = creator.newTeam();
        Resource<?> res = this.groups.read(team.getUuid()).get();
        
        User user = this.factory.teacher(this.accounts.create().setTeacher(true));
        group.add(this.accounts.read(user.getUuid()).get());
        team.letEdit(this.groups.read(group.getUuid()).get());
        
        assertFalse (this.owns (user, res));
        assertTrue (this.edits (user, res));
        assertTrue (this.views(user, res));
    }
    
    @Test
    public void testGroupViews () throws Exception {
        User creator = this.factory.teacher(this.accounts.create().setTeacher(true));
        Team team = creator.newTeam();
        Team group = creator.newTeam();
        Resource<?> res = this.groups.read(team.getUuid()).get();
        
        User user = this.factory.teacher(this.accounts.create().setTeacher(true));
        group.add(this.accounts.read(user.getUuid()).get());
        team.letView(this.groups.read(group.getUuid()).get());
        
        assertFalse (this.owns (user, res));
        assertFalse (this.edits (user, res));
        assertTrue (this.views(user, res));
    }
    
    @Test
    public void testSubgroupEdits () throws Exception {
        User creator = this.factory.teacher(this.accounts.create().setTeacher(true));
        Team team = creator.newTeam();
        Team group = creator.newTeam();
        Team parent = creator.newTeam();
        Resource<?> res = this.groups.read(team.getUuid()).get();
        
        User user = this.factory.teacher(this.accounts.create().setTeacher(true));
        group.add(this.accounts.read(user.getUuid()).get());
        parent.add (this.groups.read(group.getUuid()).get());
        team.letEdit(this.groups.read(parent.getUuid()).get());
        
        assertFalse (this.owns (user, res));
        assertTrue (this.edits (user, res));
        assertTrue (this.views(user, res));
    }
    
    @Test
    public void testSubgroupViews () throws Exception {
        User creator = this.factory.teacher(this.accounts.create().setTeacher(true));
        Team team = creator.newTeam();
        Team group = creator.newTeam();
        Team parent = creator.newTeam();
        Resource<?> res = this.groups.read(team.getUuid()).get();
        
        User user = this.factory.teacher(this.accounts.create().setTeacher(true));
        group.add(this.accounts.read(user.getUuid()).get());
        parent.add (this.groups.read(group.getUuid()).get());
        team.letEdit(this.groups.read(parent.getUuid()).get());
        
        assertFalse (this.owns (user, res));
        assertFalse (this.edits (user, res));
        assertTrue (this.views(user, res));
    }
}
