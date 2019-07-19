package com.opinionated.ws.configuration.auth;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

@Component
public class SecurityCustomizer implements AuthenticationDetailsSource<HttpServletRequest, WebAuthenticationDetails> {
	
	@Override
	public WebAuthenticationDetails buildDetails(HttpServletRequest context) {
		return new CodeAuthenticationHandler(context);
	}

}
