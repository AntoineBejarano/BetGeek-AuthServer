package io.betgeek.authserver.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import io.beetgeek.passbolt.exceptions.BadRequestException;
import io.beetgeek.passbolt.model.json.UserRequest;
import io.betgeek.authserver.entity.PassboltClientMainInfo;
import io.betgeek.authserver.entity.PassboltUser;
import io.betgeek.authserver.entity.User;
import io.betgeek.authserver.exception.RedirecException;
import io.betgeek.authserver.exception.UserException;
import io.betgeek.authserver.mapper.UserMapper;
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
	private UserMapper userMapper;
	
	@Autowired
	private PassboltUserRespository passboltUserRepository;
	
	private static final String PRIVATE_KEY_BEGIN = "-----BEGIN PGP PRIVATE KEY BLOCK-----";
	private static final String PRIVATE_KEY_VERSION = "Version: OpenPGP.js v4.6.2";
	private static final String PRIVATE_KEY_COMMENT = "Comment: https://openpgpjs.org";
	private static final String PRIVATE_KEY_END = "-----END PGP PRIVATE KEY BLOCK-----";

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
	public void savePassboltData(PassboltClientMainInfo mainInfo, String userId, MultipartFile privateFile)
			throws UserException, BadRequestException, RedirecException {
		checkPrivateFile(privateFile);
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
		try {
			newUserPassbolt.setPrivateKey(privateFile.getBytes());
			newUserPassbolt.setPublicKey(userPassbolt.getGpgkey().getArmored_key().getBytes());
		} catch (IOException e) {
			throw new BadRequestException("El archivo privado es obligatorio!");
		}
		
		passboltUserRepository.save(newUserPassbolt);
		user.setPassboltComplete(true);
		userRepository.save(user);
	}

	@Override
	public UserVO getUser(String userId) throws BadRequestException {
		User user = userRepository.findById(userId).get();
		if (user == null) throw new BadRequestException("userId no encontrado");
		return userMapper.entityToVo(user);
	}

	@Override
	public void updateUser(UserVO user) throws BadRequestException {
		checkInfoUpdateUser(user);
		User entityUser = userRepository.findById(user.getUserId()).get();
		
		if (user.getPassword() != null && !user.getPassword().isEmpty()) {
			entityUser.setPassword(passwordEncoder.encode(user.getPassword()));
		}
		entityUser.setFirstName(user.getFirstName());
		entityUser.setLastName(user.getLastName());
		userRepository.save(entityUser);
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
	
	private void checkInfoUpdateUser(UserVO user) throws BadRequestException {
		if (user.getFirstName() == null || user.getFirstName().isEmpty()
			|| user.getLastName() == null || user.getLastName().isEmpty()) {
			throw new BadRequestException("No se enviaron todos los campos");
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
	
	private String inputStreamToString(InputStream inputStream) {
		String text = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))
		    .lines()
		    .collect(Collectors.joining("\n"));
		return text;
	}
	
	private void checkPrivateFile(MultipartFile privateFile) throws BadRequestException {
		try {
			String privateText = inputStreamToString(privateFile.getInputStream());
			String[] privateTextSplit = privateText.split("\n");
			int lastLineIndex = privateTextSplit.length - 1;
			
			if (!privateTextSplit[0].toString().equals(PRIVATE_KEY_BEGIN) ||
				// !privateTextSplit[1].toString().equals(PRIVATE_KEY_VERSION) ||
				!privateTextSplit[2].toString().equals(PRIVATE_KEY_COMMENT) ||
				!privateTextSplit[lastLineIndex].toString().equals(PRIVATE_KEY_END)) {
				throw new BadRequestException("El archivo privado no es valido");
			}
		} catch (IOException e) {
			throw new BadRequestException("El archivo privado no es valido");
		} catch (Exception e) {
			throw new BadRequestException("El archivo privado no es valido");
		}
	}
}
