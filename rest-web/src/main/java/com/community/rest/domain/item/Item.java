package com.community.rest.domain.item;

import com.community.rest.domain.Category;
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
public class Item {
	@Id
	@GeneratedValue
	@Column(name = "ITEM_ID")
	private Long id;

	private String name;
	private int price;
	private int stockQuantity;

	@ManyToMany(mappedBy = "items")
	private List<Category> categories = new ArrayList<Category>();

	public void addStock(int quantity) {
		this.stockQuantity += quantity;
	}

	public void removeStock(int quantity) {
		int restStock = this.stockQuantity - quantity;
		if(restStock < 0) {
			throw new RuntimeException("need more stock");
		}
		this.stockQuantity = restStock;
	}




}
