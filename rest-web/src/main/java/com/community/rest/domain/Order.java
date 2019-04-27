package com.community.rest.domain;

import com.community.rest.domain.enums.DeliveryStatus;
import com.community.rest.domain.enums.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name="ORDERS")
public class Order {

	@Id
	@GeneratedValue
	@Column(name="ORDER_ID")
	private Long id;

	@ManyToOne
	@JoinColumn(name="MEMBER_ID")
	private Member member;

	@OneToMany(mappedBy = "order")
	private List<OrderItem> orderItems = new ArrayList<OrderItem>();

	@OneToOne
	@JoinColumn(name = "DELIVERY_ID")
	private Delivery delivery;

	private Date orderDate;

	@Enumerated(EnumType.STRING)
	private OrderStatus status;

	public void setMember(Member member) {
		this.member = member;
		member.getOrders().add(this);
	}

	public void addOrderItem(OrderItem orderItem) {
		orderItems.add(orderItem);
		orderItem.setOrder(this);
	}

	public void setDelivery(Delivery delivery) {
		this.delivery = delivery;
		delivery.setOrder(this);
	}

	/**
	 * 주문생성
	 * @param member
	 * @param delivery
	 * @param orderItems
	 * @return
	 */
	public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems) {
		Order order = new Order();
		order.setMember(member);
		order.setDelivery(delivery);
		for(OrderItem orderItem : orderItems) {
			order.addOrderItem(orderItem);
		}

		order.setStatus(OrderStatus.ORDER);
		order.setOrderDate(new Date());
		return order;
	}

	/**
	 * 주문취소
	 */
	public void cancel() {
		if(delivery.getStatus() == DeliveryStatus.COMP) {
			throw new RuntimeException("이미 배송완료된 상품은 취소가 불가능합니다.");
		}
		this.setStatus(OrderStatus.CANCEL);
		for(OrderItem orderItem : orderItems) {
			orderItem.cancel();
		}
	}

	public int getTotalPrice() {
		int totalPrice = 0;
		for(OrderItem orderItem : orderItems) {
			totalPrice += orderItem.getTotalPrice();
		}

		return totalPrice;
	}
}
