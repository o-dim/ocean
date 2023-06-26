package com.gdu.ocean.domain;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderDTO {
	
	private int orderNo;
	private int total;
	private String email;
	private String phoneNo;
	private String postcode;
	private String roadAddress;
	private String jibunAddress;
	private String detailAddress;
	private String name;
	private int count;
	private Date orderAt;
	private CartDTO cartNo;
	private CdDTO cdNo;
	
}
