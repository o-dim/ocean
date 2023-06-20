package com.gdu.ocean.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/shop")
public class IdolController {
	
	@GetMapping("/aespa")
	public String aespa() {
		return "/shop/aespa";
	}
	@GetMapping("/blackpink")
	public String blackpink() {
		return "/shop/blackpink";
	}
	@GetMapping("/ive")
	public String ive() {
		return "/shop/ive";
	}
	@GetMapping("/bts")
	public String bts() {
		return "/shop/bts";
	}
	@GetMapping("/theboys")
	public String theboys() {
		return "/shop/theboys";
	}
	@GetMapping("/txt")
	public String txt() {
		return "/shop/txt";
	}
}
