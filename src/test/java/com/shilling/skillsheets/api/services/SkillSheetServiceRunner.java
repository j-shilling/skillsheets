package com.shilling.skillsheets.api.services;

import com.shilling.skillsheets.dao.Resource;
import com.shilling.skillsheets.dao.ResourceDao;
import com.shilling.skillsheets.dao.ResourceIndex;
import com.shilling.skillsheets.dao.User;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Optional;
import java.util.UUID;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.mockito.Mock;
import org.mockito.Mockito;

/**
 *
 * @author Jake Shilling
 */
public class SkillSheetServiceRunner extends Runner {

    private final Class<?> testClass;
    private final Object testObject;
    private final ResourceIndex index;
    
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
    
    private void runTest (
            Method method, 
            RunNotifier notifier, 
            Object[] args, 
            Exception expect) {
        
        Description testDescription = Description.
                            createTestDescription(this.testClass, method.getName());
        notifier.fireTestStarted(testDescription);
        
        try {
            method.invoke(this.testObject, args);
        } catch (Exception e) {
            if (expect == null || !e.getClass().equals(expect.getClass())) {
                Failure fail = new Failure (testDescription, e);
                notifier.fireTestFailure(fail);
            }
        }
        
        notifier.fireTestFinished(testDescription);
    }
    
    private Object[] getSuccess (Method method) {
        try {
            Parameter[] params = method.getParameters();
            Object[] ret = new Object[params.length];

            for (int i = 0; i < params.length; i ++) {
                Parameter param = params[i];

                if (param.getType().equals(User.class)) {
                    User user = Mockito.mock(User.class);
                    if (param.getAnnotation(Teacher.class) != null) {
                        Mockito.when(user.isTeacher()).thenReturn(true);
                    } else {
                        Mockito.when(user.isTeacher()).thenReturn(false);
                    }
                    ret[i] = user;
                }
                
                if (param.getType().equals(UUID.class)) {
                    UUID uuid = UUID.randomUUID();
                    ResourceDao<Resource> dao = Mockito.mock(ResourceDao.class);
                    Resource resource = Mockito.mock(Resource.class);
                       
                    Mockito.when(this.index.get(uuid)).thenReturn(Optional.of(dao));
                    Mockito.when(dao.read(uuid)).thenReturn(Optional.of(resource));
                    
                    if (param.getAnnotation(CanEdit.class) != null) {
                        Mockito.when(resource.canEdit(Mockito.any(UUID.class))).thenReturn(true);
                        Mockito.when(resource.canView(Mockito.any(UUID.class))).thenReturn(true);
                    } else if (param.getAnnotation(CanView.class) != null) {
                        Mockito.when(resource.canEdit(Mockito.any(UUID.class))).thenReturn(false);
                        Mockito.when(resource.canView(Mockito.any(UUID.class))).thenReturn(true);
                    } else {
                        Mockito.when(resource.canEdit(Mockito.any(UUID.class))).thenReturn(false);
                        Mockito.when(resource.canView(Mockito.any(UUID.class))).thenReturn(false);
                    }
                    
                    ret[i] = uuid;
                }
            }

            return ret;
        } catch (Exception e) {
            throw new RuntimeException (e);
        }
    }
    
    @Override
    public Description getDescription() {
        return Description
                .createTestDescription(this.testClass, 
                        "Test SkillSheet Service Layer");
    }

    @Override
    public void run(RunNotifier notifier) {
        try {
            
            for (Method method : this.testClass.getMethods()) {
                if (method.getAnnotation(Test.class) != null) {
                    Object[] args = this.getSuccess(method);
                    this.runTest(method, notifier, args, null);
                }
            }
            
        } catch (Exception e) {
            throw new RuntimeException (e);
        }
    }
    
}
