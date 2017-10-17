package com.shilling.skillsheets.api;

import java.util.Optional;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.shilling.skillsheets.GoogleUserFactory;
import com.shilling.skillsheets.model.Notification;
import com.shilling.skillsheets.model.User;

@RestController
public class Notifications {
	
	private final Logger logger;
	private final GoogleUserFactory users;
	
	@Autowired
	private Notifications (GoogleUserFactory users) {
		this.logger = LogManager.getLogger(Notifications.class);
		this.users = users;
	}

	@RequestMapping(value = "/api/messages",
			method = RequestMethod.POST,
			produces="application/json")
	public Notification[] getNotifications (@RequestBody(required = false) String tokenid) {
		this.logger.traceEntry();
		Optional<User> user = this.users.getUser(tokenid);
		if (!user.isPresent())
			return new Notification[0];
		
		int rand = new Random().nextInt(10);
		Notification[] ret = new Notification[rand];
		
		for (int i = 0; i < rand; i ++)
			ret[i] = new Notification(Integer.toString(i));
		
		return ret;
	}
}
