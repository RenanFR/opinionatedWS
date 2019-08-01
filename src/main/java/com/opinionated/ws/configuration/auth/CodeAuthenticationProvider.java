package com.opinionated.ws.configuration.auth;

import org.jboss.aerogear.security.otp.Totp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import com.opinionated.ws.domain.auth.User;
import com.opinionated.ws.service.auth.UserService;

public class CodeAuthenticationProvider extends DaoAuthenticationProvider {
	
	@Autowired
	private UserService userService;
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String loginCode = ((CodeAuthenticationHandler) authentication.getDetails()).getVerificationCode();
		User user = userService.findByEmail(authentication.getName());
		if (user == null) {
			throw new BadCredentialsException("Invalid credentials provided for authentication");
		}
		if (user.isUsing2FA()) {
			Totp totp = new Totp(user.getTwoFASecret());
			if (!isValidNumber(loginCode) || !totp.verify(loginCode)) {
				throw new BadCredentialsException("Invalid two factor authentication code");
			}
			
		}
		Authentication auth = super.authenticate(authentication);
		return new UsernamePasswordAuthenticationToken(user, auth.getCredentials(), auth.getAuthorities());
	}
	
	private boolean isValidNumber(String code) {
		try {
			Long.parseLong(code);
		} catch (NumberFormatException numberFormatException) {
			return false;
		}
		return true;
	}
	
	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

}
