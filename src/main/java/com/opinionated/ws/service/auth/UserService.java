package com.opinionated.ws.service.auth;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
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
public class UserService implements UserDetailsService{
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ProfileRepository profileRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return userRepository
					.findByName(username)
					.orElseThrow(() -> new UsernameNotFoundException("User with name " + username + " not founded"));
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
		return userRepository.findByName(user).isPresent();
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
	
}
