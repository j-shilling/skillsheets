package com.shilling.skillsheets.api.services;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.shilling.skillsheets.dao.User;

/**
 * Service to get user accounts from Id Tokens
 * 
 * @author Jake Shilling
 *
 */
@Service
public interface UserService {

	/**
	 * Read an Id Token and return a corresponding user or
	 * Optional.empty() if failed.
	 * 
	 * @param id_token		String from the client
	 * @return				Optional<User> or Optional.empty()
	 * @throws IOException 
	 */
	public Optional<User> fromToken (String id_token) throws IOException;

	public Optional<User> fromUuid(UUID id);
	
}
