package com.gdu.ocean.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

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
	private ShopMapper shopMapper;
	/*
	 * 결제요청시도
	 */
	public KakaoReadyResponse kakaoPayReady(String orderId, int totalAmount) {
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
		String itemName = "";
		
		/*
		 * 카카오 페이가 요구하는 결제요청 request 담기
		 * */
		MultiValueMap<String, String> parameters = new LinkedMultiValueMap<String, String>();
		parameters.add("cid", "TC0ONETIME");
		parameters.add("partner_order_id", orderId);
		parameters.add("partner_user_id", "ocean");
		parameters.add("item_name", itemName); 
		parameters.add("quantity", "1"); // String.valueOf(carts.size()) 총개수  
		parameters.add("total_amount", "" + totalAmount);
		parameters.add("tax_free_amount", "0");
		parameters.add("approval_url", "http://localhost:8080/order/payCompleted"); // 결제승인시 넘어갈 url
		parameters.add("cancel_url", "http://localhost:8080/order/kakaopayCancel"); // 결제취소시 넘어갈 url
		parameters.add("fail_url", "http://localhost:8080/order/kakaopayFail"); // 결제 실패시 넘어갈 url
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
	public KakaoApproveResponse kakaoPayApprove(String tid, String orderId, String pgToken) {
		// 장바구니 dto 뽑아오든지 언니가 지정해주면 됨.
		String itemName = "소민이네 카카오페이^.^";
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
	// 결제 완료된 물건들을 ORDER 테이블에 저장하고, CART 테이블에 저장된 결제 완료 물건들을 삭제하는 메소드 
	public void kakaopaySaveOrder() {
		// session을 이용해서 카트 넘버를 가져오세용
		int cartNo = session
		OrderDTO orderDTO = new OrderDTO();
		List<CartDetailDTO> carts = shopMapper.getCartDetailNo(cartNo);
		for (CartDetailDTO cart : carts) { //스트레이 3개 : cart , 레드벨벳 2개 : cart => 모두 가지고 있는 게 carts 
			int count = cart.getCount(); // 3개
			orderDTO.setCdNo(cart.getCdNo()); // 스트레이(cdDTO) 빼자마자 order에다가 넣음
			orderDTO.setCount(count); // order에다가 3개 넣음
		}
		// 현재 상황 : cart 스트레이 3개가 있고, order에도 스트레이 3개가 있음 (근데 db order에 있는게 아님 단순히 자바 order에만 존재하고 있음)
		shopMapper.addOrder(orderDTO); // db에다가 order insert문 돌려서 db 저장함 (스트레이 3 저장됨) : 현재상황 오더db에 스3존재 및 카트db에 스3존재 
		shopMapper.deleteCart(cartNo); // db에다가 cart delete함(스트레이 3 삭제됨)
		// 최종 상황 : cartDB 깨끗 및 orderDB에 스 3 저장 (레벨도 이하동문)
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
