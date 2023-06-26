package com.gdu.ocean.domain;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OutUsersDTO {
	
	private int outUserNo;
	private String email;
	private Date joinedAt;
	private Date outAt;
	
}
