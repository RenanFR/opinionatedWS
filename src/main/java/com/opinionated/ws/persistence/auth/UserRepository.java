package com.opinionated.ws.persistence.auth;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.opinionated.ws.domain.auth.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
	
	public Optional<User> findByName(String name);
	
	public Optional<User> findByEmail(String email);

}
