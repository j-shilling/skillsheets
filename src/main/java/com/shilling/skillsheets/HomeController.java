package com.shilling.skillsheets;

import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HomeController {
	
	@RequestMapping("/")
	public String index(ModelMap model) {
		return "index";
	}
	
	@RequestMapping(value = "/home", 
			method = RequestMethod.POST,
			consumes = {"application/json"})
	public String home(@RequestBody TokenId tokenid, ModelMap model) {
		
		Optional<User> user = new GoogleUserFactory().getUser(tokenid);
		
		if (user.isPresent()) {
			model.put("name", user.get().getName());
			model.put("id", user.get().getId());
		}
		
		return "home";
	}

}
