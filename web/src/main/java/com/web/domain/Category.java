package com.web.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class Category {

	@Id
	@GeneratedValue
	@Column(name="CATEGORY_ID")
	private Long id;

	private String name;

	@OneToMany(mappedBy = "category")
	private List<Item> items = new ArrayList<Item>();
}
