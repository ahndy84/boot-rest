package com.community.rest.domain;

import lombok.Getter;
import lombok.Setter;
import org.aspectj.weaver.ast.Or;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class OrderItem {

	@Id
	@GeneratedValue
	@Column(name="ORDERITEM_ID")
	private Long id;

	@ManyToOne
	@JoinColumn(name="orderItem")
	private Order order;

	private int orderPrice;

	private int count;

	public void setOrder(Order order) {
		this.order = order;
		order.addOrderItem(this);
	}
}
