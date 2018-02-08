/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shilling.skillsheets.services.impl;

import com.google.api.client.util.Preconditions;
import com.shilling.skillsheets.dao.Account;
import com.shilling.skillsheets.dao.AccountGroup;
import com.shilling.skillsheets.services.Serializer;
import com.shilling.skillsheets.services.Team;
import java.io.IOException;

/**
 *
 * @author Jake Shilling <shilling.jake@gmail.com>
 */
public class EditableTeam 
        extends AbstractEditableGroup<EditableTeam> 
        implements Team<EditableTeam> {
    
    public EditableTeam(
            Serializer<AccountGroup> serializer, 
            AccountGroup group) {
        super(serializer, group);
    }
    
    @Override
    public EditableTeam add(Account account) throws IllegalAccessException {
        Preconditions.checkNotNull (account);
        
        try {
            if (!account.isTeacher())
                throw new IllegalAccessException ("A student account cannot be added to a team of teachers.");
        } catch (IOException e) {
            throw new RuntimeException (e);
        }
        
        return super.add(account);
    }
    
    @Override
    public EditableTeam add(AccountGroup group) throws IllegalAccessException {
        Preconditions.checkNotNull (group);
        
        try {
            if (!group.isTeam())
                throw new IllegalAccessException 
                    ("A roster of student accounts cannot be added to a team of teachers.");
        } catch (IOException e) {
            throw new RuntimeException (e);
        }
        
        return super.add(group);
    }
    
}
