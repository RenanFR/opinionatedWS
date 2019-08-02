package com.opinionated.ws.resources;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import static org.springframework.http.ResponseEntity.ok;

import com.google.api.client.auth.openidconnect.IdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.opinionated.ws.configuration.auth.TokenService;
import com.opinionated.ws.domain.auth.GmailUser;
import com.opinionated.ws.domain.auth.User;
import com.opinionated.ws.service.auth.UserService;
import com.opinionated.ws.domain.auth.GmailUser.GmailUserBuilder;
import com.opinionated.ws.domain.auth.Profile;

@RestController
@RequestMapping("login")
public class LoginController {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private UserService userService;
	
	@Value("${id.client.oauth}")
	private String idClientOauth;
	
	@PostMapping("signup")
	public void signUp(@RequestBody User user) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encode = passwordEncoder.encode(user.getPassword());
		user.setPassword(encode);
		this.userService.signUp(user);
	}
	
	@GetMapping("exists/{user}")
	public boolean userExists(@PathVariable("user") String user) {
		boolean doesUserExists = userService.userExists(user);
		return doesUserExists;
	}
	
	//Get user information from token payload
	private GmailUser setUserFromPayload(Payload payload) {
		GmailUser gmailUser = new GmailUserBuilder()
			.withEmail(payload.get("email").toString())
			.withName(payload.get("name").toString())
			.withPicture(payload.get("picture").toString())
			.withJti(payload.get("jti").toString())
			.withLocale(payload.get("locale").toString())
			.withSub(payload.get("sub").toString())
			.withIss(payload.get("iss").toString())
			.withExp(payload.get("exp").toString())
			.withIat(payload.get("iat").toString())
			.withAud(payload.get("aud").toString())
			.build();
		return gmailUser;
	}
	
	@PostMapping("google")
	public ResponseEntity<Map<String, String>> handleGoogleToken(@RequestBody String tokenGmail) 
			throws IOException, GeneralSecurityException {
		GoogleIdTokenVerifier tokenVerifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new JacksonFactory())
				.setAudience(Collections.singletonList(this.idClientOauth))
				.build();
		GoogleIdToken idToken = tokenVerifier.verify(tokenGmail);
		if (idToken != null) {
			Payload payload = idToken.getPayload();
			GmailUser gmailUser = this.setUserFromPayload(payload);
			User user = checkGmailUser(gmailUser);
			String appToken = this.login(user).getBody().get("token");
			Map<String, String> userInformationMap = buildUserInformationMap(user.getName(), appToken);
			return ResponseEntity.ok(userInformationMap);
		}
		return null;
	}

	private Map<String, String> buildUserInformationMap(String user, String appToken) {
		Map<String, String> userInformationMap = new HashMap<String, String>();
		userInformationMap.put("username", user);
		userInformationMap.put("token", appToken);
		return userInformationMap;
	}
	
	private User saveSocialUser(GmailUser gmailUser) {
		User user = new User();
		user.setName(gmailUser.getName());
		user.setPassword("");
		user.setSocialLogin(true);
		user.setInclusionDate(LocalDate.now());
		user.setEmail(gmailUser.getEmail());
		Set<Profile> roleProfile = Collections.singleton(userService.findProfileByDescription("ROLE"));
		user.setProfiles(new ArrayList<Profile>(roleProfile));
		userService.saveUser(user);
		return user;
	}
	
	private User checkGmailUser(GmailUser gmailUser) {
		User onDatabase = userService.findByName(gmailUser.getName());
		if (onDatabase == null) {
			return saveSocialUser(gmailUser);
		}
		return onDatabase;
	}

	@PostMapping
	public ResponseEntity<Map<String, String>> login(@RequestBody User user) {
		try {
			String name = user.getName();
			String password = (!user.isSocialLogin()) ? user.getPassword() : "";
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(name, password));
			UserDetails loadUser = userService.loadUserByUsername(name);
			String token = TokenService.createToken(name);
			Map<String, String> userInformationMap = buildUserInformationMap(loadUser.getUsername(), token);
			return ok(userInformationMap);
		} catch (AuthenticationException authenticationException) {
			throw new BadCredentialsException("Authentication data provided is invalid");
		}
	}
	
	@GetMapping("uses-tfa/{user}")
	public ResponseEntity<Boolean> checkTFAIsEnabledForUser(@PathVariable("user") String user) {
		User account = userService.findByEmail(user);
		boolean using2fa = account.isUsing2FA();
		return ResponseEntity.ok(using2fa);
	}
	

}
