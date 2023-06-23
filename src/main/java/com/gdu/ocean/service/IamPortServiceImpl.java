package com.gdu.ocean.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class IamPortServiceImpl implements IamPortService {
	
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
	public int paymentInfo(String imp_uid, String access_token) throws IOException {
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
