package com.shilling.skillsheets.api.controllers.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus (HttpStatus.INTERNAL_SERVER_ERROR)
public class InternalErrorException extends RuntimeException {

	private static final long serialVersionUID = -3783502536226927923L;
	
	public InternalErrorException (Throwable cause) {
		super ("Internal Error", cause);
	}

}
