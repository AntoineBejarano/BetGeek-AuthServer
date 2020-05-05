package io.betgeek.authserver.entity;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomUserDetails implements UserDetails {

	private static final long serialVersionUID = 1L; 
	private Collection<? extends GrantedAuthority> authorities; 
	private String password;
	private String username;
	private String id;
	private PassboltUser passboltUser;
	
	public CustomUserDetails() {

	}
	
	public CustomUserDetails(User user, PassboltUser passboltUser) {
		this.username = user.getUsername();
		this.password = user.getPassword();
		this.id = user.getId();
		this.passboltUser = passboltUser;
		this.authorities = new ArrayList<>();
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.authorities;
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public String getUsername() {
		return this.username;
	}
	
	public String getId() {
		return this.id;
	}
	
	public PassboltUser getPassboltUser() {
		return this.passboltUser;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}