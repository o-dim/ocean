package com.gdu.ocean.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MvcController {

	@GetMapping({"/", "/index.html"})
	public String welcome() {
		return "index"; // src/main/resources/templates/index.html
	}
	
	@GetMapping("/cartLogin.html")
	   public String cartGotoLogin() {
	      return "users/login";
	   }
	}

	

