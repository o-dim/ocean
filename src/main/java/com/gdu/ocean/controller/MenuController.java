package com.gdu.ocean.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MenuController {
	
	@GetMapping("/home.html")
	public String home() {
		return "/index.html";
	}
	
}
