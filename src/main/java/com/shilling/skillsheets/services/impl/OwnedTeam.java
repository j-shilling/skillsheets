/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shilling.skillsheets.services.impl;

import com.shilling.skillsheets.dao.AccountGroup;
import com.shilling.skillsheets.dao.Dao;
import com.shilling.skillsheets.services.Serializer;
import com.shilling.skillsheets.services.Team;

/**
 *
 * @author Jake Shilling <shilling.jake@gmail.com>
 */
public class OwnedTeam 
        extends AbstractOwnedGroup<OwnedTeam> 
        implements Team<OwnedTeam> {
    
    public OwnedTeam(
            Dao<AccountGroup> dao,
            Serializer<AccountGroup> serializer, 
            AccountGroup group) {
        super(dao, serializer, group);
    }
    
}
