package com.gdu.ocean.service;

import java.util.List;

import org.apache.catalina.User;
import org.apache.catalina.manager.util.SessionUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.gdu.ocean.domain.CartDTO;
import com.gdu.ocean.domain.KakaoApproveResponse;
import com.gdu.ocean.domain.KakaoReadyResponse;
import com.gdu.ocean.domain.UsersDTO;

import lombok.RequiredArgsConstructor;

//@Service
//@RequiredArgsConstructor
//@Transactional
//public class KakaoPayService {
//	static final String cid = "TC0ONETIME"; // 가맹점 테스트 코드
//    static final String admin_Key = "${ADMIN_KEY}"; // 공개 조심! 본인 애플리케이션의 어드민 키를 넣어주세요
//    private KakaoReadyResponse kakaoReady;
//    // cartMapper 불러오기
//    
//    
//    // totalAmount 장바구니에서 계산해서 넘겨야함
//    public KakaoReadyResponse kakaoPayReady(int totalAmount) {
//    	// id 불러오기
//    	// User user =  (User)SessionUtils.getAttribute("loginID");
//    	
//    	// 카트 물건 하나씩 담아오기
//    	// userNo로 카트 불러오기
//    	// List<CartDTO> carts = cartMapper.getCartByUserNo(user.getNo());
//    	
//    	// 카트 물건 하나씩 빼서 담기
////    	String[] cartNames = new String[carts.size()];
////    	for(CartDTO cart : carts) {
////    		for (int i = 0; i < carts.size; i++) {
////				cartNames[i] = cart.getClassTitle();
////			}
////    	}
//    	// 빼놓은 카트 물건들 log로 찍기 + orderID 만들기
////    	String title = cartNames[0] + " 그외" + (carts.size()-1);
////		log.info("앨범 이름:"+title);
////		String order_id = user.getNo() + title;
//		
//    	// 카카오페이 요청 양식
//        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
//        parameters.add("cid", "TC0ONETIME");
//        parameters.add("partner_order_id", "order_id");
//        parameters.add("partner_user_id", "ocean");
//        parameters.add("item_name", "title");
//        parameters.add("quantity", "String.valueOf(cart.size())");
//        parameters.add("total_amount", "String.valueOf(totalAmount");
//        parameters.add("vat_amount", "부가세"); // 0 처리 
//        parameters.add("tax_free_amount", "상품 비과세 금액"); // 0 원 
//        parameters.add("approval_url", "http://localhost:8080/payment/success"); // 성공 시 redirect url
//        parameters.add("cancel_url", "http://localhost:8080/payment/cancel"); // 취소 시 redirect url
//        parameters.add("fail_url", "http://localhost:8080/payment/fail"); // 실패 시 redirect url
//        
//        // 파라미터, 헤더
//        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());
//        
//        // 외부에 보낼 url
//        RestTemplate restTemplate = new RestTemplate();
//
//        kakaoReady = restTemplate.postForObject(
//                "https://kapi.kakao.com/v1/payment/ready",
//                requestEntity,
//                KakaoReadyResponse.class);
//                
//        return kakaoReady;
//    }
//    
//    /**
//     * 결제 완료 승인
//     */
//    public KakaoApproveResponse ApproveResponse(String tid, String pgToken) {
//    	// userID
//    	// User user =  (User)SessionUtils.getAttribute("LOGIN_USER");
//    	// 카트
//    	// 카트 물건 하나씩 담아오기
//    	// userNo로 카트 불러오기
//    	// List<CartDTO> carts = cartMapper.getCartByUserNo(user.getNo());
//    	
//    	// 카트 물건 하나씩 빼서 담기
////    	String[] cartNames = new String[carts.size()];
////    	for(CartDTO cart : carts) {
////    		for (int i = 0; i < carts.size; i++) {
////				cartNames[i] = cart.getClassTitle();
////			}
////    	}
//    	// 빼놓은 카트 물건들 log로 찍기 + orderID 만들기
////    	String title = cartNames[0] + " 그외" + (carts.size()-1);
////		log.info("앨범 이름:"+title);
////		String order_id = user.getNo() + title;
//    	
//    	User user =  (User)SessionUtils.getAttribute("LOGIN_USER");
//		List<CartDto> carts = cartMapper.getCartByUserNo(user.getNo());
//		// 주문명 만들기.
//		String[] cartNames = new String[carts.size()];
//		for(CartDTO cart: carts) {
//			for(int i=0; i< carts.size(); i++) {
//				cartNames[i] = cart.getClassTitle();
//			}
//		}	
//		String itemName = cartNames[0] + " 그외" + (carts.size()-1);
//		
//		// orderID 지정필요
//		String order_id =  /* 랜덤숫자나 아이디 관련 숫자 + */ itemName;
//		
//		// request값 담기.
//		MultiValueMap<String, String> parameters = new LinkedMultiValueMap<String, String>();
//		parameters.add("cid", "TC0ONETIME");
//		parameters.add("tid", tid);
//		parameters.add("partner_order_id", order_id); // 주문명
//		parameters.add("partner_user_id", "ocean");
//		parameters.add("pg_token", pgToken);
//		
//        // 하나의 map안에 header와 parameter값을 담아줌.
//		HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());
//		
//        // 외부url 통신
//		RestTemplate template = new RestTemplate();
//		String url = "https://kapi.kakao.com/v1/payment/approve";
//        // 보낼 외부 url, 요청 메시지(header,parameter), 처리후 값을 받아올 클래스. 
//		KakaoApproveResponse approveResponse = template.postForObject(url, requestEntity, KakaoApproveResponse.class);
//		log.info("결재승인 응답객체: " + approveResponse);
//		
//		return approveResponse;
//    }
//    /**
//     * 카카오 요구 헤더값
//     */
//    private HttpHeaders getHeaders() {
//        HttpHeaders httpHeaders = new HttpHeaders();
//
//        String auth = "KakaoAK " + admin_Key;
//
//        httpHeaders.set("Authorization", auth);
//        httpHeaders.set("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
//
//        return httpHeaders;
//    }
//    
//    
//}
