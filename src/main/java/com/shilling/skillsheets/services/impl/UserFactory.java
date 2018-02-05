/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shilling.skillsheets.services.impl;

import com.google.api.client.util.Preconditions;
import com.shilling.skillsheets.dao.Account;
import com.shilling.skillsheets.dao.AccountGroup;
import com.shilling.skillsheets.dao.Dao;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Jake Shilling <shilling.jake@gmail.com>
 */
public class UserFactory {
    
    private final Dao<AccountGroup> groups;
    
    private final TeamFactory teams;
    
    @Autowired
    private UserFactory (
            Dao<AccountGroup> groups,
            TeamFactory teams) {
        Preconditions.checkNotNull (groups);
        Preconditions.checkNotNull (teams);
        
        this.groups = groups;
        this.teams = teams;
    }
    
    public Student student (Account account) {
        Preconditions.checkNotNull(account);
        try {
            Preconditions.checkArgument (!account.isTeacher());
        } catch (IOException e) {
            throw new RuntimeException (e);
        }
        
        return new Student (account);
    }
    
    public Student teacher (Account account) {
        Preconditions.checkNotNull(account);
        try {
            Preconditions.checkArgument (account.isTeacher());
        } catch (IOException e) {
            throw new RuntimeException (e);
        }
        
        return new Teacher (this.groups, this.teams, account);
    }
    
}
