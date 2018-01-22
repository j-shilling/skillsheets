package com.shilling.skillsheets.api.services;

import java.util.Optional;

import com.shilling.skillsheets.model.User;

/**
 * Service to get user accounts from Id Tokens
 * 
 * @author Jake Shilling
 *
 */
public interface UserService {

	/**
	 * Read an Id Token and return a corresponding user or
	 * Optional.empty() if failed.
	 * 
	 * @param id_token		String from the client
	 * @return				Optional<User> or Optional.empty()
	 */
	public Optional<User> fromToken (String id_token);
	
}
