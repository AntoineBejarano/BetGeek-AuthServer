package io.betgeek.authserver.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import io.betgeek.authserver.entity.CustomUserDetails;
import io.betgeek.authserver.entity.PartnerUsers;
import io.betgeek.authserver.entity.PassboltUser;
import io.betgeek.authserver.entity.User;
import io.betgeek.authserver.repository.PassboltUserRespository;
import io.betgeek.authserver.repository.UserRepository;
import io.betgeek.authserver.service.PartnerUsersService;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PassboltUserRespository passboltUserRespository;
	
	@Autowired
	private PartnerUsersService partnerUserService;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username);
		User partner = null;
		PassboltUser passboltUser = null;
		if (user == null) {
			throw new UsernameNotFoundException("UserName " + username + " not found");
		} else {
			if (user.getIdPassbolt() != null && !user.getIdPassbolt().isEmpty()) {
				passboltUser = passboltUserRespository.findById(user.getIdPassbolt());	
			}
			PartnerUsers partnerUser = partnerUserService.findByUser(user.getId());
			if (partnerUser != null) {
				Optional<User> optionalUser = userRepository.findById(partnerUser.getIdPartner());
				if (optionalUser.isPresent()) {
					partner = optionalUser.get();
				}
			}
		}
		return new CustomUserDetails(user, passboltUser, partner);
	}

}
