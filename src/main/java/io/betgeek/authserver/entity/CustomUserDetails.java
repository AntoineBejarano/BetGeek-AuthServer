package io.betgeek.authserver.entity;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import io.betgeek.domain.persistence.entity.PassboltUserPersistenceEntity;
import io.betgeek.domain.persistence.entity.UserPersistenceEntity;

public class CustomUserDetails implements UserDetails {

	private static final long serialVersionUID = 1L; 
	private Collection<? extends GrantedAuthority> authorities; 
	private String password;
	private String username;
	private String id;
	private String idRol;
	private PassboltUserPersistenceEntity passboltUser;
	private String firstName;
	private String lastName;
	private Boolean subscriptionActive;
	
	private String partnerId;
	private String partnerFirstName;
	private String partnerLastName;
	
	public CustomUserDetails() {

	}
	
	public CustomUserDetails(UserPersistenceEntity user, PassboltUserPersistenceEntity passboltUser) {
		this.username = user.getUsername();
		this.password = user.getPassword();
		this.id = user.getIdUser();
		this.idRol = user.getIdRole().toString();
		this.passboltUser = passboltUser;
		if (user.getPerson() != null) {
			this.firstName = user.getPerson().getFirstName();
			this.lastName = user.getPerson().getLastName();
		}
		this.authorities = new ArrayList<>();
	}
	
	public CustomUserDetails(UserPersistenceEntity user, PassboltUserPersistenceEntity passboltUser, UserPersistenceEntity partner) {
		this.username = user.getUsername();
		this.password = user.getPassword();
		this.id = user.getIdUser();
		this.idRol = user.getIdRole().toString();
		this.passboltUser = passboltUser;
		if (user.getPerson() != null) {
			this.firstName = user.getPerson().getFirstName();
			this.lastName = user.getPerson().getLastName();
		}
		this.authorities = new ArrayList<>();
		this.subscriptionActive = user.getActive();
		if (partner != null) {
			this.partnerId = partner.getIdUser();
			if (partner.getPerson() != null) {
				this.partnerFirstName = partner.getPerson().getFirstName();
				this.partnerLastName = partner.getPerson().getLastName();
			}
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
	
	public PassboltUserPersistenceEntity getPassboltUser() {
		return this.passboltUser;
	}
	
	public String getIdRol() {
		return this.idRol;
	}
	
	public Boolean getSubscriptionActive() {
		return this.subscriptionActive;
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
