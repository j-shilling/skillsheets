package com.shilling.skillsheets.api.services;

import com.shilling.skillsheets.api.model.ResourceModel;
import com.shilling.skillsheets.dao.Resource;
import com.shilling.skillsheets.dao.User;

import java.util.UUID;

import javax.annotation.Nullable;

public interface ResourceService {
	
	public Resource copy (User requester, UUID uuid);
	public Resource sendTo (User requester, UUID uuid, User user);
	
	public ResourceModel read (User requester, UUID uuid);
	
	public void setOwner (User requester, UUID uuid, @Nullable User user);
	public void setName (User requester, UUID uuid, @Nullable String name);
	public void addEditor (User requester, UUID uuid, User user);
	public void delEditor (User requester, UUID uuid, User user);
	public void addViewer (User requester, UUID uuid, User user);
	public void delViewer (User requester, UUID uuid, User user);
	
	public void delete (User requester, UUID uuid);
	
}