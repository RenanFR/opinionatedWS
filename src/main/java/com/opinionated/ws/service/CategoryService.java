package com.opinionated.ws.service;

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

}
