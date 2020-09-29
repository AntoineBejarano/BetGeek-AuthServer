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
	private String idRol;
	private PassboltUser passboltUser;
	private String firstName;
	private String lastName;
	
	private String partnerId;
	private String partnerFirstName;
	private String partnerLastName;
	
	public CustomUserDetails() {

	}
	
	public CustomUserDetails(User user, PassboltUser passboltUser) {
		this.username = user.getUsername();
		this.password = user.getPassword();
		this.id = user.getId();
		this.idRol = user.getIdRole().toString();
		this.passboltUser = passboltUser;
		this.firstName = user.getFirstName();
		this.lastName = user.getLastName();
		this.authorities = new ArrayList<>();
	}
	
	public CustomUserDetails(User user, PassboltUser passboltUser, User partner) {
		this.username = user.getUsername();
		this.password = user.getPassword();
		this.id = user.getId();
		this.idRol = user.getIdRole().toString();
		this.passboltUser = passboltUser;
		this.firstName = user.getFirstName();
		this.lastName = user.getLastName();
		this.authorities = new ArrayList<>();
		if (partner != null) {
			this.partnerId = partner.getId();
			this.partnerFirstName = partner.getFirstName();
			this.partnerLastName = partner.getLastName();
		}
	}

	public String getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(String partnerId) {
		this.partnerId = partnerId;
	}

	public String getPartnerFirstName() {
		return partnerFirstName;
	}

	public void setPartnerFirstName(String partnerFirstName) {
		this.partnerFirstName = partnerFirstName;
	}

	public String getPartnerLastName() {
		return partnerLastName;
	}

	public void setPartnerLastName(String partnerLastName) {
		this.partnerLastName = partnerLastName;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.authorities;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
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
	
	public String getIdRol() {
		return this.idRol;
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
