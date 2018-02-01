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

import com.shilling.skillsheets.api.controllers.exceptions.ForbiddenException;
import com.shilling.skillsheets.api.controllers.exceptions.NotFoundException;
import com.shilling.skillsheets.api.model.ResourceModel;
import com.shilling.skillsheets.dao.Resource;
import com.shilling.skillsheets.dao.ResourceDao;
import com.shilling.skillsheets.dao.ResourceIndex;
import com.shilling.skillsheets.dao.User;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nullable;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.mockito.Mock;
import org.mockito.Mockito;

/**
 * JUnit Runner specialized for Service Layer classes. This
 * runner is written to support additional annotations which
 * cause tests to be run under multiple conditions. If a test
 * takes a User as a parameter, then that test will be run
 * with the user as a teacher and as a student; if it takes a
 * uuid, then it will be run with the uuid producing a resource
 * that is viewable/not-viewable, editable/not-editable, etc.
 * 
 * @author Jake Shilling
 */
public class SkillSheetServiceRunner extends Runner {

    private final Class<?> testClass;
    private final Object testObject;
    private final ResourceIndex index;
    
    /* Create the test object and inject it with a mock
       resource index. */
    public SkillSheetServiceRunner (Class<?> testClass) {
        this.testClass = testClass;
        this.index = Mockito.mock(ResourceIndex.class);
        
        try {
            this.testObject = this.testClass.newInstance();

            Field[] fields = this.testClass.getDeclaredFields();
            for (Field field : fields) {
                if (field.getType().equals(ResourceIndex.class)) {
                    field.setAccessible(true);
                    field.set(this.testObject, this.index);
                } else if (field.getAnnotation(Mock.class) != null){
                    Class<?> mockType = field.getType();
                    field.setAccessible(true);
                    field.set(this.testObject, Mockito.mock(mockType));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException (e);
        }
    }
    
    /* Build a mock Resource with default values */
    private Resource getResource () {
        Resource res = Mockito.mock (Resource.class);
        
        UUID uuid = UUID.randomUUID();
        
        try {
            /* Mock Resource Methods */
            Mockito.when(res.getUuid()).thenReturn (uuid);
            Mockito.when(res.getOwner()).thenReturn (Optional.empty());
            Mockito.when(res.getName()).thenReturn (Optional.empty());
            Mockito.when(res.canEdit(Mockito.any(UUID.class)))
                    .thenReturn (false);
            Mockito.when(res.canView(Mockito.any(UUID.class)))
                    .thenReturn (false);
            Mockito.when(res.getModel(Mockito.any(UUID.class)))
                    .thenReturn (Mockito.mock(ResourceModel.class));
            
        } catch (IOException e) {
            throw new RuntimeException (e);
        }
        
        return res;
    }
    
    /* Build a mock User with default values */
    private User getUser() {
        User user = Mockito.mock (User.class);
        UUID uuid = UUID.randomUUID();
        
        try {
            /* Mock Resource Methods */
            Mockito.when(user.getUuid()).thenReturn (uuid);
            Mockito.when(user.getOwner()).thenReturn (Optional.empty());
            Mockito.when(user.getName()).thenReturn (Optional.empty());
            Mockito.when(user.canEdit(Mockito.any(UUID.class)))
                    .thenReturn (false);
            Mockito.when(user.canView(Mockito.any(UUID.class)))
                    .thenReturn (false);
            Mockito.when(user.getModel(Mockito.any(UUID.class)))
                    .thenReturn (Mockito.mock(ResourceModel.class));
        
            /* Mock User methods */
            Mockito.when(user.isTeacher())
                    .thenReturn (false);
            Mockito.when(user.setId(Mockito.anyString()))
                    .thenReturn (user);
            Mockito.when(user.getId())
                    .thenReturn (Optional.empty());
            Mockito.when(user.getEmail())
                    .thenReturn (Optional.empty());
            Mockito.when(user.setEmail(Mockito.anyString()))
                    .thenReturn (user);
            Mockito.when(user.addSkillSheet(Mockito.any(UUID.class)))
                    .thenReturn(user);
            Mockito.when(user.delSkillSheet(Mockito.any(UUID.class)))
                    .thenReturn(user);
            Mockito.when(user.getSkillSheets())
                    .thenReturn(Collections.emptyList());
            Mockito.when(user.addUserGroup(Mockito.any(UUID.class)))
                    .thenReturn(user);
            Mockito.when(user.delUserGroup(Mockito.any(UUID.class)))
                    .thenReturn(user);
            Mockito.when(user.getUserGroups())
                    .thenReturn(Collections.emptyList());
            
        } catch (IOException e) {
            throw new RuntimeException (e);
        }
        
        return user;
    }
    
    /* Run a test with the given parameters */
    private void runTest (
            Method method, 
            RunNotifier notifier, 
            String desc,
            Object[] args) {
        
        Description testDescription = Description.
                            createTestDescription(this.testClass, 
                                    method.getName() + ": " + desc);
        notifier.fireTestStarted(testDescription);
        
        try {
            method.invoke(this.testObject, args);
        } catch (Exception e) {
            Failure fail = new Failure (testDescription, 
              e instanceof InvocationTargetException ? e.getCause() : e);
            notifier.fireTestFailure(fail);
        }
        
        notifier.fireTestFinished(testDescription);
    }
    
    /* Run a test and expect it to throw the exception type expected */
    private void runTest (
            Method method, 
            RunNotifier notifier, 
            String desc,
            Object[] args,
            Class<?> expected) {
        
        Description testDescription = Description.
                            createTestDescription(this.testClass, 
                                    method.getName() + ": " + desc);
        notifier.fireTestStarted(testDescription);
        
        boolean caught_expected = false;
        try {
            method.invoke(this.testObject, args);
        } catch (InvocationTargetException e) {
            Class<?> got = e.getCause().getClass();
            if (!got.equals(expected)) {
                Exception except = 
                        new RuntimeException ("Wrong kind of exception thrown."
                        + " Expected: " + expected + " Actual: " + got, e);
                Failure fail = new Failure (testDescription, except);
                notifier.fireTestFailure(fail);
            } else {
                caught_expected = true;
            }
        } catch (Exception e) {
            throw new RuntimeException (e);
        }
        
        if (!caught_expected) {
            Exception e = new RuntimeException ("Expected exception "
                    + expected + " not thrown");
            Failure fail = new Failure (testDescription, e);
                notifier.fireTestFailure(fail);
        }
        
        notifier.fireTestFinished(testDescription);
    }
    
    /* Get parameters which should lead to a successful run */
    private Object[] getSuccess (Method method) {
        try {
            Parameter[] params = method.getParameters();
            Object[] ret = new Object[params.length];

            for (int i = 0; i < params.length; i ++) {
                Parameter param = params[i];

                if (param.getType().equals(User.class)) {
                    
                    User user = this.getUser();
                    if (param.getAnnotation(Teacher.class) != null) {
                        Mockito.when(user.isTeacher()).thenReturn(true);
                    } else {
                        Mockito.when(user.isTeacher()).thenReturn(false);
                    }
                    ret[i] = user;
                    
                } else if (param.getType().equals(UUID.class)) {
                    
                    UUID uuid = UUID.randomUUID();
                    ResourceDao<Resource> dao = Mockito.mock(ResourceDao.class);
                    Resource resource = this.getResource();
                       
                    Mockito.when(this.index.get(uuid)).thenReturn(Optional.of(dao));
                    Mockito.when(dao.read(uuid)).thenReturn(Optional.of(resource));
                    
                    if (param.getAnnotation(CanEdit.class) != null) {
                        Mockito.when(resource.canEdit(Mockito.any(UUID.class)))
                                .thenReturn(true);
                        Mockito.when(resource.canView(Mockito.any(UUID.class)))
                                .thenReturn(true);
                        Mockito.when(resource.canEdit(Mockito.any(User.class)))
                                .thenReturn(true);
                        Mockito.when(resource.canView(Mockito.any(User.class)))
                                .thenReturn(true);
                    } else if (param.getAnnotation(CanView.class) != null) {
                        Mockito.when(resource.canEdit(Mockito.any(UUID.class)))
                                .thenReturn(false);
                        Mockito.when(resource.canView(Mockito.any(UUID.class)))
                                .thenReturn(true);
                        Mockito.when(resource.canEdit(Mockito.any(User.class)))
                                .thenReturn(false);
                        Mockito.when(resource.canView(Mockito.any(User.class)))
                                .thenReturn(true);
                    } else {
                        Mockito.when(resource.canEdit(Mockito.any(UUID.class)))
                                .thenReturn(false);
                        Mockito.when(resource.canView(Mockito.any(UUID.class)))
                                .thenReturn(false);
                        Mockito.when(resource.canEdit(Mockito.any(User.class)))
                                .thenReturn(false);
                        Mockito.when(resource.canView(Mockito.any(User.class)))
                                .thenReturn(false);
                    }
                    
                    ret[i] = uuid;
                    
                } else if (param.getType().equals(String.class)) {
                    
                    ret[i] = new String("test");
                    
                } else {
                    
                    ret[i] = Mockito.mock(param.getType());
                    
                }
            }

            return ret;
        } catch (Exception e) {
            throw new RuntimeException (e);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Description getDescription() {
        return Description
                .createTestDescription(this.testClass, 
                        "Test SkillSheet Service Layer");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run(RunNotifier notifier) {
        try {
            
            for (Method method : this.testClass.getMethods()) {
                if (method.getAnnotation(Test.class) != null) {
                    
                    Parameter[] params = method.getParameters();
                    if (params.length == 0) {
                        /* just a normal junit test */
                        this.runTest(method, notifier, "", null);
                        continue;
                    }
                    
                    /* Scan for special parameters */
                    for (int i = 0; i < params.length; i++) {
                        Parameter param = params[i];
                        String name = param.getName();
                        
                        if (param.getType().equals(User.class)) {
                            User user = this.getUser();
                            Object[] args = this.getSuccess(method);
                            args[i] = user;
                            if (param.getAnnotation(Teacher.class) != null) {
                                this.runTest(
                                        method, 
                                        notifier, 
                                        "with " + name + " as a student",
                                        args, 
                                        ForbiddenException.class);
                            } else {
                                this.runTest(
                                        method,
                                        notifier,
                                        "with " + name + " as a student",
                                        args);
                            }
                            
                            user = this.getUser();
                            Mockito.when(user.isTeacher()).thenReturn(true);
                            args = this.getSuccess(method);
                            args[i] = user;
                            this.runTest(
                                    method, 
                                    notifier, 
                                    "with " + name + " as a teacher",
                                    args);
                        } else if (param.getType().equals(UUID.class)) {
                            UUID uuid = UUID.randomUUID();
                            Mockito.when(this.index.get(uuid)).thenReturn(Optional.empty());
                            Object[] args = this.getSuccess(method);
                            args[i] = uuid;
                            
                            this.runTest (
                                    method, 
                                    notifier, 
                                    "with " + name + " not found",
                                    args, 
                                    NotFoundException.class);
                            
                            Resource res = this.getResource();
                            ResourceDao<Resource> dao = Mockito.mock(ResourceDao.class);
                            Mockito.when(this.index.get(uuid)).thenReturn(Optional.of(dao));
                            Mockito.when(dao.read(uuid)).thenReturn(Optional.of(res));
                            
                            if (param.getAnnotation(CanView.class) == null
                                    && param.getAnnotation(CanEdit.class) == null
                                    && param.getAnnotation(Owner.class) == null) {
                           
                                this.runTest (
                                        method, 
                                        notifier, 
                                        "with " + name + " not accessable",
                                        args);
                            } else {
                                this.runTest (
                                        method, 
                                        notifier,
                                        "with " + name + " not accessable",
                                        args, 
                                        ForbiddenException.class);
                            }
                            
                            res = this.getResource();
                            Mockito.when(dao.read(uuid)).thenReturn(Optional.of(res));
                            Mockito.when(res.canView(Mockito.any(UUID.class))).thenReturn (true);
                            Mockito.when(res.canView(Mockito.any(User.class))).thenReturn (true);
                            
                            if (param.getAnnotation(CanEdit.class) == null
                                    && param.getAnnotation(Owner.class) == null) {
                           
                                this.runTest (
                                        method, 
                                        notifier,
                                        "with " + name + " viewable",
                                        args);
                            } else {
                                this.runTest (
                                        method, 
                                        notifier, 
                                        "with " + name + " viewable",
                                        args,
                                        ForbiddenException.class);
                            }
                            
                            res = this.getResource();
                            Mockito.when(dao.read(uuid)).thenReturn(Optional.of(res));
                            Mockito.when(res.canView(Mockito.any(UUID.class))).thenReturn (true);
                            Mockito.when(res.canView(Mockito.any(User.class))).thenReturn (true);
                            Mockito.when(res.canView(Mockito.any(UUID.class))).thenReturn (true);
                            Mockito.when(res.canView(Mockito.any(User.class))).thenReturn (true);
                            
                            if (param.getAnnotation(Owner.class) == null) {
                           
                                this.runTest (method, 
                                        notifier, 
                                        "with " + name + " editable",
                                        args);
                            } else {
                                this.runTest (
                                        method, 
                                        notifier, 
                                        "with " + name + " editable",
                                        args, 
                                        ForbiddenException.class);
                            }
                        }
                        
                        if (param.getAnnotation(Nullable.class) != null) {
                            
                            Object[] args = this.getSuccess(method);
                            args[i] = null;
                            this.runTest (
                                    method,
                                    notifier,
                                    "with " + name + " null",
                                    args);
                            
                        }
                    }
                }
            }
            
        } catch (Exception e) {
            throw new RuntimeException (e);
        }
    }
    
}
