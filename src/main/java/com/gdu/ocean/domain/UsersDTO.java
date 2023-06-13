package com.gdu.ocean.domain;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UsersDTO {
	
	private int userNo;
	private String email;
	private String pw;
	private String phoneNo;
	private String postcode;
	private String roadAddress;
	private String jibunAddress;
	private String detailAddress;
	private String name;
	private Date joinedAt;
	private int agreecode;
	private String autologinEmail;
	private Date autologinExpiredAt;

}
