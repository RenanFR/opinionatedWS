package com.opinionated.ws.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.opinionated.ws.domain.Category;
import com.opinionated.ws.persistence.CategoryRepository;

@Service
public class CategoryService {
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	public String onSave(Category category) {
		String savedCategory = categoryRepository.save(category).getDescription();
		return "Category " + savedCategory + " saved successfully";
	}
	
	public List<Category> onGet() {
		List<Category> list = categoryRepository.findAll();
		return list;
	}

}
