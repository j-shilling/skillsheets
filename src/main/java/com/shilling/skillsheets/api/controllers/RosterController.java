package com.shilling.skillsheets.api.controllers;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.shilling.skillsheets.api.controllers.exceptions.BadUuidException;
import com.shilling.skillsheets.api.controllers.exceptions.InternalErrorException;
import com.shilling.skillsheets.api.controllers.exceptions.NotFoundException;
import com.shilling.skillsheets.api.model.ResourceModel;
import com.shilling.skillsheets.api.services.UserGroupService;
import com.shilling.skillsheets.api.services.UserService;
import com.shilling.skillsheets.dao.User;
import com.shilling.skillsheets.dao.UserGroup;

/**
 * REST Controller to interface with {@link UserGroupService}. All
 * user groups are created and read as lists of students and will
 * be given student permissions.
 * 
 * @author Jake Shilling
 *
 */
@RestController
public class RosterController {
	
	private final UuidValidator uuids;
	private final UserValidator validator;
	private final UserGroupService service;
	
	@Autowired
	private RosterController (
			UuidValidator uuids,
			UserValidator validator,
			UserService users,
			UserGroupService service) {
		this.uuids = uuids;
		this.validator = validator;
		this.service = service;
	}
	
	/**
	 * Create a new student user group. Successful response will
	 * have a status code of 201.
	 * 
	 * @param id_token		ID Token for the requesting user. User
	 * 						Must have a teacher account.
	 * @param response		Used to set HTTP Response status code.
	 * @return				A client model of the created group.
	 */
	@PostMapping(value = "/api/rosters",
			produces="application/json")
	public ResourceModel create (
			@RequestHeader (value = "Id-Token") String id_token,
			HttpServletResponse response) {
		
		User user = this.validator.getTeacher(id_token);
		
		try {
			UserGroup group = this.service.create (user);
			response.setStatus(HttpServletResponse.SC_CREATED);
			return group.getModel(user);
		} catch (Exception e) {
			throw new InternalErrorException (e);
		}
		
	}
	
	/**
	 * Find all student user groups the user owns or is
	 * a member of.
	 * 
	 * @param id_token		ID Token for the user
	 * @return				A JSON list of client user group models
	 */
	@GetMapping(value = "/api/rosters",
			produces="application/json")
	public Collection<ResourceModel> read (
			@RequestHeader (value = "Id-Token") String id_token) {
		User user = this.validator.getAny(id_token);
		
		try {
			Collection<UserGroup> groups = this.service.read (user);
			Collection<ResourceModel> ret = new HashSet<>();
			
			for (UserGroup group : groups) {
				ret.add(group.getModel (user));
			}
			
			return ret;
		} catch (Exception e) {
			throw new InternalErrorException (e);
		}
	}
	
	/**
	 * Get a client model for a specific user group. The user
	 * must be a member or owner of the specified group.
	 * 
	 * @param id_token		ID Token for the user
	 * @param uuid			Identifier of the user group
	 * @return				A client model of the created group
	 */
	@GetMapping(value = "/api/rosters/{uuid}",
			produces="application/json")
	public ResourceModel read (
			@RequestHeader (value = "Id-Token") String id_token, 
			@PathVariable String uuid) {
		User user = this.validator.getAny(id_token);
		
		try {
			Optional<UserGroup> group = this.service.read (user, this.uuids.validate(uuid));
			if (group.isPresent())
				return group.get().getModel(user);
			else
				throw new NotFoundException();
		} catch (BadUuidException e) {
			throw e;
		} catch (Exception e) {
			throw new InternalErrorException (e);
		}
	}
	
	/**
	 * Delete a specific user group. User must have a teacher
	 * account and be an owner of the group.
	 * 
	 * @param id_token		ID token for the user account
	 * @param uuid			Identifier for the user group
	 */
	@DeleteMapping(value = "/api/rosters/{uuid}")
	public void delete (
			@RequestHeader (value = "Id-Token") String id_token, 
			@PathVariable String uuid) {
		User user = this.validator.getTeacher(id_token);
		
		boolean ret;
		try {
			ret = this.service.delete (user, this.uuids.validate(uuid));
		} catch (BadUuidException e) {
			throw e;
		} catch (Exception e) {
			throw new InternalErrorException (e);
		}
		
		if (!ret)
			throw new NotFoundException();
	}
	
	/**
	 * Sets or changes the name of the specified user group. This
	 * name may be null. User must have a teacher account and be
	 * and owner of the user group.
	 * 
	 * @param id_token			ID Token of the user
	 * @param uuid				Identifier of the user group
	 * @param name				New name or null
	 */
	@PutMapping(value = "/api/rosters/{uuid}/name",
			consumes ="application/json")
	public void setName (
			@RequestHeader (value = "Id-Token") String id_token,
			@PathVariable String uuid, 
			@RequestBody(required = false) String name) {
		User user = this.validator.getTeacher(id_token);
		
		boolean ret;
		try {
			ret = this.service.setName (user, this.uuids.validate(uuid), name);
		} catch (BadUuidException e) {
			throw e;
		} catch (Exception e) {
			throw new InternalErrorException (e);
		}
		
		if (!ret)
			throw new NotFoundException();
	}
	
	/**
	 * Adds a new member to the user group as a student. User must
	 * have a teacher account and be an owner of the user group.
	 * 
	 * @param id_token		ID Token for the requesting user.
	 * @param uuid			Identifier of the user group.
	 * @param member		The ID, UUID, or email of the user to add.
	 */
	@PostMapping(value = "/api/rosters/{uuid}")
	public void addMember (
			@RequestHeader (value = "Id-Token") String id_token,
			@PathVariable String uuid,
			@RequestBody String member) {
		User user = this.validator.getTeacher(id_token);
		
		boolean ret;
		try {
			ret = this.service.addMember (user, this.uuids.validate(uuid), member);
		} catch (BadUuidException e) {
			throw e;
		} catch (Exception e) {
			throw new InternalErrorException (e);
		}
		
		if (!ret)
			throw new NotFoundException();
	}
	
	/**
	 * Removes a member from the user group as a student. User must
	 * have a teacher account and be an owner of the user group.
	 * 
	 * @param id_token		ID Token for the requesting user.
	 * @param uuid			Identifier of the user group.
	 * @param member		The ID, UUID, or email of the user to add.
	 */
	@DeleteMapping(value = "/api/rosters/{uuid}/{user}")
	public void delMember (
			@RequestHeader (value = "Id-Token") String id_token,
			@PathVariable String uuid,
			@PathVariable String member) {
		User user = this.validator.getTeacher(id_token);
		
		boolean ret;
		try {
			ret = this.service.delMember (user, this.uuids.validate(uuid), member);
		} catch (BadUuidException e) {
			throw e;
		} catch (Exception e) {
			throw new InternalErrorException (e);
		}
		
		if (!ret)
			throw new NotFoundException();
	}

}
