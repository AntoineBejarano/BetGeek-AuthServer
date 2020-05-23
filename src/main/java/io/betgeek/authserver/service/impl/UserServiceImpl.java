package io.betgeek.authserver.service.impl;

import java.util.UUID;

import javax.ws.rs.BadRequestException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import io.beetgeek.passbolt.model.json.UserRequest;
import io.betgeek.authserver.entity.PassboltClientMainInfo;
import io.betgeek.authserver.entity.PassboltUser;
import io.betgeek.authserver.entity.User;
import io.betgeek.authserver.exception.RedirecException;
import io.betgeek.authserver.exception.UserException;
import io.betgeek.authserver.repository.PassboltUserRespository;
import io.betgeek.authserver.repository.UserRepository;
import io.betgeek.authserver.service.PassboltService;
import io.betgeek.authserver.service.UserService;
import io.betgeek.authserver.vo.UserVO;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private PassboltService passboltService;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PassboltUserRespository passboltUserRepository;

	@Override
	public String saveUser(PassboltClientMainInfo mainInfo, UserVO userVo) throws UserException, BadRequestException {
		checkInfoUser(userVo);
		User user = voToEntity(userVo);
		UserRequest userPassbolt = userAuthToUserPassbolt(user);
		String userId = UUID.randomUUID().toString();
		String passboltUserId = passboltService.saveUser(mainInfo, userPassbolt);
		user.setId(userId);
		user.setIdRole(3l);
		user.setIdPassbolt(passboltUserId);
		user.setActive(true);
		user.setPassboltComplete(false);
		userRepository.save(user);
		return userId;
	}

	@Override
	public void savePassboltData(PassboltClientMainInfo mainInfo, String userId)
			throws UserException, BadRequestException, RedirecException {
		User user = userRepository.findById(userId).get();
		if (user == null) {
			throw new BadRequestException("El usuario no existe!");
		}
		
		if (user.getPassboltComplete()) {
			throw new RedirecException("El usuario ya completo su registro");
		}
		
		io.beetgeek.passbolt.model.User userPassbolt = passboltService.getUser(mainInfo, user.getIdPassbolt());
		if (userPassbolt.getGpgkey() == null || userPassbolt.getRole() == null) {
			throw new BadRequestException("Tienes que completar el registro en la plataforma de Passbolt (https://passbolt.betgeek.io) para poder continuar");
		}

		PassboltUser newUserPassbolt = new PassboltUser();
		newUserPassbolt.setId(user.getIdPassbolt());
		newUserPassbolt.setPassboltRoleId(userPassbolt.getRole_id());
		newUserPassbolt.setPassboltRole(userPassbolt.getRole().getId());
		newUserPassbolt.setKeyId(userPassbolt.getGpgkey().getKey_id());
		newUserPassbolt.setFingerprint(userPassbolt.getGpgkey().getFingerprint());
		newUserPassbolt.setType(userPassbolt.getGpgkey().getType());
		newUserPassbolt.setUid(userPassbolt.getGpgkey().getUid());
		
		passboltUserRepository.save(newUserPassbolt);
		user.setPassboltComplete(true);
		userRepository.save(user);
	}

	private void checkInfoUser(UserVO user) throws BadRequestException {
		if (user.getUsername() == null || user.getUsername().isEmpty()
			|| user.getPassword() == null || user.getPassword().isEmpty()
			|| user.getFirstName() == null || user.getFirstName().isEmpty()
			|| user.getLastName() == null || user.getLastName().isEmpty()) {
			throw new BadRequestException("No se enviaron todos los campos");
		}
		User validUser = userRepository.findByUsername(user.getUsername());
		if (validUser != null) {
			throw new BadRequestException("El email [" + user.getUsername() + "] ya esta registrado");
		}
	}
	
	private User voToEntity(UserVO vo) {
		User user = new User();
		user.setUsername(vo.getUsername());
		user.setFirstName(vo.getFirstName());
		user.setLastName(vo.getLastName());
		user.setPassword(passwordEncoder.encode(vo.getPassword()));
		return user;
	}
	
	private UserRequest userAuthToUserPassbolt(User user) {
		UserRequest userPassbolt = new UserRequest();
		userPassbolt.setUsername(user.getUsername());
		userPassbolt.setFirstName(user.getFirstName());
		userPassbolt.setLastName(user.getLastName());
		return userPassbolt;
	}
}
