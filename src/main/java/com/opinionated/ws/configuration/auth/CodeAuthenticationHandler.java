package com.opinionated.ws.configuration.auth;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.web.authentication.WebAuthenticationDetails;

public class CodeAuthenticationHandler extends WebAuthenticationDetails {

	private static final long serialVersionUID = 1L;
	
	private String verificationCode;

	public CodeAuthenticationHandler(HttpServletRequest request) {
		super(request);
		setVerificationCode(request.getParameter("authCode"));
	}
	
	public String getVerificationCode() {
		return verificationCode;
	}

	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}	

}
