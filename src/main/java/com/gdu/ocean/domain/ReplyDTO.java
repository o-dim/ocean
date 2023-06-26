package com.gdu.ocean.domain;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReplyDTO {
	
	private int replyNo;
	private String content;
	private int groupNo;
	private int groupOrder;
	private int depth;
	private Date writeAt;
	private UsersDTO usersDTO;
	private int idolNo;
}
