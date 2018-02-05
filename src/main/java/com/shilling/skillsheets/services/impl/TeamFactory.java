/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shilling.skillsheets.services.impl;

import com.google.api.client.util.Preconditions;
import com.shilling.skillsheets.dao.AccountGroup;
import com.shilling.skillsheets.dao.Dao;
import com.shilling.skillsheets.services.Serializer;
import com.shilling.skillsheets.services.Team;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Jake Shilling <shilling.jake@gmail.com>
 */
public class TeamFactory {
    
    private final Dao<AccountGroup> dao;
    private final Serializer<AccountGroup> serializer;
    
    @Autowired
    private TeamFactory (
            Dao<AccountGroup> dao, 
            Serializer<AccountGroup> serializer) {
        Preconditions.checkNotNull (dao);
        Preconditions.checkNotNull (serializer);
        
        this.dao = dao;
        this.serializer = serializer;
    }

    public Team owned (AccountGroup group) {
        Preconditions.checkNotNull (group);
        try {
            Preconditions.checkArgument (group.isTeam());
        } catch (IOException e) {
            throw new RuntimeException (e);
        }
        
        return new OwnedTeam (this.dao, this.serializer, group);
    }
    
    public Team editable (AccountGroup group) {
        Preconditions.checkNotNull (group);
        try {
            Preconditions.checkArgument (group.isTeam());
        } catch (IOException e) {
            throw new RuntimeException (e);
        }
        
        return new EditableTeam (this.serializer, group);
    }
    
    public Team viewable (AccountGroup group) {
        Preconditions.checkNotNull (group);
        try {
            Preconditions.checkArgument (group.isTeam());
        } catch (IOException e) {
            throw new RuntimeException (e);
        }
        
        return new ViewableTeam (this.serializer, group);
    }
}
