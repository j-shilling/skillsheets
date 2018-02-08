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

/**
 *
 * @author Jake Shilling <shilling.jake@gmail.com>
 */
abstract class AbstractEditableGroup<T extends AbstractEditableGroup>
        extends AbstractViewableGroup<T>
        implements Group<T> {

    protected AbstractEditableGroup (
            Serializer<AccountGroup> serializer,
            WrappedResource<AccountGroup> group) {
        
        super (serializer, group);
    }
    
    abstract protected void checkArgument (Account account) throws IllegalAccessException;
    abstract protected void checkArgument (AccountGroup group) throws IllegalAccessException;

    @Override
    public T add(Account account) throws IllegalAccessException {
        Preconditions.checkNotNull (account);
        this.checkArgument (account);
        
        this.writeLock().lock();
        try {
            this.getResource().addMember(account.getUuid());
            account.addGroup (this.getUuid());
        } catch (IOException e) {
            throw new RuntimeException (e);
        } finally {
            this.writeLock().unlock();
        }
        
        return (T) this;
    }
    
    @Override
    public T add(AccountGroup group) throws IllegalAccessException {
        Preconditions.checkNotNull (group);
        this.checkArgument(group);
        
        this.writeLock().lock();
        try {
            this.getResource().addChild(group.getUuid());
            group.addParent (this.getUuid());
        } catch (IOException e) {
            throw new RuntimeException (e);
        } finally {
            this.writeLock().lock();
        }
        
        return (T) this;
    }

    @Override
    public T remove(Account account) throws IllegalAccessException {
        Preconditions.checkNotNull (account);
        
        this.writeLock().lock();
        try {
            this.getResource().delMember(account.getUuid());
            account.delGroup (this.getUuid());
        } catch (IOException e) {
            throw new RuntimeException (e);
        } finally {
            this.writeLock().unlock();
        }
        
        return (T) this;
    }
    
    @Override
    public T remove(AccountGroup group) throws IllegalAccessException {
        Preconditions.checkNotNull (group);
        
        this.writeLock().lock();
        try {
            this.getResource().delChild(group.getUuid());
            group.delParent (this.getUuid());
        } catch (IOException e) {
            throw new RuntimeException (e);
        } finally {
            this.writeLock().unlock();
        }
        
        return (T) this;
    }
    
}
