/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shilling.skillsheets.services.impl;

import com.shilling.skillsheets.AbstractHasUuid;
import com.shilling.skillsheets.dao.Account;
import com.shilling.skillsheets.dao.AccountGroup;
import com.shilling.skillsheets.dao.Resource;
import com.shilling.skillsheets.services.Service;
import java.util.Optional;
import java.util.concurrent.locks.Lock;

/**
 *
 * @author Jake Shilling <shilling.jake@gmail.com>
 */
abstract class AbstractService<T extends Resource, R extends AbstractService>
        extends AbstractHasUuid
        implements Service<R> {
    
    private final WrappedResource<T> wrap;
    
    protected AbstractService (WrappedResource<T> wrap) {
        super (wrap.getUuid());
        this.wrap = wrap;
    }
    
    protected Lock readLock() {
        return this.wrap.readLock();
    }
    
    protected Lock writeLock() {
        return this.wrap.writeLock();
    }
    
    protected T getResource() {
        return this.wrap.getResource();
    }

    @Override
    public Optional<String> getDisplayName() {
        return this.wrap.getDisplayName();
    }

    @Override
    public R setDisplayName(String displayName) throws IllegalAccessException {
        this.wrap.setDisplayName(displayName);
        return (R) this;
    }

    @Override
    public R giveTo(Account account) throws IllegalAccessException {
        this.wrap.giveTo(account);
        return (R) this;
    }

    @Override
    public R letEdit(Account account) throws IllegalAccessException {
        this.wrap.letEdit(account);
        return (R) this;
    }

    @Override
    public R letEdit(AccountGroup group) throws IllegalAccessException {
        this.wrap.letEdit(group);
        return (R) this;
    }

    @Override
    public R letView(Account account) throws IllegalAccessException {
        this.wrap.letView(account);
        return (R) this;
    }

    @Override
    public R letView(AccountGroup group) throws IllegalAccessException {
        this.wrap.letView(group);
        return (R) this;
    }

    @Override
    public void delete() throws IllegalAccessException {
        this.wrap.delete();
    }
    
}
