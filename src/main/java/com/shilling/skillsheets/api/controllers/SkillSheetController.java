package com.shilling.skillsheets.api.controllers;

import java.util.Collection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.shilling.skillsheets.api.services.SkillSheetService;
import com.shilling.skillsheets.api.services.UserService;
import com.shilling.skillsheets.model.SkillSheet;

/**
 * REST Controller for {@link com.shilling.skillsheets.api.services.SkillSheetService}
 * 
 * @author Jake Shilling
 *
 */
@RestController
public class SkillSheetController {
	
	private final Logger logger;
	private final SkillSheetService service;
	private final UserService users;
	
	@Autowired
	private SkillSheetController (SkillSheetService service, UserService users) {
		this.logger = LogManager.getLogger(SkillSheetController.class);
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
	@RequestMapping(value = "/api/skillsheets",
			method = RequestMethod.POST,
			produces="application/json")
	public SkillSheet create (@RequestHeader (value = "Id-Token") String id_token) {
		return null;
	}
	
	/**
	 * Finds all {@link SkillSheet}s associated with a given user
	 * account.
	 * 
	 * @param id_token	The Google ID Token to verify the user
	 * @return			A {@link java.util.Collection} of {@link SkillSheet}s 
	 * 					visible to this user.
	 */
	@RequestMapping(value = "/api/skillsheets",
			method = RequestMethod.GET,
			produces="application/json")
	public Collection<SkillSheet> read (@RequestHeader (value = "Id-Token") String id_token) {
		return null;
	}
	
	/**
	 * Gets a specific {@link SkillSheet} available to a user.
	 * 
	 * @param id_token	The Google ID Token to verify the user
	 * @param uuid		A unique identifier for this {@link SkillSheet}
	 * @return			Resulting {@link SkillSheet} or null.
	 */
	@RequestMapping(value = "/api/skillsheets/{uuid}",
			method = RequestMethod.GET,
			produces="application/json")
	public SkillSheet read (@RequestHeader (value = "Id-Token") String id_token, 
							@PathVariable String uuid) {
		return null;
	}
	
	/**
	 * Replaces a specific {@link SkillSheet} with a new one.
	 * 
	 * @param id_token		The Google ID Token to verify the user
	 * @param uuid			A unique identifier for this {@link SkillSheet}
	 * @param skillsheet	New value
	 */
	@RequestMapping(value = "/api/skillsheets/{uuid}",
			method = RequestMethod.PUT,
			consumes ="application/json")
	public void update (@RequestHeader (value = "Id-Token") String id_token,
						@PathVariable String uuid, 
						@RequestBody SkillSheet skillsheet) {
		
	}
	
	/**
	 * Deletes a specific {@link SkillSheet} from permanent storage.
	 * 
	 * @param id_token		The Google ID Token to verify the user
	 * @param uuid			A unique identifier for this {@link SkillSheet}
	 */
	@RequestMapping(value = "/api/skillsheets/{uuid}",
			method = RequestMethod.DELETE)
	public void delete (@RequestHeader (value = "Id-Token") String id_token, 
						@PathVariable String uuid) {
		
	}

}
