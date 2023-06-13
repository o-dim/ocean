package com.gdu.ocean.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.gdu.ocean.service.ShopService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/shop")
@Controller
public class ShopController {

	private final ShopService shopService;
	
	
	@GetMapping("/list.do")
	public String shopList(HttpServletRequest request, Model model) {
		shopService.newList(request, model);
		return "shop/list";
	}
	
	@GetMapping("/listDetail.do")
	public String detail(@RequestParam(value="cdNo",required=false, defaultValue="0") int cdNo
						,Model model) {
		shopService.getCdByNo(cdNo, model);
		//shopService.getCartCount(cdNo);
		return "shop/listDetail";
	}
	
	
	

}
