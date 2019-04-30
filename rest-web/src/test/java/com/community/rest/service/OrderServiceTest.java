package com.community.rest.service;

import com.community.rest.domain.Address;
import com.community.rest.domain.Member;
import com.community.rest.domain.Order;
import com.community.rest.domain.enums.OrderStatus;
import com.community.rest.domain.item.Book;
import com.community.rest.domain.item.Item;
import com.community.rest.repository.OrderRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class OrderServiceTest {
	@PersistenceContext
	EntityManager em;

	@Autowired
	OrderService orderService;

	@Autowired
	OrderRepository orderRepository;

	@Test
	public void 상품주문() throws Exception {
		// Given
		Member member = createMember();
		Item item = createBook("시골JPA", 10000, 10); // 이름, 가격, 재고
		int orderCount = 2;

		// Where
		Long orderId = orderService.order(member.getId(), item.getId(), orderCount);

		// then
		Order getOrder = orderRepository.findOne(orderId);

		assertThat("상품 주문시 상태는 ORDER ", getOrder.getStatus(), is(OrderStatus.ORDER));
		assertThat("주문한 상품 종류 수가 정확해야 한다.", getOrder.getOrderItems().size(), is(1));
		assertThat("주문 가격은 사격 * 수량이다.", getOrder.getTotalPrice(), is(10000*2));
		assertThat("주문수량만큼 재고가 줄어야 한다.", item.getStockQuantity(), is(8));
	}

	@Test
	public void 상품주문_재고수량초과() throws Exception {
		// given
		Member member = createMember();
		Item item = createBook("도시JPA", 10000, 10);
		int ordercount = 10;

		// when
		orderService.order(member.getId(), item.getId(), ordercount);

		// then
		fail("재고 수량 부족 예외가 발생해야 한다.");
	}

	@Test
	public void 주문취소() {
		// given
		Member member = createMember();
		Item item = createBook("쏘카JPA", 10000, 10); // 이름, 가격, 재고
		int orderCount = 2;

		Long orderId = orderService.order(member.getId(), item.getId(), orderCount);

		// when
		orderService.cancelOrder(orderId);

		// then
		Order getOrder = orderRepository.findOne(orderId);


		assertThat("주문취소시 상태는 CANCEL이다.", getOrder.getStatus(), is(OrderStatus.CANCEL));
		assertThat("주문이 취소된 상품은 그만큼 재고가 증가해야 한다.", getOrder.getStatus(), is(OrderStatus.CANCEL));

	}

	private Member createMember() {
		Member member = new Member();
		member.setName("회원1");
		member.setAddress(new Address("서울", "강가", "123-123"));
		em.persist(member);
		return member;
	}

	private Book createBook(String name, int price, int stockQuantity) {
		Book book = new Book();
		book.setName(name);
		book.setStockQuantity(stockQuantity);
		book.setPrice(price);
		em.persist(book);
		return book;
	}
}
