package com.shilling.skillsheets.dao;

import java.util.Collection;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.shilling.skillsheets.model.Resource;
import com.shilling.skillsheets.model.Resources;
import com.shilling.skillsheets.model.User;

public class UserResourcesImpl implements UserResources {
	
	private final Logger logger;
	private final ModelWriter writer;
	private final ModelReader reader;
	
	@Autowired
	private UserResourcesImpl(ModelWriter writer, ModelReader reader) {
		this.logger = LogManager.getLogger(UserResourcesImpl.class);
		this.writer = writer;
		this.reader = reader;
	}
	
	private Resources getResources(User user) {
		this.logger.traceEntry("Reading current resources for " + user);
		
		Optional<Resources> prev = this.reader.read(user, Resources.class);
		if (prev.isPresent()) {
			this.logger.traceExit("Found resources.");
			return prev.get();
		} else {
			this.logger.traceExit("No resources found.");
			return new Resources();
		}
	}

	@Override
	public Collection<Resource> getTeacherResources(User user) {
		this.logger.traceEntry ("Getting teacher resources for " + user);
		
		Resources res = this.getResources(user);
		return res.getTeacherResources();
	}

	@Override
	public Collection<Resource> getStudentResources(User user) {
		this.logger.traceEntry ("Getting student resources for " + user);
		
		Resources res = this.getResources(user);
		return res.getStudentResources();
	}

	@Override
	public Collection<Resource> getObserverResources(User user) {
		this.logger.traceEntry ("Getting observer resources for " + user);
		
		Resources res = this.getResources(user);
		return res.getObserverResources();
	}

	@Override
	public boolean addTeacherResources(User user, Resource resource) {
		this.logger.traceEntry ("Adding teacher resource for " + user);
		
		Resources res = this.getResources(user);
		if (res.addTeacherResource(resource)) {
			this.logger.trace("Saving resources.");
			boolean ret = this.writer.write(user, res);
			this.logger.traceExit("Successful: " + ret);
			return ret;
		} else {
			this.logger.traceExit ("Resources unchanged.");
			return true;
		}
	}

	@Override
	public boolean addStudentResources(User user, Resource resource) {
		this.logger.traceEntry ("Adding student resource for " + user);
		
		Resources res = this.getResources(user);
		if (res.addStudentResource(resource)) {
			this.logger.trace("Saving resources.");
			boolean ret = this.writer.write(user, res);
			this.logger.traceExit("Successful: " + ret);
			return ret;
		} else {
			this.logger.traceExit ("Resources unchanged.");
			return true;
		}
	}

	@Override
	public boolean addObserverResources(User user, Resource resource) {
		this.logger.traceEntry ("Adding observer resource for " + user);
		
		Resources res = this.getResources(user);
		if (res.addObserverResource(resource)) {
			this.logger.trace("Saving resources.");
			boolean ret = this.writer.write(user, res);
			this.logger.traceExit("Successful: " + ret);
			return ret;
		} else {
			this.logger.traceExit ("Resources unchanged.");
			return true;
		}
	}

}
