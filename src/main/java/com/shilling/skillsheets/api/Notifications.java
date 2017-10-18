package com.shilling.skillsheets.api;

import java.util.Optional;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
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
			consumes="application/json")
	public Notification[] getNotifications (@RequestBody(required = false) String tokenid) {
		this.logger.traceEntry();
		Optional<User> user = this.users.getUser(tokenid);
		if (!user.isPresent())
			return new Notification[0];
		
		int rand = new Random().nextInt(10);
		Notification[] ret = new Notification[rand];
		
		for (int i = 0; i < rand; i ++)
			ret[i] = new Notification(i, Integer.toString(i),
						(Notification.Action[]) ((rand % 2) == 0 ? 
								new Notification.Action[]
									{
										Notification.Action.create(new Random().nextInt(3)) 
									}
								: new Notification.Action[] 
									{ Notification.Action.create(new Random().nextInt(3)),
										Notification.Action.create(new Random().nextInt(3)) 
									}));
		
		return ret;
	}
	
	@RequestMapping(value = "/api/messages/{msgId}/{actionId}",
			method = RequestMethod.PUT,
			consumes="application/json")
	public void respontToNotifications (
			@PathVariable final int msgId,
			@PathVariable final int actionId,
			@RequestBody String tokenid) {
		
		this.logger.traceEntry();
		Optional<User> user = this.users.getUser(tokenid);
		if (!user.isPresent()) {
			this.logger.error("User responding to notification with invalid id token.");
			return;
		}
		
		Notification.Action action = Notification.Action.create(actionId);
		if (action == null) {
			this.logger.error("User " 
					+ user.get().getId() 
					+ " responding to notification with invalid action id: " 
					+ actionId);
			return;
		}
		
		this.logger.warn("This method is not yet written.");
	}
}
