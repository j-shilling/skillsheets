package com.shilling.skillsheets.api.services;

import java.util.Optional;

import com.shilling.skillsheets.model.User;

/**
 * Service to get user accounts from Google Id Tokens
 * @author jake
 *
 */
public interface UserService {

	public Optional<User> fromToken (String id_token);
	
}
