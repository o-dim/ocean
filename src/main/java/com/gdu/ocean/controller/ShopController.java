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
	public String ShopList(HttpServletRequest request, Model model) {
		shopService.getCdList(request, model);
		return "shop/list";
	}
	
	@GetMapping("/detail.do")
	public String Detail(@RequestParam(value="cdNo", required=false, defaultValue="0") int cdNo, Model model) {
		shopService.getCdByNo(cdNo, model);
		return "shop/detail";
	}
	
	@GetMapping("/cart.html")
	public String CartList(HttpServletRequest request, Model model) {
		shopService.getcartList(request, model);
		return "shop/cart";
	}
	
	@GetMapping("/cart.do")
	public String CartNo(@RequestParam(value="cartNo", required=false, defaultValue="0") int cartNo, 
						 @RequestParam(value="cdNo", required=false, defaultValue="0") int cdNo,
						 Model model) {
		shopService.getCartListFK(cartNo, cdNo, model);
		return "shop/cart";
	}
	

	 @GetMapping("/my-page")
	    public String myPage(Model model) {
	        model.addAttribute("photoId1", "S3eqr293Vho");
	        model.addAttribute("photoId2", "aIYFR0vbADk");
	        model.addAttribute("photoId3", "sfze-8LfCXI");
	        return "my-page";
	    }
	
	@GetMapping("/idol.html")
	public String idolList() {
		return "shop/idol";
	}
	
	
	
	

}
