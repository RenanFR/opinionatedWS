package com.opinionated.ws.service.auth;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.NoSuchElementException;

import org.jboss.aerogear.security.otp.api.Base32;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.opinionated.ws.domain.auth.Profile;
import com.opinionated.ws.domain.auth.User;
import com.opinionated.ws.persistence.auth.ProfileRepository;
import com.opinionated.ws.persistence.auth.UserRepository;

@Service
@PropertySource("classpath:2fa.properties")
public class UserService implements UserDetailsService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ProfileRepository profileRepository;
	
	private static String QR_PREFIX;
	
	private static String APP_NAME ;

	@Value("${code.prefix}")
	public static void setQRPrefix(String qrPrefix) {
		QR_PREFIX = qrPrefix;
	}

	@Value("${app.name}")
	public void setAppName(String appName) {
		APP_NAME = appName;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		return userRepository
					.findByEmail(email)
					.orElseThrow(() -> new UsernameNotFoundException("User with email " + email + " not founded"));
	}
	
	public User findByName(String userName) {
		try {
			return userRepository.findByName(userName).get();
		} catch (NoSuchElementException exception) {
			return null;
		}
	}
	
	public void signUp(User user) {
		List<Profile> allProfile = user.getProfiles();
		for (Profile profile : allProfile) {
			if (profileRepository.findById(profile.getRole()).get() == null) {
				profileRepository.save(profile);
			}
		}
		if (user.isUsing2FA()) {
			user.setTwoFASecret(Base32.random());
		}
		this.userRepository.save(user);
	}
	
	public List<User> getAll() {
		return userRepository.findAll();
	}
	
	public List<Profile> getAllProfiles() {
		return profileRepository.findAll();
	}
	
	public void saveProfile(Profile profile) {
		profileRepository.save(profile);
	}
	
	public boolean userExists(String user) {
		return userRepository.findByEmail(user).isPresent();
	}
	
	public void saveUser(User user) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		if (user.getPassword() != null)
			user.setPassword(passwordEncoder.encode(user.getPassword()));
		userRepository.save(user);
	}
	
	public Profile findProfileByDescription(String id) {
		return profileRepository.findById(id).get();
	}
	
	public String generateQRCode(User account) throws UnsupportedEncodingException {
		return QR_PREFIX + URLEncoder.encode(String.format("otpauth://totp/%s:%s?secret=%s&issuer=%s", APP_NAME, account.getEmail(), account.getTwoFASecret(), APP_NAME), "UTF-8");
	}
	
}
