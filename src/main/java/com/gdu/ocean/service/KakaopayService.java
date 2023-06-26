<<<<<<< HEAD
package com.gdu.ocean.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.catalina.manager.util.SessionUtils;
import org.apache.hc.core5.http.impl.bootstrap.HttpServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.gdu.ocean.domain.CartDTO;
import com.gdu.ocean.domain.CartDetailDTO;
import com.gdu.ocean.domain.CdDTO;
import com.gdu.ocean.domain.KakaoApproveResponse;
import com.gdu.ocean.domain.KakaoReadyResponse;
import com.gdu.ocean.domain.OrderDTO;
import com.gdu.ocean.mapper.ShopMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class KakaopayService {
	// private final CartMapper cartMapper;
	private KakaoReadyResponse kakaoReadyResponse;
	private ShopMapper shopMapper1;
	/*
	 * 결제요청시도
	 */
	public KakaoReadyResponse kakaoPayReady() {
//		UsersDTO user = (UsersDTO)SessionUtils.getattribute("로그인아이디설정한거");
//		List<CartDTO> carts = cartMapper..getCartByUserEmail(user.getEmail(); // 암튼 이런이름으로 있겠지...
//		
//		/*
//		 * 카트 물건 하나씩 뽑기 -> 밑에거는 물건 없이도 돌아가는거 이거는 물건있게 돌아가는건데 한번 생각해보기 
//		 * */
//		String[] cartNames = new String[carts.size()];
//		for (CartDTO cart : carts) {
//			for (int i = 0; i < carts.size(); i++) {
//				cartNames[i] = cart.getClassTitle();
//			}
//		}
//		String itemName = cartNames[0];
		String itemName = "oceanCD";
		String orderId = "3333333";
		int total = 68000;
		log.info("서비스 금액............" + total);
		/*
		 * 카카오 페이가 요구하는 결제요청 request 담기
		 * */
		MultiValueMap<String, String> parameters = new LinkedMultiValueMap<String, String>();
		parameters.add("cid", "TC0ONETIME");
		parameters.add("partner_order_id", orderId);
		parameters.add("partner_user_id", "ocean");
		parameters.add("item_name", itemName); 
		parameters.add("quantity", "1"); // String.valueOf(carts.size()) 총개수  
		parameters.add("total_amount", String.valueOf(total)); // int total 받을 시에는 total대신 String.valueOf(total)
		parameters.add("tax_free_amount", "0");
		parameters.add("approval_url", "http://quddls6.cafe24.com/order/payCompleted"); // 결제승인시 넘어갈 url
		parameters.add("cancel_url", "http://quddls6.cafe24.com/order/kakaopayCancel"); // 결제취소시 넘어갈 url
		parameters.add("fail_url", "http://quddls6.cafe24.com/order/kakaopayFail"); // 결제 실패시 넘어갈 url
		log.info("파트너 주문 아이디 : " + parameters.get("partner_order_id"));

		HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeader());
		log.info("requestEntity까지는 성공.............");
		/*
		 * 외부 url 요청 통로 열기
		 * */
		RestTemplate template = new RestTemplate();
		String url = "https://kapi.kakao.com/v1/payment/ready";
		/*
		 * template으로 값을 보내고 받아온 ReadyResponse값 readyResponse에 저장.
		 */
		KakaoReadyResponse readyResponse = template.postForObject(url, requestEntity, KakaoReadyResponse.class);
		log.info("결제준비 응답객체: " + readyResponse);
		log.info("결제준비 tid : " + readyResponse.getTid());
		log.info("결제준비 리다이렉트 경로 : " + readyResponse.getNext_redirect_pc_url());
		return readyResponse;
	}

   
   /* 
    * 결제 승인요청
    */
   public KakaoApproveResponse kakaoPayApprove(String tid, String pgToken) {
	   
	  log.info(pgToken + "........service 승인요청 pgToken");
	  log.info(tid + "........service 승인요청 tid");
      // 장바구니 dto 뽑아오든지 언니가 지정해주면 됨.
      String itemName = "oceanCD";
      String orderId = "3333333";
      // request값 담기.
      MultiValueMap<String, String> parameters = new LinkedMultiValueMap<String, String>();
      parameters.add("cid", "TC0ONETIME");
      parameters.add("tid", tid);
      parameters.add("partner_order_id", orderId); // 주문명
      parameters.add("partner_user_id", "ocean");//회사
      parameters.add("pg_token", pgToken);
      
      // parameter값과 header 담기 건드릴 필요없음
      HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeader());
      RestTemplate template = new RestTemplate();

     String url = "https://kapi.kakao.com/v1/payment/approve";
     KakaoApproveResponse approveResponse = template.postForObject(url, requestEntity, KakaoApproveResponse.class);
         log.info("결제승인 응답객체: " + approveResponse); 
         return approveResponse;

   }
   
   /*
    * header 건드릴 필요 x 
    */
   @Value("${kakaopay.Authorization}")
   private String auth;
   
   private HttpHeaders getHeader() {
      log.info("가져오기 : ........... " + auth);
      HttpHeaders headers = new HttpHeaders();
      headers.set("Authorization", auth);
      headers.set("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
      
      return headers;
   }

}
=======
package com.gdu.ocean.service;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.gdu.ocean.domain.KakaoApproveResponse;
import com.gdu.ocean.domain.KakaoReadyResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class KakaopayService {
	/*
	 * 결제요청시도
	 */
	public KakaoReadyResponse kakaoPayReady() {

		String itemName = "oceanCD";
		String orderId = "3333333";
		int total = 68000;
		log.info("서비스 금액............" + total);
		
		/*
		 * 카카오 페이가 요구하는 결제요청 request 담기
		 * */
		MultiValueMap<String, String> parameters = new LinkedMultiValueMap<String, String>();
		parameters.add("cid", "TC0ONETIME");
		parameters.add("partner_order_id", orderId);
		parameters.add("partner_user_id", "ocean");
		parameters.add("item_name", itemName); 
		parameters.add("quantity", "1"); // String.valueOf(carts.size()) 총개수  
		parameters.add("total_amount", String.valueOf(total)); // int total 받을 시에는 total대신 String.valueOf(total)
		parameters.add("tax_free_amount", "0");
		parameters.add("approval_url", "http://quddls6.cafe24.com/order/payCompleted"); // 결제승인시 넘어갈 url
		parameters.add("cancel_url", "http://quddls6.cafe24.com/order/kakaopayCancel"); // 결제취소시 넘어갈 url
		parameters.add("fail_url", "http://quddls6.cafe24.com/order/kakaopayFail"); // 결제 실패시 넘어갈 url
		log.info("파트너 주문 아이디 : " + parameters.get("partner_order_id"));

		HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeader());
		log.info("requestEntity까지는 성공.............");
		/*
		 * 외부 url 요청 통로 열기
		 * */
		RestTemplate template = new RestTemplate();
		String url = "https://kapi.kakao.com/v1/payment/ready";
		/*
		 * template으로 값을 보내고 받아온 ReadyResponse값 readyResponse에 저장.
		 */
		KakaoReadyResponse readyResponse = template.postForObject(url, requestEntity, KakaoReadyResponse.class);
		log.info("결제준비 응답객체: " + readyResponse);
		log.info("결제준비 tid : " + readyResponse.getTid());
		log.info("결제준비 리다이렉트 경로 : " + readyResponse.getNext_redirect_pc_url());
		return readyResponse;
	}

   
   /* 
    * 결제 승인요청
    */
   public KakaoApproveResponse kakaoPayApprove(String tid, String pgToken) {
	   
	  log.info(pgToken + "........service 승인요청 pgToken");
	  log.info(tid + "........service 승인요청 tid");

	  String itemName = "oceanCD";
      String orderId = "3333333";

      MultiValueMap<String, String> parameters = new LinkedMultiValueMap<String, String>();
      parameters.add("cid", "TC0ONETIME");
      parameters.add("tid", tid);
      parameters.add("partner_order_id", orderId); // 주문명
      parameters.add("partner_user_id", "ocean");//회사
      parameters.add("pg_token", pgToken);
      
      // parameter값과 header 담기 건드릴 필요없음
      HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeader());
      RestTemplate template = new RestTemplate();

     String url = "https://kapi.kakao.com/v1/payment/approve";
     KakaoApproveResponse approveResponse = template.postForObject(url, requestEntity, KakaoApproveResponse.class);
         log.info("결제승인 응답객체: " + approveResponse); 
         return approveResponse;

   }
   
   /*
    * header 건드릴 필요 x 
    */
   @Value("${kakaopay.Authorization}")
   private String auth;
   
   private HttpHeaders getHeader() {
      log.info("가져오기 : ........... " + auth);
      HttpHeaders headers = new HttpHeaders();
      headers.set("Authorization", auth);
      headers.set("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
      
      return headers;
   }

}
>>>>>>> fa6e8411a6c889010baf48b55e573e4559e4810a
