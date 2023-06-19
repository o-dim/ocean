package com.gdu.ocean.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gdu.ocean.domain.CartDTO;
import com.gdu.ocean.domain.HashtagDTO;
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
	public String Detail(@RequestParam(value="cdNo", required=false, defaultValue="0") int cdNo, 
						Model model) {
		shopService.getCdByNo(cdNo, model);
		return "shop/detail";
	}
	
	//@GetMapping("/cart.html")
	public String CartList(HttpServletRequest request, Model model) {
		//shopService.getcartList(request, model);
		return "shop/cart";
	}
	
	@ResponseBody
	@GetMapping(value="/getcart.do", produces="application/json")
	public List<CartDTO> getCartNo(@RequestParam("cartNo") int cartNo) {
		return shopService.getCartNo(cartNo);
	}
	
	@GetMapping("/idol.html")
	public String idolList() {
		return "shop/idol";
	}
	
	@ResponseBody
	@GetMapping(value="/getHashtagName.do", produces="application/json")
	public List<HashtagDTO> getHashtagName(@RequestParam("cdNo") int cdNo) {
		return shopService.getHashtagName(cdNo);
	}
	
	@GetMapping("/preOrder.html")
	public String preOrder() {
		return "shop/preOrder";
	}
	
	@GetMapping("/cartDetail.html")
	public String cartDetail() {
		return "shop/cartDetail";
	}

}
