package com.shilling.skillsheets.api;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.shilling.skillsheets.GoogleUserFactory;
import com.shilling.skillsheets.dao.UserResources;
import com.shilling.skillsheets.model.Resource;
import com.shilling.skillsheets.model.Tokens;
import com.shilling.skillsheets.model.User;

@RestController
public class ResourcesService {

	private final Logger logger;
	private final GoogleUserFactory users;
	private final UserResources dao;
	
	@Autowired
	private ResourcesService (
			GoogleUserFactory users,
			UserResources dao) {
		this.logger = LogManager.getLogger(NotificationsService.class);
		this.users = users;
		this.dao = dao;
	}
	
	@RequestMapping(value = "/api/my/teacher",
			method = RequestMethod.POST,
			consumes="application/json",
			produces="application/json")
	public Collection<Resource> getTeacherResources (@RequestBody Tokens tokens) {
		this.logger.traceEntry();
		
		Optional<User> user = this.users.getUser(tokens);
		if (!user.isPresent()) {
			this.logger.warn("Someone is trying to look at our resources with an invalid token.");
			return Collections.emptySet();
		}
		
		return this.dao.getTeacherResources(user.get());
	}
	
	@RequestMapping(value = "/api/my/student",
			method = RequestMethod.POST,
			consumes="application/json",
			produces="application/json")
	public Collection<Resource> getStudentResources (@RequestBody Tokens tokens) {
		this.logger.traceEntry();
		
		Optional<User> user = this.users.getUser(tokens);
		if (!user.isPresent()) {
			this.logger.warn("Someone is trying to look at our resources with an invalid token.");
			return Collections.emptySet();
		}
		
		return this.dao.getStudentResources(user.get());
	}
	
	@RequestMapping(value = "/api/my/observer",
			method = RequestMethod.POST,
			consumes="application/json",
			produces="application/json")
	public Collection<Resource> getObserverResources (@RequestBody Tokens tokens) {
		this.logger.traceEntry();
		
		Optional<User> user = this.users.getUser(tokens);
		if (!user.isPresent()) {
			this.logger.warn("Someone is trying to look at our resources with an invalid token.");
			return Collections.emptySet();
		}
		
		return this.dao.getObserverResources(user.get());
	}
	
	@RequestMapping(value = "/api/create/{typeString}",
			method = RequestMethod.POST,
			consumes="application/json",
			produces="application/json")
	public Optional<Resource> createUserResource (
			@PathVariable String typeString,
			@RequestBody Tokens tokens) {
		this.logger.traceEntry();
		
		Optional<User> user = this.users.getUser(tokens);
		if (!user.isPresent()) {
			this.logger.warn("Someone is trying to create a resource without valid credentials.");
			return Optional.empty();
		}
		
		Resource.Type type = null;
		try {
			type = Resource.Type.parseString(typeString);
		} catch (IllegalArgumentException e) {
			this.logger.warn(e.getMessage());
			return Optional.empty();
		}
		
		Resource ret = new Resource (type);
		if (!this.dao.addTeacherResources(user.get(), ret)) {
			this.logger.error("Could not save new resource.");
			return Optional.empty();
		}
		
		return Optional.of(ret);
	}
}
