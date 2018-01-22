package com.shilling.skillsheets;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * A class to receive and process HTTP requests.
 * 
 * @author Jake Shilling
 *
 */
@Controller
public class MainController {
	
	private final Logger logger;
	
	@Autowired
	private MainController () {
		this.logger = LogManager.getLogger(MainController.class);
	}
	
	@RequestMapping(value="/", method = RequestMethod.GET)
	public String index() {
		this.logger.traceEntry();
		return "index.html";
	}
	
	@RequestMapping(value="/oauth2callback")
	@ResponseBody
	public String oauth2callback() {
		this.logger.traceEntry("Responding to /oauth2callback");
		return "";
	}

}
