package com.gdu.ocean.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.gdu.ocean.domain.KakaoApproveResponse;
import com.gdu.ocean.domain.KakaoReadyResponse;
import com.gdu.ocean.service.KakaopayService;
import com.gdu.ocean.service.ShopService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@SessionAttributes({"tid", "order"})
@RequiredArgsConstructor
public class KakaoPayController {
	
	private final KakaopayService kakaoPayService;
	private final ShopService shopService;
	
	
	@GetMapping("/order/order")
	public String order(int cartNo, int userNo, Model model) {
		shopService.getCartDetailList(cartNo, model);
		//model.addAttribute("total", total); < int total 매개변수로 넣어야함
		return "/order/order";
	}

	@PostMapping(value="/order/ready" ,produces="application/json")
	@ResponseBody
	public Map<String, Object> kakaoPayReady(HttpSession session) {
		log.info("kakaopayReady 성공..............");
		// log.info("total금액.................." + total);
		KakaoReadyResponse readyResponse = kakaoPayService.kakaoPayReady();
		session.setAttribute("tid", readyResponse.getTid());
		log.info("kakaopayReady tid : " + readyResponse.getTid());
		//log.info(".........주문가격 : "+totalAmount);
		
		Map<String, Object> map = new HashMap<>();
		
		map.put("url", readyResponse.getNext_redirect_pc_url());
		
		return map; //만약 성공시 qr코드가 뜬다 
	}
	
	
	/*
	 * 결제 승인 요청 큐알 찎을시 내가 돌아가 
	 */
	@GetMapping("/order/payCompleted")
	public String kakaoPayCompleted(@RequestParam("pg_token") String pgToken, HttpSession session) {
		
		log.info("kakaopayCompleted 성공..............");
		log.info("KakapayCompleted pgToken : " + pgToken + "..............");
		String tid = (String) session.getAttribute("tid");
		// 카카오 결제 요청
		KakaoApproveResponse approveResponse = kakaoPayService.kakaoPayApprove(tid, pgToken);
		
		log.info("KakapayCompleted찐찐성공 : " + approveResponse);
		// 성공한 카트애들을 ORDER 테이블로 이동하기
		
		return "redirect:/order/kakaopayCompleted";
	}
	
	@GetMapping("/order/kakaopayCompleted")
	public String completedPage() {
		return "/order/kakaopayCompleted";
	}
	
	/*
	 * 	결제 취소
	 */
	@GetMapping("/order/kakaopayCancel")
	public String kakaoPayCancel() {
		log.info("kakaopayCancel..............");
		return "/order/kakaopayCancel";
	}
	
	/*
	 * 결제 실패
	 */
	@GetMapping("/order/kakaopayfail")
	public String kakaoPayFail() {
		log.info("kakaopayFail..............");
		return "redirect:/order/kakaopayFail";
	}
	
   
}
