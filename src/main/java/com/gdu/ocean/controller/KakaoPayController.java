package com.gdu.ocean.controller;

import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
import com.gdu.ocean.domain.OrderDTO;
import com.gdu.ocean.mapper.ShopMapper;
import com.gdu.ocean.service.KakaopayService;
import com.gdu.ocean.service.ShopService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import retrofit2.http.GET;

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
		return "/order/order";
	}
	/*
	@PostMapping("/getBuyCdList.do")
	public String buy(@RequestParam("userNo") int userNo
				 	, @RequestParam("cdNo")   int cdNo
					, @RequestParam("count")  int count
					, Model model) {
		shopService.
		return "shop"
	}	  
	 */
	
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

	@PostMapping("/order/ready")
	@ResponseBody
	public String kakaoPayReady(HttpSession session) {
		log.info("kakaopayReady 성공..............");
		
		KakaoReadyResponse readyResponse = kakaoPayService.kakaoPayReady();
		session.setAttribute("tid", readyResponse.getTid());
		log.info("kakaopayReady tid : " + readyResponse.getTid());
		//log.info(".........주문가격 : "+totalAmount);
		return readyResponse.getNext_redirect_pc_url();//만약 성공시 qr코드가 뜬다 
	}
	
	/*
	@PostMapping("/semiOrder/pay")
	@ResponseBody
	public String kakaoPayReady(Model model, HttpSession session, HttpServletResponse response) {
		log.info("kakaopayReady 성공..............");
		KakaoReadyResponse readyResponse = kakaoPayService.kakaoPayReady();
		session.setAttribute("tid", readyResponse.getTid());
		log.info("kakaopayReady tid : " + readyResponse.getTid());
		try {
			PrintWriter out = response.getWriter();
			out.println("<a href='" + readyResponse.getNext_redirect_pc_url() + "'>go for pay</a>");
		} catch (Exception e) {
			e.printStackTrace();
		}
		//log.info(".........주문가격 : "+totalAmount);
		return null;
	}
	*/
	 
	/*
	 * 결제 승인 요청 큐알 찎을시 내가 돌아가 
	 */
	@GetMapping("/order/payCompleted")
	public String kakaoPayCompleted(@RequestParam("pg_token") String pgToken, @RequestParam("order_id") String orderId, HttpSession session) {
		
		log.info("kakaopayCompleted 성공..............");
		log.info("KakapayCompleted pgToken : " + pgToken + "..............");
		String tid = (String) session.getAttribute("tid");
		// 카카오 결제 요청
		KakaoApproveResponse approveResponse = kakaoPayService.kakaoPayApprove(tid, orderId, pgToken);
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
