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
import com.shilling.skillsheets.dao.Account;
import com.shilling.skillsheets.dao.AccountDao;
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
public class UserFactory {
    
    private final GroupFactory groups;
    private final AccountDao accounts;
    private final LoadingCache<UUID, Account> cache;
    
    @Autowired
    private UserFactory (
            AccountDao accounts,
            GroupFactory groups) {
        
        Preconditions.checkNotNull (accounts);
        Preconditions.checkNotNull (groups);
        
        this.accounts = accounts;
        this.groups = groups;
        this.cache = CacheBuilder.newBuilder()
                .weakValues()
                .build(new CacheLoader<UUID, Account>() {
                    
                    @Override
                    public Account load(UUID k) throws IOException {
                        Optional<Account> account = UserFactory.this.accounts.read(k);
                        if (account.isPresent())
                            return account.get();
                        else
                            throw new NoSuchElementException();
                    }
                    
                });
    }
    
    public User newUserWithEmail (String email) {
        Preconditions.checkNotNull (email);
        Preconditions.checkArgument (!email.isEmpty());
        Preconditions.checkArgument (!this.fromEmail(email).isPresent());
        
        Account account;
        try {
            account = this.accounts.newWithEmail(email);
        } catch (IOException e) {
            throw new RuntimeException (e);
        }
        
        return new UserImpl (this.groups, account);
    }
    
    public User newUserWithId (String id) {
        Preconditions.checkNotNull (id);
        Preconditions.checkArgument (!id.isEmpty());
        Preconditions.checkArgument (!this.fromEmail(id).isPresent());
        
        Account account;
        try {
            account = this.accounts.newWithId(id);
        } catch (IOException e) {
            throw new RuntimeException (e);
        }
        
        return new UserImpl (this.groups, account);
    }
    
    public Optional<User> user (UUID uuid) {
        Preconditions.checkNotNull (uuid);
        
        Account account;
        try {
            account = this.cache.get(uuid);
        } catch (ExecutionException e) {
            if (e.getCause().getClass().equals(NoSuchElementException.class))
                return Optional.empty();
            else
                throw new RuntimeException (e.getCause());
        }
        
        return Optional.of(new UserImpl (this.groups, account));
    }
    
    public Optional<User> fromEmail (String email) {
        Preconditions.checkNotNull (email);
        
        Optional<UUID> uuid;
        try {
            uuid = this.accounts.getByEmail (email);
        } catch (IOException e) {
            throw new RuntimeException (e);
        }
        
        if (uuid.isPresent())
            return this.user(uuid.get());
        else
            return Optional.empty();
    }
    
    public Optional<User> fromId (String id) {
        Preconditions.checkNotNull (id);
        
        Optional<UUID> uuid;
        try {
            uuid = this.accounts.getById (id);
        } catch (IOException e) {
            throw new RuntimeException (e);
        }
        
        if (uuid.isPresent())
            return this.user(uuid.get());
        else
            return Optional.empty();
    }
    
}
