/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shilling.skillsheets.services;

import com.shilling.skillsheets.dao.Account;
import com.shilling.skillsheets.dao.AccountDao;
import com.shilling.skillsheets.dao.AccountGroup;
import com.shilling.skillsheets.dao.Dao;
import com.shilling.skillsheets.services.impl.UserFactory;
import java.util.Collection;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.UUID;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 *
 * @author Jake Shilling <shilling.jake@gmail.com>
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes={SeriveLayerTest.class})
public class UserTest {
    
    @Autowired
    private UserFactory factory;
    @Autowired
    private AccountDao accounts;
    @Autowired
    private Dao<AccountGroup> groups;
    
    @Test
    public void newTeamTest() throws Exception {
        Account account = accounts.create().setTeacher(true);
        User user = this.factory.user(account.getUuid()).get();
        Team team = user.newTeam();
        
        assertTrue (groups.read (team.getUuid()).isPresent());
        AccountGroup group = groups.read (team.getUuid()).get();
        
        assertTrue (group.isOwner(user.getUuid()));
        assertTrue (group.isTeam());
        assertTrue (account.getKnownResources().contains(team.getUuid()));
        
        groups.remove(team.getUuid());
    }
    
    @Test(expected = IllegalAccessException.class)
    public void newTeamStudentTest() throws Exception {
        Account account = accounts.create().setTeacher(false);
        User user = this.factory.user(account.getUuid()).get();
        user.newTeam();
    }
    
    @Test
    public void teamTest () throws Exception {
        Account account = accounts.create().setTeacher(true);
        User user = this.factory.user(account.getUuid()).get();
        
        Team team = user.newTeam();
        
        assertEquals (team, user.team(team.getUuid()));
        
        groups.remove (team.getUuid());
    }
    
    @Test(expected = NoSuchElementException.class)
    public void teamNotFoundTest () throws Exception {
        Account account = accounts.create().setTeacher(true);
        User user = this.factory.user(account.getUuid()).get();
        
        user.team(UUID.randomUUID());
    }
    
    @Test(expected = IllegalAccessException.class)
    public void teamStudentTest () throws Exception {
        Account account = accounts.create().setTeacher(false);
        User user = this.factory.user(account.getUuid()).get();
        
        user.team(UUID.randomUUID());
    }
    
    @Test
    public void teamsTest () throws Exception {
        Account account = accounts.create().setTeacher(true);
        User user = this.factory.user(account.getUuid()).get();
        
        Collection<Team> teams = new HashSet<>();
        
        teams.add(user.newTeam());
        teams.add(user.newTeam());
        teams.add(user.newTeam());
        
        assertEquals (teams, user.teams());
        
        for (Team team : teams)
            groups.remove(team.getUuid());
    }
    
    @Test(expected = IllegalAccessException.class)
    public void teamsStudentTest () throws Exception {
        Account account = accounts.create().setTeacher(false);
        User user = this.factory.user(account.getUuid()).get();
        
        user.teams();
    }
}
