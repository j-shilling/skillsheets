/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shilling.skillsheets.services.impl;

import com.google.api.client.util.Preconditions;
import com.shilling.skillsheets.dao.Account;
import com.shilling.skillsheets.dao.AccountGroup;
import com.shilling.skillsheets.services.Group;
import com.shilling.skillsheets.services.Serializer;
import java.io.IOException;
import java.util.UUID;

/**
 *
 * @author Jake Shilling <shilling.jake@gmail.com>
 */
abstract class AbstractEditableGroup<T extends AbstractEditableGroup>
        extends AbstractEditableResource<AccountGroup, T> 
        implements Group<T> {
    
    private final Serializer<AccountGroup> serializer;

    protected AbstractEditableGroup (
            Serializer<AccountGroup> serializer,
            AccountGroup group) {
        super (group);
        
        Preconditions.checkNotNull (serializer);
        
        this.serializer = serializer;
    }

    @Override
    public final String serialize() {
        
        return this.serializer.writeValueAsString(this.getResource());
        
    }

    @Override
    public T add(Account account) throws IllegalAccessException {
        Preconditions.checkNotNull (account);
        
        try {
            this.getResource().addMember(account.getUuid());
            account.addKnownResource(this.getUuid());
            account.addGroup (this.getUuid());
        } catch (IOException e) {
            throw new RuntimeException (e);
        }
        
        return (T) this;
    }

    @Override
    public T add(AccountGroup group) throws IllegalAccessException {
        Preconditions.checkNotNull (group);
        
        try {
            this.getResource().addMember(group.getUuid());
            group.addKnownResource(this.getUuid());
        } catch (IOException e) {
            throw new RuntimeException (e);
        }
        
        return (T) this;
    }

    @Override
    public T remove(Account account) throws IllegalAccessException {
        Preconditions.checkNotNull (account);
        
        try {
            this.getResource().delMember(account.getUuid());
            account.delKnownResource(this.getUuid());
            account.delGroup (this.getUuid());
        } catch (IOException e) {
            throw new RuntimeException (e);
        }
        
        return (T) this;
    }

    @Override
    public T remove(AccountGroup group) throws IllegalAccessException {
        Preconditions.checkNotNull (group);
        
        try {
            this.getResource().delMember(group.getUuid());
            group.delKnownResource(this.getUuid());
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
