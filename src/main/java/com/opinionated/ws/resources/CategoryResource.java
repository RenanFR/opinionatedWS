package com.opinionated.ws.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.opinionated.ws.domain.Category;
import com.opinionated.ws.service.CategoryService;

//TODO	Apply HATEOAS
@RestController
@RequestMapping("categories")
public class CategoryResource {
	
	@Autowired
	private CategoryService categoryService;
	
	@PostMapping
	public ResponseEntity<String> saveCategory(@RequestBody	Category category) {
		ResponseEntity<String> response = ResponseEntity
			.ok(categoryService.onSave(category));
		return response;
	}
	
	@GetMapping
	public ResponseEntity<List<Category>> getCategories() {
		ResponseEntity<List<Category>> response = ResponseEntity
			.ok(categoryService.onGet());
		return response;
	}
	
	@GetMapping("test")
	public String test() {
		return "test";
	}

}
