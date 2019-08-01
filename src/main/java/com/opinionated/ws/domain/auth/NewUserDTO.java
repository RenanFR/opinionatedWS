package com.opinionated.ws.domain.auth;

public class NewUserDTO {
	
	private String userMail;
	
	private String userName;
	
	private String password;
	
	private String passwordConfirmation;
	
	private boolean useTwoFactorAuth;
	
	private boolean isSocialLogin;

	public String getUserMail() {
		return userMail;
	}

	public void setUserMail(String userMail) {
		this.userMail = userMail;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPasswordConfirmation() {
		return passwordConfirmation;
	}

	public void setPasswordConfirmation(String passwordConfirmation) {
		this.passwordConfirmation = passwordConfirmation;
	}

	public boolean isUseTwoFactorAuth() {
		return useTwoFactorAuth;
	}

	public void setUseTwoFactorAuth(boolean useTwoFactorAuth) {
		this.useTwoFactorAuth = useTwoFactorAuth;
	}

	public boolean isSocialLogin() {
		return isSocialLogin;
	}

	public void setSocialLogin(boolean isSocialLogin) {
		this.isSocialLogin = isSocialLogin;
	}

	@Override
	public String toString() {
		return "NewUserDTO [userMail=" + userMail + ", userName=" + userName + ", password=" + password
				+ ", passwordConfirmation=" + passwordConfirmation + ", useTwoFactorAuth=" + useTwoFactorAuth
				+ ", isSocialLogin=" + isSocialLogin + "]";
	}

}
