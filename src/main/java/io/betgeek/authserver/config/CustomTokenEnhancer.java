package io.betgeek.authserver.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import io.betgeek.authserver.entity.CustomUserDetails;

public class CustomTokenEnhancer implements TokenEnhancer {

	@Override
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
		if (authentication.getPrincipal() instanceof CustomUserDetails) {
			CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
			Map<String, Object> additionalInfo = new HashMap<>();
			additionalInfo.put("userId", user.getId());
			additionalInfo.put("passbolt", user.getPassboltUser());
			((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
		}
		
		return accessToken;
	}

}