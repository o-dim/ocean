//package com.gdu.ocean.controller;
//
//import org.apache.catalina.servlets.DefaultServlet.SortManager.Order;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.gdu.ocean.domain.KakaoApproveResponse;
//import com.gdu.ocean.domain.KakaoReadyResponse;
//import com.gdu.ocean.domain.Payment;
//import com.gdu.ocean.exception.BusinessLogicException;
//import com.gdu.ocean.exception.ExceptionCode;
//import com.gdu.ocean.service.KakaoPayService;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//
//@Slf4j
//@RestController
//@RequestMapping("/kakaopay")
//@RequiredArgsConstructor
//public class KakaoPayController {
//	private final KakaoPayService kakaoPayService;
//	
//	/**
//     * 결제요청
//     */
//    @PostMapping("/order/pay")
//    public KakaoReadyResponse readyToKakaoPay() {
//        return kakaoPayService.kakaoPayReady();
//    }
//
//    /**
//     * 결제 성공
//     */
//    @GetMapping("/order/pay/completed")
//    public ResponseEntity afterPayRequest(@RequestParam("pg_token") String pgToken, @ModelAttribute("order") Order order,  Model model) {
//
//        KakaoApproveResponse kakaoApprove = kakaoPayService.ApproveResponse(pgToken);
//
//        return new ResponseEntity<>(kakaoApprove, HttpStatus.OK);
//    }
//    // 카카오페이결제 요청
// 	@GetMapping("/order/pay")
// 	public @ResponseBody KakaoReadyResponse payReady(@RequestParam(name = "total_amount") int totalAmount, Order order, Model model) {
// 		
// 		log.info("주문정보:"+order);
// 		log.info("주문가격:"+totalAmount);
// 		// 카카오 결제 준비하기	- 결제요청 service 실행.
// 		KakaoReadyResponse readyResponse = kakaoPayService.kakaoPayReady(totalAmount);
// 		// 요청처리후 받아온 결재고유 번호(tid)를 모델에 저장
// 		model.addAttribute("tid", readyResponse.getTid());
// 		log.info("결재고유 번호: " + readyResponse.getTid());		
// 		// Order정보를 모델에 저장
// 		model.addAttribute("order",order);
// 		
// 		return readyResponse; // 클라이언트에 보냄.(tid,next_redirect_pc_url이 담겨있음.)
// 	}
//
// // 결제승인요청
// 	@GetMapping("/order/pay/completed")
// 	public String payCompleted(@RequestParam("pg_token") String pgToken, @ModelAttribute("tid") String tid, @ModelAttribute("order") Order order,  Model model) {
// 		
// 		log.info("결제승인 요청을 인증하는 토큰: " + pgToken);
// 		log.info("주문정보: " + order);		
// 		log.info("결재고유 번호: " + tid);
// 		
// 		// 카카오 결재 요청하기
// 		KakaoApproveResponse approveResponse = kakaoPayService.ApproveResponse(tid, pgToken);	
// 		
// 		// 5. payment 저장
// 		//	orderNo, payMathod, 주문명.
// 		// - 카카오 페이로 넘겨받은 결재정보값을 저장.
// 		Payment payment = Payment.builder() 
// 				.paymentClassName(approveResponse.getItem_name())
// 				.payMathod(approveResponse.getPayment_method_type())
// 				.payCode(tid)
// 				.build();
// 		
// 		orderService.saveOrder(order,payment);
// 		
// 		return "redirect:/orders";
// 	}
//    /**
//     * 결제 진행 중 취소
//     */
//    @GetMapping("/cancel")
//    public void cancel() {
//
//        throw new BusinessLogicException(ExceptionCode.PAY_CANCEL);
//    }
//
//    /**
//     * 결제 실패
//     */
//    @GetMapping("/fail")
//    public void fail() {
//
//        throw new BusinessLogicException(ExceptionCode.PAY_FAILED);
//    }
//	
//}
