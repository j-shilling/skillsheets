/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shilling.skillsheets.dao.memory;

import com.shilling.skillsheets.dao.Account;
import com.shilling.skillsheets.dao.AccountDao;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 *
 * @author Jake Shilling <shilling.jake@gmail.com>
 */
public class MemoryAccountDao 
        extends MemoryDao<Account> 
        implements AccountDao {
    
    private final Map<String, UUID> keys = new HashMap<>();
    
    public Map<String, UUID> getKeys() {
        return this.keys;
    }

    @Override
    public Account create() throws IOException {
        Account account = new MemoryAccount(this, UUID.randomUUID());
        this.vals().put(account.getUuid(), account);
        return account;
    }

    @Override
    public Account newWithId(String id) throws IOException {
        Account account = this.create().setId(id);
        this.getKeys().put(id, account.getUuid());
        return account;
    }

    @Override
    public Account newWithEmail(String email) throws IOException {
        Account account = this.create().setId(email);
        this.getKeys().put(email, account.getUuid());
        return account;
    }

    @Override
    public Optional<UUID> getById(String id) throws IOException {
        return Optional.ofNullable(this.getKeys().get(id));
    }

    @Override
    public Optional<UUID> getByEmail(String email) throws IOException {
        return Optional.ofNullable(this.getKeys().get(email));
    }
    
}
