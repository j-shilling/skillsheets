package com.shilling.skillsheets.dao;

import java.util.Collection;

import com.shilling.skillsheets.model.Resource;
import com.shilling.skillsheets.model.User;

public interface UserResources {
	public Collection<Resource> getTeacherResources(User user);
	public Collection<Resource> getStudentResources(User user);
	public Collection<Resource> getObserverResources(User user);
	
	public boolean addTeacherResources(User user, Resource resource);
	public boolean addStudentResources(User user, Resource resource);
	public boolean addObserverResources(User user, Resource resource);
}
