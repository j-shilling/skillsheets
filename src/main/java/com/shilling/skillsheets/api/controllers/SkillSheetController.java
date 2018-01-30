package com.shilling.skillsheets.api.controllers;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

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

import com.shilling.skillsheets.api.controllers.exceptions.InternalErrorException;
import com.shilling.skillsheets.api.controllers.exceptions.NotFoundException;
import com.shilling.skillsheets.api.model.ResourceModel;
import com.shilling.skillsheets.api.services.SkillSheetService;
import com.shilling.skillsheets.dao.SkillSheet;
import com.shilling.skillsheets.dao.User;

/**
 * REST Controller for {@link com.shilling.skillsheets.api.services.SkillSheetService}
 * 
 * @author Jake Shilling
 *
 */
@RestController
public class SkillSheetController {
	
	private final SkillSheetService service;
	private final UserValidator users;
	private final UuidValidator uuids;
	
	@Autowired
	private SkillSheetController (
			SkillSheetService service, 
			UserValidator users, 
			UuidValidator uuids) {
		
		this.service = service;
		this.users = users;
		this.uuids = uuids;
	}
	
	/**
	 * Creates a new {@link SkillSheet} and saves it on the server
	 * for future reads.
	 * 
	 * @param id_token	The Google ID Token to verify the user
	 * @return			The created {@link SkillSheet} or null.
	 */
	@PostMapping(value = "/api/skillsheets",
			produces="application/json")
	public ResourceModel create (
			@RequestHeader (value = "Id-Token") String id_token, 
			HttpServletResponse response) {
		
		User user = users.getTeacher(id_token);
		
		SkillSheet skillsheet = null;
		try {
			skillsheet = this.service.create(user);
			response.setStatus(HttpServletResponse.SC_CREATED);
		} catch (IOException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		
		return skillsheet.getModel(user);
		
	}
	
	/**
	 * Finds all {@link SkillSheet}s associated with a given user
	 * account.
	 * 
	 * @param id_token	The Google ID Token to verify the user
	 * @return			A {@link java.util.Collection} of {@link SkillSheet}s 
	 * 					visible to this user.
	 */
	@GetMapping(value = "/api/skillsheets",
			produces="application/json")
	public Collection<ResourceModel> read (
			@RequestHeader (value = "Id-Token") String id_token) {
		
	
		User user = users.getAny(id_token);
		
		Collection<ResourceModel> ret = Collections.emptySet();
		
		try {
			ret = this.service.read(user)
					.stream()
					.map(s -> s.getModel(user))
					.collect(Collectors.toSet());
		} catch (IOException e) {
			throw new InternalErrorException (e);
		}
		
		return ret;
	}
	
	
	/**
	 * Gets a specific {@link SkillSheet} available to a user.
	 * 
	 * @param id_token	The Google ID Token to verify the user
	 * @param uuid		A unique identifier for this {@link SkillSheet}
	 * @return			Resulting {@link SkillSheet} or null.
	 */
	@GetMapping(value = "/api/skillsheets/{id}",
			produces="application/json")
	public ResourceModel read (
			@RequestHeader (value = "Id-Token") String id_token, 
			@PathVariable String id) {
		
		User user = users.getAny(id_token);
		UUID uuid = uuids.validate(id);
		
		try {
			Optional<SkillSheet> result = this.service.read(user, uuid);
			if (result.isPresent())
				return result.get().getModel(user);
			else
				throw new NotFoundException ();
		} catch (IOException e) {
			throw new InternalErrorException (e);
		}
	}
	
	/**
	 * Deletes a specific {@link SkillSheet} from permanent storage.
	 * 
	 * @param id_token		The Google ID Token to verify the user
	 * @param uuid			A unique identifier for this {@link SkillSheet}
	 */
	@DeleteMapping(value = "/api/skillsheets/{id}")
	public void delete (
			@RequestHeader (value = "Id-Token") String id_token, 
			@PathVariable String id) {
		
		User user = this.users.getTeacher(id_token);
		UUID uuid = this.uuids.validate(id); 
		
		try {
			if (!this.service.delete(user, uuid))
				throw new NotFoundException ();
		} catch (IOException e) {
			throw new InternalErrorException (e);
		}
	}
	
	/**
	 * Replaces the display name of a SkillSheet
	 * 
	 * @param id_token		The Google ID Token to verify the user
	 * @param uuid			A unique identifier for this {@link SkillSheet}
	 * @param name			New value
	 */
	@PutMapping(value = "/api/skillsheets/{id}/name",
				consumes ="application/json")
	public void setName (
			@RequestHeader (value = "Id-Token") String id_token,
			@PathVariable String id, 
			@RequestBody(required = false) String name) {
		
		User user = this.users.getTeacher(id_token);
		UUID uuid = this.uuids.validate(id);
		
		try {
			if (!this.service.setName (user, uuid, name)) {
				throw new NotFoundException();
			}
		} catch (IOException e) {
			throw new InternalErrorException (e);
		}
		
	}

}
