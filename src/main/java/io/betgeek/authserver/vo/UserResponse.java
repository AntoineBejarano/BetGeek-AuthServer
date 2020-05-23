package io.betgeek.authserver.vo;

import lombok.Data;

@Data
public class UserResponse {

	private String userId;
	
	public UserResponse(String userId) {
		this.userId = userId;
	}
	
}
