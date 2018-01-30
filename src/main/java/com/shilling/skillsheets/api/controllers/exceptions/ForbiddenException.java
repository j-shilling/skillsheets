package com.shilling.skillsheets.api.controllers.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus (HttpStatus.FORBIDDEN)
public class ForbiddenException extends RuntimeException {

	private static final long serialVersionUID = -6822249885098812046L;

	public ForbiddenException () {
		super ("Insufficient permission for this operation");
	}
	
	public ForbiddenException (Throwable cause) {
		super ("Insufficient permission for this operation", cause);
	}
}
