package com.opinionated.ws.persistence.auth;

import org.springframework.data.jpa.repository.JpaRepository;

import com.opinionated.ws.domain.auth.Profile;

public interface ProfileRepository extends JpaRepository<Profile, String> {
	
}
