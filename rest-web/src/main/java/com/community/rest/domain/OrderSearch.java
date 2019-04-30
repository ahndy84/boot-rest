package com.community.rest.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderSearch {
	private String memberName;  // 회원이름
	private OrderSearch orderSearch;  //주문상태 [ORDER, CANCEL]


}
