package com.gdu.ocean.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecommendDTO {
	private int recommendNo;
	private UsersDTO userNo;
	private CdDTO cdNo;

}
