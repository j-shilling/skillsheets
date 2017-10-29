package com.shilling.skillsheets.dao;

import java.util.Optional;

import com.shilling.skillsheets.model.User;

public interface ModelReader {
	public <T> Optional<T> read (User user, Class<T> klass);
}
