package com.community.rest.domain;

import com.community.rest.domain.enums.Address;
import com.community.rest.domain.enums.DeliveryStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

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

	private DeliveryStatus status;
}
