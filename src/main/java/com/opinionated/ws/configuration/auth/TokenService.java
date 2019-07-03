package com.opinionated.ws.configuration.auth;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.opinionated.ws.domain.auth.User;
import com.opinionated.ws.service.auth.UserService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

//Useful class to generate and read token from token parameters
@Component
@PropertySource("classpath:token.properties")
public class TokenService {
	
	public static long EXPIRATION_TIME;
	
	public static String SECRET;
	
	public static String TOKEN_PREFIX;
	
	public static String HEADER_STRING;
	
	private static UserService userService;
	
	public static void addTokenToResponse(HttpServletResponse response, String userName) {
		String jsonWebToken = createToken(userName);
		response.addHeader(HEADER_STRING, jsonWebToken);
	}
	
	public static String createToken(String userName) {
		User user = userService.findByName(userName);
		Map<String, Object> claims = new HashMap<>();
		claims.put("userEmail", user.getEmail());
		claims.put("socialUser", user.isSocialLogin());
		claims.put("inclusionDate", DateTimeFormatter.ofPattern("dd/MM/yyyy").format(LocalDate.parse(user.getInclusionDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"))));
		String profiles = user.getAuthorities().stream().map(r -> r.getAuthority()).collect(Collectors.joining(","));
		claims.put("profiles", profiles);
		return TOKEN_PREFIX + " " + Jwts.builder()
			.setSubject(user.getUsername())
			.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
			.addClaims(claims)
			.signWith(SignatureAlgorithm.HS512, SECRET)
			.compact();
	}
	
	public static Authentication getTokenFromRequest(HttpServletRequest request) {
		String jwt = request.getHeader(HEADER_STRING);
		if (jwt != null) {
			String userFromToken = Jwts.parser()
					.setSigningKey(SECRET)
					.parseClaimsJws(jwt.replace(TOKEN_PREFIX, ""))
					.getBody()
					.getSubject();
			if (userFromToken != null) {
				return new UsernamePasswordAuthenticationToken(userFromToken, null, Collections.emptyList());
			}
		}
		return null;
	}

	public static void setUserService(UserService userService) {
		TokenService.userService = userService;
	}

	@Value("${token.expiration.time}")
	public void setExpirationTime(long expirationTime) {
		TokenService.EXPIRATION_TIME = expirationTime;
	}

	@Value("${token.secret}")
	public void setSecret(String secret) {
		TokenService.SECRET = secret;
	}

	@Value("${token.prefix}")
	public void setTokenPrefix(String tokenPrefix) {
		TokenService.TOKEN_PREFIX = tokenPrefix;
	}

	@Value("${token.header}")
	public void setHeaderString(String headerString) {
		TokenService.HEADER_STRING = headerString;
	}

}
