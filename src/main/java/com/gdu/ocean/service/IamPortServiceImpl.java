package com.gdu.ocean.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.gdu.ocean.domain.CartDTO;
import com.gdu.ocean.domain.CartDetailDTO;
import com.gdu.ocean.domain.OrderDTO;
import com.gdu.ocean.mapper.ShopMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class IamPortServiceImpl implements IamPortService {
	
	private ShopMapper shopMapper;
	
	@Value("${iamport.imp_key}")
	private String impKey;
	
	@Value("${iamport.imp_secret}")
	private String impSecret;
	
	@ToString
	@Getter
	private class Response {
		private PaymentInfo response;
	}
	
	@ToString
	@Getter
	private class PaymentInfo{
		private int amount;
	}
	
	@Override
	public String getToken() throws IOException {
		log.info("service 도착................");
		HttpsURLConnection conn = null;

	    URL url = new URL("https://api.iamport.kr/users/getToken");

	    conn = (HttpsURLConnection) url.openConnection();

	    conn.setRequestMethod("POST");
	    conn.setRequestProperty("Content-type", "application/json");
	    conn.setRequestProperty("Accept", "application/json");
	    conn.setDoOutput(true);
	    JsonObject json = new JsonObject();

	    json.addProperty("imp_key", impKey);
	    json.addProperty("imp_secret", impSecret);
	    log.info("imp_key............:" + impKey);
	    log.info("imp_secret............:" + impSecret);

	    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));

	    bw.write(json.toString());
	    bw.flush();
	    bw.close();

	    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));

	    Gson gson = new Gson();

	    String response = gson.fromJson(br.readLine(), Map.class).get("response").toString();

	    System.out.println("response : " + response);

	    String token = gson.fromJson(response, Map.class).get("access_token").toString();

	    br.close();
	    conn.disconnect();
	    
		return token;

	}
	@Override
	public int paymentInfo(HttpServletRequest request, String imp_uid, String access_token) throws IOException {
		HttpsURLConnection conn = null;
		 
	    URL url = new URL("https://api.iamport.kr/payments/" + imp_uid);
	 
	    conn = (HttpsURLConnection) url.openConnection();
	 
	    conn.setRequestMethod("GET");
	    conn.setRequestProperty("Authorization", access_token);
	    conn.setDoOutput(true);
	 
	    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
	    
	    Gson gson = new Gson();
	    
	    Response response = gson.fromJson(br.readLine(), Response.class);
	    
	    br.close();
	    conn.disconnect();
	    
	 // session을 이용해서 카트 넘버를 가져오세용
		HttpSession session = request.getSession();
		CartDTO cartDTO = shopMapper.getCartByUserNo(Integer.parseInt(String.valueOf(session.getAttribute("loginUsersNo"))));
		int cartNo = cartDTO.getCartNo();
		OrderDTO orderDTO = new OrderDTO();
		List<CartDetailDTO> carts = shopMapper.getCartDetailList(cartNo);
		for (CartDetailDTO cart : carts) { //스트레이 3개 : cart , 레드벨벳 2개 : cart => 모두 가지고 있는 게 carts 
			int count = cart.getCount(); // 3개
			orderDTO.setCdNo(cart.getCdNo()); // 스트레이(cdDTO) 빼자마자 order에다가 넣음
			orderDTO.setCount(count); // order에다가 3개 넣음
		}
		// 현재 상황 : cart 스트레이 3개가 있고, order에도 스트레이 3개가 있음 (근데 db order에 있는게 아님 단순히 자바 order에만 존재하고 있음)
		shopMapper.addOrder(orderDTO); // db에다가 order insert문 돌려서 db 저장함 (스트레이 3 저장됨) : 현재상황 오더db에 스3존재 및 카트db에 스3존재 
		shopMapper.deleteCart(cartNo); // db에다가 cart delete함(스트레이 3 삭제됨)
		// 최종 상황 : cartDB 깨끗 및 orderDB에 스 3 저장 (레벨도 이하동문)
	    
	    return response.getResponse().getAmount();
	}
	
	@Override
	public void payMentCancel(String access_token, String imp_uid, String amount, String reason) throws IOException  {
		log.info("결제 취소");
		
		log.info("acess_token............" + access_token);
		
		log.info("imp_uid.........." + imp_uid);
		
		HttpsURLConnection conn = null;
		URL url = new URL("https://api.iamport.kr/payments/cancel");
 
		conn = (HttpsURLConnection) url.openConnection();
 
		conn.setRequestMethod("POST");
 
		conn.setRequestProperty("Content-type", "application/json");
		conn.setRequestProperty("Accept", "application/json");
		conn.setRequestProperty("Authorization", access_token);
 
		conn.setDoOutput(true);
		
		JsonObject json = new JsonObject();
 
		json.addProperty("reason", reason);
		json.addProperty("imp_uid", imp_uid);
		json.addProperty("amount", amount);
		json.addProperty("checksum", amount);
 
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
 
		bw.write(json.toString());
		bw.flush();
		bw.close();
		
		BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
 
		br.close();
		conn.disconnect();
		
	}
}
