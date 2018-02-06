/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shilling.skillsheets.dao.memory;

import com.shilling.skillsheets.dao.Dao;
import com.shilling.skillsheets.dao.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 *
 * @author Jake Shilling <shilling.jake@gmail.com>
 */
abstract class MemoryDao<T extends Resource> implements Dao<T> {
    
    private Map<UUID, T> vals = new HashMap<>();

    protected final Map<UUID, T> vals() {
        return this.vals;
    }
    
    @Override
    public Optional<T> read(UUID uuid) throws IOException {
        return Optional.ofNullable(this.vals.get(uuid));
    }

    @Override
    public boolean remove(UUID uuid) throws IOException {
        return null != this.vals.remove(uuid);
    }
    
}
