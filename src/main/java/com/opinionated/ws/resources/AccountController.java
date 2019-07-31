package com.opinionated.ws.resources;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.opinionated.ws.domain.auth.User;
import com.opinionated.ws.service.auth.UserService;

@RestController
@RequestMapping("account")
public class AccountController {
	
	@Autowired
	private UserService userService;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AccountController.class);
	
	@PostMapping("signup")
	public ResponseEntity<String> createAccount(@RequestBody User account) {
		userService.saveUser(account);
		return ResponseEntity.ok("Account created for user successfully");
	}

}
