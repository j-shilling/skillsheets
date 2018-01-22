package com.shilling.skillsheets.api.controllers;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
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

import com.shilling.skillsheets.api.services.SkillSheetService;
import com.shilling.skillsheets.api.services.UserService;
import com.shilling.skillsheets.model.SkillSheet;
import com.shilling.skillsheets.model.User;

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
		
		Optional<User> user = this.users.fromToken(id_token);
		if (!user.isPresent()) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return null;
		}
		
		if (!user.get().isTeacher()) {
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			return null;
		}
		
		SkillSheet skillsheet = this.service.create(user.get());
		response.setStatus(HttpServletResponse.SC_CREATED);
		return skillsheet.getTeacherView();
		
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
		
	
		Optional<User> user = this.users.fromToken(id_token);
		if (!user.isPresent()) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return Collections.emptyList();
		}
		
		Collection<SkillSheet> ret = this.service.read(user.get());
		if (user.get().isTeacher())
			return ret.stream()
					.map(SkillSheet::getTeacherView)
					.collect(Collectors.toSet());
		else
			return ret.stream()
					.map(SkillSheet::getStudentView)
					.collect(Collectors.toSet());
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
		
		Optional<User> user = this.users.fromToken(id_token);
		if (!user.isPresent()) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return null;
		}
		
		Optional<SkillSheet> result = this.service.read(user.get(), uuid);
		if (!result.isPresent()) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}
		
		if (user.get().isTeacher())
			return result.get().getTeacherView();
		else
			return result.get().getStudentView();
	}
	
	/**
	 * Replaces a specific {@link SkillSheet} with a new one.
	 * 
	 * @param id_token		The Google ID Token to verify the user
	 * @param uuid			A unique identifier for this {@link SkillSheet}
	 * @param skillsheet	New value
	 */
	@PutMapping(value = "/api/skillsheets/{uuid}",
			consumes ="application/json")
	public void update (@RequestHeader (value = "Id-Token") String id_token,
						@PathVariable String uuid, 
						@RequestBody SkillSheet skillsheet,
						HttpServletResponse response) {
		
		Optional<User> user = this.users.fromToken(id_token);
		if (!user.isPresent()) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}
		
		if (!user.get().isTeacher()) {
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			return;
		}
		
		if (!this.service.update(user.get(), uuid, skillsheet))
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		
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
		
		Optional<User> user = this.users.fromToken(id_token);
		if (!user.isPresent()) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}
		
		if (!user.get().isTeacher()) {
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			return;
		}
		
		if (!this.service.delete(user.get(), uuid))
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
	}

}
