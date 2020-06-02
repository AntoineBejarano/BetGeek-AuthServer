package io.betgeek.authserver.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Entity
@Table(name = "passbolt_users")
@Data
public class PassboltUser {

	@Id
	private String id;
	@Column(name = "passbolt_role_id")
	private String passboltRoleId;
	@Column(name = "passbolt_role")
	private String passboltRole;
	@Column(name = "key_id")
	private String keyId;
	private String fingerprint;
	private String type;
	private String uid;
	
	@JsonIgnore
	@Lob
	@Column(name = "private_key")
	private byte[] privateKey;

	@JsonIgnore
	@Lob
	@Column(name = "public_key")
	private byte[] publicKey;
}
