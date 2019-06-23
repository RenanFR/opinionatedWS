package com.opinionated.ws.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

public class Category {
	
	@Id
	private String id;
	
	@Field("description")
	private String description;

	public Category() {
	}

	public Category(String id) {
		this.id = id;
	}

	public Category(String id, String description) {
		this.id = id;
		this.description = description;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "Category [id=" + id + ", description=" + description + "]";
	}
	

}
