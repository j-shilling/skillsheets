package com.shilling.skillsheets.api.controllers.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus (HttpStatus.BAD_REQUEST)
public class BadUuidException extends RuntimeException {

	private static final long serialVersionUID = 302377128356241023L;

	public BadUuidException (String invalidUuid) {
		super ("\'" + invalidUuid + "\' is not a valid UUID");
	}
}
