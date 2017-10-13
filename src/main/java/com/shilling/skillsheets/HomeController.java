package com.shilling.skillsheets;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.shilling.skillsheets.dao.UserNotifications;
import com.shilling.skillsheets.dao.UserSettings;
import com.shilling.skillsheets.model.TokenId;
import com.shilling.skillsheets.model.User;

/**
 * A class to receive and process HTTP requests.
 * 
 * @author Jake Shilling
 *
 */
@Controller
public class HomeController {
	
	private final Logger logger;
	private final GoogleUserFactory users;
	
	private final UserNotifications userNotifications;
	private final UserSettings userSettings;
	
	@Autowired
	private HomeController (GoogleUserFactory users, 
			UserNotifications userNotifications,
			UserSettings userSettings) {
		this.logger = LogManager.getLogger(HomeController.class);
		this.users = users;
		this.userNotifications = userNotifications;
		this.userSettings = userSettings;
	}
	
	/** Return index.jsp */
	@RequestMapping("/")
	public String index(ModelMap model) {
		this.logger.traceEntry();
		return "index";
	}
	
	/** Validate user and return home.jsp on success */
	@RequestMapping(value = "/home", 
			method = RequestMethod.POST,
			consumes = {"application/json"})
	public String home(@RequestBody TokenId tokenid, ModelMap model) {
		this.logger.traceEntry();
		
		Optional<User> user = this.users.getUser(tokenid);
		
		this.logger.trace("User validated: " + user.isPresent());
		if (user.isPresent()) {
			model.put(StringConstants.USER, user.get());
			model.put(StringConstants.USER_NOTIFICATIONS, this.userNotifications);
			model.put(StringConstants.USER_SETTINGS, this.userSettings);
			
			this.logger.traceExit("home");
			return "home";
		} else {
			this.logger.traceExit("index");
			return "index";
		}
	}

}
