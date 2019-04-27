package com.community.rest.service;

import com.community.rest.domain.Delivery;
import com.community.rest.domain.Member;
import com.community.rest.domain.Order;
import com.community.rest.domain.OrderItem;
import com.community.rest.domain.item.Item;
import com.community.rest.repository.ItemRepository;
import com.community.rest.repository.MemberRepository;
import com.community.rest.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderService {
	@Autowired
	MemberRepository memberRepository;

	@Autowired
	OrderRepository orderRepository;

	@Autowired
	ItemRepository itemRepository;

	/**
	 * 주문하다.
	 * @param member_id
	 * @param item_id
	 * @param count
	 * @return
	 */
	public Long Order(Long member_id, Long item_id, int count) {
		// 엔티티 조회
		Member member = memberRepository.findOne(member_id);
		Item item = itemRepository.findOne(item_id);

		// 배송정보 생성
		Delivery delivery = Delivery.createDelivery(member.getAddress());

		// 주문상품 생성
		OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

		// 주문생성
		Order order = Order.createOrder(member, delivery, orderItem);

		// 주문저장
		orderRepository.save(order);

		return order.getId();
	}

	public void cancelOrder(Long orderId) {
		// 주문 엔티티 조회
		Order order = orderRepository.findOne(orderId);
		// 주문취소
		order.cancel();
	}
}
