package com.gdu.ocean.domain;

import java.sql.Date;

import lombok.Data;

@Data
public class Payment {
	/*
	 * 결제 정보 DTO
	 * 똑같은 내용으로 DB 작성 필요함
	 * */
	private Integer pay_code; // 일렬번호 관리
	private Long ord_code; // 주문번호
	private String pay_method; // 결제 방식
	private Date pay_date; // 결제일
	private int pay_tot_price; // 결제 금액
	private int pay_rest_price; // 미지급금
	private String pay_nobank_user; // 입금자명
	private String pay_nobank; // 입금은행
}
