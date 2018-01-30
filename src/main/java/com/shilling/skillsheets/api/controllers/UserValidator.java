package com.shilling.skillsheets.api.controllers;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.shilling.skillsheets.api.controllers.exceptions.ForbiddenException;
import com.shilling.skillsheets.api.controllers.exceptions.UnauthorizedException;
import com.shilling.skillsheets.api.services.UserService;
import com.shilling.skillsheets.dao.User;

public class UserValidator {
	
	UserService users;
	
	@Autowired
	private UserValidator (UserService users) {
		this.users = users;
	}
	
	private User validate (String id_token) {
		Optional<User> result;
		
		try {
			result = this.users.fromToken(id_token);
		} catch (IOException e) {
			throw new UnauthorizedException (e);
		}
		
		if (result.isPresent())
			return result.get();
		else
			throw new UnauthorizedException();
	}
	
	public User getTeacher (String id_token) {
		User user = this.validate(id_token);
		
		try {
			if (user.isTeacher())
				return user;
			else
				throw new ForbiddenException();
		} catch (IOException e) {
			throw new ForbiddenException (e);
		}
		
	}
	
	public User getAny (String id_token) {
		return this.validate(id_token);
	}

}
