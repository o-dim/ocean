package com.gdu.ocean.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartDetailDTO {
	private int cartDetailNo;
	private int count;
	private CartDTO cartNo;
	private CdDTO cdNo;
}
