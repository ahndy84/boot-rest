package com.community.rest.domain;

import com.community.rest.domain.item.Item;
import lombok.Getter;
import lombok.Setter;
import org.aspectj.weaver.ast.Or;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "ORDER_ITEM")
public class OrderItem {

	@Id
	@GeneratedValue
	@Column(name="ORDER_ITEM_ID")
	private Long id;

	@ManyToOne
	@JoinColumn(name="ITEM_ID")
	private Item item;  // 주문상품

	@ManyToOne
	@JoinColumn(name="ORDER_ID")
	private Order order;  //주문

	private int orderPrice;  // 주문가격
	private int count;  // 주문수량

	public void setOrder(Order order) {
		this.order = order;
		order.addOrderItem(this);
	}

	/**
	 * 주문생성
	 * @param item
	 * @param orderPrice
	 * @param count
	 * @return
	 */
	public static OrderItem createOrderItem(Item item, int orderPrice, int count) {
		OrderItem orderItem = new OrderItem();
		orderItem.setItem(item);
		orderItem.setOrderPrice(orderPrice);
		orderItem.setCount(count);
		item.removeStock(count);
		return orderItem;
	}

	/**
	 * 주문취소
	 */
	public void cancel() {
		getItem().addStock(count);
	}

	/**
	 * 조회로직
	 * @return
	 */
	public int getTotalPrice() {
		return getOrderPrice() * getCount();
	}
}
