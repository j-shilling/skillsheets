/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shilling.skillsheets.services.impl;

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
        
        super(serializer, new WrappedEditableResource (group));
        
    }

    @Override
    protected void checkArgument(Account account) throws IllegalAccessException {
        try {
            if (!account.isTeacher())
                throw new IllegalAccessException ("Cannot add a student to a teacher team");
        } catch (IOException e) {
            throw new RuntimeException (e);
        }
    }

    @Override
    protected void checkArgument(AccountGroup group) throws IllegalAccessException {
        try {
            if (!group.isTeam())
                throw new IllegalAccessException ("Cannot add a student roster to a teacher team");
        } catch (IOException e) {
            throw new RuntimeException (e);
        }
    }
    
}
