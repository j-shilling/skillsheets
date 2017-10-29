package com.shilling.skillsheets.dao.xml;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.shilling.skillsheets.dao.ModelEncoder;

public class XMLModelEncoder implements ModelEncoder {
	
	private final Logger logger;
	private final ObjectMapper mapper;
	
	private XMLModelEncoder() {
		this.logger = LogManager.getLogger(XMLModelEncoder.class);
		this.mapper = new XmlMapper();
		this.mapper.registerModule(new Jdk8Module());
		this.mapper.enable(SerializationFeature.INDENT_OUTPUT);
	}
	
	@Override
	public boolean encode(Object obj, OutputStream out) {
		
		try {
			this.mapper.writeValue(out, obj);
			return true;
		} catch (IOException e) {
			this.logger.error(e.getMessage());
			return false;
		}
		
	}

	@Override
	public String getFileExtension() {
		return "xml";
	}

}
