package com.community.rest.domain;

import com.community.rest.domain.enums.DeliveryStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity(name="DELIVERY")
public class Delivery {

	@Id
	@GeneratedValue
	@Column(name="DELIVERY_ID")
	private Long id;

	@OneToOne(mappedBy = "delivery")
	private Order order;

	private Address address;

	@Enumerated(EnumType.STRING)
	private DeliveryStatus status;

	public static Delivery createDelivery(Address address){
		Delivery delivery = new Delivery();
		delivery.setAddress(address);
		return delivery;
	}
}
