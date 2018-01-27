package com.shilling.skillsheets.api.controllers;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
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

import com.shilling.skillsheets.api.services.SkillSheetService;
import com.shilling.skillsheets.api.services.UserService;
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
	private final UserService users;
	
	@Autowired
	private SkillSheetController (SkillSheetService service, UserService users) {
		this.service = service;
		this.users = users;
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
	public SkillSheet create (@RequestHeader (value = "Id-Token") String id_token, 
							  HttpServletResponse response) {
		
		Optional<User> user;
		try {
			user = this.users.fromToken(id_token);
		} catch (IOException e1) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return null;
		}
		
		if (!user.isPresent()) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return null;
		}
		
		try {
			if (!user.get().isTeacher()) {
				response.setStatus(HttpServletResponse.SC_FORBIDDEN);
				return null;
			}
		} catch (IOException e1) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return null;
		}
		
		SkillSheet skillsheet = null;
		try {
			skillsheet = this.service.create(user.get());
			response.setStatus(HttpServletResponse.SC_CREATED);
		} catch (IOException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		
		return skillsheet;
		
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
	public Collection<SkillSheet> read (
			@RequestHeader (value = "Id-Token") String id_token,
			HttpServletResponse response) {
		
	
		Optional<User> user;
		try {
			user = this.users.fromToken(id_token);
		} catch (IOException e1) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return null;
		}
		
		if (!user.isPresent()) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return Collections.emptyList();
		}
		
		Collection<SkillSheet> ret = null;
		try {
			ret = this.service.read(user.get());
		} catch (IOException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
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
	@GetMapping(value = "/api/skillsheets/{uuid}",
			produces="application/json")
	public SkillSheet read (@RequestHeader (value = "Id-Token") String id_token, 
							@PathVariable String uuid,
							HttpServletResponse response) {
		
		Optional<User> user;
		try {
			user = this.users.fromToken(id_token);
		} catch (IOException e1) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return null;
		}
		
		if (!user.isPresent()) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return null;
		}
		
		Optional<SkillSheet> result = Optional.empty();
		try {
			result = this.service.read(user.get(), uuid);
		} catch (IOException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return null;
		}
		
		if (!result.isPresent()) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}
		
		return result.get();
	}
	
	/**
	 * Deletes a specific {@link SkillSheet} from permanent storage.
	 * 
	 * @param id_token		The Google ID Token to verify the user
	 * @param uuid			A unique identifier for this {@link SkillSheet}
	 */
	@DeleteMapping(value = "/api/skillsheets/{uuid}")
	public void delete (@RequestHeader (value = "Id-Token") String id_token, 
						@PathVariable String uuid,
						HttpServletResponse response) {
		
		Optional<User> user;
		try {
			user = this.users.fromToken(id_token);
		} catch (IOException e1) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return;
		}
		
		if (!user.isPresent()) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}
		
		try {
			if (!user.get().isTeacher()) {
				response.setStatus(HttpServletResponse.SC_FORBIDDEN);
				return;
			}
		} catch (IOException e1) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return;
		}
		
		try {
			if (!this.service.delete(user.get(), uuid))
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		} catch (IOException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}
	
	/**
	 * Replaces the display name of a SkillSheet
	 * 
	 * @param id_token		The Google ID Token to verify the user
	 * @param uuid			A unique identifier for this {@link SkillSheet}
	 * @param name			New value
	 */
	@PutMapping(value = "/api/skillsheets/{uuid}/name",
				consumes ="application/json")
	public void setName (@RequestHeader (value = "Id-Token") String id_token,
						 @PathVariable String uuid, 
						 @RequestBody(required = false) String name,
						 HttpServletResponse response) {
		
		Optional<User> user;
		try {
			user = this.users.fromToken(id_token);
		} catch (IOException e1) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return;
		}
		
		if (!user.isPresent()) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}
		
		try {
			if (!user.get().isTeacher()) {
				response.setStatus(HttpServletResponse.SC_FORBIDDEN);
				return;
			}
		} catch (IOException e1) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return;
		}
		
		try {
			if (!this.service.setName (user.get(), uuid, name)) {
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			}
		} catch (IOException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		
	}

}
