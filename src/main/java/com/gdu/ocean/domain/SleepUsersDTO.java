package com.gdu.ocean.domain;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SleepUsersDTO {
	
	private int sleepUserNo;
	private String email;
	private String pw;
	private String phoneNo;
	private String postcode;
	private String roadAddress;
	private String jibunAddress;
	private String detailAddress;
	private String name;
	private UsersDTO joinedAt;
	private Date sleptAt;

}
