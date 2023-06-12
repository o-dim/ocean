package com.gdu.ocean.domain;

import lombok.Data;

// 결제내역 확인
@Data
public class Amount {
	private int total; // 총 결제 금액
    private int tax_free; // 비과세 금액
    private int tax; // 부가세 금액
    private int discount; // 할인금액
}
