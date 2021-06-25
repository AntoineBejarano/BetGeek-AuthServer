package io.betgeek.authserver.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import javax.ws.rs.BadRequestException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;

import io.betgeek.authserver.entity.PassboltClientMainInfo;
import io.betgeek.authserver.exception.RedirecException;
import io.betgeek.authserver.exception.UserException;
import io.betgeek.authserver.service.UserService;
import io.betgeek.authserver.util.JWTUtils;
import io.betgeek.authserver.util.LoggerUtil;
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
	
	@Autowired
	private JWTUtils jwtUtils;

	@Value("${betgeek.passbolt.admin.fingerprint}")
	private String passboltAdminFingerprint;
	
	@Value("${betgeek.passbolt.admin.id}")
	private String passboltAdminId;
	
	@Value("${betgeek.passbolt.admin.secret}")
	private String passboltAdminSecret;

	@Autowired
	private LoggerUtil log;
	
	private String processName = "UserController";
	
	@GetMapping("/")
	public ResponseEntity<?> getUser(@RequestHeader(name = "Authorization", required = true) String token) {
		String traceId = UUID.randomUUID().toString();
		try {
			log.info(traceId, processName, "Init getUser");
			String userId = jwtUtils.getClaim(token, "userId");
			UserVO user = userService.getUser(userId);
			return new ResponseEntity<UserVO>(user, HttpStatus.OK);
		} catch (JWTDecodeException e){
			log.error(traceId, processName, e.getMessage(), e);
			return new ResponseEntity<HttpResponse>(new HttpResponse(HttpStatus.UNAUTHORIZED, e.getMessage()), HttpStatus.UNAUTHORIZED);
		} catch (JWTVerificationException e){
			log.error(traceId, processName, e.getMessage(), e);
			return new ResponseEntity<HttpResponse>(new HttpResponse(HttpStatus.UNAUTHORIZED, e.getMessage()), HttpStatus.UNAUTHORIZED);
		} finally {
			log.info(traceId, processName, "End getUser");
		}
	}
	
	@PutMapping("/")
	public ResponseEntity<?> updateUser(@RequestHeader(name = "Authorization", required = true) String token,
										@RequestBody(required = true) UserVO user) {
		String traceId = UUID.randomUUID().toString();
		try {
			log.info(traceId, processName, "Init updateUser");
			String userId = jwtUtils.getClaim(token, "userId");
			user.setUserId(userId);
			userService.updateUser(user);
			return new ResponseEntity<HttpResponse>(new HttpResponse(HttpStatus.OK, "Usuario actualizado correctamente"), HttpStatus.OK);
		} catch (BadRequestException e) {
			log.error(traceId, processName, e.getMessage(), e);
			return new ResponseEntity<HttpResponse>(new HttpResponse(HttpStatus.BAD_REQUEST, e.getMessage()), HttpStatus.BAD_REQUEST);
		} finally {
			log.info(traceId, processName, "End updateUser");
		}
	}
	
	@PutMapping("/{userId}")
	public ResponseEntity<?> savePassboltUser(@RequestParam(name = "file") MultipartFile file, 
											@PathVariable(name = "userId", required = true) String userId) {
		String traceId = UUID.randomUUID().toString();
		try {
			log.info(traceId, processName, "Init savePassboltUser");
			userService.savePassboltData(getMainInfoPassbolt(), userId, file);
			return new ResponseEntity<HttpResponse>(new HttpResponse(HttpStatus.OK, "Información de Passbolt registrada correctamente"), HttpStatus.OK);
		} catch (BadRequestException e) {
			log.error(traceId, processName, e.getMessage(), e);
			return new ResponseEntity<HttpResponse>(new HttpResponse(HttpStatus.BAD_REQUEST, e.getMessage()), HttpStatus.BAD_REQUEST);
		} catch (UserException e) {
			log.error(traceId, processName, e.getErrorMessage(), e);
			return new ResponseEntity<HttpResponse>(new HttpResponse(HttpStatus.BAD_REQUEST, e.getErrorMessage()), HttpStatus.BAD_REQUEST);
		} catch (RedirecException e) {
			log.error(traceId, processName, e.getMessage(), e);
			return new ResponseEntity<HttpResponse>(new HttpResponse(HttpStatus.ALREADY_REPORTED, e.getMessage()), HttpStatus.ALREADY_REPORTED);
		} catch (Exception e) {
			log.error(traceId, processName, e.getMessage(), e);
			return new ResponseEntity<HttpResponse>(new HttpResponse(HttpStatus.INTERNAL_SERVER_ERROR, null), HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			log.info(traceId, processName, "End savePassboltUser");
		}
	}
	
	@PostMapping("/")
	public ResponseEntity<?> saveUser(@RequestBody UserVO user) {
		String traceId = UUID.randomUUID().toString();
		try {
			log.info(traceId, processName, "Init saveUser");
			String userId = userService.saveUser(getMainInfoPassbolt(), user);
			return new ResponseEntity<UserResponse>(new UserResponse(userId), HttpStatus.OK);
		} catch (BadRequestException e) {
			log.error(traceId, processName, e.getMessage(), e);
			return new ResponseEntity<HttpResponse>(new HttpResponse(HttpStatus.BAD_REQUEST, e.getMessage()), HttpStatus.BAD_REQUEST);
		} catch (UserException e) {
			log.error(traceId, processName, e.getErrorMessage(), e);
			return new ResponseEntity<HttpResponse>(new HttpResponse(HttpStatus.BAD_REQUEST, e.getErrorMessage()), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			log.error(traceId, processName, e.getMessage(), e);
			return new ResponseEntity<HttpResponse>(new HttpResponse(HttpStatus.INTERNAL_SERVER_ERROR, null), HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			log.info(traceId, processName, "End saveUser");
		}
	}
	
	@GetMapping("/isActive/{userId}")
	public ResponseEntity<?> userIsActive(@PathVariable(name = "userId", required = true) String userId) {
		String traceId = UUID.randomUUID().toString();
		try {
			log.info(traceId, processName, "Init userIsActive");
			UserVO user = userService.getUser(userId);
			HttpStatus httpStatus = user.getActive() ? HttpStatus.OK : HttpStatus.UNAUTHORIZED;
			return new ResponseEntity<HttpResponse>(new HttpResponse(httpStatus, ""), httpStatus);
		} catch (BadRequestException e) {
			log.error(traceId, processName, e.getMessage(), e);
			return new ResponseEntity<HttpResponse>(new HttpResponse(HttpStatus.UNAUTHORIZED, ""), HttpStatus.UNAUTHORIZED);
		} finally {
			log.info(traceId, processName, "End userIsActive");
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
