/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shilling.skillsheets.services.impl;

import com.google.api.client.util.Preconditions;
import com.shilling.skillsheets.dao.AccountGroup;
import com.shilling.skillsheets.dao.Dao;
import com.shilling.skillsheets.services.Group;
import com.shilling.skillsheets.services.User;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Jake Shilling <shilling.jake@gmail.com>
 */
public class GroupFactory {
    
    private final Dao<AccountGroup> dao;
    
    @Autowired
    private GroupFactory (
            Dao<AccountGroup> dao) {
        
        Preconditions.checkNotNull (dao);
        
        this.dao = dao;
    }

    
    
    public Optional<Group> group (User user, UUID uuid) {
        Preconditions.checkNotNull (user);
        Preconditions.checkNotNull (uuid);
        
        try {
            Optional<AccountGroup> group = this.dao.read(uuid);
            if (group.isPresent()) {
                Optional.of(new GroupImpl (this, user, group.get()));
            }
            
            return Optional.empty();
        } catch (IOException e) {
            throw new RuntimeException (e);
        }
    }
}
