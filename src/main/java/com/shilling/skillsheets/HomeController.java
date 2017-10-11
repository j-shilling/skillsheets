package com.shilling.skillsheets;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
	
	private final GoogleUserFactory users;
	
	@Autowired
	private HomeController (GoogleUserFactory users) {
		this.users = users;
	}
	
	/** Return index.jsp */
	@RequestMapping("/")
	public String index(ModelMap model) {
		return "index";
	}
	
	/** Validate user and return home.jsp on success */
	@RequestMapping(value = "/home", 
			method = RequestMethod.POST,
			consumes = {"application/json"})
	public String home(@RequestBody TokenId tokenid, ModelMap model) {
		
		Optional<User> user = this.users.getUser(tokenid);
		
		if (user.isPresent()) {
			model.put("name", user.get().getName());
			model.put("id", user.get().getId());
			
			return "home";
		} else {
			return "index";
		}
	}

}
