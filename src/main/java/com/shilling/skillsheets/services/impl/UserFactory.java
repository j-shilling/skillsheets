/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shilling.skillsheets.services.impl;

import com.google.api.client.util.Preconditions;
import com.shilling.skillsheets.dao.Account;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Jake Shilling <shilling.jake@gmail.com>
 */
public class UserFactory {
    
    private final GroupFactory groups;
    
    @Autowired
    private UserFactory (
            GroupFactory groups) {
        Preconditions.checkNotNull (groups);
        
        this.groups = groups;
    }
    
    public UserImpl user (Account account) {
        Preconditions.checkNotNull(account);
        try {
            Preconditions.checkArgument (!account.isTeacher());
        } catch (IOException e) {
            throw new RuntimeException (e);
        }
        
        return new UserImpl (this.groups, account);
    }
    
}
