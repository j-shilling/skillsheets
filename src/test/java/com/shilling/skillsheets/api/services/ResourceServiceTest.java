package com.shilling.skillsheets.api.services;

import com.shilling.skillsheets.dao.Resource;
import com.shilling.skillsheets.dao.ResourceDao;
import com.shilling.skillsheets.dao.ResourceIndex;
import com.shilling.skillsheets.dao.User;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nullable;
import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

@RunWith(SkillSheetServiceRunner.class)
public class ResourceServiceTest {
    
    private ResourceIndex index;
    
    private ResourceService service() {
        return new ResourceServiceImpl (this.index);
    }

    @Test
    public void testCopy(
            @Teacher User requester,
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
                @Teacher User requester,
                @CanView UUID uuid,
                @Teacher User user) {
            
		fail("Not yet implemented");
                
	}

	@Test
	public void testRead(
                User requester,
                @CanView UUID uuid) {
            
		fail("Not yet implemented");
	}

	@Test
	public void testSetOwner(
                @Teacher User requester,
                @Owner UUID uuid,
                @Teacher @Nullable User user) {
            
		fail("Not yet implemented");
	}

	@Test
	public void testSetName(
                @Teacher User requester,
                @CanEdit UUID user,
                @Nullable String name) {
            
		fail("Not yet implemented");
	}

	@Test
	public void testAddEditor(
                @Teacher User requester,
                @CanEdit UUID user) {
		fail("Not yet implemented");
	}

	@Test
	public void testDelEditor(
                @Teacher User requester,
                @CanEdit UUID user) {
		fail("Not yet implemented");
	}

	@Test
	public void testAddViewer(
                @Teacher User requester,
                @CanEdit UUID user) {
		fail("Not yet implemented");
	}

	@Test
	public void testDelViewer(
                @Teacher User requester,
                @CanEdit UUID user) {
		fail("Not yet implemented");
	}

	@Test
	public void testDelete(
                @Teacher User requester,
                @Owner UUID user) {
		fail("Not yet implemented");
	}

}
