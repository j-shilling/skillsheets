package com.shilling.skillsheets.dao.xml;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.shilling.skillsheets.dao.ModelDecoder;

public class XMLModelDecoder implements ModelDecoder {
	
	private final Logger logger;
	private final ObjectMapper mapper;
	
	public XMLModelDecoder () {
		this.logger = LogManager.getLogger(XMLModelDecoder.class);
		this.mapper = new XmlMapper();
		this.mapper.registerModule(new Jdk8Module());
		this.mapper.enable(SerializationFeature.INDENT_OUTPUT);
	}

	@Override
	public <T> Optional<T> decode(InputStream in, Class<T> klass) {
		try {
			return  Optional.of(this.mapper.readValue(in, klass));
		} catch (IOException e) {
			this.logger.error(e.getMessage());
			return Optional.empty();
		}
	}

}
