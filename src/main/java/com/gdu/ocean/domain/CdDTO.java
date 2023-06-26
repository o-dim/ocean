package com.gdu.ocean.domain;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CdDTO {
	
	private int cdNo;
	private String title;
	private String singer;
	private String mainImg;
	private String detailImg;
	private int price;
	private int stock;
	private int recommendCount;
	private Date writedAt;
	private HashtagCdDTO hashtagCdDTO; 
}
