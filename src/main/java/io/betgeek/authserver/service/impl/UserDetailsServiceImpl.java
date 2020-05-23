package io.betgeek.authserver.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException.BadRequest;

import io.betgeek.authserver.entity.CustomUserDetails;
import io.betgeek.authserver.entity.PassboltUser;
import io.betgeek.authserver.entity.User;
import io.betgeek.authserver.repository.PassboltUserRespository;
import io.betgeek.authserver.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PassboltUserRespository passboltUserRespository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		System.out.println("Init Auth [" + username + "]");
		User user = userRepository.findByUsername(username);
		PassboltUser passboltUser = null;
		if (user == null) {
			throw new UsernameNotFoundException("UserName " + username + " not found");
		} else {
			if (user.getIdPassbolt() != null && !user.getIdPassbolt().isEmpty()) {
				passboltUser = passboltUserRespository.findById(user.getIdPassbolt());	
			}
		}
		return new CustomUserDetails(user, passboltUser);
	}
	
	public void saveUser(User user) throws BadRequest {
		userRepository.save(user);
	}

}
