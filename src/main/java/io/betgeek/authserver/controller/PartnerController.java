package io.betgeek.authserver.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;

import io.betgeek.authserver.dto.PartnerUserDTO;
import io.betgeek.authserver.enums.Roles;
import io.betgeek.authserver.service.PartnerUsersService;
import io.betgeek.authserver.util.JWTUtils;
import io.betgeek.authserver.vo.HttpResponse;

@RestController
@RequestMapping("/partner/")
public class PartnerController {
	
	@Autowired
	private PartnerUsersService partnerUsersService;
	
	@Autowired
	private JWTUtils jwtUtils;

	@GetMapping("/getUsers/{partnerId}")
	public ResponseEntity<?> getUser(@RequestHeader(name = "Authorization", required = true) String token, 
				@PathVariable(name = "partnerId", required = true) String partnerId) {
		try {
			String rolId = jwtUtils.getClaim(token, "rolId");
			if (!Roles.PARTNER.id().toString().contentEquals(rolId)) {
				return new ResponseEntity<HttpResponse>(new HttpResponse(HttpStatus.UNAUTHORIZED, "No tienes permisos para realizar esta acción"), HttpStatus.UNAUTHORIZED);
			}
			
			List<PartnerUserDTO> userList = partnerUsersService.getDTOByPartner(partnerId);
			
			return new ResponseEntity<Object>(userList, HttpStatus.OK);
		} catch (JWTDecodeException e){
			return new ResponseEntity<HttpResponse>(new HttpResponse(HttpStatus.UNAUTHORIZED, e.getMessage()), HttpStatus.UNAUTHORIZED);
		} catch (JWTVerificationException e){
			return new ResponseEntity<HttpResponse>(new HttpResponse(HttpStatus.UNAUTHORIZED, e.getMessage()), HttpStatus.UNAUTHORIZED);
		}
	}
	
}
