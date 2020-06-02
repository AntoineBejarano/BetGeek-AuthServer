package io.betgeek.authserver.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.JWTVerifier;

import io.betgeek.authserver.config.AuthServerProperties;

@Component
public class JWTUtils {

	@Autowired
	private AuthServerProperties authServerProperties;

	public void verifyToken(String token) throws JWTVerificationException {
		token = getCleanToken(token);
		Algorithm algorithm = Algorithm.HMAC256(authServerProperties.getSymmetricKeyJwt());
	    JWTVerifier verifier = JWT.require(algorithm)
	        .build();
	    verifier.verify(token);
	}
	
	public String getClaim(String token, String claim) throws JWTDecodeException, JWTVerificationException {
		token = getCleanToken(token);
		verifyToken(token);
	    return JWT.decode(token).getClaims().get(claim).asString();
	}
	
	private String getCleanToken(String token) {
		return token.replace("Bearer ", "");
	}
	
}
