/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shilling.skillsheets.dao.memory;

import com.shilling.skillsheets.dao.AccountGroup;
import java.io.IOException;
import java.util.UUID;

/**
 *
 * @author Jake Shilling <shilling.jake@gmail.com>
 */
public class MemoryAccountGroupDao 
        extends MemoryDao<AccountGroup> {

    @Override
    public AccountGroup create() throws IOException {
        AccountGroup group = new MemoryAccountGroup (UUID.randomUUID());
        this.vals().put(group.getUuid(), group);
        return group;
    }
    
}
