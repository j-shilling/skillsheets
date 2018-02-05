/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shilling.skillsheets.services.impl;

import com.shilling.skillsheets.dao.AccountGroup;
import com.shilling.skillsheets.services.Serializer;
import com.shilling.skillsheets.services.Team;

/**
 *
 * @author Jake Shilling <shilling.jake@gmail.com>
 */
public class ViewableTeam 
        extends AbstractViewableGroup<ViewableTeam> 
        implements Team<ViewableTeam> {
    
    public ViewableTeam(
            Serializer<AccountGroup> serializer, 
            AccountGroup group) {
        super(serializer, group);
    }
    
}
