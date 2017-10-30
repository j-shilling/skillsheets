package com.shilling.skillsheets.api;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.shilling.skillsheets.GoogleUserFactory;
import com.shilling.skillsheets.dao.UserNotifications;
import com.shilling.skillsheets.model.Notification;
import com.shilling.skillsheets.model.Tokens;
import com.shilling.skillsheets.model.User;

@RestController
public class Notifications {
	
	private final Logger logger;
	private final GoogleUserFactory users;
	private final UserNotifications dao;
	
	@Autowired
	private Notifications (GoogleUserFactory users,
			UserNotifications dao) {
		this.logger = LogManager.getLogger(Notifications.class);
		this.users = users;
		this.dao = dao;
	}

	@RequestMapping(value = "/api/messages",
			method = RequestMethod.POST,
			consumes="application/json")
	public Collection<Notification> getNotifications (@RequestBody Tokens tokens) {
		this.logger.traceEntry();
		Optional<User> user = this.users.getUser(tokens);
		if (!user.isPresent())
			return Collections.emptyList();
		
		return this.dao.getAllMessages(user.get());
	}
	
	@RequestMapping(value = "/api/messages/{msgId}/{actionId}",
			method = RequestMethod.PUT,
			consumes="application/json")
	public void respontToNotifications (
			@PathVariable final int msgId,
			@PathVariable final int actionId,
			@RequestBody Tokens tokens) {
		
		this.logger.traceEntry();
		Optional<User> user = this.users.getUser(tokens);
		if (!user.isPresent()) {
			this.logger.error("User responding to notification with invalid id token.");
			return;
		}
		
		Notification.Action action = Notification.Action.create(actionId);
		if (action == null) {
			this.logger.error("User " 
					+ user.get() 
					+ " responding to notification with invalid action id: " 
					+ actionId);
			return;
		}
		
		this.logger.warn("This method is not yet written.");
		
		switch (action) {
		case OK:
			this.dao.deleteMessage(user.get(), msgId);
			break;
		default:
			this.logger.warn("Cannot process action " + action);
			break;
		}
	}
	
	public void notify (final User user, final String message) {
		if (user == null)
			return;
		if (message == null || message.isEmpty())
			return;
		
		Notification msg = new Notification (message);
		
		this.dao.saveMessage(user, msg);
	}
}
