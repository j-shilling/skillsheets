package com.shilling.skillsheets.dao;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.springframework.beans.factory.annotation.Autowired;

import com.shilling.skillsheets.model.Notification;

public class XMLModelEncoder implements ModelEncoder {
	
	private final Logger logger;
	
	private XMLModelEncoder(ModelReader reader) {
		this.logger = LogManager.getLogger(XMLModelEncoder.class);
	}
	
	private Document getDocument (InputStream in) {
		SAXReader reader = new SAXReader();
		
		Document doc = null;
		try {
			doc = reader.read(in);
			
		} catch (DocumentException e) {
			this.logger.warn(e.getMessage());
			this.logger.warn("Could not read existing document; creating a new one.");
			
			doc = DocumentHelper.createDocument();
			doc.addElement("root");	
		}

		return doc;
	}

	@Override
	public boolean encode(Notification notification, InputStream in, OutputStream out) {
		this.logger.traceEntry("Encoding " + notification);
		
		Document doc = this.getDocument(in);
		
		Element root = doc.getRootElement();
		Element message = root.addElement("notification")
				.addAttribute("id", Integer.toString(notification.getId()));
		
		message.addElement("timestamp").addText(notification.getTimestamp());
		message.addElement("text").addText(notification.getMessage());
		for (Notification.Action action : notification.getAction())
			message.addElement("Action").addText(Integer.toString(action.getValue()));
		
		OutputFormat format = OutputFormat.createPrettyPrint();
		
		try {
			XMLWriter writer = new XMLWriter (out, format);
			writer.write(doc);
		} catch (UnsupportedEncodingException e) {
			this.logger.error(e.getMessage());
			return false;
		} catch (IOException e) {
			this.logger.error(e.getMessage());
			return false;
		}
		
		return true;
	}

}
