package com.web.domain;

import com.web.domain.enums.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class Order {

	@Id
	@GeneratedValue
	@Column(name="ORDER_ID")
	private Long id;

	@OneToOne
	@JoinColumn(name = "DELIVERY_ID")
	private Delivery delivery;

	@Temporal(TemporalType.TIMESTAMP)
	private String oderDate;

	@Enumerated(EnumType.STRING)
	private OrderStatus status;

	@ManyToOne
	@JoinColumn(name = "MEMBER_ID")
	private Member member;

	@OneToMany(mappedBy = "order")
	private List<OrderItem> orderItems = new ArrayList<OrderItem>();

	// 연관관계 메소드
	public void setMember(Member member) {
		// 기존관계 제거
		if(this.member != null) {
			this.member.getOrders().remove(this);
		}
		this.member = member;
		member.getOrders().add(this);
	}

	public void addOrderItem(OrderItem orderItem) {
		orderItems.add(orderItem);
		orderItem.setOrder(this);
	}


}
