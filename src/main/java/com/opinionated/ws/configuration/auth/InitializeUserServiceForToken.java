package com.opinionated.ws.configuration.auth;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.opinionated.ws.service.auth.UserService;

@Component
public class InitializeUserServiceForToken {
	
	@Autowired
	private UserService userService;
	
	@PostConstruct
	public void injectOnStatic() {
		TokenService.setUserService(userService);
	}

}
