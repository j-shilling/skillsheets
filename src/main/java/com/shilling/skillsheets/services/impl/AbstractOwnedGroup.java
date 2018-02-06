/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shilling.skillsheets.services.impl;

import com.google.api.client.util.Preconditions;
import com.shilling.skillsheets.dao.AccountGroup;
import com.shilling.skillsheets.dao.Dao;
import com.shilling.skillsheets.dao.GroupMember;
import com.shilling.skillsheets.services.Group;
import com.shilling.skillsheets.services.Serializer;
import java.io.IOException;
import java.util.UUID;

/**
 *
 * @author Jake Shilling <shilling.jake@gmail.com>
 */
abstract class AbstractOwnedGroup<T extends AbstractOwnedGroup>
        extends AbstractOwnedResource<AccountGroup, T> 
        implements Group<T> {
    
    private final Serializer<AccountGroup> serializer;

    protected AbstractOwnedGroup (
            Dao<AccountGroup> dao,
            Serializer<AccountGroup> serializer,
            AccountGroup group) {
        super (dao, group);
        
        Preconditions.checkNotNull (serializer);
        
        this.serializer = serializer;
    }

    @Override
    public final String serialize() {
        
        return this.serializer.writeValueAsString(this.getResource());
        
    }

    @Override
    public T add(GroupMember account) throws IllegalAccessException {
        Preconditions.checkNotNull (account);
        
        try {
            this.getResource().addMember(account.getUuid());
            account.addGroup (this.getUuid());
        } catch (IOException e) {
            throw new RuntimeException (e);
        }
        
        return (T) this;
    }

    @Override
    public T remove(GroupMember account) throws IllegalAccessException {
        Preconditions.checkNotNull (account);
        
        try {
            this.getResource().delMember(account.getUuid());
            account.delGroup (this.getUuid());
        } catch (IOException e) {
            throw new RuntimeException (e);
        }
        
        return (T) this;
    }

    @Override
    public final boolean contains(UUID uuid) {
        try {
            return this.getResource().contains(uuid);
        } catch (IOException e) {
            throw new RuntimeException (e);
        }
    }
    
}
