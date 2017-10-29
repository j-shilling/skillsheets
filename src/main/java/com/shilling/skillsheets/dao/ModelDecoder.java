package com.shilling.skillsheets.dao;

import java.io.InputStream;
import java.util.Optional;

public interface ModelDecoder {
	public <T> Optional<T> decode (InputStream in, Class<T> klass);
}
