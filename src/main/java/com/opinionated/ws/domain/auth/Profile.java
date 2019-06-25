package com.opinionated.ws.domain.auth;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(name = "tbl_profile")
public class Profile implements GrantedAuthority{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	private String role;
	
	@ManyToMany(mappedBy = "profiles")
	private List<User> users;

	public String getRole() {
		return role;
	}

	@Override
	public String getAuthority() {
		return role;
	}
	
	public void setRole(String role) {
		this.role = role;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

}
