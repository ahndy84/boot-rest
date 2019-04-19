package com.community.rest.domain.item;

import com.community.rest.domain.OrderItem;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "DTYPE")
@Table(name = "ITEMS")
public class Item {

	@Id
	@GeneratedValue
	@Column(name = "ITEM_ID")
	private Long id;

	private String name;

	@OneToMany(mappedBy = "items")
	private List<OrderItem> OrderItems = new ArrayList<OrderItem>();

	private int stockQuantity;

}
