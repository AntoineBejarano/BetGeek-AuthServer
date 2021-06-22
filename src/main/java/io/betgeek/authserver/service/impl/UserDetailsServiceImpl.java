package io.betgeek.authserver.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import io.betgeek.authserver.entity.CustomUserDetails;
import io.betgeek.authserver.service.PartnerUsersService;
import io.betgeek.domain.persistence.entity.PartnerUsersPersistenceEntity;
import io.betgeek.domain.persistence.entity.PassboltUserPersistenceEntity;
import io.betgeek.domain.persistence.entity.UserPersistenceEntity;
import io.betgeek.domain.persistence.repository.PassboltUserPersistenceRepository;
import io.betgeek.domain.persistence.repository.UserPersistenceRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserPersistenceRepository userRepository;
	
	@Autowired
	private PassboltUserPersistenceRepository passboltUserRespository;
	
	@Autowired
	private PartnerUsersService partnerUserService;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserPersistenceEntity user = userRepository.findByUsername(username);
		UserPersistenceEntity partner = null;
		PassboltUserPersistenceEntity passboltUser = null;
		if (user == null) {
			throw new UsernameNotFoundException("UserName " + username + " not found");
		} else {
			if (user.getIdPassbolt() != null && !user.getIdPassbolt().isEmpty()) {
				passboltUser = passboltUserRespository.findById(user.getIdPassbolt()).orElse(null);	
			}
			PartnerUsersPersistenceEntity partnerUser = partnerUserService.findByUser(user.getIdUser());
			if (partnerUser != null) {
				Optional<UserPersistenceEntity> optionalUser = userRepository.findById(partnerUser.getIdPartner());
				if (optionalUser.isPresent()) {
					partner = optionalUser.get();
				}
			}
		}
		return new CustomUserDetails(user, passboltUser, partner);
	}

}
