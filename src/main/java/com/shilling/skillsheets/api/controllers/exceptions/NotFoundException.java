package com.shilling.skillsheets.api.controllers.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus (HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {

	private static final long serialVersionUID = -5188483342471883161L;
	
	public NotFoundException () {
		super ("The requested resource does not exist or you to not have permission to view it");
	}

}
