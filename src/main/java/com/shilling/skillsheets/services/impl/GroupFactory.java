/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shilling.skillsheets.services.impl;

import com.google.api.client.util.Preconditions;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.shilling.skillsheets.dao.AccountGroup;
import com.shilling.skillsheets.dao.Dao;
import com.shilling.skillsheets.services.Group;
import com.shilling.skillsheets.services.User;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Jake Shilling <shilling.jake@gmail.com>
 */
class GroupFactory {
    
    private final Dao<AccountGroup> dao;
    private final LoadingCache<UUID, AccountGroup> cache;
    
    @Autowired
    private GroupFactory (
            Dao<AccountGroup> dao) {
        
        Preconditions.checkNotNull (dao);
        
        this.dao = dao;
        this.cache = CacheBuilder.newBuilder()
                .weakValues()
                .build(new CacheLoader<UUID, AccountGroup> () {
                    
                    @Override
                    public AccountGroup load(UUID k) throws NoSuchElementException, IOException {
                        Optional<AccountGroup> group = GroupFactory.this.dao.read(k);
                        if (group.isPresent())
                            return group.get();
                        else
                            throw new NoSuchElementException ();
                    }
                    
                });
    }
    
    public Group newGroup (User user, boolean team) {
        Preconditions.checkNotNull (user);
        
        AccountGroup group;
        try {
            group = this.dao.create()
                    .setTeam(team)
                    .setOwner(user.getUuid());
        } catch (IOException e) {
            throw new RuntimeException (e);
        }
        
        this.cache.put(group.getUuid(), group);
        user.addKnownResource(group.getUuid());
        
        return new GroupImpl (this, user, group);
    }
    
    public Group newTeam (User user) {
        return this.newGroup(user, true);
    }
    
    public Group newRoster (User user) {
        return this.newGroup(user, false);
    }
    
    public Optional<Group> group (User user, UUID uuid) {
        Preconditions.checkNotNull (user);
        Preconditions.checkNotNull (uuid);
        
        try {
            AccountGroup group = this.cache.get(uuid);
            
            return Optional.of(new GroupImpl (this, user, group));
        } catch (ExecutionException e) {
            if (e.getCause().getClass().equals(NoSuchElementException.class))
                return Optional.empty();
            else
                throw new RuntimeException (e.getCause());
        }
    }
}
