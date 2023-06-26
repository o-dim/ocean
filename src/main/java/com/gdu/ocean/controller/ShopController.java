package com.gdu.ocean.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
	
	
	@GetMapping("/getCartDetailList.do") //shopservice를 컨트롤러에서 반환하고 있기 때문에 service에서 getCartDetailList는 void 타입으로 해도 좋다 
	public String getCartDetailList(@RequestParam("cartNo") int cartNo
								  ,	Model model) {
		shopService.getCartDetailList(cartNo, model);
		return "shop/cart";
	}
	
	@ResponseBody
	@GetMapping(value="/getHashtagName.do", produces="application/json")
	public List<HashtagDTO> getHashtagName(@RequestParam("cdNo") int cdNo) {
		return shopService.getHashtagName(cdNo);
	}
	
	@ResponseBody
	@PostMapping(value="/addCartDetail.do", produces="application/json")
	public Map<String, Object> addCartDetail(HttpServletRequest request) {
		return shopService.addCartDetail(request);
	}
	
	@GetMapping("/idol.html")
	public String idolList() {
		return "shop/idol";
	}
	
	@GetMapping("/preOrder.html")
	public String preOrder() {
		return "shop/preOrder";
	}


}
