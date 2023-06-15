package com.gdu.ocean.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HashtagCdDTO {

	private int htcdNo;
	private CdDTO cdNo;
	private HashtagDTO htNo;
	private HashtagDTO hashtagDTO;
}
