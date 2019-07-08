package com.opinionated.ws.domain;

import org.springframework.data.annotation.Id;
import org.springframework.web.multipart.MultipartFile;

public class Post {
	
	@Id
	private String id;
	
	private String title;
	
	private String text;
	
	private MultipartFile media;
	
	private Category category;

}
