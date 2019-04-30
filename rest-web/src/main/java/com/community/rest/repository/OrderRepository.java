package com.community.rest.repository;

import com.community.rest.domain.Member;
import com.community.rest.domain.Order;
import com.community.rest.domain.OrderSearch;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class OrderRepository {
	@PersistenceContext
	EntityManager em;

	public void save(Order order) {
		em.persist(order);
	}

	public Order findOne(Long id) {
		return em.find(Order.class, id);
	}

	public List<Order> findAll(OrderSearch orderSearch) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Order> cq = cb.createQuery(Order.class);
		Root<Order> o = cq.from(Order.class);

		List<Predicate> criteria = new ArrayList<Predicate>();

		// 주문상태 검색
		if(orderSearch.getOrderSearch() != null){
			Predicate status = cb.equal(o.get("status"), orderSearch.getOrderSearch());
			criteria.add(status);
		}

		// 회원이름 검색
		if(StringUtils.hasText(orderSearch.getMemberName())) {
			// 회원과 조인
			Join<Order, Member> m = o.join("member", JoinType.INNER);
			Predicate name = cb.like(m.<String>get("name"), "%" + orderSearch.getMemberName() + "%");
			criteria.add(name);
		}

		cq.where(cb.and(criteria.toArray(new Predicate[criteria.size()])));

		TypedQuery<Order> query = em.createQuery(cq).setMaxResults(1000); // 최대 1000건

		return query.getResultList();
	}
}
