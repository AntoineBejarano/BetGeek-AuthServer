package io.betgeek.authserver.controller;

import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.BadRequestException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.betgeek.authserver.entity.PassboltClientMainInfo;
import io.betgeek.authserver.exception.RedirecException;
import io.betgeek.authserver.exception.UserException;
import io.betgeek.authserver.service.UserService;
import io.betgeek.authserver.util.PassboltUtils;
import io.betgeek.authserver.vo.HttpResponse;
import io.betgeek.authserver.vo.UserResponse;
import io.betgeek.authserver.vo.UserVO;

@RestController
@RequestMapping("/user/")
public class UserController {
	
	@Autowired
	private PassboltUtils passboltUtils;
	
	@Autowired
	private UserService userService;

	@Value("${betgeek.passbolt.admin.fingerprint}")
	private String passboltAdminFingerprint;
	
	@Value("${betgeek.passbolt.admin.id}")
	private String passboltAdminId;
	
	@Value("${betgeek.passbolt.admin.secret}")
	private String passboltAdminSecret;
	
	@GetMapping("/{userId}")
	public ResponseEntity<?> savePassboltUser(@PathVariable(name = "userId", required = true) String userId) {
		try {
			userService.savePassboltData(getMainInfoPassbolt(), userId);
			return new ResponseEntity<HttpResponse>(new HttpResponse(HttpStatus.OK, "Información de Passbolt registrada correctamente"), HttpStatus.OK);
		} catch (BadRequestException e) {
			return new ResponseEntity<HttpResponse>(new HttpResponse(HttpStatus.BAD_REQUEST, e.getMessage()), HttpStatus.BAD_REQUEST);
		} catch (UserException e) {
			return new ResponseEntity<HttpResponse>(new HttpResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (RedirecException e) {
			return new ResponseEntity<HttpResponse>(new HttpResponse(HttpStatus.ALREADY_REPORTED, e.getMessage()), HttpStatus.ALREADY_REPORTED);
		}
	}
	
	@PostMapping("/")
	public ResponseEntity<?> saveUser(@RequestBody UserVO user) {
		try {
			String userId = userService.saveUser(getMainInfoPassbolt(), user);
			return new ResponseEntity<UserResponse>(new UserResponse(userId), HttpStatus.OK);
		} catch (BadRequestException e) {
			return new ResponseEntity<HttpResponse>(new HttpResponse(HttpStatus.BAD_REQUEST, e.getMessage()), HttpStatus.BAD_REQUEST);
		} catch (UserException e) {
			return new ResponseEntity<HttpResponse>(new HttpResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	private PassboltClientMainInfo getMainInfoPassbolt() throws BadRequestException {
		InputStream privateKeyPath = null;
		InputStream publicKeyPath = null;
		try {
			privateKeyPath = passboltUtils.getPrivateKeyFileAdminInputStream();
			publicKeyPath = passboltUtils.getPublicKeyFileAdminInputStream();
		} catch (IOException e) {
			e.printStackTrace();
			throw new BadRequestException("No se encuentran los archivos llave!");
		}
		PassboltClientMainInfo mainInfo = new PassboltClientMainInfo();
		mainInfo.setKeyId(passboltAdminFingerprint);
		mainInfo.setUserIdPassbolt(passboltAdminId);
		mainInfo.setPrivateKey(privateKeyPath);
		mainInfo.setPublicKey(publicKeyPath);
		mainInfo.setPassword(passboltAdminSecret);
		return mainInfo;
	}
}
