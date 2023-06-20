package com.gdu.ocean.controller;

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

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@SessionAttributes({"tid", "order"})
@RequiredArgsConstructor
public class KakaoPayController {
	
	private final KakaopayService kakaoPayService;
	
	@GetMapping("/semiOrder/order")
	public String order() {
		return "/semiOrder/order";
	}
	
	/*
	@PostMapping("/semiOrder/pay")
	@ResponseBody
	public KakaoReadyResponse kakaoPayReady() {
		log.info("kakaopayReady 성공..............");
		//log.info(".........주문가격 : "+totalAmount);
		KakaoReadyResponse readyResponse = kakaoPayService.kakaoPayReady();
		// 결제고유번호인 tid 모델에 저장
		// model.addAttribute("tid", readyResponse.getTid());
		log.info("결제 고유번호 tid : " + readyResponse.getTid());
		log.info("결제요청 리다이렉트: " + readyResponse.getNext_redirect_pc_url());
		// order 정보 (매개변수에 Order order 넣어야함
		// model.addAttribute("order", order);
		return readyResponse;
	}
	*/
	
	 @PostMapping("/semiOrder/pay")
	 @ResponseBody
	public KakaoReadyResponse kakaoPayReady(Model model, HttpSession session) {
		log.info("kakaopayReady 성공..............");
		KakaoReadyResponse readyResponse = kakaoPayService.kakaoPayReady();
		session.setAttribute("tid", readyResponse.getTid());
		log.info("kakaopayReady tid : " + readyResponse.getTid());
		//log.info(".........주문가격 : "+totalAmount);
		return readyResponse;
	}

	 
	/*
	 * 결제 승인 요청
	 */
	@GetMapping("/semiOrder/payCompleted")
	public String kakaoPayCompleted(@RequestParam("pg_token") String pgToken, HttpSession session) {
		
		log.info("kakaopayCompleted 성공..............");
		log.info("KakapayCompleted pgToken : " + pgToken + "..............");
		String tid = (String) session.getAttribute("tid");
		// 카카오 결제 요청
		KakaoApproveResponse approveResponse = kakaoPayService.kakaoPayApprove(tid, pgToken);
		log.info("KakapayCompleted찐찐성공 : " + approveResponse);
		return "redirect:/semiOrder/kakaopayCompleted";
	}
	
	@GetMapping("/semiOrder/kakaopayCompleted")
	public String completedPage() {
		return "/semiOrder/kakaopayCompleted";
	}
	/*
	 * 	결제 취소
	 */
	@GetMapping("/semiOrder/pay/cancel")
	public String kakaoPayCancel() {
		log.info("kakaopayCancel..............");
		return "redirect:/shop/cart";
	}
	
	/*
	 * 결제 실패
	 */
	@GetMapping("/semiOrder/pay/fail")
	public String kakaoPayFail() {
		log.info("kakaopayFail..............");
		return "redirect:/semiOrder/kakaopayFail";
	}
	
}