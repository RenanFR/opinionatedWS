package com.opinionated.ws;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

import com.opinionated.ws.domain.auth.Profile;
import com.opinionated.ws.domain.auth.User;
import com.opinionated.ws.service.auth.UserService;

@SpringBootApplication
public class OpinionatedWsApplication {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(OpinionatedWsApplication.class);
	
	@Autowired
	private UserService userService;
	
	public static void main(String[] args) {
		SpringApplication.run(OpinionatedWsApplication.class, args);
	}
	@EventListener(ApplicationReadyEvent.class)
	public void fillUser() {
		if (userService.getAll().isEmpty()) {
			LOGGER.info("No users available, creating a test default user");
			User user = new User();
			user.setName("user");
			user.setPassword("pass");
			user.setSocialLogin(false);
			if (userService.getAllProfiles().isEmpty()) {
				Profile profile = new Profile();
				profile.setRole("ROLE");
				userService.saveProfile(profile);
				user.setProfiles(Arrays.asList(profile));
			}
			LOGGER.info("Default user created with name " + user.getName());
			userService.saveUser(user);
		}
	}
	
	@Bean
	public MultipartResolver multipartResolver() {
		return new StandardServletMultipartResolver();
	}
	
}