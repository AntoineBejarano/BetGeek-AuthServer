package io.betgeek.authserver.vo;

import lombok.Data;

@Data
public class UserVO {
	
	private String userId;
	private String username;
	private String firstName;
	private String lastName;
	private String password;
	private Long rolId;
	private Boolean active;
	private String registerKeyId;
	
	public UserVO() {
		
	}
	
	public UserVO(String username, String firstName, String lastName, Long rolId, String userId, Boolean active) {
		this.username = username;
		this.firstName = firstName;
		this.lastName = lastName;
		this.rolId = rolId;
		this.userId = userId;
		this.active = active;
	}
	
}
