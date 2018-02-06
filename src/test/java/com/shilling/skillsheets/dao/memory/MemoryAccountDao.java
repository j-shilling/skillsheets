/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shilling.skillsheets.dao.memory;

import com.shilling.skillsheets.dao.Account;
import com.shilling.skillsheets.dao.AccountDao;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

/**
 *
 * @author Jake Shilling <shilling.jake@gmail.com>
 */
public class MemoryAccountDao 
        extends MemoryDao<Account> 
        implements AccountDao {

    @Override
    public Account create() throws IOException {
        Account account = new MemoryAccount(UUID.randomUUID());
        this.vals().put(account.getUuid(), account);
        return account;
    }

    @Override
    public Account newWithId(String id) throws IOException {
        return this.create().setId(id);
    }

    @Override
    public Account newWithEmail(String email) throws IOException {
        return this.create().setEmail(email);
    }

    @Override
    public Optional<Account> getById(String id) throws IOException {
        for (Account account : this.vals().values()) {
            if (account.getId().equals(Optional.ofNullable(id)))
                return Optional.of(account);
        }
        
        return Optional.empty();
    }

    @Override
    public Optional<Account> getByEmail(String email) throws IOException {
         for (Account account : this.vals().values()) {
            if (account.getEmail().equals(Optional.ofNullable(email)))
                return Optional.of(account);
        }
        
        return Optional.empty();
    }
    
}
