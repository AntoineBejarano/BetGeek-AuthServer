package io.betgeek.authserver.entity;

import lombok.Data;

@Data
public class PassboltUserInfo {

	private String id;
	private String passboltRoleId;
	private String passboltRole;
	private String keyId;
	private String fingerprint;
	private String type;
	private String uid;

}
