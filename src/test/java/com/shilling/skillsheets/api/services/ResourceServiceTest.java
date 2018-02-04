package com.shilling.skillsheets.api.services;

import com.shilling.skillsheets.services.ResourceService;
import com.shilling.skillsheets.services.ResourceServiceImpl;
import com.shilling.skillsheets.dao.Resource;
import com.shilling.skillsheets.dao.ResourceDao;
import com.shilling.skillsheets.dao.ResourceIndex;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nullable;
import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import com.shilling.skillsheets.dao.Account;

@RunWith(SkillSheetServiceRunner.class)
public class ResourceServiceTest {
    
    private ResourceIndex index;
    
    private ResourceService service() {
        return new ResourceServiceImpl (this.index);
    }

    @Test
    public void testCopy(
            @Teacher Account requester,
            @CanView UUID uuid) throws Exception {
        
        ResourceService service = this.service();
        Resource target = null;
        Resource mock = Mockito.mock(Resource.class);
                
        Optional<ResourceDao<?>> dao = this.index.get(uuid);
        if (dao.isPresent()) {
            if (dao.get().read(uuid).isPresent())
                target = dao.get().read(uuid).get();
            Mockito.when(dao.get().create()).thenReturn (mock);
        }
        
        Resource copy = service.copy(requester, uuid);
        
        if (target != null) {
            Mockito.verify(target, Mockito.times(1)).copy(copy);
            Mockito.verify(copy, Mockito.times(1)).setOwner(requester);
            Mockito.verify(copy, Mockito.times(1)).clearEditors();
            Mockito.verify(copy, Mockito.times(1)).clearViewers();
        }
    }

    @Test
    public void testSendTo(
            @Teacher Account requester,
            @CanView UUID uuid,
            @Teacher Account user) throws Exception {
        
        ResourceService service = this.service();
        ResourceService spy = Mockito.spy(service);
        
        Resource mock = Mockito.mock(Resource.class);
        
        Optional<ResourceDao<?>> dao = this.index.get(uuid);
        if (dao.isPresent()) {
            Mockito.when(dao.get().create()).thenReturn (mock);
        }
        
        Resource copy = spy.sendTo(requester, uuid, user);
        Mockito.verify(spy, Mockito.times(1)).copy(requester, uuid);
        Mockito.verify(copy, Mockito.times(1)).setOwner(user);
                
    }

    @Test
    public void testRead(
            Account requester,
            @CanView UUID uuid) throws Exception {
        
        ResourceService service = this.service();
        Resource mock = Mockito.mock(Resource.class);
        
        Optional<ResourceDao<?>> dao = this.index.get(uuid);
        if (dao.isPresent()) {
            if (dao.get().read(uuid).isPresent())
                mock = dao.get().read(uuid).get();
        }
        
        service.read(requester, uuid);
        
        Mockito.verify (mock, Mockito.times(1)).getModel(requester);
    }

    @Test
    public void testSetOwner(
            @Teacher Account requester,
            @Owner UUID uuid,
            @Teacher @Nullable Account user) throws Exception {
            
        ResourceService service = this.service();
        Resource mock = Mockito.mock(Resource.class);
        
        Optional<ResourceDao<?>> dao = this.index.get(uuid);
        if (dao.isPresent()) {
            if (dao.get().read(uuid).isPresent())
                mock = dao.get().read(uuid).get();
        }
        
        service.setOwner(requester, uuid, user);
        
        Mockito.verify (mock, Mockito.times(1)).setOwner(user);
        
    }

    @Test
    public void testSetName(
            @Teacher Account requester,
            @CanEdit UUID uuid,
            @Nullable String name) throws Exception {
            
        ResourceService service = this.service();
        Resource mock = Mockito.mock(Resource.class);
        
        Optional<ResourceDao<?>> dao = this.index.get(uuid);
        if (dao.isPresent()) {
            if (dao.get().read(uuid).isPresent())
                mock = dao.get().read(uuid).get();
        }
        
        service.setName(requester, uuid, name);
        
        Mockito.verify (mock, Mockito.times(1)).setName (name);
    }

    @Test
    public void testAddEditor(
            @Teacher Account requester,
            @CanEdit UUID uuid,
            @Teacher Account user) throws Exception {

        ResourceService service = this.service();
        Resource mock = Mockito.mock(Resource.class);
        
        Optional<ResourceDao<?>> dao = this.index.get(uuid);
        if (dao.isPresent()) {
            if (dao.get().read(uuid).isPresent())
                mock = dao.get().read(uuid).get();
        }
        
        service.addEditor(requester, uuid, user);
        
        Mockito.verify (mock, Mockito.times(1)).addEditor(user);
        
    }

    @Test
    public void testDelEditor(
            @Teacher Account requester,
            @CanEdit UUID uuid,
            @Teacher Account user) throws Exception {

        ResourceService service = this.service();
        Resource mock = Mockito.mock(Resource.class);
        
        Optional<ResourceDao<?>> dao = this.index.get(uuid);
        if (dao.isPresent()) {
            if (dao.get().read(uuid).isPresent())
                mock = dao.get().read(uuid).get();
        }
        
        service.delEditor(requester, uuid, user);
        
        Mockito.verify (mock, Mockito.times(1)).delEditor(user);
        
    }

    @Test
    public void testAddViewer(
            @Teacher Account requester,
            @CanEdit UUID uuid,
            @Teacher Account user) throws Exception {

        ResourceService service = this.service();
        Resource mock = Mockito.mock(Resource.class);
        
        Optional<ResourceDao<?>> dao = this.index.get(uuid);
        if (dao.isPresent()) {
            if (dao.get().read(uuid).isPresent())
                mock = dao.get().read(uuid).get();
        }
        
        service.addViewer(requester, uuid, user);
        
        Mockito.verify (mock, Mockito.times(1)).addViewer(user);
        
    }

    @Test
    public void testDelViewer(
            @Teacher Account requester,
            @CanEdit UUID uuid,
            @Teacher Account user) throws Exception {

        ResourceService service = this.service();
        Resource mock = Mockito.mock(Resource.class);
        
        Optional<ResourceDao<?>> dao = this.index.get(uuid);
        if (dao.isPresent()) {
            if (dao.get().read(uuid).isPresent())
                mock = dao.get().read(uuid).get();
        }
        
        service.delViewer(requester, uuid, user);
        
        Mockito.verify (mock, Mockito.times(1)).delViewer(user);
        
    }

    @Test
    public void testDelete(
            @Teacher Account requester,
            @Owner UUID uuid) throws Exception {

        ResourceService service = this.service();
        Resource mock = Mockito.mock(Resource.class);
        
        Optional<ResourceDao<?>> dao = this.index.get(uuid);
        if (dao.isPresent()) {
            if (dao.get().read(uuid).isPresent())
                mock = dao.get().read(uuid).get();
        }
        
        service.delete (requester, uuid);
        
        Mockito.verify (mock, Mockito.times(1)).delete ();
        Mockito.verify (dao.get(), Mockito.times(1)).delete (uuid);
    }

}
