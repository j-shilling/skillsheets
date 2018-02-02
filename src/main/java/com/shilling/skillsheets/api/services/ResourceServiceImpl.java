/*
 * Copyright (C) 2018 Jake Shilling
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
package com.shilling.skillsheets.api.services;

import com.google.common.base.Preconditions;
import com.shilling.skillsheets.api.controllers.exceptions.ForbiddenException;
import com.shilling.skillsheets.api.controllers.exceptions.InternalErrorException;
import com.shilling.skillsheets.api.controllers.exceptions.NotFoundException;
import com.shilling.skillsheets.api.model.ResourceModel;
import com.shilling.skillsheets.dao.Resource;
import com.shilling.skillsheets.dao.ResourceDao;
import com.shilling.skillsheets.dao.ResourceIndex;
import com.shilling.skillsheets.dao.User;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Jake Shilling
 */
public class ResourceServiceImpl implements ResourceService {
    
    private final ResourceIndex index;
    
    @Autowired
     ResourceServiceImpl (ResourceIndex index) {
        this.index = index;
    }
    
    private Resource get (UUID uuid) {
        Optional<ResourceDao<?>> result = this.index.get(uuid);
        if (result.isPresent()) {
            try {
                Optional<?> res = result.get().read(uuid);
                
                if (res.isPresent())
                    return (Resource) res.get();
            } catch (IOException e) {
                throw new InternalErrorException (e);
            }
        }
        
        throw new NotFoundException();
    }

    @Override
    public Resource copy(User requester, UUID uuid) {
        Preconditions.checkNotNull (requester);
        Preconditions.checkNotNull (uuid);
        
        try {
            if (!requester.isTeacher())
                throw new ForbiddenException();
            
            Resource res = this.get(uuid);
            
            if (!res.canView(requester)) {
                throw new ForbiddenException ();
            }
            
            ResourceDao<? extends Resource> dao = this.index.get(uuid).get();
            Resource copy = dao.create();
            
            res.copy(copy);
            copy.clearEditors();
            copy.clearViewers();
            copy.setOwner(requester);
            
            return copy;
        } catch (IOException e) {
            throw new InternalErrorException (e);
        }
    }

    @Override
    public Resource sendTo(User requester, UUID uuid, User user) {
        Preconditions.checkNotNull (requester);
        Preconditions.checkNotNull (uuid);
        Preconditions.checkNotNull (user);
        
        try {
            if (!user.isTeacher())
                throw new ForbiddenException();
        
            Resource res = this.copy (requester, uuid);
            res.setOwner(user);
            return res;
        } catch (IOException e) {
            throw new InternalErrorException (e);
        }
    }

    @Override
    public ResourceModel read(User requester, UUID uuid) {
        Preconditions.checkNotNull (requester);
        Preconditions.checkNotNull (uuid);
        
        Resource res = this.get (uuid);
        
        try {
            if (!res.canView (requester)) {
                throw new ForbiddenException();
            }
            
            return res.getModel(requester);
        } catch (IOException e) {
            throw new InternalErrorException (e);
        }
    }

    @Override
    public void setOwner(User requester, UUID uuid, User user) {
        Preconditions.checkNotNull (requester);
        Preconditions.checkNotNull (uuid);
        
        Resource res = this.get (uuid);
        
        try {
            if (!requester.isTeacher()
                    || !res.isOwner(requester)
                    || ((user != null) && !user.isTeacher())) {
                throw new ForbiddenException();
            }
            
            res.setOwner(user);
        } catch (IOException e) {
            throw new InternalErrorException (e);
        }
    }

    @Override
    public void setName(User requester, UUID uuid, String name) {
        Preconditions.checkNotNull (requester);
        Preconditions.checkNotNull (uuid);
        
        Resource res = this.get (uuid);
        
        try {
            if (!requester.isTeacher() || !res.canEdit (requester)) {
                throw new ForbiddenException();
            }
            
            res.setName (name);
        } catch (IOException e) {
            throw new InternalErrorException (e);
        }
    }

    @Override
    public void addEditor(User requester, UUID uuid, User user) {
        Preconditions.checkNotNull (requester);
        Preconditions.checkNotNull (uuid);
        Preconditions.checkNotNull (user);
        
        Resource res = this.get (uuid);
        
        try {
            if (!requester.isTeacher()
                    || !user.isTeacher()
                    || !res.canEdit (requester)) {
                throw new ForbiddenException();
            }
            
            res.addEditor(user);
        } catch (IOException e) {
            throw new InternalErrorException (e);
        }
    }

    @Override
    public void delEditor(User requester, UUID uuid, User user) {
        Preconditions.checkNotNull (requester);
        Preconditions.checkNotNull (uuid);
        Preconditions.checkNotNull (user);
        
        Resource res = this.get (uuid);
        
        try {
            if (!requester.isTeacher()
                    || !user.isTeacher()
                    || !res.canEdit (requester)) {
                throw new ForbiddenException();
            }
            
            res.delEditor (user);
        } catch (IOException e) {
            throw new InternalErrorException (e);
        }
    }

    @Override
    public void addViewer(User requester, UUID uuid, User user) {
        Preconditions.checkNotNull (requester);
        Preconditions.checkNotNull (uuid);
        Preconditions.checkNotNull (user);
        
        Resource res = this.get (uuid);
        
        try {
            if (!requester.isTeacher()
                    || !user.isTeacher()
                    || !res.canEdit (requester)) {
                throw new ForbiddenException();
            }
            
            res.addViewer (user);
        } catch (IOException e) {
            throw new InternalErrorException (e);
        }
    }

    @Override
    public void delViewer(User requester, UUID uuid, User user) {
        Preconditions.checkNotNull (requester);
        Preconditions.checkNotNull (uuid);
        Preconditions.checkNotNull (user);
        
        Resource res = this.get (uuid);
        
        try {
            if (!requester.isTeacher()
                    || !user.isTeacher()
                    || !res.canEdit (requester)) {
                throw new ForbiddenException();
            }
            
            res.delViewer (user);
        } catch (IOException e) {
            throw new InternalErrorException (e);
        }
    }

    @Override
    public void delete(User requester, UUID uuid) {
        Preconditions.checkNotNull (requester);
        Preconditions.checkNotNull (uuid);
        
        Resource res = this.get(uuid);
        
        try {
            if (res.isOwner(requester)) {
                
                res.delete();
                ResourceDao<? extends Resource> dao = this.index.get (uuid).get();
                dao.delete(uuid);
            
            }
        } catch (IOException e) {
            throw new InternalErrorException (e);
        }
    }
    
}
