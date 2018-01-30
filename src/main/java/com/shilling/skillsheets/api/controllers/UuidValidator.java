package com.shilling.skillsheets.api.controllers;

import java.util.UUID;

import com.shilling.skillsheets.api.controllers.exceptions.BadUuidException;

public class UuidValidator {

	public UUID validate (String uuid) {
		try {
			return UUID.fromString(uuid);
		} catch (IllegalArgumentException e) {
			throw new BadUuidException (uuid);
		}
	}
}
