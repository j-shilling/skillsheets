package com.shilling.skillsheets.api.controllers.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus (HttpStatus.UNAUTHORIZED)
public class UnauthorizedException extends RuntimeException {

	private static final long serialVersionUID = -4334167449342576542L;
	
	public UnauthorizedException() {
		super ("User could not be validated");
	}
	
	public UnauthorizedException(Throwable cause) {
		super ("User could not be validated", cause);
	}

}
