package com.gdu.ocean.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtil {

  // 크로스 사이트 스크립팅(Cross Site Scripting) 방지하기
  public String preventXSS(String str) {
    str = str.replace("<", "&lt;");
    str = str.replace(">", "&gt;");
    return str;
  }
  
  // 인증코드 반환하기
  public String getRandomString(int count, boolean letters, boolean numbers) {
    return RandomStringUtils.random(count, letters, numbers);
  }
  
  // SHA-256 암호화하기
  /*
    1. 입력 값을 256비트(32바이트) 암호화 처리하는 해시 알고리즘이다.
    2. 암호화는 가능하지만 복호화는 불가능한 알고리즘이다.
    3. 암호화된 결과를 저장하기 위한 32바이트 byte 배열이 필요하다.
    4. 1바이트 -> 16진수로 변환해서 암호화된 문자열을 만든다. (1바이트는 16진수 2개 문자로 변환된다.)
    5. 32바이트 -> 16진수로 변환하면 64글자가 생성된다. (DB 칼럼의 크기를 VARCHAR2(64 BYTE)로 설정한다.)
    6. java.security 패키지를 이용한다.
  */
  public String getSha256(String plainText) {
	  
	    StringBuffer hexSHA256hash = new StringBuffer();

	    try {
	        MessageDigest mdSHA256 = MessageDigest.getInstance("SHA-256");
	        mdSHA256.update(plainText.getBytes("UTF-8"));
	        byte[] sha256Hash = mdSHA256.digest();
	        for (byte b : sha256Hash) {
	            String hexString = String.format("%02x", b);
	            hexSHA256hash.append(hexString);
	        }
	    } catch (NoSuchAlgorithmException e) {
	        e.printStackTrace();
	    } catch (UnsupportedEncodingException e) {
	        e.printStackTrace();
	    }
	    	
	    return hexSHA256hash.toString();
	    
	}
  
}
