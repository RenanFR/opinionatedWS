package com.opinionated.ws.configuration.auth;

import java.io.IOException;
import java.util.Collections;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.aerogear.security.otp.Totp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.opinionated.ws.domain.auth.AuthInfoDTO;
import com.opinionated.ws.domain.auth.User;

//Responsible to intercept login authentication and after reading credentials provide the JWT to the user
public class JWTLoginFilter extends AbstractAuthenticationProcessingFilter {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(JWTLoginFilter.class);
	
	private static String loginCode;

	public JWTLoginFilter(String url, AuthenticationManager authenticationManager) {
		super(new AntPathRequestMatcher(url));
		setAuthenticationManager(authenticationManager);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {
		LOGGER.info("Authenticating from " + request.getRequestURI());
		AuthInfoDTO user = new ObjectMapper()
				.readValue(request.getInputStream(), AuthInfoDTO.class);
		loginCode = user.getAuthCode();
		Authentication auth = getAuthenticationManager()
					.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword(), Collections.emptyList()));
		return auth;
	}
	
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		LOGGER.info("Authenticated with success, invoking handler");
		response.addHeader("Access-Control-Expose-Headers", "Authorization");
		User user = (User) authResult.getPrincipal();
		if (user == null) {
			throw new BadCredentialsException("Invalid credentials provided for authentication");
		}
		if (user.isUsing2FA()) {
			Totp totp = new Totp(user.getTwoFASecret());
			if (!isValidNumber(loginCode) || !totp.verify(loginCode)) {
				throw new BadCredentialsException("Invalid two factor authentication code");
			}
			
		}		
		TokenService.addTokenToResponse(response, authResult.getName());
	}
	
	private boolean isValidNumber(String code) {
		try {
			Long.parseLong(code);
		} catch (NumberFormatException numberFormatException) {
			return false;
		}
		return true;
	}	

}
