package com.web.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class Item {

	@Id
	@GeneratedValue
	@Column(name="ITEM_ID")
	private Long id;

	private String name;

	private int price;

	private int stockQuantity;

	@OneToMany(mappedBy = "item")
	private OrderItem orderItem;

	@ManyToMany
	@JoinColumn(name="CATEGORY_ID")
	private List<Category> categories = new ArrayList<>();

	public void addCategories(Category category) {
		categories.add(category);
		category.setItems(this);
	}
}
