/*
 * Copyright (C) 2018 Pivotal Software, Inc.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package com.shilling.skillsheets.services.impl;

import com.shilling.skillsheets.services.SeriveLayerTest;
import com.shilling.skillsheets.services.User;
import com.shilling.skillsheets.services.Group;
import java.util.Optional;
import java.util.UUID;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 *
 * @author jake
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes={SeriveLayerTest.class})
public class TeamTest {
    
    @Autowired
    private UserFactory users;
    @Autowired
    private GroupFactory groups;

    private User teacher() {
        return this.users.newUserWithId(UUID.randomUUID().toString())
                .setTeacher(true);
    }
    
    private User student() {
        return this.users.newUserWithId(UUID.randomUUID().toString())
                .setTeacher(false);
    }
    
    @Test
    public void newTeamTest () throws Exception {
        User user = this.teacher();
        Group team = this.groups.newTeam(user);
        
        Optional<Group> read = this.groups.group(user, team.getUuid());
        assertTrue (read.isPresent());
        assertTrue (team.isTeam());
        
        assertEquals (team, user.team(team.getUuid()));
        assertTrue (user.team(team.getUuid()).isOwned());
    }
}
