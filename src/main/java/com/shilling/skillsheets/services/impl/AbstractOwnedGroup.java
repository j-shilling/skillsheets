/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shilling.skillsheets.services.impl;

import com.shilling.skillsheets.dao.AccountGroup;
import com.shilling.skillsheets.services.Group;
import com.shilling.skillsheets.services.Serializer;

/**
 *
 * @author Jake Shilling <shilling.jake@gmail.com>
 */
abstract class AbstractOwnedGroup<T extends AbstractOwnedGroup>
        extends AbstractEditableGroup<T>
        implements Group<T> {
    
    protected AbstractOwnedGroup (
            Serializer<AccountGroup> serializer,
            WrappedResource<AccountGroup> group) {
        
        super (serializer, group);
    }
    
}
