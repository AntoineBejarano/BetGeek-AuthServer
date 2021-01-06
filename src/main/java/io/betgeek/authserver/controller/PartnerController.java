package io.betgeek.authserver.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.exceptions.UnauthorizedClientException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;

import io.beetgeek.passbolt.exceptions.BadRequestException;
import io.betgeek.authserver.dto.PartnerUserDTO;
import io.betgeek.authserver.enums.Roles;
import io.betgeek.authserver.exception.UserException;
import io.betgeek.authserver.service.PartnerUsersService;
import io.betgeek.authserver.util.JWTUtils;
import io.betgeek.authserver.vo.DashBoardDataResponse;
import io.betgeek.authserver.vo.HttpResponse;
import io.betgeek.authserver.vo.UserVO;

@RestController
@RequestMapping("/partner/")
public class PartnerController {
	
	@Autowired
	private PartnerUsersService partnerUsersService;
	
	@Autowired
	private JWTUtils jwtUtils;

	@GetMapping("/getUsers/{partnerId}")
	public ResponseEntity<?> getUser(@RequestHeader(name = "Authorization", required = true) String token, 
				@PathVariable(name = "partnerId", required = true) String partnerId) throws BadRequestException {
		try {
			String rolId = jwtUtils.getClaim(token, "rolId");
			if (!Roles.PARTNER.id().toString().contentEquals(rolId) && !Roles.ADMIN.id().toString().contentEquals(rolId)) {
				return new ResponseEntity<>(new HttpResponse(HttpStatus.UNAUTHORIZED, "No tienes permisos para realizar esta acción"), HttpStatus.UNAUTHORIZED);
			}
			
			List<PartnerUserDTO> userList = partnerUsersService.getDTOByPartner(partnerId, rolId);
			
			return new ResponseEntity<>(userList, HttpStatus.OK);
		} catch (JWTVerificationException e){
			return new ResponseEntity<>(new HttpResponse(HttpStatus.UNAUTHORIZED, e.getMessage()), HttpStatus.UNAUTHORIZED);
		}
	}
	
	@GetMapping("/dashboard/data")
	public ResponseEntity<?> getDashboardData(@RequestHeader(name = "Authorization", required = true) String token) throws BadRequestException {
		try {
			String userId = jwtUtils.getClaim(token, "userId");
			String rolId = jwtUtils.getClaim(token, "rolId");
			DashBoardDataResponse data = partnerUsersService.getDashBoardData(userId, rolId);
			return new ResponseEntity<DashBoardDataResponse>(data, HttpStatus.OK);
		} catch (JWTDecodeException e){
			return new ResponseEntity<HttpResponse>(new HttpResponse(HttpStatus.UNAUTHORIZED, e.getMessage()), HttpStatus.UNAUTHORIZED);
		} catch (JWTVerificationException e){
			return new ResponseEntity<HttpResponse>(new HttpResponse(HttpStatus.UNAUTHORIZED, e.getMessage()), HttpStatus.UNAUTHORIZED);
		}
	}
	
	@GetMapping("/getById/{userId}/{partnerId}")
	public ResponseEntity<?> getUser(@RequestHeader(name = "Authorization", required = true) String token, 
				@PathVariable(name = "userId", required = true) String idUser,
				@PathVariable(name = "partnerId", required = true) String partnerId) throws BadRequestException {
		try {
			String rolId = jwtUtils.getClaim(token, "rolId");
			if (!Roles.PARTNER.id().toString().contentEquals(rolId)) {
				return new ResponseEntity<>(new HttpResponse(HttpStatus.UNAUTHORIZED, "No tienes permisos para realizar esta acción"), HttpStatus.UNAUTHORIZED);
			}
			
			PartnerUserDTO user = partnerUsersService.getDTOByPartnerAndUser(partnerId, idUser);
			
			return new ResponseEntity<>(user, HttpStatus.OK);
		} catch (JWTVerificationException e){
			return new ResponseEntity<>(new HttpResponse(HttpStatus.UNAUTHORIZED, e.getMessage()), HttpStatus.UNAUTHORIZED);
		}
	}
	
	@PostMapping("/add/{partnerId}")
	public ResponseEntity<?> add(@RequestHeader(name = "Authorization", required = true) String token, 
				@RequestBody UserVO user, @PathVariable(name = "partnerId", required = true) String partnerId) throws UserException, BadRequestException {
		try {
			String rolId = jwtUtils.getClaim(token, "rolId");
			if (!Roles.PARTNER.id().toString().contentEquals(rolId)) {
				return new ResponseEntity<>(new HttpResponse(HttpStatus.UNAUTHORIZED, "No tienes permisos para realizar esta acción"), HttpStatus.UNAUTHORIZED);
			}
			
			partnerUsersService.save(partnerId, user);
			
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (JWTVerificationException e){
			return new ResponseEntity<>(new HttpResponse(HttpStatus.UNAUTHORIZED, e.getMessage()), HttpStatus.UNAUTHORIZED);
		}
	}
	
	@PostMapping("/update/{partnerId}")
	public ResponseEntity<?> updateUser(@RequestHeader(name = "Authorization", required = true) String token, 
			@RequestBody UserVO user, @PathVariable(name = "partnerId", required = true) String partnerId) throws BadRequestException {
		try {
			String rolId = jwtUtils.getClaim(token, "rolId");
			if (!Roles.PARTNER.id().toString().contentEquals(rolId)) {
				return new ResponseEntity<>(new HttpResponse(HttpStatus.UNAUTHORIZED, "No tienes permisos para realizar esta acción"), HttpStatus.UNAUTHORIZED);
			}
			
			partnerUsersService.updateUser(partnerId, user);
			
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (JWTVerificationException e){
			return new ResponseEntity<>(new HttpResponse(HttpStatus.UNAUTHORIZED, e.getMessage()), HttpStatus.UNAUTHORIZED);
		}
	}
	
	@PostMapping(value = "/changeState/{idUser}")
	public ResponseEntity<HttpResponse> changeState(@RequestHeader("Authorization") String token,
			@PathVariable String idUser) throws BadRequestException {
		partnerUsersService.changeState(idUser);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@DeleteMapping("/deleteUser/{userId}/{partnerId}")
	public void deleteUser(@RequestHeader(name = "Authorization", required = true) String token, 
				@PathVariable(name = "userId", required = true) String idUser,
				@PathVariable(name = "partnerId", required = true) String partnerId) throws BadRequestException {
		try {
			String rolId = jwtUtils.getClaim(token, "rolId");
			if (!Roles.PARTNER.id().toString().contentEquals(rolId)) {
				throw new UnauthorizedClientException("No tienes permisos para realizar esta acción");
			}
			partnerUsersService.delete(partnerId, idUser);
		} catch (JWTVerificationException e){
			throw new UnauthorizedClientException("No tienes permisos para realizar esta acción", e);
		}
	}
	
}
