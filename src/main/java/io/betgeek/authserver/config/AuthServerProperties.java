package io.betgeek.authserver.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
@ConfigurationProperties("betgeek.config.oauth2")
public class AuthServerProperties {

	private String userClientOauth2;
	private String secretClientOauth2;
	private String symmetricKeyJwt;
	
}
