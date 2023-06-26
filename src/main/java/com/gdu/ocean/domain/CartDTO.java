package com.gdu.ocean.domain;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartDTO {
	private int cartNo;
	private Date madeAt;
	private UsersDTO userNo;
}
