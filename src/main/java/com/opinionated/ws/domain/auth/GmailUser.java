package com.opinionated.ws.domain.auth;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

public class GmailUser {
	
	private String name;
	
	private String email;
	
	private String picture;
	
	private String locale;
	
	private String sub;
	
	private String jti;
	
	private String iss;
	
	private String exp;
	
	private String iat;
	
	private String aud;
	
	public static class GmailUserBuilder {
		
		private GmailUser gmailUser = new GmailUser();
		
		public GmailUserBuilder withName(String name) {
			this.gmailUser.name = name;
			return this;
		}
		
		public GmailUserBuilder withEmail(String email) {
			this.gmailUser.email = email;
			return this;
		}
		
		public GmailUserBuilder withPicture(String picture) {
			this.gmailUser.picture = picture;
			return this;
		}
		
		public GmailUserBuilder withLocale(String locale) {
			this.gmailUser.locale = locale;
			return this;
		}
		
		public GmailUserBuilder withSub(String sub) {
			this.gmailUser.sub = sub;
			return this;
		}
		
		public GmailUserBuilder withJti(String jti) {
			this.gmailUser.jti = jti;
			return this;
		}
		
		public GmailUserBuilder withIss(String iss) {
			this.gmailUser.iss = iss;
			return this;
		}
		public GmailUserBuilder withExp(String exp) {
			this.gmailUser.exp = convertDate(exp);
			return this;
		}
		public GmailUserBuilder withIat(String iat) {
			this.gmailUser.iat = convertDate(iat);
			return this;
		}
		
		public GmailUserBuilder withAud(String aud) {
			this.gmailUser.aud = aud;
			return this;
		}
		private String convertDate(String iat) {
			String issuedAt = DateTimeFormatter
				.ofPattern("dd/MM/yyyy h:ma")
				.format(LocalDateTime.ofInstant(Instant.ofEpochSecond(Long.parseLong(iat)), TimeZone.getDefault().toZoneId()));
			return issuedAt;
		}
		
		public GmailUser build() {
			return this.gmailUser;
		}
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public String getSub() {
		return sub;
	}

	public void setSub(String sub) {
		this.sub = sub;
	}

	public String getJti() {
		return jti;
	}

	public void setJti(String jti) {
		this.jti = jti;
	}

	public String getIss() {
		return iss;
	}

	public void setIss(String iss) {
		this.iss = iss;
	}

	public String getExp() {
		return exp;
	}

	public void setExp(String exp) {
		this.exp = exp;
	}

	public String getIat() {
		return iat;
	}

	public void setIat(String iat) {
		this.iat = iat;
	}

	public String getAud() {
		return aud;
	}

	public void setAud(String aud) {
		this.aud = aud;
	}

}
